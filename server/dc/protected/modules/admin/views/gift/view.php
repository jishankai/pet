<?php
/* @var $this GiftController */
/* @var $model Gift */

$this->breadcrumbs=array(
	'Gifts'=>array('index'),
	$model->name,
);

$this->menu=array(
	array('label'=>'List Gift', 'url'=>array('index')),
	array('label'=>'Create Gift', 'url'=>array('create')),
	array('label'=>'Update Gift', 'url'=>array('update', 'id'=>$model->gift_id)),
	array('label'=>'Delete Gift', 'url'=>'#', 'linkOptions'=>array('submit'=>array('delete','id'=>$model->gift_id),'confirm'=>'Are you sure you want to delete this item?')),
	array('label'=>'Manage Gift', 'url'=>array('admin')),
);
?>

<h1>View Gift #<?php echo $model->gift_id; ?></h1>

<?php $this->widget('zii.widgets.CDetailView', array(
	'data'=>$model,
	'attributes'=>array(
		'gift_id',
		'name',
		'level',
		'price',
		'add_rq',
		'ratio',
		'is_real',
		'detail_image',
		'sale_status',
		'effect_des',
	),
)); ?>
