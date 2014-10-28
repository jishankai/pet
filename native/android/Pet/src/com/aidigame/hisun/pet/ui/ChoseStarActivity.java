package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 选择星球   1.汪星，2.喵星
 * @author admin
 *
 */
public class ChoseStarActivity extends Activity implements OnClickListener{
	ImageView cloud1,cloud2,catImage,dogImage,imageView2;
	boolean catAnimaShow=true;
	int mode=-1;//1,表明从策划Menu  “穿越” 条目进入此界面;-1 程序刚启动时，进入此界面
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_chose_star);
		mode=getIntent().getIntExtra("mode", -1);
		imageView2=(ImageView)findViewById(R.id.imageView2);
		SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		if(mode!=1){
		
			boolean isFirst=sp.getBoolean(Constants.IS_FIRST_START_APP, true);
			if(isFirst){
				//TODO
				SharedPreferences.Editor editor=sp.edit();
				editor.putBoolean(Constants.IS_FIRST_START_APP, false);
				editor.commit();
//				imageView2.setVisibility(View.GONE);
			}else{
				//TODO
				String star=sp.getString(Constants.CURRENT_STAR, "dog");
				if("dog".equals(star)){
					Constants.planet=2;
				}else{
					Constants.planet=1;
				}
				if(!Constants.hasStart){
//					timeDelay();
				}else{
					/*Intent intent=new Intent(ChoseStarActivity.this,NewHomeActivity.class);
					
					ChoseStarActivity.this.startActivity(intent);
					ChoseStarActivity.this.finish();*/
				}
				
				
			}
			imageView2.setVisibility(View.VISIBLE);
			timeDelay();
		}
		
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		cloud1=(ImageView)findViewById(R.id.cloud_imageview1);
		cloud2=(ImageView)findViewById(R.id.cloud_imageview2);
		dogImage=(ImageView)findViewById(R.id.dog_star_imageview);
		catImage=(ImageView)findViewById(R.id.cat_star_imageview);
		dogImage.setOnClickListener(this);
		catImage.setOnClickListener(this);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try {
					Thread.sleep(100);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							cloudAnimation(cloud1,-1);
//							cloudAnimation(cloud2,2);
							
						}
					});
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						starShake(dogImage);
					}
				}, 2000);
				try {
					Thread.sleep(200);
                    runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
//							cloudAnimation(cloud1,-1);
							cloudAnimation(cloud2,2);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*
		 * 此处view的id名称颠倒了
		 */
		SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		Editor editor=sp.edit();
		switch (v.getId()) {
		case R.id.cat_star_imageview:
			
            imageView2.setVisibility(View.VISIBLE);
            timeDelay();
			editor.putString(Constants.CURRENT_STAR, "dog");
			Constants.planet=2;
			editor.commit();
			break;
		case R.id.dog_star_imageview:
			 imageView2.setVisibility(View.VISIBLE);
	            timeDelay();
			editor.putString(Constants.CURRENT_STAR, "cat");
			Constants.planet=1;
			editor.commit();
			break;
		}
		
	}
	public void timeDelay(){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String url=HttpUtil.downloadWelcomeImage(null,ChoseStarActivity.this);
				ImageLoader imageLoader=ImageLoader.getInstance();
				imageLoader.loadImage(Constants.WELCOME_IMAGE+url,new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						LogUtil.i("me","下载欢迎图片  开始"+imageUri);
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub
						LogUtil.i("me","下载欢迎图片  失败"+imageUri);
						Intent intent=new Intent(ChoseStarActivity.this,FirstPageActivity.class);
						intent.putExtra("url", url);
						ChoseStarActivity.this.startActivity(intent);
						ChoseStarActivity.this.finish();
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(ChoseStarActivity.this,FirstPageActivity.class);
						intent.putExtra("url", url);
						ChoseStarActivity.this.startActivity(intent);
						ChoseStarActivity.this.finish();
							
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						LogUtil.i("me","下载欢迎图片  取消"+imageUri);
						Intent intent=new Intent(ChoseStarActivity.this,FirstPageActivity.class);
						intent.putExtra("url", url);
						ChoseStarActivity.this.startActivity(intent);
						ChoseStarActivity.this.finish();
					}
				});
			
		}
	}).start();
	}
	/**
	 * 云彩的左右滑动
	 * 
	 * @param view
	 * @param direction  开始时云朵移动的方向，>0向右，<0向左
	 */
	private void cloudAnimation(View view,int direction){
		TranslateAnimation anim=null;
		if(direction<0){
			anim=new TranslateAnimation(view.getMeasuredWidth()*direction,Constants.screen_width+view.getMeasuredWidth()*(-direction),0,0);
			anim.setDuration(7000);
		}else{
			anim=new TranslateAnimation(Constants.screen_width+view.getMeasuredWidth()*(direction),view.getMeasuredWidth()*(-direction),0,0);
			anim.setDuration(6000);
			LogUtil.i("scroll", "Constants.screen_width+view.getMeasuredWidth()*(direction)="+Constants.screen_width+view.getMeasuredWidth()*(direction)+",view.getMeasuredWidth()*(-direction)="+view.getMeasuredWidth()*(-direction));
		}
		
		anim.setFillAfter(true);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		view.clearAnimation();
		view.startAnimation(anim);
	}
	private void starShake(ImageView view){
		AnimationSet animSet=new AnimationSet(false);
		TranslateAnimation anim1=new TranslateAnimation(0, 0, 0, -(view.getMeasuredHeight()/(3*1f)));
		anim1.setDuration(50);
		anim1.setRepeatCount(1);
		anim1.setRepeatMode(Animation.REVERSE);
		anim1.setFillAfter(true);
		animSet.addAnimation(anim1);
		TranslateAnimation anim2=new TranslateAnimation(0, 0, 0, -(view.getMeasuredHeight()/(5*1f)));
		anim2.setDuration(500);
		anim2.setRepeatCount(1);
		anim2.setRepeatMode(Animation.REVERSE);
		anim2.setFillAfter(true);
		animSet.addAnimation(anim2);
		view.clearAnimation();
		animSet.setAnimationListener(new AnimationListener() {
			
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
				if(catAnimaShow){
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							starShake(catImage);
							catAnimaShow=false;
						}
					}, 500);
				}else{
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							starShake(dogImage);
							catAnimaShow=true;
						}
					}, 3000);
				}
			}
		});
		view.startAnimation(animSet);
		
	}

}
