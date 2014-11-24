<?php

class WechatController extends Controller
{
    public function filters() 
    {
        return array(
            //'checkWechatSig',
        );
    }

    public function actionUpdateMenuApi()
    {
        $appid="";//填写appid
        $secret="";//填写secret

        $url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={$appid}&secret={$secret}";
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL,$url);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); 
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $a = curl_exec($ch);


        $strjson=json_decode($a);
        $token = $strjson->access_token;
        $post="{
            \"button\":[
                {
                \"type\":\"click\",
                \"name\":\"关于应用\",
                \"sub_button\":[
                    {
                    \"type\":\"view\",
                    \"name\":\"应用下载\",
                    \"url\":\"http://home4pet.aidigame.com\"
                    },
                    {
                    \"type\":\"click\",
                    \"name\":\"我要吐槽\",
                    \"key\":\"key_wytc\"
                    }
                ]
                },
                {
                \"type\":\"click\",
                \"name\":\"我要投稿\",
                \"sub_button\":[
                    {
                    \"type\":\"click\",
                    \"name\":\"故事投稿\",
                    \"key\":\"key_gstg\"
                    },
                    {
                    \"type\":\"click\",
                    \"name\":\"封面投稿\",
                    \"key\":\"key_fmtg\"
                    }

                ]
                },

                {
                \"type\":\"click\",
                \"name\":\"我要抽奖\",
                \"sub_button\":[
                    {
                    \"type\":\"click\",
                    \"name\":\"抽奖第一步\",
                    \"key\":\"key_cjone\"
                    },
                    {
                    \"type\":\"click\",
                    \"name\":\"抽奖第二步\",
                    \"key\":\"key_cjtwo\"
                    },
                ]
                }
        ]
    }";  //提交内容
    $url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token={$token}"; //查询地址 
    $ch = curl_init();//新建curl
    curl_setopt($ch, CURLOPT_URL, $url);//url  
    curl_setopt($ch, CURLOPT_POST, 1);  //post
    curl_setopt($ch, CURLOPT_POSTFIELDS, $post);//post内容  
    curl_exec($ch); //输出   
    curl_close($ch); 
    }

    public function actionMessageApi()
    {
        $postStr = $GLOBALS["HTTP_RAW_POST_DATA"];
        if (!empty($postStr)){
            $postObj = simplexml_load_string($postStr, 'SimpleXMLElement', LIBXML_NOCDATA);
            $RX_TYPE = trim($postObj->MsgType);
            switch ($RX_TYPE) {
            case 'text':
                $contentStr = $this->receiveText($postObj);
                break;
            case 'event':
                $contentStr = $this->receiveEvent($postObj);
                break;

            default:
                $contentStr = '你好啊';
                break;
            }

            $fromUsername = $postObj->FromUserName;
            $toUsername = $postObj->ToUserName;
            $time = time();
            $textTpl = "<xml>
                <ToUserName><![CDATA[%s]]></ToUserName>
                <FromUserName><![CDATA[%s]]></FromUserName>
                <CreateTime>%s</CreateTime>
                <MsgType><![CDATA[%s]]></MsgType>
                <Content><![CDATA[%s]]></Content>
                <FuncFlag>0<FuncFlag>
                </xml>";
            $msgType = "text";
            $resultStr = sprintf($textTpl, $fromUsername, $toUsername, $time, $msgType, $contentStr);
            echo $resultStr;
        }else {
            echo '';
            exit;
        }
    }

    private function receiveText($object)
    {
        $keyword = trim($object->Content);
        if(!empty( $keyword )) {
            switch ($keyword) {
            case '感谢有你':
                $contentStr = '1';
                break;

            default:
                break;
            }
        }
        if (isset($contentStr)) {
            return $contentStr;
        } else {
            echo '';
            exit;
        }
    }

    private function receiveEvent($object)
    {
        switch ($object->Event)
        {
        case "subscribe":
            $contentStr = "感谢关注宠物星球社交应用~您的陪伴和支持将使我们做得更好~更多内容请使用下方菜单，或联系contact@aidigame.com~mo-害羞";    //关注后回复内容
            break;
        default:
            break;
        }
        if (isset($contentStr)) {
            return $contentStr;
        } else {
            echo '';
            exit;
        }
    }
}

