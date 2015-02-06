<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width = device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>口粮页</title>
		<link rel="stylesheet" type="text/css" href="css/ration.css">
	</head>
<body>

<div class="comWidth">
	<div class="box1 clearfix">
		<div class="imgBox"><img src="css/images/a1.jpg" id="head1"></div>
		<div class="conBox">
			<h3 class="tit">已挣得口粮： <span id="food1">5903</span> 份</h3>
			<p class="con" id="con1">我是照片描述我是照片描述我是照片描述我是照片描述我是照片描述我是照片描述</p>
			<p class="time" id="time1">1小时前</p>
		</div>
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
	<div class="hr"></div>
	<div class="box clearfix">
		<div class="imgBox"><img src="css/images/test_pet7.jpg"></div>
		<div class="conBox">
			<h3 class="tit">已挣得口粮： <span>5903</span> 份</h3>
			<p class="con">我是照片描述我是照我是照片描述我是照片描述我是照片描述我是照片描述我是照片描述我是照片描述我是照片描述我是照片描述</p>
			<p class="time">1小时前</p>
		</div>
	</div>
	<div class="box clearfix">
		<div class="imgBox"><img src="css/images/a1.jpg"></div>
		<div class="conBox">
			<h3 class="tit">已挣得口粮： <span>5903</span> 份</h3>
			<p class="con">我是照片描述我是照片描述我是照片描述我是照片描述我是照片描述我是照片描述我是照片描述我是照片描述</p>
			<p class="time">1小时前</p>
		</div>
	</div> 

</div>
</body>
<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
<script type="text/javascript">

$(window).on('load',function(){
	
	var dataInt={
	    'head':[{'src':'pet1.jpg'},{'src':'pet2.jpg'},{'src':'pet3.jpg'}],
	    'food':[{'food':'223'},{'food':'333'},{'food':'444'}],
	    'con':[{'con':'dfffsfsfsfsfsf'},{'con':'dfsfsfsfsfsf'},{'con':'ggeasdswrfad'}],
	    't':[{'time':'1小时'},{'time':'1小时'},{'time':'1小时'}],
	    'flag':[{'flag':'false'},{'flag':'true'},{'flag':'false'}]
	    };


 /*$.getJSON('http://api.flickr.com/services/feeds/photos_public.gne?tags=cat&tagmode=any&format=json&jsoncallback=?', function(data){*/
	for(var i=0;i<dataInt.head.length;i++){
		
       if($(dataInt.flag[i]).attr('flag')=="true"){
         	$("#head1").attr('src','css/images/'+$(dataInt.head[i]).attr('src'));
         	$("#food1").html($(dataInt.food[i]).attr('food'));
         	$("#con1").html($(dataInt.con[i]).attr('con'));
         	$("#time").html($(dataInt.t[i]).attr('time'));
         }  else{ 

            var box=$('<div>').addClass('box').addClass('clearfix').appendTo($('.comWidth'));
           	var imgBox=$('<div>').addClass('imgBox').appendTo($(box));
           	var img=$('<img>').attr('src','css/images/'+$(dataInt.head[i]).attr('src')).appendTo($(imgBox));
           	var conBox=$('<div>').addClass('conBox').appendTo($(box));
           	var tit=$('<h3>').addClass('tit').appendTo($(conBox));
           	var span1=$('<span>').html("已挣得口粮： ").appendTo($(tit));
           	var span2=$('<span>').html($(dataInt.food[i]).attr('food')).appendTo($(tit));
           	var span2=$('<span>').html(" 份").appendTo($(tit));
           	var con=$('<p>').addClass('con').html($(dataInt.con[i]).attr('con')).appendTo($(conBox));
           	var time=$('<p>').addClass('time').html($(dataInt.t[i]).attr('time')).appendTo($(conBox));

          	}
        }
     /*})*/
 })
</script>
</html>