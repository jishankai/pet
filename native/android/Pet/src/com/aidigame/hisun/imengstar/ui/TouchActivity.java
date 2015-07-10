package com.aidigame.hisun.imengstar.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.ImageUtil;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.view.EraserView_user_drawPath;
import com.aidigame.hisun.imengstar.view.EraserView_user_drawPath.OnEraserOverListener;
import com.aidigame.hisun.imengstar.widget.ShowProgress;
import com.aidigame.hisun.imengstar.widget.WeixinShare;
import com.aidigame.hisun.imengstar.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
/**
 * 摸一摸
 * @author admin
 *
 */
public class TouchActivity extends Activity {
	private  DisplayImageOptions displayImageOptions;//显示图片的格式
	
	private ViewPager viewPager;
	private View view1,view2,view3,view4;
	private HomeViewPagerAdapter adapter;
	private ArrayList<View> viewList;
	private Animal animal;
	private HandleHttpConnectionException handleHttpConnectionException;
	private String voicePath;
	private EraserView_user_drawPath eraserView;
	private RelativeLayout layout;
	private String imagePath;
	private boolean isTouched;
	private ShowProgress showProgress;
	private LinearLayout progressLayout;
	private UMSocialService mController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_touch);
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		SinaSsoHandler  sinaSsoHandler=new SinaSsoHandler(this);
		sinaSsoHandler.addToSocialSDK();
		
		
		
		animal=(Animal)getIntent().getSerializableExtra("animal");
		MobclickAgent.onEvent(this, "touch_button");
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		progressLayout=(LinearLayout)findViewById(R.id.progresslayout);
		if(showProgress==null){
			showProgress=new ShowProgress(this, progressLayout);
		}else{
			showProgress.showProgress();
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String pathString=null;
//						pathString=HttpUtil.getVoiceUrl(TouchActivity.this,animal.a_id, handleHttpConnectionException.getHandler(TouchActivity.this));
//						pathString=HttpUtil.downloadVoiceFile(pathString, handleHttpConnectionException.getHandler(TouchActivity.this));
//						final boolean flag=HttpUtil.touchApi(TouchActivity.this,animal.a_id, handleHttpConnectionException.getHandler(TouchActivity.this));
						
						final Animal animal=HttpUtil.isTouched(TouchActivity.this,TouchActivity.this.animal, handleHttpConnectionException.getHandler(TouchActivity.this));
						imagePath=animal.touchedPath;
				           isTouched=animal.isTouched;
				           isTouched=false;
						if(pathString!=null){
					voicePath=pathString;
//					audioRecordAndPlayer.playAudio(pathString);
					
					
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
								if(showProgress!=null)
									showProgress.progressCancel();
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
					        	tv2.setText(/*animal.pet_nickName+*/"摸萌照，得金币，萌萌印心中~");
//					    		eraserView.setEnabled(false);
					        	loadBitmap(animal.touchedPath);
					        	imagePath=animal.touchedPath;
						           isTouched=animal.isTouched;
					    		/*if(StringUtil.isEmpty(animal.pet_iconUrl)){
					    			loadBitmap("pet_icon");
					    		}else{
					    			loadBitmap(animal.pet_iconUrl);
					    		}*/
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
		int w=getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
		imageLoader.displayImage(Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+animal.pet_iconUrl+"@"+w+"w_"+w+"h_0l.jpg",icon , displayImageOptions,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				animal.pet_iconPath=StringUtil.compressEmotion(TouchActivity.this, null);
			}
			
			public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
				// TODO Auto-generated method stub
				
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						/*添加毛玻璃效果
						 *首先要将Bitmap的Config转化为Config.ARGB_8888类型的 
						 */
						animal.pet_iconPath=StringUtil.compressEmotion(TouchActivity.this, loadedImage);
//						
							
					}
				}).start();
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				animal.pet_iconPath=StringUtil.compressEmotion(TouchActivity.this, null);
			}
		});
		TextView nameTv=(TextView)findViewById(R.id.textView4);
		findViewById(R.id.close_view).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				TouchActivity.this.finish();
				System.gc();
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
//		eraserView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pet_icon), eraserView.getMeasuredWidth(),eraserView.getMeasuredHeight());
        if(mode==1){
        	TextView tv2=(TextView)view1.findViewById(R.id.textView2);
    		tv2.setText(/*animal.pet_nickName+*/"摸萌照，得金币，萌萌印心中~");
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
							if(!isTouched){
								final boolean flag=HttpUtil.touchApi(TouchActivity.this,animal.a_id, handleHttpConnectionException.getHandler(TouchActivity.this));
								if(HomeActivity.homeActivity!=null){
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
										}
									});
									
								}
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
//									if(flag){
									MobclickAgent.onEvent(TouchActivity.this, "touch_suc");
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
									MobclickAgent.onEvent(TouchActivity.this, "touch_suc");
									imagePath=animal.touchedPath;
									viewPager.setCurrentItem(1);
								}
							});
						}
						
					}
				}).start();
				
			}
		});
	    
	}
	String url="";
	private  void loadBitmap(String url){
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=StringUtil.getScaleByDPI(TouchActivity.this,url);
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		int w=getResources().getDimensionPixelSize(R.dimen.one_dip)*280;
		int h=getResources().getDimensionPixelSize(R.dimen.one_dip)*170;
		if("pet_icon".equals(url)){
			eraserView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pet_icon),layout.getMeasuredWidth(),layout.getMeasuredHeight());
			if(showProgress!=null)
				showProgress.progressCancel();
		}else{
			
			DisplayImageOptions displayImageOptions=new DisplayImageOptions
		            .Builder()
		            .showImageOnLoading(R.drawable.pet_icon)
		            .showImageOnFail(R.drawable.pet_icon)
			        .cacheInMemory(true)
			        .cacheOnDisc(true)
			        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
			        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//			        .decodingOptions(options)
	                .build();
			ImageLoader imageLoader=ImageLoader.getInstance();
			String path="";
			if(!animal.pet_iconUrl.equals(url)){
				this.url=animal.pet_iconUrl;
				path=Constants.UPLOAD_IMAGE_THUBMAIL_IMAG+url;
			}else{
				path=Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+url;
				this.url=url;
			}
			
			imageLoader.loadImage(path+"@"+w+"w_"+h+"h_0l.jpg",displayImageOptions, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					/*if(showProgress!=null)
					showProgress.progressCancel();*/
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					if(showProgress!=null)
					showProgress.progressCancel();
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					LogUtil.i("scroll", "layot.w="+layout.getWidth()+",layout.h="+layout.getHeight());
					
					eraserView.setBitmap(loadedImage,layout.getMeasuredWidth(),layout.getMeasuredHeight());
					
					handleHttpConnectionException.getHandler(TouchActivity.this).postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(showProgress!=null)
								showProgress.progressCancel();
						}
					}, 300);
					
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					if(showProgress!=null)
					showProgress.progressCancel();
				}
			});
		}
		
	}
	private void initView2() {
		// TODO Auto-generated method stub
		
		ImageView playIv=(ImageView)view2.findViewById(R.id.imageView2);
		final ImageView imageIv=(ImageView)view2.findViewById(R.id.imageView1);
		int w=getResources().getDimensionPixelSize(R.dimen.one_dip)*280;
		int h=getResources().getDimensionPixelSize(R.dimen.one_dip)*170;
		if("pet_icon".equals(imagePath)){
			eraserView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pet_icon),layout.getMeasuredWidth(),layout.getMeasuredHeight());
		}else{
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds=false;
			options.inSampleSize=StringUtil.getScaleByDPIget4(this,imagePath);
			options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			
			/*if(imagePath.contains("@")){
            	int a=imagePath.indexOf("@");
            	int b=imagePath.lastIndexOf("@");
            	int lenth=Integer.parseInt(imagePath.substring(a+1, b));
            	if(lenth>1024*100){
            		options.inSampleSize=4;
            	}else{
            		options.inSampleSize=StringUtil.getScaleByDPI(this);;
            	}
            }else{
        		options.inSampleSize=StringUtil.getScaleByDPI(this);;
        	}*/
			
			
			DisplayImageOptions displayImageOptions=new DisplayImageOptions
		            .Builder()
		            .showImageOnLoading(R.drawable.noimg)
			        .cacheInMemory(true)
			        .cacheOnDisc(true)
			        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
			        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//			        .decodingOptions(options)
	                .build();
			ImageLoader imageLoader=ImageLoader.getInstance();
			String path="";
			if(!animal.pet_iconUrl.equals(imagePath)){
				path=Constants.UPLOAD_IMAGE_THUBMAIL_IMAG+imagePath;
			}else{
				path=Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+imagePath;
			}
			
		imageLoader.loadImage(path+"@"+w+"w_"+h+"h_0l.jpg",displayImageOptions, new ImageLoadingListener() {
			
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
	private  void share(View view){
		ImageView weixinIV=(ImageView)view.findViewById(R.id.imageView3);
		ImageView friendIV=(ImageView)view.findViewById(R.id.imageView4);
		ImageView xinlangIV=(ImageView)view.findViewById(R.id.imageView5);
		weixinIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weixinShare(animal.pet_iconPath);
				
			}
		});
		friendIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				friendShare(animal.pet_iconPath);
				
			}
		});
		xinlangIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				xinlangShare();
				/*Bitmap bmp=ImageUtil.getImageFromView(shareBitmapLayout);
				String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".png";
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(path);
					bmp.compress(CompressFormat.PNG, 100, fos);
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					data.des="我在宠物星球里面摸了萌星"+animal.pet_nickName+"，“"+animal.pet_nickName+"”乖巧地冲我叫了一声，真可爱~http://home4pet.aidigame.com/(分享自@宠物星球社交应用）";
					
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
				}*/
				
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		if(eraserView!=null){
			eraserView.recyleBmp();
		}
		super.onDestroy();
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
	      private  void weixinShare(String path){
		   	   WeiXinShareContent weixinContent = new WeiXinShareContent();
		   	 //设置分享文字
			   	weixinContent.setShareContent("我摸了摸大萌星"+animal.pet_nickName+"，软软哒真可爱~~舍不得洗手了呢嘤嘤嘤");
			   	 //设置title
//			   	 weixinContent.setTitle("我是"+animal.pet_nickName+"，来自宠物星球的大萌星！");//
			   	weixinContent.setTitle("摸一摸，屏幕清晰~");
			   	 //设置分享内容跳转URL
			   	 weixinContent.setTargetUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=social/touch&aid="+animal.a_id+"&img_url="+url/*+"&SID="+PetApplication.SID*/);
		   	 //设置分享图片
		   	 UMImage umImage=new UMImage(this,path );
		   	 weixinContent.setShareImage(umImage);
		   	 mController.setShareMedia(weixinContent);
//		   	 mController.openShare(this, true);
		   	 mController.postShare(this,SHARE_MEDIA.WEIXIN, 
		   		        new SnsPostListener() {
		   		                @Override
		   		                public void onStart() {
//		   		                    Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
		   		                }
		   		                @Override
		   		                public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
		   		                     if (eCode == 200) {
		   		                         Toast.makeText(TouchActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		   		                     } else {
		   		                          String eMsg = "";
		   		                          if (eCode == -101){
		   		                              eMsg = "没有授权";
		   		                          }
		   		                          Toast.makeText(TouchActivity.this, "分享失败[" + eCode + "] " + 
		   		                                             eMsg,Toast.LENGTH_SHORT).show();
		   		                     }
		   		              }
		   		});
		   	   
		   		
		      }
	      private  void friendShare(String path){
		   	   CircleShareContent circleMedia = new CircleShareContent();
		   	   UMImage umImage=new UMImage(this, path);
		   	   circleMedia.setShareImage(umImage);
		   	 //设置分享文字
//			   	circleMedia.setShareContent("人家在宠物星球好开心，快来跟我一起玩嘛~");//
				circleMedia.setShareContent("摸一摸，屏幕清晰~");
				   	 //设置title
//			   	circleMedia.setTitle("我是"+animal.pet_nickName+"，来自宠物星球的大萌星！");//
			   	circleMedia.setTitle("我摸了摸大萌星"+animal.pet_nickName+"，软软哒真可爱~~舍不得洗手了呢嘤嘤嘤");
				   	 //设置分享内容跳转URL
			   	circleMedia.setTargetUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=social/touch&aid="+animal.a_id+"&img_url="+url/*+"&SID="+PetApplication.SID*/);
		   	   mController.setShareMedia(circleMedia);
		   	   mController.postShare(this,SHARE_MEDIA.WEIXIN_CIRCLE,
		   			   new SnsPostListener() {
		              @Override
		              public void onStart() {
//		                  Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
		              }
		              @Override
		              public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
		                   if (eCode == 200) {
		                    Toast.makeText(TouchActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		                   } else {
		                        String eMsg = "";
		                        if (eCode == -101){
		                            eMsg = "没有授权";
		                        }
		                        Toast.makeText(TouchActivity.this, "分享失败[" + eCode + "] " + 
		                                           eMsg,Toast.LENGTH_SHORT).show();
		                   }
		            }
		   });
		   		
		      }
	      private  void xinlangShare(){
		    	  UserImagesJson.Data data=new UserImagesJson.Data();
			   		data.path=animal.pet_iconPath;
			   			
			   			
			   	
				   	   SinaShareContent content=new SinaShareContent();
				   	   content.setShareContent(data.des);
				   	   UMImage umImage=new UMImage(this, data.path);
				   	   
				   	 content.setShareContent("我摸了摸大萌星"+animal.pet_nickName+"，软软哒真可爱~~舍不得洗手了呢嘤嘤嘤"+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/touch&aid="+animal.a_id+"&img_url="+url/*+"&SID="+PetApplication.SID*/+" #我是大萌星#");
				   	   content.setShareImage(umImage);
				   	   mController.setShareMedia(content);
				   	   mController.postShare(this, SHARE_MEDIA.SINA,new SnsPostListener() {
				   		
				   		@Override
				   		public void onStart() {
				   			// TODO Auto-generated method stub
				   			
				   		}
				   		
				   		@Override
				   		public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
				   			// TODO Auto-generated method stub
				   			if (eCode == 200) {
				                   Toast.makeText(TouchActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				                  } else {
				                       String eMsg = "";
				                       if (eCode == -101){
				                           eMsg = "没有授权";
				                       }
				                       Toast.makeText(TouchActivity.this, "分享失败[" + eCode + "] " + 
				                                          eMsg,Toast.LENGTH_SHORT).show();
				                  }
				   		}
				   	});
					 
					 
					 
				/*	 if(!UserStatusUtil.hasXinlangAuth(this)){
							return;
						}
							UserImagesJson.Data data=new UserImagesJson.Data();
							data.path=path;
							data.des="轻轻一点，免费赏粮！快把你每天的免费粮食赏给我家"+pp.animal.pet_nickName+"！#挣口粮# "+shareUrl+pp.img_id+"&to=webo"+"（分享自@宠物星球社交应用）";
							if(UserStatusUtil.hasXinlangAuth(this)){
								XinlangShare.sharePicture(data,this);
							}*/
				 }
		      @Override
				protected void onActivityResult(int requestCode, int resultCode, Intent data) {
					// TODO Auto-generated method stub
					super.onActivityResult(requestCode, resultCode, data);
					UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
			        if(ssoHandler != null){
			           ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			        }
				}
	      
}
