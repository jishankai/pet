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
                    'sql' => 'SELECT MAX(update_time) FROM dc_follow WHERE usr_id = :usr_id',
                    'params' => array(
                        'usr_id' => $this->usr_id,
                    ),
                ),
            ),
        );
    }

    public function actionUploadApi($aid)
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

        if (isset($_FILES['image'])&&isset($aid)) {
            $model->aid = $aid;
            $model->cmt = $_POST['comment'];
            /*
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
                    $model->topic_name = $topic->name;
                } else {
                    $model->topic_name = $matches[1];
                }
            }
             */
            if (isset($_POST['topic_id'])) {
                $model->topic_id = $_POST['topic_id'];
                $model->topic_name = $_POST['topic_name'];
            } else {
                $model->topic_name = $_POST['topic_name'];
            }
            if (isset($_POST['relates'])) {
                $model->relates = $_POST['relates'];
            }
            $model->create_time = time();
            $img_count = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_image WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();

            $fname = basename($_FILES['image']['name']);
            #$success = Yii::app()->s3->upload( $_FILES['image']['tmp_name'], 'upload/'.$fname, 'pet4jishankaitest' );
            $path = Yii::app()->basePath.'/../images/upload/'.$model->aid.'_'.$img_count.'.'.$fname;
            if (move_uploaded_file($_FILES['image']['tmp_name'], $path)) {
                $model->url = $model->aid.'_'.$img_count.'.'.$fname;
                $model->save();
                
                //events
                $user = User::model()->findByPk($this->usr_id);
                $user->uploadImage($aid);
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
            //PMail::create($image->usr_id, $user, $user->name.'赞了你');

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

    public function actionFavoriteApi($img_id=9999999999)
    {
        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.cmt, i.likes, i.aid, a.tx, a.name, i.create_time FROM dc_image i INNER JOIN dc_follow f ON f.aid=i.aid LEFT JOIN dc_animal a ON i.aid=a.aid WHERE usr_id=:usr_id AND img_id<:img_id ORDER BY img_id DESC LIMIT 30')->bindValues(array(
            ':usr_id' => $this->usr_id,
            ':img_id' => $img_id,
        ))->queryAll();

        $this->echoJsonData($r);
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

    public function actionInfoApi($img_id, $usr_id)
    {
        $dependency = new CDbCacheDependency("SELECT update_time FROM dc_image WHERE img_id = :img_id");
        $dependency->params[':img_id'] = $img_id;
        $image = Image::model()->cache(3600, $dependency)->findByPk($img_id);

        $is_follow = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_follow WHERE aid=:aid AND usr_id=:usr_id ')->bindValues(array(
            ':aid' => $image->aid,
            ':usr_id' => $usr_id,
        ))->queryScalar();

        if (isset($image->likers)&&$image->likers!='') {
            $liker_tx = Yii::app()->db->createCommand("SELECT tx FROM dc_user WHERE usr_id IN ($image->likers)")->queryColumn();
        }
        
        if (isset($image->senders)&&$image->senders!='') {
            $liker_tx = Yii::app()->db->createCommand("SELECT tx FROM dc_user WHERE usr_id IN ($image->senders)")->queryColumn();
        }
        
        $this->echoJsonData(array(
            'image'=>$image,
            'is_follow'=>$is_follow,
            'sender_tx'=>isset($sender_tx)?$sender_tx:NULL,
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
        if (isset($_POST['reply_id'])) {
            $image->comments = $image->comments.';'.'usr_id:'.$this->usr_id.','.'name:'.$user->name.','.'reply_id:'.$_POST['reply_id'].','.'reply_name'.$_POST['reply_name'].','.'body:'.$body.','.'create_time:'.time();
            PMail::create($_POST['reply_id'], $user, $user->name.'在'.$image->img_id.'回复了你');
        } else {
            $image->comments = $image->comments.';'.'usr_id:'.$this->usr_id.','.'name:'.$user->name.','.'body:'.$body.','.'create_time:'.time();
        }
        

        if ($image->saveAttributes(array('comments'))) {
            $this->echoJsonData(array('isSuccess'=>TRUE));
        }
    }
}
