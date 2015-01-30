/*选项卡*/
var oTab = document.getElementById("tabs");
var oUl = oTab.getElementsByTagName("ul")[0];
var oLis = oUl.getElementsByTagName("li");
var oDivs= oTab.getElementsByTagName("div");

for(var i= 0,len = oLis.length;i<len;i++){
   oLis[i].index = i;
   oLis[i].onclick = function() {
       for(var n= 0;n<len;n++){
           oLis[n].className = "";
           oDivs[n].className = "hide";
       }
       this.className = "on";
       oDivs[this.index].className = "";
   }
}
$(window).on('scroll',function(){

        var h1=$(document).height();
        var wh=$(window).height();    
        if($(window).scrollTop()>=h1-wh-200){
            // loadData();  
        }
    })


var page=0;
function loadData(){

     var dataInt={'name':[{'name':'123'},{'name':'456'},{'name':'789'}],
    'head':[{'src':'pet1.jpg'},{'src':'pet2.jpg'},{'src':'pet3.jpg'}],
    'name1':[{'name':'223'},{'name':'333'},{'name':'444'}],
    'head1':[{'src':'pet4.jpg'},{'src':'pet5.jpg'},{'src':'pet6.jpg'}],
    'name2':[{'name':'333'},{'name':'444'},{'name':'555'}],
    'head2':[{'src':'pet1.jpg'},{'src':'pet3.jpg'},{'src':'pet5.jpg'}],
    'name3':[{'name':'456'},{'name':'555'},{'name':'789'}],
    'head3':[{'src':'pet1.jpg'},{'src':'pet2.jpg'},{'src':'pet4.jpg'}],
    'data':[{'data':'123456'},{'data':'4567899'},{'data':'0987654'}],
    'time1':[{'time':'刚刚'},{'time':'刚刚'},{'time':'刚刚'}]};
    
    
    page=page+1;
    if(page>2){dataInt=null;}
/*$.getJSON("http://www.baidu.com",function(dataInt){*/
    if(dataInt!=null){
        if(dataInt.name!=null){
            for(var i=0;i<dataInt.name.length;i++){

                var oLi_like=$('<li>').appendTo($('#like_list'));
                var oSpan_like=$('<span>').addClass('left').appendTo($(oLi_like));
                var oImg_like=$('<img>').attr('src','css/images/'+$(dataInt.head[i]).attr('src')).appendTo($(oSpan_like));
                var oH3_like=$('<h3>').addClass('left').html($(dataInt.name[i]).attr('name')).appendTo($(oLi_like));
            }
        }
        if(dataInt.name1!=null){
            for(var i=0;i<dataInt.name1.length;i++){
                var oLi_gift=$('<li>').appendTo($('#gift_list'));
                var oSpan_gift=$('<span>').addClass('left').appendTo($(oLi_gift));
                var oImg_gift=$('<img>').attr('src','css/images/'+$(dataInt.head1[i]).attr('src')).appendTo($(oSpan_gift));
                var oH3_gift=$('<h3>').addClass('left').html($(dataInt.name1[i]).attr('name')).appendTo($(oLi_gift));
            }
        }
        if(dataInt.name2!=null){
            for(var i=0;i<dataInt.name2.length;i++){
                var oLi_comment=$('<li>').appendTo($('#comment_list'));
                var oSpan_comment=$('<span>').addClass('left').appendTo($(oLi_comment));
                var oImg_comment=$('<img>').attr('src','css/images/'+$(dataInt.head2[i]).attr('src')).appendTo($(oSpan_comment));
                var oH3_comment=$('<h3>').addClass('left').html($(dataInt.name2[i]).attr('name')).appendTo($(oLi_comment));
            }
        }
        if(dataInt.name3!=null){
            for(var i=0;i<dataInt.name3.length;i++){
                var oLi_share=$('<li>').appendTo($('#share_list'));
                var oSpan_share=$('<span>').addClass('left').appendTo($(oLi_share));
                var oImg_share=$('<img>').attr('src','css/images/'+$(dataInt.head3[i]).attr('src')).appendTo($(oSpan_share));
                var oDiv_share=$('<div>').addClass('left').appendTo($(oLi_share));
                var oP_share=$('<p>').appendTo($(oDiv_share));
                var oB_share=$('<b>').html($(dataInt.name3[i]).attr('name')).appendTo($(oP_share));
                var oSpanTime_share=$('<span>').addClass('time').addClass('right').html($(dataInt.time1[i]).attr('time')).appendTo($(oP_share));
                var oP2_share=$('<p>').html($(dataInt.data[i]).attr('data')).appendTo($(oDiv_share));
            }
        }
    

    }

}
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

