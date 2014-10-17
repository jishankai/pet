<?php
require_once 'aliyun.php';

use \Aliyun\OSS\OSSClient;

class OSS extends OSSClient {
	// ACL flags
	const ACL_PRIVATE = 'private';
	const ACL_PUBLIC_READ = 'public-read';
	const ACL_PUBLIC_READ_WRITE = 'public-read-write';

	private static $__accessKey; // AWS Access key
	private static $__secretKey; // AWS Secret key


	/**
	* Constructor - if you're not using the class statically
	*
	* @param string $accessKey Access key
	* @param string $secretKey Secret key
	* @return void
	*/
	public function __construct($accessKey = null, $secretKey = null, $useSSL = true) {
		if ($accessKey !== null && $secretKey !== null) {
            self::setAuth($accessKey, $secretKey);
            self::factory(array(
                'AccessKeyId' => $__accessKey,
                'AccessKeySecret' => $__secretKey,
            ));
        }
	}

	/**
	* Set AWS access key and secret key
	*
	* @param string $accessKey Access key
	* @param string $secretKey Secret key
	* @return void
	*/
	public static function setAuth($accessKey, $secretKey) {
		self::$__accessKey = $accessKey;
		self::$__secretKey = $secretKey;
	}

    public function upload($bucket, $key, $content)
    {
        try {
            $rtn = $this->putObject(array(
                'Bucket' => $bucket,
                'Key' => $key,
                'Content' => $content,
            ));

            return $rtn;
        } catch (\Aliyun\OSS\Exceptions\OSSException $ex) {
            throw new PException("Error: " . $ex->getErrorCode());
        } catch (\Aliyun\Common\Exceptions\ClientException $ex) {
            throw new PException("ClientError: " . $ex->getMessage());
        }
    }

    public function upload_file($bucket, $key, $content, $length)
    {
        try {
            $rtn = $this->putObject(array(
                'Bucket' => $bucket,
                'Key' => $key,
                'Content' => $content,
                'ContentLength' => $length,
            ));

            return $rtn;
        } catch (\Aliyun\OSS\Exceptions\OSSException $ex) {
            throw new PException("Error: " . $ex->getErrorCode());
        } catch (\Aliyun\Common\Exceptions\ClientException $ex) {
            throw new PException("ClientError: " . $ex->getMessage());
        }
    }

    public function get_obj_content($bucket, $key)
    {
        try {
            $obj = $this->getObject(array(
                'Bucket' => $bucket,
                'Key' => $key,
            ));

            return stream_get_contents($obj->getObjectContent());
        } catch (\Aliyun\OSS\Exceptions\OSSException $ex) {
            throw new PException("Error: " . $ex->getErrorCode());
        } catch (\Aliyun\Common\Exceptions\ClientException $ex) {
            throw new PException("ClientError: " . $ex->getMessage());
        }
    }
}
