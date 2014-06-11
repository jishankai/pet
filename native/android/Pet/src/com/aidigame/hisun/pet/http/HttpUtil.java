package com.aidigame.hisun.pet.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.json.InfoJson;
import com.aidigame.hisun.pet.http.json.LoginJson;
import com.aidigame.hisun.pet.http.json.RegisterJson;
import com.aidigame.hisun.pet.http.json.UpdateIconJson;
import com.aidigame.hisun.pet.http.json.UpdateImageJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.OtherUserTopicActivity;
import com.aidigame.hisun.pet.ui.SubmitPictureActivity;
import com.aidigame.hisun.pet.ui.UserHomepageActivity;
import com.aidigame.hisun.pet.util.LogUtil;

public class HttpUtil {

	/**
	 * 注册
	 * 
	 * @param user
	 */
	public static void register(Handler handler, User user) {
		String url = "http://" + Constants.IP + Constants.REGISTER_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		String name = null;
		try {
			name = new String(user.nickName.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String value = "age=" + user.age + "&code="/* +1 */+ "&gender="
				+ user.gender + /* "&name="+name+ */"&type=" + user.classs
				+ "dog&cat";
		LogUtil.i("me", "value" + value);
		String SIG = null;
		SIG = getMD5(value);
		String param = "&age=" + user.age + "&code="/* +1 */+ "&gender="
				+ user.gender + "&name=" + name + "&type=" + user.classs
				+ "&sig=" + SIG + "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me", "注册url==" + url);
			LogUtil.i("me", "注册返回结果==" + result);
            
			if (resultCode == HttpStatus.SC_OK) {
				RegisterJson loginJson = parseRegisterJson(result);
				if(judgeSID(loginJson.errorCode)){
					register(handler,user);
					return;
				}
				if(loginJson.errorCode==1){
					Message msg=handler.obtainMessage();
					msg.obj=loginJson.errorMessage;
					handler.sendEmptyMessage(Constants.REGISTER_FAIL);
					return;
				}
				if (loginJson.data.isSuccess) {
					//上传头像
					String path=uploadUserIcon(user.iconPath);
					if(path==null){
						//上传头像失败
					}
					handler.sendEmptyMessage(Constants.REGISTER_SUCCESS);
				}
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
	 * 登陆
	 * 
	 * @param user
	 */
	public static void login(Context context, Handler handler) {
		String uid = getIMEI(context);
		if(context==null){
			uid=Constants.IMIE;
			
		}else{
			uid = getIMEI(context);
			Constants.IMIE=uid;
		}
		
		String url = "http://" + Constants.IP + Constants.LOGIN_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "uid=" + uid + "dog&cat";
		String SIG = getMD5(value);
		String param = "&uid=" + uid + "&sig=" + SIG;
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());

			
			// TODO
			LogUtil.i("me", "登陆url==" + url);
			LogUtil.i("me", "登陆的返回结果" + result);
			if (resultCode == HttpStatus.SC_OK) {
				LoginJson loginJson = parseJson(result);
				Constants.isSuccess = loginJson.data.isSuccess;
				Constants.SID = loginJson.data.SID;
				if(handler!=null)
				handler.sendEmptyMessage(Constants.LOGIN_SUCCESS);
				
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
	 * 获取用户相关信息 info
	 * 
	 * @param user
	 */
	public static void info(User user,Activity activity) {
		String url = "http://" + Constants.IP + Constants.INFO_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		
		String value = "dog&cat";
		String SIG = getMD5(value);
		String param = SIG + "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());

			if (resultCode == HttpStatus.SC_OK) {
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				InfoJson infoJson=parseInfoJson(result,activity);
				if(judgeSID(infoJson.errorCode)){
					info(user, activity);;
					return;
				}
				Constants.infoJson=infoJson;
				SharedPreferences sp=activity.getSharedPreferences("temp.xml", Context.MODE_WORLD_WRITEABLE);
				String path=sp.getString("icon", null);
				//TODO 下载图片
//				uploadUserIcon(path);
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
	 * 上传图片
	 * 
	 * @param path
	 *            
	 * @return
	 */
	
	public static boolean uploadImage(String path,String info,Handler handler) {
		boolean flag = false;
		UserImagesJson  json=post(path,info);
		UserImagesJson.Data data=null;
		if(json!=null) {
			data=json.datas.get(0);
			if(json.errorCode==2){
				return uploadImage(path, info, handler);
				
			}
		}else{
			return false;
		}
		
		if(data!=null){
			Message msg=handler.obtainMessage();
			msg.what=SubmitPictureActivity.UPLOAD_IMAGE_SUCCESS;
			msg.obj=data;
			handler.sendMessage(msg);
		}
//		uploadFile(path);
		/*String value = "dog&cat";
		String SIG = getMD5(value);
		String url = "http://" +Constants.IP+ Constants.UPLOAD_IMAGE_PATH + SIG + "&SID="
				+ Constants.SID;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpContext localContext=new BasicHttpContext();
		HttpPost post = new HttpPost(url);
		File file = new File(path);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			InputStreamEntity entity = new InputStreamEntity(bis, file.length());
			LogUtil.i("me","上传图片返回结果：文件大小="+file.length()/1024);
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			MultipartEntity entity = new MultipartEntity();
			
			entity.addPart("image", new FileBody(new File(path)));
//			entity.addPart("comment",new StringBody("test"));
			post.setEntity(entity);
			LogUtil.i("me", "requestLine"+post.getRequestLine());
			LogUtil.i("me", "llHeaders"+post.getAllHeaders().toString());
			LogUtil.i("me", "getEntity().getContentLength()"+post.getEntity().toString());
			HttpResponse response = client.execute(post);
			int status=response.getStatusLine().getStatusCode();
			if(status==HttpStatus.SC_OK){
				String result=EntityUtils.toString(response.getEntity());
				
				LogUtil.i("me","上传图片返回结果：url="+url);
				LogUtil.i("me","上传图片返回结果：文件大小="+file.length()/1024+"***"+result);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		return flag;
	}
	 /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     * 
     * @param url Service net address
     * @param params text content
     * @param files pictures
     * @return String result of Service response
     * @throws IOException
     */
    public static UserImagesJson post(String path,String info){
    	boolean flag = false;
		String value = "dog&cat";
		String SIG = getMD5(value);
		String url = "http://" +Constants.IP+ Constants.UPLOAD_IMAGE_PATH + SIG + "&SID="
				+ Constants.SID;
    	Map<String, String> params=new HashMap<String, String>();
    	params.put("comment", info);
    	Map<String, File> files=new HashMap<String, File>();
    	files.put("image", new File(path));
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri=null;
        DataOutputStream outStream=null;
        InputStream in=null;

        try {
    			uri = new URL(url);
    		if(uri==null)return null;
        	HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout(10 * 1000); // 缓存的最长时间
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
//            conn.setRequestProperty("enctype", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);


            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            LogUtil.i("me", "消息头==="+conn.toString());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }
        	outStream = new DataOutputStream(conn.getOutputStream());
             LogUtil.i("me", "sb.toString参数===="+sb.toString());
			outStream.write(sb.toString().getBytes());
			// 发送文件数据
	        if (files != null)
	            for (Map.Entry<String, File> file : files.entrySet()) {
	                StringBuilder sb1 = new StringBuilder();
	                sb1.append(PREFIX);
	                sb1.append(BOUNDARY);
	                sb1.append(LINEND);
	                sb1.append("Content-Disposition: form-data; name=\"image\"; filename=\""
	                        + file.getValue().getName() + "\"" + LINEND);
	                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
	                sb1.append(LINEND);
	                outStream.write(sb1.toString().getBytes());
	                LogUtil.i("me", "sb.toString文件参数===="+sb1.toString());
	                LogUtil.i("me", "sb.toString图片数据流====：：：");

	                InputStream is = new FileInputStream(file.getValue());
	                byte[] buffer = new byte[1024];
	                int len = 0;
	                while ((len = is.read(buffer)) != -1) {
	                    outStream.write(buffer, 0, len);
	                   LogUtil.i("me", ""+Arrays.toString(buffer)) ;
	                }


	                is.close();
	                outStream.write(LINEND.getBytes());
	            }


	        // 请求结束标志
	        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
	        outStream.write(end_data);
	        outStream.flush();
	        // 得到响应码
	        int res = conn.getResponseCode();
	        in = conn.getInputStream();
	        StringBuilder sb2 = new StringBuilder();
	        if (res == 200) {
	            int ch;
	            while ((ch = in.read()) != -1) {
	                sb2.append((char) ch);
	            }
	        }
	        outStream.close();
	        conn.disconnect();
	        sb2.toString();
	        LogUtil.i("me", "返回结果===="+sb2.toString());
//	        UpdateImageJson imaeJson=parseUpdateImageJson(sb2.toString());
	        UserImagesJson data=parseUpdateImageJson(sb2.toString());
	        if(data!=null){
	        	return data;
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(outStream!=null){
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        return null;
        
    }
    /**
     * 上传头像
     * 
     * @param file 需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    public static String uploadUserIcon(String path) {
    	String TAG = "tx";


    	     int TIME_OUT = 10 * 1000; // 超时时间


    	    String CHARSET = "utf-8"; // 设置编码
    		String value = "dog&cat";
    		String SIG = getMD5(value);
    		String RequestURL =  Constants.USER_UPDATE_TX + SIG + "&SID="
    				+ Constants.SID;
    	String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        File file=new File(path);
        DataOutputStream dos =null;
        InputStream input=null;

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);


            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */


                sb.append("Content-Disposition: form-data; name=\"tx\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                LogUtil.i(TAG, "response code:" + res);
                // if(res==200)
                // {
                Log.e(TAG, "request success");
                input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();
                LogUtil.i("me", "user icon result : " + result);
                UpdateIconJson json=new UpdateIconJson(result);
                if(json.errorCode==2){
                	return uploadUserIcon(path);
                }
                return json.data.tx;
                // }
                // else{
                // Log.e(TAG, "request error");
                // }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	if(dos!=null){
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
        return null;
    }
    /**
     * 
     * @param handler
     * @param last_id
     * @param mode  0 用户个人主页；1 Favorite列表；2 other 主页列表;3 random 列表
     */
	public static void downloadUserHomepage(Handler handler,int last_id,int  mode) {
		String SIG=null;
		String url = null;
		String param=null;
		if(last_id==-1){
			String value ="dog&cat";
			SIG = getMD5(value);
			param = "sig="+SIG+"&SID="+Constants.SID;
		}else{
			String value ="img_id="+last_id+"dog&cat";
			SIG = getMD5(value);
			param ="img_id="+last_id+"&sig="+SIG+"&SID="+Constants.SID;
		}
		
		switch (mode) {
		case 0:
			url=Constants.USER_IMAGES+param;
			break;
		case 1:
			url=Constants.IMAGE_FAVORITE+param;
			break;
		case 3:
			url=Constants.IMAGE_RANDOM+param;
			break;
		}
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());

			
			// TODO
			LogUtil.i("me", "下载图片列表url==" + url);
			LogUtil.i("me", "下载图片返回结果" + result);
			if (resultCode == HttpStatus.SC_OK) {
				UserImagesJson json=new UserImagesJson(result);
				if(json.errorCode==2){
					downloadUserHomepage(handler, last_id, mode);
					return;
				}
				Message msg=handler.obtainMessage();
				msg.obj=json;
				msg.what=Constants.MESSAGE_DOWNLOAD_IMAGES_LIST;
				handler.sendMessage(msg);
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
	 * 下载其他用户列表
	 * @param handler
	 * @param last_id
	 * @param usr_id
	 */
	public static void downloadOtherUserHomepage(Handler handler,int last_id,UserImagesJson.Data data) {
		String SIG=null;
		String url = null;
		String param=null;
		if(last_id==-1){
			String value ="usr_id="+data.usr_id+"dog&cat";
			SIG = getMD5(value);
			param = "usr_id="+data.usr_id+"&sig="+SIG+"&SID="+Constants.SID;
		}else{
			String value ="img_id="+last_id+"&usr_id="+data.usr_id+"dog&cat";
			SIG = getMD5(value);
			param ="img_id="+last_id+"&usr_id="+data.usr_id+"&sig="+SIG+"&SID="+Constants.SID;
		}
		/*if(last_id==-1){
			String value ="dog&cat";
			SIG = getMD5(value);
			param ="sig="+SIG+"&SID="+Constants.SID;
		}else{
			String value ="img_id="+last_id+"dog&cat";
			SIG = getMD5(value);
			param ="img_id="+last_id+"&sig="+SIG+"&SID="+Constants.SID;
		}*/
		url=Constants.USER_IMAGES+param;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());

			
			// TODO
			LogUtil.i("me", "下载图片列表url==" + url);
			LogUtil.i("me", "下载图片返回结果" + result);
			if (resultCode == HttpStatus.SC_OK) {
				/*JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray=jsonObject.getJSONArray("data");
				UserImagesJson json=new UserImagesJson();
				json.datas=new ArrayList<UserImagesJson.Data>();
				UserImagesJson.Data data1=null;
				for(int i=0;i<jsonArray.length();i++){
					     data1.url=((JSONObject)jsonArray.get(i)).getString("url");
					     data1.img_id=((JSONObject)jsonArray.get(i)).getInt("img_id");
						data1.comment=((JSONObject)jsonArray.get(i)).getString("comment");
						data1.usr_id=((JSONObject)jsonArray.get(i)).getInt("usr_id");
						data1.create_time=((JSONObject)jsonArray.get(i)).getLong("create_time");
						data1.user=data.user;
						data1.likes=((JSONObject)jsonArray.get(i)).getInt("likes");
						json.datas.add(data1);
						handler.sendEmptyMessage(1);
				}*/
				UserImagesJson json=new UserImagesJson(result);
				if(json.errorCode==2){
					downloadOtherUserHomepage(handler, last_id, data);
					return;
				}
				Message msg=handler.obtainMessage();
				msg.obj=json;
				msg.what=Constants.MESSAGE_DOWNLOAD_IMAGES_LIST;
				handler.sendMessage(msg);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	/**
	 * ϲ��ͼƬ
	 * 
	 * @param topic
	 */
	public static void likeImage(UserImagesJson.Data  data) {
		String url = "http://" + Constants.IP + Constants.LIKE_IMAGE_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "img_id=" + data.img_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "&img_id=" + data.img_id + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me","点赞url="+url);
			LogUtil.i("me", "点赞返回结果" + result);
			if (resultCode == HttpStatus.SC_OK) {

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
	 * 根据图片id获得图片的详细信息
	 * @param data
	 */
	public static void imageInfo(UserImagesJson.Data data,Handler handler) {
		String url =Constants.IMAGE_INFO;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "img_id=" + data.img_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "img_id=" + data.img_id + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me","获得一张图片的详细信息url="+url);
			LogUtil.i("me", "获得一张图片的详细信息" + result);
			JSONObject jsonObject=null;
			if (resultCode == HttpStatus.SC_OK) {
				jsonObject = new JSONObject(result);
				if(jsonObject.getInt("errorCode")==2){
					imageInfo(data, handler);
					return;
				}
				JSONArray jsonArray=jsonObject.getJSONArray("data");
				for(int i=0;i<jsonArray.length();i++){
						data.comment=((JSONObject)jsonArray.get(i)).getString("comment");
						data.usr_id=((JSONObject)jsonArray.get(i)).getInt("usr_id");
						data.create_time=((JSONObject)jsonArray.get(i)).getLong("create_time");
//						handler.sendEmptyMessage(1);
				}
				}
			otherUserInfo(data,handler);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 添加关注
	 * @param data
	 * @param handler
	 */
	public static void userAddFollow(UserImagesJson.Data data,Handler handler) {
		String url =Constants.USER_FOLLOW;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "usr_id=" + data.usr_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "usr_id=" + data.usr_id + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me","添加关注url="+url);
			LogUtil.i("me", "添加关注返回结果" + result);
			JSONObject jsonObject=null;
			if (resultCode == HttpStatus.SC_OK) {
				LoginJson json=parseJson(result);
				if(json!=null){
					if(json.data.isSuccess){
						handler.sendEmptyMessage(OtherUserTopicActivity.ADD_SUCCESS);
					}else{
						handler.sendEmptyMessage(OtherUserTopicActivity.ADD_FAIL);
					}
				}
				}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public static void userDeleteFollow(UserImagesJson.Data data,Handler handler) {
		String url =Constants.USER_UNFOLLOW;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "usr_id=" + data.usr_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "usr_id=" + data.usr_id + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me","取消关注url="+url);
			LogUtil.i("me", "取消关注返回结果" + result);
			JSONObject jsonObject=null;
			if (resultCode == HttpStatus.SC_OK) {
				LoginJson json=parseJson(result);
				if(json!=null){
					if(json.data.isSuccess){
						handler.sendEmptyMessage(OtherUserTopicActivity.DELETE_SUCCESS);
					}else{
						handler.sendEmptyMessage(OtherUserTopicActivity.DELETE_FAIL);
					}
				}
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
	 * 关注列表
	 * @param handler
	 * @param last_id
	 * @param mode  1 关注列表；2 被关注列表
	 */
	public static void followList(Handler handler,int last_id,int  mode) {
		String SIG=null;
		String url = null;
		String param=null;
		if(mode==1){
			url=Constants.USER_FOLLOWING;
		}else{
			url=Constants.USER_FOLLOWER;
		}
		if(last_id==-1){
			String value ="dog&cat";
			SIG = getMD5(value);
			param = "sig="+SIG+"&SID="+Constants.SID;
		}else{
			String value ="follow_id="+last_id+"dog&cat";
			SIG = getMD5(value);
			param ="follow_id="+last_id+"&sig="+SIG+"&SID="+Constants.SID;
		}
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());

			
			// TODO
			LogUtil.i("me", "下载图片列表url==" + url);
			LogUtil.i("me", "下载图片返回结果" + result);
			if (resultCode == HttpStatus.SC_OK) {
				UserImagesJson json=new UserImagesJson(result);
				Message msg=handler.obtainMessage();
				msg.obj=json;
				msg.what=Constants.MESSAGE_DOWNLOAD_IMAGES_LIST;
				handler.sendMessage(msg);
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
	 * 其他用户个人信息
	 * @param data
	 * @param handler
	 */
	public static void otherUserInfo(UserImagesJson.Data data,Handler handler) {
		String url =Constants.OTHER_INFO;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "usr_id=" + data.usr_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "usr_id=" + data.usr_id + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me","获得其他用户信息url="+url);
			LogUtil.i("me", "获得其他用户信息" + result);
			/*
			 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
			 * "confVersion":"1.0",
			 * "data":{
			 *        "user":{"usr_id":"2","name":"2345","gender":"2",
			 *              "tx":"2_png","age":"1","type":"2","code":"a6hquy",
			 *              "inviter":"0","create_time":"0",
			 *              "update_time":"2014-06-10 03:50:50"},
			 *        "isFriend":false},
			 *"currentTime":1402297428}
			 */
			JSONObject jsonObject=null;
			if (resultCode == HttpStatus.SC_OK) {
				jsonObject = new JSONObject(result);
				int errorCode=jsonObject.getInt("errorCode");
				if(errorCode==2){
					otherUserInfo(data, handler);
					return;
				}
				if(errorCode==0){
					JSONObject jsonArray=jsonObject.getJSONObject("data");
						    data.user=new User();
						    data.isFriend=jsonArray.getBoolean("isFriend");
							data.user.age=((JSONObject)jsonArray.getJSONObject("user")).getString("age");
							data.user.nickName=((JSONObject)jsonArray.getJSONObject("user")).getString("name");
							data.user.classs=((JSONObject)jsonArray.getJSONObject("user")).getInt("type");
							data.user.gender=((JSONObject)jsonArray.getJSONObject("user")).getInt("gender");
							data.user.iconUrl=((JSONObject)jsonArray.getJSONObject("user")).getString("tx");
							if(handler!=null)
							handler.sendEmptyMessage(1);
				}

				}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String  downloadImage(String methodPath,String imagePath,Handler handler){
		String value = "dog&cat";
		String SIG = getMD5(value);
		boolean flag=false;
		String url=methodPath+imagePath;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		BufferedInputStream bis=null;
		BufferedOutputStream bos=null;
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

//			String result = EntityUtils.toString(response.getEntity());

			if (resultCode == HttpStatus.SC_OK) {
				LogUtil.i("me", "url" + url);
//				LogUtil.i("me", "info返回结果" + result);
//				InfoJson infoJson=parseInfoJson(result,activity);
//				Constants.infoJson=infoJson;
				HttpEntity entity=response.getEntity();
				InputStream is=entity.getContent();
				File file=new File(Constants.Picture_Topic_Path);
				if(!file.exists()){
					file.mkdirs();
				}
				byte[] buffer=new byte[1024*8];
				int len=0;
				bis=new BufferedInputStream(is);
				bos=new BufferedOutputStream(new FileOutputStream(new File(Constants.Picture_Topic_Path+File.separator+imagePath)));
				LogUtil.i("me","downloadfile="+Constants.Picture_Topic_Path+File.separator+imagePath);
				while((len=bis.read(buffer, 0, buffer.length))!=-1){
					bos.write(buffer, 0, len);
				}
				bos.flush();
				flag=true;
				if(handler==null)return Constants.Picture_Topic_Path+File.separator+imagePath;
				Message msg=handler.obtainMessage();
				msg.what=1;
				msg.obj=Constants.Picture_Topic_Path+File.separator+imagePath;
				
				handler.sendMessage(msg);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		}
		return null;
	}
	public static boolean  downloadIconImage(String methodPath,String imagePath,Handler handler){
		String value = "dog&cat";
		String SIG = getMD5(value);
		boolean flag=false;
		String url=methodPath+imagePath;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		BufferedInputStream bis=null;
		BufferedOutputStream bos=null;
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

//			String result = EntityUtils.toString(response.getEntity());

			if (resultCode == HttpStatus.SC_OK) {
				LogUtil.i("me", "url" + url);
//				LogUtil.i("me", "info返回结果" + result);
//				InfoJson infoJson=parseInfoJson(result,activity);
//				Constants.infoJson=infoJson;
				HttpEntity entity=response.getEntity();
				InputStream is=entity.getContent();
				File file=new File(Constants.Picture_ICON_Path);
				if(!file.exists()){
					file.mkdirs();
				}
				byte[] buffer=new byte[1024*8];
				int len=0;
				bis=new BufferedInputStream(is);
				bos=new BufferedOutputStream(new FileOutputStream(new File(Constants.Picture_ICON_Path+File.separator+imagePath)));
				LogUtil.i("me","downloadfile="+Constants.Picture_ICON_Path+File.separator+imagePath);
				while((len=bis.read(buffer, 0, buffer.length))!=-1){
					bos.write(buffer, 0, len);
				}
				bos.flush();
				flag=true;
				if(handler==null)return true;
				Message msg=handler.obtainMessage();
				msg.what=2;
				msg.obj=Constants.Picture_ICON_Path+File.separator+imagePath;
				
				handler.sendMessage(msg);
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		}
		return flag;
	}

	/**
	 * 删除照片
	 * 
	 * @param topic
	 */
	public static void deleteImage(Topic topic) {
		String url = "http://" + Constants.IP + Constants.LIKE_IMAGE_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "ima_id=" + topic.img_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "&ima_id=" + topic.img_id + "&sig=" + SIG + "&SID=["
				+ "]";
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me", "��½���ؽ��������" + result);
			if (resultCode == HttpStatus.SC_OK) {

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getMD5(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes("UTF-8"));
			byte[] data = md5.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : data) {
				if ((b & 0xFF) < 0X10)
					sb.append("0");
				sb.append(Integer.toHexString(b & 0xFF));
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

	public static String getIMEI(Context context) {
		String uid = "";
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		uid = tm.getDeviceId();
		return uid;
	}

	/**
	 * 登陆结果解析
	 * 
	 * @param json
	 * @return
	 */
	public static LoginJson parseJson(String json) {
		JSONObject jsonObject;
		try {
			/*
			 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
			 * "confVersion":"1.0",
			 * "data":{"isSuccess":false,"SID":"v49j7o9hn4jb1r65acho2t34n4"},
			 * "currentTime":1401420697}
			 */
			jsonObject = new JSONObject(json);
			LoginJson loginJson = new LoginJson();
			loginJson.state = jsonObject.getInt("state");
			loginJson.errorCode = jsonObject.getInt("errorCode");
			loginJson.errorMessage = jsonObject.getString("errorMessage");
			loginJson.version = jsonObject.getString("version");
			loginJson.confVersion = jsonObject.getString("confVersion");
			loginJson.currentTime = jsonObject.getLong("currentTime");
			JSONObject data = jsonObject.getJSONObject("data");
			loginJson.data = new LoginJson.Data();
			loginJson.data.isSuccess = data.getBoolean("isSuccess");
			if(json.contains("SID")){
				loginJson.data.SID = data.getString("SID");
			}

			return loginJson;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 注册返回结果解析
	 * 
	 * @param json
	 * @return
	 */
	public static RegisterJson parseRegisterJson(String json) {
		JSONObject jsonObject;
		try {
			/*
			 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
			 * "confVersion":"1.0", "data":{"isSuccess":false},
			 * "currentTime":1401420697}
			 */
			jsonObject = new JSONObject(json);
			RegisterJson loginJson = new RegisterJson();
			loginJson.state = jsonObject.getInt("state");
			loginJson.errorCode = jsonObject.getInt("errorCode");
			loginJson.errorMessage = jsonObject.getString("errorMessage");
			loginJson.version = jsonObject.getString("version");
			loginJson.confVersion = jsonObject.getString("confVersion");
			loginJson.currentTime = jsonObject.getLong("currentTime");
			JSONObject data = jsonObject.getJSONObject("data");
			loginJson.data = new RegisterJson.Data();
			loginJson.data.isSuccess = data.getBoolean("isSuccess");
			return loginJson;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static InfoJson parseInfoJson(String json,Activity activity) {
		JSONObject jsonObject;
		try {
			/*
			 * info返回结果{ "state":0," errorCode":0, "errorMessage":"",
			 * "version":"1.0", "confVersion":"1.0", "data":[ {"usr_id":"8",
			 * "name":"23", "gender":"1", "tx":null, "age":"1", "type":"1",
			 * "code":"co9rg7", "inviter":"0", "create_time":"0",
			 * "update_time":"2014-06-05 06:48:46" }],"currentTime":1401951674}
			 */
			jsonObject = new JSONObject(json);
			InfoJson infoJson = new InfoJson();
			infoJson.state = jsonObject.getInt("state");
			infoJson.errorCode = jsonObject.getInt("errorCode");
			infoJson.errorMessage = jsonObject.getString("errorMessage");
			infoJson.version = jsonObject.getString("version");
			infoJson.confVersion = jsonObject.getString("confVersion");
			infoJson.currentTime = jsonObject.getLong("currentTime");
			infoJson.data=new ArrayList<InfoJson.Data>();
			JSONArray data = jsonObject.getJSONArray("data");
			for(int i=0;i<data.length();i++){
				JSONObject object=(JSONObject)data.get(i);
				InfoJson.Data d = new InfoJson.Data();
				d.age = object.getString("age");
				d.code=object.getString("code");
				d.create_time=object.getString("create_time");
				d.gender=object.getInt("gender");
				d.inviter=object.getString("inviter");
				/*try {*/
					d.name=/*URLDecoder.decode(*/object.getString("name")/*,"UTF-8")*/;
				/*} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				d.tx=object.getString("tx");
				d.type=object.getInt("type");
				d.update_time=object.getString("update_time");
				d.user_id=object.getInt("usr_id");
				User user=new User();
				SharedPreferences sp=activity.getSharedPreferences("temp.xml",Context.MODE_WORLD_WRITEABLE);
				user.age=d.age;
				user.classs=d.type;
				user.gender=d.gender;
				user.iconPath=sp.getString("icon", "");
				user.nickName=d.name;
				user.race=sp.getString("race", "");
				user.iconUrl=d.tx;
				user.userId=d.user_id;
				Constants.user=user;
				infoJson.data.add(d);
			}
			
			return infoJson;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static UserImagesJson parseUpdateImageJson(String json){
		JSONObject jsonObject;
			/*
			 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
			 * "confVersion":"1.0",
			 * "data": "data":[{
                           *"like":0,"url":"40_0.1402052236803.jpg",
                           *"file":"","usr_id":"40","comment":"",
                           *"create_time":1402052240,"img_id":"5",
                           *"update_time":null}],
			 * "currentTime":1402043245}
			 */
			try {
				jsonObject = new JSONObject(json);

			UpdateImageJson imageJson = new UpdateImageJson();
			imageJson.state = jsonObject.getInt("state");
			imageJson.errorCode = jsonObject.getInt("errorCode");
			imageJson.errorMessage = jsonObject.getString("errorMessage");
			imageJson.version = jsonObject.getString("version");
			imageJson.confVersion = jsonObject.getString("confVersion");
			imageJson.currentTime = jsonObject.getLong("currentTime");
			imageJson.data=new UpdateImageJson.Data();
			JSONArray data = jsonObject.getJSONArray("data");
			Topic topic=null;
			for(int i=0;i<data.length();i++){
				JSONObject object=data.getJSONObject(i);
				imageJson.data.url=object.getString("url");
				imageJson.data.likes=object.getInt("likes");
				imageJson.data.img_id=object.getInt("img_id");
				imageJson.data.usr_id=object.getInt("usr_id");
				imageJson.data.comment=object.getString("comment");
				UserImagesJson userImagesJson=new UserImagesJson();
				userImagesJson.errorCode=imageJson.errorCode;
				UserImagesJson.Data data1=new UserImagesJson.Data();
				data1.user=Constants.user;
				data1.url=imageJson.data.url;
				data1.likes=imageJson.data.likes;
				data1.img_id=imageJson.data.img_id;
				data1.usr_id=imageJson.data.usr_id;
				data1.comment=imageJson.data.comment;
				userImagesJson.datas=new ArrayList<UserImagesJson.Data>();
				userImagesJson.datas.add(data1);
				return userImagesJson;
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
	public static boolean judgeSID(int errorCode){
		if(errorCode==2){
			login(null, null);
			return true;
		}
		return false;
	}
}
