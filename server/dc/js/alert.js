/*弹框1*/

function bb(){
    alertMsg("恭喜您充值成功了！", 0)
}

function alertMsg(msg, mode) { //mode为空，即只有一个确认按钮，mode为1时有确认和取消两个按钮
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
        var styleStr = 'top:0;left:0;position:absolute;z-index:10000;background:#000000;width:' + winSize.pageWidth + 'px;height:' +  (winSize.pageHeight + 30) + 'px;';
        styleStr += (isIe) ? "filter:alpha(opacity=60);" : "opacity:0.6;"; //遮罩层DIV
        var shadowDiv = document.createElement('div'); //添加阴影DIV
        shadowDiv.style.cssText = styleStr; //添加样式
        shadowDiv.id = "shadowDiv";

        document.body.insertBefore(shadowDiv, document.body.firstChild); //遮罩层加入文档
        //弹出框
        var styleStr1 = 'display:block;position:fixed;_position:absolute;left:' + (winSize.windowWidth / 2 - 150) + 'px;top:' + (winSize.windowHeight / 2 - 150) + 'px;_top:' + (winSize.windowHeight / 2 + top - 150)+ 'px;'; //弹出框的位置
        var alertBox = document.createElement('div');
        alertBox.id = 'alertMsg';
        alertBox.style.cssText = styleStr1;

        //创建关闭按钮
        var alertClose_btn = document.createElement('a');
        alertClose_btn.id = 'alertClose_btn';
        alertClose_btn.innerHTML = "";
        alertBox.appendChild(alertClose_btn);

        alertClose_btn.onclick = function () {
            document.body.removeChild(alertBox);
            document.body.removeChild(shadowDiv);
            return true;
        };

        //创建弹出框里面的内容P标签
        var alertMsg_info = document.createElement('P');
        alertMsg_info.id = 'alertMsg_info';
        alertMsg_info.innerHTML = "在您的帮助下，已为";
        alertBox.appendChild(alertMsg_info);

        var alertMsg_name = document.createElement('span');
        alertMsg_name.id = 'alertMsg_name';
        alertMsg_name.innerHTML = "{群护区名字}";
        alertMsg_info.appendChild(alertMsg_name);


        var alertMsg_mm = document.createElement('span');
        alertMsg_mm.id = 'alertMsg_mm';
        alertMsg_mm.innerHTML = "的喵喵们";
        alertMsg_info.appendChild(alertMsg_mm);

        var alertMsg_info1 = document.createElement('P');
        alertMsg_info1.id = 'alertMsg_info1';
        alertMsg_info1.innerHTML = "募集了";
        alertBox.appendChild(alertMsg_info1);

        var alertMsg_snum = document.createElement('span');
        alertMsg_snum.id = 'alertMsg_snum';
        var total=parseInt(document.getElementById("total").innerHTML);
        var a=parseInt(document.getElementById("target_num").innerHTML);
        var c=total-a;
        alertMsg_snum.innerHTML = c;
        alertMsg_info1.appendChild(alertMsg_snum);

        var alertMsg_snum_info = document.createElement('span');
        alertMsg_snum_info.id = 'alertMsg_snum_info';
        alertMsg_snum_info.innerHTML = "份粮食～";
        alertMsg_info1.appendChild(alertMsg_snum_info);

        var alertMsg_info2 = document.createElement('P');
        alertMsg_info2.id = 'alertMsg_info2';
        alertMsg_info2.innerHTML = "目标还差";
        alertBox.appendChild(alertMsg_info2);

        var alertMsg_cnum = document.createElement('span');
        alertMsg_cnum.id = 'alertMsg_cnum';
        var a=parseInt(document.getElementById("target_num").innerHTML);
        alertMsg_cnum.innerHTML =a;
        alertMsg_info2.appendChild(alertMsg_cnum);

        var alertMsg_cnum_info = document.createElement('span');
        alertMsg_cnum_info.id = 'alertMsg_cnum_info';
        alertMsg_cnum_info.innerHTML = "份 ";
        alertMsg_info2.appendChild(alertMsg_cnum_info);




        var alertMsg1_clock = document.createElement('img');
        alertMsg1_clock.src="css/images/clock.png";
        alertMsg1_clock.id = 'alertMsg1_clock';
        alertMsg_info2.appendChild(alertMsg1_clock);



        var alertMsg_dtime = document.createElement('span');
        alertMsg_dtime.id = 'alertMsg_dtime';
        var dj=document.getElementById("d_time").innerHTML;
        alertMsg_dtime.innerHTML = dj.substr(4);
        alertMsg_info2.appendChild(alertMsg_dtime);


        //创建按钮
        var btn1 = document.createElement('a');
        btn1.id = 'alertMsg_btn1';
        btn1.href = 'javas' + 'cript:void(0)';
        btn1.innerHTML = '<cite>分享</cite>';
        btn1.onclick = function () {
            document.body.removeChild(alertBox);
            document.body.removeChild(shadowDiv);
            cc();
            return true;
        };
        alertBox.appendChild(btn1);


        var alertMsg_share = document.createElement('P');
        alertMsg_share.id = 'alertMsg_share';
        alertMsg_share.innerHTML = "快分享给小伙伴，";
        alertBox.appendChild(alertMsg_share);

        var alertMsg_share_red = document.createElement('span');
        alertMsg_share_red.id = 'alertMsg_share_red';
        alertMsg_share_red.innerHTML = "每天";
        alertMsg_share.appendChild(alertMsg_share_red);

        var alertMsg_share1 = document.createElement('span');
        alertMsg_share1.id = 'alertMsg_share1';
        alertMsg_share1.innerHTML = "都有免费的份数哦～～";
        alertMsg_share.appendChild(alertMsg_share1);

        document.body.appendChild(alertBox);
    }


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
        alertMsg1_gold.src="css/images/gold.png";
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
            bb();
            return true;
        };
        alertBox1.appendChild(a_btn2);


        document.body.appendChild(alertBox1);
    }

/*弹框3*/

function cc(){
    alertMsg_b("恭喜您充值成功了！", 0)
}

function alertMsg_b(msg, mode) { //mode为空，即只有一个确认按钮，mode为1时有确认和取消两个按钮
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
        var styleStr2 = 'top:0;left:0;position:absolute;z-index:10000;background:#000000;width:' + winSize.pageWidth + 'px;height:' +  (winSize.pageHeight + 30) + 'px;';
        styleStr2 += (isIe) ? "filter:alpha(opacity=60);" : "opacity:0.6;"; //遮罩层DIV
        var shadowDiv2 = document.createElement('div'); //添加阴影DIV
        shadowDiv2.style.cssText = styleStr2; //添加样式
        shadowDiv2.id = "shadowDiv2";

        document.body.insertBefore(shadowDiv2, document.body.firstChild); //遮罩层加入文档
        //弹出框
        var styleStr3 = 'display:block;position:fixed;_position:absolute;left:' + (winSize.windowWidth / 2 - 150) + 'px;top:' + (winSize.windowHeight / 2 - 150) + 'px;_top:' + (winSize.windowHeight / 2 + top - 150)+ 'px;'; //弹出框的位置
        var alertBox2 = document.createElement('div');
        alertBox2.id = 'alertMsg2';
        alertBox2.style.cssText = styleStr3;

        //创建关闭按钮
        var alertClose_btn2 = document.createElement('a');
        alertClose_btn2.id = 'alertClose_btn2';
        alertClose_btn2.innerHTML = "";
        alertBox2.appendChild(alertClose_btn2);

        alertClose_btn2.onclick = function () {
            document.body.removeChild(alertBox2);
            document.body.removeChild(shadowDiv2);
            return true;
        };

        //创建弹出框里面的内容P标签
        var alertMsg2_info = document.createElement('P');
        alertMsg2_info.id = 'alertMsg2_info';
        alertMsg2_info.innerHTML = "今天的免费份数用光啦";
        alertBox2.appendChild(alertMsg2_info);

        var alertMsg21_info = document.createElement('P');
        alertMsg21_info.id = 'alertMsg21_info';
        alertMsg21_info.innerHTML = "明天还有更多免费份数任你壕！";
        alertBox2.appendChild(alertMsg21_info);


        var alertMsg21_checkbox = document.createElement('input');
        alertMsg21_checkbox.id = 'alertMsg21_checkbox';
        alertMsg21_checkbox.type="checkbox";
        alertMsg21_checkbox.checked="true";
       
        alertBox2.appendChild(alertMsg21_checkbox);


        var alertMsg22_info = document.createElement('label');
        alertMsg22_info.id = 'alertMsg22_info';
        alertMsg22_info.innerHTML = "分享给小伙伴，召集更多爱心小天使~";
        alertBox2.appendChild(alertMsg22_info);



 
        //创建按钮
        var b_btn1 = document.createElement('a');
        b_btn1.id = 'alertMsg2_btn1';
        b_btn1.href = 'javas' + 'cript:void(0)';
        b_btn1.innerHTML = '<cite>好的</cite>';
        b_btn1.onclick = function () {
            var flag=document.getElementById("alertMsg21_checkbox").checked;
            if(flag){
                /*alert(flag);*/
                document.body.removeChild(alertBox2);
                document.body.removeChild(shadowDiv2);
                var ua = navigator.userAgent.toLowerCase();  
                if(ua.match(/MicroMessenger/i)!="micromessenger") { 
                    document.getElementById("wb").click();
                } else {
                    document.getElementById("share_box").style.display="block";
                    document.getElementById("share_img").style.display="block";
                }
                return true;
           
            }else{
                document.body.removeChild(alertBox2);
                document.body.removeChild(shadowDiv2);
                return true;
           }
        };
        alertBox2.appendChild(b_btn1);

        document.body.appendChild(alertBox2);
    }

/*金币不足时打赏*/

function no_gold(){
    alertMsg_e("恭喜您充值成功了！", 0)
}

function alertMsg_e(msg, mode) { //mode为空，即只有一个确认按钮，mode为1时有确认和取消两个按钮
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
        var alertMsg11_info = document.createElement('P');
        alertMsg11_info.id = 'alertMsg1_info';
        alertMsg11_info.innerHTML = "本次打赏";
        alertBox1.appendChild(alertMsg11_info);

        var alertMsg11_info_food = document.createElement('span');
        alertMsg11_info_food.id = 'alertMsg1_info_food';
        var f = parseInt(document.getElementById("g_num").innerHTML);
        alertMsg11_info_food.innerHTML = f;
        alertMsg11_info.appendChild(alertMsg11_info_food);

        var alertMsg11_info_a = document.createElement('span');
        alertMsg11_info_a.id = 'alertMsg1_info_a';
        alertMsg11_info_a.innerHTML = "份口粮";
        alertMsg11_info.appendChild(alertMsg11_info_a);

        var alertMsg12_info1 = document.createElement('P');
        alertMsg12_info1.id = 'alertMsg12_info1';
        alertMsg12_info1.innerHTML = "需要花费您：";
        alertBox1.appendChild(alertMsg12_info1);


        var alertMsg1_gold_wrap = document.createElement('div');
        alertMsg1_gold_wrap.id = 'alertMsg1_gold_wrap';
        alertBox1.appendChild(alertMsg1_gold_wrap);


        var alertMsg1_gold = document.createElement('img');
        alertMsg1_gold.src="css/images/gold.png";
        alertMsg1_gold.id = 'alertMsg1_gold';
        alertMsg1_gold_wrap.appendChild(alertMsg1_gold);


        var alertMsg1_goldNum = document.createElement('div');
        alertMsg1_goldNum.id = 'alertMsg1_goldNum';
        var f = parseInt(document.getElementById("g_num").innerHTML);
        alertMsg1_goldNum.innerHTML = f;
        alertMsg1_gold_wrap.appendChild(alertMsg1_goldNum);


         var alertMsg1_info1 = document.createElement('P');
        alertMsg1_info1.id = 'alertMsg1_info1';
        alertMsg1_info1.innerHTML = "金币不足，先去充值吧～";
        alertBox1.appendChild(alertMsg1_info1);


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
        a_btn2.innerHTML = '<cite>去充值</cite>';
        a_btn2.onclick = function () {
            document.body.removeChild(alertBox1);
            document.body.removeChild(shadowDiv1);
            return true;
        };
        alertBox1.appendChild(a_btn2);


        document.body.appendChild(alertBox1);
    }

$("#share_box").click(function(){
    $(this).css('display','none');
});
