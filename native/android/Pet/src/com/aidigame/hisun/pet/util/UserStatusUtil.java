package com.aidigame.hisun.pet.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.SubmitPictureActivity;
import com.aidigame.hisun.pet.ui.UnregisterNoteActivity;
import com.aidigame.hisun.pet.widget.XinlangShare;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class UserStatusUtil {
	/**
	 * 判断是否登录成功
	 * @param context
	 * @return
	 */
	public static boolean isLoginSuccess(final Activity context){
		boolean flag=false;
		LogUtil.i("me", "判断是否登录成功="+Constants.isSuccess);
		if(Constants.isSuccess){
			flag=true;
			if(Constants.user==null){
				downLoadUserInfo(context);
			}
			
		}else{
			if(Constants.SID==null){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.login(context, null);
					}
				}).start();
				flag=true;
			}else{
				Intent intent=new Intent(context,UnregisterNoteActivity.class);
				context.startActivity(intent);
			}
			
		}
		return flag;
	}
	/**
	 * 下载用户信息
	 * @param context
	 */
	public static void downLoadUserInfo(final Activity context){
		if(Constants.isSuccess){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//加载用户信息
					boolean flag=HttpUtil.info(null,context);
				}
			}).start();
		}
	}
	/**
	 * 判断是否获得新浪授权,没有授权，去获得授权
	 * @param context
	 * @return
	 */
	public static boolean hasXinlangAuth(final Activity context){
		boolean flag=false;
		if(Constants.accessToken==null){
			SharedPreferences sPreferences=context.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
			String token=sPreferences.getString("xinlangToken", null);
			if(token!=null){
				Oauth2AccessToken accessToken=Oauth2AccessToken.parseAccessToken(token);
				if(accessToken!=null){
					if(!accessToken.isSessionValid()){
						AlertDialog.Builder builder=new AlertDialog.Builder(context);
						builder.setTitle("提示").setMessage("新浪微博登陆账号已过期，请重新登陆！")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								XinlangShare.xinlangAuth(context);
							}
						});
						
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						final AlertDialog dialog=builder.create();
						
					}else{
						Constants.accessToken=accessToken;
						flag=true;
					}
				}else{
//					if(!(context instanceof SubmitPictureActivity))
					XinlangShare.xinlangAuth(context);
				}
				
			}else{
//				if((context instanceof SubmitPictureActivity))
				XinlangShare.xinlangAuth(context);
			}
		}else{
			flag=true;
		}
		return flag;
	}
	/**
	 * 是否获得授权
	 * @param context
	 * @return
	 */
	public static boolean isXinlangAuth(final Activity context){
		boolean flag=false;
		if(Constants.accessToken==null){
			SharedPreferences sPreferences=context.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
			String token=sPreferences.getString("xinlangToken", null);
			if(token!=null){
				Oauth2AccessToken accessToken=Oauth2AccessToken.parseAccessToken(token);
				if(accessToken!=null){
					if(!accessToken.isSessionValid()){
						flag=false;
						
					}else{
						Constants.accessToken=accessToken;
						flag=true;
					}
				}else{
					flag=false;;
				}
				
			}else{
				flag=false;
			}
		}else{
			flag=true;
		}
		return flag;
	}

}
