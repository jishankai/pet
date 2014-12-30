<?php

class SocialController extends Controller
{
    public function filters() 
    {
        return array(
        );
    }

    public function actionFoodShareApi($img_id, $to='wechat')
    {
        $this->layout = FALSE;
        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.aid, i.cmt, i.food, i.create_time, a.name, a.tx, a.type, a.gender, u.usr_id, u.tx AS u_tx, u.name AS u_name  FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE i.img_id=:img_id')->bindValue(':img_id', $img_id)->queryRow();

        $this->render('food', array('r'=>$r, 'to'=>$to));
    }

    public function actionOAuth2CallbackApi($code, $state)
    {
        if (isset($code)) {
            $rtn = Yii::app()->curl->get('https://api.weixin.qq.com/sns/oauth2/access_token', array(
                'appid'=>'',
                'secret'=>'',
                'code'=>$code,
                'grant_type'=>'authorization_code',
            )); 
            $t = json_decode($rtn);
            if (isset($t->errcode)) {
                $this->redirect($this->createUrl('social/foodShareApi',array('img_id'=>$state)));
            } else {
                if (isset($t->openid)) {
                    $usrinfo = Yii::app()->curl->get('https://api.weixin.qq.com/sns/userinfo',array(
                        'access_token'=>$t->access_token,
                        'openid'=>$t->openid,
                    ));
                    $u = json_decode($usrinfo);
                    if (isset($u->errcode)) {
                        $this->redirect($this->createUrl('social/foodShareApi',array('img_id'=>$state)));
                    } else {
                        $json = Yii::app()->curl->get($this->createUrl('user/login',array('uid'=>$u->unionid)));
                    }
                }                
            }

        }
    }

}

