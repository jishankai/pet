<?php

class RankController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
        );
    }

    public function actionRqRankApi($category=0)
    {
       switch ($category) {
           case 0:
               $r = Yii::app()->db->createCommand('SELECT aid, name, type, tx, t_rq  FROM dc_animal ORDER BY t_rq DESC')->queryAll();
               $prev_rank = Yii::app()->cache->get('t_rq_rank');
               if (isset($prev_rank)) {
                   foreach ($r as $k=>$v) {
                       $rank[$v['aid']] = $k;    
                       if (isset($prev_rank[$v['aid']])&&$prev_rank[$v['aid']]>$k) {
                           $r[$k]['vary'] = 1;
                       } else if (isset($prev_rank[$v['aid']])&&$prev_rank[$v['aid']]<$k) {
                           $r[$k]['vary'] = -1;
                       } else {
                           $r[$k]['vary'] = 0;
                       }
                   }
               } else {
                   foreach ($r as $k=>$v) {
                       $rank[$v['aid']] = $k;    
                       $r[$k]['vary'] = 0;
                   }
               }
               Yii::app()->cache->set('t_rq_rank', $rank, 3600*24*365);               
               break;

           case 1:
               $r = Yii::app()->db->createCommand('SELECT aid, name, type, tx, d_rq, 0 AS vary  FROM dc_animal ORDER BY d_rq DESC')->queryAll();
               /*
               $r = Yii::app()->cache->get('d_rq_rank');
               if (!isset($r) OR $r==FALSE) {
                   $r = Yii::app()->db->createCommand('SELECT aid, name, type, tx, d_rq, 0 AS vary  FROM dc_animal ORDER BY d_rq DESC')->queryAll();
                   Yii::app()->cache->set('d_rq_rank', $r, 3600*24);               
               }
               */
               break;

           case 2:
               $r = Yii::app()->db->createCommand('SELECT aid, name, type, tx, w_rq, 0 AS vary  FROM dc_animal ORDER BY w_rq DESC')->queryAll();
               /*
               $r = Yii::app()->cache->get('w_rq_rank');
               if (!isset($r) OR $r==FALSE) {
                   $r = Yii::app()->db->createCommand('SELECT aid, name, type, tx, w_rq, 0 AS vary  FROM dc_animal ORDER BY w_rq DESC')->queryAll();
                   Yii::app()->cache->set('w_rq_rank', $r, 3600*24*7);               
               }
               */
               break;

           case 3:
               $r = Yii::app()->db->createCommand('SELECT aid, name, type, tx, m_rq, 0 AS vary  FROM dc_animal ORDER BY m_rq DESC')->queryAll();
               /*
               $r = Yii::app()->cache->get('m_rq_rank');
               if (!isset($r) OR $r==FALSE) {
                   $r = Yii::app()->db->createCommand('SELECT aid, name, type, tx, m_rq, 0 AS vary  FROM dc_animal ORDER BY m_rq DESC')->queryAll();
                   Yii::app()->cache->set('m_rq_rank', $r, 3600*24*30);               
               }
               */
               break;

           default:
               break;
       }

       $this->echoJsonData($r);
    }

    public function actionContributionRankApi($aid, $category=0)
    {
       switch ($category) {
           case 0:
               $r = Yii::app()->db->createCommand('SELECT c.usr_id AS usr_id, c.t_contri AS t_contri, u.tx AS tx, u.name AS name FROM dc_circle c LEFT JOIN dc_user u ON c.usr_id=u.usr_id WHERE c.aid=:aid ORDER BY c.t_contri DESC')->bindValue(':aid', $aid)->queryAll();
               $prev_rank = Yii::app()->cache->get($aid.'_t_rank');
               if (isset($prev_rank)) {
                   foreach ($r as $k=>$v) {
                       $rank[$v['usr_id']] = $k;    
                       if (isset($prev_rank[$v['usr_id']])&&$prev_rank[$v['usr_id']]>$k) {
                           $r[$k]['vary'] = 1;
                       } else if (isset($prev_rank[$v['usr_id']])&&$prev_rank[$v['usr_id']]<$k) {
                           $r[$k]['vary'] = -1;
                       } else {
                           $r[$k]['vary'] = 0;
                       }
                   }
               } else {
                   foreach ($r as $k=>$v) {
                       $rank[$v['usr_id']] = $k;    
                       $r[$k]['vary'] = 0;
                   }
               }
               Yii::app()->cache->set($aid.'_t_rank', $rank, 3600*24*365);               
               break;
           
           case 1:
               $r = Yii::app()->cache->get($aid.'_d_rank');
               if (!isset($r) OR $r==FALSE) {
                   $r = Yii::app()->db->createCommand('SELECT c.usr_id AS usr_id, c.d_contri AS d_contri, u.tx AS tx, u.name AS name FROM dc_circle c LEFT JOIN dc_user u ON c.usr_id=u.usr_id WHERE c.aid=:aid ORDER BY c.d_contri DESC')->bindValue(':aid', $aid)->queryAll();
                   Yii::app()->cache->set($aid.'_d_rank', $r, 3600*24);               
               }
               break;

           case 2:
               $r = Yii::app()->cache->get($aid.'_w_rank');
               if (!isset($r) OR $r==FALSE) {
                   $r = Yii::app()->db->createCommand('SELECT c.usr_id AS usr_id, c.w_contri AS w_contri, u.tx AS tx, u.name AS name FROM dc_circle c LEFT JOIN dc_user u ON c.usr_id=u.usr_id WHERE c.aid=:aid ORDER BY c.w_contri DESC')->bindValue(':aid', $aid)->queryAll();
                   Yii::app()->cache->set($aid.'_w_rank', $r, 3600*24*7);               
               }
               break;

           case 3:
               $r = Yii::app()->cache->get($aid.'_m_rank');
               if (!isset($r) OR $r==FALSE) {
                   $r = Yii::app()->db->createCommand('SELECT c.usr_id AS usr_id, c.m_contri AS m_contri, u.tx AS tx, u.name AS name FROM dc_circle c LEFT JOIN dc_user u ON c.usr_id=u.usr_id WHERE c.aid=:aid ORDER BY c.m_contri DESC')->bindValue(':aid', $aid)->queryAll();
                   Yii::app()->cache->set($aid.'_m_rank', $r, 3600*24*30);               
               }
               break;

           default:
               break;
       }

       $this->echoJsonData($r);
    }
}
