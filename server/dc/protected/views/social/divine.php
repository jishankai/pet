<!DOCTYPE>
<html>
<head>
<meta charset="UTF-8">
<title>占卜</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0,maximum-scale=1.0, user-scalable=no">
<style type="text/css">
*{margin: 0;padding: 0;}
.wraper{width: 100%;height:auto;margin: 0 auto;}
img{width:100%;height: 100%;}
#shake{display: none;}
#sign{display: none;}
    
</style>
</head>
<body>
<div class="wraper">
    <img src="css/images/divine_back.jpg" id="back">
    <img src="css/images/devine_animate.gif" id="shake">
</div>
</body>
<script>
		
/*微信摇一摇*/
window.onload=function(){
    document.getElementById("back").onclick=function(){
        doResult();
    }
}
		
    var SHAKE_THRESHOLD = 800;
    var last_update = 0;
    var x = y = z = last_x = last_y = last_z = 0;

    if (window.DeviceMotionEvent) {
        window.addEventListener('devicemotion', deviceMotionHandler, false);
    } else {
        alert('本设备不支持devicemotion事件');
    }

    function deviceMotionHandler(eventData) {
        var acceleration = eventData.accelerationIncludingGravity;
        var curTime = new Date().getTime();

        if ((curTime - last_update) > 100) {
            var diffTime = curTime - last_update;
            last_update = curTime;
            x = acceleration.x;
            y = acceleration.y;
            z = acceleration.z;
            var speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
            var status = document.getElementById("status");

            if (speed > SHAKE_THRESHOLD) {
                doResult();
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    function doResult() {

        document.getElementById("back").style.display="none";
        document.getElementById("shake").style.display="block";
        setTimeout(function(){
            document.getElementById("shake").style.display="none";
            /*document.getElementById("sign").style.display="block";*/
            jumpToDecode();
        }, 2000);
    }



function jumpToDecode(){
	var urls = ["http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284774&idx=1&sn=6a2d816cef0e45c4d7e07ec0786f19df#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284774&idx=1&sn=6a2d816cef0e45c4d7e07ec0786f19df#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284669&idx=1&sn=434501a12524b3863185c0445a9ffa84#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284601&idx=1&sn=b67e9ee0df458ab3f844d28ebf24a796#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284762&idx=1&sn=467c8ed9953c2e9ed0cedb1c0a1439f2#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284708&idx=1&sn=2dfa398763c67405443a5b0c7ec50a1a#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284649&idx=1&sn=ba08b135b300a8ed025c72167b0f98b2#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284747&idx=1&sn=7efb07c4aee24cb435c316009720ecd0#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284687&idx=1&sn=172e0e156010dad6a5a2400a39577fc9#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284627&idx=1&sn=3f7a07e1166980286ef9cba69d525856#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284551&idx=1&sn=48811c77b71a0f4d19d28de2d0dc710c#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284484&idx=1&sn=69799f109baacca6be902ebd4412bdc6#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284536&idx=1&sn=cee3b57ca7a2c42e17d96c71f23158e8#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204268241&idx=1&sn=f8c8697cfa3c96760edbe4dd6fc6db68#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284501&idx=1&sn=d87f13cf1d2bee125142838ed0787aa7#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204285006&idx=1&sn=76b0aefcad7e7ad71ac5390b98dcc04b#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284956&idx=1&sn=aeee438be9aa6b6249d34ebd267276c6#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284871&idx=1&sn=94c719d017307b78865721b332ba97b3#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284817&idx=1&sn=5b10b84294543db8031c5a312617c72a#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284994&idx=1&sn=23d05cf9694b5686b88afd1105610ea8#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284935&idx=1&sn=7dd401995f97577d00eafcdbb249ade6#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284859&idx=1&sn=e55e99947697ab338011347f42713ccb#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284971&idx=1&sn=897fb93b3a696401d8eb63a283c6a2de#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284912&idx=1&sn=bb3e260cfd3c3af08c0b8943f63b98bf#rd",
		"http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=204284835&idx=1&sn=f39ae88d3ffedad34a2c51c9a7fa1c5e#rd"
		
	];
	var jumpTo = urls[parseInt(Math.random() * urls.length)];
	window.location = jumpTo;
}
    
</script>


</html>