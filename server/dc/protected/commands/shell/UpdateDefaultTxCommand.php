<?php
class UpdateDefaultTxCommand extends CConsoleCommand {
	
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
