<?php
/* @var $this ImageController */
/* @var $model Image */

$this->breadcrumbs=array(
	'Images'=>array('index'),
	$model->img_id,
);

$this->menu=array(
	array('label'=>'List Image', 'url'=>array('index')),
	array('label'=>'Create Image', 'url'=>array('create')),
	array('label'=>'Update Image', 'url'=>array('update', 'id'=>$model->img_id)),
	array('label'=>'Delete Image', 'url'=>'#', 'linkOptions'=>array('submit'=>array('delete','id'=>$model->img_id),'confirm'=>'Are you sure you want to delete this item?')),
	array('label'=>'Manage Image', 'url'=>array('admin')),
);
?>

<h1>View Image #<?php echo $model->img_id; ?></h1>

<?php $this->widget('zii.widgets.CDetailView', array(
	'data'=>$model,
	'attributes'=>array(
		'img_id',
		'usr_id',
		'topic_id',
		'url',
		'cmt',
		'likes',
		'likers',
		'comments',
		'create_time',
		'update_time',
		'is_deleted',
	),
)); ?>
