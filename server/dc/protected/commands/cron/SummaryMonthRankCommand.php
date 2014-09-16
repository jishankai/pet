<?php
class SummaryMonthRankCommand extends CConsoleCommand {
	
	private function usage() {
		echo "Usage: SummaryRank start\n";
	}
	
    public function start($args) {
        $r = Yii::app()->db->createCommand('SELECT aid, name, type, tx, m_rq  FROM dc_animal ORDER BY m_rq DESC')->queryAll();
        $prev_rank = Yii::app()->cache->get('m_rq_rank_report');
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
        Yii::app()->cache->set('m_rq_rank_report', $rank, 3600*24*30);               
        Yii::app()->cache->set('m_rq_rank', $r, 3600*24*30);               
    }

    public function run($args) {
        if(isset($args[0]) && $args[0] == 'start'){
            $this->start($args);
        }else{
            return $this->usage();
        }
    }
}
