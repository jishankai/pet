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

	<b><?php echo CHtml::encode($data->getAttributeLabel('age')); ?>:</b>
	<?php echo CHtml::encode($data->age); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('type')); ?>:</b>
	<?php echo CHtml::encode($data->getTypeName()); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('code')); ?>:</b>
	<?php echo CHtml::encode($data->code); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('inviter')); ?>:</b>
	<?php echo CHtml::encode($data->inviter); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('weibo')); ?>:</b>
	<?php echo CHtml::encode($data->weibo); ?>
	<br />
    <?php /*
	<b><?php echo CHtml::encode($data->getAttributeLabel('value.exp')); ?>:</b>
	<?php echo CHtml::encode($data->value->exp); ?>
	<br />

	<b><?php echo CHtml::encode('照片数量') ?>:</b>
	<?php echo Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_image WHERE usr_id=:usr_id')->bindValue(':usr_id', $data->usr_id)->queryScalar(); ?>
	<br />

	<b><?php echo CHtml::encode('关注') ?>:</b>
    <?php echo Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_friend WHERE (usr_id=:usr_id AND relation IN (0,1)) OR (follow_id=:usr_id AND relation IN (0,-1))')->bindValue(':usr_id', $data->usr_id)->queryScalar()?>
	<br />
	<b><?php echo CHtml::encode('粉丝') ?>:</b>
    <?php echo Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_friend WHERE (usr_id=:usr_id AND relation IN (0,-1)) OR (follow_id=:usr_id AND relation IN (0,1))')->bindValue(':usr_id', $data->usr_id)->queryScalar()?>
    <br />
     */?>

	<b><?php echo CHtml::encode($data->getAttributeLabel('create_time')); ?>:</b>
	<?php echo CHtml::encode(date("Y-m-d H:i:s",$data->create_time)); ?>
	<br />

<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3)" width="80%" color=#987cb9 SIZE=3>
</div>
