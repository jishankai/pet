<?php
/* @var $this OrderController */
/* @var $data Order */
?>

<div class="view">

	<b><?php echo CHtml::encode($data->getAttributeLabel('order_id')); ?>:</b>
	<?php echo CHtml::link(CHtml::encode($data->order_id), array('view', 'id'=>$data->order_id)); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('item_id')); ?>:</b>
	<?php echo CHtml::encode($data->item->name); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('aid')); ?>:</b>
	<?php echo CHtml::encode($data->animal->name); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('usr_id')); ?>:</b>
	<?php echo CHtml::encode($data->user->name); ?>
	<br />

	<b><?php echo CHtml::encode('地址'); ?>:</b>
	<?php echo CHtml::encode(implode(';',isset(unserialize($data->animal->address))?unserialize($data->animal->address):'')); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('create_time')); ?>:</b>
	<?php echo CHtml::encode(date("Y-m-d H:i:s",$data->create_time)); ?>
	<br />

	<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3)" width="100%" color=#987cb9 SIZE=3>



</div>
