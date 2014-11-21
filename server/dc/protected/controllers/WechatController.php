<?php

class WechatController extends Controller
{
    public function filters() 
    {
        return array(
            'checkWechatSig',
        );
    }

    public function actionVerifyApi()
    {
        echo $_GET["echostr"];
    }
}

