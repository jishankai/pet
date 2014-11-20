package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.OnEditorActionListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.CommentListViewAdapter;
import com.aidigame.hisun.pet.adapter.CommentListViewAdapter.ClickUserName;
import com.aidigame.hisun.pet.adapter.ShowTopicHorizontalAdapter;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.PetPicture.Comments;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.HorizontalListView2;
import com.aidigame.hisun.pet.view.LinearLayoutForListView;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.XinlangShare;
import com.aidigame.hisun.pet.widget.fragment.ClawStyleFunction;
import com.aidigame.hisun.pet.widget.fragment.ClawStyleFunction.ClawFunctionChoseListener;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
import com.aidigame.hisun.pet.widget.fragment.MarketFragment;
import com.aviary.android.feather.library.utils.BitmapUtils;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageWorker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
/**
 * 图片详情界面
 * @author admin
 *
 */
public class ShowTopicActivity extends Activity implements OnClickListener{
	public View popupParent/*,paddingView*/;//PopupWindwo位置相关parent
	DisplayImageOptions displayImageOptions;
	ImageFetcher mImageFetcher;
	BitmapFactory.Options options;
	public Bitmap bmp;
	String bmpPath;
	FrameLayout frameLayout;
	RoundImageView bt2;
	View line1,line2,report_line1;
	TextView reportTV;
	
	
	public ImageView imageView;
	TextView tv1,tv2,tv3,tv4,tv31,giftNumTV,commentNumTV,shareNumTV,ageTV,topicName_tv,atUserTv;
	ImageView bt4,bt1,bt3,add_comment,sendGiftIV,sexIV;
	LinearLayout addcommentLayout;
	public static ShowTopicActivity showTopicActivity;
	LinearLayoutForListView linearLayoutForListView;
	LinearLayout progressLayout;
	ShowProgress showProgress;
	CommentListViewAdapter commentAdapter;
	EditText commentET;
	TextView send_comment_tv;
	boolean show_add_comment=false;
	boolean show_shareLayout=false;
	HorizontalListView2 horizontialListView;
	ShowTopicHorizontalAdapter adapter;
	LinearLayout linearLayout2,linearLayout22;
	ScrollView scrollview;
	LinearLayout scrollParentLayout;
	boolean relativelayout1gone=false;
	int touchSlop;//滑动时间还是点击事件的分界点
	RelativeLayout relativeLayout1,linearLayout33,click33,imageDesRelativelayout,moreLayout;
	public RelativeLayout black_layout;
	boolean judgeFlag=true;
	boolean hiden=false;
	public static final int DOWNLOAD_IMAGE_TX=2;
	int from_w;//来自那块：1,主页；2，宠物资料界面，
	LinearLayout shareLayout;
	RelativeLayout clawFunctionLayout;

	ClawStyleFunction clawStyleFunction;
	TextView tv5;
	LinearLayout imageView2,imageView3,imageView4;
	
	HandleHttpConnectionException handleHttpConnectionException;
	

	PetPicture petPicture;

	public static ArrayList<PetPicture> petPictures;
	int currentPosition=0;
	int mode=0;//
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==Constants.ERROR_MESSAGE){
				ShowDialog.show((String)msg.obj, ShowTopicActivity.this);
			}else if(msg.what==44){
				if(adapter!=null){

					adapter.setPetPicture(petPicture);
					adapter.notifyDataSetChanged();
				}
				if(petPicture.likers!=null&&petPicture.likers.contains(""+Constants.user.userId)){
					if(petPicture.animal.type>100&&petPicture.animal.type<200){
						bt4.setImageResource(R.drawable.fish_red);
					}else{
						bt4.setImageResource(R.drawable.bone_red);
					}
					
				}else{
					if(petPicture.animal.type>100&&petPicture.animal.type<200){
						bt4.setImageResource(R.drawable.fish_white);
					}else{
						bt4.setImageResource(R.drawable.bone_white);
					}
				}
				tv4.setText(""+msg.arg1);
			}else if(msg.what==1){}else{}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_show_topic_new);
		MobclickAgent.onEvent(ShowTopicActivity.this, "photopage");
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		//获取用户相关信息
		if(Constants.isSuccess&&Constants.user==null){
			downLoadUserInfo();
		}
		 mImageFetcher = new ImageFetcher(this, Constants.screen_width);
		mode=getIntent().getIntExtra("mode", 0);
		from_w=getIntent().getIntExtra("from_w", 1);
		showTopicActivity=this;
		initView();
		reportImages();
		initListener();
		hideBottomInfo(true);
		displayImage();
		
		
	}
	/**
	 * 举报照片
	 */
	public void reportImages(){
		report_line1=(View)findViewById(R.id.report_line1);
		reportTV=(TextView)findViewById(R.id.report_tv);
        findViewById(R.id.three_point_iv).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(show_add_comment){
					show_add_comment=false;
					replySb=false;
//					commentET.setEnabled(false);;
					Animation animation=AnimationUtils.loadAnimation(ShowTopicActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
					
					addcommentLayout.clearAnimation();
					addcommentLayout.setAnimation(animation);;
					animation.start();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							addcommentLayout.setVisibility(View.GONE);
						}
					}, 300);
					
				}
					show_shareLayout=true;
					TranslateAnimation trAnimation=new TranslateAnimation(0, 0, shareLayout.getHeight(),0 );
					trAnimation.setDuration(300);
					shareLayout.clearAnimation();
					shareLayout.setAnimation(trAnimation);
					shareLayout.setVisibility(View.VISIBLE);
					moreLayout.setVisibility(View.VISIBLE);
					reportTV.setVisibility(View.VISIBLE);
					report_line1.setVisibility(View.VISIBLE);
					trAnimation.start();
					
			}
		});
        reportTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ShowTopicActivity.this,WarningDialogActivity.class);
				intent.putExtra("mode", 2);
				intent.putExtra("img_id", petPicture.img_id);
				ShowTopicActivity.this.startActivity(intent);
				
				TranslateAnimation tAnimation=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
				
				tAnimation.setDuration(500);
			
				shareLayout.clearAnimation();
				shareLayout.setAnimation(tAnimation);
				tAnimation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						show_shareLayout=false;
						shareLayout.setVisibility(View.INVISIBLE);
						moreLayout.setVisibility(View.INVISIBLE);
					}
				}, 500);
				
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		bt1=(ImageView)findViewById(R.id.button1);
		bt3=(ImageView)findViewById(R.id.button3);
		bt2=(RoundImageView)findViewById(R.id.button2);
		bt4=(ImageView)findViewById(R.id.button4);
		horizontialListView=(HorizontalListView2)findViewById(R.id.show_topic_listview);
		progressLayout=(LinearLayout)findViewById(R.id.progress);
		progressLayout.setVisibility(View.INVISIBLE);
		imageDesRelativelayout=(RelativeLayout)findViewById(R.id.atuser_topic_des_layout);
		moreLayout=(RelativeLayout)findViewById(R.id.morelayout);
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv3=(TextView)findViewById(R.id.textView3);
		tv4=(TextView)findViewById(R.id.textView4);
		topicName_tv=(TextView)findViewById(R.id.join_activity_tv);
		atUserTv=(TextView)findViewById(R.id.at_users_tv);
		tv31=(TextView)findViewById(R.id.textView31);
		imageView=(ImageView)findViewById(R.id.imageView1);
		linearLayout33=(RelativeLayout)findViewById(R.id.linearlayout33);
		click33=(RelativeLayout)findViewById(R.id.click33);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		
		linearLayoutForListView=(LinearLayoutForListView)findViewById(R.id.comments_listview);
		commentET=(EditText)findViewById(R.id.edit_comment);
		send_comment_tv=(TextView)findViewById(R.id.send_comment);
		addcommentLayout=(LinearLayout)findViewById(R.id.add_comment_linearlayout);
		send_comment_tv.setText("取消");
		send_comment_tv.setOnClickListener(this);
		commentET.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub
						
								if(!canSend){
					        		InputMethodManager m = (InputMethodManager)   
											commentET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
											m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
											addcommentLayout.setVisibility(View.GONE);
											show_add_comment=false;
											return true;
					        	}
					        	progressLayout.removeAllViews();
					        	if(!UserStatusUtil.isLoginSuccess(ShowTopicActivity.this, progressLayout,black_layout)){
					        		return true;
					        	};
					        	if(sendingComment){
					        		Toast.makeText(ShowTopicActivity.this, "正在发送评论", Toast.LENGTH_LONG).show();
					        		return true;
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
					        		Toast.makeText(ShowTopicActivity.this, "评论内容不能为空。", Toast.LENGTH_SHORT).show();
					        		return true;
					        	}
					        	if("写个评论呗".equals(comment)){
					        		Toast.makeText(ShowTopicActivity.this, "评论内容不能为空。", Toast.LENGTH_SHORT).show();
					        		return true;
					        	}
					        	comment.trim();
//					        	commentET.setEnabled(false);
					        	
					        	sendingComment=true;
					        	//测试 发表评论api
					        	new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub\
										User temp=null;
										if(replySb){
											temp=HttpUtil.sendComment(ShowTopicActivity.this,comment, petPicture.img_id,cmt.usr_id,cmt.name,handleHttpConnectionException.getHandler(ShowTopicActivity.this));
										}else{
											temp=HttpUtil.sendComment(ShowTopicActivity.this,comment, petPicture.img_id,-1,"",handleHttpConnectionException.getHandler(ShowTopicActivity.this));
										}
										final User user=temp;
										runOnUiThread(new Runnable() {
											public void run() {
												if(user!=null){
													if(user.exp!=-1){
														PetPicture.Comments comments=new PetPicture.Comments();
														comments.usr_id=Constants.user.userId;
														comments.create_time=System.currentTimeMillis()/1000;
														comments.body=comment;
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
														
														commentNumTV.setText(""+petPicture.commentsList.size());
														commentAdapter=new CommentListViewAdapter(ShowTopicActivity.this, petPicture.commentsList);
														
														commentAdapter.setClickUserName(new ClickUserName() {
															
															@Override
															public void clickUserName(Comments cmt) {
																// TODO Auto-generated method stub
																if(!UserStatusUtil.isLoginSuccess(ShowTopicActivity.this,popupParent,black_layout))return;
																sendComment(cmt);
															}
															@Override
															public void reportComment() {
																// TODO Auto-generated method stub
																Intent intent=new Intent(ShowTopicActivity.this,WarningDialogActivity.class);
																intent.putExtra("mode", 1);
																intent.putExtra("img_id", petPicture.img_id);
																ShowTopicActivity.this.startActivity(intent);
															}
														});
														linearLayoutForListView.setAdapter(commentAdapter);
//														Toast.makeText(ShowTopicActivity.this, "发表评论成功。", Toast.LENGTH_SHORT).show();
														/*InputMethodManager m = (InputMethodManager) commentET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
														if(m.isActive()){
															m.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
														}*/
//														getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
														
													}else{
														Toast.makeText(ShowTopicActivity.this, "发表评论失败。", Toast.LENGTH_SHORT).show();
													     
													}
													sendingComment=false;
													commentET.setEnabled(true);
													addcommentLayout.setVisibility(View.GONE);
													show_add_comment=false;
													replySb=false;
												}else{
													Toast.makeText(ShowTopicActivity.this, "评论发送失败", Toast.LENGTH_LONG).show();
												}
												
											}
										});
									}
								}).start();
								
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
		atUserTv.setOnClickListener(this);
		add_comment=(ImageView)findViewById(R.id.button44);
		add_comment.setOnClickListener(this);
		line1=findViewById(R.id.line1);
		line2=findViewById(R.id.line2);
		
		popupParent=findViewById(R.id.popup_parent);
		petPicture=(PetPicture)getIntent().getSerializableExtra("PetPicture");
		showProgress=new ShowProgress(this, progressLayout);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				final boolean flag=HttpUtil.imageInfo(petPicture,handleHttpConnectionException.getHandler(ShowTopicActivity.this),ShowTopicActivity.this);
				
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(flag){
								setScrollViewHeight(false);	
							setViews();
							displayImage();
							}else{
								Toast.makeText(ShowTopicActivity.this, "加载图片信息失败", Toast.LENGTH_LONG).show();
							}
							
						}
					});
				
			}
		}).start();
		
//		paddingView=findViewById(R.id.padding_view);
		getFrom();
		if(petPictures!=null)
		currentPosition=petPictures.indexOf(petPicture);
		
		sexIV=(ImageView)findViewById(R.id.sex_iv);
		ageTV=(TextView)findViewById(R.id.age_tv);
		sendGiftIV=(ImageView)findViewById(R.id.send_gift_iv);
		giftNumTV=(TextView)findViewById(R.id.gift_num_tv);
		commentNumTV=(TextView)findViewById(R.id.comment_num_tv);
		shareNumTV=(TextView)findViewById(R.id.share_num_tv);
		linearLayout2=(LinearLayout)findViewById(R.id.linearlayout2);
		linearLayout22=(LinearLayout)findViewById(R.id.linearlayout22);
		scrollParentLayout=(LinearLayout)findViewById(R.id.scroll_parent);
		scrollview=(ScrollView)findViewById(R.id.linearlayout32);
		touchSlop=ViewConfiguration.get(this).getScaledTouchSlop();
		
		setBlurImageBackground();
		
		
		relativeLayout1=(RelativeLayout)findViewById(R.id.relativeLayout1);
		
		
		shareLayout=(LinearLayout)findViewById(R.id.share_linearlayout);
		clawFunctionLayout=(RelativeLayout)findViewById(R.id.claw_function_layout);
	
		tv5=(TextView)findViewById(R.id.textView7);
		imageView2=(LinearLayout)findViewById(R.id.imageView2);
		imageView3=(LinearLayout)findViewById(R.id.imageView3);
		imageView4=(LinearLayout)findViewById(R.id.imageView4);
		
		
		
	}
	private void initListener(){
		
		
		horizontialListView.setEnabled(false);
		click33.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction()==MotionEvent.ACTION_UP){
					if(petPicture.likers!=null){
						Intent intent=new Intent(ShowTopicActivity.this,UsersListActivity.class);
						intent.putExtra("likers", petPicture.likers);
						intent.putExtra("senders", petPicture.senders);
						if(petPicture.animal.type>100&&petPicture.animal.type<200){
							intent.putExtra("animalType",1 );
						}else if(petPicture.animal.type>200&&petPicture.animal.type<300){
							intent.putExtra("animalType", 2);
						}
						
						intent.putExtra("title", "围观群众");
						ShowTopicActivity.this.startActivity(intent);
					}
				}
				return true;
			}
		});
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		sendGiftIV.setOnClickListener(this);
		giftNumTV.setOnClickListener(this);
		imageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return detector.onTouchEvent(event);
			}
		});
		linearLayout2.setOnClickListener(this);
		
		/*
		 * ScrollView判断滑到顶部和底部
		 */
	/*	scrollview.setOnTouchListener(new OnTouchListener() {
			float distance;
			boolean isRecord;
			float ydown=0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isRecord=false;
					if(!isRecord){
						ydown=event.getY();
						isRecord=true;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if(!isRecord){
						ydown=event.getY();
						isRecord=true;
					}
					break;
				case MotionEvent.ACTION_UP:
					LogUtil.i("scroll", "手指抬起");
					distance=event.getY()-ydown;
					if(Math.abs(distance)>=touchSlop){
						
						if(scrollview.getScrollY()<=0){
							LogUtil.i("scroll", "滑动到顶部");
							if(relativelayout1gone){
								TranslateAnimation animation=new TranslateAnimation(0, 0,  -relativeLayout1.getMeasuredHeight(),0);
								animation.setDuration(100);
								animation.setFillAfter(true);
								relativeLayout1.clearAnimation();
								relativeLayout1.startAnimation(animation);
								scrollview.clearAnimation();
								relativeLayout1.setVisibility(View.VISIBLE);
								setScrollViewHeight(false);
								relativelayout1gone=false;
							}
						}else if(!relativelayout1gone){
							TranslateAnimation animation=new TranslateAnimation(0, 0, 0, -relativeLayout1.getMeasuredHeight());
							animation.setDuration(100);
							animation.setFillAfter(true);
							relativeLayout1.clearAnimation();
							relativeLayout1.startAnimation(animation);
							setScrollViewHeight(true);
							animation.setAnimationListener(new AnimationListener() {
								
								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub
//									scrollview.layout(0, 0, Constants.screen_width, scrollview.getHeight()+relativeLayout1.getHeight());
								}
								
								@Override
								public void onAnimationRepeat(Animation animation) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onAnimationEnd(Animation animation) {
									// TODO Auto-generated method stub
									relativeLayout1.setVisibility(View.GONE);
								}
							});
							relativelayout1gone=true;
							LogUtil.i("scroll", "消失");
						}else if(scrollview.getChildAt(0).getMeasuredHeight()<=scrollview.getScrollY()+scrollview.getHeight()){
							LogUtil.i("scroll", "滑动到底部"+"scrollview.getChildAt(0).getMeasuredHeight()="+scrollview.getChildAt(0).getMeasuredHeight()+
									             ",scrollview.getScrollY()="+scrollview.getScrollY()+",scrollview.getHeight()="+scrollview.getHeight());
						}
					}
					break;

				default:
					break;
				}
				return false;
			}
		});*/
		tv5.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView3.setOnClickListener(this);
		imageView4.setOnClickListener(this);
		moreLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TranslateAnimation tAnimation=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
				moreLayout.setVisibility(View.INVISIBLE);
				show_shareLayout=false;
				shareLayout.setVisibility(View.INVISIBLE);
				
				
				tAnimation.setDuration(500);
				
				shareLayout.clearAnimation();
				shareLayout.setAnimation(tAnimation);
//				tAnimation.start();
//				handler.postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						show_shareLayout=false;
//						shareLayout.setVisibility(View.INVISIBLE);
//						
//					}
//				}, 500);
			}
		});
	}
	/**
	 * 设置毛玻璃背景，
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		
	}
	public void setViews(){
		
		if(showProgress!=null){
			showProgress.progressCancel();
		}
		if(Constants.user!=null&&Constants.user.aniList!=null){
			if(Constants.user.aniList.contains(petPicture.animal)){
				if(petPicture.animal.master_id==Constants.user.userId){
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
clawStyleFunction.setClawFunctionChoseListener(new ClawFunctionChoseListener() {
			
			@Override
			public void clickItem4() {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(ShowTopicActivity.this,popupParent,black_layout)){
//				    setBlurImageBackground();
					return ;
				}
				Intent intent=new Intent(ShowTopicActivity.this,PlayGameActivity.class);
				intent.putExtra("animal", petPicture.animal);
				ShowTopicActivity.this.startActivity(intent);
			}
			
			@Override
			public void clickItem3() {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(ShowTopicActivity.this,popupParent,black_layout)){
//				    setBlurImageBackground();
					return ;
				}
				if(Constants.user!=null&&Constants.user.aniList!=null){
					if(Constants.user.aniList.contains(petPicture.animal)){
						if(petPicture.animal.master_id==Constants.user.userId){
							Intent intent=new Intent(ShowTopicActivity.this,BiteActivity.class);
							intent.putExtra("animal", petPicture.animal);
							ShowTopicActivity.this.startActivity(intent);
						}else{
							Intent intent=new Intent(ShowTopicActivity.this,TouchActivity.class);
							intent.putExtra("animal", petPicture.animal);
							ShowTopicActivity.this.startActivity(intent);
						}
					}else{
						Intent intent=new Intent(ShowTopicActivity.this,TouchActivity.class);
						intent.putExtra("animal", petPicture.animal);
						ShowTopicActivity.this.startActivity(intent);
					}
				}
				/*Intent intent=new Intent(ShowTopicActivity.this,BiteActivity.class);
				intent.putExtra("animal", petPicture.animal);
				ShowTopicActivity.this.startActivity(intent);*/
				
			}
			
			@Override
			public void clickItem2() {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(ShowTopicActivity.this,popupParent,black_layout)){
//				    setBlurImageBackground();
					return ;
				}
				if(Constants.user!=null&&Constants.user.aniList!=null){
					sendGift();
				}
				
			}
			
			@Override
			public void clickItem1() {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(ShowTopicActivity.this,ShakeActivity.class);
				intent.putExtra("animal", petPicture.animal);
				ShowTopicActivity.this.startActivity(intent);*/
				if(!UserStatusUtil.isLoginSuccess(ShowTopicActivity.this,popupParent,black_layout)){
//				    setBlurImageBackground();
					return ;
				}
				if(Constants.user!=null&&Constants.user.aniList!=null){
					if(Constants.user.aniList.contains(petPicture.animal)){
						Intent intent=new Intent(ShowTopicActivity.this,ShakeActivity.class);
						intent.putExtra("animal", petPicture.animal);
						intent.putExtra("mode", 1);
						ShowTopicActivity.this.startActivity(intent);
					}else{
						Intent intent=new Intent(ShowTopicActivity.this,PlayJokeActivity.class);
						intent.putExtra("animal", petPicture.animal);
						intent.putExtra("mode", 2);
						ShowTopicActivity.this.startActivity(intent);
					}
				}
			}
		});
		show_add_comment=false;
		addcommentLayout.setVisibility(View.GONE);
		if(petPicture.commentsList!=null){
			commentAdapter=new CommentListViewAdapter(this, petPicture.commentsList);
			commentNumTV.setText(""+petPicture.commentsList.size());
		}else{
			commentAdapter=new CommentListViewAdapter(this, new ArrayList<PetPicture.Comments>());
			commentNumTV.setText("0");
		}
		commentAdapter.setClickUserName(new ClickUserName() {
			
			@Override
			public void clickUserName(Comments cmt) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(ShowTopicActivity.this,popupParent,black_layout))return;
				sendComment(cmt);
			}
			@Override
			public void reportComment() {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ShowTopicActivity.this,WarningDialogActivity.class);
				intent.putExtra("mode", 1);
				intent.putExtra("img_id", petPicture.img_id);
				ShowTopicActivity.this.startActivity(intent);
			}
		});
		if(petPicture.gifts<=0){
			giftNumTV.setText("木有收到礼物~送TA一个吧");
		}else{
			giftNumTV.setText("已收到"+petPicture.gifts+"件礼物");
		}
		
		shareNumTV.setText(""+petPicture.shares);
		if((StringUtil.isEmpty(petPicture.topic_name)||"##".equals(petPicture.topic_name))&&StringUtil.isEmpty(petPicture.relatesString)&&StringUtil.isEmpty(petPicture.cmt)){
			imageDesRelativelayout.setVisibility(View.GONE);
		}else{
			imageDesRelativelayout.setVisibility(View.VISIBLE);
		}
		if(!StringUtil.isEmpty(petPicture.topic_name)&&!"##".equals(petPicture.topic_name)){
			topicName_tv.setText(""+petPicture.topic_name+"");
			topicName_tv.setVisibility(View.VISIBLE);
		}else{
			topicName_tv.setVisibility(View.GONE);
		}
		if(StringUtil.isEmpty(petPicture.relatesString)){
			atUserTv.setVisibility(View.GONE);
		}else{
			atUserTv.setText(""+petPicture.relatesString);
		}
//		comment_listView.setAdapter(commentAdapter);
		linearLayoutForListView.setAdapter(commentAdapter);
		if(petPicture.animal!=null){
		
			sexIV.setVisibility(View.VISIBLE);
			if(petPicture.animal.a_gender==1){
				sexIV.setImageResource(R.drawable.male1);
			}else if(petPicture.animal.a_gender==2){
				sexIV.setImageResource(R.drawable.female1);
			}
			ageTV.setText(""+petPicture.animal.a_age_str);
			tv1.setText(""+petPicture.animal.pet_nickName);
			 tv2.setText(""+petPicture.animal.race+" |");
			 if(Constants.user!=null&&petPicture.likers!=null&&petPicture.likers.contains(""+Constants.user.userId)){
				 if(petPicture.animal.type>100&&petPicture.animal.type<200){
						bt4.setImageResource(R.drawable.fish_red);
					}else{
						bt4.setImageResource(R.drawable.bone_red);
					}	
				 tv4.setTextColor(getResources().getColor(R.color.orange_red));
				}else{
					if(petPicture.animal.type>100&&petPicture.animal.type<200){
						bt4.setImageResource(R.drawable.fish_white);
					}else{
						bt4.setImageResource(R.drawable.bone_white);
					}
					 tv4.setTextColor(Color.WHITE);
				}
			tv4.setText(""+petPicture.likes);
			tv31.setText(judgeTime(petPicture.create_time));
			if((petPicture.like_txUrlList!=null&&petPicture.like_txUrlList.size()>0)||(petPicture.gift_txUrlList!=null&&petPicture.gift_txUrlList.size()>0)){
				if(adapter!=null){
					adapter.setPetPicture(petPicture);
					adapter.notifyDataSetChanged();
					linearLayout33.setVisibility(View.VISIBLE);
				}else{
					adapter=new ShowTopicHorizontalAdapter(this,null,false);
					adapter.setPetPicture(petPicture);
					horizontialListView.setAdapter(adapter);
					linearLayout33.setVisibility(View.VISIBLE);
				}
				
				
			}else{
				adapter=new ShowTopicHorizontalAdapter(this, null,false);
				adapter.setPetPicture(petPicture);
				horizontialListView.setAdapter(adapter);
				linearLayout33.setVisibility(View.GONE);
			}
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+petPicture.animal.pet_iconUrl, bt2, displayImageOptions,new ImageLoadingListener() {
				
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
					/*scrollview.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					int h=Constants.screen_height-scrollview.getMeasuredHeight();
					if(h>0){
						ViewGroup.LayoutParams params=paddingView.getLayoutParams();
						params.height=h;
						paddingView.setLayoutParams(params);
					}else{
						ViewGroup.LayoutParams params=paddingView.getLayoutParams();
						params.height=0;
						paddingView.setLayoutParams(params);
					}*/
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
		}
		
		
	}
	/**
	 * 动态的设置ScrollView的高度，
	 * @param flag 是否是全屏
	 */
	public void setScrollViewHeight(boolean flag){
		int title_height=0;
		if(flag){
			title_height=0;
		}else {
			title_height=getResources().getDimensionPixelSize(R.dimen.title_height);
		}
		LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)scrollview.getLayoutParams();
		if(params==null){
			params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,Constants.screen_height-title_height);
		}else{
			params.height=Constants.screen_height-title_height;
		}
	
		scrollview.setLayoutParams(params);
	}
	
	boolean sendingComment=false;
	boolean canSend=false;
	boolean isClickLiking=false;
	Toast toast;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			
			if(isTaskRoot()){
				if(NewHomeActivity.homeActivity!=null){
					Intent intent=NewHomeActivity.homeActivity.getIntent();
					if(intent!=null){
						intent.putExtra("mode", NewHomeActivity.HOMEFRAGMENT);
						this.startActivity(intent);
						return;
					}
					
					
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(NewHomeActivity.homeActivity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
				}else{
					Intent intent=new Intent(this,NewHomeActivity.class);
					this.startActivity(intent);
				}
			}
			if(bmp!=null){
				if(!bmp.isRecycled())
				bmp.recycle();
				bmp=null;
			}
			imageView.setImageDrawable(null);
			finish();
			
			
			
			/*if(bmp!=null){
				if(!bmp.isRecycled())
				bmp.recycle();
				bmp=null;
			}
			Intent intent0=null;
			if(NewHomeActivity.homeActivity==null){
				intent0=new Intent(ShowTopicActivity.this,NewHomeActivity.class);
			}else{
				intent0=NewHomeActivity.homeActivity.getIntent();
			}
			
			if(mode==1){
				intent0.putExtra("mode", NewHomeActivity.HOMEFRAGMENT);
				ShowTopicActivity.this.startActivity(intent0);
				imageView.setImageBitmap(null);
				this.finish();
				return;
			}else if(mode==2){
				intent0.putExtra("mode", NewHomeActivity.HOMEFRAGMENT);
				ShowTopicActivity.this.startActivity(intent0);
				imageView.setImageBitmap(null);
				this.finish();
				return;
			}else if(mode==3){
				intent0.putExtra("mode", NewHomeActivity.HOMEFRAGMENT);
				ShowTopicActivity.this.startActivity(intent0);
				imageView.setImageBitmap(null);
				this.finish();
				return ;
			}
			if(from_w==2){
				imageView.setImageBitmap(null);
				this.finish();
			}else{
				ShowTopicActivity.this.startActivity(intent0);
				imageView.setImageBitmap(null);
				this.finish();
			}*/
			
			break;
		case R.id.button2:
			if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.finish();
					Intent intent1=new Intent(this,PetKingdomActivity.class);
					intent1.putExtra("animal", petPicture.animal);
					this.startActivity(intent1);
//				this.finish();
			break;
		case R.id.add_focus_tv:
			if(!UserStatusUtil.isLoginSuccess(this,popupParent,black_layout))return;
			/*
			 * 增加或删除关注
			 */
			
			break;
		case R.id.send_gift_iv:
			/*
			 *赠送礼物弹窗 
			 */
			sendGift();
			break;
		case R.id.gift_num_tv:
			sendGift();
			break;
		case R.id.button3:
			
//			if(!UserStatusUtil.isLoginSuccess(this,popupParent,black_layout))return;
//			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			if(show_add_comment){
				show_add_comment=false;
				replySb=false;
//				commentET.setEnabled(false);;
				Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				
				addcommentLayout.clearAnimation();
				addcommentLayout.setAnimation(animation);;
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						addcommentLayout.setVisibility(View.GONE);
					}
				}, 300);
				
			}
				show_shareLayout=true;
				TranslateAnimation trAnimation=new TranslateAnimation(0, 0, shareLayout.getHeight(),0 );
				trAnimation.setDuration(300);
				shareLayout.clearAnimation();
				shareLayout.setAnimation(trAnimation);
				shareLayout.setVisibility(View.VISIBLE);
				moreLayout.setVisibility(View.VISIBLE);
				reportTV.setVisibility(View.GONE);
				report_line1.setVisibility(View.GONE);
				trAnimation.start();
				
			
			
			
			break;
		case R.id.button4:
			
			break;
        case R.id.button44:
        	
        	if(!UserStatusUtil.isLoginSuccess(ShowTopicActivity.this, popupParent,black_layout)){
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
				
				if(show_shareLayout){
					show_shareLayout=false;
					TranslateAnimation tAnimation=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
					tAnimation.setDuration(300);
					shareLayout.clearAnimation();
					shareLayout.setAnimation(tAnimation);
					tAnimation.start();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							shareLayout.setVisibility(View.INVISIBLE);
							moreLayout.setVisibility(View.INVISIBLE);
						}
					}, 300);
					
				}
				
				Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
				addcommentLayout.setAnimation(animation);
				animation.start();
				addcommentLayout.setVisibility(View.VISIBLE);
				
				
			}

			break;
        case R.id.send_comment:
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
					User temp=null;
					if(replySb){
						temp=HttpUtil.sendComment(ShowTopicActivity.this,comment, petPicture.img_id,cmt.usr_id,cmt.name,handleHttpConnectionException.getHandler(ShowTopicActivity.this));
					}else{
						temp=HttpUtil.sendComment(ShowTopicActivity.this,comment, petPicture.img_id,-1,"",handleHttpConnectionException.getHandler(ShowTopicActivity.this));
					}
					final User user=temp;
					runOnUiThread(new Runnable() {
						public void run() {
							if(user!=null){
								if(user.exp!=-1){
									PetPicture.Comments comments=new PetPicture.Comments();
									comments.usr_id=Constants.user.userId;
									comments.create_time=System.currentTimeMillis()/1000;
									comments.body=comment;
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
									
									commentNumTV.setText(""+petPicture.commentsList.size());
									commentAdapter=new CommentListViewAdapter(ShowTopicActivity.this, petPicture.commentsList);
									
									commentAdapter.setClickUserName(new ClickUserName() {
										
										@Override
										public void clickUserName(Comments cmt) {
											// TODO Auto-generated method stub
											if(!UserStatusUtil.isLoginSuccess(ShowTopicActivity.this,popupParent,black_layout))return;
											sendComment(cmt);
										}
										@Override
										public void reportComment() {
											// TODO Auto-generated method stub
											Intent intent=new Intent(ShowTopicActivity.this,WarningDialogActivity.class);
											intent.putExtra("mode", 1);
											intent.putExtra("img_id", petPicture.img_id);
											ShowTopicActivity.this.startActivity(intent);
										}
									});
									linearLayoutForListView.setAdapter(commentAdapter);
									MobclickAgent.onEvent(ShowTopicActivity.this, "comment");
//									Toast.makeText(ShowTopicActivity.this, "发表评论成功。", Toast.LENGTH_SHORT).show();
									/*InputMethodManager m = (InputMethodManager) commentET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
									if(m.isActive()){
										m.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
									}*/
//									getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
									
								}else{
									Toast.makeText(ShowTopicActivity.this, "发表评论失败。", Toast.LENGTH_SHORT).show();
								     
								}
								sendingComment=false;
								commentET.setEnabled(true);
								addcommentLayout.setVisibility(View.GONE);
								show_add_comment=false;
								replySb=false;
							}else{
								Toast.makeText(ShowTopicActivity.this, "评论发送失败", Toast.LENGTH_LONG).show();
							}
							
						}
					});
				}
			}).start();
        	
//			
        	break;
		case R.id.linearlayout2:
			if(isClickLiking){
				if(toast==null){
//					toast=Toast.makeText(this, "点赞数据刷新中", Toast.LENGTH_LONG);
//					toast.show();
				}
				
				return ;
			}
			if(UserStatusUtil.isLoginSuccess(this,popupParent,black_layout)){
				isClickLiking=true;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						boolean flag=HttpUtil.likeImage(petPicture,handler,ShowTopicActivity.this);
						if(flag){
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									linearLayout33.setVisibility(View.VISIBLE);
									tv4.setText(""+petPicture.likes);
									 /*if(StringUtil.isEmpty(petPicture.likers)){
										 petPicture.likers=""+Constants.user.userId;
									 }else{
										 petPicture.likers+=","+Constants.user.userId;
									 }
									 if(petPicture.like_txUrlList==null){
										 petPicture.like_txUrlList=new ArrayList<String>();
										 petPicture.like_txUrlList.add(Constants.user.u_iconUrl);
									 }else{
										 petPicture.like_txUrlList.add(Constants.user.u_iconUrl);
									 }*/
									adapter.setPetPicture(petPicture);;
									adapter.notifyDataSetChanged();
									if(petPicture.animal.type>100&&petPicture.animal.type<200){
										bt4.setImageResource(R.drawable.fish_red);
									}else{
										bt4.setImageResource(R.drawable.bone_red);
									}
									 tv4.setTextColor(getResources().getColor(R.color.orange_red));
									 isClickLiking=false;
									if(linearLayout33.getVisibility()==View.GONE)linearLayout33.setVisibility(View.VISIBLE);
								}
							});
						}
					}
				}).start();
			}
			

			break;
		case R.id.textView7:
			TranslateAnimation tAnimation=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
		
			tAnimation.setDuration(500);
		
			shareLayout.clearAnimation();
			shareLayout.setAnimation(tAnimation);
			tAnimation.start();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					show_shareLayout=false;
					shareLayout.setVisibility(View.INVISIBLE);
					moreLayout.setVisibility(View.INVISIBLE);
				}
			}, 500);

			break;
		case R.id.imageView2:
			if(Constants.api==null){
				boolean flag=WeixinShare.regToWeiXin(this);
				if(!flag){
					Toast.makeText(this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
					shareLayout.setVisibility(View.INVISIBLE);
					moreLayout.setVisibility(View.INVISIBLE);
					show_shareLayout=false;
					return;
				}
			}
			UserImagesJson.Data data1=new UserImagesJson.Data();
			if(bmpPath!=null){
				data1.path=bmpPath;
				Constants.shareMode=0;
				Constants.whereShare=0;
				if(WeixinShare.shareBitmap(data1, 1)){
					MobclickAgent.onEvent(ShowTopicActivity.this, "photo_share");
				}else{
					Toast.makeText(ShowTopicActivity.this,"分享失败。", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(this, "图片路径有误", Toast.LENGTH_LONG).show();
			}
			
			TranslateAnimation tAnimation2=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
			
			tAnimation2.setDuration(500);
		
			shareLayout.clearAnimation();
			shareLayout.setAnimation(tAnimation2);
			tAnimation2.start();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					show_shareLayout=false;
					shareLayout.setVisibility(View.INVISIBLE);
					moreLayout.setVisibility(View.INVISIBLE);
				}
			}, 500);
			
			break;
		case R.id.imageView3:
			if(Constants.api==null){
				boolean flag=WeixinShare.regToWeiXin(this);
				if(!flag){
					Toast.makeText(this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
					shareLayout.setVisibility(View.INVISIBLE);
					moreLayout.setVisibility(View.INVISIBLE);
					show_shareLayout=false;
					return;
				}
			}
			UserImagesJson.Data data2=new UserImagesJson.Data();
			if(bmpPath!=null){
				data2.path=bmpPath;
				Constants.whereShare=0;
				Constants.shareMode=1;
				if(WeixinShare.shareBitmap(data2, 2)){
					MobclickAgent.onEvent(ShowTopicActivity.this, "photo_share");
				}else{
					Toast.makeText(ShowTopicActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(this, "图片路径有误", Toast.LENGTH_LONG).show();
			}
			
			
			TranslateAnimation tAnimation3=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
			
			tAnimation3.setDuration(500);
		
			shareLayout.clearAnimation();
			shareLayout.setAnimation(tAnimation3);
			tAnimation3.start();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					show_shareLayout=false;
					shareLayout.setVisibility(View.INVISIBLE);
					moreLayout.setVisibility(View.INVISIBLE);
				}
			}, 500);
			
			break;
		case R.id.imageView4:
			if(UserStatusUtil.hasXinlangAuth(this)){
				shareLayout.setVisibility(View.INVISIBLE);
				moreLayout.setVisibility(View.INVISIBLE);
				show_shareLayout=false;
				UserImagesJson.Data data=new UserImagesJson.Data();
				if(bmpPath!=null){
					data.path=bmpPath;
					if(StringUtil.isEmpty(petPicture.cmt)){
						data.des="分享照片http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
					}else{
						data.des=petPicture.cmt+"http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
					}
					
				XinlangShare.sharePicture(data,ShowTopicActivity.this);
				XinlangShare.listener=new XinlangShare.ShareXinlangResultListener() {
					
					@Override
					public void resultOk() {
						// TODO Auto-generated method stub
						MobclickAgent.onEvent(ShowTopicActivity.this, "photo_share");
						shareNumChange();
					}
				};
				}else{
					Toast.makeText(this, "图片路径有误", Toast.LENGTH_LONG).show();
				}
				
				
				
			}
			
			
			break;
		case R.id.at_users_tv:
			Intent intent=new Intent(this,UsersListActivity.class);
			intent.putExtra("title", "@用户");
			intent.putExtra("likers", petPicture.relates);
			
			
			this.startActivity(intent);
			break;
		}
	}
	/**
	 * 回复别人发表的评论
	 */
	boolean replySb=false;
	PetPicture.Comments cmt;
	public void sendComment(PetPicture.Comments cmt){
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
//			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		};
		if(show_add_comment){
//			show_add_comment=false;
//			addcommentLayout.setVisibility(View.GONE);
			show_add_comment=true;
			commentET.setEnabled(true);;
			commentET.setText("");
			commentET.setHint("回复"+cmt.name);
		}else{
			show_add_comment=true;
			commentET.setEnabled(true);;
			commentET.setText("");
			commentET.setHint("回复"+cmt.name);
			if(show_shareLayout){
				show_shareLayout=false;
				TranslateAnimation tAnimation=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
				tAnimation.setDuration(1000);
				shareLayout.clearAnimation();
				shareLayout.setAnimation(tAnimation);
				tAnimation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						shareLayout.setVisibility(View.INVISIBLE);
						moreLayout.setVisibility(View.INVISIBLE);
					}
				}, 1000);
				
			}
			
			Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
			addcommentLayout.setAnimation(animation);
			animation.start();
			addcommentLayout.setVisibility(View.VISIBLE);
			
			
		}

	}
	/**
	 * 
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
				Intent intent=null;
				MarketFragment.from=1;
				if(NewHomeActivity.homeActivity==null){
					intent=new Intent(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity,NewHomeActivity.class);
				}else{
					intent=NewHomeActivity.homeActivity.getIntent();
				}
				intent.putExtra("mode", NewHomeActivity.MARKETFRAGMENT);
				DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.startActivity(intent);
				
			}

			@Override
			public void closeDialog() {
				// TODO Auto-generated method stub
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
				}else{
					petPicture.senders+=","+Constants.user.userId;
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
						adapter.setPetPicture(petPicture);
						adapter.notifyDataSetChanged();
						giftNumTV.setText("已经收到"+petPicture.gifts+"件礼物");
						if(linearLayout33.getVisibility()==View.GONE)linearLayout33.setVisibility(View.VISIBLE);
					}
				});
			}

			@Override
			public void unRegister() {
				// TODO Auto-generated method stub
//				dialog.dismiss();
				if(!UserStatusUtil.isLoginSuccess(ShowTopicActivity.this, popupParent,black_layout)){
	        		
	        	};
			}
		};
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		/*int action=event.getAction();
		if(show_add_comment&&event.getAction()==KeyEvent.KEYCODE_BACK){
			addcommentLayout.setVisibility(View.GONE);
			show_add_comment=false;
			
		}*/
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 判断从哪个界面跳转到此处
	 */
	public void getFrom(){
		/*String from=getIntent().getStringExtra("from");
		if("waterfull".equals(from)){
			showWaterFull1=HomeActivity.homeActivity.showWaterFull1;
		}else if("focus_list".equals(from)){
			showFocusTopics=HomeActivity.homeActivity.showFocusTopics;
		}else if("ScanActivity".equals(from)){
			scanActivitys=ScanActivitys.scanActivitys;
		}else if("OtherUserTopicActivity".equals(from)){
			otherUserTopicActivity=OtherUserTopicActivity.otherUserTopicActivity;
		}else if("UserHomepageActivity".equals(from)){
			userHomepageActivity=UserHomepageActivity.userHomepageActivity;
		}*/
	}
	public void showProgress(){
		
		progressLayout.setVisibility(View.VISIBLE);
		
		progressLayout.setClickable(true);
		if(showProgress==null){
			showProgress=new ShowProgress(this,  progressLayout);
		}else{
			showProgress.showProgress();
		}
		
	}
	public void loadBitmap(Bitmap bitmap){
		
				/*int h=imageView.getHeight();
				Matrix matrix=new Matrix();
				matrix.postScale(Constants.screen_width*1f/bitmap.getWidth(), Constants.screen_width*1f/bitmap.getWidth());
				Bitmap tempBitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				imageView.setImageBitmap(tempBitmap);*/
		        LogUtil.i("me", "照片详情页面，图片进行缩放处理，开始");
				BitmapDrawable drawable=new BitmapDrawable(bitmap);
				int height=(int)((Constants.screen_width*1f/drawable.getMinimumWidth())*drawable.getMinimumHeight());
				 LogUtil.i("me", "照片详情页面，图片进行缩放处理，获得高度height="+height+",Constants.screen_width="+Constants.screen_width+",drawable.getMinimumWidth()="+drawable.getMinimumWidth()+",drawable.getMinimumHeight()="+drawable.getMinimumHeight());
				RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,height);
				LogUtil.i("me", "照片详情页面，图片进行缩放处理,params.height="+params.height+"params.width"+params.width);
				LogUtil.i("me", "照片详情页面，图片进行缩放处理,bitmap.height="+bitmap.getHeight()+"bitmap.width"+bitmap.getWidth());
				imageView.setLayoutParams(params);
				imageView.setImageDrawable(drawable);
		        linearLayout2.setVisibility(View.VISIBLE);
	}
	
	public void downLoadUserInfo(){
		if(Constants.isSuccess){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//加载用户信息
					User user=HttpUtil.info(ShowTopicActivity.this,null,-1);
					
				}
			}).start();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MarketFragment.from=0;
	}
	public void displayImage(){
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=StringUtil.topicImageGetScaleByDPI(this);
		LogUtil.i("me", "照片详情页面Topic图片缩放比例"+StringUtil.topicImageGetScaleByDPI(this));
		if(StringUtil.topicImageGetScaleByDPI(this)>=2){
			options.inPreferredConfig=Bitmap.Config.ARGB_4444;
		}else{
			options.inPreferredConfig=Bitmap.Config.ARGB_8888;
		}
		
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		mImageFetcher.setWidth(Constants.screen_width);
		mImageFetcher.setImageCache(new ImageCache(this, new ImageCacheParams(petPicture.url)));
		
		mImageFetcher.setLoadCompleteListener(new ImageWorker.LoadCompleteListener(){
			@Override
			public void  onComplete(Bitmap bitmap) {
				// TODO Auto-generated method stub
				LogUtil.i("me", "获取图片宽高，宽="+options.outWidth+",高="+options.outHeight);
				LogUtil.i("me", "照片详情页面，图片开始下载");
				
				
				if(bitmap!=null){
					if(StringUtil.isEmpty(petPicture.cmt)){
						tv3.setVisibility(View.GONE);
					}else{
						tv3.setText(petPicture.cmt);
					}
					scrollview.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					int h=Constants.screen_height-scrollview.getMeasuredHeight();
					/*if(h>0){
						ViewGroup.LayoutParams params=paddingView.getLayoutParams();
						params.height=h;
						paddingView.setLayoutParams(params);
					}else{
						ViewGroup.LayoutParams params=paddingView.getLayoutParams();
						params.height=0;
						paddingView.setLayoutParams(params);
					}*/
					LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(Constants.screen_width, Constants.screen_height);
					scrollview.setLayoutParams(param);
					scrollview.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							scrollview.scrollTo(0, 0);
						}
					});
					
					
					
					ShowTopicActivity.this.bmp=bitmap;
					ShowTopicActivity.this.bmpPath=mImageFetcher.getFilePath(ShowTopicActivity.this,petPicture.url);
					LogUtil.i("me", "ShowTopicActivity.this="+ShowTopicActivity.this.bmpPath);
				}
				hideBottomInfo(false);
				
			}
			@Override
			public void getPath(String path) {
				// TODO Auto-generated method stub
				
			}
		});
		mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/petPicture.url, imageView,options);
		/*ImageLoader imageLoader1=ImageLoader.getInstance();
		
		imageLoader1.loadImage(Constants.UPLOAD_IMAGE_RETURN_URL+petPicture.url,displayImageOptions,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				LogUtil.i("me", "获取图片宽高，宽="+options.outWidth+",高="+options.outHeight);
				LogUtil.i("me", "照片详情页面，图片开始下载");
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				LogUtil.i("me", "照片详情页面，图片下载失败"+imageUri);
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				LogUtil.i("me", "照片详情页面，图片下载成功 imagUri="+imageUri);
//				loadBitmap(loadedImage);
				if(StringUtil.isEmpty(petPicture.cmt)){
					tv3.setVisibility(View.GONE);
				}else{
					tv3.setText(petPicture.cmt);
				}
				scrollview.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				int h=Constants.screen_height-scrollview.getMeasuredHeight();
				if(h>0){
					ViewGroup.LayoutParams params=paddingView.getLayoutParams();
					params.height=h;
					paddingView.setLayoutParams(params);
				}else{
					ViewGroup.LayoutParams params=paddingView.getLayoutParams();
					params.height=0;
					paddingView.setLayoutParams(params);
				}
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				LogUtil.i("me", "照片详情页面，图片下载被取消");
			}
		});*/
	}
	public Bitmap getBitmap(){
		return bmp;
	}
	/**
	 * 加载图片详细信息
	 */
	public void loadImageInfo(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(petPictures!=null&&petPictures.size()>currentPosition){
					final boolean flag=HttpUtil.imageInfo(petPictures.get(currentPosition),null,ShowTopicActivity.this);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//关闭加载进度条
							//刷新界面
							showProgress.progressCancel();
							progressLayout.setVisibility(View.INVISIBLE);
							if(flag){
								ShowTopicActivity.this.petPicture=petPictures.get(currentPosition);
								displayImage();
								setViews();
								
							}else{
								Toast.makeText(ShowTopicActivity.this,"数据加载失败", Toast.LENGTH_LONG).show();
							}
						}
					});
				}
				
			}
		}).start();
	}
	public void shareNumChange(){
		petPicture.shares++;
		shareNumTV.setText(""+petPicture.shares);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				final User user=HttpUtil.imageShareNumsApi(ShowTopicActivity.this,petPicture.img_id, handleHttpConnectionException.getHandler(ShowTopicActivity.this));
				/*if(!Constants.isSuccess)return;
				UserStatusUtil.checkUserExpGoldLvRankChange(user, ShowTopicActivity.this, progressLayout);*/
			}
		}).start();
	}
	public void hideBottomInfo(boolean flag){
		if(flag){
			linearLayoutForListView.setVisibility(View.INVISIBLE);
			linearLayout33.setVisibility(View.INVISIBLE);
			line1.setVisibility(View.INVISIBLE);
			line2.setVisibility(View.INVISIBLE);
			imageDesRelativelayout.setVisibility(View.INVISIBLE);
			linearLayout22.setVisibility(View.INVISIBLE);
			linearLayout2.setVisibility(View.INVISIBLE);
		}else{
			linearLayoutForListView.setVisibility(View.VISIBLE);
//			linearLayout33.setVisibility(View.VISIBLE);
			line1.setVisibility(View.VISIBLE);
			line2.setVisibility(View.VISIBLE);
//			imageDesRelativelayout.setVisibility(View.VISIBLE);
			
			linearLayout22.setVisibility(View.VISIBLE);
			linearLayout2.setVisibility(View.VISIBLE);
		}
		
		
	}
	boolean onMore=false;
	boolean onRefresh=false;
	GestureDetector detector=new GestureDetector(new OnGestureListener() {
		Toast toast;
		Toast toast1;
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(ShowTopicActivity.this,ShowPictureActivity.class);
			intent.putExtra("url", petPicture.url);
			ShowTopicActivity.this.startActivity(intent);
			return true;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll","distanceX="+distanceX);
			if(petPictures==null)return true;
			if(Math.abs(distanceX)>Math.abs(distanceY)&&Math.abs(distanceX)>touchSlop){
				if(!onMore){
					onMore=true;
					if(currentPosition>petPictures.size()-10){
						/*if(showWaterFull1!=null){
							showWaterFull1.onMore();
						}*/
					}
				}
				if(onRefresh)return true;
				onRefresh=true;
				showProgress();
				if(distanceX<0){
					currentPosition--;
					if(currentPosition<0){
						currentPosition=0;
						if(toast1!=null)
						toast1.cancel();
						showProgress.progressCancel();
						progressLayout.setVisibility(View.INVISIBLE);
						toast1=Toast.makeText(ShowTopicActivity.this, "已经是最新的一张。", Toast.LENGTH_LONG);
						toast1.show();
					}else{
						//显示数据加载进度条
						
						//联网加载数据
						loadImageInfo();
					}
				}else if(distanceX>0){
					currentPosition++;
					if(currentPosition>=petPictures.size()){
						currentPosition=petPictures.size()-1;
						if(toast!=null)
						toast.cancel();
						showProgress.progressCancel();
						progressLayout.setVisibility(View.INVISIBLE);
						toast=Toast.makeText(ShowTopicActivity.this, "已经是最后一张。", Toast.LENGTH_LONG);
						toast.show();
					}else{
						//显示数据加载进度条
						
						//联网加载数据
						loadImageInfo();
					}
				}else{
					progressLayout.setVisibility(View.INVISIBLE);
				}
				
				return true;
			}else{
				
				return false;
			}
			
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if(Math.abs(velocityX)>Math.abs(velocityY)){
				
				
				return true;
			}else{
				return false;
			}
			
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			onMore=false;
			onRefresh=false;
			return true;
		}
	});
	public String judgeTime(long time2){
		long time1=System.currentTimeMillis()/1000;
		long time=time1-time2;

		 String str="";
         StringBuffer sb=new StringBuffer();
         sb.append("");
         int mode=0;
         if(time<0){
        	 time=-time;
        	 mode=1;
        	 sb.append("未来");
         }
		if(time<60){
			sb.append( str+time+"秒");
		}else if(time/(60)<60){
			sb.append( str+time/(60)+"分钟");
		}else if(time/(60*60)<24){
			sb.append(  str+time/(60*60)+"个小时");
		}else if(time/(60*60*24)<30){
			sb.append(  str+time/(60*60*24)+"天");
		}else if(time/(60*60*24*30)<12){
			sb.append(  str+time/(60*60*24)+"个月");
		}else if(time/(60*60*24*30*12)<1000){
			sb.append( str+time/(60*60*24*30*12)+"年");
		}
		if(mode==0){
			sb.append("前");
		}else{
			sb.append("后");
		}
		if(time<60){
			return "刚刚";
		}else{
			return sb.toString();
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
