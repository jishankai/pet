package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.ShakeSensor;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.XinlangShare;
import com.aidigame.hisun.pet.widget.ShakeSensor.OnShakeLisener;
import com.aviary.android.feather.library.utils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
/**
 * 摇一摇
 * @author admin
 *
 */
public class ShakeActivity extends Activity {
	DisplayImageOptions displayImageOptions;//显示图片的格式
	RelativeLayout progressLayout;
      ViewPager viewPager;
      View view1,view2,view3,view4,view5;
      HomeViewPagerAdapter adapter;
      ArrayList<View> viewList;
      ShakeSensor shakeSensor;
      Vibrator vibrator;
      int currentPosition=0;
      int optortunity=0;
      TextView optNumTv,titleTv;
      ImageView cloudIV1,cloudIV2;
      User user;
      Animal animal;
      Gift gift;//当前摇出的礼物
      int mode;//1，摇一摇；2，捣捣乱;
      RelativeLayout shareBitmapLayout;
      HandleHttpConnectionException handleHttpConnectionException;
      boolean isSending=false;
      ArrayList<Gift> giftList;
      Handler handler=new Handler(){
    	  public void handleMessage(android.os.Message msg) {
    		  
    	  };
      };
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	UiUtil.setScreenInfo(this);
    	UiUtil.setWidthAndHeight(this);
    	setContentView(R.layout.activity_shake);
    	progressLayout=(RelativeLayout)findViewById(R.id.porgress_layout);
    	giftList=StringUtil.getGiftList(this);
    	animal=(Animal)getIntent().getSerializableExtra("animal");
    	mode=getIntent().getIntExtra("mode", 1);
    	MobclickAgent.onEvent(this, "shake_button");
    	handleHttpConnectionException=HandleHttpConnectionException.getInstance();
    	shareBitmapLayout=(RelativeLayout)findViewById(R.id.share_bitmap_layout);
    	BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
    	initView();
    }

	private void initView() {
		// TODO Auto-generated method stub
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		ImageLoader imageLoader=ImageLoader.getInstance();
		ImageView icon=(ImageView)findViewById(R.id.roundImageView1);
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl,icon , displayImageOptions);
		TextView nameTv=(TextView)findViewById(R.id.textView4);
		titleTv=(TextView)findViewById(R.id.textView5);
		
		if(mode==1){
			titleTv.setText("摇一摇");
		}else{
//			titleTv.setText("捣捣乱");
		}
		
		findViewById(R.id.close_view).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(shakeSensor!=null)
				shakeSensor.stop();
				
				ShakeActivity.this.finish();
			}
		});;
		nameTv.setText(animal.pet_nickName);
		optNumTv=(TextView)findViewById(R.id.textView7);
		optNumTv.setText("");
		view1=LayoutInflater.from(this).inflate(R.layout.item_shake_1, null);
		view2=LayoutInflater.from(this).inflate(R.layout.item_shake_2, null);
		view3=LayoutInflater.from(this).inflate(R.layout.item_shake_3, null);
		view4=LayoutInflater.from(this).inflate(R.layout.item_shake_4, null);
		view5=LayoutInflater.from(this).inflate(R.layout.item_shake_5, null);
		
		viewList=new ArrayList<View>();
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		viewList.add(view5);
		adapter=new HomeViewPagerAdapter(viewList);
		cloudIV1=(ImageView)view1.findViewById(R.id.imageView2);
		cloudIV2=(ImageView)view1.findViewById(R.id.imageView3);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				shareBitmapLayout.setBackgroundResource(R.drawable.shake_background2);
				switch (arg0) {
				case 0:
					StringUtil.viewStartTransAnim(cloudIV1, 5000, 20, 20-Constants.screen_width);
					StringUtil.viewStartTransAnim(cloudIV2, 4800, -20, -20+Constants.screen_width);
					shareBitmapLayout.setBackgroundResource(R.drawable.shake_background);
					break;
				case 1:
					
					break;
				case 2:
					
					break;
				case 3:
					initView4();
					break;
				case 4:
					initView5();
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		StringUtil.viewStartTransAnim(cloudIV1, 5000, 20, 20-Constants.screen_width);
		StringUtil.viewStartTransAnim(cloudIV2, 4800, -20, -20+Constants.screen_width);
		
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			  int count= HttpUtil.shakeApi(ShakeActivity.this,animal.a_id, handleHttpConnectionException.getHandler(ShakeActivity.this));
			  if(count!=-1){
				  optortunity=count;
				  runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						optNumTv.setText(""+optortunity);
						viewPager.setAdapter(adapter);
                        if(optortunity==0){
							viewPager.setCurrentItem(4);
						}else{
							initVibrator();
						}
                        
					}
				});
			  }
			}
		}).start();
		
		
		/*
		 * 禁止ViewPager滑动
		 */
		viewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		
		
	}
	public void initVibrator(){
		vibrator=(Vibrator)getApplication().getSystemService(Context.VIBRATOR_SERVICE);
		shakeSensor=new ShakeSensor(this);
		shakeSensor.setOnShakeListener(new OnShakeLisener() {
			
			@Override
			public void onShake() {
				// TODO Auto-generated method stub
				isSending=true;
				shakeSensor.stop();
				//TODO 1.打开震动和声音；
				startVibrator();
				 handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(optortunity<=0){
								vibrator.cancel();
								optNumTv.setText("0");
								currentPosition=4;
								viewPager.setCurrentItem(4);
								optortunity=0;
								return;
							}
							gift=null;
							Random random=new Random();
							int r=random.nextInt(1000)+1;
							int index=0;
							ArrayList<Integer> intList=null;
							if(r>=1&&r<=800){
								index=1;
								intList=new ArrayList<Integer>();
								for(int i=0;i<giftList.size();i++){
									
										if(giftList.get(i).level==1&&giftList.get(i).add_rq>0){
											intList.add(i);
										}
									
									
								}
								int r1=random.nextInt(intList.size());
								gift=giftList.get(intList.get(r1));
							}else if(r>=801&&r<=900){
								index=1;
								intList=new ArrayList<Integer>();
								for(int i=0;i<giftList.size();i++){
									
										if(giftList.get(i).level==2&&giftList.get(i).add_rq>0){
											intList.add(i);
										}
									
								}
								int r1=random.nextInt(intList.size());
								gift=giftList.get(intList.get(r1));
							}else if(r>=901&&r<=970){
								index=1;
								intList=new ArrayList<Integer>();
								for(int i=0;i<giftList.size();i++){
                                   
                                    	if(giftList.get(i).level==3&&giftList.get(i).add_rq>0){
    										intList.add(i);
    									}
									
									
								}
								int r1=random.nextInt(intList.size());
								gift=giftList.get(intList.get(r1));
							}else if(r>=971&&r<=1000){
								index=1;
								intList=new ArrayList<Integer>();
								for(int i=0;i<giftList.size();i++){
                                   
                                    	if(giftList.get(i).level==4&&giftList.get(i).add_rq>0){
    										intList.add(i);
    									}
									
									
								}
								int r1=random.nextInt(intList.size());
								gift=giftList.get(intList.get(r1));
							}
							
							optNumTv.setText(""+optortunity);
							currentPosition=index;
							
							viewPager.setCurrentItem(index);
							switch(index){
							case 1:
								if(gift!=null)
								initView2(gift);
								break;

							case 2:
								
								break;

							case 3:
								
								break;

							case 4:
								initView5();
								break;
								
							}
							
                           new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									while(isSending){
										try {
											Thread.sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									int count= HttpUtil.shakeApi(ShakeActivity.this,animal.a_id, handleHttpConnectionException.getHandler(ShakeActivity.this));
									 optortunity=count;
									//TODO 2.打开震动和声音；
										vibrator.cancel();
									 shakeSensor.start();
									 runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											optNumTv.setText(""+optortunity);
											MobclickAgent.onEvent(ShakeActivity.this, "shake_suc");
											LogUtil.i("mi", "还剩"+optortunity+"次机会");
											if(NewHomeActivity.homeActivity!=null){
												LogUtil.i("mi", "还剩"+optortunity+"次机会");
												NewHomeActivity.homeActivity.homeFragment.homeMyPet.adapter.updateTV("还剩"+optortunity+"次",gift.add_rq);
											}
										}
									});
								}
							}).start();
							
						}
					}, 2000);
				
				
			}
		});
	}

	private void startVibrator() {
		// TODO Auto-generated method stub
		MediaPlayer player=MediaPlayer.create(this, R.raw.rocking);
		player.setLooping(false);
		player.start();
		//定义震动
		//第一个参数 节奏 ；第二个参数  重复次数（-1，不重复）
		vibrator.vibrate(new long[]{500,200,500,200}, -1);
	}
	/**
	 * 小界面2，用户获得奖品
	 */
	public void initView2(final Gift gift){
		TextView awardNameTv=(TextView)view2.findViewById(R.id.textView23);
		ImageView awardIv=(ImageView)view2.findViewById(R.id.imageView2);
		TextView addHotTV=(TextView)view2.findViewById(R.id.textView9);
		addHotTV.setText(""+animal.pet_nickName+"人气+"+gift.add_rq);
		gift.is_shake=true;
		gift.aid=animal.a_id;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//送礼物
				User user=HttpUtil.sendGift(ShakeActivity.this,gift, handleHttpConnectionException.getHandler(ShakeActivity.this));
				
				isSending=false;
			}
		}).start();
		awardNameTv.setText(gift.name);
		try {
			awardIv.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		share(view2);
		
	}
	public void initView3(){
		
	}
    public void initView4(){
	ImageView cloudIV1=(ImageView)view4.findViewById(R.id.cloud1);
	ImageView cloudIV2=(ImageView)view4.findViewById(R.id.cloud2);
	ImageView cloudIV3=(ImageView)view4.findViewById(R.id.cloud3);
	StringUtil.viewStartTransAnim(cloudIV1, 4600, 20, -20-Constants.screen_width);
	StringUtil.viewStartTransAnim(cloudIV2, 5000, -20, 20+Constants.screen_width);
	StringUtil.viewStartTransAnim(cloudIV3, 4800, 20, -20-Constants.screen_width);
	}
    public void initView5(){
    	/*猫君*/
    	TextView tView=(TextView)view5.findViewById(R.id.textView23);
    	tView.setText(animal.pet_nickName+"今天的摇一摇次数用完啦~");
    	ImageView cloudIV1=(ImageView)view5.findViewById(R.id.cloud1);
    	ImageView cloudIV2=(ImageView)view5.findViewById(R.id.cloud2);
    	ImageView cloudIV3=(ImageView)view5.findViewById(R.id.cloud3);
    	StringUtil.viewStartTransAnim(cloudIV1, 4600, 20, -20-Constants.screen_width);
    	StringUtil.viewStartTransAnim(cloudIV2, 5000, -20, 20+Constants.screen_width);
    	StringUtil.viewStartTransAnim(cloudIV3, 4800, 20, -20-Constants.screen_width);
	}
	public void share(View view){
		ImageView weixinIV=(ImageView)view.findViewById(R.id.imageView3);
		ImageView friendIV=(ImageView)view.findViewById(R.id.imageView4);
		ImageView xinlangIV=(ImageView)view.findViewById(R.id.imageView5);
		weixinIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.api==null){
					boolean flag=WeixinShare.regToWeiXin(ShakeActivity.this);
					if(!flag){
						Toast.makeText(ShakeActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();

						return;
					}
				}
				Bitmap bmp=ImageUtil.getImageFromView(shareBitmapLayout);
				String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".png";
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(path);
					bmp.compress(CompressFormat.PNG, 100, fos);
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					if(Constants.api==null){
						boolean flag=WeixinShare.regToWeiXin(ShakeActivity.this);
						if(!flag){
							Toast.makeText(ShakeActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
							
							return;
						}
					}
					if(WeixinShare.shareBitmap(data, 1)){
//						Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(ShakeActivity.this,"分享失败。", Toast.LENGTH_LONG).show();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(fos!=null){
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		});
		friendIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.api==null){
					boolean flag=WeixinShare.regToWeiXin(ShakeActivity.this);
					if(!flag){
						Toast.makeText(ShakeActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						
						return;
					}
				}
				Bitmap bmp=ImageUtil.getImageFromView(shareBitmapLayout);
				String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".png";
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(path);
					bmp.compress(CompressFormat.PNG, 100, fos);
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					if(WeixinShare.shareBitmap(data, 2)){
//						Toast.makeText(ShakeActivity.this,"成功分享到微信。", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(ShakeActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(fos!=null){
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		});
		xinlangIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.hasXinlangAuth(ShakeActivity.this)){
					return;
				}
				Bitmap bmp=ImageUtil.getImageFromView(shareBitmapLayout);
				String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".png";
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(path);
					bmp.compress(CompressFormat.PNG, 100, fos);
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					data.des="随便一摇就摇出了一个"+gift.name+"，好惊喜，你也想试试吗？http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
					if(UserStatusUtil.hasXinlangAuth(ShakeActivity.this)){
						
						XinlangShare.sharePicture(data,ShakeActivity.this);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(fos!=null){
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		});
	}
	 @Override
	    protected void onDestroy() {
	    	// TODO Auto-generated method stub
	    	if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.kingdomTrends.onRefresh(null);
	    	if(shakeSensor!=null)
	    	shakeSensor.stop();
	    	if(NewHomeActivity.homeActivity!=null){
				LogUtil.i("mi", "还剩"+optortunity+"次机会");
				NewHomeActivity.homeActivity.homeFragment.homeMyPet.adapter.tv=null;
				NewHomeActivity.homeActivity.homeFragment.homeMyPet.adapter.contriTV=null;
			}
	    	super.onDestroy();
	    }
	   @Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }
}
