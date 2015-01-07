<?php

Yii::import('ext.sinaWeibo.SinaWeibo',true);

class SocialController extends Controller
{
    public function filters()
    {
        return array(
        );
    }

    public function actionFoodShareApi($img_id, $to='weibo', $SID='')
    {
        switch ($to) {
            case 'wechat':
                $oauth2 = Yii::app()->wechat;
                $oauth2->get_code_by_authorize($img_id);
                break;
            case 'weibo':
                $oauth2 = new SinaWeibo(WB_AKEY, WB_SKEY);
                $this->redirect($oauth2->getAuthorizeURL($oauth2->url, 'code', $img_id, 'mobile'));
                break;
            default:
                # code...
                break;
        }
        $this->layout = FALSE;
        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.aid, i.cmt, i.food, i.create_time, a.name, a.tx, a.type, a.gender, u.usr_id, u.tx AS u_tx, u.name AS u_name  FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE i.img_id=:img_id')->bindValue(':img_id', $img_id)->queryRow();

        $this->render('food', array('r'=>$r, 'sid'=>$SID));
    }

    public function actionActivity()
    {
        $this->layout = FALSE;

        $this->render('activity_index', array('sid'=>$SID));
    }

    public function actionActivityview($id, $SID='')
    {
        $this->layout = FALSE;

        $this->render('activity_view_'.$id, array('sid'=>$SID));
    }

}

