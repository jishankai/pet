package com.aidigame.hisun.pet.ui;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.blur.Blur;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.ShowMore;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.fragment.DialogExpGoldConAdd;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
import com.aidigame.hisun.pet.widget.fragment.UserActivity;
import com.aidigame.hisun.pet.widget.fragment.UserFocus;
import com.aidigame.hisun.pet.widget.fragment.UserGiftList;
import com.aidigame.hisun.pet.widget.fragment.UserKingdomList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 用户个人信息界面，
 * @author admin
 *
 */
public class UserDossierActivity extends Activity implements OnClickListener{
	public View popupParent;
	public RelativeLayout black_layout;
	ImageView backIV,moreIv,changeIconIV,kingdomsIV,
	           starsIV,activityIV,boxesIV,userSexIV;
	TextView titleTV,userLevelTV,userNameTV,provinceTV,
	         cityTV,kingdomNameTV,userJobTV,experienceProgressTV,coinTV;
	View hatLineView,starLineView,activityLineView,boxLineView,experenceProgressView,line1,line2;
	RoundImageView userIcon,kingdomIcon;
	RelativeLayout moreParentLayout,expLayout;
	FrameLayout framelayout;
	ShowProgress showProgress;
	HandleHttpConnectionException handleHttpConnectionException;
	public static UserDossierActivity userDossierActivity;
	LinearLayout bottomLinearLayout2,moreLayout,animLayout,noteLayout;
	public LinearLayout linearLayout2,linearLayout3,listParentLayout,blurLayout,progresslayout;
	public View topWhiteView,infoShadowView;
	public boolean isShowInfoLayout=true;
	UserKingdomList userKingdomList;
	public UserFocus userFocus;
	UserGiftList userGiftList;
	UserActivity userActivity;
	public Bitmap   loadedImage1;
	/*
	 * 一张图片的所有信息，
	 * 根据用户id，判断是否为本人创建或加入的王国，还是其他人的王国，
	 * 两种情况下界面显示不同
	 */
//	UserImagesJson.Data data;
	User user;
	
	DisplayImageOptions displayImageOptions;//显示图片的格式
	
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
		setContentView(R.layout.activity_user_dossier);
		user=(User)getIntent().getSerializableExtra("user");
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		userDossierActivity=this;
		BitmapFactory.Options options=new BitmapFactory.Options();
		
		options.inJustDecodeBounds=false;
		options.inSampleSize=4;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
		        .cacheInMemory(false)
		        .cacheOnDisc(false)
		        .showImageOnLoading(R.drawable.user_icon)
			.showImageOnFail(R.drawable.user_icon)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		initView();
		showProgress=new ShowProgress(this, progresslayout);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final User u=HttpUtil.info(UserDossierActivity.this, handleHttpConnectionException.getHandler(UserDossierActivity.this), user.userId);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showProgress.progressCancel();
						if(u!=null){
							UserDossierActivity.this.user=u;
							setUserInfo(UserDossierActivity.this.user);
						}
					}
				});
			}
		}).start();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		backIV=(ImageView)findViewById(R.id.back);
		moreIv=(ImageView)findViewById(R.id.take_picture_imageview);
		changeIconIV=(ImageView)findViewById(R.id.change_icon_imageview);
		kingdomsIV=(ImageView)findViewById(R.id.track_imageview);
		activityIV=(ImageView)findViewById(R.id.peoples_imageview);
		boxesIV=(ImageView)findViewById(R.id.gift_imageview);
		userSexIV=(ImageView)findViewById(R.id.pet_sex_imageview);
		starsIV=(ImageView)findViewById(R.id.image_list_imageview);
		activityLineView=(View)findViewById(R.id.peoples_line_view);
		hatLineView=(View)findViewById(R.id.track_line_view);
		starLineView=(View)findViewById(R.id.image_line_view);
		boxLineView=(View)findViewById(R.id.gift_line_view);
		coinTV=(TextView)findViewById(R.id.coin_count_tv);
		moreParentLayout=(RelativeLayout)findViewById(R.id.more_parent_latyout);
		expLayout=(RelativeLayout)findViewById(R.id.progress_view_layout);
		framelayout=(FrameLayout)findViewById(R.id.framelayout1);
		experenceProgressView=findViewById(R.id.progress_view);
		titleTV=(TextView)findViewById(R.id.title_tv);
		userLevelTV=(TextView)findViewById(R.id.add_focus_tv);
		userNameTV=(TextView)findViewById(R.id.pet_name_tv);
		provinceTV=(TextView)findViewById(R.id.pet_race_tv);
		cityTV=(TextView)findViewById(R.id.pet_age_tv);
		line1=(TextView)findViewById(R.id.line1);
		line2=(TextView)findViewById(R.id.line2);
		kingdomNameTV=(TextView)findViewById(R.id.user_job);
		userJobTV=(TextView)findViewById(R.id.user_name);
		experienceProgressTV=(TextView)findViewById(R.id.progress_value_tv);
		userIcon=(RoundImageView)findViewById(R.id.imageView3);
		userIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.user_icon));
		kingdomIcon=(RoundImageView)findViewById(R.id.user_icon);
		listParentLayout=(LinearLayout)findViewById(R.id.show_list_layout);
		
		bottomLinearLayout2=(LinearLayout)findViewById(R.id.bottom_linearlayout2);
		noteLayout=(LinearLayout)findViewById(R.id.note_layout);
		linearLayout2=(LinearLayout)findViewById(R.id.linearLayout2);
		blurLayout=(LinearLayout)findViewById(R.id.framelayout);
		animLayout=(LinearLayout)findViewById(R.id.anim_layout);
		topWhiteView=(View)findViewById(R.id.top_white_view);
		infoShadowView=(View)findViewById(R.id.frame_view2);
		linearLayout3=(LinearLayout)findViewById(R.id.linearLayout3);
		
		progresslayout=(LinearLayout)findViewById(R.id.progress_layout);
		
		moreLayout=(LinearLayout)findViewById(R.id.more_layout);
		
		
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		showKingdomPeoples();
		showUserFocus();
		showKingdomGift();
		showKingdoms();
		
	}
	boolean hasMeasured=false;
	public void setUserInfo(final User user){
		hasMeasured=false;
		line1.setVisibility(View.GONE);
		line2.setVisibility(View.VISIBLE);
		kingdomIcon.setVisibility(View.VISIBLE);
		userSexIV.setVisibility(View.VISIBLE);
		if(user!=null){
			if(Constants.user!=null&&user.userId==Constants.user.userId){
				titleTV.setText("我的档案");
			}else{
				titleTV.setText(/*user.u_nick+*/"TA的档案");
			}
			coinTV.setText(""+user.coinCount);
			userNameTV.setText(user.u_nick);
			ViewTreeObserver viewTreeObserver=expLayout.getViewTreeObserver();
			viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
				
				@Override
				public boolean onPreDraw() {
					// TODO Auto-generated method stub
					if(!hasMeasured){
						hasMeasured=true;
						int width=expLayout.getMeasuredWidth();
						LogUtil.i("scroll", "经验条长度"+width);
						int expDex1=0;
						int expDexTotal=0;
						if(user.lv<50){
							expDex1=user.exp/*-StringUtil.getExpByLevel(user.lv)*/;
							 expDexTotal=StringUtil.getExpByLevel(user.lv+1)/*-StringUtil.getExpByLevel(user.lv)*/;
						}else{
							//升到50级
						}
						experienceProgressTV.setText(""+expDex1+"/"+expDexTotal);
						LayoutParams params=experenceProgressView.getLayoutParams();
						if(params==null){
							params=new RelativeLayout.LayoutParams((int)(width*(expDex1*1f/(expDexTotal*1f))), RelativeLayout.LayoutParams.WRAP_CONTENT);
						}else{
							params.width=(int)(width*(expDex1*1f/(expDexTotal*1f)));
						}
						experenceProgressView.setLayoutParams(params);
					}
					return true;
				}
			});
			
			
			
			/*
			 * 省
			 */
			provinceTV.setText(""+user.province+" | "+user.city);
			  
			   
			 /*
			  * 
			  */
			    if(user.u_gender==1){
					//公
					userSexIV.setImageResource(R.drawable.male1);
				}else if(user.u_gender==2){
					//母
					userSexIV.setImageResource(R.drawable.female1);
				}else{
					
				}
			cityTV.setText(""+user.city);
			cityTV.setVisibility(View.GONE);
			userLevelTV.setText("Lv."+user.lv);
			if(user.currentAnimal!=null){
//				kingdomNameTV.setText(""+user.rank);
				kingdomNameTV.setText("萌星");
				userJobTV.setText(""+user.currentAnimal.pet_nickName);
			}
			
			if(getIntent().getIntExtra("mode", 1)==4){
				changeTabState();
				boxesIV.setImageResource(R.drawable.box_red);
				boxLineView.setVisibility(View.VISIBLE);
				showKingdomGift();
			}else{
				showKingdoms();
			}
			
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+user.u_iconUrl, userIcon, displayImageOptions,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
				
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
//					linearLayout2.setBackgroundDrawable(new BitmapDrawable(loadedImage));
					/*添加毛玻璃效果
					 *首先要将Bitmap的Config转化为Config.ARGB_8888类型的 */
					 
					int[] pixels=new int[loadedImage.getWidth()*loadedImage.getHeight()];
					loadedImage.getPixels(pixels, 0, loadedImage.getWidth(), 0, 0, loadedImage.getWidth(), loadedImage.getHeight());
					loadedImage=Bitmap.createBitmap(pixels, loadedImage.getWidth(), loadedImage.getHeight(), Config.ARGB_8888);
					
					loadedImage1=Blur.fastblur(UserDossierActivity.this, loadedImage, 18);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(50);
								
								
								
								
								/*Matrix matrix=new Matrix();
								float scale1=Constants.screen_width*1f/loadedImage1.getWidth();
								float scale2=linearLayout2.getMeasuredHeight()*1f/loadedImage1.getHeight();
								float scale=scale1>scale2?scale1:scale2;
								matrix.postScale(scale, scale);
								Bitmap temp=Bitmap.createBitmap(loadedImage1, 0, 0, loadedImage1.getWidth(), loadedImage1.getHeight(),matrix,true);
								temp=Bitmap.createBitmap(temp, (temp.getWidth()-linearLayout2.getMeasuredWidth())/2, (temp.getHeight()-linearLayout2.getMeasuredHeight())/2, linearLayout2.getMeasuredWidth(), linearLayout2.getMeasuredHeight());
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
										
									}
								});
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
			BitmapFactory.Options options=new BitmapFactory.Options();
			
			options.inJustDecodeBounds=false;
			options.inSampleSize=4;
			options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			displayImageOptions=new DisplayImageOptions
		            .Builder()
			.showImageOnLoading(R.drawable.pet_icon)
			.showImageOnFail(R.drawable.pet_icon)
			        .cacheInMemory(false)
			        .cacheOnDisc(false)
			        .bitmapConfig(Bitmap.Config.RGB_565)
			        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			        .decodingOptions(options)
	                .build();
			imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+user.currentAnimal.pet_iconUrl, kingdomIcon, displayImageOptions);
		}
	}
	private void initListener() {
		// TODO Auto-generated method stub
		backIV.setOnClickListener(this);
		kingdomsIV.setOnClickListener(this);
		starsIV.setOnClickListener(this);
		activityIV.setOnClickListener(this);
		boxesIV.setOnClickListener(this);
		changeIconIV.setOnClickListener(this);
		moreIv.setOnClickListener(this);
		kingdomIcon.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.peoples_imageview:
			changeTabState();
			activityIV.setImageResource(R.drawable.flag_red);
			activityLineView.setVisibility(View.VISIBLE);
			showKingdomPeoples();
			break;
		case R.id.gift_imageview:
			changeTabState();
			boxesIV.setImageResource(R.drawable.box_red);
			boxLineView.setVisibility(View.VISIBLE);
			showKingdomGift();
			break;
		case R.id.track_imageview:
			changeTabState();
			kingdomsIV.setImageResource(R.drawable.hat_king_red);
			hatLineView.setVisibility(View.VISIBLE);
			showKingdoms();
			break;
		case R.id.image_list_imageview:
			changeTabState();
			starsIV.setImageResource(R.drawable.star_five_red);
			starLineView.setVisibility(View.VISIBLE);
			showUserFocus();
			break;
		case R.id.change_icon_imageview:
			changeUserIcon();
			break;
		case R.id.take_picture_imageview:
			getSharePetKingdomPicture();
			if(!StringUtil.isEmpty(sharePath)){
				 ShowMore showMore=new ShowMore(moreLayout, this,sharePath,moreParentLayout);
					showMore.userDossier(user);
//					showMore.onlyCanShare();
			}
		   
			/*//聊天
			Intent intent=new Intent(this,ChatActivity.class);
			intent.putExtra("user", data.user);
			this.startActivity(intent);*/
			break;
		case R.id.user_icon:
			if(PetKingdomActivity.petKingdomActivity!=null){
				if(PetKingdomActivity.petKingdomActivity.loadedImage1!=null){
					PetKingdomActivity.petKingdomActivity.linearLayout2.setBackgroundDrawable(null);
					PetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
					PetKingdomActivity.petKingdomActivity.loadedImage1=null;
				}
				PetKingdomActivity.petKingdomActivity.finish();
			}
			Intent intent2 =new Intent(this,PetKingdomActivity.class);
			intent2.putExtra("animal", user.currentAnimal);
			this.startActivity(intent2);
//			this.finish();
			break;
			
		}
	}
	/**
	 * 显示用户加入的王国列表
	 */
	public void showKingdoms(){
		listParentLayout.setVisibility(View.VISIBLE);
		noteLayout.setVisibility(View.GONE);
		if(userKingdomList==null){
			userKingdomList=new UserKingdomList(this,user);
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					userKingdomList.onRefresh();
				}
			}).start();
			
		}
		listParentLayout.removeAllViews();
		listParentLayout.addView(userKingdomList.getView());
		
	}
	/**
	 * 显示用户关注信息
	 */
	public void showUserFocus(){
		listParentLayout.setVisibility(View.VISIBLE);
		noteLayout.setVisibility(View.GONE);
		if(userFocus==null){
			userFocus=new UserFocus(this,user);
		}else{
            new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					userFocus.onRefresh();
				}
			}).start();
			
		}
		listParentLayout.removeAllViews();
		listParentLayout.addView(userFocus.getView());
	}
	/**
	 * 显示用户活动
	 */
	public void showKingdomPeoples(){
		if(userActivity==null){
			userActivity=new UserActivity(this, user);
		}/*else{
			userActivity.onRefresh();
		}*/
//		listParentLayout.removeAllViews();
//		listParentLayout.addView(userActivity.getView());
		listParentLayout.setVisibility(View.GONE);
		noteLayout.setVisibility(View.VISIBLE);
	}
	/**
	 * 显示国王收到的所有礼物
	 */
	public void showKingdomGift(){
		listParentLayout.setVisibility(View.VISIBLE);
		noteLayout.setVisibility(View.GONE);
		if(userGiftList==null){
			userGiftList=new UserGiftList(this,user);
		}else{
            new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					userGiftList.onRefresh();
				}
			}).start();
			
		}
		listParentLayout.removeAllViews();
		listParentLayout.addView(userGiftList.getView());
	}
	/**
	 * 改变选项条状态
	 */
	public void changeTabState(){
		activityIV.setImageResource(R.drawable.flag_gray);
		kingdomsIV.setImageResource(R.drawable.hat_king_gray);
		boxesIV.setImageResource(R.drawable.box_gray);
		starsIV.setImageResource(R.drawable.star_five_gray);
		
		activityLineView.setVisibility(View.INVISIBLE);
		boxLineView.setVisibility(View.INVISIBLE);
		hatLineView.setVisibility(View.INVISIBLE);
		starLineView.setVisibility(View.INVISIBLE);
		
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
				Intent intent2=new Intent(UserDossierActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
				UserDossierActivity.this.startActivityForResult(intent2, 1);
				
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
				Intent intent=new Intent(UserDossierActivity.this,AlbumPictureBackground.class);
				intent.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
				UserDossierActivity.this.startActivityForResult(intent, 1);
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
				Animation animation=AnimationUtils.loadAnimation(UserDossierActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
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
			imageLoader.displayImage(uri.toString(), userIcon,displayImageOptions);
			bottomLinearLayout2.setVisibility(View.INVISIBLE);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String nameString=HttpUtil.uploadUserIcon(path,UserDossierActivity.this,1);
					if(nameString==null){
						
					}else{
						/*if(new File(Constants.Picture_ICON_Path+File.separator+nameString).exists()){
							Constants.user.iconPath=Constants.Picture_ICON_Path+File.separator+nameString;
							
						}*/
						if(new File(path).exists()){
							Constants.user.pet_iconPath=path;
							
						}
					}
				}
			}).start();
			cursor.close();
		}
	}	
	/**
	 * 分享 用户资料截图
	 */
	String sharePath;
	public void getSharePetKingdomPicture(){
		Bitmap bmp=ImageUtil.getImageFromView(framelayout);
		String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis();
		path=ImageUtil.compressImage(bmp, 100, path);
		if(!StringUtil.isEmpty(path)){
			sharePath=path;
		}
	}
	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(HomeFragment.blurBitmap==null){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						listParentLayout.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
						listParentLayout.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
	}
	public void shareNumChange(){
		/*petPicture.shares++;
		shareNumTV.setText(""+petPicture.shares);*/
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(/*!Constants.isSuccess*/true)return;
				final int gold=HttpUtil.userShareNumsApi(UserDossierActivity.this,handleHttpConnectionException.getHandler(UserDossierActivity.this));
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						DialogExpGoldConAdd dialogExpGoldConAdd=new DialogExpGoldConAdd(UserDossierActivity.this, 2,gold- Constants.user.coinCount,progresslayout);
						Constants.user.coinCount=gold;
					}
				});
			}
		}).start();
	}
	public void userKingdomsChanged(Animal animal){
		userKingdomList.animalList.remove(animal);
		userKingdomList.adapter.notifyDataSetChanged();
		
	}
	public void addNewKingdom(){
		userKingdomList.onRefresh();
	}
}
