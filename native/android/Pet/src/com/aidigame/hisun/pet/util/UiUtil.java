package com.aidigame.hisun.pet.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;

public class UiUtil {
	/**
	 * 设置屏幕 全屏 方向 是否有Title
	 * @param context
	 */
	public static void setScreenInfo(Context context){
		Activity activity=(Activity)context;
		//竖屏显示
//		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//没有标题
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏显示
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

}
