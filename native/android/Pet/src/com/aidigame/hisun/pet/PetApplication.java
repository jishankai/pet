package com.aidigame.hisun.pet;

import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;

import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.LogUtil;

public class PetApplication extends Application{
	public static PetApplication petApp;
	public User user;
    Constants constants;
    public LinkedList<Activity> activityList;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		petApp=this;
		LogUtil.i("exception","petApplication执行onCreate方法" );
		activityList=new LinkedList<Activity>();
		constants=new Constants();
		
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		LogUtil.i("exception","petApplication执行onTerminate方法" );
		for(Activity a:activityList){
			if(a!=null)a.finish();
		}
	}
	

}
