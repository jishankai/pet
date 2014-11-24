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
                    \"name\":\"抽奖必读!\",
                    \"key\":\"key_cjbd\"
                    },
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
            if ($contentStr!='key_cjone') {
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
            } else {
                $msgType = "news";
                $title = "感恩节快到了，球长挥泪大回馈";
                $content = '点击右上角分享至朋友圈，并将朋友圈界面截图发送给我们；\n再在“我要抽奖”-> “第二步” 中获取您的抽奖码，就会自动获取感恩节抽奖资格哟~';
                $picUrl = "https://mmbiz.qlogo.cn/mmbiz/AWNc3NhEROZ88HwfxWQx8nRpySLMcnqcTtB1CT7XiaFWdibvtRWAqqe6LQdsaPnL01ghjgGgf3UGW0bk6F8ztuQQ/0";
                $url = 'http://mp.weixin.qq.com/s?__biz=MjM5OTQwMjYwNw==&mid=201770437&idx=1&sn=26d0825337243589ed6b1d77c5c5c60f&scene=1&from=groupmessage&isappinstalled=0#rd';
                $newsTpl = "<xml>
                    <ToUserName><![CDATA[%s]]></ToUserName>
                    <FromUserName><![CDATA[%s]]></FromUserName>
                    <CreateTime>%s</CreateTime>
                    <MsgType><![CDATA[%s]]></MsgType>
                    <Content><![CDATA[%s]]></Content>
                    <ArticleCount>1</ArticleCount>
                    <Articles>
                        <item>
                            <Title><![CDATA[%s]]></Title>
                            <Description><![CDATA[]]></Description>
                            <PicUrl><![CDATA[%s]]></PicUrl>
                            <Url><![CDATA[%s]]></Url>
                        </item>
                    </Articles>
                    <FuncFlag>0<FuncFlag>
                    </xml>";
                $resultStr = sprintf($newsTpl, $fromUsername, $toUsername, $time, $msgType, $contentStr, $title, $picUrl, $url);
            }
            
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
        case "CLICK":
            $contentStr = $this->receiveClick($object);
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

    private function receiveClick($object)
    {
        switch ($object->Event) {
            case 'key_wytc':
                $contentStr = '吐槽请直接回复“吐槽+您要吐槽的内容”，我们的产品汪会第一时间收集您的反馈并给您答复~\n
                    我们珍惜您的每一次吐槽，感谢您伴我们成长、帮我们做得更好！\n
                    嫌麻烦的话也可以直接发语音，可能答复稍晚，见谅哟~\n';
                break;
            case 'key_gstg':
                $contentStr = '你可以直接回复“投稿+要投稿的文字内容”，或者将文件发送至ntact@aidigame.com';
                break;
            case 'key_fmtg':
                $contentStr = '您所投稿的封面图一经采用，将出现在应用的欢迎封面，供万千用户一睹风采~请注意，作为封面图片，竖长构图最佳。请保证图片内容必须有您的宝贝宠物出镜，真人同时出镜更棒！图片请直接回复~感谢~';
                break;
            case 'key_cjbd':
                $contentStr = '1. 在公众号内点击菜单项 “我要抽奖”-> 点击“第一步”->  收到推送的图文消息“感恩节快到了，球长挥泪大回馈”并将此文分享至朋友圈；\n
                    2. 文章分享后将朋友圈界面截图发送至我们的公众号“爱迪-宠物星球”\n
                    3. 再次在公众号内点击“我要抽奖”-> 点击“第二步”->  自动获得您的感恩节抽奖码\n；
                    4. 活动结束后我们将第一时间公示抽奖情况。\n
                    特别说明：\n
                    需同时完成上述步骤2,3才具备抽奖资格哟~~';
                break;
            case 'key_cjone':
                $contentStr = 'key_cjone';
                break;
            case 'key_cjtwo':
                Yii::app()->db->createCommand('INSERT IGNORE INTO event_3xgiving SET from_usr_name=:from_usr_name')->bindValue(':from_usr_name', $object->FromUserName)->execute();
                $event_usr_id = Yii::app()->db->createCommand('SELECT event_usr_id FROM event_3xgivint WHERE from_usr_name=:from_usr_name')->bindValue(':from_usr_name', $object->FromUserName)->queryScalar();
                $contentStr = '您的感恩节抽奖码是{'.$event_usr_id.'}。活动结束之后，我们将第一时间公布开奖结果~';
                break;
            default:
                // code...
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

