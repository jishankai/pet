/*选金币效果*/
$(".give_left").click(function () {

	$("#gold").toggle();
});

	$("#one").click(function(){
		var x=$(this).html();
		$("#g_num").html(x);
		$("#gold").hide();
	})
	$("#one1").click(function(){
		var x=$(this).html();
		$("#g_num").html(x);
		$("#gold").hide();
	})
	$("#one2").click(function(){
		var x=$(this).html();
		$("#g_num").html(x);
		$("#gold").hide();
	})

	$("#one3").click(function(){
		var x=$(this).html();
		$("#g_num").html(x);
		$("#gold").hide();
	})

/*加1效果*/

	/*$("#reward").click(function(){
		var b=parseInt($(".food_num").html());
		var c=parseInt($("#g_num").html());
		


		var left = parseInt($('.food_num').position().left-80), top =  parseInt($('.food_num').position().top-40), obj=$('.food_num');


		if(c==1){
        $('.food_num').append('<div id="zhan"><img src="images/food.png" width="29px" height="28px"><b>+1</b></div>');
        }
        else if(c==10){
        	$('.food_num').append('<div id="zhan"><img src="images/food.png" width="29px" height="28px"><b>+10</b></div>');
        }
        else if(c==100){
        	$('.food_num').append('<div id="zhan"><img src="images/food.png" width="29px" height="28px"><b>+100</b></div>');
        }
        else if(c==1000){
        	$('.food_num').append('<div id="zhan"><img src="images/food.png" width="29px" height="28px"><b>+1000</b></div>');
        }


        $('#zhan').css({'position':'absolute','z-index':'1','color':'#C30',
        'left':left+'px','top':top+'px','font-size':'24px'});
        $('#zhan').animate({top:top-80,opacity: 0},1000,
        function(){
            $(this).fadeOut(1500).remove();
            var Num = parseInt(obj.text());
               Num=Num+c;
               obj.text(Num);
        });
    return false;



	})*/


/*弹框2*/
function aa(){
    alertMsg_a("恭喜您充值成功了！", 0)
}

function alertMsg_a(msg, mode) { //mode为空，即只有一个确认按钮，mode为1时有确认和取消两个按钮
        msg = msg || '';
        mode = mode || 0;
        var top = document.body.scrollTop || document.documentElement.scrollTop;
        var isIe = (document.all) ? true : false;
        var isIE6 = isIe && !window.XMLHttpRequest;
        var sTop = document.documentElement.scrollTop || document.body.scrollTop;
        var sLeft = document.documentElement.scrollLeft || document.body.scrollLeft;
        var winSize = function(){
            var xScroll, yScroll, windowWidth, windowHeight, pageWidth, pageHeight;
            // innerHeight获取的是可视窗口的高度，IE不支持此属性
            if (window.innerHeight && window.scrollMaxY) {
                xScroll = document.body.scrollWidth;
                yScroll = window.innerHeight + window.scrollMaxY;
            } else if (document.body.scrollHeight > document.body.offsetHeight) { // all but Explorer Mac
                xScroll = document.body.scrollWidth;
                yScroll = document.body.scrollHeight;
            } else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
                xScroll = document.body.offsetWidth;
                yScroll = document.body.offsetHeight;
            }

            if (self.innerHeight) {    // all except Explorer
                windowWidth = self.innerWidth;
                windowHeight = self.innerHeight;
            } else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
                windowWidth = document.documentElement.clientWidth;
                windowHeight = document.documentElement.clientHeight;
            } else if (document.body) { // other Explorers
                windowWidth = document.body.clientWidth;
                windowHeight = document.body.clientHeight;
            }

            // for small pages with total height less then height of the viewport
            if (yScroll < windowHeight) {
                pageHeight = windowHeight;
            } else {
                pageHeight = yScroll;
            }

            // for small pages with total width less then width of the viewport
            if (xScroll < windowWidth) {
                pageWidth = windowWidth;
            } else {
                pageWidth = xScroll;
            }

            return{
                'pageWidth':pageWidth,
                'pageHeight':pageHeight,
                'windowWidth':windowWidth,
                'windowHeight':windowHeight
            }
        }();
        //alert(winSize.pageWidth);
        //遮罩层
        var styleStr1 = 'top:0;left:0;position:absolute;z-index:10000;background:#000000;width:' + winSize.pageWidth + 'px;height:' +  (winSize.pageHeight + 30) + 'px;';
        styleStr1 += (isIe) ? "filter:alpha(opacity=60);" : "opacity:0.6;"; //遮罩层DIV
        var shadowDiv1 = document.createElement('div'); //添加阴影DIV
        shadowDiv1.style.cssText = styleStr1; //添加样式
        shadowDiv1.id = "shadowDiv1";
        
        document.body.insertBefore(shadowDiv1, document.body.firstChild); //遮罩层加入文档
        //弹出框
        var styleStr2 = 'display:block;position:fixed;_position:absolute;left:' + (winSize.windowWidth / 2 - 150) + 'px;top:' + (winSize.windowHeight / 2 - 150) + 'px;_top:' + (winSize.windowHeight / 2 + top - 150)+ 'px;'; //弹出框的位置
        var alertBox1 = document.createElement('div');
        alertBox1.id = 'alertMsg1';
        alertBox1.style.cssText = styleStr2;

        //创建关闭按钮
        var alertClose_btn1 = document.createElement('a');
        alertClose_btn1.id = 'alertClose_btn1';
        alertClose_btn1.innerHTML = "";
        alertBox1.appendChild(alertClose_btn1);

        alertClose_btn1.onclick = function () {
            document.body.removeChild(alertBox1);
            document.body.removeChild(shadowDiv1);
            return true;
        };

        //创建弹出框里面的内容P标签
        var alertMsg1_info = document.createElement('P');
        alertMsg1_info.id = 'alertMsg1_info';
        alertMsg1_info.innerHTML = "本次打赏";
        alertBox1.appendChild(alertMsg1_info);

        var alertMsg1_info_food = document.createElement('span');
        alertMsg1_info_food.id = 'alertMsg1_info_food';
        var f = parseInt(document.getElementById("g_num").innerHTML);
        alertMsg1_info_food.innerHTML = f;
        alertMsg1_info.appendChild(alertMsg1_info_food);

        var alertMsg1_info_a = document.createElement('span');
        alertMsg1_info_a.id = 'alertMsg1_info_a';
        alertMsg1_info_a.innerHTML = "份口粮";
        alertMsg1_info.appendChild(alertMsg1_info_a);

        var alertMsg1_info1 = document.createElement('P');
        alertMsg1_info1.id = 'alertMsg1_info1';
        alertMsg1_info1.innerHTML = "需要花费您：";
        alertBox1.appendChild(alertMsg1_info1);


         var alertMsg1_gold_wrap = document.createElement('div');
        alertMsg1_gold_wrap.id = 'alertMsg1_gold_wrap';
        alertBox1.appendChild(alertMsg1_gold_wrap);


        var alertMsg1_gold = document.createElement('img');
        alertMsg1_gold.src="images/gold.png";
        alertMsg1_gold.id = 'alertMsg1_gold';
        alertMsg1_gold_wrap.appendChild(alertMsg1_gold);


        var alertMsg1_goldNum = document.createElement('div');
        alertMsg1_goldNum.id = 'alertMsg1_goldNum';
        var f = parseInt(document.getElementById("g_num").innerHTML);
        alertMsg1_goldNum.innerHTML = f;
        alertMsg1_gold_wrap.appendChild(alertMsg1_goldNum);

        //创建按钮
        var a_btn1 = document.createElement('a');
        a_btn1.id = 'alertMsg1_btn1';
        a_btn1.href = 'javas' + 'cript:void(0)';
        a_btn1.innerHTML = '<cite>再想想</cite>';
        a_btn1.onclick = function () {
            document.body.removeChild(alertBox1);
            document.body.removeChild(shadowDiv1);

            return true;
        };
        alertBox1.appendChild(a_btn1);

        var a_btn2 = document.createElement('a');
        a_btn2.id = 'alertMsg1_btn2';
        a_btn2.href = 'javas' + 'cript:void(0)';
        a_btn2.innerHTML = '<cite>没问题</cite>';
        a_btn2.onclick = function () {
            document.body.removeChild(alertBox1);
            document.body.removeChild(shadowDiv1);
            
            document.getElementById("give_sc").style.display="block";
            setTimeout("close_give_sc()",1000);
            return true;
        };
        alertBox1.appendChild(a_btn2);            
        document.body.appendChild(alertBox1);
    }

function close_give_sc(){
	document.getElementById("give_sc").style.display="none";
}

/*window.location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx60351bb051c0a468&redirect_uri=http%3A%2F%2Fkouliang.tuturead.com&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect"*/

