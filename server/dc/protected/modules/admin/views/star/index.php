<?php
/* @var $this StarController */
/* @var $dataProvider CActiveDataProvider */

$this->breadcrumbs=array(
	'Stars',
);

$this->menu=array(
	array('label'=>'Create Star', 'url'=>array('create')),
	array('label'=>'Manage Star', 'url'=>array('admin')),
);
?>

<h1>Stars</h1>

<?php $this->widget('zii.widgets.CListView', array(
	'dataProvider'=>$dataProvider,
	'itemView'=>'_view',
)); ?>
