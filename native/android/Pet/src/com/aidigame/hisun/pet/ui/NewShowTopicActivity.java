package com.aidigame.hisun.pet.ui;

import java.net.Authenticator.RequestorType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import u.aly.co;
import u.aly.p;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.CommentListViewAdapter;
import com.aidigame.hisun.pet.adapter.TopicUsersListAdapter;
import com.aidigame.hisun.pet.adapter.CommentListViewAdapter.ClickUserName;
import com.aidigame.hisun.pet.adapter.TopicUsersListAdapter.GestureListener;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.bean.PetPicture.Comments;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.LinearLayoutForListView;
import com.aidigame.hisun.pet.view.MyScrollView;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.Rotate3dAnimation;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom;
import com.aidigame.hisun.pet.widget.fragment.DialogNote;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom.ResultListener;
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
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.OnEditorActionListener;

public class NewShowTopicActivity extends Activity implements OnClickListener{
	public static final int SHOW_LIKE_LIST=1;
	public static final int SHOW_GIFT_LIST=2;
	public static final int SHOW_COMMENT_LIST=3;
	public static final int SHOW_SHARE_LIST=4;
	
	ScrollView scrollView,scrollview2;
	MyScrollView myScrollView;
	
	
	public int current_show=SHOW_LIKE_LIST;
	public int current_page=1;//1,正面；-1，反面
	/*
	 * 一直存在的控件
	 */
	ImageView bottomLikeIv,bottomGiftIv,bottomCommentIv,bottomMoreIv,guideIv1;
	ViewGroup mContainer;
	View popupParent;
	RelativeLayout black_layout;
	PetPicture petPicture;
	LinearLayout progressLayout;
	RelativeLayout touchLayout;
	FrameLayout parent_framelayout;
	//评论相关
	EditText commentET;
	TextView send_comment_tv;
	LinearLayout addcommentLayout;
	
	public static NewShowTopicActivity newShowTopicActivity;
	Handler handler;
	ImageFetcher mImageFetcher;
	
	public Bitmap bmp;
	String bmpPath;
	int mode;
	int from_w;
	ShowProgress showProgress;
	
	/*
	 *正面显示的控件
	 */
	RelativeLayout oneLayout;
	public ImageView closeIv1,imageView;
	TextView desTv,topicTv,timeTv;
	View paddingView;//卡片长度超过或接近屏幕底部时显示，以防内容显示不全
	
	
	/*
	 * 反面显示控件
	 */
	RelativeLayout twoLayout;
	RoundImageView petIcon,userIcon;
	ImageView genderIv,middleTabLikeIv,middleTabGiftIv,middleTabCommentIv,middleTabShareIv,
	          triangleIv1, triangleIv2, triangleIv3, triangleIv4,
	          closeIv2;
	TextView likeNumTv1,giftTv1,commentTv1,shareTv1,
	         triangleTv1,triangleTv2,triangleTv3,triangleTv4,
	         petNameTv,petRaceTv,userNameTv;
	LinearLayoutForListView listView;
	CommentListViewAdapter commentListViewAdapter;
	TopicUsersListAdapter topicUsersListAdapter;
	FrameLayout infoFrameLayout;
	boolean urlIsEmpty=false;
	UMSocialService mController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_show_topic);
		MobclickAgent.onEvent(this, "photopage");
		newShowTopicActivity=this;
		guideIv1=(ImageView)findViewById(R.id.guide1);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor e=sp.edit();
		boolean guide1=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE1, true);
		if(guide1){
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
				if(Constants.isSuccess&&Constants.user==null){
					downLoadUserInfo();
				}
				 mImageFetcher = new ImageFetcher(this, Constants.screen_width);
				mode=getIntent().getIntExtra("mode", 0);
				from_w=getIntent().getIntExtra("from_w", 1);
		
		initAlwaysStayView();
//		showProgress=new ShowProgress(this, progressLayout);
		initOneView();
		iniTwoView();
		
		initShareView();
		petPicture=(PetPicture)getIntent().getSerializableExtra("PetPicture");
		urlIsEmpty=StringUtil.isEmpty(petPicture.url);
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
									initMoreView();
									if(urlIsEmpty)displayImage();
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
		touchLayout=(RelativeLayout)findViewById(R.id.touch_layout);
		scrollView=(ScrollView)findViewById(R.id.scrollview);
		myScrollView=(MyScrollView)findViewById(R.id.my_scrollview);
		parent_framelayout=(FrameLayout)findViewById(R.id.parent_framelayout);
		
		commentET=(EditText)findViewById(R.id.edit_comment);
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
		
		commentET.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
					sendComment();
					return true;
			}
		});
	commentET.addTextChangedListener(new TextWatcher() {
		
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
		closeIv1=(ImageView)findViewById(R.id.show_topic_close_iv1);
		desTv=(TextView)findViewById(R.id.show_topic_comment_tv);
		topicTv=(TextView)findViewById(R.id.show_topic_topic_tv);
		timeTv=(TextView)findViewById(R.id.show_topic_time_tv);
		paddingView=findViewById(R.id.paddingView);
		
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
		
		myScrollView.setDrawingCacheEnabled(false);
	}
	/**
	 * 更多按钮显示的条目
	 */
//	RelativeLayout moreLayout;
	
	private void initMoreView() {
		// TODO Auto-generated method stub
		
//		TextView bigImageTv=(TextView)findViewById(R.id.bigimagetv);
		TextView chatTv=(TextView)findViewById(R.id.messagetv);
		TextView reportTv=(TextView)findViewById(R.id.reporttv);
//		TextView cancelTv=(TextView)findViewById(R.id.canceltv);
		final TextView pengta_tv=(TextView)findViewById(R.id.pengta_tv);
		if(Constants.user!=null&&Constants.user.aniList!=null&&Constants.user.aniList.contains(petPicture.animal) ){
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
				if(Constants.user!=null&&Constants.user.aniList!=null&&Constants.user.aniList.contains(petPicture.animal) ){
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
				for(int i=0;i<Constants.user.aniList.size();i++){
//					if(Constants.user.aniList.get(i).master_id!=Constants.user.userId)
						count++;
				}
				
				
				if(count>=10&&count<=20){
					num=(count)*5;
				}else if(count>20){
					num=100;
				}
				
				if(Constants.user.coinCount<num){
//					DialogNote dialog=new DialogNote(popupParent, NewShowTopicActivity.this, black_layout, 1);
					Intent intent=new Intent(NewShowTopicActivity.this,DialogNoteActivity.class);
					intent.putExtra("mode", 10);
					intent.putExtra("info", "钱包君告急！挣够金币再来捧萌星吧");
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
				
				Intent intent=new Intent(NewShowTopicActivity.this,com.aidigame.hisun.pet.huanxin.ChatActivity.class);
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
	RelativeLayout shareLayout;
	private void initShareView() {
		// TODO Auto-generated method stub
		shareLayout=(RelativeLayout)findViewById(R.id.sharelayout);
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
        	 desTv.setText(petPicture.cmt);
         }else{
        	 desTv.setVisibility(View.GONE);
         }
         if(!StringUtil.isEmpty(petPicture.topic_name)&&!"##".equals(petPicture.topic_name)){
        	 topicTv.setText(petPicture.topic_name);
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
       
       
       if(Constants.user!=null&&petPicture.likers!=null&&petPicture.likers.contains(""+Constants.user.userId)){
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
			FrameLayout.LayoutParams param1=(FrameLayout.LayoutParams)myScrollView.getLayoutParams();
			if(param1==null){
				param1=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,h1);
			}
			int minH=Constants.screen_height*3/4;
			
			if(h1<minH){
				h1=minH;
			}
			param.height=h1;
			param1.height=h1;
			myScrollView.setLayoutParams(param1);
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
		int current_Middle_tab_position=3;
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
				guideIv1.setVisibility(View.GONE);
				
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
						myScrollView.setVisibility(View.VISIBLE);
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
				
				
				break;
				
			}
		}
		public void close(){

			if(isTaskRoot()){
				if(HomeActivity.homeActivity!=null){
					Intent intent=HomeActivity.homeActivity.getIntent();
					if(intent!=null){
						this.startActivity(intent);
						if(bmp!=null){
							if(!bmp.isRecycled())
							bmp.recycle();
							bmp=null;
						}
						imageView.setImageDrawable(null);
						newShowTopicActivity=null;
						
						if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(this)){
							PetApplication.petApp.activityList.remove(this);
						}
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
			if(bmp!=null){
				if(!bmp.isRecycled())
				bmp.recycle();
				bmp=null;
			}
			imageView.setImageDrawable(null);
			newShowTopicActivity=null;
			
			if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(this)){
				PetApplication.petApp.activityList.remove(this);
			}
			finish();
			System.gc();
		}
		
		/**
		 * 下载用户信息
		 */
		public void downLoadUserInfo(){
			if(Constants.isSuccess){
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
			
			options.inPurgeable=true;
			options.inInputShareable=true;
			mImageFetcher.setWidth(imageView.getMeasuredWidth());
			mImageFetcher.setImageCache(new ImageCache(this, new ImageCacheParams(petPicture.url)));
			
			mImageFetcher.setLoadCompleteListener(new ImageWorker.LoadCompleteListener(){
				@Override
				public void  onComplete(Bitmap bitmap) {
					// TODO Auto-generated method stub
					LogUtil.i("me", "获取图片宽高，宽="+options.outWidth+",高="+options.outHeight);
					LogUtil.i("me", "照片详情页面，图片开始下载");
					
					
					if(bitmap!=null){
						bmp=bitmap;
						bmpPath=mImageFetcher.getFilePath(NewShowTopicActivity.this,petPicture.url);
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
								param.height=h;
								twoLayout.setLayoutParams(param);
								int h1=scrollView.getMeasuredHeight();
								FrameLayout.LayoutParams param1=(FrameLayout.LayoutParams)myScrollView.getLayoutParams();
								if(param1==null){
									param1=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,h1);
								}
								param1.height=h1;
								myScrollView.setLayoutParams(param1);
								
								
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
									}
								});
									
							}
					  });
				}
				@Override
				public void getPath(String path) {
					// TODO Auto-generated method stub
					
				}
			});
			mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/petPicture.url, imageView,options);
			
		
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
			        .decodingOptions(options)
	                .build();
			ImageLoader imageLoader1=ImageLoader.getInstance();
			imageLoader1.displayImage(Constants.ANIMAL_DOWNLOAD_TX+petPicture.animal.pet_iconUrl, petIcon, displayImageOptions1);
			DisplayImageOptions displayImageOptions2=new DisplayImageOptions
		            .Builder()
		            .showImageOnLoading(R.drawable.user_icon)
		            .showImageOnFail(R.drawable.user_icon)
			        .cacheInMemory(true)
			        .cacheOnDisc(true)
			        .bitmapConfig(Bitmap.Config.RGB_565)
			        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			        .decodingOptions(options)
	                .build();
			ImageLoader imageLoader2=ImageLoader.getInstance();
			imageLoader2.displayImage(Constants.USER_DOWNLOAD_TX+petPicture.animal.u_tx, userIcon, displayImageOptions2);
			
		}
		/**
		 * 下载用户列表信息
		 * @param mode 1，点赞人列表；2送礼物人列表；4，分享人列表
		 */
		public void downLoadUserInfo(final int mode){
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
		public void showLikeUsersList(){
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
		public void showGiftUsersList(){
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
		public void showShareUsersList(){
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
		boolean loadIcon=false;
		public void showCommentsUsersList(){
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
		public void triangleIvInvisible(){
			triangleIv1.setVisibility(View.INVISIBLE);
			triangleIv2.setVisibility(View.INVISIBLE);
			triangleIv3.setVisibility(View.INVISIBLE);
			triangleIv4.setVisibility(View.INVISIBLE);
		}
		public void actionLike(){
			
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
						
						petPicture.senders=""+Constants.user.userId;
						petPicture.giftUsersList=new ArrayList<MyUser>();
						petPicture.giftUsersList.add(Constants.user);
					}else{
						petPicture.senders+=","+Constants.user.userId;
						if(petPicture.giftUsersList==null){
							petPicture.giftUsersList=new ArrayList<MyUser>();
						}
						if(!petPicture.giftUsersList.contains(Constants.user))
						petPicture.giftUsersList.add(Constants.user);
					}
					if(petPicture.gift_txUrlList!=null&&Constants.user.u_iconUrl!=null){
						if(!petPicture.gift_txUrlList.contains(Constants.user.u_iconUrl))
						petPicture.gift_txUrlList.add(Constants.user.u_iconUrl);
					}else if(Constants.user.u_iconUrl!=null){
						petPicture.gift_txUrlList=new ArrayList<String>();
						petPicture.gift_txUrlList.add(Constants.user.u_iconUrl);
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
		boolean replySb=false;
		boolean show_add_comment=false;
		public void showCommentEditor(){
             
        	if(!UserStatusUtil.isLoginSuccess(this, popupParent,black_layout)){
        		return;
        	};
        	replySb=false;
			if(show_add_comment){
				show_add_comment=false;
				commentET.setVisibility(View.INVISIBLE);
				addcommentLayout.setVisibility(View.GONE);
			}else{
				commentET.setVisibility(View.VISIBLE);
				show_add_comment=true;
//				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				commentET.setEnabled(true);
				commentET.setFocusable(true);
				commentET.setFocusableInTouchMode(true);
				if(commentET.requestFocus(EditText.FOCUS_FORWARD)){
					commentET.setSelection(0);;
					InputMethodManager m = (InputMethodManager)   
							commentET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
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
		boolean canSend=false;
		boolean sendingComment=false;
		public void sendComment(){
			if(!canSend){
        		InputMethodManager m = (InputMethodManager)   
						commentET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
						m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
						addcommentLayout.setVisibility(View.GONE);
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
        	final String comment=commentET.getText().toString();
        	commentET.setText("");
        	if(replySb){
        		commentET.setHint("回复"+cmt.name);
        	}else{
        		commentET.setHint("写个评论呗");
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
									comments.usr_id=Constants.user.userId;
									comments.create_time=System.currentTimeMillis()/1000;
									comments.body=comment;
									comments.usr_tx=Constants.user.u_iconUrl;
									if(replySb){
										comments.isReply=true;
										comments.reply_id=cmt.usr_id;
										comments.reply_name=Constants.user.u_nick+"@"+cmt.name;
									}
									comments.name=Constants.user.u_nick;
									if(petPicture.commentsList==null){
										petPicture.commentsList=new ArrayList<PetPicture.Comments>();
										petPicture.commentsList.add(comments);
									}else{
										petPicture.commentsList.add(0, comments);
									}
									
									commentTv1.setText(""+petPicture.commentsList.size());
									if(current_show==SHOW_COMMENT_LIST){
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
									if(petPicture.commentsList.size()>0){
//										listView.update(petPicture.commentsList.size()-1);
									}else if(petPicture.commentsList.size()==0){
//										listView.update(0);
									}
									}
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
								commentET.setEnabled(true);
								addcommentLayout.setVisibility(View.GONE);
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
		PetPicture.Comments cmt;
		public void replyComment(PetPicture.Comments cmt){
			if(Constants.user!=null&&Constants.user.userId==cmt.usr_id){
				Toast.makeText(this, "请不要回复自己发的评论", Toast.LENGTH_LONG).show();;
				return;
			}
		
			replySb=true;
			this.cmt=cmt;
			commentET.setVisibility(View.VISIBLE);
			if(commentET.requestFocus(EditText.FOCUS_FORWARD)){
				commentET.setSelection(0);;
				InputMethodManager m = (InputMethodManager)   
						commentET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
						m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
//				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			};
			if(show_add_comment){
//				show_add_comment=false;
//				addcommentLayout.setVisibility(View.GONE);
				show_add_comment=true;
				commentET.setEnabled(true);;
				commentET.setText("");
				commentET.setHint("回复"+cmt.name);
			}else{
				show_add_comment=true;
				commentET.setEnabled(true);;
				commentET.setText("");
				commentET.setHint("回复"+cmt.name);
				Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
				addcommentLayout.setAnimation(animation);
				animation.start();
				addcommentLayout.setVisibility(View.VISIBLE);
			}

		}
		public Bitmap getBitmap(){
			return bmp;
		}
		
		
		
		
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
		boolean hasRecord=false;
		float centerY;
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
          myScrollView.setGestureDector(gestureDetector);
          myScrollView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
				
					return gestureDetector.onTouchEvent(event);
				}
			});
          scrollview2.setOnTouchListener(new OnTouchListener() {
				
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
		public boolean  positive=true;
		boolean hasStart=false;
		int touchSlop;
		public class MyGestureDector implements OnGestureListener{
		    	int mode;//1,照片详情页面；2，分享，送礼，点赞列表；3，评论列表,4查看大图
		    	
		    
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
					LogUtil.i("mi", "arg2==="+arg2+",arg3="+arg3+",hasStart="+hasStart);
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
					if(!hasStart&&Math.abs(detX)>Math.abs(detY)/*&&Math.abs(arg2)>touchSlop*/){
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
									myScrollView.setVisibility(View.VISIBLE);
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
									myScrollView.setVisibility(View.GONE);
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
					hasStart=false;
					if(mode==4)return true;
					return false;
				}
			}

   public void weixinShare(){
	   WeiXinShareContent weixinContent = new WeiXinShareContent();
	   long time=petPicture.create_time+24*3600-System.currentTimeMillis()/1000;
	  if("1".equals(petPicture.is_food)&&time>0){
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
	   if("1".equals(petPicture.is_food)&&time>0){
			 //设置分享文字
		   circleMedia.setShareContent(StringUtil.isEmpty(petPicture.cmt)?"看在我这么努力卖萌的份上快来宠宠我！免费送我点口粮好不好？":petPicture.cmt);
			 //设置title
		   circleMedia.setTitle("轻轻一点，免费赏粮！我的口粮全靠你啦~");
			
	  }else{
		//设置分享文字
		  if(StringUtil.isEmpty(petPicture.cmt)){
			  circleMedia.setShareContent((StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"没有照片描述：这是我最新的美照哦~~打滚儿求表扬~~");
		  }else{
			  circleMedia.setShareContent(petPicture.cmt);
		  }
			
			 //设置title
		  circleMedia.setTitle("我是"+petPicture.animal.pet_nickName+"，你有没有爱上我？");
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
	   if("1".equals(petPicture.is_food)&&time>0){
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
				if(user!=null&&Constants.user!=null){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(petPicture.shareUsersList!=null&&petPicture.shareUsersList.size()>0&&petPicture.shareUsersList.contains(Constants.user)){
								
							}else{
								
								if(StringUtil.isEmpty(petPicture.share_ids)){
									petPicture.share_ids=""+Constants.user.userId;
								}else{
									petPicture.share_ids+=","+Constants.user.userId;
								}
								if(petPicture.shareUsersList==null){
									petPicture.shareUsersList=new ArrayList<MyUser>();
								}
								petPicture.shareUsersList.add(Constants.user);
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
			if(NewShowTopicActivity.newShowTopicActivity.bmp!=null&&!NewShowTopicActivity.newShowTopicActivity.bmp.isRecycled()){
				NewShowTopicActivity.newShowTopicActivity.bmp.recycle();
			}
			NewShowTopicActivity.newShowTopicActivity.bmp=null;
			NewShowTopicActivity.newShowTopicActivity=null;
			finish();
		}
	}
	public void onTouchScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		LogUtil.i("mi", "arg2==="+arg2+",arg3="+arg3+",hasStart="+hasStart);
		final float centerX = mContainer.getWidth() / 2.0f; 
		final float centerY = mContainer.getHeight() / 2.0f; 
		if(!hasStart&&Math.abs(arg2)>Math.abs(arg3)&&Math.abs(arg2)>touchSlop){
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
						myScrollView.setVisibility(View.VISIBLE);
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
						myScrollView.setVisibility(View.GONE);
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
	

	
	
	
	
	
	
	

}
