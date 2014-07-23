package com.aidigame.hisun.pet.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.internet.NewsAddress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ViewDebug.FlagToString;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.json.ActivityJson;
import com.aidigame.hisun.pet.http.json.ActivityJson.Reward;
import com.aidigame.hisun.pet.http.json.InfoJson;
import com.aidigame.hisun.pet.http.json.LoginJson;
import com.aidigame.hisun.pet.http.json.MessagJson;
import com.aidigame.hisun.pet.http.json.RegisterJson;
import com.aidigame.hisun.pet.http.json.UpdateIconJson;
import com.aidigame.hisun.pet.http.json.UpdateImageJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.http.json.UserJson.Data;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.OtherUserTopicActivity;
import com.aidigame.hisun.pet.ui.SubmitPictureActivity;
import com.aidigame.hisun.pet.ui.UserHomepageActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.widget.ShowDialog;

public class HttpUtil {

	/**
	 * 注册
	 * 
	 * @param user
	 */
	public static void register(Handler handler, User user,Activity activity) {
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
				+ user.gender + /* "&name="+name+ */"&type=" + user.race
				+ "dog&cat";
		if(Constants.accessToken!=null){
			value = "age=" + user.age + "&code="/* +1 */+ "&gender="
					+ user.gender + /* "&name="+name+ */"&type=" + user.race+"&weibo="+Constants.accessToken.getUid()
					+ "dog&cat";
		}
		
		LogUtil.i("me", "value" + value);
		String SIG = null;
		SIG = getMD5(value);
		String param = "&age=" + user.age + "&code="/* +1 */+ "&gender="
				+ user.gender + "&name=" + name + "&type=" + user.race
				+ "&sig=" + SIG + "&SID=" + Constants.SID;
		if(Constants.accessToken!=null){
			param = "&age=" + user.age + "&code="/* +1 */+ "&gender="
					+ user.gender + "&name=" + name + "&type=" + user.race
					+"&weibo="+Constants.accessToken.getUid()
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		}
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
					register(handler,user,activity);
					return;
				}
				if(loginJson.errorCode==1||loginJson.errorCode==-1){
					Message msg=handler.obtainMessage();
					msg.obj=loginJson.errorMessage;
					msg.what=Constants.REGISTER_FAIL;
					handler.sendMessage(msg);
					return;
				}
				if (loginJson.data.isSuccess) {
					//上传头像
					String path=uploadUserIcon(user.iconPath,activity);
					if(path==null){
						//上传头像失败
					}
					Constants.isSuccess=true;
					handler.sendEmptyMessage(Constants.REGISTER_SUCCESS);
				    info(null, activity);
				}
			}else{
				if(ShowDialog.count==0)
				ShowDialog.show(judgeError(resultCode), activity);
			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0)
				ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
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
	public static void login(Activity context, Handler handler) {
		if(Constants.accessToken!=null)
		LogUtil.i("me", "新浪微博授权token:"+Constants.accessToken.getToken());
		String uid = getIMEI(context);
		
		if(context==null){
			uid=Constants.IMIE;
			
		}else{
			uid = getIMEI(context);
			Constants.IMIE=uid;
			if(uid==null||"null".equals(uid)||"".equals(uid)){
				uid=getUniqueID(context);
			}
		}
		
		String url = "http://" + Constants.IP + Constants.LOGIN_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "uid=" + uid + "dog&cat";
		String SIG = getMD5(value);
		String param = "&uid=" + uid + "&sig=" + SIG;
		url = url + param;
		HttpGet get = new HttpGet(url);
		LogUtil.i("scroll", "Constants.IMIE=="+Constants.IMIE);
		if(Constants.OPEN_UDID!=null)
		LogUtil.i("scroll", "Constants.OPEN_UDID=="+Constants.OPEN_UDID);
		try {
			long t1=System.currentTimeMillis();
			HttpResponse response = client.execute(get);
			LogUtil.i("me","登陆用时："+(System.currentTimeMillis()-t1));
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());

			
			// TODO
			LogUtil.i("me", "登陆url==" + url);
			LogUtil.i("me", "登陆的返回结果" + result);
			if (resultCode == HttpStatus.SC_OK) {
				boolean flag=JsonUtil.getErrorMessage(result,context);
				
				LoginJson loginJson = parseJson(result);
				if(loginJson!=null){
					Constants.isSuccess = loginJson.data.isSuccess;
					Constants.SID = loginJson.data.SID;

					if(handler!=null)
						handler.sendEmptyMessage(Constants.LOGIN_SUCCESS);
				}
				
//				if(handler!=null)
//				handler.sendEmptyMessage(Constants.LOGIN_SUCCESS);
				
			}else{
				if(ShowDialog.count==0)
				ShowDialog.show(judgeError(resultCode), context);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, context);
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
	public static boolean info(User user,Activity activity) {
		String url = "http://" + Constants.IP + Constants.INFO_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
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
					return info(user, activity);
				
				}
				if(infoJson.errorCode==0)flag=true;
				Constants.infoJson=infoJson;
				SharedPreferences sp=activity.getSharedPreferences("temp.xml", Context.MODE_WORLD_WRITEABLE);
				String path=sp.getString("icon", null);
				//TODO 下载图片
//				uploadUserIcon(path);
			}else{
				if(ShowDialog.count==0)
				ShowDialog.show(judgeError(resultCode), activity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0)
				ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return flag;
	}

	/**
	 * 上传图片
	 * 
	 * @param path
	 *            
	 * @return
	 */
	
	public static boolean uploadImage(String path,String info,Handler handler,Activity activity) {
		boolean flag = false;
		UserImagesJson  json=post(path,info,activity);
		UserImagesJson.Data data=null;
		if(json!=null&&json.errorCode==-1){
			if(ShowDialog.count==0&&json.errorMessage!=null)
				ShowDialog.show(json.errorMessage, activity);
			handler.sendEmptyMessage(SubmitPictureActivity.DISMISS_PROGRESS);
			return false;
		}
		if(json!=null) {
			data=json.datas.get(0);
			if(json.errorCode==2){
				return uploadImage(path, info, handler,activity);
				
			}
		}else{
			if(handler!=null)handler.sendEmptyMessage(SubmitPictureActivity.UPLOAD_IMAGE_FAILS);
			return false;
		}
		
		if(data!=null){
			Message msg=handler.obtainMessage();
			msg.what=SubmitPictureActivity.UPLOAD_IMAGE_SUCCESS;
			msg.obj=data;
			Constants.user.imagesCount+=1;
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
    public static UserImagesJson post(String path,String info,Activity activity){
    	boolean flag = false;
		String value = "dog&cat";
		String SIG = getMD5(value);
		String url = "http://" +Constants.IP+ Constants.UPLOAD_IMAGE_PATH + SIG + "&SID="
				+ Constants.SID;
    	Map<String, String> params=new HashMap<String, String>();
    	/*try {
			info=URLEncoder.encode(info, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
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
        HttpURLConnection conn =null;
        InputStreamReader reader=null;
        try {
    			uri = new URL(url);
    		if(uri==null)return null;
        	conn = (HttpURLConnection) uri.openConnection();
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
	        reader=new InputStreamReader(in, Charset.forName("UTF-8"));
	        
	        StringBuilder sb2 = new StringBuilder();
	        if (res == 200) {
	            int ch;
	            while ((ch = reader.read()) != -1) {
	                sb2.append((char) ch);
	            }
	        }
	        outStream.close();
	        conn.disconnect();
	        sb2.toString();
	        LogUtil.i("me", "上传图片url===="+url);
	        LogUtil.i("me", "返回结果===="+sb2.toString());
//	        UpdateImageJson imaeJson=parseUpdateImageJson(sb2.toString());
	        UserImagesJson data=parseUpdateImageJson(sb2.toString());
	        if(data!=null){
	        	return data;
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		}finally{
			if(outStream!=null){
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(reader!=null){
				try {
					reader.close();
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
			if(conn!=null){
				conn.disconnect();
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
    public static String uploadUserIcon(String path,final Activity activity) {
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
        HttpURLConnection conn =null;

        try {
            URL url = new URL(RequestURL);
            conn = (HttpURLConnection) url.openConnection();
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
                	return uploadUserIcon(path,activity);
                }
                
                return json.data.tx;
                // }
                // else{
                // Log.e(TAG, "request error");
                // }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if(ShowDialog.count==0)
            ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
        } catch (IOException e) {
            e.printStackTrace();
            if(ShowDialog.count==0)
            ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
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
			if(conn!=null){
				conn.disconnect();
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
	public static void downloadUserHomepage(Handler handler,int last_id,int  mode,final Activity activity) {
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
					login(null, null);
					downloadUserHomepage(handler, last_id, mode,activity);
					return;
				}
				Message msg=handler.obtainMessage();
				msg.obj=json;
				msg.what=Constants.MESSAGE_DOWNLOAD_IMAGES_LIST;
				handler.sendMessage(msg);
			}else{
//				ShowDialog.show(judgeError(resultCode), activity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
			handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
		}
	}
	/**
	 * 下载其他用户列表
	 * @param handler
	 * @param last_id
	 * @param usr_id
	 */
	public static void downloadOtherUserHomepage(Handler handler,int last_id,UserImagesJson.Data data,final Activity activity) {
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

				UserImagesJson json=new UserImagesJson(result);
				if(json.errorCode==2){
					login(null, null);
					downloadOtherUserHomepage(handler, last_id, data,activity);
					return;
				}
				Message msg=handler.obtainMessage();
				msg.obj=json;
				msg.what=Constants.MESSAGE_DOWNLOAD_IMAGES_LIST;
				handler.sendMessage(msg);
			}else{
				if(ShowDialog.count==0)
				ShowDialog.show(judgeError(resultCode), activity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		} /*catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	/**
	 * 点赞
	 * 
	 * @param topic
	 */
	public static boolean likeImage(UserImagesJson.Data  data,TextView tv,Handler handler) {
		boolean flag=false;
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
				RegisterJson json=parseRegisterJson(result);
				if(json!=null){
					if(json.errorCode==0){
						data.likes+=1;
						
						if(data.likers_icons_urls==null){
							data.likers_icons_urls=new ArrayList<String>();
							data.likers_icons_urls.add(Constants.user.iconUrl);
						}else{
								data.likers_icons_urls.add(Constants.user.iconUrl);
						}
						if(data.likersString==null){
							
							data.likersString=""+Constants.user.userId;
						}else{
							data.likersString+=","+Constants.user.userId;
						}
						if(StringUtil.isEmpty(data.likers_icons_url_strString)&&data.user.iconUrl!=null){
							data.likers_icons_url_strString+=","+data.user.iconUrl;
						}else if(data.user.iconUrl!=null){
							data.likers_icons_url_strString=data.user.iconUrl;
						}
						if(handler!=null){
							Message msg=handler.obtainMessage();
							msg.obj=tv;
							msg.what=44;
							msg.arg1=data.likes;
							handler.sendMessage(msg);
						}
						
						return true;
					}if(json.errorCode==-1){
						LogUtil.i("exception","您已经点过赞了");
						
						if(handler!=null){
							Message msg=handler.obtainMessage();
							msg.what=Constants.ERROR_MESSAGE;
							msg.obj=json.errorMessage;
							handler.sendMessage(msg);
						}
						
						
					}
				}
				

			}else{
//				ShowDialog.show(judgeError(resultCode), c);
			}
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
	 * 根据图片id获得图片的详细信息
	 * @param data
	 */
	public static boolean imageInfo(UserImagesJson.Data data,Handler handler,final Activity activity) {
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
					login(null, null);
					imageInfo(data, handler,activity);
					return false;
				}
				JSONObject jsonArray=jsonObject.getJSONObject("data");
				JSONObject image=jsonArray.getJSONObject("image");
						data.comment=image.getString("cmt");
						data.usr_id=image.getInt("usr_id");
						data.create_time=image.getLong("create_time");
						data.likersString=image.getString("likers");
						data.likes=image.getInt("likes");
						data.comments=image.getString("comments");
						if(data.comments!=null){
							String[] strs=data.comments.split(";usr_id");
							
							// * :143,name:小小猫,body:省钱打老虎,create_time:1404992978,
							 
							if(strs!=null&&strs.length>1){
								UserImagesJson.Comments comment=null;
								data.listComments=new ArrayList<UserImagesJson.Comments>();
								for(int i=1;i<strs.length;i++){
									comment=new UserImagesJson.Comments();
									String cstr=strs[i];
									int start=cstr.indexOf(":");
									int end=cstr.indexOf(",");
									comment.usr_id=Integer.parseInt(cstr.substring(start+1, end));
									cstr=cstr.substring(end+1);
									start=cstr.indexOf(":");
									end=cstr.indexOf(",");
									comment.name=cstr.substring(start+1, end);
									cstr=cstr.substring(end+1);
									start=cstr.indexOf(":");
									end=cstr.indexOf(",");
									comment.body=cstr.substring(start+1, end);
									cstr=cstr.substring(end+1);
									start=cstr.indexOf(":");
									end=cstr.indexOf(",");
									if(end<0||end>cstr.length()){
										end=cstr.length();
									}
									comment.create_time=Long.parseLong(cstr.substring(start+1,end));
									data.listComments.add(comment);
									
									
								}
								if(data.listComments!=null){
									ArrayList<UserImagesJson.Comments> temp=new ArrayList<UserImagesJson.Comments>();
									for(int i=data.listComments.size()-1;i>=0;i--){
										temp.add(data.listComments.get(i));
									}
									data.listComments=temp;
								}
							}
						}
						String tx=jsonArray.getString("liker_tx");
						if(tx!=null&&!"null".equals(tx)){
							JSONArray arrays=jsonArray.getJSONArray("liker_tx");
							if(arrays!=null&&arrays.length()>0){
								ArrayList<String> strs=new ArrayList<String>();
								for(int i=0;i<arrays.length();i++){
									strs.add(arrays.getString(i));
								}
								data.likers_icons_urls=strs;
							}
						}
						
//						handler.sendEmptyMessage(1);
				
				}else{
					if(ShowDialog.count==0)
					ShowDialog.show(judgeError(resultCode), activity);
				}
			return otherUserInfo(data,handler,activity);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		}
		return false;
	}
	/**
	 * 添加关注
	 * @param data
	 * @param handler
	 */
	public static boolean userAddFollow(UserImagesJson.Data data,Handler handler,Activity activity) {
		String url =Constants.USER_FOLLOW;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "usr_id=" + data.usr_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "usr_id=" + data.usr_id + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		boolean flag=false;
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
					if(judgeSID(json.errorCode)){
						return userAddFollow(data, handler, activity);
					}
					if(json.data.isSuccess){
						flag=true;
						if(handler!=null)
						handler.sendEmptyMessage(OtherUserTopicActivity.ADD_SUCCESS);
					}else{
						if(handler!=null)
						handler.sendEmptyMessage(OtherUserTopicActivity.ADD_FAIL);
					}
				}
				}else{
					if(ShowDialog.count==0)
					ShowDialog.show(judgeError(resultCode), activity);
				}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		} 
		return flag;
	}
	public static boolean userDeleteFollow(UserImagesJson.Data data,Handler handler,Activity activity) {
		String url =Constants.USER_UNFOLLOW;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "usr_id=" + data.usr_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "usr_id=" + data.usr_id + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		boolean flag=false;
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
					if(judgeSID(json.errorCode)){
						return userDeleteFollow(data, handler, activity);
					}
					if(json.data.isSuccess){
						flag=true;
						if(handler!=null)
						handler.sendEmptyMessage(OtherUserTopicActivity.DELETE_SUCCESS);
					}else{
						if(handler!=null)
						handler.sendEmptyMessage(OtherUserTopicActivity.DELETE_FAIL);
					}
				}
				}else{
					if(ShowDialog.count==0)
					ShowDialog.show(judgeError(resultCode), activity);
				}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} 
		return flag;
	}
	/**
	 * 关注列表
	 * @param handler
	 * @param last_id
	 * @param mode  1 关注列表；2 被关注列表
	 */
	public static void followList(Handler handler,int last_id,int  mode,Activity activity) {
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
			String value ="usr_id="+last_id+"dog&cat";
			SIG = getMD5(value);
			param ="usr_id="+last_id+"&sig="+SIG+"&SID="+Constants.SID;
		}
		url=url+param;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());

			
			// TODO
			LogUtil.i("me", "关注与被关注列表url==" + url);
			LogUtil.i("me", "关注与被关注列表返回结果" + result);
			if (resultCode == HttpStatus.SC_OK) {
				UserJson userJson=new UserJson(result,2);
				Message msg=handler.obtainMessage();
				if(judgeSID(userJson.errorCode)){
					followList(handler, last_id, mode, activity);
					return ;
				}
				msg.obj=userJson;
				if(mode==1){
					msg.what=UserHomepageActivity.MESSAGE_SHOW_FOLLOWING;
					handler.sendMessage(msg);
				}else{
					msg.what=UserHomepageActivity.MESSAGE_SHOW_FOLLOWER;
					handler.sendMessage(msg);
				}
			}else{
				if(ShowDialog.count==0)
				ShowDialog.show(judgeError(resultCode), activity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		}
	}
	/**
	 * 根据一串user_id，获取多个user信息（头像，性别等）
	 * @param data
	 * @param handler
	 * @return
	 */
	public static String[] getOthersList(String likers,Handler handler,Activity activity) {
		String url =Constants.OTHERS_INFO;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "usr_ids=" + likers + "dog&cat";
		String SIG = getMD5(value);
		String param = "usr_ids=" + likers + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		boolean flag=false;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me","获取一串用户信息的url="+url);
			LogUtil.i("me", "获取一串用户信息的返回结果" + result);
			JSONObject jsonObject=null;
			if (resultCode == HttpStatus.SC_OK) {
				UserJson json=new UserJson();
				json.datas=new ArrayList<UserJson.Data>();
				JSONObject object1=new JSONObject(result);
				json.errorCode=object1.getInt("errorCode");
				if(judgeSID(json.errorCode)){
					return getOthersList(likers, handler, activity);
				}
				json.errorMessage=object1.getString("errorMessage");
				if(json.errorCode==0){
					JSONArray object2=object1.getJSONArray("data");
					if(object2!=null&&object2.length()>0){
						JSONArray array=object2.getJSONArray(0);
						if(array!=null&&array.length()>0){
							/*
							 * {"usr_id":"47","name":"1\u697c\u4e3b","gender":"1",
							 * "tx":"47_47_prev3.png","age":"1","type":"1","code":"4dkrc3",
							 * "inviter":"0","create_time":"0",
							 * "update_time":"2014-06-19 09:05:08"},
							 */
							JSONObject object3=null;
							UserJson.Data data=null;
							User user=null;
							String[] urls=new String[array.length()];
							for(int i=0;i<array.length();i++){
								object3=array.getJSONObject(i);
								data=new UserJson.Data();
								user=new User();
								user.userId=object3.getInt("usr_id");
								user.nickName=object3.getString("name");
								user.gender=object3.getInt("gender");
								user.iconUrl=object3.getString("tx");
								urls[i]=user.iconUrl;
								user.age=object3.getString("age");
								user.race=object3.getString("type");
								user.code=object3.getString("code");
								user.inviter=object3.getString("inviter");
								user.create_time=object3.getString("create_time");
								user.update_time=object3.getString("update_time");
								data.user=user;
								json.datas.add(data);
								
							}
							if(handler==null)return urls;
							Message msgMessage=handler.obtainMessage();
							msgMessage.obj=json;
							
							msgMessage.what=1;
							handler.sendMessage(msgMessage);
						}
					}
				}
				}else{
					if(ShowDialog.count==0)
					ShowDialog.show(judgeError(resultCode), activity);
				}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} 
		return null;
	}
	/**
	 * 其他用户个人信息
	 * @param data
	 * @param handler
	 */
	public static boolean otherUserInfo(UserImagesJson.Data data,Handler handler,Activity activity) {
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
				if(judgeSID(errorCode)){
					otherUserInfo(data, handler, activity);
					return false;
				
				}
				if(errorCode==2){
					otherUserInfo(data, handler,activity);
					return false;
				}
				if(errorCode==0){
					String str=jsonObject.getString("data");
					if(str==null||"null".equals(str))return false;
				    
					JSONObject jsonArray=jsonObject.getJSONObject("data");
						    data.user=new User();
						    data.isFriend=jsonArray.getBoolean("isFriend");
							data.user.age=((JSONObject)jsonArray.getJSONObject("user")).getString("age");
							data.user.nickName=((JSONObject)jsonArray.getJSONObject("user")).getString("name");
							data.user.race=((JSONObject)jsonArray.getJSONObject("user")).getString("type");
							data.user.gender=((JSONObject)jsonArray.getJSONObject("user")).getInt("gender");
							data.user.iconUrl=((JSONObject)jsonArray.getJSONObject("user")).getString("tx");
							data.user.userId=((JSONObject)jsonArray.getJSONObject("user")).getInt("usr_id");
							if(handler!=null)
							handler.sendEmptyMessage(1);
							return true;
				}

				}else{
					if(ShowDialog.count==0)
					ShowDialog.show(judgeError(resultCode), activity);
				}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			if(handler!=null)
				handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0);
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
//			if(handler!=null)
//			handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0);
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			if(handler!=null)
			handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
		}
		return false;
	}
	public static String  downloadImage(String methodPath,String imagePath,Handler handler,Activity activity){
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
				byte[] buffer=new byte[1024*8];
				int len=0;
				bis=new BufferedInputStream(is);
				LogUtil.i("me", "欢迎页文件" + Constants.Picture_Topic_Path+File.separator+imagePath);
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
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
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
	/**
	 * 
	 * @param data
	 */
	public static void followingList(int id,int last_id,Activity activity) {
		String url = Constants.USER_FOLLOWING;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = null;
		String param =null;
		if(last_id==-1){
			value ="dog&cat";
			String SIG = getMD5(value);
			param = "sig=" + SIG + "&SID="
					+Constants.SID ;
		}else{
			value = "usr_id=" + id + "dog&cat";
			String SIG = getMD5(value);
			param = "usr_id="+id+"&sig=" + SIG + "&SID="
					+Constants.SID ;
		}
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me","关注列表url="+url);
			LogUtil.i("me", "关注列表结果" + result);
			if (resultCode == HttpStatus.SC_OK) {

			}else{
				if(ShowDialog.count==0)
				ShowDialog.show(judgeError(resultCode), activity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0);
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		}
	}
	
	public static String  downloadIconImage(String methodPath,String imagePath,Handler handler,Activity activity){
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
				if(handler==null)return Constants.Picture_ICON_Path+File.separator+imagePath;
				Message msg=handler.obtainMessage();
				msg.what=2;
				msg.obj=Constants.Picture_ICON_Path+File.separator+imagePath;
				
				handler.sendMessage(msg);
				
			}else{
				if(ShowDialog.count==0)
				ShowDialog.show(judgeError(resultCode), activity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
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
	/**
	 * 根据地址下载图片
	 * @param url
	 * @param fileName  文件名(路径加文件名)
	 * @return
	 */
	public static boolean  downloadImage(String url,String fileName){
		boolean flag=false;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		BufferedInputStream bis=null;
		BufferedOutputStream bos=null;
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

//			String result = EntityUtils.toString(response.getEntity());

			if (resultCode == HttpStatus.SC_OK) {
				LogUtil.i("me", "图片下载url" + url);
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
				bos=new BufferedOutputStream(new FileOutputStream(new File(fileName)));
				LogUtil.i("me","downloadfile="+fileName);
				while((len=bis.read(buffer, 0, buffer.length))!=-1){
					bos.write(buffer, 0, len);
				}
				bos.flush();
				flag=true;
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	public static void deleteImage(Topic topic,Activity activity) {
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

			}else{
				if(ShowDialog.count==0)
				ShowDialog.show(judgeError(resultCode), activity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		}
	}
	/**
	 * 下载活动列表
	 * @param activity
	 * @param id
	 * @return
	 */
    public static ActivityJson loadTopicList(Activity activity,int id){
    	String valus="dog&cat";
    	String param=null;
    	String url=Constants.ACTIVITY_LIST;
    	if(id==-1){
    		param=getMD5(valus);
    		
    	}else{
    		valus="topic_id="+id+valus;
    		param=getMD5(valus);
    		url=url+"&topic_id="+id;
    	}
    	
    	url=url+"&sig="+param+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpGet get=new HttpGet(url);
    	try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			if(resultCode==200){
				LogUtil.i("me", "活动列表url="+url);
				String result=EntityUtils.toString(response.getEntity());
				LogUtil.i("me", "活动列表返回结果="+result);
				return new ActivityJson(result);
				
			}else{
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ShowDialog.show("网络异常", activity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    public static void downloadActivityImagesList(Handler handler,int last_id,int  topic_id,final Activity activity,ActivityJson.Data d,int mode) {
		String SIG=null;
		String url = null;
		String param=null;
		if(last_id==-1){
			String value ="topic_id="+d.topic_id+"dog&cat";
			SIG = getMD5(value);
			param ="topic_id="+d.topic_id+ "&sig="+SIG+"&SID="+Constants.SID;
		}else{
			String value ="img_id="+last_id+"&topic_id="+d.topic_id+"dog&cat";
			SIG = getMD5(value);
			param ="img_id="+last_id+"&topic_id="+d.topic_id+"&sig="+SIG+"&SID="+Constants.SID;
		}
		
		switch (mode) {
		case 0:
			url=Constants.ACTIVITY_POPULAR+param;
			break;
		case 1:
			url=Constants.ACTIVITY_NEWEST+param;
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
					login(null, null);
					downloadUserHomepage(handler, last_id, mode,activity);
					return;
				}
				Message msg=handler.obtainMessage();
				msg.obj=json;
				msg.what=Constants.MESSAGE_DOWNLOAD_IMAGES_LIST;
				handler.sendMessage(msg);
			}else{
//				ShowDialog.show(judgeError(resultCode), activity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
			handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
		}
	}
    public static boolean loadActivityInfo(ActivityJson.Data data){
    	String value="topic_id="+data.topic_id+"dog&cat";
    	String url=Constants.ACTIVITY_INFO+"&topic_id="+data.topic_id+"&sig="+getMD5(value)+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpGet get=new HttpGet(url);
    	try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			if(resultCode==200){
				LogUtil.i("me", "活动信息 url="+url);
				String result=EntityUtils.toString(response.getEntity());
				LogUtil.i("me", "活动信息返回结果="+result);
                if(result!=null){
					JSONObject o1=new JSONObject(result);
					int errorCode=o1.getInt("errorCode");
					String errorMessage=o1.getString("errorMessage");
					if(errorCode==0){
						/*
						 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
						 * "confVersion":"1.0",
						 * "data":[{"des":"\u6d3b\u52a8\u6d4b\u8bd52014","remark":"","txs":["69"]}],
						 * "currentTime":1404117270}
						 */
						JSONArray arr=o1.getJSONArray("data");
						if(arr!=null&&arr.length()>0){
							JSONObject o2=arr.getJSONObject(0);
							data.des=o2.getString("des");
							data.remark=o2.getString("remark");
							JSONArray arr2=o2.getJSONArray("txs");
							data.txs="";
							if(arr2!=null&arr2.length()>0){
								for(int i=0;i<arr2.length();i++){
									
									if(i==arr2.length()-1){
										data.txs+=arr2.getString(i);
									}else{
										data.txs+=arr2.getString(i)+",";
									}
								}
								
							}
							return true;
						}
					}else if(errorCode==2){
						login(null, null);
						return loadActivityInfo(data);
					}
				}
				
			}else{
				
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
    	return false;
    }
    /**
     * 获取 奖品详细信息
     * @param data
     * @return
     */
    public static boolean loadRewardInfo(ActivityJson.Data data){
    	String value="topic_id="+data.topic_id+"dog&cat";
    	String url=Constants.ACTIVITY_REWARD_INFO+"&topic_id="+data.topic_id+"&sig="+getMD5(value)+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpGet get=new HttpGet(url);
    	try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			if(resultCode==200){
				LogUtil.i("me", "活动信息 url="+url);
				String result=EntityUtils.toString(response.getEntity());
				LogUtil.i("me", "活动信息返回结果="+result);
                if(result!=null){
					JSONObject o1=new JSONObject(result);
					int errorCode=o1.getInt("errorCode");
					String errorMessage=o1.getString("errorMessage");
					if(errorCode==0){
						
						/*
						 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
						 * "confVersion":"1.0",
						 * "data":[{
						 *          "1":[{"item_id":"1","name":"\u53ef\u53e3\u53ef\u4e50",
						 *                "des":"\u975e\u5e38\u732b\u72d7\u4e4b\u53ef\u53e3\u53ef
						 *                 \u4e50\uff0c\u559d\u5b8c\u5c31\u53d8\u8eab",
						 *                 "img":"","price":"1","create_time":"1404031105",
						 *                 "update_time":"2014-06-30 09:33:25"
						 *            }]
						 *         }],
						 * "currentTime":1404120929}
						 */
						 
						JSONArray arr=o1.getJSONArray("data");
						data.rewards=new ArrayList<ActivityJson.Reward>();
						ActivityJson.Reward reward=null;
						if(arr!=null&&arr.length()>0){
							JSONObject o2=arr.getJSONObject(0);
							JSONArray o3=o2.getJSONArray("1");
							if(o3!=null&&o3.length()>0){
								JSONObject  o4=o3.getJSONObject(0);
								if(o4!=null){
									reward=new Reward();
									reward.item_id=o4.getInt("item_id");
									reward.name=o4.getString("name");
									reward.des=o4.getString("des");
									reward.img=o4.getString("img");
									reward.price=o4.getInt("price");
									data.rewards.add(reward);
								}
							}
							return true;
						}
					}else if(errorCode==2){
						login(null, null);
						return loadRewardInfo(data);
					}
				}
				
			}else{
				
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
    	return false;
    }
    /**
     * 获取消息和活动数目
     * @return
     */
    public static LoginJson.Data getMailAndActivityNum(){
    	boolean flag=false;
    	String param="dog&cat";
    	String value=getMD5(param);
    	String url=Constants.MAIL_ACTIVITY_NEW_NUM+"&sig="+value+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpGet get=new HttpGet(url);
    	try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			if(resultCode==200){
				String result=EntityUtils.toString(response.getEntity());
				LogUtil.i("me", "获取消息和活动数目url="+url);
				LogUtil.i("me", "获取消息和数目返回结果："+result);
				/*
				 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
				 * "data":{"mail_count":"3","topic_count":"1"},
				 * "currentTime":1404794631}
				 */
				if(result!=null){
					JSONObject o1=new JSONObject(result);
					int errorCode=o1.getInt("errorCode");
					String errorMessage=o1.getString("errorMessage");
					if(errorCode==0){
						String str=o1.getString("data");
						if(str!=null&&!"null".equals(str)){
							JSONObject o2=o1.getJSONObject("data");
							LoginJson.Data data=new LoginJson.Data();
							if(str.contains("mail_count")){
								data.mail_count=o2.getInt("mail_count");
							}
							if(str.contains("topic_count")){
								data.topic_count=o2.getInt("topic_count");
							}
							return data;
						}
					}else if(judgeSID(errorCode)){
						getMailAndActivityNum();
					}else if(errorCode==1||errorCode==-1){
						
					}
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
    	return null;
    }
    /**
     * 发表评论
     * @param comment
     * @param id
     * @return
     */
    public static boolean sendComment(String comment,int id){
    	String params="dog&cat";
    	String url=Constants.ADD_A_COMMENT+"&sig="+getMD5(params)+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpPost post=new HttpPost(url);
    	ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
    	NameValuePair pair=new BasicNameValuePair("img_id", ""+id);
    	pairs.add(pair);
    	try {
			pair=new BasicNameValuePair("body",URLEncoder.encode(comment, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	pairs.add(pair);
    	try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response=client.execute(post);
			int resultCode=response.getStatusLine().getStatusCode();
			if(resultCode==200){
				String result=EntityUtils.toString(response.getEntity());
				LogUtil.i("me", "发表评论url="+url);
				LogUtil.i("me", "发表评论返回结果="+result);
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
    /**
     * 消息列表
     * @param mail_id
     * @param is_system
     * @return
     */
    public static MessagJson getMailList(int mail_id,boolean is_system){
    	String param="is_system="+(is_system?1:0);
    	if(mail_id!=-1){
    		param+="&mail_id="+mail_id;
    	}
    	param+="dog&cat";
    	String url=Constants.MAIL_LIST+"&sig="+getMD5(param)+"&SID="+Constants.SID+"&is_system="+(is_system?1:0);
    	if(mail_id!=-1){
    		url+="&mail_id="+mail_id;
    	}
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpGet get=new HttpGet(url);
    	try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			if(resultCode==200){
				String result=EntityUtils.toString(response.getEntity());
				LogUtil.i("me", "消息列表url="+url);
				LogUtil.i("me", "消息列表返回结果:"+result);
				MessagJson json=new MessagJson(result);
				return json;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    /**
     * 删除消息
     * @param dataSystem
     * @return
     */
    public static boolean deleteMail(MessagJson.DataSystem dataSystem){
    	boolean flag=false;
    	if(dataSystem==null)return false;
    	String param="mail_id="+dataSystem.mail_id+"dog&cat";
    	String url=Constants.MAIL_DELETE+"&mail_id="+dataSystem.mail_id+"&sig="+getMD5(param)+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpGet get=new HttpGet(url);
    	try {
			HttpResponse response=client.execute(get);
			int resultCode=response.getStatusLine().getStatusCode();
			if(resultCode==200){
				String result=EntityUtils.toString(response.getEntity());
				LogUtil.i("me", "删除消息url="+url);
				LogUtil.i("me", "删除消息返回结果="+result);
				JSONObject o1=new JSONObject(result);
				int errorCode=o1.getInt("errorCode");
				if(errorCode==0){
					String dataString=o1.getString("data");
					if(dataString!=null&&!"null".equals(dataString)&&dataString.contains("isSuccess")){
						JSONObject o2=o1.getJSONObject("data");
						boolean isSuccess=o2.getBoolean("isSuccess");
						return isSuccess;
					}
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
    	
       return flag;
    }
    public static boolean  sendMail(int to_id,String body){
    	String params="dog&cat";
    	String url=Constants.MAIL_CREATE+"&sig="+getMD5(params)+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpPost post=new HttpPost(url);
    	ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
    	NameValuePair pair=new BasicNameValuePair("to_id", ""+to_id);
    	pairs.add(pair);
    	try {
			pair=new BasicNameValuePair("body",URLEncoder.encode(body,"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	pairs.add(pair);
    	try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response=client.execute(post);
			int resultCode=response.getStatusLine().getStatusCode();
			if(resultCode==200){
				String result=EntityUtils.toString(response.getEntity());
				JSONObject o1=new JSONObject(result);
				String dataStr=o1.getString("data");
				if(dataStr!=null&&!"null".equals(dataStr)&&!"".equals(dataStr)){
					JSONObject o2=new JSONObject(dataStr);
					boolean isSuccess=o2.getBoolean("isSuccess");
					if(isSuccess){
						return true;
					}
				}
				LogUtil.i("me", "发送消息url="+url);
				LogUtil.i("me", "发送消息返回结果="+result);
				
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
    	return false;
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
	/**
	 * 获取种族类别
	 * @param context
	 */

	public static boolean getRaceType(Activity activity){
		boolean flag=false;
		String value ="dog&cat";
		String SIG = getMD5(value);
		String param = "&sig=" + SIG /*+ "&SID="+Constants.SID*/;
		String url=Constants.GET_RACE_TYPE+param;
		DefaultHttpClient client=new DefaultHttpClient();
		HttpGet get=new HttpGet(url);
		try {
			LogUtil.i("me","种族列表url："+url);
			HttpResponse response=client.execute(get);
			
			int resultCode=response.getStatusLine().getStatusCode();
			if(resultCode==200){
				
				String result=EntityUtils.toString(response.getEntity());
				LogUtil.i("me","种族列表返回结果："+result);
				SharedPreferences sp=activity.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
				Editor editor=sp.edit();
				JSONObject o=new JSONObject(result);
				if(judgeSID(o.getInt("errorCode"))){
					return getRaceType(activity);
				
				}
				if(o.getInt("errorCode")==0){
					JSONObject o1=o.getJSONObject("data").getJSONObject("1");
					int length=o1.length();
					int count=0;
					String temp=null;
					for(int i=1;i<=length;i++){
						temp=null;
						count++;
						if(i<10){
							temp=o1.getString("10"+i);
//							temp=StringUtil.revert(temp);
							editor.putString("10"+i, temp);
						}else{
							temp=o1.getString("1"+i);
//							temp=StringUtil.revert(temp);
							editor.putString("1"+i, temp);
						}
						
					}
					editor.putInt("cat", count);
					count=0;
					JSONObject o2=o.getJSONObject("data").getJSONObject("2");
					length=o2.length();
					temp=null;
					for(int i=1;i<=length;i++){
						
						count++;
						if(i<10){
							temp=o2.getString("20"+i);
							temp=StringUtil.revert(temp);
							editor.putString("20"+i, temp);
						}else{
							temp=o2.getString("2"+i);
							temp=StringUtil.revert(temp);
							editor.putString("2"+i, temp);
						}
						
					}
					editor.putInt("dog", count);
					count=0;
					JSONObject o3=o.getJSONObject("data").getJSONObject("3");
					length=o3.length();
					temp=null;
					for(int i=1;i<=length;i++){
						
						count++;
						if(i<10){
							temp=o3.getString("30"+i);
							temp=StringUtil.revert(temp);
							editor.putString("30"+i, temp);
						}else{
							temp=o3.getString("3"+i);
							temp=StringUtil.revert(temp);
							editor.putString("3"+i, temp);
						}
						
					}
					editor.putInt("other", count);
					flag=true;
					editor.putBoolean("race", true);
				}
				
				
				editor.commit();
				editor=null;
				sp=null;
				
			}else{
				if(ShowDialog.count==0)
				ShowDialog.show(judgeError(resultCode), activity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		}
		return flag;
	}
	/*public static boolean getMailList(boolean isSystem){
		
	}*/
	/**
	 * 下载欢迎页图片
	 * @param handler
	 * @return
	 */
	public static String  downloadWelcomeImage(Handler handler,Activity activity){
		
		String value = "dog&cat";
		String SIG = getMD5(value);
		boolean flag=false;
		String url=Constants.WELCOME_IMAGE+"&sig="+SIG;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();
			if(resultCode==200){
				String result=EntityUtils.toString(response.getEntity());
				LogUtil.i("me", "欢迎图片url="+url);
				LogUtil.i("me", "欢迎图片返回结果="+result);
				String urlString=new JSONObject(result).getJSONObject("data").getString("url");
				File file=new File(Constants.Picture_Topic_Path+File.separator+urlString);
				
				
				SharedPreferences sp=activity.getSharedPreferences("setup",Context.MODE_WORLD_READABLE );
				Editor editor=sp.edit();
				if(file!=null&&file.exists()){
					editor.putString("welcome",Constants.Picture_Topic_Path+File.separator+urlString );
					editor.commit();
					return Constants.Picture_Topic_Path+File.separator+urlString;
				}
				String path=downloadImage(Constants.UPLOAD_IMAGE_RETURN_URL,urlString , null,activity);
				if(path!=null)
				editor.putString("welcome", path);
				editor.commit();
				
			}else{
				if(ShowDialog.count==0)
				ShowDialog.show(judgeError(resultCode), activity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
		}
		

		return null;
	}
	/**
	 * 获取android唯一设备编号    IMEI
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		String uid = null;
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		uid = tm.getDeviceId();
		LogUtil.i("scroll", "uid="+uid);
		getAndroid_id(context);
		return uid;
	}
	/**
	 * 若是无法获得设备的IMEI编号，则使用自制唯一ID
	 * @param context
	 * @return
	 */
	public static String getUniqueID(Context context){
		String IMEI=getIMEI(context);
		IMEI=judgeEmpty(IMEI);
		String Pseudo_Unique_ID=getPseudoUniqueID();
		String Android_ID=getAndroid_id(context);
		String WLAN_MAC=getWLAN_MAC_Adress(context);
		String BT_MAC=getBT_MAC_Adress();
		String UNIQUE_ID=IMEI+Pseudo_Unique_ID+Android_ID+WLAN_MAC+BT_MAC;
		try {
			MessageDigest md=MessageDigest.getInstance("MD5");
			md.update(UNIQUE_ID.getBytes(), 0, UNIQUE_ID.length());
			byte[] md5Byte=md.digest();
			UNIQUE_ID="";
			for(int i=0;i<md5Byte.length;i++){
				int b=(0xFF&md5Byte[i]);
				if(b<=0xF){
					UNIQUE_ID+="0";
				}
				UNIQUE_ID+=Integer.toHexString(b);
			}
			UNIQUE_ID=UNIQUE_ID.toUpperCase();
			return UNIQUE_ID;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取android唯一设备编号  Android_ID
	 * @param context
	 * @return
	 */
	public static String getAndroid_id(Context context){
		String android_id=null;
		android_id=Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		android_id=judgeEmpty(android_id);
		LogUtil.i("scroll", "android_id="+android_id);
		return android_id;
	}
	/**
	 * 获得android唯一设备号  
	 * 可以通过取出ROM版本、制造商、CPU型号、以及其他硬件信息来实现这一点。
	 */
	public static String getPseudoUniqueID(){
		String Pseudo_Unique_ID="35"+Build.BOARD.length()%10+
				          Build.BRAND.length()%10+
				          Build.CPU_ABI.length()%10+
				          Build.DEVICE.length()%10 + 
				          Build.DISPLAY.length()%10 + 
				          Build.HOST.length()%10 + 
				          Build.ID.length()%10 + 
				          Build.MANUFACTURER.length()%10 + 
				          Build.MODEL.length()%10 +
				          Build.PRODUCT.length()%10 + 
				          Build.TAGS.length()%10 + 
				          Build.TYPE.length()%10 + 
				          Build.USER.length()%10 ;
		Pseudo_Unique_ID=judgeEmpty(Pseudo_Unique_ID);
		return Pseudo_Unique_ID;
	}
	/**
	 * 获得wifi MAC地址
	 * @param context
	 */
	public static String getWLAN_MAC_Adress(Context context){
		WifiManager wm=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		String WLAN_MAC_Adress=wm.getConnectionInfo().getMacAddress();
		WLAN_MAC_Adress=judgeEmpty(WLAN_MAC_Adress);
		return WLAN_MAC_Adress;
	}
	public static String getBT_MAC_Adress(){
		BluetoothAdapter baAdapter=BluetoothAdapter.getDefaultAdapter();
		String BT_MAC_Adress=baAdapter.getAddress();
		BT_MAC_Adress=judgeEmpty(BT_MAC_Adress);
		return BT_MAC_Adress;
	}
	public static String judgeEmpty(String str){
		if(str==null||"".equals(str)||"null".equals(str)){
			str="";
		}
		return str;
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
			loginJson.data = new LoginJson.Data();
			JSONObject data = jsonObject.getJSONObject("data");
			
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
		RegisterJson loginJson=null;
		try {
			/*
			 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
			 * "confVersion":"1.0", "data":{"isSuccess":false},
			 * "currentTime":1401420697}
			 */
			jsonObject = new JSONObject(json);
			loginJson = new RegisterJson();
			loginJson.state = jsonObject.getInt("state");
			loginJson.errorCode = jsonObject.getInt("errorCode");
			loginJson.errorMessage = jsonObject.getString("errorMessage");
			loginJson.version = jsonObject.getString("version");
			loginJson.confVersion = jsonObject.getString("confVersion");
			loginJson.currentTime = jsonObject.getLong("currentTime");
			JSONObject  jb=jsonObject.getJSONObject("data");
			if(jb!=null){
				JSONObject data = jb;
				loginJson.data = new RegisterJson.Data();
				loginJson.data.isSuccess = data.getBoolean("isSuccess");
			}else{
				loginJson.data=null;
			}
			
			return loginJson;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return loginJson;
	}

	public static InfoJson parseInfoJson(String json,Activity activity) {
		JSONObject jsonObject;
		InfoJson infoJson=null;
		try {
			/*
			 * info返回结果{ "state":0," errorCode":0, "errorMessage":"",
			 * "version":"1.0", "confVersion":"1.0", 
			 * "data":[ {"usr_id":"47","name":"1\u697c\u4e3b","gender":"1",
			 * "tx":"47_icon_profile_block_user.hdpi.png","age":"1",
			 * "type":"1","code":"4dkrc3","inviter":"0","create_time":"0",
			 * "update_time":"2014-06-12 11:38:57","exp":"0","lv":"0",
			 * "follow":"2","follower":"1","con_login":"0","imagesCount":"9"}],
			 * "currentTime":1401951674}
			 */
			jsonObject = new JSONObject(json);
			infoJson = new InfoJson();
			infoJson.state = jsonObject.getInt("state");
			infoJson.errorCode = jsonObject.getInt("errorCode");
			infoJson.errorMessage = jsonObject.getString("errorMessage");
			infoJson.version = jsonObject.getString("version");
			infoJson.confVersion = jsonObject.getString("confVersion");
			infoJson.currentTime = jsonObject.getLong("currentTime");
			infoJson.data=new ArrayList<InfoJson.Data>();
			JSONArray data = jsonObject.getJSONArray("data");
			JSONObject object=null;
			InfoJson.Data d=null;
			User user=null;
			for(int i=0;i<data.length();i++){
				object=(JSONObject)data.get(i);
				d = new InfoJson.Data();
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
				d.exp=object.getInt("exp");
//				d.lv=object.getInt("lv");
				d.follow=object.getInt("follow");
				d.follower=object.getInt("follower");
				d.con_login=object.getInt("con_login");
				d.imagesCount=object.getInt("imagesCount");
				user=new User();
				
				user.age=d.age;
				user.classs=d.type;
				user.gender=d.gender;
				user.iconPath=Constants.Picture_ICON_Path+File.separator+d.tx;
				user.nickName=d.name;
				user.race=""+d.type;
				user.iconUrl=d.tx;
				user.userId=d.user_id;
				user.exp=d.exp;
//				user.lv=d.lv;
				/*user.exp=380;
				user.lv=2;*/
				user.follow=d.follow;
				user.follower=d.follower;
				user.con_login=d.con_login;
				user.imagesCount=d.imagesCount;
				Constants.user=user;
				infoJson.data.add(d);
			}
			
			return infoJson;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return infoJson;
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
				UserImagesJson userImagesJson=null;
				userImagesJson=new UserImagesJson();
				userImagesJson.state = jsonObject.getInt("state");
				userImagesJson.errorCode = jsonObject.getInt("errorCode");
				userImagesJson.errorMessage = jsonObject.getString("errorMessage");
				userImagesJson.version = jsonObject.getString("version");
				userImagesJson.confVersion = jsonObject.getString("confVersion");
				userImagesJson.currentTime = jsonObject.getLong("currentTime");
			String temp=jsonObject.getString("data");
			if(temp==null||"null".equals(temp)){
				return userImagesJson;
			}
			JSONArray data = jsonObject.getJSONArray("data");
			Topic topic=null;
			JSONObject object=null;
			UserImagesJson.Data data1=null;
			for(int i=0;i<data.length();i++){
				object=data.getJSONObject(i);
				data1=new UserImagesJson.Data();
				data1.url=object.getString("url");
//				if(json.contains("likes")){
				data1.likes=object.getInt("likes");
//				}else{
//					imageJson.data.likes=0;
//				}
				
				data1.img_id=object.getInt("img_id");
				data1.usr_id=object.getInt("usr_id");
				data1.comment=object.getString("cmt");
				data1.create_time=object.getLong("create_time");
				
				
				data1.user=Constants.user;
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
	public static String judgeError(int status){
		if(status>=400&&status<500){
//			return Constants.NOTE_MESSAGE_3;
			return null;
		}else if(status>=500){
			return Constants.NOTE_MESSAGE_4;
		}
		return "未知错误";
	}
}
