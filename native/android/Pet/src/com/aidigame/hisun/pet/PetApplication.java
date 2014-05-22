package com.aidigame.hisun.pet;

import android.app.Application;
import android.graphics.Bitmap;

public class PetApplication extends Application{
	public static PetApplication petApp;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		petApp=this;
	}

}
