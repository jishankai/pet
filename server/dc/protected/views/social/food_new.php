<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initail-scale=1.0 ,maximum-scale=1, user-scalable=no">
	<title>照片详情页－－NEW</title>
	<link rel="stylesheet" type="text/css" href="css/reset.css">
	<link rel="stylesheet" type="text/css" href="css/food_new.css">
</head>
<body>
<div class="wrap">
	<div class="top">
		<img src="css/images/base_head13.jpg" class="pet_head"/>
		<span class="pet_name">我是来钱儿思密达</span>
	</div>
	<div class="main_body">
		<!-- 图片＋粮食＋倒计时 -->
		<div class="pet_img_box">
			<img src="css/images/pet8.jpg" class="pet_img"/>
			<div class="food_time clearfix">
				<div class="food_box left">
					<img src="css/images/food_white_border.png"/>
					<span id="food">12</span>
				</div>
				<div class="d_time_box right">
					<img src="css/images/time_white_border.png"/>
					<span id="d_time">12:44:06</span>
				</div>
			</div>
		</div>
		<!-- 话题和描述 -->
		<div class="topic_describe">
			<span class="topic">＃妈啊我的包子脸＃</span>
			<span class="describe">好饿 好饿 好饿</span>
		</div>
		<!-- 点赞，评论，礼物。。按钮组 -->
		<ul class="btns_box clearfix">
			<li>
				<img src="css/images/page_like.png" id="like_img"/>
				<span id="like">已赞</span>
			</li>
			<li id="comment_btn">
				<img src="css/images/page_comment.png" id="comment_img"/>
				<span id="comment">评论</span>
			</li>
			<li>
				<img src="css/images/icon_gift.png" id="gift_img"/>
				<span id="gift">礼物</span>
			</li>
			<li>
				<img src="css/images/bt_more.png"/>
			</li>
		</ul>
		<!-- 点赞头像显示 -->
		<div class="like_box clearfix">
			<div class="like_grey">
				<img src="css/images/page_comment_gray.jpg">
			</div>
			<ul class="like_head clearfix">
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
				<li><img src="css/images/base_head13.jpg"></li>
			</ul>
			<span id="like_num">960</span>
		</div>
		<!-- 评论情况展示 -->
		<div class="comment_box">
			<div class="comment_more">
				<img src="css/images/page_comment_grey.jpg">
				<a class="the_more_comment">查看所有<span id="comment_num">35</span>条评论</a>
			</div>
			<ul class="comment_list clearfix">
				<li>
					<img src="css/images/base_head13.jpg"/>
					<p>姑娘，胸毛不错～</p>
				</li>
				<li>
					<img src="css/images/base_head13.jpg"/>
					<p>姑娘，胸毛不错～</p>
				</li>
			</ul>
		</div>
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
		<div class="give_btn" id="give_btn">
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
	</div>

	<form class="comment_alert">
		<h3>说点什么</h3>
		<input type="submit" id="submit" value=""/>
		<input type="button" id="close"/>
		<textarea></textarea>
	</form>

</div>
</body>
<script type="text/javascript">
	$(function(){
		$("#comment_btn").click(function(){
			$(".comment_alert").show();
		})
		$("#close").click(function(){
			$(".comment_alert").hide();
		})
	})
</script>
</html>





















