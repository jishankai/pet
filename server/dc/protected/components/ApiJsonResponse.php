<?php
/**
 * ApiJsonResponse class file.
 * @author Qi Changhai <qi.changhai@adways.net>
 */
/**
 * ApiJsonReponse represents the response data of api. It provide method to write data as json.
 * @author Qi Changhai <qi.changhai@adways.net>
 */
class ApiJsonResponse
{
    const STATE_OK              = 0; //正常
    const STATE_ERROR           = 1; //错误
    const STATE_EXPIRED         = 2; //session过期
    const STATE_CLIENT_OUTDATED = 3; //客户端版本过期
    const STATE_MAINTENANCE     = 4; //服务器维护

    protected $state = self::STATE_OK;

    protected $errorCode = 0;

    protected $errorMessage = '';

    protected $version = VERSION;

    //@TODO confVersion should be get by analyse resource? 
    //but perhapse has performence problem. Anyway, it should not be
    //set by hand.
    protected $confVersion = CONF_VERSION;

    protected $data;

    /**
     * Set normal data.
     * @param mixed $result the result data of request.
     */
    public function setData($data)
    {
        $this->data = $data;
    }

    public function getData()
    {
        return $this->data; 
    }

    /**
     * Set error as response.
     * @param integer $errorCode the php error code. 
     * see @link http://www.php.net/manual/en/errorfunc.constants.php.
     * @param integer $errorMessage the error message.
     */
    public function setError($errorCode, $errorMessage)
    {
        $this->state = self::STATE_ERROR;
        //@TODO error code and error message should be filtered.
        $this->errorCode = $errorCode;
        $this->errorMessage = YII_DEBUG ? $errorMessage : "System Error";
    }

    /**
     * Set exception as response. Exception will be processed as error.
     * @param Exception $exception the php exception.
     * @TODO filter some exception message.
     */
    public function setException($exception)
    {
        $this->state = self::STATE_ERROR;
        $this->errorCode = -1;
        if ($exception instanceof BPException) {
            $this->errorMessage = $exception->getMessage();
        } else {
            $this->errorMessage = YII_DEBUG ? $exception->__toString() : "System Exception";
        }
    }

    /**
     * Set expired response.
     */
    public function setExpired()
    {
        $this->state = self::STATE_EXPIRED;
    }

    /**
     * Set client version outdated reponse.
     */
    public function setClientOutdated()
    {
        $this->state = self::STATE_CLIENT_OUTDATED;
    }

    /**
     * Set maintenance infomantion as response.
     */
    public function setMaintenance()
    {
        $this->state = self::STATE_MAINTENANCE;
    }

    /**
     * Render the response as string.
     * @param boolean $return whether return the rendered result as a
     * string instread of echo.
     */
    public function render($return=false)
    {
        $json = CJSON::encode(array(
            'state' => $this->state,
            'errorCode' => $this->errorCode,
            'errorMessage' => $this->errorMessage,
            'version' => $this->version,
            'confVersion' => $this->confVersion,
            'data' => $this->data,
            'currentTime' => time(),
        ), JSON_NUMERIC_CHECK);
        if($return){
            return $json;
        }else{
	    Yii::trace($json, 'json');
            echo $json;
        }
    }
}
