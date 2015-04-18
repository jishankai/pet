<?php

/**
 * This is the model class for table "dc_gift".
 *
 * The followings are the available columns in table 'dc_gift':
 * @property string $gift_id
 * @property string $name
 * @property integer $level
 * @property string $price
 * @property string $add_rq
 * @property double $ratio
 * @property integer $is_real
 * @property string $detail_image
 * @property integer $sale_status
 * @property string $effect_des
 * @property string $create_time
 * @property string $update_time
 */
class Gift extends CActiveRecord
{
	/**
	 * @return string the associated database table name
	 */
	public function tableName()
	{
		return 'dc_gift';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('level, is_real, sale_status', 'numerical', 'integerOnly'=>true),
			array('ratio', 'numerical'),
			array('gift_id, price, add_rq, create_time', 'length', 'max'=>10),
			array('name', 'length', 'max'=>45),
			array('detail_image, effect_des', 'length', 'max'=>255),
			// The following rule is used by search().
			// @todo Please remove those attributes that should not be searched.
			array('gift_id, name, level, price, add_rq, ratio, is_real, detail_image, sale_status, effect_des, create_time, update_time', 'safe', 'on'=>'search'),
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
            //'behavior' => 'GiftBehavior',
        );
    }

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'gift_id' => '礼物编号',
			'name' => '名称',
			'level' => '等级',
			'price' => '价格',
			'add_rq' => '人气',
			'ratio' => '掉率',
			'is_real' => '是否虚拟（1是0否）',
			'detail_image' => '图片地址',
			'sale_status' => '销售状况（0新品1热卖-1无状态）',
			'effect_des' => '效果描述',
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

		$criteria->compare('gift_id',$this->gift_id,true);
		$criteria->compare('name',$this->name,true);
		$criteria->compare('level',$this->level);
		$criteria->compare('price',$this->price,true);
		$criteria->compare('add_rq',$this->add_rq,true);
		$criteria->compare('ratio',$this->ratio);
		$criteria->compare('is_real',$this->is_real);
		$criteria->compare('detail_image',$this->detail_image,true);
		$criteria->compare('sale_status',$this->sale_status);
		$criteria->compare('effect_des',$this->effect_des,true);
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
	 * @return Gift the static model class
	 */
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
}
