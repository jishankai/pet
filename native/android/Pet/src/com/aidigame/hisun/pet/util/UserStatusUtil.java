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

import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.pet.ui.ChoseStarActivity;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.widget.XinlangShare;
import com.aidigame.hisun.pet.widget.fragment.DialogGoRegister;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
import com.aidigame.hisun.pet.widget.fragment.MenuFragment;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class UserStatusUtil {
	/**
	 * 判断是否登录成功
	 * @param context
	 * @return
	 */
	public static boolean isLoginSuccess(final Activity context,View view,View blackView){
		boolean flag=false;
		LogUtil.i("me", "判断是否登录成功="+Constants.isSuccess);
		if(Constants.isSuccess/*false*/){
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
						HttpUtil.login(context,null);
					}
				}).start();
				flag=true;
			}else{
				new DialogGoRegister(view, context,blackView,0);
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
		if(Constants.isSuccess&&Constants.user==null){
			new Thread(new Runnable() {
				 
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//加载用户信息
					User user=HttpUtil.info(context,handler,Constants.user.userId);
					
					if(user!=null){
						Constants.user=user;
						final ArrayList<Animal> temp=HttpUtil.usersKingdom(context,Constants.user, 1, handler);
						Constants.user.aniList=temp;
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(HomeFragment.homeFragment!=null)HomeFragment.homeFragment.initArcView();
								if(MenuFragment.menuFragment!=null)MenuFragment.menuFragment.setViews();
							}
						});
					}
					
					
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
	public static void setDefaultKingdom(){
		if(MenuFragment.menuFragment!=null){
			/*
			 * 官职
			 * 王国头像列表
			 */
			MenuFragment.menuFragment.setViews();
		}
		if(HomeFragment.homeFragment!=null){
			/*
			 * 1.相机是否显示
			 * 2.快捷按钮
			 */
			if(Constants.user.userId==Constants.user.currentAnimal.master_id){
				HomeFragment.homeFragment.cameraBt.setVisibility(View.VISIBLE);
			}else{
				HomeFragment.homeFragment.cameraBt.setVisibility(View.INVISIBLE);
			}
			HomeFragment.homeFragment.initArcView();
		}
		if(UserDossierActivity.userDossierActivity!=null){
			/*
			 * 1.用户王国列表
			 * 2.资料中间栏
			 */
			UserDossierActivity.userDossierActivity.addNewKingdom();
			UserDossierActivity.userDossierActivity.setUserInfo(Constants.user);
		}
		if(NewShowTopicActivity.newShowTopicActivity!=null){
		}
	}

}
