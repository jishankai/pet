package com.aidigame.hisun.imengstar.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.ui.UpdateAPKActivity;
import com.aidigame.hisun.imengstar.util.LogUtil;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;

public class DownLoadApkService extends Service {
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
   long loadTotal=0;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	    new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String value = "dog&cat";
				String SIG = HttpUtil.getMD5(value);
				boolean flag=false;
				String url=Constants.android_url;
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				BufferedInputStream bis=null;
				BufferedOutputStream bos=null;
				try {
					HttpResponse response = client.execute(get);
					int resultCode = response.getStatusLine().getStatusCode();

//					String result = EntityUtils.toString(response.getEntity());
					LogUtil.i("me", "url" + url+",resultCode="+resultCode);
					if (resultCode == HttpStatus.SC_OK) {
						LogUtil.i("me", "url" + url);
						HttpEntity entity=response.getEntity();
						InputStream is=entity.getContent();
						
						File file=new File(Constants.Picture_Root_Path);
						if(!file.exists()){
							file.mkdir();
							//屏蔽此目录下的图片资源
							File temp=new File(Constants.Picture_Root_Path+File.separator+".nomedia");
							temp.mkdir();
						}
						file=new File(Constants.Picture_Topic_Path);
						if(!file.exists()){
							file.mkdir();
						}
						file=new File(Constants.Picture_ICON_Path);
						if(!file.exists()){
							file.mkdir();
						}
						file=new File(Constants.Picture_Camera);
						if(!file.exists()){
							file.mkdir();
						}
						byte[] buffer=new byte[1024*10];
						int len=0;
						bis=new BufferedInputStream(is);
						LogUtil.i("me", "下载apk" + Constants.DOWNLOAD_APK);
						File file1=new File(Constants.Picture_Root_Path+File.separator+"pet.apk");
						if(file1.exists()){
							file1.delete();
						}
						bos=new BufferedOutputStream(new FileOutputStream(new File(Constants.Picture_Root_Path+File.separator+"pet.apk")));
						LogUtil.i("me","downloadfile="+Constants.Picture_Topic_Path+File.separator+"pet.apk");
						while((len=bis.read(buffer, 0, buffer.length))!=-1){
							LogUtil.i("me", "下载apk len="+len );
							loadTotal+=len;
							if(UpdateAPKActivity.updateAPKActivity!=null){
								Message msg=UpdateAPKActivity.updateAPKActivity.progressHandler.obtainMessage();
								msg.what=1;
								msg.arg1=(int)loadTotal;
								UpdateAPKActivity.updateAPKActivity.progressHandler.sendMessage(msg);
							}
							bos.write(buffer, 0, len);
						}
						bos.flush();
						if(UpdateAPKActivity.updateAPKActivity!=null){
							Message msg=UpdateAPKActivity.updateAPKActivity.progressHandler.obtainMessage();
							msg.what=2;
							msg.arg1=(int)loadTotal;
							UpdateAPKActivity.updateAPKActivity.progressHandler.sendMessage(msg);
						}else{
							String path=Constants.Picture_Root_Path+File.separator+"pet.apk";
							Intent intent=new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(new File(path)),  
					                "application/vnd.android.package-archive");  
							startActivity(intent);
						}
						stopSelf();
						LogUtil.i("me", "下载apk 成功" );
						
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(UpdateAPKActivity.updateAPKActivity!=null){
						Message msg=UpdateAPKActivity.updateAPKActivity.progressHandler.obtainMessage();
						msg.what=3;
						msg.arg1=(int)loadTotal;
						UpdateAPKActivity.updateAPKActivity.progressHandler.sendMessage(msg);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(UpdateAPKActivity.updateAPKActivity!=null){
						Message msg=UpdateAPKActivity.updateAPKActivity.progressHandler.obtainMessage();
						msg.what=3;
						msg.arg1=(int)loadTotal;
						UpdateAPKActivity.updateAPKActivity.progressHandler.sendMessage(msg);
					}
				}finally{
					if(bis!=null){
						try {
							bis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(bos!=null){
						try {
							bis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					stopSelf();
				}
			}
		}).start();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.i("me", "销毁service");
		super.onDestroy();
	}

}
