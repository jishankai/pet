package com.aidigame.hisun.pet.ui;

import java.io.File;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.blur.TopCenterImageView;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.LoginJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.service.DownloadImagesAsyncTask;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.CircleView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuActivity extends Activity implements OnClickListener{
	TextView tv3,tv2,tv1,mailText,activityText;
	CircleView imageView3;
	ImageView imageView5,back;
	LinearLayout userHome,exp,setup;
	TopCenterImageView blur_view;
	RelativeLayout act,message;
	public static final  int USER_INFO=1;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case USER_INFO:
				if(Constants.user==null){
					handler.sendEmptyMessageDelayed(USER_INFO, 100);
				}else{
					setViews();
				}
				break;
		}}};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		LogUtil.i("exception", "创建HomeActivity");
		setContentView(R.layout.activity_menu);
		
		initView();
		//获取消息和活动数目
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						
						final LoginJson.Data data=HttpUtil.getMailAndActivityNum();
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(data!=null){
									if(data.mail_count==0){
										mailText.setVisibility(View.INVISIBLE);
									}else{
										mailText.setVisibility(View.VISIBLE);
										mailText.setText(""+data.mail_count);
									}
									if(data.topic_count==0){
										activityText.setVisibility(View.INVISIBLE);
									}else{
										activityText.setVisibility(View.VISIBLE);
										String temp=""+data.topic_count;
										activityText.setText(temp);
									}
								}
							}
						});
						
					}
				}).start();
		if(Constants.user==null){
			handler.sendEmptyMessageDelayed(USER_INFO, 100);
		}else{
			setViews();
		}
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv3=(TextView)findViewById(R.id.textView3);
		blur_view=(TopCenterImageView)findViewById(R.id.blur_view);
		Bitmap bmp=ImageUtil.getBitmapFromView(HomeActivity.homeRelativeLayout,this);
		blur_view.setImageBitmap(bmp);
		blur_view.setAlpha(0.9342857f);//0.9342857
		blur_view.setVisibility(View.VISIBLE);
		mailText=(TextView)findViewById(R.id.message_text);
		activityText=(TextView)findViewById(R.id.activity_text);
		imageView3=(CircleView)findViewById(R.id.imageView3);
		imageView5=(ImageView)findViewById(R.id.imageView5);
		back=(ImageView)findViewById(R.id.imageView1);
		userHome=(LinearLayout)findViewById(R.id.userhome_linearLayout);
		setup=(LinearLayout)findViewById(R.id.setup_linearLayout);
		exp=(LinearLayout)findViewById(R.id.experence_linearLayout);
		message=(RelativeLayout)findViewById(R.id.message_linearLayout);
		act=(RelativeLayout)findViewById(R.id.activity_linearLayout);
		userHome.setOnClickListener(this);
		setup.setOnClickListener(this);
		exp.setOnClickListener(this);
		message.setOnClickListener(this);
		act.setOnClickListener(this);
		back.setOnClickListener(this);
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_user_info_view);
		findViewById(R.id.user_info_relativelayout).setAnimation(animation);
		animation.start();
	}

	/**
	 * 界面初始化，头像，昵称，性别 等 
	 */
	public void setViews(){
		if(Constants.user.gender==1){
			imageView5.setImageResource(R.drawable.male1);//性别
		}else if(Constants.user.gender==2){
			imageView5.setImageResource(R.drawable.female1);//性别
		}
        imageView3.setOnClickListener(this);
		tv1.setText(Constants.user.nickName);//昵称//种族
		 final SharedPreferences sp=this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);

		    if( sp.contains(Constants.user.race)){
			  tv2.setText(""+sp.getString(Constants.user.race, ""));
		   }else{
			   new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					boolean flag=HttpUtil.getRaceType(MenuActivity.this);
					if(flag){
						tv2.setText(sp.getString(Constants.user.race, ""));
					}
				}
			}).start();
		   }
		tv3.setText(""+Constants.user.age+"岁");//年龄
		if(Constants.user.iconPath!=null&&StringUtil.judgeImageExists(Constants.user.iconPath)){
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=4;
			imageView3.setImageBitmap(BitmapFactory.decodeFile(Constants.user.iconPath,options));
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					Constants.user.iconPath=HttpUtil.downloadIconImage(Constants.USER_DOWNLOAD_TX, Constants.user.iconUrl, null,MenuActivity.this);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							BitmapFactory.Options options=new BitmapFactory.Options();
							options.inSampleSize=4;
							imageView3.setImageBitmap(BitmapFactory.decodeFile(Constants.user.iconPath,options));
						}
					});
				}
			}).start();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.imageView1:
			/*if(HomeActivity.homeActivity!=null){
				HomeActivity.homeActivity.finish();
			}*/
			Intent intentHome=new Intent(this,HomeActivity.class);
			this.startActivity(intentHome);
			this.finish();
			break;
		case R.id.userhome_linearLayout:
			UserStatusUtil.downLoadUserInfo(this);
			Intent intent1=new Intent(this,UserHomepageActivity.class);
			intent1.putExtra("mode", 1);//1  用户主页；2 经验页
			intent1.putExtra("from", "HomeActivity");
			this.startActivity(intent1);
			this.finish();
			break;
		case R.id.experence_linearLayout:
			Intent intent2=new Intent(this,UserHomepageActivity.class);
			intent2.putExtra("mode", 2);//1  用户主页；2 经验页,3设置页
			intent2.putExtra("from", "HomeActivity");
			this.startActivity(intent2);
			this.finish();
			break;
		case R.id.setup_linearLayout:
			Intent intent3=new Intent(this,UserHomepageActivity.class);
			intent3.putExtra("mode", 3);//1  用户主页；2 经验页,3设置页
			intent3.putExtra("from", "HomeActivity");
			this.startActivity(intent3);
			this.finish();
			break;
		case R.id.message_linearLayout:
			Intent mIntent=new Intent(this,MessageActivity.class);
			this.startActivity(mIntent);
			this.finish();
			break;
		case R.id.activity_linearLayout:
			Intent intent4=new Intent(this,ScanActivitys.class);
			this.startActivity(intent4);
			this.finish();
			break;
		case R.id.imageView3:
			Intent intent=new Intent(this,ShowPictureActivity.class);
			intent.putExtra("path", Constants.user.iconPath);
			this.startActivity(intent);
			break;
			
		}
		
	}

}
