<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>充值成功</title>
	<style type="text/css">
		*{padding:0;margin: 0;}
		.comWidth{width: 640px;height:100%;margin:0 auto;}
	 	.wraper{
	 		text-align: center;
	 		background: url(../css/images/recharge-success-background.jpg)left top no-repeat;
	 		background-size: 100% 100%;}
	 	img{margin-top: 30%;width: 200px;}
	 	h1{color:#cc6715;margin-top: 64px;}
	 	h2{color:#351e0c;margin-top: 32px;}
	 	@media only screen and (min-width: 300px) and (max-width: 640px) {
	 		.comWidth{width: 100%;height:100%;margin:0 auto;}
	 		img{width: 120px;}
	 		h1{margin-top: 40px;font-size: 22px;}
	 		h2{margin-top: 20px;font-size: 18px;}
	 	}
	</style>		
</head>
<body>
<div class="wraper comWidth">
	<img src="css/images/recharge-success.gif">
	<h1>充值成功！</h1>
	<h2>请留意应用内的私信通知哟～ </h2>
</div>
</body>
<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
<script type="text/javascript">

$(function(){
	$(".wraper").height($(window).height());
	var a=$(window).height()/5;
	$("img").css("margin-top",a+"px");
})
	
</script>

	

</html>