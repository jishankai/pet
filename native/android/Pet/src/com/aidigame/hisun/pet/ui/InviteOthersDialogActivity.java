package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.fragment.UserCenterFragment;
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
 * 邀请相关弹出窗
 * @author admin
 *
 */
public class InviteOthersDialogActivity extends Activity {
	Animal animal;
	RoundImageView petIcon;
	int mode;//1,邀请；2，输入邀请码；3，已经使用过邀请
	LinearLayout llayout1,llayout2,llayout3,llayout4,shareBitmapLayout;
	DisplayImageOptions displayImageOptions;
	Handler handler;
	UMSocialService mController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_dialog);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		animal=(Animal)getIntent().getSerializableExtra("animal");
		petIcon=(RoundImageView)findViewById(R.id.pet_icon);
		llayout1=(LinearLayout)findViewById(R.id.llayout1);
		llayout2=(LinearLayout)findViewById(R.id.llayout2);
		llayout3=(LinearLayout)findViewById(R.id.llayout3);
		llayout4=(LinearLayout)findViewById(R.id.llayout4);
		mode=getIntent().getIntExtra("mode", 1);
		
		
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
		ImageLoader imageLoader=ImageLoader.getInstance();
		findViewById(R.id.close_iv).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		if(mode!=2)
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl, petIcon, displayImageOptions,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				animal.pet_iconPath=StringUtil.compressEmotion(InviteOthersDialogActivity.this, null);
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				animal.pet_iconPath=StringUtil.compressEmotion(InviteOthersDialogActivity.this, loadedImage);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				animal.pet_iconPath=StringUtil.compressEmotion(InviteOthersDialogActivity.this, null);
			}
		});
		switch (mode) {
		case 1:
			llayout1.setVisibility(View.VISIBLE);
			initView1();
			break;
		case 2:
			if(PetApplication.myUser!=null&&PetApplication.myUser.inviter==0){
				llayout2.setVisibility(View.VISIBLE);
				initView2();
			}else{
				initView4();
			}
			
			break;

		default:
			break;
		}
		
	}

	private void initView1() {
		// TODO Auto-generated method stub
		TextView petNameTv=(TextView)findViewById(R.id.tv1);
		TextView numTv=(TextView)findViewById(R.id.invite_num_tv);
		petNameTv.setText("为萌星"+animal.pet_nickName+"助力");
		numTv.setText(animal.invite_code);
		shareBitmapLayout=(LinearLayout)findViewById(R.id.linearlayout);
		share();
	}
	private void initView2() {
		// TODO Auto-generated method stub
		final EditText inputEt=(EditText)findViewById(R.id.input);
		TextView sure=(TextView)findViewById(R.id.cancel2);
		petIcon.setVisibility(View.GONE);
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String numStr=inputEt.getText().toString();
				if(StringUtil.isEmpty(numStr)){
					Toast.makeText(InviteOthersDialogActivity.this, "邀请码不能为空",Toast.LENGTH_LONG ).show();
				}else{
					/*
					 * 使用邀请码
					 * 
					 * 成功 initView3()
					 * 失败 initView4()
					 */
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							final Animal a=HttpUtil.userInviteCode(handler, numStr, InviteOthersDialogActivity.this);
							runOnUiThread(new Runnable() {
								public void run() {
									if(a!=null){
										animal=a;
										PetApplication.myUser.coinCount+=300;
										if(UserCenterFragment.userCenterFragment!=null){
									    	UserCenterFragment.userCenterFragment.updatateInfo(true);;
										}
										initView3();
									}else{
										Toast.makeText(InviteOthersDialogActivity.this, "使用邀请码失败", Toast.LENGTH_LONG).show();
									}
								}
							});
						}
					}).start();
					
				}
			}
		});
	}
	private void initView3() {
		// TODO Auto-generated method stub
		TextView coinNumTv=(TextView)findViewById(R.id.coin_num_tv);
		TextView sure=(TextView)findViewById(R.id.cancel3);
		coinNumTv.setText(""+300);
		petIcon.setVisibility(View.VISIBLE);
		llayout2.setVisibility(View.GONE);
		llayout3.setVisibility(View.VISIBLE);
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl, petIcon, displayImageOptions);
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	private void initView4() {
		// TODO Auto-generated method stub
		llayout2.setVisibility(View.GONE);
		llayout4.setVisibility(View.VISIBLE);
		final TextView nameTv=(TextView)findViewById(R.id.user_name_tv);
		final TextView sure=(TextView)findViewById(R.id.cancel4);
		
		
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final MyUser u=HttpUtil.info(InviteOthersDialogActivity.this,handler, PetApplication.myUser.inviter);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(u!=null){
							nameTv.setText("您已经接受过"+u.u_nick+"的邀请啦~");
							ImageLoader imageLoader=ImageLoader.getInstance();
							imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+u.u_iconUrl, petIcon, displayImageOptions);
						}
					}
				});
			}
		}).start();
		
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	
	
	
	public void share(){
		ImageView weixinIV=(ImageView)findViewById(R.id.weixin_iv);
		ImageView friendIV=(ImageView)findViewById(R.id.friend_iv);
		ImageView xinlangIV=(ImageView)findViewById(R.id.xinlang_iv);
		weixinIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Bitmap bmp=ImageUtil.getImageFromView(shareBitmapLayout);
				String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".png";
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(path);
//					bmp.compress(CompressFormat.PNG, 100, fos);
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					weixinShare(data);
				
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
			
				Bitmap bmp=ImageUtil.getImageFromView(shareBitmapLayout);
				String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".png";
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(path);
//					bmp.compress(CompressFormat.PNG, 100, fos);
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					friendShare(data);
					
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
				
				Bitmap bmp=ImageUtil.getImageFromView(shareBitmapLayout);
				String path=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".png";
				FileOutputStream fos=null;
				try {
					fos = new FileOutputStream(path);
//					bmp.compress(CompressFormat.PNG, 100, fos);
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=path;
					data.des="我家萌星最闪亮！小伙伴们快来助力~~邀请码："+animal.invite_code+"，http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
					xinlangShare(data);
					
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
	      
	      public void weixinShare(UserImagesJson.Data data){
		   	   WeiXinShareContent weixinContent = new WeiXinShareContent();
		   	//设置分享文字
			   	 weixinContent.setShareContent("人家在宠物星球好开心，快来跟我一起玩嘛~");
			   	 //设置title
			   	 weixinContent.setTitle("我是"+animal.pet_nickName+"，来自宠物星球的大萌星！");
			   	 //设置分享内容跳转URL
			   	 weixinContent.setTargetUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=animal/infoShare&aid="+animal.a_id);
		   	 UMImage umImage=new UMImage(this,animal.pet_iconPath);
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
		   		                    	MobclickAgent.onEvent(InviteOthersDialogActivity.this, "invite_share");
		   		                         Toast.makeText(InviteOthersDialogActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		   		                     } else {
		   		                          String eMsg = "";
		   		                          if (eCode == -101){
		   		                              eMsg = "没有授权";
		   		                          }
		   		                          Toast.makeText(InviteOthersDialogActivity.this, "分享失败[" + eCode + "] " + 
		   		                                             eMsg,Toast.LENGTH_SHORT).show();
		   		                     }
		   		              }
		   		});
		   	   
		   		
		      }
		 public void friendShare(UserImagesJson.Data data){
			   CircleShareContent circleMedia = new CircleShareContent();
			   //设置分享文字
			   	circleMedia.setShareContent("人家在宠物星球好开心，快来跟我一起玩嘛~");
				   	 //设置title
			   	circleMedia.setTitle("我是"+animal.pet_nickName+"，来自宠物星球的大萌星！");
				   	 //设置分享内容跳转URL
			   	circleMedia.setTargetUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=animal/infoShare&aid="+animal.a_id);
			   UMImage umImage=new UMImage(this, animal.pet_iconPath);
			   circleMedia.setShareImage(umImage);
			  /* circleMedia.setShareContent("努力卖萌，只为给自己代粮！快把你每天的免费粮食赏给我~");
			   circleMedia.setTitle("轻轻一点，免费赏粮！我家"+pp.animal.pet_nickName+"的口粮就靠你啦~");
			   circleMedia.setTargetUrl(shareUrl+pp.img_id+"&to=wechat");*/
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
		                	MobclickAgent.onEvent(InviteOthersDialogActivity.this, "invite_share");
		                 Toast.makeText(InviteOthersDialogActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		                } else {
		                     String eMsg = "";
		                     if (eCode == -101){
		                         eMsg = "没有授权";
		                     }
		                     Toast.makeText(InviteOthersDialogActivity.this, "分享失败[" + eCode + "] " + 
		                                        eMsg,Toast.LENGTH_SHORT).show();
		                }
		         }
		});
			   
				
		   }
		 public void xinlangShare(UserImagesJson.Data data){
			
		   	   SinaShareContent content=new SinaShareContent();
		   	   content.setShareContent(data.des);
		   	   UMImage umImage=new UMImage(this, animal.pet_iconPath);
		   	 content.setShareContent("人家在宠物星球好开心，快来跟我一起玩嘛~"+"http://"+Constants.IP+Constants.URL_ROOT+"r=animal/infoShare&aid="+animal.a_id+"（分享自@宠物星球社交应用）");
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
		                   Toast.makeText(InviteOthersDialogActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		                   MobclickAgent.onEvent(InviteOthersDialogActivity.this, "invite_share");    
		   			} else {
		                       String eMsg = "";
		                       if (eCode == -101){
		                           eMsg = "没有授权";
		                       }
		                       Toast.makeText(InviteOthersDialogActivity.this, "分享失败[" + eCode + "] " + 
		                                          eMsg,Toast.LENGTH_SHORT).show();
		                  }
		   		}
		   	});
			 
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
