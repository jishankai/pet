package com.aidigame.hisun.pet.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;

public class FirstPageActivity extends Activity implements OnClickListener{
	LinearLayout linearLayout2,linearLayout1,linearLayout3;
	ImageView welcomeImage;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==0){
				welcomeImage.setVisibility(View.VISIBLE);
				if(Constants.status==0){
					
					welcomeImage.setImageResource(R.drawable.welcome_weixin);
				}else{
					welcomeImage.setImageResource(R.drawable.welcome_xinlang);
				}
			}
			if(msg.what==1){
				if(Constants.isSuccess){
					Intent intent2=new Intent(FirstPageActivity.this,HomeActivity.class);
					FirstPageActivity.this.startActivity(intent2);
					FirstPageActivity.this.finish();
				}else{
					Intent intent2=new Intent(FirstPageActivity.this,RegisterActivity.class);
					FirstPageActivity.this.startActivity(intent2);
					FirstPageActivity.this.finish();
				}
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_first_page);
		linearLayout1=(LinearLayout)findViewById(R.id.weixin_linearLayout);
		linearLayout2=(LinearLayout)findViewById(R.id.xinlang_linearLayout);
		linearLayout3=(LinearLayout)findViewById(R.id.just_scan_linearLayout);
		welcomeImage=(ImageView)findViewById(R.id.welcome_imageview);
		linearLayout1.setClickable(true);
		linearLayout2.setClickable(true);
		linearLayout3.setClickable(true);
		linearLayout1.setOnClickListener(this);
		linearLayout2.setOnClickListener(this);
		linearLayout3.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.weixin_linearLayout:
			Constants.status=0;
			handler.sendEmptyMessage(0);
			/*Intent intent1=new Intent(this,WelcomeActivity.class);
			this.startActivity(intent1);
			this.finish();*/
			login();
			
			break;
		case R.id.xinlang_linearLayout:
			Constants.status=1;
			/*Intent intent2=new Intent(this,WelcomeActivity.class);
			this.startActivity(intent2);
			this.finish();*/
			handler.sendEmptyMessage(0);
			login();
			break;
		case R.id.just_scan_linearLayout:
			Constants.status=2;
			Intent intent3=new Intent(this,HomeActivity.class);
			this.startActivity(intent3);
			this.finish();

			break;
		}

	}
	public void login(){
		File file=new File(Environment.getExternalStorageDirectory()+File.separator+"pet");
		if(!file.exists()){
			
			
			 Toast.makeText(this, "将pet文件夹考入内置内存卡中", 10000).show();
			 return;
		}
	    handler.sendEmptyMessage(1);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				HttpUtil.login(FirstPageActivity.this,handler);
				
			}
		}).start();
			
	}

}
