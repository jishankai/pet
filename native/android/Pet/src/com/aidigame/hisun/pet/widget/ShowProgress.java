package com.aidigame.hisun.pet.widget;

import android.content.Context;
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

import com.aidigame.hisun.pet.R;

public class ShowProgress {
	Context context;
	String info;
	LinearLayout parent;
	View view;
	RelativeLayout  allRelativeLayout;
	ImageView cicleIV;
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
		cicleIV=(ImageView)view.findViewById(R.id.imageView2);
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
		
		startAnimation();
	}
	public void progressCancel(){
		parent.removeView(view);
		parent.setVisibility(View.INVISIBLE);
	}
	public void showProgress(){
		initView();
	}
	public void startAnimation(){
		Animation anim=AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
		cicleIV.clearAnimation();
		cicleIV.startAnimation(anim);
	}

}
