<?php
/* @var $this UserController */
/* @var $dataProvider CActiveDataProvider */

$this->breadcrumbs=array(
	'工具',
);

$this->menu=array(
	array('label'=>'删除用户', 'url'=>array('clear')),
	array('label'=>'切换用户', 'url'=>array('replace')),
);
?>

<h1>切换用户</h1>
<?php echo CHtml::beginForm();?>
<?php echo CHtml::label('用户名：', 'u_name');?>
<?php echo CHtml::textField('u_name');?>
<br/>
<?php echo CHtml::submitButton('删除'); ?>  
<br/>
<?php if($error): ?>  
<span style="color:red"><?php echo $error; ?></span>  
<?php endif; ?>
<?php if($result): ?>  
<span style="color:green">Success!!!</span>  
<?php endif; ?>
<?php echo CHtml::endForm(); ?>  
