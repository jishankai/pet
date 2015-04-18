<?php

class StarController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId - listApi, popularApi, newestApi, rankApi, contriApi',
            array(
                'COutputCache + listApi',
                'duration' => 300,
                'varyByParam' => array('img_id'),
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_star",
                ),
            ),
            array(
                'COutputCache + pupularApi,newestApi,rankApi',
                'duration' => 30,
                'varyByParam' => array('star_id','page'),
            ),
        );
    }

    public function actionListApi()
    {
        $session = Yii::app()->session;
        $stars = Yii::app()->db->createCommand('SELECT star_id, name, icon, title, description, banner, url, end_time FROM dc_star ORDER BY end_time DESC')->queryAll();

        foreach ($stars as $k => $v) {
            if (empty($session['usr_id'])) {
                $votes = 3;
            } else {
                $usr_id = $session['usr_id'];
                if (!isset($session[$usr_id.'_star_'.$v['star_id']])) {
                    $session[$usr_id.'_star_'.$v['star_id']] = 3;
                }
                $votes = $session[$usr_id.'_star_'.$v['star_id']];
            }
            $stars[$k]['votes'] = $votes;
            // $a = array();
            // $r = Yii::app()->db->createCommand('SELECT starers FROM dc_image WHERE star_id=:star_id')->bindValue(':star_id', $v['star_id'])->queryColumn();
            // foreach ($r as $r_v) {
            //     $t = explode(',', $r_v);
            //     $a = array_merge($a, $t);
            // }
            // $usr_ids = array_count_values($a);
            // rsort($usr_ids);
            // $usr_ids = array_slice($usr_ids, 0, 6);
            // $users_str = implode(',', $usr_ids);
            // if ($users_str!='') {
            //     $stars[$k]['user_txs'] = Yii::app()->db->createCommand('SELECT usr_id, tx FROM dc_user WHERE usr_ids IN (:users_str)')->bindValue(':users_str', $users_str)->queryAll();
            // } else {
            //     $stars[$k]['user_txs'] = array();
            // }
            $stars[$k]['animals'] = Yii::app()->db->createCommand('SELECT a.aid, a.tx, a.name, SUM(i.stars) AS cnt FROM dc_image i LEFT JOIN dc_animal a ON a.aid=i.aid WHERE star_id=:star_id GROUP BY i.aid,a.aid,a.tx,a.name ORDER BY cnt DESC LIMIT 6')->bindValue(':star_id', $v['star_id'])->queryAll();
            $stars[$k]['images'] = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.stars, a.name FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid WHERE i.star_id=:star_id ORDER BY i.stars DESC LIMIT 30')->bindValue(':star_id', $v['star_id'])->queryAll();
        }
           
        $this->echoJsonData(array('stars'=>$stars, 'vote_price'=>30));
    }

    public function actionPopularApi($star_id, $page)
    {
        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.stars, a.name FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid WHERE i.star_id=:star_id ORDER BY i.stars DESC LIMIT :m, 30')->bindValues(array(':star_id'=>$star_id, ':m'=>$page*30))->queryAll(); 
        $this->echoJsonData($r);
    }

    public function actionNewestApi($star_id, $page=0)
    {
        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.stars, a.name FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid WHERE i.star_id=:star_id ORDER BY i.create_time DESC LIMIT :m, 30')->bindValues(array(':star_id'=>$star_id, ':m'=>$page*30))->queryAll(); 
        $this->echoJsonData($r);
    }
    
    public function actionVoteApi($img_id)
    {
        $image = Image::model()->findByPk($img_id);
        $session = Yii::app()->session;
        if (!isset($session[$this->usr_id.'_star_'.$image->star_id])) {
            $session[$this->usr_id.'_star_'.$image->star_id] = 3;
        } 
        
        $transaction = Yii::app()->db->beginTransaction();
        try {
            if ($session[$this->usr_id.'_star_'.$image->star_id]>0) {
                $session[$this->usr_id.'_star_'.$image->star_id] = $session[$this->usr_id.'_star_'.$image->star_id] - 1;
            } else {
                $user = User::model()->findByPk($this->usr_id);
                $user->gold-=100;
                $user->saveAttributes(array('gold'));
            }
            $image->stars++;
            if ($image->starers=='') {
                $image->starers = $this->usr_id;
            } else {
                $image->starers = $image->starers.','.$this->usr_id;
            }
            $image->saveAttributes(array('stars', 'starers'));
            $flag = TRUE;
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }

        $this->echoJsonData(array('isSuccess'=>$flag));
    }

    public function actionRankApi($star_id, $page=0)
    {
        $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, SUM(i.stars) AS stars FROM dc_image i LEFT JOIN dc_animal a ON a.aid=i.aid WHERE i.star_id=:star_id GROUP BY a.aid,a.name,a.tx ORDER BY stars DESC LIMIT :m,30')->bindValues(array(':star_id'=>$star_id, ':m'=>$page*30))->queryAll();

        $this->echoJsonData($r);
    }

    public function actionContriApi($aid, $star_id, $SID='')
    {
        if ($SID!='') {
            $session = Yii::app()->session;
            $this->usr_id = $session['usr_id'];
        } 

        $a = array();
        $r = Yii::app()->db->createCommand("SELECT starers FROM dc_image WHERE star_id=:star_id AND aid=:aid AND starers!=''")->bindValues(array(':star_id'=>$star_id, ':aid'=>$aid))->queryColumn();
        foreach ($r as $r_v) {
            $t = explode(',', $r_v);
            $a = array_merge($a, $t);
        }
        $usr_ids = array_count_values($a);
        $total_votes = array_sum($usr_ids);
        arsort($usr_ids);
        if (isset($this->usr_id)) {
            $my_votes = isset($usr_ids[$this->usr_id])?$usr_ids[$this->usr_id]:0;
        } else {
            $my_votes = 0;
        }
       
        $i = 0;
        $rank_ids = array();
        foreach ($usr_ids as $k => $v) {
            $rank_ids[$k] = $v;
            if (++$i>=3) {
                break;
            }
        }
        $users_str = implode(',', array_keys($rank_ids));
        $user_txs = array();
        if ($users_str!='') {
            $tx_r = Yii::app()->db->createCommand('SELECT usr_id, tx FROM dc_user WHERE usr_id IN ('.$users_str.') ORDER BY FIELD(usr_id, '.$users_str.')')->queryAll();
            foreach ($tx_r as $tx_v) {
                $user_txs[$tx_v['usr_id']] = $tx_v['tx'];
            }
        } 
        $j = 0;
        $rank = array();
        foreach ($rank_ids as $k => $v) {
            $rank[$j][$k]['tx'] = $user_txs[$k];
            $rank[$j++][$k]['votes'] = $v;
        }
        $info = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.stars, a.gender FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid WHERE i.star_id=:star_id AND i.aid=:aid')->bindValues(array(':star_id'=>$star_id, ':aid'=>$aid))->queryRow();
        $this->echoJsonData(array(
            'total_votes' => $total_votes,
            'my_votes' => $my_votes,
            'rank' => $rank,
            'info' => $info,
        ));
    }

    // public function actionChargeApi($star_id)
    // {
    //     $transaction = Yii::app()->db->beginTransaction();
    //     try {
    //         $session = Yii::app()->session;
    //         $session[$this->usr_id.'_star_'.$star_id] = 3;
    //         $user = User::model()->findByPk($this->usr_id);
    //         $user->gold-=100;
    //         $user->saveAttributes(array('gold'));
    //         $transaction->commit();
    //     } catch (Exception $e) {
    //         $transaction->rollback();
    //         throw $e;
    //     }

    //     $this->echoJsonData(array('isSuccess'=>TRUE));
    // }
}
