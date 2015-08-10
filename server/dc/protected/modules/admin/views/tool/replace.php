<?php
/* @var $this UserController */
/* @var $dataProvider CActiveDataProvider */

$this->breadcrumbs=array(
	'工具',
);

$this->menu=array(
	array('label'=>'切换用户', 'url'=>array('replace')),
	array('label'=>'删除用户', 'url'=>array('clear')),
    array('label'=>'充值', 'url'=>array('gold')),
    array('label'=>'设置密码', 'url'=>array('password')),
);
?>

<h1>切换用户</h1>
<?php echo CHtml::beginForm();?>
<?php echo CHtml::label('原用户名：', 'from_name');?>
<?php echo CHtml::textField('from_name');?>
<br/>
<?php echo CHtml::label('现用户名：', 'from_name');?>
<?php echo CHtml::textField('to_name');?>
<br/>
<?php echo CHtml::submitButton('替换'); ?>  
<br/>
<?php if($error): ?>  
<span style="color:red"><?php echo $error; ?></span>  
<?php endif; ?>
<?php if($result): ?>  
<span style="color:green">Success!!!</span>  
<?php endif; ?>
<?php echo CHtml::endForm(); ?>  
