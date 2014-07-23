package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.XinlangShare;

public class UnregisterNoteActivity extends Activity implements OnClickListener{
	Button cancel;
	ImageView weixin,xinlang;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_unregiser_note);
		cancel=(Button)findViewById(R.id.button1);
		weixin=(ImageView)findViewById(R.id.imageView1);
		xinlang=(ImageView)findViewById(R.id.imageView2);
		cancel.setOnClickListener(this);
		weixin.setOnClickListener(this);
		xinlang.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Intent intent0=new Intent(this,RegisterActivity.class);
			this.startActivity(intent0);
			this.finish();
			break;
		case R.id.imageView1:
		    if(Constants.api==null)WeixinShare.regToWeiXin(this);
		    Intent intent1=new Intent(this,IntroduceActivity.class);
			this.startActivity(intent1);
			this.finish();
			break;
		case R.id.imageView2:
			XinlangShare.xinlangAuth(this);
			break;
		}
		
	}

}
