<?php
class AppFile{
	const appFileInfoKey = APPFILEINFOKEY;
    const appFileMD5Key = APPFILEMD5KEY;

  // 得到文件存放的目录
    public static function appFileDir(){//此函数返回要验证文件的路径
    	return PATH.'/resources/';
    }
    public static function test() {
        $basePath = AppFile::appFileDir();
  	    $allFile=AppFile::allBaseFile($basePath);
        foreach ($allFile as $filename) {
            $path=substr($filename,strlen($basePath));
            if (preg_match('/^(Job\d+-)(\d).png$/', $path, $arr)) {
                $newFile = $arr[1] . '1' . '.png';
                echo $basePath . $newFile . "<br>";
                copy($filename, $basePath . $newFile);
                $newFile = $arr[1] . '2' . '.png';
                echo $basePath . $newFile . "<br>";
                copy($filename, $basePath . $newFile);
            }
        }
    }
  // 得到更新的文件列表
  // 返回array( 总md5, 文件属性列表 )
    public static function allFileList(){//此函数得到所有gif格式文件的信息
        $basePath = AppFile::appFileDir();
  	    $allFile=AppFile::allBaseFile($basePath);
  		$i=0;
  		$valentineData = Util::loadconfig("checkResources");
  		$checktypes=$valentineData['checktypes'];
  		$files=array();
  		while(isset($allFile[$i])){
  			if(is_dir($allFile[$i])==false){
  				$type=pathinfo($allFile[$i],PATHINFO_EXTENSION);
  				if(in_array($type,$checktypes)){
  					$path=substr($allFile[$i],strlen($basePath));
  					$fileInfo = stat( $allFile[$i] );
      				$md5 = md5_file( $allFile[$i] );
      				if($type=='png'||$type=='gif'){
      					$size = getimagesize($allFile[$i] );
      					 $files[] = array(
       		    		 	'file' => $path, 
                     	 	'mtime' => $fileInfo['mtime'], 
                            'md5' => $md5,
                            'type' => $type, 
                            'width' => $size[0],
                            'height' => $size[1]);
      				}
      				else{
      					$files[] = array(
       		    		 	'file' => $path, 
                     	 	'mtime' => $fileInfo['mtime'], 
                            'md5' => $md5,
                            'type' => $type, );
      				}
  				}
  			}
  			$i++;
  		}
        
        uasort($files, "AppFile::sortFile");
        
    // 生成总的MD5
    	$md5String = '';
    	foreach( $files as $file ){
      		$md5 = $file['md5'];
      		$md5String = $md5String . $md5;
        }
    	$md5String = md5( $md5String );
    	return array( $md5String, $files );
    	return $allFile;
  }
  
  public static function sortFile($a, $b) {
      if ($a['type'] == 'json')
          return -1;
      
      if ($b['type'] == 'json')
          return 1;
      
      return -1;
  }
  
  // 刷新文件列表
	public static function fresh(){
    	$fileInfo = AppFile::allFileList();
    	$md5String = $fileInfo[0];
    	$files = $fileInfo[1];
    	$cache = Yii::app()->cache;
    	$cache->set(AppFile::appFileMD5Key, $md5String, 0);
    	$cache->set(AppFile::appFileInfoKey, $files, 0);
    	return $fileInfo;
	}
  // 得到所有文件的总计MD5
	public static function appFileMD5(){
//    	$cache = Yii::app()->cache;
//    	$md5String = $cache->get(AppFile::appFileMD5Key);
//    	if( $md5String === false ){
      		$appFileInfo =AppFile::fresh();
      		$md5String = $appFileInfo[0];
//    	}
    	return $md5String;
  	}
  // 得到文件列表。如果指定了time，则只给出大于等于给定时间之后更新过的文件
	public static function listModifiedFiles($time = null){
    	$cache = Yii::app()->cache;
    	$files = $cache->get(AppFile::appFileInfoKey);
    // 如果取不到文件列表，重新生成
    	if( $files === false ){
      		$fileInfo = AppFile::fresh();
      		$files = $fileInfo[1];
    	}
    // 没指定时间，返回所有的文件列表
   		if( $time === null ){
      		return $files;
    	}
    	$re = array();
    	foreach( $files as $file ){
      		// 找到更新后的文件
      		if( $file['mtime'] >= $time ){
        	$re[] = $file;
     	 }
    	}
    	return $re;
    }
	public static function allBaseFile($handle){	//此函数得到所有文件的路径的数组
  		$filepath[0]= $handle;
		$i=0;
		$j=1;
		while(isset($filepath[$i])){
			if(substr($filepath[$i],-1)=="/"){
				$dirHandle = opendir( $filepath[$i]);
				while( ( $file = readdir( $dirHandle ) ) != false ){
					if( $file == '.' || $file == '..' || $file == '.svn' ){
       				 	continue;
     			 	}
     				if(is_dir($filepath[$i].$file)==true){//判断条件
     		 			$filepath[$j]=$filepath[$i].$file;
     		 			$filepath[$j].="/";
     		 			$j++;
     				}
     				else {
     					$filepath[$j]=$filepath[$i].$file;
     		 			$j++;
     				}
				}
			}
			$i++;
		}
		return $filepath;
 	 }
}
