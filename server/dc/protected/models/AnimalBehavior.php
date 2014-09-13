<?php

class AnimalBehavior extends CActiveRecordBehavior
{
    public function isNameExist($name)
    {
        return Yii::app()->db->createCommand('SELECT usr_id FROM dc_animal WHERE name=:name')->bindValue(':name', $name)->queryScalar();
    }

    public function attrWithRelated(array $with)
    {
        $attr = $this->owner->getAttributes();
        foreach ($with as $val) {
            $attr = array_merge($attr, $this->owner->$val->getAttributes());
        }

        return $attr;
    }

    public function getTypeName()
    {
        $pet_type = Util::loadConfig('pet_type');

        $n = $this->owner->type/100;
        
        return $pet_type[$n][$this->owner->type];        
    }
    
    public function showTxImage(){
        return CHtml::image(Yii::app()->request->hostInfo.Yii::app()->baseUrl.'/images/tx/'.$this->owner->tx, $this->owner->tx, array('width'=>'50px','max-height'=>'50px'));
    }   

    public function getGender()
    {
        switch($this->owner->gender) {
        case 1:
            return '公';
            break;
        case 2:
            return '母';
            break;
        default:
            break;
        }
    }
}
