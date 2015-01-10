<?php
    define('PATH', dirname(dirname(dirname(dirname(__FILE__))))); 
    define('APPFILEINFOKEY', 'appFileInfoKey_06b286e2b77448d1fc46dbffb0d0beca');       
    define('APPFILEMD5KEY', 'appFileMD5Key_030197af1c1adadebd4508e791631d4a'); 
	define('TOKEN_SECRET_KEY', 'tokenSecretKey_abc');
    define('DB_MAX_INT', 999999999);
    define('VERSION', '1.0.0');
    define('LOGIN_VERSION', '1.0.0');
	define('CONF_VERSION', '1.1');
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
    define('OSS_ACCESS_KEY', 'k81GLIYHXaGKTuFD');
    define('OSS_SECRET_KEY', 'XO1qXBf1kIwxgoeyxoAkyAshxcNiJo');
    define('OSS_ENDPOINT', 'http://oss-cn-beijing-internal.aliyuncs.com');
    define('OSS_PREFIX', 'pet');

    //MYSQL
    define('MYSQL_MASTER_SERVER', '10.171.90.112');
    define('MYSQL_SLAVE_SERVER', '10.173.0.203');
    define('MYSQL_MASTER_USER', 'jishankai');
    define('MYSQL_SLAVE_USER', 'jishankai');
    define('MYSQL_MASTER_PASSWORD', 'jishankai');
    define('MYSQL_SLAVE_PASSWORD', 'jishankai');
    define('MYSQL_DB_NAME', 'pet');
    
