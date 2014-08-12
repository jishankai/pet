<?php

class ImageBehavior extends CActiveRecordBehavior
{
    public function showImage(){
        return CHtml::image(Yii::app()->request->hostInfo.Yii::app()->baseUrl.'/images/upload/'.$this->owner->url, $this->owner->url, array('max-width'=>'240px','max-height'=>'320px'));
    }   

    public function getTopic(array $bar)
    {
        
    }
}
