<?php

class DeviceBehavior extends CActiveRecordBehavior
{
    public function register($weibo, $wechat, $name, $gender, $age, $type, $inviter)
    {
        $user = new User();
        
        $user->weibo = $weibo;
        $user->wechat = $wechat;
        $user->name = $name;
        $user->gender = $gender;
        $user->age = $age;
        $user->type = $type;
        $user->code = $this->createInviteCode();
        $user->inviter = $inviter;
                
        $user->save();

        $user->initialize();
        $user->rewardInviter();
        //$this->onRegister = array($user, 'initialize');
        //$this->onRegister = array($user, 'rewardInviter');

        $this->owner->usr_id = $user->usr_id;
        $this->owner->saveAttributes(array('usr_id'));

        $this->onRegister(new CEvent());

        return $user;
    }

    public function createInviteCode()
    {
        do {
            $str='abcdefghijklmnopqrstuvwxyz0123456789';
            $str_temp=str_shuffle($str);
            $code = substr($str_temp, 0, USER_INVITECODE_LENGTH);
            $isExist = Yii::app()->db->createCommand("SELECT usr_id FROM dc_user WHERE code=:code")->bindValue(':code', $code)->queryScalar();
        } while ($isExist);

        return $code;
    }

    public function onRegister($event)
    {
        $this->raiseEvent('onRegister', $event);
    }
}
