<?php 

$curPath = dirname(__FILE__);
$commandPath = $curPath.DIRECTORY_SEPARATOR.'cron';

$yii = $curPath.DIRECTORY_SEPARATOR.'..'.DIRECTORY_SEPARATOR.'..'.DIRECTORY_SEPARATOR.'..'.DIRECTORY_SEPARATOR.'yii'.DIRECTORY_SEPARATOR.'framework'.DIRECTORY_SEPARATOR.'yii.php';

$config = $curPath.DIRECTORY_SEPARATOR.'..'.DIRECTORY_SEPARATOR.'..'.DIRECTORY_SEPARATOR.'protected'.DIRECTORY_SEPARATOR.'config'.DIRECTORY_SEPARATOR.'main.php';

require_once($yii);

$console = Yii::createConsoleApplication($config);

//set command path to commands/cron
$console->setCommandPath($commandPath);
$console->getCommandRunner()->addCommands($console->getCommandPath());

$console->run();

?>
