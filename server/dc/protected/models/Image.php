<?php

/**
 * This is the model class for table "dc_image".
 *
 * The followings are the available columns in table 'dc_image':
 * @property integer $img_id
 * @property integer $usr_id
 * @property string $comment
 * @property integer $like
 * @property string $url
 * @property string $file
 * @property integer $create_time
 * @property string $update_time
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
			array('usr_id, like, create_time', 'numerical', 'integerOnly'=>true),
			array('comment, url, file', 'length', 'max'=>255),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('img_id, usr_id, comment, like, url, file, create_time, update_time', 'safe', 'on'=>'search'),
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

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'img_id' => 'Img',
			'usr_id' => 'Usr',
			'comment' => 'Comment',
			'like' => 'Like',
			'url' => 'Url',
			'file' => 'File',
			'create_time' => 'Create Time',
			'update_time' => 'Update Time',
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

		$criteria->compare('img_id',$this->img_id);
		$criteria->compare('usr_id',$this->usr_id);
		$criteria->compare('comment',$this->comment,true);
		$criteria->compare('like',$this->like);
		$criteria->compare('url',$this->url,true);
		$criteria->compare('file',$this->file,true);
		$criteria->compare('create_time',$this->create_time);
		$criteria->compare('update_time',$this->update_time,true);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}
}
