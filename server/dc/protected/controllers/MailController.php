<?php

class MailController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId',
        );
    }

    public function actionListApi($is_system=FALSE, $mail_id=NULL)
    {
        $c = new CDbCriteria;
        $c->compare('is_system', $is_system);
        $c->compare('is_deleted', 0);
        $c->compare('usr_id', $this->usr_id);
        $c->limit = 30;
        $c->order = 'mail_id DESC';
        if(isset($mail_id)) {
            $c->compare('mail_id', '<'.$mail_id);
        }
        $mails = Mail::model()->findAll($c);

        Yii::app()->db->createCommand('UPDATE dc_mail SET is_read=1 WHERE usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->execute();

        $this->echoJsonData(array($mails));
    }

    public function actionCreateApi()
    {
        $user = User::model()->findByPk($this->usr_id);
        $isSuccess = PMail::create($_POST['to_id'], $user, $_POST['body'], FALSE);

        $this->echoJsonData(array('isSuccess'=>$isSuccess)); 
    }

    public function actionDeleteApi($mail_id)
    {
        $mail = PMail::model()->findByPk($mail_id);
        $isSuccess = $mail->saveAttributes(array('is_deleted'=>1));

        $this->echoJsonData(array('isSuccess'=>$isSuccess));
    }

}
