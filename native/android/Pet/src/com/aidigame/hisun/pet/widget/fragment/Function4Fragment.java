package com.aidigame.hisun.pet.widget.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aidigame.hisun.pet.R;

public class Function4Fragment implements OnClickListener{
	Context context;
	LinearLayout parent;
	View view;
	Button bt1,bt2,bt3;
	public Function4Fragment(Context context,LinearLayout view){
		this.context=context;
		this.parent=view;
		parent.removeAllViews();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(context).inflate(R.layout.fragment_function_4, null);
		parent.addView(view,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		bt1=(Button)view.findViewById(R.id.button1);
		bt2=(Button)view.findViewById(R.id.button2);
		bt3=(Button)view.findViewById(R.id.button3);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			
			break;
		case R.id.button2:
			
			break;
		case R.id.button3:
			
			break;
		}
	}

}
