package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.util.ArrayList;

import android.R.anim;
import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.blur.Blur;
import com.aidigame.hisun.pet.blur.TopCenterImageView;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.Dialog4Activity.Dialog3ActivityListener;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.MyScrollView;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.view.MyScrollView.OnScrollListener;
import com.aidigame.hisun.pet.widget.ShowMore;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom;
import com.aidigame.hisun.pet.widget.fragment.DialogNote;
import com.aidigame.hisun.pet.widget.fragment.DialogQuitKingdom;
import com.aidigame.hisun.pet.widget.fragment.MyPetFragment;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom.ResultListener;
import com.easemob.chat.core.HeartBeatReceiver;
import com.miloisbadboy.view.PullToRefreshView;
import com.miloisbadboy.view.PullToRefreshView.OnFooterRefreshListener;
import com.miloisbadboy.view.PullToRefreshView.OnHeaderRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
/**
 * 宠物档案界面（王国或家族），展示宠物的所有相关信息
 * @author admin
 *
 */
public class NewPetKingdomActivity extends Activity implements OnClickListener{
	private  ImageView backIV,petSexIV,moreIv,supportIv,giftIv;
	private  TextView petNameTV,petRaceTV,modifyIv,giveHeartTv,
	         petAgeTV,userJobTV,petSignTv,trendsNumTv,fansNumTv,pictureNumTv,foodNumTv,likeNumTv,giftNumTv;
	private  EditText modifyEt;
	public View popupParent;
	public RelativeLayout black_layout;
	private  RoundImageView petIcon,userIcon;
	public static NewPetKingdomActivity petKingdomActivity;
	private  ShowProgress showProgress;
	private  LinearLayout bottomLinearLayout2,moreLayout,progresslayout,foodLayout,rqLayout,giftLayout
    ,begLayout,shakeLayout,sendGiftLayout,touchLayout;
	public   LinearLayout linearLayout2,blurLayout;
	
	private  LinearLayout camera_album;//显示获取照片界面

	
	private  FrameLayout frameLayout;
	public Bitmap   loadedImage1,loadedImage2;
	private  RelativeLayout moreParentLayout,trendsNumLayout,fansNumLayout,pictureNumLayout;
	/*
	 * 一张图片的所有信息，
	 * 根据用户id，判断是否为本人创建或加入的王国，还是其他人的王国，
	 * 两种情况下界面显示不同
	 */
	private  Animal data;
	private  DisplayImageOptions displayImageOptions;//显示图片的格式
	
	
	
	
	
	
	
	
	
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	UMSocialService mController;
	ImageView guideIv2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_new_petdossier);
		petKingdomActivity=this;
		data=(Animal)getIntent().getSerializableExtra("animal");
		MobclickAgent.onEvent(this, "pet_homenpage");
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		guideIv2=(ImageView)findViewById(R.id.guide2);
		
		
		
		initView();
		initListener();
		initModifySign();
	}
    boolean canOver;
	private void initModifySign() {
		// TODO Auto-generated method stub
		if(PetApplication.myUser!=null&&PetApplication.myUser.userId==data.master_id){
			modifyIv.setVisibility(View.VISIBLE);
		}else{
			modifyIv.setBackgroundResource(R.drawable.private_message);
		}
		modifyIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(PetApplication.myUser!=null&&PetApplication.myUser.userId==data.master_id){
				if(!canOver){
					modifyEt.setFocusable(true);
					modifyEt.setFocusableInTouchMode(true);
					modifyEt.setEnabled(true);
					modifyEt.requestFocus(EditText.FOCUS_FORWARD);
					modifyEt.setSelection(0);
					InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					
					if(modifyEt.getText().length()>0)
					modifyEt.setSelection(modifyEt.getText().length());
					canOver=true;
					modifyIv.setText("取消");
					modifyIv.setBackgroundDrawable(null);
				}else{
					if(StringUtil.isEmpty(modifyEt.getText().toString())){
						modifyEt.setText(data.announceStr);
						modifyIv.setText("");
						modifyIv.setBackgroundResource(R.drawable.modify);
						canOver=false;
						modifyEt.setEnabled(false);
						return;
					}
					modifyEt.setEnabled(false);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							final boolean flag=HttpUtil.modifyPetAnnounceInfo(handler,data, modifyEt.getText().toString(), NewPetKingdomActivity.this);
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(flag){
										data.announceStr=modifyEt.getText().toString();
										LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)modifyEt.getLayoutParams();
										if(param==null){
											param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

										}
										param.height=LinearLayout.LayoutParams.WRAP_CONTENT;
										param.width=LinearLayout.LayoutParams.WRAP_CONTENT;
										modifyEt.setLayoutParams(param);
									}else{
										modifyEt.setText(data.announceStr);
										Toast.makeText(NewPetKingdomActivity.this, "修改失败", Toast.LENGTH_LONG).show();
									}
									modifyIv.setText("");
									modifyIv.setBackgroundResource(R.drawable.modify);
									canOver=false;
									modifyEt.setEnabled(false);
									
								}
							});
						}
					}).start();
					
				}
			}else{
				if(com.aidigame.hisun.pet.huanxin.ChatActivity.activityInstance!=null){
					com.aidigame.hisun.pet.huanxin.ChatActivity.activityInstance.finish();
				}
				MyUser myUser=new MyUser();
				Intent intent2=new Intent(NewPetKingdomActivity.this,com.aidigame.hisun.pet.huanxin.ChatActivity.class);
				intent2.putExtra("chatType", com.aidigame.hisun.pet.huanxin.ChatActivity.CHATTYPE_SINGLE);
				myUser.userId=data.master_id;
				myUser.u_nick=data.u_name;
				myUser.u_iconUrl=data.u_tx;
				intent2.putExtra("user", myUser);
				startActivity(intent2);
			}
			}
		});
		modifyEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(s.length()>0){
//					canOver=true;
					if(canOver){
						modifyIv.setText("完成");
						modifyIv.setBackgroundDrawable(null);;
					}
					
				}else{
//					canOver=false;
					modifyIv.setText("取消");
					modifyIv.setBackgroundDrawable(null);;
				}
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		backIV=(ImageView)findViewById(R.id.back);
		petSexIV=(ImageView)findViewById(R.id.pet_sex_imageview);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		
		frameLayout=(FrameLayout)findViewById(R.id.framelayout1);
		
		camera_album=(LinearLayout)findViewById(R.id.camera_album);
		
		giveHeartTv=(TextView)findViewById(R.id.give_heart_iv);
		giftIv=(ImageView)findViewById(R.id.gift_iv);
		moreParentLayout=(RelativeLayout)findViewById(R.id.more_parent_latyout);
		
		
		moreIv=(ImageView)findViewById(R.id.join_kingdom_tv);
		supportIv=(ImageView)findViewById(R.id.pet_raise);
		modifyIv=(TextView)findViewById(R.id.modify_iv);
		modifyEt=(EditText)findViewById(R.id.pet_sign_et);
		petNameTV=(TextView)findViewById(R.id.pet_name_tv);
		petRaceTV=(TextView)findViewById(R.id.pet_race_tv);
		petAgeTV=(TextView)findViewById(R.id.pet_age_tv);
		userJobTV=(TextView)findViewById(R.id.user_job);
		petIcon=(RoundImageView)findViewById(R.id.imageView3);
		userIcon=(RoundImageView)findViewById(R.id.user_icon);
		linearLayout2=(LinearLayout)findViewById(R.id.linearLayout2);
		blurLayout=(LinearLayout)findViewById(R.id.framelayout);
		bottomLinearLayout2=(LinearLayout)findViewById(R.id.bottom_linearlayout2);
		
		moreLayout=(LinearLayout)findViewById(R.id.more_layout);
		progresslayout=(LinearLayout)findViewById(R.id.progress_layout);  
		
		
		foodNumTv=(TextView)findViewById(R.id.food_nums);
		likeNumTv=(TextView)findViewById(R.id.like_nums);
		giftNumTv=(TextView)findViewById(R.id.gift_nums);
		trendsNumLayout=(RelativeLayout)findViewById(R.id.trends_num_layout);
		fansNumLayout=(RelativeLayout)findViewById(R.id.fans_num_layout);
		pictureNumLayout=(RelativeLayout)findViewById(R.id.picture_num_layout);
		trendsNumTv=(TextView)findViewById(R.id.trends_num);
		fansNumTv=(TextView)findViewById(R.id.fans_num);
		pictureNumTv=(TextView)findViewById(R.id.picture_num);
		foodLayout=(LinearLayout)findViewById(R.id.food_layout);
		rqLayout=(LinearLayout)findViewById(R.id.rq_layout);
		giftLayout=(LinearLayout)findViewById(R.id.gift_layout);
		
		
		begLayout=(LinearLayout)findViewById(R.id.beg_layout);
		shakeLayout=(LinearLayout)findViewById(R.id.shake_layout);
		sendGiftLayout=(LinearLayout)findViewById(R.id.send_layout);
		touchLayout=(LinearLayout)findViewById(R.id.touch_layout);
		
		loadAnimal(data);
		initArc();
		
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		
	}
	public void initArc(){
	
	}
	public void loadAnimal(final Animal data){
		this.data=data;
		showProgress=new ShowProgress(this, progresslayout);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.animalInfo(NewPetKingdomActivity.this,data, handler);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						setPetInfo(data);
						initArc();
						showProgress.progressCancel();
					}
				});
			}
		}).start();
	}
	public void setPetInfo(final Animal data){
		/*
		 * 是否为自己创建的王国
		 */
		userJobTV.setText("经纪人");
		petNameTV.setText(data.pet_nickName);
		userIcon.setVisibility(View.VISIBLE);
		petSexIV.setVisibility(View.VISIBLE);
		if(data.tb_version==0){
			giveHeartTv.setText("献爱心");
			giftIv.setImageResource(R.drawable.claw_gift1);
		}else{
			giveHeartTv.setText("买周边");
			giftIv.setImageResource(R.drawable.buy_around);
		}
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(data)){
			supportIv.setImageResource(R.drawable.pet_raise_2);
		}else{
			supportIv.setImageResource(R.drawable.pet_raise_1);
		}
		if(PetApplication.myUser!=null&&data.master_id==PetApplication.myUser.userId){
		SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		Editor e=sp.edit();
		boolean guide7=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE7, true);
		if(guide7){
			guideIv2.setImageResource(R.drawable.guide7);
			guideIv2.setVisibility(View.VISIBLE);
			e.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE7, false);
			e.commit();
		}else{
			
			guideIv2.setVisibility(View.GONE);
			
		}
		}
		/*
		 * 宠物种族
		 */
		 petRaceTV.setText(data.race);
		 /*
		  * 宠物性别
		  */
		    if(data.a_gender==1){
				//公
				petSexIV.setImageResource(R.drawable.male1);
			}else if(data.a_gender==2){
				//母
				petSexIV.setImageResource(R.drawable.female1);
			}else{
				
			}
		petAgeTV.setText(""+data.a_age_str);
		fansNumTv.setText(""+data.fans);
		likeNumTv.setText("("+data.t_rq+")");
		trendsNumTv.setText(""+data.newsNum);
		pictureNumTv.setText(""+data.imagesNum);
		foodNumTv.setText("("+data.totalfoods+")");
		giftNumTv.setText("");
		if(StringUtil.isEmpty(data.announceStr)){
			modifyEt.setText(data.pet_nickName+"暂时沉默中~");
		}else{
			modifyEt.setText(""+data.announceStr);
		}
		
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=4;
		options.inPreferredConfig=Bitmap.Config.ARGB_8888;
		options.inPurgeable=true;
		options.inInputShareable=true;
		ImageLoader imageLoader=ImageLoader.getInstance();
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.ARGB_8888)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+data.pet_iconUrl, petIcon, displayImageOptions,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				data.pet_iconPath=StringUtil.compressEmotion(NewPetKingdomActivity.this, null);
			}
			
			public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
				// TODO Auto-generated method stub
				
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						/*添加毛玻璃效果
						 *首先要将Bitmap的Config转化为Config.ARGB_8888类型的 
						 */
						data.pet_iconPath=StringUtil.compressEmotion(NewPetKingdomActivity.this, loadedImage);
//						loadedImage=Bitmap.createBitmap(pixels, loadedImage.getWidth(), loadedImage.getHeight(), Config.ARGB_8888);
						LogUtil.i("mi", "图片像素数："+loadedImage.getByteCount());
						Matrix matrix=new Matrix();
						matrix.setScale(0.4f, 0.4f);
						Bitmap  bmp=loadedImage.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);
//						int[] pixels=new int[bmp.getWidth()*bmp.getHeight()];
//						bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
//						
//						
//						bmp=Bitmap.createBitmap(pixels, bmp.getWidth(), bmp.getHeight(), Config.ARGB_8888);
						
						LogUtil.i("mi", "图片像素数："+bmp.getByteCount());
						loadedImage2=loadedImage;
						loadedImage1=Blur.fastblur(NewPetKingdomActivity.this, bmp, 18);
				        if(bmp!=null&&!bmp.isRecycled()){
				        	bmp.recycle();
				        	bmp=null;
				        }
							/*Matrix matrix=new Matrix();
							float scale1=Constants.screen_width*1f/loadedImage1.getWidth();
							float scale2=linearLayout2.getMeasuredHeight()*1f/loadedImage1.getHeight();
							float scale=scale1>scale2?scale1:scale2;
							matrix.postScale(scale, scale);
							Bitmap temp=Bitmap.createBitmap(loadedImage1, 0, 0, loadedImage1.getWidth(), loadedImage1.getHeight(),matrix,true);
							temp=Bitmap.createBitmap(temp, (temp.getWidth()-linearLayout2.getMeasuredWidth())/2, (temp.getHeight()-blurLayout.getMeasuredHeight())/2, linearLayout2.getMeasuredWidth(), linearLayout2.getMeasuredHeight());
							final BitmapDrawable bitmapDrawable=new BitmapDrawable(temp);*/
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									final BitmapDrawable bitmapDrawable=new BitmapDrawable(loadedImage1);
									int height=Constants.screen_width/bitmapDrawable.getMinimumWidth()*bitmapDrawable.getMinimumHeight();
									if(bitmapDrawable!=null){
										LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)linearLayout2.getLayoutParams();
										if(lp==null){
										   lp=new LinearLayout.LayoutParams(Constants.screen_width,height);	
										}
										linearLayout2.setLayoutParams(lp);
										linearLayout2.setBackgroundDrawable(bitmapDrawable);
										linearLayout2.setAlpha(0.9342857f);
									}
									/*linearLayout2.setBackgroundDrawable(bitmapDrawable);
									linearLayout2.setAlpha(0.9342857f);*/
								}
							});
							
					}
				}).start();
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				data.pet_iconPath=StringUtil.compressEmotion(NewPetKingdomActivity.this, null);
			}
		});
		options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=16;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		DisplayImageOptions displayImageOptions2=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
//		ImageLoader imageLoader2=ImageLoader.getInstance();
//imageLoader2.loadImage(Constants.ANIMAL_DOWNLOAD_TX+data.pet_iconUrl,  displayImageOptions2,new ImageLoadingListener() {
//			
//			@Override
//			public void onLoadingStarted(String imageUri, View view) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onLoadingFailed(String imageUri, View view,
//					FailReason failReason) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
//				// TODO Auto-generated method stub
//				
//				
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						/*添加毛玻璃效果
//						 *首先要将Bitmap的Config转化为Config.ARGB_8888类型的 
//						 */
//						int[] pixels=new int[loadedImage.getWidth()*loadedImage.getHeight()];
//						loadedImage.getPixels(pixels, 0, loadedImage.getWidth(), 0, 0, loadedImage.getWidth(), loadedImage.getHeight());
////						loadedImage=Bitmap.createBitmap(pixels, loadedImage.getWidth(), loadedImage.getHeight(), Config.ARGB_8888);
//						LogUtil.i("mi", "图片像素数："+loadedImage.getByteCount());
//						Bitmap bmp=Bitmap.createBitmap(pixels, loadedImage.getWidth(), loadedImage.getHeight(), Config.ARGB_8888);
//						Matrix matrix=new Matrix();
//						matrix.setScale(0.5f, 0.5f);
//						bmp=bmp.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
//						LogUtil.i("mi", "图片像素数："+bmp.getByteCount());
//						loadedImage1=Blur.fastblur(PetKingdomActivity.this, bmp, 18);
//				        if(bmp!=null&&!bmp.isRecycled()){
//				        	bmp.recycle();
//				        	bmp=null;
//				        }
//							/*Matrix matrix=new Matrix();
//							float scale1=Constants.screen_width*1f/loadedImage1.getWidth();
//							float scale2=linearLayout2.getMeasuredHeight()*1f/loadedImage1.getHeight();
//							float scale=scale1>scale2?scale1:scale2;
//							matrix.postScale(scale, scale);
//							Bitmap temp=Bitmap.createBitmap(loadedImage1, 0, 0, loadedImage1.getWidth(), loadedImage1.getHeight(),matrix,true);
//							temp=Bitmap.createBitmap(temp, (temp.getWidth()-linearLayout2.getMeasuredWidth())/2, (temp.getHeight()-blurLayout.getMeasuredHeight())/2, linearLayout2.getMeasuredWidth(), linearLayout2.getMeasuredHeight());
//							final BitmapDrawable bitmapDrawable=new BitmapDrawable(temp);*/
//							runOnUiThread(new Runnable() {
//								
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									final BitmapDrawable bitmapDrawable=new BitmapDrawable(loadedImage1);
//									int height=Constants.screen_width/bitmapDrawable.getMinimumWidth()*bitmapDrawable.getMinimumHeight();
//									if(bitmapDrawable!=null){
//										LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)linearLayout2.getLayoutParams();
//										if(lp==null){
//										   lp=new LinearLayout.LayoutParams(Constants.screen_width,height);	
//										}
//										linearLayout2.setLayoutParams(lp);
//										linearLayout2.setBackgroundDrawable(bitmapDrawable);
//										linearLayout2.setAlpha(0.9342857f);
//									}
//									/*linearLayout2.setBackgroundDrawable(bitmapDrawable);
//									linearLayout2.setAlpha(0.9342857f);*/
//								}
//							});
//					}
//				}).start();
//				
//			}
//			
//			@Override
//			public void onLoadingCancelled(String imageUri, View view) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		userIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.user_icon));
BitmapFactory.Options options2=new BitmapFactory.Options();
options2.inJustDecodeBounds=false;
options2.inSampleSize=8;
options2.inPreferredConfig=Bitmap.Config.RGB_565;
options2.inPurgeable=true;
options2.inInputShareable=true;
DisplayImageOptions displayImageOptions3=new DisplayImageOptions
.Builder()
.showImageOnLoading(R.drawable.user_icon)
.showImageOnFail(R.drawable.user_icon)
.cacheInMemory(false)
.cacheOnDisc(true)
.bitmapConfig(Bitmap.Config.RGB_565)
.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
.decodingOptions(options2)
.build();
ImageLoader imageLoader3=ImageLoader.getInstance();
		imageLoader3.displayImage(Constants.USER_DOWNLOAD_TX+data.u_tx, userIcon, displayImageOptions3);
	}
	private void initListener() {
		// TODO Auto-generated method stub
		backIV.setOnClickListener(this);
		supportIv.setOnClickListener(this);
		userIcon.setOnClickListener(this);
		moreIv.setOnClickListener(this);
		petIcon.setOnClickListener(this);
		
		
		trendsNumLayout.setOnClickListener(this);
		fansNumLayout.setOnClickListener(this);
		pictureNumLayout.setOnClickListener(this);
		foodLayout.setOnClickListener(this);
		rqLayout.setOnClickListener(this);
		giftLayout.setOnClickListener(this);
		begLayout.setOnClickListener(this);
		shakeLayout.setOnClickListener(this);
		sendGiftLayout.setOnClickListener(this);
		touchLayout.setOnClickListener(this);
		guideIv2.setOnClickListener(this);
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.trends_num_layout:
			if(PetTrendsActivity.petTrendsActivity!=null){
				PetTrendsActivity.petTrendsActivity.finish();
				PetTrendsActivity.petTrendsActivity=null;
			}
			Intent intentTrends=new Intent(this,PetTrendsActivity.class);
			intentTrends.putExtra("animal", data);
			this.startActivity(intentTrends);
			break;
		case R.id.fans_num_layout:
			if(PetFansActivity.petFansActivity!=null){
				PetFansActivity.petFansActivity.finish();
				PetFansActivity.petFansActivity=null;
			}
			Intent intentFans=new Intent(this,PetFansActivity.class);
			intentFans.putExtra("animal", data);
			this.startActivity(intentFans);
			break;
		case R.id.picture_num_layout:
			if(PetPicturesActivity.petPictureActivity!=null){
				PetPicturesActivity.petPictureActivity.finish();
				PetPicturesActivity.petPictureActivity=null;
			}
			Intent pictrueIntent=new Intent(this,PetPicturesActivity.class);
			pictrueIntent.putExtra("animal", data);
			this.startActivity(pictrueIntent);
			break;
		case R.id.food_layout:
			if(BegPicturesActivity.begPicturesActivity!=null){
				BegPicturesActivity.begPicturesActivity.finish();
				BegPicturesActivity.begPicturesActivity=null;
			}
			Intent begIntent=new Intent(this,BegPicturesActivity.class);
			begIntent.putExtra("animal", data);
			this.startActivity(begIntent);
			break;
		case R.id.rq_layout:
			if(PopularRankListActivity.popularRankListActivity!=null){
				PopularRankListActivity.popularRankListActivity.finish();
				PopularRankListActivity.popularRankListActivity=null;
			}
			Intent rankIntent=new Intent(this,PopularRankListActivity.class);
			startActivity(rankIntent);
			break;
		case R.id.gift_layout:
			if(PetGiftActivity.petGiftActivity!=null){
				PetGiftActivity.petGiftActivity.finish();
				PetGiftActivity.petGiftActivity=null;
			}
			Intent giftIntent=new Intent(this,PetGiftActivity.class);
			giftIntent.putExtra("animal", data);
			this.startActivity(giftIntent);
			break;
		case R.id.beg_layout:
			clickItem4();
			break;
		case R.id.shake_layout:
			clickItem1();
			break;
		case R.id.send_layout:
			clickItem2();
			break;
		case R.id.touch_layout:
			clickItem3();
			break;
		case R.id.back:
			if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null){
				if(!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
					NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
					NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
				}
				NewPetKingdomActivity.petKingdomActivity.linearLayout2.setBackgroundDrawable(null);
				
			}
			if(loadedImage2!=null&&!loadedImage2.isRecycled()){
				loadedImage2.recycle();
				loadedImage2=null;
			}
				if(HomeActivity.homeActivity!=null){
					/*Intent intent=HomeActivity.homeActivity.getIntent();
					if(intent!=null){
						this.startActivity(intent);
						finish();
						return;
					}*/
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
				}else{
					Intent intent=new Intent(this,HomeActivity.class);
					this.startActivity(intent);
				}
				petKingdomActivity=null;
				if(PetApplication.petApp.activityList.contains(this)){
					PetApplication.petApp.activityList.remove(this);
				}
			this.finish();
			System.gc();
			break;
		case R.id.user_icon:
			Intent intent=new Intent(NewPetKingdomActivity.this,UserCardActivity.class);
			data.user=new MyUser();
			data.user.userId=data.master_id;
			data.user.u_iconUrl=data.u_tx;
			data.user.u_nick=data.u_name;
			intent.putExtra("user", data.user);
			
			NewPetKingdomActivity.this.startActivity(intent);
			
			break;
		case R.id.join_kingdom_tv:
//			getSharePetKingdomPicture();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
//					final boolean flag=HttpUtil.kingAndUserRelation(NewPetKingdomActivity.this,data, handleHttpConnectionException.getHandler(NewPetKingdomActivity.this));
					
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
//								if(!StringUtil.isEmpty(sharePath)){
//									if(flag){
										new ShowMore(moreLayout, NewPetKingdomActivity.this,sharePath,moreParentLayout).kindomShowMore(data);
//									}else{
//										new ShowMore(moreLayout, NewPetKingdomActivity.this,sharePath,moreParentLayout).kindomShowMore(data)/*.onlyCanShare()*/;
//									}
									
//								}
								
							}
						});
				}
			}).start();
			
			break;
		case R.id.imageView3:
			if(PetApplication.myUser!=null&&PetApplication.myUser.userId==data.master_id){
				Intent intent6=new Intent(this,ModifyPetInfoActivity.class);
				intent6.putExtra("animal", data);
				intent6.putExtra("mode", 1);
				this.startActivity(intent6);
			}else{
				Intent intent6=new Intent(this,ShowPictureActivity.class);
				intent6.putExtra("mode", 1);
				intent6.putExtra("url", data.pet_iconUrl);
				this.startActivity(intent6);
			}
			
			break;
		case R.id.modify_iv:
			
			break;
		case R.id.pet_raise:
			if(!UserStatusUtil.isLoginSuccess(this,popupParent,black_layout)){
				return;
		    }
			if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(data)){
//				Toast.makeText(this, "您已经捧TA", Toast.LENGTH_LONG).show();
				
				
				
				
				cancelPengTA();
				
				
				return;
			}
			pengTA();
			break;
		case R.id.guide2:
			guideIv2.setImageDrawable(new BitmapDrawable());
			guideIv2.setVisibility(View.GONE);
			break;
			
		}
	}
	private void cancelPengTA() {
		// TODO Auto-generated method stub
		if(PetApplication.myUser!=null&&PetApplication.myUser.userId==data.master_id){
//			Toast.makeText(this, "不要抛弃自己家的萌星呀~", Toast.LENGTH_LONG).show();
			Intent intent=new Intent(this,DialogNoteActivity.class);
			intent.putExtra("mode", 10);
			intent.putExtra("info", "不要抛弃自己家的萌星呀~");
			startActivity(intent);
			return;
		}
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.size()==1){
//			Toast.makeText(this, "不能不捧萌星，现在只剩一个啦", Toast.LENGTH_LONG).show();
			Intent intent=new Intent(this,DialogNoteActivity.class);
			intent.putExtra("mode", 10);
			intent.putExtra("info", "不能不捧萌星，现在只剩一个啦");
			startActivity(intent);
			return;
		}
		isPenging=true;
		DialogQuitKingdom dialog=new DialogQuitKingdom(popupParent, this, black_layout, data);
		dialog.setResultListener(new DialogQuitKingdom.ResultListener() {
			
			@Override
			public void getResult(boolean isSuccess) {
				// TODO Auto-generated method stub
				
				if(isSuccess){
					supportIv.setImageResource(R.drawable.pet_raise_1);
					data.hasJoinOrCreate=false;
					
				}
				isPenging=false;
			}
		});
	}
	boolean isPenging=false;
	public void pengTA(){
		if(isPenging){
			Toast.makeText(this, "数据处理中，请稍后", Toast.LENGTH_LONG).show();
			return;
		}
		isPenging=true;
		int num=0;
		int count=0;
		for(int i=0;i<PetApplication.myUser.aniList.size();i++){
//			if(Constants.user.aniList.get(i).master_id!=Constants.user.userId)
				count++;
		}
		
		
		if(count>=10&&count<=20){
			num=(count)*5;
		}else if(count>20){
			num=100;
		}
		
		if(PetApplication.myUser.coinCount<num){
//			DialogNote dialog=new DialogNote(popupParent, this, black_layout, 1);
			/*Intent intent=new Intent(this,DialogNoteActivity.class);
			intent.putExtra("mode", 10);
			intent.putExtra("info", "钱包君告急！挣够金币再来捧萌星吧");
			startActivity(intent);*/
			
			
			
			 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
					
					@Override
					public void onClose() {
						// TODO Auto-generated method stub
						isPenging=false;
					}
					
					@Override
					public void onButtonTwo() {
						// TODO Auto-generated method stub
						Intent intent=new Intent(NewPetKingdomActivity.this,ChargeActivity.class);
						startActivity(intent);
						isPenging=false;
					}
					
					@Override
					public void onButtonOne() {
						// TODO Auto-generated method stub
						isPenging=false;
					}
				};
				 Intent intent=new Intent(this,Dialog4Activity.class);
				 intent.putExtra("mode", 8);
				 intent.putExtra("num", num);
				 startActivity(intent);
				 isPenging=false;
				 return ;
	}
	
	
	DialogJoinKingdom dialog=new DialogJoinKingdom(popupParent, this, black_layout, data);
	dialog.setResultListener(new ResultListener() {
		
		@Override
		public void getResult(boolean isSuccess) {
			// TODO Auto-generated method stub
			if(isSuccess){
				supportIv.setImageResource(R.drawable.pet_raise_2);
			}
			
			data.hasJoinOrCreate=isSuccess;
			isPenging=false;
		}
	});
	}
	public void judgeScrollVieHeight(){
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		petKingdomActivity=null;
		
	}
	
	boolean isChangingUserIcon;
	int getPictureMode=1;//1 从相机中获取，2从图库中获取
	
	/**
	 * 改变宠物头像 
	 */
	private void changeUserIcon() {
		// TODO Auto-generated method stub
		isChangingUserIcon=true;
		/*Bitmap bmp=ImageUtil.getBitmapFromView(allLayout,UserHomepageActivity.this);
		blur_view.setImageBitmap(bmp);
		blur_view.setAlpha(0.85f);//0.9342857
		blur_view.setVisibility(View.VISIBLE);*/
		final View view=LayoutInflater.from(this).inflate(R.layout.popup_user_homepage, null);
		bottomLinearLayout2.removeAllViews();
		bottomLinearLayout2.addView(view);
		bottomLinearLayout2.setVisibility(View.VISIBLE);
		bottomLinearLayout2.setClickable(true);
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
		view.clearAnimation();
		view.setAnimation(animation);
		animation.start();
		
		TextView camera=(TextView)view.findViewById(R.id.textView1);
		TextView album=(TextView)view.findViewById(R.id.textView2);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPictureMode=1;
				Intent intent2=new Intent(NewPetKingdomActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
				NewPetKingdomActivity.this.startActivityForResult(intent2, 1);
				
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
				bottomLinearLayout2.setClickable(false);
//				blur_view.setVisibility(View.INVISIBLE);
				isChangingUserIcon=false;
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPictureMode=2;
				Intent intent=new Intent(NewPetKingdomActivity.this,AlbumPictureBackground.class);
				intent.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
				NewPetKingdomActivity.this.startActivityForResult(intent, 1);
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
				bottomLinearLayout2.setClickable(false);
//				blur_view.setVisibility(View.INVISIBLE);
				isChangingUserIcon=false;
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation=AnimationUtils.loadAnimation(NewPetKingdomActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						bottomLinearLayout2.setVisibility(View.INVISIBLE);
						bottomLinearLayout2.setClickable(false);
//						blur_view.setVisibility(View.INVISIBLE);
						isChangingUserIcon=false;
					}
				}, 1000);
				
			}
		});
	
	}
	private void publishPicture(){
		/*Bitmap bmp=ImageUtil.getBitmapFromView(homeRelativeLayout,this);
		blur_view.setImageBitmap(bmp);
		blur_view.setAlpha(0.9342857f);//0.9342857
		blur_view.setVisibility(View.VISIBLE);*/
		isChangingUserIcon=true;
		long l1=System.currentTimeMillis();
//		blur_view.setImageBitmap(ImageUtil.fastblur(bmp, 12));
		LogUtil.i("scroll",""+( System.currentTimeMillis()-l1));
		final View view=LayoutInflater.from(this).inflate(R.layout.popup_user_homepage, null);
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
		view.clearAnimation();
		view.setAnimation(animation);
		animation.start();
		
		bottomLinearLayout2.removeAllViews();
		bottomLinearLayout2.addView(view);
		bottomLinearLayout2.setVisibility(View.VISIBLE);
		bottomLinearLayout2.setClickable(true);
		TextView camera=(TextView)view.findViewById(R.id.textView1);
		TextView album=(TextView)view.findViewById(R.id.textView2);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(NewPetKingdomActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				NewPetKingdomActivity.this.startActivity(intent2);
				/*Animation animation=AnimationUtils.loadAnimation(HomeActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {*/
						// TODO Auto-generated method stub
						bottomLinearLayout2.setVisibility(View.INVISIBLE);
						bottomLinearLayout2.setClickable(false);
//						blur_view.setVisibility(View.INVISIBLE);
						isChangingUserIcon=false;
					/*}
				}, 1000);*/
				
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(NewPetKingdomActivity.this,AlbumPictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				NewPetKingdomActivity.this.startActivity(intent2);
				/*Animation animation=AnimationUtils.loadAnimation(HomeActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {*/
						// TODO Auto-generated method stub
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
				bottomLinearLayout2.setClickable(false);
//						blur_view.setVisibility(View.INVISIBLE);
						isChangingUserIcon=false;
					/*}
				}, 1000);*/
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation=AnimationUtils.loadAnimation(NewPetKingdomActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						bottomLinearLayout2.setVisibility(View.INVISIBLE);
						bottomLinearLayout2.setClickable(false);
//						blur_view.setVisibility(View.INVISIBLE);
						isChangingUserIcon=false;
					}
				}, 1000);
				
			}
		});
	}
	String path;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/**使用SSO授权必须添加如下代码 */
		if(requestCode!=1&&requestCode!=2){
			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	        if(ssoHandler != null){
	           ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	        }
		}
        
		switch (resultCode) {
		case RESULT_OK:
			if(requestCode==1){
				path=data.getStringExtra("path");
				if(path!=null){
					Intent intent=new Intent(this,com.aviary.android.feather.FeatherActivity.class);
					intent.setData(Uri.parse("file://"+path));
					intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
					startActivityForResult(intent, 2); 
				}
			}else if(requestCode==2){
				/*if(data.getBooleanExtra("cancel", false)){
					if(getPictureMode==1){
						Intent intent2=new Intent(UserHomepageActivity.this,TakePictureBackground.class);
						intent2.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
						UserHomepageActivity.this.startActivityForResult(intent2, 1);
					}else{
						Intent intent=new Intent(UserHomepageActivity.this,AlbumPictureBackground.class);
						intent.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
						UserHomepageActivity.this.startActivityForResult(intent, 1);
					}
				}*/
				if(data.getData()!=null)
				loadBitmap(data.getData());
			}
			

			
			break;

		default:
			break;
		}
	}
	public void loadBitmap(Uri uri){
		LogUtil.i("me", "uri="+uri.toString()+",path="+path);
		Cursor cursor=getContentResolver().query(uri, null, null, null, null);
		if(cursor!=null){
			cursor.moveToFirst();
			final String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=4;
			options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(uri.toString(), petIcon,displayImageOptions);
			bottomLinearLayout2.setVisibility(View.INVISIBLE);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String nameString=HttpUtil.uploadUserIcon(path,NewPetKingdomActivity.this,1);
					if(nameString==null){
						
					}else{
						/*if(new File(Constants.Picture_ICON_Path+File.separator+nameString).exists()){
							Constants.user.iconPath=Constants.Picture_ICON_Path+File.separator+nameString;
							
						}*/
						if(new File(path).exists()){
							PetApplication.myUser.pet_iconPath=path;
							
						}
					}
				}
			}).start();
			cursor.close();
		}
		
		
	}	
	public void clickItem1() {
		// TODO Auto-generated method stub
		if(!UserStatusUtil.isLoginSuccess(NewPetKingdomActivity.this,popupParent,black_layout)){
//		    setBlurImageBackground();
			return ;
		}
//		if(Constants.user!=null&&Constants.user.aniList!=null){
//			if(Constants.user.aniList.contains(data)){
				Intent intent=new Intent(this,ShakeActivity.class);
				intent.putExtra("animal", data);
				intent.putExtra("mode", 1);
				this.startActivity(intent);
		/*	}else{
				
			}
		}*/
		
	}
	public void clickItem2() {
		// TODO Auto-generated method stub
		if(!UserStatusUtil.isLoginSuccess(NewPetKingdomActivity.this,popupParent,black_layout)){
//		    setBlurImageBackground();
			return ;
		}
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
			
			
			if(data.tb_version==0/*||StringUtil.isEmpty(data.tburl)*/){
				sendGift();
				return;
			}
			if(data.tb_version==1&&!StringUtil.isEmpty(data.tburl)){
				//买周边
				Intent it=new Intent(this,ChargeActivity.class);
				it.putExtra("animal", data);
				it.putExtra("mode", 2);
				startActivity(it);
				return;
			}
			
			
			Intent it=new Intent(this,Dialog4Activity.class);
			it.putExtra("animal", data);
			it.putExtra("mode", 7);
			Dialog4Activity.listener=new Dialog3ActivityListener() {
				
				@Override
				public void onClose() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onButtonTwo() {
					// TODO Auto-generated method stub
					sendGift();
				}
				
				@Override
				public void onButtonOne() {
					// TODO Auto-generated method stub
					new ShowMore(moreLayout, NewPetKingdomActivity.this,sharePath,moreParentLayout).kindomShowMore(data);
				}
			};
			this.startActivity(it);
			
			
			
			
			
			
			
			
			
			
		}
		
	}
	public void sendGift(){
		/*DialogGiveSbGift dgb=new DialogGiveSbGift(this,data);
		final AlertDialog dialog=new AlertDialog.Builder(this).setView(dgb.getView())
				.show();*/
		if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.finish();
		Intent intent=new Intent(this,DialogGiveSbGiftActivity1.class);
		intent.putExtra("animal", data);
		this.startActivity(intent);
		DialogGiveSbGiftActivity1 dgb=DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity;
		DialogGiveSbGiftActivity1.dialogGoListener=new DialogGiveSbGiftActivity1.DialogGoListener() {
			
			@Override
			public void toDo() {
				// TODO Auto-generated method stub
            Intent intent=intent=new Intent(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity,MarketActivity.class);
				
				DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.startActivity(intent);
			}
			
			@Override
			public void closeDialog() {
				// TODO Auto-generated method stub
				if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)
				DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.finish();
				
			}
			@Override
			public void lastResult(boolean isSuccess) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void unRegister() {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	
	
	public void clickItem3() {
		// TODO Auto-generated method stub
		if(!UserStatusUtil.isLoginSuccess(NewPetKingdomActivity.this,popupParent,black_layout)){
//		    setBlurImageBackground();
			return ;
		}
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
			if(PetApplication.myUser.aniList.contains(data)){
				/*if(data.master_id==Constants.user.userId){
				}else{*/
					Intent intent=new Intent(this,TouchActivity.class);
					intent.putExtra("animal", data);
					this.startActivity(intent);
//				}
			}else{
				Intent intent=new Intent(this,TouchActivity.class);
				intent.putExtra("animal", data);
				this.startActivity(intent);
			}
		}
		
	}
	public void clickItem4() {
		// TODO Auto-generated method stub
		if(!UserStatusUtil.isLoginSuccess(NewPetKingdomActivity.this,popupParent,black_layout)){
//		    setBlurImageBackground();
			return ;
		}
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
//			if(Constants.user.aniList.contains(data)){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final PetPicture pp=HttpUtil.shareFoodApi(handler, data, NewPetKingdomActivity.this);
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(pp==null){
									if(PetApplication.myUser.userId==data.master_id){
											showCameraAlbum(data,true);
									}else{
										Toast.makeText(NewPetKingdomActivity.this, "萌星 "+data.pet_nickName+"，今天还没挣口粮呢~", Toast.LENGTH_LONG).show();
									}
								}else{
									long time=pp.create_time+24*3600-System.currentTimeMillis()/1000;
						    			if(time<=0){
						    				Toast.makeText(NewPetKingdomActivity.this, "萌星 "+data.pet_nickName+"，今天还没挣口粮呢~", Toast.LENGTH_LONG).show();
						    				return;
						    			}
									Intent intent6=new Intent(NewPetKingdomActivity.this,Dialog6Activity.class);
									intent6.putExtra("picture", pp);
									startActivity(intent6);
								}
							}
						});
						
						
					}
				}).start();
//			}else{
////				Toast.makeText(context, "不是自己的萌宠", 1000).show();
//			}
		}
	}
	boolean isShowingCameraAlbum=false;
	public void showCameraAlbum(final Animal animal,final boolean isBeg) {
		// TODO Auto-generated method stub
		isShowingCameraAlbum=true;
		long l1=System.currentTimeMillis();
		LogUtil.i("scroll",""+( System.currentTimeMillis()-l1));
		final View view=LayoutInflater.from(this).inflate(R.layout.popup_user_homepage, null);
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
		view.clearAnimation();
		view.setAnimation(animation);
		animation.start();
		
		camera_album.removeAllViews();
		camera_album.addView(view);
		camera_album.setVisibility(View.VISIBLE);
		camera_album.setClickable(true);
		TextView camera=(TextView)view.findViewById(R.id.textView1);
		TextView album=(TextView)view.findViewById(R.id.textView2);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(NewPetKingdomActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				if(animal!=null){
					intent2.putExtra("animal", animal);
					intent2.putExtra("isBeg", isBeg);
				}else{
					intent2.putExtra("animal", PetApplication.myUser.currentAnimal);
					intent2.putExtra("isBeg", isBeg);
				}
				
				startActivity(intent2);
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
						isShowingCameraAlbum=false;
				
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(NewPetKingdomActivity.this,AlbumPictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				
				if(animal!=null){
					intent2.putExtra("animal", animal);
					intent2.putExtra("isBeg", isBeg);
				}else{
					intent2.putExtra("animal", PetApplication.myUser.currentAnimal);
					intent2.putExtra("isBeg", isBeg);
				}
				startActivity(intent2);
				
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
						isShowingCameraAlbum=false;
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation=AnimationUtils.loadAnimation(NewPetKingdomActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
						isShowingCameraAlbum=false;
					}
				}, 300);
				
			}
		});
	}


	/**
	 * 分享 王国资料截图
	 */
	String sharePath;

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
       
	}
	public void setPeoplesNum(int count,boolean isSuccess){
//		peopleNumTV.setText("成员 "+count);
		if(isSuccess){
			
		}
		initArc();
		
	}
	public void fansNum(int count,boolean isSuccess){
//		fansNumTv.setText("粉丝  "+count);
		if(isSuccess){
			
		}
		
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
