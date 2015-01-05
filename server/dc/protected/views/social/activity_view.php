<!DOCTYPE>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>wram-winter-activities</title>
		<link rel="stylesheet" type="text/css" href="css/w_act_style.css">
		<link href="css/animate.css" rel="stylesheet" type="text/css">		
		<script src="js/zepto.min.js"></script>		
	</head>

<body>
	<div class="wrapper comWidth">
		<div class="amain_body">
			<div class="amain_top">
				<div class="hr_15"></div>
				<div class="astory">
					<h3>TA们的故事</h3>
					<img src="css/images/a_right.png"/>
				</div>
				<div class="event_s">
					<h3 class="left">活动赞助</h3>
					<img src="css/images/ali.png" alt="" class="left" />
				</div>
			</div>
			<img src="css/images/pet_head.png" class="pet_head"/>
			<div class="amain_bottom">
				<div class="b_tit">
					<h3>代粮喵：瓜皮</h3>
					<p>为北京香山地区流浪猫代粮</p>
				</div>
				<div class="d_act">
					活动宣言：给香山的流浪喵们带来温暖的冬天！
				</div>
				<div class="atarget">
					<div class="atarget_con">
						<div class="afood_num left">目标还剩 <span id="afood">200</span> 份口粮</div>
						<div class="atime right">倒计时：<span id="ad_time"></span></div>
					</div>
					<div class="aprogressbar">
						<div class="abar" id="abar" style="width:60%"></div>
					</div>
				</div>
				<div class="anote"> 
					<p class="anotes left">
						注：每人每天可以免费捐赠 5 份口粮 
					</p>
					<a href="#" class="right">关于活动</a>
				</div>
			</div>
			<div class="aimg_box">
				<h3 class="aimg_tit">
					活动照片
				</h3>
				 
			 	<ul class="img-container clearfix" id="container">
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
					<div class="target"></div>
				</div>
				<div class="give_btn">
					<div class="give_btn_bottom">
						<div class="give_btn_con">
							<div class="give_left" >
								<h3 id="g_num">1</h3>
							</div>
							<div class="give_m"></div>
							<div class="give_right">
								<img src="css/images/reword_btn.gif" id="reward" onclick="aa()" />
								<img src="css/images/reword_btn.png" style="display:none" id="reward1" />
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

	</div>

	<!-- 大图显示 -->
	 
	<div class="large animated fadeInDown" id="large_container" style="display:none">
		<img id="large_img">
		<a href="#" class="close" id="close"><img src="css/images/img_close.png"></a>
			<div class="close_bigImg" id="close_bigImg" onmouseover="closeShow()" onmouseout="closeHide()"></div>
			<a href="#"  id="left" ><img src="css/images/img_left.png"></a>
			<div class="left_bigImg" id="left_bigImg" onmouseover="leftShow()" onmouseout="leftHide()"></div>
			<a href="#"  id="right"><img src="css/images/img_right.png"></a>
			<div class="right_bigImg" id="right_bigImg" onmouseover="rightShow()" onmouseout="rightHide()"></div>
	</div>

</body>
<script src="js/alink.js"></script>
<script>

</script>
	
</html>

















