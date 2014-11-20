package com.aidigame.hisun.pet;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class FirstPageActivity extends Activity{
	ImageView welcomeImage;
	ImageView imageView;
	boolean canJump=false;
	Animation animation;
	HandleHttpConnectionException handleHttpConnectionException;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				
			}if(msg.what==Constants.LOGIN_SUCCESS){
				canJump=true;
				
			}else if(msg.what==3){
				animation=AnimationUtils.loadAnimation(FirstPageActivity.this, R.anim.anim_scale);
				imageView.setAnimation(animation);
				animation.start();
			}
			if(msg.what==Constants.LOGIN_SUCCESS){
				
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		IntentFilter filter=new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver, filter);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		//需要获得SID，不管登陆成不成功
		LogUtil.i("me", "创建界面时，执行登陆方法");
//		login();
		
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_first_page);
		welcomeImage=(ImageView)findViewById(R.id.imageView1);
		imageView=(ImageView)findViewById(R.id.imageView1);
		
		String url=getIntent().getStringExtra("url");
						BitmapFactory.Options options=new BitmapFactory.Options();
						options.inJustDecodeBounds=false;
						options.inSampleSize=1;
						options.inPreferredConfig=Bitmap.Config.RGB_565;
						options.inPurgeable=true;
						options.inInputShareable=true;
						DisplayImageOptions displayImageOptions=new DisplayImageOptions
					            .Builder()
					            .showImageOnLoading(R.drawable.blur)
						        .cacheInMemory(true)
						        .cacheOnDisc(true)
						        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
						        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
						        .decodingOptions(options)
				                .build();
						ImageLoader imageLoader=ImageLoader.getInstance();
						imageLoader.displayImage(Constants.WELCOME_IMAGE+url, imageView,new ImageLoadingListener() {
							
							@Override
							public void onLoadingStarted(String imageUri, View view) {
								// TODO Auto-generated method stub
								LogUtil.i("me","下载欢迎图片  开始"+imageUri);
							}
							
							@Override
							public void onLoadingFailed(String imageUri, View view,
									FailReason failReason) {
								// TODO Auto-generated method stub
								LogUtil.i("me","下载欢迎图片  失败"+imageUri);
								Intent intent=new Intent(FirstPageActivity.this,NewHomeActivity.class);
								FirstPageActivity.this.startActivity(intent);
								FirstPageActivity.this.finish();
							}
							
							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								LogUtil.i("me","下载欢迎图片  完成"+imageUri);
								/*Animation anim=AnimationUtils.loadAnimation(FirstPageActivity.this, R.anim.anim_scale);
								imageView.clearAnimation();
								imageView.startAnimation(anim);*/
								handler.postDelayed(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Intent intent=new Intent(FirstPageActivity.this,NewHomeActivity.class);
										FirstPageActivity.this.startActivity(intent);
										FirstPageActivity.this.finish();
									}
								},4000);
									
							}
							
							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								// TODO Auto-generated method stub
								LogUtil.i("me","下载欢迎图片  取消"+imageUri);
								Intent intent=new Intent(FirstPageActivity.this,NewHomeActivity.class);
								FirstPageActivity.this.startActivity(intent);
								FirstPageActivity.this.finish();
							}
						});
//						imageLoader.displayImage(Constants.UPLOAD_IMAGE_RETURN_URL+url, imageView);
			
	}
	/**
	 * 登陆
	 */
	public void login(){
		LogUtil.i("me", "执行登陆");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				SharedPreferences sPreferences=FirstPageActivity.this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
				/*boolean flag=sPreferences.getBoolean("race", false);
				if(!flag)
					HttpUtil.getRaceType(FirstPageActivity.this);*/
				String SID=sPreferences.getString("SID", null);
			   
				if(StringUtil.isEmpty(SID)){
					/*
					 * 防止用户频繁删除安装软件，将Sid清除，每次先判断
					 */
					getSIDAndUserID();
					
				}else{
					Constants.SID=SID;
					Constants.isSuccess=sPreferences.getBoolean("isRegister", false);
					if(Constants.isSuccess){
						Constants.user=new User();
						Constants.user.userId=sPreferences.getInt("usr_id", 0);
						Constants.user.u_nick=sPreferences.getString("name", "游荡的两脚兽");
						Constants.user.coinCount=sPreferences.getInt("gold", 500);
						Constants.user.u_iconUrl=sPreferences.getString("url", "");
						Constants.user.currentAnimal=new Animal();
						Constants.user.currentAnimal.u_rank=sPreferences.getString("job", "陌生人");
					}
					if(Constants.user!=null&&Constants.user.userId==0){
						getSIDAndUserID();
					}else{
						handler.sendEmptyMessageAtTime(Constants.LOGIN_SUCCESS, 50);
						
					}
					
				}
				
			}
		}).start();
			
	}
	public void getSIDAndUserID(){
		String SID=HttpUtil.getSID(this,handleHttpConnectionException.getHandler(FirstPageActivity.this));
		SharedPreferences sPreferences=FirstPageActivity.this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		if(!StringUtil.isEmpty(SID)){
			Constants.SID=SID;
			if(Constants.isSuccess){
				Constants.user.u_nick=sPreferences.getString("name", "游荡的两脚兽");
				Constants.user.coinCount=sPreferences.getInt("gold", 500);
				Constants.user.u_gender=sPreferences.getInt("usr_gender", 500);
				Constants.user.u_iconUrl=sPreferences.getString("url", "");
				Constants.user.currentAnimal=new Animal();
				Constants.user.currentAnimal.u_rank=sPreferences.getString("job", "陌生人");
				Constants.user.currentAnimal.pet_nickName=sPreferences.getString("a_nick", "陌生人");
			}
			
			handler.sendEmptyMessageAtTime(Constants.LOGIN_SUCCESS, 50);
			
		}else{
			HttpUtil.login(this,handler);
		}
	}
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	};
	/**
	 * 注册广播接受者，当网络连接状态发生改变时，需要处理的事项
	 * IntentFilter filter=new IntentFilter();
	 * filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	 * context.registerReceiver(receiver);
	 * 
	 * 使用完毕，注销接受者
	 * if(receiver!=null){
	 *    context.unregisterReceiver(receiver);
	 * }
	 */
	boolean netIsNotConnect=false;
	BroadcastReceiver receiver=new BroadcastReceiver(){
		public void onReceive(Context context, android.content.Intent intent) {
			ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileInfo=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			//注意wifiInfo与mobileInfo可能为空
			if(mobileInfo!=null&&mobileInfo.isConnected()){
				if(netIsNotConnect){
					//连接网络
					LogUtil.i("me", "广播1，网络连接监听器方法");
					login();
				}
				
			}else if(wifiInfo!=null&&wifiInfo.isConnected()){
				if(netIsNotConnect){
				//连接网络
				LogUtil.i("me", "广播2，网络连接监听器方法");
				login();
				}
			}else{
				netIsNotConnect=true;
			}
			/*if(!mobileInfo.isConnected()&&!wifiInfo.isConnected()){
				//未连接网络
			}else{
				//连接网络
			}*/
		};
	};


}
