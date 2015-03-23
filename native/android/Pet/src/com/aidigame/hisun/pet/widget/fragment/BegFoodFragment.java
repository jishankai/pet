package com.aidigame.hisun.pet.widget.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.ChargeActivity;
import com.aidigame.hisun.pet.ui.Dialog4Activity;
import com.aidigame.hisun.pet.ui.DialogNoteActivity;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.NewPetKingdomActivity;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.ui.PictureBegActivity;
import com.aidigame.hisun.pet.ui.UserCardActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.PLAWaterfull;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.dodola.model.DuitangInfo;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class BegFoodFragment extends Fragment implements OnClickListener{
	private View view;
	private LinearLayout progressLayout;
	private View popupParent;
	private RelativeLayout black_layout;
	
	
	
	/*
	 * 信息栏
	 */
	private ImageView genderIv,giveHeartIv;
	private  TextView petNameTv,petRaceTv,userNameTv,/*foodNum,timeTv,desTv,*/giveNum;
	private  RoundImageView petIcon,userIcon;
	private RelativeLayout showMoreNumLayout;
	private  PopupWindow moreNumWindow;
    
    
    
	private  Handler handler;
	private ArrayList<PetPicture> list;
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	private BitmapFactory.Options options;
//	ArrayList<TextView> timeTvs=new ArrayList<TextView>();
//	ArrayList<TextView> foodNumTvs=new ArrayList<TextView>();
	private int current_give_num=1;
	private int current_page=0;
	private Animation heartAnim;
	private AnimationListener animationListener;
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
		handler=HandleHttpConnectionException.getInstance().getHandler(getActivity());
		options=new BitmapFactory.Options();
		options.inSampleSize=StringUtil.getScaleByDPI(getActivity());
		LogUtil.i("me", "图片像素压缩比例="+StringUtil.getScaleByDPI(getActivity()));
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		current_view=null;
		Context context;
		
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
		

		giveNum=(TextView)view.findViewById(R.id.give_num);
		giveHeartIv=(ImageView)view.findViewById(R.id.give_food_tv);
		showMoreNumLayout=(RelativeLayout)view.findViewById(R.id.reward_layout2);
		heartAnim=AnimationUtils.loadAnimation(PetApplication.petApp.getApplicationContext(), R.anim.anim_scale_heart);
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
				final ArrayList<PetPicture> pps=HttpUtil.begFoodList(handler, current_page, getActivity());
				
	          	if(pps!=null){
	          		for(int i=0;i<pps.size();i++){
	          			if((pps.get(i).create_time*1000+24*3600*1000)<System.currentTimeMillis()){
	          				pps.remove(i);
	          				i--;
	          			}
	          		}
	          		if(getActivity()==null)return;
	          		getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(pps.size()>list.size()){
								
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
				 if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					 return ;
				 }
				 if(PetApplication.myUser!=null){
					 if(PetApplication.myUser.coinCount+PetApplication.myUser.food<current_give_num){
						 
						 if(Constants.CON_VERSION.equals(StringUtil.getAPKVersionName(getActivity()))){
							 Intent intent=new Intent(HomeActivity.homeActivity,DialogNoteActivity.class);
								intent.putExtra("mode", 10);
								intent.putExtra("info", "金币不足");
								HomeActivity.homeActivity.startActivity(intent);
							
						 }else{
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
										Intent intent=new Intent(getActivity(),ChargeActivity.class);
										getActivity().startActivity(intent);
									}
									
									@Override
									public void onButtonOne() {
										// TODO Auto-generated method stub
										isGiving=false;
									}
								};
								 Intent intent=new Intent(getActivity(),Dialog4Activity.class);
								 intent.putExtra("mode", 3);
								 intent.putExtra("num", current_give_num);
								 getActivity().startActivity(intent);
						 }
						 
						 
							 return ;
					 }
					 isGiving=true;
					 if(PetApplication.myUser!=null&&PetApplication.myUser.food>0){
						 giveFood(); 
						 return ;
					 }
					 SharedPreferences sp=getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
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
						 Intent intent=new Intent(getActivity(),Dialog4Activity.class);
						 intent.putExtra("mode", 2);
						 intent.putExtra("num", current_give_num);
						 getActivity().startActivity(intent);
					 }else{
						 giveFood(); 
					 }
					 
				 }
			}
		});
		showMoreNumLayout.setOnClickListener(this);
		petIcon.setOnClickListener(this);
		userIcon.setOnClickListener(this);
	}
	private boolean loadingMore=false;
//    ArrayList<View> viewList=new ArrayList<View>();
    
	private void initViewPager() {
		// TODO Auto-generated method stub
		list=new ArrayList<PetPicture>();
		pagerAdapter=new PagerAdapter() {
			SharedPreferences sp=getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_READABLE);
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
				return list.size();
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
				
                    View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_imageview1, null);;
                    if(position==0&&current_view==null){
                    	current_view=view;
                    }
                    view.setId(position);
                	ImageView imageView=(ImageView)view.findViewById(R.id.imageview);
                	ImageView animIV=(ImageView)view.findViewById(R.id.beg_anim_iv);
                	String p=sp.getString("p_mid"+list.get(position).picture_type+"_animate1_path", "");
                    if(list.get(position).picture_type==1||StringUtil.isEmpty(p)||!new File(p).exists()){
                    	animIV.setImageResource(R.drawable.anim_beg_frame);
                    	AnimationDrawable ad=(AnimationDrawable)animIV.getDrawable();
                    	ad.start();
                    }else /*if(list.get(position).picture_type==1)*/{
                    	AnimationDrawable ad2=new AnimationDrawable();
                    	String path1=sp.getString("p_mid"+list.get(position).picture_type+"_animate1_path", "");
                    	String path2=sp.getString("p_mid"+list.get(position).picture_type+"_animate1_path", "");
                    	if(width==0){
                    		Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.anim_beg1);
                    		width=bmp.getWidth();
                    		height=bmp.getHeight();
                    	}
                    	
                    	RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)animIV.getLayoutParams();
                    	if(param==null)param=new RelativeLayout.LayoutParams(width, height);
                    	param.width=width;
                    	param.height=height;
                    	animIV.setLayoutParams(param);
                    	ad2.addFrame(new BitmapDrawable(BitmapFactory.decodeFile(sp.getString("p_mid"+list.get(position).picture_type+"_animate1_path", ""))), 300);
                    	ad2.addFrame(new BitmapDrawable(BitmapFactory.decodeFile(sp.getString("p_mid"+list.get(position).picture_type+"_animate2_path", ""))), 300);
//                    	ad2.addFrame(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.anim_beg1)), 300);
//                    	ad2.addFrame(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.anim_beg2)), 300);
                    	animIV.clearAnimation();
                    	ad2.setOneShot(false);
                    	animIV.setImageDrawable(ad2);
                    	ad2.start();
                    }
                	
                	
                	
                	
                	
                ImageFetcher imageFetcher=new ImageFetcher(getActivity(), 0);
                int h=Constants.screen_height-getActivity().getResources().getDimensionPixelSize(R.dimen.one_dip)*180;
                int w=Constants.screen_width-getActivity().getResources().getDimensionPixelSize(R.dimen.one_dip)*52;
                imageFetcher.IP=imageFetcher.UPLOAD_THUMBMAIL_IMAGE;
                imageFetcher.setImageCache(new ImageCache(getActivity(), list.get(position).url+"@"+w+"w_"+h+"h_"+"1l.jpg"));
                imageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
                	
    				@Override
    				public void onComplete(Bitmap bitmap) {
    					// TODO Auto-generated method stub
    					
    				    
    				}
    				@Override
    				public void getPath(String path) {
    					// TODO Auto-generated method stub
    					File f=new File(path);
    					for(int i=0;i<list.size();i++){
    						if(f.getName().contains(list.get(i).url)){
    							list.get(i).petPicture_path=f.getName();
    						}
    					}
    					
    				}
    				
    				
    			});
                
                
                
                
                imageFetcher.loadImage(list.get(position).url+"@"+w+"w_"+h+"h_"+"1l.jpg", imageView, /*options*/null);
				
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
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						PetPicture p=list.get(position);
						if(NewShowTopicActivity.newShowTopicActivity!=null){
							NewShowTopicActivity.newShowTopicActivity.recyle();
						}
						Intent intent=new Intent(getActivity(),NewShowTopicActivity.class);
//						Intent intent=new Intent(homeActivity,PictureBegActivity .class);
						intent.putExtra("PetPicture", p);
						getActivity().startActivity(intent);
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
				LogUtil.i("me", "current_position="+current_position);
				View view=viewPager.findViewById(arg0);
				current_view=view;
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
	private int current_position=0;
	private View current_view;
	private  void updateInfo(int position){
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
	       SharedPreferences sp=getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
	      if(pp.picture_type==1){
	    	  giveHeartIv.setImageResource(R.drawable.give_heart);
	    	
	      }else{
	    	  String path=sp.getString("p_mid"+list.get(position).picture_type+"_pic_path", "");
	    	  if(!StringUtil.isEmpty(path)&&new File(path).exists()){
	    		  giveHeartIv.setImageBitmap(BitmapFactory.decodeFile(path));
	    	  }else{
	    		  giveHeartIv.setImageResource(R.drawable.give_heart);
	    	  }
	      }
	       
	       
	       
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
//		        .decodingOptions(options)
                .build();
		ImageLoader imageLoader1=ImageLoader.getInstance();
		int w=getActivity().getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
		imageLoader1.displayImage(Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+pp.animal.pet_iconUrl+"@"+w+"w_"+w+"h_0l.jpg", petIcon, displayImageOptions1);
		DisplayImageOptions displayImageOptions2=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
	            .showImageOnFail(R.drawable.user_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		        .decodingOptions(options)
                .build();
		ImageLoader imageLoader2=ImageLoader.getInstance();
		imageLoader2.displayImage(Constants.USER_THUBMAIL_DOWNLOAD_TX+pp.animal.u_tx+"@"+w+"w_"+w+"h_0l.jpg", userIcon, displayImageOptions2);
		
	}
    
	/**
	 * 选择打赏的数目
	 */
	private TextView tv4,tv3,tv2,tv1;
	private void initMoreNum() {
		// TODO Auto-generated method stub
		showMoreNumLayout.setOnClickListener(this);
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_more_num, null);
		tv4=(TextView)view.findViewById(R.id.tv4);
		tv3=(TextView)view.findViewById(R.id.tv3);
		tv2=(TextView)view.findViewById(R.id.tv2);
		tv1=(TextView)view.findViewById(R.id.tv1);
	
		
		moreNumWindow=new PopupWindow(view,getActivity().getResources().getDimensionPixelSize(R.dimen.one_dip)*120,LayoutParams.WRAP_CONTENT);
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
	private  MyTimerTask myTimerTask;
	private class MyTimerTask extends TimerTask{
		long time;
        public MyTimerTask(long time){
        	this.time=time;
        }
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(timeHandler==null)return;
			timeHandler.sendEmptyMessage(1);
		}
	}
   public Handler timeHandler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what==1){
    			
    		LogUtil.i("me","有过一秒钟");
    		if(current_position>=0&&current_position<list.size()&&current_view!=null){
    			long time=list.get(current_position).create_time+24*3600-System.currentTimeMillis()/1000;
    			LogUtil.i("me","有过一秒钟time="+time);
    			TextView timeTv=(TextView)current_view.findViewById(R.id.time_tv);
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
    			if(current_position<list.size()){
    				timeTv.setText(hh+":"+mm+":"+ss);
    			}
    			
    		}
    		} else if(msg.what==10){
    			if(myTimerTask!=null){
    				myTimerTask.cancel();
    				myTimerTask=null;
    				timeHandler=null;
    			}
    		}else{
    			giveHeartIv.clearAnimation();
    			giveHeartIv.setAnimation(heartAnim);
    			heartAnim.start();
    		}
    		
    	};
    };
    private  boolean isGiving=false;//是否是正在赏
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
			Intent intent1=new Intent(getActivity(),UserCardActivity.class);
			MyUser user=new MyUser();
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
				System.gc();
			}
			Intent intent2=new Intent(getActivity(),NewPetKingdomActivity.class);
			intent2.putExtra("animal", list.get(current_position).animal);
			this.startActivity(intent2);
			break;
		case R.id.give_food_tv:
			
			break;
		case R.id.reward_layout2:
			if(moreNumWindow!=null){
				moreNumWindow.showAsDropDown(viewPager, getActivity().getResources().getDimensionPixelSize(R.dimen.one_dip)*75, -getActivity().getResources().getDimensionPixelSize(R.dimen.one_dip)*108);
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
	private  void loadMore(final int last_id){
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				current_page++;
                final ArrayList<PetPicture> pps=HttpUtil.begFoodList(handler, current_page, getActivity());
				
	          	if(pps!=null){
	          		for(int i=0;i<pps.size();i++){
	          			if((pps.get(i).create_time*1000+24*3600)>=System.currentTimeMillis()){
	          				pps.remove(i);
	          				i--;
	          			}
	          		}
	          		
	          		getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							for(int i=0;i<pps.size();i++){
								list.add(pps.get(i));
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
	private boolean isRefresh=false;
	public void refreshList(){
		if(isRefresh)return;
		isRefresh=true;
       new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				current_page=0;
				current_position=0;
				current_view=null;
				final ArrayList<PetPicture> pps=HttpUtil.begFoodList(handler, current_page, getActivity());
				
	          	if(pps!=null){
	          		for(int i=0;i<pps.size();i++){
	          			if((pps.get(i).create_time*1000+24*3600*1000)<System.currentTimeMillis()){
	          				pps.remove(i);
	          				i--;
	          			}
	          		}
	          		if(getActivity()==null)return;
	          		getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(pps.size()>list.size()){}
							list=pps;
							viewPager.removeAllViews();
							viewPager.setAdapter(pagerAdapter);
							pagerAdapter.notifyDataSetChanged();
							
							if(list.size()>0){
								viewPager.setCurrentItem(0);
								updateInfo(0);
							}
							isRefresh=false;
						}
					});
	          	}else {
	          		isRefresh=false;
	          	}
			}
		}).start();
	}
	/**
	 * 打赏
	 */
	private  void giveFood(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(current_position>list.size())return;
				final boolean flag=HttpUtil.awardApi(handler, list.get(current_position), current_give_num, getActivity());
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						PetApplication.myUser.food=PetApplication.myUser.food-current_give_num;
						if(PetApplication.myUser.food<0)PetApplication.myUser.food=0;
						
						if(flag){
							Animal animal=list.get(current_position).animal;
							if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(animal)){
								int index=PetApplication.myUser.aniList.indexOf(animal);
								PetApplication.myUser.aniList.get(index).foodNum+=current_give_num;
							}
							TextView foodNumTv=(TextView)current_view.findViewById(R.id.food_num_tv);
							foodNumTv.setText(""+list.get(current_position).animal.foodNum);
							giveFoodAnimation();
						}else{
							 Toast.makeText(getActivity(), "亲，数据错误导致打赏失败", 1000).show();
						}
						isGiving=false;
					}
				});
			}
		}).start();
	}
	private  void giveFoodAnimation(){
		View view=current_view;
	
		final RelativeLayout layout=(RelativeLayout)view.findViewById(R.id.anim_layout);
		final View animView=LayoutInflater.from(getActivity()).inflate(R.layout.item_food_anim_view, null);
		
		TextView numTv=(TextView)animView.findViewById(R.id.anim_num_tv);
		numTv.setText("+"+current_give_num);
		layout.setVisibility(View.VISIBLE);
		layout.addView(animView);
		Animation anim=AnimationUtils.loadAnimation(getActivity(), R.anim.anim_set);
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
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.i("run", "xiaohuiqiukouliang  fRagment");
	}
}
