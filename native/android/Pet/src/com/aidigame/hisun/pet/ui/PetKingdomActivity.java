package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.util.ArrayList;

import android.R.anim;
import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.blur.Blur;
import com.aidigame.hisun.pet.blur.TopCenterImageView;
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
import com.aidigame.hisun.pet.widget.ShowMore;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.fragment.ClawStyleFunction;
import com.aidigame.hisun.pet.widget.fragment.DialogExpGoldConAdd;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
import com.aidigame.hisun.pet.widget.fragment.KingdomImages;
import com.aidigame.hisun.pet.widget.fragment.KingdomPeoples;
import com.aidigame.hisun.pet.widget.fragment.KingdomTrends;
import com.aidigame.hisun.pet.widget.fragment.MarketFragment;
import com.aidigame.hisun.pet.widget.fragment.PetGiftList;
import com.aidigame.hisun.pet.widget.fragment.UserGiftList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 宠物档案界面（王国或家族），展示宠物的所有相关信息
 * @author admin
 *
 */
public class PetKingdomActivity extends Activity implements OnClickListener,ClawStyleFunction.ClawFunctionChoseListener{
	ImageView backIV,takePictureIV,changeIconIV,trackClawIV,
	           pictureListIV,peoplesListIV,giftListIV,petSexIV,moreIv;
	TextView titleTV,addFocusTV,petNameTV,petRaceTV,
	         petAgeTV,userJobTV,userNameTV,hotProgressTV,contributeTV,hotRankTV,peopleNumTV,hotNumTv,fansNumTv,line1,line2;
	View trackLineView,pictureLineView,peopleLineView,giftLineView;
	public View popupParent;
	public RelativeLayout black_layout;
	RoundImageView petIcon,userIcon;
	public static PetKingdomActivity petKingdomActivity;
	HandleHttpConnectionException handleHttpConnectionException;
	ShowProgress showProgress;
	LinearLayout bottomLinearLayout2,userInfoLayout,moreLayout,animLayout,progresslayout;
	public LinearLayout linearLayout2,linearLayout3,listParentLayout,blurLayout,listParentLayout2,listParentLayout3,listParentLayout4;
	public View topWhiteView,infoShadowView;
	KingdomTrends kingdomTrends;
	KingdomImages kingdomImages;
	KingdomPeoples kingdomPeoples;
	PetGiftList userGiftList;
	public boolean isShowInfoLayout=true;
	
	FrameLayout frameLayout;
	public Bitmap   loadedImage1;
	RelativeLayout clawFunctionLayout,moreParentLayout;
	
	ClawStyleFunction clawStyleFunction;
	/*
	 * 一张图片的所有信息，
	 * 根据用户id，判断是否为本人创建或加入的王国，还是其他人的王国，
	 * 两种情况下界面显示不同
	 */
	Animal data;
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
		setContentView(R.layout.activity_petdossier);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		petKingdomActivity=this;
		data=(Animal)getIntent().getSerializableExtra("animal");
		
		
		initView();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		backIV=(ImageView)findViewById(R.id.back);
//		takePictureIV=(ImageView)findViewById(R.id.take_picture_imageview);
		changeIconIV=(ImageView)findViewById(R.id.change_icon_imageview);
		trackClawIV=(ImageView)findViewById(R.id.track_imageview);
		peoplesListIV=(ImageView)findViewById(R.id.peoples_imageview);
		giftListIV=(ImageView)findViewById(R.id.gift_imageview);
		petSexIV=(ImageView)findViewById(R.id.pet_sex_imageview);
		pictureListIV=(ImageView)findViewById(R.id.image_list_imageview);
		peopleLineView=(View)findViewById(R.id.peoples_line_view);
		trackLineView=(View)findViewById(R.id.track_line_view);
		pictureLineView=(View)findViewById(R.id.image_line_view);
		giftLineView=(View)findViewById(R.id.gift_line_view);
		line1=(TextView)findViewById(R.id.line1);
		line2=(TextView)findViewById(R.id.line2);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		
		frameLayout=(FrameLayout)findViewById(R.id.framelayout1);
		
		
		peopleNumTV=(TextView)findViewById(R.id.people_num_tv);
		hotNumTv=(TextView)findViewById(R.id.people_hot_num_tv);
		fansNumTv=(TextView)findViewById(R.id.fans_num_tv);
		
		
		clawFunctionLayout=(RelativeLayout)findViewById(R.id.claw_function_layout);
		moreParentLayout=(RelativeLayout)findViewById(R.id.more_parent_latyout);
		
		
		titleTV=(TextView)findViewById(R.id.title_tv);
		moreIv=(ImageView)findViewById(R.id.join_kingdom_tv);
		addFocusTV=(TextView)findViewById(R.id.add_focus_tv);
		petNameTV=(TextView)findViewById(R.id.pet_name_tv);
		petRaceTV=(TextView)findViewById(R.id.pet_race_tv);
		petAgeTV=(TextView)findViewById(R.id.pet_age_tv);
		userJobTV=(TextView)findViewById(R.id.user_job);
		userNameTV=(TextView)findViewById(R.id.user_name);
		hotProgressTV=(TextView)findViewById(R.id.progress_value_tv);
		contributeTV=(TextView)findViewById(R.id.contribute_tv);
		hotRankTV=(TextView)findViewById(R.id.hot_rank_tv);
		petIcon=(RoundImageView)findViewById(R.id.imageView3);
		userIcon=(RoundImageView)findViewById(R.id.user_icon);
		listParentLayout=(LinearLayout)findViewById(R.id.show_list_layout);
		listParentLayout2=(LinearLayout)findViewById(R.id.show_list_layout2);
		listParentLayout3=(LinearLayout)findViewById(R.id.show_list_layout3);
		listParentLayout4=(LinearLayout)findViewById(R.id.show_list_layout4);
		linearLayout2=(LinearLayout)findViewById(R.id.linearLayout2);
		blurLayout=(LinearLayout)findViewById(R.id.framelayout);
		animLayout=(LinearLayout)findViewById(R.id.anim_layout);
		topWhiteView=(View)findViewById(R.id.top_white_view);
		infoShadowView=(View)findViewById(R.id.frame_view2);
		linearLayout3=(LinearLayout)findViewById(R.id.linearLayout3);
		bottomLinearLayout2=(LinearLayout)findViewById(R.id.bottom_linearlayout2);
		userInfoLayout=(LinearLayout)findViewById(R.id.user_info_layout);
		
		moreLayout=(LinearLayout)findViewById(R.id.more_layout);
		progresslayout=(LinearLayout)findViewById(R.id.progress_layout);  
		
		
		
		
		
		showKingdomImages();
		showKingdomPeoples();
		showKingdomTrends();
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		kingdomTrends.getView().setLayoutParams(params);
		listParentLayout.removeAllViews();
		listParentLayout.addView(kingdomTrends.getView());
		showProgress=new ShowProgress(this, progresslayout);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.animalInfo(PetKingdomActivity.this,data, handler);
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
		initArc();
		
	}
	public void initArc(){
		if(Constants.user!=null&&Constants.user.aniList!=null){
			if(Constants.user.aniList.contains(data)){
				if(data.master_id==Constants.user.userId){
					clawStyleFunction=new ClawStyleFunction(this,true,true);
				}else{
					clawStyleFunction=new ClawStyleFunction(this,false,true);
				}
			}else{
				clawStyleFunction=new ClawStyleFunction(this,false,false);
			}
		}else{
			clawStyleFunction=new ClawStyleFunction(this,false,false);
		}
		
		
		View arcView=clawStyleFunction.getView();
		arcView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		clawFunctionLayout.removeAllViews();
		clawFunctionLayout.addView(arcView);
		clawStyleFunction.setClawFunctionChoseListener(this);
	}
	public void setPetInfo(final Animal data){
		/*
		 * 是否为自己创建的王国
		 */
		titleTV.setText("萌星 "+data.pet_nickName);
		userJobTV.setText("经纪人");
		petNameTV.setText(data.pet_nickName);
		line1.setVisibility(View.VISIBLE);
		line2.setVisibility(View.VISIBLE);
		userIcon.setVisibility(View.VISIBLE);
		petSexIV.setVisibility(View.VISIBLE);
		/*
		 * 宠物种族
		 */
		 petRaceTV.setText(data.race);
		  hotNumTv.setText("人气  "+data.t_rq); 
		  peopleNumTV.setText("成员  "+data.fans);
		  fansNumTv.setText("粉丝  "+data.followers);
		  userNameTV.setText(data.u_name);
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
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=4;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		ImageLoader imageLoader=ImageLoader.getInstance();
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
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+data.pet_iconUrl, petIcon, displayImageOptions,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
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
						int[] pixels=new int[loadedImage.getWidth()*loadedImage.getHeight()];
						loadedImage.getPixels(pixels, 0, loadedImage.getWidth(), 0, 0, loadedImage.getWidth(), loadedImage.getHeight());
//						loadedImage=Bitmap.createBitmap(pixels, loadedImage.getWidth(), loadedImage.getHeight(), Config.ARGB_8888);
						
						loadedImage1=Blur.fastblur(PetKingdomActivity.this, Bitmap.createBitmap(pixels, loadedImage.getWidth(), loadedImage.getHeight(), Config.ARGB_8888), 18);
						try {
							Thread.sleep(50);
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
		userIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.user_icon));
		imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+data.u_tx, userIcon, displayImageOptions);
	}
	private void initListener() {
		// TODO Auto-generated method stub
		backIV.setOnClickListener(this);
		trackClawIV.setOnClickListener(this);
		pictureListIV.setOnClickListener(this);
		peoplesListIV.setOnClickListener(this);
		giftListIV.setOnClickListener(this);
		changeIconIV.setOnClickListener(this);
//		takePictureIV.setOnClickListener(this);
		userIcon.setOnClickListener(this);
		userInfoLayout.setOnClickListener(this);
		contributeTV.setOnClickListener(this);
		hotRankTV.setOnClickListener(this);
		moreIv.setOnClickListener(this);
		
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
			peoplesListIV.setImageResource(R.drawable.people_of_the_kingdom);
			peopleLineView.setVisibility(View.VISIBLE);
			showKingdomPeoples();
			break;
		case R.id.gift_imageview:
			changeTabState();
			giftListIV.setImageResource(R.drawable.gift_red);
			giftLineView.setVisibility(View.VISIBLE);
			showKingdomGift();
			break;
		case R.id.track_imageview:
			changeTabState();
			trackClawIV.setImageResource(R.drawable.track_claw);
			trackLineView.setVisibility(View.VISIBLE);
			showKingdomTrends();
			break;
		case R.id.image_list_imageview:
			changeTabState();
			pictureListIV.setImageResource(R.drawable.image_symbol);
			pictureLineView.setVisibility(View.VISIBLE);
			showKingdomImages();
			break;
		case R.id.change_icon_imageview:
			changeUserIcon();
			break;
		case R.id.take_picture_imageview:
			publishPicture();
			break;
		case R.id.user_icon:
			if(UserDossierActivity.userDossierActivity!=null){
				if(UserDossierActivity.userDossierActivity.loadedImage1!=null){
					UserDossierActivity.userDossierActivity.linearLayout2.setBackgroundDrawable(null);
					UserDossierActivity.userDossierActivity.loadedImage1.recycle();
					UserDossierActivity.userDossierActivity.loadedImage1=null;
				}
				UserDossierActivity.userDossierActivity.finish();
			}
			Intent intent=new Intent(PetKingdomActivity.this,UserDossierActivity.class);
			data.user=new User();
			data.user.userId=data.master_id;
			data.user.u_iconUrl=data.u_tx;
			data.user.u_nick=data.u_name;
			intent.putExtra("user", data.user);
			PetKingdomActivity.this.startActivity(intent);
			
			break;
		case R.id.user_info_layout:
			/*Intent intent2=new Intent(this,UserDossierActivity.class);
			
			intent2.putExtra("user", data);
			this.startActivity(intent2);
			this.finish();*/
			break;
		case R.id.contribute_tv:
			Intent intent3=new Intent(this,ContributeRankListActivity.class);
			intent3.putExtra("animal", data);
			this.startActivity(intent3);
			break;
		case R.id.hot_rank_tv:
			Intent intent4=new Intent(this,PopularRankListActivity.class);
			this.startActivity(intent4);
			break;
		case R.id.join_kingdom_tv:
			getSharePetKingdomPicture();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final boolean flag=HttpUtil.kingAndUserRelation(PetKingdomActivity.this,data, handleHttpConnectionException.getHandler(PetKingdomActivity.this));
					
//					if(flag){
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(!StringUtil.isEmpty(sharePath)){
									if(flag){
										new ShowMore(moreLayout, PetKingdomActivity.this,sharePath,moreParentLayout).kindomShowMore(data);
									}else{
										new ShowMore(moreLayout, PetKingdomActivity.this,sharePath,moreParentLayout).kindomShowMore(data)/*.onlyCanShare()*/;
									}
									
								}
								
							}
						});
//					}
				}
			}).start();
			
			break;
			
		}
	}
	/**
	 * 显示王国最新动态
	 */
	public void showKingdomTrends(){
		if(kingdomTrends==null){
			kingdomTrends=new KingdomTrends(this,data);
		}else{
            new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					kingdomTrends.refresh();
				}
			}).start();

		}
		listParentLayout2.setVisibility(View.GONE);
		listParentLayout.setVisibility(View.VISIBLE);
		listParentLayout3.setVisibility(View.GONE);
		listParentLayout4.setVisibility(View.GONE);
		listParentLayout.removeAllViews();
		listParentLayout.addView(kingdomTrends.getView());
	}
	/**
	 * 显示王国图片
	 */
	public void showKingdomImages(){
		if(kingdomImages==null){
			kingdomImages=new KingdomImages(this,data);
		}else{
            new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					kingdomImages.refresh();
				}
			}).start();

		}
		listParentLayout.setVisibility(View.GONE);
		listParentLayout2.setVisibility(View.VISIBLE);
		listParentLayout3.setVisibility(View.GONE);
		listParentLayout4.setVisibility(View.GONE);
		listParentLayout2.removeAllViews();
		listParentLayout2.addView(kingdomImages.getView());
	}
	/**
	 * 显示王国子民
	 */
	public void showKingdomPeoples(){
		if(kingdomPeoples==null){
			kingdomPeoples=new KingdomPeoples(this, data);
		}else{
            new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					kingdomPeoples.refresh();
				}
			}).start();
			
		}
		listParentLayout.setVisibility(View.GONE);
		listParentLayout3.setVisibility(View.VISIBLE);
		listParentLayout2.setVisibility(View.GONE);
		listParentLayout4.setVisibility(View.GONE);
		listParentLayout3.removeAllViews();
		listParentLayout3.addView(kingdomPeoples.getView());
	}
	/**
	 * 显示国王收到的所有礼物
	 */
	public void showKingdomGift(){
		if(userGiftList==null){
			userGiftList=new PetGiftList(this,data);
		}else{
            new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					userGiftList.refresh();
				}
			}).start();

		}
		listParentLayout.setVisibility(View.GONE);
		listParentLayout4.setVisibility(View.VISIBLE);
		listParentLayout3.setVisibility(View.GONE);
		listParentLayout2.setVisibility(View.GONE);
		listParentLayout4.removeAllViews();
		listParentLayout4.addView(userGiftList.getView());
	}
	/**
	 * 改变选项条状态
	 */
	public void changeTabState(){
		peoplesListIV.setImageResource(R.drawable.people_of_the_kingdom_gray);
		trackClawIV.setImageResource(R.drawable.track_claw_gray);
		giftListIV.setImageResource(R.drawable.gift_red_gray);
		pictureListIV.setImageResource(R.drawable.image_symbol_gray);
		
		peopleLineView.setVisibility(View.INVISIBLE);
		giftLineView.setVisibility(View.INVISIBLE);
		trackLineView.setVisibility(View.INVISIBLE);
		pictureLineView.setVisibility(View.INVISIBLE);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MarketFragment.from=0;
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
				Intent intent2=new Intent(PetKingdomActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
				PetKingdomActivity.this.startActivityForResult(intent2, 1);
				
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
				Intent intent=new Intent(PetKingdomActivity.this,AlbumPictureBackground.class);
				intent.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
				PetKingdomActivity.this.startActivityForResult(intent, 1);
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
				Animation animation=AnimationUtils.loadAnimation(PetKingdomActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
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
				Intent intent2=new Intent(PetKingdomActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				PetKingdomActivity.this.startActivity(intent2);
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
				Intent intent2=new Intent(PetKingdomActivity.this,AlbumPictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				PetKingdomActivity.this.startActivity(intent2);
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
				Animation animation=AnimationUtils.loadAnimation(PetKingdomActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
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
			imageLoader.displayImage(uri.toString(), petIcon,displayImageOptions);
			bottomLinearLayout2.setVisibility(View.INVISIBLE);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String nameString=HttpUtil.uploadUserIcon(path,PetKingdomActivity.this,1);
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
	@Override
	public void clickItem1() {
		// TODO Auto-generated method stub
		if(!UserStatusUtil.isLoginSuccess(PetKingdomActivity.this,popupParent,black_layout)){
//		    setBlurImageBackground();
			return ;
		}
		if(Constants.user!=null&&Constants.user.aniList!=null){
			if(Constants.user.aniList.contains(data)){
				Intent intent=new Intent(this,ShakeActivity.class);
				intent.putExtra("animal", data);
				intent.putExtra("mode", 1);
				this.startActivity(intent);
			}else{
				Intent intent=new Intent(this,PlayJokeActivity.class);
				intent.putExtra("animal", data);
				intent.putExtra("mode", 2);
				this.startActivity(intent);
			}
		}
		
	}
	@Override
	public void clickItem2() {
		// TODO Auto-generated method stub
		if(!UserStatusUtil.isLoginSuccess(PetKingdomActivity.this,popupParent,black_layout)){
//		    setBlurImageBackground();
			return ;
		}
		if(Constants.user!=null&&Constants.user.aniList!=null){
			/*DialogGiveSbGift dgb=new DialogGiveSbGift(this,data);
			final AlertDialog dialog=new AlertDialog.Builder(this).setView(dgb.getView())
					.show();*/
			if(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.finish();
			Intent intent=new Intent(this,DialogGiveSbGiftActivity.class);
			intent.putExtra("animal", data);
			this.startActivity(intent);
			DialogGiveSbGiftActivity dgb=DialogGiveSbGiftActivity.dialogGiveSbGiftActivity;
			DialogGiveSbGiftActivity.dialogGoListener=new DialogGiveSbGiftActivity.DialogGoListener() {
				
				@Override
				public void toDo() {
					// TODO Auto-generated method stub
					MarketFragment.from=2;
					Intent intent=null;
					if(NewHomeActivity.homeActivity==null){
						intent=new Intent(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity,NewHomeActivity.class);
					}else{
						intent=NewHomeActivity.homeActivity.getIntent();
					}
					intent.putExtra("mode", NewHomeActivity.MARKETFRAGMENT);
					DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.startActivity(intent);
				}
				
				@Override
				public void closeDialog() {
					// TODO Auto-generated method stub
					DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.finish();
					kingdomTrends.onRefresh();
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
		
	}
	@Override
	public void clickItem3() {
		// TODO Auto-generated method stub
		if(!UserStatusUtil.isLoginSuccess(PetKingdomActivity.this,popupParent,black_layout)){
//		    setBlurImageBackground();
			return ;
		}
		if(Constants.user!=null&&Constants.user.aniList!=null){
			if(Constants.user.aniList.contains(data)){
				if(data.master_id==Constants.user.userId){
					Intent intent=new Intent(this,BiteActivity.class);
					intent.putExtra("animal", data);
					this.startActivity(intent);
				}else{
					Intent intent=new Intent(this,TouchActivity.class);
					intent.putExtra("animal", data);
					this.startActivity(intent);
				}
			}else{
				Intent intent=new Intent(this,TouchActivity.class);
				intent.putExtra("animal", data);
				this.startActivity(intent);
			}
		}
		
	}
	@Override
	public void clickItem4() {
		// TODO Auto-generated method stub
		if(!UserStatusUtil.isLoginSuccess(PetKingdomActivity.this,popupParent,black_layout)){
//		    setBlurImageBackground();
			return ;
		}
		Intent intent=new Intent(this,PlayGameActivity.class);
		intent.putExtra("animal", data);
		this.startActivity(intent);
	}
	

	/**
	 * 分享 王国资料截图
	 */
	String sharePath;
	public void getSharePetKingdomPicture(){
		Bitmap bmp=ImageUtil.getImageFromView(frameLayout);
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
				final int gold=HttpUtil.userShareNumsApi(PetKingdomActivity.this,handleHttpConnectionException.getHandler(PetKingdomActivity.this));
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						DialogExpGoldConAdd dialogExpGoldConAdd=new DialogExpGoldConAdd(PetKingdomActivity.this, 2,gold- Constants.user.coinCount,progresslayout);
						Constants.user.coinCount=gold;
					}
				});
			}
		}).start();
	}
	public void setPeoplesNum(int count,boolean isSuccess){
		peopleNumTV.setText("成员 "+count);
		if(isSuccess){
			kingdomPeoples.loadData();
			/*if(count>kingdomPeoples.datas.size()){
				kingdomPeoples.datas.add(Constants.user);
				kingdomPeoples.adapter.notifyDataSetChanged();
			}else if(count<kingdomPeoples.datas.size()){
				kingdomPeoples.datas.remove(Constants.user);
				kingdomPeoples.adapter.notifyDataSetChanged();
			}*/
		}
		initArc();
		
	}
	public void fansNum(int count,boolean isSuccess){
		fansNumTv.setText("粉丝  "+count);
		if(isSuccess){
			
		}
		
	}
}
