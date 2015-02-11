package com.aidigame.hisun.pet.util;

import java.util.ArrayList;

import android.R.anim;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.pet.ui.DialogGoRegisterActivity;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.ui.RegisterNoteDialog;
import com.aidigame.hisun.pet.widget.fragment.DialogNote;
import com.aidigame.hisun.pet.widget.fragment.UserCenterFragment;

public class UserStatusUtil {
	/**
	 * 判断是否登录成功
	 * @param context
	 * @return
	 */
	public static boolean isLoginSuccess(final Activity context,View view,View blackView){
		boolean flag=false;
		LogUtil.i("me", "判断是否登录成功="+PetApplication.isSuccess);
		if(PetApplication.isSuccess/*false*/){
			flag=true;
			if(PetApplication.myUser==null){
				downLoadUserInfo(context);
			}
			
		}else{
			if(PetApplication.SID==null){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.login(context,null);
					}
				}).start();
				flag=true;
			}else{
//				new DialogGoRegister(view, context,blackView,0);
				Intent intent=new Intent(context,DialogGoRegisterActivity.class);
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
		final Handler handler=HandleHttpConnectionException.getInstance().getHandler(context);
		if(PetApplication.isSuccess&&PetApplication.myUser!=null){
			new Thread(new Runnable() {
				 
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//加载用户信息
					MyUser user=HttpUtil.info(context,handler,PetApplication.myUser.userId);
					
					if(user!=null){
						PetApplication.myUser=user;
						final ArrayList<Animal> temp=HttpUtil.usersKingdom(context,PetApplication.myUser, 1, handler);
						PetApplication.myUser.aniList=temp;
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
							}
						});
					}
					
					
				}
			}).start();
		}
	}
	
	/**
	 * 是否获得授权
	 * @param context
	 * @return
	 */
	public static boolean isXinlangAuth(final Activity context){/*
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
	*/
	return true;	
	}
	public static void setDefaultKingdom(){
		if(UserCenterFragment.userCenterFragment!=null){
			/*
			 * 官职
			 * 王国头像列表
			 */
			UserCenterFragment.userCenterFragment.updatateInfo(true);;
		}
		
		
		if(NewShowTopicActivity.newShowTopicActivity!=null){
		}
	}

}
