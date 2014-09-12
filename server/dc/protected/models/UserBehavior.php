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

    public function uploadImage($aid)
    {
        $today_count = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_image WHERE aid=:aid AND create_time>=:time')->bindValues(array(
            ':aid' => $aid,
            ':time' => mktime(0,0,0,date('m'),date('d'),date('Y')),
        ))->queryScalar();
        #Yii::trace('today_count:'.mktime(0,0,0,date('m'),date('d'),date('Y')).'  '.$today_count, 'access');
        if ($today_count<=10) {
            $this->onUpload = array($this, 'addExp');
            $this->onUpload(new CEvent($this, array('on'=>'upload'))); 
        }
    }

    public function login()
    {
        if (date("m.d.y",$this->owner->login_time)!=date("m.d.y")) {
            $this->owner->con_login>0 ? $this->owner->con_login++ : $this->owner->con_login=1;
            $this->owner->saveAttributes(array('con_login'));

            $this->onLogin = array($this, 'addExp');
            $this->onLogin = array($this, 'addGold');
        }

        $this->owner->login_time = time();
        $this->owner->saveAttributes(array('login_time'));
        #Yii::trace('exp:'.$value->exp, 'access');
        $this->onLogin(new CEvent($this, array('on'=>'login'))); 
    }

    public function onShare($event)
    {
        $this->raiseEvent('onShare', $event);
    }

    public function onGift($event)
    {
        $this->raiseEvent('onGift', $event);
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
                if ($this->owner->con_login==1) {
                    $this->owner->exp+=LOGIN_X1;
                } else if ($this->owner->con_login<=6) {
                    $this->owner->exp+=$this->owner->con_login*LOGIN_X2;
                } else {
                    $this->owner->exp+=LOGIN_X3;
                }
                break;
            case 'gift':
                if ($event->params['is_shake']) {
                    $this->owner->exp+=GIFT_X1;
                } else {
                    $this->owner->exp+=GIFT_X2;
                }
                break;
            case 'like':
                $this->owner->exp+=1;    
                break;
            case 'upload':
                $this->owner->exp+=PHOTO_X1;    
                break;
            case 'comment':
                $this->owner->exp+=COMMENT_X1;
                break;
            case 'voice':
                $this->owner->exp+=VOICE_X;
                break;
            default:
                break;
        }

        $this->owner->saveAttributes(array('exp'));
    }

    public function addGold($event)
    {
        switch ($event->params['on']) {
            case 'login':
                if ($this->owner->con_login==1) {
                    $this->owner->gold+=LOGIN_X1;
                } else if ($this->owner->con_login<=6) {
                    $this->owner->gold+=$this->owner->con_login*LOGIN_X2;
                } else {
                    $this->owner->gold+=LOGIN_X3;
                }
                break;
             case 'share':
                 $this->owner->gold+=SHARE_X1;
                 break;
            default:
                // code...
                break;
        }
        $this->owner->saveAttributes(array('gold'));
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
