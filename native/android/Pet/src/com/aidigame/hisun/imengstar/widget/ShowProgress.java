package com.aidigame.hisun.imengstar.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.R;

public class ShowProgress {
	Context context;
	String info;
	LinearLayout parent;
	View view;
	RelativeLayout  allRelativeLayout;
	ImageView catRunning;
	
	public ShowProgress(Context context,LinearLayout parent) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.info=info;
		this.parent=parent;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(context).inflate(R.layout.item_loading_progress, null);
		allRelativeLayout=(RelativeLayout)view.findViewById(R.id.parent);
		allRelativeLayout.setClickable(true);
		catRunning=(ImageView)view.findViewById(R.id.cat_running);
		allRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
		parent.setVisibility(View.VISIBLE);
		parent.removeAllViews();
		parent.addView(view);
		
	}
	public void progressCancel(){
		parent.removeView(view);
		parent.setVisibility(View.INVISIBLE);
	}
	public void showProgress(){
		initView();
	}
	

}
