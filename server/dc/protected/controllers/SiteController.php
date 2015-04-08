<?php

class SiteController extends Controller
{
	/**
	 * Declares class-based actions.
	 */
	public function actions()
	{
		return array(
			// captcha action renders the CAPTCHA image displayed on the contact page
			'captcha'=>array(
				'class'=>'CCaptchaAction',
				'backColor'=>0xFFFFFF,
			),
			// page action renders "static" pages stored under 'protected/views/site/pages'
			// They can be accessed via: index.php?r=site/page&view=FileName
			'page'=>array(
				'class'=>'CViewAction',
			),
		);
	}

	/**
	 * This is the default 'index' action that is invoked
	 * when an action is not explicitly requested by users.
	 */
	public function actionIndex()
	{
		// renders the view file 'protected/views/site/index.php'
		// using the default layout 'protected/views/layouts/main.php'
		$this->render('index');
	}

	/**
	 * This is the action to handle external exceptions.
	 */
	public function actionError()
	{
		if($error=Yii::app()->errorHandler->error)
		{
			if(Yii::app()->request->isAjaxRequest)
				echo $error['message'];
			else
				$this->render('error', $error);
		}
	}

	/**
	 * Displays the contact page
	 */
	public function actionContact()
	{
		$model=new ContactForm;
		if(isset($_POST['ContactForm']))
		{
			$model->attributes=$_POST['ContactForm'];
			if($model->validate())
			{
				$name='=?UTF-8?B?'.base64_encode($model->name).'?=';
				$subject='=?UTF-8?B?'.base64_encode($model->subject).'?=';
				$headers="From: $name <{$model->email}>\r\n".
					"Reply-To: {$model->email}\r\n".
					"MIME-Version: 1.0\r\n".
					"Content-Type: text/plain; charset=UTF-8";

				mail(Yii::app()->params['adminEmail'],$subject,$model->body,$headers);
				Yii::app()->user->setFlash('contact','Thank you for contacting us. We will respond to you as soon as possible.');
				$this->refresh();
			}
		}
		$this->render('contact',array('model'=>$model));
	}

	/**
	 * Displays the login page
	 */
	public function actionLogin()
	{
		$model=new LoginForm;

		// if it is ajax validation request
		if(isset($_POST['ajax']) && $_POST['ajax']==='login-form')
		{
			echo CActiveForm::validate($model);
			Yii::app()->end();
		}

		// collect user input data
		if(isset($_POST['LoginForm']))
		{
			$model->attributes=$_POST['LoginForm'];
			// validate user input and redirect to the previous page if valid
			if($model->validate() && $model->login())
                Yii::app()->user->setReturnUrl(array('site/index'));
				$this->redirect(Yii::app()->user->returnUrl);
		}
		// display the login form
		$this->render('login',array('model'=>$model));
	}

	/**
	 * Logs out the current user and redirect to homepage.
	 */
	public function actionLogout()
	{
		Yii::app()->user->logout();
		$this->redirect(Yii::app()->homeUrl);
	}

    public function actionApi()
    {
        $this->render('api');
    }

    public function actionClear()
    {
        /*
        Yii::app()->db->createCommand('DELETE FROM dc_device WHERE 1')->execute();
        Yii::app()->db->createCommand('DELETE FROM dc_user WHERE 1')->execute();
         */
    }

    public function actionClearUser($name)
    {
        $usr_id = Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE name=:name')->bindValue(':name', $name)->queryScalar();
        Yii::app()->db->createCommand('UPDATE dc_device SET usr_id=NULL WHERE usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->execute();
        echo '成功啦！';
    }

    public function actionReplaceUser($from, $to)
    {
        $from_id = Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE name=:name')->bindValue(':name', $from)->queryScalar();
        $to_id = Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE name=:name')->bindValue(':name', $to)->queryScalar();
        if (isset($to_id)) {
            Yii::app()->db->createCommand('UPDATE dc_device SET usr_id=:to WHERE usr_id=:from')->bindValues(array(':from'=>$from_id, ':to'=>$to_id))->execute();
            echo '成功啦！';
        } else {
            echo '失败了...';
        }
    }
    public function actionRepair()
    {
        $images = Image::model()->findAll();
        foreach ($images as $image) {
            $c = new Comment;
            $c->img_id = $image->img_id;
            $c->create_time = time();

            $c->save();
        }
    }

    public function actionEcho()
    {
    }

    public function actionSetGold($gold, $u_name)
    {
        Yii::app()->db->createCommand('UPDATE dc_user SET gold=:gold WHERE name=:name')->bindValues(array(
            ':gold'=>$gold,
            ':name'=>$u_name,
        ))->execute();

        echo "成功获得金币";
    }

    public function actionWelcome()
    {
        if(isset($_FILES['image']))
        {   
            $fname = basename($_FILES['image']['name']);
            $rtn = Yii::app()->oss->upload_file(OSS_PREFIX.'4welcome', $fname, fopen($_FILES['image']['tmp_name'],'r'), $_FILES['image']['size']); 
            $this->redirect('http://'.OSS_PREFIX.'4welcome.oss-cn-beijing.aliyuncs.com/'.$fname);
        }           

        $this->render('welcome'); 

    }

    public function actionFaq()
    {
        $this->renderPartial('faq');
    } 

    public function actionAgreement()
    {
        $this->renderPartial('agreement');
    }    

    public function actionRechargeUrl()
    {
        $this->echoJsonData(array('recharge_url'=>'http://suo.im/bbon7'));
    }
}
