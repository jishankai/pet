<?php

/**
 * This is the model class for table "dc_item".
 *
 * The followings are the available columns in table 'dc_item':
 * @property string $item_id
 * @property string $name
 * @property string $icon
 * @property string $desc
 * @property string $img
 * @property string $price
 * @property integer $rq
 * @property integer $exp
 * @property integer $type
 * @property string $create_time
 * @property string $update_time
 */
class Item extends CActiveRecord
{
	/**
	 * @return string the associated database table name
	 */
	public function tableName()
	{
		return 'dc_item';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('rq, exp, type', 'numerical', 'integerOnly'=>true),
			array('name, icon, img', 'length', 'max'=>255),
			array('price, create_time', 'length', 'max'=>10),
			// The following rule is used by search().
			// @todo Please remove those attributes that should not be searched.
			array('item_id, name, icon, desc, img, price, rq, exp, type, create_time, update_time', 'safe', 'on'=>'search'),
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

    public function behaviors()
    {
        return array(
            //'behavior' => 'ItemBehavior',
        );
    }

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'item_id' => '物品编号',
			'name' => '名称',
			'icon' => '标志',
			'desc' => '描述',
			'img' => '图片地址',
			'price' => '价格',
			'rq' => '人气变化',
			'exp' => '增加经验',
			'type' => '类别',
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

		$criteria->compare('item_id',$this->item_id,true);
		$criteria->compare('name',$this->name,true);
		$criteria->compare('icon',$this->icon,true);
		$criteria->compare('desc',$this->desc,true);
		$criteria->compare('img',$this->img,true);
		$criteria->compare('price',$this->price,true);
		$criteria->compare('rq',$this->rq);
		$criteria->compare('exp',$this->exp);
		$criteria->compare('type',$this->type);
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
	 * @return Item the static model class
	 */
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
}
