<?php
/* @var $this ImageController */
/* @var $model Image */

$this->breadcrumbs=array(
	'Images'=>array('index'),
	$model->img_id=>array('view','id'=>$model->img_id),
	'Update',
);

$this->menu=array(
	array('label'=>'List Image', 'url'=>array('index')),
	array('label'=>'Create Image', 'url'=>array('create')),
	array('label'=>'View Image', 'url'=>array('view', 'id'=>$model->img_id)),
	array('label'=>'Manage Image', 'url'=>array('admin')),
);
?>

<h1>Update Image <?php echo $model->img_id; ?></h1>

<?php echo $this->renderPartial('_form', array('model'=>$model)); ?>