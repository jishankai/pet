<?php
/* @var $this ImageController */
/* @var $data Image */
?>

<div class="view">

	<b><?php echo CHtml::encode($data->getAttributeLabel('img_id')); ?>:</b>
	<?php echo CHtml::link(CHtml::encode($data->img_id), array('view', 'id'=>$data->img_id)); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('usr_id')); ?>:</b>
	<?php echo CHtml::encode($data->usr_id); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('topic_id')); ?>:</b>
	<?php echo CHtml::encode($data->topic_id); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('url')); ?>:</b>
	<?php echo $data->showImage(); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('cmt')); ?>:</b>
	<?php echo CHtml::encode($data->cmt); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('likes')); ?>:</b>
	<?php echo CHtml::encode($data->likes); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('likers')); ?>:</b>
	<?php echo CHtml::encode($data->likers); ?>
	<br />

	<?php /*
	<b><?php echo CHtml::encode($data->getAttributeLabel('comments')); ?>:</b>
	<?php echo CHtml::encode($data->comments); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('file')); ?>:</b>
	<?php echo CHtml::encode($data->file); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('create_time')); ?>:</b>
	<?php echo CHtml::encode($data->create_time); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('update_time')); ?>:</b>
	<?php echo CHtml::encode($data->update_time); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('is_deleted')); ?>:</b>
	<?php echo CHtml::encode($data->is_deleted); ?>
	<br />

	*/ ?>

<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3)" width="100%" color=#987cb9 SIZE=3>
</div>
