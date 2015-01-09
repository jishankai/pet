<?php

Yii::import('ext.sinaWeibo.SinaWeibo',true);

class WeiboController extends Controller
{
	public function actionCallback(){
		$oauth2 = new SinaWeibo(WB_AKEY, WB_SKEY);
		$state = $_REQUEST['state'];
		if (isset($_REQUEST['code'])) {
			$keys = array();
			$keys['code'] = $_REQUEST['code'];
			$keys['redirect_uri'] = $this->createAbsoluteUrl('weibo/callback');
			try {
				$token = $oauth2->getAccessToken( 'code', $keys ) ;
			} catch (OAuthException $e) {
			}
		} else {
			$this->redirect($oauth2->getAuthorizeURL(WB_CALLBACK_URL, 'code', $state, 'mobile'));
		}

		if ($token) {
			parse_str($state);
			setcookie( 'weibojs_'.$oauth2->client_id, http_build_query($token) );
			$c = new SaeTClientV2( WB_AKEY , WB_SKEY , $token['access_token'] );
			$uid_get = $c->get_uid();
			$u = $c->show_user_by_id($uid_get['uid']);
			$params = array(
				'uid'=>$u['id'],
			);
			$params['sig'] = $this->signature($params);
			$json = file_get_contents($this->createAbsoluteUrl('user/loginApi', $params));
    	    $j = json_decode($json);
        	if (!$j->data->isSuccess) {
            	$r = Yii::app()->db->createCommand('SELECT a.aid,a.name,a.gender,a.age,a.type FROM dc_animal a WHERE aid=:aid')->bindValue(':aid', $aid)->queryRow();
            	$params = array(
                	'aid'=>$r['aid'],
                	'name'=>$r['name'],
                	'gender'=>$r['gender'],
                	'age'=>$r['age'],
                	'type'=>$r['type'],
                	'code'=>'',
                	'u_name'=>'星'.mb_substr($u['id'], 0, 6),
                	'u_gender'=>$u['gender']=='m'?1:2,
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
        	Yii::import('ext.sinaWeibo.SinaWeibo',true);
            $oauth2 = new SinaWeibo(WB_AKEY, WB_SKEY);
        	setcookie('weibooauth2_'.$oauth2->client_id, http_build_query(array('usr_id'=>$j->data->usr_id)) );
        	if ($img_id==0) {
            	$this->redirect(array('social/activityview', 'aid'=>$aid, 'SID'=>$j->data->SID));
        	} else {
            	$this->redirect(array('social/foodShareApi', 'img_id'=>$img_id, 'aid'=>$aid, 'SID'=>$j->data->SID));
        	}
		} else {
		    echo '认证失败';
		}
	}
}
