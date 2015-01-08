package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.ChargeActivity;
import com.aidigame.hisun.pet.ui.Dialog4Activity;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.NewPetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserCardActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class BegFoodFragment extends Fragment implements OnClickListener{
	HomeActivity homeActivity ;
	View view;
	LinearLayout progressLayout;
	ShowProgress showProgress;
	View popupParent;
	RelativeLayout black_layout;
	
	
	
	/*
	 * 信息栏
	 */
	ImageView genderIv,moreGive,giveHeartIv;
    TextView petNameTv,petRaceTv,userNameTv,/*foodNum,timeTv,desTv,*/giveNum;
    RoundImageView petIcon,userIcon;
    RelativeLayout showMoreNumLayout;
    PopupWindow moreNumWindow;
//    RelativeLayout imageRelativeLayout;
    View moreNumView;
    
    
    Handler handler;
	ArrayList<PetPicture> list;
	ViewPager viewPager;
	PagerAdapter pagerAdapter;
	BitmapFactory.Options options;
//	ArrayList<TextView> timeTvs=new ArrayList<TextView>();
//	ArrayList<TextView> foodNumTvs=new ArrayList<TextView>();
	int current_give_num=1;
	int current_page=0;
	Animation heartAnim;
	AnimationListener animationListener;
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.fragment_beg_food, null);
		homeActivity=HomeActivity.homeActivity;
		handler=HandleHttpConnectionException.getInstance().getHandler(homeActivity);
		options=new BitmapFactory.Options();
		options.inSampleSize=StringUtil.getScaleByDPI(homeActivity);
		LogUtil.i("me", "图片像素压缩比例="+StringUtil.getScaleByDPI(homeActivity));
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		initView();
		return view;
	}
	private void initView() {
		// TODO Auto-generated method stub
		progressLayout=(LinearLayout)view.findViewById(R.id.progress_layout);
		popupParent=view.findViewById(R.id.popup_parent);
		black_layout=(RelativeLayout)view.findViewById(R.id.black_layout);
		
		
		
		userIcon=(RoundImageView)view.findViewById(R.id.show_topic_usericon);
		petIcon=(RoundImageView)view.findViewById(R.id.show_topic_peticon);
		genderIv=(ImageView)view.findViewById(R.id.show_topic_petgender);
		petNameTv=(TextView)view.findViewById(R.id.show_topic_petname);
		petRaceTv=(TextView)view.findViewById(R.id.show_topic_pet_race);
		userNameTv=(TextView)view.findViewById(R.id.show_topic_username);
		
//		foodNum=(TextView)view.findViewById(R.id.food_num_tv);
//		timeTv=(TextView)view.findViewById(R.id.time_tv);
//		desTv=(TextView)view.findViewById(R.id.show_topic_comment_tv);
		giveNum=(TextView)view.findViewById(R.id.give_num);
		moreGive=(ImageView)view.findViewById(R.id.more_give_iv);
		giveHeartIv=(ImageView)view.findViewById(R.id.give_food_tv);
		showMoreNumLayout=(RelativeLayout)view.findViewById(R.id.reward_layout2);
//		imageRelativeLayout=(RelativeLayout)view.findViewById(R.id.image_layout);
		
		heartAnim=AnimationUtils.loadAnimation(homeActivity, R.anim.anim_scale_heart);
		heartAnim.setInterpolator(new LinearInterpolator());
		giveHeartIv.setAnimation(heartAnim);
		
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
		heartAnim.start();
		viewPager=(ViewPager)view.findViewById(R.id.viewpager);
		

		
		initViewPager();
		initListener();
		initMoreNum();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				current_page=0;
				final ArrayList<PetPicture> pps=HttpUtil.begFoodList(handler, current_page, homeActivity);
				
	          	if(pps!=null){
	          		for(int i=0;i<pps.size();i++){
	          			if((pps.get(i).create_time*1000+24*3600*1000)<System.currentTimeMillis()){
	          				pps.remove(i);
	          				i--;
	          			}
	          		}
	          		
	          		homeActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(pps.size()>list.size()){
								viewList=new ArrayList<View>();
								for(int i=0;i<pps.size();i++){
					                View view=LayoutInflater.from(homeActivity).inflate(R.layout.item_imageview1, null);
					                /*ImageView imageView=(ImageView)view.findViewById(R.id.imageview);
					                ImageFetcher imageFetcher=new ImageFetcher(homeActivity, 0);
					                imageFetcher.setImageCache(new ImageCache(homeActivity,pps.get(i).url));
					                imageFetcher.loadImage(pps.get(i).url, imageView, options);
					                TextView foodNum=(TextView)view.findViewById(R.id.food_num_tv);
									TextView timeTv=(TextView)view.findViewById(R.id.time_tv);
									TextView desTv=(TextView)view.findViewById(R.id.show_topic_comment_tv);
									PetPicture pp=pps.get(i); 
									if(!StringUtil.isEmpty(pp.cmt)){
								    	   desTv.setText(pp.cmt);
								    }
									foodNum.setText(""+pp.animal.foodNum);*/
									viewList.add(view);
								}
							}
							
							
							list=pps;
							pagerAdapter.notifyDataSetChanged();
							if(list.size()>0){
								updateInfo(0);
							}
						}
					});
	          	}
			}
		}).start();
		
		
	}



	private void initListener() {
		// TODO Auto-generated method stub
		giveHeartIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if(isGiving){
					Toast.makeText(homeActivity, "亲，正在打赏", 1000).show();
					return ;
				}*/
				 if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
					 return ;
				 }
				 if(Constants.user!=null){
					 if(Constants.user.coinCount+Constants.user.food<current_give_num){
						 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
								
								@Override
								public void onClose() {
									// TODO Auto-generated method stub
									isGiving=false;
								}
								
								@Override
								public void onButtonTwo() {
									// TODO Auto-generated method stub
									isGiving=false;
									Intent intent=new Intent(homeActivity,ChargeActivity.class);
									homeActivity.startActivity(intent);
								}
								
								@Override
								public void onButtonOne() {
									// TODO Auto-generated method stub
									isGiving=false;
								}
							};
							 Intent intent=new Intent(homeActivity,Dialog4Activity.class);
							 intent.putExtra("mode", 3);
							 intent.putExtra("num", current_give_num);
							 homeActivity.startActivity(intent);
							 return ;
					 }
					 isGiving=true;
					 if(Constants.user!=null&&Constants.user.food>0){
						 giveFood(); 
						 return ;
					 }
					 SharedPreferences sp=homeActivity.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
					 boolean flag=sp.getBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
					 
					 if(!flag){
						 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
							
							@Override
							public void onClose() {
								// TODO Auto-generated method stub
								isGiving=false;
							}
							
							@Override
							public void onButtonTwo() {
								// TODO Auto-generated method stub
								giveFood();
							}
							
							@Override
							public void onButtonOne() {
								// TODO Auto-generated method stub
								isGiving=false;
							}
						};
						 Intent intent=new Intent(homeActivity,Dialog4Activity.class);
						 intent.putExtra("mode", 2);
						 intent.putExtra("num", current_give_num);
						 homeActivity.startActivity(intent);
					 }else{
						 giveFood(); 
					 }
					 
				 }
			}
		});
		/*giveHeartIv.setOnTouchListener(new OnTouchListener() {
			int h,w;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)giveHeartIv.getLayoutParams();
				if(param==null){
					param=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					heartAnim.cancel();
					giveHeartIv.clearAnimation();
					h=giveHeartIv.getHeight();
					w=giveHeartIv.getWidth();
					param.width=(int)(w*1.2f);
					param.height=(int)(h*1.2f);
					
					
					giveHeartIv.setLayoutParams(param);
					break;

				case MotionEvent.ACTION_UP:
					param.width=w;
					param.height=h;
					
					giveHeartIv.setLayoutParams(param);
					giveHeartIv.clearAnimation();
					heartAnim=AnimationUtils.loadAnimation(homeActivity, R.anim.anim_scale_heart);
					heartAnim.setInterpolator(new LinearInterpolator());
					heartAnim.setAnimationListener(animationListener);
					giveHeartIv.setAnimation(heartAnim);
					heartAnim.start();
					if(isGiving){
						Toast.makeText(homeActivity, "亲，正在打赏", 1000).show();
						return true;
					}
					 if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
						 return true;
					 }
					 if(Constants.user!=null){
						 if(Constants.user.coinCount+Constants.user.food<current_give_num){
							 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
									
									@Override
									public void onClose() {
										// TODO Auto-generated method stub
										isGiving=false;
									}
									
									@Override
									public void onButtonTwo() {
										// TODO Auto-generated method stub
										isGiving=false;
										Intent intent=new Intent(homeActivity,ChargeActivity.class);
										homeActivity.startActivity(intent);
									}
									
									@Override
									public void onButtonOne() {
										// TODO Auto-generated method stub
										isGiving=false;
									}
								};
								 Intent intent=new Intent(homeActivity,Dialog4Activity.class);
								 intent.putExtra("mode", 3);
								 intent.putExtra("num", current_give_num);
								 homeActivity.startActivity(intent);
								 return true;
						 }
						 isGiving=true;
						 if(Constants.user!=null&&Constants.user.food>0){
							 giveFood(); 
							 return true;
						 }
						 SharedPreferences sp=homeActivity.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
						 boolean flag=sp.getBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
						 
						 if(!flag){
							 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
								
								@Override
								public void onClose() {
									// TODO Auto-generated method stub
									isGiving=false;
								}
								
								@Override
								public void onButtonTwo() {
									// TODO Auto-generated method stub
									giveFood();
								}
								
								@Override
								public void onButtonOne() {
									// TODO Auto-generated method stub
									isGiving=false;
								}
							};
							 Intent intent=new Intent(homeActivity,Dialog4Activity.class);
							 intent.putExtra("mode", 2);
							 intent.putExtra("num", current_give_num);
							 homeActivity.startActivity(intent);
						 }else{
							 giveFood(); 
						 }
						 
					 }
					break;
				}
				return true;
			}
		});*/
		showMoreNumLayout.setOnClickListener(this);
		petIcon.setOnClickListener(this);
		userIcon.setOnClickListener(this);
	}
    boolean loadingMore=false;
    ArrayList<View> viewList=new ArrayList<View>();
    
	private void initViewPager() {
		// TODO Auto-generated method stub
		list=new ArrayList<PetPicture>();
		pagerAdapter=new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				if(position<viewList.size())
				container.removeView(viewList.get(position));
//				super.destroyItem(container, position, object);
			}

			@Override
			public Object instantiateItem(ViewGroup container,final int position) {
				// TODO Auto-generated method stub
				
                    View view=viewList.get(position);
                
                	ImageView imageView=(ImageView)view.findViewById(R.id.imageview);
                    
                ImageFetcher imageFetcher=new ImageFetcher(homeActivity, 0);
                imageFetcher.setImageCache(new ImageCache(homeActivity, list.get(position).url));
                imageFetcher.loadImage(list.get(position).url, imageView, options);
				
				TextView foodNum=(TextView)view.findViewById(R.id.food_num_tv);
				TextView timeTv=(TextView)view.findViewById(R.id.time_tv);
				TextView desTv=(TextView)view.findViewById(R.id.show_topic_comment_tv);
				PetPicture pp=list.get(position); 
				if(!StringUtil.isEmpty(pp.cmt)){
			    	   desTv.setText(pp.cmt);
			    }else{
			    	desTv.setText("");
			    }
				foodNum.setText(""+pp.animal.foodNum);
				
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
				LogUtil.i("me", "current_position="+current_position);
				updateInfo(arg0);
				if(list.size()>0&&list.size()-arg0<=3&&!loadingMore){
					loadingMore=true;
					loadMore(list.get(list.size()-1).img_id);
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
	 * 更新界面信息  
	 * @param position
	 */
	int current_position=0;
	public void updateInfo(int position){
		current_position=position;
		PetPicture pp=list.get(position);
	       if(pp.animal.a_gender==1){
	    	   genderIv.setImageResource(R.drawable.male1);
	       }else{
	    	   genderIv.setImageResource(R.drawable.female1);
	       }
		petNameTv.setText(pp.animal.pet_nickName);
	       petRaceTv.setText(pp.animal.race);
	       if(!StringUtil.isEmpty(pp.animal.u_name))
	       userNameTv.setText(pp.animal.u_name);
	      
		loadIcon(pp);
		if(myTimerTask!=null){
		    myTimerTask.cancel();
		}
		myTimerTask=new MyTimerTask(pp.create_time*1000+24*3600-System.currentTimeMillis());
		Timer timer=new Timer();
		timer.schedule(myTimerTask, 0, 1000);
	}

	/**
	 * 用户，宠物头像下载
	 */
	private void loadIcon(PetPicture pp) {
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
		imageLoader1.displayImage(Constants.ANIMAL_DOWNLOAD_TX+pp.animal.pet_iconUrl, petIcon, displayImageOptions1);
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
		imageLoader2.displayImage(Constants.USER_DOWNLOAD_TX+pp.animal.u_tx, userIcon, displayImageOptions2);
		
	}
    
	/**
	 * 选择打赏的数目
	 */
	TextView tv4,tv3,tv2,tv1;
	private void initMoreNum() {
		// TODO Auto-generated method stub
		showMoreNumLayout.setOnClickListener(this);
		View view=LayoutInflater.from(homeActivity).inflate(R.layout.item_more_num, null);
		tv4=(TextView)view.findViewById(R.id.tv4);
		tv3=(TextView)view.findViewById(R.id.tv3);
		tv2=(TextView)view.findViewById(R.id.tv2);
		tv1=(TextView)view.findViewById(R.id.tv1);
		/*LinearLayout.LayoutParams param1=(LinearLayout.LayoutParams)tv1.getLayoutParams();
		if(param1==null){
			param1=new LinearLayout.LayoutParams(homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*120, LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		param1.width=homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*120;
		tv1.setLayoutParams(param1);
		
		LinearLayout.LayoutParams param2=(LinearLayout.LayoutParams)tv2.getLayoutParams();
		if(param2==null){
			param2=new LinearLayout.LayoutParams(homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*120, LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		param2.width=homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*120;
		tv2.setLayoutParams(param2);
		
		LinearLayout.LayoutParams param3=(LinearLayout.LayoutParams)tv3.getLayoutParams();
		if(param3==null){
			param3=new LinearLayout.LayoutParams(homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*120, LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		param3.width=homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*120;
		tv3.setLayoutParams(param3);
		
		LinearLayout.LayoutParams param4=(LinearLayout.LayoutParams)tv4.getLayoutParams();
		if(param4==null){
			param4=new LinearLayout.LayoutParams(homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*120, LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		param4.width=homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*120;
		tv4.setLayoutParams(param1);
		*/
		
		moreNumWindow=new PopupWindow(view,homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*120,LayoutParams.WRAP_CONTENT);
		moreNumWindow.setFocusable(true);
		moreNumWindow.setOutsideTouchable(true);
		moreNumWindow.setBackgroundDrawable(new BitmapDrawable());
		LogUtil.i("mi", "宽度==="+view.getMeasuredWidth());
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv1.setBackgroundDrawable(new BitmapDrawable());
				tv2.setBackgroundDrawable(new BitmapDrawable());
				tv3.setBackgroundDrawable(new BitmapDrawable());
				tv4.setBackgroundDrawable(new BitmapDrawable());
				
				tv1.setBackgroundResource(R.drawable.more_item_bg);
				giveNum.setText(""+1);
				current_give_num=1;
				moreNumWindow.dismiss();
			}
		});
tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv1.setBackgroundDrawable(new BitmapDrawable());
				tv2.setBackgroundDrawable(new BitmapDrawable());
				tv3.setBackgroundDrawable(new BitmapDrawable());
				tv4.setBackgroundDrawable(new BitmapDrawable());
				tv2.setBackgroundResource(R.drawable.more_item_bg);
				giveNum.setText(""+10);
				current_give_num=10;
				moreNumWindow.dismiss();
			}
		});
tv3.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		tv1.setBackgroundDrawable(new BitmapDrawable());
		tv2.setBackgroundDrawable(new BitmapDrawable());
		tv3.setBackgroundDrawable(new BitmapDrawable());
		tv4.setBackgroundDrawable(new BitmapDrawable());
		tv3.setBackgroundResource(R.drawable.more_item_bg);
		giveNum.setText(""+100);
		current_give_num=100;
		moreNumWindow.dismiss();
	}
});
tv4.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		tv1.setBackgroundDrawable(new BitmapDrawable());
		tv2.setBackgroundDrawable(new BitmapDrawable());
		tv3.setBackgroundDrawable(new BitmapDrawable());
		tv4.setBackgroundDrawable(new BitmapDrawable());
		tv4.setBackgroundResource(R.drawable.more_item_bg);
		giveNum.setText(""+1000);
		current_give_num=1000;
		moreNumWindow.dismiss();
	}
});
	}
	/**
      倒计时
    */
    MyTimerTask myTimerTask;
	class MyTimerTask extends TimerTask{
		long time;
        public MyTimerTask(long time){
        	this.time=time;
        }
		@Override
		public void run() {
			// TODO Auto-generated method stub
			timeHandler.sendEmptyMessage(1);
		}
	}
    Handler timeHandler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what==1){
    			
    		LogUtil.i("me","有过一秒钟");
    		if(current_position>=0&&current_position<list.size()){
    			long time=list.get(current_position).create_time+24*3600-System.currentTimeMillis()/1000;
    			LogUtil.i("me","有过一秒钟time="+time);
    			TextView timeTv=(TextView)viewList.get(current_position).findViewById(R.id.time_tv);
    			if(time<=0){
    				timeTv.setText("00:00:00");
    				return;
    			}
    			long h=time/3600;
    			String hh="";
    			if(h<10){
    				hh="0"+h;
    			}else{
    				hh=""+h;
    			}
    			long m=time/60%60;
    			String mm="";
    			if(m<10){
    				mm="0"+m;
    			}else{
    				mm=""+m;
    			}
    			long s=time%60;
    			String ss="";
    			if(s<10){
    				ss="0"+s;
    			}else{
    				ss=""+s;
    			}
    			LogUtil.i("me","有过一秒钟time="+hh+":"+mm+":"+ss);
    			LogUtil.i("me", "current_position="+current_position);
    			if(current_position<viewList.size()){
    				timeTv.setText(hh+":"+mm+":"+ss);
    			}
    			
    		}
    		}else{
    			giveHeartIv.clearAnimation();
    			giveHeartIv.setAnimation(heartAnim);
    			heartAnim.start();
    		}
    		
    	};
    };
    boolean isGiving=false;//是否是正在赏
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
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
			Intent intent1=new Intent(homeActivity,UserDossierActivity.class);
			User user=new User();
			user.userId=list.get(current_position).animal.master_id;
			intent1.putExtra("user", user);
			this.startActivity(intent1);*/
			Intent intent1=new Intent(homeActivity,UserCardActivity.class);
			User user=new User();
			user.userId=list.get(current_position).animal.master_id;
			intent1.putExtra("user", user);
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
			}
			Intent intent2=new Intent(homeActivity,NewPetKingdomActivity.class);
			intent2.putExtra("animal", list.get(current_position).animal);
			this.startActivity(intent2);
			break;
		case R.id.give_food_tv:
			
			break;
		case R.id.reward_layout2:
			if(moreNumWindow!=null){
				moreNumWindow.showAsDropDown(viewPager, homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*75, -homeActivity.getResources().getDimensionPixelSize(R.dimen.one_dip)*108);
			}
			break;

		default:
			break;
		}
	}
	/**
	 * 加载更多
	 * @param last_id
	 */
	public void loadMore(final int last_id){
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				current_page++;
                final ArrayList<PetPicture> pps=HttpUtil.begFoodList(handler, current_page, homeActivity);
				
	          	if(pps!=null){
	          		for(int i=0;i<pps.size();i++){
	          			if((pps.get(i).create_time*1000+24*3600)>=System.currentTimeMillis()){
	          				pps.remove(i);
	          				i--;
	          			}
	          		}
	          		
	          		homeActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							for(int i=0;i<pps.size();i++){
								list.add(pps.get(i));
								 View view=LayoutInflater.from(homeActivity).inflate(R.layout.item_imageview1, null);
								 viewList.add(view);
							}
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
	
	public void refreshList(){
       new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				current_page=0;
				current_position=0;
				final ArrayList<PetPicture> pps=HttpUtil.begFoodList(handler, current_page, homeActivity);
				
	          	if(pps!=null){
	          		for(int i=0;i<pps.size();i++){
	          			if((pps.get(i).create_time*1000+24*3600*1000)<System.currentTimeMillis()){
	          				pps.remove(i);
	          				i--;
	          			}
	          		}
	          		
	          		homeActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(pps.size()>list.size()){
								viewList=new ArrayList<View>();
								for(int i=0;i<pps.size();i++){
					                View view=LayoutInflater.from(homeActivity).inflate(R.layout.item_imageview1, null);
					                /*ImageView imageView=(ImageView)view.findViewById(R.id.imageview);
					                ImageFetcher imageFetcher=new ImageFetcher(homeActivity, 0);
					                imageFetcher.setImageCache(new ImageCache(homeActivity,pps.get(i).url));
					                imageFetcher.loadImage(pps.get(i).url, imageView, options);
					                TextView foodNum=(TextView)view.findViewById(R.id.food_num_tv);
									TextView timeTv=(TextView)view.findViewById(R.id.time_tv);
									TextView desTv=(TextView)view.findViewById(R.id.show_topic_comment_tv);
									PetPicture pp=pps.get(i); 
									if(!StringUtil.isEmpty(pp.cmt)){
								    	   desTv.setText(pp.cmt);
								    }
									foodNum.setText(""+pp.animal.foodNum);*/
									viewList.add(view);
								}
							}
							list=pps;
							viewPager.removeAllViews();
							viewPager.setAdapter(pagerAdapter);
							pagerAdapter.notifyDataSetChanged();
							
							if(list.size()>0){
								viewPager.setCurrentItem(0);
								updateInfo(0);
							}
						}
					});
	          	}
			}
		}).start();
	}
	/**
	 * 打赏
	 */
	public void giveFood(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(current_position>list.size())return;
				final boolean flag=HttpUtil.awardApi(handler, list.get(current_position), current_give_num, homeActivity);
				homeActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Constants.user.food=Constants.user.food-current_give_num;
						if(Constants.user.food<0)Constants.user.food=0;
						
						if(flag){
							Animal animal=list.get(current_position).animal;
							if(Constants.user!=null&&Constants.user.aniList!=null&&Constants.user.aniList.contains(animal)){
								int index=Constants.user.aniList.indexOf(animal);
								Constants.user.aniList.get(index).foodNum+=current_give_num;
							}
							TextView foodNumTv=(TextView)viewList.get(current_position).findViewById(R.id.food_num_tv);
							foodNumTv.setText(""+list.get(current_position).animal.foodNum);
							giveFoodAnimation();
						}else{
							 Toast.makeText(homeActivity, "亲，数据错误导致打赏失败", 1000).show();
						}
						isGiving=false;
					}
				});
			}
		}).start();
	}
	public void giveFoodAnimation(){
		View view=viewList.get(current_position);
	/*	final LinearLayout layout=(LinearLayout)view.findViewById(R.id.anim_layout);
		TextView numTv=(TextView)view.findViewById(R.id.anim_num_tv);
		numTv.setText("+"+current_give_num);
		layout.setVisibility(View.VISIBLE);
		Animation anim=AnimationUtils.loadAnimation(homeActivity, R.anim.anim_set);
		layout.clearAnimation();
		layout.setAnimation(anim);
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
				layout.setVisibility(View.GONE);
			}
		});
		anim.start();*/
		final RelativeLayout layout=(RelativeLayout)view.findViewById(R.id.anim_layout);
		final View animView=LayoutInflater.from(homeActivity).inflate(R.layout.item_food_anim_view, null);
		
		TextView numTv=(TextView)animView.findViewById(R.id.anim_num_tv);
		numTv.setText("+"+current_give_num);
		layout.setVisibility(View.VISIBLE);
		layout.addView(animView);
		Animation anim=AnimationUtils.loadAnimation(homeActivity, R.anim.anim_set);
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		giveNum.setText(""+current_give_num);
	}
}
