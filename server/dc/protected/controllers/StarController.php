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
        $session = Yii::app()->session;
        $stars = Yii::app()->db->createCommand('SELECT star_id, name, icon, title, description, banner, url FROM dc_star where start_time<=:time AND end_time>:time')->bindValue(':time', time())->queryAll();

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
            $stars[$k]['animals'] = Yii::app()->db->createCommand('SELECT a.aid, a.tx, COUNT(i.stars) AS cnt FROM dc_image i LEFT JOIN dc_animal a ON a.aid=i.aid WHERE star_id=:star_id GROUP BY i.aid,a.aid,a.tx ORDER BY cnt DESC LIMIT 6')->bindValue(':star_id', $v['star_id'])->queryAll();
            $stars[$k]['images'] = Yii::app()->db->createCommand('SELECT img_id, url, stars FROM dc_image WHERE star_id=:star_id ORDER BY stars DESC LIMIT 30')->bindValue(':star_id', $v['star_id'])->queryAll();
        }
           
        $this->echoJsonData(array('stars'=>$stars, 'vote_info'=>array('gold'=>100, 'times'=>3)));
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
        $image = Image::model()->findByPk($img_id);
        $session = Yii::app()->session;
        if (!isset($session[$this->usr_id.'_star_'.$image->star_id])) {
            $session[$this->usr_id.'_star_'.$image->star_id] = 3;
        } 
        if ($session[$this->usr_id.'_star_'.$image->star_id]>0) {
            $flag = TRUE;
            $transaction = Yii::app()->db->beginTransaction();
            try {
                $session[$this->usr_id.'_star_'.$image->star_id] = $session[$this->usr_id.'_star_'.$image->star_id] - 1;
                $image->stars++;
                $image->starers = $image->starers.','.$this->usr_id;
                $image->saveAttributes(array('stars', 'starers'));
                $transaction->commit();
            } catch (Exception $e) {
                $transaction->rollback();
                throw $e;
            }
        } else {
            $flag = FALSE;
        }

        $this->echoJsonData(array('isSuccess'=>$flag));
    }

    public function actionChargeApi($star_id)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $session = Yii::app()->session;
            $session[$this->usr_id.'_star_'.$star_id] = 3;
            $user = User::model()->findByPk($this->usr_id);
            $user->gold-=100;
            $user->saveAttributes(array('gold'));
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }

        $this->echoJsonData(array('isSuccess'=>TRUE));
    }
}
