package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.util.Map;
import java.util.Set;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 用户未注册的，弹窗提示
 * @author admin
 *
 */
public class DialogGoRegisterActivity extends Activity implements OnClickListener{
	LinearLayout weixinLayout,accountLayout,loginLayout;
	LinearLayout progressLayout;
	ShowProgress showProgress;
	UMSocialService mController;
	Handler handler;
	ImageView closeIv;
	TextView registerTv;
	public static DialogGoRegisterActivity dialogGoRegisterActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_dialog_go_register);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		dialogGoRegisterActivity=this;
		weixinLayout=(LinearLayout)findViewById(R.id.weixin_layout);
		accountLayout=(LinearLayout)findViewById(R.id.account_layout);
		loginLayout=(LinearLayout)findViewById(R.id.register_layout);
		progressLayout=(LinearLayout)findViewById(R.id.progress_layout);
		closeIv=(ImageView)findViewById(R.id.close);
		registerTv=(TextView)findViewById(R.id.register_tv);
		
		
        mController = (UMSocialService) UMServiceFactory.getUMSocialService("com.umeng.login");
		
		UMWXHandler wxHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxHandler.setRefreshTokenAvailable(false);
		wxHandler.addToSocialSDK();
		SinaSsoHandler handler=new SinaSsoHandler(this);
		handler.addToSocialSDK();
		MobclickAgent.onEvent(DialogGoRegisterActivity.this, "register_hint");
		weixinLayout.setOnClickListener(this);
		accountLayout.setOnClickListener(this);
		loginLayout.setOnClickListener(this);
		closeIv.setOnClickListener(this);
		registerTv.setOnClickListener(this);
		
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
	    if(showProgress!=null)showProgress.progressCancel();
	   }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.close:
				dialogGoRegisterActivity=null;
				finish();
				break;
			case R.id.register_tv:
				Intent intent1=new Intent(this,ChoseAcountTypeActivity.class);
				this.startActivity(intent1);
				finish();
				break;
			case R.id.weixin_layout:
				if(showProgress!=null){
					showProgress.showProgress();
				}else{
					showProgress=new ShowProgress(this, progressLayout);
				}
				weixinLogin();
				break;
			case R.id.account_layout:
				if(showProgress!=null){
					showProgress.showProgress();
				}else{
					showProgress=new ShowProgress(this, progressLayout);
				}
				xinlangLogin();
				break;

			case R.id.register_layout:
				Intent intent=new Intent(this,RegisterNoteDialog.class);
				this.startActivity(intent);
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(this)){
					PetApplication.petApp.activityList.remove(this);
				}
				finish();
				System.gc();
				break;

			default:
				break;
			}
		}
		/**
		 * 微信授权登录
		 */
		public void weixinLogin(){
			mController.doOauthVerify(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
			    @Override
			    public void onStart(SHARE_MEDIA platform) {
			        Toast.makeText(DialogGoRegisterActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
			    }
			    @Override
			    public void onError(SocializeException e, SHARE_MEDIA platform) {
			        Toast.makeText(DialogGoRegisterActivity.this, "授权错误", Toast.LENGTH_SHORT).show();
			        if(showProgress!=null)showProgress.progressCancel();
			    }
			    @Override
			    public void onComplete(Bundle value, SHARE_MEDIA platform) {
			        Toast.makeText(DialogGoRegisterActivity.this, "授权完成", Toast.LENGTH_SHORT).show();
			        
			        getWeixinInfo();
			    }
			    @Override
			    public void onCancel(SHARE_MEDIA platform) {
			    	 if(showProgress!=null)showProgress.progressCancel();
			        Toast.makeText(DialogGoRegisterActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			    }
			} );
		}
		public void getWeixinInfo(){
			 //获取相关授权信息
			if(showProgress!=null)showProgress.showProgress();;
	        mController.getPlatformInfo(DialogGoRegisterActivity.this, SHARE_MEDIA.WEIXIN, new UMDataListener() {
	    @Override
	    public void onStart() {
	        Toast.makeText(DialogGoRegisterActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
	    }                                              
	    @Override
	        public void onComplete(final int status, final Map<String, Object> info) {
	    	/*
	    	 * 12-08 15:32:28.550: D/TestData(17146): sex=1

	12-08 15:32:28.550: D/TestData(17146): nickname=祥

	12-08 15:32:28.550: D/TestData(17146): unionid=ooe9XuJBHjz90GNnv1aaUqRZDRJk

	12-08 15:32:28.550: D/TestData(17146): province=Beijing

	12-08 15:32:28.550: D/TestData(17146): openid=oo1jksxuyAFH-BGeUH_vhSP-TdFQ

	12-08 15:32:28.550: D/TestData(17146): language=zh_CN

	12-08 15:32:28.550: D/TestData(17146): headimgurl=http://wx.qlogo.cn/mmopen/LPLYlyQ5GAoM88JEWfkpldwnQeGod3Og2jE9QxIz76GPQIP0JD7fWdeFaCBEKu2o6bia7oTAxgoLHUia9QsDK8saichJRLABD6z/0

	12-08 15:32:28.550: D/TestData(17146): country=CN

	                      city=Haidian
	    	 */
	    	new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					  if(status == 200 && info != null){
			                StringBuilder sb = new StringBuilder();
			                Set<String> keys = info.keySet();
			               
			                MyUser user=new MyUser();
			                if("1".equals(""+(Integer)info.get("sex"))){
			                	user.u_gender=1;
			                }else{
			                	user.u_gender=2;
			                }
			                user.u_nick=(String)info.get("nickname");
			                user.weixin_id=(String)info.get("openid");
			                user.u_iconPath=(String)info.get("headimgurl");
			                user.wechat_union=(String)info.get("unionid");
			              
			                
			                
			               
			                	if(!StringUtil.isEmpty(user.u_iconPath)){
				                	boolean flag=HttpUtil.downloadImage(user.u_iconPath, Constants.Picture_ICON_Path+File.separator+"weixin_"+user.weixin_id+".png");
									if(flag){
										user.u_iconPath=Constants.Picture_ICON_Path+File.separator+"weixin_"+user.weixin_id+".png";
									}
				                }
				                bindLogin(user,true);
			                
			                for(String key : keys){
			                   sb.append(key+"="+info.get(key).toString()+"\r\n");
			                  
			                }
			                Log.d("TestData",sb.toString());
			            }else{
			               Log.d("TestData","发生错误："+status);
			           }
				}
			}).start();
	          
	        }
	});
		}
		/**
		 * 新浪授权登陆
		 */
		public void xinlangLogin(){
			LogUtil.i("exception", "新浪微博授权：");
//			XinlangShare.xinlangAuth(this, true);
			mController.doOauthVerify(this,SHARE_MEDIA.SINA , new UMAuthListener() {
				
				@Override
				public void onStart(SHARE_MEDIA arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(DialogGoRegisterActivity.this, "授权错误", Toast.LENGTH_SHORT).show();
			        if(showProgress!=null)showProgress.progressCancel();
				}
				
				@Override
				public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
					// TODO Auto-generated method stub
					if(showProgress!=null)showProgress.showProgress();;
					mController.getPlatformInfo(DialogGoRegisterActivity.this, SHARE_MEDIA.SINA, new UMDataListener() {
					    @Override
					    public void onStart() {
//					        Toast.makeText(RegisterNoteDialog.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
					    }                                              
					    @Override
					        public void onComplete(final int status, final Map<String, Object> info) {
					    	/*
					    	 * D/TestData(27729): uid=3835971321

	12-31 13:55:45.400: D/TestData(27729): favourites_count=0

	12-31 13:55:45.400: D/TestData(27729): location=北京 石景山区

	12-31 13:55:45.400: D/TestData(27729): description=

	12-31 13:55:45.400: D/TestData(27729): verified=false

	12-31 13:55:45.400: D/TestData(27729): friends_count=18

	12-31 13:55:45.400: D/TestData(27729): gender=1

	12-31 13:55:45.400: D/TestData(27729): screen_name=shicx2014

	12-31 13:55:45.400: D/TestData(27729): statuses_count=153

	12-31 13:55:45.400: D/TestData(27729): followers_count=2

	12-31 13:55:45.400: D/TestData(27729): profile_image_url=http://tp2.sinaimg.cn/3835971321/180/0/1

	12-31 13:55:45.400: D/TestData(27729): access_token=2.00L42bLESzSe4E097cb335b7umIATE
					    	 */
					    	new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(status == 200 && info != null){
						                StringBuilder sb = new StringBuilder();
						                Set<String> keys = info.keySet();
						                
						                MyUser user=new MyUser();
						                if("1".equals(""+(Integer)info.get("gender"))){
						                	user.u_gender=1;
						                }else{
						                	user.u_gender=2;
						                }
						                user.u_nick=(String)info.get("screen_name");
						                user.xinlang_id=""+info.get("uid");
						                user.u_iconPath=(String)info.get("profile_image_url");
						                
						              
						                
						                
						               
						                	if(!StringUtil.isEmpty(user.u_iconPath)){
							                	boolean flag=HttpUtil.downloadImage(user.u_iconPath, Constants.Picture_ICON_Path+File.separator+"xinlang_"+user.xinlang_id+".png");
												if(flag){
													user.u_iconPath=Constants.Picture_ICON_Path+File.separator+"xinlang_"+user.xinlang_id+".png";
												}
							                }
							                bindLogin(user,false);
						                
						                
						                for(String key : keys){
						                   sb.append(key+"="+info.get(key).toString()+"\r\n");
						                }
						                Log.d("TestData",sb.toString());
						            }else{
						               Log.d("TestData","发生错误："+status);
						           }
								}
							}).start();
					            
					        }
					});
				}
				
				@Override
				public void onCancel(SHARE_MEDIA arg0) {
					// TODO Auto-generated method stub
					 if(showProgress!=null)showProgress.progressCancel();
				        Toast.makeText(DialogGoRegisterActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
				}
			});
		}
		@Override 
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    super.onActivityResult(requestCode, resultCode, data);
		    /**使用SSO授权必须添加如下代码 */
		    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
		    if(ssoHandler != null){
		       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		    }
		}
		/**
		 * 微信或新浪绑定登陆
		 * @param user
		 */
		public void bindLogin(final MyUser user,boolean isWeixin){
//			 if(showProgress!=null)showProgress.progressCancel();

			boolean flag=false;
			if(isWeixin){
				flag= HttpUtil.isBind(handler, user.weixin_id, isWeixin, DialogGoRegisterActivity.this,user.wechat_union);
			}else{
				flag= HttpUtil.isBind(handler, user.xinlang_id, isWeixin, DialogGoRegisterActivity.this,null);
			}
			 final boolean isBinded=flag;
			
			 runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(isBinded){
							getSIDAndUserID();
						}else{
							if(ChoseAcountTypeActivity.choseAcountTypeActivity!=null){
								ChoseAcountTypeActivity.choseAcountTypeActivity.finish();
								ChoseAcountTypeActivity.choseAcountTypeActivity=null;
							}
							Intent intent=new Intent(DialogGoRegisterActivity.this,ChoseAcountTypeActivity.class);
							intent.putExtra("user", user);
							intent.putExtra("isBind", true);
							LogUtil.i("mi","bindLogin::::"+ "user==null?"+(user==null));
							DialogGoRegisterActivity.this.startActivity(intent);
							
							if(showProgress!=null){
								showProgress.progressCancel();
							}
						}
						finish();
						
					}
				});
			
		}
		public void getSIDAndUserID(){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String SID=HttpUtil.getSID(DialogGoRegisterActivity.this,handler);
					SharedPreferences sPreferences=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
					if(!StringUtil.isEmpty(SID)){
						PetApplication.SID=SID;
					}else{
						boolean flag=HttpUtil.login(DialogGoRegisterActivity.this,handler);
						if(flag){
							SID=HttpUtil.getSID(DialogGoRegisterActivity.this,handler);
						}
					}
					
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(HomeActivity.homeActivity!=null){
								if(HomeActivity.homeActivity.userCenterFragment!=null){
									HomeActivity.homeActivity.userCenterFragment.updatateInfo(true);;
								}
								if(HomeActivity.homeActivity.myPetFragment!=null){
									HomeActivity.homeActivity.myPetFragment.homeMyPet.refresh();
								}
								com.aidigame.hisun.pet.PetApplication.logout(null);
								HomeActivity.homeActivity.initEMChatLogin();
								ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
								am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
							}else{
								Intent intent=new Intent(DialogGoRegisterActivity.this,HomeActivity.class);
								DialogGoRegisterActivity.this.startActivity(intent);
								
							}
							finish();
						}
					});
					
				}
			}).start();
			
			//TODO 版本更新
			
			
			
			
		}
		public void showProgress(){
			if(showProgress!=null)showProgress.showProgress();;
		}
		public void hideProgress(){
			if(showProgress!=null)showProgress.progressCancel();;
		}

}
