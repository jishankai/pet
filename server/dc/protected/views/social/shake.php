<?php
require_once "jssdk.php";
$jssdk = new JSSDK(WECHAT_MP_ID, WECHAT_MP_SECRET);
$signPackage = $jssdk->GetSignPackage();
?>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<meta name='HandheldFriendly' content='True' />
		<!--<meta name='viewport' content='initial-scale=1.0; maximum-scale=1.0; user-scalable=0;' />-->
		<meta name='viewport' content='user-scalable=0' />
		<meta name="viewport" content="width=device-width" />
        <title>摇一摇</title>
	<link rel="stylesheet" type="text/css" href="css/shake.css">
</head>
<body>
<div class="comWidth">
	<div class="wraper">
		<div class="head clearfix">
			<div class="img"><img src="http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>" id="head"></div>
			<div>帮 <span><?php echo $r['name']?></span> 摇一摇</div>
		</div>
		<div class="box">
			<div class="container">
				<img src="css/images/shake_shake_image.png" class="shake" id="shake"/>
				<div id="gift_Box "></div>
			</div>

			<div class="container1">
				<div class="gift_Box ">
					<div class="gift_tit"><span id="gift_name">女神蝴蝶结</span>x1</div>
					<div class="gift_con" style="display:none;">
						<div id="pet_name">猫君</div>
						<p id="effect">收到了您送的女神蝴蝶结感到神清气爽</p>
					</div>
					<div class="gift_bottom">
						<img src="css/images/gift1.png" id="gift_img" class="gift_img">
						<p>人气<span id="popularity">10</span></p>
					</div>
				</div>
			</div>
		</div>
		
		
		<p class="text" id="text">每天摇一摇，精彩礼品大放送～</p>
		<p class="none">今天的次数用完啦，记得明天还来哦～</p>

		<ul class="shareList clearfix">
			<li>
				<img src="css/images/dialog_go_register_weixin.png">
				<p>微信好友</p>
			</li>
			<li>
				<img src="css/images/dialog_go_register_friend.png">
				<p>朋友圈</p>
			</li>
			<li>
				<img src="css/images/dialog_go_register_xinlang.png.png">
				<p>微博</p>
			</li>
		</ul>
		
	</div>
	<div class="prompt">每天第一次成功分享后，可以再摇3次喔～</div>

	<div class="s_btn">
		<a href="#" id="s_btn">我也来摇</a>
	</div>
	<div class="s_btn1 clearfix">
		<a href="#" id="again">再摇<span id="again_times">1</span>次</a>
		<a href="#" id="send">就送这个</a>		
	</div>
	<div id="chance">还剩<span id="chance_times"><?php echo $chance_times?>/span>次</div>

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
	<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
	<script type = "text/javascript">

window.onload=function(){

	var s_btn= document.getElementById("s_btn");
	
	s_btn.onclick=function(){
		if (<?php echo isset($SID)&&$SID!=''?>) {
			location.href = <?php echo "'".$this->createUrl('animal/shakeMobileApi', array('aid'=>$aid, 'SID'=>$SID))."'" ?>
		};

		/*摇一摇实现*/
		$("#s_btn").hide();
  
	    var SHAKE_THRESHOLD = 1000;
	    var last_update = 0;
	    var x = y = z = last_x = last_y = last_z = 0;

	    if (window.DeviceMotionEvent) {
	        window.addEventListener('devicemotion', deviceMotionHandler, false);
	    } else {
	        alert('本设备不支持devicemotion事件');
	    }

	    function deviceMotionHandler(eventData) {
	        var acceleration = eventData.accelerationIncludingGravity;
	        var curTime = new Date().getTime();

	        if ((curTime - last_update) > 100) {
	            var diffTime = curTime - last_update;
	            last_update = curTime;
	            x = acceleration.x;
	            y = acceleration.y;
	            z = acceleration.z;
	            var speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
	            var status = document.getElementById("status");

	            if (speed > SHAKE_THRESHOLD) {
	                doResult();
	            }
	            last_x = x;
	            last_y = y;
	            last_z = z;
	        }
	    }

	    flag="true";  //控制摇一摇

	    /*摇一摇后随机礼物函数*/
	    function doResult() {
	     
			$(".container").hide();
			$(".container1").show();

			if(flag=="true"){
				var num=Math.floor((Math.random())*16+1);
				/*alert(num);*/
				$("#gift_img").attr("src","css/images/gift"+num+".png");
				flag="false";

				switch(num)
				{
					case 1:
						$("#gift_name").html("好大一条鱼");
						$("#popularity").html("＋28");
						$("#effect").html("盯着大鱼，口水流出来了~");
						gift_id=1101;
						break;
					case 2:
						$("#gift_name").html("甜蜜缤纷");
						$("#popularity").html("＋18");
						$("#effect").html("舔了舔糖果，幸福到不行~");
						gift_id=1102;
						break;
					case 3:
						$("#gift_name").html("萌铛铛");
						$("#popularity").html("＋18");
						$("#effect").html("拨弄着铃铛，主人快被烦死了~");
						gift_id=1103;
						break;
					case 4:
						$("#gift_name").html("女神蝴蝶结");
						$("#popularity").html("＋28");
						$("#effect").html("戴上了臭美哒蝴蝶结，照镜子去啦~");
						gift_id=1104;
						break;
					case 5:
						$("#gift_name").html("求陪玩");
						$("#popularity").html("＋45");
						$("#effect").html("立刻精神抖擞地去追球球了~");
						gift_id=1201;
						break;
					case 6:
						$("#gift_name").html("带我飞");
						$("#popularity").html("＋45");
						$("#effect").html("纵身一跃，宇宙翱翔~");
						gift_id=1202;
						break;
					case 7:
						$("#gift_name").html("土豪零食");
						$("#popularity").html("＋58");
						$("#effect").html("吃货本质终于暴露在你面前啦！");
						gift_id=1203;
						break;
					case 8:
						$("#gift_name").html("萌力无边");
						$("#popularity").html("＋58");
						$("#effect").html("瞪大了圆圆滴眼睛，萌！");
						gift_id=1204;
						break;
					case 9:
						$("#gift_name").html("喷香香");
						$("#popularity").html("＋80");
						$("#effect").html("现在香喷喷哒，快来亲一口！");
						gift_id=1301;
						break;
					case 10:
						$("#gift_name").html("撒气小兽");
						$("#popularity").html("＋80");
						$("#effect").html("对着小兽撒了一通气，可爽啦！");
						gift_id=1302;
						break;
					case 11:
						$("#gift_name").html("求约会");
						$("#popularity").html("＋95");
						$("#effect").html("得看你的表现才会决定跟不跟你约会哦！");
						gift_id=1303;
						break;
					case 12:
						$("#gift_name").html("暖男围巾");
						$("#popularity").html("＋95");
						$("#effect").html("顿时热乎乎的，夸赞你是暖男呢！");
						gift_id=1304;
						break;
					case 13:
						$("#gift_name").html("温馨小窝");
						$("#popularity").html("＋140");
						$("#effect").html("满意的躺到了新家里，身子暖暖哒~~");
						gift_id=1401;
						break;
					case 14:
						$("#gift_name").html("萌星合约");
						$("#popularity").html("＋140");
						$("#effect").html("捧着萌星合约，决心一定要做大萌星！");
						gift_id=1402;
						break;
					case 15:
						$("#gift_name").html("深情比金坚");
						$("#popularity").html("＋200");
						$("#effect").html("戴上了新项圈，跑到镜子前臭美去了~~");
						gift_id=1403;
						break;				
					default:
						$("#gift_name").html("宇宙第一萌");
						$("#popularity").html("＋240");
						$("#effect").html("戴上了皇冠，英姿飒爽的叫了一声！");
						gift_id=1404;
						break;
						
				}

			}

			$(".text").hide();
			$(".s_btn1").show();
			$("#chance").show();
	    }

	    /*PC拟摇一摇动作*/
	    var shake1= document.getElementById("text");
	    shake1.onclick=function(){
			doResult();
	    }

	    /*再摇一次*/
	    $("#again").click(function(){

			var b=parseInt($("#chance_times").html());
			b--;
			if(b>=0){
				$("#chance_times").html(b);
				flag="true";

				$(".container1").hide();
				$(".s_btn1").hide();
				$("#chance").hide();

				$(".container").show();
				$(".text").show();

			}
			else{

				/*今天的次数用完了*/
				$(".container1").hide();
				$(".s_btn1").hide();
				$("#chance").hide();
				$(".container").show();
				$("#shake").attr("src","css/images/no_choice.png");
				$(".none").show();
				$(".shareList").show();
				$(".prompt").show();

			}

		})
	}

	/*送礼物*/
	$("#send").click(function(){
		$(".gift_tit").hide();
		$(".s_btn1").hide();
		$("#chance").hide();
		$(".gift_con").show();
		$("#gift_img").attr("src","css/images/happy.png");
		$(".shareList").show();
		$(".prompt").show();
		$.ajax({
                    url: <?php echo "'".$this->createUrl('animal/shakeMobileApi', array('aid'=>$aid, 'is_shake'=>1, 'SID'=>$SID))."'" ?>,
                    data: { },
                    type: "get",
                    success: function (data) {
                    }
                });
		location.href = <?php echo "'".$this->createUrl('social/shake', array('aid'=>$aid, 'SID'=>$SID))."'" ?>
	});
	
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
        title: "摇一摇，手不酸了~",
        desc: "你有事儿么？没事摇一摇~",
        link: "http://"+window.location.host+"/index.php?r=social/shake&aid="+<?php echo $aid?>, 
        imgUrl: "http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>",
        success: function () { 
       		location.href = <?php echo "'".$this->createUrl('animal/shakeMobileApi', array('aid'=>$aid, 'SID'=>$SID))."'" ?>
    	},
    	cancel: function () { 
       	// 用户取消分享后执行的回调函数
    	}
    };
    wx.onMenuShareAppMessage(shareData);
    wx.onMenuShareTimeline(shareData);
    wx.onMenuShareQQ(shareData);
    wx.onMenuShareWeibo(shareData);
  });
</script>


</html>
