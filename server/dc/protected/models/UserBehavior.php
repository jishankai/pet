<?php

class UserBehavior extends CActiveRecordBehavior
{
    public function isNameExist($name, $usr_id=0)
    {
        return Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE name=:name AND usr_id<>:usr_id')->bindValues(array(
            ':name'=>$name,
            ':usr_id'=>$usr_id,
        ))->queryScalar();
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
        if ($session[date("m.d.y").'_like']<=20) {
            $this->onLike = array($this, 'addGold');
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
            if ($today_count<=6) {
                $this->onUpload = array($this, 'addGold');
            }
            $this->onUpload = array($this, 'addExp');
            $this->onUpload(new CEvent($this, array('on'=>'upload'))); 
        }
    }

    public function login()
    {
        if (date("m.d.y",$this->owner->login_time)!=date("m.d.y")) {
            $this->owner->con_login>0 ? $this->owner->con_login++ : $this->owner->con_login=1;
            $this->owner->saveAttributes(array('con_login'));

            $this->thanksGivingReward();
            $this->onLogin = array($this, 'addExp');
            $this->onLogin = array($this, 'addGold');
        }

        $this->owner->login_time = time();
        $this->owner->saveAttributes(array('login_time'));
        #Yii::trace('exp:'.$value->exp, 'access');
        $this->onLogin(new CEvent($this, array('on'=>'login'))); 
    }

    private function thanksGivingReward()
    {
        switch (date("Ymd")) {
            case '20141123':
                $rewardGold = 199;
                break;
            
            case '20141124':
                $rewardGold = 299;
                break;
            
            case '20141125':
                $rewardGold = 399;
                break;
            
            case '20141126':
                $rewardGold = 499;
                break;
            
            case '20141127':
                $rewardGold = 599;
                break;
            
            default:
                $rewardGold = 0;
                break;
        }

        if ($rewardGold!=0) {
            $this->owner->gold+=$rewardGold;
            $this->owner->saveAttributes(array('gold'));

            Talk::model()->sendMsg(NPC_SYSTEM_USRID, $this->owner->usr_id, "宠物星球感谢祭~~这是今天的礼金".$rewardGold."金币，小主们请收好~拿了钱任性去吧！");
            $easemob = Yii::app()->easemob;
            $npc = User::model()->findByPk(NPC_SYSTEM_USRID);
            $easemob->sendToUsers($this->owner->usr_id, NPC_SYSTEM_USRID, array(
                'mixed'=>TRUE,
                'msg'=>"宠物星球感谢祭~~这是今天的礼金".$rewardGold."金币，小主们请收好~拿了钱任性去吧！",
                'ext'=>array(
                    'nickname'=>$npc->name,
                    'tx'=>$npc->tx,
                ),
            ));
        }
    }

    public function sendGift($is_shake)
    {
        $this->onGift = array($this, 'addExp');
        $this->onGift(new CEvent($this, array('on'=>'gift', 'is_shake'=>$is_shake))); 
    }
    
    public function comment($comment_count)
    {
        if ($comment_count<=3) {
            $this->onComment = array($this, 'addGold');
        }
        $this->onComment = array($this, 'addExp');
        $this->onComment(new CEvent($this, array('on'=>'comment'))); 
    }

    public function invite()
    {
        $this->onInvite = array($this, 'addGold');
        $this->onInvite(new CEvent($this, array('on'=>'invite'))); 
    }

    public function inviter($invited_name, $aid)
    {
        $a_name = Yii::app()->db->createCommand('SELECT name FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();
        Talk::model()->sendMsg(NPC_SYSTEM_USRID, $this->owner->usr_id, $invited_name.'成功填写您分享的邀请码，成为'.$a_name.'的粉丝！这是您的100金币，不客气~');
        $easemob = Yii::app()->easemob;
        $npc = User::model()->findByPk(NPC_SYSTEM_USRID);
        $easemob->sendToUsers($this->owner->usr_id, NPC_SYSTEM_USRID, array(
            'mixed'=>TRUE,
            'msg'=>$invited_name.'成功填写您分享的邀请码，成为'.$a_name.'的粉丝！这是您的100金币，不客气~',
            'ext'=>array(
                'nickname'=>$npc->name,
                'tx'=>$npc->tx,
            ),
        ));
        $this->onInviter = array($this, 'addGold');
        $this->onInviter(new CEvent($this, array('on'=>'inviter'))); 
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
        if ($circle->rank>0) {
            $new_rank = $this->caclRank($circle);
            if ($new_rank>$circle->rank) {
                $circle->rank = $new_rank;
                $circle->saveAttributes(array('rank'));

                $this->onRankUp = array($this, 'addGold');
                $this->onRankUp(new CEvent($this, array('on'=>'rankUp', 'rank'=>$circle->rank, 'aid'=>$circle->aid)));
            }
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

    public function onInvite($event)
    {
        $this->raiseEvent('onInvite', $event);
    }

    public function onInviter($event)
    {
        $this->raiseEvent('onInviter', $event);
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
        for ($i = $circle->rank; $i < 8; $i++) {
             if ($ranks[$i]>$circle->t_contri) {
                 break;
             }
        }
        return $i;
    }
    
    public function caclLevel($exp)
    {
        $levels = Util::loadConfig('level');
        for ($i = $this->owner->lv; $i < 50; $i++) {
             if ($levels[$i]>$exp) {
                 break;
             }
        }
        return $i;
    }

    public function addExp($event)
    {
        switch ($event->params['on']) {
            case 'login':
                /*
                if ($this->owner->con_login==1) {
                    $this->owner->exp+=LOGIN_X1;
                    $gold = LOGIN_X1;
                } else if ($this->owner->con_login<=6) {
                    $this->owner->exp+=$this->owner->con_login*LOGIN_X2;
                    $gold = $this->owner->con_login*LOGIN_X2;
                } else {
                    $this->owner->exp+=LOGIN_X3;
                    $gold = LOGIN_X3;
                }
                 */
                Talk::model()->sendMsg(NPC_SYSTEM_USRID, $this->owner->usr_id, "Hello ".$this->owner->name."，欢迎回到宠物星球～今天的福利5金币已经入账咯～");
                $easemob = Yii::app()->easemob;
                $npc = User::model()->findByPk(NPC_SYSTEM_USRID);
                $easemob->sendToUsers($this->owner->usr_id, NPC_SYSTEM_USRID, array(
                    'mixed'=>TRUE,
                    'msg'=>"Hello ".$this->owner->name."，欢迎回到宠物星球～今天的福利5金币已经入账咯～",
                    'ext'=>array(
                        'nickname'=>$npc->name,
                        'tx'=>$npc->tx,
                    ),
                ));
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
                /*
                if ($this->owner->con_login==1) {
                    $this->owner->gold+=LOGIN_X1;
                } else if ($this->owner->con_login<=6) {
                    $this->owner->gold+=$this->owner->con_login*LOGIN_X2;
                } else {
                    $this->owner->gold+=LOGIN_X3;
                }
                 */
                 $this->owner->gold+=5;
                 break;
             case 'touch':
                 //$this->owner->gold+=rand(5,10);
                 break;
             case 'share':
                 //$this->owner->gold+=SHARE_X1;
                 break;
             case 'like':
                 //$this->owner->gold+=1;    
                 break;
             case 'levelUp':
                 $gold = ($this->owner->lv/5+1)*LEVELUP_A;
                 $easemob = Yii::app()->easemob;
                 $npc = User::model()->findByPk(NPC_SYSTEM_USRID);
                 $easemob->sendToUsers($this->owner->usr_id, NPC_SYSTEM_USRID, array(
                    'mixed'=>TRUE,
                    'msg'=>"幸运的两脚兽啊，你找到了本喵埋下的彩蛋，送你$gold金币，别客气~还有其他彩蛋哦，加了个油~",
                    'ext'=>array(
                        'nickname'=>$npc->name,
                        'tx'=>$npc->tx,
                    ),
                 ));
                 $this->owner->gold+=($this->owner->lv/5+1)*LEVELUP_A;
                 break;
             case 'rankUp':
                 $easemob = Yii::app()->easemob;
                 $npc = User::model()->findByPk(NPC_SYSTEM_USRID);
                 $a_name = Yii::app()->db->createCommand('SELECT name FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $event->params['aid'])->queryScalar();
                 switch ($event->params['rank']) {
                     case 1:
                         $r_name = '凉粉';
                         break;
                     case 2:
                         $r_name = '淡粉';
                         break;
                     case 3:
                         $r_name = '狠粉';
                         break;
                     case 4:
                         $r_name = '灰常粉';
                         break;
                     case 5:
                         $r_name = '超级粉';
                         break;
                     case 6:
                         $r_name = '铁杆粉';
                         break; 
                     case 7:
                         $r_name = '脑残粉';
                         break; 
                     case 8:
                         $r_name = '骨灰粉';
                         break; 
                     default:
                         # code...
                         break;
                 }
                 $gold = $event->params['rank']*RANKUP_A;
                 $easemob->sendToUsers($this->owner->usr_id, NPC_SYSTEM_USRID, array(
                    'mixed'=>TRUE,
                    'msg'=>"矮油不错喔~你已经升格成为萌星$a_name的$r_name了，本喵赏你$gold金币，拿去high吧~",
                    'ext'=>array(
                        'nickname'=>$npc->name,
                        'tx'=>$npc->tx,
                    ),
                 ));
                 $this->owner->gold+=$event->params['rank']*RANKUP_A;
                 break;
             case 'invite':
                 $this->owner->gold+=100;//300;
                 break;
             case 'inviter':
                 $this->owner->gold+=100;//300;
                 break;
             case 'upload':
                 //$this->owner->gold+=PHOTO_GOLD;
                 break;
             case 'comment':
                 /*
                 $r = rand(1,10);
                 if ($r<=4) {
                     $g = 1;
                 } else if ($r<=7) {
                     $g = 2;
                 } else if ($r<=9) {
                     $g = 3;
                 } else {
                     $g = 4;
                 }
                 $this->owner->gold+=$g;
                  */
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
        return CHtml::image('http://pet4tx.oss-cn-beijing.aliyuncs.com/tx_usr/'.$this->owner->tx, $this->owner->tx, array('max-width'=>'50px','max-height'=>'50px'));
    }   

    public function getGender()
    {
        switch($this->owner->gender) {
        case 1:
            return 'Boy';
            break;
        case 2:
            return 'Girl';
            break;
        default:
            break;
        }
    }
}
