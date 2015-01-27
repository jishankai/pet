<?php

class SocialController extends Controller
{
    public function filters()
    {
        return array(
        );
    }

    public function actionFoodShareApi($img_id, $alert_flag=0, $to='', $SID='')
    {
        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.aid, i.cmt, i.food, i.is_food, i.create_time, a.name, a.tx, a.type, a.gender, u.usr_id, u.tx AS u_tx, u.name AS u_name  FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE i.img_id=:img_id')->bindValue(':img_id', $img_id)->queryRow();
        
        $pet_type = Util::loadConfig('pet_type');
        $n = floor($r['type']/100);
        if (isset($pet_type[$n][$r['type']])) {
            $a_type = $pet_type[$n][$r['type']];
        } else {
            switch ($n) {
                case 1:
                    $a_type = '喵星人';
                    break;
                case 2:
                    $a_type = '汪星人';
                    break;
                
                default:
                    $a_type = '其他星人';
                    break;
            }
        }        

        if ($r['is_food']) {
            $this->renderPartial('food', array('r'=>$r, 'a_type'=>$a_type, 'img_id'=>$img_id, 'alert_flag'=>$alert_flag, 'aid'=>$r['aid'], 'to'=>$to, 'sid'=>$SID));
        } else {
            $this->renderPartial('image', array('r'=>$r, 'a_type'=>$a_type, 'img_id'=>$img_id, 'alert_flag'=>$alert_flag, 'aid'=>$r['aid'], 'to'=>$to, 'sid'=>$SID));
        }
    }

    public function actionNewYearEvent($SID='')
    {
        $users = Yii::app()->db->createCommand('SELECT COUNT(usr_id) FROM dc_user')->queryScalar();
       
        $this->renderPartial('activity_index', array('users'=>$users, 'sid'=>$SID));
    }

    public function actionActivityview($aid, $alert_flag=0, $SID='')
    {
        $users = Yii::app()->db->createCommand('SELECT COUNT(usr_id) FROM dc_user')->queryScalar();
        $food = Yii::app()->db->createCommand('SELECT food FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();
        
        $this->renderPartial('activity_view_'.$aid, array('aid'=>$aid, 'users'=>$users, 'alert_flag'=>$alert_flag, 'food'=>$food, 'sid'=>$SID));
    }

}

