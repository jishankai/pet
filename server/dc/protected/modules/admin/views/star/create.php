<?php
/* @var $this StarController */
/* @var $model Star */

$this->breadcrumbs=array(
	'Stars'=>array('index'),
	'Create',
);

$this->menu=array(
	array('label'=>'List Star', 'url'=>array('index')),
	array('label'=>'Manage Star', 'url'=>array('admin')),
);
?>

<h1>Create Star</h1>

<?php $this->renderPartial('_form', array('model'=>$model)); ?>