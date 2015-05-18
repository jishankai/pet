package com.aidigame.hisun.imengstar.widget.fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.huanxin.SettingsActivity;
import com.aidigame.hisun.imengstar.ui.Dialog3Activity;
import com.aidigame.hisun.imengstar.ui.HomeActivity;
import com.aidigame.hisun.imengstar.ui.InviteOthersDialogActivity;
import com.aidigame.hisun.imengstar.ui.ReceiverAddressActivity;
import com.aidigame.hisun.imengstar.ui.RegisterNoteDialog;
import com.aidigame.hisun.imengstar.ui.SetPassActivity;
import com.aidigame.hisun.imengstar.ui.UsersListActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.widget.ShowProgress;
import com.aidigame.hisun.imengstar.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class SetupFragment extends Fragment implements OnClickListener{
	private LinearLayout setPssLayout,exchangeAccountLayout,bindWeixin,bindXinlang;
	private RelativeLayout shareWeixinLayout,shareXinlangLayout;
	public static SetupFragment accountActivity;
	private UMSocialService mController;
	private ImageView back,bindWeixinIV,bindXinlangIV,shareWeixinIV,shareXinlangIV;
	private LinearLayout progressLayout,linearLayout2,linearlayout12,linearlayout13,messageLayout;
	private View lineInvite,lineAddress;
	private ShowProgress showProgress;
	private int acount=-1;//1同步发送到新浪微博，2 绑定新浪微博；-1 Activity启动时进入onResume方法，啥也不做。
	private Handler handler;
	private TextView weixinTv,xinlangTv;
	private View popupParent,view;
	private RelativeLayout black_layout,titleLayout,rootLayout;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.fragment_account, null);
		handler=HandleHttpConnectionException.getInstance().getHandler(getActivity());
		mController = (UMSocialService) UMServiceFactory.getUMSocialService("com.umeng.login");
		SinaSsoHandler sinaSsoHandler=new SinaSsoHandler(getActivity());
		UMWXHandler wxHandler = new UMWXHandler(getActivity(),Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxHandler.addToSocialSDK();
		sinaSsoHandler.addToSocialSDK();
		initView();
		return view;
	}
	private void initView() {
		// TODO Auto-generated method stub
		setPssLayout=(LinearLayout)view.findViewById(R.id.set_pass_layout);
		exchangeAccountLayout=(LinearLayout)view.findViewById(R.id.change_account);
		bindWeixin=(LinearLayout)view.findViewById(R.id.bind_layout_weixin);
		bindXinlang=(LinearLayout)view.findViewById(R.id.bind_layout_xinlang);
		shareWeixinLayout=(RelativeLayout)view.findViewById(R.id.share_weixin);
		shareXinlangLayout=(RelativeLayout)view.findViewById(R.id.share_xinlang);
		back=(ImageView)view.findViewById(R.id.back);
		progressLayout=(LinearLayout)view.findViewById(R.id.progress_layout);
		
		messageLayout=(LinearLayout)view.findViewById(R.id.message_layout);
		
		rootLayout=(RelativeLayout)view.findViewById(R.id.root_layout);
		rootLayout.setBackgroundDrawable(null);
		titleLayout=(RelativeLayout)view.findViewById(R.id.title_layout);
		titleLayout.setVisibility(View.GONE);
		
		
		
		bindWeixinIV=(ImageView)view.findViewById(R.id.setup_imageview1);
		bindXinlangIV=(ImageView)view.findViewById(R.id.setup_imageview2);
		shareWeixinIV=(ImageView)view.findViewById(R.id.setup_imageview3);
		shareXinlangIV=(ImageView)view.findViewById(R.id.setup_imageview4);
		
		
		
		setPssLayout.setOnClickListener(this);
		exchangeAccountLayout.setOnClickListener(this);
		bindWeixin.setOnClickListener(this);
		bindXinlang.setOnClickListener(this);
		shareWeixinLayout.setOnClickListener(this);
		shareXinlangLayout.setOnClickListener(this);
		messageLayout.setOnClickListener(this);
		back.setOnClickListener(this);
		weixinTv=(TextView)view.findViewById(R.id.weixin_bind_tv);
		xinlangTv=(TextView)view.findViewById(R.id.xinlang_bind_tv);
		
		
		linearLayout2=(LinearLayout)view.findViewById(R.id.linearlayout2);
		lineAddress=view.findViewById(R.id.address_line);
		
		linearLayout2.setVisibility(View.GONE);
		lineAddress.setVisibility(View.GONE);
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
			for(int i=0;i<PetApplication.myUser.aniList.size();i++){
				if(PetApplication.myUser.aniList.get(i).master_id==PetApplication.myUser.userId){
					linearLayout2.setVisibility(View.VISIBLE);
					lineAddress.setVisibility(View.VISIBLE);
					break;
				}
			}
		}
		
		
		
		linearlayout12=(LinearLayout)view.findViewById(R.id.linearlayout12);
		lineInvite=view.findViewById(R.id.lineInvite);
		linearlayout13=(LinearLayout)view.findViewById(R.id.linearlayout13);
		black_layout=(RelativeLayout)view.findViewById(R.id.black_layout);
		popupParent=(View)view.findViewById(R.id.popup_parent);
		/*if(!StringUtil.isEmpty(Constants.CON_VERSION)&&"1.0".equals(Constants.CON_VERSION))
		{
			linearlayout12.setVisibility(View.GONE);
			lineInvite.setVisibility(View.GONE);
		}else{*/
			linearlayout12.setVisibility(View.VISIBLE);
			lineInvite.setVisibility(View.VISIBLE);
		/*}*/
		
		linearlayout12.setOnClickListener(this);
		linearLayout2.setOnClickListener(this);
		linearlayout13.setOnClickListener(this);
		
		SharedPreferences sp=getActivity().getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
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
		SharedPreferences sp=getActivity().getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		Editor editor=sp.edit();
		switch (v.getId()) {
		case R.id.linearlayout2:
			Intent intent22=new Intent(getActivity(),ReceiverAddressActivity.class);
			this.startActivity(intent22);
			break;
			case R.id.linearlayout12:
				if(PetApplication.myUser!=null&&PetApplication.myUser.inviter!=0){
					Intent intent13=new Intent(getActivity(),InviteOthersDialogActivity.class);
					intent13.putExtra("mode", 2);
					this.startActivity(intent13);
				}else
				if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
					if(/*Constants.user.aniList.size()>=10*/false){
						DialogNote dialog=new DialogNote(popupParent, getActivity(),black_layout, 3);
					}else{
						Intent intent13=new Intent(getActivity(),InviteOthersDialogActivity.class);
						intent13.putExtra("mode", 2);
						this.startActivity(intent13);
					}
				}

				break;
			case R.id.linearlayout13:
				Intent intent14=new Intent(getActivity(),UsersListActivity.class);
				intent14.putExtra("mode", 1);
				this.startActivity(intent14);
				break;
		case R.id.set_pass_layout:
			if(SetPassActivity.setPassActivity!=null){
				SetPassActivity.setPassActivity.finish();
				SetPassActivity.setPassActivity=null;
			}
			Intent intent1=new Intent(getActivity(),SetPassActivity.class);
			
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
						Intent intent2=new Intent(getActivity(),RegisterNoteDialog.class);
						intent2.putExtra("mode", 2);
						SetupFragment.this.startActivity(intent2);
					}
					
					@Override
					public void onButtonOne() {
						// TODO Auto-generated method stub
						Intent intent=new Intent(getActivity(),SetPassActivity.class);
						SetupFragment.this.startActivity(intent);
					}
				};
				Intent intent2=new Intent(getActivity(),Dialog3Activity.class);
				intent2.putExtra("mode", 1);
				this.startActivity(intent2);
				return;
			}
			if(RegisterNoteDialog.registerNoteDialog!=null){
				RegisterNoteDialog.registerNoteDialog.finish();
				RegisterNoteDialog.registerNoteDialog=null;
			}
			Intent intent2=new Intent(getActivity(),RegisterNoteDialog.class);
			intent2.putExtra("mode", 2);
			this.startActivity(intent2);
			break;

		case R.id.bind_layout_weixin:
			
			if(!StringUtil.isEmpty(PetApplication.myUser.weixin_id)){
	    		return;
	    	}
           
			
			
			
			 if(!sp.getBoolean(Constants.LOCK_TO_WEIXIN, false)){
				 if(showProgress==null){
						showProgress=new ShowProgress(getActivity(), progressLayout);
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
			 if(!StringUtil.isEmpty(PetApplication.myUser.xinlang_id)){
	            	return;
		    	}
			if(!sp.getBoolean(Constants.LOCK_TO_XINLANG, false)){
				if(showProgress==null){
					showProgress=new ShowProgress(getActivity(), progressLayout);
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
        	   mController.doOauthVerify(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
       		    @Override
       		    public void onStart(SHARE_MEDIA platform) {
       		        Toast.makeText(getActivity(), "授权开始", Toast.LENGTH_SHORT).show();
       		    }
       		    @Override
       		    public void onError(SocializeException e, SHARE_MEDIA platform) {
       		        Toast.makeText(getActivity(), "授权错误", Toast.LENGTH_SHORT).show();
       		     SharedPreferences sp=getActivity().getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
        			Editor editor=sp.edit();
       		     shareWeixinIV.setImageResource(R.drawable.unchecked);
					editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
					editor.commit();
       		        if(showProgress!=null)showProgress.progressCancel();
       		    }
       		    @Override
       		    public void onComplete(Bundle value, SHARE_MEDIA platform) {
       		        Toast.makeText(getActivity(), "授权完成", Toast.LENGTH_SHORT).show();
       		     SharedPreferences sp=getActivity().getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
       			Editor editor=sp.edit();
       		     editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, true);
					shareWeixinIV.setImageResource(R.drawable.checked);
					editor.commit();
//       		        getWeixinInfo();
       		    }
       		    @Override
       		    public void onCancel(SHARE_MEDIA platform) {
       		    	 if(showProgress!=null)showProgress.progressCancel();
       		        Toast.makeText(getActivity(), "授权取消", Toast.LENGTH_SHORT).show();
       		     shareWeixinIV.setImageResource(R.drawable.unchecked);
       		  SharedPreferences sp=getActivity().getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
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
				 mController.doOauthVerify(getActivity(), SHARE_MEDIA.SINA, new UMAuthListener() {
		       		    @Override
		       		    public void onStart(SHARE_MEDIA platform) {
		       		        Toast.makeText(getActivity(), "授权开始", Toast.LENGTH_SHORT).show();
		       		    }
		       		    @Override
		       		    public void onError(SocializeException e, SHARE_MEDIA platform) {
		       		        Toast.makeText(getActivity(), "授权错误", Toast.LENGTH_SHORT).show();
		       		     SharedPreferences sp=getActivity().getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		        			Editor editor=sp.edit();
		        			shareXinlangIV.setImageResource(R.drawable.unchecked);
							editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, false);
							editor.commit();
		       		        if(showProgress!=null)showProgress.progressCancel();
		       		    }
		       		    @Override
		       		    public void onComplete(Bundle value, SHARE_MEDIA platform) {
		       		        Toast.makeText(getActivity(), "授权完成", Toast.LENGTH_SHORT).show();
		       		     SharedPreferences sp=getActivity().getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		       			Editor editor=sp.edit();
		       		     editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, true);
		       		  shareXinlangIV.setImageResource(R.drawable.checked);
							editor.commit();
//		       		        getWeixinInfo();
		       		    }
		       		    @Override
		       		    public void onCancel(SHARE_MEDIA platform) {
		       		    	 if(showProgress!=null)showProgress.progressCancel();
		       		        Toast.makeText(getActivity(), "授权取消", Toast.LENGTH_SHORT).show();
		       		     shareXinlangIV.setImageResource(R.drawable.unchecked);
		       		  SharedPreferences sp=getActivity().getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
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
			
			break;
		case R.id.message_layout:
			Intent intent=new Intent(getActivity(),SettingsActivity.class);
			startActivity(intent);
			
			break;
		}
		editor.commit();
	}

}
