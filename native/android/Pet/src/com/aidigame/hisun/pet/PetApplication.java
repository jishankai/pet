package com.aidigame.hisun.pet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;

import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.huanxin.DemoHXSDKHelper;
import com.aidigame.hisun.pet.huanxin.User;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import dalvik.system.VMRuntime;

public class PetApplication extends Application{
	public static PetApplication petApp;
	public MyUser user;
    Constants constants;
    private final static float TARGET_HEAP_UTILIZATION = 0.75f;
    public LinkedList<Activity> activityList;
    public Bitmap blurBmp;
    
    
  //登陆是否成功
  	public static boolean isSuccess=false;
	public static String SID;
    
    public static MyUser myUser;
    
//    public static final String ERROR_MESSAGE="COM.AIDIGAME.HISUN.PET.ERROR_MESSAGE";
	
    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	
		VMRuntime.getRuntime().setTargetHeapUtilization(TARGET_HEAP_UTILIZATION);
		final int HEAP_SIZE = 10 * 1024* 1024 ; 
		VMRuntime.getRuntime().setMinimumHeapSize(HEAP_SIZE);
		petApp=this;
		CrashHandler crashHandler=CrashHandler.getInstance();
		crashHandler.init(this);
		LogUtil.i("exception","petApplication执行onCreate方法" );
		activityList=new LinkedList<Activity>();
		constants=new Constants();
		
		
		SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		Editor ed=sp.edit();
		String versions=sp.getString(Constants.BASEIC_VERSION, "");
		if(!StringUtil.isEmpty(versions)){
			if(!versions.equals(StringUtil.getAPKVersionName(this))){
				ed.putString(Constants.BASEIC_VERSION, StringUtil.getAPKVersionName(this));
				ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE1, true);
				ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE2, true);
				ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE3, true);
				ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE4, true);
				ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE5, true);
				ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE6, true);
				ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE7, true);
			}else{
				
			}
		}else{
			ed.putString(Constants.BASEIC_VERSION, StringUtil.getAPKVersionName(this));
			ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE1, true);
			ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE2, true);
			ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE3, true);
			ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE4, true);
			ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE5, true);
			ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE6, true);
			ed.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE7, true);
		}
		ed.commit();
		
		
		
		File file=new File(Constants.Picture_Root_Path);
		if(!file.exists()){
			file.mkdir();
		}
		String path=Constants.Picture_Root_Path+File.separator+".nomedia";
		File file2=new File(path);
		if(file2.exists()){
			if(file2.isDirectory())
			{
				file2.delete();
				try {
					file2.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			try {
				file2.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*
		 * 配置ImageLoaderConfiguration
		 */
		//图片缓存路径
		File cacheDir=StorageUtils.getOwnCacheDirectory(getApplicationContext(), "pet/imageloader/cache");
		ImageLoaderConfiguration config=new ImageLoaderConfiguration
				.Builder(getApplicationContext())
		        .threadPriority(Thread.NORM_PRIORITY-2)//
		        .denyCacheImageMultipleSizesInMemory()
		        .discCache(new UnlimitedDiscCache(cacheDir))
		        .discCacheSize(100*1024*1024)
		        .tasksProcessingOrder(QueueProcessingType.LIFO)
//		        .memoryCacheSize(2*1024*1024)
		        .threadPoolSize(2)//建议小于5个
//				.memoryCache(new WeakMemoryCache())
//		        .writeDebugLogs()//当应用发布时移出
		        .build();//构建完成
		ImageLoader.getInstance().init(config);
		
		
		
		
		/**
		 * 环信初始化
		 */
		/*EMChat.getInstance().setDebugMode(true);
		EMChat.getInstance().init(this);*/
		setInit(this);
		
	}
    
    
    /**
     * 环信
     */
    public static Context applicationContext;
	private static Application instance;
	// login user name
	public final String PREF_USERNAME = "username";
	
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();
	/**
	 * by scx
	 * 初始化，
	 */
	public static void setInit(Application application){
	    applicationContext = application;
        instance = application;

        /**
         * this function will initialize the HuanXin SDK
         * 
         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
         * 
         * 环信初始化SDK帮助函数
         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         * 
         * for example:
         * 例子：
         * 
         * public class DemoHXSDKHelper extends HXSDKHelper
         * 
         * HXHelper = new DemoHXSDKHelper();
         * if(HXHelper.onInit(context)){
         *     // do HuanXin related work
         * }
         */
        hxSDKHelper.onInit(applicationContext);
	}

	public static Application getInstance() {
		return instance;
	}
 
	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
	public static Map<String, User> getContactList() {
	    return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 *
	 * @param contactList
	 */
	public static void setContactList(Map<String, User> contactList) {
	    hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public static String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public static String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param myUser
	 */
	public static void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public static void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public static void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(emCallBack);
	}
    

	

}
