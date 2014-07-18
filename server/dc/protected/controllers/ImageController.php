<?php

class ImageController extends Controller
{
    public function filters()
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId - randomApi,infoApi',
            array(
                'COutputCache + randomApi',
                'duration' => 300,
                'varyByParam' => array('img_id'),
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_image",
                ),
            ),
            array(
                'COutputCache + favoriteApi',
                'duration' => 300,
                'varyByParam' => array('img_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => 'SELECT MAX(update_time) FROM dc_friend WHERE usr_id = :usr_id',
                    'params' => array(
                        'usr_id' => $this->usr_id,
                    ),
                ),
            ),
        );
    }

    public function actionUploadApi()
    {
        $model = new Image;

        if (isset($_POST['comment'])) {
        Yii::trace("Image: ".$_POST['comment'], 'access');
        }
        /*
        if (isset($_FILES['image'])) {
        Yii::trace("Image: ".$_FILES['image'], 'access');
        }
        */
        $cmtlen = (strlen($_POST['comment'])+mb_strlen($_POST['comment'],"UTF8"))/2;
        if ($cmtlen>40) {
            throw new PException('描述不合要求');
        }
        
        if (isset($_FILES['image'])) {
            $model->usr_id = $this->usr_id;
            $model->cmt = $_POST['comment'];
            preg_match("/#\s*([^#]*)\s*#/",$_POST['comment'],$matches);
            #Yii::trace($_POST['comment'].$matches[0].'...'.$matches[1], 'access');
            if (isset($matches[1])) {
                $c = new CDbCriteria;
                $c->compare('topic', $matches[1], true);
                $c->compare('start_time', '<='.time());  
                $c->compare('end_time', '>'.time());  
                $c->select = 'topic_id';
                $topic = Topic::model()->find($c);
                //$topic_id = Yii::app()->db->createCommand('SELECT topic_id FROM dc_topic WHERE topic LIKE "%:topic%" AND start_time<=:time AND end_time>:time')->bindValues(array(':topic'=>$matches[1], ':time'=>time()))->queryScalar();
                //Yii::trace('topic:'.$topic_id.$matches[1].$topic->topic_id, 'access');
                if (isset($topic)) {
                    $model->topic_id = $topic->topic_id;
                }
            }
            $model->create_time = time();
            $img_count = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_image WHERE usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryScalar();

            $fname = basename($_FILES['image']['name']);
            #$success = Yii::app()->s3->upload( $_FILES['image']['tmp_name'], 'upload/'.$fname, 'pet4jishankaitest' );
            $path = Yii::app()->basePath.'/../images/upload/'.$model->usr_id.'_'.$img_count.'.'.$fname;
            if (move_uploaded_file($_FILES['image']['tmp_name'], $path)) {
                $model->url = $model->usr_id.'_'.$img_count.'.'.$fname;
                $model->save();
                
                //events
                $user = User::model()->findByPk($this->usr_id);
                $user->uploadImage();
            }
        }

        $this->echoJsonData(array($model));
    }

    public function actionLikeApi($img_id)
    {
        $image = Image::model()->findByPk($img_id);

        $transaction = Yii::app()->db->beginTransaction();
        try {
            if (isset($image->likers)&&$image->likers!='') {
                $likers = explode(',', $image->likers);
                if (in_array($this->usr_id, $likers)) {
                    throw new PException('您已经点过赞了');
                } else {
                    $likers[] = $this->usr_id;
                    $image->likers = implode(',', $likers);
                }
            } else {
                $image->likers = $this->usr_id;
            }
            $image->likes++;
            $image->saveAttributes(array('likes', 'likers'));

            $user = User::model()->findByPk($this->usr_id);
            $user->like();

            //events
            if ($image->usr_id!=$this->usr_id) {
                PMail::create($image->usr_id, $user, $user->name.'赞了你');
            }

            $transaction->commit();

            $this->echoJsonData(array('isSuccess'=>true));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionUnlikeApi($img_id)
    {
        $image = Image::model()->findByPk($img_id);

        $transaction = Yii::app()->db->beginTransaction();
        try {
            if (($likers = explode(',', $image->likers)) && in_array($this->usr_id, $likers)) {
                $image->likes--;
                unset($likers[array_search($this->usr_id, $likers)]);
                $image->likers = implode(',', $likers);
                $image->saveAttributes(array('likes', 'likers'));

                $transaction->commit();
            } else {
                throw new PException('您还未点过赞');
            }

            $this->echoJsonData(array('isSuccess'=>true));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionDeleteApi($img_id)
    {
        $image = Image::model()->findByPk($img_id);
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
        $follow_1 = Yii::app()->db->createCommand('SELECT follow_id FROM dc_friend WHERE usr_id = :usr_id AND relation IN (0,1)')->bindValue(':usr_id', $this->usr_id)->queryColumn();
        $follow_2 = Yii::app()->db->createCommand('SELECT usr_id FROM dc_friend WHERE follow_id = :usr_id AND relation IN (0,-1)')->bindValue(':usr_id', $this->usr_id)->queryColumn();

        $follow_ids = array_merge($follow_1, $follow_2);

        $c = new CDbCriteria;
        $c->compare('t.usr_id', $follow_ids);
        $c->limit = 10;
        $c->order = 'img_id DESC';
        if(isset($img_id)) {
            $c->compare('img_id', '<'.$img_id);
        }
        $imgs = Image::model()->with('usr')->findAll($c);
        /*
        $images = array();
        foreach ($imgs as $img) {
            $user = $img->usr;
            $images[] = array_merge($user->getAttributes(), $img->getAttributes());
        }
         */

        $this->echoJsonData(array($imgs));
    }

    public function actionRandomApi($img_id=NULL)
    {
        if (isset($img_id)) {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url FROM dc_image i WHERE i.img_id<:img_id ORDER BY i.create_time DESC LIMIT 30')->bindValue(':img_id', $img_id)->queryAll();        
        } else {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url FROM dc_image i ORDER BY i.create_time DESC LIMIT 30')->queryAll();        
        }

        $this->echoJsonData(array($images));
    }

    public function actionInfoApi($img_id)
    {
        $dependency = new CDbCacheDependency("SELECT update_time FROM dc_image WHERE img_id = :img_id");
        $dependency->params[':img_id'] = $img_id;
        $image = Image::model()->cache(3600, $dependency)->findByPk($img_id);

        if (isset($image->likers)&&$image->likers!='') {
            $liker_tx = Yii::app()->db->createCommand("SELECT tx FROM dc_user WHERE usr_id IN ($image->likers)")->queryColumn();
        }
        
        $this->echoJsonData(array(
            'image'=>$image,
            'liker_tx'=>isset($liker_tx)?$liker_tx:NULL,
        )); 
    }

    public function actionCommentApi()
    {
        $img_id = $_POST['img_id'];
        $body = $_POST['body'];

        $image = Image::model()->findByPk($img_id);
        $user = User::model()->findByPk($this->usr_id);

        /*
        $comment = array(
            'usr_id' => $this->usr_id,
            'name' => $user->name,
            'body' => $body,
            'create_time' => time(),
        );
         */

        $image->comments = $image->comments.';'.'usr_id:'.$this->usr_id.','.'name:'.$user->name.','.'body:'.$body.','.'create_time:'.time();

        if ($image->saveAttributes(array('comments'))) {
            $this->echoJsonData(array('isSuccess'=>TRUE));
        }
    }
}
