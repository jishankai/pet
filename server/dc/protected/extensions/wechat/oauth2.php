<?php
class oauth2{
	public $REDIRECT_URL="";
 	public $APPID="";
 	public $SECRET="";
 	
 	public $Code="";
 	public $State="";
 	public $Access_token="";
 	
 	public $Openid="";
 	
 	function __construct(){		
 		//默认使用的appid
 		$this->APPID='';
 		$this->SECRET='';		
 	} 	
    
 	/**
 	 * 初始化参数。(包括微信接口参数$code、$state)
 	 * @param string $APPID
 	 * @param string $SECRET
 	 * @param string $REDIRECT_URL
 	 */
 	function init($APPID='',$SECRET='',$REDIRECT_URL='http://kouliang.tuturead.com/index.php?r=wechat/callback'){
 		$this->REDIRECT_URL=$REDIRECT_URL;
 		if ($APPID!='') $this->APPID=$APPID;
 		if ($SECRET!='') $this->SECRET=$SECRET;
 		
 		if (isset($_GET['code'])) $this->Code=$_GET['code'];//code
 		if (isset($_GET['state'])) $this->State=$_GET['state'];//state参数

 	}
 	
 	/**
 	 * 获取Code
 	 * (传递state参数)
 	 */
 	function get_code($state='1'){		
 		$APPID=$this->APPID;
 		$redirect_uri=$this->REDIRECT_URL;
 		$url_get_code="https://open.weixin.qq.com/connect/oauth2/authorize?appid=$APPID&redirect_uri=$redirect_uri&response_type=code&scope=snsapi_login&state=$state#wechat_redirect";
 		header("Location: $url_get_code");//重定向请求微信用户信息
 	}
 	/**
 	 * 获取用户openid
 	 * @param string $redirect_uri
 	 * @param string $state 传参
 	 */
 	function get_openid(){
 		$APPID=$this->APPID;
 		$SECRET=$this->SECRET;
 		$code=$this->Code;
 		
 		$url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=$APPID&secret=$SECRET&code=$code&grant_type=authorization_code";
		$content=file_get_contents($url);
		$o=json_decode($content,true);
		$this->Openid=$o['openid'];
		return $o['openid'];
 	}
 	
 	/**
 	 * 授权获取code
 	 */
 	function get_code_by_authorize($state){
 		$APPID=$this->APPID;
 		$redirect_uri=$this->REDIRECT_URL;
 		$url_get_code="https://open.weixin.qq.com/connect/oauth2/authorize?appid=$APPID&redirect_uri=$redirect_uri&response_type=code&scope=snsapi_login&state=$state#wechat_redirect";
 		header("Location: $url_get_code");//重定向请求微信用户信息		
 	}
 	
 	/**
 	 * 授权获取用户信息
 	 */
 	function get_userinfo_by_authorize($code){
 		$APPID=$this->APPID;
 		$SECRET=$this->SECRET;
 			
 		$url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=$APPID&secret=$SECRET&code=$code&grant_type=authorization_code";
 		$content=file_get_contents($url);
 		$o=json_decode($content,true);
 		$openid=$o['openid'];
 		$access_token=$o['access_token'];
 		
 		$url2="https://api.weixin.qq.com/sns/userinfo?access_token=$access_token&openid=$openid&lang=zh_CN";
 		$content2=file_get_contents($url2);
 		$o2=json_decode($content2,true);//微信获取用户信息
 		
 		//处理昵称里的特殊字符
 		$str_nickname=substr($content2,strpos($content2,",")+1);
 		$str_nickname=substr($str_nickname,12,strpos($str_nickname,",")-13);
 		
 		$data=$o2;
 		$data['nickname']=$str_nickname;
 		
 		return $data;
 		 		
 	}
 	
 	
 }
?>
