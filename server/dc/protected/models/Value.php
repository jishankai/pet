<?php

/**
 * This is the model class for table "dc_value".
 *
 * The followings are the available columns in table 'dc_value':
 * @property string $usr_id
 * @property string $exp
 * @property string $lv
 * @property string $follow
 * @property string $follower
 * @property string $con_login
 * @property integer $create_time
 * @property string $update_time
 *
 * The followings are the available model relations:
 * @property User $usr
 */
class Value extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Value the static model class
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
		return 'dc_value';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('create_time', 'numerical', 'integerOnly'=>true),
			array('exp, lv, follow, follower, con_login', 'length', 'max'=>10),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('usr_id, exp, lv, follow, follower, con_login, create_time, update_time', 'safe', 'on'=>'search'),
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

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'usr_id' => 'Usr',
			'exp' => 'Exp',
			'lv' => 'Lv',
			'follow' => 'Follow',
			'follower' => 'Follower',
			'con_login' => 'Con Login',
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

		$criteria->compare('usr_id',$this->usr_id,true);
		$criteria->compare('exp',$this->exp,true);
		$criteria->compare('lv',$this->lv,true);
		$criteria->compare('follow',$this->follow,true);
		$criteria->compare('follower',$this->follower,true);
		$criteria->compare('con_login',$this->con_login,true);
		$criteria->compare('create_time',$this->create_time);
		$criteria->compare('update_time',$this->update_time,true);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}
}
