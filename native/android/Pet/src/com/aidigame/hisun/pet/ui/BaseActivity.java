package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
	}
	   @Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }

}
