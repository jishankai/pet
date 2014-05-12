<?php

/**
 * This is the model class for table "dc_image".
 *
 * The followings are the available columns in table 'dc_image':
 * @property integer $img_id
 * @property integer $uid
 * @property string $comment
 * @property integer $like
 * @property string $url
 * @property string $file
 * @property integer $create_time
 * @property string $update_time
 *
 * The followings are the available model relations:
 * @property User $u
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
			array('uid, url, file, create_time, update_time', 'required'),
			array('uid, like, create_time', 'numerical', 'integerOnly'=>true),
			array('comment, url, file', 'length', 'max'=>255),
			// The following rule is used by search().
			// @todo Please remove those attributes that should not be searched.
			array('img_id, uid, comment, like, url, file, create_time, update_time', 'safe', 'on'=>'search'),
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
			'u' => array(self::BELONGS_TO, 'User', 'uid'),
		);
	}

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'img_id' => 'Img',
			'uid' => 'Uid',
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

		$criteria->compare('img_id',$this->img_id);
		$criteria->compare('uid',$this->uid);
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
