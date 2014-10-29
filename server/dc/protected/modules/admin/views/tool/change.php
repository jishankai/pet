<?php
/* @var $this UserController */
/* @var $dataProvider CActiveDataProvider */

$this->breadcrumbs=array(
	'工具',
);

$this->menu=array(
	array('label'=>'切换用户', 'url'=>array('change')),
);
?>

<h1>切换用户</h1>
<?php echo CHtml::beginForm();?>
<?php echo CHtml::label('原用户名：', 'from_name');?>
<?php echo CHtml::label('现用户名：', 'from_name');?>
<?php echo CHtml::submitButton('替换'); ?>  
<?php if($result): ?>  
<span style="color:green">Success!!!</span>  
<?php endif; ?>
<?php echo CHtml::endForm(); ?>  
