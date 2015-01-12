<?php
require_once "jssdk.php";
$jssdk = new JSSDK(WECHAT_MP_ID, WECHAT_MP_SECRET);
$signPackage = $jssdk->GetSignPackage();
?>
<!DOCTYPE>
<html>
<head>
	<meta name="viewport" content="width = device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>宠物星球年夜饭计划</title>
	<link rel="stylesheet" type="text/css" href="css/activity_index.css">
    <script>
        var _hmt = _hmt || [];
        (function() {
          var hm = document.createElement("script");
          hm.src = "//hm.baidu.com/hm.js?fffd5628b5c5fe81d7a7867d554d07ca";
          var s = document.getElementsByTagName("script")[0]; 
          s.parentNode.insertBefore(hm, s);
        })();
    </script>
</head>
<body>
<div class="act1_wrap comWidth">
	<div class="act1_top">
		<img alt="" src="css/images/1.jpg">
	</div>
	<div class="act1_body">
		<div class="ab_tit">点击代粮喵头像，为ta的小伙伴捐粮</div>
		<div class="act1_imgBox">
			<ul class="act1_imgList clearfix">
                <li><a href="<?php echo $this->createUrl('social/activityview',array('aid'=>654, 'SID'=>$sid))?>"><img alt="" src="css/images/a5.jpg"></a></li>
				<li><a href="<?php echo $this->createUrl('social/activityview',array('aid'=>651, 'SID'=>$sid))?>"><img alt="" src="css/images/a2.jpg"></a></li>
				<li><a href="<?php echo $this->createUrl('social/activityview',array('aid'=>652, 'SID'=>$sid))?>"><img alt="" src="css/images/a3.jpg"></a></li>
				<li><a href="<?php echo $this->createUrl('social/activityview',array('aid'=>660, 'SID'=>$sid))?>"><img alt="" src="css/images/a4.jpg"></a></li>
				<li><a href="<?php echo $this->createUrl('social/activityview',array('aid'=>653, 'SID'=>$sid))?>"><img alt="" src="css/images/a1.jpg"></a></li>
				<li><a href="<?php echo $this->createUrl('social/activityview',array('aid'=>655, 'SID'=>$sid))?>"><img alt="" src="css/images/a6.jpg"></a></li>
			</u>
		</div>
	</div>
	<div class="act1_bottom">
	<!-- <div class="am_tit">TA们正在参与伸出援手</div> -->
	<div class="person_num">已经有<span><?php echo $users ?></span>人参与了捐粮，快来加入爱心大军~~</div>
		<div class="head_imgBox">
			<ul class="head_imgList clearfix">
				<li style="position: relative">
					<img alt="" src="css/images/base_head1.jpg" id="head1">
					<img src="css/images/photo-m-t.png" style="position: absolute;left:0;top:0px;z-index:1；">
				</li>
				<li style="position: relative">
					<img alt="" src="css/images/base_head2.jpg" id="head2">
					<img src="css/images/photo-m-t.png" style="position: absolute;left:0;top:0px;z-index:1；">
				</li>
				<li style="position: relative">
					<img alt="" src="css/images/base_head3.jpg" id="head3">
					<img src="css/images/photo-m-t.png" style="position: absolute;left:0;top:0px;z-index:1；">
				</li>
				<li style="position: relative">
					<img alt="" src="css/images/base_head4.jpg" id="head4">
					<img src="css/images/photo-m-t.png" style="position: absolute;left:0;top:0px;z-index:1；">
				</li>
				<li style="position: relative">
					<img alt="" src="css/images/base_head5.jpg" id="head5">
					<img src="css/images/photo-m-t.png" style="position: absolute;left:0;top:0px;z-index:1；">
				</li>
				<li style="position: relative">
					<img alt="" src="css/images/base_head6.jpg" id="head6">
					<img src="css/images/photo-m-t.png" style="position: absolute;left:0;top:0px;z-index:1；">
				</li>
				<li style="position: relative">
					<img alt="" src="css/images/base_head7.jpg" id="head7">
					<img src="css/images/photo-m-t.png" style="position: absolute;left:0;top:0px;z-index:1；">
				</li>
				<li style="position: relative">
					<img alt="" src="css/images/base_head8.jpg" id="head8">
					<img src="css/images/photo-m-t.png" style="position: absolute;left:0;top:0px;z-index:1；">
				</li>

			</ul>
		</div>
		<div class="mian">连续登陆天数越长，能免费捐助的份数就越多哦！</div>
		<div id="tabs">
			<ul>
				<li class="on">活动介绍</li>
				<li>参与方式</li>
				<li>主办方介绍</li>
			</ul>
			<div>
				<p>宠物星球社交应用携手酷宠新天地流浪动物救助平台，为北京地区的流浪猫筹集1个月的冬粮。</p>
				<p>筹集粮食目标是由酷宠新天地流浪动物救助平台经过详细调研、科学计算出的。</p>
				<p>筹得的粮食将由酷宠新天地流浪动物救助平台统一调派发放。</p>
				<p>技术支持：<font color="orange">宠物星球社交应用</font></p>
				<p>粮食提供：<font color="orange">宠物星球社交应用</font>，<font color="purple">好时天然猫粮</font></p>

			</div>
			<div class="hide">
				<p>1.点击代粮猫头像进入捐粮页面→在相应区域主页选择捐粮份数→点击“赏”→完成相关授权→捐赠成功</p>
				<p>2.每人每天都能获得金币用于捐粮，连续登陆天数越长获得金币越多哦！爱心不怕积累和重复，记得每天都要来！</p>
				<p>3.你的朋友也可以在你分享出的活动页面进行捐助！快喊小伙伴一起为流浪喵筹集冬粮吧！</p>

			</div>
			<div class="hide">
				<p><font color="orange">宠物星球社交应用</font>是一个萌宠至上的宠物社区，在这里你可以：<br />卖萌换口粮，让你家宝贝为自己代粮！<br />结交萌宠和亲亲主人，交流我们的小秘密！<br />参加星球活动，和可爱萌宠大联欢！<br />我们希望，展示每一只宠物的与众不同，打造最闪亮的萌星！</p>
				<p><font color="green">酷宠新天地流浪动物救助平台</font>是一个致力于帮助流浪猫狗寻找领养主人的优秀平台，通过对各地区领养信息的扩散宣传，为每一个等家的小生命搭建爱心桥梁。同时传递科学养宠与责任养宠的理念，领养代替购买，让生命不再流浪。</p>

			</div>
		</div>
		<div class="hr_100"></div>
	</div>
	<div class="float">
		<div class="load_shadow"><img src="css/images/load_shadow.png"></div>
		<div class="load">

			<div class="load_left">
				<div class="logo_icon">
					<img src="css/images/logo_icon.jpg"/>
				</div>
				<div class="load_info">
					<h3>宠物星球</h3>
					<p>我为自己代粮</p>
				</div>
			</div>
			<div class="load_right">
				<a href="http://home4pet.aidigame.com" target="_blank">
				    <img src="css/images/load.jpg"/>
                </a>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
	var head1=document.getElementById("head1");
	var head2=document.getElementById("head2");
	var head3=document.getElementById("head3");
	var head4=document.getElementById("head4");
	var head5=document.getElementById("head5");
	var head6=document.getElementById("head6");
	var head7=document.getElementById("head7");
	var head8=document.getElementById("head8");
	var ad1=Math.floor((Math.random())*4);
	var ad2=Math.floor((Math.random())*4+4);
	var ad3=Math.floor((Math.random())*4+8);
	var ad4=Math.floor((Math.random())*4+12);
	var ad5=Math.floor((Math.random())*4+16);
	var ad6=Math.floor((Math.random())*4+20);
	var ad7=Math.floor((Math.random())*4+24);
	var ad8=Math.floor((Math.random())*4+28);
	head1.src="css/images/base_head"+ad1+".jpg";
	head2.src="css/images/base_head"+ad2+".jpg";
	head3.src="css/images/base_head"+ad3+".jpg";
	head4.src="css/images/base_head"+ad4+".jpg";
	head5.src="css/images/base_head"+ad5+".jpg";
	head6.src="css/images/base_head"+ad6+".jpg";
	head7.src="css/images/base_head"+ad7+".jpg";
	head8.src="css/images/base_head"+ad8+".jpg";
	window.onload = function(){
		var oTab = document.getElementById("tabs");
		var oUl = oTab.getElementsByTagName("ul")[0];
		var oLis = oUl.getElementsByTagName("li");
		var oDivs= oTab.getElementsByTagName("div");

		for(var i= 0,len = oLis.length;i<len;i++){
			oLis[i].index = i;
			oLis[i].onclick = function() {
				for(var n= 0;n<len;n++){
					oLis[n].className = "";
					oDivs[n].className = "hide";
				}
				this.className = "on";
				oDivs[this.index].className = "";
			}
		};
	}
</script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
  // 注意：所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。 
  // 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
  // 完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
  wx.config({
    appId: '<?php echo $signPackage["appId"];?>',
    timestamp: <?php echo $signPackage["timestamp"];?>,
    nonceStr: '<?php echo $signPackage["nonceStr"];?>',
    signature: '<?php echo $signPackage["signature"];?>',
    jsApiList: [
      // 所有要调用的 API 都要加到这个列表中
      'checkJsApi',
      'onMenuShareTimeline',
      'onMenuShareAppMessage',
      'onMenuShareQQ',
      'onMenuShareWeibo',
    ]
  });
  wx.ready(function () {
    // 在这里调用 API
    var shareData = {
    	title: "宠物星球·年夜饭计划",
    	desc: "举手之劳，免费捐粮。你的轻轻一点，给ta一个温暖冬天。",
    	link: "http://"+window.location.host+"/index.php?r=social/newYearEvent",
    	imgUrl: "http://"+window.location.host+"/css/images/w1.jpg"
    };
    wx.onMenuShareAppMessage(shareData);
    wx.onMenuShareTimeline(shareData);
    wx.onMenuShareQQ(shareData);
    wx.onMenuShareWeibo(shareData);
  });
</script>
</html>
