<?php

class SocialController extends Controller
{
    public function filters()
    {
        return array(
            // array(
            //     'COutputCache + articles',
            //     'duration' => 300,
            //     'varyByParam' => array('page'),
            //     'dependency' => array(
            //         'class' => 'CDbCacheDependency',
            //         'sql' => "SELECT MAX(update_time) FROM dc_article",
            //     ),
            // ),
        );
    }

    // public function actionFoodShareApi($img_id, $alert_flag=0, $to='', $SID='')
    // {
    //     $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.aid, i.cmt, i.likes, i.likers, i.gifts, i.senders, i.shares, i.sharers, i.comments, i.food, i.is_food=1 AS is_food, i.create_time, a.name, a.tx, a.type, a.gender, u.usr_id, u.tx AS u_tx, u.name AS u_name  FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE i.img_id=:img_id')->bindValue(':img_id', $img_id)->queryRow();
        
    //     $pet_type = Util::loadConfig('pet_type');
    //     $n = floor($r['type']/100);
    //     if (isset($pet_type[$n][$r['type']])) {
    //         $a_type = $pet_type[$n][$r['type']];
    //     } else {
    //         switch ($n) {
    //             case 1:
    //                 $a_type = '喵星人';
    //                 break;
    //             case 2:
    //                 $a_type = '汪星人';
    //                 break;
                
    //             default:
    //                 $a_type = '其他星人';
    //                 break;
    //         }
    //     }        
        
    //     if (isset($r['likers'])&&$r['likers']!='') {
    //         $liker_tx = Yii::app()->db->createCommand("SELECT usr_id, name, tx FROM dc_user WHERE usr_id IN (:likers)")->bindValue(':likers', $r['likers'])->queryColumn();
    //     }
        
    //     if (isset($r['senders'])&&$r['senders']!='') {
    //         $sender_tx = Yii::app()->db->createCommand("SELECT usr_id, name, tx FROM dc_user WHERE usr_id IN (:senders)")->bindValue(':senders', $r['senders'])->queryColumn();
    //     }

    //     if (isset($r['sharers'])&&$r['sharers']!='') {
    //         $sharer_tx = Yii::app()->db->createCommand("SELECT usr_id, name, tx FROM dc_user WHERE usr_id IN (:sharers)")->bindValue(':sharers', $r['sharers'])->queryColumn();
    //     }

    //     if (isset($r['comments'])&&$r['comments']!='') {
    //         $c = explode(';', $r['comments']);
    //         $coment_count = count($c); 
    //         foreach ($c as $k1=>$c1) {
    //             $c2 = explode(',', $c1);
    //             foreach ($c2 as $c3) {
    //                 $c4 = explode(':', $c3);
    //                 // $comments[$k1][$c4[0]]=$c4[1];
    //             }
    //         }
    //         //$r['comments'] = $comments;
    //     }

    //     $this->renderPartial('food', array('r'=>$r, /*'comment_count'=>$comment_count, 'liker_tx'=>$liker_tx, 'sender_tx'=>$sender_tx, 'sharer_tx'=>$sharer_tx,*/ 'a_type'=>$a_type, 'img_id'=>$img_id, 'alert_flag'=>$alert_flag, 'aid'=>$r['aid'], 'to'=>$to, 'sid'=>$SID));


    // }

   public function actionFoodShareApi($img_id, $alert_flag=0, $to='', $SID='')
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
                    $a = implode('$', array('img_id',$img_id));
                    $state = $a;
                    $oauth2->get_code_by_authorize($state);
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
                    $this->redirect($oauth2->getAuthorizeURL(WB_CALLBACK_URL, 'code', http_build_query(array('img_id'=>$img_id)), 'mobile'));
                    exit;
                }
                break;
            default:
                # code...
                break;
            }
        }

        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.aid, i.cmt, i.topic_name, i.star_id, i.stars, i.likes, i.likers, i.gifts, i.senders, i.shares, i.sharers, i.comments, i.food, i.is_food=1 AS is_food, i.create_time, a.name, a.tx, a.type, a.gender, u.usr_id, u.tx AS u_tx, u.name AS u_name  FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE i.img_id=:img_id')->bindValue(':img_id', $img_id)->queryRow();
        if ($r['star_id']) {
            $start_time = Yii::app()->db->createCommand('SELECT start_time FROM dc_star WHERE star_id=:star_id')->bindValue(':star_id', $r['star_id'])->queryScalar();
        } else {
            $start_time = 0;
        }
        
        if (!isset($session[$this->usr_id.'_star_'.$r['star_id']])) {
            $session[$this->usr_id.'_star_'.$r['star_id']] = 3;
        }
        $votes = $session[$this->usr_id.'_star_'.$r['star_id']];

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
        
        $is_liked = 0;
        $liker_tx = array();
        if (isset($r['likers'])&&$r['likers']!='') {
            $liker_array = explode(',', $r['likers']);
            if(isset($this->usr_id)) $is_liked = in_array($this->usr_id, $liker_array);
            $likers = $r['likers'];
            $liker_tx = Yii::app()->db->createCommand("SELECT usr_id, name, tx FROM dc_user WHERE usr_id IN ($likers)")->queryAll();
        }

        if (isset($r['comments'])&&$r['comments']!='') {
            $c = explode(';', $r['comments']);
            $comment_count = count($c); 
            foreach ($c as $k1=>$c1) {
                $c2 = explode(',', $c1);
                foreach ($c2 as $c3) {
                    $c4 = explode(':', $c3);
                    if(isset($c4[1])) $comments[$k1][$c4[0]]=$c4[1];
                }
            }
            $r['comments'] = $comments;
            $r['comment_count'] = $comment_count-1;
        } else {
            $r['comment_count'] = 0;
        }

        $this->renderPartial('food_new', array('r'=>$r, 'votes'=>$votes, 'start_time'=>$start_time, 'is_liked'=>$is_liked, 'liker_tx'=>$liker_tx, 'a_type'=>$a_type, 'img_id'=>$img_id, 'alert_flag'=>$alert_flag, 'aid'=>$r['aid'], 'to'=>$to, 'SID'=>$SID));

    }

    public function actionFood($img_id, $alert_flag=0, $to='', $SID='')
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
                    $a = implode('$', array('img_id',$img_id));
                    $state = $a;
                    $oauth2->get_code_by_authorize($state);
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
                    $this->redirect($oauth2->getAuthorizeURL(WB_CALLBACK_URL, 'code', http_build_query(array('img_id'=>$img_id)), 'mobile'));
                    exit;
                }
                break;
            default:
                # code...
                break;
            }
        }

        $r = Yii::app()->db->createCommand('SELECT i.img_id, i.url, i.aid, i.cmt, i.topic_name, i.star_id, i.stars, i.likes, i.likers, i.gifts, i.senders, i.shares, i.sharers, i.comments, i.food, i.is_food=1 AS is_food, i.create_time, a.name, a.tx, a.type, a.gender, u.usr_id, u.tx AS u_tx, u.name AS u_name  FROM dc_image i LEFT JOIN dc_animal a ON i.aid=a.aid LEFT JOIN dc_user u ON a.master_id=u.usr_id WHERE i.img_id=:img_id')->bindValue(':img_id', $img_id)->queryRow();
        if ($r['star_id']) {
            $start_time = Yii::app()->db->createCommand('SELECT start_time FROM dc_star WHERE star_id=:star_id')->bindValue(':star_id', $r['star_id'])->queryScalar();
        } else {
            $start_time = 0;
        }
        
        if (!isset($session[$this->usr_id.'_star_'.$r['star_id']])) {
            $session[$this->usr_id.'_star_'.$r['star_id']] = 3;
        }
        $votes = $session[$this->usr_id.'_star_'.$r['star_id']];

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
        
        $is_liked = 0;
        $liker_tx = array();
        if (isset($r['likers'])&&$r['likers']!='') {
            $liker_array = explode(',', $r['likers']);
            if(isset($this->usr_id)) $is_liked = in_array($this->usr_id, $liker_array);
            $likers = $r['likers'];
            $liker_tx = Yii::app()->db->createCommand("SELECT usr_id, name, tx FROM dc_user WHERE usr_id IN ($likers)")->queryAll();
        }

        if (isset($r['comments'])&&$r['comments']!='') {
            $c = explode(';', $r['comments']);
            $comment_count = count($c); 
            foreach ($c as $k1=>$c1) {
                $c2 = explode(',', $c1);
                foreach ($c2 as $c3) {
                    $c4 = explode(':', $c3);
                    if(isset($c4[1])) $comments[$k1][$c4[0]]=$c4[1];
                }
            }
            $r['comments'] = $comments;
            $r['comment_count'] = $comment_count-1;
        } else {
            $r['comment_count'] = 0;
        }

        $this->renderPartial('food_new', array('r'=>$r, 'votes'=>$votes, 'start_time'=>$start_time, 'is_liked'=>$is_liked, 'liker_tx'=>$liker_tx, 'a_type'=>$a_type, 'img_id'=>$img_id, 'alert_flag'=>$alert_flag, 'aid'=>$r['aid'], 'to'=>$to, 'SID'=>$SID));

    }
    
    public function actionNewYearEvent($SID='')
    {

        $users = Yii::app()->db->createCommand('SELECT COUNT(usr_id) FROM dc_user')->queryScalar();
       
        $this->renderPartial('activity_index', array('users'=>$users, 'sid'=>$SID));
    }

    public function actionActivityview($aid, $alert_flag=0, $SID='')
    {
        $users = Yii::app()->db->createCommand('SELECT COUNT(usr_id) FROM dc_user')->queryScalar();
        $food = Yii::app()->db->createCommand('SELECT food FROM dc_animal WHERE aid=:aid')->bindValue(':aid', $aid)->queryScalar();
        
        $this->renderPartial('activity_view_'.$aid, array('aid'=>$aid, 'users'=>$users, 'alert_flag'=>$alert_flag, 'food'=>$food, 'sid'=>$SID));
    }

    public function actionRation($aid, $SID='')
    {
        $r = Yii::app()->db->createCommand('SELECT img_id, url, cmt, food, create_time FROM dc_image WHERE aid=:aid AND is_food=1 ORDER BY create_time DESC')->bindValues(array(
            ':aid'=>$aid,
        ))->queryAll();

        $this->renderPartial('ration', array('aid'=>$aid, 'r'=>$r, 'SID'=>$SID));
    }

    public function actionTouch($aid, $SID='')
    {
        $img_url = Yii::app()->db->createCommand('SELECT url FROM dc_image WHERE aid=:aid ORDER BY update_time DESC LIMIT 1')->bindValue(':aid', $aid)->queryScalar();
        $session = Yii::app()->session;
        if (isset($session[$aid.'touch_count'])) {
            $chance_times = $session[$aid.'touch_count'];
        } else {
            $chance_times = 0;
        }
        
        $r = Yii::app()->db->createCommand('SELECT aid, name, tx FROM dc_animal WHERE aid=:aid')->bindValue(":aid", $aid)->queryRow();

        $this->renderPartial('touch', array('img_url'=>$img_url, 'r'=>$r, 'chance_times'=>$chance_times, 'SID'=>$SID));
    }

    public function actionShake($aid, $SID='')
    {
        $r = Yii::app()->db->createCommand('SELECT aid, name, tx FROM dc_animal WHERE aid=:aid')->bindValue(":aid", $aid)->queryRow();
        $session = Yii::app()->session;
        $chance_times = $session[$aid.'_shake_count'];
        $this->renderPartial('shake', array('r'=>$r, 'chance_times'=>$chance_times, 'SID'=>$SID));
    }

    public function actionGift($aid, $SID='')
    {
        $r = Yii::app()->db->createCommand('SELECT aid, name FROM dc_animal WHERE aid=:aid')->bindValue(":aid", $aid)->queryRow();
        
        $this->renderPartial('gift', array('r'=>$r, 'SID'=>$SID));
    }

    public function actionRankPromotion()
    {
        $this->renderPartial('rank-promotion');
    }

    public function actionDivine()
    {
        $this->renderPartial('divine');
    }

    public function actionPayActivity($SID='')
    {
        $this->renderPartial('pay_active', array('SID'=>$SID));
    }

    public function actionArticles($page=0)
    {
        $articles = Yii::app()->db->createCommand("SELECT * FROM dc_article WHERE image='' ORDER BY create_time DESC LIMIT :m, 10")->bindValue(':m', $page*10)->queryAll();
        if ($page==0) {
            $banner = Yii::app()->db->createCommand("SELECT * FROM dc_article WHERE image!='' ORDER BY create_time DESC LIMIT 1")->queryRow();
        } else {
            $banner = array();
        }

        $this->echoJsonData(array('banner'=>$banner, 'articles'=>$articles));
    }

    public function actionVote($alert_flag=0, $SID='')
    {
        $users = Yii::app()->db->createCommand('SELECT SUM(food) FROM dc_animal WHERE aid IN (1650,1653,1655,1652,1656,1651)')->queryScalar();
        $animals = Yii::app()->db->createCommand('SELECT aid, food FROM dc_animal WHERE aid IN (1650,1653,1655,1652,1656,1651)')->queryAll();
        foreach ($animals as $v) {
            $a[$v['aid']] = $v['food'];
        }

        $this->renderPartial('vote', array('users'=>$users, 'animals'=>$a, 'alert_flag'=>$alert_flag, 'SID'=>$SID));
    }
}

