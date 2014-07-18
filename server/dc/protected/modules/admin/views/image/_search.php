<?php
/* @var $this ImageController */
/* @var $model Image */
/* @var $form CActiveForm */
?>

<div class="wide form">

<?php $form=$this->beginWidget('CActiveForm', array(
	'action'=>Yii::app()->createUrl($this->route),
	'method'=>'get',
)); ?>

	<div class="row">
		<?php echo $form->label($model,'img_id'); ?>
		<?php echo $form->textField($model,'img_id',array('size'=>10,'maxlength'=>10)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'usr_id'); ?>
<?php echo $form->dropDownList($model,'usr_id',CHtml::listData(User::model()->findAll(), 'usr_id', 'name')); ?>    
	</div>

	<div class="row">
		<?php echo $form->label($model,'topic_id'); ?>
<?php echo $form->dropDownList($model,'topic_id',CHtml::listData(Topic::model()->findAll(), 'topic_id', 'topic')); ?>    
	</div>

	<div class="row">
		<?php echo $form->label($model,'cmt'); ?>
		<?php echo $form->textField($model,'cmt',array('size'=>60,'maxlength'=>255)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'likes'); ?>
		<?php echo $form->textField($model,'likes',array('size'=>10,'maxlength'=>10)); ?>
	</div>

	<div class="row">
		<?php echo $form->label($model,'create_time'); ?>
        <?php $this->widget('application.extensions.timepicker.timepicker', array(
            'options'=>array(    
                'showSecond'=>TRUE              
             ),
            'model'=>$model,                 
            'name'=>'create_time',                 
        )); ?>  
	</div>

	<div class="row">
		<?php echo $form->label($model,'is_deleted'); ?>
		<?php echo $form->dropDownList($model,'is_deleted',array(0=>'否',1=>'是')); ?>
	</div>

	<div class="row buttons">
		<?php echo CHtml::submitButton('Search'); ?>
	</div>

<?php $this->endWidget(); ?>

</div><!-- search-form -->
