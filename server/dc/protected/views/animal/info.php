<?php
require_once "jssdk.php";
$jssdk = new JSSDK(WECHAT_MP_ID, WECHAT_MP_SECRET);
$signPackage = $jssdk->GetSignPackage();
?>
<!DOCTYPE>
<html>
<head>
	<meta name="viewport" content="width = device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>宠物主页</title>
	<link rel="stylesheet" type="text/css" href="css/pet_index.css">
	<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
	<script type="text/javascript" src="js/pet_index.js"></script>
	<script>
 //    var _hmt = _hmt || [];
 //    (function() {
 //      var hm = document.createElement("script");
 //      hm.src = "//hm.baidu.com/hm.js?fffd5628b5c5fe81d7a7867d554d07ca";
 //      var s = document.getElementsByTagName("script")[0]; 
 //      s.parentNode.insertBefore(hm, s);
 //  	})();
	</script>
</head>
<body>
	<div class="wraper comWidth">
		<div class="pi_top">
			<div class="hr_125"></div>
			<div class="header clearfix">
				<img src="css/images/bg_headimg.png" class="pi_head_wrap">
				<img src="css/images/<?php echo $is_circle?"pet_raise_2.png":"bt_follow.png"?>" id="bt_follow"><!-- pet_raise_2.png -->
				<img src="http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>" id="pi_head">
				<img src="http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_usr/<?php echo $r['u_tx']?>" id="owner_head">
				<div class="owner">经纪人</div>
			</div>
		 
			<div class="pi_name"><span id="name"><?php echo $r['name']?> </span><img src="css/images/<?php echo $r['gender']==1?"male1.png":"female1.png"?>" id="pi_sex"></div>

			<div class="pet_info clearfix">
				<div id="breed"><?php echo $a_type?></div>
				<div id="vertical">|</div>
				<div id="age"><?php echo $r['age']?>个月</div>
			</div>
			<h3 id="signature"><?php echo $r['msg']?></h3>

			<ul class="tab clearfix">
				<!-- <li class="one">
					<h3 id="dynamic_num"><?php echo $r['news']?></h3>
					<p id="dynamic">动态</p>
				</li>
				<li>
					<h3 id="fans_num"><?php echo $r['fans']?></h3>
					<p id="fans">粉丝</p>
				</li>
				<li>
					<h3 id="photo_num"><?php echo $r['images']?></h3>
					<p id="photo">照片</p>
				</li> -->
				<li class="one">
					<a href='<?php //echo $this->createUrl('social/ration',array('aid'=>$r['aid'],'SID'=>$SID))?>'><img src="css/images/pet_icon1.png"></a>
				</li>
				<li>
					<img src="css/images/pet_icon2.png">
				</li>
				<li>
					<img src="css/images/pet_icon3.png">
				</li>
				<li>
					<a href='<?php //echo $this->createUrl('social/touch',array('aid'=>$r['aid'],'SID'=>$SID))?>'><img src="css/images/pet_icon4.png"></a>
				</li>
			</ul>
		</div>

		<div class="pi_body">
			<ul class="imgBox clearfix" id="imgBox">
				<?php /*foreach ($images as $key=>$image) {
					echo "<li><a href='".$this->createUrl('social/foodShareApi', array('img_id'=>$image['img_id']))."'><img src='http://".OSS_PREFIX."4upload.oss-cn-beijing.aliyuncs.com/".$image['url']."' alt='$key'></a></li>";
				}*/?>
				<!-- <li><img src="css/images/pet1.jpg" alt="1"></li>
				<li><img src="css/images/pet2.jpg" alt="2"></li>
				<li><img src="css/images/pet3.jpg" alt="3"></li>
				<li><img src="css/images/pet4.jpg" alt="4"></li>
				<li><img src="css/images/pet5.jpg" alt="5"></li>
				<li><img src="css/images/pet6.jpg" alt="6"></li>
				<li><img src="css/images/pet7.jpg" alt="7"></li>
				<li><img src="css/images/pet8.jpg" alt="8"></li>
				<li><img src="css/images/pet9.jpg" alt="9"></li> -->
			</ul>
		</div>
		<div class="hr_125"></div>
		
		<div class="float">
		<div class="load_shadow"><img src="css/images/load_shadow.png"></div>
			<div class="load">

				<div class="load_left">
					<div class="logo_icon">
						<img src="css/images/logo_icon.jpg"/>
					</div>
					<div class="load_info">
						<h3>宠物星球</h3>
						<p>我为自己代粮</p>
					</div>
				</div>
				<div class="load_right">
					<img src="css/images/load.jpg"/>
				</div>
			</div>
		</div>

	</div>
</body>
<script type="text/javascript">
function waterfall(parent,box){
	var oParent=document.getElementById("imgBox");
	var oBoxs=getByClass(oParent,box);
	var oBoxW=oBoxs[0].offsetHeight;
	var cols=Math.floor(oParent.offsetHeight/oBoxW);
	
	var hArr=[];
	for(var i=0;i<oBoxs.length;i++){
		if(i<cols+1){
			hArr.push(oBoxs[i].offsetHeight);
		}else{
			var minH=Math.min.apply(null,hArr);

			var index=getminHIndex(hArr,minH);
		
			oBoxs[i].style.position='absolute';
			oBoxs[i].style.top=minH+'px';
			//oBoxs[i].style.left=oBoxW*index+'px';
			oBoxs[i].style.left=oBoxs[index].offsetLeft+'px';
			hArr[index]+=oBoxs[i].offsetHeight;
		}
	}	
}

function getByClass(parent,clsName){
	var boxArr=new Array(),
		oElements=parent.getElementsByTagName('*');
	for(var i=0;i<oElements.length;i++){
		if(oElements[i].className==clsName){
			boxArr.push(oElements[i]);
		}
	}
	return boxArr;
}

function getminHIndex(arr,minH){
    for(var i in arr){
        if(arr[i]==minH){
            return i;
        }
    }
}

$(window).on('load',function(){
	var oParent=document.getElementById('imgBox');
	<?php foreach ($images as $key => $image) { ?>
		var oLi=document.createElement('li');
			oLi.className='box';
			oParent.appendChild(oLi);
		var oA=document.createElement('a');
			oA.setAttribute('href',"<?php echo $this->createUrl('social/foodShareApi', array('img_id'=>$image['img_id'], 'SID'=>$SID))?>");
			oLi.appendChild(oA);
		var oImg=document.createElement('img');
			oImg.src="<?php echo "http://".OSS_PREFIX."4upload.oss-cn-beijing.aliyuncs.com/".$image['url']?>";
			oA.appendChild(oImg);
	<?php }?>
	if(<?php echo $percent?>) {
		follow2Open('<?php echo $r['name']?>', <?php echo $percent?>);
	}

	// waterfall('imgBox','box');
	var followBtn=document.getElementById("bt_follow");

	/*alert(followBtn.src);*/
	if(followBtn.src=="http://"+window.location.host+"/css/images/bt_follow.png"){
		followBtn.onclick = function(){
			location.href = '<?php echo $this->createUrl('animal/joinMobileApi', array('aid'=>$r["aid"], 'SID'=>$SID))?>';
			/*follow3Open();*/
		}
	}else{
		followBtn.onclick = function(){
			/*openFollow();*/
			// follow3Open();
		}
	}
})

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

	var follow_imgBox=document.createElement('div');
		follow_imgBox.id="follow_imgBox";
		document.body.appendChild(follow_imgBox);

	var follow_img = document.createElement('img');
		follow_img.id="follow_img";
		follow_img.src="css/images/follow.gif";
		follow_imgBox.appendChild(follow_img);

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
		
	var fImg_w=follow_img.offsetWidth;

		follow_imgBox.style.left=(sWidth-fImg_w)/2 + "px";
		follow_imgBox.style.top=(wHeight-fHeight)/2-90+"px";

	var fClose = document.getElementById("fclose");
		fMask.onclick=fClose.onclick=function(){

						document.body.removeChild(fMask);
						document.body.removeChild(follow);	
						document.body.removeChild(follow_imgBox);				    
		}

	var noProblem = document.getElementById("f1_no_problem");
		noProblem.onclick = function(){
			document.body.removeChild(follow);	
			document.body.removeChild(follow_imgBox);
			
		}
}

/*捧弹框2*/
function follow2Open(a,b){
	//屏幕的高度和宽度
	var sHeight=document.documentElement.scrollHeight;
	var sWidth=document.documentElement.scrollWidth;
	//可视区域的高度
	var wHeight=document.documentElement.clientHeight;

	var fMask2 = document.createElement("div");
		fMask2.id="fMask2";
		fMask2.className="mask";
		fMask2.style.height = sHeight + "px";
		fMask2.style.width = sWidth + "px";
		document.body.appendChild(fMask2);

	var follow_imgBox=document.createElement('div');
		follow_imgBox.id="follow_imgBox";
		document.body.appendChild(follow_imgBox);

	var follow_img = document.createElement('img');
		follow_img.id="follow_img";
		follow_img.src="css/images/follow.gif";
		follow_imgBox.appendChild(follow_img);

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
			pet_name.innerHTML=a;
		var fans_grade=document.getElementById('fans_grade');
			fans_grade.innerHTML="凉粉一枚";
		var percent=document.getElementById('percent');
			percent.innerHTML=b+"%";

	var f2Height = follow2.offsetHeight;
	var f2Width = follow2.offsetWidth;
	
		follow2.style.left=(sWidth-f2Width)/2 + "px";
		follow2.style.top=(wHeight-f2Height)/2+"px";

	var fImg_w=follow_img.offsetWidth;

		follow_imgBox.style.left=(sWidth-fImg_w)/2 - 120+"px";
		follow_imgBox.style.top=(wHeight-f2Height)/2+10+"px";
		

		var f2Close=document.getElementById('f2Btn');
		f2Close.onclick=function(){

						document.body.removeChild(follow2);					    
						document.body.removeChild(fMask2);
						document.body.removeChild(follow_imgBox);
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
</script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
  // 注意：所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。 
  // 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
  // 完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
  wx.config({
    appId: '<?php echo $signPackage["appId"];?>',
    timestamp: <?php echo $signPackage["timestamp"];?>,
    nonceStr: '<?php echo $signPackage["nonceStr"];?>',
    signature: '<?php echo $signPackage["signature"];?>',
    jsApiList: [
      // 所有要调用的 API 都要加到这个列表中
      'checkJsApi',
      'onMenuShareTimeline',
      'onMenuShareAppMessage',
      'onMenuShareQQ',
      'onMenuShareWeibo',
    ]
  });
  wx.ready(function () {
    // 在这里调用 API
    var shareData = {
        title: "我是<?php echo $r['name']?>，来自宠物星球的大萌星！",
        desc: "人家在宠物星球好开心，快来跟我一起玩嘛~",
        link: "http://"+window.location.host+"/index.php?r=animal/infoShare&aid=<?php echo $r['aid']?>", 
        imgUrl: "http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>"
    };
    wx.onMenuShareAppMessage(shareData);
    wx.onMenuShareTimeline(shareData);
    wx.onMenuShareQQ(shareData);
    wx.onMenuShareWeibo(shareData);
  });
</script>
</html>
