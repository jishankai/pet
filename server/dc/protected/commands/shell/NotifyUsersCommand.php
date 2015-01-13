<?php
class NotifyUsersCommand extends CConsoleCommand {
	
	private function usage() {
		echo "Usage: NotifyUsers start\n";
	}
	
    public function start() {
        $users = User::model()->findAll();
        foreach ($users as $user) {
            Talk::model()->sendMsg(NPC_SYSTEM_USRID, $user->usr_id, "完挣口粮照片，可以再点一下“挣口粮”，就能发到微信微博，让小伙伴帮着赏粮呢，只需要轻轻一点哟~");
            sleep(1);
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
