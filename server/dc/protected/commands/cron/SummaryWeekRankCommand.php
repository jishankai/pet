<?php
class SummaryWeekRankCommand extends CConsoleCommand {
	
	private function usage() {
		echo "Usage: SummaryRank start\n";
	}
	
	public function start() {
        /*
        $r = Yii::app()->db->createCommand('SELECT aid, name, type, tx, w_rq  FROM dc_animal ORDER BY w_rq DESC')->queryAll();
        $prev_rank = Yii::app()->cache->get('w_rq_rank_report');
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
        */
        //奖励计算
        /*
        $total_member = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_user')->queryScalar();
        $total_a = Yii::app()->db->createCommand('SELECT COUNT(aid), SUM(w_rq) FROM dc_animal')->queryRow();
        $all_gold = $total_member*RANK_REWARD_E;
        $total_popularity = $total_a['SUM(w_rq)'];
        $rewarw_count = round($total_a['COUNT(aid)']*30/100);
        for ($i = 0; $i <$rewarw_count; $i++) {
            $aid = $r[$i]['aid'];
            $single_popularity = $r[$i]['w_rq'];
            $total_gold = $all_gold*$single_popularity/$total_popularity;
            if ($total_gold!=0) {
                $circles = Circle::model()->findAllByAttributes(array('aid'=>$aid));
                foreach ($circles as $circle) {
                    $result_money = round($total_gold*$circle->w_contri/$single_popularity);
                    $user = User::model()->findByPk($circle->usr_id);
                    $user->gold+=$result_money;
                    $user->saveAttributes(array('gold'));

                    $self_rank = $i+1;
                    $msg = "您的王国/家族在人气周排行榜中获得第".$self_rank."名，您获得".$result_money."金币奖励";
                    Talk::model()->sendMsg(NPC_SYSTEM_USRID, $user->usr_id, $msg);
                }
            }
        }
         */
        //Yii::app()->cache->set('w_rq_rank_report', $rank, 3600*24*7);               
        //Yii::app()->cache->set('w_rq_rank', $r, 3600*24*7);               

        //人气清零
        Yii::app()->db->createCommand('UPDATE dc_animal SET w_rq=0')->execute();
    }

    public function run($args) {
        if(isset($args[0]) && $args[0] == 'start'){
            $this->start();
        }else{
            return $this->usage();
        }
    }
}
