<?php
/* @var $this StarController */
/* @var $model Star */

$this->breadcrumbs=array(
	'Stars'=>array('index'),
	$model->star_id,
);

$this->menu=array(
	array('label'=>'List Star', 'url'=>array('index')),
	array('label'=>'Create Star', 'url'=>array('create')),
	array('label'=>'Update Star', 'url'=>array('update', 'id'=>$model->star_id)),
	array('label'=>'Delete Star', 'url'=>'#', 'linkOptions'=>array('submit'=>array('delete','id'=>$model->star_id),'confirm'=>'Are you sure you want to delete this item?')),
	array('label'=>'Manage Star', 'url'=>array('admin')),
);
?>

<h1>View Star #<?php echo $model->star_id; ?></h1>

<?php $this->widget('zii.widgets.CDetailView', array(
	'data'=>$model,
	'attributes'=>array(
		'star_id',
		'name',
		'title',
		'icon',
		'description',
		'banner',
		'url',
		'start_time',
		'end_time',
		'create_time',
		'update_time',
	),
)); ?>
