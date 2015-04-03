<?php
require_once "jssdk.php";
$jssdk = new JSSDK(WECHAT_MP_ID, WECHAT_MP_SECRET);
$signPackage = $jssdk->GetSignPackage();
?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initail-scale=1.0 ,maximum-scale=1, user-scalable=no">
	<title>照片详情页－－NEW</title>
	<link rel="stylesheet" type="text/css" href="css/reset.css">
	<link rel="stylesheet" type="text/css" href="css/food_new.css">
</head>
<body>
<div class="wrap">
	<div class="top">
		<a href="<?php echo $this->createUrl('animal/infoShare', array('aid'=>$r['aid'], 'SID'=>$sid))?>"><img src="http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>" alt="" class="pet_head"/></a>
		<!-- <img src="css/images/base_head13.jpg" class="pet_head"/> -->
		<span class="pet_name"><?php echo $r['name']?></span>
		<!-- <span class="pet_name">我是来钱儿思密达</span> -->
	</div>
	<div class="main_body">
		<!-- 图片＋粮食＋倒计时 -->
		<div class="pet_img_box">
			<img src="http://<?php echo OSS_PREFIX?>4upload.oss-cn-beijing.aliyuncs.com/<?php echo $r['url']?>" alt="banner" class="pet_img"/>
			<!-- <img src="css/images/pet8.jpg" class="pet_img"/> -->

           <?php if($r['is_food']) {
            echo '<div class="food_time clearfix">';
	           	echo '<div class="food_box left">';
	           		echo '<img src="css/images/food_white_border.png"/>';
	           		echo '<span id="food">'.$r['food'].'</span>';
	           	echo '</div>';
	           	echo '<div class="d_time_box right">';
	           		echo '<img src="css/images/time_white_border.png"/>';
	           		echo '<span id="d_time">12:44:06</span>';
	           	echo '</div>';
	        echo '</div>';
           	}?>
			<!-- <div class="food_time clearfix">
				<div class="food_box left">
					<img src="css/images/food_white_border.png"/>
					<span id="food">12</span>
				</div>
				<div class="d_time_box right">
					<img src="css/images/time_white_border.png"/>
					<span id="d_time">12:44:06</span>
				</div>
			</div> -->
		</div>
		<!-- 话题和描述 -->
		<div class="topic_describe">
			<span class="topic">＃妈啊我的包子脸＃</span>
			<span class="describe"><?php echo $r['cmt']?></span>
			<!-- <span class="describe">好饿 好饿 好饿</span> -->
		</div>
		<!-- 点赞，评论，礼物。。按钮组 -->
		<ul class="btns_box clearfix">
			<li>
				<img src="css/images/page_like.png" id="like_img"/>
				<span id="like">已赞</span>
			</li>
			<li id="comment_btn">
				<img src="css/images/page_comment.png" id="comment_img"/>
				<span id="comment">评论</span>
			</li>
			<li>
				<img src="css/images/icon_gift.png" id="gift_img"/>
				<span id="gift">礼物</span>
			</li>
			<li>
				<img src="css/images/bt_more.png"/>
			</li>
		</ul>
		<!-- 点赞头像显示 -->
		<div class="like_box clearfix">
			<div class="like_grey">
				<img src="css/images/page_comment_gray.jpg">
			</div>
			<ul class="like_head clearfix">
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
			</ul>
			<span id="like_num"><?php echo $r['likes']?></span>
			<!-- <span id="like_num">960</span> -->
		</div>
		<!-- 评论情况展示 -->
		<div class="comment_box">
			<div class="comment_more">
				<img src="css/images/page_comment_grey.jpg">
				<a class="the_more_comment">查看所有<span id="comment_num"><?php echo 0?></span>条评论</a>
				<!-- <a class="the_more_comment">查看所有<span id="comment_num">35</span>条评论</a> -->
			</div>
			<ul class="comment_list clearfix">
				<li>
					<img src="css/images/base_head13.jpg"/>
					<p>姑娘，胸毛不错～</p>
				</li>
				<li>
					<img src="css/images/base_head13.jpg"/>
					<p>姑娘，胸毛不错～</p>
				</li>
			</ul>
		</div>
	</div>

	<div class="float">
		<div class="select_money_t" id="gold">
			<div class="select_money">
				<ul id="gold_num">
					<li id="one">1000</li>
					<li id="one1">100</li>
					<li id="one2">10</li>
					<li id="one3">1</li>
				</ul>
			</div>
			<div class="target"></div>
		</div>

		<?php if($r['is_food']) {
			echo '<div class="give_btn">';
				echo '<div class="give_btn_bottom">';
					echo '<div class="give_btn_con">';
						echo '<div class="give_left" >';
							echo '<h3 id="g_num">1</h3>';
						echo '</div>';
						echo '<div class="give_m"></div>';
						echo '<div class="give_right">';
							echo '<img src="css/images/reword_btn.gif" id="reward"/>';
						echo '</div>';
					echo '</div>';
				echo '</div>';
			echo '</div>';
    	}?>
		<!-- <div class="give_btn" id="give_btn">
			<div class="give_btn_bottom">
				<div class="give_btn_con">
					<div class="give_left" >
						<h3 id="g_num">1</h3>
					</div>
					<div class="give_m"></div>
					<div class="give_right">
						<img src="css/images/reword_btn.gif" id="reward"/>
					</div>
				</div>
			</div>
		</div> -->
	</div>

	<form class="comment_alert">
		<h3>说点什么</h3>
		<input type="submit" id="submit" value=""/>
		<input type="button" id="close"/>
		<textarea></textarea>
	</form>

</div>
</body>
<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
<script type="text/javascript">
	/*$(function(){
		$("#comment_btn").click(function(){
			$(".comment_alert").show();
		})
		$("#close").click(function(){
			$(".comment_alert").hide();
		})
	});*/

/*倒计时*/
$(window).on('load',function(){
    if (<?php echo $r['is_food']?>) {
      FreshTime();
    }
    if (<?php echo $alert_flag?>) {
      cc();
    };
    //调整主图片位置
    var a=$(".info_photo").height();
    var b=$(".active").height();
    var c=0;
    if(a>b){
        c=Math.floor((a-b)/2);
    }
    $(".active").css("margin-top",c+"px");

    //评论框控制 
    $("#comment_btn").click(function(){
			$(".comment_alert").show();
		})
		$("#close").click(function(){
			$(".comment_alert").hide();
		});



$("#reward").click(function(){
  if ($("#d_time")[0].innerHTML!="已结束") {
    var b=parseInt($("#food").html());
    var c=parseInt($("#g_num").html());
    var n=c;
    var img_id = <?php echo $img_id ?>;
    var to = <?php echo "'".$to."'" ?>;
    var aid =<?php echo $aid ?>;
    var sig =$.md5('aid='+aid+'&img_id='+img_id+'&n='+n+'&to='+to+'dog&cat');
    location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&img_id='+img_id+'&n='+n+'&to='+to+'&sig='+sig+'&SID='+<?php echo "'".$sid."'" ?>;
  } else {
     var btn=$(".give_btn");
         btn.attr("display","none");
  }

    });

})

function FreshTime()
{
        var endtime = <?php echo $r['create_time']?>+60*60*24;//结束时间
        var nowtime = new Date();//当前时间

        var lefttime= parseInt(endtime-(nowtime.getTime())/1000); 
        h=  parseInt(lefttime/(60*60)%24);
        m=  parseInt(lefttime/(60)%60);
        s=  parseInt(lefttime%60);
        
        if(h < 10){
    		h="0"+h;
		}
		if(m<10){
			m="0"+m;
		}
		if(s<10){
			s="0"+s;
		}
       
        document.getElementById("d_time").innerHTML=h+":"+m+":"+s;
        if(lefttime<=0){
        document.getElementById("d_time").innerHTML="已结束";
        }
        setTimeout(FreshTime,1000);
}



</script>
<script type="text/javascript" src="js/link.js"></script>
<script type="text/javascript" src="js/jquery.md5.js"></script>
<script type="text/javascript" src="js/alert.js"></script>

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
        title: "轻轻一点，免费赏粮！<?php echo $r['name']?>的口粮就靠你啦~",
        desc: '<?php echo $r["cmt"]!=""?$r["cmt"]:"努力卖萌，只为自己代粮！快把你每天的免费粮食赏给我~"?>',
        link: "http://"+window.location.host+"/index.php?r=social/foodShareApi&img_id=<?php echo $img_id?>", 
        imgUrl: "http://<?php echo OSS_PREFIX?>4upload.oss-cn-beijing.aliyuncs.com/<?php echo $r['url']?>"
    };
    wx.onMenuShareAppMessage(shareData);
    wx.onMenuShareTimeline(shareData);
    wx.onMenuShareQQ(shareData);
    wx.onMenuShareWeibo(shareData);
  });
</script>
</html>





















