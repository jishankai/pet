<?php
require_once "jssdk.php";
$jssdk = new JSSDK(WECHAT_MP_ID, WECHAT_MP_SECRET);
$signPackage = $jssdk->GetSignPackage();
?>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>我为自己代粮</title>
<script src="http://libs.baidu.com/jquery/1.9.0/jquery.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
<link type="text/css" rel="stylesheet" href="css/alert.css"/>
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
<div class="comWidth">
	<div class="food_title">
    <!-- <h3>宠物星球<i>-我为自己代粮</i></h3> -->
    <h3>请在浏览器中打开</h3>
  </div>
  <div class="logo">
    <!-- <img src="css/images/r_logo.png" alt=""/> -->
    <a href="http://home4pet.aidigame.com"><img src="css/images/load.jpg" alt=""/></a>
  </div>
	<div class="food_body">
		<div class="info">
        <img src="http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>" alt="" class="ph_m"/>
			<div class="info_tit">
            <h3><?php echo $r['name']?></h3>
				<img src="css/images/man_icon.jpg">
			</div>
			<div class="info_con">
            <h3 class="info_con1"><?php echo $a_type?></h3>
            <h3 class="info_con2"><?php echo $r['u_name']?></h3>
			</div>
		</div>
        <img src="http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_usr/<?php echo $r['u_tx']?>" alt="" class="ph_s"/>
      <div class="info_photo">
          <img src="http://<?php echo OSS_PREFIX?>4upload.oss-cn-beijing.aliyuncs.com/<?php echo $r['url']?>" alt="banner" class="active"/>
      </div>
      <?php if($r['is_food']) {?>
			<div class="details clearfix">
	        	<ul class="about_food">
	            	<li  class="received"><div>已收到</div></li>
                    <li ><div class="food_num" id="food_num"><?php echo $r['food']?></div></li>
	                <li ><div class="time_img" id="time_img"></div></li>
	            </ul>
        	</div>
          <?php }?>
        	<div class="black">
        	</div>
        	<div class="come_from">
            <h3><?php echo $r['cmt']?></h3>
        	</div>
      </div>
          <div class="tab_wraper">      
            <div id="tabs">
              <ul class="tab_tit clearfix">
                <li class="on"><img src="css/images/show_topic_like_red.png">&nbsp;<span><?php echo $r['likes']?></span></li>
                <li><img src="css/images/show_topic_gift_red.png">&nbsp;<span><?php echo $r['gifts']?></span></li>
                <li><img src="css/images/show_topic_share_red.png">&nbsp;<span><?php echo $r['shares']?></span></li>
                <li id="a1"><img src="css/images/show_topic_comment_red.png">&nbsp;<span><?php echo 0?></span></li>
              </ul>
              <div id="like">
               <ul class="tab_con clearfix" id="like_list">
              </ul>    
            </div>
            <div class="hide" id="gift">
              <ul class="tab_con clearfix" id="gift_list">
              </ul>        
            </div>
            <div class="hide" id="comment">
              <ul class="tab_con clearfix" id="comment_list">
              </ul> 
            </div>
            <div class="hide" id="share">

              <ul class="tab_con comment" id="share_list">
              </ul>
              
            </div> 

          </div>

        </div>

  <div class="hr"></div>
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
			<div class="target"></div>
		</div> -->
		<div class="give_sc" id="give_sc">
            <h3>打赏成功～萌星<span><?php echo $r['name']?></span>感谢您的关爱</h3>
		</div>
    <?php if($r['is_food']) {?>
		<div class="give_btn">
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
		</div>
    <?php }?>
		<!-- <div class="load">
			
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
				<a href="https://home4pet.aidigame.com" target="_blank">
                    <img src="css/images/load.jpg"/>
                </a>
			</div>
		</div> -->
	</div>
	
</div>
</body>

<script type="text/javascript" src="js/link.js"></script>
<script type="text/javascript" src="js/jquery.md5.js"></script>
<script type="text/javascript" src="js/alert.js"></script>
<script type="text/javascript">
/*倒计时*/
$(window).on('load',function(){
    FreshTime();
    if (<?php echo $alert_flag?>) {
      cc();
    };
    /* 
    if(dataInt!=null){
      if(dataInt.name!=null){
        for(var i=0;i<dataInt.name.length;i++){

          var oLi_like=$('<li>').appendTo($('#like_list'));
          var oSpan_like=$('<span>').addClass('left').appendTo($(oLi_like));
          var oImg_like=$('<img>').attr('src','css/images/'+$(dataInt.head[i]).attr('src')).appendTo($(oSpan_like));
          var oH3_like=$('<h3>').addClass('left').html($(dataInt.name[i]).attr('name')).appendTo($(oLi_like));
        }
      }
      if(dataInt.name1!=null){
        for(var i=0;i<dataInt.name1.length;i++){
          var oLi_gift=$('<li>').appendTo($('#gift_list'));
          var oSpan_gift=$('<span>').addClass('left').appendTo($(oLi_gift));
          var oImg_gift=$('<img>').attr('src','css/images/'+$(dataInt.head1[i]).attr('src')).appendTo($(oSpan_gift));
          var oH3_gift=$('<h3>').addClass('left').html($(dataInt.name1[i]).attr('name')).appendTo($(oLi_gift));
        }
      }
      if(dataInt.name2!=null){
        for(var i=0;i<dataInt.name2.length;i++){
          var oLi_comment=$('<li>').appendTo($('#comment_list'));
          var oSpan_comment=$('<span>').addClass('left').appendTo($(oLi_comment));
          var oImg_comment=$('<img>').attr('src','css/images/'+$(dataInt.head2[i]).attr('src')).appendTo($(oSpan_comment));
          var oH3_comment=$('<h3>').addClass('left').html($(dataInt.name2[i]).attr('name')).appendTo($(oLi_comment));
        }
      }
      if(dataInt.name3!=null){
        for(var i=0;i<dataInt.name3.length;i++){
          var oLi_share=$('<li>').appendTo($('#share_list'));
          var oSpan_share=$('<span>').addClass('left').appendTo($(oLi_share));
          var oImg_share=$('<img>').attr('src','css/images/'+$(dataInt.head3[i]).attr('src')).appendTo($(oSpan_share));
          var oDiv_share=$('<div>').addClass('left').appendTo($(oLi_share));
          var oP_share=$('<p>').appendTo($(oDiv_share));
          var oB_share=$('<b>').html($(dataInt.name3[i]).attr('name')).appendTo($(oP_share));
          var oSpanTime_share=$('<span>').addClass('time').addClass('right').html($(dataInt.time1[i]).attr('time')).appendTo($(oP_share));
          var oP2_share=$('<p>').html($(dataInt.data[i]).attr('data')).appendTo($(oDiv_share));
        }
      }
}*/
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
       
        document.getElementById("time_img").innerHTML=h+":"+m+":"+s;
        if(lefttime<=0){
        document.getElementById("time_img").innerHTML="已结束";
        }
        setTimeout(FreshTime,1000);
}

$("#reward").click(function(){
  if ($("#time_img")[0].innerHTML!="已结束") {
    var b=parseInt($(".food_num").html());
    var c=parseInt($("#g_num").html());
    var n=c;
    var img_id = <?php echo $img_id ?>;
    var to = <?php echo "'".$to."'" ?>;
    var aid =<?php echo $aid ?>;
    var sig =$.md5('aid='+aid+'&img_id='+img_id+'&n='+n+'&to='+to+'dog&cat');
    location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&img_id='+img_id+'&n='+n+'&to='+to+'&sig='+sig+'&SID='+<?php echo "'".$sid."'" ?>;
  }
        	/*alert(data);*/
        //     var b=parseInt($(".food_num").html());

        //     var left = parseInt($('.food_num').position().left-80), top =  parseInt($('.food_num').position().top-40), obj=$('.food_num');
        // 	if(data==1){
        // 		$('.food_num').append('<div id="zhan"><img src="css/images/food.png" width="29px" height="28px"><b>+1</b></div>');
        // 		}
       	// 	 else if(data==10){
        //    		 $('.food_num').append('<div id="zhan"><img src="css/images/food.png" width="29px" height="28px"><b>+10</b></div>');
        // 		}
        // 	else if(data==100){
        //    		 $('.food_num').append('<div id="zhan"><img src="css/images/food.png" width="29px" height="28px"><b>+100</b></div>');
        // 		}
        // 	else if(data==1000){
        //     	$('.food_num').append('<div id="zhan"><img src="css/images/food.png" width="29px" height="28px"><b>+1000</b></div>');
        // 		}
       	// 	$('.food_num').remove("#zhan");

        // 	$('#zhan').css({'position':'absolute','z-index':'1','color':'#C30','left':left+'px','top':top+'px','font-size':'24px'});
        // 	$('#zhan').animate({top:top-40,opacity: 0},1000,
        // 	function(){
        //     	$(this).fadeOut(100).remove();
        //     	var Num = parseInt(obj.text());
        //        	Num=Num+c;
        //        	obj.text(Num);
        // 	});
        // 	aa();


        // });
	

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
        title: "轻轻一点，免费赏粮！<?php echo $r['name']?>的口粮就靠你啦~",
        desc: "努力卖萌，只为自己代粮！快把你每天的免费粮食赏给我~",
        link: "http://"+window.location.host+"/index.php?r=social/foodShareApi&img_id=<?php echo $img_id?>", 
        imgUrl: "http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>"
    };
    wx.onMenuShareAppMessage(shareData);
    wx.onMenuShareTimeline(shareData);
    wx.onMenuShareQQ(shareData);
    wx.onMenuShareWeibo(shareData);
  });
</script>
</html>
