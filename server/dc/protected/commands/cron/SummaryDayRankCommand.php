<?php
class SummaryDayRankCommand extends CConsoleCommand {
	
	private function usage() {
		echo "Usage: SummaryRank start\n";
	}
	
	public function start() {
        $r = Yii::app()->db->createCommand('SELECT aid, name, type, tx, d_rq  FROM dc_animal ORDER BY d_rq DESC')->queryAll();
        $prev_rank = Yii::app()->cache->get('d_rq_rank_report');
        if (isset($prev_rank)) {
            foreach ($r as $k=>$v) {
                $rank[$v['aid']] = $k;    
                if (isset($prev_rank[$v['aid']])&&$prev_rank[$v['aid']]>$k) {
                    $r[$k]['change'] = 1;
                } else if (isset($prev_rank[$v['aid']])&&$prev_rank[$v['aid']]<$k) {
                    $r[$k]['change'] = -1;
                } else {
                    $r[$k]['change'] = 0;
                }
            }
        } else {
            foreach ($r as $k=>$v) {
                $rank[$v['aid']] = $k;    
                $r[$k]['change'] = 0;
            }
        }

        //奖励计算
        $total_member = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_user')->queryScalar();
        $total_a = Yii::app()->db->createCommand('SELECT COUNT(aid), SUM(d_rq) FROM dc_animal')->queryRow();
        $all_gold = $total_member*RANK_REWARD_E;
        $total_popularity = $total_a['SUM(d_rq)'];
        $reward_count = round($total_a['COUNT(aid)']*30/100);
        for ($i = 0; $i <$reward_count; $i++) {
            $aid = $r[$i]['aid'];
            $single_popularity = $r[$i]['d_rq'];
            $total_gold = $all_gold*$single_popularity/$total_popularity;
            if ($total_gold!=0) {
                $circles = Circle::model()->findAllByAttributes(array('aid'=>$aid));
                foreach ($circles as $circle) {
                    $result_money = round($total_gold*$circle->d_contri/$single_popularity);
                    $user = User::model()->findByPk($circle->usr_id);
                    $user->gold+=$result_money;
                    $user->saveAttributes(array('gold'));

                    $self_rank = $i+1;
                    $msg = "您的王国/家族在人气日排行榜中获得第".$self_rank."名，您获得".$result_money."金币奖励";
                    Talk::model()->sendMsg(NPC_SYSTEM_USRID, $user->usr_id, $msg);
                }
            }
        }

        Yii::app()->cache->set('d_rq_rank_report', $rank, 3600*24);               
        Yii::app()->cache->set('d_rq_rank', $r, 3600*24);               
    }

    public function run($args) {
        if(isset($args[0]) && $args[0] == 'start'){
            $this->start();
        }else{
            return $this->usage();
        }
    }
}
