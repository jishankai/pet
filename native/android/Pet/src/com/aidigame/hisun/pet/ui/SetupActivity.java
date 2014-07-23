package com.aidigame.hisun.pet.ui;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.WeixinShare;

public class SetupActivity extends Activity implements OnClickListener{
	ImageView back;
	TextView tvAcountVision;
	ImageView setup_image1;
	ImageView setup_image2,setup_image3,setup_image4;
	ImageView setup_image5,setup_acount_image;
	RelativeLayout acountVisiLayout;
	public static boolean getXinlangAuth=false;
	int acount=-1;//1同步发送到新浪微博，2 绑定新浪微博；-1 Activity启动时进入onResume方法，啥也不做。
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_setup);
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		back=(ImageView)findViewById(R.id.imageView1);
		setup_image1=(ImageView)findViewById(R.id.setup_imageview1);
		setup_image2=(ImageView)findViewById(R.id.setup_imageview2);
		setup_image3=(ImageView)findViewById(R.id.setup_imageview3);
		setup_image4=(ImageView)findViewById(R.id.setup_imageview4);
		setup_image5=(ImageView)findViewById(R.id.setup_imageview5);
		setup_acount_image=(ImageView)findViewById(R.id.setup_acount_image);
		tvAcountVision=(TextView)findViewById(R.id.tv_acount_visibilty);
		acountVisiLayout=(RelativeLayout)findViewById(R.id.account_visibility_relativelayout);
		acountVisiLayout.setClickable(true);
		acountVision();
		SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		if(sp.getBoolean(Constants.LOCK_TO_XINLANG, false)){
			setup_image1.setImageResource(R.drawable.checked);;
		}else{
			setup_image1.setImageResource(R.drawable.unchecked);;
		}
		if(sp.getBoolean(Constants.LOCK_TO_WEIXIN, false)){
			setup_image2.setImageResource(R.drawable.checked);;
		}else{
			setup_image2.setImageResource(R.drawable.unchecked);;
		}
		if(sp.getBoolean(Constants.PICTURE_SEND_TO_XINLANG, false)){
			setup_image3.setImageResource(R.drawable.checked);;
		}else{
			setup_image3.setImageResource(R.drawable.unchecked);;
		}
		if(sp.getBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false)){
			setup_image4.setImageResource(R.drawable.checked);;
		}else{
			setup_image4.setImageResource(R.drawable.unchecked);;
		}
	}
	private void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(this);
		setup_image1.setOnClickListener(this);
		setup_image2.setOnClickListener(this);
		setup_image3.setOnClickListener(this);
		setup_image4.setOnClickListener(this);
		acountVisiLayout.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtil.i("exception", "onResume启动");
		acountVision();
		SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		Editor editor=sp.edit();
		if(getXinlangAuth){
			if(acount==2){
				setup_image1.setImageResource(R.drawable.checked);
				acount=-1;
				editor.putBoolean(Constants.LOCK_TO_XINLANG,true);
			}else if(acount==1){
				setup_image3.setImageResource(R.drawable.checked);
				editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG,true);
			}
			
			
		}else{
			if(acount!=-1){
				setup_image3.setImageResource(R.drawable.unchecked);
				setup_image1.setImageResource(R.drawable.unchecked);
				editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG,false);
				editor.putBoolean(Constants.LOCK_TO_XINLANG, false);
				acount=-1;
			}
			
		}
		editor.commit();
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		Editor editor=sp.edit();
		switch (v.getId()) {
		case R.id.imageView1:
			this.finish();
			break;
		case R.id.setup_imageview1:
//			setup_acount_image.setImageResource(R.drawable.xinlang);
			/*if(setup_image1.isChecked()){
				setup_image2.setChecked(false);
				
				editor.putBoolean(Constants.LOCK_TO_WEIXIN, setup_image2.isChecked());
				
			}else{
//				setup_image1.setChecked(true);
//				setup_image2.setChecked(false);
			}*/
			if(!sp.getBoolean(Constants.LOCK_TO_XINLANG, false)){
				acount=2;
				if(UserStatusUtil.hasXinlangAuth(this)){
					editor.putBoolean(Constants.LOCK_TO_XINLANG, true);
					setup_image1.setImageResource(R.drawable.checked);
				}else{
					setup_image1.setImageResource(R.drawable.unchecked);
					editor.putBoolean(Constants.LOCK_TO_XINLANG, false);
				}
			}else{
				acount=-1;
				setup_image1.setImageResource(R.drawable.unchecked);
				editor.putBoolean(Constants.LOCK_TO_XINLANG, false);
//				editor.putString("xinlangToken", null);
				editor.remove("xinlangToken");
				Constants.accessToken=null;
			}
			
			break;
		case R.id.setup_imageview2:
//			setup_acount_image.setImageResource(R.drawable.friend_weixin);
			if(!sp.getBoolean(Constants.LOCK_TO_WEIXIN, false)){
				editor.putBoolean(Constants.LOCK_TO_WEIXIN, true);
				setup_image1.setImageResource(R.drawable.checked);
			}else{
//				setup_image2.setChecked(true);
//				setup_image1.setChecked(false);
				setup_image1.setImageResource(R.drawable.unchecked);
				editor.putBoolean(Constants.LOCK_TO_WEIXIN, false);
			}
			acount=1;
			break;
		case R.id.setup_imageview3:
			acount=1;
			if(!sp.getBoolean(Constants.PICTURE_SEND_TO_XINLANG, false)){
				if(UserStatusUtil.hasXinlangAuth(this)){
					setup_image3.setImageResource(R.drawable.checked);
					editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, true);
				}else{
					setup_image3.setImageResource(R.drawable.unchecked);
					editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, false);
				}
			}else{
				editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, false);
				setup_image3.setImageResource(R.drawable.unchecked);
			}
			break;
		case R.id.setup_imageview4:
			if(!sp.getBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false)){
				
				if(Constants.api==null){
					boolean flag=WeixinShare.regToWeiXin(this);
					if(!flag){
						Toast.makeText(this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						setup_image4.setImageResource(R.drawable.unchecked);
						editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
						return;
					}else{
						editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, true);
						setup_image4.setImageResource(R.drawable.checked);
					}
				}else{
					editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, true);
					setup_image4.setImageResource(R.drawable.checked);
				}
			}else{
				editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
				setup_image4.setImageResource(R.drawable.unchecked);
			}
			
			break;
		case R.id.account_visibility_relativelayout:
			Intent intent=new Intent(this,AcountVisibilty.class);
			intent.putExtra("acount", acount);
			this.startActivity(intent);
			break;
		}
		editor.commit();
	}
	private void acountVision(){
		SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
	
		switch (sp.getInt(Constants.ACOUNT_VISIVAL_AREAR, 2)) {
		case 1:
			tvAcountVision.setText("所有人");
			
			break;
		case 2:
			tvAcountVision.setText("我关注的人");
			break;
		case 3:
			tvAcountVision.setText("我自己");
			break;
		}
	}

}
