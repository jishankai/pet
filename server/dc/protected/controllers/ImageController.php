<?php

class ImageController extends Controller
{
    public function filters()
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId - randomApi',
            array(
                'COutputCache + randomApi',
                'duration' => 300,
                'varyByParam' => 'img_id',
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_image",
                ),
            ),
        );
    }

    public function actionUploadApi()
    {
        $model = new Image;

        /*
        if (isset($_POST['comment'])) {
        Yii::trace("Image: ".$_POST['comment'], 'access');
        }
        if (isset($_FILES['image'])) {
        Yii::trace("Image: ".$_FILES['image'], 'access');
        }
        */

        if (isset($_FILES['image'])) {
            $model->usr_id = $this->usr_id;
            $model->comment = $_POST['comment'];
            $model->create_time = time();
            $img_count = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_image WHERE usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryScalar();

            $fname = basename($_FILES['image']['name']);
            $path = Yii::app()->basePath.'/../images/upload/'.$model->usr_id.'_'.$img_count.'.'.$fname;
            if (move_uploaded_file($_FILES['image']['tmp_name'], $path)) {
                $model->url = $model->usr_id.'_'.$img_count.'.'.$fname;
                $model->save();
            }
        }

        $this->echoJsonData(array($model));
    }

    public function actionLikeApi($img_id)
    {
        $image = $this->loadModel($img_id);

        $transaction = Yii::app()->db->beginTransaction();
        try {
            $image->likes++;
            $image->saveAttributes(array('likes'));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionDeleteApi($img_id)
    {
        $image = $this->loadModel($img_id);
        if (file_exists($model->url)) {
            unlink($image->url);
        }
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $image->delete();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        $this->echoJsonData(array('isSuccess'=>true));
    }

    public function actionFavoriteApi($img_id=NULL)
    {
        $dependency = new CDbCacheDependency("SELECT MAX(update_time) FROM dc_friend WHERE usr_id = :usr_id");
        $dependency->params[':usr_id']=$this->usr_id;
        $follow_ids = Yii::app()->db->cache(1000, $dependency)->createCommand('SELECT follow_id FROM dc_friend WHERE usr_id = :usr_id')->bindValue(':usr_id', $this->usr_id)->queryColumn();

        $c = new CDbCriteria;
        $c->compare('usr_id', $follow_ids);
        $c->limit = 10;
        $c->order = 'img_id DESC';
        if(isset($img_id)) {
            $c->addCondition("img_id<:img_id");
            $c->params[':img_id'] = $img_id;
        }
        $images = Image::model()->findAll($c);

        $this->echoJsonData(array($images));
    }

    public function actionRandomApi($img_id=NULL)
    {
        if (isset($img_id)) {
            $images =  Yii::app()->db->createCommand('SELECT img_id, likes, url FROM dc_image WHERE img_id<:img_id ORDER BY create_time DESC LIMIT 10')->bindValue(':img_id', $img_id)->queryAll();        
        } else {
            $images =  Yii::app()->db->createCommand('SELECT img_id, likes, url FROM dc_image ORDER BY create_time DESC LIMIT 10')->queryAll();        
        }

        $this->echoJsonData(array($images));
    }

    public function actionInfoApi($img_id)
    {
        $dependency = new CDbCacheDependency("SELECT update_time FROM dc_image WHERE img_id = :img_id");
        $dependency->params[':img_id']=$img_id;
        $image = Image::model()->cache(3600, $dependency)->findByPk($img_id);
        
        $this->echoJsonData(array($image)); 
    }
}
