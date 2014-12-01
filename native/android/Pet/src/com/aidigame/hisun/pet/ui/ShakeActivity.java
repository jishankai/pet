package com.aidigame.hisun.pet.ui;

import it.sephiroth.android.library.widget.AbsHListView.PositionScroller;

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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
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
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.aidigame.hisun.pet.view.RoundImageView;
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
      View view1,view22,view33,view4,view5;
      HomeViewPagerAdapter adapter;
      ArrayList<View> viewList;
      ShakeSensor shakeSensor;
//      Vibrator vibrator;
      int currentPosition=0;
      int optortunity=0;
      TextView titleTv;
      ImageView cloudIV1,cloudIV2;
      User user;
      Animal animal;
      Gift gift;//当前摇出的礼物
      int mode;//1，摇一摇；2，捣捣乱;
      RelativeLayout shareBitmapLayout;
      
      TextView nameTv,hasChancTv,chanceDesTv;
      LinearLayout nameTvLayout,noteLayout;
      public static ShakeActivity shakeActivity;
      
      View shine_view;
      Handler handleHttpConnectionException;
      boolean isSending=false;
      ArrayList<Gift> giftList;
      Animation anim;
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
    	shakeActivity=this;
    	progressLayout=(RelativeLayout)findViewById(R.id.porgress_layout);
    	
    	giftList=StringUtil.getGiftList(this);
    	animal=(Animal)getIntent().getSerializableExtra("animal");
    	mode=getIntent().getIntExtra("mode", 1);
    	MobclickAgent.onEvent(this, "shake_button");
    	handleHttpConnectionException=HandleHttpConnectionException.getInstance().getHandler(this);
    	shareBitmapLayout=(RelativeLayout)findViewById(R.id.share_bitmap_layout);
    	shine_view=findViewById(R.id.shine_view);
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
		shine_view.setVisibility(View.GONE);
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		ImageLoader imageLoader=ImageLoader.getInstance();
		ImageView icon=(ImageView)findViewById(R.id.roundImageView1);
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl,icon , displayImageOptions);
		nameTv=(TextView)findViewById(R.id.help_animalname);
		titleTv=(TextView)findViewById(R.id.textView5);
		hasChancTv=(TextView)findViewById(R.id.has_chance_tv);
		chanceDesTv=(TextView)findViewById(R.id.chance_des_tv);
		nameTvLayout=(LinearLayout)findViewById(R.id.help_layout);
		noteLayout=(LinearLayout)findViewById(R.id.shake_note_layout);
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
		view1=LayoutInflater.from(this).inflate(R.layout.item_shake_1, null);
		view22=LayoutInflater.from(this).inflate(R.layout.item_shake_22, null);
		view33=LayoutInflater.from(this).inflate(R.layout.item_shake_33, null);//
		view4=LayoutInflater.from(this).inflate(R.layout.item_shake_4, null);
		view5=LayoutInflater.from(this).inflate(R.layout.item_shake_5, null);
		
		viewList=new ArrayList<View>();
		viewList.add(view1);
//		viewList.add(view2);
//		viewList.add(view3);
		viewList.add(view22);
		viewList.add(view33);
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
					shine_view.setVisibility(View.GONE);
					break;
				case 1:
					
					break;
				case 2:
					initView33();
					break;
				case 3:
					initView4();
					break;
				case 4:
					shine_view.setVisibility(View.GONE);
					shareBitmapLayout.setBackgroundResource(R.drawable.shake_background);
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
			  int count= HttpUtil.shakeApi(ShakeActivity.this,animal.a_id, handleHttpConnectionException,0);
			  if(count!=-1){
				  optortunity=count;
				  runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						hasChancTv.setText("还有"+optortunity+"次机会");
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
//		vibrator=(Vibrator)getApplication().getSystemService(Context.VIBRATOR_SERVICE);
		shakeSensor=new ShakeSensor(this);
		shakeSensor.setOnShakeListener(new OnShakeLisener() {
			
			@Override
			public void onShake() {
				// TODO Auto-generated method stub
				isSending=true;
				shakeSensor.stop();
				//TODO 1.打开震动和声音；
				startVibrator();
				/* handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {}
					}, 2500);*/
				
				
			}
		});
	}

	private void startVibrator() {
		// TODO Auto-generated method stub
		
		MediaPlayer player=MediaPlayer.create(this, R.raw.rocking);
		player.setLooping(false);
		
		shine_view.setVisibility(View.GONE);
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				/*MediaPlayer player=MediaPlayer.create(ShakeActivity.this, R.raw.rocked);
				player.setLooping(false);
				player.start();*/
				
				getGift();
				/*player.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						
					}
				});*/
			}
		});
		player.start();
		
		
		
		//定义震动
		//第一个参数 节奏 ；第二个参数  重复次数（-1，不重复）
//		vibrator.vibrate(new long[]{500,200,500,200}, -1);
	}
	/**
	 * 小界面2，用户获得奖品
	 */
	public void initView22(){
		
		if(anim!=null)anim.cancel();
		shine_view.clearAnimation();
		
		shine_view.setVisibility(View.GONE);
		nameTvLayout.setVisibility(View.GONE);
		noteLayout.setVisibility(View.VISIBLE);
		hasChancTv.setText("还剩"+optortunity+"次机会");
		shareBitmapLayout.setBackgroundResource(R.drawable.shake_ground_get);
		TextView awardNameTv=(TextView)view22.findViewById(R.id.textView23);
		ImageView awardIv=(ImageView)view22.findViewById(R.id.imageView2);
		TextView addHotTV=(TextView)view22.findViewById(R.id.textView9);
		addHotTV.setText("人气+"+gift.add_rq);
		TextView sureTv=(TextView)view22.findViewById(R.id.sure_shake);
		TextView cancelTv=(TextView)view22.findViewById(R.id.cancel_shake);
		gift.is_shake=true;
		gift.aid=animal.a_id;
		awardNameTv.setText(gift.name+" X 1");
		try {
			awardIv.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sureTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//送礼物
						final User user=HttpUtil.sendGift(ShakeActivity.this,gift, handleHttpConnectionException);
						isSending=false;
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(user!=null){
									viewPager.setCurrentItem(2);
									optortunity=0;
									if(NewHomeActivity.homeActivity!=null){
										LogUtil.i("mi", "还剩"+0+"次机会");
										NewHomeActivity.homeActivity.homeFragment.homeMyPet.adapter.updateTV("还剩"+optortunity+"次",gift.add_rq);
									}
								}else{
									Toast.makeText(ShakeActivity.this, "网络连接出错", Toast.LENGTH_LONG).show();
								}
							}
						});
					}
				}).start();
			}
		});
		cancelTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(optortunity==0){
					viewPager.setCurrentItem(4);
				}else{
					viewPager.setCurrentItem(0);
					initVibrator();
				}
				
			}
		});
		view22.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				/*SoundPool soundPool=new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
				int id=soundPool.load(ShakeActivity.this, R.raw.rocked, 1);
				soundPool.play(id, 1, 1, 0, 0, 1);*/
				MediaPlayer player=MediaPlayer.create(ShakeActivity.this, R.raw.rocked);
				player.setLooping(false);
				player.start();
			}
		});
		
	}
	public void initView33(){
		nameTvLayout.setVisibility(View.GONE);
		noteLayout.setVisibility(View.VISIBLE);
		hasChancTv.setText("每天只能送1个礼物哦~");
		chanceDesTv.setText("每日第一次成功分享后可以多送1个礼物");
		shareBitmapLayout.setBackgroundResource(R.drawable.shake_ground_get2);
		
		shine_view.setVisibility(View.VISIBLE);
		anim=AnimationUtils.loadAnimation(this, R.anim.anim_rotate_repeat);
		anim.setInterpolator(new LinearInterpolator());
		shine_view.setAnimation(anim);
		anim.start();
		
		TextView giftAnimalNameTv=(TextView)view33.findViewById(R.id.textView2);
		TextView desTv=(TextView)view33.findViewById(R.id.des_tv);
		TextView addRqTv=(TextView)view33.findViewById(R.id.textView23);
		RoundImageView animalIv=(RoundImageView)view33.findViewById(R.id.imageView2);
		TextView sureTv=(TextView)view33.findViewById(R.id.textView9);
		sureTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		giftAnimalNameTv.setText(animal.pet_nickName+"收到了您送的"+gift.name);
		desTv.setText(gift.effect_des);
		addRqTv.setText("人气+"+gift.add_rq);
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl, animalIv, displayImageOptions);
		
		share(view33);
	}
    public void initView4(){
    	shine_view.setVisibility(View.GONE);
	ImageView cloudIV1=(ImageView)view4.findViewById(R.id.cloud1);
	ImageView cloudIV2=(ImageView)view4.findViewById(R.id.cloud2);
	ImageView cloudIV3=(ImageView)view4.findViewById(R.id.cloud3);
	StringUtil.viewStartTransAnim(cloudIV1, 4600, 20, -20-Constants.screen_width);
	StringUtil.viewStartTransAnim(cloudIV2, 5000, -20, 20+Constants.screen_width);
	StringUtil.viewStartTransAnim(cloudIV3, 4800, 20, -20-Constants.screen_width);
	}
    public void initView5(){
    	shine_view.setVisibility(View.GONE);
    	/*猫君*/
    	nameTvLayout.setVisibility(View.GONE);
		noteLayout.setVisibility(View.VISIBLE);
		hasChancTv.setText("还剩"+optortunity+"次机会");
		hasChancTv.setVisibility(View.GONE);
		chanceDesTv.setText("每日第一次成功分享后可以多送1个礼物");
    	TextView tView=(TextView)view5.findViewById(R.id.textView23);
    	tView.setText(animal.pet_nickName+"今天的摇一摇次数用完啦~");
    	ImageView cloudIV1=(ImageView)view5.findViewById(R.id.cloud1);
    	share(view5);
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
					Constants.shareMode=0;
					Constants.whereShare=4;
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
					Constants.shareMode=1;
					Constants.whereShare=4;
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
					Constants.whereShare=4;
					if(gift!=null){
						data.des="随便一摇就摇出了一个"+gift.name+"，好惊喜，你也想试试吗？http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
					}else{
						data.des="随便一摇就摇会有"+"惊喜，快来试试吧？http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
					}
					
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
	      
	      public void getGift(){

				// TODO Auto-generated method stub
				
				
				
				if(optortunity<=0){
//					vibrator.cancel();
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
				optortunity--;
				currentPosition=index;
				
				
				
             new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						/*while(isSending){
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}*/
						final int count= HttpUtil.shakeApi(ShakeActivity.this,animal.a_id, handleHttpConnectionException,1);
						
						
						//TODO 2.打开震动和声音；
							/*vibrator.cancel();
						 shakeSensor.start();*/
						 runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(count!=-1){
									
									switch(currentPosition){
									case 1:
										optortunity=count;
										viewPager.setCurrentItem(currentPosition);
										if(gift!=null){
											initView22();
										}
										if(NewHomeActivity.homeActivity!=null){
											LogUtil.i("mi", "还剩"+optortunity+"次机会");
											NewHomeActivity.homeActivity.homeFragment.homeMyPet.adapter.updateTV("还剩"+optortunity+"次",0);
										}
										break;

									case 2:
										
										break;

									case 3:
										
										break;

									case 4:
										initView5();
										break;
										
									}
								}
								
								
								MobclickAgent.onEvent(ShakeActivity.this, "shake_suc");
								LogUtil.i("mi", "还剩"+optortunity+"次机会");
								
							}
						});
					}
				}).start();
				
			
	      }
	      
	    public void shareNumChange(){
	    	new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final int count=HttpUtil.shakeShareNum(ShakeActivity.this, animal.a_id, handleHttpConnectionException);
					if(count==3){
						optortunity=3;
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								shine_view.setVisibility(View.GONE);
								shine_view.clearAnimation();
								if(anim!=null)anim.cancel();
								viewPager.setCurrentItem(0);
								initVibrator();
								if(NewHomeActivity.homeActivity!=null){
									LogUtil.i("mi", "还剩"+optortunity+"次机会");
									NewHomeActivity.homeActivity.homeFragment.homeMyPet.adapter.updateTV("还剩"+optortunity+"次",0);
								}
							}
						});
					}
				}
			}).start();
	    }
}
