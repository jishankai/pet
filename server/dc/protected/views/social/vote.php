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
		<img alt="" src="css/images/1.jpg">
	</div>
	<div class="act1_body">
		<div class="ab_tit clearfix">
			<img src="css/images/ab_tit1.gif" class="tit_img">
			<a href="http://home4pet.aidigame.com/pet_release_1.1.2.apk"><img src="css/images/android.png" class="android_btn"></a>
			<a href="https://itunes.apple.com/cn/app/chong-wu-xing-qiu/id932758265?mt=8?"><img src="css/images/apple.png" class="apple_btn"></a>

		</div>
		<div class="act1_imgBox">
			<ul class="act1_imgList clearfix">
				
				<li>
					<div class="pet_img">
						<div class="pet_img_con">
							<div class="t_progress"></div>
							<div class="t_num_icon">11</div>
							<!-- <img src="css/images/t_pet1.jpg"/> -->
							<h1>年薪过百万</h1>
							<h3>来自邓不拉多星球</h3>
						</div>
					</div>
					<div class="t_btn" id="t_btn1"></div>
				</li>
				<li>
					<div class="pet_img">
						<div class="pet_img_con">
							<div class="t_progress"></div>
							<div class="t_num_icon">11</div>
							<!-- <img src="css/images/t_pet1.jpg"/> -->
							<h1>年薪过百万</h1>
							<h3>来自邓不拉多星球</h3>
						</div>
					</div>
					<div class="t_btn"></div>
				</li>
				<li>
					<div class="pet_img">
						<div class="pet_img_con">
							<div class="t_progress"></div>
							<div class="t_num_icon">11</div>
							<!-- <img src="css/images/t_pet1.jpg"/> -->
							<h1>年薪过百万</h1>
							<h3>来自邓不拉多星球</h3>
						</div>
					</div>
					<div class="t_btn"></div>
				</li><li>
					<div class="pet_img">
						<div class="pet_img_con">
							<div class="t_progress"></div>
							<div class="t_num_icon">11</div>
							<!-- <img src="css/images/t_pet1.jpg"/> -->
							<h1>年薪过百万</h1>
							<h3>来自邓不拉多星球</h3>
						</div>
					</div>
					<div class="t_btn"></div>
				</li>
			</ul>
		</div>
	</div>
	<div class="act1_bottom">
		<div class="person_num">已经有<span>123445</span>人参与了捐粮，快来加入爱心大军~~</div>
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
		<div class="mian">每天都能免费赠3份，爱心不怕重复，明天再来哟！</div>
		<div id="tabs">
		    <ul>
		        <li class="on" style="color:red;">喜大普奔</li>
		        <li>参与方式</li>
		        <li>主办方介绍</li>
		    </ul>
    <div>
       	<p>喜大普奔~严寒和雾霾都挡不住小伙伴们的爱心！据活动结束还有两天，各位可爱的小主就帮助我们筹集齐了六个地区的冬粮，流浪喵的年夜饭有着落啦！小宠这厢有礼了喵~（看看上边羞耻的动态图就知道我们有多开心了~~才不会告诉你那是我们单身的CTO呢！）</p>
      	<p>截止目标达成，共有22000位网友参与了我们的公益捐粮活动。我们也将在本周六（1月24日），和@酷宠新天地流浪动物救助平台 @全峰快递集团 一起，专车为各个群护区捐赠大家打赏的年夜饭。届时，将有微博全程直播，欢迎大家监督~</p>
		<p>小宠在这里代表为本次活动辛勤付出的全体工作人员，向支持公益和陪伴宠物星球成长的你们，说一声：谢谢。</p>
		

    </div>
    <div class="hide">
       	<p>1.点击代粮猫头像进入捐粮页面→在相应区域主页选择捐粮份数→点击“赏”→完成相关授权→捐赠成功</p>
		<p>2.每人每天都能获得金币用于捐粮，连续登陆天数越长获得金币越多哦！爱心不怕积累和重复，记得每天都要来！</p>
		<p>3.你的朋友也可以在你分享出的活动页面进行捐助！快喊小伙伴一起为流浪喵筹集冬粮吧！</p>

    </div>
    <div class="hide">
        <p>宠物星球社交应用是一个萌宠至上的宠物社区，在这里你可以：卖萌换口粮，让你家宝贝为自己代粮！结交萌宠和亲亲主人，交流我们的小秘密！参加星球活动，和可爱萌宠大联欢！我们希望，展示每一只宠物的与众不同，打造最闪亮的萌星！</p>
		<p>酷宠新天地流浪动物救助平台，是一个致力于帮助流浪猫狗寻找领养主人的优秀平台，通过对各地区领养信息的扩散宣传，为每一个等家的小生命搭建爱心桥梁。同时传递科学养宠与责任养宠的理念，领养代替购买，让生命不再流浪。</p>

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
				<img src="css/images/load.jpg"/>
			</div>
		</div>
	</div>
</div>
</body>
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

	head1.src="images/base_head"+ad1+".jpg";
	head2.src="images/base_head"+ad2+".jpg";
	head3.src="images/base_head"+ad3+".jpg";
	head4.src="images/base_head"+ad4+".jpg";
	head5.src="images/base_head"+ad5+".jpg";
	head6.src="images/base_head"+ad6+".jpg";
	head7.src="images/base_head"+ad7+".jpg";
	head8.src="images/base_head"+ad8+".jpg";

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
</html>
