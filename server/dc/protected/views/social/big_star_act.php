<!DOCTYPE>
<html>
<head>
	<meta name="viewport" content="width = device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><?php echo $star['name']?></title>
	<link rel="stylesheet" type="text/css" href="css/big_star_act.css">
    <script>
       /* var _hmt = _hmt || [];
        (function() {
          var hm = document.createElement("script");
          hm.src = "//hm.baidu.com/hm.js?fffd5628b5c5fe81d7a7867d554d07ca";
          var s = document.getElementsByTagName("script")[0]; 
          s.parentNode.insertBefore(hm, s);
        })();


    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
    window.shareData = {
        "timeLineLink": "http://192.168.10.151:8001/index.php?r=social/newYearEvent",   
        "sendFriendLink": "http://192.168.10.151:8001/index.php?r=social/newYearEvent",
        "weiboLink": "http://192.168.10.151:8001/index.php?r=social/newYearEvent",
        "tTitle": "宠物星球·年夜饭计划",
        "tContent": "举手之劳，免费捐粮。你的轻轻一点，给ta一个温暖冬天。",
        "fTitle": "宠物星球·年夜饭计划",
        "fContent": "举手之劳，免费捐粮。你的轻轻一点，给ta一个温暖冬天。",
        "wContent": "举手之劳，免费捐粮。你的轻轻一点，给ta一个温暖冬天。"
        };
        
        // 发送给好友
        WeixinJSBridge.on('menu:share:appmessage', function (argv) {
            WeixinJSBridge.invoke('sendAppMessage', {
                "img_url": "http://192.168.10.151:8001/css/images/w1.jpg",
                "img_width": "401",
                "img_height": "275",
                "link": window.shareData.sendFriendLink,
                "desc": window.shareData.fContent,
                "title": window.shareData.fTitle
            }, function (res) {
                _report('send_msg', res.err_msg);
            })
        });
        // 分享到朋友圈
        WeixinJSBridge.on('menu:share:timeline', function (argv) {
            WeixinJSBridge.invoke('shareTimeline', {
                "img_url": "http://192.168.10.151:8001/css/images/w1.jpg",
                "img_width": "401",
                "img_height": "275",
                "link": window.shareData.timeLineLink,
                "desc": window.shareData.tContent,
                "title": window.shareData.tTitle
            }, function (res) {
                _report('timeline', res.err_msg);
            });
        });
 
    }, false);*/

    </script>
    <script src="js/zepto.min.js"></script>
</head>
<body>
<div class="act1_wrap comWidth">
	<div class="act1_top">
		<a href="<?php echo $star['url']?>"><img alt="" src="<?php echo $star['banner'];?>"></a>
	</div>
	<div class="act1_body">
		<div class="ab_tit clearfix">
			<img src="css/images/ab_tit1.gif" class="tit_img">
			<a href="http://home4pet.imengstar.com"><img src="css/images/android.png" class="android_btn"></a>
			<a href="https://itunes.apple.com/cn/app/chong-wu-xing-qiu/id932758265?mt=8?"><img src="css/images/apple.png" class="apple_btn"></a>
		</div>
	</div>

	<div class="star">最热萌星</div>
	<div class="act1_bottom">
		<div class="head_imgBox">
			<ul class="head_imgList clearfix">
                <?php for($i=0;$i<count($star['animals'])&&$i<7;$i++) {?>
                <li style="position: relative">
                    <img src="http://<?php echo OSS_PREFIX;?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $star['animals'][$i]['tx'];?>">
                    <h3><?php echo $star['animals'][$i]['name'];?></h3>
                </li>
                <?php }?>
			</ul>
		</div>	
	</div>
	<div class="hr_30"></div>

	<div class="hot_photos">热门照片</div>
	<div class="hot_photos_box">	
		<ul class="hot_photos_list" id="imgBox">
            <?php /*for($i=0;$i<count($star['images'])&&$i<9;$i++) {*/?>
                <!-- <li>
                    <img src="http://<?php /*echo OSS_PREFIX;*/?>4upload.oss-cn-beijing.aliyuncs.com/<?php /*echo $star['images'][$i]['url'];*/?>">
                    <div><?php /*echo $star['images'][$i]['stars'];*/?></div>
                </li> -->
            <?php /*}*/?>
		</ul>
	</div>

</div>
</body>
<script type="text/javascript">
    $(window).on('load',function(){
        var oParent=document.getElementById('imgBox');
        <?php for($i=0;$i<count($star['images'])&&$i<9;$i++) {?>      
            var oA=document.createElement('a');
                oA.setAttribute('href',"<?php echo $this->createUrl('social/foodShareApi', array('img_id'=>$star['images'][$i]['img_id'], 'SID'=>$SID))?>");
                oParent.appendChild(oA);
            var oLi=document.createElement('li');
                oLi.className='box';
                oA.appendChild(oLi);
            var oImg=document.createElement('img');
                oImg.src="http://<?php echo OSS_PREFIX;?>4upload.oss-cn-beijing.aliyuncs.com/<?php echo $star['images'][$i]['url'];?>";
                oLi.appendChild(oImg);
            var oDiv=document.createElement('div');
                oDiv.innerHTML=<?php echo $star['images'][$i]['stars'];?>;
                oLi.appendChild(oDiv);            
        <?php }?>
    })
</script>
</html>
