package com.aidigame.hisun.pet.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
	public ShowProgress(Context context,String info,LinearLayout parent) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.info=info;
		this.parent=parent;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(context).inflate(R.layout.widget_progress, null);
		allRelativeLayout=(RelativeLayout)view.findViewById(R.id.parent);
		allRelativeLayout.setClickable(true);
		allRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
		parent.addView(view);
		TextView tView=(TextView)view.findViewById(R.id.textView1);
		if(info!=null)tView.setText(info);
	}
	public void progressCancel(){
		parent.removeView(view);
	}
	public void showProgress(){
		parent.removeAllViews();;
		parent.addView(view);
	}

}
