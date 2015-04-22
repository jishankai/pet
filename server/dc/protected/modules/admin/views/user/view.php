<?php
/* @var $this UserController */
/* @var $model User */

$this->breadcrumbs=array(
	'Users'=>array('index'),
	$model->name,
);

$this->menu=array(
	array('label'=>'List User', 'url'=>array('index')),
	array('label'=>'Create User', 'url'=>array('create')),
	array('label'=>'Update User', 'url'=>array('update', 'id'=>$model->usr_id)),
	array('label'=>'Delete User', 'url'=>'#', 'linkOptions'=>array('submit'=>array('delete','id'=>$model->usr_id),'confirm'=>'Are you sure you want to delete this item?')),
	array('label'=>'Manage User', 'url'=>array('admin')),
);
?>

<h1>View User #<?php echo $model->name; ?></h1>

<?php $this->widget('zii.widgets.CDetailView', array(
	'data'=>$model,
	'attributes'=>array(
		'name',
	    array('name'=>'gender','value'=>$model->gender==1?"公":"母"),
		array('name'=>'tx','value'=>$model->showTxImage(),'type'=>'html'),
		'age',
        'exp',
        'lv',
        'con_login',
        'code',
        array('name'=>'create_time','value'=>date("Y-m-d H:i:s",$model->create_time)),
	),
)); ?>
