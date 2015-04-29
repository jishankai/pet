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
	<title>投票活动</title>
	<link rel="stylesheet" type="text/css" href="css/vote.css">
    <script>
       /* var _hmt = _hmt || [];
        (function() {
          var hm = document.createElement("script");
          hm.src = "//hm.baidu.com/hm.js?fffd5628b5c5fe81d7a7867d554d07ca";
          var s = document.getElementsByTagName("script")[0]; 
          s.parentNode.insertBefore(hm, s);
        })();


    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    window.shareData = {
        "timeLineLink": "http://192.168.10.151:8001/index.php?r=social/newYearEvent",   
        "sendFriendLink": "http://192.168.10.151:8001/index.php?r=social/newYearEvent",
        "weiboLink": "http://192.168.10.151:8001/index.php?r=social/newYearEvent",
        "tTitle": "宠物星球·年夜饭计划",
        "tContent": "举手之劳，免费捐粮。你的轻轻一点，给ta一个温暖冬天。",
        "fTitle": "宠物星球·年夜饭计划",
        "fContent": "举手之劳，免费捐粮。你的轻轻一点，给ta一个温暖冬天。",
        "wContent": "举手之劳，免费捐粮。你的轻轻一点，给ta一个温暖冬天。"
        };
        
        // 发送给好友
        WeixinJSBridge.on('menu:share:appmessage', function (argv) {
            WeixinJSBridge.invoke('sendAppMessage', {
                "img_url": "http://192.168.10.151:8001/css/images/w1.jpg",
                "img_width": "401",
                "img_height": "275",
                "link": window.shareData.sendFriendLink,
                "desc": window.shareData.fContent,
                "title": window.shareData.fTitle
            }, function (res) {
                _report('send_msg', res.err_msg);
            })
        });
        // 分享到朋友圈
        WeixinJSBridge.on('menu:share:timeline', function (argv) {
            WeixinJSBridge.invoke('shareTimeline', {
                "img_url": "http://192.168.10.151:8001/css/images/w1.jpg",
                "img_width": "401",
                "img_height": "275",
                "link": window.shareData.timeLineLink,
                "desc": window.shareData.tContent,
                "title": window.shareData.tTitle
            }, function (res) {
                _report('timeline', res.err_msg);
            });
        });
 
    }, false);*/


    </script>
    <script src="js/zepto.min.js"></script>
</head>
<body>
<div class="act1_wrap comWidth">
	<div class="act1_top">
	<a href="http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=205459850&idx=1&sn=5d2b70fc0c605a109b858438390e1cea#rd">
		<img alt="" src="css/images/vote_bannner.png">
	</a>
	</div>
	<div class="act1_body">
		<div class="ab_tit clearfix">
			
			<img src="css/images/vote_tit.gif" class="tit_img">		
			<a href="http://home4pet.aidigame.com/pet_release_2.0.0ac.apk"><img src="css/images/android.png" class="android_btn"></a>
			<a href="https://itunes.apple.com/cn/app/chong-wu-xing-qiu/id932758265?mt=8?"><img src="css/images/apple.png" class="apple_btn"></a>

		</div>
		<div class="act1_imgBox">
			<ul class="act1_imgList clearfix">
				
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1650">
						<div class="pet_img p1">
							<div class="pet_img_con">
								<div class="t_progress" style="width:<?php echo sprintf("%.2f",$animals[1650]*100*0.75/5000)?>%"></div>
								<div class="t_num_icon" style="left:<?php echo sprintf("%.2f",$animals[1650]*100*0.76/5000)?>%"><?php echo $animals[1650]?></div>
								<!-- <img src="css/images/t_pet1.jpg"/> -->
								<h1>宝宝</h1>
								<a href="http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=205465713&idx=1&sn=c1faa57d8c04ca2a286a1d11ee50d026#rd">
									<h3>来自爱犬有家</h3>
								</a>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn1"></div>
				</li>
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1653">
						<div class="pet_img p2">
							<div class="pet_img_con">
								<div class="t_progress" style="width:<?php echo sprintf("%.2f",$animals[1653]*100*0.75/5000)?>%"></div>
								<div class="t_num_icon" style="left:<?php echo sprintf("%.2f",$animals[1653]*100*0.76/5000)?>%"><?php echo $animals[1653]?></div>
								<!-- <img src="css/images/t_pet1.jpg"/> -->
								<h1>小花</h1>
								<a href="http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=205463973&idx=1&sn=fc9da90b7ba18d6b97d99d32b56ce340#rd">
									<h3>来自阿汪不哭</h3>
								</a>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn2"></div>
				</li>
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1655">
						<div class="pet_img p3">
							<div class="pet_img_con">
								<div class="t_progress" style="width:<?php echo sprintf("%.2f",$animals[1655]*100*0.75/5000)?>%"></div>
								<div class="t_num_icon" style="left:<?php echo sprintf("%.2f",$animals[1655]*100*0.76/5000)?>%"><?php echo $animals[1655]?></div>
								<!-- <img src="css/images/t_pet1.jpg"/> -->
								<h1>雪儿</h1>
								<a href="http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=205462915&idx=1&sn=a7999a7300d0b594eab110db8cd6b6b3#rd">
									<h3>来自张莹流浪狗救助小院</h3>
								</a>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn3"></div>
				</li>
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1652">
						<div class="pet_img p4">
							<div class="pet_img_con">
								<div class="t_progress" style="width:<?php echo sprintf("%.2f",$animals[1652]*100*0.75/5000)?>%"></div>
								<div class="t_num_icon" style="left:<?php echo sprintf("%.2f",$animals[1652]*100*0.76/5000)?>%"><?php echo $animals[1652]?></div>
								<!-- <img src="css/images/t_pet1.jpg"/> -->				
								<h1>小嗝嗝</h1>
								<a href="http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=205461402&idx=1&sn=125e9c94e85b4fd26da09acf676ad731#rd">
									<h3>来自诺爱之家</h3>
								</a>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn4"></div>
				</li>
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1656">
						<div class="pet_img p5">
							<div class="pet_img_con">
								<div class="t_progress" style="width:<?php echo sprintf("%.2f",$animals[1656]*100*0.75/5000)?>%"></div>
								<div class="t_num_icon" style="left:<?php echo sprintf("%.2f",$animals[1656]*100*0.76/5000)?>%"><?php echo $animals[1656]?></div>
								<!-- <img src="images1/t_pet1.jpg"/> -->	
								<h1>白手套</h1>
								<a href="http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=205465668&idx=1&sn=b05a2f111da3f2c94489138574f6e48a#rd">
								<h3>来自社区汪咪孤儿院</h3>
								</a>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn5"></div>
				</li>
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1651">
						<div class="pet_img p6">
							<div class="pet_img_con">
								<div class="t_progress" style="width:<?php echo sprintf("%.2f",$animals[1651]*100*0.75/5000)?>%"></div>
								<div class="t_num_icon" style="left:<?php echo sprintf("%.2f",$animals[1651]*100*0.76/5000)?>%"><?php echo $animals[1651]?></div>
								<!-- <img src="images1/t_pet1.jpg"/> -->
								<h1>甜甜</h1>
								<a href="http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=205464763&idx=1&sn=a75c4417660a324c57fec2e0942292a3#rd">
									<h3>来自杨洁阿姨爱心狗舍</h3>
								</a>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn6"></div>
				</li>
			</ul>
		</div>
	</div>
	<div class="act1_bottom">
		<div class="person_num">已经有<span><?php echo $users?></span>人参与了汪店长投票活动~</div>
		<div class="head_imgBox">
			<ul class="head_imgList clearfix">
				<li style="position: relative">
					<img alt="" src="" id="head1">
				</li>
				<li style="position: relative">
					<img alt="" src="" id="head2">
				</li>
				<li style="position: relative">
					<img alt="" src="" id="head3">
				</li>
				<li style="position: relative">
					<img alt="" src="" id="head4">
				</li>
				<li style="position: relative">
					<img alt="" src="" id="head5">
				</li>
				<li style="position: relative">
					<img alt="" src="" id="head6">
				</li>
				<li style="position: relative">
					<img alt="" src="" id="head7">
				</li>
				<li style="position: relative">
					<img alt="" src="" id="head8">
				</li>
			</ul>
		</div>
		<div class="mian">每天都能免费投5票~明天再来哟！</div>
		<div id="tabs">
		    <ul>
		        <li class="on" style="color:red;">活动介绍</li>
		        <li>参与方式</li>
		        <li>主办方介绍</li>
		    </ul>
    <div>
       	<p>宠物星球·我是大萌星联合BARKSTARS汪星客咖啡第一届汪店长公开选拔开始啦！6只萌萌哒汪星人，谁能登上店长宝座？快为你支持的候选汪投票吧！第一只达到票数目标的候选汪将成为BARKSTARS汪星客咖啡店驻店店长，任期一个月（其他完成目标的汪也会按完成时间顺延做汪店长哦！）。</p>
      	<p>汪店长将由帝都首家对狗儿说‘YES’的爱犬主题精品咖啡店汪星客负责饲养管理，有意领养者还可到店与狗狗进行亲密接触，符合领养条件还有机会把汪店长带回家哦~5月28日荣祥广场“萌宠家庭日”现场，汪店长还将与粉丝见面，欢迎调戏~还有更多神秘惊喜等着你！</p>	
    </div>
    <div class="hide">
       	<p>1.在投票页面选择喜欢的汪星人，点击“投TA一票”→完成相关授权→投票成功！</p>
		<p>2.每人每天都能获得金币用于投票，萌的路上不怕积累和重复，记得每天都要来哟！</p>
		<p>3.你的朋友也可以在你分享出的活动页面进行投票！快喊小伙伴一起为钟爱的萌犬投票吧~</p>

    </div>
    <div class="hide">
        <p>宠物星球·我是大萌星App是一个萌宠至上的宠物社区，在这里你可以：卖萌换口粮，让你家宝贝为自己代粮！结交萌宠和亲亲主人，交流我们的小秘密！参加星球活动，和可爱萌宠大联欢！我们希望，展示每一只宠物的与众不同，打造最闪亮的萌星！</p>
		<p>BARKSTARS汪星客咖啡，是帝都首家对狗儿说‘YES’的爱犬主题精品咖啡店。有萌犬有咖啡~还等什么，快带上你的爱犬来这里做客吧~不是星巴克，是汪星客哦！</p>

    </div>
</div>
		<div class="hr_100"></div> 
	</div>
</div>
</body>
<script src="js/md5.js"></script>
<script  src="js/jquery-1.11.1.js"></script>
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
        if (<?php echo $alert_flag?>) {
            no_ticket();
        };
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

        	//投票按钮组
					$("#t_btn1").click(function(){
						var n=1;
						var aid = 1650;
						var sig =md5('aid='+aid+'&is_dog=1&n='+n+'dog&cat');
						location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&is_dog=1&n='+n+'&sig='+sig+'&SID='+<?php echo "'".$SID."'" ?>;			
					});
					$("#t_btn2").click(function(){
						var n=1;
						var aid = 1653;
						var sig =md5('aid='+aid+'&is_dog=1&n='+n+'dog&cat');
						location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&is_dog=1&n='+n+'&sig='+sig+'&SID='+<?php echo "'".$SID."'" ?>;	
					});
					$("#t_btn3").click(function(){
						var n=1;
						var aid = 1655;
						var sig =md5('aid='+aid+'&is_dog=1&n='+n+'dog&cat');
						location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&is_dog=1&n='+n+'&sig='+sig+'&SID='+<?php echo "'".$SID."'" ?>;	
					});
					$("#t_btn4").click(function(){
						var n=1;
						var aid = 1652;
						var sig =md5('aid='+aid+'&is_dog=1&n='+n+'dog&cat');
						location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&is_dog=1&n='+n+'&sig='+sig+'&SID='+<?php echo "'".$SID."'" ?>;	
					});
					$("#t_btn5").click(function(){
						var n=1;
						var aid = 1656;
						var sig =md5('aid='+aid+'&is_dog=1&n='+n+'dog&cat');
						location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&is_dog=1&n='+n+'&sig='+sig+'&SID='+<?php echo "'".$SID."'" ?>;	
					});
					$("#t_btn6").click(function(){
						var n=1;
						var aid = 1651;
						var sig =md5('aid='+aid+'&is_dog=1&n='+n+'dog&cat');
						location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&is_dog=1&n='+n+'&sig='+sig+'&SID='+<?php echo "'".$SID."'" ?>;	
					});

    }


//今天的免费次数用完了
function no_ticket(){
	//屏幕的高度和宽度
	var sHeight=document.documentElement.scrollHeight;
	var sWidth=document.documentElement.scrollWidth;
	//可视区域的高度
	var wHeight=document.documentElement.clientHeight;

	var rMask = document.createElement("div");
		rMask.id="rMask";
		rMask.className="mask";
		rMask.style.height = sHeight + "px";
		rMask.style.width = sWidth + "px";
		document.body.appendChild(rMask);

		var recommend_alert = document.createElement("div");
		recommend_alert.id = "recommend_alert";
		recommend_alert.className = "recommend_alert";
		recommend_alert.innerHTML = "<div id='close'></div><h3>今天的免费投票次数用完了</h3><p>明天还有免费机会，再来哟！</p><div id='tok_btn'>好的</div><div class='the_more'>戳顶部<span>下载链接</span>寻找更多大萌星</div>";


		/*<div id='close'></div>
		<h3>今天的免费投票次数用完了</h3>
		<p>明天还有免费机会，再来哟！</p>
		<div id='tok_btn'></div>
		<div class='the_more'>戳顶部<span>下载链接</span>寻找更多大萌星</div>*/

		document.body.appendChild(recommend_alert);
		var rHeight = recommend_alert.offsetHeight;
		var rWidth = recommend_alert.offsetWidth;

		recommend_alert.style.left = (sWidth-rWidth)/2 + "px";
		recommend_alert.style.top = (wHeight-rHeight)/2 + "px";

		var rec_btn_no = document.getElementById("close");
		rec_btn_no.onclick = function(){
			document.body.removeChild(recommend_alert);
			document.body.removeChild(rMask);
		}
		var rec_btn_yes = document.getElementById("tok_btn");
		rec_btn_yes.onclick = function(){
			document.body.removeChild(recommend_alert);
			document.body.removeChild(rMask);
		}

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
        title: "包吃包住包撒欢儿，宠物星球·我是大萌星携手BARKSTARS汪星客咖啡，第一任汪店长公开选拔开！始！啦！",
        desc: "每天都有免费投票机会，快来给你喜欢的候选汪投票呀~",
        link: "http://"+window.location.host+"/index.php?r=social/vote", 
        imgUrl: "http://"+window.location.host+"/css/images/vote_share_icon.jpg",
        
    };
    wx.onMenuShareAppMessage(shareData);
    wx.onMenuShareTimeline(shareData);
    wx.onMenuShareQQ(shareData);
    wx.onMenuShareWeibo(shareData);
  });
</script>
</html>
