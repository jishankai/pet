<?php
/* @var $this TopicController */
/* @var $model Topic */

$this->breadcrumbs=array(
	'活动'=>array('index'),
	'管理',
);

$this->menu=array(
	array('label'=>'活动列表', 'url'=>array('index')),
	array('label'=>'创建活动', 'url'=>array('create')),
);

Yii::app()->clientScript->registerScript('search', "
$('.search-button').click(function(){
	$('.search-form').toggle();
	return false;
});
$('.search-form form').submit(function(){
	$('#topic-grid').yiiGridView('update', {
		data: $(this).serialize()
	});
	return false;
});
");
?>

<h1>管理活动</h1>

<p>
You may optionally enter a comparison operator (<b>&lt;</b>, <b>&lt;=</b>, <b>&gt;</b>, <b>&gt;=</b>, <b>&lt;&gt;</b>
or <b>=</b>) at the beginning of each of your search values to specify how the comparison should be done.
</p>

<?php echo CHtml::link('Advanced Search','#',array('class'=>'search-button')); ?>
<div class="search-form" style="display:none">
<?php $this->renderPartial('_search',array(
	'model'=>$model,
)); ?>
</div><!-- search-form -->

<?php $this->widget('zii.widgets.grid.CGridView', array(
	'id'=>'topic-grid',
	'dataProvider'=>$model->search(),
	'filter'=>$model,
	'columns'=>array(
		//'topic_id',
		'topic',
		array('name'=>'to', 'value'=>'$data->to==0?"所有人":($data->to==1?"汪星人":($data->to==2?"喵星人":"其他星人"))', 'filter'=>array(0=>'所有', 1=>'汪星人', 2=>'喵星人', 3=>'其他星人')),
		//'des',
		//'reward',
        //'img',
        array('name'=>'status','value'=>'$data->status?"发布":"未发布"', 'filter'=>array(0=>'未发布', 1=>'发布')),
        array('name'=>'start_time','value'=>'date("Y-m-d H:i:s",$data->start_time)'),
        array('name'=>'end_time','value'=>'date("Y-m-d H:i:s",$data->end_time)'),
		//'create_time',
		//'update_time',
		array(
			'class'=>'CButtonColumn',
		),
	),
)); ?>
