package com.aidigame.hisun.pet;

import java.io.File;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.blur.Blur;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.Dialog4Activity;
import com.aidigame.hisun.pet.ui.Dialog4Activity.Dialog3ActivityListener;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.UpdateAPKActivity;
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
	ImageView imageView,begSure,imageView11;
	boolean canJump=false;
	Animation animation;
	HandleHttpConnectionException handleHttpConnectionException;
	RelativeLayout begLayout1,begLayout;
	TextView foodNum,petNum;
	public static FirstPageActivity firstPageActivity;
	ScrollView scrollview;
	Handler handler=new Handler(){
		int lastY=0;
		public void handleMessage(android.os.Message msg) {
			/*if(msg.what==1){
				int temp=scrollview.getScrollY();
				LogUtil.i("mi", "lastY===="+lastY);
				if(temp>=Constants.screen_height){
					finish();
					firstPageActivity=null;
					return;
				}
				if(temp>=Constants.screen_height*0.15f){
					TranslateAnimation anim=new TranslateAnimation(0, 0, -temp, -Constants.screen_height);
					anim.setDuration(500);
					anim.setFillAfter(true);
					begLayout1.setAnimation(anim);
					begLayout1.startAnimation(anim);
					anim.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							finish();
							firstPageActivity=null;
						}
					});
					return;
				}
				if(temp!=0){
					scrollview.scrollTo(0, 0);
					lastY=temp;
					handler.sendEmptyMessageDelayed(1, 100);
				}
				
			}if(msg.what==Constants.LOGIN_SUCCESS){
				canJump=true;
				
			}else if(msg.what==3){
				animation=AnimationUtils.loadAnimation(FirstPageActivity.this, R.anim.anim_scale);
				imageView.setAnimation(animation);
				animation.start();
			}
			if(msg.what==Constants.LOGIN_SUCCESS){
				
			}*/
			if(msg.what==10){
				Intent intent=new Intent(FirstPageActivity.this,HomeActivity.class);
				FirstPageActivity.this.startActivity(intent);
				firstPageActivity=null;
				/*Runtime r=Runtime.getRuntime();
				LogUtil.i("me", "Runtime.maxMemory()="+r.maxMemory()+";Runtime.totalMemory()="+r.totalMemory()+";Runtime.freeMemory()="+r.freeMemory());*/
				if(PetApplication.petApp.activityList.contains(FirstPageActivity.this)){
					PetApplication.petApp.activityList.remove(FirstPageActivity.this);
				}
				/*begLayout1.setVisibility(View.INVISIBLE);
				begLayout1.setBackgroundDrawable(null);;
				if(PetApplication.petApp.blurBmp!=null){
					if(!PetApplication.petApp.blurBmp.isRecycled()){
						PetApplication.petApp.blurBmp.recycle();
					}
					PetApplication.petApp.blurBmp=null;
				}*/
				/*if(handleHttpConnectionException.handler!=null){
					handleHttpConnectionException.handler.removeCallbacks(null);
					handleHttpConnectionException.handler=null;
				}*/
				
				finish();
				System.gc();
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
		login();
		
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_first_page);
		welcomeImage=(ImageView)findViewById(R.id.imageView1);
		imageView=(ImageView)findViewById(R.id.imageView1);
		imageView11=(ImageView)findViewById(R.id.imageView11);
		begLayout1=(RelativeLayout)findViewById(R.id.beg_layout1);
		begLayout=(RelativeLayout)findViewById(R.id.beg_layout);
		begSure=(ImageView)findViewById(R.id.beg_sure);
		foodNum=(TextView)findViewById(R.id.food_num_tv);
		petNum=(TextView)findViewById(R.id.pet_num_tv);
//		scrollview=(ScrollView)findViewById(R.id.scrllview);
		firstPageActivity=this;
		foodNum.setText(""+Constants.Toatl_food);
		petNum.setText(""+Constants.Toatl_animal);
		
		
		begSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.i("mi", "点击确定。。。。。");
				handler.sendEmptyMessage(10);
				
			}
		});
		
		
		
		loadWelcomePage(true);
		
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
			
			
	}
	
	public void loadWelcomePage(final boolean loadPicture){
         new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String url=HttpUtil.downloadWelcomeImage(null,FirstPageActivity.this);
				/*try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						foodNum.setText(""+Constants.Toatl_food);
						petNum.setText(""+Constants.Toatl_animal);
						if(loadPicture){
							
						
						ImageLoader imageLoader=ImageLoader.getInstance();
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
						        .cacheOnDisc(false)
						        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
						        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
						        .decodingOptions(options)
				                .build();
						File f=new File(Constants.Picture_Root_Path+File.separator+"imageloader"+File.separator+"cache"+File.separator+"150654825");
						if(f.exists()){
							f.delete();
						}
						
						imageLoader.loadImage(Constants.WELCOME_IMAGE+/*url*/"home.jpg", /*imageView,*/ displayImageOptions,new ImageLoadingListener() {
							
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
								begLayout.setVisibility(View.VISIBLE);
								imageView.setVisibility(View.GONE);
							}
							
							@Override
							public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
								// TODO Auto-generated method stub
								
                                    handler.postDelayed(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										new Thread(new Runnable() {
											
											@Override
											public void run() {
												// TODO Auto-generated method stub
												while(PetApplication.petApp.blurBmp==null){
													try {
														Thread.sleep(50);
													} catch (InterruptedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}
												if(PetApplication.petApp.blurBmp!=null){
													/*Intent intent=new Intent(FirstPageActivity.this,NewHomeActivity.class);
													FirstPageActivity.this.startActivity(intent);
													FirstPageActivity.this.finish();*/
													runOnUiThread(new Runnable() {
														
														@Override
														public void run() {
															// TODO Auto-generated method stub
															begLayout1.setBackgroundDrawable(new BitmapDrawable(PetApplication.petApp.blurBmp));
															begLayout.setVisibility(View.VISIBLE);
															imageView.setImageDrawable(new BitmapDrawable());
															imageView.setVisibility(View.GONE);
															imageView11.setVisibility(View.GONE);
															System.gc();
															
														}
													});
													
												}
											}
										}).start();
										
									}
								},4000);
								if(PetApplication.petApp.blurBmp==null)
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										LogUtil.i("mi", "图片像素数："+loadedImage.getByteCount());
										Matrix matrix=new Matrix();
										matrix.setScale(0.1f, 0.1f);
										Bitmap  bmp=loadedImage.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);
										LogUtil.i("mi", "图片像素数："+bmp.getByteCount());
										PetApplication.petApp.blurBmp=Blur.fastblur(FirstPageActivity.this, bmp, 18);
								        if(bmp!=null&&!bmp.isRecycled()){
								        	bmp.recycle();
								        	bmp=null;
								        }
								        
									}
								}).start();
									
							}
							
							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								// TODO Auto-generated method stub
								LogUtil.i("me","下载欢迎图片  取消"+imageUri);
								begLayout.setVisibility(View.VISIBLE);
								imageView.setVisibility(View.GONE);
								imageView11.setVisibility(View.GONE);
							}
						});
					}
				}
				});
				
			
		}
	}).start();
	     
	}
	
	/**
	 * 
	 * 登陆
	 */
	public void login(){
		LogUtil.i("me", "执行登陆");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				SharedPreferences sPreferences=FirstPageActivity.this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
				
				String SID=sPreferences.getString("SID", null);
			   
				if(StringUtil.isEmpty(SID)){
					/*
					 * 防止用户频繁删除安装软件，将Sid清除，每次先判断
					 */
					getSIDAndUserID();
					
				}else{
					Constants.SID=SID;
					Constants.isSuccess=sPreferences.getBoolean("isRegister", false);
					if(StringUtil.isEmpty(Constants.realVersion)){
						Constants.realVersion=sPreferences.getString("real_version", "");
					}
					if(Constants.isSuccess){
						Constants.user=new MyUser();
						Constants.user.userId=sPreferences.getInt("usr_id", 0);
						Constants.user.u_nick=sPreferences.getString("name", "游荡的两脚兽");
						Constants.user.coinCount=sPreferences.getInt("gold", 500);
						Constants.user.lv=sPreferences.getInt("lv", 0);
						Constants.user.u_iconUrl=sPreferences.getString("url", "");
						Constants.user.city=sPreferences.getString("city", "");
						Constants.user.province=sPreferences.getString("province", "");
						Constants.user.locationCode=sPreferences.getInt("locationCode", 1000);
						Constants.user.u_gender=sPreferences.getInt("usr_gender", 1);
						Constants.user.currentAnimal=new Animal();
						Constants.user.rank=sPreferences.getString("job", "陌生人");
						Constants.user.rankCode=sPreferences.getInt("rankCode", -1);
						Constants.user.currentAnimal.a_id=sPreferences.getLong("a_id", 0);
						Constants.user.currentAnimal.race=sPreferences.getString("a_race", "");
						Constants.user.currentAnimal.a_age_str=sPreferences.getString("a_age_str", "");
						Constants.user.currentAnimal.pet_iconUrl=sPreferences.getString("a_url", "");
						Constants.user.currentAnimal.a_age=sPreferences.getInt("a_age", 0);
						Constants.user.currentAnimal.type=sPreferences.getInt("a_type", 101);
						Constants.user.currentAnimal.pet_nickName=sPreferences.getString("a_nick", "");
						Constants.user.currentAnimal.master_id=sPreferences.getInt("master_id", 0);
						
					}
					if(Constants.user==null||Constants.user.userId<=0||Constants.user.currentAnimal==null||Constants.user.currentAnimal.a_id<=0||Constants.user.coinCount<0||Constants.user.rankCode<0||Constants.user.lv<0||Constants.user.currentAnimal.master_id<=0){
						getSIDAndUserID();
					}else{
						if("-1".equals(Constants.user.rank)){
							getSIDAndUserID();
						}else{
							update();
							
							
							/*
							 * 更新用户信息
							 */
							getSIDAndUserID();
						}
					}
					
				}
				
			}
		}).start();
			
	}
	public void getSIDAndUserID(){
		String SID=HttpUtil.getSID(this,handleHttpConnectionException.getHandler(FirstPageActivity.this));
		if("repate".equals(SID)){
			Intent intent=new Intent(this,Dialog4Activity.class);
			
			Dialog4Activity.listener=new Dialog3ActivityListener() {
				
				@Override
				public void onClose() {
					// TODO Auto-generated method stub
					finish();
				}
				
				@Override
				public void onButtonTwo() {
					// TODO Auto-generated method stub
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Dialog4Activity.canClose=false;
							loadWelcomePage(false);
							getSIDAndUserID();
						}
					}).start();
				}
				
				@Override
				public void onButtonOne() {
					// TODO Auto-generated method stub
					finish();
				}
			};
			intent.putExtra("mode", 5);
			startActivity(intent);
			
			return;
		}
		SharedPreferences sPreferences=FirstPageActivity.this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		if(!StringUtil.isEmpty(SID)){
			Constants.SID=SID;
			update();
			
		}else{
			boolean flag=HttpUtil.login(this,handleHttpConnectionException.getHandler(this));
			if(flag){
				SID=HttpUtil.getSID(this,handleHttpConnectionException.getHandler(FirstPageActivity.this));
				update();
			}
			
		}
		
		//TODO 版本更新
		
		if(!StringUtil.isEmpty(Constants.CON_VERSION)&&!"1.0".equals(Constants.CON_VERSION)){
			
		}
			String version=StringUtil.getAPKVersionName(this);
			if(Constants.realVersion!=null&&StringUtil.canUpdate(this, Constants.realVersion)){
				Intent intent=new Intent(FirstPageActivity.this,UpdateAPKActivity.class);
				FirstPageActivity.this.startActivity(intent);
			}
		
		
	}
	/**
	 * 用户信息下载成功
	 */
	public void update(){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
//				if(MenuFragment.menuFragment!=null)MenuFragment.menuFragment.setViews();
			}
		});
	}
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		begLayout1.setBackgroundDrawable(null);;
		if(PetApplication.petApp.blurBmp!=null){
			if(!PetApplication.petApp.blurBmp.isRecycled()){
				PetApplication.petApp.blurBmp.recycle();
			}
			PetApplication.petApp.blurBmp=null;
		}
		System.gc();
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			
			finish();
			if(HomeActivity.homeActivity!=null){
				HomeActivity.homeActivity.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	


}
