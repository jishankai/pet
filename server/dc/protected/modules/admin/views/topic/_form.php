<?php
/* @var $this TopicController */
/* @var $model Topic */
/* @var $form CActiveForm */
?>

<div class="form">

<?php $form=$this->beginWidget('CActiveForm', array(
	'id'=>'topic-form',
	'enableAjaxValidation'=>false,
    'htmlOptions'=>array('enctype'=>'multipart/form-data'), 
)); ?>

	<?php echo $form->errorSummary($model); ?>

	<div class="row">
		<?php echo $form->labelEx($model,'topic'); ?>
		<?php echo $form->textField($model,'topic',array('size'=>12,'maxlength'=>12)); ?>
		<?php echo $form->error($model,'topic'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'to'); ?>
		<?php echo $form->dropDownList($model,'to',array(0=>'所有人',1=>'汪星人',2=>'喵星人',3=>'其他星人')); ?>
		<?php echo $form->error($model,'to'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'des'); ?>
		<?php echo $form->textArea($model,'des',array('rows'=>6,'cols'=>50)); ?>
		<?php echo $form->error($model,'des'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'reward'); ?>
		<?php echo $form->textArea($model,'reward',array('rows'=>6,'cols'=>50)); ?>
		<?php echo $form->error($model,'reward'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'remark'); ?>
		<?php echo $form->textArea($model,'remark',array('rows'=>6,'cols'=>50)); ?>
		<?php echo $form->error($model,'remark'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'img'); ?>
		<?php echo $form->fileField($model,'img'); ?>
		<?php echo $form->error($model,'img'); ?>
	</div>

    <div class="row">
        <?php echo $form->labelEx($model,'status'); ?>
        <?php echo $form->dropDownList($model,'status',array(0=>'未发布', 1=>'发布')); ?>
        <?php echo $form->error($model,'status'); ?>
    </div>

    <div class="row">
        <?php echo $form->labelEx($model,'start_time'); ?>
        <?php $this->widget('application.extensions.timepicker.timepicker', array(
            'options'=>array(    
                'showSecond'=>TRUE              
             ),
            'model'=>$model,                 
            'name'=>'start_time',                 
        )); ?>  
        <?php echo $form->error($model,'start_time'); ?>
    </div>

    <div class="row">
        <?php echo $form->labelEx($model,'end_time'); ?>
        <?php $this->widget('application.extensions.timepicker.timepicker', array(
            'options'=>array(    
                'showSecond'=>TRUE              
            ),
            'model'=>$model,                 
            'name'=>'end_time',                 
        )); ?>  
        <?php echo $form->error($model,'end_time'); ?>
    </div>

    <div class="row buttons">
        <?php echo CHtml::submitButton($model->isNewRecord ? 'Create' : 'Save'); ?>
    </div>

<?php $this->endWidget(); ?>

</div><!-- form -->
