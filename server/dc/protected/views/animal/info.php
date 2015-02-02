<!DOCTYPE>
<html>
<head>
	<meta name="viewport" content="width = device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>宠物主页</title>
	<link rel="stylesheet" type="text/css" href="css/pet_index.css">
	<script type="text/javascript" src="js/jquery-1.11.1.js"></script>
</head>
<body>
	<div class="wraper comWidth">
		<div class="pi_top">
			<div class="hr_125"></div>
			<div class="header clearfix">
				<img src="css/images/bg_headimg.png" class="pi_head_wrap">
				<img src="css/images/bt_follow.png" id="bt_fllow">
				<img src="http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_ani/<?php echo $r['tx']?>" id="pi_head">
				<img src="http://<?php echo OSS_PREFIX?>4tx.oss-cn-beijing.aliyuncs.com/tx_usr/<?php echo $r['u_tx']?>" id="owner_head">
				<div class="owner">经纪人</div>
			</div>
		 
			<div class="pi_name"><span id="name"><?php echo $r['name']?> </span><img src="css/images/female1.png" id="pi_sex"></div>

			<div class="pet_info clearfix">
				<div id="breed"><?php echo $a_type?></div>
				<div id="vertical">|</div>
				<div id="age"><?php echo $r['age']?>月</div>
			</div>
			<h3 id="signature"><?php echo $r['cmt']?></h3>

			<ul class="tab clearfix">
				<li>
					<h3 id="dynamic_num"><?php echo $r['news']?></h3>
					<p id="dynamic">动态</p>
				</li>
				<li class="middle">
					<h3 id="fans_num"><?php echo $r['fans']?></h3>
					<p id="fans">粉丝</p>
				</li>
				<li>
					<h3 id="photo_num"><?php echo $r['images']?></h3>
					<p id="photo">照片</p>
				</li>
			</ul>
		</div>

		<div class="pi_body">
			<ul class="imgBox clearfix" id="imgBox">
				<?php foreach ($images as $key=>$image) {
					echo "<li><a href='".$this->createUrl('social/foodShareApi', array('img_id'=>$image['img_id'])."'><img src='http://".OSS_PREFIX."4upload.oss-cn-beijing.aliyuncs.com/".$image['url'])."' alt='$key'></li>";
				}?>
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

$(window).on('load',function(){
	var h=$('#imgBox li').width();
	console.log(h);
	$('#imgBox li').height(h);
	var dataInt={'data':[{'src':'pet1.jpg'},{'src':'pet2.jpg'},{'src':'pet3.jpg'},
	{'src':'pet4.jpg'},{'src':'pet3.jpg'},{'src':'pet3.jpg'},{'src':'pet3.jpg'},
	{'src':'pet3.jpg'},{'src':'pet0.jpg'},{'src':'pet8.jpg'},{'src':'pet3.jpg'},
	{'src':'pet3.jpg'},{'src':'pet3.jpg'},{'src':'pet3.jpg'},{'src':'pet3.jpg'}
	,{'src':'pet3.jpg'},{'src':'pet4.jpg'},{'src':'pet4.jpg'},{'src':'pet3.jpg'},
	{'src':'pet3.jpg'},{'src':'pet3.jpg'},{'src':'pet6.jpg'}]};
	var a = $('#imgBox img');
	
	$get("http://www.baidu.com",function(data){
		for(var i=0;i<data.length;i++){

			var oLi=$('<li>').appendTo($('#imgBox'));
			var oImg=$('<img>').attr('src','css/images/'+$(data[i]).attr('src')).appendTo($(oLi));
			
			var h=$('#imgBox li').width();
			$('#imgBox li').height(h);

		}

	},"json");
	// for(var i=0;i<dataInt.data.length;i++){

	// 	var oLi=$('<li>').appendTo($('#imgBox'));
	// 	var oImg=$('<img>').attr('src','css/images/'+$(dataInt.data[i]).attr('src')).appendTo($(oLi));
	// 	var h=$('#imgBox li').width();
	// 	$('#imgBox li').height(h);
		
	// }
	$(window).on('scroll',function(){
		if(checkScrollSlide()){
			
				loadImg();
		
			}			
	})
})

function checkScrollSlide(){
	var $lastBox=$('#imgBox>li').last();
	var lastBoxLis = $lastBox.get(0).offsetTop + Math.floor($lastBox.height()/2);
	var scrollTop=$(window).scrollTop();
	var documentH=$(window).height();
	return (lastBoxLis < scrollTop + documentH )?true:false;
}
function loadImg(){

	var dataInt={'data':[{'src':'pet1.jpg'},{'src':'pet2.jpg'},{'src':'pet3.jpg'},
	{'src':'pet4.jpg'},{'src':'pet3.jpg'},{'src':'pet3.jpg'},{'src':'pet3.jpg'},
	{'src':'pet3.jpg'},{'src':'pet0.jpg'},{'src':'pet8.jpg'},{'src':'pet3.jpg'},
	{'src':'pet3.jpg'},{'src':'pet3.jpg'},{'src':'pet3.jpg'},{'src':'pet3.jpg'}
	,{'src':'pet3.jpg'},{'src':'pet4.jpg'},{'src':'pet4.jpg'},{'src':'pet3.jpg'},
	{'src':'pet3.jpg'},{'src':'pet3.jpg'},{'src':'pet6.jpg'}]};

	$get("http://www.baidu.com",function(data){
		for(var i=0;i<data.length;i++){

			var oLi=$('<li>').appendTo($('#imgBox'));
			var oImg=$('<img>').attr('src','css/images/'+$(data[i]).attr('src')).appendTo($(oLi));

			var h=$('#imgBox li').width();
			$('#imgBox li').height(h);

		}

	},"json");
	// for(var i=0;i<dataInt.data.length;i++){

	// 			var oLi=$('<li>').appendTo($('#imgBox'));
	// 			var oImg=$('<img>').attr('src','css/images/'+$(dataInt.data[i]).attr('src')).appendTo($(oLi));
				
	// 			var h=$('#imgBox li').width();
	// 			$('#imgBox li').height(h);

	// 		}



	 // ajax请求数据  
    /*jQuery.ajax({  
        type:"POST",  
        url: "/show/getPins/",  
        data:data,  
        dataType: "json",  
        beforeSend: function(XMLHttpRequest){  
          $("#loading").css('display','');  
        },  
        success:function(response) {  
          if(response.data){  
            for(var i=0, length = response.data.length; i<length; i++){  
                var html = response.data[i];  
                var test = $(html);  
                target.append(test);  
                var img = test.find('img');  
                X.util.flowPin(img[0],188);  
            }  
              
                target.attr('index',parseInt(current_page)+1);  
               
                $("#loading").css('display','none');  
          }  
    },  
    error:function(){  
          alert("加载失败");  
    }  
    });  */

}
</script>
</html>
