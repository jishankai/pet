<?php

class AnimalController extends Controller
{
    public function filters() 
    {
        return array(
            'checkUpdate',
            'checkSig - infoShare,joinMobileApi',
            'getUserId - infoApi,recommendApi,popularApi,cardApi,searchApi,newsApi,txApi,imagesApi,fansApi,itemsApi,infoShare,joinMobileApi',
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
            /*
            array(
                'COutputCache + imagesApi',
                'duration' => 3600,
                'varyByParam' => array('img_id', 'aid'),
                'dependency' => array(
                    'class' => 'CDbCacheDependency',
                    'sql' => "SELECT MAX(update_time) FROM dc_image WHERE usr_id=:usr_id",
                    'params' => array(
                        ':usr_id' => $this->usr_id,
                    ),
                ),
            ),
             */
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
                'COutputCache + cardApi',
                'duration' => 86400,
                'varyByParam' => array('aid'),
            )
        );
    }

    public function actionInfoApi($aid)
    {
        $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.gender, a.from, a.type, a.age, a.master_id, a.t_rq, a.msg, u.name AS u_name, u.tx AS u_tx, c.rank AS u_rank, (SELECT COUNT(nid) FROM dc_news n WHERE n.aid=a.aid) AS news, (SELECT COUNT(*) FROM dc_circle c WHERE c.aid=a.aid) AS fans, (SELECT COUNT(i.img_id) FROM dc_image i WHERE i.aid=a.aid) AS images, a.total_food, (SELECT COUNT(*) FROM dc_follow f WHERE f.aid=a.aid) AS followers FROM dc_animal a JOIN dc_user u ON a.master_id=u.usr_id LEFT JOIN dc_circle c ON a.aid=c.aid AND a.master_id=c.usr_id WHERE a.aid=:aid')->bindValue(':aid', $aid)->queryRow();

        $this->echoJsonData($r);
    }

    public function actionRelationApi($aid)
    {
        $is_fan = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_circle WHERE aid=:aid AND usr_id=:usr_id')->bindValues(array(
            ':aid' => $aid,
            ':usr_id' => $this->usr_id,
        ))->queryScalar();
        $is_follow = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_follow WHERE aid=:aid AND usr_id=:usr_id')->bindValues(array(
            ':aid' => $aid,
            ':usr_id' => $this->usr_id,
        ))->queryScalar();

        $this->echoJsonData(array(
            'is_fan' => $is_fan,
            'is_follow' => $is_follow,
        ));
    }

    public function actionTxApi($aid)
    {
        $a = Animal::model()->findByPk($aid);

        $tx = '';
        if (isset($_FILES['tx'])) {
            $fname = basename($_FILES['tx']['name']);
            $rtn = Yii::app()->oss->upload_file(OSS_PREFIX.'4tx', 'tx_ani/'.$aid.'_'.$fname, fopen($_FILES['tx']['tmp_name'],'r'), $_FILES['tx']['size']); 
            if ($rtn) {
                $tx = $aid.'_'.$fname;
                if (isset($a)) {
                    $a->tx = $tx;
                    $a->saveAttributes(array('tx'));
                } else {
                    Yii::app()->session->add('tx_ani', array('aid'=>$aid,'tx'=>$tx));
                }
            } else {
                throw new PException('未上传成功');
            }
        }

        $this->echoJsonData(array('tx'=>$tx));
    }


    public function actionImagesApi($aid, $img_id=NULL)
    {
        $c = new CDbCriteria;

        $c->compare('aid', $aid);
        $c->limit = 20;
        $c->order = 't.img_id DESC';
        if(isset($img_id)) {
            $c->addCondition("t.img_id<:img_id");
            $c->params[':img_id'] = $img_id;
        }

        $images = Image::model()->findAll($c);

        $this->echoJsonData(array($images));
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
            } else {
                throw new PException("已关注");
            }
            $f->save();

            //events
            //$user = User::model()->findByPk($this->usr_id);
            //PMail::create($usr_id, $user, $user->name.'关注了你');
            $animal = Animal::model()->findByPk($aid);
            $animal->t_rq+=5;
            $animal->d_rq+=5;
            $animal->w_rq+=5;
            $animal->m_rq+=5;
            $animal->saveAttributes(array('t_rq','d_rq','w_rq','m_rq'));

            $news = new News;
            $news->aid = $aid;
            $news->type = 1;
            $news->create_time = time();
            $user = User::model()->findByPk($this->usr_id);
            $news->content = serialize(array(
                'usr_id'=>$user->usr_id,
                'u_name'=>$user->name,
            ));
            $news->save();

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
            if (isset($f)) {
                $f->delete();
            } else {
                throw new PException("未关注");
            }

            $animal = Animal::model()->findByPk($aid);
            $animal->t_rq-=5;
            $animal->d_rq-=5;
            $animal->w_rq-=5;
            $animal->m_rq-=5;
            $animal->saveAttributes(array('t_rq','d_rq','w_rq','m_rq'));

            $transaction->commit();

            $this->echoJsonData(array(
                'isSuccess' => true,
            ));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionFansApi($aid, $rank=999999999, $usr_id=0, $page=0)
    {
        if ($page==0) {
            if ($usr_id!=0) {
                    $t_contri = Yii::app()->db->createCommand('SELECT t_contri FROM dc_circle WHERE aid=:aid AND usr_id=:usr_id')->bindValues(array(':aid'=>$aid, ':usr_id'=>$usr_id))->queryScalar();
                $r = Yii::app()->db->createCommand('SELECT c.usr_id as usr_id, rank, t_contri, u.tx, name, gender, city FROM dc_circle c INNER JOIN dc_user u ON c.usr_id=u.usr_id WHERE c.aid=:aid AND t_contri<:t_contri ORDER BY t_contri DESC LIMIT 30')->bindValues(array(
                    ':aid'=>$aid,
                    ':t_contri'=>$t_contri,
                ))->queryAll();
            } else {
                $r = Yii::app()->db->createCommand('SELECT c.usr_id as usr_id, rank, t_contri, u.tx, name, gender, city FROM dc_circle c INNER JOIN dc_user u ON c.usr_id=u.usr_id WHERE c.aid=:aid ORDER BY t_contri DESC LIMIT 30')->bindValues(array(
                    ':aid'=>$aid,
                ))->queryAll();
            }
            
        } else {
            $r = Yii::app()->db->createCommand('SELECT c.usr_id as usr_id, rank, t_contri, u.tx, name, gender, city FROM dc_circle c INNER JOIN dc_user u ON c.usr_id=u.usr_id WHERE c.aid=:aid ORDER BY t_contri DESC, c.usr_id DESC LIMIT :m,30')->bindValues(array(
                ':aid'=>$aid,
                ':m'=>$page*30,
            ))->queryAll();
        }

        $this->echoJsonData(array($r));
    }

    public function actionItemsApi($aid)
    {
        $i = Yii::app()->db->createCommand('SELECT items FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();
        $r = unserialize($i);

        $this->echoJsonData($r);
    }

    public function actionNewsApi($aid, $nid=999999999)
    {
        $r = Yii::app()->db->createCommand('SELECT nid, aid, type, content, create_time FROM dc_news WHERE aid=:aid AND nid<:nid ORDER BY nid DESC LIMIT 30')->bindValues(array(
            ':aid' => $aid,
            ':nid' => $nid,
        ))->queryAll();

        foreach ($r as $k=>$v) {
            $r[$k]['content'] = unserialize($v['content']);
        }

        $this->echoJsonData($r);
    }

    public function actionCreateApi($name, $gender, $age, $type)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $animal = new Animal();
            $animal->name = $name;
            $animal->gender = $gender;
            $animal->age = $age;
            $animal->type = $type;
            $animal->from = substr($type,0,1);
            $animal->master_id = $this->usr_id;
            $animal->save();

            $circle = new Circle();
            $circle->aid = $animal->aid;
            $circle->usr_id = $this->usr_id;
            $circle->rank = 0;
            $circle->save();
            
            $f = new Follow();
            $f->usr_id = $this->usr_id;
            $f->aid = $animal->aid;
            $f->create_time = time();
            $f->save();

            Yii::app()->db->createCommand('UPDATE dc_user SET aid=:aid WHERE usr_id=:usr_id')->bindValues(array(':aid'=>$animal->aid, ':usr_id'=>$this->usr_id))->execute();

            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }

        $this->echoJsonData(array('aid'=>$animal->aid));
    }

    public function actionJoinMobileApi($aid, $SID='')
    {
        if ($SID!='') {
            $session = Yii::app()->session;
            $this->usr_id = $session['usr_id'];
        } 

        if (!isset($this->usr_id)) {
            if ($to=='') {
                if (isset($_SERVER['HTTP_USER_AGENT'])&&strpos($_SERVER['HTTP_USER_AGENT'], "MicroMessenger")) {
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
                    $oauth2->get_code_by_authorize(serialize(array('aid'=>$aid)));
                    exit;
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
                    $this->redirect($oauth2->getAuthorizeURL(WB_CALLBACK_URL, 'code', http_build_query(array('aid'=>$aid)), 'mobile'));
                    exit;
                }
                break;
            default:
                # code...
                break;
            }
        }
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $user = User::model()->findByPk($this->usr_id);
            
            $circle = new Circle();
            $circle->aid = $aid;
            $circle->usr_id = $this->usr_id;
            $circle->save();

            $f = Follow::model()->findByPk(array(
                'usr_id' => $this->usr_id,
                'aid' => $aid,
            ));
            if (!isset($f)) {
                $f = new Follow;
                $f->usr_id = $this->usr_id;
                $f->aid = $aid;
                $f->create_time = time();
                $f->save();
            }
            $news = new News;
            $news->aid = $aid;
            $news->type = 2;
            $news->create_time = time();
            $news->content = serialize(array(
                'usr_id'=>$user->usr_id,
                'u_name'=>$user->name,
            ));
            $news->save();

            $max_users = Yii::app()->db->createCommand('SELECT COUNT(aid) FROM dc_animal')->queryScalar();
            $t_rq = Yii::app()->db->createCommand('SELECT t_rq FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();
            $rank = Yii::app()->db->createCommand('SELECT COUNT(aid) FROM dc_animal WHERE t_rq<=:t_rq')->bindValue(':t_rq', $t_rq)->queryScalar();
            $percent = floor($rank*100/$max_users);

            $a = Animal::model()->findByPk($aid);
            Talk::model()->sendMsg(NPC_SYSTEM_USRID, $a->master_id, "路人".$user->name."被".$a->name."的魅力折服，成为了TA的粉丝哟～");
            $easemob = Yii::app()->easemob;
            $easemob->sendToUsers($a->master_id, NPC_SYSTEM_USRID, array(
                'mixed'=>TRUE,
                'msg'=>"路人".$user->name."被".$a->name."的魅力折服，成为了TA的粉丝哟～",
                'ext'=>array(
                    'nickname'=>'事务官',
                ),
            ));

            $transaction->commit();

            $this->redirect(array('animal/infoShare', 'aid'=>$aid, 'SID'=>$SID, 'percent'=>$percent));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionJoinApi($aid)
    {
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $n = $this->countCircle($this->usr_id);
            $user = User::model()->findByPk($this->usr_id);
            if ($n>10) {
                if ($n<=20) {
                    $g = $n*5;
                } else {
                    $g = 100;
                }
                if ($user->gold<$n) {
                    throw new PException('亲，您的金币不足');
                }
                $user->gold-=$g;
                $user->saveAttributes(array('gold'));
            }
            $circle = new Circle();
            $circle->aid = $aid;
            $circle->usr_id = $this->usr_id;
            $circle->save();

            $f = Follow::model()->findByPk(array(
                'usr_id' => $this->usr_id,
                'aid' => $aid,
            ));
            if (!isset($f)) {
                $f = new Follow;
                $f->usr_id = $this->usr_id;
                $f->aid = $aid;
                $f->create_time = time();
                $f->save();
            }
            $news = new News;
            $news->aid = $aid;
            $news->type = 2;
            $news->create_time = time();
            $news->content = serialize(array(
                'usr_id'=>$user->usr_id,
                'u_name'=>$user->name,
            ));
            $news->save();

            $max_users = Yii::app()->db->createCommand('SELECT COUNT(aid) FROM dc_animal')->queryScalar();
            $t_rq = Yii::app()->db->createCommand('SELECT t_rq FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();
            $rank = Yii::app()->db->createCommand('SELECT COUNT(aid) FROM dc_animal WHERE t_rq<=:t_rq')->bindValue(':t_rq', $t_rq)->queryScalar();
            $percent = floor($rank*100/$max_users);

            $a = Animal::model()->findByPk($aid);
            Talk::model()->sendMsg(NPC_SYSTEM_USRID, $a->master_id, "路人".$user->name."被".$a->name."的魅力折服，成为了TA的粉丝哟～");
            $easemob = Yii::app()->easemob;
            $easemob->sendToUsers($a->master_id, NPC_SYSTEM_USRID, array(
                'mixed'=>TRUE,
                'msg'=>"路人".$user->name."被".$a->name."的魅力折服，成为了TA的粉丝哟～",
                'ext'=>array(
                    'nickname'=>'事务官',
                ),
            ));

            $transaction->commit();

            $this->echoJsonData(array('isSuccess'=>TRUE, 'percent'=>$percent));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    private function countCircle($usr_id)
    {
        $n = Yii::app()->db->createCommand('SELECT COUNT(*) FROM dc_circle WHERE usr_id=:usr_id')->bindValue(':usr_id', $usr_id)->queryScalar();

        return $n;
    }

    public function actionExitApi($aid)
    {
        $rtn = Yii::app()->db->createCommand('SELECT COUNT(aid) FROM dc_circle WHERE usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryScalar();
        $default_aid = Yii::app()->db->createCommand('SELECT aid FROM dc_user WHERE usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryScalar();
        if ($aid==$default_aid) {
            throw new PException('您不能退出默认联萌');
        }
        if ($rtn<=1) {
            throw new PException('这是您唯一的联萌！');
        } else {
            $transaction = Yii::app()->db->beginTransaction();
            try {
                $circle = Circle::model()->findByPk(array(
                    'aid' => $aid,
                    'usr_id' => $this->usr_id,
                ));
                $circle->delete();
                $transaction->commit();

                $this->echoJsonData(array('isSuccess'=>true));
            } catch (Exception $e) {
                $transaction->rollback();
                throw $e;
            }
        }
    }

    public function actionIsVoicedApi($aid)
    {
        $session = Yii::app()->session;
        if (isset($session[$aid.'_is_voiced'])) {
            $this->echoJsonData(array('is_voiced'=>TRUE));
        } else {
            $this->echoJsonData(array('is_voiced'=>FALSE));
        }

    }
    public function actionVoiceUpApi($aid)
    {
        if (isset($_FILES['voice'])) {
            $fname = basename($_FILES['voice']['name']);
            $rtn = Yii::app()->oss->upload_file(OSS_PREFIX.'4voices', 'ani/voice_'.date('y-m-d').'_'.$aid, fopen($_FILES['voice']['tmp_name'],'r'), $_FILES['voice']['size']); 
            if ($rtn) {
                $transaction = Yii::app()->db->beginTransaction();
                try {
                    $user = User::model()->findByPk($this->usr_id);
                    $ex_gold = $user->gold;
                    $ex_exp = $user->exp;
                    $ex_lv = $user->lv;
                    $user->voiceUp();

                    $news = new News;
                    $news->aid = $aid;
                    $news->type = 5;
                    $news->create_time = time();
                    $news->save();
                    $transaction->commit();

                    $session = Yii::app()->session;
                    $session[$aid.'_is_voiced'] = 1;
                    $this->echoJsonData(array('exp'=>$user->exp-$ex_exp, 'gold'=>$user->gold-$ex_gold, 'lv'=>$user->lv-$ex_lv));
                } catch (Exception $e) {
                    $transaction->rollback();
                    throw $e;
                }
            } else {
                throw new PException('上传失败'); 
            }
        } else {
            throw new PException('音频文件不存在'); 
        }


    }

    public function actionVoiceDownApi($aid)
    {
        $obj = Yii::app()->oss->get_obj(OSS_PREFIX.'4voices', 'ani/voice_'.date('y-m-d').'_'.$aid);
        if ($obj) {
            $this->echoJsonData(array('url'=>'voice_'.date('y-m-d').'_'.$aid));
        } else {
            $this->echoJsonData(array('url'=>''));
        }
    }

    public function actionIsTouchedApi($aid)
    {
        $session = Yii::app()->session;
        $img_url = Yii::app()->db->createCommand('SELECT url FROM dc_image WHERE aid=:aid ORDER BY update_time DESC LIMIT 1')->bindValue(':aid',$aid)->queryScalar();
        if (isset($session[$aid.'touch_count'])) {
            $is_touched = TRUE;
        } else {
            $is_touched = FALSE;
        }
        $this->echoJsonData(array(
            'is_touched'=>$is_touched,
            'img_url'=>$img_url,
        ));
    }

    public function actionTouchApi($aid)
    {
        $session = Yii::app()->session;
        $transaction = Yii::app()->db->beginTransaction();
        try {
            $user = User::model()->findByPk($this->usr_id);
            $ex_gold = $user->gold;
            $ex_exp = $user->exp;
            $ex_lv = $user->lv;
            if (!isset($session[$aid.'touch_count'])) {
                $user->touch();
                $session[$aid.'touch_count']=1;
            }
            $transaction->commit();

            $this->echoJsonData(array(
                'gold' => $user->gold-$ex_gold,
                'exp' => $user->exp-$ex_exp,
                'lv' => $user->lv-$ex_lv,
            )); 
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionShakeApi($aid, $is_shake=0)
    {
        $session = Yii::app()->session;
        if (!isset($session[$aid.'_shake_count'])) {
            if ($is_shake) {
                $session[$aid.'_shake_count']=2;
            } else {
                $session[$aid.'_shake_count']=3;
            }
        } else if ($is_shake) {
            $session[$aid.'_shake_count']-=1;
        }
        $this->echoJsonData(array('shake_count'=>$session[$aid.'_shake_count'])); 
    }

    public function actionSendGiftApi($item_id, $aid, $img_id=NULL, $is_shake=FALSE)
    {
        $itemList = Util::loadConfig('items');
        $item = $itemList[$item_id];
        if (isset($item)) {
            $transaction = Yii::app()->db->beginTransaction();
            try {
                $user = User::model()->findByPk($this->usr_id);
                $ex_gold = $user->gold;
                $ex_exp = $user->exp;
                $ex_lv = $user->lv;
                if (!$is_shake) {
                    $items = unserialize($user->items);
                    if (isset($items[$item_id])&&$items[$item_id]>0) {
                        $items[$item_id]--;
                        $user->items = serialize($items);
                        $user->saveAttributes(array('items'));
                    } else {
                        throw new PException('礼物数量不足');
                    }
                }
                $user->sendGift($is_shake);

                $circle = Circle::model()->findByPk(array('aid'=>$aid,'usr_id'=>$this->usr_id));
                if (isset($circle)) {
                    $ex_rank = $circle->rank;
                    $circle->t_contri+=$item['rq'];
                    $circle->m_contri+=$item['rq'];
                    $circle->w_contri+=$item['rq'];
                    $circle->d_contri+=$item['rq'];

                    $user->contributionChange($circle);

                    $circle->saveAttributes(array('t_contri','m_contri','w_contri','d_contri'));
                }

                $animal = Animal::model()->findByPk($aid);
                $animal->d_rq+=$item['rq'];
                $animal->m_rq+=$item['rq'];
                $animal->w_rq+=$item['rq'];
                $animal->t_rq+=$item['rq'];
                if ($animal->d_rq<0) {
                    $animal->d_rq = 0;
                }
                if ($animal->m_rq<0) {
                    $animal->m_rq = 0;
                }
                if ($animal->w_rq<0) {
                    $animal->w_rq = 0;
                }
                if ($animal->t_rq<0) {
                    $animal->t_rq = 0;
                }
                $a_items = unserialize($animal->items);
                if (isset($a_items[$item_id])) {
                    $a_items[$item_id]++;
                } else {
                    $a_items[$item_id] = 1;
                }
                $animal->items = serialize($a_items);
                $animal->saveAttributes(array('d_rq', 'm_rq', 'w_rq', 't_rq', 'items'));

                if (isset($img_id)) {
                    $image = Image::model()->findByPk($img_id);
                    $image->gifts++;
                    if (isset($image->senders)&&$image->senders!='') {
                        $image->senders = $this->usr_id.','.$image->senders;
                    } else {
                        $image->senders = $this->usr_id;
                    }
                    $image->saveAttributes(array('gifts', 'senders'));
                }

                $session = Yii::app()->session;
                if ($is_shake) {
                    $session[$aid.'_shake_count']=0;
                } else {
                    if (isset($session[$aid.'_gift_count'])) {
                        $session[$aid.'_gift_count']+=1;
                    } else {
                        $session[$aid.'_gift_count']=1;
                    }
                }

                $news = new News;
                $news->aid = $aid;
                $news->type = $item['rq']>=0?4:7;
                $news->create_time = time();
                $news->content = serialize(array(
                    'usr_id'=>$user->usr_id,
                    'u_name'=>$user->name,
                    'rank' => isset($circle)?$circle->rank:-1,
                    'item_id'=>$item_id,
                    'rq'=>$item['rq'],
                ));
                $news->save();

                $transaction->commit();

                $this->echoJsonData(array('exp'=>$user->exp-$ex_exp, 'gold'=>$user->gold-$ex_gold, 'lv'=>$user->lv-$ex_lv, 'rank'=>isset($circle)?$circle->rank-$ex_rank:-1));
            } catch (Exception $e) {
                $transaction->rollback();
                throw $e;
            }
        } else {
            throw new PException('礼物不存在');
        }
    }

    public function actionShakeShareApi($aid)
    {
        $session = Yii::app()->session;
        if (!isset($session[$aid.'_shake_shared'])) {
            $session[$aid.'_shake_count']=3;
            $session[$aid.'_shake_shared']=1;
        }

        $this->echoJsonData(array('shake_count'=>$session[$aid.'_shake_count'])); 
    }

    public function actionCardApi($aid)
    {
        $images = Yii::app()->db->createCommand('SELECT img_id, url FROM dc_image WHERE aid=:aid ORDER BY update_time LIMIT 4')->bindValue(':aid', $aid)->queryAll();

        $master_id = Yii::app()->db->createCommand('SELECT master_id FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();
        $master = Yii::app()->db->createCommand('SELECT usr_id, name, tx, gender, city FROM dc_user WHERE usr_id=:usr_id')->bindValue(':usr_id', $master_id)->queryRow();

        $this->echoJsonData(array(
            'master' => $master,
            'images' => $images,
        ));
    }

    public function actionRecommendApi($type=NULL, $page=0, $from=0)
    {
        if (isset($type)) {
            $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.gender, a.from, a.type, a.age, a.t_rq, (SELECT COUNT(*) FROM dc_circle c WHERE c.aid=a.aid) AS fans FROM dc_animal a WHERE type=:type ORDER BY a.t_rq DESC LIMIT :m,30')->bindValues(array(
                ':type'=>$type,
                ':m'=>30*$page,
            ))->queryAll();
        } else if ($from!=0) {
            $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.gender, a.from, a.type, a.age,  a.t_rq, (SELECT COUNT(*) FROM dc_circle c WHERE c.aid=a.aid) AS fans FROM dc_animal a WHERE `from`=:from ORDER BY a.t_rq DESC LIMIT :m,30')->bindValues(array(
                ':m'=>30*$page,
                ':from'=>$from,
            ))->queryAll();
        } else {
            $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.gender, a.from, a.type, a.age,  a.t_rq, (SELECT COUNT(*) FROM dc_circle c WHERE c.aid=a.aid) AS fans FROM dc_animal a ORDER BY a.t_rq DESC LIMIT :m,30')->bindValues(array(
                ':m'=>30*$page,
            ))->queryAll();
        }
        
        $this->echoJsonData(array($r));
    }

    public function actionPopularApi($type=NULL, $page=0, $from=0)
    {
        if (isset($type)) {
            $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.gender, a.from, a.type, a.age, a.t_rq, (SELECT COUNT(*) FROM dc_circle c WHERE c.aid=a.aid) AS fans FROM dc_animal a WHERE type=:type ORDER BY a.t_rq DESC LIMIT :m,30')->bindValues(array(
                ':type'=>$type,
                ':m'=>30*$page,
            ))->queryAll();
        } else if ($from!=0) {
            $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.gender, a.from, a.type, a.age,  a.t_rq, (SELECT COUNT(*) FROM dc_circle c WHERE c.aid=a.aid) AS fans FROM dc_animal a WHERE `from`=:from ORDER BY a.t_rq DESC LIMIT :m,30')->bindValues(array(
                ':m'=>30*$page,
                ':from'=>$from,
            ))->queryAll();
        } else {
            $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.gender, a.from, a.type, a.age,  a.t_rq, (SELECT COUNT(*) FROM dc_circle c WHERE c.aid=a.aid) AS fans FROM dc_animal a ORDER BY a.t_rq DESC LIMIT :m,30')->bindValues(array(
                ':m'=>30*$page,
            ))->queryAll();
        }
        
        $this->echoJsonData(array($r));
    }

    public function actionSearchApi($name, $aid=0)
    {
        $r = Yii::app()->db->createCommand("SELECT a.aid, a.name, a.tx, a.gender, a.from, a.type, a.age, a.t_rq, (SELECT COUNT(*) FROM dc_circle c WHERE c.aid=a.aid) AS fans FROM dc_animal a WHERE a.aid>:aid AND name LIKE '%$name%' ORDER BY a.aid ASC LIMIT 30")->bindValues(array(
            ':aid'=>$aid,
        ))->queryAll();

        $this->echoJsonData(array($r));
    }

    public function actionOthersApi($aids, $aid=NULL)
    {
        $tmp_ids = explode(',',$aids);
        if (isset($aid)) {
            foreach ($tmp_ids as $k=>$tmp_id) {
                if ($tmp_id!=$aid) {
                    unset($tmp_ids[$k]);
                } else {
                    break;
                }
            }
        }
        for ($i = 0; $i < 30 && isset($tmp_ids[$i]); $i++) {
            $search_ids[] = $tmp_ids[$i];
        }
        $search_ids = implode(',', $search_ids);

        $r = Yii::app()->db->createCommand('SELECT aid, name, tx, age, gender, (SELECT COUNT(*) FROM dc_follow f WHERE f.aid=a.aid AND usr_id=:usr_id) AS is_follow FROM dc_animal a WHERE aid IN (:aids)')->bindValues(array(
            ':usr_id'=>$this->usr_id,
            ':aids'=>$search_ids,
        ))->queryAll();

        $this->echoJsonData($r);
    }

    public function actionAddressApi($aid)
    {
        $animal = Animal::model()->findByPk($aid);
        if (isset($_POST['name'])&&$animal->master_id==$this->usr_id) {
            $transaction = Yii::app()->db->beginTransaction();
            try {
                $address = array(
                    'name'=>$_POST['name'],
                    'telephone'=>$_POST['telephone'],
                    'zipcode'=>$_POST['zipcode'],
                    'region'=>$_POST['region'],
                    'building'=>$_POST['building'],
                );
                $animal->address = serialize($address);
                $animal->saveAttributes(array('address'));
                $transaction->commit();
            } catch (Exception $e) {
                $transaction->rollback();
                throw $e;
            }

            $this->echoJsonData(array(unserialize($animal->address)));
        } else if (isset($_POST['name'])) {
            throw new PException('您没有创建宠物，无权修改此信息');
        }  else {
            $this->echoJsonData(array(''));
        }
    }

    public function actionModifyInfoApi($aid, $name, $gender, $age, $type)
    {
        $pattern = '/^[a-zA-Z0-9\x{30A0}-\x{30FF}\x{3040}-\x{309F}\x{4E00}-\x{9FBF}]+$/u';
        //$namelen = (strlen($name)+mb_strlen($name,"UTF8"))/2;
        $namelen = mb_strlen($name,"UTF8");
        if ($namelen>8) {
            throw new PException('宠物昵称超过最大长度');
        }
        if (!preg_match($pattern, $name)) {
            throw new PException('宠物昵称含有特殊字符');
        }

        $transaction = Yii::app()->db->beginTransaction();
        try {
            $animal = Animal::model()->findByPk($aid);
            $animal->name = $name;
            $animal->age = $age;
            $animal->type = $type;
            $animal->gender = $gender;
            $animal->saveAttributes(array('name', 'age', 'type', 'gender'));
            $transaction->commit();

            $this->echoJsonData(array('isSuccess'=>true));
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }
    }

    public function actionFoodApi($aid)
    {
        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.food, i.cmt, i.create_time FROM dc_image i WHERE i.aid=:aid AND is_food=1 AND i.create_time>=:create_time')->bindValues(array(':aid'=>$aid, ':create_time'=> time()-(60*60*24)))->queryRow();

        $this->echoJsonData(array($r));
    }

    public function actionMineApi()
    {
        $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.msg, a.t_rq, a.master_id, u.name AS u_name, u.tx AS u_tx, c.rank, c.t_contri FROM dc_circle c INNER JOIN dc_animal a ON c.aid=a.aid INNER JOIN dc_user u ON a.master_id=u.usr_id WHERE c.usr_id=:usr_id ORDER BY c.t_contri DESC')->bindValue(':usr_id', $this->usr_id)->queryAll();
        $code = Yii::app()->db->createCommand('SELECT code FROM dc_user WHERE usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryScalar();

        $session = Yii::app()->session;
        $max_users = Yii::app()->db->createCommand('SELECT COUNT(aid) FROM dc_animal')->queryScalar();
        foreach ($r as $k=>$v) {
            $aid = $v['aid'];
            $r[$k]['images'] = Yii::app()->db->createCommand('SELECT img_id, url FROM dc_image WHERE aid=:aid ORDER BY update_time DESC LIMIT 4')->bindValue(':aid', $aid)->queryAll();
            $rank = Yii::app()->db->createCommand('SELECT COUNT(aid) FROM dc_animal WHERE t_rq<=:t_rq')->bindValue(':t_rq', $v['t_rq'])->queryScalar();
            $r[$k]['percent'] = floor($rank*100/$max_users);
            $r[$k]['shake_count'] = $session[$aid.'_shake_count'];
            $r[$k]['gift_count'] = $session[$aid.'_gift_count'];
            $r[$k]['is_touched'] = $session[$aid.'touch_count'];
            $r[$k]['is_voiced'] = $session[$aid.'_is_voiced'];
            $r[$k]['invite_code'] = $code.'@'.dechex($aid);
        }        

        $this->echoJsonData(array($r));
    }

    public function actionModifyMsgApi($aid)
    {
        $is_master = Yii::app()->db->createCommand('SELECT aid FROM dc_animal WHERE aid=:aid AND master_id=:usr_id')->bindValues(array(
            ':aid' => $aid,
            ':usr_id' => $this->usr_id,
        ))->queryScalar();
        if (!$is_master) {
            throw new PException('不是经纪人，无权更改!');
        }

        $msg = $_POST['msg'];
        $msglen = mb_strlen($msg,"UTF8");
        if ($msglen>20) {
            throw new PException('文本超过规定长度');
        }
        $transaction = Yii::app()->db->beginTransaction();
        try {
            Yii::app()->db->createCommand('UPDATE dc_animal SET msg=:msg WHERE aid=:aid')->bindValues(array(
                ':msg'=>$msg,
                ':aid'=>$aid,
            ))->execute();
            $transaction->commit();
        } catch (Exception $e) {
            $transaction->rollback();
            throw $e;
        }

        $this->echoJsonData(array('isSuccess'=>true));
    }

    public function actionFoodListApi($aid, $page=0)
    {
        $r = Yii::app()->db->createCommand('SELECT img_id, url, cmt, food, create_time FROM dc_image WHERE aid=:aid AND is_food=1 ORDER BY create_time DESC LIMIT :m, 30')->bindValues(array(
            ':aid'=>$aid,
            ':m'=>30*$page,
        ))->queryAll();

        $this->echoJsonData(array($r));
    }

    public function actionInfoShare($aid, $SID='', $percent=-1)
    {
        if ($SID!='') {
            $session = Yii::app()->session;
            $this->usr_id = $session['usr_id'];
        } 
        if (isset($this->usr_id)) {
            $is_circle = Yii::app()->db->createCommand('SELECT aid FROM dc_circle WHERE aid=:aid AND usr_id=:usr_id')->bindValues(array(':aid'=>$aid, ':usr_id'=>$this->usr_id))->queryScalar();
        } else {
            $is_circle = 0;
        }
        $r = Yii::app()->db->createCommand('SELECT a.aid, a.name, a.tx, a.gender, a.from, a.type, a.age, a.master_id, a.t_rq, a.msg, u.name AS u_name, u.tx AS u_tx, c.rank AS u_rank, (SELECT COUNT(nid) FROM dc_news n WHERE n.aid=a.aid) AS news, (SELECT COUNT(*) FROM dc_circle c WHERE c.aid=a.aid) AS fans, (SELECT COUNT(i.img_id) FROM dc_image i WHERE i.aid=a.aid) AS images, a.total_food, (SELECT COUNT(*) FROM dc_follow f WHERE f.aid=a.aid) AS followers FROM dc_animal a JOIN dc_user u ON a.master_id=u.usr_id LEFT JOIN dc_circle c ON a.aid=c.aid AND a.master_id=c.usr_id WHERE a.aid=:aid')->bindValue(':aid', $aid)->queryRow();
        $images = Yii::app()->db->createCommand('SELECT img_id, url FROM dc_image WHERE aid=:aid')->bindValue(':aid', $aid)->queryAll();
        $pet_type = Util::loadConfig('pet_type');
        $n = floor($r['type']/100);
        if (isset($pet_type[$n][$r['type']])) {
            $a_type = $pet_type[$n][$r['type']];
        } else {
            switch ($n) {
                case 1:
                    $a_type = '喵星人';
                    break;
                case 2:
                    $a_type = '汪星人';
                    break;
                
                default:
                    $a_type = '其他星人';
                    break;
            }
        }
        $this->renderPartial('info', array('r'=>$r, 'is_circle'=>$is_circle, 'a_type'=>$a_type, 'images'=>$images, 'percent'=>$percent, 'SID'=>$SID));
    }
}
