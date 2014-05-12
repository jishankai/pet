<?php

class StickerController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
        );
    }

    public function actionListApi()
    {
        $stickers = Sticker::model()->findAllByAttributes(array('uid'=>$this->uid));
        $this->echoJsonData(array('list'=>$stickers));        
    }

    public function actionBuyApi($sti_id)
    {
        $sticker = new Sticker;
        $sticker->sti_id = $sti_id;
        $sticker->uid = $this->uid;
        $sticker->create_time = time();
        $sticker->save();

        $this->echoJsonData(array('isSuccess'=>true));
    }
}
