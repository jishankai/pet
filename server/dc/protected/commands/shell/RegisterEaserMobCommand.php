<?php
class RegisterEaserMobCommand extends CConsoleCommand {
	
	private function usage() {
		echo "Usage: RegisterEaserMob start\n";
	}
	
    public function start() {
        $url = "https://a1.easemob.com/aidigame/imengstar/users";
        $data = Yii::app()->db->createCommand('SELECT usr_id AS username, code AS password FROM dc_user')->queryAll();
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type:application/json','Authorization:Bearer YWMtTp7twos3EeSYHsOnGR7qCAAAAUuwAR-1ErFSM6i9m7OaVhO-0RA5zpislhc'));
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
        $output = curl_exec($ch);
        curl_close($ch);
        print_r($output);

    }

    public function run($args) {
        if(isset($args[0]) && $args[0] == 'start'){
            $this->start();
        }else{
            return $this->usage();
        }
    }
}
