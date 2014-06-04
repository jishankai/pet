<?php
require_once(dirname(__FILE__).'/dc/app.cfg.php');
require_once(dirname(__FILE__).'/dc/const.cfg.php');

// uncomment the following to define a path alias
// Yii::setPathOfAlias('local','path/to/local-folder');

// This is the main Web application configuration. Any writable
// CWebApplication properties can be configured here.
return array(
	'basePath'=>dirname(__FILE__).DIRECTORY_SEPARATOR.'..',
	'name'=>'阿猫阿狗',

	// preloading 'log' component
	'preload'=>array('log'),

	// autoloading model and component classes
	'import'=>array(
		'application.models.*',
		'application.components.*',
	),

	'modules'=>array(
        // uncomment the following to enable the Gii tool         
        'admin',
        'gii'=>array(
			'class'=>'system.gii.GiiModule',
			'password'=>'gii',
			// If removed, Gii defaults to localhost only. Edit carefully to taste.
			'ipFilters'=>array('127.0.0.1','::1'),
		),
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
		// uncomment the following to enable URLs in path-format
        /*
        'urlManager'=>array(
			'urlFormat'=>'path',
			'rules'=>array(
				'<controller:\w+>/<id:\d+>'=>'<controller>/view',
				'<controller:\w+>/<action:\w+>/<id:\d+>'=>'<controller>/<action>',
				'<controller:\w+>/<action:\w+>'=>'<controller>/<action>',
			),
        ),
         */

        'cache' => array(
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
            'keyPrefix' => 'PetSession',
            'servers' => array(
                array(
                    'host' => '127.0.0.1',
                    'port' => 11211,
                    'weight' => 100,
                ),
            ),
        ),
        'session' => array(
            'class' => 'CCacheHttpSession',
            'cacheID' => 'cache',
            'sessionName' => 'SID',
            'timeout' => 86400,
            'cookieMode' => 'only',
        ),
        
		'db'=>array(
			'connectionString' => 'mysql:host=localhost;dbname=pet',
			'emulatePrepare' => true,
			'username' => 'root',
			'password' => '',
			'charset' => 'utf8',
            'class' => 'CDbConnection',
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
				// uncomment the following to show log messages on web pages
				array(
					'class'=>'CWebLogRoute',
                    'enabled'=>true,
                    'categories'=>'system.db.*',
				),
			),
		),
        'image'=>array(
            'class'=>'ext.Image.CImageComponent',
            'driver'=>'GD',
            'params'=>array('directory'=>'/usr/bin'),
        ),
	),

	// application-level parameters that can be accessed
	// using Yii::app()->params['paramName']
	'params'=>array(
		// this is used in contact page
		'adminEmail'=>'webmaster@pet.server.com',
	),
);
