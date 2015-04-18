<?php
/* @var $this GiftController */
/* @var $data Gift */
?>

<div class="view">

	<b><?php echo CHtml::encode($data->getAttributeLabel('gift_id')); ?>:</b>
	<?php echo CHtml::link(CHtml::encode($data->gift_id), array('view', 'id'=>$data->gift_id)); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('name')); ?>:</b>
	<?php echo CHtml::encode($data->name); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('level')); ?>:</b>
	<?php echo CHtml::encode($data->level); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('price')); ?>:</b>
	<?php echo CHtml::encode($data->price); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('add_rq')); ?>:</b>
	<?php echo CHtml::encode($data->add_rq); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('ratio')); ?>:</b>
	<?php echo CHtml::encode($data->ratio); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('is_real')); ?>:</b>
	<?php echo CHtml::encode($data->is_real); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('detail_image')); ?>:</b>
	<?php echo CHtml::encode($data->detail_image); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('sale_status')); ?>:</b>
	<?php echo CHtml::encode($data->sale_status); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('effect_des')); ?>:</b>
	<?php echo CHtml::encode($data->effect_des); ?>
	<br />

<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3)" width="100%" color=#987cb9 SIZE=3>

</div>
