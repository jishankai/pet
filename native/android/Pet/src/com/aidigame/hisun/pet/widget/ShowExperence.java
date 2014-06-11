package com.aidigame.hisun.pet.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter;
import com.aidigame.hisun.pet.bean.Topic;

public class ShowExperence {
	
	Activity activity;
	LinearLayout parent;
	View showExperience;
	public ShowExperence(Activity activity,LinearLayout parent){
		this.activity=activity;
		this.parent=parent;
		initView();
		initListener();
	}
	private void initListener() {
		// TODO Auto-generated method stub
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		showExperience=LayoutInflater.from(activity).inflate(R.layout.widget_user_experence, null);
		parent.removeAllViews();
		parent.addView(showExperience);
	}

}
