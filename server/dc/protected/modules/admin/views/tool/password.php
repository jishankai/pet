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

<h1>密码</h1>
<?php echo CHtml::beginForm();?>
<?php echo CHtml::label('用户标识：', 'code');?>
<?php echo CHtml::textField('code');?>
<br/>
<?php echo CHtml::label('密码：', 'password');?>
<?php echo CHtml::textField('password');?>
<br/>
<?php echo CHtml::submitButton('修改'); ?>
<br/>
<?php if($error): ?>
<span style="color:red"><?php echo $error; ?></span>
<?php endif; ?>
<?php if($result): ?>
<span style="color:green">Success!!!</span>
<?php endif; ?>
<?php echo CHtml::endForm(); ?>
