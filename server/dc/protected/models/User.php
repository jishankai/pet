<?php

/**
 * This is the model class for table "dc_user".
 *
 * The followings are the available columns in table 'dc_user':
 * @property string $usr_id
 * @property string $name
 * @property integer $gender
 * @property string $tx
 * @property integer $age
 * @property integer $type
 * @property string $code
 * @property integer $inviter
 * @property integer $create_time
 * @property string $update_time
 *
 * The followings are the available model relations:
 * @property Account $account
 * @property Device[] $devices
 * @property Friend[] $friends
 * @property Image[] $images
 * @pooperty Qq $qq
 * @property Value $value
 * @property Weibo $weibo
 */
class User extends CActiveRecord
{
    public $relation = NULL;

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
			array('gender, age, type, inviter, create_time', 'numerical', 'integerOnly'=>true),
			array('name, tx', 'length', 'max'=>45),
			array('code', 'length', 'max'=>6),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('usr_id, name, gender, tx, age, type, code, inviter, create_time, update_time', 'safe', 'on'=>'search'),
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
			'qq' => array(self::HAS_ONE, 'Qq', 'usr_id'),
			'value' => array(self::HAS_ONE, 'Value', 'usr_id'),
			'weibo' => array(self::HAS_ONE, 'Weibo', 'usr_id'),
            'friend' => array(self::HAS_MANY, 'Friend', 'usr_id'),
		);
	}

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'usr_id' => 'Usr',
			'name' => 'Name',
			'gender' => 'Gender',
			'tx' => 'Tx',
			'age' => 'Age',
			'type' => 'Type',
			'code' => 'Code',
			'inviter' => 'Inviter',
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
		$criteria->compare('name',$this->name,true);
		$criteria->compare('gender',$this->gender);
		$criteria->compare('tx',$this->tx,true);
		$criteria->compare('age',$this->age);
		$criteria->compare('type',$this->type);
		$criteria->compare('code',$this->code,true);
		$criteria->compare('inviter',$this->inviter);
		$criteria->compare('create_time',$this->create_time);
		$criteria->compare('update_time',$this->update_time,true);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}

    public function isNameExist($name)
    {
        return Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE name=:name')->bindValue(':name', $name)->queryScalar();
    }

    public function getUserIdByCode($code)
    { 
        return Yii::app()->db->createCommand("SELECT usr_id FROM dc_user WHERE code=:code")->bindValue(':code',$code)->queryScalar();
    }  

    public function initialize($event)
    {
        $v = new Value;
        $v->usr_id = $this->usr_id;
        $v->save();
    }

    public function rewardInviter($event)
    {
        
    }

    public function login()
    {
        
    }
}
