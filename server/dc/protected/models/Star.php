<?php

/**
 * This is the model class for table "dc_star".
 *
 * The followings are the available columns in table 'dc_star':
 * @property string $star_id
 * @property string $banner
 * @property string $url
 * @property string $start_time
 * @property string $end_time
 * @property string $create_time
 * @property string $update_time
 */
class Star extends CActiveRecord
{
	/**
	 * @return string the associated database table name
	 */
	public function tableName()
	{
		return 'dc_star';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('star_id, start_time, end_time, create_time', 'length', 'max'=>10),
			array('name, title', 'length', 'max'=>45),
			array('icon, description, banner, url', 'length', 'max'=>255),
			// The following rule is used by search().
			// @todo Please remove those attributes that should not be searched.
			array('star_id, name, title, icon, description, banner, url, start_time, end_time, create_time, update_time', 'safe', 'on'=>'search'),
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
            //'behavior' => 'StarBehavior',
        );
    }

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'star_id' => '活动编号',
			'name' => '活动名称',
			'title' => '分享标题',
			'icon' => '分享图片',
			'description' => '分享描述',
			'banner' => 'Banner图片',
			'url' => '跳转链接',
			'start_time' => '开始时间',
			'end_time' => '结束时间',
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

		$criteria->compare('star_id',$this->star_id,true);
		$criteria->compare('name',$this->name,true);
		$criteria->compare('title',$this->title,true);
		$criteria->compare('icon',$this->icon,true);
		$criteria->compare('description',$this->description,true);
		$criteria->compare('banner',$this->banner,true);
		$criteria->compare('url',$this->url,true);
		$criteria->compare('start_time',$this->start_time,true);
		$criteria->compare('end_time',$this->end_time,true);
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
	 * @return Star the static model class
	 */
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}

	protected function beforeValidate()
	{
		$this->start_time = strtotime($this->start_time);
		$this->end_time = strtotime($this->end_time);
		$this->create_time = strtotime($this->create_time);
		return parent::beforeValidate();
	}
}
