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
        $reward_items = array(
            1101 => 3,
            1102 => 3,
            1103 => 3,
            1104 => 3,
            1105 => 3,
            1106 => 3,
            1107 => 3,
            1201 => 2,
            1202 => 2,
            1203 => 2,
            1204 => 2,
            1205 => 2,
            1206 => 2,
            1207 => 2,
            1301 => 1,
            1302 => 1,
            1303 => 1,
            1304 => 1,
            1305 => 1,
            2101 => 3,
            2102 => 3,
            2103 => 3,
            2104 => 3,
        );
        $user->items = serialize($reward_items);
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
            $circle = new Circle();
            $circle->aid = $aid;
            $circle->usr_id = $user->usr_id;
            $circle->rank = 0; 
            $circle->save();
        } else {
            $circle = new Circle();
            $circle->aid = $aid;
            $circle->usr_id = $user->usr_id;
            $circle->save();
        }
        $f = new Follow;
        $f->usr_id = $user->usr_id;
        $f->aid = $aid;
        $f->create_time = time();
        $f->save();

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
