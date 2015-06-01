<?php
class ExecuteSQLCommand extends CConsoleCommand {
	
	private function usage() {
		echo "Usage: ExecuteSQL start\n";
	}
	
    public function start() {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $user = User::model()->findAll(array('tx'=>''));
            foreach ($user as $u) {
                $u->tx = rand(1,15).'.jpg';
                $u->saveAttributes(array('tx'));
            }
            Yii::app()->db->createCommand('update dc_animal set food=food+100 WHERE aid IN (1650,1653,1655,1652,1656,1651)')->execute();
            Yii::app()->db->createCommand("update dc_user set password='123456' where name='沐瑶瑶'")->execute();
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
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
