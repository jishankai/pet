<?php
/* @var $this BannerController */
/* @var $model Banner */

$this->breadcrumbs=array(
	'Banners'=>array('index'),
	$model->title=>array('view','id'=>$model->eid),
	'Update',
);

$this->menu=array(
	array('label'=>'List Banner', 'url'=>array('index')),
	array('label'=>'Create Banner', 'url'=>array('create')),
	array('label'=>'View Banner', 'url'=>array('view', 'id'=>$model->eid)),
	array('label'=>'Manage Banner', 'url'=>array('admin')),
);
?>

<h1>Update Banner <?php echo $model->eid; ?></h1>

<?php $this->renderPartial('_form', array('model'=>$model)); ?>