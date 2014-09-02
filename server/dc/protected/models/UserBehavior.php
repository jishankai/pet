<?php

class UserBehavior extends CActiveRecordBehavior
{
    public function isNameExist($name)
    {
        return Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE name=:name')->bindValue(':name', $name)->queryScalar();
    }

    public function getUserIdByCode($code)
    { 
        return Yii::app()->db->createCommand("SELECT usr_id FROM dc_user WHERE code=:code")->bindValue(':code',$code)->queryScalar();
    }  

    public function initialize()
    {
    }

    public function rewardInviter()
    {
        
    }

    public function like()
    {
        $session = Yii::app()->session;
        isset($session[date("m.d.y").'_like']) ? ($session[date("m.d.y").'_like']+=1) : ($session[date("m.d.y").'_like']=1); 
        Yii::trace($session[date("m.d.y").'_like'], 'access');
        if ($session[date("m.d.y").'_like']<=20) {
           # $this->onLike = array($this, 'addExp');
        }

        $this->onLike(new CEvent($this, array('on'=>'like'))); 
    }

    public function uploadImage()
    {
        $today_count = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_image WHERE usr_id=:usr_id AND create_time>=:time')->bindValues(array(
            ':usr_id' => $this->owner->usr_id,
            ':time' => mktime(0,0,0,date('m'),date('d'),date('Y')),
        ))->queryScalar();
        #Yii::trace('today_count:'.mktime(0,0,0,date('m'),date('d'),date('Y')).'  '.$today_count, 'access');
        if ($today_count<=5) {
            $this->onUpload = array($this, 'addExp');
        }

        $this->onUpload(new CEvent($this, array('on'=>'upload'))); 
    }

    public function login()
    {
        if (date("m.d.y",$this->owner->login_time)!=date("m.d.y")) {
            $this->owner->con_login>0 ? $this->owner->con_login++ : $this->owner->con_login=1;
            $this->owner->saveAttributes(array('con_login'));

            $this->onLogin = array($this, 'addExp');
        }

        $this->owner->login_time = time();
        $this->owner->saveAttributes(array('login_time'));
        #Yii::trace('exp:'.$value->exp, 'access');
        $this->onLogin(new CEvent($this, array('on'=>'login'))); 
    }

    public function onLike($event)
    {
        $this->raiseEvent('onLike', $event);
    }

    public function onUpload($event)
    {
        $this->raiseEvent('onUpload', $event);
    }

    public function onLogin($event)
    {
        $this->raiseEvent('onLogin', $event);
    }

    public function addExp($event)
    {
        switch ($event->params['on']) {
            case 'login':
                $this->owner->exp+=($this->owner->con_login>5?5:$this->owner->con_login);
                break;
            case 'like':
                $this->owner->exp+=1;    
                break;
            case 'upload':
                $this->owner->exp+=2;    
                break;
            default:
                break;
        }

        $this->owner->saveAttributes(array('exp'));
    }

    public function attrWithRelated(array $with)
    {
        $attr = $this->owner->getAttributes();
        foreach ($with as $val) {
            $attr = array_merge($attr, $this->owner->$val->getAttributes());
        }

        return $attr;
    }

    public function getTypeName()
    {
        $pet_type = Util::loadConfig('pet_type');

        $n = $this->owner->type/100;
        
        return $pet_type[$n][$this->owner->type];        
    }
    
    public function showTxImage(){
        return CHtml::image(Yii::app()->request->hostInfo.Yii::app()->baseUrl.'/images/tx/'.$this->owner->tx, $this->owner->tx, array('width'=>'50px','max-height'=>'50px'));
    }   

    public function getGender()
    {
        switch($this->owner->gender) {
        case 1:
            return '公';
            break;
        case 2:
            return '母';
            break;
        default:
            break;
        }
    }
}
