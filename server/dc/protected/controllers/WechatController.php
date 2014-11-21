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
                    $contentStr = '';
                    break;
            }
        } else {
            $contentStr = '';
        }

        return $contentStr = '';
    }

    private function receiveEvent($object)
    {
        $contentStr = "";
        switch ($object->Event)
        {
        case "subscribe":
            $contentStr = "感谢关注宠物星球社交应用~您的陪伴和支持将使我们做得更好~更多内容请使用下方菜单，或联系contact@aidigame.com~mo-害羞";    //关注后回复内容
            break;
        case "unsubscribe":
            $contentStr = "";
            break;
        case "CLICK":
            $contentStr =  $this->receiveClick($object);    //点击事件
            break;
        default:
            $contentStr = "receive a new event: ".$object->Event;
            break;
        }

        return $contentStr;
    }
}

