<?php

/**
 * This is the model class for table "dc_topic".
 *
 * The followings are the available columns in table 'dc_topic':
 * @property string $topic_id
 * @property string $topic
 * @property integer $to
 * @property string $des
 * @property string $reward
 * @property string $remark
 * @property string $img
 * @property integer $status
 * @property string $start_time
 * @property string $end_time
 * @property string $create_time
 * @property string $update_time
 */
class Topic extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Topic the static model class
	 */
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}

	/**
	 * @return string the associated database table name
	 */
	public function tableName()
	{
		return 'dc_topic';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('to, status', 'numerical', 'integerOnly'=>true),
			array('topic', 'length', 'max'=>12),
			array('des, reward, remark, img', 'length', 'max'=>255),
			array('start_time, end_time, create_time', 'length', 'max'=>10),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('topic_id, topic, to, des, reward, remark, img, status, start_time, end_time, create_time, update_time', 'safe', 'on'=>'search'),
		);
	}

	/**
	 * @return array relational rules.
	 */
	public function relations()
	{
		// NOTE: you may need to adjust the relation name and the related
		// class name for the relations automatically generated below.
		return array(
		);
	}

    /*
    public function behaviors()
    {
        return array(
            'behavior' => 'TopicBehavior',
        );
    }
     */

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'topic_id' => '编号',
			'topic' => '标题',
			'to' => '对象',
			'des' => '描述',
			'reward' => '奖品',
            'remark' => '备注',
			'img' => '图片',
			'status' => '状态',
			'start_time' => '开始时间',
			'end_time' => '结束时间',
			'create_time' => '创建时间',
			'update_time' => '更新时间',
		);
	}

	/**
	 * Retrieves a list of models based on the current search/filter conditions.
	 * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
	 */
	public function search()
	{
		// Warning: Please modify the following code to remove attributes that
		// should not be searched.

		$criteria=new CDbCriteria;

		$criteria->compare('topic_id',$this->topic_id,true);
		$criteria->compare('topic',$this->topic,true);
		$criteria->compare('to',$this->to);
		$criteria->compare('des',$this->des,true);
		$criteria->compare('reward',$this->reward,true);
		$criteria->compare('remark',$this->remark,true);
		$criteria->compare('img',$this->img,true);
		$criteria->compare('status',$this->status);
		$criteria->compare('start_time',$this->start_time,true);
		$criteria->compare('end_time',$this->end_time,true);
		$criteria->compare('create_time',$this->create_time,true);
		$criteria->compare('update_time',$this->update_time,true);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}

    public function showTopicImage(){
        return CHtml::image(Yii::app()->request->hostInfo.Yii::app()->baseUrl.'/images/topic/'.$this->img, $this->img, array('width'=>'200px','max-height'=>'200px'));
    }   
}
