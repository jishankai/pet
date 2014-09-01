<?php

class TalkController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId',
        );
    }

    public function actionListApi($talk_id=NULL)
    {
        $c = new CDbCriteria;
        $c->addCondition('usra_id=:usra_id OR usrb_id=:usrb_id');
        $c->limit = 30;
        $c->params[':usra_id'] = $this->usr_id;
        $c->params[':usrb_id'] = $this->usr_id;
        $c->order = 'update_time DESC';
        if(isset($talk_id)) {
            $update_time = Yii::app()->db->createCommand('SELECT update_time FROM dc_talk WHERE talk_id=:talk_id')->bindValue(':talk_id', $talk_id)->queryScalar();
            $c->compare('update_time', '<'.$update_time);
        }
        $talks = Talk::model()->findAll($c);

        $r = array();
        foreach ($talks as $talk) {
            $path = Yii::app()->basePath.'/../assets/talks/'.$talk->content;
            if (file_exists($path)) {
                $file = fopen($path, 'r');
                $buf = fread($file,filesize($path));
                $messages = unserialize($buf);
                foreach ($messages as $message) {
                    if ($message['usr_id']!=$this->usr_id && !$message['is_read']) {
                        $message['is_read'] = TRUE;
                        $r[$talk->talk_id]['msg'][$message['timestamp']] = $message['msg'];
                        isset($r[$talk->talk_id]['new_msg'])?$r[$talk->talk_id]['new_msg']++:$r[$talk->talk_id]['new_msg']=1;
                    } else {
                        break;
                    }
                }
                fclose($file);

                if (isset($r[$talk->talk_id])) {
                    if ($this->usr_id==$talk->usra_id) {
                        $r[$talk->talk_id]['usr_id'] = $talk->usrb_id;
                    } else {
                        $r[$talk->talk_id]['usr_id'] = $talk->usra_id;
                    }
                    $tmp = Yii::app()->db->createCommand('SELECT tx, name FROM dc_user WHERE usr_id=:usr_id')->bindValue(':usr_id', $r[$talk->talk_id]['usr_id'])->queryRow();
                    if (isset($tmp)) {
                        $r[$talk->talk_id]['usr_tx'] = $tmp['tx'];
                        $r[$talk->talk_id]['usr_name'] = $tmp['name'];
                    }
                } else {
                    break;
                }
            }
        }
        $this->echoJsonData($r);
    }

    public function actionSendMsgApi()
    {
        $usr_id = $_POST['usr_id'];
        $c = new CDbCriteria;
        $c->addCondition('(usra_id=:usra_id AND usrb_id=:usrb_id) OR (usra_id=:usrb_id AND usrb_id=:usra_id)');
        $c->params[':usra_id'] = $this->usr_id;
        $c->params[':usrb_id'] = $usr_id;
        $talk = Talk::model()->find($c);
        if (empty($talk)) {
            $talk = new Talk();
            $talk->usra_id = $this->usr_id;
            $talk->usrb_id = $usr_id;
            $talk->content = $this->usr_id.'&'.$usr_id;
            $talk->save();
        }
        $path = Yii::app()->basePath.'/../assets/talks/'.$talk->content;
        $file = fopen($path, 'w+');
        $buf = fread($file,filesize($path));
        $messages = unserialize($buf);
        $msg = array(
            'usr_id' => $this->usr_id,
            'msg' => $_POST['msg'],
            'timestamp' => time(),
            'is_read' => FALSE,
        );
        array_unshift($messages, $msg);
        $buf = serialize($messages);
        fwrite($file, $buf);
        fclose($file);

        $this->echoJsonData(array('isSuccess'=>$isSuccess)); 
    }

    public function actionDeleteApi($talk_id)
    {
        $this->echoJsonData(array('isSuccess'=>$isSuccess));
    }

}
