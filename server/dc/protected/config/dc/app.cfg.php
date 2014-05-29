<?php
    define('PATH', dirname(dirname(dirname(dirname(__FILE__))))); 
    define('APPFILEINFOKEY', 'appFileInfoKey_06b286e2b77448d1fc46dbffb0d0beca');       
    define('APPFILEMD5KEY', 'appFileMD5Key_030197af1c1adadebd4508e791631d4a'); 
	define('TOKEN_SECRET_KEY', 'tokenSecretKey_abc');
    define('DB_MAX_INT', 999999999);
    define('VERSION', '1.0');
	define('CONF_VERSION', '1.0');
    define('SIGKEY', 'dog&cat');
	
    //notification
    define('CHECK_SIG_FLAG', true);     //check signature or not
    define('APPLE_NOTIFICATION_SANDBOX', true);
    define('DEBUG_MODE', true);         //send notification use which signature
