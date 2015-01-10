<!DOCTYPE>
<html>
	<head>
		<meta name="viewport" content="width = device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>暖冬活动详情页--动物园酥饼</title>
		<link rel="stylesheet" type="text/css" href="css/activity_view.css">
		<link rel="stylesheet" type="text/css" href="css/alert.css">		
		<script src="js/zepto.min.js"></script>
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
		<img src="css/images/head_background.png" alt="" class="act2_head">
		<div class="other_story">
			<img src="css/images/other_story.png" alt="">
		</div>
		<div class="sponsor">
			<img src="css/images/act2_sponsor.png">
		</div>
		<div class="introduce">
			<div class="pet_name">代粮喵：<span>酥饼</span></div>
			<div class="pet_place">代粮地点：<span>北京动物园</span></div>
			<div class="pet_weibo"><a href="http://weibo.com/u/5041262490?topnav=1&wvr=6&topsug=1">新浪微博：<span>酥饼大人</span></a></div>
		</div>
		<div class="act2_petHead">
			<img  alt="" src="css/images/h4.png">

		</div>
		
		<div class="act2_range">
			<div class="hr_10"></div>
			<div class="target_Box clearfix">
				<div class="target">目标还剩 <span class="target_num" id="target_num"><?php echo 243000-$food ?></span> 份口粮</div>
				<div class="d_time" id="d_time"></div>
			</div>
			<div class="hr_10"></div>
			<div class="aprogressbar">
				<div class="abar" id="abar" style="width:<?php echo ceil($food/16200)?>%"></div>
			</div>
		</div>
		<div class="zhu">注：每人每天可以免费捐赠 3 份口粮</div>
		<img src="css/images/dashed.png" alt="" class="dashed">
	</div>
	<div class="act2_body">
		<div class="body_tit">活动照片</div>
		<ul class="act2_imgBox clearfix" id="act2_imgBox">
			<li ><img src="css/img/dwy/1.png" alt="" name="1"></li>
			<li ><img src="css/img/dwy/2.png" alt="" name="2"></li>
			<li ><img src="css/img/dwy/3.png" alt="" name="3"></li>
			<li ><img src="css/img/dwy/4.png" alt="" name="4"></li>
			<li ><img src="css/img/dwy/5.png" alt="" name="5"></li>
			<li ><img src="css/img/dwy/6.png" alt="" name="6"></li>
			<li ><img src="css/img/dwy/7.png" alt="" name="7"></li>
			<li ><img src="css/img/dwy/8.png" alt="" name="8"></li>
			<li ><img src="css/img/dwy/9.png" alt="" name="9"></li>
		</ul>
		<div class="hr_100"></div>
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
			<div class="target_3"></div>
		</div>
		<div class="give_btn">
			<div class="give_btn_bottom">
				<div class="give_btn_con">
					<div class="give_left" >
						<h3 id="g_num">1</h3>
					</div>
					<div class="give_m"></div>
					
					<div class="give_right">
						<img src="css/images/reword_btn.gif" id="reward"  />
						
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
                <a href="https://home4pet.aidigame.com" target="_blank">
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
        a.src="css/img/dwy/"+y+".png"; 
        a.name=y;     
});
$('#right_bigImg').click(function(){
    var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x+1;
        if(y>10){
            y=10;
        }
        a.src="css/img/dwy/"+y+".png"; 
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
        a.src="css/img/dwy/"+y+".png";
        a.name=y;       
});
$('#s_one').tap(function(){
    var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x-1;
        if(y<1){
            y=1;
        }
        a.src="css/img/dwy/"+y+".png";
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
    if (<?php echo $alert_flag?>) {
        no_gold();
    };
}
$("#reward").click(function(){
var b=parseInt($("#target_num").html());
var c=parseInt($("#g_num").html());

var n=c;
var aid =<?php echo $aid ?>;
var sig =$.md5('aid='+aid+'&n='+n+'dog&cat');
location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&n='+n+'&sig='+sig+'&SID='<?php echo "'".$sid."'" ?>
});
</script>
</html>











