<?php

class Easemob
{
    const URL = 'https://a1.easemob.com';

    public $client_id;
    public $client_secret;
    public $org_name;
    public $app_name;
    private $url;
    private $debug;
    private $storageAdapter;

    /**
     * 初始化环形参数
     *
     * @param array $options
     * @param $options ['client_id']
     * @param $options ['client_secret']
     * @param $options ['org_name']
     * @param $options ['app_name']
     */
    public function __construct($client_id=NULL, $client_secret=NULL, $org_name='', $app_name='', $debug = false)
    {
        $this->debug = $debug;
        if ($client_id!=NULL&&$client_secret!=NULL) {
            $this->client_id = $client_id;
            $this->client_secret = $client_secret;
            $this->org_name = $org_name;
            $this->app_name = $app_name;
        }

        $this->url = self::URL . '/' . $this->org_name . '/' . $this->app_name;
    }

    /**
     * 创建新用户[授权模式]
     * @param $username
     * @param $password
     * @return mixed
     * @throws \ErrorException
     */
    public function userAuthorizedRegister($username, $password)
    {
        $url = $this->url . '/users';
        return $this->contact($url, array(
            'username' => $username,
            'password' => $password
        ));
    }

    /**
     * 查看用户是否在线
     * @param $username
     * @return bool
     * @throws \ErrorException
     */
    public function userOnline($username)
    {
        $url = $this->url . '/users/' . $username . '/status';
        $res = $this->contact($url, '', 'GET');
        if (isset($res['data'])) {
            if (isset($res['data'][$username])) {
                return ($res['data'][$username] === 'online');
            }
        }
        return false;
    }

    /**
     * 向群组中加一个人
     * @param $groupId
     * @param $username
     * @return mixed
     * @throws \ErrorException
     */
    public function groupAddUser($groupId, $username)
    {
        $url = $this->url . '/chatgroups/' . $groupId . '/users/' . $username;
        return $this->contact($url);
    }

    /**
     * 删除一个用户
     * @param $username
     * @return mixed
     * @throws \ErrorException
     */
    public function userDelete($username)
    {
        $url = $this->url . '/users/' . $username;
        return $this->contact($url, '', 'DELETE');
    }

    /**
     * @param string|array $groupId 发给群ID
     * @param string $from 谁发的
     * @param array $options
     * @param $options ['mixed'] 是否需要将ext的内容同时发送到txt里 环信的webim不支持接受ext 故加入此功能
     * @param $options ['msg'] 消息内容
     * @param $options ['ext'] 扩展消息内容
     * @return mixed
     */
    public function sendToGroups($groupId, $from, $options)
    {
        return $this->sendMessage($from, $groupId, $options, 'chatgroups');
    }

    /**
     * @param string|array $username 发给谁
     * @param string $from 谁发的
     * @param array $options
     * @param $options ['mixed'] 是否需要将ext的内容同时发送到txt里 环信的webim不支持接受ext 故加入此功能
     * @param $options ['msg'] 消息内容
     * @param $options ['ext'] 扩展消息内容
     * @return mixed
     */
    public function sendToUsers($username, $from, $options)
    {
        return $this->sendMessage($from, $username, $options);
    }

    /**
     * @param string $from 谁发的
     * @param string|array $to 发给谁,人或群
     * @param array $options
     * @param $options ['mixed'] 是否需要将ext的内容同时发送到txt里 环信的webim不支持接受ext 故加入此功能
     * @param $options ['msg'] 消息内容
     * @param $options ['ext'] 扩展消息内容
     * @param string $target_type 群还是人
     * @return mixed
     * @throws \ErrorException
     */
    private function sendMessage($from, $to, $options, $target_type = 'users')
    {
        $data = array(
            'target_type' => $target_type,
            'target' => is_array($to) ? $to : array($to),
            'from' => $from,
        );
        if (isset($options['mixed'])) {
            $data['msg'] = array(
                'type' => 'txt',
                'msg' => json_encode($options['ext'])
            );
        }
        if (isset($options['msg'])) {
            $data['msg'] = array(
                'type' => 'txt',
                'msg' => strval($options['msg'])
            );
        }
        if (isset($options['ext'])) {
            $data['ext'] = $options['ext'];
        }
        $url = $this->url . '/messages';
        return $this->contact($url, $data);
    }

    public function getChatMessages($from, $to, $start, $end, $cursor = false)
    {
        $ql = "select+*+where+to%3D'{$to}'+and+from%3D'{$from}'+and+timestamp%3C{$end}+and+timestamp%3E{$start}";
        $ql .= '&limit=300';
        if (!is_bool($cursor)) {
            $ql .= '&cursor=' . $cursor;
        }
        $url = $this->url . '/chatmessages?ql=' . $ql;
        return $this->contact($url, '', 'GET');
    }

    /**
     * 获取token
     * @return bool
     * @throws \ErrorException
     */
    private function getToken()
    {
        $token = $this->cacheToken();
        if ($token) {
            return $token;
        } else {
            $option ['grant_type'] = "client_credentials";
            $option ['client_id'] = $this->client_id;
            $option ['client_secret'] = $this->client_secret;
            $token = $this->contact($this->url . '/token', $option);
            if (isset($token['access_token'])) {
                $this->cacheToken($token);
                return $token['access_token'];
            } else {
                return false;
            }
        }
    }

    /**
     * 持久化token
     * @param bool $saveToken
     * @return bool
     */
    private function cacheToken($saveToken = false)
    {
        $cacheFilePath = dirname(dirname(dirname(__FILE__))) . '/storage/data';
        if ($saveToken) {
            $saveToken['expires_in'] = $saveToken['expires_in'] + time();
            if ($this->storageAdapter) {
                return call_user_func($this->storageAdapter, serialize($saveToken));
            } else {
                $fp = @fopen($cacheFilePath, 'w');
                @fwrite($fp, serialize($saveToken));
                fclose($fp);
            }
        } else {
            if ($this->storageAdapter) {
                $tokenData = call_user_func($this->storageAdapter, false);
            } else {
                $fp = @fopen($cacheFilePath, 'r');
                $tokenData = fgets($fp);
                fclose($fp);
            }
            if ($tokenData) {
                $data = unserialize($tokenData);
                if (!isset($data['expires_in']) || !isset($data['access_token'])) {
                    return false;
                }
                if ($data['expires_in'] < time()) {
                    return false;
                } else {
                    return $data['access_token'];
                }
            }
            return false;
        }
    }

    /**
     * 设置
     * @param callable $callback
     */
    public function setStorageAdapter($callback)
    {
        if (is_callable($callback)) {
            $this->storageAdapter = $callback;
        }
    }

    /**
     * 向环信请求
     * @param $url
     * @param string $params
     * @param string $type POST|GET
     * @return mixed
     * @throws \ErrorException
     */
    private function contact($url, $params = '', $type = 'POST')
    {
        $postData = '';
        if (is_array($params)) {
            $postData = json_encode($params);
        }
        $curl = Yii::app()->curl;
        $curl->setUserAgent('skji/Easemob SDK; Shankai Ji<jishankai@qq.com>');
        $curl->setOpt(CURLOPT_SSL_VERIFYPEER, false);
        $curl->setOpt(CURLOPT_SSL_VERIFYHOST, false);
        $curl->setHeader('Content-Type', 'application/json');
        $token = "";
        if ($url !== $this->url . '/token') {
            $token = $this->getToken();
            $curl->setHeader('Authorization', 'Bearer ' . $token);
        }
        switch ($type) {
            case 'POST': {
                $curl->post($url, $postData);
                break;
            }
            case 'GET': {
                $curl->get($url);
                break;
            }
            case 'DELETE': {
                $curl->delete($url);
                break;
            }
        }
        $curl->close();
        if ($this->debug) {
            echo "URL: {$url}\n Header: {$token} \nBody: \"{$postData}\"\n";
        }
        if ($curl->error) {
            throw new \ErrorException('CURL Error: ' . $curl->error_message, $curl->error_code);
        }
        if ($this->debug) {
//            echo "return: {$curl->raw_response} \n";
        }
        return json_decode($curl->raw_response, true);
    }


}
