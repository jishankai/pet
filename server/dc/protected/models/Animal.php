<?php

/**
 * This is the model class for table "dc_animal".
 *
 * The followings are the available columns in table 'dc_animal':
 * @property string $aid
 * @property string $name
 * @property string $tx
 * @property integer $gender
 * @property integer $from
 * @property integer $type
 * @property integer $age
 * @property string $address
 * @property string $master_id
 * @property string $items
 * @property integer $d_rq
 * @property integer $w_rq
 * @property integer $m_rq
 * @property integer $t_rq
 * @property string $create_time
 * @property string $update_time
 *
 * The followings are the available model relations:
 * @property User $master
 * @property Circle[] $circles
 */
class Animal extends CActiveRecord
{
	/**
	 * @return string the associated database table name
	 */
	public function tableName()
	{
		return 'dc_animal';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('gender, from, type, age, d_rq, w_rq, m_rq, t_rq', 'numerical', 'integerOnly'=>true),
			array('name, tx', 'length', 'max'=>45),
            array('address, items', 'length', 'max'=>255),
			array('master_id, create_time', 'length', 'max'=>10),
			// The following rule is used by search().
			// @todo Please remove those attributes that should not be searched.
			array('aid, name, tx, gender, from, type, age, address, master_id, items, d_rq, w_rq, m_rq, t_rq, create_time, update_time', 'safe', 'on'=>'search'),
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
			'master' => array(self::BELONGS_TO, 'User', 'master_id'),
			'circles' => array(self::HAS_MANY, 'Circle', 'aid'),
		);
	}

    public function behaviors()
    {
        return array(
            'behavior' => 'AnimalBehavior',
        );
    }

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'aid' => '宠物编号',
			'name' => '名字',
			'tx' => '头像地址',
			'gender' => '性别',
			'from' => '星球',
			'type' => '种族',
			'age' => '年龄',
            'address' => '邮寄地址',
			'master_id' => '主人编号',
			'items' => '礼物',
			'd_rq' => '日人气',
			'w_rq' => '周人气',
			'm_rq' => '月人气',
			't_rq' => '总人气',
			'create_time' => '创建时间',
			'update_time' => '更新时间',
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

		$criteria->compare('aid',$this->aid,true);
		$criteria->compare('name',$this->name,true);
		$criteria->compare('tx',$this->tx,true);
		$criteria->compare('gender',$this->gender);
		$criteria->compare('from',$this->from);
		$criteria->compare('type',$this->type);
		$criteria->compare('age',$this->age);
        $criteria->compare('address',$this->address,true);
		$criteria->compare('master_id',$this->master_id,true);
		$criteria->compare('items',$this->items,true);
		$criteria->compare('d_rq',$this->d_rq);
		$criteria->compare('w_rq',$this->w_rq);
		$criteria->compare('m_rq',$this->m_rq);
		$criteria->compare('t_rq',$this->t_rq);
		$criteria->compare('create_time',$this->create_time,true);
		$criteria->compare('update_time',$this->update_time,true);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}

	/**
	 * Returns the static model of the specified AR class.
	 * Please note that you should have this exact method in all your CActiveRecord descendants!
	 * @param string $className active record class name.
	 * @return Animal the static model class
	 */
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
}
