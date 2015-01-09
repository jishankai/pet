<!DOCTYPE>
<html>
	<head>
		<meta name="viewport" content="width = device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>暖冬活动详情页--北植十四阙</title>
		<link rel="stylesheet" type="text/css" href="css/activity_view.css">
		<link rel="stylesheet" type="text/css" href="css/alert.css">		
		<script src="js/zepto.min.js"></script>
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
			<div class="pet_name">代粮喵：<span>61</span></div>
			<div class="pet_place">代粮地点：<span>北京植物园</span></div>
			<div class="pet_weibo">新浪微博：<span><a href="http://weibo.com/u/1678351712?topnav=1&wvr=6&topsug=1">十四阙</a></span></div>
		</div>
		<div class="declaration">活动宣言：<span>给植物园的流浪喵们带来温暖的冬天！</span></div>
		<div class="act2_petHead">
			<img  alt="" src="css/images/h3.png">

		</div>
		
		<div class="act2_range">
			<div class="hr_10"></div>
			<div class="target">目标还剩 <span class="target_num" id="target_num">40000</span> 份口粮<span class="d_time" id="d_time"></span></div>
			<div class="hr_10"></div>
			<div class="aprogressbar">
				<div class="abar" id="abar" style="width:60%"></div>
			</div>
		</div>
		<div class="zhu">注：每人每天可以免费捐赠 5 份口粮</div>
		<img src="css/images/dashed.png" alt="" class="dashed">
	</div>
	<div class="act2_body">
		<div class="body_tit">活动照片</div>
		<ul class="act2_imgBox clearfix" id="act2_imgBox">
			<li ><img src="css/img/zwy/1.png" alt="" name="1"></li>
			<li ><img src="css/img/zwy/2.png" alt="" name="2"></li>
			<li ><img src="css/img/zwy/3.png" alt="" name="3"></li>
			<li ><img src="css/img/zwy/4.png" alt="" name="4"></li>
			<li ><img src="css/img/zwy/5.png" alt="" name="5"></li>
			<li ><img src="css/img/zwy/6.png" alt="" name="6"></li>
			<li ><img src="css/img/zwy/7.png" alt="" name="7"></li>
			<li ><img src="css/img/zwy/8.png" alt="" name="8"></li>
			<li ><img src="css/img/zwy/9.png" alt="" name="9"></li>
			<li ><img src="css/img/zwy/10.png" alt="" name="10"></li>
		</ul>
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
						<img src="css/images/reword_btn.gif" id="reward"  onclick="aa()" />
						
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
				<img src="css/images/load.jpg"/>
			</div>
		</div>
	</div>

</div>

<!-- 大图显示 -->
	 
	<div class="large animated fadeInDown" id="large_container" style="display:none">
		
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
  $('#large_container').tap(function(){
        $('#container').css({height:'auto','overflow':'auto'})
        $('#large_container').hide();
    });

/*左滑屏幕*/

$('#large_container').swipeLeft(function(){
        var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x+1;
        if(y>9){
            y=9;
        }
        a.src="css/img/zwy/"+y+".png"; 
        a.name=y;
});

/*浏览器中单击右图标*/
$('#right_bigImg').click(function(){
    var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x+1;
        if(y>10){
            y=10;
        }
        a.src="css/img/zwy/"+y+".png"; 
        a.name=y;     
});


/*手机中右滑*/
$('#large_container').swipeRight(function(){
     var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x-1;
        if(y<1){
            y=1;
        }
        a.src="css/img/zwy/"+y+".png"; 
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
        a.src="css/img/zwy/"+y+".png";
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

/*浏览器中单击关闭按钮*/
$('#close_bigImg').click(function(){
        $('#container').css({height:'auto','overflow':'auto'})
        $('#large_container').hide();
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
</script>
</html>











