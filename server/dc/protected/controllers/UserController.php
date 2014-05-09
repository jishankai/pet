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

    public function actionLoginApi($uid, $ver, $token=NULL)
    {
        $user = User::model()->findByAttributes(array('uid'=>$uid));
        if (empty($user)) {
            $user = new User();
            $user->token = $token;
            $user->create_time = time();
            $user->save();
        }
        $session = Yii::app()->session;
        $session['uid'] = $user->uid;
        $this->echoJsonData(array(
            'isSuccess'=>true,
            'SID'=>$session->sessionID,
        )); 
    }

    public function actionRegisterApi($name, $gender, $age, $class, $code)
    {
        if (empty($name)) {
            throw new PException('注册信息不完整');
        }
        $session = Yii::app()->session;
        $session->open();
        $uid = $session['uid'];
        if (empty($uid)) {
            $this->response->setError(102, '重新登录');
            $this->response->render();
        }
        $user = User::model()->findByPk($uid);
        if (empty($user)) {
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
            $user->register(trim($name), empty($invter)?NULL:$inviter);
            $this->echoJsonData(array('isSuccess'=>true));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionInfoApi()
    {
        $user = User::model()->findByPk($this->uid);

        $this->echoJsonData(array($user));
    }
}
