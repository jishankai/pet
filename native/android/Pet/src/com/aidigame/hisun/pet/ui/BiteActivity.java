package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.BallProgressView;
import com.aidigame.hisun.pet.view.BallProgressView.OnProgressStopListener;
import com.aidigame.hisun.pet.widget.AudioRecordAndPlayer;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.XinlangShare;
import com.buihha.audiorecorder.Mp3Recorder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * 叫一叫界面
 * @author admin
 *
 */
public class BiteActivity extends Activity {
	DisplayImageOptions displayImageOptions;//显示图片的格式
	ViewPager viewPager;
	View view1,view2,view3,view4,view5;
	HomeViewPagerAdapter adapter;
	ArrayList<View> viewList;
	AudioRecordAndPlayer audioRecordAndPlayer;
	LinearLayout progressLayout;
	Mp3Recorder mp3Recorder;
	HandleHttpConnectionException handleHttpConnectionException;
	Animal animal;
	ShowProgress showProgress;
	String voicePath;
	ImageView cloudIV1,cloudIV2;
	RelativeLayout shareBitmapLayout;
	String view4Note="期待明天的萌叫叫哦~";
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_bite);
		mp3Recorder=new Mp3Recorder();
		progressLayout=(LinearLayout)findViewById(R.id.progressLayout);
		animal=(Animal)getIntent().getSerializableExtra("animal");
		shareBitmapLayout=(RelativeLayout)findViewById(R.id.share_bitmap_layout);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
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
		audioRecordAndPlayer=new AudioRecordAndPlayer(this);
		
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ImageLoader imageLoader=ImageLoader.getInstance();
		ImageView icon=(ImageView)findViewById(R.id.roundImageView1);
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl,icon , displayImageOptions);
		TextView nameTv=(TextView)findViewById(R.id.textView4);
		findViewById(R.id.close_view).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BiteActivity.this.finish();
			}
		});;
		nameTv.setText(animal.pet_nickName);
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		view1=LayoutInflater.from(this).inflate(R.layout.item_bite_1, null);
		view2=LayoutInflater.from(this).inflate(R.layout.item_bite_2, null);
		view3=LayoutInflater.from(this).inflate(R.layout.item_bite_3, null);
		view4=LayoutInflater.from(this).inflate(R.layout.item_bite_4, null);
		view5=LayoutInflater.from(this).inflate(R.layout.item_bite_5, null);
		viewList=new ArrayList<View>();
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		viewList.add(view5);
		adapter=new HomeViewPagerAdapter(viewList);
		
		cloudIV1=(ImageView)view1.findViewById(R.id.imageView2);
		cloudIV2=(ImageView)view1.findViewById(R.id.imageView3);
		showProgress=new ShowProgress(this, progressLayout);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final boolean flag=HttpUtil.isVoicedApi(BiteActivity.this,animal, handleHttpConnectionException.getHandler(BiteActivity.this));
				if(flag){
					String pathString=null;
					pathString=HttpUtil.getVoiceUrl(BiteActivity.this,animal.a_id, handleHttpConnectionException.getHandler(BiteActivity.this));
					if(new File(Constants.Picture_Root_Path+File.separator+pathString+".mp3").exists()){
						pathString=Constants.Picture_Root_Path+File.separator+pathString+".mp3";
					}else{
						pathString=HttpUtil.downloadVoiceFile(pathString, handleHttpConnectionException.getHandler(BiteActivity.this));
					}
					if(!StringUtil.isEmpty(pathString)){
						voicePath=pathString;
						audioRecordAndPlayer.recordFileName=voicePath;
					}
				}
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								viewPager.setAdapter(adapter);
								if(!StringUtil.isEmpty(audioRecordAndPlayer.recordFileName)){
									view4Note="去看看其他萌星吧";
									share(view4);
									viewPager.setCurrentItem(3);
									
								}else{
									showProgress.progressCancel();
									initView1();
								}
								
								}
								
							
						});
				
				
			}
		}).start();
		viewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				shareBitmapLayout.setBackgroundResource(R.drawable.shake_background);
				switch (arg0) {
				case 0:
					initView1();
					break;
				case 1:
					initView2();
					break;
				case 2:
					initView3();
					break;
				case 3:
					shareBitmapLayout.setBackgroundResource(R.drawable.shake_background2);
					initView4();
					break;
				case 4:
					shareBitmapLayout.setBackgroundResource(R.drawable.shake_background2);
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
		
	}
	/**
	 * 初始化view1界面
	 */
	boolean islongClick=false;
	String timeStr;
	int recordTime;
	private void initView1() {
		// TODO Auto-generated method stub
		//进度条
		StringUtil.viewStartTransAnim(cloudIV1, 5000, 20, 20-Constants.screen_width);
		StringUtil.viewStartTransAnim(cloudIV2, 4800, -20, -20+Constants.screen_width);
		final BallProgressView ballProgressView=(BallProgressView)view1.findViewById(R.id.ballprogressview);
		ballProgressView.reset();
		recordTime=0;
		final ImageView pressIv=(ImageView)view1.findViewById(R.id.imageView1);
		final ImageView greenIv=(ImageView)view1.findViewById(R.id.green_iv);
		final RelativeLayout stopLayout=(RelativeLayout)view1.findViewById(R.id.stop_layout);
		final TextView timeTv=(TextView)view1.findViewById(R.id.textView3);
		
		stopLayout.setLongClickable(true);
		stopLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.i("scroll", "手势抬起。。。。。。");
				timeTv.setVisibility(View.INVISIBLE);
				pressIv.setVisibility(View.VISIBLE);
				ballProgressView.setStop(true);
				ballProgressView.reset();
				islongClick=false;
			}
		});
        stopLayout.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.i("scroll", "手势抬起。。。。。。");
				
				if(!audioRecordAndPlayer.isRecording){
					pressIv.setVisibility(View.INVISIBLE);
					timeTv.setVisibility(View.VISIBLE);
//					audioRecordAndPlayer.startRecord();
					try {
						mp3Recorder.startRecording();
						ballProgressView.setSpeed(30,BiteActivity.this);
						islongClick=true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				return true;
			}
		});
		stopLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					
					
					
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					if(!islongClick){
						/*LogUtil.i("scroll", "手势抬起。。。。。。");
						timeTv.setVisibility(View.INVISIBLE);
						pressIv.setVisibility(View.VISIBLE);
						ballProgressView.setStop(true);
						ballProgressView.reset();
						islongClick=false;*/
						
					}else {
						if(recordTime<2){
							timeTv.setVisibility(View.INVISIBLE);
							pressIv.setVisibility(View.VISIBLE);
							ballProgressView.setStop(true);
							ballProgressView.reset();
							islongClick=false;
							return true;
						}
						ballProgressView.setStop(true);
						viewPager.setCurrentItem(1,false);
//						audioRecordAndPlayer.stopRecord();
						try {
							mp3Recorder.stopRecording();
							audioRecordAndPlayer.recordFileName=mp3Recorder.fileName;
							islongClick=false;
							pressIv.setVisibility(View.VISIBLE);
							timeTv.setVisibility(View.INVISIBLE);
							ballProgressView.reset();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					break;
				}
				return false;
			}
		});
		ballProgressView.setOnProgressStopListener(new OnProgressStopListener() {
			String temp;
			@Override
			public void onStop() {
				// TODO Auto-generated method stub
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(audioRecordAndPlayer.isRecording&&audioRecordAndPlayer.mediaRecorder!=null){
							audioRecordAndPlayer.stopRecord();
							if(recordTime>25){
								viewPager.setCurrentItem(1);
								ballProgressView.reset();
								pressIv.setVisibility(View.VISIBLE);
								timeTv.setVisibility(View.INVISIBLE);
							}
						}
						
					}
				});
				
			}
			
			@Override
			public void onProgress(int time) {
				// TODO Auto-generated method stub
				int t=30-time;
				recordTime=time;
				timeStr="";
				if(time<10){
					timeStr="0:0"+time;
				}else if(time>=10){
					timeStr="0:"+time;
				}
				if(t<0){
					temp="0";
				}else if(t<10){
					temp="0:0"+t;
				}else{
					temp="0:"+t;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						timeTv.setText(temp);
					}
				});
				
			}
		});
		
	}
	/**
	 * 播放或上传声音
	 */
	int playState=0;//0,未播放或结束；1,正在播放;2,暂停
    private void initView2(){
    	final BallProgressView ballProgressView=(BallProgressView)view2.findViewById(R.id.ballprogressview);
    	ballProgressView.reset();
    	final ImageView controlIv=(ImageView)view2.findViewById(R.id.imageView4);
    	final TextView timeTV=(TextView)view2.findViewById(R.id.textView1);
    	ImageView reRecordIV=(ImageView)view2.findViewById(R.id.imageView2);
    	ImageView updateImageView=(ImageView)view2.findViewById(R.id.imageView3);
    	ImageView cloudIV1=(ImageView)view2.findViewById(R.id.cloud1);
    	ImageView cloudIV2=(ImageView)view2.findViewById(R.id.cloud2);
    	StringUtil.viewStartTransAnim(cloudIV1, 5000, 20, 20-Constants.screen_width);
		StringUtil.viewStartTransAnim(cloudIV2, 4800, -20, -20+Constants.screen_width);
    	reRecordIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(0,false);
			}
		});
    	updateImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(2,false);
			}
		});
    	timeTV.setText(timeStr+"/0:00");
    	controlIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (playState) {
				case 0:
					playState=1;
					//测试一下，播放刚才录制的声音
					timeTV.setText(timeStr+"/0:00");
					audioRecordAndPlayer.playAudio(audioRecordAndPlayer.recordFileName);
					controlIv.setImageResource(R.drawable.bite_pause);
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ballProgressView.setSpeed(recordTime,BiteActivity.this);
						}
					}, 100);
					
					break;
				case 1:
					playState=2;
					ballProgressView.setPause(true);
					audioRecordAndPlayer.pauseAudio();
					controlIv.setImageResource(R.drawable.bite_start);
					break;
				case 2:
					playState=1;
					ballProgressView.setPause(false);
					audioRecordAndPlayer.pauseAudio();
					controlIv.setImageResource(R.drawable.bite_pause);
					break;
				}
			}
		});
    	ballProgressView.setOnProgressStopListener(new OnProgressStopListener() {
			String temp;
			@Override
			public void onStop() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playState=0;
						controlIv.setImageResource(R.drawable.bite_start);
					}
				});
				
			}
			
			@Override
			public void onProgress(int time) {
				// TODO Auto-generated method stub
				temp="";
				if(time<0){
					temp="0";
				}else if(time<10){
					temp="0:0"+time;
				}else{
					temp="0:"+time;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						timeTV.setText(timeStr+"/"+temp);
					}
				});
			}
		});
    }
    int max;
    int progress;
    public void initView3(){
    	/*
    	 * 圆宽88像素  左边事391像素
    	 */
//    	final ImageView progressIv=(ImageView)view3.findViewById(R.id.progress);
//    	final ImageView backgroundIv=(ImageView)view3.findViewById(R.id.background_iv);
    	final TextView timeTV=(TextView)view3.findViewById(R.id.textView3);
    	timeTV.setText(timeStr);
    	ImageView cloudIV1=(ImageView)view3.findViewById(R.id.imageView2);
    	ImageView cloudIV2=(ImageView)view3.findViewById(R.id.imageView3);
    	StringUtil.viewStartTransAnim(cloudIV1, 5000, 20, 20-Constants.screen_width);
		StringUtil.viewStartTransAnim(cloudIV2, 4800, -20, -20+Constants.screen_width);
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				PetPicture petPicture=new PetPicture();
				petPicture.isVoice=true;
				petPicture.voicePath=audioRecordAndPlayer.recordFileName;
				petPicture.animal=animal;
				final PetPicture picture=HttpUtil.post(petPicture, BiteActivity.this);
				
				File file=new File(audioRecordAndPlayer.recordFileName);
				
//				max=(int)((backgroundIv.getMeasuredWidth())*(394f/478f));
//				while(/*progress<=max*/!petPicture.updateVoiceSuccess){
//					try {
//						Thread.sleep(50);
//						progress+=5;
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(picture.updateVoiceSuccess){
//									ViewGroup.LayoutParams params=progressIv.getLayoutParams();
//									params.width=progress;
//									progressIv.setLayoutParams(params);
									if(progress==max){
										viewPager.setCurrentItem(3,false);
										share(view4);
										Toast.makeText(BiteActivity.this, "上传音频成功", Toast.LENGTH_LONG).show();
									}
								}else{
									Toast.makeText(BiteActivity.this, "上传音频失败", Toast.LENGTH_LONG).show();
//									ViewGroup.LayoutParams params=progressIv.getLayoutParams();
//									params.width=0;
//									progress=0;
//									progressIv.setLayoutParams(params);
								}
								
								
							}
						});
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
					
					
//					if(progress>=max)progress=max;
//					progress=max;
//					runOnUiThread(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							if(picture.updateVoiceSuccess){
//								ViewGroup.LayoutParams params=progressIv.getLayoutParams();
//								params.width=progress;
//								progressIv.setLayoutParams(params);
//								if(progress==max){
//									viewPager.setCurrentItem(3,false);
//									share(view4);
//								}
//							}else{
//								ViewGroup.LayoutParams params=progressIv.getLayoutParams();
//								params.width=0;
//								progress=0;
//								progressIv.setLayoutParams(params);
//							}
//							
//							
//						}
//					});
				
			}
		}).start();
    	
    }

	public void initView4(){
		//上传成功，期待明天的萌叫叫哦~
		if(showProgress!=null)
		showProgress.progressCancel();
		TextView tv=(TextView)view4.findViewById(R.id.textView2);
		TextView tv2=(TextView)view4.findViewById(R.id.t2);
		if(view4Note.equals("去看看其他萌星吧")){
			tv.setText("今天"+animal.pet_nickName+"的萌叫叫已经录过了呢，");
			tv2.setText("去看看其他萌星吧");
		}else{
			tv.setText("上传成功，");
			tv2.setText("期待明天的萌叫叫哦~");
			
		}
		
		ImageView cloudIV1=(ImageView)view4.findViewById(R.id.cloud1);
		ImageView cloudIV2=(ImageView)view4.findViewById(R.id.cloud2);
		ImageView cloudIV3=(ImageView)view4.findViewById(R.id.cloud3);
		StringUtil.viewStartTransAnim(cloudIV1, 4600, 20, -20-Constants.screen_width);
		StringUtil.viewStartTransAnim(cloudIV2, 5000, -20, 20+Constants.screen_width);
		StringUtil.viewStartTransAnim(cloudIV3, 4800, 20, -20-Constants.screen_width);
		ImageView iv=(ImageView)view4.findViewById(R.id.imageView1);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				audioRecordAndPlayer.playAudio(audioRecordAndPlayer.recordFileName);
				
			}
		});
	}
    private void initView5() {
		// TODO Auto-generated method stub
		TextView tv=(TextView)view5.findViewById(R.id.textView2);
		String htmlStr1="<html>"
	             +"<body>"
				    +"今天"
	                +"<font color=\"#fb6137\">"
	                +animal.pet_nickName+" "
	                +"</font>"
	                +" 已经叫过了"
	             +"</body>"
	      + "</html>";
		tv.setText("今天 "+animal.pet_nickName+" 录过了哟");
		ImageView cloudIV1=(ImageView)view5.findViewById(R.id.cloud1);
		ImageView cloudIV2=(ImageView)view5.findViewById(R.id.cloud2);
		ImageView cloudIV3=(ImageView)view5.findViewById(R.id.cloud3);
		StringUtil.viewStartTransAnim(cloudIV1, 4600, 20, -20-Constants.screen_width);
		StringUtil.viewStartTransAnim(cloudIV2, 5000, -20, 20+Constants.screen_width);
		StringUtil.viewStartTransAnim(cloudIV3, 4800, 20, -20-Constants.screen_width);
		ImageView iv=(ImageView)view4.findViewById(R.id.imageView1);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				audioRecordAndPlayer.playAudio(audioRecordAndPlayer.recordFileName);
				
			}
		});
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
					boolean flag=WeixinShare.regToWeiXin(BiteActivity.this);
					if(!flag){
						Toast.makeText(BiteActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						
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
						boolean flag=WeixinShare.regToWeiXin(BiteActivity.this);
						if(!flag){
							Toast.makeText(BiteActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
							
							return;
						}
					}
					if(WeixinShare.shareBitmap(data, 1)){
//						Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(BiteActivity.this,"分享失败。", Toast.LENGTH_LONG).show();
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
					boolean flag=WeixinShare.regToWeiXin(BiteActivity.this);
					if(!flag){
						Toast.makeText(BiteActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						
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
//						Toast.makeText(BiteActivity.this,"成功分享到微信。", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(BiteActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
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
				if(!UserStatusUtil.hasXinlangAuth(BiteActivity.this)){
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
					data.des="我家萌宠“"+animal.pet_nickName+"”今天乖巧的冲我撒娇，来听听吧。http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
					if(UserStatusUtil.hasXinlangAuth(BiteActivity.this)){
						
						XinlangShare.sharePicture(data,BiteActivity.this);
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
    
}
