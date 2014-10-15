<?php
class SummaryDayContributionRankCommand extends CConsoleCommand {
	
	private function usage() {
		echo "Usage: SummaryRank start\n";
    }

    public function start() {
        $aids = Yii::app()->db->createCommand('SELECT aid FROM dc_animal')->queryColumn();
        foreach ($aids as $aid) {
            $r = Yii::app()->db->createCommand('SELECT c.usr_id AS usr_id, c.d_contri AS d_contri, u.tx AS tx, u.name AS name FROM dc_circle c LEFT JOIN dc_user u ON c.usr_id=u.usr_id WHERE c.aid=:aid ORDER BY c.d_contri DESC')->bindValue(':aid', $aid)->queryAll();
            $prev_rank = Yii::app()->cache->get($aid.'_d_rank');
            if (isset($prev_rank)) {
                foreach ($r as $k=>$v) {
                    $rank[$v['usr_id']] = $k;    
                    if (isset($prev_rank[$v['usr_id']])&&$prev_rank[$v['usr_id']]>$k) {
                        $r[$k]['change'] = 1;
                    } else if (isset($prev_rank[$v['usr_id']])&&$prev_rank[$v['usr_id']]<$k) {
                        $r[$k]['change'] = -1;
                    } else {
                        $r[$k]['change'] = 0;
                    }
                }
            } else {
                foreach ($r as $k=>$v) {
                    $rank[$v['usr_id']] = $k;    
                    $r[$k]['change'] = 0;
                }
            }
            Yii::app()->cache->set($aid.'_d_rank_report', $rank, 3600*24);               
            Yii::app()->cache->set($aid.'_d_rank', $r, 3600*24);               
        }
    }

    public function run($args) {
        if(isset($args[0]) && $args[0] == 'start'){
            $this->start();
        }else{
            return $this->usage();
        }
    }
}
