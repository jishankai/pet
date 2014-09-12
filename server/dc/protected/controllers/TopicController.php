<?php

class TopicController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            //'getUserId',
        );
    }

    public function actionListApi()
    {
        $topics = Yii::app()->db->createCommand('SELECT t.topic_id AS topic_id, topic, reward, img, start_time, end_time, COUNT(DISTINCT i.aid) AS people FROM dc_topic t LEFT JOIN dc_image i ON t.topic_id=i.topic_id WHERE t.status!=0 GROUP BY t.topic_id')->queryAll();

        $this->echoJsonData(array($topics));        
    }

    public function actionInfoApi($topic_id)
    {
        $topic = Yii::app()->db->createCommand('SELECT des, remark FROM dc_topic WHERE topic_id=:topic_id')->bindValue(':topic_id', $topic_id)->queryRow();
        $topic['txs'] = Yii::app()->db->createCommand('SELECT a.aid, tx FROM dc_animal a INNER JOIN dc_image i ON a.aid=i.aid WHERE i.topic_id=:topic_id GROUP BY tx')->bindValue(':topic_id', $topic_id)->queryColumn(); 
    
        $this->echoJsonData(array($topic));        
    }

    public function actionPopularApi($topic_id, $img_id=NULL)
    {
        if (isset($img_id)) {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url FROM dc_image i WHERE i.topic_id=:topic_id AND i.img_id<:img_id ORDER BY i.likes DESC LIMIT 10')->bindValues(array(':img_id'=>$img_id, ':topic_id'=>$topic_id))->queryAll();        
        } else {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url FROM dc_image i WHERE i.topic_id=:topic_id ORDER BY i.likes DESC LIMIT 10')->bindValue(':topic_id', $topic_id)->queryAll();        
        }

        $this->echoJsonData(array($images));
    }
        
    public function actionNewestApi($topic_id, $img_id=NULL)
    {
        if (isset($img_id)) {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url FROM dc_image i WHERE i.topic_id=:topic_id AND i.img_id<:img_id ORDER BY i.create_time DESC LIMIT 10')->bindValues(array(':img_id'=>$img_id, ':topic_id'=>$topic_id))->queryAll();        
        } else {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url FROM dc_image i WHERE i.topic_id=:topic_id ORDER BY i.create_time DESC LIMIT 10')->bindValue(':topic_id', $topic_id)->queryAll();        
        }

        $this->echoJsonData(array($images));
        
    }

    public function actionRewardApi($topic_id)
    {
        $rewards_str = Yii::app()->db->createCommand('SELECT reward FROM dc_topic WHERE topic_id=:topic_id')->bindValue(':topic_id', $topic_id)->queryScalar();

        $r = array();
        if (isset($rewards_str) && $rewards_str!='') {
            $rewards = explode(';',$rewards_str);
            foreach ($rewards as $reward_str) {
                $reward = explode(':', $reward_str);
                $r[$reward[0]] = Yii::app()->db->createCommand("SELECT * FROM dc_item WHERE item_id IN ($reward[1])")->queryAll();
            }
        }

        $this->echoJsonData(array($r));
    }
}
