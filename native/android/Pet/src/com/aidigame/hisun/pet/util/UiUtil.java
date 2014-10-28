package com.aidigame.hisun.pet.util;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.constant.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class UiUtil {
	/**
	 * 界面通用设置
	 * @param context
	 */
	public static void setScreenInfo(Context context){
		Activity activity=(Activity)context;
		PetApplication.petApp.activityList.add(activity);
		//垂直显示
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//没有标题
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏显示
//		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	public static void setWidthAndHeight(Activity activity){
		DisplayMetrics dm=new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constants.screen_width=dm.widthPixels;
		Constants.screen_height=dm.heightPixels;
		
		LogUtil.i("me", "Constants.screen_width="+Constants.screen_width+",Constants.screen_height="+Constants.screen_height);
	}

}
