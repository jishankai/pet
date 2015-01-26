package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageWorker;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 查看图片界面，图片可以缩放拖动
 * @author admin
 *
 */
public class ShowPictureActivity extends Activity {
	DisplayImageOptions displayImageOptions;
	ImageView imageView;
	ImageFetcher mImageFetcher;
	public static ShowPictureActivity showPictureActivity;
	int mode=0;//0,查看发布的照片；1，查看 头像
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		showPictureActivity=this;
		UiUtil.setScreenInfo(this);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=StringUtil.getScaleByDPI(this);
//		options.inPreferredConfig=Bitmap.Config.RGB_565;
//		options.inPurgeable=true;
//		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.noimg)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		setContentView(R.layout.activity_show_picture);
		String path=getIntent().getStringExtra("path");
		mode=getIntent().getIntExtra("mode", 0);
		final String url=getIntent().getStringExtra("url");
		imageView=(ImageView)findViewById(R.id.imageView1);
//		imageView.setImageURI(Uri.parse(path));
		/*if(mode==0&&ShowTopicActivity.showTopicActivity!=null){
			Bitmap bmp=ShowTopicActivity.showTopicActivity.getBitmap();
			if(bmp!=null){
				imageView.setImageBitmap(bmp);
			}
		}else{*/
		if(mode==0){
mImageFetcher=new ImageFetcher(this, Constants.screen_width, Constants.screen_height);
			
			if(!StringUtil.isEmpty(url)){
				LogUtil.i("me", "照片手势缩放界面url="+Constants.UPLOAD_IMAGE_RETURN_URL+url);
//				mImageFetcher.setWidth(Constants.screen_width);
				mImageFetcher.setImageCache(new ImageCache(this, new ImageCacheParams(url)));
				mImageFetcher.setLoadCompleteListener(new ImageWorker.LoadCompleteListener(){
					@Override
					public void  onComplete(Bitmap bitmap) {
						// TODO Auto-generated method stub
//						imageView.setImageBitmap(bitmap);
						LogUtil.i("me", "照片手势缩放界面w="+imageView.getWidth()+",h="+imageView.getHeight()+",bitmap=null:"+(bitmap==null));
					}
					@Override
					public void getPath(String path) {
						// TODO Auto-generated method stub
						LogUtil.i("me", "照片手势缩放界面path="+path);
//						imageView.setImageBitmap(BitmapFactory.decodeFile(path));
					}
				});
				
				
				mImageFetcher.loadImage(url, imageView,options);
				
			}
		}else if(mode==1){
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+url, imageView/*,displayImageOptions*/);
		} if(mode==2){
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+url, imageView/*,displayImageOptions*/);
		}
			
//		}
		imageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return detector.onTouchEvent(event);
			}
		});
		
	}
	GestureDetector detector=new GestureDetector(new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			imageView.setImageBitmap(null);
			
			showPictureActivity=null;
			if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(ShowPictureActivity.this)){
				PetApplication.petApp.activityList.remove(ShowPictureActivity.this);
			}
			ShowPictureActivity.this.finish();
			System.gc();
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
			LogUtil.i("mi", "onScroll");
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			LogUtil.i("mi", "onFling");
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}
	});
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
