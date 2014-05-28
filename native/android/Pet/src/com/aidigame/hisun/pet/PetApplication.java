package com.aidigame.hisun.pet;

import android.app.Application;

import com.aidigame.hisun.pet.bean.User;

public class PetApplication extends Application{
	public static PetApplication petApp;
	public User user;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		petApp=this;
	}

}
