<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<meta name='HandheldFriendly' content='True' />
		<!--<meta name='viewport' content='initial-scale=1.0; maximum-scale=1.0; user-scalable=0;' />-->
		<meta name='viewport' content='user-scalable=0' />
		<meta name="viewport" content="width=device-width" />
        <title>摸一摸</title>
	<link rel="stylesheet" type="text/css" href="css/eraser.css">
</head>
<body>
<div class="comWidth">
	<div class="wraper">
		<div class="head clearfix">
			<div class="img"><img src="css/images/a1.jpg" id="head"></div>
			<div>摸一摸 <span>猫君</span></div>
		</div>
		<div class="box">
			<div class="container">
				<img src="css/images/no_choice.png" class="no_choice"/>
				<img id="robot" src="css/images/a1.jpg" />
				<img id="redux" src="css/images/eraser.png" />
			</div>
		</div>
		
		<p class="happy"><span>猫君</span>很开心</p>
		<p class="popularity">人气＋10 </p>
		<p class="text">每天摸一摸，人气涨的高～</p>
		<p class="none">今天的次数用完啦，记得明天还来哦～</p>
		<ul class="shareList clearfix">
			<li>
				<img src="css/images/dialog_go_register_weixin.png">
				<p>微信好友</p>
			</li>
			<li>
				<img src="css/images/dialog_go_register_friend.png">
				<p>朋友圈</p>
			</li>
			<li>
				<img src="css/images/dialog_go_register_xinlang.png.png">
				<p>微博</p>
			</li>
		</ul>
		
	</div>
	<div class="prompt">每天第一次成功分享后，可以再摸1次喔～</div>
	<div class="e_btn">
		<a href="#" id="e_btn">我也来摸</a>
	</div>

	<div class="float">
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

	
</body>
	<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
	<script src='js/jquery.eraser.js' type='text/javascript'></script>
	<script type = "text/javascript">

	$(window).on('load',function(){
		
		$("#e_btn").click(function(){
			$("#e_btn").hide();

			function init( event ) {
			$("#redux").eraser();
				// you can alse specify the brush size (in pixel) by using options :
				 //$("#redux").eraser({size: 100});
			}

			function remove(event) {
				$("#redux").eraser('clear');
				//event.preventDefault();
			}

			function reset(event) {
				$("#redux").eraser('reset');
				event.preventDefault();
			}

			function grow(event) {
				$("#redux").eraser("size",200);
				event.preventDefault();
			}


			$('#redux').eraser( {
			    completeRatio: .6,
			    completeFunction: showResetButton
			});


			function showResetButton(){
				/*alert("111");*/
				$(".text").hide();
				/*$(".happy").show();*/
				$(".happy").fadeIn("500");
				$(".popularity").fadeIn("500");
				$(".shareList").show();
				$(".prompt").show();
			

				/*今天的次数用完*/
				/*$("#robot").hide();
				$("#redux").hide();
				$(".happy").hide();
				$(".popularity").hide();
				$(".container").attr("style","border:none");
				$(".no_choice").show();
				$(".none").fadeIn("500");*/
			}
		})


		

	})
	

		

	</script>



</html>