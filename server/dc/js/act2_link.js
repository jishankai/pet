/*倒计时*/

window.onload=function(){
     FreshTime();
    }
function FreshTime()
{
        var endtime=new Date("2015/5/15,12:20:12");//结束时间
        var nowtime = new Date();//当前时间

        var lefttime= parseInt((endtime.getTime()-nowtime.getTime())/1000); 
        h=  parseInt(lefttime/(60*60)%24);
        m=  parseInt(lefttime/(60)%60);
        s=  parseInt(lefttime%60);
        
        if(h < 10){
            h="0"+h;
        }
        if(m<10){
            m="0"+m;
        }
        if(s<10){
            s="0"+s;
        }
       
        document.getElementById("d_time").innerHTML=h+":"+m+":"+s;
        if(lefttime<=0){
        document.getElementById("d_time").innerHTML="已结束";
        }

        setTimeout(FreshTime,1000);
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

/*赏*/

$("#reward").click(function(){
    var b=parseInt($("#target_num").html());
    var c=parseInt($("#g_num").html());
    $("#target_num").html(b-c);
    var d=parseInt($("#target_num").html());

    /*进度条*/
    var total=100000;
    var pwidth=(total-d)/total*100+"%";
    $("#abar").width(pwidth);

    /*弹出框*/

    $("#shadow_Box").css('display','block');
    $("#t2").css('display','block');

});
/*大图显示*/
 var zWin = $(window);
 $('#act2_imgBox img').click(function(){  

        $('#large_container').css({
            width:zWin.width(),
            height:zWin.height()
        }).show();
        var a=document.getElementById("large_img")
        a.src = this.src;

        $('#large_img').css({
            width:zWin.width(),
            height:zWin.height()
        });

    });

/*触屏时单击关闭*/
  $('#large_container').tap(function(){
        $('#container').css({height:'auto','overflow':'auto'})
        $('#large_container').hide();
    });

/*左滑屏幕*/

$('#large_container').swipeLeft(function(){
        var a=document.getElementById("large_img");
        a.src="css/img/2.png";       
});

$('#large_container').swipeRight(function(){
     var a=document.getElementById("large_img");
        a.src="css/img/1.png"; 
});

 
