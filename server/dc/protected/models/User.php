<?php

/**
 * This is the model class for table "dc_user".
 *
 * The followings are the available columns in table 'dc_user':
 * @property string $usr_id
 * @property string $weibo
 * @property string $wechat
 * @property string $name
 * @property integer $gender
 * @property string $tx
 * @property integer $age
 * @property integer $type
 * @property string $code
 * @property string $inviter
 * @property string $create_time
 * @property string $update_time
 *
 * The followings are the available model relations:
 * @property Account $account
 * @property Device[] $devices
 * @property Friend[] $friends
 * @property Image[] $images
 * @property Mail $mail
 * @property Sticker[] $stickers
 * @property Value $value
 */
class User extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return User the static model class
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
		return 'dc_user';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('gender, age, type', 'numerical', 'integerOnly'=>true),
			array('weibo, wechat, name, tx', 'length', 'max'=>45),
			array('code', 'length', 'max'=>6),
			array('inviter, create_time', 'length', 'max'=>10),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('usr_id, weibo, wechat, name, gender, tx, age, type, code, inviter, create_time, update_time', 'safe', 'on'=>'search'),
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
			'account' => array(self::HAS_ONE, 'Account', 'usr_id'),
			'devices' => array(self::HAS_MANY, 'Device', 'usr_id'),
			'friends' => array(self::HAS_MANY, 'Friend', 'usr_id'),
			'images' => array(self::HAS_MANY, 'Image', 'usr_id'),
			'mail' => array(self::HAS_ONE, 'Mail', 'usr_id'),
			'stickers' => array(self::HAS_MANY, 'Sticker', 'usr_id'),
			'value' => array(self::HAS_ONE, 'Value', 'usr_id'),
		);
	}

    public function behaviors()
    {
        return array(
            'behavior' => 'UserBehavior',
        );
    }

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'usr_id' => '用户编号',
			'weibo' => '微博账号',
			'wechat' => '微信账号',
			'name' => '昵称',
			'gender' => '性别',
			'tx' => '头像',
			'age' => '年龄',
			'type' => '类型',
			'code' => '邀请码',
			'inviter' => '邀请者',
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

		$criteria->compare('usr_id',$this->usr_id,true);
		$criteria->compare('weibo',$this->weibo,true);
		$criteria->compare('wechat',$this->wechat,true);
		$criteria->compare('name',$this->name,true);
		$criteria->compare('gender',$this->gender);
		$criteria->compare('tx',$this->tx,true);
		$criteria->compare('age',$this->age);
		$criteria->compare('type',$this->type);
		$criteria->compare('code',$this->code,true);
		$criteria->compare('inviter',$this->inviter,true);
		$criteria->compare('create_time',$this->create_time,true);
		$criteria->compare('update_time',$this->update_time,true);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}
}
