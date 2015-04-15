<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width = device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>充值活动</title>
	<style>
		
		*{padding: 0;margin: 0;}
		body{background: #efefef;}
		.wrapper{width: 640px;margin: 0 auto;text-align: center;
			}
		.pay_active_btn{position: absolute;left: 50%;/*margin-left: -190px;*/
			/*top:420px;*/display: block;width: 450px;height: 80px;}

		@media only screen and (min-width: 300px) and (max-width: 640px){

			.wrapper{width: 100%;}
			.pay_active{width: 100%;height: auto;}
			.pay_active_btn{
				display: block;
				width: 70%;height: 50px;/*background-color: red;*/
				/*margin-left:-35%;*//*top:35%;*/}
		}

		/*@media only screen and (min-height: 480px) and (max-height: 540px){
			.pay_active_btn{top:45%!important;}
		}*/

	</style>
</head>
<body>
<div class="wrapper">
	<img src="css/images/pay_active.jpg" class="pay_active"/>
    <a href="<?php if($SID!='') { echo $this->createUrl('alipay/pay', array('SID'=>$SID)); } else { echo "http://tb.cn/PgvVLDy"; } ?>"><!-- <img src="pay_active_btn.jpg" class="pay_active_btn"/> --><span class="pay_active_btn"></span>
	</a>
</div>
	
</body>
<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
<script type="text/javascript">
$(function(){
	var a=$(".pay_active_btn").width();
	$(".pay_active_btn").css("margin-left",-a/2+"px");
	var b=$(".pay_active").height();
	$(".pay_active_btn").css("top",b*0.42+"px");
})
	
</script>
</html>
