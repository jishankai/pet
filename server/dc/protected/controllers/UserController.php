<?php

class UserController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId - welcomeApi,getSIDApi,planetApi,loginApi,typeApi,registerApi,othersApi,infoApi,petsApi,followingApi,topicApi,itemsApi,recommendApi,searchApi,reportApi,loginBy3PartyApi,bindUserApi',
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
        /*
        srand(floor(time()/(60*60*24)));
        $max_img_id = Yii::app()->db->createCommand('SELECT MAX(img_id) FROM dc_image')->queryScalar();
        $img_id = $max_img_id;
        //$img_id = rand(1, $max_img_id);
        $url = Yii::app()->db->createCommand("SELECT url FROM dc_image WHERE img_id=$img_id")->queryScalar();
         */
        $r = Yii::app()->db->createCommand('SELECT COUNT(aid) AS a, SUM(total_food) AS f FROM dc_animal')->queryRow();

        $this->echoJsonData(array('url'=>'home.jpg', 'animal'=>$r['a'], 'food'=>$r['f']));    
    }

    public function actionGetSIDApi($uid)
    {
        $r = Yii::app()->db->createCommand('SELECT usr_id, sid FROM dc_device WHERE uid=:uid')->bindValue(':uid', $uid)->queryRow();

        $this->echoJsonData($r);
    }

    public function actionLoginApi($uid, $ver=NULL, $token=NULL)
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
                    $device->usr_id = 0;
                    $device->sid = '';
                    //$device->token = $token;
                    $device->create_time = time();
                    $device->save();
                }
                $session['id'] = $device->id;
                $session['not_registered'] = TRUE;

                $isSuccess = false;
            } else {
                $session['id'] = $device->id;
                $session['usr_id'] = $device->usr_id;

                $user = User::model()->findByAttributes(array('usr_id'=>$device->usr_id));
                $user->login();
            }

            $device->sid = $session->sessionID;
            $device->saveAttributes(array('sid'));

            $transaction->commit();

            $this->echoJsonData(array(
                'isSuccess' => $isSuccess,
                'usr_id' => $device->usr_id,
                'SID' => $session->sessionID,
                'version' => LOGIN_VERSION,
            )); 
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionPlanetApi($planet)
    {
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

    public function actionBindApi($weibo='', $wechat='')
    {
        $isBinded = FALSE;
        if ((isset($weibo)&&$weibo!='') or (isset($wechat)&&$wechat!='')) {
            $user = User::model()->findByPk($this->usr_id);
            if (isset($user)) {
                $user->weibo = $weibo;
                $user->wechat = $wechat;
                $user->saveAttributes(array('weibo', 'wechat'));
                $isBinded = TRUE;
            }
        }
        $this->echoJsonData(array('isBinded'=>$isBinded));
    }

    public function actionLoginBy3PartyApi($weibo='', $wechat='')
    {
        $isBinded = FALSE;
        if ((isset($weibo)&&$weibo!='') or (isset($wechat)&&$wechat!='')) {
            $c = new CDbCriteria;
            if (isset($weibo)&&$weibo!='') {
                $c->compare('weibo',$weibo);
            }
            if (isset($wechat)&&$wechat!='') {
                $c->compare('wechat',$wechat); 
            }

            $user = User::model()->find($c);
            if (isset($user)) {
                $session = Yii::app()->session;
                $id = $session['id'];
                $device = Device::model()->findByPk($id);
                $device->usr_id = $user->usr_id;
                $device->saveAttributes(array('usr_id'));
                $session['usr_id'] = $user->usr_id;
                $isBinded = TRUE;

                $this->echoJsonData(array('usr_id'=>$user->usr_id, 'aid'=>$user->aid, 'isBinded'=>$isBinded));
            } else {
                $this->echoJsonData(array('isBinded'=>$isBinded));
            }
        }
    }

    public function actionBindUserApi($name, $pwd)
    {
        $isBinded = FALSE;
        $c = new CDbCriteria;
        $c->compare('name',$name);
        $c->compare('password',$pwd); 

        $user = User::model()->find($c);
        $transaction = Yii::app()->db->beginTransaction();
        try {
            if (isset($user)) {
                $session = Yii::app()->session;
                $id = $session['id'];
                $device = Device::model()->findByPk($id);
                $device->usr_id = $user->usr_id;
                $device->saveAttributes(array('usr_id'));
                $session['usr_id'] = $user->usr_id;
                $isBinded = TRUE;
            } 
            $transaction->commit();

        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }

        if (isset($user)) {
            $this->echoJsonData(array('usr_id'=>$user->usr_id, 'aid'=>$user->aid, 'isBinded'=>$isBinded));
        } else {
            $this->echoJsonData(array('isBinded'=>$isBinded));
        }
    }

    public function actionModifyInfoApi($u_name, $u_gender, $u_city)
    {
        $pattern = '/^[a-zA-Z0-9\x{30A0}-\x{30FF}\x{3040}-\x{309F}\x{4E00}-\x{9FBF}]+$/u';
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

    public function actionRegisterApi($aid=NULL, $name, $gender, $age, $type, $u_name, $u_gender, $u_city, $code, $weibo='', $wechat='')
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
            $this->response->setExpired();//重新登录
            $this->response->render();
            return ;
        }
        $device = Device::model()->findByPk($id);
        if (empty($device)) {
            $this->response->setExpired();//重新登录
            $this->response->render();
            return ;
        }
        $pattern = '/^[a-zA-Z0-9\x{30A0}-\x{30FF}\x{3040}-\x{309F}\x{4E00}-\x{9FBF}]+$/u';
        if (empty($aid)) {
            //$namelen = (strlen($name)+mb_strlen($name,"UTF8"))/2;
            $namelen = mb_strlen($name,"UTF8");
            if ($namelen>8) {
                throw new PException('宠物昵称超过最大长度');
            }
            if (!preg_match($pattern, trim($name))) {
                throw new PException('宠物昵称含有特殊字符');
            }
        }
        //$u_namelen = (strlen($u_name)+mb_strlen($u_name,"UTF8"))/2;
        $u_namelen = mb_strlen($u_name,"UTF8");
        if ($u_namelen>8) {
            //throw new PException('用户名超过最大长度');
            //$u_name = mb_substr($u_name, 0, 8);
        }
        
        if (User::model()->isNameExist(trim($u_name))) {
            throw new PException('用户名已被注册');
        }
        if (!preg_match($pattern, trim($u_name))) {
            throw new PException('用户名含有特殊字符');
        }
        if ($code != '') {
            $c = explode('@',$code);
            $invite_code = $c[0];
            if (!$inviter = User::model()->getUserIdByCode(strtolower(trim($invite_code)))) {
                throw new PException('邀请ID不存在');
            }
            $invite_aid = hexdec($c[1]); 
        }
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $user = $device->register($aid, trim($name), $gender, $age, $type, trim($u_name), $u_gender, $u_city,  empty($invter)?NULL:$inviter, $weibo, $wechat);

            $rtn_code = 0;
            $times = 0;
            while ($rtn_code!=200&&$times<10) { 
                $url = "https://a1.easemob.com/aidigame/imengstar/users";
                $data = array('username'=>$user->usr_id, 'password'=>$user->code);
                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
                curl_setopt($ch, CURLOPT_POST, 1);
                curl_setopt($ch, CURLOPT_POSTFIELDS, CJSON::encode($data));
                $rtn = curl_exec($ch);
                $rtn_code = curl_getinfo($ch,CURLINFO_HTTP_CODE); 
                curl_close($ch);

                $times++;
            }
            if ($times>=10) {
                throw new PException('失败');
            }

            $session['usr_id'] = $device->usr_id;
            $session['not_registered'] = FALSE;
            $user->login();

            Talk::model()->sendMsg(NPC_SYSTEM_USRID, $user->usr_id, "HI~我是事务官，欢迎来到宠物星球，以后星球上的新鲜事，本汪会光速传达你，放心交给我吧，汪汪");
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        $this->echoJsonData(array('usr_id'=>$user->usr_id, 'aid'=>$user->aid));
    }

    public function actionInfoApi($usr_id)
    {
        $r = Yii::app()->db->createCommand('SELECT u.usr_id, u.name, u.tx, u.gender, u.city, u.age, u.exp, u.weibo, u.wechat, u.password, u.lv, u.gold, u.con_login, u.inviter, u.code, c.rank, a.aid, a.name AS a_name, a.age AS a_age, a.tx AS a_tx FROM dc_user u LEFT JOIN dc_animal a ON u.aid=a.aid LEFT JOIN dc_circle c ON u.usr_id=c.usr_id AND u.aid=c.aid WHERE u.usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryRow();
        if ($r['con_login']<=5) {
            $r['next_gold'] = ($r['con_login']+1)*LOGIN_X2;
        } else {
            $r['next_gold'] = LOGIN_X3;
        }
        $session = Yii::app()->session;
        if (!isset($session['food'])) {
            $session['food'] = 5;
        }
        $r['food'] = $session['food'];

        $this->echoJsonData(array($r));
    }

    public function actionTxApi()
    {
        $user = User::model()->findByPk($this->usr_id);

        $tx = '';
        if (isset($_FILES['tx'])) {
            $fname = basename($_FILES['tx']['name']);
            $rtn = Yii::app()->oss->upload_file(OSS_PREFIX.'4tx', 'tx_usr/'.$this->usr_id.'_'.$fname, fopen($_FILES['tx']['tmp_name'],'r'), $_FILES['tx']['size']); 
            if ($rtn) {
                $tx = $this->usr_id.'_'.$fname;
                if (isset($user)) {
                    $user->tx = $tx;
                    $user->saveAttributes(array('tx'));
                } else {
                    Yii::app()->session->add('tx_usr', $tx);
                }
            } else {
                throw new PException('未上传成功');
            }
        }
        $this->echoJsonData(array('tx'=>$tx));
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

        if ($search_ids!='') {
            $r = Yii::app()->db->createCommand("SELECT usr_id, name, tx, city, gender FROM dc_user WHERE usr_id IN ($search_ids)")->queryAll();
        } else {
            $r = array();
        }

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
            $r = Yii::app()->db->createCommand('SELECT a.aid, a.tx, a.name, a.master_id, a.type, a.age, a.gender, a.food FROM dc_circle c LEFT JOIN dc_animal a ON c.aid=a.aid WHERE c.usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryAll();
        } else {
            $r = Yii::app()->db->createCommand('SELECT a.aid, a.tx, a.name, a.master_id, a.d_rq, a.food, c.t_contri, c.rank, (SELECT COUNT(*) FROM dc_news n WHERE c.aid=n.aid) AS news_count, (SELECT COUNT(*) FROM dc_circle c1 WHERE c.aid=c1.aid) AS fans_count FROM dc_circle c LEFT JOIN dc_animal a ON c.aid=a.aid WHERE c.usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryAll();
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

    public function actionReportApi($usr_id)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $user = User::model()->findByPk($usr_id);
            $user->reports++;
            $user->saveAttributes(array('reports'));

            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        $this->echoJsonData(array('isSuccess'=>TRUE));
    }

    public function actionRecommendApi($page=0, $usr_id=0)
    {
        $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.gender, a.master_id, u.name AS u_name, u.tx AS u_tx, a.t_rq, (SELECT COUNT(*) FROM dc_circle c WHERE c.aid=a.aid) AS fans FROM dc_animal a LEFT JOIN dc_user u ON a.master_id=u.usr_id ORDER BY a.d_rq DESC, u.update_time DESC LIMIT :m,30')->bindValues(array(
            ':m'=>30*$page,
        ))->queryAll();

        $max_users = Yii::app()->db->createCommand('SELECT COUNT(aid) FROM dc_animal')->queryScalar();
        foreach ($r as $k=>$v) {
            $in_circle = Yii::app()->db->createCommand('SELECT aid FROM dc_circle WHERE aid=:aid AND usr_id=:usr_id')->bindValues(array(
                ':aid' => $v['aid'],
                ':usr_id' => $usr_id,
            ))->queryScalar();
            $r[$k]['in_circle'] = $in_circle?1:0;
            $r[$k]['images'] = Yii::app()->db->createCommand('SELECT img_id, url FROM dc_image WHERE aid=:aid ORDER BY update_time DESC LIMIT 5')->bindValue(':aid', $v['aid'])->queryAll();
            $rank = Yii::app()->db->createCommand('SELECT COUNT(aid) FROM dc_animal WHERE t_rq<=:t_rq')->bindValue(':t_rq', $v['t_rq'])->queryScalar();
            $r[$k]['percent'] = floor($rank*100/$max_users);
        }

        $this->echoJsonData(array($r));
    }

    public function actionInputCodeApi($code)
    {
        $user = User::model()->findByPk($this->usr_id);
        if (isset($user->inviter)&&$user->inviter!=0) {
            throw new PException('您已经填过邀请码');
        }
        $c = explode('@',$code);
        $invite_code = $c[0];
        if (!$inviter = User::model()->getUserIdByCode(strtolower(trim($invite_code)))) {
            throw new PException('邀请ID不存在');
        }
        $invite_aid = hexdec($c[1]); 

        //邀请码处理
        if (isset($invite_aid)) {
            $aids = Yii::app()->db->createCommand('SELECT aid FROM dc_circle WHERE usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryColumn();
            $transaction = Yii::app()->db->beginTransaction();
            try {
                if (!in_array($invite_aid,$aids)) {
                    Yii::app()->db->createCommand('UPDATE dc_user SET inviter=:inviter WHERE usr_id=:usr_id')->bindValues(array(':inviter'=>$inviter,':usr_id'=>$this->usr_id))->execute();
                    $circle = new Circle();
                    $circle->aid = $invite_aid;
                    $circle->usr_id = $this->usr_id;
                    $circle->save();

                    $f = Follow::model()->findByPk(array(
                        'usr_id' => $this->usr_id,
                        'aid' => $invite_aid,
                    ));
                    if (!isset($f)) {
                        $f = new Follow;
                        $f->usr_id = $this->usr_id;
                        $f->aid = $invite_aid;
                        $f->create_time = time();
                        $f->save();
                    }
                }
                //奖励
                $user->invite();
                if (isset($inviter)) {
                    $inviter_obj = User::model()->findByPk($inviter);
                    $inviter_obj->inviter($user->name, $invite_aid);
                }
                $transaction->commit();
            } catch (Exception $e) {
                $transaction->rollback();
                throw $e;
            }

            $a_tx = Yii::app()->db->createCommand('SELECT tx FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $invite_aid)->queryScalar();
            $this->echoJsonData(array('aid'=>$invite_aid, 'tx'=>$a_tx, 'inviter'=>$inviter, 'u_name'=>$inviter_obj->name));
        } else {
            throw new PException('邀请码不正确');
        }
    }

    public function actionSearchApi($name, $page=0)
    {
        $r = Yii::app()->db->createCommand("SELECT usr_id, name, tx, gender, city FROM dc_user WHERE name LIKE '%$name%' ORDER BY usr_id ASC LIMIT :m, 30")->bindValue(':m', 30*$page)->queryAll();

        $this->echoJsonData(array($r));
    }

    public function actionUpgradeApi($version)
    {
        $c = Util::loadConfig($version);

        $this->echoJsonData(array(
            'android_url'=>'',
            'android_byte'=>0,
            'ios_url'=>'', 
            'upgrade_content'=>$c
        )); 
    }

    public function actionSetPwdApi($pwd)
    {
        $user = User::model()->findByPk($this->usr_id);

        $transaction = Yii::app()->db->beginTransaction();
        try {
            $user->password = $pwd;
            $user->saveAttributes(array('password')); 
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }

        $this->echoJsonData(array('isSuccess'=>TRUE));
    }

}
