<?php
    define('PATH', dirname(dirname(dirname(dirname(__FILE__))))); 
    define('APPFILEINFOKEY', '');       
    define('APPFILEMD5KEY', ''); 
	define('TOKEN_SECRET_KEY', '');
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

    //WECHAT_MP
    define('WECHAT_MP_ID', '');
    define('WECHAT_MP_SECRET', '');

    //WECHAT_OAUTH2
    define('WECHAT_OAUTH2_ID', '');
    define('WECHAT_OAUTH2_SECRET', '');

    //ALIPAY
    define('ALIPAY_PID', '');
    define('ALIPAY_AKEY', '');
    
