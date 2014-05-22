package com.aidigame.hisun.pet.widget;

import java.text.SimpleDateFormat;

import com.aidigame.hisun.pet.R;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateTitle {
	Context context;
	LinearLayout parentView;
	View view;
	TextView timeTv,batteryTv;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			long time=System.currentTimeMillis();
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
			String timeStr=sdf.format(time);
			timeTv.setText(timeStr);
		};
	};
	public CreateTitle(Context context,LinearLayout view){
		this.context=context;
		this.parentView=view;
		initView();
		handler.sendEmptyMessage(1);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					try {
						Thread.sleep(1000);
						handler.sendEmptyMessage(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	private void initView() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(context).inflate(R.layout.widget_title, null);
		parentView.addView(view);
		timeTv=(TextView)view.findViewById(R.id.textView3);
		batteryTv=(TextView)view.findViewById(R.id.textView4);
	}

}
