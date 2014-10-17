<?php

class ImageController extends Controller
{
    public function filters()
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId - recommendApi,randomApi,infoApi,shareApi',
            array(
                'COutputCache + randomApi',
                'duration' => 300,
                'varyByParam' => array('img_id'),
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_image",
                ),
            ),
            /*
            array(
                'COutputCache + favoriteApi',
                'duration' => 300,
                'varyByParam' => array('img_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => 'SELECT MAX(i.update_time) FROM dc_follow f LEFT JOIN dc_image i ON f.aid=i.aid WHERE usr_id = :usr_id',
                    'params' => array(
                        'usr_id' => $this->usr_id,
                    ),
                ),
            ),
                */

            array(
                'COutputCache + topicApi',
                'duration' => 3600,
            )
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
        //$cmtlen = (strlen($_POST['comment'])+mb_strlen($_POST['comment'],"UTF8"))/2;
        $cmtlen = mb_strlen($_POST['comment'],"UTF8");
        if ($cmtlen>40) {
            throw new PException('描述不合要求');
        }

        if (isset($_FILES['image'])&&isset($aid)) {
            $transaction = Yii::app()->db->beginTransaction();
            try {
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

                $fname = basename($_FILES['image']['name']);
                #$success = Yii::app()->s3->upload( $_FILES['image']['tmp_name'], 'upload/'.$fname, 'pet4jishankaitest' );
                //$rtn = Yii::app()->oss->upload_file('pet4upload', $model->aid.'_'.$fname, fopen($_FILES['image']['tmp_name'],'r'), $_FILES['image']['size']); 
                $path = Yii::app()->basePath.'/../images/upload/'.$model->aid.'_'.$fname;
                if (move_uploaded_file($_FILES['image']['tmp_name'], $path)) {
                    $model->url = $model->aid.'_'.$fname;
                    $model->save();

                    //events
                    $user = User::model()->findByPk($this->usr_id);
                    $ex_gold = $user->gold;
                    $ex_exp = $user->exp;
                    $ex_lv = $user->lv;
                    $user->uploadImage($aid);

                    $news = new News;
                    $news->aid = $aid;
                    $news->type = 3;
                    $news->create_time = time();
                    $user = User::model()->findByPk($this->usr_id);
                    $news->content = serialize(array(
                        'usr_id'=>$user->usr_id,
                        'u_name'=>$user->name,
                        'img_id'=>$model->img_id,
                        'img_url'=>$model->url,
                    ));
                    $news->save();
                    
                    if (isset($_POST['relates'])) {
                        $usr_ids = explode(',',$model->relates);
                        foreach ($usr_ids as $usr_id) {
                            Talk::model()->sendMsg(NPC_IMAGE_USRID, $usr_id, $user->name."[".$model->img_id."]在爱宠的照片中@了你，没想到你人缘还不错，还真让本喵吃惊呀");
                        }
                    }
                }
                $transaction->commit();
            } catch (Exception $e) {
                $transaction->rollback();
                throw $e;
            }
        }

        $this->echoJsonData(array('image'=>$model, 'exp'=>$user->exp-$ex_exp, 'gold'=>$user->gold-$ex_gold, 'lv'=>$user->lv-$ex_lv));
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

            $animal = Animal::model()->findByPk($image->aid);
            $animal->t_rq+=5;
            $animal->d_rq+=5;
            $animal->w_rq+=5;
            $animal->m_rq+=5;
            $animal->saveAttributes(array('t_rq','d_rq','w_rq','m_rq'));

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
        $dependency = new CDbCacheDependency("SELECT MAX(i.update_time) FROM dc_follow f LEFT JOIN dc_image i ON f.aid=i.aid WHERE usr_id = :usr_id");
        $dependency->params[':usr_id'] = $this->usr_id;
        $r = Yii::app()->db->cache(3600, $dependency)->createCommand('SELECT i.img_id, i.url, i.cmt, i.likes, i.likers, i.aid, a.tx, a.name, a.type, i.create_time FROM dc_image i INNER JOIN dc_follow f ON f.aid=i.aid LEFT JOIN dc_animal a ON i.aid=a.aid WHERE usr_id=:usr_id AND img_id<:img_id ORDER BY img_id DESC LIMIT 30')->bindValues(array(
            ':usr_id' => $this->usr_id,
            ':img_id' => $img_id,
        ))->queryAll();

        $this->echoJsonData($r);
    }

    public function actionRecommendApi($img_id=NULL)
    {
        if (isset($img_id)) {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url FROM dc_image i WHERE i.img_id<:img_id ORDER BY i.create_time DESC LIMIT 30')->bindValue(':img_id', $img_id)->queryAll();        
        } else {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url FROM dc_image i ORDER BY i.create_time DESC LIMIT 30')->queryAll();        
        }

        $this->echoJsonData(array($images));
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
            $sender_tx = Yii::app()->db->createCommand("SELECT tx FROM dc_user WHERE usr_id IN ($image->senders)")->queryColumn();
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
        $ex_gold = $user->gold;
        $ex_exp = $user->exp;
        $ex_lv = $user->lv;

        /*
        $comment = array(
            'usr_id' => $this->usr_id,
            'name' => $user->name,
            'body' => $body,
            'create_time' => time(),
        );
         */
        if (isset($_POST['reply_id'])) {
            $image->comments = 'usr_id:'.$this->usr_id.','.'name:'.$user->name.','.'reply_id:'.$_POST['reply_id'].','.'reply_name:'.$_POST['reply_name'].','.'body:'.$body.','.'create_time:'.time().';'.$image->comments;
            //PMail::create($_POST['reply_id'], $user, $user->name.'在'.$image->img_id.'回复了你');
        } else {
            $image->comments = 'usr_id:'.$this->usr_id.','.'name:'.$user->name.','.'body:'.$body.','.'create_time:'.time().';'.$image->comments;
        }
        

        if ($image->saveAttributes(array('comments'))) {
            $animal = Animal::model()->findByPk($image->aid);
            $animal->t_rq+=5;
            $animal->d_rq+=5;
            $animal->w_rq+=5;
            $animal->m_rq+=5;
            $animal->saveAttributes(array('t_rq','d_rq','w_rq','m_rq'));

            $session = Yii::app()->session;
            if (isset($session['comment_count'])) {
                $session['comment_count']+=1;
            } else {
                $session['comment_count']=1;
            }
            
            if ($session['comment_count']<=15) {
                $user->comment();
            }
            if (isset($_POST['reply_id'])) {
                Talk::model()->sendMsg(NPC_IMAGE_USRID, $_POST['reply_id'], "[".$image->img_id."]".$user->name."回复了你：".$body);
            } else {
                Talk::model()->sendMsg(NPC_IMAGE_USRID, $animal->master_id, "[".$image->img_id."]".$user->name."评论了你：".$body);
            }

            $this->echoJsonData(array('exp'=>$user->exp-$ex_exp, 'gold'=>$user->gold-$ex_gold, 'lv'=>$user->lv-$ex_lv));
        }
    }

    public function actionTopicApi()
    {
        $r = Yii::app()->db->createCommand('SELECT topic_id, topic FROM dc_topic ORDER BY topic_id DESC')->queryAll();

        $this->echoJsonData($r);       
    }
    
    public function actionShareApi($img_id)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $image = Image::model()->findByPk($img_id);
            $image->shares++;
            $image->saveAttributes(array('shares'));

            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        $this->echoJsonData(array('isSuccess'=>TRUE));
    }
}
