<?php

class UserController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId - welcomeApi,getSIDApi,loginApi,typeApi,bindApi,registerApi,othersApi,infoApi, petsApi, followingApi, topicApi, itemsApi',
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
                    'sql' => "SELECT update_time FROM dc_user WHERE usr_id=:usr_id",
                    'params' => array(
                        ':usr_id' => $this->usr_id,
                    ),
                ),
            ),
             */
            /*
            array(
                'COutputCache + othersApi',
                'duration' => 3600,
                'varyByParam' => array('usr_ids'),
            )
             */
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

    public function actionGetSIDApi($uid)
    {
        $r = Yii::app()->db->createCommand('SELECT usr_id, sid FROM dc_device WHERE uid=:uid')->bindValue(':uid', $uid)->queryScalar();

        $this->echoJsonData($r);
    }

    public function actionLoginApi($uid, $planet, $ver=NULL, $token=NULL)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
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
                $session['not_registered'] = TRUE;

                $isSuccess = false;
            } else {
                $session['usr_id'] = $device->usr_id;

                $user = User::model()->findByAttributes(array('usr_id'=>$device->usr_id));
                $user->login();
            }
            $session['planet'] = $planet;

            $device->sid = $session->sessionID;
            $device->saveAttributes(array('sid'));

            $transaction->commit();

            $this->echoJsonData(array(
                'isSuccess' => $isSuccess,
                'usr_id' => $device->usr_id,
                'SID' => $session->sessionID,
            )); 
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionPlanetApi($planet)
    {
        $session = Yii::app()->session;
        $session['planet'] = $planet;

        $this->echoJsonData(array('isSuccess'=>TRUE));
    }

    /*
    public function actionShareApi()
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $user = User::model()->findByPk($this->usr_id);
            if ($session['share_count']<=6) {
                $user->share();
            }
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        $this->echoJsonData(array('gold'=>$user->gold));
    }
     */
    
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
        $transaction = Yii::app()->db->beginTransaction();
        try {
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
            $transaction->commit();
            $this->echoJsonData(array('isBinded'=>$isBinded));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }

    }

    public function actionModifyInfoApi($aid=NULL, $name, $gender, $age, $type, $u_name, $u_gender, $u_city)
    {
        $pattern = '/^[a-zA-Z0-9\x{30A0}-\x{30FF}\x{3040}-\x{309F}\x{4E00}-\x{9FBF}]+$/u';
        if (isset($aid)) {
            //$namelen = (strlen($name)+mb_strlen($name,"UTF8"))/2;
            $namelen = mb_strlen($name,"UTF8");
            if ($namelen>8) {
                throw new PException('宠物昵称超过最大长度');
            }
            if (Animal::model()->isNameExist(trim($name), $aid)) {
                throw new PException('宠物名已被注册');
            }
            if (!preg_match($pattern, $name)) {
                throw new PException('宠物昵称含有特殊字符');
            }
        }
        //$u_namelen = (strlen($u_name)+mb_strlen($u_name,"UTF8"))/2;
        $u_namelen = mb_strlen($u_name,"UTF8");
        if ($u_namelen>8) {
            throw new PException('用户名超过最大长度');
        }
        if (User::model()->isNameExist(trim($u_name), $this->usr_id)) {
            throw new PException('用户名已被注册');
        }
        if (!preg_match($pattern, $u_name)) {
            throw new PException('用户名含有特殊字符');
        }
    
        $transaction = Yii::app()->db->beginTransaction();
        try {
            if (isset($aid)) {
                $animal = Animal::model()->findByPk($aid);
                $animal->name = $name;
                $animal->age = $age;
                $animal->type = $type;
                $animal->gender = $gender;
                $animal->saveAttributes(array('name', 'age', 'type', 'gender'));
            }
            $user = User::model()->findByPk($this->usr_id);
            $user->name = $u_name;
            $user->gender = $u_gender;
            $user->city = $u_city;
            $user->saveAttributes(array('name', 'gender', 'city'));
            $transaction->commit();

            $this->echoJsonData(array('isSuccess'=>true));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
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
        $pattern = '/^[a-zA-Z0-9\x{30A0}-\x{30FF}\x{3040}-\x{309F}\x{4E00}-\x{9FBF}]+$/u';
        if (empty($aid)) {
            //$namelen = (strlen($name)+mb_strlen($name,"UTF8"))/2;
            $namelen = mb_strlen($name,"UTF8");
            if ($namelen>8) {
                throw new PException('宠物昵称超过最大长度');
            }
            if (!preg_match($pattern, $name)) {
                throw new PException('宠物昵称含有特殊字符');
            }
        }
        //$u_namelen = (strlen($u_name)+mb_strlen($u_name,"UTF8"))/2;
        $u_namelen = mb_strlen($u_name,"UTF8");
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

            $session['usr_id'] = $device->usr_id;
            $session['not_registered'] = FALSE;
            $user->login();

            Talk::model()->sendMsg(NPC_SYSTEM_USRID, $user->usr_id, "HI~我是的汪汪大使，欢迎来到宠物星球，以后星球上的新鲜事，汪汪会光速传达你，放心交给我吧，汪汪");
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        $this->echoJsonData(array('isSuccess'=>true));
    }

    public function actionInfoApi($usr_id)
    {
        $r = Yii::app()->db->createCommand('SELECT u.usr_id, u.name, u.tx, u.gender, u.city, u.age, u.exp, u.lv, u.gold, u.con_login, c.rank, a.aid, a.name AS a_name, a.age AS a_age, a.tx AS a_tx FROM dc_user u LEFT JOIN dc_animal a ON u.aid=a.aid LEFT JOIN dc_circle c ON u.usr_id=c.usr_id AND u.aid=c.aid WHERE u.usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryRow();
        if ($r['con_login']<=5) {
            $r['next_gold'] = ($r['con_login']+1)*LOGIN_X2;
        } else {
            $r['next_gold'] = LOGIN_X3;
        }

        $this->echoJsonData(array($r));
    }

    public function actionTxApi()
    {
        $user = User::model()->findByPk($this->usr_id);

        if (isset($_FILES['tx'])) {
            $fname = basename($_FILES['tx']['name']);
            $path = Yii::app()->basePath.'/../images/tx/tx_usr/'.$this->usr_id.'_'.$fname;
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

        $r = Yii::app()->db->createCommand("SELECT usr_id, name, tx, city, gender FROM dc_user WHERE usr_id IN ($search_ids)")->queryAll();

        $this->echoJsonData($r);
    }

    public function actionNotifyApi()
    {
        $topic_n = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_topic WHERE status!=0 AND start_time<=:time AND end_time>:time')->bindValue(':time', time())->queryScalar();

        $this->echoJsonData(array(
            'topic_count' => $topic_n,
        ));
    }

    public function actionPetsApi($usr_id, $is_simple=0)
    {
        if ($is_simple) {
            $r = Yii::app()->db->createCommand('SELECT a.aid, a.tx, a.name, a.master_id, a.type, a.age, a.gender FROM dc_circle c LEFT JOIN dc_animal a ON c.aid=a.aid WHERE c.usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryAll();
        } else {
            $r = Yii::app()->db->createCommand('SELECT a.aid, a.tx, a.name, a.master_id, a.d_rq, c.t_contri, c.rank, (SELECT COUNT(*) FROM dc_news n WHERE c.aid=n.aid) AS news_count, (SELECT COUNT(*) FROM dc_circle c1 WHERE c.aid=c1.aid) AS fans_count FROM dc_circle c LEFT JOIN dc_animal a ON c.aid=a.aid WHERE c.usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryAll();
        }


        $this->echoJsonData($r);
    }

    public function actionFollowingApi($usr_id)
    {
        $r = Yii::app()->db->createCommand('SELECT a.aid, a.tx, a.name, a.type, a.age, a.gender, a.t_rq, u.name AS u_name FROM dc_follow f LEFT JOIN dc_animal a ON f.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE f.usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryAll();

        $this->echoJsonData($r);
    }

    public function actionTopicApi($usr_id)
    {
        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.topic_name, i.create_time FROM dc_circle c LEFT JOIN dc_image i ON c.aid=i.aid WHERE c.usr_id=:usr_id AND i.topic_name<>""')->bindValue(':usr_id', $usr_id)->queryAll();

        $this->echoJsonData($r);
    }

    public function actionItemsApi($usr_id)
    {
        $i = Yii::app()->db->createCommand('SELECT items FROM dc_user WHERE usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryScalar();
        $r = unserialize($i);

        $this->echoJsonData($r);
    }

    public function actionChgDefAniApi($aid)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            Yii::app()->db->createCommand('UPDATE dc_user SET aid=:aid WHERE usr_id=:usr_id')->bindValues(array(':aid'=>$aid, ':usr_id'=>$this->usr_id))->execute();
            $transaction->commit();

            $this->echoJsonData(array('isSuccess'=>TRUE));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }
}
