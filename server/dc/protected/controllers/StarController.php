<?php

class StarController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId - listApi, popularApi',
        );
    }

    public function actionListApi()
    {
        $stars = Yii::app()->db->createCommand('SELECT star_id, banner, url FROM dc_star where start_time<=:time AND end_time>:time')->bindValue(':time', time())->queryAll();

        foreach ($stars as $k => $v) {
            $a = array();
            $r = Yii::app()->db->createCommand('SELECT starers FROM dc_image WHERE star_id=:star_id')->bindValue(':star_id', $v['star_id'])->queryColumn();
            foreach ($r as $r_v) {
                $t = explode(',', $r_v);
                $a = array_merge($a, $t);
            }
            $usr_ids = array_count_values($a);
            $usr_ids = rsort($usr_ids);
            $usr_ids = array_slice($usr_ids, 0, 6);
            $users_str = implode(',', $usr_ids);
            if ($users_Str!='') {
                $stars[$k]['user_txs'] = Yii::app()->db->createCommand('SELECT usr_id, tx FROM dc_user WHERE usr_ids IN (:users_str)')->bindValue(':users_str', $users_str)->queryAll();
            } else {
                $stars[$k]['user_txs'] = array();
            }
            $stars[$k]['images'] = Yii::app()->db->createCommand('SELECT img_id, url, stars FROM dc_image WHERE star_id=:star_id ORDER BY stars DESC LIMIT 30')->bindValue(':star_id', $v['star_id'])->queryAll();
        }
           
        $this->echoJsonData($stars);
    }

    public function actionPopularApi($star_id, $page)
    {
        $r = Yii::app()->db->createCommand('SELECT img_id, url, stars FROM dc_image WHERE star_id=:star_id ORDER BY stars DESC LIMIT :m, 30')->bindValues(array(':star_id'=>$star_id, ':m'=>$page*30))->queryAll(); 
        $this->echoJsonData($r);
    }

    public function actionNewestApi($star_id, $page=0)
    {
        $r = Yii::app()->db->createCommand('SELECT img_id, url, stars FROM dc_image WHERE star_id=:star_id ORDER BY create_time DESC LIMIT :m, 30')->bindValues(array(':star_id'=>$star_id, ':m'=>$page*30))->queryAll(); 
        $this->echoJsonData($r);
    }
    
    public function actionVoteApi($img_id)
    {
        $rewards_str = Yii::app()->db->createCommand('SELECT reward FROM dc_topic WHERE topic_id=:topic_id')->bindValue(':topic_id', $topic_id)->queryScalar();

        $r = array();
        if (isset($rewards_str) && $rewards_str!='') {
            $rewards = explode(';',$rewards_str);
            $itemList = Util::loadConfig('items');
            foreach ($rewards as $reward_str) {
                $reward = explode(':', $reward_str);
                $r[$reward[0]] = $itemList[$reward[1]];
            }
        }

        $this->echoJsonData(array($r));
    }
}
