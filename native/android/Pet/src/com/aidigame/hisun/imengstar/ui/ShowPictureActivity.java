package com.aidigame.hisun.imengstar.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.huanxin.PhotoView;
import com.aidigame.hisun.imengstar.huanxin.PhotoViewAttacher.OnPhotoTapListener;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.R;
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
public class ShowPictureActivity extends BaseActivity implements MediaScannerConnectionClient{
	DisplayImageOptions displayImageOptions;
	PhotoView imageView;
	ImageFetcher mImageFetcher;
	public static ShowPictureActivity showPictureActivity;
	int mode=0;//0,查看发布的照片；1，查看 头像
	String save_path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		showPictureActivity=this;
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
		imageView=(PhotoView)findViewById(R.id.imageView1);
//		imageView.setImageURI(Uri.parse(path));
		/*if(mode==0&&ShowTopicActivity.showTopicActivity!=null){
			Bitmap bmp=ShowTopicActivity.showTopicActivity.getBitmap();
			if(bmp!=null){
				imageView.setImageBitmap(bmp);
			}
		}else{*/
		if(mode==0){
			
mImageFetcher=new ImageFetcher(this, Constants.screen_width, Constants.screen_height);
int h=Constants.screen_height;
int w=Constants.screen_width;
mImageFetcher.IP=mImageFetcher.UPLOAD_THUMBMAIL_IMAGE;
			if(!StringUtil.isEmpty(url)){
				LogUtil.i("me", "照片手势缩放界面url="+Constants.UPLOAD_IMAGE_RETURN_URL+url);
//				mImageFetcher.setWidth(Constants.screen_width);
				mImageFetcher.setImageCache(new ImageCache(this, new ImageCacheParams(url+"@"+w+"w_"+h+"h_"+"1l.jpg")));
				mImageFetcher.setLoadCompleteListener(new ImageWorker.LoadCompleteListener(){
					@Override
					public void  onComplete(Bitmap bitmap) {
						// TODO Auto-generated method stub
						LogUtil.i("me", "照片手势缩放界面w="+imageView.getWidth()+",h="+imageView.getHeight()+",bitmap=null:"+(bitmap==null));
					}
					@Override
					public void getPath(String path) {
						// TODO Auto-generated method stub
						LogUtil.i("me", "照片手势缩放界面path="+path);
						save_path=path;
					}
				});
				
				
				mImageFetcher.loadImage(url+"@"+w+"w_"+h+"h_"+"1l.jpg", imageView,null);
				
			}
		}else if(mode==1){
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+url+"@"+Constants.screen_height+"w_"+Constants.screen_width+"h_0l.jpg", imageView/*,displayImageOptions*/,new ImageLoadingListener() {
				
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
					String p=Constants.Picture_Root_Path+System.currentTimeMillis()+"headicon.png";
					try {
						OutputStream os=new FileOutputStream(new File(p));
						loadedImage.compress(CompressFormat.PNG, 100, os);
						save_path=p;
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
		} if(mode==2){
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Constants.USER_THUBMAIL_DOWNLOAD_TX+url+"@"+Constants.screen_height+"w_"+Constants.screen_width+"h_0l.jpg", imageView/*,displayImageOptions*/,new ImageLoadingListener() {
				
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
					String p=Constants.Picture_Root_Path+System.currentTimeMillis()+"headicon.png";
					try {
						OutputStream os=new FileOutputStream(new File(p));
						loadedImage.compress(CompressFormat.PNG, 100, os);
						save_path=p;
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
		}
			
//		}
		/*imageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return detector.onTouchEvent(event);
			}
		});*/
		imageView.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View view, float x, float y) {
				// TODO Auto-generated method stub
               
				
				showPictureActivity=null;
				
				ShowPictureActivity.this.finish();
				 imageView.setImageBitmap(null);
				System.gc();
			}
		});
//		imageView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//                imageView.setImageBitmap(null);
//				
//				showPictureActivity=null;
//				
//				ShowPictureActivity.this.finish();
//				System.gc();
//			}
//		});
		imageView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(!StringUtil.isEmpty(save_path)){
					File image=new File(save_path);
					if(image.exists()){
						File f=new File(Environment.getExternalStorageDirectory()+File.separator+"宠物星球");
						if(!f.exists()){
							f.mkdirs();
						}
						BufferedInputStream bis=null;
						BufferedOutputStream bos=null;
						try {
							bis=new BufferedInputStream(new FileInputStream(image));
							bos=new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory()+File.separator+"宠物星球"+File.separator+System.currentTimeMillis()+".png"));
							int length=0;
							byte[] buffer=new byte[1024*8];
							while((length=bis.read(buffer))>0){
								bos.write(buffer, 0, length);
							}
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}finally{
							if(bis!=null){
								try {
									bis.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							if(bos!=null){
								try {
									bos.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							int level=android.os.Build.VERSION.SDK_INT;
							if(level<=18){
								
							
							sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://" + Environment.getExternalStorageDirectory()+File.separator+"宠物星球"))); 
							}else{
								
							MediaScannerConnection.scanFile(ShowPictureActivity.this, new String[] {

									f.toString()},

									null, new MediaScannerConnection.OnScanCompletedListener() {

									public void onScanCompleted(String path, Uri uri)

									{

                                          LogUtil.i("me", "path="+path+";uri="+uri);
									}

									});
							}
							
							Intent intent2=new Intent(HomeActivity.homeActivity,DialogNoteActivity.class);
			  				intent2.putExtra("mode", 10);
			  				intent2.putExtra("info", "图片已经保存到 宠物星球 文件夹里面了~");
			  				HomeActivity.homeActivity.startActivity(intent2);
			  			
			  				
						}
					}
					
				}
				return true;
			}
		});
		
	}
	GestureDetector detector=new GestureDetector(new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			imageView.setImageBitmap(null);
			
			showPictureActivity=null;
			
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
			if(!StringUtil.isEmpty(save_path)){
				File image=new File(save_path);
				if(image.exists()){
					File f=new File(Environment.getExternalStorageDirectory()+File.separator+"宠物星球");
					if(!f.exists()){
						f.mkdirs();
					}
					BufferedInputStream bis=null;
					BufferedOutputStream bos=null;
					try {
						bis=new BufferedInputStream(new FileInputStream(image));
						bos=new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory()+File.separator+"宠物星球"+File.separator+System.currentTimeMillis()+".png"));
						int length=0;
						byte[] buffer=new byte[1024*8];
						while((length=bis.read(buffer))>0){
							bos.write(buffer, 0, length);
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}finally{
						if(bis!=null){
							try {
								bis.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if(bos!=null){
							try {
								bos.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						int level=android.os.Build.VERSION.SDK_INT;
						if(level<=10){
							
						
						sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://" + Environment.getExternalStorageDirectory()+File.separator+"宠物星球"))); 
						}else{
						MediaScannerConnection.scanFile(ShowPictureActivity.this, new String[] {

								f.getAbsolutePath()},

								null, new MediaScannerConnection.OnScanCompletedListener() {

								public void onScanCompleted(String path, Uri uri)

								{


								}

								});
							/*if(conn==null){
								conn=new MediaScannerConnection(ShowPictureActivity.this, ShowPictureActivity.this);
							}
							conn.connect();*/
						}
						
						Intent intent2=new Intent(HomeActivity.homeActivity,DialogNoteActivity.class);
		  				intent2.putExtra("mode", 10);
		  				intent2.putExtra("info", "图片已经保存到 宠物星球 文件夹里面了~");
		  				HomeActivity.homeActivity.startActivity(intent2);
		  			
		  				
					}
				}
				
			}
			
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
	MediaScannerConnection conn;

		@Override
		public void onMediaScannerConnected() {
			// TODO Auto-generated method stub
			try {
				conn.scanFile(Environment.getExternalStorageDirectory()+File.separator+"宠物星球", "image/*");
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		@Override
		public void onScanCompleted(String path, Uri uri) {
			// TODO Auto-generated method stub
			if(conn!=null)conn.disconnect();
		}

}
