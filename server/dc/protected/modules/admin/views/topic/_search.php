<?php
/* @var $this TopicController */
/* @var $model Topic */
/* @var $form CActiveForm */
?>

<div class="wide form">

<?php $form=$this->beginWidget('CActiveForm', array(
	'action'=>Yii::app()->createUrl($this->route),
	'method'=>'get',
)); ?>

	<div class="row">
		<?php echo $form->label($model,'topic'); ?>
		<?php echo $form->textField($model,'topic',array('size'=>12,'maxlength'=>12)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'to'); ?>
		<?php echo $form->dropDownList($model,'to',array(0=>'所有人',1=>'汪星人',2=>'喵星人',3=>'其他星人')); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'status'); ?>
        <?php echo $form->dropDownList($model,'status',array(0=>'未发布', 1=>'发布')); ?>
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
