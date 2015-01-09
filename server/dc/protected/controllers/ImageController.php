<?php

class ImageController extends Controller
{
    public function filters()
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId - recommendApi,randomApi,infoApi,recoApi,reportApi,ask4FoodApi,bannerApi,rewardFoodMobileApi',
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
        $is_food = 0;

        if (isset($_POST['comment'])) {
            Yii::trace("Image: ".$_POST['comment'], 'access');
        }
        if (isset($_POST['is_food'])) {
            $is_food = $_POST['is_food'];
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
                $model->is_food = $is_food;
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
                $rtn = Yii::app()->oss->upload_file(OSS_PREFIX.'4upload', $model->aid.'_'.$fname, fopen($_FILES['image']['tmp_name'],'r'), $_FILES['image']['size']); 
                if ($rtn) {
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

                    if ($is_food) {
                        $animal = Animal::model()->findByPk($aid);
                        $circles = Circle::model()->findAllByAttributes(array('aid'=>$aid));
                        foreach ($circles as $circle) {
                            if (isset($circle) and isset($animal)) {
                                Talk::model()->sendMsg(NPC_IMAGE_USRID, $circle->usr_id, "[".$model->img_id."]大萌星".$animal->name."发布了一张挣口粮的新萌照，快去支持吧～");
                            }
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
            $ex_gold = $user->gold;
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

            $this->echoJsonData(array('isSuccess'=>TRUE, 'gold'=>$user->gold-$ex_gold));
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
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url, i.cmt AS cmt FROM dc_image i WHERE i.img_id<:img_id ORDER BY i.create_time DESC LIMIT 30')->bindValue(':img_id', $img_id)->queryAll();        
        } else {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url, i.cmt AS cmt FROM dc_image i ORDER BY i.create_time DESC LIMIT 30')->queryAll();        
        }

        $this->echoJsonData(array($images));
    }
    
    public function actionRecoApi($page=0)
    {
        $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url, i.cmt AS cmt FROM dc_image i ORDER BY i.likes+i.shares+i.gifts DESC, i.create_time DESC LIMIT :m,30')->bindValue(':m', 30*$page)->queryAll();        

        $this->echoJsonData(array($images));
    }

    public function actionRandomApi($img_id=NULL)
    {
        if (isset($img_id)) {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url, i.cmt AS cmt FROM dc_image i WHERE i.img_id<:img_id ORDER BY i.create_time DESC LIMIT 30')->bindValue(':img_id', $img_id)->queryAll();        
        } else {
            $images =  Yii::app()->db->createCommand('SELECT i.img_id AS img_id, url, i.cmt AS cmt FROM dc_image i ORDER BY i.create_time DESC LIMIT 30')->queryAll();        
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

    public function actionAsk4FoodApi($page=0)
    {
        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.aid, i.cmt, i.food, i.create_time, a.name, a.tx, a.type, a.gender, u.usr_id, u.tx AS u_tx, u.name AS u_name  FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE is_food=1 AND i.create_time>=:create_time ORDER BY i.create_time DESC LIMIT :m,30')->bindValues(array(':create_time'=> time()-(60*60*24), ':m'=>$page*30))->queryAll();

        $this->echoJsonData(array($r));
    }

    public function actionRewardFoodMobileApi($img_id=0, $n, $to='', $aid=0, $SID='')
    {
        if ($SID!='') {
            $session = Yii::app()->session;
            $this->usr_id = $session['usr_id'];
        } 
        if (!isset($this->usr_id)) {
            if ($to=='') {
                if (strpos($_SERVER['HTTP_USER_AGENT'], "MicroMessenger")) {
                    $to = 'wechat';
                } else {
                    $to = 'weibo';
                }
            }
            switch ($to) {
            case 'wechat':
                $oauth2 = Yii::app()->wechat;
                $key = 'wechatoauth2_'.$oauth2->APPID;
                if (isset($_COOKIE[$key])&&$cookie=$_COOKIE[$key]) {
                    parse_str($cookie);
                    $this->usr_id = $usr_id;
                } else {
                    $oauth2->get_code_by_authorize(http_build_query(array('img_id'=>$img_id, 'aid'=>$aid)));
                }
                break;
            case 'weibo':
                Yii::import('ext.sinaWeibo.SinaWeibo',true);
                $oauth2 = new SinaWeibo(WB_AKEY, WB_SKEY);
                $key = 'weibooauth2_'.$oauth2->client_id;
                if (isset($_COOKIE[$key])&&$cookie=$_COOKIE[$key]) {
                    parse_str($cookie);
                    $this->usr_id = $usr_id;
                } else {
                    $this->redirect($oauth2->getAuthorizeURL(WB_CALLBACK_URL, 'code', http_build_query(array('img_id'=>$img_id, 'aid'=>$aid)), 'mobile'));
                }
                break;
            default:
                # code...
                break;
            }
            exit;
        }

        if ($img_id!=0) {
            $image = Image::model()->findByPk($img_id); 
            $aid = $image->aid;
        } 
        $animal = Animal::model()->findByPk($aid);

        $user = User::model()->findByPk($this->usr_id);

        if (!isset($session['food'])) {
            $session['food'] = 5;
        }

        if ($session['food']+$user->gold<$n) {
            throw new PException('您的余粮不足');
        }

        $transaction = Yii::app()->db->beginTransaction();
        try {
            if ($session['food']>=$n) {
                $session['food']-=$n;
            } else {
                $user->gold-=($n-$session['food']);
                $session['food']=0;
                $user->saveAttributes(array('gold'));
            }
            if ($img_id!=0) {
                $image->food+=$n;
                $image->saveAttributes(array('food'));
            }
            $animal->food+=$n;
            $animal->total_food+=$n;
            $animal->saveAttributes(array('food','total_food'));
            
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }

        if ($img_id==0) {
            $this->redirect(array('social/activityview', 'aid'=>$aid, 'SID'=>$SID));
        } else {
            $this->redirect(array('social/foodShareApi', 'img_id'=>$img_id, 'aid'=>$aid, 'SID'=>$SID));
        }
    }

    public function actionRewardFoodApi($img_id, $n)
    {
        $image = Image::model()->findByPk($img_id); 
        $animal = Animal::model()->findByPk($image->aid);
       
        $user = User::model()->findByPk($this->usr_id);

        $session = Yii::app()->session;
        if (!isset($session['food'])) {
            $session['food'] = 5;
        }

        if ($session['food']+$user->gold<$n) {
            throw new PException('您的余粮不足');
        }

        $transaction = Yii::app()->db->beginTransaction();
        try {
            if ($session['food']>=$n) {
                $session['food']-=$n;
            } else {
                $user->gold-=($n-$session['food']);
                $session['food']=0;
                $user->saveAttributes(array('gold'));
            }
            $image->food+=$n;
            $animal->food+=$n;
            $animal->total_food+=$n;

            $image->saveAttributes(array('food'));
            $animal->saveAttributes(array('food','total_food'));
            
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }

        $this->echoJsonData(array('food'=>$image->food, 'gold'=>$user->gold));
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
                $user->comment($session['comment_count']);
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
            if (isset($image->sharers)&&$image->sharers!='') {
                $sharers = explode(',', $image->sharers);
                $sharers[] = $this->usr_id;
                $image->sharers = implode(',', $sharers);
            } else {
                $image->sharers = $this->usr_id;
            }
            $image->saveAttributes(array('shares', 'sharers'));

            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        $this->echoJsonData(array('isSuccess'=>TRUE));
    }

    public function actionReportApi($img_id)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $image = Image::model()->findByPk($img_id);
            $image->reports++;
            $image->saveAttributes(array('reports'));

            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
        $this->echoJsonData(array('isSuccess'=>TRUE));
    }

    public function actionBannerApi()
    {
        $r = Yii::app()->db->createCommand('SELECT img_url, url, icon, title, description FROM banner WHERE start_time<=:time AND end_time>:time')->bindValue(':time', time())->queryAll();

        $this->echoJsonData(array($r));        
    }
}
