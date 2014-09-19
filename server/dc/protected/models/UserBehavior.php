<?php

class UserBehavior extends CActiveRecordBehavior
{
    public function isNameExist($name)
    {
        return Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE name=:name')->bindValue(':name', $name)->queryScalar();
    }

    public function getthisIdByCode($code)
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

    public function sendGift($is_shake)
    {
        $this->onGift = array($this, 'addExp');
        $this->onGift(new CEvent($this, array('on'=>'gift', 'is_shake'=>$is_shake))); 
    }
    
    public function comment()
    {
        $this->onComment = array($this, 'addExp');
        $this->onComment(new CEvent($this, array('on'=>'comment'))); 
    }

    public function touch()
    {
        $session = Yii::app()->session; 
        if ($session['touch_count']<=3) {
            $this->onTouch = array($this, 'addExp');   
            $this->onTouch = array($this, 'addGold');   
        } else if ($session['touch_count']<=10) {
            $this->onTouch = array($this, 'addExp');   
        }
        $this->onTouch(new CEvent($this, array('on'=>'touch'))); 
    }

    public function voiceUp()
    {
        $this->onVoiceUp = array($this, 'addExp');
        $this->onVoiceUp(new CEvent($this, array('on'=>'voice'))); 
    }

    public function contributionChange($circle)
    {
        $new_rank = $this->caclRank($circle);
        if ($new_rank>$circle->rank) {
            $circle->rank = $new_rank;
            $circle->saveAttributes(array('rank'));

            $this->onRankUp = array($this, 'addGold');
            $this->onRankUp(new CEvent($this, array('on'=>'rankUp', 'rank'=>$circle->rank)));
        }
    }

    public function share()
    {
        $this->onShare = array($this, 'addGold');
        $this->onShare(new CEvent($this, array('on'=>'share'))); 
    }

    public function onShare($event)
    {
        $this->raiseEvent('onShare', $event);
    }

    public function onRankUp($event)
    {
        $this->raiseEvent('onRankUp', $event);
    }

    public function onLevelUp($event)
    {
        $this->raiseEvent('onLevelUp', $event);
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

    public function onVoiceUp($event)
    {
        $this->raiseEvent('onVoiceUp', $event);
    }

    public function onComment($event)
    {
        $this->raiseEvent('onComment', $event);
    }

    public function onTouch($event)
    {
        $this->raiseEvent('onTouch', $event);
    }

    public function onLogin($event)
    {
        $this->raiseEvent('onLogin', $event);
    }

    public function caclRank($circle)
    {
        $ranks = Util::loadConfig('rank');
        for ($i = $circle->rank; $i < 9; $i++) {
             if ($ranks[$i]>$circle->t_contri) {
                 break;
             }
        }
        return $i;
    }
    
    public function caclLevel($exp)
    {
        $levels = Util::loadConfig('level');
        $i = $this->owner->lv;;
        $j = 50;
        /*
        while ($k=($i+$j)/2&&$j-$i>1) {
            if ($levels[$k]<$exp) {
                $i = $k;
            } else if ($levels[$k]>$exp) {
                $j = $k;
            } else {
                $i = $k;
                break;
            }
        }
         */

        return $i;
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
            case 'touch':
                $this->owner->exp+=TOUCH_X1;
                break;
            case 'voice':
                $this->owner->exp+=VOICE_X;
                break;
            default:
                break;
        }
        $new_lv = $this->caclLevel($this->owner->exp);
        if ($new_lv!=$this->owner->lv) {
            $this->owner->lv = $new_lv;
            $this->owner->saveAttributes(array('exp', 'lv'));

            $this->onLevelUp = array($this, 'addGold');
            $this->onLevelUp(new CEvent($this, array('on'=>'levelUp'))); 
        } else {
            $this->owner->saveAttributes(array('exp'));
        }
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
             case 'touch':
                 $this->owner->gold+=rand(1,6);
                 break;
             case 'share':
                 $this->owner->gold+=SHARE_X1;
                 break;
             case 'levelUp':
                 $this->owner->gold+=($this->owner->lv/5+1)*LEVELUP_A;
                 break;
             case 'rankUp':
                 $this->owner->gold+=$event->params['rank']*RANKUP_A;
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
