
window.onload=function(){

	var followBtn=document.getElementById("bt_follow");

	/*alert(followBtn.src);*/
	if(followBtn.src=="http://192.168.10.151:8001/css/images/bt_follow.png"){
		followBtn.onclick = function(){
			openFollow();
			/*follow3Open();*/
		}
	}else{
		followBtn.onclick = function(){
			/*openFollow();*/
			follow3Open();
		}
	}
	
	

}
/*捧弹框1*/
function openFollow(){

	//屏幕的高度和宽度
	var sHeight=document.documentElement.scrollHeight;
	var sWidth=document.documentElement.scrollWidth;
	//可视区域的高度
	var wHeight=document.documentElement.clientHeight;

	var fMask = document.createElement("div");
		fMask.id="fMask";
		fMask.className="mask";
		fMask.style.height = sHeight + "px";
		fMask.style.width = sWidth + "px";
		document.body.appendChild(fMask);

	var follow = document.createElement("div");
		follow.id = "follow";
		follow.className = "follow";
		follow.innerHTML ="<img src='css/images/dialog_close_white.png' id='fclose' class='close'><h3 class='f1_top'>捧了人家可要对人家负责呀～</h3><h3>一定让TA成为宇宙中最闪亮的萌星～</h3><p>温馨提示：每人可免费捧10个萌星～</p><a href='#' id='f1_no_problem'>没问题</a>"

		/*<h3>捧了人家可要对人家负责呀～</h3>
		<h3>一定让TA成为宇宙中最闪亮的萌星～</h3>
		<p>温馨提示：每人可免费捧10个萌星～</p>
		<a href="#" id="f1_no_problem">没问题</a>*/

		document.body.appendChild(follow);

	var fHeight = follow.offsetHeight;
	var fWidth = follow.offsetWidth;
	

		follow.style.left=(sWidth-fWidth)/2 + "px";
		follow.style.top=(wHeight-fHeight)/2+"px";
		

	var fClose = document.getElementById("fclose");
		fMask.onclick=fClose.onclick=function(){

						document.body.removeChild(fMask);
						document.body.removeChild(follow);					    
		}

	var noProblem = document.getElementById("f1_no_problem");
		noProblem.onclick = function(){
			document.body.removeChild(follow);	
			follow2Open();
		}
}

/*捧弹框2*/
function follow2Open(){
	//屏幕的高度和宽度
	var sHeight=document.documentElement.scrollHeight;
	var sWidth=document.documentElement.scrollWidth;
	//可视区域的高度
	var wHeight=document.documentElement.clientHeight;

	var follow2 = document.createElement("div");
		follow2.id = "follow2";
		follow2.className = "follow2";
		follow2.innerHTML ="<h3>成功捧TA</h3><div class='headerBox'><img src='css/images/a4.jpg' id='header'></div><h4><span id='pet_name'>虾饺</span> 接纳你成为</h4><h4 id='fans_grade'>凉粉一枚</h4><p>TA在过去的历史中</p><p>击败了<span id='percent'> 70% </span>的萌星</p><p>期待您的加入让TA明天更加辉煌</p><a href='#' id='f2Btn'>知道了</a>"
		
		/*<h3>成功捧TA</h3>
		<div><img src='css/images/a5.jpg' id="header"></div>
		<h4><span id="pet_name">虾饺</span>接纳你成为</h4>
		<h4 id="fans_grade">凉粉一枚</h4>
		<p>TA在过去的历史中</p>
		<p>击败了<span id='percent'>70%</span>的萌星</p>
		<p>期待您的加入让TA明天更加辉煌</p>
		<a href='#'>知道了</a>
		*/
		/*弹框里的头像*/
		document.body.appendChild(follow2);
		var header=document.getElementById('header');
		var pi_header=document.getElementById('pi_head');
		header.src=pi_header.src;

		/*弹框2中需要服务器传的值*/
		var pet_name=document.getElementById('pet_name');
			pet_name.innerHTML="虾饺";
		var fans_grade=document.getElementById('fans_grade');
			fans_grade.innerHTML="凉粉一枚";
		var percent=document.getElementById('percent');
			percent.innerHTML="70%";

	var f2Height = follow2.offsetHeight;
	var f2Width = follow2.offsetWidth;
	

		follow2.style.left=(sWidth-f2Width)/2 + "px";
		follow2.style.top=(wHeight-f2Height)/2+"px";
		

		var f2Close=document.getElementById('f2Btn');
		f2Close.onclick=function(){

						document.body.removeChild(follow2);					    
						document.body.removeChild(fMask);
					}
}


/*捧弹框3*/
function follow3Open(){
	//屏幕的高度和宽度
	var sHeight=document.documentElement.scrollHeight;
	var sWidth=document.documentElement.scrollWidth;
	//可视区域的高度
	var wHeight=document.documentElement.clientHeight;

	var fMask3 = document.createElement("div");
		fMask3.id="fMask3";
		fMask3.className="mask";
		fMask3.style.height = sHeight + "px";
		fMask3.style.width = sWidth + "px";
		document.body.appendChild(fMask3);

	var follow3 = document.createElement("div");
		follow3.id = "follow3";
		follow3.className = "follow3";
		follow3.innerHTML ="<img src='css/images/dialog_close_white.png' id='fclose3' class='close'><h3 class='f3Top'>亲爱的，真的忍心不捧了吗？</h3><h3>你舍得放弃陪伴你的TA么？</h3><h3>真的要这么无情么？</h3><a href='#' id='f3Btn1'>再想想吧</a><a href='#' id='f3Btn2'>额，是的</a>";

		/*<h3>亲爱的，真的忍心不捧了吗？</h3>
		<h3>你舍得放弃陪伴你的TA么？</h3>
		<h3>真的要这么无情么？</h3>
		<a href='#' id='f3Btn1'>再想想吧</a>
		<a href='#' id='f3Btn2'>额，是的</a>*/
		

		document.body.appendChild(follow3);

	var f3Height = follow3.offsetHeight;
	var f3Width = follow3.offsetWidth;
	

		follow3.style.left=(sWidth-f3Width)/2 + "px";
		follow3.style.top=(wHeight-f3Height)/2+"px";
		

		var f3Close=document.getElementById('fclose3');
		var f3Btn1=document.getElementById('f3Btn1');
		f3Btn1.onclick = f3Close.onclick=function(){

				document.body.removeChild(follow3);					    
				document.body.removeChild(fMask3);
		}

		var f3Btn2=document.getElementById('f3Btn2');
		f3Btn2.onclick =function(){
			//取消捧的按钮
		}
}




















