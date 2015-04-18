<?php
/* @var $this GiftController */
/* @var $model Gift */
/* @var $form CActiveForm */
?>

<div class="wide form">

<?php $form=$this->beginWidget('CActiveForm', array(
	'action'=>Yii::app()->createUrl($this->route),
	'method'=>'get',
)); ?>

	<div class="row">
		<?php echo $form->label($model,'gift_id'); ?>
		<?php echo $form->textField($model,'gift_id',array('size'=>10,'maxlength'=>10)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'name'); ?>
		<?php echo $form->textField($model,'name',array('size'=>45,'maxlength'=>45)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'level'); ?>
		<?php echo $form->textField($model,'level'); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'price'); ?>
		<?php echo $form->textField($model,'price',array('size'=>10,'maxlength'=>10)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'add_rq'); ?>
		<?php echo $form->textField($model,'add_rq',array('size'=>10,'maxlength'=>10)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'ratio'); ?>
		<?php echo $form->textField($model,'ratio'); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'is_real'); ?>
		<?php echo $form->textField($model,'is_real'); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'detail_image'); ?>
		<?php echo $form->textField($model,'detail_image',array('size'=>60,'maxlength'=>255)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'sale_status'); ?>
		<?php echo $form->textField($model,'sale_status'); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'effect_des'); ?>
		<?php echo $form->textField($model,'effect_des',array('size'=>60,'maxlength'=>255)); ?>
	</div>


	<div class="row buttons">
		<?php echo CHtml::submitButton('Search'); ?>
	</div>

<?php $this->endWidget(); ?>

</div><!-- search-form -->
