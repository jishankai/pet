<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>pay-test</title>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
<script src="http://libs.baidu.com/jquery/1.9.0/jquery.js" type="text/javascript"></script>
<style type="text/css">
	li{width:100px;height:20px;}
	li.color{color:red;background:#eee;}
	form{display: none;}
</style>
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
<div class="comWidth">
	<div class="pay_title">
		<h3>充值</h3>
	</div>
	<div class="gold">
		<ul class="money_list">
            <li id="1"><div class="num">100</div><div class="money">￥1</div></li>	
            <li id="5"><div class="num">500</div><div class="money">￥5</div></li>
            <li id="10"><div class="num">1050</div><div class="money">￥10</div></li>
             <div class="hr_3"></div>
            <li id="100"><div class="num">11000</div><div class="money">￥100</div></li>
        </ul>
	</div>
	<div class="pay_select">
		<h3>选择支付方式</h3>
	</div>
	
	<div class="share">
		<img src="css/images/pay.jpg" id="z_f_b">
		<img src="css/images/pay1.jpg" id="z_f_b1" style="display:none;">
	<div>
	

	<div class="foot" onclick="document.forms[0].submit();">
		
	</div>

	<!-- 表单 -->
	 <form name=alipayment action=<?php echo $this->createUrl('alipay/alipayapi')?> method=post target="_blank">
            <div id="body" style="clear:left">
                <dl class="content">
                    <dt>卖家支付宝帐户：</dt>
                  <dd>
                        <span class="null-star">*</span>
                        <input size="30" name="WIDseller_email" value="liria@aidigame.com" />
                        <span></span>
                    </dd>
                    <dt>商户订单号：</dt>
                    <dd>
                        <span class="null-star">*</span>
                        <input size="30" name="WIDout_trade_no" value="cwxq<?php echo time()?>" />
                        <span></span>
                    </dd>
                    <dt>订单名称：</dt>
                    <dd>
                        <span class="null-star">*</span>
                        <input size="30" name="WIDsubject" value="1元100个金币"/>
                        <span></span>
                    </dd>
                    <dt>付款金额：</dt>
                    <dd>
                        <span class="null-star">*</span>
                        <input size="30" name="WIDtotal_fee" id="rmb" />
                        <span></span>
                    </dd>
					<dt></dt>
                    <dd>
                        <span class="new-btn-login-sp">
                            <button class="new-btn-login" type="submit" style="text-align:center;">确 认</button>
                        </span>
                    </dd>
                </dl>
            </div>
		</form>	
	
	
</div>


</body>
	<script>

		$(function(){

			$("#money_list li").click(function(){
				$(this).addClass("color").siblings().removeClass();
				 $("#rmb").val($(this).attr("id"));
			});

			$("#z_f_b").click(function(){
				$(this).css("display","none");
				$("#z_f_b1").css("display","block");

			});

			$("#z_f_b1").click(function(){
				$(this).css("display","none");
				$("#z_f_b").css("display","block");

			});

		});


	</script>


</html>
