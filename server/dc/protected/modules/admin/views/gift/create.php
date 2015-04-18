<?php
/* @var $this GiftController */
/* @var $model Gift */

$this->breadcrumbs=array(
	'Gifts'=>array('index'),
	'Create',
);

$this->menu=array(
	array('label'=>'List Gift', 'url'=>array('index')),
	array('label'=>'Manage Gift', 'url'=>array('admin')),
);
?>

<h1>Create Gift</h1>

<?php $this->renderPartial('_form', array('model'=>$model)); ?>