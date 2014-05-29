<div>
<?php echo CHtml::dropDownList('controller', NULL, 
array(
    'user' => 'User',
    'image' => 'Image',
),
array(
    'empty' => 'Select a controller',
    'onChange' => 'displayAction()',
)
);
echo CHtml::dropDownList('user', NULL, 
array(
    'loginApi' => 'Login',
    'registerApi' => 'Register',
    'infoApi' => 'Info',
),
array(
    'empty' => 'Select a action',
    'hidden' => true,
    'onChange' => 'displayParams()',
)
);
echo CHtml::dropDownList('image', NULL, 
array(
    'uploadApi' => 'Upload',
    'likeApi' => 'Like',
    'deleteApi' => 'Delete',
),
array(
    'empty' => 'Select a action',
    'hidden' => true,
    'onChange' => 'displayParams()',
)
);
?>
<span hidden='1' class='loginApi'>
<?php 
echo CHtml::label('UID', 'uid');
echo CHtml::textField('uid');
?>
</span>
<span hidden='1' class='registerApi'>
<?php 
echo CHtml::label('昵称', 'name');
echo CHtml::textField('name');
echo CHtml::label('性别', 'gender');
echo CHtml::dropDownList('gender', NULL,
    array(
        '1' => '公',
        '2' => '母',
        '3' => '其他',
    ),
array(
    'empty' => '请选择一个角色',
)
);
echo CHtml::label('年龄', 'age');
echo CHtml::textField('age');
echo CHtml::label('种类', 'class');
echo CHtml::dropDownList('class', NULL,
    array(
        '1' => '汪星人',
        '2' => '喵星人',
    ),
array(
    'empty' => '请选择一个角色',
)
);
echo CHtml::label('招待ID', 'code');
echo CHtml::textField('code');
?>
</span>
<span hidden='1' class='likeApi'>
<?php 
echo CHtml::label('图片id', 'img_id');
echo CHtml::textField('img_id');
?>
</span>
<span hidden='1' class='deleteApi'>
<?php 
echo CHtml::label('图片id', 'img_id');
echo CHtml::textField('img_id');
?>
</span>
<span hidden='1' class='generate'>
<?php
echo CHtml::button('API', array('onClick'=>'generateApi()'));
?>
</span>
</div>
<SCRIPT>
function displayAction() {
    $('#user').hide();
    $('#image').hide();

    $('.loginApi').hide();
    $('.registerApi').hide();
    $('.generate').hide();
    $('.likeApi').hide();
    $('.deleteApi').hide();
    $('#'+$('#controller').val()).fadeIn(); 
};
function displayParams() {
    $('.loginApi').hide();
    $('.registerApi').hide();
    $('.infoApi').hide();
    $('.uploadApi').hide();
    $('.likeApi').hide();
    $('.deleteApi').hide();
    $('.generate').hide();
    $('.'+$('#'+$('#controller').val()).val()).fadeIn(); 
    $('.generate').fadeIn();
};
function generateApi() {
    var $host = 'http://54.199.161.210/dc/index.php?r=';
    var $ca = $('#controller').val() + '/' + $('#'+$('#controller').val()).val() + '&';
    var $params = $('.'+$('#'+$('#controller').val()).val()+' :input');
    var $arr = new Array();
    $params.each(function(){
        $arr[$(this).attr("name")] = $(this).val();
    });
    $arr = sortObjectByKey($arr);
    var $str = new Array();
    for (i in $arr) {
        $str.push(i+'='+$arr[i]);
    }
    $str = $str.join('&');
    $url = $host + $ca +$str +'&sig=' + md5($str+'<?php echo SIGKEY?>');
    alert($url);
}
function sortObjectByKey(obj) {
    var keys = [];
    var sorted_obj = {};

    for(var key in obj){
        if(obj.hasOwnProperty(key)){
            keys.push(key);
        }
    }

    // sort keys
    keys.sort();

    // create new array based on Sorted Keys
    jQuery.each(keys, function(i, key){
        sorted_obj[key] = obj[key];
    });

    return sorted_obj;
};
</SCRIPT>
