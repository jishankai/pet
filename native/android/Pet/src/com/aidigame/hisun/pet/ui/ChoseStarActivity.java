package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.blur.Blur;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 启动界面
 * @author admin
 *
 */
public class ChoseStarActivity extends Activity {
	ImageView cloud1,cloud2,catImage,dogImage,imageView2,imageView1;
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
		imageView1=(ImageView)findViewById(R.id.imageView1);
		imageView1.setVisibility(View.VISIBLE);
		timeDelay();
		
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
					public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(ChoseStarActivity.this,FirstPageActivity.class);
						intent.putExtra("url", url);
						ChoseStarActivity.this.startActivity(intent);
						ChoseStarActivity.this.finish();
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								LogUtil.i("mi", "图片像素数："+loadedImage.getByteCount());
								Matrix matrix=new Matrix();
								matrix.setScale(0.4f, 0.4f);
								Bitmap  bmp=loadedImage.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);
								LogUtil.i("mi", "图片像素数："+bmp.getByteCount());
								PetApplication.petApp.blurBmp=Blur.fastblur(ChoseStarActivity.this, bmp, 18);
						        if(bmp!=null&&!bmp.isRecycled()){
						        	bmp.recycle();
						        	bmp=null;
						        }
							}
						}).start();
							
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
