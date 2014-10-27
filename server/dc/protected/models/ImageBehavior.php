<?php

class ImageBehavior extends CActiveRecordBehavior
{
    public function showImage(){
        return CHtml::image('http://pet4upload.oss-cn-beijing-internal.aliyuncs.com/'.$this->owner->url, $this->owner->url, array('max-width'=>'240px','max-height'=>'320px'));
    }   

    public function getTopic(array $bar)
    {
        
    }
}
