<?php
/* @var $this StarController */
/* @var $model Star */

$this->breadcrumbs=array(
	'Stars'=>array('index'),
	$model->star_id=>array('view','id'=>$model->star_id),
	'Update',
);

$this->menu=array(
	array('label'=>'List Star', 'url'=>array('index')),
	array('label'=>'Create Star', 'url'=>array('create')),
	array('label'=>'View Star', 'url'=>array('view', 'id'=>$model->star_id)),
	array('label'=>'Manage Star', 'url'=>array('admin')),
);
?>

<h1>Update Star <?php echo $model->star_id; ?></h1>

<?php $this->renderPartial('_form', array('model'=>$model)); ?>