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
        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.aid, i.cmt, i.food, i.create_time, a.name, a.tx, a.type, a.gender, u.usr_id, u.tx AS u_tx, u.name AS u_name  FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE i.img_id=:img_id')->bindValue(':img_id', $img_id)->queryRow();

        $this->renderPartial('food', array('r'=>$r, 'img_id'=>$img_id, 'alert_flag'=>$alert_flag, 'aid'=>$r['aid'], 'to'=>$to, 'sid'=>$SID));
    }

    public function actionNewYearEvent($SID='')
    {
        $users = Yii::app()->db->createCommand('SELECT COUNT(usr_id) FROM dc_user')->queryScalar();
       
        $this->renderPartial('activity_index', array('users'=>$users, 'sid'=>$SID));
    }

    public function actionActivityview($aid, $alert_flag=0, $SID='')
    {
        $food = Yii::app()->db->createCommand('SELECT food FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();
        
        $this->renderPartial('activity_view_'.$aid, array('aid'=>$aid, 'alert_flag'=>$alert_flag, 'food'=>$food, 'sid'=>$SID));
    }

}

