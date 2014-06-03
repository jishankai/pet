package com.aidigame.hisun.pet.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.telephony.TelephonyManager;

import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.json.LoginJson;
import com.aidigame.hisun.pet.util.LogUtil;

public class HttpUtil {
	
	/**
	 * ע���û�
	 * @param user
	 */
	public static void register(User user){
		String url="http://"+Constants.IP+Constants.REGISTER_PATH;
		DefaultHttpClient client=new DefaultHttpClient();
		String value="age="+user.age+"&class="+user.classs+"&code="+1+"&gender="
		               +user.gender+"&name="+user.nickName+"dog&cat";
		String SIG=getMD5(value);
		String param="&age="+user.age+"&class="+user.classs+"&code="+1+"&gender="
	               +user.gender+"&name="+user.nickName+"&sig="+SIG+"&SID="+Constants.SID;
		url=url+param;
		HttpGet get=new HttpGet(url);
		try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			
			String result=EntityUtils.toString(response.getEntity());
			LogUtil.i("me", "url������"+url);
			LogUtil.i("me", "ע�᷵�ؽ��������"+result);
			LoginJson loginJson=parseJson(result);
			if(resultCode==HttpStatus.SC_OK){
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ��½
	 * @param user
	 */
	public static void login(Context context,Handler handler){
		String uid=getIMEI(context);
		String url="http://"+Constants.IP+Constants.LOGIN_PATH;
		DefaultHttpClient client=new DefaultHttpClient();
		String value="uid="+uid+"dog&cat";
		String SIG=getMD5(value);
		String param="&uid="+uid+"&sig="+SIG;
		url=url+param;
		HttpGet get=new HttpGet(url);
		try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			
			String result=EntityUtils.toString(response.getEntity());

			LoginJson loginJson=parseJson(result);
			Constants.isSuccess=loginJson.data.isSuccess;
			Constants.SID=loginJson.data.SID;
			//TODO
//			handler.sendEmptyMessage(1);
			if(resultCode==HttpStatus.SC_OK){
				LogUtil.i("me", "url������"+url);
				LogUtil.i("me", "��½���ؽ��������"+result);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/**
	 * ��ѯinfo
	 * @param user
	 */
	public static void info(User user){
		String url="http://"+Constants.IP+Constants.INFO_PATH;
		DefaultHttpClient client=new DefaultHttpClient();
		String value="dog&cat";
		String SIG=getMD5(value);
		String param=SIG+"&SID=["+"]";
		url=url+param;
		HttpGet get=new HttpGet(url);
		try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			
			String result=EntityUtils.toString(response.getEntity());
			LogUtil.i("me", "url"+url);
			LogUtil.i("me", "��½���ؽ��������"+result);
			if(resultCode==HttpStatus.SC_OK){
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * �ϴ�ͼƬ
	 * @param path  ͼƬ·��
	 * @return
	 */
	public static boolean uploadImage(String path){
		boolean flag=false;
		String value="dog&cat";
		String SIG=getMD5(value);
		String url="http://"+Constants.UPLOAD_IMAGE_PATH+SIG+"&SID=["+"]";
		DefaultHttpClient client=new DefaultHttpClient();
		HttpPost post=new HttpPost(url);
		File file=new File(path);
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
			BufferedInputStream bis=new BufferedInputStream(fis);
			InputStreamEntity entity=new InputStreamEntity(bis, file.length());
		    post.setEntity(entity);
		    HttpResponse response=client.execute(post);
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return flag;
	}
	/**
	 * ϲ��ͼƬ
	 * @param topic
	 */
	public static void likeImage(Topic topic){
		String url="http://"+Constants.IP+Constants.LIKE_IMAGE_PATH;
		DefaultHttpClient client=new DefaultHttpClient();
		String value="ima_id="+topic.img_id+"dog&cat";
		String SIG=getMD5(value);
		String param="&ima_id="+topic.img_id+"&sig="+SIG+"&SID=["+"]";
		url=url+param;
		HttpGet get=new HttpGet(url);
		try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			
			String result=EntityUtils.toString(response.getEntity());
			LogUtil.i("me", "��½���ؽ��������"+result);
			if(resultCode==HttpStatus.SC_OK){
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ɾ��ͼƬ
	 * @param topic
	 */
	public static void deleteImage(Topic topic){
		String url="http://"+Constants.IP+Constants.LIKE_IMAGE_PATH;
		DefaultHttpClient client=new DefaultHttpClient();
		String value="ima_id="+topic.img_id+"dog&cat";
		String SIG=getMD5(value);
		String param="&ima_id="+topic.img_id+"&sig="+SIG+"&SID=["+"]";
		url=url+param;
		HttpGet get=new HttpGet(url);
		try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			
			String result=EntityUtils.toString(response.getEntity());
			LogUtil.i("me", "��½���ؽ��������"+result);
			if(resultCode==HttpStatus.SC_OK){
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getMD5(String str){
		try {
			MessageDigest md5=MessageDigest.getInstance("MD5");
			md5.update(str.getBytes("UTF-8"));
			byte[] data=md5.digest();
			StringBuffer sb=new StringBuffer();
			/*for(int i=0;i<data.length;i++){
				sb.append((data[i]));
			}*/
			for(byte b:data){
				if((b&0xFF)<0X10)sb.append("0");
				sb.append(Integer.toHexString(b&0xFF));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getIMEI(Context context){
		String uid="";
		TelephonyManager tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		uid=tm.getDeviceId();
		return uid;
	}
	public static LoginJson parseJson(String json){
		JSONObject jsonObject;
		try {
			/*
			 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
             * "confVersion":"1.0",
             * "data":{"isSuccess":false,"SID":"v49j7o9hn4jb1r65acho2t34n4"},
             * "currentTime":1401420697}
			 */
			jsonObject = new JSONObject(json);
			LoginJson loginJson=new LoginJson();
			loginJson.state=jsonObject.getInt("state");
			loginJson.errorCode=jsonObject.getInt("errorCode");
			loginJson.errorMessage=jsonObject.getString("errorMessage");
			loginJson.version=jsonObject.getString("version");
			loginJson.confVersion=jsonObject.getString("confVersion");
			loginJson.currentTime=jsonObject.getLong("currentTime");
			JSONObject data=jsonObject.getJSONObject("data");
			loginJson.data=new LoginJson.Data();
			loginJson.data.isSuccess=data.getBoolean("isSuccess");
			loginJson.data.SID=data.getString("SID");
			return loginJson;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return null;
	}
}
