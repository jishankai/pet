<?php

/**
 * This is the model class for table "dc_mail".
 *
 * The followings are the available columns in table 'dc_mail':
 * @property string $mail_id
 * @property string $usr_id
 * @property string $tx
 * @property string $name
 * @property integer $gender
 * @property string $from_id
 * @property string $body
 * @property integer $is_read
 * @property integer $is_system
 * @property string $create_time
 * @property string $update_time
 * @property integer $is_deleted
 *
 * The followings are the available model relations:
 * @property User $usr
 */
class Mail extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Mail the static model class
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
		return 'dc_mail';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('usr_id, from_id, create_time', 'length', 'max'=>10),
			array('tx, name', 'length', 'max'=>45),
			array('body', 'length', 'max'=>255),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('mail_id, usr_id, tx, name, gender, from_id, body, is_read, is_system, create_time, update_time, is_deleted', 'safe', 'on'=>'search'),
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
			'mail_id' => 'Mail',
			'usr_id' => 'Usr',
			'tx' => 'Tx',
			'name' => 'Name',
			'gender' => 'Gender',
			'from_id' => 'From',
			'body' => 'Body',
			'is_read' => 'Is Read',
			'is_system' => 'Is System',
			'create_time' => 'Create Time',
			'update_time' => 'Update Time',
			'is_deleted' => 'Is Deleted',
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

		$criteria->compare('mail_id',$this->mail_id,true);
		$criteria->compare('usr_id',$this->usr_id,true);
		$criteria->compare('tx',$this->tx,true);
		$criteria->compare('name',$this->name,true);
		$criteria->compare('gender',$this->gender);
		$criteria->compare('from_id',$this->from_id,true);
		$criteria->compare('body',$this->body,true);
		$criteria->compare('is_read',$this->is_read);
		$criteria->compare('is_system',$this->is_system);
		$criteria->compare('create_time',$this->create_time,true);
		$criteria->compare('update_time',$this->update_time,true);
		$criteria->compare('is_deleted',$this->is_deleted);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}
}
