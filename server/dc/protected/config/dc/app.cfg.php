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

    //AWS
    define('AWS_ACCESS_KEY', '');
    define('AWS_SECRET_KEY', '');
    define('AWS_S3_BUCKET', 'jishankai4test');

    //OSS
    define('OSS_ACCESS_KEY', '');
    define('OSS_SECRET_KEY', '');
    define('OSS_ENDPOINT', '');
    define('OSS_PREFIX', '');

    //MYSQL
    define('MYSQL_MASTER_SERVER', '');
    define('MYSQL_SLAVE_SERVER', '');
    define('MYSQL_MASTER_USER', '');
    define('MYSQL_SLAVE_USER', '');
    define('MYSQL_MASTER_PASSWORD', '');
    define('MYSQL_SLAVE_PASSWORD', '');
    define('MYSQL_DB_NAME', '');
    
