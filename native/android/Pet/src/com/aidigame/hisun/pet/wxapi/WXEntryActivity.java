package com.aidigame.hisun.pet.wxapi;


import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.ShowTopicActivity;
import com.aidigame.hisun.pet.ui.SubmitPictureActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry);
		//通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, Constants.Weixin_APP_KEY, false);
		api.handleIntent(getIntent(), this);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			// goToGetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			// goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;

		 /*switch (resp.errCode) {
		 case BaseResp.ErrCode.ERR_OK:
		 result = R.string.errcode_success;
		 break;
		 case BaseResp.ErrCode.ERR_USER_CANCEL:
		 result = R.string.errcode_cancel;
		 break;
		 case BaseResp.ErrCode.ERR_AUTH_DENIED:
		 result = R.string.errcode_deny;
		 break;
		 default:
		 result = R.string.errcode_unknown;
		 break;
		 }*/
		if(resp.errCode==BaseResp.ErrCode.ERR_OK){
			if(Constants.shareMode==0){
				if(Constants.whereShare==0){
					Toast.makeText(this, "成功分享到微信", Toast.LENGTH_LONG).show();
					
					ShowTopicActivity.showTopicActivity.shareNumChange();
					Constants.shareMode=-1;
					Constants.whereShare=-1;
				}
				else if(Constants.whereShare==2){
					
					PetKingdomActivity.petKingdomActivity.shareNumChange();
				}else if(Constants.whereShare==3){
					UserDossierActivity.userDossierActivity.shareNumChange();
				}
			}
			if(Constants.shareMode==1){
				Toast.makeText(this, "成功分享到朋友圈", Toast.LENGTH_LONG).show();
				if(Constants.whereShare==0){
					
					ShowTopicActivity.showTopicActivity.shareNumChange();
					Constants.shareMode=-1;
					Constants.whereShare=-1;
				}
				if(Constants.whereShare==1){
					Constants.whereShare=-1;
					this.finish();
					SubmitPictureActivity.submitPictureActivity.addShares(true);
					LogUtil.i("me", "SubmitPicture关闭掉===================4");
					return;
				}else if(Constants.whereShare==2){
					PetKingdomActivity.petKingdomActivity.shareNumChange();
				}else if(Constants.whereShare==3){
					UserDossierActivity.userDossierActivity.shareNumChange();
				}
			}
			
			
			
			
		}else if(resp.errCode==BaseResp.ErrCode.ERR_USER_CANCEL){
			if(Constants.whereShare==1){
				this.finish();
				SubmitPictureActivity.submitPictureActivity.over();
				return;
			}
		}
		this.finish();
		
	}

	// private void goToGetMsg() {
	// Intent intent = new Intent(this, GetFromWXActivity.class);
	// intent.putExtras(getIntent());
	// startActivity(intent);
	// finish();
	// }
	//
	// private void goToShowMsg(ShowMessageFromWX.Req showReq) {
	// WXMediaMessage wxMsg = showReq.message;
	// WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
	//
	// StringBuffer msg = new StringBuffer(); // ��֯һ������ʾ����Ϣ����
	// msg.append("description: ");
	// msg.append(wxMsg.description);
	// msg.append("\n");
	// msg.append("extInfo: ");
	// msg.append(obj.extInfo);
	// msg.append("\n");
	// msg.append("filePath: ");
	// msg.append(obj.filePath);
	//
	// Intent intent = new Intent(this, ShowFromWXActivity.class);
	// intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
	// intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
	// intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
	// startActivity(intent);
	// finish();
	// }
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
}