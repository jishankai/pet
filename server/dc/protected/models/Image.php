<?php

/**
 * This is the model class for table "dc_image".
 *
 * The followings are the available columns in table 'dc_image':
 * @property string $img_id
 * @property string $aid
 * @property string $topic_id
 * @property string $topic_name
 * @property string $relates
 * @property string $cmt
 * @property string $url
 * @property string $likes
 * @property string $likers
 * @property string $gifts
 * @property string $senders
 * @property string $comments
 * @property string $shares
 * @property string $reports
 * @property string $create_time
 * @property string $update_time
 * @property integer $is_deleted
 *
 * The followings are the available model relations:
 * @property Animal $a
 */
class Image extends CActiveRecord
{
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
			array('reports, is_deleted', 'numerical', 'integerOnly'=>true),
			array('aid, topic_id, likes, gifts, shares, create_time', 'length', 'max'=>10),
			array('topic_name', 'length', 'max'=>45),
			array('relates, cmt, url', 'length', 'max'=>255),
			// The following rule is used by search().
			// @todo Please remove those attributes that should not be searched.
			array('img_id, aid, topic_id, topic_name, relates, cmt, url, likes, likers, gifts, senders, comments, shares, reports, create_time, update_time, is_deleted', 'safe', 'on'=>'search'),
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
			'a' => array(self::BELONGS_TO, 'Animal', 'aid'),
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
			'aid' => '宠物编号',
			'topic_id' => '活动编号',
			'topic_name' => '活动名称',
			'relates' => '@用户',
			'cmt' => '说明',
			'url' => '地址',
			'likes' => '点赞数',
			'likers' => '点赞用户',
			'gifts' => '送礼物数',
			'senders' => '赠送礼物用户',
			'comments' => '评论',
			'shares' => '分享数',
			'reports' => '举报数',
			'create_time' => '创建时间',
			'update_time' => '更新时间',
			'is_deleted' => '是否删除',
		);
	}

	/**
	 * Retrieves a list of models based on the current search/filter conditions.
	 *
	 * Typical usecase:
	 * - Initialize the model fields with values from filter form.
	 * - Execute this method to get CActiveDataProvider instance which will filter
	 * models according to data in model fields.
	 * - Pass data provider to CGridView, CListView or any similar widget.
	 *
	 * @return CActiveDataProvider the data provider that can return the models
	 * based on the search/filter conditions.
	 */
	public function search()
	{
		// @todo Please modify the following code to remove attributes that should not be searched.

		$criteria=new CDbCriteria;

		$criteria->compare('img_id',$this->img_id,true);
		$criteria->compare('aid',$this->aid,true);
		$criteria->compare('topic_id',$this->topic_id,true);
		$criteria->compare('topic_name',$this->topic_name,true);
		$criteria->compare('relates',$this->relates,true);
		$criteria->compare('cmt',$this->cmt,true);
		$criteria->compare('url',$this->url,true);
		$criteria->compare('likes',$this->likes,true);
		$criteria->compare('likers',$this->likers,true);
		$criteria->compare('gifts',$this->gifts,true);
		$criteria->compare('senders',$this->senders,true);
		$criteria->compare('comments',$this->comments,true);
		$criteria->compare('shares',$this->shares,true);
		$criteria->compare('reports',$this->reports,true);
		$criteria->compare('create_time',$this->create_time,true);
		$criteria->compare('update_time',$this->update_time,true);
		$criteria->compare('is_deleted',$this->is_deleted);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}

	/**
	 * Returns the static model of the specified AR class.
	 * Please note that you should have this exact method in all your CActiveRecord descendants!
	 * @param string $className active record class name.
	 * @return Image the static model class
	 */
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
}
