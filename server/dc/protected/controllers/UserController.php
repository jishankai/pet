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
                'COutputCache + imagesApi',
                'duration' => 3600,
                'varyByParam' => array('img_id', 'usr_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_image WHERE usr_id=:usr_id",
                    'params' => array(
                        ':usr_id' => $this->usr_id,
                    ),
                ),
            ),
            /*
            array(
                'COutputCache + followingApi',
                'duration' => 3600,
                'varyByParam' => array('follow_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT COUNT(*), MAX(update_time) FROM dc_friend WHERE (usr_id=:usr_id AND relation IN (0,1)) OR (follow_id=:usr_id AND relation IN (0,-1))",
                    'params' => array(
                        ':usr_id' => $this->usr_id,
                    ),
                ),
            ),
            array(
                'COutputCache + followerApi',
                'duration' => 3600,
                'varyByParam' => array('usr_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT COUNT(*), MAX(update_time) FROM dc_friend WHERE (follow_id=:follow_id AND relation IN (0,1)) OR (usr_id=:follow_id AND relation IN(0,-1))",
                    'params' => array(
                        ':follow_id' => $this->usr_id,
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

    public function actionLoginApi($uid, $ver=NULL, $token=NULL)
    {
        $device = Device::model()->findByAttributes(array('uid'=>$uid));
        $isSuccess = true;
        if (empty($device->usr_id)) {
            if ($device === NULL) {
                $device = new Device();
                $device->uid = $uid;
                //$device->token = $token;
                $device->create_time = time();
                $device->save();
            }
            $session = Yii::app()->session;
            $session['id'] = $device->id;

            $isSuccess = false;
        } else {
            $session = Yii::app()->session;
            $session['usr_id'] = $device->usr_id;

            $user = User::model()->findByAttributes(array('usr_id'=>$device->usr_id));
            $user->login();
        }

        $this->echoJsonData(array(
            'isSuccess' => $isSuccess,
            'SID' => $session->sessionID,
        )); 
    }

    public function actionTypeApi()
    {
        $type = Util::loadConfig('pet_type');

        $this->echoJsonData($type);
    }

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

    public function actionRegisterApi($weibo=NULL, $wechat=NULL, $name, $gender, $age, $type, $code)
    {
        if (empty($name)) {
            throw new PException('注册信息不完整');
        }
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
        $namelen = (strlen($name)+mb_strlen($name,"UTF8"))/2;
        if ($namelen>12) {
            throw new PException('用户名超过最大长度');
        }
        if (User::model()->isNameExist(trim($name))) {
            throw new PException('用户名已被注册');
        }
        $pattern = '/^[a-zA-Z0-9\x{30A0}-\x{30FF}\x{3040}-\x{309F}\x{4E00}-\x{9FBF}]+$/u';
        if (!preg_match($pattern, $name)) {
            throw new PException('用户名含有特殊字符');
        }
        if ($code != '') {
            if (!$inviter = User::model()->getUserIdByCode(strtolower(trim($code)))) {
                throw new PException('邀请ID不存在');
            }
        }
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $user = $device->register($weibo, $wechat, trim($name), $gender, $age, $type, empty($invter)?NULL:$inviter);
            $transaction->commit();

            $session['usr_id'] = $device->usr_id;
            $user->login();
            $this->echoJsonData(array('isSuccess'=>true));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionInfoApi()
    {
        $user = User::model()->with('value')->findByPk($this->usr_id);
        $info = $user->attrWithRelated(array('value'));
        $info['imagesCount'] = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_image WHERE usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryScalar();
        $info['follow'] = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_friend WHERE (usr_id=:usr_id AND relation IN (0,1)) OR (follow_id=:usr_id AND relation IN (0,-1))')->bindValue(':usr_id', $this->usr_id)->queryScalar();
        $info['follower'] = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_friend WHERE (usr_id=:usr_id AND relation IN (0,-1)) OR (follow_id=:usr_id AND relation IN (0,1))')->bindValue(':usr_id', $this->usr_id)->queryScalar();

        $this->echoJsonData(array($info));
    }

    public function actionTxApi()
    {
        $user = User::model()->findByPk($this->usr_id);

        if (isset($_FILES['tx'])) {
            $fname = basename($_FILES['tx']['name']);
            $path = Yii::app()->basePath.'/../images/tx/'.$this->usr_id.'_'.$fname;
            if (move_uploaded_file($_FILES['tx']['tmp_name'], $path)) {
                $user->tx = $this->usr_id.'_'.$fname;
                $user->saveAttributes(array('tx'));
            }
        }

        $this->echoJsonData(array('tx'=>$user->tx));
    }


    public function actionImagesApi($usr_id=NULL, $img_id=NULL)
    {
        $c = new CDbCriteria;

        if (isset($usr_id)) {
            $c->compare('usr_id', $usr_id);
        } else {
            $c->compare('usr_id', $this->usr_id);
        }
        $c->limit = 10;
        $c->order = 't.img_id DESC';
        if(isset($img_id)) {
            $c->addCondition("t.img_id<:img_id");
            $c->params[':img_id'] = $img_id;
        }

        $images = Image::model()->findAll($c);

        $this->echoJsonData(array($images));
    }

    public function actionOtherApi($usr_id)
    {

        $dependency = new CDbCacheDependency("SELECT update_time FROM dc_user WHERE usr_id = :usr_id");
        $dependency->params[':usr_id'] = $usr_id;
        $u = User::model()->cache(3600, $dependency)->findByPk($usr_id);
        $user = $u->getAttributes();
        $user['imagesCount'] = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_image WHERE usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryScalar();
        $friend = Friend::model()->findByPk(array(
            'usr_id' => $this->usr_id,
            'follow_id' => $usr_id,
        ));
        if (!isset($friend)) {
            $friend = Friend::model()->findByPk(array(
                'usr_id' => $usr_id,
                'follow_id' => $this->usr_id,
            ));
            if (isset($friend)) {
                $friend->relation = $friend->relation*(-1);
            }
        }
        isset($friend->relation)&&$friend->relation*=1; //防止NULL变为0
        
        $this->echoJsonData(array(
            'user' => $user,
            'isFriend' => isset($friend->relation)&&($friend->relation==1 or 0==$friend->relation)?TRUE:FALSE,
        ));
    }

    public function actionFollowApi($usr_id)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $f = Friend::model()->findByPk(array(
                'usr_id' => $this->usr_id,
                'follow_id' => $usr_id,
            ));
            if (!isset($f)) {
                $f = Friend::model()->findByPk(array(
                    'usr_id' => $usr_id,
                    'follow_id' => $this->usr_id,
                ));
            }
            if (!isset($f)) {
                $f = new Friend;
                $f->usr_id = $this->usr_id;
                $f->follow_id = $usr_id;
                $f->create_time = time();
            }
            if ($this->usr_id==$f->usr_id) {
                $f->relation++;
            } else {
                $f->relation--;
            }
              
            $f->save();

            //events
            $user = User::model()->findByPk($this->usr_id);
            PMail::create($usr_id, $user, $user->name.'关注了你');

            $transaction->commit();

            $this->echoJsonData(array(
                'isSuccess' => true,
            ));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionUnFollowApi($usr_id)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $f = Friend::model()->findByPk(array(
                'usr_id' => $this->usr_id,
                'follow_id' => $usr_id,
            ));
            if (!isset($f)) {
                $f = Friend::model()->findByPk(array(
                    'usr_id' => $usr_id,
                    'follow_id' => $this->usr_id,
                ));
            }
            if ($this->usr_id==$f->usr_id) {
                $f->relation--;
            } else {
                $f->relation++;
            }
            if (0==$f->relation) {
                $f->delete();
            } else {
                $f->saveAttributes(array('relation'));
            }

            $transaction->commit();

            $this->echoJsonData(array(
                'isSuccess' => true,
            ));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionFollowingApi($usr_id=NULL)
    {
        if (isset($usr_id)) {
            $friends_1 = Yii::app()->db->createCommand('SELECT follow_id AS id, relation, update_time FROM dc_friend WHERE usr_id=:usr_id AND relation IN (0,1) AND update_time<(SELECT update_time FROM dc_friend WHERE follow_id=:follow_id AND usr_id=:usr_id) ORDER BY update_time DESC LIMIT 30')->bindValues(array(
                ':usr_id' => $this->usr_id,
                ':follow_id' => $usr_id,
            ))->queryAll();
            $friends_2 = Yii::app()->db->createCommand('SELECT usr_id AS id, relation*-1, update_time  AS relation FROM dc_friend WHERE follow_id=:usr_id AND relation IN (0,-1) AND update_time<(SELECT update_time FROM dc_friend WHERE usr_id=:follow_id AND follow_id=:usr_id) ORDER BY update_time DESC LIMIT 30')->bindValues(array(
                ':usr_id' => $this->usr_id,
                ':follow_id' => $usr_id,
            ))->queryAll();
        } else {
            $friends_1 = Yii::app()->db->createCommand('SELECT follow_id AS id, relation, update_time FROM dc_friend WHERE usr_id=:usr_id AND relation IN(0,1) ORDER BY update_time DESC LIMIT 30')->bindValue(':usr_id', $this->usr_id)->queryAll();
            $friends_2 = Yii::app()->db->createCommand('SELECT usr_id AS id, relation*-1 AS relation, update_time FROM dc_friend WHERE follow_id=:usr_id AND relation IN(0,-1) ORDER BY update_time DESC LIMIT 30')->bindValue(':usr_id', $this->usr_id)->queryAll();
        }
            //var_dump($friends_1);
            //var_dump($friends_2);
        $friends = array_merge($friends_1, $friends_2);
            //var_dump($friends);
        
        $follow_ids = array();
        $relations = array();
        $final_friend = NULL;
        foreach ($friends as $friend) {
            array_push($follow_ids, $friend['id']);
            $relations[$friend['id']] = $friend['relation'];
            if (!isset($final_friend)) {
                $final_friend = $friend;
            } else {
                if ($final_friend['update_time']>$friend['update_time']) {
                    $final_friend = $friend;
                }
            }
        }
        if (empty($follow_ids)) {
            $following = array();
        } else {
            $c = new CDbCriteria;
            $c->compare('usr_id', $follow_ids);
            $following = User::model()->findAll($c);
        }

        $result = array();
        $r = array();
        foreach ($following as $f) {
            $result[] = array(
                'user' => $f,
                'isFriend' => ($relations[$f->usr_id]==0 or 1==$relations[$f->usr_id]) ? TRUE:FALSE,
            );
        }
        $r['result'] = $result;
        if (isset($final_friend)) {
            $r['final_id'] = $final_friend['id'];
        }

        $this->echoJsonData(array($r));
    }

    public function actionFollowerApi($usr_id=NULL)
    {
        if (isset($usr_id)) {
            $friends_1 = Yii::app()->db->createCommand('SELECT follow_id AS id, relation, update_time FROM dc_friend WHERE usr_id=:usr_id AND relation IN (0,-1) AND update_time<(SELECT update_time FROM dc_friend WHERE follow_id=:follow_id AND usr_id=:usr_id) ORDER BY update_time DESC LIMIT 30')->bindValues(array(
                ':usr_id' => $this->usr_id,
                ':follow_id' => $usr_id,
            ))->queryAll();
            $friends_2 = Yii::app()->db->createCommand('SELECT usr_id AS id, relation, update_time FROM dc_friend WHERE follow_id=:usr_id AND relation IN (0,1) AND update_time<(SELECT update_time FROM dc_friend WHERE usr_id=:follow_id AND follow_id=:usr_id) ORDER BY update_time DESC LIMIT 30')->bindValues(array(
                ':usr_id' => $this->usr_id,
                ':follow_id' => $usr_id,
            ))->queryAll();
        } else {
            $friends_1 = Yii::app()->db->createCommand('SELECT follow_id AS id, relation, update_time FROM dc_friend WHERE usr_id=:usr_id AND relation IN(0,-1) ORDER BY update_time DESC LIMIT 30')->bindValue(':usr_id', $this->usr_id)->queryAll();
            $friends_2 = Yii::app()->db->createCommand('SELECT usr_id AS id, relation, update_time FROM dc_friend WHERE follow_id=:usr_id AND relation IN(0,1) ORDER BY update_time DESC LIMIT 30')->bindValue(':usr_id', $this->usr_id)->queryAll();
        }
        $friends = array_merge($friends_1, $friends_2);
        $follower_ids = array();
        $relations = array();
        $final_friend = NULL;
        foreach ($friends as $friend) {
            array_push($follower_ids, $friend['id']);
            $relations[$friend['id']] = $friend['relation'];
            if (!isset($final_friend)) {
                $final_friend = $friend;
            } else {
                if ($final_friend['update_time']>$friend['update_time']) {
                    $final_friend = $friend;
                }
            }
        }

        if (empty($follower_ids)) {
            $followers = array();
        } else {
            $c = new CDbCriteria;
            $c->compare('usr_id', $follower_ids);
            $followers = User::model()->findAll($c);
        }
        $result = array();
        foreach ($followers as $f) {
            $result[] = array(
                'user' => $f,
                'isFriend' => $relations[$f->usr_id]==0 ? TRUE:FALSE,
            );
        }
        $r['result'] = $result;
        if (isset($final_friend)) {
            $r['final_id'] = $final_friend['id'];
        }

        $this->echoJsonData(array($r));
    }

    public function actionOthersApi($usr_ids)
    {
        $c = new CDbCriteria;
        $c->compare('usr_id', explode(',',$usr_ids));
        $users = User::model()->findAll($c);

        $this->echoJsonData(array($users));
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
}
