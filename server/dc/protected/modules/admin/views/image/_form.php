<?php
/* @var $this ImageController */
/* @var $model Image */
/* @var $form CActiveForm */
?>

<div class="form">

<?php $form=$this->beginWidget('CActiveForm', array(
	'id'=>'image-form',
	'enableAjaxValidation'=>false,
)); ?>

	<p class="note">Fields with <span class="required">*</span> are required.</p>

	<?php echo $form->errorSummary($model); ?>

	<div class="row">
		<?php echo $form->labelEx($model,'usr_id'); ?>
<?php echo $form->dropDownList($model,'usr_id',CHtml::listData(User::model()->findAll(), 'usr_id', 'name')); ?>    
		<?php echo $form->error($model,'usr_id'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'topic_id'); ?>
<?php echo $form->dropDownList($model,'topic_id',CHtml::listData(Topic::model()->findAll(), 'topic_id', 'topic')); ?>    
		<?php echo $form->error($model,'topic_id'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'cmt'); ?>
		<?php echo $form->textArea($model,'cmt',array('rows'=>6,'maxlength'=>255)); ?>
		<?php echo $form->error($model,'cmt'); ?>
    </div>
        <div class="row">
        <?php echo $form->labelEx($model,'url'); ?>
            <?php echo $model->showImage(); ?>
        </div>
    <div class="row">
        <?php echo $form->labelEx($model,'url'); ?>
        <?php echo $form->fileField($model,'url'); ?>
        <?php echo $form->error($model,'url'); ?>
    </div>

    <div class="row">
        <?php echo $form->labelEx($model,'likes'); ?>
        <?php echo $form->textField($model,'likes',array('size'=>10,'maxlength'=>10)); ?>
        <?php echo $form->error($model,'likes'); ?>
    </div>

    <div class="row">
        <?php echo $form->labelEx($model,'likers'); ?>
        <?php echo $form->textArea($model,'likers',array('rows'=>6, 'cols'=>50)); ?>
        <?php echo $form->error($model,'likers'); ?>
    </div>

    <div class="row">
        <?php echo $form->labelEx($model,'comments'); ?>
        <?php echo $form->textArea($model,'comments',array('rows'=>6, 'cols'=>50)); ?>
        <?php echo $form->error($model,'comments'); ?>
    </div>

    <div class="row">
        <?php echo $form->labelEx($model,'is_deleted'); ?>
        <?php echo $form->dropDownList($model,'is_deleted',array(0=>'否',1=>'是')); ?>
        <?php echo $form->error($model,'is_deleted'); ?>
    </div>

    <div class="row buttons">
        <?php echo CHtml::submitButton($model->isNewRecord ? 'Create' : 'Save'); ?>
    </div>

<?php $this->endWidget(); ?>

</div><!-- form -->
