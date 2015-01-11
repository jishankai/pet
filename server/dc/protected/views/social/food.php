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
		<h3>宠物星球<i>-我为自己代粮</i></h3>
	</div>
	<div class="logo">
		<img src="css/images/r_logo.png" alt=""/>
	</div>
	<div class="food_body">
		<div class="info">
        <img src="http://pet4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>" alt="" class="ph_m"/>
			<img src="css/images/photo-m-t1.png" alt="" class="ph_m"/>
			<div class="info_tit">
            <h3><?php echo $r['name']?></h3>
				<img src="css/images/man_icon.jpg">
			</div>
			<div class="info_con">
            <h3 class="info_con1"><?php echo $r['type']?></h3>
            <h3 class="info_con2"><?php echo $r['u_name']?></h3>
			</div>
		</div>
        <img src="http://pet4tx.oss-cn-beijing.aliyuncs.com/tx_usr/<?php echo $r['u_tx']?>" alt="" class="ph_s"/>
			<img src="css/images/photo-s-t.png" alt="" class="ph_s"/>
			<div class="info_photo">
	        	<ul class="imgBox">
                <li><a href="#"><img src="http://pet4upload.oss-cn-beijing.aliyuncs.com/<?php echo $r['url']?>" alt="banner" class="active"/></a></li>
                    <li><a href="#"><img src="css/images/photo_d.jpg" alt="banner" /></a></li>   
                </ul>
			</div>
			<div class="details clearfix">
	        	<ul class="about_food">
	            	<li  class="received"><div>已收到</div></li>
                    <li ><div class="food_num" id="food_num"><?php echo $r['food']?></div></li>
	                <li ><div class="time_img" id="time_img"></div></li>
	            </ul>
        	</div>
        	<div class="black">
        	</div>
        	<div class="come_from">
            <h3><?php echo $r['cmt']?></h3>
        	</div>
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
			<div class="target"></div>
		</div> -->
		<div class="give_sc" id="give_sc">
			<h3>打赏成功～萌星<span>xxxx</span>感谢您的关爱</h3>
		</div>
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
				<a href="https://home4pet.aidigame.com" target="_blank">
                    <img src="css/images/load.jpg"/>
                </a>
			</div>
		</div>
	</div>
	
</div>
</body>

<script type="text/javascript" src="js/link.js"></script>
<script type="text/javascript" src="js/jquery.md5.js"></script>
<script type="text/javascript" src="js/alert.js"></script>
<script type="text/javascript">
/*倒计时*/
	window.onload=function(){
     FreshTime();
     if (<?php echo $alert_flag?>) {
        cc();
     };
    }
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

		var b=parseInt($(".food_num").html());
		var c=parseInt($("#g_num").html());
        var n=c;
        var img_id = <?php echo $img_id ?>;
        var to = <?php echo "'".$to."'" ?>;
        var aid =<?php echo $aid ?>;
        var sig =$.md5('aid='+aid+'&img_id='+img_id+'&n='+n+'&to='+to+'dog&cat');
        location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&img_id='+img_id+'&n='+n+'&to='+to+'&sig='+sig+'&SID='+<?php echo "'".$sid."'" ?>;
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
</html>
