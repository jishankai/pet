<?php
require_once(dirname(__FILE__).'/dc/app.cfg.php');
require_once(dirname(__FILE__).'/dc/const.cfg.php');

// uncomment the following to define a path alias
// Yii::setPathOfAlias('local','path/to/local-folder');

// This is the main Web application configuration. Any writable
// CWebApplication properties can be configured here.
return array(
	'basePath'=>dirname(__FILE__).DIRECTORY_SEPARATOR.'..',
	'name'=>'宠物星球',
    'theme'=>'blackboot',
    'defaultController'=>'site/login',

	// preloading 'log' component
	'preload'=>array('log'),

	// autoloading model and component classes
	'import'=>array(
		'application.models.*',
		'application.components.*',
        'application.extensions.*',
        'application.extensions.alipay.*',
	),

	'modules'=>array(
        // uncomment the following to enable the Gii tool
        // 'admin',
        /*
        'gii'=>array(
			'class'=>'system.gii.GiiModule',
			'password'=>false,
			// If removed, Gii defaults to localhost only. Edit carefully to taste.
			//'ipFilters'=>array('0.0.0.0','::1'),
            'ipFilters'=>false,
            'generatorPaths'=>array('application.gii'),
		),
        */
	),

    // add behaviors to application.
    'behaviors' => array(
        'api' => array(
            'class' => 'WebApplicationApiBehavior',
            'responseClass' => 'ApiJsonResponse',
        ),
    ),

	// application components
    'components'=>array(
        'user'=>array(
            // enable cookie-based authentication
            'allowAutoLogin'=>true,
        ),
        /*
        'mobiledetect'=>array(
            'class' => 'ext.MobileDetect.MobileDetect'
        ),
         */
        'image'=>array(
            'class'=>'application.extensions.image.CImageComponent',
            // GD or ImageMagick
            'driver'=>'GD',
            // ImageMagick setup path
            'params'=>array('directory'=>'/usr/bin'),
        ),

        's3'=>array(
            'class'=>'ext.s3.ES3',
            'aKey'=>AWS_ACCESS_KEY,
            'sKey'=>AWS_SECRET_KEY,
        ),

        'oss'=>array(
            'class'=>'ext.aliyun.OSS',
            'aKey'=>OSS_ACCESS_KEY,
            'sKey'=>OSS_SECRET_KEY,
            'endpoint'=>OSS_ENDPOINT,
        ),

        'easemob'=>array(
            'class'=>'ext.easemob.Easemob',
            'client_id' => EASEMOB_ID,
            'client_secret' => EASEMOB_SECRET,
            'org_name' => EASEMOB_ORG,
            'app_name' => EASEMOB_APP
        ),

        'file'=>array(
            'class'=>'application.extensions.file.CFile',
        ),
        // uncomment the following to enable URLs in path-format
        'urlManager'=>array(
            /*
            'showScriptName'=>FALSE,
            'urlFormat'=>'path',
            'rules'=>array(
                //'<controller:\w+>/<id:\d+>'=>'<controller>/view',
                //'<controller:\w+>/<action:\w+>/<id:\d+>'=>'<controller>/<action>',
                //'<controller:\w+>/<action:\w+>'=>'<controller>/<action>',
                'socialCallback'=>'social/oAuth2CallbackApi'
            ),
             */
        ),
        'wechat'=>array(
            'class'=>'application.extensions.wechat.oauth2',
            'APPID'=>WECHAT_OAUTH2_ID,
            'SECRET'=>WECHAT_OAUTH2_SECRET,
            'REDIRECT_URL'=>'http://release4pet.aidigame.com/wechat/callback',
        ),
        'cache' => array(
            /*
            'class' => 'CFileCache',
            'directoryLevel' => 4,
             */
            'class' => 'CMemCache',
            'servers' => array(
                array(
                    'host' => '127.0.0.1',
                    'port' => 11211,
                    'weight' => 100,
                ),
            ),
        ),
        'sessionCache' => array(
            'class' => 'CMemCache',
            'keyPrefix' => 'PetTestSession',
            'servers' => array(
                array(
                    'host' => '127.0.0.1',
                    'port' => 11211,
                    'weight' => 100,
                ),
            /*
            array(
                'host' => 'cache4test.5edo1u.cfg.apne1.cache.amazonaws.com',
                'port' => 11211,
                'weight' => 100,
            )
             */
            ),
        ),
        'session' => array(
            'class' => 'CCacheHttpSession',
            'cacheID' => 'sessionCache',
            'sessionName' => 'SID',
            'timeout' => mktime(0,0,0,date('m'),date('d')+1,date('Y'))-time(),
            'cookieMode' => 'none',
        ),

        'db'=>array(
            'connectionString' => 'mysql:host='.MYSQL_MASTER_SERVER.';dbname='.MYSQL_DB_NAME,
            'emulatePrepare' => true,
            'username' => MYSQL_MASTER_USER,
            'password' => MYSQL_MASTER_PASSWORD,
            'charset' => 'utf8mb4',
            'class' => 'DbConnectionMan',
            'schemaCachingDuration' => 3600,
            'enableProfiling'=>true,
            'enableParamLogging'=>true,
            'enableSlave'=>true,
            'slaves'=>array(
                array(
                    'connectionString' => 'mysql:host='.MYSQL_SLAVE_SERVER.';dbname='.MYSQL_DB_NAME,
                    'username' => MYSQL_SLAVE_USER,
                    'password' => MYSQL_SLAVE_PASSWORD,
                    'charset' => 'utf8mb4',
                ),
                /*
                array(
                    'connectionString' => 'mysql:host=127.0.0.1;port=3306;dbname=pet',
                    'username' => 'root',
                    'password' => '',
                    'charset' => 'utf8mb4',
                ),
                 */
            ),
        ),
        'errorHandler'=>array(
            // use 'site/error' action to display errors
            'errorAction'=>'site/error',
        ),
        'log'=>array(
            'class'=>'CLogRouter',
            'routes'=>array(
                array(
                    'class'=>'CFileLogRoute',
                    'levels'=>'error, warning',
                ),
                /*
                array(
                    'class'=>'CFileLogRoute',
                    'levels'=>'trace',
                    'logFile'=>'json.log',
                    'categories'=>'json',
                ),
                array(
                    'class'=>'CFileLogRoute',
                    'levels'=>'trace',
                    'logFile'=>'access.log',
                    'categories'=>'access',
                ),
                 */
                /*
                array(
                    'class'=>'ext.yii-debug-toolbar.YiiDebugToolbarRoute',
                    'ipFilters'=>false,
                    // Access is restricted by default to the localhost
                    //'ipFilters'=>array('127.0.0.1','192.168.1.*', 88.23.23.0/24),
                )
                // uncomment the following to show log messages on web pages
                 */
                /*
                array(
                    'class'=>'CWebLogRoute',
                    'enabled'=>true,
                    'categories'=>'system.db.*',
                ),
                 */
            ),
        ),
        'curl'=> array(
            'class' => 'ext.curl.Curl',
            'options' => array(/* additional curl options */),
        ),
    ),

    // application-level parameters that can be accessed
    // using Yii::app()->params['paramName']
    'params'=>array(
        // this is used in contact page
        'adminEmail'=>'sunyi@aidigame.com',
    ),
);
