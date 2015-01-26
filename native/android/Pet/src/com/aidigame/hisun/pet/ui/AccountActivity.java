package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.huanxin.SettingsActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.fragment.DialogNote;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class AccountActivity extends Activity implements OnClickListener{
	LinearLayout setPssLayout,exchangeAccountLayout,bindWeixin,bindXinlang;
	RelativeLayout shareWeixinLayout,shareXinlangLayout;
	public static AccountActivity accountActivity;
	UMSocialService mController;
	ImageView back,bindWeixinIV,bindXinlangIV,shareWeixinIV,shareXinlangIV;
	LinearLayout progressLayout,linearLayout2,linearlayout12,linearlayout13,messageLayout;
	View lineInvite;
	ShowProgress showProgress;
	int acount=-1;//1同步发送到新浪微博，2 绑定新浪微博；-1 Activity启动时进入onResume方法，啥也不做。
	Handler handler;
	TextView weixinTv,xinlangTv;
	public View popupParent;
	public RelativeLayout black_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_account);
		accountActivity=this;
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		mController = (UMSocialService) UMServiceFactory.getUMSocialService("com.umeng.login");
		SinaSsoHandler sinaSsoHandler=new SinaSsoHandler(this);
		UMWXHandler wxHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxHandler.addToSocialSDK();
		sinaSsoHandler.addToSocialSDK();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		setPssLayout=(LinearLayout)findViewById(R.id.set_pass_layout);
		exchangeAccountLayout=(LinearLayout)findViewById(R.id.change_account);
		bindWeixin=(LinearLayout)findViewById(R.id.bind_layout_weixin);
		bindXinlang=(LinearLayout)findViewById(R.id.bind_layout_xinlang);
		shareWeixinLayout=(RelativeLayout)findViewById(R.id.share_weixin);
		shareXinlangLayout=(RelativeLayout)findViewById(R.id.share_xinlang);
		back=(ImageView)findViewById(R.id.back);
		progressLayout=(LinearLayout)findViewById(R.id.progress_layout);
		
		messageLayout=(LinearLayout)findViewById(R.id.message_layout);
		
		
		bindWeixinIV=(ImageView)findViewById(R.id.setup_imageview1);
		bindXinlangIV=(ImageView)findViewById(R.id.setup_imageview2);
		shareWeixinIV=(ImageView)findViewById(R.id.setup_imageview3);
		shareXinlangIV=(ImageView)findViewById(R.id.setup_imageview4);
		
		
		
		setPssLayout.setOnClickListener(this);
		exchangeAccountLayout.setOnClickListener(this);
		bindWeixin.setOnClickListener(this);
		bindXinlang.setOnClickListener(this);
		shareWeixinLayout.setOnClickListener(this);
		shareXinlangLayout.setOnClickListener(this);
		messageLayout.setOnClickListener(this);
		back.setOnClickListener(this);
		weixinTv=(TextView)findViewById(R.id.weixin_bind_tv);
		xinlangTv=(TextView)findViewById(R.id.xinlang_bind_tv);
		
		
		linearLayout2=(LinearLayout)findViewById(R.id.linearlayout2);
		linearlayout12=(LinearLayout)findViewById(R.id.linearlayout12);
		lineInvite=findViewById(R.id.lineInvite);
		linearlayout13=(LinearLayout)findViewById(R.id.linearlayout13);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		if(!StringUtil.isEmpty(Constants.CON_VERSION)&&"1.0".equals(Constants.CON_VERSION))
		{
			linearlayout12.setVisibility(View.GONE);
			lineInvite.setVisibility(View.GONE);
		}else{
			linearlayout12.setVisibility(View.VISIBLE);
			lineInvite.setVisibility(View.VISIBLE);
		}
		
		linearlayout12.setOnClickListener(this);
		linearLayout2.setOnClickListener(this);
		linearlayout13.setOnClickListener(this);
		
		SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		if(sp.getBoolean(Constants.LOCK_TO_XINLANG, false)){
			bindXinlangIV.setVisibility(View.GONE);;
			xinlangTv.setVisibility(View.VISIBLE);
			xinlangTv.setText("已绑定");
			
			
		}else{
//			bindXinlangIV.setImageResource(R.drawable.unchecked);;
		}
		if(sp.getBoolean(Constants.PICTURE_SEND_TO_XINLANG, false)){
			shareXinlangIV.setImageResource(R.drawable.checked);;
			
		}else{
//			shareXinlangIV.setImageResource(R.drawable.unchecked);;
		}
		if(sp.getBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false)){
			shareWeixinIV.setImageResource(R.drawable.checked);;
		}else{
			shareWeixinIV.setImageResource(R.drawable.unchecked);;
		}
		if(sp.getBoolean(Constants.LOCK_TO_WEIXIN, false)){
//			bindWeixinIV.setImageResource(R.drawable.checked);
			bindWeixinIV.setVisibility(View.GONE);
			weixinTv.setVisibility(View.VISIBLE);
			weixinTv.setText("已绑定");
		}else{
//			bindWeixinIV.setImageResource(R.drawable.unchecked);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		Editor editor=sp.edit();
		switch (v.getId()) {
		case R.id.linearlayout2:
			Intent intent22=new Intent(this,ReceiverAddressActivity.class);
			this.startActivity(intent22);
			break;
			case R.id.linearlayout12:
				if(Constants.user!=null&&Constants.user.inviter!=0){
					Intent intent13=new Intent(this,InviteOthersDialogActivity.class);
					intent13.putExtra("mode", 2);
					this.startActivity(intent13);
				}else
				if(Constants.user!=null&&Constants.user.aniList!=null){
					if(/*Constants.user.aniList.size()>=10*/false){
						DialogNote dialog=new DialogNote(popupParent, this,black_layout, 3);
					}else{
						Intent intent13=new Intent(this,InviteOthersDialogActivity.class);
						intent13.putExtra("mode", 2);
						this.startActivity(intent13);
					}
				}

				break;
			case R.id.linearlayout13:
				Intent intent14=new Intent(this,UsersListActivity.class);
				intent14.putExtra("mode", 1);
				this.startActivity(intent14);
				break;
		case R.id.set_pass_layout:
			if(SetPassActivity.setPassActivity!=null){
				SetPassActivity.setPassActivity.finish();
				SetPassActivity.setPassActivity=null;
			}
			Intent intent1=new Intent(this,SetPassActivity.class);
			
			this.startActivity(intent1);
			break;

		case R.id.change_account:
			if(true/*Constants.user!=null&&StringUtil.isEmpty(Constants.user.password)*/){
				Dialog3Activity.listener=new Dialog3Activity.Dialog3ActivityListener() {
					
					@Override
					public void onClose() {
						// TODO Auto-generated method stub
//						finish();
					}
					
					@Override
					public void onButtonTwo() {
						// TODO Auto-generated method stub
						Intent intent2=new Intent(AccountActivity.this,RegisterNoteDialog.class);
						intent2.putExtra("mode", 2);
						AccountActivity.this.startActivity(intent2);
					}
					
					@Override
					public void onButtonOne() {
						// TODO Auto-generated method stub
						Intent intent=new Intent(AccountActivity.this,SetPassActivity.class);
						AccountActivity.this.startActivity(intent);
					}
				};
				Intent intent2=new Intent(this,Dialog3Activity.class);
				intent2.putExtra("mode", 1);
				this.startActivity(intent2);
				return;
			}
			if(RegisterNoteDialog.registerNoteDialog!=null){
				RegisterNoteDialog.registerNoteDialog.finish();
				RegisterNoteDialog.registerNoteDialog=null;
			}
			Intent intent2=new Intent(this,RegisterNoteDialog.class);
			intent2.putExtra("mode", 2);
			this.startActivity(intent2);
			break;

		case R.id.bind_layout_weixin:
			
			if(!StringUtil.isEmpty(Constants.user.weixin_id)){
	    		return;
	    	}
           
			
			
			
			 if(!sp.getBoolean(Constants.LOCK_TO_WEIXIN, false)){
				 if(showProgress==null){
						showProgress=new ShowProgress(this, progressLayout);
					}else{
						showProgress.progressCancel();
					}
				
					/*if(Constants.api==null){
						boolean flag=WeixinShare.regToWeiXin(this);
						if(!flag){
							Toast.makeText(this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
							bindWeixinIV.setImageResource(R.drawable.unchecked);
							editor.putBoolean(Constants.LOCK_TO_WEIXIN, false);
							return;
						}else{
							editor.putBoolean(Constants.LOCK_TO_WEIXIN, true);
							bindWeixinIV.setImageResource(R.drawable.checked);
						}
					}else{
						editor.putBoolean(Constants.LOCK_TO_WEIXIN, true);
						bindWeixinIV.setImageResource(R.drawable.checked);
					}*/
				}else{
					/*editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
					Constants.api=null;
					bindWeixinIV.setImageResource(R.drawable.unchecked);*/
					weixinTv.setVisibility(View.VISIBLE);
					bindWeixinIV.setVisibility(View.GONE);
					weixinTv.setText("已绑定");
				}
			break;

		case R.id.bind_layout_xinlang:
			 if(!StringUtil.isEmpty(Constants.user.xinlang_id)){
	            	return;
		    	}
			if(!sp.getBoolean(Constants.LOCK_TO_XINLANG, false)){
				if(showProgress==null){
					showProgress=new ShowProgress(this, progressLayout);
				}else{
					showProgress.progressCancel();
				}
				
				/*acount=2;
				if(UserStatusUtil.hasXinlangAuth(this)){
					editor.putBoolean(Constants.LOCK_TO_XINLANG, true);
					bindXinlangIV.setImageResource(R.drawable.checked);
				}else{
					bindXinlangIV.setImageResource(R.drawable.unchecked);
					editor.putBoolean(Constants.LOCK_TO_XINLANG, false);
				}*/
			}else{
				/*acount=-1;
				bindXinlangIV.setImageResource(R.drawable.unchecked);
				editor.putBoolean(Constants.LOCK_TO_XINLANG, false);
//				editor.putString("xinlangToken", null);
				editor.remove("xinlangToken");
				Constants.accessToken=null;*/
				xinlangTv.setVisibility(View.VISIBLE);
				bindXinlangIV.setVisibility(View.GONE);
				xinlangTv.setText("已绑定");
			}
			break;

		case R.id.share_weixin:
           if(!sp.getBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false)){
				
				/*if(Constants.api==null){
					boolean flag=WeixinShare.regToWeiXin(this);
					if(!flag){
						Toast.makeText(this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						shareWeixinIV.setImageResource(R.drawable.unchecked);
						editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
						return;
					}else{
						editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, true);
						shareWeixinIV.setImageResource(R.drawable.checked);
					}
				}else{
					editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, true);
					shareWeixinIV.setImageResource(R.drawable.checked);
				}*/
        	   mController.doOauthVerify(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
       		    @Override
       		    public void onStart(SHARE_MEDIA platform) {
       		        Toast.makeText(AccountActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
       		    }
       		    @Override
       		    public void onError(SocializeException e, SHARE_MEDIA platform) {
       		        Toast.makeText(AccountActivity.this, "授权错误", Toast.LENGTH_SHORT).show();
       		     SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
        			Editor editor=sp.edit();
       		     shareWeixinIV.setImageResource(R.drawable.unchecked);
					editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
					editor.commit();
       		        if(showProgress!=null)showProgress.progressCancel();
       		    }
       		    @Override
       		    public void onComplete(Bundle value, SHARE_MEDIA platform) {
       		        Toast.makeText(AccountActivity.this, "授权完成", Toast.LENGTH_SHORT).show();
       		     SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
       			Editor editor=sp.edit();
       		     editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, true);
					shareWeixinIV.setImageResource(R.drawable.checked);
					editor.commit();
//       		        getWeixinInfo();
       		    }
       		    @Override
       		    public void onCancel(SHARE_MEDIA platform) {
       		    	 if(showProgress!=null)showProgress.progressCancel();
       		        Toast.makeText(AccountActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
       		     shareWeixinIV.setImageResource(R.drawable.unchecked);
       		  SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
     			Editor editor=sp.edit();
					editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
					editor.commit();
       		    }
       		} );
			}else{
				editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
				shareWeixinIV.setImageResource(R.drawable.unchecked);
			}
			break;

		case R.id.share_xinlang:
			acount=1;
			if(!sp.getBoolean(Constants.PICTURE_SEND_TO_XINLANG, false)){
				 mController.doOauthVerify(this, SHARE_MEDIA.SINA, new UMAuthListener() {
		       		    @Override
		       		    public void onStart(SHARE_MEDIA platform) {
		       		        Toast.makeText(AccountActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
		       		    }
		       		    @Override
		       		    public void onError(SocializeException e, SHARE_MEDIA platform) {
		       		        Toast.makeText(AccountActivity.this, "授权错误", Toast.LENGTH_SHORT).show();
		       		     SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		        			Editor editor=sp.edit();
		        			shareXinlangIV.setImageResource(R.drawable.unchecked);
							editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, false);
							editor.commit();
		       		        if(showProgress!=null)showProgress.progressCancel();
		       		    }
		       		    @Override
		       		    public void onComplete(Bundle value, SHARE_MEDIA platform) {
		       		        Toast.makeText(AccountActivity.this, "授权完成", Toast.LENGTH_SHORT).show();
		       		     SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		       			Editor editor=sp.edit();
		       		     editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, true);
		       		  shareXinlangIV.setImageResource(R.drawable.checked);
							editor.commit();
//		       		        getWeixinInfo();
		       		    }
		       		    @Override
		       		    public void onCancel(SHARE_MEDIA platform) {
		       		    	 if(showProgress!=null)showProgress.progressCancel();
		       		        Toast.makeText(AccountActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
		       		     shareXinlangIV.setImageResource(R.drawable.unchecked);
		       		  SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		     			Editor editor=sp.edit();
							editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, false);
							editor.commit();
		       		    }
		       		} );
				
			}else{
				editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, false);
				shareXinlangIV.setImageResource(R.drawable.unchecked);
			}
			break;
		case R.id.back:
			if(isTaskRoot()){
				if(HomeActivity.homeActivity!=null){
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
				}else{
					Intent intent=new Intent(this,HomeActivity.class);
					this.startActivity(intent);
				}
			}
			accountActivity=null;
			
			if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(this)){
				PetApplication.petApp.activityList.remove(this);
			}
			finish();
			System.gc();
			break;
		case R.id.message_layout:
			Intent intent=new Intent(this,SettingsActivity.class);
			startActivity(intent);
			
			break;
		}
		editor.commit();
	}
	public void getXinlangToken(boolean getXinlangAuth){
		LogUtil.i("exception", "onResume启动");
		SharedPreferences sp=this.getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		Editor editor=sp.edit();
		if(getXinlangAuth){
			if(acount==2){
//				bindXinlangIV.setImageResource(R.drawable.checked);
				acount=-1;
				editor.putBoolean(Constants.LOCK_TO_XINLANG,true);
			}else if(acount==1){
				shareXinlangIV.setImageResource(R.drawable.checked);
				editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG,true);
			}
			
			
		}else{
			if(acount!=-1){
				shareXinlangIV.setImageResource(R.drawable.unchecked);
//				bindXinlangIV.setImageResource(R.drawable.unchecked);
				editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG,false);
				editor.putBoolean(Constants.LOCK_TO_XINLANG, false);
				acount=-1;
			}
			
		}
		editor.commit();
	};
	
	
	
	
	
	
	public void showProgress(){
		if(showProgress!=null)showProgress.showProgress();;
	}
	public void hideProgress(){
		if(showProgress!=null)showProgress.progressCancel();;
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
	    if(Constants.user!=null){
	    	if(!StringUtil.isEmpty(Constants.user.weixin_id)){
	    		bindXinlangIV.setVisibility(View.GONE);;
				weixinTv.setVisibility(View.VISIBLE);
				weixinTv.setText("已绑定");
	    	}
            if(!StringUtil.isEmpty(Constants.user.xinlang_id)){
            	bindXinlangIV.setVisibility(View.GONE);;
				xinlangTv.setVisibility(View.VISIBLE);
				xinlangTv.setText("已绑定");
	    	}
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
}
