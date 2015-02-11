package com.aidigame.hisun.pet.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 求口粮界面，一张图片
 * @author admin
 *
 */
public class PictureBegActivity extends Activity implements OnClickListener{
	
	
	
	TextView giveNum;
	ImageView moreGive,giveHeartIv;
	RelativeLayout showMoreNumLayout,image_layout;
    PopupWindow moreNumWindow;
	int current_give_num=1;
	Animation heartAnim;
	AnimationListener animationListener;
    Handler handler;
	
    LinearLayout progressLayout;
    ShowProgress showProgress;
    
	PetPicture pp;
	
	ImageView imageView;
	TextView foodNum;
	TextView timeTv;
	TextView desTv;
	
	BitmapFactory.Options options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_picture_beg);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		options=new BitmapFactory.Options();
		options.inSampleSize=StringUtil.getScaleByDPI(this);
		LogUtil.i("me", "图片像素压缩比例="+StringUtil.getScaleByDPI(this));
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		
		pp=(PetPicture)getIntent().getSerializableExtra("PetPicture");
		progressLayout=(LinearLayout)findViewById(R.id.progress_layout);
		giveNum=(TextView)findViewById(R.id.give_num);
		moreGive=(ImageView)findViewById(R.id.more_give_iv);
		giveHeartIv=(ImageView)findViewById(R.id.give_food_tv);
		showMoreNumLayout=(RelativeLayout)findViewById(R.id.reward_layout2);
		image_layout=(RelativeLayout)findViewById(R.id.image_layout);
		foodNum=(TextView)findViewById(R.id.food_num_tv);
		timeTv=(TextView)findViewById(R.id.time_tv);
		desTv=(TextView)findViewById(R.id.show_topic_comment_tv);
		imageView=(ImageView)findViewById(R.id.imageview);
		
		heartAnim=AnimationUtils.loadAnimation(this, R.anim.anim_scale_heart);
		heartAnim.setInterpolator(new LinearInterpolator());
		giveHeartIv.setAnimation(heartAnim);
		
		showProgress=new ShowProgress(this, progressLayout);
		
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
		initListener();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final boolean flag=HttpUtil.imageInfo(pp,handler,PictureBegActivity.this);
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(flag&&pp!=null&&pp.animal!=null){
							loadPicture();
						}else{
							Toast.makeText(PictureBegActivity.this, "网络不给力~图片信息获取失败", Toast.LENGTH_LONG).show();
						}
						if(showProgress!=null){
							showProgress=new ShowProgress(PictureBegActivity.this, progressLayout);
						}
						showProgress.progressCancel();
						
					}
				});
			}
		}).start();
		initMoreNum();
	}
	
	public void  loadPicture(){
		ImageFetcher imageFetcher=new ImageFetcher(this, 0);
        imageFetcher.setImageCache(new ImageCache(this, pp.url));
        imageFetcher.loadImage(pp.url, imageView, options);
        foodNum.setText(""+pp.animal.foodNum);
        desTv.setText(pp.cmt);
        if(myTimerTask!=null){
		    myTimerTask.cancel();
		}
		myTimerTask=new MyTimerTask(pp.create_time*1000+24*3600-System.currentTimeMillis());
		Timer timer=new Timer();
		timer.schedule(myTimerTask, 0, 1000);
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
				 /*if(!UserStatusUtil.isLoginSuccess(PictureBegActivity.this,popupParent,black_layout)){
					 return ;
				 }*/
				 if(PetApplication.myUser!=null){
					 if(PetApplication.myUser.coinCount+PetApplication.myUser.food<current_give_num){
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
									Intent intent=new Intent(PictureBegActivity.this,ChargeActivity.class);
									PictureBegActivity.this.startActivity(intent);
								}
								
								@Override
								public void onButtonOne() {
									// TODO Auto-generated method stub
									isGiving=false;
								}
							};
							 Intent intent=new Intent(PictureBegActivity.this,Dialog4Activity.class);
							 intent.putExtra("mode", 3);
							 intent.putExtra("num", current_give_num);
							 PictureBegActivity.this.startActivity(intent);
							 return ;
					 }
					 isGiving=true;
					 if(PetApplication.myUser!=null&&PetApplication.myUser.food>0){
						 giveFood(); 
						 return ;
					 }
					 SharedPreferences sp=PictureBegActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
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
						 Intent intent=new Intent(PictureBegActivity.this,Dialog4Activity.class);
						 intent.putExtra("mode", 2);
						 intent.putExtra("num", current_give_num);
						 PictureBegActivity.this.startActivity(intent);
					 }else{
						 giveFood(); 
					 }
					 
				 }
			}
		});
		showMoreNumLayout.setOnClickListener(this);
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
  			long time=pp.create_time+24*3600-System.currentTimeMillis()/1000;
  			LogUtil.i("me","有过一秒钟time="+time);
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
  				timeTv.setText(hh+":"+mm+":"+ss);
  			
  		
  		}else{
  			giveHeartIv.clearAnimation();
//  			giveHeartIv.setAnimation(heartAnim);
  			giveHeartIv.startAnimation(heartAnim);
  		}
  		
  	};
  };
  boolean isGiving=false;//是否是正在赏
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.reward_layout2:
		if(moreNumWindow!=null){
			moreNumWindow.showAsDropDown(image_layout, this.getResources().getDimensionPixelSize(R.dimen.one_dip)*75, -this.getResources().getDimensionPixelSize(R.dimen.one_dip)*108);
		}
		break;
	}
}
/**
 * 打赏
 */
public void giveFood(){
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			final boolean flag=HttpUtil.awardApi(handler, pp, current_give_num, PictureBegActivity.this);
			PictureBegActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					PetApplication.myUser.food=PetApplication.myUser.food-current_give_num;
					if(PetApplication.myUser.food<0)PetApplication.myUser.food=0;
					
					if(flag){
						Animal animal=pp.animal;
						if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(animal)){
							int index=PetApplication.myUser.aniList.indexOf(animal);
							PetApplication.myUser.aniList.get(index).foodNum+=current_give_num;
						}
						
						foodNum.setText(""+pp.animal.foodNum);
						giveFoodAnimation();
					}else{
						 Toast.makeText(PictureBegActivity.this, "亲，数据错误导致打赏失败", 1000).show();
					}
					isGiving=false;
				}
			});
		}
	}).start();
}
public void giveFoodAnimation(){
	final RelativeLayout layout=(RelativeLayout)findViewById(R.id.anim_layout);
	final View animView=LayoutInflater.from(this).inflate(R.layout.item_food_anim_view, null);
	
	TextView numTv=(TextView)animView.findViewById(R.id.anim_num_tv);
	numTv.setText("+"+current_give_num);
	layout.setVisibility(View.VISIBLE);
	layout.addView(animView);
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
//			layout.removeView(animView);
		}
	});
	anim.start();
}
/**
 * 选择打赏的数目
 */
TextView tv4,tv3,tv2,tv1;
private void initMoreNum() {
	// TODO Auto-generated method stub
	showMoreNumLayout.setOnClickListener(this);
	View view=LayoutInflater.from(this).inflate(R.layout.item_more_num, null);
	tv4=(TextView)view.findViewById(R.id.tv4);
	tv3=(TextView)view.findViewById(R.id.tv3);
	tv2=(TextView)view.findViewById(R.id.tv2);
	tv1=(TextView)view.findViewById(R.id.tv1);

	
	moreNumWindow=new PopupWindow(view,this.getResources().getDimensionPixelSize(R.dimen.one_dip)*120,LayoutParams.WRAP_CONTENT);
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

}
