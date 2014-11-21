<?php

class WechatController extends Controller
{
    public function filters() 
    {
        return array(
            'checkWechatSig',
        );
    }

    public function actionMessageApi()
    {
        echo $_GET["echostr"];
    }
}

