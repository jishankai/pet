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
	<link type="text/css" rel="stylesheet" href="css/alert.css"/>
<script>
//  var _hmt = _hmt || [];
//  (function() {
//    	var hm = document.createElement("script");
//      hm.src = "//hm.baidu.com/hm.js?fffd5628b5c5fe81d7a7867d554d07ca";
//      var s = document.getElementsByTagName("script")[0]; 
//      s.parentNode.insertBefore(hm, s);
//   })();
</script>
</head>
<body>
<div class="wrap">
	<div class="top">
		<a href="<?php echo $this->createUrl('animal/infoShare', array('aid'=>$r['aid'], 'SID'=>$SID))?>"><img src="http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>" alt="" class="pet_head"/></a>
		<!-- <img src="css/images/base_head13.jpg" class="pet_head"/> -->
		<span class="pet_name"><?php echo $r['name']?></span>
		<!-- <span class="pet_name">我是来钱儿思密达</span> -->
	</div>
	<div class="main_body">
		<!-- 图片＋粮食＋倒计时 -->
		<div class="pet_img_box">
			<img src="http://<?php echo OSS_PREFIX?>4upload.oss-cn-beijing.aliyuncs.com/<?php echo $r['url']?>" alt="banner" class="pet_img"/>
			<!-- <img src="css/images/pet8.jpg" class="pet_img"/> -->

			<!-- <div class="food_time clearfix">
				<div class="food_box left">
					<img src="css/images/food_border_white.png" id="food_heart"/>
					<span id="food">12</span>
				</div>
				<div class="d_time_box right">
					<img src="css/images/time_border_white.png"/>
					<span id="d_time">12:44:06</span>
				</div>
			</div> -->
			<?php if($r['is_food']) {
            echo '<div class="food_time clearfix" id="f_condition">';
	           	echo '<div class="food_box left">';
	           		echo '<img src="css/images/food_white_border.png" id="food_heart"/>';
	           		echo '<span id="food">'.$r['food'].'</span>';
	           	echo '</div>';
	           	echo '<div class="d_time_box right">';
	           		echo '<img src="css/images/time_white_border.png"/>';
	           		echo '<span id="d_time">12:44:06</span>';
	           	echo '</div>';
	        echo '</div>';
           	}?>
			<!-- 推荐情况 (改成推荐时的数据)-->
           	<?php if($r['star_id']!=0) {
            echo '<div class="food_time clearfix" id="r_condition">';
	           	echo '<div class="food_box left">';
	           		echo '<img src="css/images/heart_orange_small.png" id="food_heart"/>';
	           		echo '<span id="t_num">'.$r['stars'].'</span>';
	           	echo '</div>';
	           	echo '<div class="d_time_box right">';
	           		echo '<img src="css/images/time_white_border.png"/>';
	           		echo '<span id="td_time">12:44:06</span>';
	           	echo '</div>';
	        echo '</div>';
           	}?>



		</div>
		<!-- 话题和描述 -->
		<div class="topic_describe">
			<span class="topic">＃<?php echo $r['topic_name']?>＃</span>
			<span class="describe"><?php echo $r['cmt']?></span>
			<!-- <span class="describe">好饿 好饿 好饿</span> -->
		</div>
		<!-- 点赞，评论，礼物。。按钮组 -->
		<ul class="btns_box clearfix">
			<li>
				<img src="css/images/page_like.png" id="like_img"/>
				<span id="like"><?php if($is_liked) {?>已赞<?php } else {?>赞<?php }?></span>
			</li>
			<li id="comment_btn">
				<img src="css/images/page_comment.png" id="comment_img"/>
				<span id="comment">评论</span>
			</li>
			<li>
				<a href='<?php echo $this->createUrl('social/gift',array('aid'=>$r['aid'],'SID'=>$SID))?>'>
				<img src="css/images/icon_gift.png" id="gift_img"/>
				</a>
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
				<?php for($i=0;$i<count($liker_tx)&&$i<7;$i++) {?>
				<li><img src="http://<?php echo OSS_PREFIX;?>4tx.oss-cn-beijing.aliyuncs.com/tx_usr/<?php echo $liker_tx[$i]['tx'];?>"></li>
				<?php }?>
			</ul>
			<span id="like_num"><?php echo $r['likes']?></span>
			<!-- <span id="like_num">960</span> -->
		</div>
		<!-- 评论情况展示 -->
		<div class="comment_box">
			<div class="comment_more">
				<img src="css/images/page_comment_grey.jpg">
				<a class="the_more_comment">查看所有<span id="comment_num"><?php echo $r['comment_count']?></span>条评论</a>
			</div>
			<ul class="comment_list clearfix">
				<?php foreach($r['comments'] AS $c) {?>
				<li>
					<!-- <img src="css/images/base_head13.jpg"/> -->
					<span><?php echo $c['name'];?></span>
					<p><?php echo $c['body'];?></p>
				</li>
				<?php }?>
			</ul>
		</div>
	</div>
	
	<div class="float giveBtn_box">
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
	</div>
	<div class="recommend_box float">
		<div class="recommend">
			<img id="recommend_btn" src="css/images/bt_heart.png"/>
			<img id="act_jump_btn" src="css/images/activity_jump.png"/>
		</div>

	</div>


	<form class="comment_alert" action="<?php echo $this->createAbsoluteUrl('image/commentApi', array('sig'=>md5('dog&cat')))?>">
		<h3>说点什么</h3>
		<input type="submit" id="submit" value=""/>
		<input type="button" id="close"/>
		<textarea></textarea>
        <input type="hidden" name="img_id" value="<?php echo $img_id?>" />
	</form>

</div>
</body>
<script type="text/javascript" src="js/jquery-1.11.1.js"></script>

<script type="text/javascript">
/*头像尺寸设置*/
	var iWidth=$(".like_head img").width();
	$(".like_head img").height(iWidth);
$(function(){
	
	/*评论弹框*/
	$("#comment_btn").click(function(){
		$(".comment_alert").show();
	});
	$("#close").click(function(){
		$(".comment_alert").hide();
	});

	//查看所有评论
	$(".the_more_comment").click(function(){


			$.ajax({
				url:"",
				dataType:"json",
				success:function(data){
					for(var i=0;i<data.length;i++){
							$(".comment_list").append("<li><span>"+"data.name"+"</span><p>"+"data.say"+"</p></li>");
						}
				}
			});
		
	});

	/*推荐按钮*/
	$("#recommend_btn").click(function(){
		var votes=<?php echo $votes?>;
		var t = $("#td_time").html();
		if(votes>0 && t!="已结束"){
			$.ajax({
				url: <?php echo "'".$this->createUrl('star/voteApi', array('img_id'=>$img_id, 'sig'=>md5('img_id='.$img_id.'dog&cat'), 'SID'=>$SID))."'" ?>,
				data: { },
				type: "get",
				success: function (data) {
					var t_num=parseInt($("#t_num").html());
					$("#t_num").html(t_num+1);
					votes--;
				}
			}else{
				recommend();
			}

		
			
	});
	/*Go活动界面*/
	$("#act_jump_btn").click(function(){
		window.location="";
	});

	/*赞按钮*/
		$("#like_img").click(function(){
			var like = $("#like").html();

			if(like=="赞"){
				$.ajax({
                    url: <?php echo "'".$this->createUrl('image/likeApi', array('img_id'=>$img_id, 'sig'=>md5('img_id='.$img_id.'dog&cat'), 'SID'=>$SID))."'" ?>,
                    data: { },
                    type: "get",
                    success: function (data) {
                    }
                });
				document.getElementById("like_img").src="css/images/page_liked.png";
				$("#like").html("已赞");
				var like_num = parseInt($("#like_num").html());
				$("#like_num").html(like_num+1);
			}
		});

	//依情况改变页面样式   默认为普通（除1和2外的其它值）1.可以赏口粮 2.可以推荐
	var flag = <?php if ($r['star_id']!=0) { echo 2; } else if ($r['is_food']==1) { echo 2; } else { echo 3; }?>;
	if(flag==1){
			$(".recommend_box").css("display","none");
			$(".giveBtn_box").css("display","block");
			$("#r_condition").css("display","none"); 
			$("#f_condition").css("display","block");
		}else if(flag==2){
			$(".giveBtn_box").css("display","none");
			$(".recommend_box").css("display","block");
			$("#f_condition").css("display","none");
			$("#r_condition").css("display","block");
		}else{
			$(".giveBtn_box").css("display","none");
			$(".recommend_box").css("display","none");
			$(".food_time").css("display","none");
		}


/*以前food页代码移入*/

/*倒计时*/

	if (<?php echo $r['is_food']?>) {
		FreshTime();
	}
	if (<?php echo $r['star_id']!=0?>) {
		FreshTime1();
	}
	if (<?php echo $alert_flag?>) {
		cc();
	};

	    

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

/*推荐倒计时*/
function FreshTime1(){
        var endtime = <?php echo $r['create_time']?>+60*60*24;//结束时间
        var nowtime = new Date();//当前时间

        var lefttime= parseInt(endtime-(nowtime.getTime())/1000); 
        mm= parseInt(lefttime/3600/24/30);//月
        d=  parseInt(lefttime/3600/24);//天
        h=  parseInt(lefttime/(60*60)%24);//小时
        m=  parseInt(lefttime/(60)%60);//分钟
        s=  parseInt(lefttime%60);//秒

        if(d>0){
        	document.getElementById("td_time").innerHTML=d+"天"+h+"小时";
        }
        else if(s>0){
        	document.getElementById("td_time").innerHTML=h+":"+m+":"+s;
        }
        else{
        	document.getElementById("td_time").innerHTML="已结束";
        }


    }

$("#reward").click(function(){
	if ($("#d_time")[0].innerHTML!="已结束") {
	    var b=parseInt($("#food").html());
	    var c=parseInt($("#g_num").html());
	    var n=c;
	    var img_id = <?php echo $img_id ?>;
	    var to = <?php echo "'".$to."'" ?>;
	    var aid =<?php echo $aid ?>;
	    var sig =$.md5('aid='+aid+'&img_id='+img_id+'&n='+n+'&to='+to+'dog&cat');
	    location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&img_id='+img_id+'&n='+n+'&to='+to+'&sig='+sig+'&SID='+<?php echo "'".$SID."'" ?>;
	  } else {
	     var btn=$(".give_btn");
	         btn.attr("display","none");
	  }
        
	
 });

/*选金币效果*/
	$(".give_left").click(function () {

		$("#gold").toggle();
	});

	$("#one").click(function(){
		var x=$(this).html();
		$("#g_num").html(x);
		$("#gold").hide();
	})
	$("#one1").click(function(){
		var x=$(this).html();
		$("#g_num").html(x);
		$("#gold").hide();
	})
	$("#one2").click(function(){
		var x=$(this).html();
		$("#g_num").html(x);
		$("#gold").hide();
	})

	$("#one3").click(function(){
		var x=$(this).html();
		$("#g_num").html(x);
		$("#gold").hide();
	})



});

/*推荐弹框*/
function recommend(){
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
		recommend_alert.innerHTML = "<h3>今天的免费投票次数用完了</h3><p>明天还有免费机会，再来哟~</p><div id='rec_btn_yes'>好的!</div>";

		/*<h3>今天的免费投票次数用完了</h3>
		<p>明天还有免费机会，再来哟！</p>
		<div id='rec_btn_yes'>好的!</div>*/

		document.body.appendChild(recommend_alert);
		var rHeight = recommend_alert.offsetHeight;
		var rWidth = recommend_alert.offsetWidth;

		recommend_alert.style.left = (sWidth-rWidth)/2 + "px";
		recommend_alert.style.top = (wHeight-rHeight)/2 + "px";

		var rec_btn_no = document.getElementById("rec_btn_yes");
		rec_btn_no.onclick = function(){
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





















