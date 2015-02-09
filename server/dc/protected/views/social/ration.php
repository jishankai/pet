<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width = device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>口粮页</title>
        <script type="text/javascript" src="js/jquery-1.11.1.js"></script>
        <script type="text/javascript" src="js/jquery.md5.js"></script>
		<link rel="stylesheet" type="text/css" href="css/ration.css">
	</head>
<body>

<div class="comWidth" id="comWidth">
	<div class="box1 clearfix">
		<div class="imgBox"><img src="" id="head1"></div>
		<div class="conBox">
			<h3 class="tit">已挣得口粮： <span id="food1"></span> 份</h3>
			<p class="con" id="con1"></p>
			<p class="time" id="time1"></p>
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
</div>
</body>
<script type="text/javascript">

$(window).on('load',function(){
       <?php if (count($r)==0) {?>
           $('#comWidth').attr('style', 'display:none');
       <?php }?>
 /*$.getJSON('http://api.flickr.com/services/feeds/photos_public.gne?tags=cat&tagmode=any&format=json&jsoncallback=?', function(data){*/
	   <?php foreach ($r as $k => $v) { ?>
       if(<?php echo (time()-$v['create_time'])>86400?0:1?>){
         	$("#head1").attr('src','<?php echo "http://".OSS_PREFIX."4upload.oss-cn-beijing.aliyuncs.com/".$v['url']?>');
         	$("#food1").html('<?php echo $v['food']?>');
         	$("#con1").html('<?php echo $v['cmt']?>');
         	$("#time1").html('<?php echo $v['create_time']?>');

            $("#reward").click(function(){
              if ($("#time")[0].innerHTML!="已结束") {
                var b=parseInt($(".food_num").html());
                var c=parseInt($("#g_num").html());
                var n=c;
                var img_id = <?php echo $v['img_id'] ?>;
                var aid =<?php echo $aid ?>;
                var sig =$.md5('aid='+aid+'&img_id='+img_id+'&n='+n+'&to='+to+'dog&cat');
                location.href = <?php echo "'".$this->createUrl('image/rewardFoodMobileApi')."'" ?>+'&aid='+aid+'&img_id='+img_id+'&n='+n+'&to='+to+'&sig='+sig+'&SID='+<?php echo "'".$SID."'" ?>;
                } else {
                var btn=$(".give_btn");
                btn.attr("display","none");
                }
            });
         }  else{ 

            var box=$('<div>').addClass('box').addClass('clearfix').appendTo($('.comWidth'));
           	var imgBox=$('<div>').addClass('imgBox').appendTo($(box));
           	var img=$('<img>').attr('src','<?php echo "http://".OSS_PREFIX."4upload.oss-cn-beijing.aliyuncs.com/".$v['url']?>').appendTo($(imgBox));
           	var conBox=$('<div>').addClass('conBox').appendTo($(box));
           	var tit=$('<h3>').addClass('tit').appendTo($(conBox));
           	var span1=$('<span>').html("已挣得口粮： ").appendTo($(tit));
           	var span2=$('<span>').html('<?php echo $v['food']?>').attr('food').appendTo($(tit));
           	var span2=$('<span>').html(" 份").appendTo($(tit));
           	var con=$('<p>').addClass('con').html('<?php echo $v['cmt']?>').appendTo($(conBox));
           	var time=$('<p>').addClass('time').html('<?php echo $v['create_time']?>').appendTo($(conBox));

          	}
       <?php }?>
     /*})*/
 })

</script>
</html>
