package com.aidigame.hisun.imengstar.ui;

import java.io.File;
import java.net.Authenticator.RequestorType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import u.aly.co;
import u.aly.p;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.CommentListViewAdapter;
import com.aidigame.hisun.imengstar.adapter.TopicUsersListAdapter;
import com.aidigame.hisun.imengstar.adapter.CommentListViewAdapter.ClickUserName;
import com.aidigame.hisun.imengstar.adapter.TopicUsersListAdapter.GestureListener;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.bean.PetPicture.Comments;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.huanxin.ExpandGridView;
import com.aidigame.hisun.imengstar.huanxin.ExpressionAdapter;
import com.aidigame.hisun.imengstar.huanxin.ExpressionPagerAdapter;
import com.aidigame.hisun.imengstar.huanxin.NewSmileUtils;
import com.aidigame.hisun.imengstar.huanxin.PasteEditText;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.view.LinearLayoutForListView;
import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.widget.ShowProgress;
import com.aidigame.hisun.imengstar.widget.WeixinShare;
import com.aidigame.hisun.imengstar.widget.fragment.DialogJoinKingdom;
import com.aidigame.hisun.imengstar.widget.fragment.DialogNote;
import com.aidigame.hisun.imengstar.widget.fragment.ShareDialogFragment;
import com.aidigame.hisun.imengstar.widget.fragment.DialogJoinKingdom.ResultListener;
import com.aidigame.hisun.imengstar.widget.fragment.ShareDialogFragment.ShareDialogFragmentResultListener;
import com.aidigame.hisun.imengstar.R;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageWorker;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.okhttp.internal.DiskLruCache.Editor;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.BufferType;
import android.widget.TextView.OnEditorActionListener;

public class NewShowTopicActivity extends BaseActivity implements OnClickListener{
	public static final int SHOW_LIKE_LIST=1;
	public static final int SHOW_GIFT_LIST=2;
	public static final int SHOW_COMMENT_LIST=3;
	public static final int SHOW_SHARE_LIST=4;
	
	private  ScrollView scrollView,scrollview2;
//	private  MyScrollView myScrollView;
	
	
	private   int current_show=SHOW_LIKE_LIST;
	private   int current_page=1;//1,正面；-1，反面
	/*
	 * 一直存在的控件
	 */
	private  	ImageView bottomLikeIv,bottomGiftIv,bottomCommentIv,bottomMoreIv,guideIv1;
	private  ViewGroup mContainer;
	private  View popupParent;
	private  RelativeLayout black_layout;
	private  PetPicture petPicture;
	private  LinearLayout progressLayout;
	private  FrameLayout parent_framelayout;
	//评论相关
//	private  EditText commentET;
	private  TextView send_comment_tv;
	private  LinearLayout addcommentLayout,reverseSideLayout;
	
	public static NewShowTopicActivity newShowTopicActivity;
	private  Handler handler;
	private  ImageFetcher mImageFetcher;
	
//	public Bitmap bmp;
	private  String bmpPath;
	private  int mode;//3,显示反面评论
	private  ShowProgress showProgress;
	
	/*
	 *正面显示的控件
	 */
	private  RelativeLayout oneLayout;
	private   ImageView closeIv1,imageView,imageViewTemp;
	private  TextView desTv,topicTv,timeTv;
	private  View paddingView;//卡片长度超过或接近屏幕底部时显示，以防内容显示不全
	private LinearLayout imageLayout;
	
	/*
	 * 反面显示控件
	 */
	private  RelativeLayout twoLayout;
	private  RoundImageView petIcon,userIcon;
	private  ImageView genderIv,middleTabLikeIv,middleTabGiftIv,middleTabCommentIv,middleTabShareIv,
	          triangleIv1, triangleIv2, triangleIv3, triangleIv4,
	          closeIv2;
	private  TextView likeNumTv1,giftTv1,commentTv1,shareTv1,
	         triangleTv1,triangleTv2,triangleTv3,triangleTv4,
	         petNameTv,petRaceTv,userNameTv;
	private  LinearLayoutForListView listView;
	private  CommentListViewAdapter commentListViewAdapter;
	private  TopicUsersListAdapter topicUsersListAdapter;
	private  FrameLayout infoFrameLayout;
	private  boolean urlIsEmpty=false;
	private  UMSocialService mController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_topic);
		MobclickAgent.onEvent(this, "photopage");
		newShowTopicActivity=this;
		guideIv1=(ImageView)findViewById(R.id.guide1);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor e=sp.edit();
		boolean guide1=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE1, true);
		if(guide1){
			guideIv1.setImageResource(R.drawable.guide1);
			guideIv1.setVisibility(View.VISIBLE);
			e.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE1, false);
			e.commit();
		}else{
			guideIv1.setVisibility(View.GONE);
		}
		
		
		
		
		
		mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		SinaSsoHandler  sinaSsoHandler=new SinaSsoHandler(this);
		sinaSsoHandler.addToSocialSDK();
		
		//获取用户相关信息
				if(PetApplication.isSuccess&&PetApplication.myUser==null){
					downLoadUserInfo();
				}
				 mImageFetcher = new ImageFetcher(this, Constants.screen_width);
				mode=getIntent().getIntExtra("mode", 0);
				
				
				
				
				initEmotion();
		initAlwaysStayView();
//		showProgress=new ShowProgress(this, progressLayout);
		initOneView();
		iniTwoView();
		
		
		
		
		
//		initShareView();
		shareLayout=(RelativeLayout)findViewById(R.id.sharelayout);
		petPicture=(PetPicture)getIntent().getSerializableExtra("PetPicture");
		urlIsEmpty=StringUtil.isEmpty(petPicture.url);
		
		
		
		
		if(mode==3||mode==1){
			scrollView.setVisibility(View.INVISIBLE);
			reverseSideLayout.setVisibility(View.VISIBLE);
			current_page=-1;
		}
		
		displayImage();
		 new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					final boolean flag=HttpUtil.imageInfo(petPicture,handler,NewShowTopicActivity.this);
					
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(flag&&petPicture!=null&&petPicture.animal!=null){
									updateInfo();
//									initMoreView();
									if(urlIsEmpty)displayImage();
									
									if(mode==2){
										
									}
									
								}else{
									Toast.makeText(NewShowTopicActivity.this, "网络不给力~图片信息获取失败", Toast.LENGTH_LONG).show();
								}
								if(showProgress==null){
									showProgress=new ShowProgress(NewShowTopicActivity.this, progressLayout);
								}
								showProgress.progressCancel();
								
							}
						});
					
				}
			}).start();
		//设置需要保存缓存 
//		mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE); 
		
		clickIV1();
//		clickIV2();
	}

	/**
	 * 初始化 界面中一直存在的控件
	 */
	private void initAlwaysStayView() {
		// TODO Auto-generated method stub
		bottomCommentIv=(ImageView)findViewById(R.id.bottom_iv_comment);
		bottomLikeIv=(ImageView)findViewById(R.id.bottom_iv_like);
		bottomGiftIv=(ImageView)findViewById(R.id.bottom_iv_gift);
		bottomMoreIv=(ImageView)findViewById(R.id.bottom_iv_more);
		mContainer=(ViewGroup)findViewById(R.id.container);
		progressLayout=(LinearLayout)findViewById(R.id.progress_layout);
		popupParent=findViewById(R.id.popup_parent);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		scrollView=(ScrollView)findViewById(R.id.scrollview);
//		myScrollView=(MyScrollView)findViewById(R.id.my_scrollview);
		parent_framelayout=(FrameLayout)findViewById(R.id.parent_framelayout);
		
//		commentET=(EditText)findViewById(R.id.edit_comment);
		send_comment_tv=(TextView)findViewById(R.id.send_comment);
		addcommentLayout=(LinearLayout)findViewById(R.id.add_comment_linearlayout);
		send_comment_tv.setText("取消");
		send_comment_tv.setOnClickListener(this);
		
		bottomCommentIv.setOnClickListener(this);
		bottomLikeIv.setOnClickListener(this);
		bottomGiftIv.setOnClickListener(this);
		bottomMoreIv.setOnClickListener(this);
		mContainer.setOnClickListener(this);
		guideIv1.setOnClickListener(this);
		
		mEditTextContent.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
					sendComment();
					return true;
			}
		});
		mEditTextContent.addTextChangedListener(new TextWatcher() {
		
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
				canSend=true;
				send_comment_tv.setText("发送");
			}else{
				canSend=false;
				send_comment_tv.setText("取消");
			}
		}
	});
	parent_framelayout.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return gestureDetector.onTouchEvent(event);
		}
	});
		
	}
	/**
	 * 正面显示时的控件
	 */
	private void initOneView() {
		// TODO Auto-generated method stub
		oneLayout=(RelativeLayout)findViewById(R.id.one_layout);
		imageView=(ImageView)findViewById(R.id.show_topic_iv);
		imageViewTemp=(ImageView)findViewById(R.id.show_topic_iv_temp);
		closeIv1=(ImageView)findViewById(R.id.show_topic_close_iv1);
		desTv=(TextView)findViewById(R.id.show_topic_comment_tv);
		topicTv=(TextView)findViewById(R.id.show_topic_topic_tv);
		timeTv=(TextView)findViewById(R.id.show_topic_time_tv);
		paddingView=findViewById(R.id.paddingView);
		imageLayout=(LinearLayout)findViewById(R.id.image_layout);
		closeIv1.setOnClickListener(this);
		
		
		scrollView.setDrawingCacheEnabled(false);
//		imageView.setOnClickListener(this);
	}
	/**
	 * 反面控件初始化
	 */
	private void iniTwoView() {
		// TODO Auto-generated method stub
		
		twoLayout=(RelativeLayout)findViewById(R.id.two_layout);
		closeIv2=(ImageView)findViewById(R.id.show_topic_close_iv2);
		userIcon=(RoundImageView)findViewById(R.id.show_topic_usericon);
		petIcon=(RoundImageView)findViewById(R.id.show_topic_peticon);
		genderIv=(ImageView)findViewById(R.id.show_topic_petgender);
		petNameTv=(TextView)findViewById(R.id.show_topic_petname);
		petRaceTv=(TextView)findViewById(R.id.show_topic_pet_race);
		userNameTv=(TextView)findViewById(R.id.show_topic_username);
		
		reverseSideLayout=(LinearLayout)findViewById(R.id.reverse_side_layout);
		
		
		scrollview2=(ScrollView)findViewById(R.id.scrollview1);
		
		
		middleTabLikeIv=(ImageView)findViewById(R.id.middle_tab_iv1);
		middleTabGiftIv=(ImageView)findViewById(R.id.middle_tab_iv2);
		middleTabCommentIv=(ImageView)findViewById(R.id.middle_tab_iv3);
		middleTabShareIv=(ImageView)findViewById(R.id.middle_tab_iv4);
		triangleIv1=(ImageView)findViewById(R.id.triangle_iv1);
		triangleIv2=(ImageView)findViewById(R.id.triangle_iv2);
		triangleIv3=(ImageView)findViewById(R.id.triangle_iv3);
		triangleIv4=(ImageView)findViewById(R.id.triangle_iv4);
		
		infoFrameLayout=(FrameLayout)findViewById(R.id.info_framelayout);
		
		likeNumTv1=(TextView)findViewById(R.id.middle_tab_tv1);
		giftTv1=(TextView)findViewById(R.id.middle_tab_tv2);
		commentTv1=(TextView)findViewById(R.id.middle_tab_tv3);
		shareTv1=(TextView)findViewById(R.id.middle_tab_tv4);
		
		triangleTv1=(TextView)findViewById(R.id.triangle_tv1);
		triangleTv2=(TextView)findViewById(R.id.triangle_tv2);
		triangleTv3=(TextView)findViewById(R.id.triangle_tv3);
		triangleTv4=(TextView)findViewById(R.id.triangle_tv4);
		
		listView=(LinearLayoutForListView)findViewById(R.id.info_listview);
		
		userIcon.setOnClickListener(this);
		petIcon.setOnClickListener(this);
		closeIv2.setOnClickListener(this);
		middleTabLikeIv.setOnClickListener(this);
		middleTabGiftIv.setOnClickListener(this);
		middleTabCommentIv.setOnClickListener(this);
		middleTabShareIv.setOnClickListener(this);
		
//		myScrollView.setDrawingCacheEnabled(false);
	}
	/**
	 * 更多按钮显示的条目
	 */
//	RelativeLayout moreLayout;
	
	private void initMoreView() {
		// TODO Auto-generated method stub
		
		TextView chatTv=(TextView)findViewById(R.id.messagetv);
		TextView reportTv=(TextView)findViewById(R.id.reporttv);
		final TextView pengta_tv=(TextView)findViewById(R.id.pengta_tv);
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(petPicture.animal) ){
//			pengta_tv.setVisibility(View.GONE);
		}
		pengta_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(NewShowTopicActivity.this,popupParent,black_layout)){
					shareLayout.setVisibility(View.INVISIBLE);
					return;
				}
				if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(petPicture.animal) ){
//					pengta_tv.setVisibility(View.GONE);
					Intent intent=new Intent(NewShowTopicActivity.this,DialogNoteActivity.class);
					intent.putExtra("mode", 10);
					intent.putExtra("info", "您已经捧TA了");
					startActivity(intent);
					shareLayout.setVisibility(View.GONE);
					return ;
				}
				int num=0;
				int count=0;
				for(int i=0;i<PetApplication.myUser.aniList.size();i++){
//					if(Constants.user.aniList.get(i).master_id!=Constants.user.userId)
						count++;
				}
				
				
				if(count>=10&&count<=20){
					num=(count)*5;
				}else if(count>20){
					num=100;
				}
				
				if(PetApplication.myUser.coinCount<num){
//					DialogNote dialog=new DialogNote(popupParent, NewShowTopicActivity.this, black_layout, 1);
					/*Intent intent=new Intent(NewShowTopicActivity.this,DialogNoteActivity.class);
					intent.putExtra("mode", 10);
					intent.putExtra("info", "钱包君告急！挣够金币再来捧萌星吧");
					startActivity(intent);*/
					 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
							
							@Override
							public void onClose() {
								// TODO Auto-generated method stub
							}
							
							@Override
							public void onButtonTwo() {
								// TODO Auto-generated method stub
								Intent intent=new Intent(NewShowTopicActivity.this,ChargeActivity.class);
								startActivity(intent);
							}
							
							@Override
							public void onButtonOne() {
								// TODO Auto-generated method stub
							}
						};
						 Intent intent=new Intent(NewShowTopicActivity.this,Dialog4Activity.class);
						 intent.putExtra("mode", 8);
						 intent.putExtra("num", num);
						 startActivity(intent);
						 shareLayout.setVisibility(View.INVISIBLE);
					return;
			}
			
			
			DialogJoinKingdom dialog=new DialogJoinKingdom(popupParent, NewShowTopicActivity.this, black_layout, petPicture.animal);
			dialog.setResultListener(new ResultListener() {
				
				@Override
				public void getResult(boolean isSuccess) {
					// TODO Auto-generated method stub
					petPicture.animal.hasJoinOrCreate=isSuccess;
//					pengta_tv.setVisibility(View.GONE);
				}
			});
			shareLayout.setVisibility(View.INVISIBLE);
			}
		});
		
		
		chatTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(NewShowTopicActivity.this,popupParent,black_layout)){
					shareLayout.setVisibility(View.INVISIBLE);
					return;
				}
				
				Intent intent=new Intent(NewShowTopicActivity.this,com.aidigame.hisun.imengstar.huanxin.ChatActivity.class);
				MyUser user=new MyUser();
				user.userId=petPicture.animal.master_id;
				user.u_iconUrl=petPicture.animal.u_tx;
				user.u_nick=petPicture.animal.u_name;
				intent.putExtra("user",user);
				NewShowTopicActivity.this.startActivity(intent);
				shareLayout.setVisibility(View.INVISIBLE);
			}
		});
		reportTv.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(NewShowTopicActivity.this,WarningDialogActivity.class);
		intent.putExtra("mode", 2);//2
		intent.putExtra("img_id", petPicture.img_id);
		NewShowTopicActivity.this.startActivity(intent);
		
		/*handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				moreLayout.setVisibility(View.INVISIBLE);
			}
		}, 500);*/
		shareLayout.setVisibility(View.INVISIBLE);
	}
});
		
		
	}
	
	
	
	/**
	 * 分享 按钮
	 */
	private  RelativeLayout shareLayout;
	private void initShareView() {
		// TODO Auto-generated method stub
		
		LinearLayout weixinLayout=(LinearLayout)findViewById(R.id.imageView22);
		LinearLayout friendLayout=(LinearLayout)findViewById(R.id.imageView32);
		LinearLayout xinlangLayout=(LinearLayout)findViewById(R.id.imageView42);
		shareLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareLayout.setVisibility(View.GONE);
			}
		});
		weixinLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareLayout.setVisibility(View.GONE);
				weixinShare();
				
			}
		});
		friendLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareLayout.setVisibility(View.GONE);
				friendShare();
				
			}
		});
		xinlangLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareLayout.setVisibility(View.GONE);
				xinlangShare();
				
			}
		});
	}
	
	
	/**
	 * 跟新界面信息
	 */
	private void updateInfo() {
		// TODO Auto-generated method stub
		/*
		 * 正面显示信息
		 */
         if(!StringUtil.isEmpty(petPicture.cmt)){
        	 Spannable span = NewSmileUtils.getSmiledText(this, petPicture.cmt);
 			// 设置内容
 			desTv.setText(span, BufferType.SPANNABLE);
         }else{
        	 desTv.setVisibility(View.GONE);
         }
         if(!StringUtil.isEmpty(petPicture.topic_name)&&!"##".equals(petPicture.topic_name)){
        	 topicTv.setText("#"+petPicture.topic_name+"#");
        	 topicTv.setVisibility(View.VISIBLE);
         }else{
        	 topicTv.setVisibility(View.GONE);
         }
       timeTv.setText(StringUtil.judgeTime(petPicture.create_time));
       
       
       
       /*
        *反面显示信息 
        */
       loadIcon();
       
       if(petPicture.animal.a_gender==1){
    	   genderIv.setImageResource(R.drawable.male1);
       }else{
    	   genderIv.setImageResource(R.drawable.female1);
       }
       
       
       if(PetApplication.myUser!=null&&petPicture.likers!=null&&petPicture.likers.contains(""+PetApplication.myUser.userId)){
    	   bottomLikeIv.setImageResource(R.drawable.show_topic_like_press);
		}else{
			bottomLikeIv.setImageResource(R.drawable.show_topic_like);
		}
       
       
       
       
       petNameTv.setText(petPicture.animal.pet_nickName);
       petRaceTv.setText(petPicture.animal.race);
       userNameTv.setText(petPicture.animal.u_name);
       likeNumTv1.setText(""+petPicture.likes);
       triangleTv1.setText(""+petPicture.likes);
       giftTv1.setText(""+petPicture.gifts);
       triangleTv2.setText(""+petPicture.gifts);
       if(petPicture.commentsList!=null){
    	   commentTv1.setText(""+petPicture.commentsList.size());
    	   triangleTv3.setText(""+petPicture.commentsList.size());
       }else{
    	   commentTv1.setText("0");
    	   triangleTv3.setText("0");
    	   petPicture.commentsList=new ArrayList<PetPicture.Comments>();
       }
       shareTv1.setText(""+petPicture.shares);
       triangleTv4.setText(""+petPicture.shares);
//       downLoadUserInfo(1);
       
       
       if(mode==3){
    	   downLoadUserInfo(3);
       }else if(mode==1){
    	   downLoadUserInfo(1);
       }else{
    	   if(petPicture.commentsList!=null&&petPicture.commentsList.size()>0){
        	   downLoadUserInfo(3);
           }else if(petPicture.gifts>0){
        	   downLoadUserInfo(2);
           }else if(petPicture.likes>0){
        	   downLoadUserInfo(1);
           }else if(petPicture.shares>0){
        	   downLoadUserInfo(4);
           }else {
        	   downLoadUserInfo(3);
           } 
       }
       
      
       
       
      oneLayout.post(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int h=oneLayout.getMeasuredHeight();
			LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)twoLayout.getLayoutParams();
			if(param==null){
				param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
			}
          int minH1=Constants.screen_height*3/4;
			
			if(h<minH1){
				h=minH1;
			}
			param.height=h;
			twoLayout.setLayoutParams(param);
			int h1=scrollView.getMeasuredHeight();
//			FrameLayout.LayoutParams param1=(FrameLayout.LayoutParams)myScrollView.getLayoutParams();
			FrameLayout.LayoutParams param1=(FrameLayout.LayoutParams)reverseSideLayout.getLayoutParams();
			if(param1==null){
				param1=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,h1);
			}
			int minH=Constants.screen_height*3/4;
			
			if(h1<minH){
				h1=minH;
			}
			param.height=h1;
			param1.height=h1;
//			myScrollView.setLayoutParams(param1);
			reverseSideLayout.setLayoutParams(param1);
			listView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					/*int start=getResources().getDimensionPixelSize(R.dimen.title_height);
					
					FrameLayout.LayoutParams param2=(FrameLayout.LayoutParams)listView.getLayoutParams();
					
					if(param2==null){
						param2=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,petPicture.likeUsersList.size()*start);
					}
					param2.height=petPicture.likeUsersList.size()*start;
					listView.setLayoutParams(param2);*/
					int h=scrollView.getMeasuredHeight();
					h=h-getResources().getDimensionPixelSize(R.dimen.view_padding)*2;
					if(h<0)h=0;
					LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)infoFrameLayout.getLayoutParams();
					if(param==null){
						param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
					}
					int minH=Constants.screen_height*3/4;
					
					if(h<minH){
						h=minH;
					}
					param.height=h;
					infoFrameLayout.setLayoutParams(param);
					/*if(0<=Constants.screen_width+10-scrollView.getMeasuredHeight()-getResources().getDimensionPixelSize(R.dimen.shadow_width)&&Constants.screen_width+10-scrollView.getMeasuredHeight()-getResources().getDimensionPixelSize(R.dimen.shadow_width)<getResources().getDimensionPixelSize(R.dimen.view_padding)){
						paddingView.setVisibility(View.VISIBLE);
					};*/
				}
			});
		}
	});
	}
	



	      private  	int current_Middle_tab_position=3;
	      @Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bottom_iv_comment:
				showCommentEditor();
				break;
			case R.id.bottom_iv_like:
				if(petPicture.animal==null){
					Toast.makeText(this, "网络不给力，数据加载中", Toast.LENGTH_LONG).show();
					return;
				}
				actionLike();
				break;
			case R.id.bottom_iv_gift:
				if(petPicture.animal==null){
					Toast.makeText(this, "网络不给力，数据加载中", Toast.LENGTH_LONG).show();
					return;
				}
				sendGift();
				break;
			case R.id.bottom_iv_more:
				if(petPicture.animal==null){
					Toast.makeText(this, "网络不给力，数据加载中", Toast.LENGTH_LONG).show();
					return;
				}
				shareLayout.setVisibility(View.VISIBLE);
				 android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
				 ShareDialogFragment sdf=new ShareDialogFragment(petPicture,popupParent,black_layout,1);
				 sdf.setShareDialogFragmentResultListener(new ShareDialogFragmentResultListener() {
					
					@Override
					public void onResult(boolean isSuccess) {
						// TODO Auto-generated method stub
						if(isSuccess){
							shareTv1.setText(""+petPicture.shares);
							if(current_show==SHOW_SHARE_LIST){
								showShareUsersList();
							}
						}
						shareLayout.setVisibility(View.GONE);
					}
				});
				 ft.replace(R.id.sharelayout,sdf );
				ft.commit();
				break;
			case R.id.show_topic_close_iv1:
				close();
				break;
			case R.id.show_topic_usericon:
				/*if(UserDossierActivity.userDossierActivity!=null){
					if(UserDossierActivity.userDossierActivity.loadedImage1!=null&&!UserDossierActivity.userDossierActivity.loadedImage1.isRecycled()){
						UserDossierActivity.userDossierActivity.loadedImage1.recycle();
						UserDossierActivity.userDossierActivity.loadedImage1=null;
					}
					if(UserDossierActivity.userDossierActivity.loadedImage2!=null&&!UserDossierActivity.userDossierActivity.loadedImage2.isRecycled()){
						UserDossierActivity.userDossierActivity.loadedImage2.recycle();
						UserDossierActivity.userDossierActivity.loadedImage2=null;
					}
					UserDossierActivity.userDossierActivity.finish();
				}
				Intent intent1=new Intent(this,UserDossierActivity.class);
				User user=new User();
				user.currentAnimal=petPicture.animal;
				user.userId=petPicture.animal.master_id;
				intent1.putExtra("user", user);
				this.startActivity(intent1);*/
				Intent intent1=new Intent(this,UserCardActivity.class);
				MyUser user=new MyUser();
				user.currentAnimal=petPicture.animal;
				user.userId=petPicture.animal.master_id;
				intent1.putExtra("user", user);
				intent1.putExtra("from", 1);
				this.startActivity(intent1);
				break;
			case R.id.show_topic_peticon:
				if(NewPetKingdomActivity.petKingdomActivity!=null){
					if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
						NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
						NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
					}
					if(NewPetKingdomActivity.petKingdomActivity.loadedImage2!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage2.isRecycled()){
						NewPetKingdomActivity.petKingdomActivity.loadedImage2.recycle();
						NewPetKingdomActivity.petKingdomActivity.loadedImage2=null;
					}
					NewPetKingdomActivity.petKingdomActivity.finish();
					NewPetKingdomActivity.petKingdomActivity=null;
				}
				Intent intent2=new Intent(this,NewPetKingdomActivity.class);
				intent2.putExtra("animal", petPicture.animal);
				this.startActivity(intent2);
				break;
			case R.id.show_topic_close_iv2:
				close();
				break;
			case R.id.middle_tab_iv1:
				if(current_Middle_tab_position==1){
					if(petPicture.animal==null){
						Toast.makeText(this, "网络不给力，数据加载中", Toast.LENGTH_LONG).show();
						return;
					}
					actionLike();
				}else{
					if(petPicture.likeUsersList==null){
						downLoadUserInfo(1);
					}else{
						showLikeUsersList();
					}
				}
				
				
				break;
			case R.id.middle_tab_iv2:
				if(current_Middle_tab_position==2){
					if(petPicture.animal==null){
						Toast.makeText(this, "网络不给力，数据加载中", Toast.LENGTH_LONG).show();
						return;
					}
					sendGift();
				}else{
					 if(petPicture.giftUsersList==null){
		                	downLoadUserInfo(2);
						}else{
							showGiftUsersList();
						}
				}
               
				
				break;
			case R.id.middle_tab_iv3:
				if(current_Middle_tab_position==3){
					showCommentEditor();
				}else{
					if(!loadIcon){
	                	 downLoadUserInfo(3);
	                 }else{
	                	 showCommentsUsersList();
	                 }
				}
                 
               
				
				break;
			case R.id.middle_tab_iv4:
                if(current_Middle_tab_position==4){
                	shareLayout.setVisibility(View.VISIBLE);
                }else{
                	if(petPicture.shareUsersList==null){
                    	downLoadUserInfo(4);
    				}else{
    					showShareUsersList();
    				}
                }
                
				
				break;
			case R.id.send_comment:
				if(petPicture.animal==null){
					Toast.makeText(this, "网络不给力，数据加载中", Toast.LENGTH_LONG).show();
					return;
				}
				sendComment();
				break;
			case R.id.show_topic_iv:
				if(ShowPictureActivity.showPictureActivity!=null){
					ShowPictureActivity.showPictureActivity.finish();
				}
				Intent intent=new Intent(NewShowTopicActivity.this,ShowPictureActivity.class);
				intent.putExtra("url", petPicture.url);
				NewShowTopicActivity.this.startActivity(intent);
				shareLayout.setVisibility(View.INVISIBLE);
				break;
			case R.id.guide1:
				guideIv1.setImageDrawable(new BitmapDrawable());
				guideIv1.setVisibility(View.GONE);
				
				showReverseSide();
				
				
				break;
				
			}
		}
	      /**
	       * 显示反面
	       */
	      private    void showReverseSide(){
	    	  Animation anim1=AnimationUtils.loadAnimation(NewShowTopicActivity.this, R.anim.anim_scale_small);
				final Animation anim2=AnimationUtils.loadAnimation(NewShowTopicActivity.this, R.anim.anim_scale_big);
				
				anim1.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						LogUtil.i("mi", "正面anim1开始");
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						mContainer.clearAnimation();
						LogUtil.i("mi", "正面anim1结束");
						scrollView.setVisibility(View.GONE);
//						myScrollView.setVisibility(View.VISIBLE);
						reverseSideLayout.setVisibility(View.VISIBLE);
						mContainer.startAnimation(anim2);
						
					}
				});
              anim2.setAnimationListener(new AnimationListener() {
					
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
						hasStart=false;
						current_page=-1;
						if(petPicture.commentsList!=null&&petPicture.commentsList.size()>0&&current_show==SHOW_COMMENT_LIST){
							SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
							SharedPreferences.Editor e=sp.edit();
							boolean guide4=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE4, true);
							if(guide4){
								guideIv1.setImageResource(R.drawable.guide4);
								guideIv1.setVisibility(View.VISIBLE);
								
								e.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE4, false);
								e.commit();
								guideIv1.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										guideIv1.setImageDrawable(new BitmapDrawable());
										guideIv1.setVisibility(View.GONE);
									}
								});
							}else{
								guideIv1.setVisibility(View.GONE);
							}
						}
					}
				});
				mContainer.startAnimation(anim1);
				positive=false;
	      }
	      private   void close(){

			if(isTaskRoot()){
				if(HomeActivity.homeActivity!=null){
					Intent intent=HomeActivity.homeActivity.getIntent();
					if(intent!=null){
						this.startActivity(intent);
						/*if(bmp!=null){
							if(!bmp.isRecycled())
							bmp.recycle();
							bmp=null;
						}*/
						imageView.setImageDrawable(null);
						newShowTopicActivity=null;
						
						
						finish();
						System.gc();
						return;
					}
					
					
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
				}else{
					Intent intent=new Intent(this,HomeActivity.class);
					this.startActivity(intent);
				}
			}
			/*if(bmp!=null){
				if(!bmp.isRecycled())
				bmp.recycle();
				bmp=null;
			}*/
			imageView.setImageDrawable(null);
			newShowTopicActivity=null;
			
			
			finish();
			System.gc();
		}
		
		/**
		 * 下载用户信息
		 */
	      private   void downLoadUserInfo(){
			if(PetApplication.isSuccess){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//加载用户信息
						MyUser user=HttpUtil.info(NewShowTopicActivity.this,null,-1);
						
					}
				}).start();
			}
		}
		/**
		 * 展示图片
		 */
		private void displayImage() {
			// TODO Auto-generated method stub
			final BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds=false;
//			options.inSampleSize=StringUtil.topicImageGetScaleByDPI(this);
			options.inSampleSize=StringUtil.getScaleByDPI(this);
			LogUtil.i("me", "照片详情页面Topic图片缩放比例"+StringUtil.topicImageGetScaleByDPI(this));
			if(StringUtil.topicImageGetScaleByDPI(this)>=2){
				options.inPreferredConfig=Bitmap.Config.ARGB_4444;
			}else{
				options.inPreferredConfig=Bitmap.Config.ARGB_8888;
			}
			int w=Constants.screen_width-getResources().getDimensionPixelSize(R.dimen.one_dip)*50;
			
			if(new File(Constants.Picture_Topic_Path+File.separator+petPicture.url+"@"+w+"w_"+"1l.jpg").exists()){
				bmpPath=Constants.Picture_Topic_Path+File.separator+petPicture.url+"@"+w+"w_"+"1l.jpg";
				petPicture.petPicture_path=Constants.Picture_Topic_Path+File.separator+File.separator+petPicture.url+"@"+w+"w_"+"1l.jpg";
				imageLayout.setVisibility(View.VISIBLE);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				/*imageView.setImageBitmap(BitmapFactory.decodeFile(Constants.Picture_Topic_Path+File.separator+petPicture.url+"@"+w+"w_"+"1l.jpg"));
				oneLayout.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int h=oneLayout.getMeasuredHeight();
						
						if(h<0)h=0;
						LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)twoLayout.getLayoutParams();
						if(param==null){
							param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
						}
						 int minH1=Constants.screen_height*3/4;
							
							if(h<minH1){
								h=minH1;
							}
						param.height=h;
						twoLayout.setLayoutParams(param);
						int h1=scrollView.getMeasuredHeight();
						FrameLayout.LayoutParams param1=(FrameLayout.LayoutParams)reverseSideLayout.getLayoutParams();
						if(param1==null){
							param1=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,h1);
						}
						param1.height=h1;
						reverseSideLayout.setLayoutParams(param1);
						
						listView.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								int h=scrollView.getMeasuredHeight();
								h=h-getResources().getDimensionPixelSize(R.dimen.view_padding)*2;
								if(h<0)h=0;
								LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)infoFrameLayout.getLayoutParams();
								if(param==null){
									param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
								}
								param.height=h;
								infoFrameLayout.setLayoutParams(param);
								
								if(0<=Constants.screen_width+10-scrollView.getMeasuredHeight()-getResources().getDimensionPixelSize(R.dimen.shadow_width)&&Constants.screen_width+10-scrollView.getMeasuredHeight()-getResources().getDimensionPixelSize(R.dimen.shadow_width)<getResources().getDimensionPixelSize(R.dimen.view_padding)){
									paddingView.setVisibility(View.VISIBLE);
								};
								handler.postAtTime(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
									}
								}, 1000);
								
							}
						});
							
					}
			  });
				*/
				mImageFetcher.setWidth(imageView.getMeasuredWidth());
				
				mImageFetcher.setImageCache(new ImageCache(this, new ImageCacheParams(petPicture.url+"@"+w+"w_"+"1l.jpg")));
				mImageFetcher.IP=mImageFetcher.UPLOAD_THUMBMAIL_IMAGE;
				mImageFetcher.setLoadCompleteListener(new ImageWorker.LoadCompleteListener(){
					@Override
					public void  onComplete(Bitmap bitmap) {
						// TODO Auto-generated method stub
						LogUtil.i("me", "获取图片宽高，宽="+options.outWidth+",高="+options.outHeight);
						LogUtil.i("me", "照片详情页面，图片开始下载");
						
						imageLayout.setVisibility(View.VISIBLE);
						imageView.setScaleType(ScaleType.CENTER_CROP);
						if(bitmap!=null){
//							bmpPath=mImageFetcher.getFilePath(NewShowTopicActivity.this,petPicture.url);
						}
						LinearLayout.LayoutParams param2=(LinearLayout.LayoutParams)imageView.getLayoutParams();
						if(param2==null){
							param2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						}
						int ivW=imageView.getMeasuredWidth();
						param2.height=(int)(ivW*1f/bitmap.getWidth()*bitmap.getHeight());
						imageView.setLayoutParams(param2);
						oneLayout.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									int h=oneLayout.getMeasuredHeight();
									
									if(h<0)h=0;
									LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)twoLayout.getLayoutParams();
									if(param==null){
										param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
									}
									 int minH1=Constants.screen_height*3/4;
										
										if(h<minH1){
											h=minH1;
										}
									param.height=h;
									twoLayout.setLayoutParams(param);
									int h1=scrollView.getMeasuredHeight();
//									FrameLayout.LayoutParams param1=(FrameLayout.LayoutParams)myScrollView.getLayoutParams();
									FrameLayout.LayoutParams param1=(FrameLayout.LayoutParams)reverseSideLayout.getLayoutParams();
									if(param1==null){
										param1=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,h1);
									}
									param1.height=h1;
//									myScrollView.setLayoutParams(param1);
									reverseSideLayout.setLayoutParams(param1);
									
									listView.post(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											/*int start=getResources().getDimensionPixelSize(R.dimen.title_height);
											FrameLayout.LayoutParams param2=(FrameLayout.LayoutParams)listView1.getLayoutParams();
											
											if(param2==null){
												param2=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,petPicture.likeUsersList.size()*start);
											}
											if(petPicture.likeUsersList!=null)
											param2.height=petPicture.likeUsersList.size()*start;
											listView1.setLayoutParams(param2);*/
											int h=scrollView.getMeasuredHeight();
											h=h-getResources().getDimensionPixelSize(R.dimen.view_padding)*2;
											if(h<0)h=0;
											LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)infoFrameLayout.getLayoutParams();
											if(param==null){
												param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
											}
											param.height=h;
											infoFrameLayout.setLayoutParams(param);
											
											if(0<=Constants.screen_width+10-scrollView.getMeasuredHeight()-getResources().getDimensionPixelSize(R.dimen.shadow_width)&&Constants.screen_width+10-scrollView.getMeasuredHeight()-getResources().getDimensionPixelSize(R.dimen.shadow_width)<getResources().getDimensionPixelSize(R.dimen.view_padding)){
												paddingView.setVisibility(View.VISIBLE);
											};
											handler.postAtTime(new Runnable() {
												
												@Override
												public void run() {
													// TODO Auto-generated method stub
													/*imageViewTemp.setVisibility(View.GONE);
													imageViewTemp.setImageDrawable(null);*/
												}
											}, 1000);
											
										}
									});
										
								}
						  });
					}
					@Override
					public void getPath(String path) {
						// TODO Auto-generated method stub
						bmpPath=path;
						petPicture.petPicture_path=path;
					}
				});
				mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/petPicture.url+"@"+w+"w_"+"1l.jpg", imageView,/*options*/null);
				
				return;
			}
			
			LogUtil.i("mi", "petPicture.petPicture_path===="+petPicture.petPicture_path);
			if(!StringUtil.isEmpty(petPicture.petPicture_path)&&new File(Constants.Picture_Topic_Path+File.separator+petPicture.petPicture_path).exists()){
				imageLayout.setVisibility(View.INVISIBLE);
				imageViewTemp.setScaleType(ScaleType.CENTER_CROP);
				
				Bitmap bmp=BitmapFactory.decodeFile(Constants.Picture_Topic_Path+File.separator+petPicture.petPicture_path);
				Matrix matrix=new Matrix();
				matrix.setScale(w/(bmp.getWidth()*1f), w/(bmp.getWidth()*1f));
//				imageViewTemp.setImageBitmap((BitmapFactory.decodeFile(Constants.Picture_Topic_Path+File.separator+petPicture.petPicture_path)));;
				imageViewTemp.setImageBitmap((Bitmap.createBitmap(bmp, 0,0, bmp.getWidth(), bmp.getHeight(), matrix, false)));;
			}else if(!StringUtil.isEmpty(petPicture.petPicture_path)&&new File(petPicture.petPicture_path).exists()){
				imageLayout.setVisibility(View.INVISIBLE);
				imageViewTemp.setScaleType(ScaleType.CENTER_CROP);
//				imageViewTemp.setImageBitmap((BitmapFactory.decodeFile(petPicture.petPicture_path)));;
				
				
				Bitmap bmp=BitmapFactory.decodeFile(petPicture.petPicture_path);
				Matrix matrix=new Matrix();
				matrix.setScale(w/(bmp.getWidth()*1f), w/(bmp.getWidth()*1f));
				imageViewTemp.setImageBitmap((Bitmap.createBitmap(bmp, 0,0, bmp.getWidth(), bmp.getHeight(), matrix, false)));;
			}
			options.inPurgeable=true;
			options.inInputShareable=true;
			mImageFetcher.setWidth(imageView.getMeasuredWidth());
			
			mImageFetcher.setImageCache(new ImageCache(this, new ImageCacheParams(petPicture.url+"@"+w+"w_"+"1l.jpg")));
			mImageFetcher.IP=mImageFetcher.UPLOAD_THUMBMAIL_IMAGE;
			mImageFetcher.setLoadCompleteListener(new ImageWorker.LoadCompleteListener(){
				@Override
				public void  onComplete(Bitmap bitmap) {
					// TODO Auto-generated method stub
					LogUtil.i("me", "获取图片宽高，宽="+options.outWidth+",高="+options.outHeight);
					LogUtil.i("me", "照片详情页面，图片开始下载");
					
					imageLayout.setVisibility(View.VISIBLE);
					imageView.setScaleType(ScaleType.CENTER_CROP);
					if(bitmap!=null){
//						bmpPath=mImageFetcher.getFilePath(NewShowTopicActivity.this,petPicture.url);
					}
					LinearLayout.LayoutParams param2=(LinearLayout.LayoutParams)imageView.getLayoutParams();
					if(param2==null){
						param2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					}
					int ivW=imageView.getMeasuredWidth();
					param2.height=(int)(ivW*1f/bitmap.getWidth()*bitmap.getHeight());
					imageView.setLayoutParams(param2);
					oneLayout.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								int h=oneLayout.getMeasuredHeight();
								
								if(h<0)h=0;
								LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)twoLayout.getLayoutParams();
								if(param==null){
									param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
								}
								 int minH1=Constants.screen_height*3/4;
									
									if(h<minH1){
										h=minH1;
									}
								param.height=h;
								twoLayout.setLayoutParams(param);
								int h1=scrollView.getMeasuredHeight();
//								FrameLayout.LayoutParams param1=(FrameLayout.LayoutParams)myScrollView.getLayoutParams();
								FrameLayout.LayoutParams param1=(FrameLayout.LayoutParams)reverseSideLayout.getLayoutParams();
								if(param1==null){
									param1=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,h1);
								}
								param1.height=h1;
//								myScrollView.setLayoutParams(param1);
								reverseSideLayout.setLayoutParams(param1);
								
								listView.post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										/*int start=getResources().getDimensionPixelSize(R.dimen.title_height);
										FrameLayout.LayoutParams param2=(FrameLayout.LayoutParams)listView1.getLayoutParams();
										
										if(param2==null){
											param2=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,petPicture.likeUsersList.size()*start);
										}
										if(petPicture.likeUsersList!=null)
										param2.height=petPicture.likeUsersList.size()*start;
										listView1.setLayoutParams(param2);*/
										int h=scrollView.getMeasuredHeight();
										h=h-getResources().getDimensionPixelSize(R.dimen.view_padding)*2;
										if(h<0)h=0;
										LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)infoFrameLayout.getLayoutParams();
										if(param==null){
											param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
										}
										param.height=h;
										infoFrameLayout.setLayoutParams(param);
										
										if(0<=Constants.screen_width+10-scrollView.getMeasuredHeight()-getResources().getDimensionPixelSize(R.dimen.shadow_width)&&Constants.screen_width+10-scrollView.getMeasuredHeight()-getResources().getDimensionPixelSize(R.dimen.shadow_width)<getResources().getDimensionPixelSize(R.dimen.view_padding)){
											paddingView.setVisibility(View.VISIBLE);
										};
										handler.postAtTime(new Runnable() {
											
											@Override
											public void run() {
												// TODO Auto-generated method stub
												/*imageViewTemp.setVisibility(View.GONE);
												imageViewTemp.setImageDrawable(null);*/
											}
										}, 1000);
										
									}
								});
									
							}
					  });
				}
				@Override
				public void getPath(String path) {
					// TODO Auto-generated method stub
					bmpPath=path;
					petPicture.petPicture_path=path;
				}
			});
			mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/petPicture.url+"@"+w+"w_"+"1l.jpg", imageView,/*options*/null);
			
		
		}
		/**
		 * 用户，宠物头像下载
		 */
		private void loadIcon() {
			// TODO Auto-generated method stub
			BitmapFactory.Options options=new BitmapFactory.Options();
			
			options.inJustDecodeBounds=false;
			options.inSampleSize=8;
			options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			DisplayImageOptions displayImageOptions1=new DisplayImageOptions
		            .Builder()
		            .showImageOnLoading(R.drawable.pet_icon)
		            .showImageOnFail(R.drawable.pet_icon)
			        .cacheInMemory(true)
			        .cacheOnDisc(true)
			        .bitmapConfig(Bitmap.Config.RGB_565)
			        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//			        .decodingOptions(options)
	                .build();
			ImageLoader imageLoader1=ImageLoader.getInstance();
			int w=getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
			imageLoader1.displayImage(Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+petPicture.animal.pet_iconUrl+"@"+w+"w_"+w+"h_0l.jpg", petIcon, displayImageOptions1);
			DisplayImageOptions displayImageOptions2=new DisplayImageOptions
		            .Builder()
		            .showImageOnLoading(R.drawable.user_icon)
		            .showImageOnFail(R.drawable.user_icon)
			        .cacheInMemory(true)
			        .cacheOnDisc(true)
			        .bitmapConfig(Bitmap.Config.RGB_565)
			        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//			        .decodingOptions(options)
	                .build();
			ImageLoader imageLoader2=ImageLoader.getInstance();
			imageLoader2.displayImage(Constants.USER_THUBMAIL_DOWNLOAD_TX+petPicture.animal.u_tx+"@"+w+"w_"+w+"h_0l.jpg", userIcon, displayImageOptions2);
			
		}
		/**
		 * 下载用户列表信息
		 * @param mode 1，点赞人列表；2送礼物人列表；4，分享人列表
		 */
		private   void downLoadUserInfo(final int mode){
			if(showProgress==null){
				showProgress=new ShowProgress(this, progressLayout);
			}else{
				showProgress.showProgress();
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ArrayList<MyUser> users=null;
					switch (mode) {
					case 1:
						users=HttpUtil.getOthersList(petPicture.likers, handler,NewShowTopicActivity.this,2);
						break;

					case 2:
						users=HttpUtil.getOthersList(petPicture.senders, handler,NewShowTopicActivity.this,1);
						break;
					case 3:
						users=HttpUtil.getOthersList(petPicture.comment_ids, handler,NewShowTopicActivity.this,1);
						break;

					case 4:
						users=HttpUtil.getOthersList(petPicture.share_ids, handler,NewShowTopicActivity.this,4);
						break;
					}
					if(users==null)users=new ArrayList<MyUser>();
					final ArrayList<MyUser> temp=users;
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showProgress.progressCancel();
								switch (mode) {
								case 1:
									petPicture.likeUsersList=temp;
									showLikeUsersList();
									break;

								case 2:
									petPicture.giftUsersList=temp;
									showGiftUsersList();
									break;
								case 3:
									if(petPicture.commentsList!=null){
										for(int i=0;i<temp.size();i++){
											for(int j=0;j<petPicture.commentsList.size();j++){
												if(temp.get(i).userId==petPicture.commentsList.get(j).usr_id){
													petPicture.commentsList.get(j).usr_tx=temp.get(i).u_iconUrl;
												}
											}
										}
										showCommentsUsersList();
									}
									
									break;

								case 4:
									petPicture.shareUsersList=temp;
									showShareUsersList();
									break;
								}
						}
					});
				}
			}).start();
		}
		
		/**
		 * 显示点赞用户列表
		 */
		private   void showLikeUsersList(){
			current_Middle_tab_position=1;
			if(current_show!=SHOW_LIKE_LIST){
				listView.removeAllViews();
				topicUsersListAdapter=new TopicUsersListAdapter(this, petPicture.likeUsersList,petPicture.animal);
				listView.setAdapter(topicUsersListAdapter);
				
				current_show=SHOW_LIKE_LIST;
			}else{
				if(topicUsersListAdapter==null){
					topicUsersListAdapter=new TopicUsersListAdapter(this, petPicture.likeUsersList,petPicture.animal);
					listView.setAdapter(topicUsersListAdapter);
				}else{
					
					topicUsersListAdapter.update(petPicture.likeUsersList);
					topicUsersListAdapter.notifyDataSetChanged();
					listView.removeAllViews();
					listView.update(0);
				}
				
			}
			topicUsersListAdapter.setGestureListener(new GestureListener() {
				
				@Override
				public void onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
						float arg3) {
					// TODO Auto-generated method stub
					onTouchScroll(arg0,arg1,arg2,arg3);
				}
			});
				triangleIvInvisible();
				triangleIv1.setVisibility(View.VISIBLE);
				listView.setVisibility(View.VISIBLE);
				listView.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int start=getResources().getDimensionPixelSize(R.dimen.title_height);
						ScrollView.LayoutParams param2=(ScrollView.LayoutParams)listView.getLayoutParams();
						
						if(param2==null){
							param2=new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,petPicture.likeUsersList.size()*start);
						}
						if(petPicture.likeUsersList==null)petPicture.likeUsersList=new ArrayList<MyUser>();
						param2.height=(petPicture.likeUsersList.size())*start;
						listView.setLayoutParams(param2);
						
					}
				});
		}
		/**
		 * 显示送礼物用户列表
		 */
		private   void showGiftUsersList(){
			current_Middle_tab_position=2;
			if(current_show!=SHOW_GIFT_LIST){
				listView.removeAllViews();
				topicUsersListAdapter=new TopicUsersListAdapter(this, petPicture.giftUsersList,petPicture.animal);
				listView.setAdapter(topicUsersListAdapter);
			
				current_show=SHOW_GIFT_LIST;
			}else{
                if(topicUsersListAdapter==null){
                	topicUsersListAdapter=new TopicUsersListAdapter(this, petPicture.giftUsersList,petPicture.animal);
    				listView.setAdapter(topicUsersListAdapter);
				}else{
					topicUsersListAdapter.update(petPicture.giftUsersList);
					topicUsersListAdapter.notifyDataSetChanged();
					listView.removeAllViews();
					listView.update(0);
				}
				
			}
            topicUsersListAdapter.setGestureListener(new GestureListener() {
				
				@Override
				public void onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
						float arg3) {
					// TODO Auto-generated method stub
					onTouchScroll(arg0,arg1,arg2,arg3);
				}
			});
				triangleIvInvisible();
				triangleIv2.setVisibility(View.VISIBLE);
				listView.setVisibility(View.VISIBLE);
				listView.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						/*int start=getResources().getDimensionPixelSize(R.dimen.title_height);
						FrameLayout.LayoutParams param2=(FrameLayout.LayoutParams)listView.getLayoutParams();
						
						if(param2==null){
							param2=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,petPicture.giftUsersList.size()*start);
						}
						param2.height=petPicture.giftUsersList.size()*start;
						listView.setLayoutParams(param2);*/
					}
				});
		}
		/**
		 * 显示分享用户列表
		 */
		private   void showShareUsersList(){
			current_Middle_tab_position=4;
			/*
			 * 分享用户数据列表
			 */
			if(current_show!=SHOW_SHARE_LIST){
				listView.removeAllViews();
				topicUsersListAdapter=new TopicUsersListAdapter(this, petPicture.shareUsersList,petPicture.animal);
				listView.setAdapter(topicUsersListAdapter);
				
				current_show=SHOW_SHARE_LIST;
			}else{
                if(topicUsersListAdapter==null){
                	topicUsersListAdapter=new TopicUsersListAdapter(this, petPicture.shareUsersList,petPicture.animal);
    				listView.setAdapter(topicUsersListAdapter);
				}else{
					topicUsersListAdapter.update(petPicture.shareUsersList);
					topicUsersListAdapter.notifyDataSetChanged();
					listView.removeAllViews();
					listView.update(0);
				}
				
			}
             topicUsersListAdapter.setGestureListener(new GestureListener() {
				
				@Override
				public void onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
						float arg3) {
					// TODO Auto-generated method stub
					onTouchScroll(arg0,arg1,arg2,arg3);
				}
			});
				triangleIvInvisible();
				triangleIv4.setVisibility(View.VISIBLE);
				listView.setVisibility(View.VISIBLE);
				listView.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						/*int start=getResources().getDimensionPixelSize(R.dimen.title_height);
						FrameLayout.LayoutParams param2=(FrameLayout.LayoutParams)listView.getLayoutParams();
						
						if(param2==null){
							param2=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,petPicture.giftUsersList.size()*start);
						}
						param2.height=petPicture.giftUsersList.size()*start;
						listView.setLayoutParams(param2);*/
					}
				});
		}
		/**
		 * 显示评论用户列表
		 */
		private  	boolean loadIcon=false;
		private   void showCommentsUsersList(){
			current_Middle_tab_position=3;
			/*
			 * 分享用户数据列表
			 */
			if(current_show!=SHOW_COMMENT_LIST){
				listView.removeAllViews();
				commentListViewAdapter=new CommentListViewAdapter(this, petPicture.commentsList);
				listView.setAdapter(commentListViewAdapter);
				current_show=SHOW_COMMENT_LIST;
			}else{
                if(commentListViewAdapter==null){
                	commentListViewAdapter=new CommentListViewAdapter(this, petPicture.commentsList);
    				listView.setAdapter(commentListViewAdapter);
				}else{
					commentListViewAdapter.update(petPicture.commentsList);
					commentListViewAdapter.notifyDataSetChanged();
					listView.removeAllViews();
					listView.update(0);
				}
				
			}
			
			commentListViewAdapter.setClickUserName(new ClickUserName() {
				
				@Override
				public void clickUserName(Comments cmt) {
					// TODO Auto-generated method stub
					if(!UserStatusUtil.isLoginSuccess(NewShowTopicActivity.this,popupParent,black_layout))return;
					replyComment(cmt);
				}
				@Override
				public void reportComment() {
					// TODO Auto-generated method stub
					Intent intent=new Intent(NewShowTopicActivity.this,WarningDialogActivity.class);
					intent.putExtra("mode", 1);//1
					intent.putExtra("img_id", petPicture.img_id);
					NewShowTopicActivity.this.startActivity(intent);
				}
				@Override
				public void onScroll(MotionEvent arg0, MotionEvent arg1,
						float arg2, float arg3) {
					// TODO Auto-generated method stub
					LogUtil.i("mi", "arg2==="+arg2+",arg3="+arg3+",hasStart="+hasStart);
					onTouchScroll(arg0,arg1,arg2,arg3);
				}
				
			});
				triangleIvInvisible();
				triangleIv3.setVisibility(View.VISIBLE);
				listView.setVisibility(View.VISIBLE);
				listView.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int start=getResources().getDimensionPixelSize(R.dimen.title_height);
						ScrollView.LayoutParams param2=(ScrollView.LayoutParams)listView.getLayoutParams();
						
						if(param2==null){
							param2=new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,ScrollView.LayoutParams.WRAP_CONTENT);
						}
						param2.height=(petPicture.commentsList.size())*start;
//						param2.height=ScrollView.LayoutParams.WRAP_CONTENT;
						listView.setLayoutParams(param2);
					}
				});
		}
		private   void triangleIvInvisible(){
			triangleIv1.setVisibility(View.INVISIBLE);
			triangleIv2.setVisibility(View.INVISIBLE);
			triangleIv3.setVisibility(View.INVISIBLE);
			triangleIv4.setVisibility(View.INVISIBLE);
		}
		private   void actionLike(){
			
			if(UserStatusUtil.isLoginSuccess(this,popupParent,black_layout)){
				if(showProgress!=null){
					showProgress.showProgress();
				}else{
					showProgress=new ShowProgress(this, progressLayout);
				}
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final boolean flag=HttpUtil.likeImage(petPicture,handler,NewShowTopicActivity.this);
						
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(flag){
									likeNumTv1.setText(""+petPicture.likes);
									bottomLikeIv.setImageResource(R.drawable.show_topic_like_press);
								/*	if(StringUtil.isEmpty(petPicture.likers)){
										petPicture.likers=""+Constants.user.userId;
									}else{
										petPicture.likers+=","+Constants.user.userId;
									}*/
									if(current_show==SHOW_LIKE_LIST){
										/*if(petPicture.likeUsersList==null){
											petPicture.likeUsersList=new ArrayList<User>();
											
										}*/
//										petPicture.likeUsersList.add(Constants.user);
										showLikeUsersList();
									}
									}else{
										Intent intent=new Intent(NewShowTopicActivity.this,DialogNoteActivity.class);
										intent.putExtra("mode", 10);
										intent.putExtra("info", "您已经点过赞了");
										startActivity(intent);
									}
									showProgress.progressCancel();
								}
							});
						
					}
				}).start();
			}
			
		}
		/**
		 * 送礼物
		 */
		private void sendGift() {
			// TODO Auto-generated method stub
			petPicture.animal.img_id=petPicture.img_id;
			if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.finish();
			Intent intent=new Intent(this,DialogGiveSbGiftActivity1.class);
			intent.putExtra("animal", petPicture.animal);
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
					if(!isSuccess)return;
					petPicture.gifts+=1;
					
					/*if(petPicture.gift_txUrlList==null){
						petPicture.gift_txUrlList=new ArrayList<String>();
						petPicture.gift_txUrlList.add(Constants.user.pet_iconUrl);
					}else{
						petPicture.like_txUrlList.add(Constants.user.pet_iconUrl);
					}*/
					
					if(StringUtil.isEmpty(petPicture.senders)){
						
						petPicture.senders=""+PetApplication.myUser.userId;
						petPicture.giftUsersList=new ArrayList<MyUser>();
						petPicture.giftUsersList.add(PetApplication.myUser);
					}else{
						petPicture.senders+=","+PetApplication.myUser.userId;
						if(petPicture.giftUsersList==null){
							petPicture.giftUsersList=new ArrayList<MyUser>();
						}
						if(!petPicture.giftUsersList.contains(PetApplication.myUser))
						petPicture.giftUsersList.add(PetApplication.myUser);
					}
					if(petPicture.gift_txUrlList!=null&&PetApplication.myUser.u_iconUrl!=null){
						if(!petPicture.gift_txUrlList.contains(PetApplication.myUser.u_iconUrl))
						petPicture.gift_txUrlList.add(PetApplication.myUser.u_iconUrl);
					}else if(PetApplication.myUser.u_iconUrl!=null){
						petPicture.gift_txUrlList=new ArrayList<String>();
						petPicture.gift_txUrlList.add(PetApplication.myUser.u_iconUrl);
					}
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							/*if(StringUtil.isEmpty(petPicture.senders)){
								petPicture.senders=""+Constants.user.userId;
							}else{
								petPicture.senders=","+Constants.user.userId;
							}*/
							if(current_show==SHOW_GIFT_LIST){
								/*if(petPicture.giftUsersList==null){
									petPicture.giftUsersList=new ArrayList<User>();
								}*/
//								petPicture.giftUsersList.add(Constants.user);
								showGiftUsersList();
							}
							giftTv1.setText(""+petPicture.gifts+"");
							
						}
					});
				}

				@Override
				public void unRegister() {
					// TODO Auto-generated method stub
					if(!UserStatusUtil.isLoginSuccess(NewShowTopicActivity.this, popupParent,black_layout)){
		        		
		        	};
				}
			};
			
		}
		
		/**
		 * 显示评论编辑框
		 */
		private  boolean replySb=false;
		private  boolean show_add_comment=false;
		private   void showCommentEditor(){
             
        	if(!UserStatusUtil.isLoginSuccess(this, popupParent,black_layout)){
        		return;
        	};
        	replySb=false;
        	emotionIv.setImageResource(R.drawable.chatting_biaoqing_btn_enable);
        	showSmile=true;
			if(show_add_comment){
				show_add_comment=false;
				mEditTextContent.setVisibility(View.INVISIBLE);
				addcommentLayout.setVisibility(View.GONE);
				
			}else{
				mEditTextContent.setVisibility(View.VISIBLE);
				show_add_comment=true;
//				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				mEditTextContent.setEnabled(true);
				mEditTextContent.setFocusable(true);
				mEditTextContent.setFocusableInTouchMode(true);
				if(mEditTextContent.requestFocus(EditText.FOCUS_FORWARD)){
					mEditTextContent.setSelection(0);;
					InputMethodManager m = (InputMethodManager)   
							mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
//					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				};
				Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
				addcommentLayout.setAnimation(animation);
				animation.start();
				addcommentLayout.setVisibility(View.VISIBLE);
				
				
			}

		}
		/**
		 * 发送评论
		 */
		private  	boolean canSend=false;
		private  boolean sendingComment=false;
		private   void sendComment(){
			if(!canSend){
        		InputMethodManager m = (InputMethodManager)   
        				mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
						m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
						addcommentLayout.setVisibility(View.GONE);
						mEditTextContent.setHint("写个评论呗");
						emotionLayout.setVisibility(View.GONE);
						show_add_comment=false;
						return;
        	}
        	progressLayout.removeAllViews();
        	if(!UserStatusUtil.isLoginSuccess(this, progressLayout,black_layout)){
        		return;
        	};
        	if(sendingComment){
        		Toast.makeText(this, "正在发送评论", Toast.LENGTH_LONG).show();
        		return;
        	}
        	final String comment=mEditTextContent.getText().toString();
        	mEditTextContent.setText("");
        	if(replySb){
        		mEditTextContent.setHint("回复"+cmt.name);
        	}else{
        		mEditTextContent.setHint("写个评论呗");
        	}
        	/*commentET.setFocusable(true);
        	commentET.requestFocus();
        	InputMethodManager im=(InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        	im.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        	*/
        	if(StringUtil.isEmpty(comment)){
        		Toast.makeText(this, "评论内容不能为空。", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	if("写个评论呗".equals(comment)){
        		Toast.makeText(this, "评论内容不能为空。", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	comment.trim();
//        	commentET.setEnabled(false);
        	
        	sendingComment=true;
        	
        	InputMethodManager im=(InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        	im.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0);
        	
        	
        	//测试 发表评论api
        	new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub\
					MyUser temp=null;
					if(replySb){
						temp=HttpUtil.sendComment(NewShowTopicActivity.this,comment, petPicture.img_id,cmt.usr_id,cmt.name,handler);
					}else{
						temp=HttpUtil.sendComment(NewShowTopicActivity.this,comment, petPicture.img_id,-1,"",handler);
					}
					final MyUser user=temp;
					runOnUiThread(new Runnable() {
						public void run() {
							if(user!=null){
								if(user.exp!=-1){
									PetPicture.Comments comments=new PetPicture.Comments();
									comments.usr_id=PetApplication.myUser.userId;
									comments.create_time=System.currentTimeMillis()/1000;
									comments.body=comment;
									comments.usr_tx=PetApplication.myUser.u_iconUrl;
									if(replySb){
										comments.isReply=true;
										comments.reply_id=cmt.usr_id;
										comments.reply_name=PetApplication.myUser.u_nick+"@"+cmt.name;
									}
									comments.name=PetApplication.myUser.u_nick;
									if(petPicture.commentsList==null){
										petPicture.commentsList=new ArrayList<PetPicture.Comments>();
										petPicture.commentsList.add(comments);
									}else{
										petPicture.commentsList.add(0, comments);
									}
									
									commentTv1.setText(""+petPicture.commentsList.size());
									mEditTextContent.setHint("写个评论呗");
									
									if(current_page==1){
										showReverseSide();
									}
									
									showCommentsUsersList();
									
									
									/*if(current_show==SHOW_COMMENT_LIST){
									commentListViewAdapter=new CommentListViewAdapter(NewShowTopicActivity.this, petPicture.commentsList);
									
									commentListViewAdapter.setClickUserName(new ClickUserName() {
										
										@Override
										public void clickUserName(Comments cmt) {
											// TODO Auto-generated method stub
											if(!UserStatusUtil.isLoginSuccess(NewShowTopicActivity.this,popupParent,black_layout))return;
											replyComment(cmt);
										}
										@Override
										public void reportComment() {
											// TODO Auto-generated method stub
											Intent intent=new Intent(NewShowTopicActivity.this,WarningDialogActivity.class);
											intent.putExtra("mode", 1);
											intent.putExtra("img_id", petPicture.img_id);
											NewShowTopicActivity.this.startActivity(intent);
										}
										@Override
										public void onScroll(MotionEvent arg0, MotionEvent arg1,
												float arg2, float arg3) {
											// TODO Auto-generated method stub
											LogUtil.i("mi", "arg2==="+arg2+",arg3="+arg3+",hasStart="+hasStart);
											onTouchScroll(arg0,arg1,arg2,arg3);
										}
									});
									listView.removeAllViews();
									listView.setAdapter(commentListViewAdapter);
									
									}else {
										showCommentsUsersList();
									}*/
									MobclickAgent.onEvent(NewShowTopicActivity.this, "comment");
//									Toast.makeText(ShowTopicActivity.this, "发表评论成功。", Toast.LENGTH_SHORT).show();
									/*InputMethodManager m = (InputMethodManager) commentET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
									if(m.isActive()){
										m.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
									}*/
//									getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
									
								}else{
									Toast.makeText(NewShowTopicActivity.this, "发表评论失败。", Toast.LENGTH_SHORT).show();
								     
								}
								sendingComment=false;
								mEditTextContent.setEnabled(true);
								addcommentLayout.setVisibility(View.GONE);
								emotionLayout.setVisibility(View.GONE);
								show_add_comment=false;
								replySb=false;
							}else{
								Toast.makeText(NewShowTopicActivity.this, "评论发送失败", Toast.LENGTH_LONG).show();
							}
							
						}
					});
				}
			}).start();
        	
		}
		private  	PetPicture.Comments cmt;
		private   void replyComment(PetPicture.Comments cmt){
			if(PetApplication.myUser!=null&&PetApplication.myUser.userId==cmt.usr_id){
				Toast.makeText(this, "请不要回复自己发的评论", Toast.LENGTH_LONG).show();;
				return;
			}
		
			replySb=true;
			this.cmt=cmt;
			mEditTextContent.setVisibility(View.VISIBLE);
			if(mEditTextContent.requestFocus(EditText.FOCUS_FORWARD)){
				mEditTextContent.setSelection(0);;
				InputMethodManager m = (InputMethodManager)   
						mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
						m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
//				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			};
			if(show_add_comment){
//				show_add_comment=false;
//				addcommentLayout.setVisibility(View.GONE);
				show_add_comment=true;
				mEditTextContent.setEnabled(true);;
				mEditTextContent.setText("");
				mEditTextContent.setHint("回复"+cmt.name);
			}else{
				show_add_comment=true;
				mEditTextContent.setEnabled(true);;
				mEditTextContent.setText("");
				mEditTextContent.setHint("回复"+cmt.name);
				Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
				addcommentLayout.setAnimation(animation);
				animation.start();
				addcommentLayout.setVisibility(View.VISIBLE);
			}

		}
		/*public Bitmap getBitmap(){
			return bmp;
		}*/
		
		
		
		
		/**
		 * 翻转动画
		 */
		/** 
		* Setup a new 3D rotation on the container view. 
		* 
		* @param position the item that was clicked to show a picture, or -1 to show the list 
		* @param start the start angle at which the rotation must begin 
		* @param end the end angle of the rotation 
		*/ 
	
	
		/*public void applyRotation(int position, float start, float end) { 
		// 计算中心点 
		final float centerX = mContainer.getWidth() / 2.0f; 
		final float centerY = mContainer.getHeight() / 2.0f; 
		if(!hasRecord){
			this.centerY=oneLayout.getHeight();
			hasRecord=true;
		}
//		final float centerY = this.centerY / 2.0f; 

		// Create a new 3D rotation with the supplied parameter 
		// The animation listener is used to trigger the next animation 
		final Rotate3dAnimation rotation = 
		new Rotate3dAnimation(start, end, centerX, centerY, 0.0f, false); //310.0f
		rotation.setDuration(500); 
		rotation.setFillAfter(true); 
		rotation.setInterpolator(new AccelerateInterpolator()); 
		//设置监听 
		rotation.setAnimationListener(new DisplayNextView(position)); 

		mContainer.startAnimation(rotation); 
		} */
		GestureDetector gestureDetector=new GestureDetector(new MyGestureDector(1));
		GestureDetector gesture=new GestureDetector(new MyGestureDector(4));
		private void clickIV1() {
			// TODO Auto-generated method stub
			imageView.setClickable(false);
			imageView.setFocusable(true);
			imageView.setFocusableInTouchMode(true);
			
			imageView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return gesture.onTouchEvent(event);
				}
			});
			scrollView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					
					
					return gestureDetector.onTouchEvent(event);
				}
			});
          listView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
				
					return gestureDetector.onTouchEvent(event);
				}
			});
          /*myScrollView.setGestureDector(gestureDetector);
          myScrollView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
				
					return gestureDetector.onTouchEvent(event);
				}
			});*/
          /*reverseSideLayout.setFocusable(true);
          reverseSideLayout.setFocusableInTouchMode(true);
          reverseSideLayout.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
				
					return gestureDetector.onTouchEvent(event);
				}
			});*/
          scrollview2.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
				
					return gestureDetector.onTouchEvent(event);
				}
			});
          mContainer.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
				
					return gestureDetector.onTouchEvent(event);
				}
			});
		}	
		
		/*private void clickIV2() {
			// TODO Auto-generated method stub
			twoLayout.setFocusable(true);
			twoLayout.setFocusableInTouchMode(true);
			twoLayout.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					
					if(event.getAction()==MotionEvent.ACTION_UP){
						applyRotation(-1, 180, 90); 
					}
					
					return true;
				}
			});
	
		}*/
		/** 
		* This class listens for the end of the first half of the animation. 
		* It then posts a new action that effectively swaps the views when the container 
		* is rotated 90 degrees and thus invisible. 
		*/ 
		/*private final class DisplayNextView implements Animation.AnimationListener { 
		private final int mPosition; 

		private DisplayNextView(int position) { 
		mPosition = position; 
		} 

		public void onAnimationStart(Animation animation) { 
		} 
		//动画结束 
		public void onAnimationEnd(Animation animation) { 
		mContainer.post(new SwapViews(mPosition)); 
		} 

		public void onAnimationRepeat(Animation animation) { 
		} 
		} */
		/** 
		* This class is responsible for swapping the views and start the second 
		* half of the animation. 
		*/ 
		/*private final class SwapViews implements Runnable { 
		private final int mPosition; 

		public SwapViews(int position) { 
		mPosition = position; 
		} 

		public void run() { 
		final float centerX = mContainer.getWidth() / 2.0f; 
		final float centerY = mContainer.getHeight() / 2.0f; 
//		final float centerY =NewShowTopicActivity.this.centerY / 2.0f;
		Rotate3dAnimation rotation; 

		if (mPosition > -1) { 
		//显示ImageView 
		scrollView.setVisibility(View.GONE); 
		myScrollView.setVisibility(View.VISIBLE); 
		myScrollView.requestFocus(); 
        if(positive){
        	rotation = new Rotate3dAnimation(-90, 0, centerX, centerY, 0.0f, true); //90  180  310.0f
        }else{
        	rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 0.0f, true); //310.0f
        }
		} else { 
		//返回listview 
			myScrollView.setVisibility(View.GONE); 
		scrollView.setVisibility(View.VISIBLE); 
		scrollView.requestFocus(); 
        if(positive){
        	rotation = new Rotate3dAnimation(-90, 0, centerX, centerY, 0.0f, true); //90 180  310.0f
        }else{
        	rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 0.0f, true); //90 180  310.0f
        }
		
		} 

		rotation.setDuration(500); 
		rotation.setFillAfter(false); 
		rotation.setInterpolator(new DecelerateInterpolator()); 
		//开始动画 
		mContainer.startAnimation(rotation); 
		} 
		} */
		
		public MyOnTouchListener myOnTouchListener=new MyOnTouchListener();
		public class MyOnTouchListener implements OnTouchListener{
            float startX,endX;
            float startY,endY;
            boolean record=false;
            
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX=event.getX();
					startY=event.getY();
					record=true;
					endX=0;
					endY=0;
					
					break;
				case MotionEvent.ACTION_MOVE:
					if(!record){
						startX=event.getX();
						startY=event.getY();
						endX=0;
						endY=0;
						record=true;
					}else{
						endX=event.getX();
						endY=event.getY();
					}
					
					break;
				case MotionEvent.ACTION_UP:
					if(endX!=0||endY!=0){
						onTouchScroll(event, event, endX-startX, endY-startY);
						startX=0;
						startY=0;
						record=false;
						endX=0;
						endY=0;
					}
					
					break;

				default:
					break;
				}
				
				
				
				
				
				return true;
			}
			
		}
		
		
		
		
		public boolean  positive=true;
		boolean hasStart=false;
		int touchSlop;
		
		
		public class MyGestureDector implements OnGestureListener{
		    	int mode;//1,照片详情页面；2，分享，送礼，点赞列表；3，评论列表,4查看大图
		    	float startY,startX;
		        float endY,endX;
		    	public MyGestureDector(int mode){
		    		this.mode=mode;
		    		touchSlop=ViewConfiguration.getTouchSlop();
		    	}
				
				@Override
				public boolean onSingleTapUp(MotionEvent arg0) {
					// TODO Auto-generated method stub
//					hasStart=false;
					if(mode==4){
						if(ShowPictureActivity.showPictureActivity!=null){
							ShowPictureActivity.showPictureActivity.finish();
							ShowPictureActivity.showPictureActivity=null;
						}
						Intent intent=new Intent(NewShowTopicActivity.this,ShowPictureActivity.class);
						intent.putExtra("url", petPicture.url);
						NewShowTopicActivity.this.startActivity(intent);
						shareLayout.setVisibility(View.INVISIBLE);
						return true;
					}
					return false;
				}
				
				@Override
				public void onShowPress(MotionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
						float arg3) {
					// TODO Auto-generated method stub
					if(startY==0&&arg0!=null){
						startY=arg0.getY();
					}
					if(startX==0&&arg0!=null){
						startX=arg0.getX();
					}
					if(arg1!=null){
						endY=arg1.getY();
						endX=arg1.getX();
					}
					
					
					
					LogUtil.i("mi", "arg2==="+arg2+",arg3="+arg3+",hasStart="+hasStart+";endy-startY="+(endY-startY)+";touchSlop="+touchSlop+";endX-startX="+(endX-startX));
					if((Math.abs(endY-startY)<10*touchSlop)&&(Math.abs(endX-startX)>6*touchSlop))
					onTouchScroll(arg0, arg1, arg2, arg3);
					return false;
					
				}
				
				@Override
				public void onLongPress(MotionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
						float arg3) {
					// TODO Auto-generated method stub
					if(arg0==null||arg1==null)return false;
					float detX=arg0.getX()-arg1.getX();
					float detY=arg0.getY()-arg1.getY();
					if(!hasStart&&Math.abs(detX)>Math.abs(detY)&&Math.abs(arg3)<touchSlop){
						LogUtil.i("mi", "滚动触摸onFling");
						if(current_page==1){
							Animation anim1=AnimationUtils.loadAnimation(NewShowTopicActivity.this, R.anim.anim_scale_small);
							final Animation anim2=AnimationUtils.loadAnimation(NewShowTopicActivity.this, R.anim.anim_scale_big);
							
							anim1.setAnimationListener(new AnimationListener() {
								
								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub
									LogUtil.i("mi", "正面anim1开始");
								}
								
								@Override
								public void onAnimationRepeat(Animation animation) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onAnimationEnd(Animation animation) {
									// TODO Auto-generated method stub
									mContainer.clearAnimation();
									LogUtil.i("mi", "正面anim1结束");
									scrollView.setVisibility(View.GONE);
//									myScrollView.setVisibility(View.VISIBLE);
									reverseSideLayout.setVisibility(View.VISIBLE);
									mContainer.startAnimation(anim2);
									
								}
							});
                            anim2.setAnimationListener(new AnimationListener() {
								
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
									hasStart=false;
								}
							});
                            
//							mContainer.startAnimation(anim1);
							
							if(arg2<0){
								positive=true;
							}else{
								positive=false;
							}
							current_page=-1;
						}else{
							Animation anim1=AnimationUtils.loadAnimation(NewShowTopicActivity.this, R.anim.anim_scale_small);
							final Animation anim2=AnimationUtils.loadAnimation(NewShowTopicActivity.this, R.anim.anim_scale_big);
							
							anim1.setAnimationListener(new AnimationListener() {
								
								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub
									LogUtil.i("mi", "反面anim1开始");
								}
								
								@Override
								public void onAnimationRepeat(Animation animation) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onAnimationEnd(Animation animation) {
									// TODO Auto-generated method stub
									LogUtil.i("mi", "反面anim1开始");
//									myScrollView.setVisibility(View.GONE);
									reverseSideLayout.setVisibility(View.GONE);
									scrollView.setVisibility(View.VISIBLE);
									mContainer.startAnimation(anim2);
								}
							});
                            anim2.setAnimationListener(new AnimationListener() {
								
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
									hasStart=false;
								}
							});
//							mContainer.startAnimation(anim1);
							if(arg2<0){
								positive=true;
								
							}else{
								positive=false;
							}
							current_page=1;
						}
						hasStart=true;
						if(mode==4){
							return false;
						}
						return false;
					}else{
						return false;
					}
				}
				
				@Override
				public boolean onDown(MotionEvent arg0) {
					// TODO Auto-generated method stub
					startY=arg0.getY();
					startX=arg0.getX();
					LogUtil.i("mi","手势落下onDown"+":startY="+startY+"；startX="+startX);
					
					
					hasStart=false;
					if(mode==4)return true;
					return false;
				}
				
			}

   public void weixinShare(){
	   WeiXinShareContent weixinContent = new WeiXinShareContent();
	   long time=petPicture.create_time+24*3600-System.currentTimeMillis()/1000;
	  if((!"0".equals(petPicture.is_food))&&time>0){
			 //设置分享文字
			 weixinContent.setShareContent(StringUtil.isEmpty(petPicture.cmt)?"看在我这么努力卖萌的份上快来宠宠我！免费送我点口粮好不好？":petPicture.cmt);
			 //设置title
			 weixinContent.setTitle("轻轻一点，免费赏粮！我的口粮全靠你啦~");
			 
	  }else{
		//设置分享文字
		  if(StringUtil.isEmpty(petPicture.cmt)){
			  weixinContent.setShareContent((StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"没有照片描述：这是我最新的美照哦~~打滚儿求表扬~~");
		  }else{
			  weixinContent.setShareContent(petPicture.cmt);
		  }
			
			 //设置title
			 weixinContent.setTitle("我是"+petPicture.animal.pet_nickName+"，你有没有爱上我？");
	  }
	//设置分享内容跳转URL
		 weixinContent.setTargetUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=wechat"*/);
	   

	 //设置分享图片
	 UMImage umImage=new UMImage(this,bmpPath );
	 weixinContent.setShareImage(umImage);
//	 weixinContent.setShareContent(petPicture.animal.pet_nickName);
	 mController.setShareMedia(weixinContent);
//	 mController.openShare(this, true);
	 mController.postShare(this,SHARE_MEDIA.WEIXIN, 
		        new SnsPostListener() {
		                @Override
		                public void onStart() {
//		                    Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
		                }
		                @Override
		                public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
		                     if (eCode == 200) {
		                    	 shareNumChange();
		                    	/* if("1".equals(petPicture.is_food)){
		         					MobclickAgent.onEvent(NewShowTopicActivity.this, "food_share_suc");
		         				}else if("2".equals(petPicture.is_food)){
		         					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic1_share_suc");
		         				}else if("3".equals(petPicture.is_food)){
		         					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic2_share_suc");
		         				}*/
		                    	 
		                    	 if("2".equals(petPicture.is_food)){
		                       		MobclickAgent.onEvent(NewShowTopicActivity.this, "topic1_share_suc");
		         					
		         				}else if("3".equals(petPicture.is_food)){
		         					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic2_share_suc");
		          				}else if(!"1".equals(petPicture.is_food)){
		          					
		          				}else{
		          					MobclickAgent.onEvent(NewShowTopicActivity.this, "food_share_suc");
		          				}
		                    	 
		                         Toast.makeText(NewShowTopicActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		                     } else {
		                          String eMsg = "";
		                          if (eCode == -101){
		                              eMsg = "没有授权";
		                          }
		                          Toast.makeText(NewShowTopicActivity.this, "分享失败[" + eCode + "] " + 
		                                             eMsg,Toast.LENGTH_SHORT).show();
		                     }
		              }
		});
	   
	   
		
		TranslateAnimation tAnimation2=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
		
		tAnimation2.setDuration(500);
	
		shareLayout.clearAnimation();
		shareLayout.setAnimation(tAnimation2);
		tAnimation2.start();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				shareLayout.setVisibility(View.INVISIBLE);
			}
		}, 500);
   }
   public void friendShare(){
	   CircleShareContent circleMedia = new CircleShareContent();
	   UMImage umImage=new UMImage(this, bmpPath);
	   circleMedia.setShareImage(umImage);
	   long time=petPicture.create_time+24*3600-System.currentTimeMillis()/1000;
	   if((!"0".equals(petPicture.is_food))&&time>0){
			 //设置分享文字
		   circleMedia.setShareContent("看在我这么努力卖萌的份上快来宠宠我！免费送我点口粮好不好？");
			 //设置title
		   circleMedia.setTitle(StringUtil.isEmpty(petPicture.cmt)?"轻轻一点，免费赏粮！我的口粮全靠你啦~":petPicture.cmt);
			
	  }else{
		//设置分享文字
		  if(StringUtil.isEmpty(petPicture.cmt)){
			  circleMedia.setShareContent((StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"没有照片描述：这是我最新的美照哦~~打滚儿求表扬~~");
		  }else{
			  circleMedia.setShareContent(petPicture.cmt);
		  }
			
			 //设置title
		  circleMedia.setTitle(StringUtil.isEmpty(petPicture.cmt)?"我是"+petPicture.animal.pet_nickName+"，你有没有爱上我？":petPicture.cmt);
	  }
	   //设置分享内容跳转URL
	   circleMedia.setTargetUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=wechat"*/);
	   mController.setShareMedia(circleMedia);
	   mController.postShare(this,SHARE_MEDIA.WEIXIN_CIRCLE,
			   new SnsPostListener() {
           @Override
           public void onStart() {
//               Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
           }
           @Override
           public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
                if (eCode == 200) {
               	 shareNumChange();
               /*	if("1".equals(petPicture.is_food)){
					MobclickAgent.onEvent(NewShowTopicActivity.this, "food_share_suc");
				}else if("2".equals(petPicture.is_food)){
 					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic1_share_suc");
 				}else if("3".equals(petPicture.is_food)){
 					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic2_share_suc");
 				}*/
               	if("2".equals(petPicture.is_food)){
              		MobclickAgent.onEvent(NewShowTopicActivity.this, "topic1_share_suc");
					
				}else if("3".equals(petPicture.is_food)){
					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic2_share_suc");
 				}else if(!"1".equals(petPicture.is_food)){
 					
 				}else{
 					MobclickAgent.onEvent(NewShowTopicActivity.this, "food_share_suc");
 				}
                 Toast.makeText(NewShowTopicActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
                } else {
                     String eMsg = "";
                     if (eCode == -101){
                         eMsg = "没有授权";
                     }
                     Toast.makeText(NewShowTopicActivity.this, "分享失败[" + eCode + "] " + 
                                        eMsg,Toast.LENGTH_SHORT).show();
                }
         }
});
	   
		
		TranslateAnimation tAnimation3=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
		
		tAnimation3.setDuration(500);
	
		shareLayout.clearAnimation();
		shareLayout.setAnimation(tAnimation3);
		tAnimation3.start();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				shareLayout.setVisibility(View.INVISIBLE);
			}
		}, 500);
		
   }
   
   @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	 /**使用SSO授权必须添加如下代码 */
    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
    if(ssoHandler != null){
       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
    }
	
}
   public void xinlangShare(){
	   UserImagesJson.Data data=new UserImagesJson.Data();
		if(bmpPath!=null){
			data.path=bmpPath;
			if(StringUtil.isEmpty(petPicture.cmt)){
				data.des=(StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"这是我最新的美照哦~~打滚儿求表扬~~"+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id+"分享照片（分享自@宠物星球社交应用）";
			}else{
				data.des=petPicture.cmt+(StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id+"（分享自@宠物星球社交应用）";
			}
		}
	   SinaShareContent content=new SinaShareContent();
	   long time=petPicture.create_time+24*3600-System.currentTimeMillis()/1000;
	   if((!"0".equals(petPicture.is_food))&&time>0){
		   content.setShareContent((StringUtil.isEmpty(petPicture.cmt)?"看在我这么努力卖萌的份上快来宠宠我！免费送我点口粮好不好？":petPicture.cmt)+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=webo"*/+"（分享自@宠物星球社交应用）");
	   }else{
		   content.setShareContent(data.des);
	   }
	   
	   
	   UMImage umImage=new UMImage(this, data.path);
	  
	   content.setShareImage(umImage);
	   mController.setShareMedia(content);
	   mController.postShare(this, SHARE_MEDIA.SINA,new SnsPostListener() {
		
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
			// TODO Auto-generated method stub
			if (eCode == 200) {
              	 shareNumChange();
              	 
              	/*if("1".equals(petPicture.is_food)){
					MobclickAgent.onEvent(NewShowTopicActivity.this, "food_share_suc");
				}else if("2".equals(petPicture.is_food)){
 					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic1_share_suc");
 				}else if("3".equals(petPicture.is_food)){
 					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic2_share_suc");
 				}*/
              	 
            	if("2".equals(petPicture.is_food)){
              		MobclickAgent.onEvent(NewShowTopicActivity.this, "topic1_share_suc");
					
				}else if("3".equals(petPicture.is_food)){
					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic2_share_suc");
 				}else if(!"1".equals(petPicture.is_food)){
 					
 				}else{
 					MobclickAgent.onEvent(NewShowTopicActivity.this, "food_share_suc");
 				}
              	
              	
                Toast.makeText(NewShowTopicActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
               } else {
                    String eMsg = "";
                    if (eCode == -101){
                        eMsg = "没有授权";
                    }
                    Toast.makeText(NewShowTopicActivity.this, "分享失败[" + eCode + "] " + 
                                       eMsg,Toast.LENGTH_SHORT).show();
               }
		}
	});
	   
   }
	public void shareNumChange(){
		petPicture.shares++;
		shareTv1.setText(""+petPicture.shares);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				final MyUser user=HttpUtil.imageShareNumsApi(NewShowTopicActivity.this,petPicture.img_id, handler);
				if(user!=null&&PetApplication.myUser!=null){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(petPicture.shareUsersList!=null&&petPicture.shareUsersList.size()>0&&petPicture.shareUsersList.contains(PetApplication.myUser)){
								
							}else{
								
								if(StringUtil.isEmpty(petPicture.share_ids)){
									petPicture.share_ids=""+PetApplication.myUser.userId;
								}else{
									petPicture.share_ids+=","+PetApplication.myUser.userId;
								}
								if(petPicture.shareUsersList==null){
									petPicture.shareUsersList=new ArrayList<MyUser>();
								}
								petPicture.shareUsersList.add(PetApplication.myUser);
								if(current_show==SHOW_SHARE_LIST){
									showShareUsersList();
								}
								
							}
						}
					});
				}
				
				
				/*if(!Constants.isSuccess)return;
				UserStatusUtil.checkUserExpGoldLvRankChange(user, ShowTopicActivity.this, progressLayout);*/
			}
		}).start();
	}
	public void recyle(){
		if(NewShowTopicActivity.newShowTopicActivity!=null){
			NewShowTopicActivity.newShowTopicActivity.imageView.setImageDrawable(null);
			/*if(NewShowTopicActivity.newShowTopicActivity.bmp!=null&&!NewShowTopicActivity.newShowTopicActivity.bmp.isRecycled()){
				NewShowTopicActivity.newShowTopicActivity.bmp.recycle();
			}
			NewShowTopicActivity.newShowTopicActivity.bmp=null;*/
			NewShowTopicActivity.newShowTopicActivity=null;
			finish();
		}
	}
	public void onTouchScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		LogUtil.i("mi", "arg2===onTouchScroll==="+arg2+",arg3="+arg3+",hasStart="+hasStart);
		final float centerX = mContainer.getWidth() / 2.0f; 
		final float centerY = mContainer.getHeight() / 2.0f; 
		if(!hasStart&&Math.abs(arg2)>Math.abs(arg3)&&Math.abs(arg3)<touchSlop){
			LogUtil.i("mi", "滚动触摸");
			if(current_page==1){
				Animation anim1=AnimationUtils.loadAnimation(NewShowTopicActivity.this, R.anim.anim_scale_small);
				final Animation anim2=AnimationUtils.loadAnimation(NewShowTopicActivity.this, R.anim.anim_scale_big);
				
				anim1.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						LogUtil.i("mi", "正面anim1开始");
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						mContainer.clearAnimation();
						LogUtil.i("mi", "正面anim1结束");
						scrollView.setVisibility(View.GONE);
//						myScrollView.setVisibility(View.VISIBLE);
						reverseSideLayout.setVisibility(View.VISIBLE);
						mContainer.startAnimation(anim2);
						
					}
				});
                anim2.setAnimationListener(new AnimationListener() {
					
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
						hasStart=false;
						current_page=-1;
						if(petPicture.commentsList!=null&&petPicture.commentsList.size()>0&&current_show==SHOW_COMMENT_LIST){
							SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
							SharedPreferences.Editor e=sp.edit();
							boolean guide4=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE4, true);
							if(guide4){
								guideIv1.setImageResource(R.drawable.guide4);
								guideIv1.setVisibility(View.VISIBLE);
								
								e.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE4, false);
								e.commit();
                                guideIv1.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										guideIv1.setImageDrawable(new BitmapDrawable());
										guideIv1.setVisibility(View.GONE);
									}
								});
							}else{
								guideIv1.setVisibility(View.GONE);
							}
						}
					}
				});
				mContainer.startAnimation(anim1);
				
				
				if(arg2<0){
					positive=true;
//					applyRotation(1, 0, 90); 
					/*ObjectAnimator visToInvis = ObjectAnimator.ofFloat(scrollView, "rotationY", 0f, 90f);  
					final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(myScrollView, "rotationY",  
					        -90f, 0f);
					visToInvis.addListener(new AnimatorListenerAdapter() {  
					    public void onAnimationEnd(Animator anim) {  
					    	scrollView.setVisibility(View.GONE);  
					    	invisToVis.start();  
					    	myScrollView.setVisibility(View.VISIBLE);  
					    }  
					});  
					visToInvis.start(); */
				}else{
					positive=false;
//					applyRotation(1, 0, -90); 
					
					/*ObjectAnimator visToInvis = ObjectAnimator.ofFloat(scrollView, "rotationY", 0f, -90f);  
					final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(myScrollView, "rotationY",  
					        90f, 0f);
					visToInvis.addListener(new AnimatorListenerAdapter() {  
					    public void onAnimationEnd(Animator anim) {  
					    	scrollView.setVisibility(View.GONE);  
					    	invisToVis.start();  
					    	myScrollView.setVisibility(View.VISIBLE);  
					    }  
					});  
					visToInvis.start(); */
				}
				
			}else{
				Animation anim1=AnimationUtils.loadAnimation(NewShowTopicActivity.this, R.anim.anim_scale_small);
				final Animation anim2=AnimationUtils.loadAnimation(NewShowTopicActivity.this, R.anim.anim_scale_big);
				
				anim1.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						LogUtil.i("mi", "反面anim1开始");
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						LogUtil.i("mi", "反面anim1开始");
//						myScrollView.setVisibility(View.GONE);
						reverseSideLayout.setVisibility(View.GONE);
						scrollView.setVisibility(View.VISIBLE);
						mContainer.startAnimation(anim2);
					}
				});
                anim2.setAnimationListener(new AnimationListener() {
					
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
						hasStart=false;
						current_page=1;
					}
				});
				mContainer.startAnimation(anim1);
				if(arg2<0){
					positive=true;
					
//					applyRotation(-1, 0, 90); //0  90
					
					/*ObjectAnimator visToInvis = ObjectAnimator.ofFloat(myScrollView, "rotationY", 0f, 90f);  
					final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(scrollView, "rotationY",  
					        -90f, 0f);
					visToInvis.addListener(new AnimatorListenerAdapter() {  
					    public void onAnimationEnd(Animator anim) {  
					    	myScrollView.setVisibility(View.GONE);  
					    	invisToVis.start();  
					    	scrollView.setVisibility(View.VISIBLE);  
					    }  
					});  
					visToInvis.start(); */
					
				}else{
					positive=false;
//					applyRotation(-1, 0, -90); //180  90
					/*ObjectAnimator visToInvis = ObjectAnimator.ofFloat(myScrollView, "rotationY", 0f, -90f);  
					final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(scrollView, "rotationY",  
					        90f, 0f);
					visToInvis.addListener(new AnimatorListenerAdapter() {  
					    public void onAnimationEnd(Animator anim) {  
					    	myScrollView.setVisibility(View.GONE);  
					    	invisToVis.start();  
					    	scrollView.setVisibility(View.VISIBLE);  
					    }  
					});  
					visToInvis.start(); */
				}
				
			}
			hasStart=true;
			if(mode==4){
			}
		}else{
		}
		
	}
	

	private List<String> reslist;
	private ViewPager expressionViewpager;
	private PasteEditText mEditTextContent;
	private ImageView emotionIv;
	private  LinearLayout emotionLayout;
	boolean showSmile=true;
	public void initEmotion(){
		emotionIv=(ImageView)findViewById(R.id.emotion_iv);
		emotionLayout=(LinearLayout)findViewById(R.id.ll_face_container);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		emotionIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(showSmile){
					emotionIv.setImageResource(R.drawable.expression_keyboard);
					
					InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//			    	im.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0);i
			    	im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
			    	handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							emotionLayout.setVisibility(View.VISIBLE);
						}
					}, 300);
			    	
			    	showSmile=false;
				}else{
					
					InputMethodManager m = (InputMethodManager)   
							mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
			    	
			    	emotionIv.setImageResource(R.drawable.chatting_biaoqing_btn_enable);
					emotionLayout.setVisibility(View.GONE);
			    	showSmile=true;
				}
				/*if(emotionLayout.getVisibility()==View.GONE){
					emotionLayout.setVisibility(View.VISIBLE);
				}else{
					emotionLayout.setVisibility(View.GONE);
				}*/
				
			}
		});
		// 表情list
				reslist = getExpressionRes(44);
				// 初始化表情viewpager
				List<View> views = new ArrayList<View>();
				View gv1 = getGridChildView(1);
				View gv2 = getGridChildView(2);
				View gv3 = getGridChildView(3);
				views.add(gv1);
				views.add(gv2);
				views.add(gv3);
				expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
	}
	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
//		return reslist;
		return NewSmileUtils.tags;

	}
	/**
	 * 获取表情的gridview的子view
	 * 
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(NewShowTopicActivity.this, R.layout.expression_gridview2, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, 40));
		}else if(i==3){
			
			list.addAll(reslist.subList(40, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(NewShowTopicActivity.this, 1, list,1);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
//					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // 不是删除键，显示表情
							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class clz = Class.forName("com.aidigame.hisun.imengstar.huanxin.NewSmileUtils");
//							Field field = clz.getField(filename);
							mEditTextContent.append(NewSmileUtils.getSmiledText(NewShowTopicActivity.this, /*(String) field.get(null)*/filename));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = mEditTextContent.getText().toString();
									String tempStr = body.substring(0, selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i, selectionStart);
										if (NewSmileUtils.containsKey(cs.toString()))
											mEditTextContent.getEditableText().delete(i, selectionStart);
										else
											mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
									} else {
										mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
									}
								}
							}

						}
//					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	
	
	
	
	
	

}
