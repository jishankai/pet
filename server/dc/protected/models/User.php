<?php

/**
 * This is the model class for table "dc_user".
 *
 * The followings are the available columns in table 'dc_user':
 * @property string $usr_id
 * @property string $name
 * @property string $tx
 * @property integer $gender
 * @property integer $city
 * @property string $weibo
 * @property string $wechat
 * @property string $password
 * @property integer $age
 * @property string $exp
 * @property string $lv
 * @property string $gold
 * @property string $items
 * @property string $con_login
 * @property string $login_time
 * @property string $vip
 * @property string $aid
 * @property string $code
 * @property string $inviter
 * @property integer $reports
 * @property string $create_time
 * @property string $update_time
 * @property integer $is_ban
 *
 * The followings are the available model relations:
 * @property Animal[] $animals
 * @property Animal[] $dcAnimals
 * @property Animal[] $dcAnimals1
 * @property Mail[] $mails
 * @property Sticker[] $stickers
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
			array('gender, city, age, reports, is_ban', 'numerical', 'integerOnly'=>true),
			array('name, tx, weibo, wechat, password', 'length', 'max'=>45),
            array('exp, lv, gold, con_login, login_time, vip, aid, inviter, create_time', 'length', 'max'=>10),
			array('code', 'length', 'max'=>6),
			// The following rule is used by search().
			// @todo Please remove those attributes that should not be searched.
			array('usr_id, name, tx, gender, city, weibo, wechat, password, age, exp, lv, gold, items, con_login, login_time, vip, aid, code, inviter, reports, create_time, update_time, is_ban', 'safe', 'on'=>'search'),
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
			'animals' => array(self::HAS_MANY, 'Animal', 'master_id'),
			'dcAnimals' => array(self::MANY_MANY, 'Animal', 'dc_circle(usr_id, aid)'),
			'dcAnimals1' => array(self::MANY_MANY, 'Animal', 'dc_follow(usr_id, aid)'),
			'mails' => array(self::HAS_MANY, 'Mail', 'usr_id'),
			'stickers' => array(self::HAS_MANY, 'Sticker', 'usr_id'),
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
			'name' => '姓名',
			'tx' => '头像地址',
			'gender' => '性别',
			'city' => '城市',
			'weibo' => '微博账号',
            'wechat' => '微信账号',
            'password' => '密码',
			'age' => '年龄',
			'exp' => '经验',
			'lv' => '等级',
            'gold' => '金币',
            'items' => '道具',
			'con_login' => '连续登录时间',
			'login_time' => '上次登录时间',
			'vip' => 'VIP值',
			'aid' => '宠物编号',
			'code' => '邀请码',
			'inviter' => '邀请者',
			'reports' => '举报数',
			'create_time' => '创建时间',
			'update_time' => '更新时间',
			'is_ban' => '禁言',
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

		$criteria->compare('usr_id',$this->usr_id,true);
		$criteria->compare('name',$this->name,true);
		$criteria->compare('tx',$this->tx,true);
		$criteria->compare('gender',$this->gender);
		$criteria->compare('city',$this->city);
		$criteria->compare('weibo',$this->weibo,true);
		$criteria->compare('wechat',$this->wechat,true);
		$criteria->compare('password',$this->password,true);
		$criteria->compare('age',$this->age);
		$criteria->compare('exp',$this->exp,true);
		$criteria->compare('lv',$this->lv,true);
		$criteria->compare('gold',$this->gold,true);
		$criteria->compare('items',$this->items,true);
		$criteria->compare('con_login',$this->con_login,true);
		$criteria->compare('login_time',$this->login_time,true);
		$criteria->compare('vip',$this->vip,true);
		$criteria->compare('aid',$this->aid,true);
		$criteria->compare('code',$this->code,true);
		$criteria->compare('inviter',$this->inviter,true);
		$criteria->compare('reports',$this->reports);
		$criteria->compare('create_time',$this->create_time,true);
		$criteria->compare('update_time',$this->update_time,true);
		$criteria->compare('is_ban',$this->is_ban);

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
}
