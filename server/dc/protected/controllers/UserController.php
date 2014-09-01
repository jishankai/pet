<?php

class UserController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId - welcomeApi,loginApi,typeApi,bindApi,registerApi,othersApi',
            /*
            array(
                'COutputCache + welcomeApi',
                'duration' => 86800,
                'dependency' => array(
                    'class' => 'CExpressionDependency',
                    'expression' => 'date("m.d.y")',
                ),
            ),
             */
            /*
            array(
                'COutputCache + infoApi',
                'duration' => 3600,
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_user WHERE usr_id=:usr_id",
                    'params' => array(
                        ':usr_id' => $this->usr_id,
                    ),
                ),
            ),
             */
            array(
                'COutputCache + othersApi',
                'duration' => 3600,
                'varyByParam' => array('usr_ids'),
            )
        );
    }

    public function actionWelcomeApi()
    {
        srand(floor(time()/(60*60*24)));
        $max_img_id = Yii::app()->db->createCommand('SELECT MAX(img_id) FROM dc_image')->queryScalar();
        $img_id = $max_img_id;
        //$img_id = rand(1, $max_img_id);
        $url = Yii::app()->db->createCommand("SELECT url FROM dc_image WHERE img_id=$img_id")->queryScalar();

        $this->echoJsonData(array('url'=>$url));    
    }

    public function actionLoginApi($uid, $planet, $ver=NULL, $token=NULL)
    {
        $device = Device::model()->findByAttributes(array('uid'=>$uid));
        $isSuccess = true;
        $session = Yii::app()->session;
        if (empty($device->usr_id)) {
            if ($device === NULL) {
                $device = new Device();
                $device->uid = $uid;
                //$device->token = $token;
                $device->create_time = time();
                $device->save();
            }
            $session['id'] = $device->id;

            $isSuccess = false;
        } else {
            $session['usr_id'] = $device->usr_id;

            $user = User::model()->findByAttributes(array('usr_id'=>$device->usr_id));
            $user->login();
        }
        $session['planet'] = $planet;

        $this->echoJsonData(array(
            'isSuccess' => $isSuccess,
            'SID' => $session->sessionID,
        )); 
    }

    /*
    public function actionTypeApi()
    {
        $type = Util::loadConfig('pet_type');

        $this->echoJsonData($type);
    }
     */

    public function actionBindApi($weibo=NULL, $wechat=NULL)
    {
        $isBinded = FALSE;
        if ((isset($weibo)&&$weibo!='') or (isset($wechat)&&$wechat!='')) {
            $c = new CDbCriteria;
            $c->compare('weibo',$weibo);
            $c->compare('wechat',$wechat); 

            $user = User::model()->find($c);
            if (isset($user)) {
                $session = Yii::app()->session;
                $session->open();
                $id = $session['id'];
                $device = Device::model()->findByPk($id);
                $device->usr_id = $user->usr_id;
                $device->saveAttributes(array('usr_id'));
                $session['usr_id'] = $user->usr_id;

                $isBinded = TRUE;
            }
        }
        $this->echoJsonData(array('isBinded'=>$isBinded));
            
    }

    public function actionRegisterApi($aid=NULL, $name, $gender, $age, $type, $u_name, $u_gender, $u_city, $code)
    {
        /*
        if (empty($name)) {
            throw new PException('注册信息不完整');
        }
         */
        $session = Yii::app()->session;
        $session->open();
        $id = $session['id'];
        if (empty($id)) {
            $this->response->setError(102, '重新登录');
            $this->response->render();
        }
        $device = Device::model()->findByPk($id);
        if (empty($device)) {
            throw new PException('未登录');
        }
        if (!empty($aid)) {
            $namelen = (strlen($name)+mb_strlen($name,"UTF8"))/2;
            if ($namelen>8) {
                throw new PException('宠物昵称超过最大长度');
            }
            $pattern = '/^[a-zA-Z0-9\x{30A0}-\x{30FF}\x{3040}-\x{309F}\x{4E00}-\x{9FBF}]+$/u';
            if (!preg_match($pattern, $name)) {
                throw new PException('宠物昵称含有特殊字符');
            }
        }
        $u_namelen = (strlen($u_name)+mb_strlen($u_name,"UTF8"))/2;
        if ($u_namelen>8) {
            throw new PException('用户名超过最大长度');
        }
        if (User::model()->isNameExist(trim($name))) {
            throw new PException('用户名已被注册');
        }
        if (!preg_match($pattern, $u_name)) {
            throw new PException('用户名含有特殊字符');
        }
        if ($code != '') {
            if (!$inviter = User::model()->getUserIdByCode(strtolower(trim($code)))) {
                throw new PException('邀请ID不存在');
            }
        }
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $user = $device->register($aid, trim($name), $gender, $age, $type, trim($u_name), $u_gender, $u_city,  empty($invter)?NULL:$inviter);
            $transaction->commit();

            $session['usr_id'] = $device->usr_id;
            $user->login();
            $this->echoJsonData(array('isSuccess'=>true));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionInfoApi($usr_id)
    {
        if (!isset($usr_id) or $usr_id='') {
           $usr_id = $this->usr_id; 
        }

        $r = Yii::app()->db->createCommand('SELECT u.usr_id, u.name, u.tx, u.gender, u.city, u.age, u.exp, u.lv, a.aid, a.name, a.tx FROM dc_user u LEFT JOIN dc_animal a ON u.aid=a.aid WHERE u.usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryAll();

        $this->echoJsonData(array($r));
    }

    public function actionTxApi()
    {
        $user = User::model()->findByPk($this->usr_id);

        if (isset($_FILES['tx'])) {
            $fname = basename($_FILES['tx']['name']);
            $path = Yii::app()->basePath.'/../images/tx_usr/'.$this->usr_id.'_'.$fname;
            if (move_uploaded_file($_FILES['tx']['tmp_name'], $path)) {
                $user->tx = $this->usr_id.'_'.$fname;
                $user->saveAttributes(array('tx'));
            }
        }

        $this->echoJsonData(array('tx'=>$user->tx));
    }

    public function actionOthersApi($usr_ids, $usr_id=NULL)
    {
        /*
        $c = new CDbCriteria;
        $c->compare('usr_id', explode(',',$usr_ids));
        $users = User::model()->findAll($c);

        $this->echoJsonData(array($users));
         */
        $tmp_ids = explode(',',$usr_ids);
        if (isset($usr_id)) {
            foreach ($tmp_ids as $k=>$tmp_id) {
                if ($tmp_id!=$usr_id) {
                    unset($tmp_ids[$k]);
                } else {
                    break;
                }
            }
        }
        for ($i = 0; $i < 30 && isset($tmp_ids[$i]); $i++) {
            $search_ids[] = $tmp_ids[$i];
        }
        $search_ids = implode(',', $search_ids);

        $r = Yii::app()->db->createCommand('SELECT usr_id, name, tx, city, gender FROM dc_user WHERE usr_id IN (:usr_ids)')->bindValue(':usr_ids', $search_ids)->queryAll();

        $this->echoJsonData($r);
    }

    public function actionNotifyApi()
    {
        $mail_n = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_mail WHERE is_read=0 AND usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryScalar();
        $topic_n = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_topic WHERE status!=0 AND start_time<=:time AND end_time>:time')->bindValue(':time', time())->queryScalar();

        $this->echoJsonData(array(
            'mail_count' => $mail_n,
            'topic_count' => $topic_n,
        ));
    }

    public function actionPetsApi($usr_id)
    {
        if (!isset($usr_id) or $usr_id='') {
           $usr_id = $this->usr_id; 
        }
        
        $r = Yii::app()->db->createCommand('SELECT a.aid, a.tx, a.name, a.d_rq, c.t_contri, COUNT(n.nid) AS news_count, COUNT(c1.usr_id) FROM dc_circle c LEFT JOIN dc_animal a ON c.aid=a.aid LEFT JOIN dc_circle c1 ON c.aid=c1.aid LEFT JOIN dc_news n ON a.aid=n.aid WHERE c.usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryAll();

        $this->echoJsonData($r);
    }

    public function actionFollowingApi($usr_id)
    {
        if (!isset($usr_id) or $usr_id='') {
           $usr_id = $this->usr_id; 
        }

        $r = Yii::app()->db->createCommand('SELECT a.aid, a.tx, a.name, a.age, a.gender, a.t_rq, u.name FROM dc_follow f LEFT JOIN dc_animal a ON f.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE f.usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryAll();

        $this->echoJsonData($r);
    }

    public function actionTopicApi($usr_id)
    {
        if (!isset($usr_id) or $usr_id='') {
           $usr_id = $this->usr_id; 
        }

        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.topic_name, i.create_time, t.name FROM dc_follow f LEFT JOIN dc_image i ON f.aid=i.aid WHERE f.usr_id=:usr_id AND i.topic_name<>""')->bindValue(':usr_id', $usr_id)->queryAll();

        $this->echoJsonData($r);
    }

    public function actionItemsApi($usr_id)
    {
        if (!isset($usr_id) or $usr_id='') {
           $usr_id = $this->usr_id; 
        }

        $i = Yii::app()->db->createCommand('SELECT items FROM dc_user WHERE usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryScalar();
        $r = unserialize($i);

        $this->echoJsonData($r);
    }
}
