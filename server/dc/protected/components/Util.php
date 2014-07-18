<?php

class Util {

    public static function loadConfig($k, $cache = true, $params = null) {
        static $cfg;
        if (!$cfg) {
            $cfg = array();
        }
        if (!isset($cfg[$k]) || !$cache) {
            if ($params) {
                foreach ($params as $name => $value) {
                    $$name = $value;
                }
            }
            if (file_exists(dirname(__FILE__) . '/../config/dc/' . $k . '.cfg.php')) {
                $cfg[$k] = require(dirname(__FILE__) . '/../config/dc/' . $k . '.cfg.php');
            }
        }
        if (isset($cfg[$k])) {
            return $cfg[$k];
        } else {
            return null;
        }
    }

    public static function getDayTime($timestamp) {
        return mktime(0, 0, 0, date("m", $timestamp), date("d", $timestamp), date("Y", $timestamp));
    }

    public static function getMonthTime($timestamp) {
        return mktime(0, 0, 0, date("m", $timestamp), 1, date("Y", $timestamp));
    }

    public static function getUpdateTimeStr($timestamp) {
        return date("Y-m-d H:i:s");
    }

    public static function makeToken($uid = '') {
        return base64_encode(md5(microtime() . rand(1, 100000) . TOKEN_SECRET_KEY));
    }

    public static function makeSign($uid = '', $token = '') {
        return base64_encode(md5($uid . $token));
    }

    //允许的ip返回为true，不允许的ip返回为false
    public static function checkIp() {
        $list = Util::loadconfig('ipCheckList');
        $realIp = Util::getRealIp();
        foreach ($list as $item) {
            if (preg_match($item, $realIp)) {
                return true;
            }
        }
        return false;
    }

    //获取客户端的真实IP
    static function getRealIp() {
        if (getenv('HTTP_X_REAL_IP')) {
            $ip = getenv('HTTP_X_REAL_IP');
        } elseif (getenv('HTTP_X_FORWARDED_FOR')) {
            $remote_addr = getenv("HTTP_X_FORWARDED_FOR");
            $tmp_ip = explode(",", $remote_addr);
            $ip = $tmp_ip[0];
        } elseif (getenv('HTTP_CLIENT_IP')) {
            $ip = getenv('HTTP_CLIENT_IP');
        } elseif (getenv('REMOTE_ADDR')) {
            $ip = getenv('REMOTE_ADDR');
        } elseif (isset($_SERVER['REMOTE_ADDR'])) {
            $ip = $_SERVER['REMOTE_ADDR'];
        } else {
            $ip = "";
        }
        return $ip;
    }

    public static function changeArrayToHashByKey($data, $hashKey) {
        $result = array();
        foreach ($data as $item) {
            $result[$item[$hashKey]] = $item;
        }
        return $result;
    }

    public static function postRequest($url,$data){
        $ch = curl_init();
        curl_setopt($ch,CURLOPT_SSL_VERIFYPEER,0);
        curl_setopt($ch,CURLOPT_COOKIEJAR,null);
        curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
        curl_setopt($ch,CURLOPT_URL,$url);
        curl_setopt($ch,CURLOPT_POST,true);
        curl_setopt($ch,CURLOPT_POSTFIELDS,$data);
        $content = curl_exec($ch);
        return $content;
    }
}
