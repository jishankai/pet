<?php

class DeviceBehavior extends CActiveRecordBehavior
{
    public function register($aid, $name, $gender, $age, $type, $u_name, $u_gender, $u_city, $inviter)
    {
        
        $user = new User();
        
        $user->name = $u_name;
        $user->gender = $u_gender;
        $user->city = $u_city;
        $user->code = $this->createInviteCode();
        $user->inviter = $inviter;
        $user->gold = 500;
                
        $user->save();

        if (!isset($aid)) {
            $animal = new Animal();
            $animal->name = $name;
            $animal->gender = $gender;
            $animal->age = $age;
            $animal->type = $type;
            $animal->from = substr($type,0,1);
            $animal->master_id = $user->usr_id;
            $animal->save();
            $aid = $animal->aid;
        }
        $circle = new Circle();
        $circle->aid = $aid;
        $circle->usr_id = $user->usr_id;
        $circle->rank = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_circle WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar() + 1; 
        $circle->save();

        $user->aid = $aid;
        $user->saveAttributes(array('aid'));
        
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
