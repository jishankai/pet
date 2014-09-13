<?php
class SummaryRankCommand extends CConsoleCommand {
	
	private function usage() {
		echo "Usage: SummaryRank start\n";
	}
	
	public function start($args) {
        
	}
	
	public function run($args) {
        if(isset($args[0]) && $args[0] == 'start'){
            $this->start($args);
        }else{
            return $this->usage();
        }
    }
}
