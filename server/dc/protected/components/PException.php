<?php

class PException extends CException
{
    
    /**
     * 
     */
    public function __construct($message, $data=NULL)
    {
        $this->message = urlencode($message);
        Yii::app()->controller->response->setData($data);
    }
}
