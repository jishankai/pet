<?php

Yii::import('ext.sinaWeibo.SinaWeibo',true);

class WeiboController extends Controller
{
	public function actionCallback(){
		$oauth2 = new SinaWeibo(WB_AKEY, WB_SKEY);
		if (isset($_REQUEST['code'])) {
			$keys = array();
			$keys['code'] = $_REQUEST['code'];
			$keys['redirect_uri'] = $this->createAbsoluteUrl('social/foodShareApi', array('img_id'=>$_REQUEST['state']));
			try {
				$token = $oauth2->getAccessToken( 'code', $keys ) ;
			} catch (OAuthException $e) {
			}
		} else {
			$this->redirect($oauth2->getAuthorizeURL(WB_CALLBACK_URL, 'code', $state, 'mobile'));
		}

		if ($token) {
			setcookie( 'weibojs_'.$oauth2->client_id, http_build_query($token) );
			$uid_get = $oauth2->get_uid();
			$u = $oauth2->show_user_by_id($uid_get['uid']);
			$params = array(
				'uid'=>$u['id'],
			);
			$params['sig'] = $this->signature($params);
			$json = file_get_contents($this->createAbsoluteUrl('user/loginApi', $params));
    	    $j = json_decode($json);
        	if (!$j->data->isSuccess) {
            	$r = Yii::app()->db->createCommand('SELECT a.aid,a.name,a.gender,a.age,a.type FROM dc_image i INNER JOIN dc_animal a ON i.aid=a.aid WHERE img_id=:img_id')->bindValue(':img_id', $state)->queryRow();
            	$params = array(
                	'aid'=>$r['aid'],
                	'name'=>$r['name'],
                	'gender'=>$r['gender'],
                	'age'=>$r['age'],
                	'type'=>$r['type'],
                	'code'=>'',
                	'u_name'=>$u['name'],
                	'u_gender'=>$u['sex']=='m'?1:2,
                	'u_city'=>1001,
                	'weibo'=>$u['id'],
                	'SID'=>$j->data->SID,
            	);
            	$params['sig'] = $this->signature($params);
            	$res_register = file_get_contents($this->createAbsoluteUrl('user/registerApi', $params));
            	$json_register = json_decode($res_register);
            	if (!isset($json_register->usr_id)) {
                	$this->redirect($oauth2->getAuthorizeURL(WB_CALLBACK_URL, 'code', $state, 'mobile'));
            	}
        	}
        	$this->layout = FALSE;
        	$r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.aid, i.cmt, i.food, i.create_time, a.name, a.tx, a.type, a.gender, u.usr_id, u.tx AS u_tx, u.name AS u_name  FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE i.img_id=:img_id')->bindValue(':img_id', $state)->queryRow();
        	$this->render('/social/food', array('r'=>$r, 'sid'=>$j->data->SID));
		} else {
		    echo '认证失败';
		}
	}
}
