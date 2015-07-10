package com.aidigame.hisun.imengstar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.aidigame.hisun.imengstar.R;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.huanxin.BaseActivity;
import com.aidigame.hisun.imengstar.widget.fragment.ShareDialogFragment;
import com.aidigame.hisun.imengstar.widget.fragment.ShareDialogFragment.ShareDialogFragmentResultListener;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

public class ShareActivity extends BaseActivity {
	private RelativeLayout black_layout,share_layout;
	private View popupParent;
	private  UMSocialService mController;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_share_layout);
		PetPicture petPicture=(PetPicture)getIntent().getSerializableExtra("PetPicture");
		 share_layout=(RelativeLayout)findViewById(R.id.sharelayout);
		 share_layout.setVisibility(View.VISIBLE);
		 popupParent=findViewById(R.id.popup_parent);
			black_layout=(RelativeLayout)findViewById(R.id.black_layout);
			mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
			ShareDialogFragment sf=new ShareDialogFragment(petPicture, popupParent, black_layout, 4);
			FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.sharelayout, sf);
			sf.setShareDialogFragmentResultListener(new ShareDialogFragmentResultListener() {
				
				@Override
				public void onResult(boolean isSuccess) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			ft.commit();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/**使用SSO授权必须添加如下代码 */
		if(requestCode!=1&&requestCode!=2){
			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	        if(ssoHandler != null){
	           ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	        }
		}
	}
}
