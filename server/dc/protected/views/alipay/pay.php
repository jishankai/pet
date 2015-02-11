<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>我的口粮你做主</title>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
<script src="js/jquery-1.11.1.js" type="text/javascript"></script>
<style type="text/css">
    li{width:100px;height:20px;}
    li.color{background-color: #F97954;color: #FFFFFF;}
    .P1,.P11,.P2,.P22,.P3,.P33,.P4,.P44{display: none;}
   
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
    <!-- <div class="pay_title">
        <h3>充值</h3>
    </div> -->
    <div class="gold">
        <ul class="money_list" id="money_list">
            <li id="1"><div class="num">100</div><div class="money">￥1</div></li>   
            <li id="5"><div class="num">500</div><div class="money">￥5</div></li>
            <li id="10"><div class="num">1050</div><div class="money">￥10</div></li>
            <li id="100"><div class="num">11000</div><div class="money">￥100</div></li>
        </ul>
    </div>
    <!-- <div class="pay_select">
        <h3>选择支付方式</h3>
    </div> -->
    
    <div class="share">
        <img src="css/images/pay.jpg" id="z_f_b">
    <div>

    <!-- 表单 -->
     <form name=alipayment action=<?php echo $this->createUrl('alipay/alipayapi')?> method=post target="_blank">
            <div id="body" style="clear:left">
                <dl class="content">
                    <dt class="p1">卖家支付宝帐户：</dt>
                    <dd class="p11">
                        <span class="null-star">*</span>
                        <input size="30" name="WIDseller_email" value="liria@aidigame.com" />
                        <span></span>
                    </dd>
                    <dt class="p2">商户订单号：</dt>
                    <dd class="p22">
                        <span class="null-star">*</span>
                        <input size="30" name="WIDout_trade_no" id="orderno" />
                        <span></span>
                    </dd>
                    <dt class="p3">订单名称：</dt>
                    <dd class="p33">
                        <span class="null-star">*</span>
                        <input size="30" name="WIDsubject" id="dname" value="1元100个金币"/>
                        <span></span>
                    </dd>
                    <dt class="p4">付款金额：</dt>
                    <dd class="p44">
                        <span class="null-star">*</span>
                        <input size="30" name="WIDtotal_fee" id="rmb" />
                        <span></span>
                    </dd>
          
                    <dd>
                              
                            <input class="new-btn-login" type="submit" value="" style="text-align:center;">
                      
                    </dd>
                </dl>
            </div>
        </form> 
    
    
</div>


</body>
    <script>

  $(function(){
  
            $("#rmb").val("1");
            $("#1").addClass("color").siblings().removeClass();
            var timestamp=(new Date().valueOf()); 
                $("#orderno").val("cwxq"+timestamp);
      
            $("#money_list li").click(function(){
                $(this).addClass("color").siblings().removeClass();
                 $("#rmb").val($(this).attr("id"));
                 var rmb = $(this).attr("id");
                 var gold = 0;
                 if(rmb==1){
                    gold = 100;
                 }
                 else if(rmb==5){
                    gold = 500;
                 }
                 else if(rmb==10){
                    gold = 1050;
                 }
                 else if(rmb==100){
                    gold = 11000;
                 }
                 
                 $("#dname").val(rmb+"元"+gold+"个金币");

                 var timestamp=(new Date().valueOf()); 
                $("#orderno").val("cwxq"+timestamp);
               /* alert(timestamp);*/
            });

    })

    </script>


</html>
