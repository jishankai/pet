package com.aidigame.hisun.pet.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;

public class UiUtil {
	/**
	 * ������Ļ ȫ�� ���� �Ƿ���Title
	 * @param context
	 */
	public static void setScreenInfo(Context context){
		Activity activity=(Activity)context;
		//������ʾ
//		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//û�б���
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȫ����ʾ
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

}
