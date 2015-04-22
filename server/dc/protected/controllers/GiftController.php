<?php

class GiftController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            //'getUserId',
        );
    }

    public function actionListApi($code='0')
    {
        $r = Yii::app()->db->createCommand('SELECT * FROM dc_gift')->queryAll();
        if (isset($r)) {
            $str='';
            foreach ($r as $v) {
                $str = $str.implode(',', $v);
            }
            $tmp_code = md5($str);
        } else {
            $tmp_code = '0';
        }
        
        if ($code==$tmp_code) {
            $this->echoJsonData(array('is_update'=>FALSE));        
        } else {
            $this->echoJsonData(array(
                'is_update'=>TRUE,
                'gifts'=>$r,
                'code' => $tmp_code,
            ));        
        }
    }

}
