<?php

class ImageController extends Controller
{
    public function filters()
    {
        return array(
            'checkUpdate',
            'checkSig',
        );
    }

    public function actionUploadApi()
    {
        $model = new Image;

        if (isset($_POST['image'])) {
            $model->file = CUploadedFile::getInstanceByName($_POST['image']);
            $model->uid = $this->uid;
            $model->comment = $_POST['comment'];
            $model->create_time = time();
            $img_count = Yii::app()->db->createCommand('SELECT COUNT(*) FROM image WHERE uid=:uid')->bindValue(':uid', $uid)->queryScalar();
            $path = Yii;;app()->basePath.'/../images/upload/'.$model->uid.'_'.$img_count.'.'.$model->file->getExtensionName();
            $model->file->saveAs($path);

            $model->url = $model->uid.'_'.$img_count.'.'.$model->file->getExtensionName();
            $model->save();
        }

        $this->echoJsonData(array('url'=>$model->url));
    }

    public function actionLikeApi($img_id)
    {
        $image = $this->loadModel($img_id);

        $transaction = Yii::app()->db->beginTransaction();
        try {
            $image->like++;
            $image->saveAttributes(array('like'));
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
}
