<?php

/**
 * This is the model class for table "dc_image".
 *
 * The followings are the available columns in table 'dc_image':
 * @property string $img_id
 * @property string $usr_id
 * @property string $topic_id
 * @property string $cmt
 * @property string $url
 * @property string $likes
 * @property string $likers
 * @property string $comments
 * @property string $create_time
 * @property string $update_time
 * @property integer $is_deleted
 *
 * The followings are the available model relations:
 * @property User $usr
 */
class Image extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Image the static model class
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
		return 'dc_image';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('is_deleted', 'numerical', 'integerOnly'=>true),
			array('usr_id, topic_id, likes, create_time', 'length', 'max'=>10),
			array('cmt, url', 'length', 'max'=>255),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('img_id, usr_id, topic_id, cmt, url, likes, likers, comments, create_time, update_time, is_deleted', 'safe', 'on'=>'search'),
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
			'usr' => array(self::BELONGS_TO, 'User', 'usr_id'),
		);
	}

    public function behaviors()
    {
        return array(
            'behavior' => 'ImageBehavior',
        );
    }

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'img_id' => '图片编号',
			'usr_id' => '用户编号',
			'topic_id' => '活动编号',
			'cmt' => '说明',
			'url' => '地址',
			'likes' => '点赞数',
			'likers' => '点赞用户',
			'comments' => '评论',
			'create_time' => '创建时间',
			'update_time' => '更新时间',
			'is_deleted' => '是否删除',
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

		$criteria->compare('img_id',$this->img_id,true);
		$criteria->compare('usr_id',$this->usr_id,true);
		$criteria->compare('topic_id',$this->topic_id,true);
		$criteria->compare('cmt',$this->cmt,true);
		$criteria->compare('url',$this->url,true);
		$criteria->compare('likes',$this->likes,true);
		$criteria->compare('likers',$this->likers,true);
		$criteria->compare('comments',$this->comments,true);
		$criteria->compare('create_time',$this->create_time,true);
		$criteria->compare('update_time',$this->update_time,true);
		$criteria->compare('is_deleted',$this->is_deleted);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
    }
}
