<?php
/* @var $this TopicController */
/* @var $model Topic */

$this->breadcrumbs=array(
	'活动'=>array('index'),
	$model->topic_id,
);

$this->menu=array(
	array('label'=>'活动列表', 'url'=>array('index')),
	array('label'=>'创建活动', 'url'=>array('create')),
	array('label'=>'更新活动', 'url'=>array('update', 'id'=>$model->topic_id)),
	array('label'=>'删除活动', 'url'=>'#', 'linkOptions'=>array('submit'=>array('delete','id'=>$model->topic_id),'confirm'=>'Are you sure you want to delete this item?')),
	array('label'=>'管理活动', 'url'=>array('admin')),
);
?>

<h1>活动 #<?php echo $model->topic_id; ?></h1>
<div>
    <?php echo $model->showTopicImage(); ?>
</div> 
<?php $this->widget('zii.widgets.CDetailView', array(
    'data'=>$model,
    'attributes'=>array(
		'topic_id',
		'topic',
		array('name'=>'to', 'value'=>$model->to==0?"所有人":($model->to==1?"汪星人":($model->to==2?"喵星人":"其他星人"))),
		'des',
		'reward',
        'remark',
		'img',
	    array('name'=>'status','value'=>$model->status?'发布':'未发布'),
        array('name'=>'start_time','value'=>date("Y-m-d H:i:s",$model->start_time)),
        array('name'=>'end_time','value'=>date("Y-m-d H:i:s",$model->end_time)),
		//'create_time',
		//'update_time',
	),
)); ?>
