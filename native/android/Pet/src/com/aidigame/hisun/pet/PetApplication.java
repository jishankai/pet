package com.aidigame.hisun.pet;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

import dalvik.system.VMRuntime;

public class PetApplication extends Application{
	public static PetApplication petApp;
	public User user;
    Constants constants;
    private final static float TARGET_HEAP_UTILIZATION = 0.75f;
    public LinkedList<Activity> activityList;
//    public static final String ERROR_MESSAGE="COM.AIDIGAME.HISUN.PET.ERROR_MESSAGE";
	
    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				/*OpenUDID_manager.sync(PetApplication.this);
				handler.sendEmptyMessageAtTime(1, 10);*/
			}
		}).start();
		
		VMRuntime.getRuntime().setTargetHeapUtilization(TARGET_HEAP_UTILIZATION);
		final int HEAP_SIZE = 10 * 1024* 1024 ; 
		VMRuntime.getRuntime().setMinimumHeapSize(HEAP_SIZE);
		petApp=this;
		CrashHandler crashHandler=CrashHandler.getInstance();
		crashHandler.init(this);
		LogUtil.i("exception","petApplication执行onCreate方法" );
		activityList=new LinkedList<Activity>();
		constants=new Constants();
		
		
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	
	

}
