<?php

/**
 * This is the model class for table "dc_device".
 *
 * The followings are the available columns in table 'dc_device':
 * @property string $id
 * @property string $uid
 * @property string $usr_id
 * @property string $token
 * @property string $terminal
 * @property string $os
 * @property string $create_time
 * @property string $update_time
 *
 * The followings are the available model relations:
 * @property User $usr
 */
class Device extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Device the static model class
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
		return 'dc_device';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('uid, token, terminal', 'length', 'max'=>45),
			array('usr_id, create_time', 'length', 'max'=>10),
			array('os', 'length', 'max'=>25),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('id, uid, usr_id, token, terminal, os, create_time, update_time', 'safe', 'on'=>'search'),
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
            'behavior' => 'DeviceBehavior',
        );
    }

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'id' => '编号',
			'uid' => '设备唯一标示',
			'usr_id' => '用户编号',
			'token' => 'Token',
			'terminal' => '平台',
			'os' => '系统',
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

		$criteria->compare('id',$this->id,true);
		$criteria->compare('uid',$this->uid,true);
		$criteria->compare('usr_id',$this->usr_id,true);
		$criteria->compare('token',$this->token,true);
		$criteria->compare('terminal',$this->terminal,true);
		$criteria->compare('os',$this->os,true);
		$criteria->compare('create_time',$this->create_time,true);
		$criteria->compare('update_time',$this->update_time,true);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}
}
