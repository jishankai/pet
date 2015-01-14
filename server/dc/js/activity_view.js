/*倒计时*/

window.onload=function(){
     FreshTime();
    }
function FreshTime()
{
        var endtime=new Date("2015/1/22,00:00:00");//结束时间
        var nowtime = new Date();//当前时间

        var lefttime= parseInt((endtime.getTime()-nowtime.getTime())/1000);
        d=  parseInt(lefttime/3600/24);
        h=  parseInt(lefttime/(60*60)%24);
        m=  parseInt(lefttime/(60)%60);
        /*s=  parseInt(lefttime%60);*/

        if(h < 10){
            h="0"+h;
        }
        if(m<10){
            m="0"+m;
        }
        /*if(s<10){
            s="0"+s;
        }*/

        document.getElementById("d_time").innerHTML="倒计时："+d+"天"+h+"时"+m+"分";
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

/*$("#reward").click(function(){
    var b=parseInt($("#target_num").html());
    var c=parseInt($("#g_num").html());
    $("#target_num").html(b-c);
    var d=parseInt($("#target_num").html());

    进度条
    var total=100000;
    var pwidth=(total-d)/total*100+"%";
    $("#abar").width(pwidth);*/

    /*弹出框*/
    $("#share_box").click(function(){
        $(this).css('display','none');
    });
/*
    $("#shadow_Box").css('display','block');
    $("#t2").css('display','block');

});
*/
