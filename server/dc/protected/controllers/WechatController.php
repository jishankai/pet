<?php

class WechatController extends Controller
{
    public function filters()
    {
        return array(
            //'checkWechatSig',
        );
    }

    public function actionGetMenuApi()
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

        $url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token={$token}"; //查询地址
        $ch = curl_init();//新建curl
        curl_setopt($ch, CURLOPT_URL, $url);//url
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $b = curl_exec($ch); //输出
        curl_close($ch);

        echo $b;
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
                \"name\":\"内容回顾\",
                \"sub_button\":[
                    {
                    \"type\":\"click\",
                    \"name\":\"萌星物语\",
                    \"key\":\"key_mxwy\"
                    },
                    {
                    \"type\":\"click\",
                    \"name\":\"宠物讲堂\",
                    \"key\":\"key_cwjt\"
                    },
                    {
                    \"type\":\"click\",
                    \"name\":\"往期活动\",
                    \"key\":\"key_wqhd\"
                    },
                    {
                    \"type\":\"click\",
                    \"name\":\"搞笑图片\",
                    \"key\":\"key_gxtp\"
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
                $resultStr = $this->receiveText($postObj);
                break;
            case 'event':
                $resultStr = $this->receiveEvent($postObj);
                break;

            default:
                break;
            }

                /*
                $event_usr_id = Yii::app()->db->createCommand('SELECT event_usr_id FROM event_3xgiving WHERE from_usr_name=:from_usr_name')->bindValue(':from_usr_name', $postObj->FromUserName)->queryScalar();
                if (!$event_usr_id) {
                    Yii::app()->db->createCommand('INSERT IGNORE INTO event_3xgiving SET from_usr_name=:from_usr_name')->bindValue(':from_usr_name', $postObj->FromUserName)->execute();
                }
                $msgType = "news";
                $title = "感恩节快到了，球长挥泪大回馈";
                $content = '点击右上角分享至朋友圈，并将朋友圈界面截图发送给我们；再在“我要抽奖”-> “第二步” 中获取您的抽奖码，就会自动获取感恩节抽奖资格哟~';
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
                 */

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
                $resultStr = $this->transmitText($object, '我爱周泓');
                break;
            case '1':
                $a = Util::loadConfig('wechat_mxwy');
                $resultStr = $this->transmitNews($object, $a);
                break;
            case '2':
                $a = Util::loadConfig('wechat_cwjt');
                $resultStr = $this->transmitNews($object, $a);
                break;
            case '3':
                $a = Util::loadConfig('wechat_wqhd');
                $resultStr = $this->transmitNews($object, $a);
                break;
            case '4':
                $a = Util::loadConfig('wechat_gxtp');
                $resultStr = $this->transmitNews($object, $a);
                break;
            default:
                break;
            }
        }
        if (isset($resultStr)) {
            return $resultStr;
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
            $contentStr = "地球人泥嚎，恭喜你成功登陆宠物星球！本球长已经等你好久了啊喂～           欢迎带着萌宠来星球定居，本球长和众事务官将专职陪你一起卖萌一起飞！大家有事儿骚扰，没事儿卖萌♪(^∇^*)准备好了吗？一大波萌宠马上就要来袭了．．．
回复【1】查看暖暖哒【萌星物语】
回复【2】查看实用哒【宠物讲堂】
回复【3】查看精彩哒【往期活动】
回复【4】查看蠢萌哒【搞笑图片】";
            $resultStr = $this->transmitText($object, $contentStr);
            break;
        case "CLICK":
            $resultStr = $this->receiveClick($object);
            break;
        default:
            break;
        }
        if (isset($resultStr)) {
            return $resultStr;
        } else {
            echo '';
            exit;
        }
    }

    private function receiveClick($object)
    {
        switch ($object->EventKey) {
            case 'key_wytc':
                $contentStr = '吐槽请直接回复“吐槽+您要吐槽的内容”，我们的产品汪会第一时间收集您的反馈并给您答复~
我们珍惜您的每一次吐槽，感谢您伴我们成长、帮我们做得更好！
                    嫌麻烦的话也可以直接发语音，可能答复稍晚，见谅哟~';
                $resultStr = $this->transmitText($object, $contentStr);
                break;
            case 'key_gstg':
                $contentStr = '你可以直接回复“投稿+要投稿的文字内容”，或者将文件发至contact@aidigame.com';
                $resultStr = $this->transmitText($object, $contentStr);
                break;
            case 'key_fmtg':
                $contentStr = '您所投稿的封面图一经采用，将出现在应用的欢迎封面，供万千用户一睹风采~请注意，作为封面图片，竖长构图最佳。请保证图片内容必须有您的宝贝宠物出镜，真人同时出镜更棒！图片请直接回复~感谢~';
                $resultStr = $this->transmitText($object, $contentStr);
                break;
            case 'key_cjbd':
                $contentStr = '1. 在公众号内点击菜单项 “我要抽奖”-> 点击“第一步”->  收到推送的图文消息“感恩节快到了，球长挥泪大回馈”并将此文分享至朋友圈；
2. 文章分享后将朋友圈界面截图发送至我们的公众号“爱迪-宠物星球”
3. 再次在公众号内点击“我要抽奖”-> 点击“第二步”->  自动获得您的感恩节抽奖码；
4. 活动结束后我们将第一时间公示抽奖情况。
特别说明：
需同时完成上述步骤2,3才具备抽奖资格哟~~';
                $resultStr = $this->transmitText($object, $contentStr);
                break;
            case 'key_cjone':
                $contentStr = 'key_cjone';
                break;
            case 'key_cjtwo':
                $event_usr_id = Yii::app()->db->createCommand('SELECT event_usr_id FROM event_3xgiving WHERE from_usr_name=:from_usr_name')->bindValue(':from_usr_name', $object->FromUserName)->queryScalar();
                if (!$event_usr_id) {
                    $contentStr = '请您先执行抽奖第一步';
                } else {
                    $contentStr = '您的感恩节抽奖码是{'.$event_usr_id.'}。活动结束之后，我们将第一时间公布开奖结果~';
                }
                $resultStr = $this->transmitText($object, $contentStr);
                break;
            case 'key_mxwy':
                $a = Util::loadConfig('wechat_mxwy');
                $resultStr = $this->transmitNews($object, $a);
                break;
            case 'key_gxtp':
                $a = Util::loadConfig('wechat_gxtp');
                $resultStr = $this->transmitNews($object, $a);
                break;
            case 'key_cwjt':
                $a = Util::loadConfig('wechat_cwjt');
                $resultStr = $this->transmitNews($object, $a);
                break;
            case 'key_wqhd':
                $a = Util::loadConfig('wechat_wqhd');
                $resultStr = $this->transmitNews($object, $a);
                break;
            default:
                // code...
                break;
        }
        if (isset($resultStr)) {
            return $resultStr;
        } else {
            echo '';
            exit;
        }
    }

    private function transmitText($object, $content)
    {
        $textTpl = "<xml>
            <ToUserName><![CDATA[%s]]></ToUserName>
            <FromUserName><![CDATA[%s]]></FromUserName>
            <CreateTime>%s</CreateTime>
            <MsgType><![CDATA[%s]]></MsgType>
            <Content><![CDATA[%s]]></Content>
            <FuncFlag>0<FuncFlag>
            </xml>";
        $msgType = "text";
        $resultStr = sprintf($textTpl, $object->FromUserName, $object->ToUserName, time(), $msgType, $content);

        return $resultStr;
    }

    private function transmitNews($object, $arr_item)
    {
        if(!is_array($arr_item)) return;

        $itemTpl = "
            <item>
            <Title><![CDATA[%s]]></Title>
            <Description><![CDATA[%s]]></Description>
            <PicUrl><![CDATA[%s]]></PicUrl>
            <Url><![CDATA[%s]]></Url>
            </item>
            ";
        $item_str = "";
        foreach ($arr_item as $item)
            $item_str .= sprintf($itemTpl, $item['Title'], $item['Description'], $item['PicUrl'], $item['Url']);

        $newsTpl = "<xml>
            <ToUserName><![CDATA[%s]]></ToUserName>
            <FromUserName><![CDATA[%s]]></FromUserName>
            <CreateTime>%s</CreateTime>
            <MsgType><![CDATA[news]]></MsgType>
            <ArticleCount>%s</ArticleCount>
            <Articles>
            $item_str
            </Articles>
            <FuncFlag>0<FuncFlag>
            </xml>";
        $resultStr = sprintf($newsTpl, $object->FromUserName, $object->ToUserName, time(), count($arr_item));

        return $resultStr;
    }

    public function actionCallback($code, $state)
    {
        if (!isset($code)) {
            Yii::app()->wechat->get_code_by_authorize($state);
        }
        parse_str($state);
        $u = Yii::app()->wechat->get_userinfo_by_authorize($code);
        $params = array(
            'uid'=>$u['openid'],
        );
        $params['sig'] = $this->signature($params);
        $json = file_get_contents($this->createAbsoluteUrl('user/loginApi', $params));
        $j = json_decode($json);
        if (!$j->data->isSuccess) {
            $r = Yii::app()->db->createCommand('SELECT a.aid,a.name,a.gender,a.age,a.type FROM dc_animal a WHERE aid=:aid')->bindValue('aid', $aid)->queryRow();
            $params = array(
                'aid'=>$r['aid'],
                'name'=>$r['name'],
                'gender'=>$r['gender'],
                'age'=>$r['age'],
                'type'=>$r['type'],
                'u_name'=>'萌'.mb_substr($u['unionid'], 0, 6),
                'u_gender'=>$u['sex'],
                'u_city'=>1001,
                'code'=>'',
                'wechat'=>$u['unionid'],
                'SID'=>$j->data->SID,
            );
            $params['sig'] = $this->signature($params);
            $res_register = file_get_contents($this->createAbsoluteUrl('user/registerApi', $params));
            $json_register = json_decode($res_register);
            if (!isset($json_register->usr_id)) {
                Yii::app()->wechat->get_code_by_authorize($state);
            }
        }
        $oauth2 = Yii::app()->wechat;
        $session = Yii::app()->session->readSession($j->data->SID);
        setcookie('wechatauth2_'.$oauth2->APPID, http_build_query(array('usr_id'=>$session['usr_id'])));
        if ($img_id==0) {
            $this->renderPartial('/social/activity_view_'.$aid, array('aid'=>$aid, 'sid'=>$j->data->SID));
        } else {
            $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.aid, i.cmt, i.food, i.create_time, a.name, a.tx, a.type, a.gender, u.usr_id, u.tx AS u_tx, u.name AS u_name  FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE i.img_id=:img_id')->bindValue(':img_id', $img_id)->queryRow();
            $this->renderPartial('/social/food', array('r'=>$r, 'img_id'=>$img_id, 'to'=>'wechat', 'aid'=>$aid, 'sid'=>$j->data->SID));
        }
    }

}

