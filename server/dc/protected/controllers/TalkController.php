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

    public function actionListApi()
    {
        $c = new CDbCriteria;
        $c->addCondition('usra_id=:usra_id OR usrb_id=:usrb_id AND is_block!=:usr_id');
        //$c->limit = 30;
        $c->params[':usra_id'] = $this->usr_id;
        $c->params[':usrb_id'] = $this->usr_id;
        $c->params[':usr_id'] = $this->usr_id;
        $c->order = 'update_time DESC';
        /*
        if(isset($talk_id)) {
            $update_time = Yii::app()->db->createCommand('SELECT update_time FROM dc_talk WHERE talk_id=:talk_id')->bindValue(':talk_id', $talk_id)->queryScalar();
            $c->compare('update_time', '<'.$update_time);
        }
         */
        $talks = Talk::model()->findAll($c);

        $r = array();
        foreach ($talks as $talk) {
            $obj = Yii::app()->oss->get_obj(OSS_PREFIX.'4talks',$talk->content);
            if (isset($obj)) {
                $arr = array();
                //$buf = file_get_contents($path);
                $buf = Yii::app()->oss->get_obj_content(OSS_PREFIX.'4talks', $talk->content);
                $messages = unserialize($buf);
                if ($messages!=FALSE) {
                    foreach ($messages as $k=>$message) {
                        if ($message['usr_id']!=$this->usr_id && !$message['is_read']) {
                            $message['is_read'] = TRUE;
                            $arr[$talk->talk_id]['msg'][$message['timestamp']] = $message['msg'];
                            isset($arr[$talk->talk_id]['new_msg'])?$arr[$talk->talk_id]['new_msg']++:$arr[$talk->talk_id]['new_msg']=1;
                            $messages[$k] = $message;
                        } else {
                            break;
                        }
                    }
                    $buf = serialize($messages);
                    //file_put_contents($path, $buf);
                    Yii::app()->oss->upload(OSS_PREFIX.'4talks', $talk->content, $buf);
                }

                if (isset($arr[$talk->talk_id])) {
                    if ($this->usr_id==$talk->usra_id) {
                        $arr[$talk->talk_id]['usr_id'] = $talk->usrb_id;
                    } else {
                        $arr[$talk->talk_id]['usr_id'] = $talk->usra_id;
                    }
                    $tmp = Yii::app()->db->createCommand('SELECT tx, name FROM dc_user WHERE usr_id=:usr_id')->bindValue(':usr_id', $arr[$talk->talk_id]['usr_id'])->queryRow();
                    if (isset($tmp)) {
                        $arr[$talk->talk_id]['usr_tx'] = $tmp['tx'];
                        $arr[$talk->talk_id]['usr_name'] = $tmp['name'];
                    }
                    $r[] = $arr;
                }
            } else {
                break;
            }
        }
        $this->echoJsonData($r);
    }

    public function actionSendMsgApi($usr_id)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
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
            $obj = Yii::app()->oss->get_obj(OSS_PREFIX.'4talks',$talk->content);
            if (isset($obj)) {
                //$buf = file_get_contents($path);
                $buf = Yii::app()->oss->get_obj_content(OSS_PREFIX.'4talks', $talk->content);
                $messages = unserialize($buf);
            } else {
                $messages = array();
            }
            $msg = array(
                'usr_id' => $this->usr_id,
                'msg' => $_POST['msg'],
                'timestamp' => time(),
                'is_read' => FALSE,
            );
            array_unshift($messages, $msg);
            $buf = serialize($messages);
            //file_put_contents($path, $buf);
            Yii::app()->oss->upload(OSS_PREFIX.'4talks', $talk->content, $buf);
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        /*
        $file = fopen($path, 'w+');
        clearstatcache();
        var_dump(filesize($path));
        if (filesize($path)>0) {
            $buf = fread($file,filesize($path));
            $messages = unserialize($buf);
        } else {
            $messages = array();
        }
        $msg = array(
            'usr_id' => $this->usr_id,
            'msg' => $_POST['msg'],
            'timestamp' => time(),
            'is_read' => FALSE,
        );
        array_unshift($messages, $msg);
        $buf = serialize($messages);
        fwrite($file, $buf);
        clearstatcache();
        fclose($file);
         */
        $this->echoJsonData(array('isSuccess'=>TRUE)); 
    }

    public function actionSearchApi($usr_id)
    {
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

        $this->echoJsonData(array('talk_id'=>$talk->talk_id));
    }

    public function actionDeleteApi($talk_id)
    {
        $this->echoJsonData(array('isSuccess'=>TRUE));
    }

    public function actionBlockApi($talk_id)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $talk = Talk::model()->findByPk($talk_id);
            $talk->is_block = 1;
            $talk->saveAttributes(array('is_block'));

            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        $this->echoJsonData(array('isSuccess'=>TRUE));
    }

    public function actionUnBlockApi($usr_id)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            Yii::app()->db->createCommand('UPDATE dc_talk SET is_block=0 WHERE (usra_id=:usra_id AND usrb_id=:usrb_id) OR (usrb_id=:usra_id AND usra_id=:usrb_id) AND is_block=:usra_id')->bindValues(array(':usra_id'=>$this->usr_id, ':usrb_id'=>$usr_id))->execute();
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        $this->echoJsonData(array('isSuccess'=>TRUE));
    }

    public function actionBlockListApi()
    {
        $usra_ids = Yii::app()->db->createCommand('SELECT usra_id FROM dc_talk WHERE usrb_id=:usr_id AND is_block=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryColumn(); 
        $usrb_ids = Yii::app()->db->createCommand('SELECT usrb_id FROM dc_talk WHERE usra_id=:usr_id AND is_block=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryColumn(); 
        $usr_ids = array_merge($usra_ids, $usrb_ids);
        if (empty($usr_ids)) {
            $r = array();
        } else {
            $r = Yii::app()->db->createCommand('SELECT usr_id, name, tx FROM dc_user WHERE usr_id IN (:usr_ids)')->bindValue(':usr_ids', implode(',',$usr_ids))->queryAll();
        }

        $this->echoJsonData($r); 
    }
}
