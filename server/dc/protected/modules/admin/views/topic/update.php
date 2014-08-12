<?php
/* @var $this TopicController */
/* @var $model Topic */

$this->breadcrumbs=array(
	'活动'=>array('index'),
	$model->topic_id=>array('view','id'=>$model->topic_id),
	'更新',
);

$this->menu=array(
	array('label'=>'活动列表', 'url'=>array('index')),
	array('label'=>'创建活动', 'url'=>array('create')),
	array('label'=>'浏览活动', 'url'=>array('view', 'id'=>$model->topic_id)),
	array('label'=>'管理活动', 'url'=>array('admin')),
);
?>

<h1>更新活动#<?php echo $model->topic_id; ?></h1>

<?php echo $this->renderPartial('_form', array('model'=>$model)); ?>
