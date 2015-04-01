<?php

class ToolController extends Controller
{
	/**
	 * @var string the default layout for the views. Defaults to '//layouts/column2', meaning
	 * using two-column layout. See 'protected/views/layouts/column2.php'.
	 */
	public $layout='//layouts/column2';

	/**
	 * @return array action filters
	 */
	public function filters()
	{
		return array(
			'accessControl', // perform access control for CRUD operations
			'postOnly + delete', // we only allow deletion via POST request
		);
	}

	/**
	 * Specifies the access control rules.
	 * This method is used by the 'accessControl' filter.
	 * @return array access control rules
	 */
	public function accessRules()
	{
		return array(
			array('allow',  // allow all users to perform 'index' and 'view' actions
				'actions'=>array('index','view'),
				'users'=>array('*'),
			),
			array('allow', // allow authenticated user to perform 'create' and 'update' actions
				'actions'=>array('update'),
				'users'=>array('@'),
			),
			array('allow', // allow admin user to perform 'admin' and 'delete' actions
				'actions'=>array('admin','replace','clear','gold'),
				'users'=>array('admin'),
			),
			array('deny',  // deny all users
				'users'=>array('*'),
			),
		);
	}

    public function actionReplace()
    {
        $error=FALSE;
        $result=FALSE;
        if (isset($_POST['from_name'])&&$_POST['from_name']!=''&&isset($_POST['to_name'])&&$_POST['to_name']!='') {
            $from_name = $_POST['from_name'];
            $to_name   = $_POST['to_name'];
            $from_id = Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE name=:name')->bindValue(':name', $from_name)->queryScalar();
            $to_id   = Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE name=:name')->bindValue(':name', $to_name)->queryScalar();
            if (!$from_id) {
                $error='用户名'.$from_name.'不存在';
            } else if (!$to_id) {
                $error='用户名'.$to_name.'不存在';
            } else {
                $device_id = Yii::app()->db->createCommand('SELECT id FROM dc_device WHERE usr_id=:usr_id')->bindValue(':usr_id', $from_id)->queryScalar();
                if ($device_id) {
                    Yii::app()->db->createCommand('UPDATE dc_device SET usr_id=:usr_id WHERE id=:id')->bindValues(array(':usr_id'=>$to_id, ':id'=>$device_id))->execute();
                    $result = TRUE;
                } else {
                    $error='没有设备绑定'.$from_name.'用户';
                }
            }
        }
        $this->render('replace',array('error'=>$error,'result'=>$result)); 
    }

    public function actionClear()
    {
        $error  = FALSE;
        $result = FALSE;
        if (isset($_POST['u_name'])&&$_POST['u_name']!='') {
            $u_name = $_POST['u_name'];
            $usr_id = Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE name=:name')->bindValue(':name', $u_name)->queryScalar();
            if (!$usr_id) {
                $error='用户名'.$u_name.'不存在';
            } else {
                $device_id = Yii::app()->db->createCommand('SELECT id FROM dc_device WHERE usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryScalar();
                if ($device_id) {
                    $transaction = Yii::app()->db->beginTransaction();
                    try {
                        Yii::app()->db->createCommand('UPDATE dc_device SET usr_id=:usr_id WHERE id=:id')->bindValues(array(':usr_id'=>0, ':id'=>$device_id))->execute();
                        //Yii::app()->db->createCommand('DELETE FROM dc_user WHERE usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->execute();
                        //Yii::app()->db->createCommand('DELETE FROM dc_image WHERE usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->execute();
                        $result = TRUE;
                        $transaction->commit();
                    } catch (Exception $e) {
                        $transaction->rollback();
                        throw $e;
                    }
                } else {
                    $error='没有设备绑定'.$u_name.'用户';
                }
            }
        }
        $this->render('clear',array('error'=>$error,'result'=>$result)); 
    }

	/**
	 * Displays a particular model.
	 * @param integer $id the ID of the model to be displayed
	 */
	public function actionView($id)
	{
		$this->render('view',array(
			'model'=>$this->loadModel($id),
		));
	}

	/**
	 * Creates a new model.
	 * If creation is successful, the browser will be redirected to the 'view' page.
	 */
	public function actionCreate()
	{
		$model=new User;

		// Uncomment the following line if AJAX validation is needed
		// $this->performAjaxValidation($model);

		if(isset($_POST['User']))
		{
			$model->attributes=$_POST['User'];
			if($model->save())
                $file = CUploadedFile::getInstance($model,'tx');
                if (isset($file)) {
                    $path = Yii::app()->basePath.'/../images/tx/'.$model->usr_id.'.'.$file->getExtensionName();           
                    $file->saveAs($path);    
                    $model->tx = $model->usr_id.'.'.$file->getExtensionName(); 
                    $model->saveAttributes(array('tx'));
                }

				$this->redirect(array('view','id'=>$model->usr_id));
		}

		$this->render('create',array(
			'model'=>$model,
		));
	}

	/**
	 * Updates a particular model.
	 * If update is successful, the browser will be redirected to the 'view' page.
	 * @param integer $id the ID of the model to be updated
	 */
	public function actionUpdate($id)
	{
		$model=$this->loadModel($id);

		// Uncomment the following line if AJAX validation is needed
		// $this->performAjaxValidation($model);

		if(isset($_POST['User']))
		{
			$model->attributes=$_POST['User'];
			if($model->save())
                $file = CUploadedFile::getInstance($model,'tx');
                if (isset($file)) {
                    $path = Yii::app()->basePath.'/../images/tx/'.$model->usr_id.'.'.$file->getExtensionName();           
                    $file->saveAs($path);    
                    $model->tx = $model->usr_id.'.'.$file->getExtensionName(); 
                    $model->saveAttributes(array('tx'));
                }

				$this->redirect(array('view','id'=>$model->usr_id));
		}

		$this->render('update',array(
			'model'=>$model,
		));
	}

	/**
	 * Deletes a particular model.
	 * If deletion is successful, the browser will be redirected to the 'admin' page.
	 * @param integer $id the ID of the model to be deleted
	 */
	public function actionDelete($id)
	{
		$this->loadModel($id)->delete();

		// if AJAX request (triggered by deletion via admin grid view), we should not redirect the browser
		if(!isset($_GET['ajax']))
			$this->redirect(isset($_POST['returnUrl']) ? $_POST['returnUrl'] : array('admin'));
	}

	/**
	 * Lists all models.
	 */
	public function actionIndex()
	{
		$this->render('index');
	}

	/**
	 * Manages all models.
	 */
	public function actionAdmin()
	{
		$model=new User('search');
		$model->unsetAttributes();  // clear any default values
		if(isset($_GET['User']))
			$model->attributes=$_GET['User'];

		$this->render('admin',array(
			'model'=>$model,
		));
	}

    public function actionGold()
    {
        $error=FALSE;
        $result=FALSE;
        if (isset($_POST['code'])&&$_POST['code']!=''&&isset($_POST['gold'])&&$_POST['gold']!=0) {
            $code = $_POST['code'];
            $gold = $_POST['gold'];
            $user = User::model()->findByAttributes(array('code'=>$code));
            if (isset($user)) {
                $user->gold+=$gold;
                $user->saveAttributes(array('gold'));
                $result = TRUE;
            } else {
                $error='用户不存在';
            }
            
        }
        $this->render('gold',array('error'=>$error,'result'=>$result)); 
    }
}
