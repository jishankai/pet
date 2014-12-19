package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.XinlangShare;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageWorker;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 弹出框样式3
 * @author admin
 *
 */
public class Dialog6Activity extends Activity implements OnClickListener{
	ImageView closeIv;
	public static Dialog6ActivityListener listener;
	Handler handler;
	PetPicture pp;
	ImageView iv,weixinIv,friendIv,xinlangIv;
	TextView name,foodNum,timeTv;
	ImageFetcher mImageFetcher;
	UMSocialService mController;
	RelativeLayout parent;
	String shareUrl="http://kouliang.tuturead.com/index.php?r=social/foodShareApi&img_id=";
	String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_dialog6);
		pp=(PetPicture)getIntent().getSerializableExtra("picture");
		closeIv=(ImageView)findViewById(R.id.close);
		closeIv.setOnClickListener(this);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		 mImageFetcher = new ImageFetcher(this, Constants.screen_width);
		name=(TextView)findViewById(R.id.name_tv);
		foodNum=(TextView)findViewById(R.id.food_tv);
		timeTv=(TextView)findViewById(R.id.time_tv);
		iv=(ImageView)findViewById(R.id.beg_iv);
		weixinIv=(ImageView)findViewById(R.id.weixin);
		friendIv=(ImageView)findViewById(R.id.friend);
		xinlangIv=(ImageView)findViewById(R.id.xinlang);
		parent=(RelativeLayout)findViewById(R.id.parent);
		
       mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    // 添加微信平台
    	UMWXHandler wxHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
    		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		
		
		
		closeIv.setOnClickListener(this);
		friendIv.setOnClickListener(this);
		xinlangIv.setOnClickListener(this);
		weixinIv.setOnClickListener(this);
		displayImage();
		
	}
	@Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.close:
				if(listener!=null){
					listener.onClose();
				}
				finish();
				break;
			case R.id.weixin:
				weixinShare();
				
				break;

			case R.id.friend:
				friendShare();
				
				break;

			case R.id.xinlang:
				xinlangShare();
				break;
				
			}
			
		}
		public static void setDialog3ActivityListener(Dialog6ActivityListener listener){
			Dialog6Activity.listener=listener;
		}
		/**
		 * 展示图片
		 */
		private void displayImage() {
			// TODO Auto-generated method stub
			String html="<html>"
		             +"<body>"
					   +"快分享给小伙伴，一起为"
		                +"<font color=\"#fb6137\">"
		                +pp.animal.pet_nickName+""
		                +"</font>"
		                +"助力！ "
		             +"</body>"
		      + "</html>";;
			
			name.setText(Html.fromHtml(html));
			foodNum.setText(""+pp.animal.foodNum);
			
			
			final BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds=false;
			options.inSampleSize=StringUtil.topicImageGetScaleByDPI(this);
			LogUtil.i("me", "照片详情页面Topic图片缩放比例"+StringUtil.topicImageGetScaleByDPI(this));
			if(StringUtil.topicImageGetScaleByDPI(this)>=2){
				options.inPreferredConfig=Bitmap.Config.ARGB_4444;
			}else{
				options.inPreferredConfig=Bitmap.Config.ARGB_8888;
			}
			
			options.inPurgeable=true;
			options.inInputShareable=true;
			mImageFetcher.setWidth(iv.getMeasuredWidth());
			mImageFetcher.setImageCache(new ImageCache(this, new ImageCacheParams(pp.url)));
			mImageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
				
				@Override
				public void onComplete(Bitmap bitmap) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void getPath(String path) {
					// TODO Auto-generated method stub
					Dialog6Activity.this.path=path;
				}
			});
			mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/pp.url, iv,options);
			if(myTimerTask!=null){
			    myTimerTask.cancel();
			}
			myTimerTask=new MyTimerTask(pp.create_time*1000+24*3600-System.currentTimeMillis());
			Timer timer=new Timer();
			timer.schedule(myTimerTask, 0, 1000);
		
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
	    			long time=pp.create_time+24*3600-System.currentTimeMillis()/1000;
	    		/*	if(time<=0){
	    				timeTvs.get(current_position).setText("00:00:00");
	    				return;
	    			}*/
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
	    			timeTv.setText(""+hh+":"+mm+":"+ss);
	    		
	    		
	    	};
	    };
		public static interface Dialog6ActivityListener{
			void onClose();
			void onButtonOne();
			void onButtonTwo();
		}
		 public void weixinShare(){
		   	   WeiXinShareContent weixinContent = new WeiXinShareContent();
		   	 //设置分享文字
		   	 weixinContent.setShareContent("努力卖萌，只为给自己代粮！快把你每天的免费粮食赏给我~");
		   	 //设置title
		   	 weixinContent.setTitle("轻轻一点，免费赏粮！我家"+pp.animal.pet_nickName+"的口粮就靠你啦~");
		   	 //设置分享内容跳转URL
		   	 weixinContent.setTargetUrl(shareUrl+pp.img_id+"&to=wechat");
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
		   		                         Toast.makeText(Dialog6Activity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		   		                     } else {
		   		                          String eMsg = "";
		   		                          if (eCode == -101){
		   		                              eMsg = "没有授权";
		   		                          }
		   		                          Toast.makeText(Dialog6Activity.this, "分享失败[" + eCode + "] " + 
		   		                                             eMsg,Toast.LENGTH_SHORT).show();
		   		                     }
		   		              }
		   		});
		   	   
		   		
		      }
		 public void friendShare(){
			   CircleShareContent circleMedia = new CircleShareContent();
			   UMImage umImage=new UMImage(this, path);
			   circleMedia.setShareImage(umImage);
			   circleMedia.setShareContent("努力卖萌，只为给自己代粮！快把你每天的免费粮食赏给我~");
			   circleMedia.setTitle("轻轻一点，免费赏粮！我家"+pp.animal.pet_nickName+"的口粮就靠你啦~");
			   circleMedia.setTargetUrl(shareUrl+pp.img_id+"&to=wechat");
			   mController.setShareMedia(circleMedia);
			   mController.postShare(this,SHARE_MEDIA.WEIXIN_CIRCLE,
					   new SnsPostListener() {
//		           @Override
		           public void onStart() {
//		               Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
		           }
		           @Override
		           public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
		                if (eCode == 200) {
		                 Toast.makeText(Dialog6Activity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		                } else {
		                     String eMsg = "";
		                     if (eCode == -101){
		                         eMsg = "没有授权";
		                     }
		                     Toast.makeText(Dialog6Activity.this, "分享失败[" + eCode + "] " + 
		                                        eMsg,Toast.LENGTH_SHORT).show();
		                }
		         }
		});
			   
				
		   }
		 public void xinlangShare(){
			 if(!UserStatusUtil.hasXinlangAuth(this)){
					return;
				}
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					data.des="轻轻一点，免费赏粮！快把你每天的免费粮食赏给我家"+pp.animal.pet_nickName+"！#挣口粮# "+shareUrl+pp.img_id+"&to=webo"+"（分享自@宠物星球社交应用）";
					if(UserStatusUtil.hasXinlangAuth(this)){
						XinlangShare.sharePicture(data,this);
					}
		 }
		
}
