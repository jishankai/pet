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
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;






import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.anim;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Banner;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.KingdomCard;
import com.aidigame.hisun.pet.bean.PetNews;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.TalkMessage;
import com.aidigame.hisun.pet.bean.TalkMessage.Msg;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.AddressData;
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
import com.aidigame.hisun.pet.ui.DialogNoteActivity;
import com.aidigame.hisun.pet.ui.ReceiverAddressActivity.UserAddress;
import com.aidigame.hisun.pet.ui.SubmitPictureActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.umeng.analytics.MobclickAgent;

public class HttpUtil {

	/**
	 * 注册
	 * 
	 * @param user
	 */
	public static boolean register(Handler handler, User user,Activity activity) {
		String url = "http://" + Constants.REGISTER_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		
		String name = null;
		LogUtil.i("me", "注册：宠物名"+user.pet_nickName+",用户名："+user.u_nick);
		/*user.pet_nickName=new String(user.pet_nickName.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
		user.u_nick=new String(user.u_nick.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));*/
		String pet_nickName=user.pet_nickName;
		String u_nick=user.u_nick;
		try {
			pet_nickName = URLEncoder.encode(pet_nickName, "UTF-8");
			u_nick = URLEncoder.encode(u_nick, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		LogUtil.i("me", "注册：宠物名"+user.pet_nickName+",用户名："+user.u_nick);
		String value="";
		String SIG = null;
		String param=null;
		if(user.currentAnimal!=null){
			if(user.isBind){
				value = "age=" + user.a_age+"&aid="+user.currentAnimal.a_id + "&code="/* +1 */+ "&gender="
						+ user.a_gender /*+  "&name="+user.pet_nickName*/+ "&type=" + user.race
						+"&u_city="+user.city+"&u_gender="+user.u_gender/*+"&u_name="+user.u_nick*/
						+"&wechat="+user.weixin_id+"&weibo="+user.xinlang_id;
				
				SIG = getMD5Value(value);
				param = "&age=" + user.a_age +"&aid="+user.currentAnimal.a_id+ "&code="/* +1 */+ "&gender="
						+ user.a_gender + "&name=" + pet_nickName + "&type=" + user.race
						+"&u_city="+user.city+"&u_gender="+user.u_gender+"&u_name="+u_nick
						+ "&sig=" + SIG + "&SID=" + Constants.SID
						+"&wechat="+user.weixin_id+"&weibo="+user.xinlang_id;
			}else{
				value = "age=" + user.a_age+"&aid="+user.currentAnimal.a_id + "&code="/* +1 */+ "&gender="
						+ user.a_gender /*+  "&name="+user.pet_nickName*/+ "&type=" + user.race
						+"&u_city="+user.city+"&u_gender="+user.u_gender/*+"&u_name="+user.u_nick*/;
				
				SIG = getMD5Value(value);
				param = "&age=" + user.a_age +"&aid="+user.currentAnimal.a_id+ "&code="/* +1 */+ "&gender="
						+ user.a_gender + "&name=" + pet_nickName + "&type=" + user.race
						+"&u_city="+user.city+"&u_gender="+user.u_gender+"&u_name="+u_nick
						+ "&sig=" + SIG + "&SID=" + Constants.SID;
			}
			
			
		}else{
            if(user.isBind){
            	value = "age=" + user.a_age+"&aid="+"0"  + "&code="/* +1 */+ "&gender="
    					+ user.a_gender /*+  "&name="+user.pet_nickName*/+ "&type=" + user.race
    					+"&u_city="+user.city+"&u_gender="+user.u_gender/*+"&u_name="+user.u_nick*/
    					+"&wechat="+user.weixin_id+"&weibo="+user.xinlang_id;;
    		
    			SIG = getMD5Value(value);
    			param = "&age=" + user.a_age + "&code="/* +1 */+ "&gender="
    					+ user.a_gender + "&name=" + pet_nickName + "&type=" + user.race
    					+"&u_city="+user.city+"&u_gender="+user.u_gender+"&u_name="+u_nick
    					+ "&sig=" + SIG + "&SID=" + Constants.SID+"&aid=0"
    					+"&wechat="+user.weixin_id+"&weibo="+user.xinlang_id;;
			}else{
				value = "age=" + user.a_age+"&aid="+"0"  + "&code="/* +1 */+ "&gender="
						+ user.a_gender /*+  "&name="+user.pet_nickName*/+ "&type=" + user.race
						+"&u_city="+user.city+"&u_gender="+user.u_gender/*+"&u_name="+user.u_nick*/;
			
				SIG = getMD5Value(value);
				param = "&age=" + user.a_age + "&code="/* +1 */+ "&gender="
						+ user.a_gender + "&name=" + pet_nickName + "&type=" + user.race
						+"&u_city="+user.city+"&u_gender="+user.u_gender+"&u_name="+u_nick
						+ "&sig=" + SIG + "&SID=" + Constants.SID+"&aid=0";
			}
			
		
		}
		LogUtil.i("me", "value" + value);
		url = url + param;
		HttpGet get = new HttpGet(url);
			String result=connect(client, handler, get);
			LogUtil.i("me", "注册url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "注册返回结果==" + result);
				 int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
					   * "data":{"usr_id":"79","aid":"7"},"currentTime":1413953344}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
						JSONObject jo=new JSONObject(result);
						String dataStr=jo.getString("data");
						if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
							JSONObject j1=jo.getJSONObject("data");
							int usr_id=j1.getInt("usr_id");
							if(usr_id>0){
								Constants.user=new User();
								Constants.user.userId=usr_id;
								Constants.user.currentAnimal=new Animal();
								Constants.user.currentAnimal.a_id=j1.getInt("aid");
								Constants.isSuccess=true;
								return true;
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  
				  }else if(status==1){
					  return false;
				  }else if(status==2){
			       return register(handler,user,activity);
				  }
			}
		return false;
	}
	/**
	 * 修改信息，用户和宠物信息
	 * @param handler
	 * @param user
	 * @param activity
	 * @return
	 */
	public static boolean modifyUserInfo(Handler handler, User user,Activity activity) {
		String url = "http://" + Constants.USER_MODIFY;
		DefaultHttpClient client = new DefaultHttpClient();
		
		String name = null;
		String u_nick=user.u_nick;
		try {
			u_nick = URLEncoder.encode(u_nick, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String value="";
		String SIG = null;
		String param=null;
			value = "code="/* +1 *//*+  "&name="+user.pet_nickName*/
					+"&u_city="+user.city+"&u_gender="+user.u_gender/*+"&u_name="+user.u_nick*/;
			
			SIG = getMD5Value(value);
			param =  "&code="/* +1 */
					+"&u_city="+user.city+"&u_gender="+user.u_gender+"&u_name="+u_nick
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  return true;
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return modifyUserInfo(handler, user, activity);
				  }
			}
			
		return false;
	}
	public static ArrayList<PetPicture> petBegPicturesList(Handler handler, Animal animal,int page,Activity activity) {
		String url = "http://" + Constants.PET_BEG_PICTURE_LIST;
		DefaultHttpClient client = new DefaultHttpClient();
		ArrayList<PetPicture> list=null;
		String value="";
		String SIG = null;
		String param=null;
			value = "aid="+animal.a_id+"&page="+page;
			
			SIG = getMD5Value(value);
			param = ""+ animal.a_id
					+ "&sig=" + SIG + "&SID=" + Constants.SID+"&page="+page;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					 /*
					  * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0.0","confVersion":"1.1",
					  * "data":[[
					  * {"img_id":"1941","url":"298_mmexport1419298263144.jpg",
					  * "cmt":"\u7c73\u5708\u5708\u997f\u4e86\u3002\u6c42\u53e3\u7cae\u3002",
					  * "food":"107","create_time":"1419685231"},
					  * {"img_id":"1904","url":"298_-185116349a9c62a3.jpg",
					  * "cmt":"\u5c3c\u739b\u65e9\u4e0a\u5730\u94c1\u5751\u7239\u554a\u3002\u6324\u6210\u8fd9\u6837\u4e86\uff01\u7ed9\u70b9\u53e3\u7cae\u5b89\u6170\u5b89\u6170\u5427",
					  * "food":"6","create_time":"1419561381"},
					  * {"img_id":"1814","url":"298_mmexport1419298240661.jpg",
					  * "cmt":"\u4eca\u5929\u6211\u957f\u8fd9\u6837\u3002","food":"148","create_time":"1419386262"},
					  * {"img_id":"1792","url":"298_mmexport1419298226062.jpg",
					  * "cmt":"\u5feb\u70b9\u7ed9\u53e3\u7cae\uff01\u8be5\u6b7b\u6211\u7684\u8033\u6735\u3002\u3002\u3002",
					  * "food":"2011","create_time":"1419299753"}
					  * ]],"currentTime":1420012615}
					  */
					  
					try {
						JSONObject jo;
						jo = new JSONObject(result);
						String dataStr=jo.getString("data");
						  if(!StringUtil.isEmpty(dataStr)&&!"[[]]".equals(dataStr)){
							  JSONArray temp=jo.getJSONArray("data");
							  if(temp!=null&&temp.length()>0){
								  JSONArray ja=temp.getJSONArray(0);
								  if(ja!=null){
									  PetPicture pp=null;
									  JSONObject jobj=null;
									  list=new ArrayList<PetPicture>();
									  for(int i=0;i<ja.length();i++){
										  jobj=ja.getJSONObject(i);
										  pp=new PetPicture();
										  pp.img_id=jobj.getInt("img_id");
										  pp.animal=animal;
										  pp.url=jobj.getString("url");
										  pp.cmt=jobj.getString("cmt");
										  pp.foodNum=jobj.getLong("food");
										  pp.create_time=jobj.getLong("create_time");
										  list.add(pp);
									  }
									  return list;
								  }
							  }
						  }	 
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
					   
					

					  return list;
				  }else if(status==1){
					  return list;
				  }else if(status==2){
					  return petBegPicturesList(handler, animal, page, activity);
				  }
			}
			
		return list;
	}
	/**
	 * 要口粮列表api
	 * @param handler
	 * @param user
	 * @param activity
	 * @return
	 */
	public static ArrayList<PetPicture> begFoodList(Handler handler, int page,Activity activity) {
		String url = "http://" + Constants.BEG_FOOD_LIST;
		DefaultHttpClient client = new DefaultHttpClient();
		ArrayList<PetPicture> list=null;
		String value="";
		String SIG = null;
		String param=null;
			value = "page="+page;
			
			SIG = getMD5Value(value);
			param = ""+ page
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   * {"state":0,"errorCode":0,"errorMessage":"",
					   * "version":"1.0.0","confVersion":"1.0",
					   * "data":[[
					   *    {"img_id":"1326","aid":"309",
					   *    "cmt":"\u522b\u85cf\u4e86\uff0c\u6211\u770b\u89c1\u4f60\u4e86\ud83d\udc7d",
					   *    "food":"0","create_time":"1417756946",
					   *    "name":"\u963f\u9ec4\u6211\u8bf4\u6211\u662f\u554a\u9ec4",
					   *    "tx":"309_1414581941593_pet_icon.jpg","type":"208",
					   *    "gender":"1","usr_id":"317","u_tx":"317_1415103222519_usr_icon.jpg",
					   *    "u_name":"\u697c\u697c"}]],"currentTime":1417757920}
					   */
					  
					try {
						JSONObject jo;
						jo = new JSONObject(result);
						String dataStr=jo.getString("data");
						  if(!StringUtil.isEmpty(dataStr)&&!"[[]]".equals(dataStr)){
							  JSONArray temp=jo.getJSONArray("data");
							  if(temp!=null&&temp.length()>0){
								  JSONArray ja=temp.getJSONArray(0);
								  if(ja!=null){
									  PetPicture pp=null;
									  JSONObject jobj=null;
									  list=new ArrayList<PetPicture>();
									  for(int i=0;i<ja.length();i++){
										  jobj=ja.getJSONObject(i);
										  pp=new PetPicture();
										  pp.img_id=jobj.getInt("img_id");
										  pp.animal=new Animal();
										  pp.animal.a_id=jobj.getLong("aid");
										  pp.url=jobj.getString("url");
										  pp.cmt=jobj.getString("cmt");
										  pp.animal.foodNum=jobj.getLong("food");
										  pp.create_time=jobj.getLong("create_time");
										  pp.animal.pet_nickName=jobj.getString("name");
										  pp.animal.pet_iconUrl=jobj.getString("tx");
										  pp.animal.a_gender=jobj.getInt("gender");
										  pp.animal.type=jobj.getInt("type");
										  pp.animal.master_id=jobj.getInt("usr_id");
										  pp.animal.u_name=jobj.getString("u_name");
										  pp.animal.u_tx=jobj.getString("u_tx");
										  pp.animal.race=StringUtil.getRaceStr(pp.animal.type);
										  list.add(pp);
									  }
									  return list;
								  }
							  }
						  }	 
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
					   
					

					  return list;
				  }else if(status==1){
					  return list;
				  }else if(status==2){
					  return begFoodList(handler, page, activity);
				  }
			}
			
		return list;
	}
	/**
	 * 举报图片
	 * @param handler
	 * @param animal
	 * @param msg
	 * @param activity
	 * @return
	 */
	public static boolean reportPicture(Handler handler, int img_id,Activity activity) {
		String url = "http://" + Constants.REPORT_IMAGE;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
			value = "img_id="+img_id;
			
			SIG = getMD5Value(value);
			param = img_id
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
					   * "data":{"isSuccess":true},"currentTime":1415607031}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						
							return jo.getJSONObject("data").getBoolean("isSuccess");
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return reportPicture(handler, img_id, activity);
				  }
			}
			
		return false;
	}
	/**
	 * 举报图片
	 * @param handler
	 * @param animal
	 * @param msg
	 * @param activity
	 * @return
	 */
	public static ArrayList<Banner> bannerList(Handler handler,Activity activity) {
		String url = "http://" + Constants.BANNER_LIST;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
			value = "";
			
			SIG = getMD5Value(value);
			param = "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   
					   {"state":0,"errorCode":0,"errorMessage":"",
					   "version":"1.0.0","confVersion":"1.0",
					   "data":
					   [[{"img_url":"1.jpg",
					   "url":"http:\/\/baidu.com",
					   "icon":"1.jpg",
					   "title":"\u6211\u662f\u5927\u68a6\u9192",
					   "description":"\u54aa\u54aa\u6c6a\u6c6a\u54aa\u54aa\u6c6a\u6c6a"},
					   {"img_url":"2.jpg","url":"http:\/\/weibo.com","icon":"2.jpg","title":"\u6211\u662f\u5927\u68a6\u9192","description":"\u54aa\u54aa\u6c6a\u6c6a\u54aa\u54aa\u6c6a\u6c6a"}]],
					   "currentTime":1418816013}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						    JSONArray ja1=jo.getJSONArray("data");
						    if(ja1!=null&&ja1.length()>0){
						    	JSONArray ja2=ja1.getJSONArray(0);
						    	ArrayList<Banner> banners=new ArrayList<Banner>();
						    	Banner banner=null;
						    	if(ja2!=null&&ja2.length()>0){
						    		for(int i=0;i<ja2.length();i++){
						    			banner=new Banner();
						    			banner.img_url=ja2.getJSONObject(i).getString("img_url");
						    			banner.url=ja2.getJSONObject(i).getString("url");
						    			banner.icon=ja2.getJSONObject(i).getString("icon");
						    			banner.title=ja2.getJSONObject(i).getString("title");
						    			banner.description=ja2.getJSONObject(i).getString("description");
						    			banners.add(banner);
						    		}
						    		return banners;
						    	}
						    }
							return null;
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return null;
				  }else if(status==2){
					  return bannerList(handler, activity);
				  }
			}
			
		return null;
	}
	/**
	 * 设置密码
	 * @param handler
	 * @param pass
	 * @param activity
	 * @return
	 */
	public static boolean setPassWord(Handler handler, String  pwd,Activity activity) {
		String url = "http://" + Constants.SET_PASSWORD;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
			value = "pwd="+pwd;
			
			SIG = getMD5Value(value);
			param = pwd
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
					   * "data":{"isSuccess":true},"currentTime":1415607031}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						
							return jo.getJSONObject("data").getBoolean("isSuccess");
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return setPassWord(handler, pwd, activity);
				  }
			}
			
		return false;
	}
	/**
	 * 切换账号
	 * @param handler
	 * @param pwd
	 * @param activity
	 * @return
	 */
	public static boolean changeAccount(Handler handler, String  pwd,String name,Activity activity) {
		String url = "http://" + Constants.CHANGE_ACCOUNT;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
		String u_nick=name;
		try {
			u_nick = URLEncoder.encode(u_nick, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			value = "pwd="+pwd;
			
			SIG = getMD5Value(value);
			param = u_nick+"&pwd="+pwd
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
					   * "data":{"isSuccess":true},"currentTime":1415607031}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						
						  boolean isBinded=jo.getJSONObject("data").getBoolean("isBinded");
						    if(isBinded){
						    	Constants.user=new User();
						        Constants.user.currentAnimal=new Animal();
						    	Constants.isSuccess=true;
						    	Constants.user.userId=jo.getJSONObject("data").getInt("usr_id");
						    	Constants.user.currentAnimal.a_id=jo.getJSONObject("data").getLong("aid");
						    }
							return isBinded;
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return changeAccount(handler, pwd, name, activity);
				  }
			}
			
		return false;
	}
	/**
	 * 判断是否绑定微信账号或微博账号
	 * @param handler
	 * @param id
	 * @param activity
	 * @return
	 */
	public static boolean isBind(Handler handler, String  id,boolean isWeixin,Activity activity) {
		String url = "http://" + Constants.IS_BIND;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
		if(isWeixin){
			value = "wechat="+id;
		}else{
			value = "weibo="+id;
		}
			
			
			SIG = getMD5Value(value);
			if(isWeixin){
				param = "&wechat="+id
						+ "&sig=" + SIG + "&SID=" + Constants.SID;
			}else{
				param = "&weibo="+id
						+ "&sig=" + SIG + "&SID=" + Constants.SID;
			}
			
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   *{"state":0,"errorCode":0,"errorMessage":"","version":"1.0.0","confVersion":"1.0",
					   *"data":{"usr_id":"553","aid":"298","isBinded":true},
					   *"currentTime":1418121852}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						    boolean isBinded=jo.getJSONObject("data").getBoolean("isBinded");
						    if(isBinded){
						    	Constants.user=new User();
						        Constants.user.currentAnimal=new Animal();
						    	Constants.isSuccess=true;
						    	Constants.user.userId=jo.getJSONObject("data").getInt("usr_id");
						    	Constants.user.currentAnimal.a_id=jo.getJSONObject("data").getLong("aid");
						    }
							return isBinded;
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return isBind(handler, id, isWeixin, activity);
				  }
			}
			
		return false;
	}
	/**
	 * 分享赏口粮弹窗相关信息
	 * @param handler
	 * @param id
	 * @param isWeixin
	 * @param activity
	 * @return
	 */
	public static PetPicture shareFoodApi(Handler handler, Animal  animal,Activity activity) {
		String url = "http://" + Constants.SHARE_FOOD;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
		value = "aid="+animal.a_id;
			
			
			SIG = getMD5Value(value);
		
				param = animal.a_id
						+ "&sig=" + SIG + "&SID=" + Constants.SID;
			
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   {"state":0,"errorCode":0,"errorMessage":"",
					   "version":"1.0.0","confVersion":"1.0",
					   "data":[{
					   "img_id":"1443","url":"401_1418134223@206609@_2448&3264.png",
					   "food":"1040","create_time":"1418134223"}],
					   "currentTime":1418219470}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"[false]".equals(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						 
						  JSONArray ja=jo.getJSONArray("data");
						  if(ja!=null&&ja.length()>0){
							  PetPicture pp=new PetPicture();
							  JSONObject j=ja.getJSONObject(0);
							  pp.img_id=j.getInt("img_id");
							  pp.url=j.getString("url");
							  pp.animal=animal;
							  pp.animal.foodNum=j.getLong("food");
							  pp.create_time=j.getLong("create_time");
							  return pp;
						  }
						  
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return null;
				  }else if(status==2){
					  return shareFoodApi(handler, animal, activity);
				  }
			}
			
		return null;
	}
	/**
	 * 兑换口粮列表
	 * @param handler
	 * @param code
	 * @param activity
	 * @return
	 */
	public static ArrayList<Gift> exchangeFoodList(Handler handler, String   code,Activity activity) {
		String url = "http://" + Constants.EXCHANGE_FOOD_LIST;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
		ArrayList<Gift> gifts=null;
		if(code!=null){
           value = "code="+code;
			SIG = getMD5Value(value);
			param = "&code="+code
						+ "&sig=" + SIG + "&SID=" + Constants.SID;
		}else{
			value = "";
			SIG = getMD5Value(value);
			param =  "&sig=" + SIG + "&SID=" + Constants.SID;
		}
		
			
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					  {"state":0,"errorCode":0,"errorMessage":"",
					  "version":"1.0.0",
					  "confVersion":"1.0",
					  "data":
					  {"is_update":true,
					  "item_ids":["1001","1002","2001","2002"],
					  "code":"3ddb27eee7e2e650bbe026f154876571"},
					  "currentTime":1418281939}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"[false]".equals(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						  JSONObject j=jo.getJSONObject("data");
						  JSONArray ja=j.getJSONArray("item_ids");
						  if(ja!=null&&ja.length()>0){
							  gifts=new ArrayList<Gift>();
							  String c=j.getString("code");
							  boolean is_update=j.getBoolean("is_update");
							  Gift gift=null;
							  for(int i=0;i<ja.length();i++){
								  gift=new Gift();
								  gift.is_update=is_update;
								  gift.code=c;
								  gift.no=Integer.parseInt(ja.getString(i));
								  gifts.add(gift);
							  }
							  return gifts;
						  }
						  
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return null;
				  }else if(status==2){
					  return exchangeFoodList(handler, code, activity);
				  }
			}
			
		return null;
	}
	/**
	 * 兑换口粮
	 * @param handler
	 * @param code
	 * @param activity
	 * @return
	 */
	public static int exchangeFood(Handler handler, Gift   gift,Activity activity) {
		String url = "http://" + Constants.EXCHANGE_FOOD;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
		/*
		 * &item_id=&aid=
		 */
           value = "aid="+gift.animal.a_id+"&item_id="+gift.no;
			SIG = getMD5Value(value);
			param = "aid="+gift.animal.a_id+"&item_id="+gift.no
						+ "&sig=" + SIG + "&SID=" + Constants.SID;
		
			
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   
                       {"state":0,"errorCode":0,"errorMessage":"",
                       "version":"1.0.0","confVersion":"1.0",
                       "data":{"food":1730},
                       "currentTime":1418291512}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"[false]".equals(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						  JSONObject j=jo.getJSONObject("data");
						  int c=j.getInt("food");
							  return c;
						  
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return -1;
				  }else if(status==2){
					  return exchangeFood(handler, gift, activity);
				  }
			}
			
		return -1;
	}
	/**
	 * 食品的详细信息
	 * @param handler
	 * @param gift
	 * @param activity
	 * @return
	 */
	public static boolean giftInfo(Handler handler, Gift   gift,Activity activity) {
		String url = "http://" + Constants.FOOD_INFO;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
		/*
		 * &item_id=&aid=
		 */
           value = "item_id="+gift.no;
			SIG = getMD5Value(value);
			param = ""+gift.no
						+ "&sig=" + SIG + "&SID=" + Constants.SID;
		
			
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   {"state":0,"errorCode":0,"errorMessage":"","version":"1.0.0",
					   "confVersion":"1.0",
					   "data":
					   {"item_id":"1001",
					   "name":"Now!GrainFree\u5929\u7136\u732b\u7cae 500g\u6563\u88c5",
					   "icon":"1_small.png",
					   "description":"\u4ea7\u54c1\u540d\u79f0\uff1aNOW!Grain Free\u5929\u7136\u732b\u7cae 500g\u6563\u88c5&\u9002\u7528\u8303\u56f4\uff1a\u6210\u732b&\u4ea7\u54c1\u89c4\u683c\uff1a500g&\u751f\u4ea7\u5382\u5bb6\uff1anow",
					   "img":"1_big.png","price":"3350","type":"1",
					   "spec":"500g","create_time":"0",
					   "update_time":"2014-12-11 14:45:22"},
					   "currentTime":1418283634}
					   产品名称：爱肯拿无谷猫鸡肉鱼 500g散装&适用范围：猫&产品规格：500g&生产厂家：爱肯拿
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"[false]".equals(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						  JSONObject j=jo.getJSONObject("data");
						  gift.name=j.getString("name");
						  gift.smallImage=j.getString("icon");
						  gift.totalDes=j.getString("description");
						  gift.bigImage=j.getString("img");
						  gift.price=j.getInt("price");
						  gift.type=j.getInt("type");
						  gift.spec=j.getString("spec");
							  return true;
						  
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return giftInfo(handler, gift, activity);
				  }
			}
			
		return false;
	}
	public static boolean bindAccount(Handler handler, String  id,boolean isWeixin,Activity activity) {
		String url = "http://" + Constants.BIND_ACCOUNT;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
		if(isWeixin){
			value = "wechat="+id;
		}else{
			value = "weibo="+id;
		}
			
			
			SIG = getMD5Value(value);
			if(isWeixin){
				param = "&wechat="+id
						+ "&sig=" + SIG + "&SID=" + Constants.SID;
			}else{
				param = "&weibo="+id
						+ "&sig=" + SIG + "&SID=" + Constants.SID;
			}
			
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   *{"state":0,"errorCode":0,"errorMessage":"","version":"1.0.0","confVersion":"1.0",
					   *"data":{"usr_id":"553","aid":"298","isBinded":true},
					   *"currentTime":1418121852}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						    boolean isBinded=jo.getJSONObject("data").getBoolean("isBinded");
						    if(isBinded){
						    	/*Constants.user=new User();
						        Constants.user.currentAnimal=new Animal();
						    	Constants.isSuccess=true;
						    	Constants.user.userId=jo.getJSONObject("data").getInt("usr_id");
						    	Constants.user.currentAnimal.a_id=jo.getJSONObject("data").getLong("aid");*/
						    }
							return isBinded;
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return bindAccount(handler, id, isWeixin, activity);
				  }
			}
			
		return false;
	}
	/**
	 * 赏口粮
	 * @param handler
	 * @param img_id
	 * @param num
	 * @param activity
	 * @return
	 */
	public static boolean awardApi(Handler handler, PetPicture petPicture,int num,Activity activity) {
		String url = "http://" + Constants.AWARD_FOOD;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
			value = "img_id="+petPicture.img_id+"&n="+num;
			
			SIG = getMD5Value(value);
			param = petPicture.img_id
					+ "&sig=" + SIG + "&SID=" + Constants.SID+"&n="+num;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   * {"food":1,"gold":"3287"}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						  petPicture.animal.foodNum=jo.getJSONObject("data").getLong("food");
						  Constants.user.coinCount=jo.getJSONObject("data").getInt("gold");
						  /*Intent intent=new Intent(activity,DialogNoteActivity.class);
						  intent.putExtra("mode", 8);
					    	intent.putExtra("name", petPicture.animal.pet_nickName);
					    	activity.startActivity(intent);*/
							return true;
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return awardApi(handler, petPicture, num, activity);
				  }
			}
			
		return false;
	}
	public static boolean reportUser(Handler handler, int usr_id,Activity activity) {
		String url = "http://" + Constants.REPORT_USER;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
			value = "usr_id="+usr_id;
			
			SIG = getMD5Value(value);
			param = usr_id
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
					   * "data":{"isSuccess":true},"currentTime":1415607031}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						
							return jo.getJSONObject("data").getBoolean("isSuccess");
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return reportUser(handler, usr_id, activity);
				  }
			}
			
		return false;
	}
	
	public static boolean blockOther(Handler handler, int talk_id,Activity activity) {
		String url = "http://" + Constants.BLOCK_OTHER;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
			value = "talk_id="+talk_id;
			
			SIG = getMD5Value(value);
			param = talk_id
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
					   * "data":{"isSuccess":true},"currentTime":1415607031}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						
							return jo.getJSONObject("data").getBoolean("isSuccess");
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return blockOther(handler, talk_id, activity);
				  }
			}
			
		return false;
	}
	/**
	 * 取消拉黑
	 * @param handler
	 * @param talk_id
	 * @param activity
	 * @return
	 */
	public static boolean unBlockOther(Handler handler, int usr_id,Activity activity) {
		String url = "http://" + Constants.UNBLOCK_OTHER;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
			value = "usr_id="+usr_id;
			
			SIG = getMD5Value(value);
			param = usr_id
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
					   * "data":{"isSuccess":true},"currentTime":1415607031}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						
							return jo.getJSONObject("data").getBoolean("isSuccess");
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return unBlockOther(handler, usr_id, activity);
				  }
			}
			
		return false;
	}
	
	public static String updateVersionInfo(Handler handler, String version,Activity activity) {
		String url = "http://" + Constants.VERSION_INFO;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
  		String v=new String(version.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
  		try {
  			v = URLEncoder.encode(v, "UTF-8");
  		} catch (UnsupportedEncodingException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
			value = "version="+version;
			
			SIG = getMD5Value(value);
			param = v
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   *{"state":0,"errorCode":0,"errorMessage":"","version":"1.0.0",
					   *"confVersion":"1.0",
					   *"data":{"android_url":"","ios_url":"",,"android_byte":9901044,
					   *"upgrade_content":"\u66f4\u65b0\u5185\u5bb9\\n1.\u6ca1\u6709 2.\u6709"},
					   *"currentTime":1415950452}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						    Constants.android_url=jo.getJSONObject("data").getString("android_url");
						    Constants.apk_size=jo.getJSONObject("data").getLong("android_byte");
							return jo.getJSONObject("data").getString("upgrade_content");
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return null;
				  }else if(status==2){
					  return updateVersionInfo(handler, version, activity);
				  }
			}
			
		return null;
	}
	
	
	public static Animal  userInviteCode(Handler handler, String code,Activity activity) {
		String url = "http://" + Constants.USE_INVITECODE;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
		Animal animal=null;
			value = "code="+code;
			
			SIG = getMD5Value(value);
			param = code
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  /*
					   *{"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
					   *"data":{"aid":309,"tx":"309_1414581941593_pet_icon.jpg"},"currentTime":1415785097}
					   */
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						animal=new Animal();
						JSONObject jo1=jo.getJSONObject("data");
						animal.a_id=jo1.getLong("aid");
						animal.pet_iconUrl=jo1.getString("tx");
						if(dataStr.contains("inviter")){
							Constants.user.inviter=jo1.getInt("inviter");
						}
							return animal;
						
					  }
					  } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
					}
				  }else if(status==1){
					  return null;
				  }else if(status==2){
					  return userInviteCode(handler, code, activity);
				  }
			}
			
		return null;
	}
	
	/**
	 * 修改宠物宣言 或 今天的状态
	 * @param handler
	 * @param user
	 * @param activity
	 * @return
	 */
	
	public static boolean modifyPetAnnounceInfo(Handler handler, Animal animal,String msg,Activity activity) {
		String url = "http://" + Constants.PET_MODIFY_ANOUNCE;
		DefaultHttpClient client = new DefaultHttpClient();
		String value="";
		String SIG = null;
		String param=null;
			value = "aid="+animal.a_id;
			
			SIG = getMD5Value(value);
			param = animal.a_id
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
    	NameValuePair pair=new BasicNameValuePair("msg",msg);//URLEncoder.encode(comment, "UTF-8")
		pairs.add(pair);
    	User user=null;
		
		
		
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		HttpPost post=new HttpPost(url);
		
		String result=connectPost(client, handler, post,pairs);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  try {
						  JSONObject jo=new JSONObject(result);
						  String dataStr=jo.getString("data");
						  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
							
								return jo.getJSONObject("data").getBoolean("isSuccess");
							
						  }
						  } catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								if(handler!=null)
								handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
						}
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return modifyPetAnnounceInfo(handler, animal, msg, activity);
				  }
			}
			
		return false;
	}
	
	
	/**
	 * 我的萌星
	 * @param handler
	 * @param user
	 * @param activity
	 * @return
	 */
	public static ArrayList<Animal> myPetCard(Handler handler,Activity activity) {
		String url = "http://" + Constants.MY_PET_CARD;
		DefaultHttpClient client = new DefaultHttpClient();
		ArrayList<Animal> animals=new ArrayList<Animal>();
		Animal animal=null;
		String value="";
		String SIG = null;
		String param=null;
			value = "";
			
			SIG = getMD5Value(value);
			param = "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		/*
		 *  shake  次数 null  
		 */
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  
					  JSONObject jo1=null;
						/*
						 *[[{"aid":"309","name":"\u963f\u9ec4",
						 *"tx":"309_1414581941593_pet_icon.jpg","msg":"",
						 *"t_rq":"825","rank":"0","t_contri":"818",
						 *"images":[{"img_id":"259","url":"309_1414206413260.png"},
						 *{"img_id":"271","url":"309_1414235855186.png"}],
						 *"percent":8,"shake_count":null,"gift_count":null,"is_touched":null,"is_voiced":null，"invite_code":"qf2wh3@155"}]]
						 */
				
					  try {
							jo1 = new JSONObject(result);
							JSONObject jo=null;
							 String dataStr=jo1.getString("data");
							 if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
								 JSONArray j=jo1.getJSONArray("data");
								 JSONArray ja=null;
								 JSONArray j2=null;
								 PetPicture pp=null;
								 if(j.length()>0){
									 ja=j.getJSONArray(0);
								 }
								  if(ja!=null){
									  for(int i=0;i<ja.length();i++){
										  animal=new Animal();
										  jo=ja.getJSONObject(i);
										  animal.a_id=jo.getLong("aid");
										  animal.master_id=jo.getInt("master_id");
										  animal.pet_nickName=jo.getString("name");
										  animal.pet_iconUrl=jo.getString("tx");
										  animal.announceStr=jo.getString("msg");
										  animal.u_rankCode=jo.getInt("rank");
										  animal.invite_code=jo.getString("invite_code");
										  if(StringUtil.isEmpty(animal.announceStr)){
											  animal.announceStr="点击创建独一无二的萌宣言吧~";
										  }
										  if(animal.u_rankCode==0){
											  animal.u_rank="经纪人";
										  }else{
											  String[] jobs=null;
											  jobs=StringUtil.getUserJobs();
											  if(jobs!=null){
												  animal.u_rank=jobs[animal.u_rankCode-1];
											  }
										  }
										  animal.t_contri=jo.getInt("t_contri");
										  animal.t_rq=jo.getInt("t_rq");
										  String temp=jo.getString("shake_count");
										  if(StringUtil.isEmpty(temp)||"null".equals(temp)){
											  animal.shake_count=3;
										  }else{
											  animal.shake_count=jo.getInt("shake_count");
										  }
										  temp=null;
										  temp=jo.getString("gift_count");
										  if(StringUtil.isEmpty(temp)||"null".equals(temp)){
											  animal.send_gift_count=0;
										  }else{
											  animal.send_gift_count=jo.getInt("gift_count");
										  }
										  temp=null;
										  temp=jo.getString("is_touched");
										  if(StringUtil.isEmpty(temp)||"null".equals(temp)){
											  animal.touch_count=0;
										  }else{
											  animal.shake_count=jo.getInt("is_touched");
										  }
										  temp=null;
										  temp=jo.getString("is_voiced");
										  if(StringUtil.isEmpty(temp)||"null".equals(temp)){
											  animal.touch_count=0;
										  }else{
											  animal.shake_count=jo.getInt("is_voiced");
										  }
										  
										  
										  animal.percent=jo.getInt("percent");
										  String imagesStr=jo.getString("images");
										  if(!StringUtil.isEmpty(imagesStr)&&!"false".equals(imagesStr)&&!"null".equals(imagesStr)){
											  j2=jo.getJSONArray("images");
											  if(j2!=null&&j2.length()>0){
												  animal.picturs=new ArrayList<PetPicture>();
												  for(int k=0;k<j2.length();k++){
													  pp=new PetPicture();
													  pp.img_id=j2.getJSONObject(k).getInt("img_id");
													  pp.url=j2.getJSONObject(k).getString("url");
													  animal.picturs.add(pp);
												  }
												  animals.add(animal);
												 
											  }else{
												  animals.add(animal);
											  }
										  }else{
											  animals.add(animal);
										  }
										  
										  
										  
									  }
								  }
							 }
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					  
					  
					  
					  return animals;
				  }else if(status==1){
					  return animals;
				  }else if(status==2){
					  return myPetCard(handler,activity);
				  }
			}
			
		return animals;
	}
	/**
	 * 我的萌星  若注册传递 usr_id 参数
	 * @param handler
	 * @param user
	 * @param activity
	 * @return
	 */
	public static ArrayList<Animal> petRecommend(Handler handler,Activity activity,int page,int usr_id) {
		String url = "http://" + Constants.PET_RECOMMEND;
		DefaultHttpClient client = new DefaultHttpClient();
		ArrayList<Animal> animals=new ArrayList<Animal>();
		Animal animal=null;
		String value="";
		String SIG = null;
		String param=null;
			value = "page="+page+"&usr_id="+usr_id;
			
			SIG = getMD5Value(value);
			param = "&sig=" + SIG + "&SID=" + Constants.SID+"&page="+page+"&usr_id="+usr_id;
		LogUtil.i("me", "value" + value);
		

		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
			LogUtil.i("me", "修改信息url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "修改信息返回结果==" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  JSONObject jo1=null;
						/*
						 * [[{"aid":"255","name":"guww","tx":"255_1414110572513.jpg",
						 * "gender":"2","u_name":"cc","u_tx":"261_1414110597980.jpg",
						 * "t_rq":"0","fans":"1","in_circle":0,
						 * "images":[{"img_id":"265","url":"242_1414234955642.png"},
						 *          {"img_id":"267","url":"242_1414235083276.png"},
						 *          {"img_id":"268","url":"242_1414235215420.png"},
						 *          {"img_id":"290","url":"242_1414244472779.jpg"},
						 *          {"img_id":"286","url":"242_1414240382259.png"}],"percent":0}]],
						 */
					try {
						jo1 = new JSONObject(result);
						JSONObject jo=null;
						 String dataStr=jo1.getString("data");
						 if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
							 JSONArray j=jo1.getJSONArray("data");
							 JSONArray ja=null;
							 JSONArray j2=null;
							 PetPicture pp=null;
							 if(j.length()>0){
								 ja=j.getJSONArray(0);
							 }
							  if(ja!=null){
								  for(int i=0;i<ja.length();i++){
									  animal=new Animal();
									  jo=ja.getJSONObject(i);
									  animal.a_id=jo.getLong("aid");
									  animal.pet_nickName=jo.getString("name");
									  animal.pet_iconUrl=jo.getString("tx");
									  animal.a_gender=jo.getInt("gender");
									  animal.master_id=jo.getInt("master_id");
									  animal.u_name=jo.getString("u_name");
									  animal.u_tx=jo.getString("u_tx");
									  animal.t_rq=jo.getInt("t_rq");
									  animal.fans=jo.getInt("fans");
									  int in_circle=jo.getInt("in_circle");
									  if(in_circle==0){
										  animal.hasJoinOrCreate=false;
									  }else{
										  animal.hasJoinOrCreate=true;
									  }
									  animal.percent=jo.getInt("percent");
									  String imagesStr=jo.getString("images");
									  if(!StringUtil.isEmpty(imagesStr)&&!"false".equals(imagesStr)&&!"null".equals(imagesStr)){
										  j2=jo.getJSONArray("images");
										  if(j2!=null&&j2.length()>0){
											  animal.picturs=new ArrayList<PetPicture>();
											  for(int k=0;k<j2.length();k++){
												  pp=new PetPicture();
												  pp.img_id=j2.getJSONObject(k).getInt("img_id");
												  pp.url=j2.getJSONObject(k).getString("url");
												  animal.picturs.add(pp);
											  }
											  if(animal.picturs.size()>0)
											  animals.add(animal);
											 
										  }else{
											  continue;
										  }
									  }else{
										  continue;
									  }
									  
									  
									  
								  }
							  }
						 }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  return animals;
				  }else if(status==1){
					  return animals;
				  }else if(status==2){
					  return petRecommend(handler,activity,page,usr_id);
				  }
			}
			
		return animals;
	}
	
	public static boolean modifyPetInfo(Handler handler, User user,Activity activity) {
		String url = "http://" + Constants.PET_MODIFY;
		DefaultHttpClient client = new DefaultHttpClient();
		
		String pet_nickName=user.pet_nickName;
		try {
			pet_nickName = URLEncoder.encode(pet_nickName, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String value="";
		String SIG = null;
		String param=null;
			value = "age=" + user.a_age+"&aid="+user.currentAnimal.a_id + "&code="/* +1 */+ "&gender="
					+ user.a_gender /*+  "&name="+user.pet_nickName*/+ "&type=" + user.race;
			
			SIG = getMD5Value(value);
			param = "&age=" + user.a_age +"&aid="+user.currentAnimal.a_id+ "&code="/* +1 */+ "&gender="
					+ user.a_gender + "&name=" + pet_nickName + "&type=" + user.race
					+ "&sig=" + SIG + "&SID=" + Constants.SID;
		LogUtil.i("me", "value" + value);
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		String result=connect(client, handler, get);
		LogUtil.i("me", "修改信息url==" + url);
		if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
			LogUtil.i("me", "修改信息返回结果==" + result);
			  int status=handleResult(activity,result,handler);
			  if(status==0){
//				  RegisterJson loginJson = parseRegisterJson(result,handler);
				  return true;
			  }else if(status==1){
				  return false;
			  }else if(status==2){
				  return modifyPetInfo(handler, user, activity);
			  }
		}
			
		return false;
	}



	/**
	 * 登陆
	 * @param planet 星球 1.猫；2.狗
	 * @param user
	 */
	public static boolean  login(Context context,Handler handler) {
		String uid =null;
			uid = getIMEI(PetApplication.petApp);
			Constants.IMIE=uid;
			if(uid==null||"null".equals(uid)||"".equals(uid)){
				uid=getUniqueID(PetApplication.petApp);
			}
		
		String url = "http://" + Constants.IP + Constants.LOGIN_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = /*"planet="+Constants.planet+*/"uid=" + uid + "dog&cat";
		String SIG = getMD5(value);
		String param = /*"&planet="+Constants.planet+*/"&uid=" + uid + "&sig=" + SIG;
		url = url + param;
		HttpGet get = new HttpGet(url);
		LogUtil.i("scroll", "Constants.IMIE=="+Constants.IMIE);
		if(Constants.OPEN_UDID!=null)
		LogUtil.i("scroll", "Constants.OPEN_UDID=="+Constants.OPEN_UDID);
		String result=connect(client, handler, get);
			LogUtil.i("me", "登陆url==" + url);
			
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
			LogUtil.i("me", "登陆的返回结果" + result);
			int status=handleResult((Activity)context,result,handler);
			  if(status==0){
				  LoginJson loginJson = parseJson(result);
					if(loginJson!=null){
						Constants.isSuccess = loginJson.data.isSuccess;
						Constants.SID = loginJson.data.SID;
						SharedPreferences sPreferences=PetApplication.petApp.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
						Editor editor=sPreferences.edit();
						editor.putString("SID", Constants.SID);
						editor.putBoolean("isRegister", Constants.isSuccess);
						editor.commit();
						return true;
					}
			  }else if(status==1){
				  return false;
			  }else if(status==2){
				  return login(context, handler);
			  }
				
			}
			return false;
	}
	public static String getSID(Context context,Handler handler) {
		String uid =null;
		uid = getIMEI(PetApplication.petApp);
		Constants.IMIE=uid;
		if(uid==null||"null".equals(uid)||"".equals(uid)){
			uid=getUniqueID(PetApplication.petApp);
		}
		String SID=null;
		String sig=getMD5Value("uid="+uid);
		String url ="http://"+Constants.IP+Constants.GET_SID+uid+"&sig="+sig;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		User user=null;
		boolean flag=false;
		String result=connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             * {"usr_id":"254","sid":"l2jp3k06emllljq35mk3im27o5"}
             *}
             */
		LogUtil.i("me", "url" + url);
			if (!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)) {
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  user=new User();
					  JSONObject j1=null;
					try {
						j1 = new JSONObject(result);
					  String dataStr=j1.getString("data");
					  String usr_id="";
					  if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)&&!"false".equals(dataStr)){
						  JSONObject j2=j1.getJSONObject("data");
						  SID=j2.getString("sid");
						  usr_id=j2.getString("usr_id");
						  if(!StringUtil.isEmpty(usr_id)&&!"null".equals(SID)&&!"false".equals(SID)){
							  int id=Integer.parseInt(usr_id);
							  if(id!=0&&id>0){
								  Constants.user=new User();
								  Constants.user.userId=id;
								  Constants.isSuccess=true; 
								  Constants.SID=SID;
								  Constants.user=info((Activity)context, handler, id);
								  if(Constants.user!=null){
									  Constants.user.currentAnimal=animalInfo(context, Constants.user.currentAnimal, handler);
								  }
								  
								  final ArrayList<Animal> temp=HttpUtil.usersKingdom(context,Constants.user, 1, handler);
									if(Constants.user!=null)
								  Constants.user.aniList=temp;
							  }else{
								  Constants.isSuccess=false;
							  }
							  
						  }
						  if(!StringUtil.isEmpty(SID)&&!"null".equals(SID)&&!"false".equals(SID)){
							  LogUtil.i("me", "返回SID++++++++++" );
							  return SID;
						  }else{
							  return null;
						  }
					  }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						if(handler!=null)
							handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
						e.printStackTrace();
					}
					  
				  }else if(status==1){
				  }else if(status==2){
					  
					
				  }
			}
		return SID;
	}
	public static boolean changePlant(Context context,int planet,Handler handler) {
		String url = "http://" + Constants.IP + Constants.CHANGE_PLANT;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "planet="+planet;
		String SIG = getMD5Value(value);
		String param = SIG +"&planet="+planet+ "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		boolean flag=false;
		try {
			String result=connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             *,"isSuccess":true}
             */
				LogUtil.i("me", "url" + url);
				if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  user=new User();
					  JSONObject j1=new JSONObject(result);
					  JSONObject j2=j1.getJSONObject("data");
					  flag=j2.getBoolean("isSuccess");
					  return flag;
				  }else if(status==1){
				  }else if(status==2){
					  
					  return changePlant(context,planet, handler);
				  }
				}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
			e.printStackTrace();
		}
		return flag;
	}
	
	public static long createKingdom(Context context,Animal animal,Handler handler) {
		String url = "http://" + Constants.IP + Constants.CREATE_KINGDOM;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "age="+animal.a_age+"&gender="+animal.a_gender+"&type="+animal.race;
		String SIG = getMD5Value(value);
		String temp=animal.pet_nickName;
		try {
			temp=URLEncoder.encode(temp, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String param = SIG +"&age="+animal.a_age+"&gender="+animal.a_gender+"&type="+animal.race+"&name="+temp+ "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		boolean flag=false;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
             * "confVersion":"1.0",
             * "data":{"aid":"3000000407"},"currentTime":1412915824}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						  JSONObject j2=j1.getJSONObject("data");
						  return j2.getLong("aid");
					  }
					  
				  }else if(status==1){
				  }else if(status==2){
					  
					  return createKingdom(context,animal, handler);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
			e.printStackTrace();
		}
		return 0;
	}
	//http://54.199.161.210:8001/index.php?r=animal/isTouchedApi&aid=
	/**
	 * 用户是否已经摸过
	 * @param animal
	 * @param handler
	 * @return
	 */
	public static Animal isTouched(Context context,Animal animal,Handler handler) {
		String url = "http://" + Constants.IP + Constants.IS_TOUCHED;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "aid="+animal.a_id;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+animal.a_id+ "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		boolean flag=false;
		String path=null;
		try {
			String result =connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
             * "confVersion":"1.0",
             * "data":{"is_touched":false,"img_url":"3000000356_10.1412931791.png"},"currentTime":1412915824}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						  JSONObject j2=j1.getJSONObject("data");
						  flag=j2.getBoolean("is_touched");
							  
							  if(dataStr.contains("img_url")){
								  path=j2.getString("img_url");
							  }
							  if(StringUtil.isEmpty(path)||"false".equals(path)||"null".equals(path)){
								  path=animal.pet_iconUrl;
							  }
							  if(StringUtil.isEmpty(path)){
								  path="pet_icon";
							  }
							  animal.isTouched=j2.getBoolean("is_touched");;
							  animal.touchedPath=path;
							  if(StringUtil.isEmpty(animal.touchedPath)){
								  animal.touchedPath=animal.pet_iconUrl;
							  }
						  
						  return animal;
					  }
					  
				  }else if(status==1){
				  }else if(status==2){
					  
					  return isTouched(context,animal, handler);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取用户相关信息 info
	 * 
	 * @param user
	 */
	public static User info(Activity activity,Handler handler,int usr_id) {
		String url = "http://" + Constants.IP + Constants.INFO_PATH;
		LogUtil.i("me", "获取用户信息方法正在执行++++++++++"+url );
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "usr_id=";
		String SIG = getMD5Value(value);
		String param = SIG +"&usr_id="+ "&SID=" + Constants.SID;
		if(usr_id!=-1){
			value = "usr_id="+usr_id;
			SIG = getMD5Value(value);
			param = SIG +"&usr_id="+usr_id+ "&SID=" + Constants.SID;
		}
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		 LogUtil.i("me", "获取用户信息方法正在执行++++++++++"+url );
			String result =connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":[{"usr_id":"725","name":"\u9b54\u7ae5","tx":"","gender":"1","city":"1008",
             * "age":"0","exp":"5","lv":"1","gold":"505","con_login":"1","rank":"1",
             * "aid":"3000000470","a_name":"\u50bb\u903c\u706b\u67aa\u5403\u6211\u4e00\u52fe","password":"qqqqq",
             * "a_age":"1","a_tx":"","next_gold":8}],"currentTime":1413629577}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(activity,result,handler);
				  if(status==0){
					  user=parseInfoJson(result,activity);
					  if(user!=null){
					  String temp=""+user.locationCode;
					  if(temp.length()==4){
						  int p=Integer.parseInt(temp.substring(0, 2));
						  user.province=AddressData.PROVINCES[p-10];
						  int p2=Integer.parseInt(temp.substring(2, 4));
						  user.city=AddressData.CITIES[p-10][p2];
					  }else{
						  user.province=AddressData.PROVINCES[0];
						  user.city=AddressData.CITIES[0][0];
					  }
					
						  return user;
					  }
				  }else if(status==1){
				  }else if(status==2){
					  return info(activity, handler,usr_id);
				  }
			}
		return user;
	}
	public static Animal animalInfo(Context context,Animal animal,Handler handler) {
		String url = "http://" + Constants.IP + Constants.ANIMAL_INFO_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "aid="+animal.a_id;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+animal.a_id+ "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0.0","confVersion":"1.1",
             * "data":{"aid":"298","name":"\u8bf7\u53eb\u6211\u7c73\u5708\u5708",
             * "tx":"298_1414137295274_pet_icon.png","gender":"2",
             * "from":"1","type":"124","age":"48","master_id":"304",
             * "t_rq":"36824","msg":"\u5f00\u73a9\u800d\uff01",
             * "u_name":"\u4e00\u53ea\u7490","u_tx":"304_1414137337872_usr_icon.png",
             * "u_rank":"0","news":"721","fans":"80",
             * "images":"75","total_food":"2272","gifts":"32",
             * "followers":"88"},"currentTime":1420013316}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject jsonObject=new JSONObject(result);
					  if(jsonObject!=null){
						  String dataString=jsonObject.getString("data");
						  if(!StringUtil.isEmpty(dataString)){
							  JSONObject object=jsonObject.getJSONObject("data");
							  animal.a_id=object.getLong("aid");
							  animal.pet_nickName=object.getString("name");
							  animal.pet_iconUrl=object.getString("tx");
							  animal.a_gender=object.getInt("gender");
							  animal.imagesNum=object.getLong("images");
							  animal.totalfoods=object.getLong("total_food");
//							  animal.giftsNum=object.getLong("gifts");
							  animal.newsNum=object.getLong("news");
							  String aidStr=""+animal.a_id;
							  animal.from=Integer.parseInt(aidStr.substring(0, 1));
							  animal.type=object.getInt("type");
							  animal.a_age=object.getInt("age");
							  animal.announceStr=object.getString("msg");
							  if(animal.a_age<=0)animal.a_age=1;
							  animal.a_age_str=getAge(animal.a_age);
							  
							  animal.master_id=object.getInt("master_id");
							  animal.t_rq=object.getInt("t_rq");
							  animal.u_name=object.getString("u_name");
							  animal.u_tx=object.getString("u_tx");
							  animal.u_rankCode=object.getInt("u_rank");
							  
							 
							  animal.fans=object.getInt("fans");
							  animal.followers=object.getInt("followers");
							  animal.race=StringUtil.getRaceStr(animal.type);
							  if(animal.u_rankCode==0){
								  animal.u_rank="经纪人";
							  }else{
								  String[] jobs=null;
								  jobs=StringUtil.getUserJobs();
								  if(jobs!=null){
									  animal.u_rank=jobs[animal.u_rankCode-1];
								  }
							  }
							  kingAndUserRelation(context,animal, handler);
							  return animal;
						  }

					  }
					 
				  }else if(status==1){
				  }else if(status==2){
					  return animalInfo(context,animal, handler);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return animal;
	}
	/**
	 * 人气榜
	 * @param category
	 * @param aid
	 * @param handler
	 * @return
	 */
	public static HashMap<String, ArrayList<Animal>> rqRankApi(int category,long aid,Handler handler,Context context) {
		String url = "http://" + Constants.IP + Constants.RQ_RANK;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		/*String value = "aid="+aid+"category="+category;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+aid+ "&SID=" + Constants.SID+"&category="+category;
		if(category==0){
			value="aid="+aid;
			SIG = getMD5Value(value);
			param = SIG +"&aid="+aid+ "&SID=" + Constants.SID;
		}*/
		String value = "category="+category;
		String SIG = getMD5Value(value);
		String param = SIG +"&SID=" + Constants.SID+"&category="+category;
		/*if(category==0){
			value="";
			SIG = getMD5Value(value);
			param = SIG + "&SID=" + Constants.SID;
		}*/
		url = url + param;
		HttpGet get = new HttpGet(url);
		ArrayList<Animal> animalList=null;
		
		ArrayList<Animal> mAnimalList=null;
		HashMap<String, ArrayList<Animal>> map=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             * [{"aid":"3000000237","name":"\u5462\u5462","tx":"","t_rq":"0","vary":0}]
             * ,"currentTime":1409652826}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)&&!"false".equals(dataStr)){
						 
						  JSONArray ja=j1.getJSONArray("data");
						  if(ja!=null&&ja.length()>0){
							  animalList=new ArrayList<Animal>();
							  Animal animal=null;
							  Animal temp=null;
							  Animal temp2=null;
							  Animal temp3=null;
							  JSONObject j2=null;
							  if(Constants.user!=null&&Constants.user.aniList!=null){
								  mAnimalList=new ArrayList<Animal>();
							  }
							  /*
							   * 只取前100名
							   */
							  for(int i=0;i<ja.length();i++){
								  j2=ja.getJSONObject(i);
								  animal=new Animal();
								  animal.type=j2.getInt("type");
								  animal.a_id=j2.getLong("aid");
								  animal.pet_nickName=j2.getString("name");
								  animal.pet_iconUrl=j2.getString("tx");
								  switch (category) {
									case 0:
										animal.t_rq=j2.getInt("t_rq");
										animal.rq=animal.t_rq;
										break;

									case 1:
										animal.d_rq=j2.getInt("d_rq");
										animal.rq=animal.d_rq;
										break;

									case 2:
										animal.w_rq=j2.getInt("w_rq");
										animal.rq=animal.w_rq;
										break;

									case 3:
										animal.m_rq=j2.getInt("m_rq");
										animal.rq=animal.m_rq;
										break;
									}
								  animal.change=j2.getInt("vary");
								  animal.ranking=i+1;
								  if(Constants.user!=null&&Constants.user.aniList!=null){
									  for(int j=0;j<Constants.user.aniList.size();j++){
										  temp=Constants.user.aniList.get(j);
										  if(temp.a_id==animal.a_id){
											  animal.hasJoinOrCreate=true;
//											  animal.isScale=true;
											  mAnimalList.add(animal);
										  }
									  }
								  }
								  if(i<100)
								  animalList.add(animal);
								  
							  }
							  SharedPreferences sp=context.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
							  Editor editor=sp.edit();
							  switch (category) {
							case 0:
								 editor.putString("json", dataStr);
								break;
							case 1:
								 editor.putString("json1", dataStr);
								break;
							case 2:
								 editor.putString("json2", dataStr);
								break;
							case 3:
								 editor.putString("json3", dataStr);
								break;
							}
							 
							  editor.commit();
							  map=new HashMap<String, ArrayList<Animal>>();
							  map.put("total", animalList);
							  map.put("my", mAnimalList);
							  return map;
						  }
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return rqRankApi(category, aid, handler,context);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return map;
	}
	/**
	 * 王国内  成员的贡献排名
	 * @param category
	 * @param aid
	 * @param handler
	 * @return
	 */
	public static  ArrayList<User> contributeRankList(Context context,int category,long aid,Handler handler) {
		String url = "http://" + Constants.IP + Constants.CONTRIBUTE_RANK;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "aid="+aid+"&category="+category;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+aid+ "&SID=" + Constants.SID+"&category="+category;
		if(category==0){
			value="aid="+aid;
			SIG = getMD5Value(value);
			param = SIG +"&aid="+aid+ "&SID=" + Constants.SID;
		}
		url = url + param;
		HttpGet get = new HttpGet(url);
	    ArrayList<User> animalList=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             * [{"usr_id":"254","t_contri":"0","tx":"254_1410249196154.jpg","name":"tom","vary":0}]
             * ,"currentTime":1409652826}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
						  JSONArray ja=j1.getJSONArray("data");
						  if(ja!=null&&ja.length()>0){
							  animalList=new ArrayList<User>();
							  User animal=null;
							  JSONObject j2=null;
							  for(int i=0;i<ja.length();i++){
								  j2=ja.getJSONObject(i);
								  animal=new User();
								  animal.userId=j2.getInt("usr_id");
								  animal.u_nick=j2.getString("name");
								  animal.u_iconUrl=j2.getString("tx");
								  switch (category) {
								case 0:
									animal.t_contri=j2.getInt("t_contri");
									break;

								case 1:
									animal.d_contri=j2.getInt("d_contri");
									break;

								case 2:
									animal.w_contri=j2.getInt("w_contri");
									break;

								case 3:
									animal.m_contri=j2.getInt("m_contri");
									break;
								}
								  if(dataStr.contains("\"vary\":")){
									  animal.change=j2.getInt("vary");
								  }else{
									  animal.change=0;
								  }
								  
								  animal.ranking=i+1;
								  animalList.add(animal);
							  }
							  return animalList;
						  }
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return contributeRankList(context,category, aid, handler);
				  }
				
				
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return animalList;
	}
	/**
	 * 王国动态
	 * @param nid
	 * @param aid
	 * @param handler
	 * @return
	 */
	public static ArrayList<PetNews> kingdomTrends(Context context,int nid,long aid,Handler handler) {
		String url = "http://" + Constants.IP + Constants.KINGDOM_TRENDS;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "aid="+aid;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+aid+ "&SID=" + Constants.SID;
		if(nid!=-1){
			value="aid="+aid+"&nid="+nid;
			SIG = getMD5Value(value);
			param = SIG +"&aid="+aid+ "&SID=" + Constants.SID+"&nid="+nid;
		}
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		ArrayList<PetNews> petNewsList=null;
		try {
			
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             * ,"currentTime":1409652826}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
						  JSONArray jaArray=j1.getJSONArray("data");
						  
						  if(jaArray!=null&&jaArray.length()>0){
							  JSONObject joJsonObject=null;
							  JSONObject jo=null;
							  PetNews petNews=null;
							  boolean isCorrect=false;
							  petNewsList=new ArrayList<PetNews>();
							  for(int i=0;i<jaArray.length();i++){
								  isCorrect=false;
								  joJsonObject=jaArray.getJSONObject(i);
								  petNews=new PetNews();
								  petNews.nid=joJsonObject.getInt("nid");
								  petNews.aid=joJsonObject.getLong("aid");
								  
								  petNews.type=joJsonObject.getInt("type");
								  String content=joJsonObject.getString("content");
								  petNews.create_time=joJsonObject.getLong("create_time");
								  if(StringUtil.isEmpty(content)||"null".equals(content)||"false".equals(content)){
									  petNews.content=false;
									  petNewsList.add(petNews);
									  continue;
								  }else{
									  petNews.content=true;
								  }
								  //type==1   暂时
								  if(petNews.type==-1){
									  jo=joJsonObject.getJSONObject("content");
									  petNews.u_name=jo.getString("u_name");
									  petNews.usr_id=jo.getInt("usr_id");
									  petNewsList.add(petNews);
								  }else if(petNews.type==2){
									  jo=joJsonObject.getJSONObject("content");
									  petNews.u_name=jo.getString("u_name");
									  petNews.usr_id=jo.getInt("usr_id");
									  petNewsList.add(petNews);
								  }else if(petNews.type==3){
									  jo=joJsonObject.getJSONObject("content");
									  petNews.usr_id=jo.getInt("usr_id");
									  petNews.u_name=jo.getString("u_name");
									  petNews.img_id=jo.getInt("img_id");
									  petNews.img_url=jo.getString("img_url");
									  petNewsList.add(petNews);
								  }else if(petNews.type==4){
									  jo=joJsonObject.getJSONObject("content");
									  petNews.usr_id=jo.getInt("usr_id");
									  petNews.u_name=jo.getString("u_name");
//									  petNews.a_name=jo.getString("a_name");
									  petNews.item_id=jo.getInt("item_id");
//									  petNews.item_name=jo.getString("item_name");、
									  
									  if(petNews.item_id>=1101&&petNews.item_id<=1104){
										  isCorrect=true;
									  }
									  if(petNews.item_id>=1201&&petNews.item_id<=1204){
										  isCorrect=true;
									  }
									  if(petNews.item_id>=1301&&petNews.item_id<=1304){
										  isCorrect=true;
									  }
									  if(petNews.item_id>=1401&&petNews.item_id<=1404){
										  isCorrect=true;
									  }
									  if(petNews.item_id>=2101&&petNews.item_id<=2104){
										  isCorrect=true;
									  }
									  if(!isCorrect){
										  continue;
									  }
									  
									  if(content.contains("\"rank\"")){
										  petNews.rank=jo.getInt("rank");
										  if(petNews.rank==0){
											  petNews.job="经纪人";
										  }else if(petNews.rank==-1){
											  petNews.job="路人";
										  }else{
											  String[] jobs=null;
											  jobs=StringUtil.getUserJobs();
											  if(jobs!=null){
												  petNews.job=jobs[petNews.rank-1];
											  }
										  }
									  }
									  
									  petNews.rq=jo.getInt("rq");
									  petNewsList.add(petNews);
								  }else if(petNews.type==5){
									 
								  }else if(petNews.type==6){
									  petNewsList.add(petNews);
								  }else if(petNews.type==7){
									  jo=joJsonObject.getJSONObject("content");
									  petNews.usr_id=jo.getInt("usr_id");
									  petNews.u_name=jo.getString("u_name");
//									  petNews.a_name=jo.getString("a_name");
									  petNews.item_id=jo.getInt("item_id");
//									  petNews.item_name=jo.getString("item_name");
									  if(petNews.item_id>=1101&&petNews.item_id<=1104){
										  isCorrect=true;
									  }
									  if(petNews.item_id>=1201&&petNews.item_id<=1204){
										  isCorrect=true;
									  }
									  if(petNews.item_id>=1301&&petNews.item_id<=1304){
										  isCorrect=true;
									  }
									  if(petNews.item_id>=1401&&petNews.item_id<=1404){
										  isCorrect=true;
									  }
									  if(petNews.item_id>=2101&&petNews.item_id<=2104){
										  isCorrect=true;
									  }
									  if(!isCorrect){
										  continue;
									  }
									  petNews.rank=jo.getInt("rank");
									  if(petNews.rank==0){
										  petNews.job="经纪人";
									  }else if(petNews.rank==-1){
										  petNews.job="路人";
									  }
									  else{
										  String[] jobs=null;
										  jobs=StringUtil.getUserJobs();
										  if(jobs!=null){
											  petNews.job=jobs[petNews.rank-1];
										  }
									  }
									  petNews.rq=jo.getInt("rq");
									  petNewsList.add(petNews);
								  }
								  
							  }
							  return petNewsList;
						  }
					  }
					  
				  }else if(status==1){
				  }else if(status==2){
					  
					  return kingdomTrends(context,nid,aid,handler);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return petNewsList;
	}
	/**
	 * 王国成员
	 * @param usr_id
	 * @param aid
	 * @param handler
	 * @return
	 */
	public static ArrayList<User> kingdomPeoples(Context context,int usr_id,Animal animal,Handler handler) {
		String url = "http://" + Constants.IP + Constants.KINGDOM_PEOPLES;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		ArrayList<User> userList=null;
		String value = "aid="+animal.a_id;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+animal.a_id+ "&SID=" + Constants.SID;
		if(usr_id!=-1){
			value="aid="+animal.a_id+"&usr_id="+usr_id;
			SIG = getMD5Value(value);
			param = SIG +"&aid="+animal.a_id+ "&SID=" + Constants.SID+"&usr_id="+usr_id;
		}
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":[[
             *  {"usr_id":"373","rank":"4","t_contri":"3167","tx":"373_1411376545userHeadImage.png",
             *  "name":"\u5361\u5361\u5982\u540c\u9c7c\u6709\u610f\u4e49","gender":"2","city":"1000"}
             *  ]],"currentTime":1409820659}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "kingdom_people返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String data=j1.getString("data");
					  if(!StringUtil.isEmpty(data)&&!"null".equals(data)){
						  JSONArray ja1=j1.getJSONArray("data");
						  JSONArray ja=null;
						  if(ja1!=null&&ja1.length()>0){
							  ja=ja1.getJSONArray(0);
						  }
						  JSONObject j2=null;
						  User user=null;
						  userList=new ArrayList<User>();
						  if(ja!=null&&ja.length()>0){
							  for(int i=0;i<ja.length();i++){
								  j2=ja.getJSONObject(i);
								  user=new User();
								  user.currentAnimal=new Animal();
								  user.currentAnimal.a_id=animal.a_id;
								  user.userId=j2.getInt("usr_id");
								  user.rankCode=j2.getInt("rank");
								  if(user.rankCode==0||animal.master_id==user.userId){
									  user.rank="经纪人";
								  }else{
									  String[] array=StringUtil.getUserJobs();
									  if(array!=null&&user.rankCode<=8){
										  user.rank=array[user.rankCode-1];
									  }
								  }
								 
								  user.t_contri=j2.getInt("t_contri");
								  user.u_iconUrl=j2.getString("tx");
								  user.u_nick=j2.getString("name");
								  user.u_gender=j2.getInt("gender");
								  user.locationCode=j2.getInt("city");
								  if(user.locationCode<1000)user.locationCode=1000;
								  String temp=""+user.locationCode;
								  if(temp.length()==4){
									  int p1=Integer.parseInt(temp.substring(0, 2))-10;
									  int p2=Integer.parseInt(temp.substring(2, 4));
									  user.province=AddressData.PROVINCES[p1];
									  user.city=AddressData.CITIES[p1][p2];
								  }
								  userList.add(user);
							  }
							  return userList;
						  }
						  
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return kingdomPeoples(context,usr_id, animal, handler);
				  }
				
				
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return userList;
	}
	/**
	 * 加入或退出王国
	 * @param animal
	 * @param handler
	 * @param mode 0加入王国；1退出王国
	 * @return
	 */
	public static Animal joinOrQuitKingdom(Context context,Animal animal,Handler handler,int mode) {
		String url = "http://" + Constants.IP + Constants.JOIN_KINGDOM;
		if(mode==1){
			 url = "http://" + Constants.IP + Constants.EXIT_KINGDOM;
		}
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "aid="+animal.a_id;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+animal.a_id+ "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0.0","confVersion":"1.0",
             * "data":{"isSuccess":true,"percent":98},"currentTime":1416314044}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  JSONObject j2=j1.getJSONObject("data");
					  String dataStr=j1.getString("data");
					  flag=j2.getBoolean("isSuccess");
					  if(!StringUtil.isEmpty(dataStr)&&dataStr.contains("percent"))
					  animal.percent=j2.getInt("percent");
					  if(flag){
						 
						  if(Constants.user!=null&&Constants.user.aniList!=null&&Constants.user.aniList.size()>0){
							flag=setDefaultKingdom(context,Constants.user.aniList.get(0), handler); 
						  }
						  return animal;
						 
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return joinOrQuitKingdom(context,animal, handler, mode);
				  }
				
				
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return null;
	}
	/**
	 * 分享数目变化api
	 * @param animal
	 * @param handler
	 * @param mode
	 * @return
	 */
	public static User imageShareNumsApi(Context context,int img_id,Handler handler) {
		String url = "http://" + Constants.IP + Constants.IMAGE_SHARE_NUM;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "img_id="+img_id;
		String SIG = getMD5Value(value);
		String param = SIG +"&img_id="+img_id+ "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             *,"currentTime":1409652826}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					 
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"false".equals(dataStr)&&!"null".equals(dataStr)){
						  user=new User();
						  if(dataStr.contains("\"gold\"")){
							  JSONObject j2=j1.getJSONObject("data");
							  int gold=0,exp=0,lv=0;
							  if(dataStr.contains("\"gold\"")){
								  gold=j2.getInt("gold");
								  user.coinCount=j2.getInt("gold");
							  }
							  if(dataStr.contains("\"exp\"")){
								  exp=j2.getInt("exp");
								  user.exp=j2.getInt("exp");
							  }if(dataStr.contains("\"lv\"")){
								  lv=j2.getInt("lv");
								  user.lv=j2.getInt("lv");
							  }
							  if(dataStr.contains("rank")){
								  user.rankCode=j2.getInt("rank"); 
							  }
				        		 sendLevelChangeReceiver(context, user.lv, user.exp, user.coinCount, user.rankCode, 0, false,0);
						  }
						  
					  }
					  
					  return user;
				  }else if(status==1){
				  }else if(status==2){
					  
					  return imageShareNumsApi(context,img_id, handler);
				  }
				
				
			}
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return user;
	}
	/**
	 * 用户分享  分享用户资料界面截图，宠物资料界面截图
	 * @param img_id
	 * @param handler
	 * @return
	 */
	public static int userShareNumsApi(Context context,Handler handler) {
		String url = "http://" + Constants.IP + Constants.USER_SHARE_NUM;
		DefaultHttpClient client = new DefaultHttpClient();
		int gold=0;
		String value = "";
		String SIG = getMD5Value(value);
		String param = SIG + "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             *,"currentTime":1409652826}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  JSONObject j2=j1.getJSONObject("data");
					  gold=j2.getInt("gold");
					  return gold;
				  }else if(status==1){
				  }else if(status==2){
					  
					  return userShareNumsApi(context,handler);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return gold;
	}
	/**
	 * 宠物和用户关系
	 * @param animal
	 * @param handler
	 * @return
	 */
	public static boolean kingAndUserRelation(Context context,Animal animal,Handler handler) {
		String url = "http://" + Constants.IP + Constants.KING_USER_RELATION;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "aid="+animal.a_id;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+animal.a_id+ "&SID=" + Constants.SID;
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		try {
			String result =connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":{"is_fan":"0","is_follow":"0"},"currentTime":1409831726}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataString=j1.getString("data");
					  if(StringUtil.isEmpty(dataString)||"null".equals(dataString))return false;
					  JSONObject j2=j1.getJSONObject("data");
					  int fan=j2.getInt("is_fan");
					  if(fan==0){
						  animal.is_join=false;
					  }else{
						  animal.is_join=true;
					  }
					  fan=j2.getInt("is_follow");
					  if(fan==0){
						  animal.is_follow=false;
					  }else{
						  animal.is_follow=true;
					  }
					  flag=true;
					  return flag;
				  }else if(status==1){
				  }else if(status==2){
					  
					  return kingAndUserRelation(context,animal, handler);
				  }
				
				
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return flag;
	}
	/**
	 * 王国礼物列表
	 * @param animal
	 * @param handler
	 * @return
	 */
	public static ArrayList<Gift> kingdomGift(Context context,Animal animal,Handler handler) {
		String url = "http://" + Constants.IP + Constants.KINGDOM_GIFT;
		DefaultHttpClient client = new DefaultHttpClient();
		ArrayList<Gift> total=StringUtil.getGiftList(PetApplication.petApp);
		ArrayList<Gift> temp=null;
		String value = "aid="+animal.a_id;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+animal.a_id+ "&SID=" + Constants.SID;
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":{"1102":41},"currentTime":1409831726}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataString=j1.getString("data");
					  if(!StringUtil.isEmpty(dataString)&&!"null".equals(dataString)&&!"false".equals(dataString)){
						  JSONObject j2=j1.getJSONObject("data");
						 temp=new ArrayList<Gift>();
						 Gift gift=null;
						 int index=0;
						  Iterator iterator=j2.keys();
						  boolean isCorrect=false;
						  while(iterator.hasNext()){
							  gift=new Gift();
							  isCorrect=false;
							  gift.no=Integer.parseInt(iterator.next().toString());
							  if(gift.no>=1101&&gift.no<=1104){
								  isCorrect=true;
							  }
							  if(gift.no>=1201&&gift.no<=1204){
								  isCorrect=true;
							  }
							  if(gift.no>=1301&&gift.no<=1304){
								  isCorrect=true;
							  }
							  if(gift.no>=1401&&gift.no<=1404){
								  isCorrect=true;
							  }
							  if(gift.no>=2101&&gift.no<=2104){
								  isCorrect=true;
							  }
							  
							  if(!isCorrect){
								  continue;
							  }
							  index=total.indexOf(gift);
							  if(index!=-1){
								  gift=total.get(index);
								  gift.boughtNum=j2.getInt(""+gift.no);
								  temp.add(gift);
							  }
							  
						  }
						  return temp;
					  }
					 
				  }else if(status==1){
				  }else if(status==2){
					  
					  return kingdomGift(context,animal, handler);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return temp;
	}
	/**
	 * 用户的王国列表
	 * @param nid
	 * @param aid
	 * @param handler
	 * @return
	 */
	public static ArrayList<Animal> usersKingdom(Context context,User user,long aid,Handler handler) {
		String url = "http://" + Constants.IP + Constants.USER_PETS;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "usr_id="+user.userId;
		String SIG = getMD5Value(value);
		String param = SIG +"&usr_id="+user.userId+ "&SID=" + Constants.SID;
		if(aid==1){
			value="is_simple=1"+"&usr_id="+user.userId;
			SIG = getMD5Value(value);
			param = SIG +"&usr_id="+user.userId+ "&SID=" + Constants.SID+"&is_simple=1";
		}
		ArrayList<Animal> animalList=null;
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			String result =connect(client, handler, get);
            /*
             *{"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             *"data":[{
             *    {"aid":"1000000221","tx":"1000000221_headImage.png","name":"dog006",
             *    "master_id":"232","d_rq":"5","t_contri":"0","rank":"1",
             *    "news_count":"13","fans_count":"5"}],"currentTime":1409886257} 
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(!StringUtil.isEmpty("dataStr")||!"null".equals(dataStr)){
						  JSONArray ja =j1.getJSONArray("data");
						  if(ja!=null){
							  animalList=new ArrayList<Animal>();
							  JSONObject j2=null;
							  Animal animal=null;
							  for(int i=0;i<ja.length();i++){
								  animal=new Animal();
								  j2=ja.getJSONObject(i);
								  animal.a_id=j2.getLong("aid");
								  String aidStr=""+animal.a_id;
								  animal.master_id=j2.getInt("master_id");
								  animal.from=Integer.parseInt(aidStr.substring(0, 1));
								  animal.pet_iconUrl=j2.getString("tx");
								  animal.foodNum=j2.getInt("food");
								  if(dataStr.contains("\"rank\"")){
									  animal.u_rankCode=j2.getInt("rank");
									  if(animal.u_rankCode==0){
										  if(user.userId==animal.master_id){
											  animal.u_rank="经纪人";
										  }else{
											  String[] jobs=null;
											  jobs=StringUtil.getUserJobs();
											  if(jobs!=null){
												  animal.u_rank=jobs[0];
											  }
										  }
										  
									  }else{
										  if(animal.u_rankCode>8){
											  animal.u_rankCode=1;
										  }
										  String[] jobs=null;
										  jobs=StringUtil.getUserJobs();
										  if(jobs!=null){
											  animal.u_rank=jobs[animal.u_rankCode-1];
										  }
									  }
								  }
								  if(aid!=1){
									  animal.pet_nickName=j2.getString("name");
									  animal.user=user;
									  animal.d_rq=j2.getInt("d_rq");
									  animal.t_contri=j2.getInt("t_contri");
									  animal.news_count=j2.getInt("news_count");
									  animal.fans=j2.getInt("fans_count");
								  }
								  if(dataStr.contains("\"type\"")&&dataStr.contains("\"age\"")&&dataStr.contains("\"gender\"")){
									  animal.pet_nickName=j2.getString("name");
									  animal.type=j2.getInt("type");
									  animal.race=StringUtil.getRaceStr(animal.type);
									  animal.a_age=j2.getInt("age");
									  animal.a_age_str=getAge(animal.a_age);
									  animal.a_gender=j2.getInt("gender");
								  }
								  if(Constants.user!=null&&Constants.user.currentAnimal!=null&&Constants.user.currentAnimal.a_id==animal.a_id){
									  animalList.add(0,animal);
								  }else{
									  animalList.add(animal);
								  }
							  }
							  return animalList;
						  }
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return usersKingdom(context,user, aid, handler);
				  }
			}
			} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return animalList;
	}
	public static boolean setDefaultKingdom(Context context,Animal animal,Handler handler) {
		String url = "http://" + Constants.IP + Constants.CHANGE_DEFAULT_KINGDOM;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "aid="+animal.a_id;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+animal.a_id+ "&SID=" + Constants.SID;
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			String result =connect(client, handler, get);
            /*
             *{"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             *"data":{"isSuccess":true},"currentTime":1409886257} 
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(!StringUtil.isEmpty("dataStr")||!"null".equals(dataStr)){
						  JSONObject ja =j1.getJSONObject("data");
						  if(ja!=null){
							  flag=ja.getBoolean("isSuccess");
							  return flag;
						  }
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return setDefaultKingdom(context,animal, handler);
				  }
				
				
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return flag;
	}
	/**
	 * 用户物品栏
	 * @param user
	 * @param aid
	 * @param handler
	 * @return
	 */
	public static ArrayList<Gift> userItems(Context context,User user,long aid,Handler handler) {
		String url = "http://" + Constants.IP + Constants.USER_GIFT;
		DefaultHttpClient client = new DefaultHttpClient();
		ArrayList<Gift> total=StringUtil.getGiftList(PetApplication.petApp);
		ArrayList<Gift> temp=null;
		String value = "usr_id="+user.userId;
		String SIG = getMD5Value(value);
		String param = SIG +"&usr_id="+user.userId+ "&SID=" + Constants.SID;
		/*if(nid!=-1){
			value="aid="+aid+"&nid="+nid;
			SIG = getMD5Value(value);
			param = SIG +"&aid="+aid+ "&SID=" + Constants.SID+"&nid="+nid;
		}*/
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0","data":[{"usr_id":"185","name":"\u8def","tx":"","gender":"1","city":"1000","age":"0","exp":"1","lv":"0","aid":"1000000180","a_name":"\u4ed6","a_tx":""}],"currentTime":1409652826}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataString=j1.getString("data");
					  if(!StringUtil.isEmpty(dataString)&&!"null".equals(dataString)&&!"false".equals(dataString)){
						  JSONObject j2=j1.getJSONObject("data");
						 temp=new ArrayList<Gift>();
						 Gift gift=null;
						 int index=0;
						  Iterator iterator=j2.keys();
						  boolean isCorrect=false;
						  while(iterator.hasNext()){
							  gift=new Gift();
							  isCorrect=false;
							  gift.no=Integer.parseInt(iterator.next().toString());
							  if(gift.no>=1101&&gift.no<=1104){
								  isCorrect=true;
							  }
							  if(gift.no>=1201&&gift.no<=1204){
								  isCorrect=true;
							  }
							  if(gift.no>=1301&&gift.no<=1304){
								  isCorrect=true;
							  }
							  if(gift.no>=1401&&gift.no<=1404){
								  isCorrect=true;
							  }
							  if(gift.no>=2101&&gift.no<=2104){
								  isCorrect=true;
							  }
							  
							  if(!isCorrect){
								  continue;
							  }
							  index=total.indexOf(gift);
							  if(index!=-1){
								  gift=total.get(index);
								  gift.boughtNum=j2.getInt(""+gift.no);
								  if(gift.boughtNum!=0){
									  temp.add(gift);
								  }
								  
							  }
							  
						  }
					  }
					  return temp;
				  }else if(status==1){
				  }else if(status==2){
					  
					  return userItems(context,user, aid, handler);
				  }
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return temp;
	}
	/**
	 * 用户活动列表
	 * @param user
	 * @param aid
	 * @param handler
	 * @return
	 */
	public static ArrayList<PetPicture>  userActivity(Context context,User user,long aid,Handler handler) {
		String url = "http://" + Constants.IP + Constants.USER_ACTIVITY;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "usr_id="+user.userId;
		String SIG = getMD5Value(value);
		String param = SIG +"&usr_id="+user.userId+ "&SID=" + Constants.SID;
		/*if(nid!=-1){
			value="aid="+aid+"&nid="+nid;
			SIG = getMD5Value(value);
			param = SIG +"&aid="+aid+ "&SID=" + Constants.SID+"&nid="+nid;
		}*/
		ArrayList<PetPicture> petPictures=null;
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			String result = connect(client, handler, get);
            /*
             * [{"img_id":"2526","url":"2000000233_2.1409898642168.jpg",
             * "topic_name":"#\u5c31\u662f\u6211#",
             * "create_time":"1409898677"}]
             * {"img_id":"2575","url":"2000000241_11.1410840701687.jpg","topic_name":"null","create_time":"1410840714"},
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					 JSONObject j1=new JSONObject(result);
					 String dataStr=j1.getString("data");
					 if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
						 JSONArray ja=j1.getJSONArray("data");
						 if(ja!=null){
							 JSONObject j2=null;
							 petPictures=new ArrayList<PetPicture>();
							 PetPicture picture=null;
							 for(int i=0;i<ja.length();i++){
								 j2=ja.getJSONObject(i);
								 picture=new PetPicture();
								 picture.img_id=j2.getInt("img_id");
								 picture.url=j2.getString("url");
								 picture.topic_name=j2.getString("topic_name");
								 picture.create_time=j2.getLong("create_time");
								 petPictures.add(picture);
							 }
							 return petPictures;
							 
						 }
					 }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return userActivity(context,user, aid, handler);
				  }
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return petPictures;
	}
	/**
	 * 用户所在地址
	 * @param userAddress
	 * @param handler
	 * @return
	 */
	public static boolean userAddress(Context context,UserAddress userAddress,Handler handler) {
		String url = "http://" + Constants.IP + Constants.USER_ADDRESS;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "aid="+userAddress.aid;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+userAddress.aid+ "&SID=" + Constants.SID;
		url = url + param;
		HttpPost get = new HttpPost(url);
		
		ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
    	NameValuePair pair=null;
    	if(!StringUtil.isEmpty(userAddress.name)){
    		try {
//    			userAddress.name=new String(userAddress.name.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
//    			userAddress.building=new String(userAddress.building.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
//    			userAddress.region=new String(userAddress.region.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
    			pair=new BasicNameValuePair("zipcode",userAddress.zipcode);
    			pairs.add(pair);
    			pair=new BasicNameValuePair("telephone",userAddress.telephone);
    			pairs.add(pair);
    			pair=new BasicNameValuePair("region",userAddress.region);
    			pairs.add(pair);
    			pair=new BasicNameValuePair("name",userAddress.name);
    			pairs.add(pair);
    			pair=new BasicNameValuePair("building",userAddress.building);
    			pairs.add(pair);
    			get.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
    		} catch (UnsupportedEncodingException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    	}
    	
		
		
		
		
		User user=null;
		try {
			
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             * [{"name":"illè?????","telephone":"566","zipcode":"565",
             * "region":"????????? é????????","building":"??????"}]
             * ,"currentTime":1409652826}
             */
			if (resultCode == HttpStatus.SC_OK) {
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
						 String dataStr=j1.getString("data");
						 if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
							 JSONArray ja=j1.getJSONArray("data");
							 if(ja!=null&&ja.length()>0){
								JSONObject j2=ja.getJSONObject(0);
								userAddress.name=j2.getString("name");
								userAddress.telephone=j2.getString("telephone");
								userAddress.zipcode=j2.getString("zipcode");
								userAddress.region=j2.getString("region");
								userAddress.building=j2.getString("building");
								return true;
							 }
						 }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return userAddress(context,userAddress, handler);
				  }
				
				
			}else{
				judgeHttpStatus(resultCode,handler);
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Network_Status_Error);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Network_Status_Error);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 商城礼物列表下载
	 * @param last_id
	 * @param handler
	 * @return
	 */
	public static boolean marketList(Context context,int last_id,Handler handler) {
		String url = "http://" + Constants.IP + Constants.MARKET_ITEMS;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value =""+ "code=";
		String SIG = getMD5Value(value);
		String param = SIG + "&SID=" + Constants.SID+"&code=";
		if(last_id!=-1){
			value="code="+last_id;
			SIG = getMD5Value(value);
			param = SIG +"&code="+last_id+ "&SID=" + Constants.SID;
		}
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0","data":[{"usr_id":"185","name":"\u8def","tx":"","gender":"1","city":"1000","age":"0","exp":"1","lv":"0","aid":"1000000180","a_name":"\u4ed6","a_tx":""}],"currentTime":1409652826}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					 JSONObject j1=new JSONObject(result);
					 String dataStr=j1.getString("data");
					 if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
						 JSONArray ja=j1.getJSONArray("data");
						 if(ja!=null&&ja.length()>0){
							
						 }
					 }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return marketList(context,last_id, handler);
				  }
				
				
			}
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return false;
	}
	
	/**
	 * 推荐王国列表
	 * 用户认养宠物界面
	 * @param type
	 * @param aid
	 * @param handler
	 * @param currentStyle  1，推荐；2，人气
	 * @return
	 */
	
	public static ArrayList<Animal> recommendKingdom(Context context,int type,long aid,Handler handler,int currentStyle,int from) {
		String url = "http://" + Constants.IP + Constants.RECOMMEND_KINGDOM;
		if(currentStyle==2){
			url = "http://" + Constants.IP + Constants.POPULAR_KINGDOM;
		}
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String strAid="";
		String strType="";
		
		String value = ""/*+"aid="+strAid+"&type="+strType*/;
		String SIG = getMD5Value(value);
		String param = ""/*+"&aid="+strAid+"&type="+strType*/;
		/*if(type!=-1&&aid>0){
			value = ""+"page="+aid+"&type="+type;
			SIG = getMD5Value(value);
			param = SIG + "&SID=" + Constants.SID+"&page="+aid+"&type="+type;
		}else if(type!=-1){
			value = ""+"type="+type;
			SIG = getMD5Value(value);
			param = SIG + "&SID=" + Constants.SID+"&type="+type;
		}else if(aid>0){
			value = ""+"page="+aid;
			SIG = getMD5Value(value);
			param = SIG + "&SID=" + Constants.SID+"&page="+aid;
		}*/
		/*if(from!=0){
			if(!StringUtil.isEmpty(value)){
				value="from="+from;
				SIG=getMD5Value(value);
				param+="&from="+from;
			}else{
				value="from="+from;
				SIG=getMD5Value(value);
				param+="&from="+from;
			}
		}
		if(aid>0){
			if(!StringUtil.isEmpty(value)){
				value+="page="+aid;
				SIG=getMD5Value(value);
				param+="&page="+aid;
			}else{
				value+="&page="+aid;
				SIG=getMD5Value(value);
				param+="&page="+aid;
			}
		}*/
		value="from="+from+"&page="+aid;
		SIG=getMD5Value(value);
		
		param=SIG + "&SID=" + Constants.SID+"&from="+from+"&page="+aid;
		url = url + param;
		HttpGet get = new HttpGet(url);
		ArrayList<Animal> animalList=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             *[[{"aid":"1000000222","name":"lolno","tx":"","gender":"2","from":"0",
             *"type":"107","age":"55","t_rq":"0","fans":"2"},{"aid":"1000000210","name":"18",
             *"tx":"1000000210_headImage.png","gender":"2","from":"0",
             *"type":"101","age":"18","t_rq":"0","fans":"2"}]]
             *,"currentTime":1409652826}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
						  JSONArray ja1=j1.getJSONArray("data");
						  if(ja1!=null&&ja1.length()>0){
							  JSONArray ja=ja1.getJSONArray(0);
							  if(ja!=null&&ja.length()>0){
								  animalList=new ArrayList<Animal>();
								  JSONObject j2=null;
								  Animal animal=null;
								  for(int i=0;i<ja.length();i++){
									  j2=ja.getJSONObject(i);
									  animal=new Animal();
									  animal.a_id=j2.getLong("aid");
									  animal.pet_nickName=j2.getString("name");
									  animal.pet_iconUrl=j2.getString("tx");
									  animal.a_gender=j2.getInt("gender");
									  String aidStr=""+animal.a_id;
									  animal.type=j2.getInt("type");
									  if(animal.type>100&&animal.type<200){
										  animal.from=1;
									  }else if(animal.type>200&&animal.type<300){
										  animal.from=2;
									  }else if(animal.type>300){
										  animal.from=3;
									  }
									 
									  
									  animal.a_age=j2.getInt("age");
									  animal.a_age_str=getAge(animal.a_age);
									  animal.t_rq=j2.getInt("t_rq");
									  animal.fans=j2.getInt("fans");
									  animal.race=StringUtil.getRaceStr(animal.type);
									  if(animal.u_rankCode==0){
										  
											  animal.u_rank="经纪人";
										  
									  }else{
										  String[] jobs=null;
										  jobs=StringUtil.getUserJobs();
										  if(jobs!=null){
											  animal.u_rank=jobs[animal.u_rankCode-1];
										  }
									  }
									  if(from==1){
										  if(animal.from==1){
											  animalList.add(animal); 
										  }
									  }else if(from==2){
										  if(animal.from==2)
										  animalList.add(animal);
									  }else if(from==3){
										  if(animal.from==3)
										  animalList.add(animal);
									  }else{
										  animalList.add(animal);
									  }
									  
								  }
								  return animalList;
							  }
						  }
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return recommendKingdom(context,type, aid, handler,currentStyle,from);
				  }
				
				
			}
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return animalList;
	}
	/**
	 * 认养宠物   宠物的卡片信息
	 * @param aid
	 * @param handler
	 * @return
	 */
	public static KingdomCard cardApi(Context context,long aid,Handler handler) {
		String url = "http://" + Constants.IP + Constants.CARD_API;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "aid="+aid;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+aid+ "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		KingdomCard card=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             *{"master":{"usr_id":"221","name":"\u5341\u516b","tx":"221_userHeadImage.png",
             *"gender":"1","city":"1000"},
             *"images":[{"img_id":"2494","url":"1000000210_0.1409748512.png"}]}
             *,"currentTime":1409652826}
             */
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  JSONObject j2=j1.getJSONObject("data");
					  if(j2!=null){
						  card=new KingdomCard();
						  JSONObject j3=j2.getJSONObject("master");
						  if(j3!=null){
							  card.user=new User();
							  card.user.userId=j3.getInt("usr_id");
							  card.user.u_nick=j3.getString("name");
							  card.user.u_iconUrl=j3.getString("tx");
							  card.user.u_gender=j3.getInt("gender");
							  card.user.locationCode=j3.getInt("city");
							  String temp=""+card.user.locationCode;
							  if(temp.length()==4){
								  int p=Integer.parseInt(temp.substring(0, 2));
								  card.user.province=AddressData.PROVINCES[p-10];
								  int p2=Integer.parseInt(temp.substring(2, 4));
								  card.user.city=AddressData.CITIES[p-10][p2];
							  }else{
								  card.user.province=AddressData.PROVINCES[0];
								  card.user.city=AddressData.CITIES[0-10][0];
							  }
						  }
						  String imageStr=j2.getString("images");
						  if(!StringUtil.isEmpty(imageStr)&&!"null".equals(imageStr)){
							  card.list=new ArrayList<PetPicture>();
							  JSONArray ja=j2.getJSONArray("images");
							  PetPicture picture=null;
							  JSONObject j4=null;
							  if(ja!=null&&ja.length()>0){
								  for(int i=0;i<ja.length();i++){
									  j4=ja.getJSONObject(i);
									  picture=new PetPicture();
									  picture.img_id=j4.getInt("img_id");
									  picture.url=j4.getString("url");
									  card.list.add(picture);
								  }
							  }
						  }
						  return card;
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return cardApi(context,aid, handler);
				  }
				
				
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return card;
	}
	
	
	
	/**
	 * 上传图片
	 * 
	 * @param path
	 *            
	 * @return
	 */
	
	public static PetPicture uploadImage(PetPicture petPicture,Handler handler,Activity activity) {
		boolean flag = false;
		PetPicture  petPicture2=post(petPicture,activity);
		if(petPicture2!=null&&petPicture2.errorCode==-1){
			if(ShowDialog.count==0&&petPicture2.errorMessage!=null)
				ShowDialog.show(petPicture2.errorMessage, activity);
			handler.sendEmptyMessage(SubmitPictureActivity.DISMISS_PROGRESS);
			return petPicture2;
		}
		if(petPicture2!=null) {
//			data=json.datas.get(0);
			if(petPicture2.errorCode==2){
				login(activity,null);
				return uploadImage(petPicture, handler,activity);
				
			}
		}else{
			if(handler!=null)handler.sendEmptyMessage(SubmitPictureActivity.UPLOAD_IMAGE_FAILS);
			return petPicture2;
		}
		
		/*if(petPicture2!=null){
			Message msg=handler.obtainMessage();
			msg.what=SubmitPictureActivity.UPLOAD_IMAGE_SUCCESS;
			msg.obj=data;
			Constants.user.imagesCount+=1;
			handler.sendMessage(msg);
		}*/

		return petPicture2;
	}
	public static boolean isVoicedApi(Context context,Animal animal,Handler handler) {
		String url = "http://" + Constants.IP + Constants.isVoicedApi;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "aid="+animal.a_id;
		String SIG = getMD5Value(value);
		String param = SIG + "&SID=" + Constants.SID+"&aid="+animal.a_id;;
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		try {
            /**
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":{"is_voiced":true},"currentTime":1411355715}
             */
			String result = connect(client, handler, get);
            
           
			LogUtil.i("me", "url" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  JSONObject j2=j1.getJSONObject("data");
					  flag=j2.getBoolean("is_voiced");
					  return flag;
				  }else if(status==1){
				  }else if(status==2){
					  
					  return isVoicedApi(context,animal, handler);
				  }
				
				
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
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
    public static PetPicture post(PetPicture petPicture,Activity activity){
    	boolean flag = false;
    	String TAG="image";
		String value = "aid="+petPicture.animal.a_id/*+"&relates="+"&topic_name="*/;//+"&topic_id="
		String SIG = getMD5Value(value);
		String url = "http://" +Constants.IP+ Constants.UPLOAD_IMAGE_PATH + SIG + "&SID="
				+ Constants.SID+"&aid="+petPicture.animal.a_id/*+"&relates="+"&topic_name="*/;
    	Map<String, String> params=new HashMap<String, String>();
    	/*try {
			info=URLEncoder.encode(info, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	Map<String, File> files=new HashMap<String, File>();
    	if(petPicture.isVoice){
    		TAG="voice";
    		files.put("voice", new File(petPicture.voicePath));
    		url = "http://" +Constants.IP+ Constants.UPLOAD_VOICE_PATH + SIG + "&SID="
    				+ Constants.SID+"&aid="+petPicture.animal.a_id/*+"&relates="+"&topic_name="*/;
    	}else{
    		params.put("comment", petPicture.cmt);
    		if(petPicture.isBeg){
    			params.put("is_food", ""+1);
    			LogUtil.i("mi", "求口粮-------");
    		}
        	
        	params.put("topic_name", "");
        	params.put("relates", "");
        	if(!StringUtil.isEmpty(petPicture.relates)){
        		params.put("relates", petPicture.relatesString+";"+petPicture.relates);
        	}
        	if(petPicture.topic_id!=-1){
            	params.put("topic_id", ""+petPicture.topic_id);
            	params.put("topic_name", petPicture.topic_name);
            	
        	}else if(!StringUtil.isEmpty(petPicture.topic_name)){
        		params.put("topic_name", petPicture.topic_name);
        	}
        	File file;
        	
        	files.put("image", new File(petPicture.petPicture_path));
    	}
    	
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
            conn.setReadTimeout(60 * 1000); // 缓存的最长时间
            conn.setConnectTimeout(60 * 1000);
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
	                sb1.append("Content-Disposition: form-data; name=\""+TAG+"\"; filename=\""
	                        + file.getValue().getName() + "\"" + LINEND);
	                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
	                sb1.append(LINEND);
	                outStream.write(sb1.toString().getBytes());
	                LogUtil.i("me", "sb.toString文件参数===="+sb1.toString());
	                LogUtil.i("me", "sb.toString图片数据流====：：：");
	                LogUtil.i("me", "图片大小====：：："+file.getValue().length());

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
	        LogUtil.i("me", "上传图片url===="+url);
	        LogUtil.i("me", "上传音频或图片返回结果===="+sb2.toString());
	        if(petPicture.isVoice){
	        	/*
	        	 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
	        	 * "data":{"isSuccess":true},"currentTime":1409905865}
	        	 */
	        	JSONObject jsonObject=new JSONObject(sb2.toString());
	        	int status=jsonObject.getInt("state");
	        	if(status==2){
	        		login(activity,null);
	        		post(petPicture, activity);
	        	}
	        	String dataStr=jsonObject.getString("data");
	        	if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
	        		jsonObject=jsonObject.getJSONObject("data");
	        		
	        		 int gold=0,exp=0,lv=0;
					  if(dataStr.contains("\"gold\"")){
						  gold=jsonObject.getInt("gold");
					  }
					  if(dataStr.contains("\"exp\"")){
						  exp=jsonObject.getInt("exp");
					  }if(dataStr.contains("\"lv\"")){
						  lv=jsonObject.getInt("lv");
					  }
	        		sendLevelChangeReceiver(activity, lv, exp, gold, 0, 0, false,0);
	        		petPicture.updateVoiceSuccess=true;
	        		if(exp!=-1){
	        			petPicture.updateVoiceSuccess=true;
	        		}

	        		return petPicture;
	        	}
	        }
	        PetPicture  petPicture2=parseUpdateImageJson(sb2.toString());
//	        UserImagesJson data=parseUpdateImageJson(sb2.toString());
	        if(petPicture2!=null){
	        	return petPicture2;
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ShowDialog.count==0)
			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
		} catch (JSONException e) {
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
        return petPicture;
        
    }
    /**
     * 上传头像
     * 
     * @param file 需要上传的文件
     * @param RequestURL 请求的rul
     * @param aid -1,上传用户头像；非-1，宠物头像且为宠物aid
     * @return 返回响应的内容
     */
    public static String uploadUserIcon(String path,final Activity activity,long aid) {
    	String TAG = "tx";
            File fi=new File(path);
            LogUtil.i("me", "头像大小"+fi.length());

    	     int TIME_OUT = 50 * 1000; // 超时时间


    	    String CHARSET = "utf-8"; // 设置编码
    		String value = "";
    		String SIG = getMD5Value("");
    		String RequestURL =  Constants.USER_UPDATE_TX + SIG + "&SID="
    				+ Constants.SID;
    		if(aid!=-1){
    			value="aid="+aid;
    			SIG=getMD5Value(value);
    			RequestURL =  Constants.Animal_UPDATE_TX + SIG + "&SID="
        				+ Constants.SID+"&aid="+aid;
    			TAG="tx";
    		}
    	String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        File file=new File(path);
        DataOutputStream dos =null;
        InputStream input=null;
        HttpURLConnection conn =null;
LogUtil.i("me", "上传头像+文件路径="+path);
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


                sb.append("Content-Disposition: form-data; name=\""+TAG+"\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                LogUtil.i("me", "上传头像开始");
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                    LogUtil.i("me", "上传头像进行中");
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                LogUtil.i("me", "上传头像结束");
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
                LogUtil.i("me", "user url : " + RequestURL);
                LogUtil.i("me", "user icon result : " + result);
                UpdateIconJson json=new UpdateIconJson(result);
                if(json.errorCode==2){
                	login(activity,null);
                	return uploadUserIcon(path,activity,aid);
                }
                
                return json.data.tx;
                // }
                // else{
                // Log.e(TAG, "request error");
                // }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LogUtil.i("me", "上传头像结束MalformedURLException");
            if(ShowDialog.count==0)
            ShowDialog.show(Constants.NOTE_MESSAGE_5, activity);
        } catch (IOException e) {
        	LogUtil.i("me", "上传头像结束IOException");
            e.printStackTrace();
            if(ShowDialog.count==0)
            ShowDialog.show(Constants.NOTE_MESSAGE_5, activity);
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
     * @param mode  0 王国图片列表；1 Favorite列表；2 recommend 萌宠推荐列表;3 random 列表
     */
	public static UserImagesJson downloadPetkingdomImages(Handler handler,int last_id,int  mode,final Activity activity,long aid) {
		String SIG=null;
		String url = null;
		String param=null;
		if(mode==0&&last_id==-1){
			String value ="aid="+aid;
			SIG = getMD5Value(value);
			param = "sig="+SIG+"&SID="+Constants.SID+"&aid="+aid;
		}else if(mode==0&&last_id!=-1){
			String value ="aid="+aid+"&img_id="+last_id;
			SIG = getMD5Value(value);
			param ="img_id="+last_id+"&sig="+SIG+"&SID="+Constants.SID+"&aid="+aid;;
		}else if(last_id==-1){
			String value ="";
			SIG = getMD5Value(value);
			param = "sig="+SIG+"&SID="+Constants.SID;
		}else{
			String value ="img_id="+last_id;
			SIG = getMD5Value(value);
			param ="img_id="+last_id+"&sig="+SIG+"&SID="+Constants.SID;
		}
		
		switch (mode) {
		case 0:
			url=Constants.KINGDOM_IMAGES+param;
			break;
		case 1:
			url=Constants.IMAGE_FAVORITE+param;
			break;
		case 3:
			url=Constants.IMAGE_RANDOM+param;
			break;
		case 2:
			url=Constants.IMAGE_RECOMMEND+param;
			mode=3;
			break;
		}
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);

			String result = connect(client, handler, get);
			LogUtil.i("me", "下载图片列表url==" + url);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "下载图片返回结果" + result);
				UserImagesJson json=new UserImagesJson(result);
				
				if(json.errorCode==2){
//					login(null, null);
					return downloadPetkingdomImages(handler, last_id, mode,activity,aid);
					
				}
				if(handler!=null){
					Message msg=handler.obtainMessage();
					msg.obj=json.petPictures;
					msg.what=Constants.MESSAGE_DOWNLOAD_IMAGES_LIST;
					handler.sendMessage(msg);
				}
				
				return json;
			}
		return null;
	}
	/**
	 * 下载其他用户列表
	 * @param handler
	 * @param last_id
	 * @param usr_id
	 */
	public static UserImagesJson downloadOtherUserHomepage(Handler handler,int last_id,UserImagesJson.Data data,final Activity activity) {
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
		url=Constants.USER_IMAGES+param;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
			String result = connect(client, handler, get);
			// TODO
			LogUtil.i("me", "下载图片列表url==" + url);
			
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "下载图片返回结果" + result);
				UserImagesJson json=new UserImagesJson(result);
				if(json.errorCode==2){
//					login(null, null);
					return downloadOtherUserHomepage(handler, last_id, data,activity);
				}
				if(handler!=null){
					Message msg=handler.obtainMessage();
					msg.obj=json;
					msg.what=Constants.MESSAGE_DOWNLOAD_IMAGES_LIST;
				}
				
//				handler.sendMessage(msg);
				return json;
			}
		return null;
	}
	/**
	 * 点赞
	 * 
	 * @param topic
	 */
	public static boolean likeImage(PetPicture petPicture,Handler handler,final Context context) {
	
		
		
		
		boolean flag=false;
		String url = "http://" + Constants.IP + Constants.LIKE_IMAGE_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "img_id=" + petPicture.img_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "&img_id=" + petPicture.img_id + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		HttpGet get = new HttpGet(url);
			String result = connect(client, handler, get);
			LogUtil.i("me","点赞url="+url);
			
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "点赞返回结果" + result);
			
				RegisterJson json=parseRegisterJson(result,handler);
				if(json!=null){
					if(json.errorCode==0){
						petPicture.likes+=1;
						
						/*
						 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0.0",
						 * "confVersion":"1.0","data":{"gold":1},"currentTime":1416228827}
						 */
						try {
						JSONObject jo=new JSONObject(result);
						String dataStr=jo.getString("data");
						if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)&&!"false".equals(dataStr)){
							JSONObject jb;
							
								jb = jo.getJSONObject("data");
							
							if(jb!=null){
								int gold= jb.getInt("gold");
								
								sendLevelChangeReceiver(context,0,0,gold,0,0,false,0);
							
								if(StringUtil.isEmpty(petPicture.likers)){
									
									petPicture.likers=""+Constants.user.userId;
//									petPicture.likeUsersList=new ArrayList<User>();
//									petPicture.likeUsersList.add(Constants.user);
								}else{
//									petPicture.likeUsersList.add(Constants.user);
									petPicture.likers+=","+Constants.user.userId;
								}
								if(petPicture.like_txUrlList!=null&&Constants.user.u_iconUrl!=null){
									petPicture.like_txUrlList.add(Constants.user.u_iconUrl);
									
								}else if(Constants.user.u_iconUrl!=null){
									petPicture.like_txUrlList=new ArrayList<String>();
									petPicture.like_txUrlList.add(Constants.user.u_iconUrl);
								}
								if(handler!=null){
									handler.post(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											LogUtil.i("mi", "点赞数目更新");
											MobclickAgent.onEvent(context, "like");
										}
									});
								}
								return true;
							}
							
						}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
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
		return flag;
	}
	/**
	 * 根据图片id获得图片的详细信息
	 * @param data
	 */
	public static boolean imageInfo(PetPicture petPicture,Handler handler,final Activity activity) {
		String url =Constants.IMAGE_INFO;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "img_id=" + petPicture.img_id +"&usr_id=";
		String SIG = getMD5Value(value);
		String param = "img_id=" + petPicture.img_id + "&sig=" + SIG + "&SID="
				+Constants.SID+"&usr_id=" ;
		if(Constants.isSuccess&&Constants.user!=null){
			value = "img_id=" + petPicture.img_id + "&usr_id="+Constants.user.userId;
			 SIG = getMD5Value(value);
			 param = "img_id=" + petPicture.img_id + "&sig=" + SIG + "&SID="
						+Constants.SID+"&usr_id="+Constants.user.userId ;
		}
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			String result = connect(client, handler, get);
			LogUtil.i("me","获得一张图片的详细信息url="+url);
			
			JSONObject jsonObject=null;
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "获得一张图片的详细信息" + result);
				jsonObject = new JSONObject(result);
				if(jsonObject.getInt("errorCode")==2){
//					login(null, null);
					imageInfo(petPicture, handler,activity);
					return false;
				}
				/*
				 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
				 * "data":{
				 *    "image":{"img_id":"2499","aid":"1000000219","topic_id":"0","topic_name":"",
				 *    "relates":"","cmt":"","url":"1000000219_1.1409797542.png",
				 *    "likes":"1","likers":"231","gifts":"0","senders":"","comments":"","sharers":"",
				 *    "shares":"0","create_time":"1409797540","update_time":"2014-09-04 03:05:50",
				 *    "is_deleted":"\u0000"},"is_follow":false,"sender_tx":null,
				 *    "liker_tx":["231_userHeadImage.png"]},"currentTime":1409801016}
				 */
				JSONObject jsonArray=jsonObject.getJSONObject("data");
				JSONObject image=jsonArray.getJSONObject("image");
						petPicture.cmt=image.getString("cmt");
						petPicture.animal=new Animal();
						petPicture.topic_id=image.getInt("topic_id");
						petPicture.animal.a_id=image.getLong("aid");
						petPicture.topic_name="#"+image.getString("topic_name")+"#";
						petPicture.relates=image.getString("relates");
						petPicture.url=image.getString("url");
						if(!StringUtil.isEmpty(petPicture.relates)){
							String[] str=petPicture.relates.split(";");
							for(int i=0;i<str.length;i++){
								if(i==0){
									petPicture.relatesString=str[0];
								}else if(i==1){
									petPicture.relates=str[1];
								}
							}
						}
						petPicture.gifts=image.getInt("gifts");
						petPicture.senders=image.getString("senders");
						petPicture.shares=image.getInt("shares");
						petPicture.is_deleted=image.getString("is_deleted");
						
						petPicture.create_time=image.getLong("create_time");
						petPicture.likers=image.getString("likers");
						petPicture.likes=image.getInt("likes");
						petPicture.comments=image.getString("comments");
						petPicture.share_ids=image.getString("sharers");
						if(petPicture.comments!=null){
							
							String[] strs=petPicture.comments.split("usr_id:");
							
							// 253,name:fans1,reply_id:281,reply_name:äºåäº,body:法人,create_time:1411126981
							 
							if(strs!=null&&strs.length>1){
								PetPicture.Comments comment=null;
								petPicture.comment_ids="";
								petPicture.commentsList=new ArrayList<PetPicture.Comments>();
								for(int i=0;i<strs.length;i++){
									comment=new PetPicture.Comments();
									String cstr=strs[i];
									if(StringUtil.isEmpty(cstr))continue;
									int start=0;
									int end=cstr.indexOf(",name:");
									if(end<0)continue;
									comment.usr_id=Integer.parseInt(cstr.substring(start, end));
									if(StringUtil.isEmpty(petPicture.comment_ids)){
										petPicture.comment_ids=""+comment.usr_id;
									}else{
										petPicture.comment_ids+=","+comment.usr_id;
									}
									cstr=cstr.substring(end+6);
									start=0;
									if(cstr.contains(",reply_name")&&cstr.contains(",reply_id")){
										end=cstr.indexOf(",reply_id:");
										comment.isReply=true;
										comment.name=cstr.substring(0, end);
										cstr=cstr.substring(end+10);
										end=cstr.indexOf(",reply_name:");
										comment.reply_id=Integer.parseInt(cstr.substring(0, end));
										cstr=cstr.substring(end+12);
										end=cstr.indexOf(",body:");
										comment.reply_name=cstr.substring(0, end);
										cstr=cstr.substring(end+6);
									}else{
										end=cstr.indexOf(",body:");
										comment.name=cstr.substring(0,end);
										cstr=cstr.substring(end+6);
									}
									end=cstr.indexOf(",create_time:");
									comment.body=cstr.substring(0, end);
									cstr=cstr.substring(end+13);
									if(cstr.contains(";")){
										comment.create_time=Long.parseLong(cstr.substring(0,cstr.length()-1));
									}else{
										comment.create_time=Long.parseLong(cstr.substring(0,cstr.length()));
									}
									
									petPicture.commentsList.add(comment);
									
									
								}
								/*if(petPicture.commentsList!=null){
									ArrayList<PetPicture.Comments> temp=new ArrayList<PetPicture.Comments>();
									for(int i=petPicture.commentsList.size()-1;i>=0;i--){
										temp.add(petPicture.commentsList.get(i));
									}
									petPicture.commentsList=temp;
								}*/
							}
						}
						int follow=jsonArray.getInt("is_follow");
						if(follow==0){
							petPicture.animal.is_follow=false;
						}else{
							petPicture.animal.is_follow=true;
						}
						
						String tx=jsonArray.getString("liker_tx");
						if(tx!=null&&!"null".equals(tx)){
							JSONArray arrays=jsonArray.getJSONArray("liker_tx");
							User user=null;
							if(arrays!=null&&arrays.length()>0){
								ArrayList<String> strs=new ArrayList<String>();
//								ArrayList<User> users=new ArrayList<User>();
								for(int i=0;i<arrays.length();i++){
									strs.add(arrays.getString(i));
									user=new User();
									user.u_iconUrl=arrays.getString(i);
//									users.add(user);
								}
								petPicture.like_txUrlList=strs;
//								petPicture.likeUsersList=users;
							}
						}
//						if(petPicture.likeUsersList==null)petPicture.likeUsersList=new ArrayList<User>();
						tx=jsonArray.getString("sender_tx");
						if(tx!=null&&!"null".equals(tx)){
							JSONArray arrays=jsonArray.getJSONArray("sender_tx");
							User user=null;
							if(arrays!=null&&arrays.length()>0){
								ArrayList<String> strs=new ArrayList<String>();
//								ArrayList<User> users=new ArrayList<User>();
								for(int i=0;i<arrays.length();i++){
									strs.add(arrays.getString(i));
									user=new User();
									user.u_iconUrl=arrays.getString(i);
//									users.add(user);
								}
								petPicture.gift_txUrlList=strs;
//								petPicture.giftUsersList=users;
							}
						}
//						if(petPicture.giftUsersList==null)petPicture.giftUsersList=new ArrayList<User>();
						petPicture.animal=animalInfo(activity,petPicture.animal, handler);
//						handler.sendEmptyMessage(1);
						return true;
				
				}
		      petPicture.animal=animalInfo(activity,petPicture.animal, handler);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
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
	public static boolean userAddFollow(Animal animal,Handler handler,Activity activity) {
		String url =Constants.USER_FOLLOW;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "aid=" + animal.a_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "aid=" + animal.a_id + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		boolean flag=false;
		HttpGet get = new HttpGet(url);
		try {
			String result = connect(client, handler, get);
			LogUtil.i("me","添加关注url="+url);
			LogUtil.i("me", "添加关注返回结果" + result);
			JSONObject jsonObject=null;
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  jsonObject=new JSONObject(result);
					  String dataStr=jsonObject.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)&&!"false".equals(dataStr)){
						JSONObject jo=jsonObject.getJSONObject("data");
						return jo.getBoolean("isSuccess");
					  }
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return userDeleteFollow(animal, handler, activity);
				  }
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		} 
		return flag;
	}
	public static ArrayList<Animal> otherAnimals(Handler handler,String aids,long aid,Activity activity) {
		String SIG=null;
		String url = null;
		String param=null;
//		aids=aids+",";
		url=Constants.ANIMAL_OTHERS_INFO;
		/*if(mode==1){
			url=Constants.USER_FOLLOWING;
		}else{
			url=Constants.USER_FOLLOWER;
		}*/
		ArrayList<Animal> animalList=null;
		String value ="aids="+aids;
		SIG = getMD5Value(value);
		param = "sig="+SIG+"&SID="+Constants.SID+"&aids="+aids;
		/*if(last_id==-1){
			String value ="usr_id="+usr_id;
			SIG = getMD5Value(value);
			param = "sig="+SIG+"&SID="+Constants.SID+"&usr_id="+usr_id;
		}else{
			String value ="usr_id="+last_id+"dog&cat";
			SIG = getMD5(value);
			param ="usr_id="+last_id+"&sig="+SIG+"&SID="+Constants.SID;
		}*/
		url=url+param;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			String result = connect(client, handler, get);
			/*
			 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
			 * "data":[{
			 *    "aid":"2000000241","name":"Joke","tx":"2000000241_1411010687969.jpg",
			 *    "age":"2","gender":"2","is_follow":"0"}],
			 * "currentTime":1409834271}
			 */
         
			
			// TODO
			LogUtil.i("me", "根据宠物aids获得一串宠物信息url==" + url);
			
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "根据宠物aids获得一串宠物信息" + result);
				int status=handleResult(activity,result, handler);
				
				if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(StringUtil.isEmpty(dataStr)||"null".equals(dataStr))return null;
					  JSONArray ja=j1.getJSONArray("data");
					  if(ja!=null&&ja.length()>0){
						  animalList=new ArrayList<Animal>();
						  Animal animal=null;
						  JSONObject j2=null;
						  for(int i=0;i<ja.length();i++){
							  j2=ja.getJSONObject(i);
							  animal=new Animal();
							  animal.a_id=j2.getLong("aid");
							  String aidStr=""+animal.a_id;
							  animal.from=Integer.parseInt(aidStr.substring(0, 1));
							  animal.pet_iconUrl=j2.getString("tx");
							  animal.pet_nickName=j2.getString("name");
							  animal.a_age=j2.getInt("age");
							  animal.a_age_str=getAge(animal.a_age);
							  animal.a_gender=j2.getInt("gender");
							  int is_follow=j2.getInt("is_follow");
							  if(is_follow==0){
								  animal.is_follow=false;
							  }else{
								  animal.is_follow=true;
							  }
//							  animal.t_rq=j2.getInt("t_rq");
//							 animal.type=j2.getInt("type");
////							 animal.u_name=j2.getString("u_name");
//							  if(animal.type>100&&animal.type<200){
//								  animal.type-=101;
//								  String[] strArray=PetApplication.petApp.getResources().getStringArray(R.array.cat_race);
//								  if(animal.type<strArray.length){
//									  animal.race=strArray[animal.type];
//								  }else{
//									  animal.race=strArray[0];
//								  }
//								  
//							  }else if(animal.type>200&&animal.type<300){
//								  animal.type-=201;
//								  String[] strArray=PetApplication.petApp.getResources().getStringArray(R.array.dog_race);
//								  if(animal.type<strArray.length){
//									  animal.race=strArray[animal.type];
//								  }else{
//									  animal.race=strArray[0];
//								  }
//							  }
							  animalList.add(animal);
						  }
						  return animalList;
					  }
				  }else if(status==1){
				  }else if(status==2){
					  return otherAnimals(handler, aids, aid, activity);
				  }
				
				return animalList;
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return animalList;
	}
	public static boolean userDeleteFollow(Animal animal,Handler handler,Activity activity) {
		String url =Constants.USER_UNFOLLOW;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "aid=" + animal.a_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "aid=" + animal.a_id + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		boolean flag=false;
		HttpGet get = new HttpGet(url);
		try {
			String result = connect(client, handler, get);
			LogUtil.i("me","取消关注url="+url);
			
			JSONObject jsonObject=null;
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "取消关注返回结果" + result);
				  int status=handleResult(activity,result,handler);
				  if(status==0){
					  jsonObject=new JSONObject(result);
					  String dataStr=jsonObject.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)&&!"false".equals(dataStr)){
						JSONObject jo=jsonObject.getJSONObject("data");
						return jo.getBoolean("isSuccess");
					  }
				  }else if(status==1){
					  return false;
				  }else if(status==2){
					  return userDeleteFollow(animal, handler, activity);
				  }
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		} 
		return flag;
	}
	/**
	 * 用户关注列表
	 * @param handler
	 * @param last_id
	 * @param mode  1 关注列表；2 被关注列表
	 */
	public static ArrayList<Animal> followList(Handler handler,long last_id,int  usr_id,Activity activity) {
		String SIG=null;
		String url = null;
		String param=null;
		url=Constants.USER_FOLLOWING;
		/*if(mode==1){
			url=Constants.USER_FOLLOWING;
		}else{
			url=Constants.USER_FOLLOWER;
		}*/
		ArrayList<Animal> animalList=null;
		String value ="usr_id="+usr_id;
		SIG = getMD5Value(value);
		param = "sig="+SIG+"&SID="+Constants.SID+"&usr_id="+usr_id;
		/*if(last_id==-1){
			String value ="usr_id="+usr_id;
			SIG = getMD5Value(value);
			param = "sig="+SIG+"&SID="+Constants.SID+"&usr_id="+usr_id;
		}else{
			String value ="usr_id="+last_id+"dog&cat";
			SIG = getMD5(value);
			param ="usr_id="+last_id+"&sig="+SIG+"&SID="+Constants.SID;
		}*/
		url=url+param;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			String result = connect(client, handler, get);
			/*
			 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
			 * "data":[{
			 * "aid":"1000000220","tx":"1000000220_headImage.png",
			 * "name":"01","type":"101","age":"1","gender":"2",
			 * "t_rq":"0","u_name":"01"}],
			 * "currentTime":1409834271}
			 */
         
			
			// TODO
			LogUtil.i("me", "关注与被关注列表url==" + url);
			LogUtil.i("me", "关注与被关注列表返回结果" + result);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				int status=handleResult(activity,result, handler);
				
				if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(StringUtil.isEmpty(dataStr)||"null".equals(dataStr))return null;
					  JSONArray ja=j1.getJSONArray("data");
					  if(ja!=null&&ja.length()>0){
						  animalList=new ArrayList<Animal>();
						  Animal animal=null;
						  JSONObject j2=null;
						  for(int i=0;i<ja.length();i++){
							  j2=ja.getJSONObject(i);
							  animal=new Animal();
							  animal.a_id=j2.getLong("aid");
							  String aidStr=""+animal.a_id;
							  animal.from=Integer.parseInt(aidStr.substring(0, 1));
							  animal.pet_iconUrl=j2.getString("tx");
							  animal.pet_nickName=j2.getString("name");
							  animal.a_age=j2.getInt("age");
							  animal.a_age_str=getAge(animal.a_age);
							  animal.a_gender=j2.getInt("gender");
							  animal.t_rq=j2.getInt("t_rq");
							 animal.type=j2.getInt("type");
							 animal.u_name=j2.getString("u_name");
							 animal.race=StringUtil.getRaceStr(animal.type);
							  if(animal.u_rankCode==0){
								 
									  animal.u_rank="经纪人";
								  
							  }else{
								  String[] jobs=null;
								  jobs=StringUtil.getUserJobs();
								  if(jobs!=null){
									  animal.u_rank=jobs[animal.u_rankCode-1];
								  }
							  }
							  animalList.add(animal);
						  }
						  return animalList;
					  }
				  }else if(status==1){
				  }else if(status==2){
					  return followList(handler, last_id, usr_id, activity);
				  }
				
				return animalList;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return animalList;
	}
	/**
	 * 根据一串user_id，获取多个user信息（头像，性别等）
	 * @param petPicture
	 * @param handler
	 * @mode 1,送礼物；2，点赞；
	 * @return
	 */
	public static ArrayList<User> getOthersList(String likers,Handler handler,Activity activity,int mode) {
		String url =Constants.OTHERS_INFO;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "usr_ids=" + likers + "dog&cat";
		String SIG = getMD5(value);
		String param = "usr_ids=" + likers + "&sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		boolean flag=false;
		ArrayList<User> animalList=null;
		HttpGet get = new HttpGet(url);
		try {
			String result =connect(client, handler, get);
			LogUtil.i("me","获取一串用户信息的url="+url);
			
			JSONObject jsonObject=null;
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "获取一串用户信息的返回结果" + result);
				UserJson json=new UserJson();
				json.datas=new ArrayList<UserJson.Data>();
				JSONObject object1=new JSONObject(result);
				json.errorCode=object1.getInt("errorCode");
				if(judgeSID(json.errorCode)){
					return getOthersList(likers, handler, activity,mode);
				}
				json.errorMessage=object1.getString("errorMessage");
				if(json.errorCode==0){
					JSONArray array=object1.getJSONArray("data");
					if(array!=null&&array.length()>0){
//						JSONArray array=object2.getJSONArray(0);
						if(array!=null&&array.length()>0){
							/*
							 *{"usr_id":"231","name":"01","tx":"231_userHeadImage.png",
							 *"city":"1000","gender":"1"}
							 */
							JSONObject object3=null;
							User user=null;
							animalList=new ArrayList<User>();
							for(int i=0;i<array.length();i++){
								object3=array.getJSONObject(i);
								user=new User();
								user.userId=object3.getInt("usr_id");
								user.u_nick=object3.getString("name");
								user.u_iconUrl=object3.getString("tx");
								user.locationCode=object3.getInt("city");
								user.u_gender=object3.getInt("gender");
							   user.senderOrLiker=mode;
								String temp=""+user.locationCode;
								  if(temp.length()==4){
									  int p=Integer.parseInt(temp.substring(0, 2));
									  user.province=AddressData.PROVINCES[p-10];
									  int p2=Integer.parseInt(temp.substring(2, 4));
									  user.city=AddressData.CITIES[p-10][p2];
								  }else{
									  user.province=AddressData.PROVINCES[0];
									  user.city=AddressData.CITIES[0-10][0];
								  }
								  if(!animalList.contains(user))
							    animalList.add(user);
							 }
							return animalList;
						}
					}
				}
				}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		} 
		return animalList;
	}
	
	/**
	 * 获取黑名单列表
	 * @param likers
	 * @param handler
	 * @param activity
	 * @param mode
	 * @return
	 */
	public static ArrayList<User> getBlockList(Handler handler,Activity activity) {
		String url =Constants.BLOCK_LIST;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "";
		String SIG = getMD5Value(value);
		String param ="sig=" + SIG + "&SID="
				+Constants.SID ;
		url = url + param;
		boolean flag=false;
		ArrayList<User> animalList=null;
		HttpGet get = new HttpGet(url);
		try {
			String result =connect(client, handler, get);
			LogUtil.i("me","获取一串用户信息的url="+url);
			
			JSONObject jsonObject=null;
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "获取一串用户信息的返回结果" + result);
				UserJson json=new UserJson();
				json.datas=new ArrayList<UserJson.Data>();
				JSONObject object1=new JSONObject(result);
				json.errorCode=object1.getInt("errorCode");
				if(judgeSID(json.errorCode)){
					return getBlockList(handler, activity);
				}
				json.errorMessage=object1.getString("errorMessage");
				if(json.errorCode==0){
					JSONArray array=object1.getJSONArray("data");
					if(array!=null&&array.length()>0){
//						JSONArray array=object2.getJSONArray(0);
						if(array!=null&&array.length()>0){
							/*
							 *{"usr_id":"231","name":"01","tx":"231_userHeadImage.png",
							 *"city":"1000","gender":"1"}
							 *data":[{"usr_id":"315","name":"\u6211\u7231\u4e48\u4e48\u54d2","tx":"315_1414160303402_usr_icon.png"}],"
							 */
							JSONObject object3=null;
							User user=null;
							animalList=new ArrayList<User>();
							for(int i=0;i<array.length();i++){
								object3=array.getJSONObject(i);
								user=new User();
								user.userId=object3.getInt("usr_id");
								user.u_nick=object3.getString("name");
								user.u_iconUrl=object3.getString("tx");
							    animalList.add(user);
							 }
							return animalList;
						}
					}
				}
				}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
//			if(ShowDialog.count==0)
//			ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		} 
		return animalList;
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
			String result = connect(client, handler, get);
			LogUtil.i("me","获得其他用户信息url="+url);
			
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
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "获得其他用户信息" + result);
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
							data.user.a_age=((JSONObject)jsonArray.getJSONObject("user")).getString("age");
							data.user.pet_nickName=((JSONObject)jsonArray.getJSONObject("user")).getString("name");
							data.user.race=((JSONObject)jsonArray.getJSONObject("user")).getString("type");
							data.user.a_gender=((JSONObject)jsonArray.getJSONObject("user")).getInt("gender");
							data.user.pet_iconUrl=((JSONObject)jsonArray.getJSONObject("user")).getString("tx");
							data.user.userId=((JSONObject)jsonArray.getJSONObject("user")).getInt("usr_id");
							if(handler!=null)
							handler.sendEmptyMessage(1);
							return true;
				}

				}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
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
	 * @param user
	 * @param aid
	 * @param handler
	 * @return
	 */
	public static String getVoiceUrl(Context context,long aid,Handler handler) {
		String url = "http://" + Constants.IP + Constants.DOWNLOAD_VOICE_URL;
		DefaultHttpClient client = new DefaultHttpClient();
		String path=null;
		String value = "aid="+aid;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+aid+ "&SID=" + Constants.SID;
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			String result =connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "currentTime":1409652826}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject jObject=new JSONObject(result);
					  String dataString=jObject.getString("data");
					  if(!StringUtil.isEmpty(dataString)&&!"null".equals(dataString)){
						  JSONObject j1=jObject.getJSONObject("data");
						  path=j1.getString("url");
						  return path;
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return getVoiceUrl(context,aid, handler);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return path;
	}
	public static String  downloadVoiceFile(String aid,Handler handler){
		String value = "aid="+/*aid*/2000000243;
		boolean flag=false;
		String url=/*"http://"+Constants.IP+*/Constants.DOWNLOAD_VOICE+aid;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		BufferedInputStream bis=null;
		BufferedOutputStream bos=null;
		String path=null;
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

//			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me", "url" + url);
//			LogUtil.i("me", "result=" + result);
			if (resultCode == HttpStatus.SC_OK) {
				
//				LogUtil.i("me", "info返回结果" + result);
//				InfoJson infoJson=parseInfoJson(result,activity);
//				Constants.infoJson=infoJson;
				HttpEntity entity=response.getEntity();
//				LogUtil.i("me", ""+EntityUtils.toString(entity));
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
				LogUtil.i("me","下载声音url="+url);
				LogUtil.i("me", "欢迎页文件" + Constants.Picture_Root_Path+File.separator+aid+".mp3");
				bos=new BufferedOutputStream(new FileOutputStream(new File(Constants.Picture_Root_Path+File.separator+aid+".mp3")));
				LogUtil.i("me","downloadfile="+Constants.Picture_Root_Path+File.separator+aid+".mp3");
				int count=0;
				while((len=bis.read(buffer, 0, buffer.length))!=-1){
					bos.write(buffer, 0, len);
					count+=len;
					LogUtil.i("me","下载声音url="+buffer.toString());
				}
				bos.flush();
				flag=true;
				if(new File(Constants.Picture_Root_Path+File.separator+aid+".mp3").exists()&&count>1024){
					path=Constants.Picture_Root_Path+File.separator+aid+".mp3";
					return path;
				}else{
					path=null;
				}
				
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
		return path;
	}
	/**
	 * 官方话题列表
	 * @param handler
	 * @return
	 */
	public static ArrayList<Topic> imageTopicApi(Context context,Handler handler) {
		String url = "http://" + Constants.IP + Constants.IMAGE_TOPIC_API;
		DefaultHttpClient client = new DefaultHttpClient();
		String path=null;
		String value = "";
		String SIG = getMD5Value(value);
		String param = SIG + "&SID=" + Constants.SID;
		
		ArrayList<Topic> topicList=null;
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			String result =connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":[{"topic_id":"4","topic":"\u72d7\u72d7\u9009\u7f8e\u5927\u8d5b"},
             * {"topic_id":"3","topic":"\u840c\u5ba0\u65f6\u88c5\u79c0"},
             * {"topic_id":"1","topic":"\u6d3b\u52a8\u6d4b\u8bd5"}]，
             * "currentTime":1409652826}
             */
			
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject jObject=new JSONObject(result);
					  String dataString=jObject.getString("data");
					  if(!StringUtil.isEmpty(dataString)&&!"null".equals(dataString)){
						  JSONArray ja=jObject.getJSONArray("data");
						  topicList=new ArrayList<Topic>();
						  JSONObject j2=null;
						  Topic topic=null;
						  if(ja!=null&&ja.length()>0){
							  for(int i=0;i<ja.length();i++){
								  topic=new Topic();
								  j2=ja.getJSONObject(i);
								  topic.topic_id=j2.getInt("topic_id");
								  topic.topic=j2.getString("topic");
								  topicList.add(topic);
							  }
							  return topicList;
						  }
						  
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return imageTopicApi(context,handler);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return topicList;
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
	public static void deleteImage(UserImagesJson.Data data,Activity activity) {
		String url = "http://" + Constants.IP + Constants.LIKE_IMAGE_PATH;
		DefaultHttpClient client = new DefaultHttpClient();
		String value = "ima_id=" + data.img_id + "dog&cat";
		String SIG = getMD5(value);
		String param = "&ima_id=" + data.img_id + "&sig=" + SIG + "&SID=["
				+ "]";
		url = url + param;
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();

			String result = EntityUtils.toString(response.getEntity());
			LogUtil.i("me", "删除图片结果" + result);
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
    public static ActivityJson loadTopicList(Activity activity,int id,Handler handler){
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
    		String result=connect(client, handler, get);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "活动列表url="+url);
				
				LogUtil.i("me", "活动列表返回结果="+result);
				int status=handleResult(activity,result,handler);
				  if(status==0){
					  return new ActivityJson(result);
				  }else if(status==1){
				  }else if(status==2){
					  
					  return loadTopicList(activity, id, handler);
				  }
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
		case 4:
			url=Constants.ACTIVITY_POPULAR+param;
			break;
		case 5:
			url=Constants.ACTIVITY_NEWEST+param;
			break;
		}
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
			String result =connect(client, handler, get);

			
			// TODO
			LogUtil.i("me", "下载图片列表url==" + url);
			LogUtil.i("me", "下载图片返回结果" + result);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				int status=handleResult(activity,result,handler);
				  if(status==0){
					  UserImagesJson json=new UserImagesJson(result);
					  Message msg=handler.obtainMessage();
						msg.obj=json.petPictures;
						msg.what=Constants.MESSAGE_DOWNLOAD_IMAGES_LIST;
						handler.sendMessage(msg);
				  }else if(status==1){
				  }else if(status==2){
					  downloadActivityImagesList(handler,last_id,topic_id,activity,d,mode);
				  }
				
			}
	}
    public static boolean loadActivityInfo(ActivityJson.Data data,Handler handler,Context context){
    	String value="topic_id="+data.topic_id+"dog&cat";
    	String url=Constants.ACTIVITY_INFO+"&topic_id="+data.topic_id+"&sig="+getMD5(value)+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpGet get=new HttpGet(url);
    	try {
    		String result=connect(client, handler, get);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "活动信息 url="+url);
				
				LogUtil.i("me", "活动信息返回结果="+result);
                	int status=handleResult(context,result,handler);
                	
  				  if(status==0){
					JSONObject o1=new JSONObject(result);
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
  				  }else if(status==1){
				  }else if(status==2){
						return loadActivityInfo(data,handler,context);
					
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
    	return false;
    }
    /**
     * 获取 奖品详细信息
     * @param data
     * @return
     */
    public static boolean loadRewardInfo(ActivityJson.Data data,Handler handler,Context context){
    	String value="topic_id="+data.topic_id+"dog&cat";
    	String url=Constants.ACTIVITY_REWARD_INFO+"&topic_id="+data.topic_id+"&sig="+getMD5(value)+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpGet get=new HttpGet(url);
    	try {
    		String result=connect(client, handler, get);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "活动信息 url="+url);
				
				LogUtil.i("me", "活动信息返回结果="+result);
				int status=handleResult(context,result,handler);
				
				  if(status==0){
					  JSONObject o1=new JSONObject(result);
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
				  }else if(status==1){
				  }else if(status==2){
					  return loadRewardInfo(data,handler,context);
					
				  }
				  }
						
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
						 
						
					
			
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
    	return false;
    }
    /**
     * 获取消息和活动数目
     * @return
     */
    public static LoginJson.Data getMailAndActivityNum(Handler handler,Context context){
    	boolean flag=false;
    	String param="dog&cat";
    	String value=getMD5(param);
    	String url=Constants.MAIL_ACTIVITY_NEW_NUM+"&sig="+value+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpGet get=new HttpGet(url);
    	try {
    		String result=connect(client, handler, get);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "获取消息和活动数目url="+url);
				LogUtil.i("me", "获取消息和数目返回结果："+result);
				/*
				 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
				 * "data":"topic_count":"3"},
				 * "currentTime":1404794631}
				 */
				int status=handleResult(context,result,handler);
				
				  if(status==0){
					  JSONObject o1=new JSONObject(result);
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
				  }else if(status==1){
				  }else if(status==2){
					  return getMailAndActivityNum(handler,context);
				  }
				}
			
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
    	return null;
    }
    /**
     * 发表评论
     * @param comment
     * @param id
     * @return
     */
    public static User sendComment(Context context,String comment,int id,int reply_id,String reply_name,Handler handler){
    	String params="dog&cat";
    	String url=Constants.ADD_A_COMMENT+"&sig="+getMD5(params)+"&SID="+Constants.SID;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpPost post=new HttpPost(url);
    	ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
    	NameValuePair pair=new BasicNameValuePair("img_id", ""+id);
    	pairs.add(pair);
		pair=new BasicNameValuePair("body",comment);//URLEncoder.encode(comment, "UTF-8")
		pairs.add(pair);
	    if(reply_id!=-1){
	    	pair=new BasicNameValuePair("reply_id",""+reply_id);
	    	pairs.add(pair);
	    	pair=new BasicNameValuePair("reply_name",Constants.user.u_nick+"@"+reply_name);
	    	pairs.add(pair);
	    }
    	
    	User user=null;
    	try {
			post.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
			HttpResponse response=client.execute(post);
			int resultCode=response.getStatusLine().getStatusCode();
			if (resultCode == HttpStatus.SC_OK) {
				String result=EntityUtils.toString(response.getEntity());
				  int status=handleResult(context,result,handler);
				  if(status==0){
//					  RegisterJson loginJson = parseRegisterJson(result,handler);
					  LogUtil.i("me", "发表评论url="+url);
						LogUtil.i("me", "发表评论返回结果="+result);
						JSONObject jo=new JSONObject(result);
						String dataString=jo.getString("data");
						if(!StringUtil.isEmpty(dataString)&&!"null".equals(dataString)){
							JSONObject j1=jo.getJSONObject("data");
							user=new User();
							user.exp=j1.getInt("exp");
							user.coinCount=j1.getInt("gold");
							user.lv=j1.getInt("lv");
							if(result.contains("rank"))
							user.rankCode=j1.getInt("rank");
							sendLevelChangeReceiver(context,user.lv,user.exp,user.coinCount,0,0,false,0);
							return user;
						}
				  }else if(status==1){
					  return null;
				  }else if(status==2){
					  return sendComment(context,comment, id, reply_id, reply_name, handler);
				  }
			}else{
				judgeHttpStatus(resultCode,handler);
				return null;
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
    	return user;
    }
    /**
     * 消息列表
     * @param mail_id
     * @param is_system
     * @return
     */
    public static MessagJson getMailList(int mail_id,boolean is_system,Handler handler,Context context){
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
			String result=connect(client, handler, get);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				int status=handleResult(context,result,handler);
				
				  if(status==0){
					  MessagJson json=new MessagJson(result);
					  return json;
				  }else if(status==1){
				  }else if(status==2){
					  return getMailList(mail_id, is_system,handler,context);
				  }
				LogUtil.i("me", "消息列表url="+url);
				LogUtil.i("me", "消息列表返回结果:"+result);
				
				
			}
    	return null;
    }
    /**
     * 删除消息
     * @param dataSystem
     * @return
     */
    public static boolean deleteMail(TalkMessage dataSystem){
    	boolean flag=false;
    	if(dataSystem==null)return false;
    	String param="talk_id="+dataSystem.position;
    	String url=Constants.MAIL_DELETE+"&talk_id="+dataSystem.position+"&sig="+getMD5Value(param)+"&SID="+Constants.SID;
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
    /**
     * 发送消息
     * @param to_id
     * @param body
     * @return
     */
    public static boolean  sendMail(int to_id,String body){
    	String params="usr_id="+to_id;
    	String url=Constants.MAIL_CREATE+"&sig="+getMD5Value(params)+"&SID="+Constants.SID+"&usr_id="+to_id;
    	DefaultHttpClient client=new DefaultHttpClient();
    	HttpPost post=new HttpPost(url);
    	ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
    	NameValuePair pair=new BasicNameValuePair("usr_id", ""+to_id);
    	int talk_id=-1;
    	boolean flag=false;
    		pair=new BasicNameValuePair("msg",body);
			pairs.add(pair);
    	
    	try {
			StringEntity req=new StringEntity("msg="+body, "UTF-8");
			req.setContentType("application/x-www-form-urlencoded");
			post.setEntity(req);
			HttpResponse response=client.execute(post);
			int resultCode=response.getStatusLine().getStatusCode();
			if(resultCode==200){
				String result=EntityUtils.toString(response.getEntity());
				LogUtil.i("me", "发送消息url="+url);
				LogUtil.i("me", "发送消息返回结果="+result);
				JSONObject o1=new JSONObject(result);
				String dataStr=o1.getString("data");
				if(dataStr!=null&&!"null".equals(dataStr)&&!"".equals(dataStr)){
					JSONObject o2=new JSONObject(dataStr);
					boolean isSuccess=o2.getBoolean("isSuccess");
					if(isSuccess){
						flag=true;
						return true;
					}
					
				}
				
				
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
    	return flag;
    }
    public static int getTalk_id(Context context,int usr_id,Handler handler) {
		String url = "http://" + Constants.IP + Constants.TALK_ID;
		DefaultHttpClient client = new DefaultHttpClient();
		int talk_id=-1;
		String value = "usr_id="+usr_id;
		String SIG = getMD5Value(value);
		String param = SIG +"&usr_id="+usr_id+ "&SID=" + Constants.SID;
		
		
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0","data":[{"usr_id":"185","name":"\u8def","tx":"","gender":"1","city":"1000","age":"0","exp":"1","lv":"0","aid":"1000000180","a_name":"\u4ed6","a_tx":""}],"currentTime":1409652826}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataString=j1.getString("data");
					  if(!StringUtil.isEmpty(dataString)&&!"null".equals(dataString)){
						  JSONObject j2=j1.getJSONObject("data");
						  talk_id=j2.getInt("talk_id");
						  return talk_id;
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return getTalk_id(context,usr_id, handler);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return talk_id;
	}
    /**
     * 获取聊天列表内容
     * @param talk_id
     * @param handler
     */
    public static ArrayList<TalkMessage> getTalkList(Context context,int talk_id,Handler handler) {
		String url = "http://" + Constants.IP + Constants.TALK_LIST;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "";
		String SIG = getMD5Value(value);
		String param = SIG + "&SID=" + Constants.SID;
		if(talk_id!=-1){
			value="talk_id="+talk_id;
			SIG=getMD5Value(value);
			param= SIG + "&SID=" + Constants.SID+"&talk_id="+talk_id;
		}
		 LogUtil.i("mi", "===获取消息");
		url = url + param;
		HttpGet get = new HttpGet(url);
		ArrayList<TalkMessage> talkMessages=null;
		try {
			String result =connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             *[
  {"8":
     {"msg":         {"1410424994":"hi","1410424988":"you"},
     "new_msg":3,"usr_id":"257","usr_tx":"","usr_name":"my"
     }
  },
 {"7":
    {"msg":{"1410425069":"happy","1410425061":"moon"},
     "new_msg":8,"usr_id":"256","usr_tx":"","usr_name":"\u963f"
     }
  }
]
             *,"currentTime":1409652826}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(StringUtil.isEmpty(dataStr)||"null".equals(dataStr))return null;
					  JSONArray ja=j1.getJSONArray("data");
					  if(ja!=null&&ja.length()>0){
						  talkMessages=new ArrayList<TalkMessage>();
						  ArrayList<Msg> tempList=null;
						  TalkMessage talkMessage=null;
						  Msg msg=null;
						  String temp="";
						  String temp1=null;
						  JSONObject jo=null;
						  JSONObject jo2=null;
						  JSONObject jo3=null;
						  String[] strings=null;
						  for(int i=0;i<ja.length();i++){
							  jo=ja.getJSONObject(i);
							  temp=jo.toString();
							  LogUtil.i("scroll", "jo.toString()="+temp);
							  if(!StringUtil.isEmpty(temp)){
								  talkMessage=new TalkMessage();
								  temp=getJsonKey(temp,0);
								  jo2=jo.getJSONObject(temp);
								  talkMessage.position=Integer.parseInt(temp);
								  talkMessage.new_msg=jo2.getInt("new_msg");
								  talkMessage.usr_id=jo2.getInt("usr_id");
								  talkMessage.usr_tx=jo2.getString("usr_tx");
								  talkMessage.usr_name=jo2.getString("usr_name");
								  if(talkMessage.usr_id==1){
									  talkMessage.usr_name="事务官";
								  }else if(talkMessage.usr_id==2){
									  talkMessage.usr_name="联络官";
								  }else if(talkMessage.usr_id==3){
									  talkMessage.usr_name="顺风小鸽";
								  }
								  tempList=new ArrayList<TalkMessage.Msg>();
								  jo3=jo2.getJSONObject("msg");
								  temp=jo3.toString();
								  LogUtil.i("mi", "msg===="+temp);
								  if(!StringUtil.isEmpty(temp)){
									  strings=temp.split("\",");
									  for(int j=strings.length-1;j>=0;j--){
										  temp1=strings[j];
										  msg=new Msg();
										  msg.time=Long.parseLong(getJsonKey(temp1, 0));
										  int start=temp1.indexOf(":");
										  start=temp1.indexOf("\"",start)+1;
										  msg.content=temp1.substring(start,temp1.length());
										  if(j==strings.length-1)
											  msg.content=temp1.substring(start,temp1.length()-2);
										  msg.from=talkMessage.usr_id;
										  if(talkMessage.usr_id==2){
											  String[] strs=msg.content.split("]");
											  if(strs!=null&&strs.length>0){
												  msg.img_id=Long.parseLong(strs[0].substring(1));
												  msg.content=strs[1];
											  }
										  }
										  
										  tempList.add(msg);
									  }
									  talkMessage.msgList=tempList;
								  }
								  talkMessages.add(talkMessage);
							  }
						  }
						  return talkMessages;
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return getTalkList(context,talk_id, handler);
					  
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return talkMessages;
	}
    
    public static String getJsonKey(String temp,int start){
    	if(start==0){
       	 start=temp.indexOf("\"")+1;
    	}
		  temp=temp.substring(start, temp.indexOf("\"",start));
		  return temp;
    }
    public static User  sendGift(Context context,Gift gift,Handler handler) {
  		String url = "http://" + Constants.IP + Constants.SEND_GIFT_API;
  		DefaultHttpClient client = new DefaultHttpClient();
  		String value = "aid="+gift.aid;
  		String SIG = "";
  		String param ="&aid="+gift.aid+ "&SID=" + Constants.SID;
  		if(gift.img_id!=-1){
  			value+="&img_id="+gift.img_id;
  	  		param+="&img_id="+gift.img_id;
  		}
  		if(gift.is_shake){
  			value +="&is_shake="+gift.is_shake;
  	  		param+="&is_shake="+gift.is_shake;
  		}
  		value +="&item_id="+gift.no/*1102*/;
  		SIG = getMD5Value(value);
  		param+="&item_id="+gift.no/*1102*/;
  		url = url +SIG+ param;
  		HttpGet get = new HttpGet(url);
  		User user=null;
  		try {
  			String result =connect(client, handler, get);
              /*
               * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
               * "data":{"exp":872,"gold":"338","lv":"0","rank":"1"},"currentTime":1409652826}
               */
  			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
  				LogUtil.i("me", "url" + url);
  				LogUtil.i("me", "info返回结果" + result);
  				int status=handleResult(context,result,handler);
  				  if(status==0){
  					  JSONObject jo=new JSONObject(result);
  					  JSONObject j1=jo.getJSONObject("data");
  					  user=new User();
  					  user.exp=j1.getInt("exp");
  					  user.lv=j1.getInt("lv");
  					  user.coinCount=j1.getInt("gold");
  					  user.rankCode=j1.getInt("rank");
//  					  sendLevelChangeReceiver(context, user.lv, user.exp, user.coinCount, user.rankCode, 0, false,0);
  					  return user;
  				  }else if(status==1){
  				  }else if(status==2){
  					  
  					  return sendGift(context,gift, handler);
  				  }
  				
  				
  			}
  		}catch (JSONException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
  		}
  		return user;
  	}
    public static User buyGift(Context context,Gift gift,Handler handler) {
  		String url = "http://" + Constants.IP + Constants.BUY_GIFT_API;
  		DefaultHttpClient client = new DefaultHttpClient();
  		String value = "item_id="+gift.no/*1102*/+"&num="+gift.buyingNum;
  		String SIG = "";
  		String param ="&SID=" + Constants.SID+"&item_id="+gift.no/*1102*/+"&num="+gift.buyingNum;;
  		SIG = getMD5Value(value);
  		url = url +SIG+ param;
  		HttpGet get = new HttpGet(url);
  		User user=null;
  		try {
  			LogUtil.i("me", "url" + url);
  			
  			String result = connect(client, handler, get);
              /*
               * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
               * "data":{"user_gold":367},"currentTime":1409652826}
               */
  			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
  				
  				LogUtil.i("me", "info返回结果" + result);
  				int status=handleResult(context,result,handler);
  				  if(status==0){
  					  JSONObject jo=new JSONObject(result);
  					  JSONObject j1=jo.getJSONObject("data");
  					  user=new User();
  					  /*user.exp=j1.getInt("exp");
  					  user.lv=j1.getInt("lv");*/
  					  user.coinCount=j1.getInt("user_gold");
//  					  user.rankCode=j1.getInt("rank");
  					  return user;
  				  }else if(status==1){
  				  }else if(status==2){
  					  
  					  return buyGift(context,gift, handler);
  				  }
  				
  				
  			}
  		}catch (JSONException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
  		}
  		return user;
  	}
    /**
     * 摇一摇
     * @param aid
     * @param handler
     * @return
     */
    public static int  shakeApi(Context context,long aid,Handler handler,int is_shake) {
		String url = "http://" + Constants.IP + Constants.SHAKE_API;
		DefaultHttpClient client = new DefaultHttpClient();
		int count=-1;
		String value = "aid="+aid;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+aid+ "&SID=" + Constants.SID;
		if(is_shake==1){
			value = "aid="+aid+"&is_shake="+is_shake;
			SIG = getMD5Value(value);
			param = SIG +"&aid="+aid+ "&SID=" + Constants.SID+"&is_shake="+is_shake;
		}
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0","data":{"shake_count":3},"currentTime":1409652826}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject jo=new JSONObject(result);
					  JSONObject j1=jo.getJSONObject("data");
					  count=j1.getInt("shake_count");
					  if(count<0)count=0;
					  return count;
				  }else if(status==1){
				  }else if(status==2){
					  
					  return shakeApi(context,aid, handler,is_shake);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return count;
	}
    public static int  shakeShareNum(Context context,long aid,Handler handler) {
		String url = "http://" + Constants.IP + Constants.SHAKE_SHARE;
		DefaultHttpClient client = new DefaultHttpClient();
		int count=-1;
		String value = "aid="+aid;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+aid+ "&SID=" + Constants.SID;
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0","data":{"shake_count":3},"currentTime":1409652826}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject jo=new JSONObject(result);
					  JSONObject j1=jo.getJSONObject("data");
					  count=j1.getInt("shake_count");
					  if(count<0)count=0;
					  return count;
				  }else if(status==1){
				  }else if(status==2){
					  
					  return shakeShareNum(context,aid, handler);
				  }
				
				
			}
		} catch (/*JSON*/Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return count;
	}
    /**
     * 摸一摸
     * @param aid
     * @param handler
     * @return
     */
    public static boolean touchApi(Context context,long aid,Handler handler) {
		String url = "http://" + Constants.IP + Constants.TOUCH_API;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "aid="+aid;
		String SIG = getMD5Value(value);
		String param = SIG +"&aid="+aid+ "&SID=" + Constants.SID;
		
		url = url + param;
		HttpGet get = new HttpGet(url);
		User user=null;
		try {
			String result =connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":{"gold":"69","exp":165},"currentTime":1409652826}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject jo=new JSONObject(result);
					  String dataStr=jo.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)&&!"false".equals(dataStr)){
						  JSONObject data=jo.getJSONObject("data");
						  int gold=0,exp=0,lv=0;
						  if(dataStr.contains("\"gold\"")){
							  gold=data.getInt("gold");
						  }
						  if(dataStr.contains("\"exp\"")){
							  exp=data.getInt("exp");
						  }if(dataStr.contains("\"lv\"")){
							  lv=data.getInt("lv");
						  }
						  sendLevelChangeReceiver(context, lv, exp, gold, 0, 0, false,0);
					  }
					  return true;
				  }else if(status==1){
				  }else if(status==2){
					  
					  return touchApi(context,aid, handler);
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return false;
	}
    /**
     * 根据宠物名称搜索宠物
     * @param name
     * @param aid
     * @param handler
     */
    public static ArrayList<Animal> searchUserOrPet(Context context,String name,long aid,Handler handler) {
		String url = "http://" + Constants.IP + Constants.SEARCH_PET;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "";
		name=new String(name.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String SIG = getMD5Value(value);
		String param = SIG + "&SID=" + Constants.SID+"&name="+name;
		if(aid!=-1){
			value="aid="+aid;
			SIG=getMD5Value(value);
			param= SIG + "&SID=" + Constants.SID+"&aid="+aid+"&name="+name;
		}
		url = url + param;
		HttpGet get = new HttpGet(url);
		ArrayList<Animal> animalList=null;
		try {
			String result =connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
             * "confVersion":"1.0",
             * "data":[[{"aid":"300","name":"\u5c0f\u6cf0\u6cf0",
             * "tx":"300_1414138207headImage.png","gender":"1","from":"2",
             * "type":"201","age":"27","t_rq":"1946","fans":"3"}]],
             * "currentTime":1415088926}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  String dataStr=j1.getString("data");
					  if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
					  JSONArray j2=j1.getJSONArray("data");
					  JSONArray ja=null;
					  JSONObject j4=null;
					  if(j2!=null&&j2.length()>0){
						 ja=j2.getJSONArray(0);
							  if(ja!=null&&ja.length()>0){
								  animalList=new ArrayList<Animal>();
								  Animal animal=null;
								  Animal temp=null;
								  Animal temp2=null;
								  Animal temp3=null;
								  for(int i=0;i<ja.length();i++){
									  j4=ja.getJSONObject(i);
									  animal=new Animal();
									  animal.a_id=j4.getLong("aid");
									  animal.pet_nickName=j4.getString("name");
									  animal.pet_iconUrl=j4.getString("tx");
									  animal.a_gender=j4.getInt("gender");
									  String aidStr=""+animal.a_id;
									  animal.from=Integer.parseInt(aidStr.substring(0, 1));
									  animal.type=j4.getInt("type");
									  animal.a_age=j4.getInt("age");
									  animal.a_age_str=getAge(animal.a_age);
									  animal.t_rq=j4.getInt("t_rq");
									  animal.fans=j4.getInt("fans");
									  animal.race=StringUtil.getRaceStr(animal.type);
									  if(animal.u_rankCode==0){
											  animal.u_rank="经纪人";
										 
									  }else{
										  String[] jobs=null;
										  jobs=StringUtil.getUserJobs();
										  if(jobs!=null){
											  animal.u_rank=jobs[animal.u_rankCode-1];
										  }
									  }
									  animalList.add(animal);
									  
								  }
								  return animalList;
							  }
						  }
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  return searchUserOrPet(context,name, aid, handler);
					 
				  }
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return animalList;
	}
    public static ArrayList<User> searchUser(Context context,String name,int page,Handler handler) {
  		String url = "http://" + Constants.IP + Constants.SEARCH_USER;
  		DefaultHttpClient client = new DefaultHttpClient();
  		boolean flag=false;
  		String value = "";
  		name=new String(name.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
  		try {
  			name = URLEncoder.encode(name, "UTF-8");
  		} catch (UnsupportedEncodingException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
  		String SIG = "";
  		String param = "";
  			value="page="+page;
  			SIG=getMD5Value(value);
  			param= SIG + "&SID=" + Constants.SID+"&page="+page+"&name="+name;
  		url = url + param;
  		HttpGet get = new HttpGet(url);
  		ArrayList<User> users=null;
  		try {
  			String result =connect(client, handler, get);
              /*
               {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
               "data":[[{"usr_id":"317","name":"\u697c\u697c","tx":"317_1415103222519_usr_icon.jpg",
               "gender":"1","city":"1610"}]],"currentTime":1415863596}
               */
  			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
  				LogUtil.i("me", "url" + url);
  				LogUtil.i("me", "info返回结果" + result);
  				int status=handleResult(context,result,handler);
  				  if(status==0){
  					  JSONObject j1=new JSONObject(result);
  					  String dataStr=j1.getString("data");
  					  if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
  					  JSONArray j2=j1.getJSONArray("data");
  					  JSONArray ja=null;
  					  JSONObject j4=null;
  					  User user=null;
  					  if(j2!=null&&j2.length()>0){
  						 ja=j2.getJSONArray(0);
  							  if(ja!=null&&ja.length()>0){
  								  users=new ArrayList<User>();
  								  for(int i=0;i<ja.length();i++){
  									j4=ja.getJSONObject(i);
  	  								user=new User();
  	  								user.userId=j4.getInt("usr_id");
  	  								user.u_nick=j4.getString("name");
  	  								user.u_iconUrl=j4.getString("tx");
  	  								user.u_gender=j4.getInt("gender");
  	  								user.locationCode=j4.getInt("city");
  	  							 String temp=""+user.locationCode;
  	  						  if(temp.length()==4){
  	  							  int p=Integer.parseInt(temp.substring(0, 2));
  	  							  user.province=AddressData.PROVINCES[p-10];
  	  							  int p2=Integer.parseInt(temp.substring(2, 4));
  	  							  user.city=AddressData.CITIES[p-10][p2];
  	  						  }else{
  	  							  user.province=AddressData.PROVINCES[0];
  	  							  user.city=AddressData.CITIES[0][0];
  	  						  }
  	  						  users.add(user);
  								  }
  								 return users;
  								
  							  }
  							 
  						  }
  					  }
  				  }else if(status==1){
  				  }else if(status==2){
  					  
  					  return searchUser(context, name, page, handler);
  					 
  				  }
  				
  				
  			}
  		} catch (JSONException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  			if(handler!=null)
  				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
  		}
  		return users;
  	}
    public static void deleteTalk(Context context,int talk_id,Handler handler) {
		String url = "http://" + Constants.IP + Constants.TALK_DELETE;
		DefaultHttpClient client = new DefaultHttpClient();
		boolean flag=false;
		String value = "";
		String SIG = getMD5Value(value);
		String param = SIG + "&SID=" + Constants.SID;
		if(talk_id!=-1){
			value="talk_id="+talk_id;
			SIG=getMD5Value(value);
			param= SIG + "&SID=" + Constants.SID+"&talk_id="+talk_id;
		}
		url = url + param;
		HttpGet get = new HttpGet(url);
		KingdomCard card=null;
		try {
			String result = connect(client, handler, get);
            /*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             *{"master":{"usr_id":"221","name":"\u5341\u516b","tx":"221_userHeadImage.png",
             *"gender":"1","city":"1000"},
             *"images":[{"img_id":"2494","url":"1000000210_0.1409748512.png"}]}
             *,"currentTime":1409652826}
             */
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				LogUtil.i("me", "url" + url);
				LogUtil.i("me", "info返回结果" + result);
				int status=handleResult(context,result,handler);
				  if(status==0){
					  JSONObject j1=new JSONObject(result);
					  JSONObject j2=j1.getJSONObject("data");
					  if(j2!=null){
						  
					  }
				  }else if(status==1){
				  }else if(status==2){
					  
					  getTalkList(context,talk_id, handler);
					  return;
				  }
				
				
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
//		return card;
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
	public static String getMD5Value(String str) {
		try {
			str=str+"dog&cat";
			LogUtil.i("me", "加密字符串："+str);
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
	 * 下载欢迎页图片
	 * @param handler
	 * @return
	 */
	public static String  downloadWelcomeImage(Handler handler,Activity activity){
		
		String value = "dog&cat";
		String SIG = getMD5(value);
		boolean flag=false;
		String url=/*Constants.WELCOME_IMAGE+"&sig="+SIG;*/
		"http://"+Constants.IP+Constants.URL_ROOT+"r=user/welcomeApi"+"&sig="+SIG;;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			String result=connect(client, handler, get);
			if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
				
				LogUtil.i("me", "欢迎图片url="+url);
				LogUtil.i("me", "欢迎图片返回结果="+result);
				/**
				 * {"url":"home.jpg","animal":"507","food":"507"}
				 */
				String urlString=new JSONObject(result).getJSONObject("data").getString("url");
				Constants.Toatl_animal=new JSONObject(result).getJSONObject("data").getLong("animal");
				Constants.Toatl_food=new JSONObject(result).getJSONObject("data").getLong("food");
				SharedPreferences sp=activity.getSharedPreferences("setup",Context.MODE_WORLD_READABLE );
				Editor editor=sp.edit();
				editor.putString("welcome", urlString);
				editor.commit();
				return urlString;
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
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
			 * "data":{"isSuccess":true,"usr_id":"254","SID":""},
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
			Constants.realVersion=data.getString("version");
		    String userIdStr=data.getString("usr_id");
		 /*   if(!StringUtil.isEmpty(userIdStr)&&!"null".equals(userIdStr)&&!"false".equals(userIdStr)){
		    	Constants.user=new User();
		    	Constants.user.userId=data.getInt("usr_id");
		    }*/
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
	public static RegisterJson parseRegisterJson(String json,Handler handler) {
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
			String dataStr=jsonObject.getString("data");
			if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)&&!"false".equals(dataStr)){
				JSONObject  jb=jsonObject.getJSONObject("data");
				if(jb!=null){
					JSONObject data = jb;
					loginJson.data = new RegisterJson.Data();
					loginJson.data.isSuccess = data.getBoolean("isSuccess");
				}else{
					loginJson.data=null;
				}
			}
			
			
			return loginJson;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
		return loginJson;
	}

	public static User parseInfoJson(String json,Activity activity) {
		JSONObject jsonObject=null;
		User user=null;
		try {
			/*
             * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
             * "data":
             * [{"usr_id":"254","name":"\u6c64\u91cc\u53ef\u5f97",
             * "tx":"254_1411010757445.jpg","gender":"1","city":"1900",
             * "age":"0","exp":"1104","lv":"9","gold":"429","con_login":"12",
             * "rank":"2","aid":"2000000241","a_name":"Joke","a_age":"2","inviter":"0",
             * "a_tx":"2000000241_1411010687969.jpg","next_gold":30，"weibo":"3835971321","wechat":""}]
             * "currentTime":1409652826}
             */
			jsonObject = new JSONObject(json);
			String dataStr=jsonObject.getString("data");
			if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
			JSONArray data = jsonObject.getJSONArray("data");
			if(data!=null){
				
				if(data.length()>0){
					JSONObject obj=data.getJSONObject(0);
					user=new User();
					user.userId=obj.getInt("usr_id");
					user.u_nick=obj.getString("name");
					user.u_iconUrl=obj.getString("tx");
					user.u_gender=obj.getInt("gender");
					user.inviter=obj.getInt("inviter");
					user.password=obj.getString("password");
					user.weixin_id=obj.getString("wechat");
					user.xinlang_id=obj.getString("weibo");
					if(dataStr.contains("code")){
						user.code=obj.getString("code");
					}
					if(dataStr.contains("food")){
						user.food=obj.getInt("food");
					}
					String temp=obj.getString("rank");
					if("null".equals(temp)){
						user.rankCode=0;
					}else{
						user.rankCode=obj.getInt("rank");
					}
					
					if(user.rankCode==0){
						 user.rank="经纪人";
							  
						  }else{
							  String[] jobs=null;
							  jobs=StringUtil.getUserJobs();
							  if(user.rankCode>8)user.rankCode=1;
							  if(jobs!=null){
								  user.rank=jobs[user.rankCode-1];
							  }
						  }
					user.u_age=obj.getInt("age");
					user.locationCode=obj.getInt("city");
					user.coinCount=obj.getInt("gold");
					user.exp=obj.getInt("exp");
					user.lv=obj.getInt("lv");
					user.con_login=obj.getInt("con_login");
					user.next_gold=obj.getInt("next_gold");
					user.currentAnimal=new Animal();
					user.currentAnimal.a_id=obj.getLong("aid");
					 String aidStr=""+user.currentAnimal.a_id;
					 user.currentAnimal.from=Integer.parseInt(aidStr.substring(0, 1));
					user.currentAnimal.pet_iconUrl=obj.getString("a_tx");
					user.currentAnimal.pet_nickName=obj.getString("a_name");
					
					return user;
				}
			}
			}
			
			/*jsonObject = new JSONObject(json);
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
				try {
					d.name=URLDecoder.decode(object.getString("name"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				
				user.a_age=d.age;
				user.classs=d.type;
				user.a_gender=d.gender;
				user.pet_iconPath=Constants.Picture_ICON_Path+File.separator+d.tx;
				user.pet_nickName=d.name;
				user.race=""+d.type;
				user.pet_iconUrl=d.tx;
				user.userId=d.user_id;
				user.exp=d.exp;
//				user.lv=d.lv;
				user.exp=380;
				user.lv=2;
				user.follow=d.follow;
				user.follower=d.follower;
				user.con_login=d.con_login;
				user.imagesCount=d.imagesCount;
				Constants.user=user;
				infoJson.data.add(d);
			}*/
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	public static PetPicture parseUpdateImageJson(String json){
		JSONObject jsonObject;
		PetPicture petPicture=null;
			/*
			 *{"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
			 *"data":{"image":{"topic_id":"0","topic_name":"","cmt":"",
			 *"url":"2000000241_6.1410752765013.jpg","likes":"0",
			 *"gifts":"0","shares":"0","is_deleted":0,"aid":"2000000241",
			 *"relates":"","create_time":1410752779,
			 *"img_id":"2555","likers":null,"senders":null,
			 *"comments":null,"update_time":null},"exp":"412"},
			 *"currentTime":1409739151}
			 */
		
			try {
				petPicture=new PetPicture();
				jsonObject = new JSONObject(json);
				UserImagesJson userImagesJson=null;
				userImagesJson=new UserImagesJson();
				userImagesJson.state = jsonObject.getInt("state");
				userImagesJson.errorCode = jsonObject.getInt("errorCode");
				userImagesJson.errorMessage = jsonObject.getString("errorMessage");
				userImagesJson.version = jsonObject.getString("version");
				userImagesJson.confVersion = jsonObject.getString("confVersion");
				userImagesJson.currentTime = jsonObject.getLong("currentTime");
				petPicture.errorCode=userImagesJson.errorCode;
			String temp=jsonObject.getString("data");
			if(temp==null||"null".equals(temp)){
				return null;
			}
			JSONObject data = jsonObject.getJSONObject("data");
			
			JSONObject object=null;
//			for(int i=0;i<data.length();i++){
				object=data.getJSONObject("image");
				
				petPicture.url=object.getString("url");
//				if(json.contains("likes")){
				petPicture.likes=object.getInt("likes");
//				}else{
//					imageJson.data.likes=0;
//				}
				
				petPicture.img_id=object.getInt("img_id");
				petPicture.animal=new Animal();
				petPicture.animal.a_id=object.getLong("aid");
				
				petPicture.cmt=object.getString("cmt");
				petPicture.create_time=object.getLong("create_time");
				
				petPicture.exp=data.getInt("exp");
				
//				petPicture.user=Constants.user;
//				userImagesJson.datas=new ArrayList<UserImagesJson.Data>();
//				userImagesJson.datas.add(data1);
				return petPicture;
//			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return petPicture;
	}
	public static boolean judgeSID(int errorCode){
		if(errorCode==2){
//			login(null, null);
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
	
	
	
	
    /**
     * 处理网络返回结果
     * 网络状态判断   是否断开
     * 
     * 自身定义错误   0  正常；1，-1  异常；2 SID过期
     * 
     * @param result
     * @return
     */
	public static int handleResult(Context context,String result,Handler handler){
    	int status=0;
    	JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(result);
		
    	int errorCode=jsonObject.getInt("errorCode");
    	int state=jsonObject.getInt("state");
    	String version=jsonObject.getString("version");
		if(state==2){
			login(context,null);
			return 2;
		}
		if(state==1){
			Message msg=handler.obtainMessage();
			msg.obj=jsonObject.getString("errorMessage");
			msg.what=HandleHttpConnectionException.Json_Data_Server_Exception;
			handler.sendMessage(msg);
			return 1;
		}
		if (errorCode==0) {
			Constants.VERSION=version;
			Constants.CON_VERSION=jsonObject.getString("confVersion");
			return 0;
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
		}
    	return status;
    }
	/**
	 * http 相关错误  404 500 等的处理
	 * @param status
	 * @param handler
	 */
	public static void judgeHttpStatus(int status,Handler handler){
		if(status>100&&status<200){
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Connect_Error_1XX);
		}else if(status>300&&status<400){
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Connect_Error_3XX);
		}else if(status>400&&status<500){
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Connect_Error_4XX);
		}else if(status>500){
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Connect_Error_5XX);
		}
	}
	public static String getAge(int age){
		if(age==0)age=1;
		int y=age/12;
		  int m=age%12;
		  String ageStr="";
		  if(y==0){
			  
		  }else{
			  ageStr+=""+y+"岁";
		  }
		  if(m==0){
			  
		  }else{
			  ageStr+=""+m+"个月";
		  }
		  return ageStr;
	}
	public static void sendLevelChangeReceiver(Context context,int lv,int exp,int gold,int rank,int con_login,boolean isLogin,int con_tri){
		
		Intent intent=new Intent(context,DialogNoteActivity.class);
	/*	if(exp>0){
			intent.putExtra("mode", 4);
	    	intent.putExtra("num", exp);
//	    	context.startActivity(intent);
	    	try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		if(gold>0){
			intent.putExtra("mode", 6);
	    	intent.putExtra("num", gold);
	    	context.startActivity(intent);
	    	try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(con_tri>0){
			intent.putExtra("mode", 5);
	    	intent.putExtra("num", con_tri);
	    	context.startActivity(intent);
		}
		/*if(lv!=0&&lv>0){
			intent.putExtra("mode", 2);
			intent.putExtra("num", gold);
			
			context.startActivity(intent);
			return ;
		}*/else if(isLogin){
			intent.putExtra("mode", 1);
			intent.putExtra("num", con_login);
			context.startActivity(intent);
			return;
	    }/*else if(rank!=0&&rank>0){
	    	intent.putExtra("mode", 3);
	    	intent.putExtra("num", gold);
	    	intent.putExtra("rank", rank);
	    	context.startActivity(intent);
	    	return;
	    }*/
	}
	public static void setConnectionTime(DefaultHttpClient client,int time){
		client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, time);
		client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, time);
	}
	/**
	 * 执行网络连接，处理联网相关异常
	 * @param client
	 * @param handler
	 * @param get
	 * @return
	 */
	public static String connect(DefaultHttpClient client,Handler  handler,HttpGet get){
		String result=null;
		try {
			HttpResponse response=null;
			setConnectionTime(client, 30000);
			response = client.execute(get);
			int resultCode = response.getStatusLine().getStatusCode();
			result = EntityUtils.toString(response.getEntity());
			if (resultCode == HttpStatus.SC_OK) {  
			}else{
				judgeHttpStatus(resultCode,handler);
			}
		}catch(ConnectTimeoutException e){
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.CONNECT_OUT_TIME);
		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.CONNECT_OUT_TIME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Network_Status_Error);
		}
		return result;
	}
	public static String connectPost(DefaultHttpClient client,Handler  handler,HttpPost post,ArrayList<NameValuePair> pairs ){
		String result=null;
		try {
			HttpResponse response=null;
			post.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
			setConnectionTime(client, 30000);
			response = client.execute(post);
			int resultCode = response.getStatusLine().getStatusCode();
			result = EntityUtils.toString(response.getEntity());
			if (resultCode == HttpStatus.SC_OK) {  
			}else{
				judgeHttpStatus(resultCode,handler);
			}
		}catch(ConnectTimeoutException e){
			if(handler!=null)
				handler.sendEmptyMessage(HandleHttpConnectionException.CONNECT_OUT_TIME);
		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Network_Status_Error);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(handler!=null)
			handler.sendEmptyMessage(HandleHttpConnectionException.Network_Status_Error);
		}
		return result;
	}
	public static String getJsonString(JSONObject jo,String key){
		String str=null;
		try {
			str=jo.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	public static long getJsonLong(JSONObject jo,String key){
		long  num=0;
		try {
			num=jo.getLong(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
	public static int getJsonInt(JSONObject jo,String key){
		int  num=0;
		try {
			num=jo.getInt(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
}
