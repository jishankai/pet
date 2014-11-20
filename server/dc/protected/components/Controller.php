<?php
/**
 * Controller is the customized base controller class.
 * All controller classes for this application should extend from this base class.
 */
class Controller extends CController
{
    /**
     * @var string the default layout for the controller view. Defaults to '//layouts/column1',
     * meaning using a single column layout. See 'protected/views/layouts/column1.php'.
     */
    public $layout='//layouts/column1';
    /**
     * @var array context menu items. This property will be assigned to {@link CMenu::items}.
     */
    public $menu=array();
    /**
     * @var array the breadcrumbs of the current page. The value of this property will
     * be assigned to {@link CBreadcrumbs::links}. Please refer to {@link CBreadcrumbs::links}
     * for more details on how to specify this property.
     */
    public $breadcrumbs=array();

    public $usr_id;

    public function filterCheckUpdate($filterChain)
    {
        /*
        $ver = $_REQUEST['ver'];
        if ($ver < VERSION) {
            $this->response->setClientOutdated();//重新登录
            $this->response->render();
        } else {
            $filterChain->run();
        }
         */
        $filterChain->run();
            
    }

    public function filterGetUserId($filterChain)
    {
        //if (isset($_REQUEST['SID']) && $_REQUEST['SID']=='') unset($_REQUEST['SID']); 
        $session = Yii::app()->session;
        $session->open();
        
        if (empty($session['usr_id'])) {
            if (isset($session['not_registered'])&&$session['not_registered']==TRUE) {
                $this->response->setNotRegistered();
            } else {
                $this->response->setExpired();//重新登录
            }
            $this->response->render();
        } else {
            $this->usr_id = $session['usr_id'];
            
            //规避头像上传500问题
            if (isset($session['tx_usr'])) {
                $user = User::model()->findByPk($this->usr_id);
                $user->tx = $session['tx_usr'];
                $user->saveAttributes(array('tx'));
                $session->remove('tx_usr');
            }
            if (isset($session['tx_ani'])) {
                $animal = Animal::model()->findByPk($session['tx_ani']['aid']);
                if (isset($animal)) {
                    $animal->tx = $session['tx_usr']['tx'];
                    $animal->saveAttributes(array('tx'));
                    $session->remove('tx_ani');
                }
            }
            // ------------------
            //用户是否被禁
            $rtn = Yii::app()->db->createCommand('SELECT usr_id FROM dc_user WHERE is_ban=1 AND usr_id=:usr_id')->bindValue(':usr_id', $this->usr_id)->queryScalar();
            if ($rtn) {
                throw new PException('您因违反用户协议被禁止使用此功能');
            }
            // ------------------

            $filterChain->run(); 
        }
    }

    public function filterCheckSig($filterChain)
    {
        if (!CHECK_SIG_FLAG or $this->checkSig()) {
            $filterChain->run();
        } else {
            throw new CException('Signature Error');
        }
    }

    public function signature($params) 
    {
        if (array_key_exists('sig', $params)) unset($params['sig']);
        if (array_key_exists('r', $params)) unset($params['r']); 
        if (array_key_exists('name', $params)) unset($params['name']); 
        if (array_key_exists('u_name', $params)) unset($params['u_name']); 
        if (array_key_exists('SID', $params)) unset($params['SID']); 

        ksort($params);
        $newArray = array();
        foreach ($params as $key => $val) {
            $newArray[] = $key. '=' . $val;
        }
        $string = implode('&', $newArray);
        return md5($string . SIGKEY);
    }

    public function checkSig()
    {
        $params = $this->getActionParams();
        if (array_key_exists('sig', $params)) return $params['sig'] == $this->signature($params);
    }

    /**
     * Returns the reqeust parameters that will be used for action parameter binding. Include $_GET and $_POST.
     * @return array the request parameters to be used for action parameter binding.
     */
    public function getActionParams()
    {
        return $_GET;// + $_POST;
    }

    public function getResponse()
    {
        return Yii::app()->getResponse(); 
    }
	
    protected function echoJsonData($data=array()) 
    {
        $this->response->setData($data);
        $this->response->render();
    }
}
