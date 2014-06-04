<?php

/**
 * This is the model class for table "dc_user".
 *
 * The followings are the available columns in table 'dc_user':
 * @property integer $uid
 * @property string $name
 * @property integer $gender
 * @property integer $age
 * @property integer $class
 * @property integer $treasure
 * @property string $code
 * @property integer $inviter
 * @property integer $create_time
 * @property string $update_time
 *
 * The followings are the available model relations:
 * @property Image[] $images
 * @property Stricker[] $strickers
 */
class User extends CActiveRecord
{
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
			//array('inviter, create_time, update_time', 'required'),
			array('gender, age, class, treasure, inviter, create_time', 'numerical', 'integerOnly'=>true),
			array('name', 'length', 'max'=>45),
			array('code', 'length', 'max'=>6),
			// The following rule is used by search().
			// @todo Please remove those attributes that should not be searched.
			array('uid, name, gender, age, class, treasure, code, inviter, create_time, update_time', 'safe', 'on'=>'search'),
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
			'images' => array(self::HAS_MANY, 'Image', 'uid'),
			'strickers' => array(self::HAS_MANY, 'Stricker', 'uid'),
		);
	}

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'uid' => 'Uid',
			'name' => 'Name',
			'gender' => 'Gender',
			'age' => 'Age',
			'class' => 'Class',
			'treasure' => 'Treasure',
			'code' => 'Code',
			'inviter' => 'Inviter',
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

		$criteria->compare('uid',$this->uid);
		$criteria->compare('name',$this->name,true);
		$criteria->compare('gender',$this->gender);
		$criteria->compare('age',$this->age);
		$criteria->compare('class',$this->class);
		$criteria->compare('treasure',$this->treasure);
		$criteria->compare('code',$this->code,true);
		$criteria->compare('inviter',$this->inviter);
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
	 * @return User the static model class
	 */
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}

    public function isNameExist($name)
    {
        return Yii::app()->db->createCommand('SELECT uid FROM dc_user WHERE name=:name')->bindValue(':name', $name)->queryScalar();
    }

    public function getUserIdByCode($code)
    { 
        return Yii::app()->db->createCommand("SELECT uid FROM dc_user WHERE code=:code")->bindValue(':code',$code)->queryScalar();
    }  

    public function register($name, $gender, $age, $class, $inviter)
    {
        $this->name = $name;
        $this->gender = $gender;
        $this->age = $age;
        $this->class = $class;
        $this->inviter = $inviter;

        $this->save();
    }
}
