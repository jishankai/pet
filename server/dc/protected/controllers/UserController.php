<?php

class UserController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
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
        $id = $session['`id'];
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
}
