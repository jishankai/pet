package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.EraserView_user_drawPath;
import com.aidigame.hisun.pet.view.EraserView_user_drawPath.OnEraserOverListener;
import com.aidigame.hisun.pet.widget.AudioRecordAndPlayer;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.XinlangShare;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 摸一摸
 * @author admin
 *
 */
public class TouchActivity extends Activity {
     DisplayImageOptions displayImageOptions;//显示图片的格式
	
     AudioRecordAndPlayer andPlayer;
	ViewPager viewPager;
	View view1,view2,view3,view4;
	HomeViewPagerAdapter adapter;
	ArrayList<View> viewList;
	Animal animal;
	RelativeLayout shareBitmapLayout;
	HandleHttpConnectionException handleHttpConnectionException;
	String voicePath;
	EraserView_user_drawPath eraserView;
	RelativeLayout layout;
	String imagePath;
	boolean isTouched;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_touch);
		animal=(Animal)getIntent().getSerializableExtra("animal");
		shareBitmapLayout=(RelativeLayout)findViewById(R.id.share_bitmap_layout);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String pathString=null;
						pathString=HttpUtil.getVoiceUrl(TouchActivity.this,animal.a_id, handleHttpConnectionException.getHandler(TouchActivity.this));
						pathString=HttpUtil.downloadVoiceFile(pathString, handleHttpConnectionException.getHandler(TouchActivity.this));
				if(pathString!=null){
					voicePath=pathString;
//					audioRecordAndPlayer.playAudio(pathString);
//					final boolean flag=HttpUtil.touchApi(animal.a_id, handleHttpConnectionException.getHandler(TouchActivity.this));
					final Animal animal=HttpUtil.isTouched(TouchActivity.this,TouchActivity.this.animal, handleHttpConnectionException.getHandler(TouchActivity.this));
                    runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(animal!=null){
								/*final EraserView_user_drawPath eraserView=(EraserView_user_drawPath)view1.findViewById(R.id.eraser_view);
							     
					        	TextView tv2=(TextView)view1.findViewById(R.id.textView2);
					    		
					    		tv2.setText("你今天已经摸过"+animal.pet_nickName+"了");
					    		eraserView.setEnabled(false);*/
								loadBitmap(animal.touchedPath);
								imagePath=animal.touchedPath;
					           isTouched=animal.isTouched;
							}else{
								viewPager.setCurrentItem(2);
							}
						}
					});
					
				}else{
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							final EraserView_user_drawPath eraserView=(EraserView_user_drawPath)view1.findViewById(R.id.eraser_view);
					     
					        	TextView tv2=(TextView)view1.findViewById(R.id.textView2);
					        	tv2.setText(animal.pet_nickName+"还没有叫一叫");
//					    		eraserView.setEnabled(false);
					    		
					    		if(StringUtil.isEmpty(animal.pet_iconUrl)){
					    			loadBitmap("pet_icon");
					    		}else{
					    			loadBitmap(animal.pet_iconUrl);
					    		}
						}
					});
				}
			}
		}).start();
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		
		initView();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		ImageLoader imageLoader=ImageLoader.getInstance();
		ImageView icon=(ImageView)findViewById(R.id.roundImageView1);
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl,icon , displayImageOptions);
		TextView nameTv=(TextView)findViewById(R.id.textView4);
		findViewById(R.id.close_view).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TouchActivity.this.finish();
			}
		});;
		nameTv.setText(animal.pet_nickName);
		
		
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		view1=LayoutInflater.from(this).inflate(R.layout.item_touch_1, null);
		view2=LayoutInflater.from(this).inflate(R.layout.item_touch_2, null);
		view3=LayoutInflater.from(this).inflate(R.layout.item_touch_3, null);
		viewList=new ArrayList<View>();
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		adapter=new HomeViewPagerAdapter(viewList);
		viewPager.setAdapter(adapter);
		viewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
//				return true;
				return true;
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
//					initView1(0);
					break;
				case 1:
					initView2();
					break;
				case 2:
					initView3();
					break;
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
		initView1(0);
	}
	/**
	 * 
	 * @param mode  0,正常；1，宠物主人未上传声音
	 */
	private void initView1(int mode) {
		// TODO Auto-generated method stub
		
		
		layout=(RelativeLayout)view1.findViewById(R.id.layout);
		eraserView=(EraserView_user_drawPath)view1.findViewById(R.id.eraser_view);
        if(mode==1){
        	TextView tv2=(TextView)view1.findViewById(R.id.textView2);
    		tv2.setText(animal.pet_nickName+"还没有叫一叫");
    		eraserView.setEnabled(false);
        }
		
		ImageView cloudIV1=(ImageView)view1.findViewById(R.id.cloud1);
		ImageView cloudIV2=(ImageView)view1.findViewById(R.id.cloud2);
		StringUtil.viewStartTransAnim(cloudIV1, 5000, 20, 20-Constants.screen_width);
		StringUtil.viewStartTransAnim(cloudIV2, 4800, -20, -20+Constants.screen_width);
		
	    eraserView.setOnEraserOverListener(new OnEraserOverListener() {
			
			@Override
			public void onEraserOver() {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(!StringUtil.isEmpty(voicePath)){
							if(!isTouched){
								final boolean flag=HttpUtil.touchApi(TouchActivity.this,animal.a_id, handleHttpConnectionException.getHandler(TouchActivity.this));
							}
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
//									if(flag){
										
										eraserView.setEnabled(false);
										viewPager.setCurrentItem(1);
//									}else{
//										Toast.makeText(TouchActivity.this, "摸一摸失败", Toast.LENGTH_LONG).show();
//									}
								}
							});
						}else{
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									imagePath=animal.pet_iconUrl;
									viewPager.setCurrentItem(1);
								}
							});
						}
						
					}
				}).start();
				
			}
		});
	    
	}
	public void loadBitmap(String url){
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=2;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		if("pet_icon".equals(url)){
			eraserView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pet_icon),layout.getMeasuredWidth(),layout.getMeasuredHeight());
		}else{
			DisplayImageOptions displayImageOptions=new DisplayImageOptions
		            .Builder()
		            .showImageOnLoading(R.drawable.noimg)
			        .cacheInMemory(true)
			        .cacheOnDisc(true)
			        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
			        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			        .decodingOptions(options)
	                .build();
			ImageLoader imageLoader=ImageLoader.getInstance();
			String path="";
			if(!animal.pet_iconUrl.equals(url)){
				path=Constants.UPLOAD_IMAGE_RETURN_URL+url;
			}else{
				path=Constants.ANIMAL_DOWNLOAD_TX+url;
			}
			imageLoader.loadImage(path, new ImageLoadingListener() {
				
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
					LogUtil.i("scroll", "layot.w="+layout.getWidth()+",layout.h="+layout.getHeight());
					eraserView.setBitmap(loadedImage,layout.getMeasuredWidth(),layout.getMeasuredHeight());
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
	}
	private void initView2() {
		// TODO Auto-generated method stub
		if(voicePath!=null){
			if(andPlayer==null){
				andPlayer=new AudioRecordAndPlayer(this);
			}
			
			andPlayer.playAudio(voicePath);
		}
		ImageView playIv=(ImageView)view2.findViewById(R.id.imageView2);
		final ImageView imageIv=(ImageView)view2.findViewById(R.id.imageView1);
		if("pet_icon".equals(imagePath)){
			eraserView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pet_icon),layout.getMeasuredWidth(),layout.getMeasuredHeight());
		}else{
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds=false;
			options.inSampleSize=2;
			options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			DisplayImageOptions displayImageOptions=new DisplayImageOptions
		            .Builder()
		            .showImageOnLoading(R.drawable.noimg)
			        .cacheInMemory(true)
			        .cacheOnDisc(true)
			        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
			        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			        .decodingOptions(options)
	                .build();
			ImageLoader imageLoader=ImageLoader.getInstance();
			String path="";
			if(!animal.pet_iconUrl.equals(imagePath)){
				path=Constants.UPLOAD_IMAGE_RETURN_URL+imagePath;
			}else{
				path=Constants.ANIMAL_DOWNLOAD_TX+imagePath;
			}
		imageLoader.loadImage(path, new ImageLoadingListener() {
			
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
				float scalX=imageIv.getWidth()/(loadedImage.getWidth()*1f);
				float scalY=imageIv.getHeight()/(loadedImage.getHeight()*1f);
				float scale=scalX>scalY?scalY:scalX;
				Matrix matrix=new Matrix();
				matrix.postScale(scalX, scalX);
				loadedImage=Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(),matrix,true);
			    int bmpH=0;
			    if(imageIv.getHeight()>=loadedImage.getHeight()){
			    	bmpH=loadedImage.getHeight();
			    }else{
			    	bmpH=imageIv.getHeight();
			    }
			    loadedImage=Bitmap.createBitmap(loadedImage, 0, (loadedImage.getHeight()-bmpH)/2, loadedImage.getWidth(), bmpH);
				
				imageIv.setImageBitmap(loadedImage);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
		}
		playIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(voicePath!=null)
				andPlayer.playAudio(voicePath);
			}
		});
		share(view2);
	}
	private void initView3() {
		// TODO Auto-generated method stub
		TextView nameTv=(TextView)view3.findViewById(R.id.textView2);
		String htmlStr="<html><body>"
				+"今天已经摸过 "
				+"<font color=\"#fb6137\">"
				+animal.pet_nickName
				+" </font>"
				+"啦"
				+"</body></html>";
		nameTv.setText(Html.fromHtml(htmlStr));
	}
	public void share(View view){
		ImageView weixinIV=(ImageView)view.findViewById(R.id.imageView3);
		ImageView friendIV=(ImageView)view.findViewById(R.id.imageView4);
		ImageView xinlangIV=(ImageView)view.findViewById(R.id.imageView5);
		weixinIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.api==null){
					boolean flag=WeixinShare.regToWeiXin(TouchActivity.this);
					if(!flag){
						Toast.makeText(TouchActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						
						return;
					}
				}
				Bitmap bmp=ImageUtil.getImageFromView(shareBitmapLayout);
				String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".png";
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(path);
					bmp.compress(CompressFormat.PNG, 100, fos);
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					if(Constants.api==null){
						boolean flag=WeixinShare.regToWeiXin(TouchActivity.this);
						if(!flag){
							Toast.makeText(TouchActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
							
							return;
						}
					}
					if(WeixinShare.shareBitmap(data, 1)){
//						Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(TouchActivity.this,"分享失败。", Toast.LENGTH_LONG).show();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(fos!=null){
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		});
		friendIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.api==null){
					boolean flag=WeixinShare.regToWeiXin(TouchActivity.this);
					if(!flag){
						Toast.makeText(TouchActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						
						return;
					}
				}
				Bitmap bmp=ImageUtil.getImageFromView(shareBitmapLayout);
				String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".png";
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(path);
					bmp.compress(CompressFormat.PNG, 100, fos);
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					if(WeixinShare.shareBitmap(data, 2)){
						Toast.makeText(TouchActivity.this,"成功分享到微信。", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(TouchActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(fos!=null){
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		});
		xinlangIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.hasXinlangAuth(TouchActivity.this)){
					return;
				}
				Bitmap bmp=ImageUtil.getImageFromView(shareBitmapLayout);
				String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".png";
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(path);
					bmp.compress(CompressFormat.PNG, 100, fos);
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					data.des="我在宠物星球里面摸了萌主“"+animal.pet_nickName+"”，“"+animal.pet_nickName+"”乖巧地冲我叫了一声，真可爱~http://home4pet.aidigame.com/(分享自@宠物星球社交应用）";
					if(UserStatusUtil.hasXinlangAuth(TouchActivity.this)){
						
						XinlangShare.sharePicture(data,TouchActivity.this);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(fos!=null){
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		});
	}
}
