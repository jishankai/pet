<?php
/* @var $this GiftController */
/* @var $model Gift */

$this->breadcrumbs=array(
	'Gifts'=>array('index'),
	$model->name=>array('view','id'=>$model->gift_id),
	'Update',
);

$this->menu=array(
	array('label'=>'List Gift', 'url'=>array('index')),
	array('label'=>'Create Gift', 'url'=>array('create')),
	array('label'=>'View Gift', 'url'=>array('view', 'id'=>$model->gift_id)),
	array('label'=>'Manage Gift', 'url'=>array('admin')),
);
?>

<h1>Update Gift <?php echo $model->gift_id; ?></h1>

<?php $this->renderPartial('_form', array('model'=>$model)); ?>