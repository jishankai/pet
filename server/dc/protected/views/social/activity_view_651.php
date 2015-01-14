<?php
require_once "jssdk.php";
$jssdk = new JSSDK(WECHAT_MP_ID, WECHAT_MP_SECRET);
$signPackage = $jssdk->GetSignPackage();
?>
<!DOCTYPE>
<html xmlns:wb="http://open.weibo.com/wb">
	<head>
		<meta name="viewport" content="width = device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>香山喵年夜饭 大！募！集！</title>
		<link rel="stylesheet" type="text/css" href="css/activity_view.css">
		<link rel="stylesheet" type="text/css" href="css/alert.css">
		<script src="js/zepto.min.js"></script>
        <script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js?appkey=1950643552" type="text/javascript" charset="utf-8"></script>
	</head>
    <script>
        var _hmt = _hmt || [];
        (function() {
          var hm = document.createElement("script");
          hm.src = "//hm.baidu.com/hm.js?fffd5628b5c5fe81d7a7867d554d07ca";
          var s = document.getElementsByTagName("script")[0]; 
          s.parentNode.insertBefore(hm, s);
        })();
    </script>
<body>
<div class="act2_wrap comWidth">
	<div class="act2_top">
		<img src="css/images/head_background.jpg" alt="" class="act2_head">
		<div class="other_story">
			<a href="http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=202818856&idx=4&sn=16fd774ac39ab9cd94ddfa6c2fe6d6c9#rd"><img src="css/images/other_story.png" alt=""></a>
		</div>
        <div class="wb">
            <!-- <wb:publish toolbar="face,topic" button_type="red" button_size="small" id="wb" default_text="#暖冬季•年夜饭计划#北京香山公园的流浪喵还差<?php echo 495000-$food?>份口粮才能温暖过冬呢~轻轻一点，免费捐粮！快来和我一起做爱心小天使~" button_text="暖冬季•年夜饭计划" default_image="http://kouliang.tuturead.com/css/images/a2.jpg" ></wb:publish> -->
        </div>
        <div class="the_more">
			<a href="http://release4pet.aidigame.com/index.php?r=social/newYearEvent"><img src="css/images/the_more.png"></a>
		</div>
		<div class="introduce">
			<div class="pet_name">代粮喵：<span>16</span></div>
			<div class="pet_place">代粮地点：<span>北京香山公园</span></div>
			<div class="pet_weibo"><a href="http://weibo.com/u/1678351712?topnav=1&wvr=6&topsug=1">新浪微博：<span><u>十四阙</u></span></a></div>
		</div>
		<div class="act2_petHead">
			<img  alt="" src="css/images/h2.png">

		</div>

		<div class="act2_range">
			<div class="hr_10"></div>
			<div class="target_Box clearfix">
				<div class="target">目标<span class="target_num" id="target_num">495000</span>还剩 <span class="target_num" id="target_num"><?php echo 495000-$food ?></span> 份口粮</div>
				<div class="d_time" id="d_time"></div>
			</div>
			<div class="hr_10"></div>
			<div class="aprogressbar">
				<div class="abar" id="abar" style="width:<?php echo sprintf("%.2f",$food*100/495000)?>%"></div>
			</div>
		</div>
		<div class="zhu">注：连续登陆天数越长，能免费捐助的份数就越多哦！</div>
		<img src="css/images/dashed.png" alt="" class="dashed">
	</div>
	<div class="act2_body">
		<div class="body_tit">活动照片</div>
		<ul class="act2_imgBox clearfix" id="act2_imgBox">
			<li ><img src="css/img/xs/1.jpg" alt="" name="1"></li>
			<li ><img src="css/img/xs/2.jpg" alt="" name="2"></li>
			<li ><img src="css/img/xs/3.jpg" alt="" name="3"></li>
			<li ><img src="css/img/xs/4.jpg" alt="" name="4"></li>
			<li ><img src="css/img/xs/5.jpg" alt="" name="5"></li>
			<li ><img src="css/img/xs/6.jpg" alt="" name="6"></li>
			<li ><img src="css/img/xs/7.jpg" alt="" name="7"></li>
			<li ><img src="css/img/xs/8.jpg" alt="" name="8"></li>
			<li ><img src="css/img/xs/9.jpg" alt="" name="9"></li>
		</ul>
		<div class="hr_100"></div>
	</div>

	<div class="float">
		<!-- <div class="select_money_t" id="gold">
			<div class="select_money">
				<ul id="gold_num">
					<li id="one">1000</li>
					<li id="one1">100</li>
					<li id="one2">10</li>
					<li id="one3">1</li>
				</ul>
			</div>
			<div class="target_3"></div>
		</div> -->
		<div class="give_btn">
			<div class="give_btn_bottom">
				<div class="give_btn_con">
					<div class="give_left" >
						<h3 id="g_num">1</h3>
					</div>
					<div class="give_m"></div>

					<div class="give_right">
						<img src="css/images/reword_btn.gif" id="reward" />

					</div>
				</div>
			</div>
		</div>
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

<!-- 大图显示 -->

	<div class="large animated fadeInDown" id="large_container" style="display:none">

		<div class="hr_10"></div>
		<div class="bigImg_btn">
			<span id="s_one">上一张</span>
			<span id="close_one">关闭</span>
			<span id="x_one">下一张</span>
		</div>
		<div class="large_imgBox">
			<img id="large_img" name="">
			<a href="#" class="close" id="close"><img src="css/images/img_close.png"></a>
			<div class="close_bigImg" id="close_bigImg" onmouseover="closeShow()" onmouseout="closeHide()"></div>
			<a href="#"  id="left" ><img src="css/images/img_left.png"></a>
			<div class="left_bigImg" id="left_bigImg" onmouseover="leftShow()" onmouseout="leftHide()"></div>
			<a href="#"  id="right"><img src="css/images/img_right.png"></a>
			<div class="right_bigImg" id="right_bigImg" onmouseover="rightShow()" onmouseout="rightHide()"></div>
		</div>


	</div>


</body>
<script src="js/activity_view.js"></script>
<script src="js/alert.js"></script>
<script src="js/md5.js"></script>
<script type="text/javascript">
	var zWin = $(window);

 $('#act2_imgBox img').tap(function(){  

        $('#large_container').css({
            width:zWin.width(),
            height:zWin.height()
        }).show();

        var a=document.getElementById("large_img");
        a.src = this.src;
        a.name = this.name;

    });

/*触屏时单击关闭*/
 $('#close_one').click(function(){
        $('#container').css({height:'auto','overflow':'auto'})
        $('#large_container').hide();
    });
 $('#close_bigImg').click(function(){
        $('#container').css({height:'auto','overflow':'auto'})
        $('#large_container').hide();
    });
  

/*浏览器中单击右图标*/
$('#x_one').tap(function(){
    var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x+1;
        if(y>9){
            y=9;
        }
        a.src="css/img/xs/"+y+".jpg"; 
        a.name=y;     
});
$('#right_bigImg').click(function(){
    var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x+1;
        if(y>9){
            y=9;
        }
        a.src="css/img/xs/"+y+".jpg"; 
        a.name=y;     
});


/*浏览器中单击左图标*/
$('#left_bigImg').click(function(){
    var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x-1;
        if(y<1){
            y=1;
        }
        a.src="css/img/xs/"+y+".jpg";
        a.name=y;       
});
$('#s_one').tap(function(){
    var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x-1;
        if(y<1){
            y=1;
        }
        a.src="css/img/xs/"+y+".jpg";
        a.name=y;       
});

/*浏览器中单击，实现图片放大*/
$('#act2_imgBox img').click(function(){  

        $('#large_container').css({
            width:zWin.width(),
            height:zWin.height()
        }).show();
        
        var a=document.getElementById("large_img");
        a.src = this.src;
        a.name = this.name;

    });




/*浏览器版大图*/
function closeShow(){
    var left=document.getElementById("close");
   
    left.style.display="block";
}
function closeHide(){
    var left=document.getElementById("close");
   
    left.style.display="none";
}

function leftShow(){
    var left=document.getElementById("left");
   
    left.style.display="block";
}
function leftHide(){
    var left=document.getElementById("left");
   
    left.style.display="none";
}
function rightShow(){
    var left=document.getElementById("right");
   
    left.style.display="block";
}
function rightHide(){
    var left=document.getElementById("right");
   
    left.style.display="none";
}
/*赏按钮（进度条相关）*/
window.onload=function(){
    FreshTime();
    if (<?php echo $alert_flag?>) {
        cc();
    };
}
$("#reward").click(function(){
var b=parseInt($("#target_num").html());
var c=parseInt($("#g_num").html());

var n=c;
var aid =<?php echo $aid ?>;
var sig =md5('aid='+aid+'&n='+n+'dog&cat');
location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&n='+n+'&sig='+sig+'&SID='+<?php echo "'".$sid."'" ?>;
});
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
        title: "香山喵年夜饭 大！募！集！",
        desc: "宠物星球已经召集"+<?php echo $users?>+"位小伙伴为香山喵募集年夜饭，下一位暖心小天使是你吗？",
        link: "http://"+window.location.host+"/index.php?r=social/activityview&aid="+<?php echo $aid?>, 
        imgUrl: "http://"+window.location.host+"/css/images/a2.jpg"
    };
    wx.onMenuShareAppMessage(shareData);
    wx.onMenuShareTimeline(shareData);
    wx.onMenuShareQQ(shareData);
    wx.onMenuShareWeibo(shareData);
  });
</script>
</html>











