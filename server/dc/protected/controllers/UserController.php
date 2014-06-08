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
                    'sql' => "SELECT MAX(update_time) FROM dc_user WHERE usr_id=$this->usr_id",
                ),
            ),
            array(
                'COutputCache + imagesApi',
                'duration' => 3600,
                'varyByParam' => array('img_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_image WHERE usr_id=$this->usr_id",
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


    public function actionImagesApi($img_id=NULL)
    {
        $c = new CDbCriteria;
        
        $c->compare('usr_id', $this->usr_id);
        $c->limit = 10;
        $c->order = 'img_id DESC';
        if(isset($img_id)) {
            $c->addCondition("img_id<:img_id");
            $c->params[':img_id'] = $img_id;
        }

        $images = Image::model()->findAll($c);

        $this->echoJsonData(array($images));
    }
}
