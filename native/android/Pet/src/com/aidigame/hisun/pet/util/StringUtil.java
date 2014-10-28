package com.aidigame.hisun.pet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.TalkMessage;
import com.aidigame.hisun.pet.bean.TalkMessage.Msg;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

public class StringUtil {
	public static String timeFormat(long time){
		//1403514413
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return sdf.format(new Date(time*1000));
		
	}
	/** 
	* 将字符串转成unicode 
	* @param str 待转字符串 
	* @return unicode字符串 
	*/ 
	public static String convert(String str) 
	{ 
	str = (str == null ? "" : str); 
	String tmp; 
	StringBuffer sb = new StringBuffer(1000); 
	char c; 
	int i, j; 
	sb.setLength(0); 
	for (i = 0; i < str.length(); i++) 
	{ 
	c = str.charAt(i); 
	sb.append("\\u"); 
	j = (c >>>8); //取出高8位 
	tmp = Integer.toHexString(j); 
	if (tmp.length() == 1) 
	sb.append("0"); 
	sb.append(tmp); 
	j = (c & 0xFF); //取出低8位 
	tmp = Integer.toHexString(j); 
	if (tmp.length() == 1) 
	sb.append("0"); 
	sb.append(tmp); 

	} 
	return (new String(sb)); 
	} 
	/** 
	* 将unicode 字符串 
	* @param str 待转字符串 
	* @return 普通字符串 
	*/ 
	public static String revert(String str) 
	{ 
	str = (str == null ? "" : str); 
	if (str.indexOf("\\u") == -1)//如果不是unicode码则原样返回 
	return str; 

	StringBuffer sb = new StringBuffer(1000); 

	for (int i = 0; i < str.length() - 6;) 
	{ 
	String strTemp = str.substring(i, i + 6); 
	String value = strTemp.substring(2); 
	int c = 0; 
	for (int j = 0; j < value.length(); j++) 
	{ 
	char tempChar = value.charAt(j); 
	int t = 0; 
	switch (tempChar) 
	{ 
	case 'a': 
	t = 10; 
	break; 
	case 'b': 
	t = 11; 
	break; 
	case 'c': 
	t = 12; 
	break; 
	case 'd': 
	t = 13; 
	break; 
	case 'e': 
	t = 14; 
	break; 
	case 'f': 
	t = 15; 
	break; 
	default: 
	t = tempChar - 48; 
	break; 
	} 

	c += t * ((int) Math.pow(16, (value.length() - j - 1))); 
	} 
	sb.append((char) c); 
	i = i + 6; 
	} 
	return sb.toString(); 
	}
	public static void deleteFile(File file){
		if(file.isDirectory()){
			File[] files=file.listFiles();
			if(files.length==0)file.delete();
			for(File f:files){
				if(f.isDirectory()){
					deleteFile(f);
				}else{
					f.delete();
				}
			}
		}else{
			file.delete();
		}
	}
	/**
	 * 判断图片是否存在，两种情况  ：图片 不存在，图片存在但不完整（未下载完）;
	 * @param path
	 * @return
	 */
	public static boolean judgeImageExists(String path){
		boolean flag=false;
		if(path==null)return false;
		File file=new File(path);
		if(file.exists()){
			BitmapFactory.Options opts=new BitmapFactory.Options();
				opts.inJustDecodeBounds=false;
				opts.inSampleSize=32;
				Bitmap bitmap=BitmapFactory.decodeFile(path,opts);
				if(bitmap!=null){
					flag=true;
					if(!bitmap.isRecycled()){
						bitmap.recycle();
					}
				}else{
					flag=false;
					file.delete();
				}
				
		}
		LogUtil.i("me", "判断文件是否存在"+flag);
		return flag;
	}
	/**
	 * 通过流的方式读取图片，然后设置给ImagView
	 * @param path
	 * @param view
	 * @param opt
	 */
	public static void setImageViaStream(String path,ImageView view,BitmapFactory.Options opt){
		FileInputStream fis=null;
		try {
			fis=new FileInputStream(path);
			view.setImageBitmap(BitmapFactory.decodeStream(fis,null,opt));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static boolean isEmpty(String str){
		if(str!=null&&!"".equals(str)&&!" ".equals(str)){
			return false;
		}
		return true;
	}
	public static ArrayList<TalkMessage> getTalkHistory(Context context){
		ArrayList<TalkMessage> datasMail=new ArrayList<TalkMessage>();
		SharedPreferences sp=context.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		String numString=sp.getString("talk_num", null);
		if(!StringUtil.isEmpty(numString)){
			String[] strs=numString.split(",");
			if(strs.length>=0){
				String json=null;
				TalkMessage talkMessage=null;
				Msg msg=null;
				ArrayList<Msg> list;
				JSONArray ja=null;
				JSONObject jo=null;
				JSONObject j1=null;
				try {
					
				for(int i=0;i<strs.length;i++){
					json=sp.getString("talk_"+strs[i], null);
					if(!StringUtil.isEmpty(json)){
						j1=new JSONObject(json);
						ja=j1.getJSONArray("msg");
							talkMessage=new TalkMessage();
							talkMessage.position=Integer.parseInt(strs[i]);
							
							if(ja!=null&&ja.length()>0){
								list=new ArrayList<TalkMessage.Msg>();
								for(int j=0;j<ja.length();j++){
									jo=ja.getJSONObject(j);
									msg=new Msg();
									msg.content=jo.getString("content");
									msg.time=jo.getLong("time");
									msg.from=jo.getInt("from");
									msg.img_id=jo.getLong("img_id");
									list.add(msg);
								}
								talkMessage.msgList=list;
								talkMessage.usr_id=j1.getInt("usr_id");
								talkMessage.usr_name=j1.getString("usr_name");
								talkMessage.usr_tx=j1.getString("usr_tx");
								talkMessage.sortMsgList();
								if(json.contains("old_new_msg_num"))
								talkMessage.old_new_msg_num=j1.getInt("old_new_msg_num");
								if(!datasMail.contains(talkMessage)){
									datasMail.add(talkMessage);
								}
								
							}
						
					}
					
					
				}
				TalkMessage[] array=new TalkMessage[datasMail.size()];
				for(int i=0;i<array.length;i++){
					array[i]=datasMail.get(i);
				}
				for(int i=0;i<array.length-1;i++){
					for(int j=i;j<array.length-1;j++){
						if(array[j].msgList.size()>0&&array[j+1].msgList.size()>0){
							if(array[j].msgList.get(array[j].msgList.size()-1).time<array[j+1].msgList.get(array[j+1].msgList.size()-1).time){
								talkMessage=array[j];
								array[j]=array[j+1];
								array[j+1]=talkMessage;
							}
						}else if(array[j].msgList.size()>0){
							
						}else if(array[j+1].msgList.size()>0){
							talkMessage=array[j];
							array[j]=array[j+1];
							array[j+1]=talkMessage;
						}
					}
				}
				ArrayList<TalkMessage> msgs=new ArrayList<TalkMessage>();
				for(int i=0;i<array.length;i++){
					msgs.add(array[i]);
				}
				datasMail=msgs;
				if(datasMail!=null){
					int count=-1;
					for(int i=0;i<datasMail.size();i++){
						talkMessage=datasMail.get(i);
						if(talkMessage.old_new_msg_num>0){
							count++;
							datasMail.remove(i);
							datasMail.add(count, talkMessage);
						}
					}
				}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return datasMail;
	}
	/**
	 * 保存聊天历史记录
	 * @param datasMail
	 * @param context
	 */
	public static void saveTalkHistory(ArrayList<TalkMessage> datasMail,Context context){
		SharedPreferences sp=context.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
    	Editor editor=sp.edit();
    	if(datasMail.size()>0){
    		String numString=sp.getString("talk_num", null);
        	if(!StringUtil.isEmpty(numString)){
        		String[] strs=numString.split(",");
        		boolean has=false;
        		for(int j=0;j<datasMail.size();j++){
        			
        		has=false;
        		for(int i=0;i<strs.length;i++){
        			if((""+datasMail.get(j).usr_id).equals(strs[i])){
        				has=true;
        				continue;
        			}
        		}
        		if(!has){
        			numString+=""+datasMail.get(j).usr_id+",";
        		}
        		}
        	}else{
        		numString="";
        		for(int i=0;i<datasMail.size();i++){
        			numString+=""+datasMail.get(i).usr_id+",";
        		}
        		
        	}
        	editor.putString("talk_num", numString);
		try {
        	JSONObject jo=new JSONObject();
        	JSONObject j1=new JSONObject();
        	JSONArray ja=new JSONArray();
        	TalkMessage mailList=null;
        	String json1=null;
        	for(int j=0;j<datasMail.size();j++){
        		mailList=datasMail.get(j);
        		jo=new JSONObject();
            	j1=new JSONObject();
            	ja=new JSONArray();
        		for(int i=0;i<mailList.msgList.size();i++){
            		j1=new JSONObject();
        				j1.put("time", mailList.msgList.get(i).time);
        				j1.put("from", mailList.msgList.get(i).from);
        				j1.put("content", mailList.msgList.get(i).content);
        				j1.put("img_id", mailList.msgList.get(i).img_id);
        				ja.put(j1);
        			
            	}
            	jo.put("msg", ja);
            	jo.put("usr_id", mailList.usr_id);
            	jo.put("usr_name", mailList.usr_name);
            	jo.put("usr_tx", mailList.usr_tx);
            	jo.put("old_new_msg_num", mailList.old_new_msg_num);
            	editor.putString("talk_"+mailList.usr_id,jo.toString());
        	}
        	} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	editor.commit();
	}
	}
	/**
	 * 需要开启工作线程
	 * @return
	 */
	public static int getNewMessageNum(Context context,Handler handleHttpConnectionException){
		ArrayList<TalkMessage> datasMail=null;
		datasMail=StringUtil.getTalkHistory(PetApplication.petApp);
		if(datasMail==null){
			datasMail=new ArrayList<TalkMessage>();
		}
		ArrayList<TalkMessage> system=HttpUtil.getTalkList(context,-1, handleHttpConnectionException);
		int count=0;
		if(system!=null&&system.size()>0){
			TalkMessage talkMessage=null;
			TalkMessage tm=null;
			for(int i=0;i<system.size();i++){
				if(datasMail.contains(system.get(i))){
					talkMessage=system.get(i);
					tm=datasMail.get(datasMail.indexOf(talkMessage));
					tm.usr_name=system.get(i).usr_name;
					tm.usr_tx=system.get(i).usr_tx;
					tm.old_new_msg_num+=system.get(i).new_msg;
					tm.new_msg=system.get(i).new_msg;
					for(int j=0;j<talkMessage.msgList.size();j++){
						if(!tm.msgList.contains(talkMessage.msgList.get(j))){
							tm.msgList.add(talkMessage.msgList.get(j));
							count++;
						}
					}
				}else{
					system.get(i).old_new_msg_num=system.get(i).new_msg;
					datasMail.add(system.get(i));
				}
			}
			TalkMessage[] array=new TalkMessage[datasMail.size()];
			for(int i=0;i<array.length;i++){
				array[i]=datasMail.get(i);
			}
			
			for(int i=0;i<array.length-1;i++){
				for(int j=i;j<array.length-1;j++){
					if(array[j].msgList.size()>0&&array[j+1].msgList.size()>0){
						if(array[j].msgList.get(array[j].msgList.size()-1).time<array[j+1].msgList.get(array[j+1].msgList.size()-1).time){
							talkMessage=array[j];
							array[j]=array[j+1];
							array[j+1]=talkMessage;
						}
					}else if(array[j].msgList.size()>0){
						
					}else if(array[j+1].msgList.size()>0){
						talkMessage=array[j];
						array[j]=array[j+1];
						array[j+1]=talkMessage;
					}
				}
			}
			ArrayList<TalkMessage> msgs=new ArrayList<TalkMessage>();
			for(int i=0;i<array.length;i++){
				msgs.add(array[i]);
			}
			
			/*
			 * 有新消息放前边
			 */
			count=-1;
			for(int i=0;i<msgs.size();i++){
				talkMessage=msgs.get(i);
				if(talkMessage.old_new_msg_num>0){
					count++;
					msgs.remove(i);
					msgs.add(count, talkMessage);
				}
			}
			datasMail=msgs;
		}
		
		if(datasMail!=null){
			count=0;
			for(int i=0;i<datasMail.size();i++){
				count+=datasMail.get(i).old_new_msg_num;
			}
			saveTalkHistory(datasMail, PetApplication.petApp);
		}
		return count;
	}
	public static ArrayList<Gift> getGiftList(Context context) {
		// TODO Auto-generated method stub
		InputStream in=null;
		ArrayList<Gift> giftList=null;
		try {
			in=context.getResources().getAssets().open("gift.xml");
			XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
			XmlPullParser parser=factory.newPullParser();
			parser.setInput(in, "UTF-8");
			int event=parser.getEventType();
			giftList=new ArrayList<Gift>();
			Gift gift=null;
			while(event!=XmlPullParser.END_DOCUMENT){
				switch (event) {
				case XmlPullParser.START_TAG:
					if("gift".equals(parser.getName())){
						gift=new Gift();
					}
					if("no".equals(parser.getName())){
						gift.no=Integer.parseInt(parser.nextText());
					}
					if("name".equals(parser.getName())){
						gift.name=parser.nextText();
					}
					if("price".equals(parser.getName())){
						gift.price=Integer.parseInt(parser.nextText());
					}
					if("ratio".equals(parser.getName())){
						gift.ratio=Float.parseFloat(parser.nextText());
					}
					if("add_rq".equals(parser.getName())){
						gift.add_rq=Integer.parseInt(parser.nextText());
					}
					if("level".equals(parser.getName())){
						gift.level=Integer.parseInt(parser.nextText());
					}
					if("isReal".equals(parser.getName())){
						int isReal=Integer.parseInt(parser.nextText());
						if(isReal==0){
							gift.isReal=false;
						}else{
							gift.isReal=true;
						}
						
					}
					if("detail_image".equals(parser.getName())){
						String value=parser.nextText();
						if(!StringUtil.isEmpty(value)){
							gift.detail_image=Integer.parseInt(value);
						}
						
					}
					if("sale_status".equals(parser.getName())){
						String value=parser.nextText();
						if(!StringUtil.isEmpty(value)){
							gift.sale_status=Integer.parseInt(value);
						}
					}
					if("effect_des".equals(parser.getName())){
						gift.effect_des=parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if("gift".equals(parser.getName())){
						giftList.add(gift);
					}
					break;
				case XmlPullParser.END_DOCUMENT:
					
					break;
				}
				event=parser.next();
				
			}
			return giftList;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return giftList;
	}
	public static String[] getUserJobs(){
		String[] jobs=null;
		jobs=PetApplication.petApp.getResources().getStringArray(R.array.job);
		/*if(plant==2){
			jobs=PetApplication.petApp.getResources().getStringArray(R.array.job_dog);
		}else{
			jobs=PetApplication.petApp.getResources().getStringArray(R.array.job_cat);
		}*/
		return jobs;
	}
	/**
	 * 根据手机的dpi不同，设定不同的缩放比例
	 */
	public static int getScaleByDPI(Activity activity){
		DisplayMetrics displayMetrics=new DisplayMetrics();
	    activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	    int desityDpi=displayMetrics.densityDpi;
	    float desity=displayMetrics.density;
	    if(desityDpi<=320){
	    	return 4;
	    }else if(desityDpi<480){
	    	
	    }
	    return 2;
	}
	/**
	 * 根据手机的dpi不同，设定不同的缩放比例
	 */
	public static int topicImageGetScaleByDPI(Activity activity){
		DisplayMetrics displayMetrics=new DisplayMetrics();
	    activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	    int desityDpi=displayMetrics.densityDpi;
	    float desity=displayMetrics.density;
	    if(desityDpi<=320){
	    	return 2;
	    }else if(desityDpi<480){
	    	
	    }
	    return 1;
	}
	/**
	 * 根据等级获得省级需要的经验
	 * @param level
	 * @return
	 */
	public static int getExpByLevel(int level){
//		return ((level*10+225)*(level*10+225)-235*235)/40;
		return 110*(level-1)+5*(level+2)*(level-1)/2;
	}
	/**
	 * 根据官衔等级获取需要的贡献度
	 * @param rank
	 * @return
	 */
	public static int getContriByJobRank(int rank){
		if(rank==1){
		   return 0;	
		}
		return rank*rank*132-48;
//		return 132*rank*-48;
	}
	public static void viewStartTransAnim(View view,long time,int start,int to){
		view.clearAnimation();
		TranslateAnimation transAnim1=new TranslateAnimation(start, to, 0, 0);
		transAnim1.setDuration(time);
		transAnim1.setFillAfter(true);
		transAnim1.setRepeatMode(Animation.REVERSE);
		transAnim1.setInterpolator(new AccelerateInterpolator());
		transAnim1.setRepeatCount(Animation.INFINITE);
		view.startAnimation(transAnim1);
	}
	/**
	 * 隐藏软键盘
	 * @param activity
	 */
	public static void hideSoftKeybord(Activity activity){
		InputMethodManager im=(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		
//		if(activity.getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
		im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	public static String getAPKVersionName(Context context){
		PackageManager packageManager=context.getPackageManager();
		try {
			PackageInfo pInfo=packageManager.getPackageInfo("com.aidigame.hisun.pet", 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "1.0";
	}


}
