<?php

class TalkBehavior extends CActiveRecordBehavior
{
    public function sendMsg($usra_id, $usrb_id, $msg)
    {
        $c = new CDbCriteria;
        $c->addCondition('(usra_id=:usra_id AND usrb_id=:usrb_id) OR (usra_id=:usrb_id AND usrb_id=:usra_id)');
        $c->params[':usra_id'] = $usra_id;
        $c->params[':usrb_id'] = $usrb_id;
        $talk = Talk::model()->find($c);
        if (empty($talk)) {
            $talk = new Talk();
            $talk->usra_id = $usra_id;
            $talk->usrb_id = $usrb_id;
            $talk->content = $usra_id.'&'.$usrb_id;
            $talk->save();
        }
        $path = Yii::app()->basePath.'/../assets/talks/'.$talk->content;
        if (file_exists($path)) {
            $buf = file_get_contents($path);
            $messages = unserialize($buf);
        } else {
            $messages = array();
        }
        $msg = array(
            'usr_id' => $usra_id,
            'msg' => $msg,
            'timestamp' => time(),
            'is_read' => FALSE,
        );
        array_unshift($messages, $msg);
        $buf = serialize($messages);
        file_put_contents($path, $buf);
    }
}
