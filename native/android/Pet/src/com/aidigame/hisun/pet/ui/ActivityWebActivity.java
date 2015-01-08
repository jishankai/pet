package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Banner;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
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

public class ActivityWebActivity extends Activity {
	WebView webView;
//	String url="http://"+Constants.IP+Constants.URL_ROOT+"r=game/2048&sig=";
//	String shareUrl="http://"+Constants.IP+Constants.URL_ROOT+"r=game/dcz&aid=";
	Banner banner;
	ImageView back;
	TextView shareTv;
	UMSocialService mController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		MobclickAgent.onEvent(this, "play");
		setContentView(R.layout.activity_web);
		banner=(Banner)getIntent().getSerializableExtra("banner");
		webView=(WebView)findViewById(R.id.webview);
		back=(ImageView)findViewById(R.id.button1);
		shareTv=(TextView)findViewById(R.id.three_point_iv);
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
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isTaskRoot()){
					if(HomeActivity.homeActivity!=null){
						ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
						am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
					}else{
						Intent intent=new Intent(ActivityWebActivity.this,HomeActivity.class);
						ActivityWebActivity.this.startActivity(intent);
					}
				}
				
				finish();
			}
		});
		shareTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				share();
				initShareView();
			}

		});
//		LogUtil.i("me", "逗一逗url="+url);
		webView.loadUrl(banner.url+"&SID="+Constants.SID);
		
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				LogUtil.i("me", "捏虫子url="+url);
				return true;
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}
		});
		/**
		 * 必须得加
		 */
		WebSettings webSettings = webView.getSettings(); 
		webSettings.setJavaScriptEnabled(true); 
		
		
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=StringUtil.getScaleByDPI(this);;
		LogUtil.i("me", "图片像素压缩比例="+StringUtil.getScaleByDPI(this));
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		
		ImageFetcher imageFetcher=new ImageFetcher(this, Constants.screen_width);
		imageFetcher.itemUrl="banner/";
		imageFetcher.setImageCache(new ImageCache(this, new ImageCacheParams(banner.icon)));
		imageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
				
				@Override
				public void onComplete(Bitmap bitmap) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void getPath(String path) {
					// TODO Auto-generated method stub
					banner.icon_path=path;
				}
			});
		imageFetcher.loadImage(banner.icon, new ImageView(this), options);
		
		
	}

		/**
		 * 分享 按钮
		 */
		RelativeLayout shareLayout;
		private void initShareView() {
			// TODO Auto-generated method stub
			shareLayout=(RelativeLayout)findViewById(R.id.sharelayout);
			shareLayout.setVisibility(View.VISIBLE);
			LinearLayout weixinLayout=(LinearLayout)findViewById(R.id.imageView22);
			LinearLayout friendLayout=(LinearLayout)findViewById(R.id.imageView32);
			LinearLayout xinlangLayout=(LinearLayout)findViewById(R.id.imageView42);
			shareLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					shareLayout.setVisibility(View.GONE);
				}
			});
			weixinLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					shareLayout.setVisibility(View.GONE);
					weixinShare();
					
				}
			});
			friendLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					shareLayout.setVisibility(View.GONE);
					friendShare();
					
				}
			});
			xinlangLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					shareLayout.setVisibility(View.GONE);
					xinlangShare();
					
				}
			});
		}
		 public void weixinShare(){
			   WeiXinShareContent weixinContent = new WeiXinShareContent();
			 //设置分享文字
//			 weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，微信");
			 //设置title
//			 weixinContent.setTitle("友盟社会化分享组件-微信");
			 //设置分享内容跳转URL
//			 weixinContent.setTargetUrl("你的URL链接");
			 //设置分享图片
			 UMImage umImage=new UMImage(this,banner.icon_path );
			 weixinContent.setShareImage(umImage);
			 weixinContent.setShareContent(banner.description);
			 weixinContent.setTitle(banner.title);
			 weixinContent.setTargetUrl(banner.url);
			 mController.setShareMedia(weixinContent);
			
			 mController.postShare(this,SHARE_MEDIA.WEIXIN, 
				        new SnsPostListener() {
				                @Override
				                public void onStart() {
//				                    Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
				                }
				                @Override
				                public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
				                     if (eCode == 200) {
				                         Toast.makeText(ActivityWebActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				                     } else {
				                          String eMsg = "";
				                          if (eCode == -101){
				                              eMsg = "没有授权";
				                          }
				                          Toast.makeText(ActivityWebActivity.this, "分享失败[" + eCode + "] " + 
				                                             eMsg,Toast.LENGTH_SHORT).show();
				                     }
				              }
				});
			 shareLayout.setVisibility(View.INVISIBLE);
				
		   }
		   public void friendShare(){
			   CircleShareContent circleMedia = new CircleShareContent();
			   UMImage umImage=new UMImage(this, banner.icon_path);
			   circleMedia.setShareImage(umImage);
			   circleMedia.setShareContent(banner.description);
			   circleMedia.setTitle(banner.title);
			   circleMedia.setTargetUrl(banner.url);
//			   circleMedia.setTargetUrl("你的URL链接");
			   mController.setShareMedia(circleMedia);
			   mController.postShare(this,SHARE_MEDIA.WEIXIN_CIRCLE,
					   new SnsPostListener() {
		           @Override
		           public void onStart() {
//		               Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
		           }
		           @Override
		           public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
		                if (eCode == 200) {
		                 Toast.makeText(ActivityWebActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		                } else {
		                     String eMsg = "";
		                     if (eCode == -101){
		                         eMsg = "没有授权";
		                     }
		                     Toast.makeText(ActivityWebActivity.this, "分享失败[" + eCode + "] " + 
		                                        eMsg,Toast.LENGTH_SHORT).show();
		                }
		         }
		});
						shareLayout.setVisibility(View.INVISIBLE);
				
		   }
		   public void xinlangShare(){
			   
			  /* if(UserStatusUtil.hasXinlangAuth(this)){
					shareLayout.setVisibility(View.INVISIBLE);
					UserImagesJson.Data data=new UserImagesJson.Data();
					if(banner.icon_path!=null){
						data.path=banner.icon_path;
						data.des=banner.description+" "+banner.url+" "+"（分享自@宠物星球社交应用）";
						
					XinlangShare.sharePicture(data,ActivityWebActivity.this);
					XinlangShare.listener=new XinlangShare.ShareXinlangResultListener() {
						
						@Override
						public void resultOk() {
							// TODO Auto-generated method stub
							MobclickAgent.onEvent(ActivityWebActivity.this, "photo_share");
						}
					};
					}else{
						Toast.makeText(this, "失败", Toast.LENGTH_LONG).show();
					}
					
					
					
				}*/
			   
			   
			   shareLayout.setVisibility(View.INVISIBLE);
				UserImagesJson.Data data=new UserImagesJson.Data();
				if(banner.icon_path!=null){
					data.path=banner.icon_path;
					data.des=banner.description+" "+banner.url+" "+"（分享自@宠物星球社交应用）";
		   	   SinaShareContent content=new SinaShareContent();
		   	   content.setShareContent(data.des);
		   	   UMImage umImage=new UMImage(ActivityWebActivity.this, data.path);
		   	  
		   	   content.setShareImage(umImage);
		   	   mController.setShareMedia(content);
		   	   mController.postShare(ActivityWebActivity.this, SHARE_MEDIA.SINA,new SnsPostListener() {
		   		
		   		@Override
		   		public void onStart() {
		   			// TODO Auto-generated method stub
		   			
		   		}
		   		
		   		@Override
		   		public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
		   			// TODO Auto-generated method stub
		   			if (eCode == 200) {
		                   Toast.makeText(ActivityWebActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		                  } else {
		                       String eMsg = "";
		                       if (eCode == -101){
		                           eMsg = "没有授权";
		                       }
		                       Toast.makeText(ActivityWebActivity.this, "分享失败[" + eCode + "] " + 
		                                          eMsg,Toast.LENGTH_SHORT).show();
		                  }
		   		}
		   	});
			 
				}
				
				
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
