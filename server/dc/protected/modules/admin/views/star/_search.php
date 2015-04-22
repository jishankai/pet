<?php
/* @var $this StarController */
/* @var $model Star */
/* @var $form CActiveForm */
?>

<div class="wide form">

<?php $form=$this->beginWidget('CActiveForm', array(
	'action'=>Yii::app()->createUrl($this->route),
	'method'=>'get',
)); ?>

	<div class="row">
		<?php echo $form->label($model,'star_id'); ?>
		<?php echo $form->textField($model,'star_id',array('size'=>10,'maxlength'=>10)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'name'); ?>
		<?php echo $form->textField($model,'name',array('size'=>45,'maxlength'=>45)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'title'); ?>
		<?php echo $form->textField($model,'title',array('size'=>45,'maxlength'=>45)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'icon'); ?>
		<?php echo $form->textField($model,'icon',array('size'=>60,'maxlength'=>255)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'description'); ?>
		<?php echo $form->textField($model,'description',array('size'=>60,'maxlength'=>255)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'banner'); ?>
		<?php echo $form->textField($model,'banner',array('size'=>45,'maxlength'=>45)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'url'); ?>
		<?php echo $form->textField($model,'url',array('size'=>45,'maxlength'=>45)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'start_time'); ?>
        <?php $this->widget('application.extensions.timepicker.timepicker', array(
            'options'=>array(    
                'showSecond'=>TRUE              
             ),
            'model'=>$model,                 
            'name'=>'start_time',                 
        )); ?>  
	</div>

	<div class="row">
		<?php echo $form->label($model,'end_time'); ?>
        <?php $this->widget('application.extensions.timepicker.timepicker', array(
            'options'=>array(    
                'showSecond'=>TRUE              
             ),
            'model'=>$model,                 
            'name'=>'end_time',                 
        )); ?>  
	</div>


	<div class="row buttons">
		<?php echo CHtml::submitButton('Search'); ?>
	</div>

<?php $this->endWidget(); ?>

</div><!-- search-form -->
