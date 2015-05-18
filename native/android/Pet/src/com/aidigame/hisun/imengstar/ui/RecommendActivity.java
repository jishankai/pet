package com.aidigame.hisun.imengstar.ui;

import java.io.File;
import java.util.ArrayList;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.widget.fragment.ShareDialogFragment;
import com.aidigame.hisun.imengstar.widget.fragment.ShareDialogFragment.ShareDialogFragmentResultListener;
import com.aidigame.hisun.imengstar.R;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendActivity extends BaseActivity implements OnClickListener{
	private ImageView backIv,shareIv,heartIv,guidIv;
	private TextView titleTv,numTv/*,moreTv*/;
	private ViewPager viewPager;
	private ArrayList<PetPicture> pictures;
	private PagerAdapter pagerAdapter;
	private int current_page=0;
	private  Handler handler;
	private RelativeLayout rootLayout,shareLayout;
	private PetPicture current_picture;
	private long star_id;
	private String star_title;
	private int gold;
	public View popupParent;
	  public RelativeLayout black_layout;
	  private PetPicture petPicture;
	  private Animation heartAnim;
	  private AnimationListener animationListener;
		private  UMSocialService mController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		star_id=getIntent().getLongExtra("star_id", 1);
		gold=getIntent().getIntExtra("gold", 0);
		petPicture=(PetPicture)getIntent().getSerializableExtra("PetPicture");
		star_title=getIntent().getStringExtra("star_title");
		
		mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		SinaSsoHandler  sinaSsoHandler=new SinaSsoHandler(this);
//		sinaSsoHandler.addToSocialSDK();
		mController.getConfig().setSsoHandler(sinaSsoHandler);
		
		initView();
		initListener();
		initViewPager();
		initData();
	}
	private void initListener() {
		// TODO Auto-generated method stub
		backIv.setOnClickListener(this);
		shareIv.setOnClickListener(this);
		heartIv.setOnClickListener(this);
		guidIv.setOnClickListener(this);
//		moreTv.setOnClickListener(this);
	}
	private void initData() {
		// TODO Auto-generated method stub
//		moreTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//		moreTv.setTextColor(Color.WHITE);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		rootLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.blur, options)));
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				current_page=0;
				final ArrayList<PetPicture> pps=HttpUtil.starImageFavouriteListApi(handler, star_id, 0, RecommendActivity.this);
//				final ArrayList<PetPicture> pps=HttpUtil.begFoodList(handler, current_page, RecommendActivity.this);
				
	          	if(pps!=null){
	          		/*for(int i=0;i<pps.size();i++){
	          			if((pps.get(i).create_time*1000+24*3600*1000)<System.currentTimeMillis()){
	          				pps.remove(i);
	          				i--;
	          			}
	          		}*/runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(petPicture!=null&&pps.contains(petPicture)){
								pps.remove(petPicture);
								pps.add(0,petPicture);
							}else{
								pps.add(0,petPicture);
							}
							if(pps.size()>0){
								current_picture=pps.get(0);
								numTv.setText(""+current_picture.stars);
							}
							
							
							pictures=pps;
							
							pagerAdapter.notifyDataSetChanged();
							if(pictures.size()>0){
								pagerAdapter.notifyDataSetChanged();
							}
						}
					});
	          	}
			}
		}).start();
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		backIv=(ImageView)findViewById(R.id.back);
		shareIv=(ImageView)findViewById(R.id.setup_iv);
		heartIv=(ImageView)findViewById(R.id.bt_heart_iv);
		guidIv=(ImageView)findViewById(R.id.guide);
		titleTv=(TextView)findViewById(R.id.title_tv);
		titleTv.setText(star_title);
		
		numTv=(TextView)findViewById(R.id.num_tv);
//		moreTv=(TextView)findViewById(R.id.more_tv);
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		rootLayout=(RelativeLayout)findViewById(R.id.root_layout);

		popupParent=findViewById(R.id.popup_parent);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		shareLayout=(RelativeLayout)findViewById(R.id.sharelayout);
		
		
		SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		Editor e=sp.edit();
		boolean guide2=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE8, true);
		if(guide2){
			guidIv.setImageResource(R.drawable.guide8);
			guidIv.setVisibility(View.VISIBLE);
			e.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE8, false);
			e.commit();
		}else{
			guidIv.setVisibility(View.GONE);
			
		}
		
		
		
		
		heartAnim=AnimationUtils.loadAnimation(PetApplication.petApp.getApplicationContext(), R.anim.anim_scale_heart);
		heartAnim.setInterpolator(new LinearInterpolator());
		
		
		animationListener=new AnimationListener() {
			
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
				timeHandler.sendEmptyMessageDelayed(2, 1500);
			}
		};
        heartAnim.setAnimationListener(animationListener);
		heartIv.startAnimation(heartAnim);
		
	}
	
	 public Handler timeHandler=new Handler(){
	    	public void handleMessage(android.os.Message msg) {
	    		if(msg.what==1){
	    		
	    		} else if(msg.what==10){
	    		
	    		}else{
//	    			heartIv.clearAnimation();
//	    			heartAnim=AnimationUtils.loadAnimation(PetApplication.petApp.getApplicationContext(), R.anim.anim_scale_heart);
//	    			heartAnim.setInterpolator(new LinearInterpolator());
//	    			heartIv.setAnimation(heartAnim);
//	    			
//	    			animationListener=new AnimationListener() {
//	    				
//	    				@Override
//	    				public void onAnimationStart(Animation animation) {
//	    					// TODO Auto-generated method stub
//	    					
//	    				}
//	    				
//	    				@Override
//	    				public void onAnimationRepeat(Animation animation) {
//	    					// TODO Auto-generated method stub
//	    					
//	    				}
//	    				
//	    				@Override
//	    				public void onAnimationEnd(Animation animation) {
//	    					// TODO Auto-generated method stub
//	    					timeHandler.sendEmptyMessageDelayed(2, 1500);
//	    				}
//	    			};
//	    			heartAnim.setAnimationListener(animationListener);
	    			heartIv.startAnimation(heartAnim);
	    		}
	    		
	    	};
	    };
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	boolean isSending=false;//是否已经点击推荐
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			if(isTaskRoot()){
				if(HomeActivity.homeActivity!=null){
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
				}else{
					Intent intent=new Intent(this,HomeActivity.class);
					this.startActivity(intent);
				}
			}
			
			
			finish();
			System.gc();
			break;

		case R.id.setup_iv:
			shareLayout.setVisibility(View.VISIBLE);
			 android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
			 
			 current_picture.star_title=star_title;
			 ShareDialogFragment sdf=new ShareDialogFragment(current_picture,popupParent,black_layout,3);
			 sdf.setShareDialogFragmentResultListener(new ShareDialogFragmentResultListener() {
				
				@Override
				public void onResult(boolean isSuccess) {
					// TODO Auto-generated method stub
					/*if(isSuccess){
						shareTv1.setText(""+petPicture.shares);
						if(current_show==SHOW_SHARE_LIST){
							showShareUsersList();
						}
					}*/
					shareLayout.setVisibility(View.GONE);
				}
			});
			 ft.replace(R.id.sharelayout,sdf );
			ft.commit();
			break;

		/*case R.id.more_tv:
			buyTicket();
			break;*/

		case R.id.bt_heart_iv:
			if(isSending)return;
			if(pictures==null||pictures.size()==0){
				Toast.makeText(this, "亲，现在还没有可推荐的美图~", Toast.LENGTH_LONG).show();
				return;
			}
			isSending=true;
//			if(PetApplication.myUser.ticket_num>0){
				
//				giveTicket();
			judgeGiveTicket();
//			}else{
//				buyTicket();
//			}
			break;
		case R.id.guide:
			guidIv.setVisibility(View.GONE);
			guidIv.setImageDrawable(new BitmapDrawable());
			break;

		default:
			break;
		}
	}
	private void buyTicket() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(this,Dialog4Activity.class);
		intent.putExtra("mode", 11);
		Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
			
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				isSending=false;
			}
			
			@Override
			public void onButtonTwo() {
				// TODO Auto-generated method stub
				doBuyTicket();
				isSending=false;
			}
			
			@Override
			public void onButtonOne() {
				// TODO Auto-generated method stub
				isSending=false;
				
			}
		};
		startActivity(intent);
	}
	public void doBuyTicket(){
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final boolean result=HttpUtil.starImageBuyTicketApi(handler, star_id, RecommendActivity.this);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(result){
							PetApplication.myUser.ticket_num+=3;
//							numTv.setText("x "+PetApplication.myUser.ticket_num);
						}else{
							Toast.makeText(RecommendActivity.this, "购买成功", Toast.LENGTH_LONG).show();
						}
						
						isSending=false;
					}
				});
			}
		}).start();
	}
	
	public void judgeGiveTicket(){
		 if(!UserStatusUtil.isLoginSuccess(this,HomeActivity.homeActivity.popupParent,HomeActivity.homeActivity.black_layout)){
			 return ;
		 }
		 if(PetApplication.myUser!=null){
			 if(PetApplication.myUser!=null&&PetApplication.myUser.ticket_num>0){
				 giveTicket();
				 return ;
			 }
			 if(PetApplication.myUser.coinCount<gold){
				
				 if(Constants.CON_VERSION.equals(StringUtil.getAPKVersionName(RecommendActivity.this))){
					 Intent intent=new Intent(HomeActivity.homeActivity,DialogNoteActivity.class);
						intent.putExtra("mode", 10);
						intent.putExtra("info", "金币不足");
						HomeActivity.homeActivity.startActivity(intent);
					
				 }else{
					 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
							
							@Override
							public void onClose() {
								// TODO Auto-generated method stub
								isSending=false;
							}
							
							@Override
							public void onButtonTwo() {
								// TODO Auto-generated method stub
								isSending=false;
								Intent intent=new Intent(RecommendActivity.this,ChargeActivity.class);
								RecommendActivity.this.startActivity(intent);
							}
							
							@Override
							public void onButtonOne() {
								// TODO Auto-generated method stub
								isSending=false;
							}
						};
						 Intent intent=new Intent(RecommendActivity.this,Dialog4Activity.class);
						 intent.putExtra("mode", 14);
						 intent.putExtra("num", gold);
						 RecommendActivity.this.startActivity(intent);
				 }
				 
				 
					 return ;
			 }
			
			 SharedPreferences sp=RecommendActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			 boolean flag=sp.getBoolean(Constants.GIVE_TICKET_NOTE_SHOW, false);
			 
			 if(!flag){
				 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
					
					@Override
					public void onClose() {
						// TODO Auto-generated method stub
						isSending=false;
					}
					
					@Override
					public void onButtonTwo() {
						// TODO Auto-generated method stub
						giveTicket();
					}
					
					@Override
					public void onButtonOne() {
						// TODO Auto-generated method stub
						isSending=false;
					}
				};
				 Intent intent=new Intent(this,Dialog4Activity.class);
				 intent.putExtra("mode", 13);
				 intent.putExtra("num",gold);
				 RecommendActivity.this.startActivity(intent);
			 }else{
				 giveTicket();; 
			 }
			 
		 }
	}
	public void giveTicket(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final boolean result=HttpUtil.starImageGiveTicketApi(handler, current_picture.img_id, RecommendActivity.this);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(result){
							PetApplication.myUser.ticket_num--;
							PetApplication.myUser.coinCount=PetApplication.myUser.coinCount-gold;
							current_picture.stars++;
							if(PetApplication.myUser.ticket_num<0)PetApplication.myUser.ticket_num=0;
							if(PetApplication.myUser.coinCount<0)PetApplication.myUser.coinCount=0;
							numTv.setText(""+current_picture.stars);
//							Toast.makeText(RecommendActivity.this, "推荐成功~", Toast.LENGTH_LONG).show();
							giveTicketAnimation();
						}else{
							Toast.makeText(RecommendActivity.this, "推荐失败~", Toast.LENGTH_LONG).show();
						}
						isSending=false;
					}
				});
			}
		}).start();
	}
	
	
	private  void giveTicketAnimation(){
		
		final RelativeLayout layout=(RelativeLayout)findViewById(R.id.anim_layout);
		final View animView=LayoutInflater.from(this).inflate(R.layout.item_food_anim_view, null);
		RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)layout.getLayoutParams();
		if(param!=null){
			if(current_picture.stars>=100){
				param.leftMargin=getResources().getDimensionPixelOffset(R.dimen.one_dip)*70;
			}else if(current_picture.stars>=10){
				param.leftMargin=getResources().getDimensionPixelOffset(R.dimen.one_dip)*60;
			}else {
				param.leftMargin=getResources().getDimensionPixelOffset(R.dimen.one_dip)*55;
			}
			layout.setLayoutParams(param);
			
		}
		
		
		TextView numTv=(TextView)animView.findViewById(R.id.anim_num_tv);
		numTv.setText("+ "+1);
		numTv.setTextColor(getResources().getColor(R.color.anim_plus_one));
		numTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		layout.setVisibility(View.VISIBLE);
		layout.addView(animView);
		animView.findViewById(R.id.iv).setVisibility(View.GONE);
		Animation anim=AnimationUtils.loadAnimation(this, R.anim.anim_set);
		animView.clearAnimation();
		animView.setAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {
			
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
				animView.setVisibility(View.GONE);
//				layout.removeView(animView);
			}
		});
		anim.start();
	}
	
	private void initViewPager() {
		// TODO Auto-generated method stub
		pictures=new ArrayList<PetPicture>();
		if(petPicture!=null){
			pictures.add(petPicture);
			current_picture=petPicture;
			numTv.setText(""+current_picture.stars);
		}
		pagerAdapter=new PagerAdapter() {
			SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_READABLE);
			int width;
			int height;
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return pictures.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
					container.removeView((View)object);
					System.gc();
				
//				super.destroyItem(container, position, object);
			}

			@Override
			public Object instantiateItem(ViewGroup container,final int position) {
				// TODO Auto-generated method stub
				
                    View view=LayoutInflater.from(RecommendActivity.this).inflate(R.layout.item_recommend_viewpager, null);;
                   
                    view.setId(position);
                	ImageView imageView=(ImageView)view.findViewById(R.id.imageview);
                	
                ImageFetcher imageFetcher=new ImageFetcher(RecommendActivity.this, 0);
                int h=Constants.screen_height-getResources().getDimensionPixelSize(R.dimen.one_dip)*180;
                int w=Constants.screen_width-getResources().getDimensionPixelSize(R.dimen.one_dip)*52;
                imageFetcher.IP=imageFetcher.UPLOAD_THUMBMAIL_IMAGE;
                imageFetcher.setImageCache(new ImageCache(RecommendActivity.this, pictures.get(position).url+"@"+w+"w_"+h+"h_"+"1l.jpg"));
                imageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
                	
    				@Override
    				public void onComplete(Bitmap bitmap) {
    					// TODO Auto-generated method stub
    					
    				    
    				}
    				@Override
    				public void getPath(String path) {
    					// TODO Auto-generated method stub
    					File f=new File(path);
    					for(int i=0;i<pictures.size();i++){
    						if(f.getName().contains(pictures.get(i).url)){
    							pictures.get(i).petPicture_path=path;
    						}
    					}
    					
    				}
    				
    				
    			});
                
                
                
                
                imageFetcher.loadImage(pictures.get(position).url+"@"+w+"w_"+h+"h_"+"1l.jpg", imageView, /*options*/null);
				
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						PetPicture p=pictures.get(position);
						if(NewShowTopicActivity.newShowTopicActivity!=null){
							NewShowTopicActivity.newShowTopicActivity.recyle();
						}
						Intent intent=new Intent(RecommendActivity.this,NewShowTopicActivity.class);
//						Intent intent=new Intent(homeActivity,PictureBegActivity .class);
						intent.putExtra("PetPicture", p);
						startActivity(intent);
					}
				});
				container.addView(view);
				
				return view;
			}
			class Holder{
				ImageView imageView;
				TextView foodNum;
				TextView timeTv;
				TextView desTv;
			}
			
		};
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setPageTransformer(true, new PagerTransformer());
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				View view=viewPager.findViewById(arg0);
				current_picture=pictures.get(arg0);
				numTv.setText(""+current_picture.stars);
				if(pictures.size()>0&&pictures.size()-arg0<=3&&!loadingMore){
					loadingMore=true;
					loadMore(pictures.get(pictures.size()-1).img_id);
					
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
	private boolean loadingMore=false;
	private  void loadMore(final long last_id){
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				current_page++;
				
//                final ArrayList<PetPicture> pps=HttpUtil.begFoodList(handler, current_page, RecommendActivity.this);
                final ArrayList<PetPicture> pps=HttpUtil.starImageFavouriteListApi(handler, star_id, current_page, RecommendActivity.this);
	          	if(pps!=null){
	          		/*for(int i=0;i<pps.size();i++){
	          			if((pps.get(i).create_time*1000+24*3600)>=System.currentTimeMillis()){
	          				pps.remove(i);
	          				i--;
	          			}
	          		}*/
	          		
	          		runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							for(int i=0;i<pps.size();i++){
								if(!pictures.contains(pps.get(i)))
								pictures.add(pps.get(i));
							}
							if(pagerAdapter!=null)
							pagerAdapter.notifyDataSetChanged();
							loadingMore=false;
						}
					});
	          	}else{
	          		loadingMore=false;
	          	}
			}
		}).start();
	}
	class PagerTransformer implements ViewPager.PageTransformer{
		float MIN_SCALE = 0.85f;
		 
	    float MIN_ALPHA = 0.5f;
	 
	    @Override
	    public void transformPage(View view, float position) {
	    	int pageWidth = view.getWidth();
	        int pageHeight = view.getHeight();
	 
	        if (position < -1) { // [-Infinity,-1)
	                                // This page is way off-screen to the left.
	            view.setAlpha(0);
	        } else if (position <= 1) { // [-1,1]
	                                    // Modify the default slide transition to
	                                    // shrink the page as well
	            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
	            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
	            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
	            if (position < 0) {
	                view.setTranslationX(horzMargin - vertMargin / 2);
	            } else {
	                view.setTranslationX(-horzMargin + vertMargin / 2);
	            }
	            // Scale the page down (between MIN_SCALE and 1)
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);
	            // Fade the page relative to its size.
	            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
	                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
	        } else { // (1,+Infinity]
	                    // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
	}
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
	}

}
