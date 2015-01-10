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

/*window.location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx60351bb051c0a468&redirect_uri=http%3A%2F%2Fkouliang.tuturead.com&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect"*/

