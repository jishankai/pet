<?php

class AnimalController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig',
            'getUserId',
            /*
            array(
                'COutputCache + welcomeApi',
                'duration' => 86800,
                'dependency' => array(
                    'class' => 'CExpressionDependency',
                    'expression' => 'date("m.d.y")',
                ),
            ),
             */
            /*
            array(
                'COutputCache + infoApi',
                'duration' => 3600,
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_user WHERE usr_id=:usr_id",
                    'params' => array(
                        ':usr_id' => $this->usr_id,
                    ),
                ),
            ),
             */
            array(
                'COutputCache + imagesApi',
                'duration' => 3600,
                'varyByParam' => array('img_id', 'usr_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_image WHERE usr_id=:usr_id",
                    'params' => array(
                        ':usr_id' => $this->usr_id,
                    ),
                ),
            ),
            /*
            array(
                'COutputCache + followingApi',
                'duration' => 3600,
                'varyByParam' => array('follow_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT COUNT(*), MAX(update_time) FROM dc_friend WHERE (usr_id=:usr_id AND relation IN (0,1)) OR (follow_id=:usr_id AND relation IN (0,-1))",
                    'params' => array(
                        ':usr_id' => $this->usr_id,
                    ),
                ),
            ),
            array(
                'COutputCache + followerApi',
                'duration' => 3600,
                'varyByParam' => array('usr_id'),
                'varyBySession' => true,
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT COUNT(*), MAX(update_time) FROM dc_friend WHERE (follow_id=:follow_id AND relation IN (0,1)) OR (usr_id=:follow_id AND relation IN(0,-1))",
                    'params' => array(
                        ':follow_id' => $this->usr_id,
                    ),
                ),
            ),
             */
            array(
                'COutputCache + othersApi',
                'duration' => 3600,
                'varyByParam' => array('usr_ids'),
            )
        );
    }

    public function actionInfoApi($aid)
    {
        $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.gender, a.from, a.type, a.age, a.master_id, a.t_rq, u.name, u.tx, u.rank FROM dc_animal a JOIN dc_user u ON a.master_id=u.usr_id WHERE a.aid=:aid')->bindValue(':aid', $aid)->queryColumn();
        $r['fans'] = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_circle WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();
        $r['followers'] = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_follow WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();

        $this->echoJsonData($r);
    }

    public function actionTxApi($aid)
    {
        $a = Animal::model()->findByPk($aid);

        if (isset($_FILES['tx'])) {
            $fname = basename($_FILES['tx']['name']);
            $path = Yii::app()->basePath.'/../images/tx_ani/'.$aid.'_'.$fname;
            if (move_uploaded_file($_FILES['tx']['tmp_name'], $path)) {
                $a->tx = $aid.'_'.$fname;
                $a->saveAttributes(array('tx'));
            }
        }

        $this->echoJsonData(array('tx'=>$a->tx));
    }


    public function actionImagesApi($aid, $img_id=NULL)
    {
        $c = new CDbCriteria;

        $c->compare('aid', $aid);
        $c->limit = 10;
        $c->order = 't.img_id DESC';
        if(isset($img_id)) {
            $c->addCondition("t.img_id<:img_id");
            $c->params[':img_id'] = $img_id;
        }

        $images = Image::model()->findAll($c);

        $this->echoJsonData($images);
    }

    public function actionFollowApi($aid)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $f = Follow::model()->findByPk(array(
                'usr_id' => $this->usr_id,
                'aid' => $aid,
            ));
            if (!isset($f)) {
                $f = new Follow;
                $f->usr_id = $this->usr_id;
                $f->aid = $aid;
                $f->create_time = time();
            }
            $f->save();

            //events
            //$user = User::model()->findByPk($this->usr_id);
            //PMail::create($usr_id, $user, $user->name.'关注了你');

            $transaction->commit();

            $this->echoJsonData(array(
                'isSuccess' => true,
            ));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionUnFollowApi($aid)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $f = Follow::model()->findByPk(array(
                'usr_id' => $this->usr_id,
                'aid' => $aid,
            ));
            $f->delete();

            $transaction->commit();

            $this->echoJsonData(array(
                'isSuccess' => true,
            ));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionFansApi($aid, $rank=999999999, $usr_id=0)
    {
        $r = Yii::app()->db->createCommand('SELECT c.usr_id as usr_id, rank, contri, u.tx, name, gender, city FROM dc_circle c INNER JOIN dc_user u ON c.usr_id=u.usr_id WHERE c.aid=:aid AND rank<:rank AND c.usr_id>:usr_id  ORDER BY rank DESC, c.usr_id DESC LIMIT 30')->bindValues(array(
            ':aid'=>$aid,
            ':rank'=>$rank,
            ':usr_id'=>$usr_id,
        ))->queryAll();

        $this->echoJsonData($r);
    }

    public function actionItemsApi($aid)
    {
        $i = Yii::app()->db->createCommand('SELECT items FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();
        $r = unserialize($i);

        $this->echoJsonData($r);
    }

    public function actionNotifyApi()
    {
        $mail_n = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_mail WHERE is_read=0 AND usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryScalar();
        $topic_n = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_topic WHERE status!=0 AND start_time<=:time AND end_time>:time')->bindValue(':time', time())->queryScalar();

        $this->echoJsonData(array(
            'mail_count' => $mail_n,
            'topic_count' => $topic_n,
        ));
    }

    public function actionCreateApi()
    {
        
    }

    public function actionJoinApi()
    {
        
    }

    public function actionUnJoinApi($aid)
    {
        
    }

    public function actionVoiceUpApi($aid)
    {
        
    }

    public function actionVoiceDownApi($aid)
    {
        
    }

    public function actionTouchApi($aid)
    {
        
    }

    public function actionShakeApi($aid)
    {
        
    }
}
