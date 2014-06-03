package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.UiUtil;

public class WelcomeActivity extends Activity {
	ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_welcome);
		imageView=(ImageView)findViewById(R.id.imageView1);
		if(Constants.status==0){
			imageView.setImageResource(R.drawable.welcome_weixin);
		}else if(Constants.status==1){
			imageView.setImageResource(R.drawable.welcome_xinlang);
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(5000);
					Intent intent=null;
					intent=new Intent(WelcomeActivity.this,IntroduceActivity.class);
					
					WelcomeActivity.this.startActivity(intent);
					WelcomeActivity.this.finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();;
	}

}
