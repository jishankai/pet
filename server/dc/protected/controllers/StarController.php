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
        $image = Image::model()->findByPk($img_id);
        $session = Yii::app()->session;
        if (!isset($session['star_'.$image->star_id.'_'.$usr_id])) {
            $session[$this->usr_id.'_star_'.$image->star_id] = 3;
        } 
        if ($session[$usr_id.'_star_'.$image->star_id]>0) {
            $flag = TRUE;
            $transaction = Yii::app()->db->beginTransaction();
            try {
                $session[$usr_id.'_star_'.$image->star_id]--;
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
            $session[$this->usr_id.'_star_'.$image->star_id] = 3;
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
