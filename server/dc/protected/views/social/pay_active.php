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
		.pay_active_btn{position: absolute;left: center;margin-left: -190px;
			top:420px;}

		@media only screen and (min-width: 300px) and (max-width: 640px){

			.wrapper{width: 100%;}
			.pay_active{width: 100%;height: auto;}
			.pay_active_btn{width: 70%;height: auto;
				margin-left:-35%;top:35%;}
		}

		@media only screen and (min-height: 480px) and (max-height: 540px){
			.pay_active_btn{width: 70%;height: auto;
				margin-left:-35%;top:45%;}
		}

	</style>
</head>
<body>
<div class="wrapper">
	<img src="css/images/pay_active.jpg" class="pay_active"/>
	<a href="#"><img src="css/images/pay_active_btn.jpg" class="pay_active_btn"/></a>
</div>
	
</body>
</html>