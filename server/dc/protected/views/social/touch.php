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
        <title>摸一摸</title>
	<link rel="stylesheet" type="text/css" href="css/eraser.css">
</head>
<body>
<div class="comWidth">
	<div class="wraper">
		<div class="head clearfix">
			<div class="img"><img src="http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>" id="head"></div>
			<div>摸一摸 <span><?php echo $r['name']?></span></div>
		</div>
		<div class="box">
			<div class="container">
				<img src="css/images/no_choice.png" class="no_choice"/>
				<img id="robot" src="http://<?php echo OSS_PREFIX?>4upload.oss-cn-beijing.aliyuncs.com/<?php echo $img_url?>" />
				<img id="redux" src="css/images/eraser.png" />
			</div>
		</div>
		
		<p class="happy"><span><?php echo $r['name']?></span>很开心</p>
		<p class="popularity">人气＋10 </p>
		<p class="text">每天摸一摸，人气涨的高～</p>
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
	<div class="prompt">每天第一次成功分享后，可以再摸1次喔～</div>
	<div class="e_btn">
		<a href="#" id="e_btn">我也来摸</a>
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
	<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
	<script src='js/jquery.eraser.js' type='text/javascript'></script>
	<script type = "text/javascript">

	$(window).on('load',function(){
		
		$("#e_btn").click(function(){
			if (<?php echo !isset($SID)&&$SID!=''?1:0?>) {
				var img_id = <?php echo $img_id ?>;
				var img_url = <?php echo $img_url ?>;
    			var aid =<?php echo $r['aid'] ?>;
    			location.href = <?php echo "'".$this->createUrl('animal/touchMobileApi')."'" ?>+'&img_url='+img_url+'&aid='+aid+'&img_id='+img_id+'&SID='+<?php echo "'".$SID."'" ?>;
			}
			$("#e_btn").hide();
			function init( event ) {
				$("#redux").eraser();
				// you can alse specify the brush size (in pixel) by using options :
				 //$("#redux").eraser({size: 100});
			}

			function remove(event) {
				$("#redux").eraser('clear');
				//event.preventDefault();
			}

			function reset(event) {
				$("#redux").eraser('reset');
				event.preventDefault();
			}

			function grow(event) {
				$("#redux").eraser("size",200);
				event.preventDefault();
			}


			$('#redux').eraser( {
			    completeRatio: .6,
			    completeFunction: showResetButton
			});


			function showResetButton(){
				/*alert("111");*/
				if (<?php echo $chance_times?>) {
					$(".text").hide();
					/*$(".happy").show();*/
					$(".happy").fadeIn("500");
					$(".popularity").fadeIn("500");
					$(".shareList").show();
					$(".prompt").show();
					$.ajax({
                    url: <?php echo "'".$this->createUrl('animal/touchApi', array('aid'=>$r['aid'], 'SID'=>$SID))."'" ?>,
                    data: { },
                    type: "get",
                    success: function (data) {
                    }
				} else {
					/*今天的次数用完*/
					$("#robot").hide();
					$("#redux").hide();
					$(".happy").hide();
					$(".popularity").hide();
					$(".container").attr("style","border:none");
					$(".no_choice").show();
					$(".none").fadeIn("500");
				}
			}
			
		})
	})
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
        title: "摸一摸，屏幕清晰~",
        desc: "我在宠物星球摸了摸萌星"+<?php echo $r['name']?>+"，软软哒真可爱~~舍不得洗手了呢嘤嘤嘤",
        link: "http://"+window.location.host+"/index.php?r=social/touch&aid="+<?php echo $r['aid']?>, 
        imgUrl: "http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>",
        success: function () { 
       		location.href = <?php echo "'".$this->createUrl('animal/shakeMobileApi', array('aid'=>$r['aid'], 'img_url'=>$img_url, 'img_id'=>$img_id, 'SID'=>$SID))."'" ?>
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
