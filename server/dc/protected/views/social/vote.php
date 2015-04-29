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
		<img alt="" src="css/images/vote_bannner.png">
	</div>
	<div class="act1_body">
		<div class="ab_tit clearfix">
			<img src="css/images/vote_tit.gif" class="tit_img">
			<a href="http://home4pet.aidigame.com/pet_release_1.1.2.apk"><img src="css/images/android.png" class="android_btn"></a>
			<a href="https://itunes.apple.com/cn/app/chong-wu-xing-qiu/id932758265?mt=8?"><img src="css/images/apple.png" class="apple_btn"></a>

		</div>
		<div class="act1_imgBox">
			<ul class="act1_imgList clearfix">
				
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1650">
						<div class="pet_img p1">
							<div class="pet_img_con">
								<div class="t_progress"></div>
								<div class="t_num_icon">11</div>
								<!-- <img src="css/images/t_pet1.jpg"/> -->
								<h1>宝宝</h1>
								<h3>来自爱犬有家</h3>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn1"></div>
				</li>
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1653">
						<div class="pet_img p2">
							<div class="pet_img_con">
								<div class="t_progress"></div>
								<div class="t_num_icon">11</div>
								<!-- <img src="css/images/t_pet1.jpg"/> -->
								<h1>小花</h1>
								<h3>来自阿汪不哭</h3>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn2"></div>
				</li>
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1655">
						<div class="pet_img p3">
							<div class="pet_img_con">
								<div class="t_progress"></div>
								<div class="t_num_icon">11</div>
								<!-- <img src="css/images/t_pet1.jpg"/> -->
								<h1>雪儿</h1>
								<h3>来自张莹流浪狗救助小院</h3>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn3"></div>
				</li>
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1652">
						<div class="pet_img p4">
							<div class="pet_img_con">
								<div class="t_progress"></div>
								<div class="t_num_icon">11</div>
								<!-- <img src="css/images/t_pet1.jpg"/> -->				
								<h1>小嗝嗝</h1>
								<h3>来自诺爱之家</h3>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn4"></div>
				</li>
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1656">
						<div class="pet_img p5">
							<div class="pet_img_con">
								<div class="t_progress"></div>
								<div class="t_num_icon">11</div>
								<!-- <img src="images1/t_pet1.jpg"/> -->	
								<h1>白手套</h1>
								<h3>来自社区汪咪孤儿院</h3>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn5"></div>
				</li>
				<li>
					<a href="http://release4pet.imengstar.com/index.php?r=animal/infoShare&aid=1655">
						<div class="pet_img p6">
							<div class="pet_img_con">
								<div class="t_progress"></div>
								<div class="t_num_icon">11</div>
								<!-- <img src="images1/t_pet1.jpg"/> -->
								<h1>甜甜</h1>
								<h3>来自杨洁阿姨爱心狗舍</h3>
							</div>
						</div>
					</a>
					<div class="t_btn" id="t_btn6"></div>
				</li>
			</ul>
		</div>
	</div>
	<div class="act1_bottom">
		<div class="person_num">已经有<span>123445</span>人参与了汪店长投票活动~</div>
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
	<div class="float">
		<div class="load_shadow"><img src="css/images/load_shadow.png"></div>
		<div class="load">

			<div class="load_left">
				<div class="logo_icon">
					<img src="css/images/logo_icon.jpg"/>
				</div>
				<div class="load_info">
					<h3>我是大萌星</h3>
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
</html>
