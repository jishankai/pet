<?php

class UserController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId + infoApi,txApi',
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
            array(
                'COutputCache + imagesApi',
                'duration' => 3600,
                'varyByParam' => array('img_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_image WHERE usr_id=:usr_id",
                    'params' => array(
                        ':usr_id' => $this->usr_id,
                    ),
                ),
            ),
            array(
                'COutputCache + otherApi',
                'duration' => 3600,
                'varyByParam' => array('usr_id'),
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_user WHERE usr_id=:usr_id",
                    'params' => array(
                        ':usr_id' => $this->usr_id,
                    ),
                ),
            ),
            array(
                'COutputCache + followingApi',
                'duration' => 3600,
                'varyByParam' => array('follow_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_friend WHERE usr_id=:usr_id",
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
                    'sql' => "SELECT MAX(update_time) FROM dc_friend WHERE follow_id=:follow_id",
                    'params' => array(
                        ':follow_id' => $this->usr_id,
                    ),
                ),
            ),
        );
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
            $user = User::model()->findByAttributes(array('usr_id'=>$device->usr_id));
            $user->login();

            $session = Yii::app()->session;
            $session['usr_id'] = $device->usr_id;
        }

        $this->echoJsonData(array(
            'isSuccess' => $isSuccess,
            'SID' => $session->sessionID,
        )); 
    }

    public function actionRegisterApi($name, $gender, $age, $type, $code)
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
            $user = $device->register(trim($name), $gender, $age, $type, empty($invter)?NULL:$inviter);
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
        $user = User::model()->findByPk($this->usr_id);

        $this->echoJsonData(array($user));
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
        $c->order = 'img_id DESC';
        if(isset($img_id)) {
            $c->addCondition("img_id<:img_id");
            $c->params[':img_id'] = $img_id;
        }

        $images = Image::model()->findAll($c);

        $this->echoJsonData(array($images));
    }

    public function actionOtherApi($usr_id)
    {
        $user = User::model()->findByPk($usr_id);

        $this->echoJsonData(array($user));
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

            $v_self = Value::model()->findByPk($this->usr_id);
            $v_self->follow++;
            $v_self->saveAttributes(array('follow'));
            $v_other = Value::model()->findByPk($usr_id);
            $v_other->follower++;
            $v_other->saveAttributes(array('follower'));

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
            

            $v_self = Value::model()->findByPk($this->usr_id);
            $v_self->follow--;
            $v_self->saveAttributes(array('follow'));
            $v_other = Value::model()->findByPk($usr_id);
            $v_other->follower--;
            $v_other->saveAttributes(array('follower'));

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
            $friends_1 = Yii::app()->db->createCommand('SELECT follow_id AS id, relation FROM dc_friend WHERE usr_id=:usr_id AND relation IN (0,1) AND create_time<(SELECT create_time FROM dc_friend WHERE follow_id=:follow_id AND usr_id=:usr_id) LIMIT 30 ORDER BY create_time DESC')->bindValues(array(
                ':usr_id' => $this->usr_id,
                ':follow_id' => $usr_id,
            ))->queryColumn();
            $friends_2 = Yii::app()->db->createCommand('SELECT usr_id AS id, relation FROM dc_friend WHERE follow_id=:usr_id AND relation IN (0,-1) AND create_time<(SELECT create_time FROM dc_friend WHERE usr_id=:follow_id AND follow_id=:usr_id) LIMIT 30 ORDER BY create_time DESC')->bindValues(array(
                ':usr_id' => $this->usr_id,
                ':follow_id' => $usr_id,
            ))->queryColumn();
        } else {
            $friends_1 = Yii::app()->db->createCommand('SELECT follow_id AS id, relation FROM dc_friend WHERE usr_id=:usr_id AND realtion IN(0,1) LIMIT 30 ORDER BY create_time DESC')->bindValue(':usr_id', $this->usr_id)->queryColumn();
            $friends_2 = Yii::app()->db->createCommand('SELECT follow_id AS id, relation FROM dc_friend WHERE follow_id=:usr_id AND realtion IN(0,-1) LIMIT 30 ORDER BY create_time DESC')->bindValue(':usr_id', $this->usr_id)->queryColumn();
        }
        $friends = array_merge($friends_1, $friends_2);

        $follow_ids = array();
        $realtions = array();
        foreach ($friends as $friend) {
            array_push($follow_ids, $friend['id']);
            $relations[$friend['id']] = $friend['relation'];
        }

        $c = new CDbCriteria;
        $c->compare('usr_id', $follow_ids);
        $following = User::model()->findAll($c);

        foreach ($following as $f) {
            $f->realtion = $relations[$f->usr_id];
        }

        $this->echoJsonData(array($following));
    }

    public function actionFollowerApi($usr_id=NULL)
    {
        if (isset($usr_id)) {
            $friends_1 = Yii::app()->db->createCommand('SELECT follow_id AS id, relation FROM dc_friend WHERE usr_id=:usr_id AND relation IN (0,-1) AND create_time<(SELECT create_time FROM dc_friend WHERE follow_id=:follow_id AND usr_id=:usr_id) LIMIT 30 ORDER BY create_time DESC')->bindValues(array(
                ':usr_id' => $this->usr_id,
                ':follow_id' => $usr_id,
            ))->queryColumn();
            $friends_2 = Yii::app()->db->createCommand('SELECT usr_id AS id, relation FROM dc_friend WHERE follow_id=:usr_id AND relation IN (0,1) AND create_time<(SELECT create_time FROM dc_friend WHERE usr_id=:follow_id AND follow_id=:usr_id) LIMIT 30 ORDER BY create_time DESC')->bindValues(array(
                ':usr_id' => $this->usr_id,
                ':follow_id' => $usr_id,
            ))->queryColumn();
        } else {
            $friends_1 = Yii::app()->db->createCommand('SELECT follow_id AS id, relation FROM dc_friend WHERE usr_id=:usr_id AND realtion IN(0,-1) LIMIT 30 ORDER BY create_time DESC')->bindValue(':usr_id', $this->usr_id)->queryColumn();
            $friends_2 = Yii::app()->db->createCommand('SELECT follow_id AS id, relation FROM dc_friend WHERE follow_id=:usr_id AND realtion IN(0,1) LIMIT 30 ORDER BY create_time DESC')->bindValue(':usr_id', $this->usr_id)->queryColumn();
        }
        $friends = array_merge($friends_1, $friends_2);

        $follower_ids = array();
        $realtions = array();
        foreach ($friends as $friend) {
            array_push($follower_ids, $friend['id']);
            $relations[$friend['id']] = $friend['relation'];
        }

        $c = new CDbCriteria;
        $c->compare('usr_id', $follower_ids);
        $followers = User::model()->findAll($c);

        foreach ($followers as $f) {
            $f->realtion = $relations[$f->usr_id];
        }

        $this->echoJsonData(array($followers));
    }
}
