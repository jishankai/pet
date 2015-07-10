package com.aidigame.hisun.imengstar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.blur.Blur;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.ui.Dialog4Activity;
import com.aidigame.hisun.imengstar.ui.Dialog4Activity.Dialog3ActivityListener;
import com.aidigame.hisun.imengstar.ui.HomeActivity;
import com.aidigame.hisun.imengstar.ui.UpdateAPKActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
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
			
			if(msg.what==10){
				Intent intent=new Intent(FirstPageActivity.this,HomeActivity.class);
				FirstPageActivity.this.startActivity(intent);
				firstPageActivity=null;
				
				
				
				
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
		loadGiftInfo();
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_first_page);
		welcomeImage=(ImageView)findViewById(R.id.imageView1);
		imageView=(ImageView)findViewById(R.id.imageView1);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=1;
//		imageView.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.start_image,options)));
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
		
		
			
			
	}
	
	public void loadWelcomePage(final boolean loadPicture){
         new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String url=HttpUtil.downloadWelcomeImage(null,FirstPageActivity.this);
				
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
										imageView.setImageBitmap(loadedImage);
									}
								}, 2000);
								
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
					PetApplication.SID=SID;
					PetApplication.isSuccess=sPreferences.getBoolean("isRegister", false);
					if(StringUtil.isEmpty(Constants.realVersion)){
						Constants.realVersion=sPreferences.getString("real_version", "");
					}
					if(PetApplication.isSuccess){
						PetApplication.myUser=new MyUser();
						PetApplication.myUser.userId=sPreferences.getLong("usr_id", 0);
						PetApplication.myUser.u_nick=sPreferences.getString("name", "游荡的两脚兽");
						PetApplication.myUser.coinCount=sPreferences.getInt("gold", 500);
						PetApplication.myUser.lv=sPreferences.getInt("lv", 0);
						PetApplication.myUser.u_iconUrl=sPreferences.getString("url", "");
						PetApplication.myUser.city=sPreferences.getString("city", "");
						PetApplication.myUser.province=sPreferences.getString("province", "");
						PetApplication.myUser.locationCode=sPreferences.getInt("locationCode", 1000);
						PetApplication.myUser.u_gender=sPreferences.getInt("usr_gender", 1);
						PetApplication.myUser.currentAnimal=new Animal();
						PetApplication.myUser.rank=sPreferences.getString("job", "陌生人");
						PetApplication.myUser.rankCode=sPreferences.getInt("rankCode", -1);
						PetApplication.myUser.currentAnimal.a_id=sPreferences.getLong("a_id", 0);
						PetApplication.myUser.currentAnimal.race=sPreferences.getString("a_race", "");
						PetApplication.myUser.currentAnimal.a_age_str=sPreferences.getString("a_age_str", "");
						PetApplication.myUser.currentAnimal.pet_iconUrl=sPreferences.getString("a_url", "");
						PetApplication.myUser.currentAnimal.a_age=sPreferences.getInt("a_age", 0);
						PetApplication.myUser.currentAnimal.type=sPreferences.getInt("a_type", 101);
						PetApplication.myUser.currentAnimal.pet_nickName=sPreferences.getString("a_nick", "");
						PetApplication.myUser.currentAnimal.master_id=sPreferences.getLong("master_id", 0);
						
					}
					if(PetApplication.myUser==null||PetApplication.myUser.userId<=0||PetApplication.myUser.currentAnimal==null||PetApplication.myUser.currentAnimal.a_id<=0||PetApplication.myUser.coinCount<0||PetApplication.myUser.rankCode<0||PetApplication.myUser.lv<0||PetApplication.myUser.currentAnimal.master_id<=0){
						getSIDAndUserID();
					}else{
						if("-1".equals(PetApplication.myUser.rank)){
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
			PetApplication.SID=SID;
			update();
			
		}else{
			boolean flag=HttpUtil.login(this,handleHttpConnectionException.getHandler(this));
			if(flag){
				SID=HttpUtil.getSID(this,handleHttpConnectionException.getHandler(FirstPageActivity.this));
				update();
			}
			
		}
		
		//TODO 版本更新
		
		
			String version=StringUtil.getAPKVersionName(this);
			if(Constants.VERSION!=null&&StringUtil.canUpdate(this, Constants.VERSION)){
				Intent intent=new Intent(FirstPageActivity.this,UpdateAPKActivity.class);
				FirstPageActivity.this.startActivity(intent);
			}
		
			if(sPreferences.getInt("p_mid_size", 0)>0){
				ImageLoader imageLoader=ImageLoader.getInstance();
				Editor editor=sPreferences.edit();
				int label=0;
				for(int i=0;i<sPreferences.getInt("p_mid_size", 0);i++){
					 label=sPreferences.getInt("p_mid"+i+"_label", 0);
					loadMidPic(imageLoader,sPreferences.getString("p_mid"+label+"_icon", ""),"p_mid"+label+"_icon_path","_icon_path");
					loadMidPic(imageLoader,sPreferences.getString("p_mid"+label+"_animate1", ""),"p_mid"+label+"_animate1_path","_animate1_path");
					loadMidPic(imageLoader,sPreferences.getString("p_mid"+label+"_animate2", ""),"p_mid"+label+"_animate2_path","_animate2_path");
					loadMidPic(imageLoader,sPreferences.getString("p_mid"+label+"_pic", ""),"p_mid"+label+"_pic_path","_pic_path");
				}
				editor.commit();
			}
	}
	private void loadMidPic(final ImageLoader imageLoader, final String string,
			final String name,final String pathName) {
		// TODO Auto-generated method stub
		if(new File(Constants.Picture_ICON_Path+File.separator+string+".png").exists()){
			return;
		}
		imageLoader.loadImage(Constants.PICTURE_TYPE_MENUS+string, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				File file=new File(Constants.Picture_ICON_Path);
				if(!file.exists()){
					file.mkdirs();
				}
				String pname=string+pathName+".png";
				OutputStream os;
				try {
					os = new FileOutputStream(new File(Constants.Picture_ICON_Path+File.separator+pname));
					loadedImage.compress(CompressFormat.PNG, 100, os);
					SharedPreferences sp=FirstPageActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
					Editor editor=sp.edit();
					editor.putString(name, Constants.Picture_ICON_Path+File.separator+pname);
					editor.commit();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
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
    private void loadGiftInfo() {
	// TODO Auto-generated method stub
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 SharedPreferences sp=FirstPageActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);

			    String code= sp.getString(Constants.GIFT_INFO_CODE, "");
				HttpUtil.loadGiftInfo(FirstPageActivity.this, handler,code);
			}
		}).start();
	
    }
	


}
