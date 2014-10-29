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
		array('name'=>'type','value'=>$model->getTypeName()),
        'value.exp',
        'value.con_login',
        array('label'=>CHtml::encode('照片数量'),'value'=>Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_image WHERE usr_id=:usr_id')->bindValue(':usr_id', $model->usr_id)->queryScalar()),
        array('label'=>CHtml::encode('关注'),'value'=>Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_friend WHERE (usr_id=:usr_id AND relation IN (0,1)) OR (follow_id=:usr_id AND relation IN (0,-1))')->bindValue(':usr_id', $model->usr_id)->queryScalar()),
        array('label'=>CHtml::encode('粉丝'),'value'=>Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_friend WHERE (usr_id=:usr_id AND relation IN (0,-1)) OR (follow_id=:usr_id AND relation IN (0,1))')->bindValue(':usr_id', $model->usr_id)->queryScalar()),
		'code',
		'weibo',
        array('name'=>'create_time','value'=>date("Y-m-d H:i:s",$model->create_time)),
	),
)); ?>
