<?php
class NotifyEaserMobUsersCommand extends CConsoleCommand {
	
	private function usage() {
		echo "Usage: NotifyEaserMobUsers start\n";
	}
	
    public function start() {
        $easemob = Yii::app()->easemob;
        $n = 20;
        for ($i=0; $n >= 20; $i++) { 
            $usr_ids = Yii::app()->db->createCommand('SELECT usr_id FROM dc_user ORDER BY usr_id DESC LIMIT :i,20')->bindValue(':i', 20*$i)->queryColumn();
            $n = count($usr_ids);
            $easemob->sendToUsers($usr_ids, NPC_SYSTEM_USRID, array(
                'mixed'=>TRUE,
                'msg'=>"hi地球银，\n
新年过得好么？~新年活动已经进入最后冲刺阶段啦！\n第二周战况在此：http://t.cn/RwWYXF5 你有木有上榜呢？还没参加的小伙伴快戳：http://t.cn/Rw0KSlO 有独家定制惊喜大奖等你拿哦~~",
                'ext'=>array(
                    'nickname'=>'事务官',
                ),
            ));
            sleep(5);
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
