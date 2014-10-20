<?php
require_once 'aliyun.php';

use \Aliyun\OSS\OSSClient;

class OSS extends CApplicationComponent {
	// ACL flags
	const ACL_PRIVATE = 'private';
	const ACL_PUBLIC_READ = 'public-read';
	const ACL_PUBLIC_READ_WRITE = 'public-read-write';

	private $_oss;
	public $aKey; // AWS Access key
	public $sKey; // AWS Secret key	
    public $endpoint;

	private function getInstance(){
		if ($this->_oss === NULL)
			$this->connect();
		return $this->_oss;
	}

	/**
	 * Instance the S3 object
	 */
	public function connect()
	{
		if ( $this->aKey === NULL || $this->sKey === NULL )
			throw new CException('OSS Keys are not set.');
			
		$this->_oss = OSSClient::factory(array('AccessKeyId'=>$this->aKey,'AccessKeySecret'=>$this->sKey,'Endpoint'=>$this->endpoint));
	}

    public function upload($bucket, $key, $content)
    {
        try {
            $oss = $this->getInstance();
            $rtn = $oss->putObject(array(
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
            $oss = $this->getInstance();
            $rtn = $oss->putObject(array(
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
            $oss = $this->getInstance();
            $obj = $oss->getObject(array(
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

    public function get_obj($bucket, $key)
    {
        try {
            $oss = $this->getInstance();
            $obj = $oss->getObject(array(
                'Bucket' => $bucket,
                'Key' => $key,
                'Range' => array(0,3),
            ));

            return $obj;
        } catch (\Aliyun\OSS\Exceptions\OSSException $ex) {
            return ;
        } catch (\Aliyun\Common\Exceptions\ClientException $ex) {
            throw new PException("ClientError: " . $ex->getMessage());
        }
    }
}
