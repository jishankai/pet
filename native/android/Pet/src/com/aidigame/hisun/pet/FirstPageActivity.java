package com.aidigame.hisun.pet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;

public class FirstPageActivity extends Activity{
	ImageView welcomeImage;
	ImageView imageView;
	boolean canJump=false;
	Animation animation;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				
			}if(msg.what==Constants.LOGIN_SUCCESS){
				if(canJump){
					Intent intent2=new Intent(FirstPageActivity.this,HomeActivity.class);
					FirstPageActivity.this.startActivity(intent2);
					FirstPageActivity.this.finish();
				}else{
					handler.sendEmptyMessageAtTime(Constants.LOGIN_SUCCESS, 50);
				}
				
			}else if(msg.what==3){
				animation=AnimationUtils.loadAnimation(FirstPageActivity.this, R.anim.anim_scale);
				imageView.setAnimation(animation);
				animation.start();
			}
			
			
			
			/*if(msg.what==0){
				SharedPreferences sPreferences=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
				
				if(Constants.status==0){
					welcomeImage.setImageResource(R.drawable.welcome_weixin);
				}else{
					welcomeImage.setImageResource(R.drawable.welcome_xinlang);
				}
				welcomeImage.setVisibility(View.VISIBLE);
				if(Constants.status==0){
					boolean flag=WeixinShare.regToWeiXin(FirstPageActivity.this);
					if(flag){
						login();
					}else{
						Toast.makeText(FirstPageActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						welcomeImage.setVisibility(View.INVISIBLE);
					}
				}else if(Constants.status==1){
					String token=sPreferences.getString("xinlangToken", null);
					if(token!=null){
						Oauth2AccessToken accessToken=Oauth2AccessToken.parseAccessToken(token);
						if(accessToken!=null){
							if(!accessToken.isSessionValid()){
								AlertDialog.Builder builder=new AlertDialog.Builder(FirstPageActivity.this);
								builder.setTitle("提示").setMessage("新浪微博登陆账号已过期，请重新登陆！")
								.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										xinlangAuth();
									}
								});
								
								builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
								final AlertDialog dialog=builder.show();
								
							}else{
								login();
							}
						}else{
							xinlangAuth();
						}
						Constants.accessToken=accessToken;
					}else{
						xinlangAuth();
					}
				}
			}*/
			if(msg.what==Constants.LOGIN_SUCCESS){
				/*else if(Constants.status==1){
					Intent intent1=new Intent(FirstPageActivity.this,HomeActivity.class);
					FirstPageActivity.this.startActivity(intent1);
					Intent intent2=new Intent(FirstPageActivity.this,IntroduceActivity.class);
					FirstPageActivity.this.startActivity(intent2);
					FirstPageActivity.this.finish();
				}*/
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(HomeActivity.homeActivity!=null){
			Intent intent=new Intent(this,HomeActivity.class);
			this.startActivity(intent);
			this.finish();
		}
		IntentFilter filter=new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver, filter);
		//需要获得SID，不管登陆成不成功
		login();
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_first_page);
		welcomeImage=(ImageView)findViewById(R.id.imageView1);
		imageView=(ImageView)findViewById(R.id.imageView1);
		SharedPreferences sp=this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		String path=sp.getString("welcome", null);
		if(path!=null){
			BitmapFactory.Options opt=new BitmapFactory.Options();
			opt.inSampleSize=2;
			imageView.setImageBitmap(BitmapFactory.decodeFile(path,opt));
		}

		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				handler.sendEmptyMessage(3);
				/*try {
//					Thread.sleep(50);
					handler.sendEmptyMessage(3);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				try {
					Thread.sleep(3000);
					canJump=true;
					handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	/**
	 * 登陆
	 */
	public void login(){
		LogUtil.i("exception", "执行登陆");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.login(FirstPageActivity.this,handler);
				SharedPreferences sPreferences=FirstPageActivity.this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
				boolean flag=sPreferences.getBoolean("race", false);
				if(!flag)
				HttpUtil.getRaceType(FirstPageActivity.this);
				HttpUtil.downloadWelcomeImage(null,FirstPageActivity.this);
			}
		}).start();
			
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
	BroadcastReceiver receiver=new BroadcastReceiver(){
		public void onReceive(Context context, android.content.Intent intent) {
			ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileInfo=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			//注意wifiInfo与mobileInfo可能为空
			if(mobileInfo!=null&&mobileInfo.isConnected()){
				//连接网络
				login();
			}else if(wifiInfo!=null&&wifiInfo.isConnected()){
				//连接网络
				login();
			}
			/*if(!mobileInfo.isConnected()&&!wifiInfo.isConnected()){
				//未连接网络
			}else{
				//连接网络
			}*/
		};
	};


}
