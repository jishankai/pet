<?php
/* @var $this UserController */
/* @var $data User */
?>

<div class="view">

	<b><?php echo CHtml::encode($data->getAttributeLabel('name')); ?>:</b>
	<?php echo CHtml::link(CHtml::encode($data->name), array('view', 'id'=>$data->usr_id)); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('gender')); ?>:</b>
	<?php echo $data->getGender(); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('tx')); ?>:</b>
	<?php echo $data->showTxImage(); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('exp')); ?>:</b>
	<?php echo CHtml::encode($data->exp); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('lv')); ?>:</b>
	<?php echo CHtml::encode($data->lv); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('code')); ?>:</b>
	<?php echo CHtml::encode($data->code); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('create_time')); ?>:</b>
	<?php echo CHtml::encode(date("Y-m-d H:i:s",$data->create_time)); ?>
	<br />

<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3)" width="80%" color=#987cb9 SIZE=3>
</div>
