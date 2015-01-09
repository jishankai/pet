<!DOCTYPE>
<html>
	<head>
		<meta name="viewport" content="width = device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>暖冬活动详情页--税后五百万翻番</title>
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
			<div class="pet_name">代粮喵：<span>税后五百万翻番</span></div>
			<div class="pet_place">代粮地点：<span>北京航天航空学院</span></div>
			<div class="pet_weibo"><a href="http://weibo.com/u/1883332187?topnav=1&wvr=6&topsug=1">新浪微博：<span>税后五百万翻番</span></a></div>
		</div>
		<!-- <div class="declaration">活动宣言：<span>给北航的流浪喵们带来温暖的冬天！</span></div> -->
		<div class="act2_petHead">
			<img  alt="" src="css/images/h1.png">

		</div>

		<div class="act2_range">
			<div class="hr_10"></div>
			<div class="target_Box clearfix">
				<div class="target">目标还剩 <span class="target_num" id="target_num">162000</span> 份口粮</div>
				<div class="d_time" id="d_time"></div>
			</div>
			<div class="hr_20"></div>
			<div class="aprogressbar">
				<div class="abar" id="abar" style="width:10%"></div>
			</div>
		</div>
		<div class="zhu">注：每人每天可以免费捐赠 3 份口粮</div>
		<img src="css/images/dashed.png" alt="" class="dashed">
	</div>
	<div class="act2_body">
		<div class="body_tit">活动照片</div>
		<ul class="act2_imgBox clearfix" id="act2_imgBox">
			<li ><img src="css/img/bh/1.png" alt="" name="1"></li>
			<li ><img src="css/img/bh/2.png" alt="" name="2"></li>
			<li ><img src="css/img/bh/3.png" alt="" name="3"></li>
			<li ><img src="css/img/bh/4.png" alt="" name="4"></li>
			<li ><img src="css/img/bh/5.png" alt="" name="5"></li>
			<li ><img src="css/img/bh/6.png" alt="" name="6"></li>
			<li ><img src="css/img/bh/7.png" alt="" name="7"></li>
			<li ><img src="css/img/bh/8.png" alt="" name="8"></li>
			<li ><img src="css/img/bh/9.png" alt="" name="9"></li>		
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
						<img src="css/images/reword_btn.gif" id="reward"  onclick="aa()" />

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
				<img src="css/images/load.jpg"/>
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
        if(y>10){
            y=10;
        }
        a.src="css/img/bh/"+y+".png"; 
        a.name=y;     
});
$('#right_bigImg').click(function(){
    var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x+1;
        if(y>9){
            y=9;
        }
        a.src="css/img/bh/"+y+".png"; 
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
        a.src="css/img/bh/"+y+".png";
        a.name=y;       
});
$('#s_one').tap(function(){
    var a=document.getElementById("large_img"); 
        var x=parseInt(a.name);
        var y=x-1;
        if(y<1){
            y=1;
        }
        a.src="css/img/bh/"+y+".png";
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

var b=parseInt($("#target_num").html());
var c=parseInt($("#g_num").html());

var n=c;
var aid =<?php echo $aid ?>;
var sid =<?php echo $sid ?>;
var sig =$.md5('aid='+aid+'&n='+n+'dog&cat');
$.getJSON(<?php echo $this->createUrl('images/rewardFoodMobileApi')?>+'&aid='+aid+'&n='+n+'&SID='+sid+'&sig='+sig, function(data){

   $("#reward").click(function(){

    $("#target_num").html(b-c);
    var d=parseInt($("#target_num").html());

    /*进度条*/
    var total=162000;
    var pwidth=(total-d)/total*100+"%";
    $("#abar").width(pwidth);

    /*弹出框*/

    $("#shadow_Box").css('display','block');
    $("#t2").css('display','block');

});
});  	



</script>
</html>











