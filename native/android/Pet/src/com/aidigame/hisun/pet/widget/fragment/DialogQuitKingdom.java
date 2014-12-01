package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.UserStatusUtil;

/**
 * 用户是否注册弹窗
 * @author admin
 *
 */
public class DialogQuitKingdom {
	View parent,view;
	Context context;
	PopupWindow popupWindow;
	View blackView;
	Animal animal;
	Handler handleHttpConnectionException;
	ResultListener listener;
	public DialogQuitKingdom(View parent,Context context,View blackView,Animal animal){
		this.parent=parent;
		this.context=context;
		this.blackView=blackView;
		this.animal=animal;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance().getHandler(context);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(context).inflate(R.layout.popup_dialog_quit_kingdom, null);
		popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int[] location=new int[2];
		parent.getLocationInWindow(location);
		blackView.setBackgroundResource(R.color.window_black_bagd);
		popupWindow.showAsDropDown(parent, location[0]+parent.getWidth()/2-view.getMeasuredWidth()/2, -view.getMeasuredHeight()/2);
		view.findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				blackView.setBackgroundDrawable(null);
			}
		});
		view.findViewById(R.id.textView3).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						/*
						 * 退出王国
						 */
						
						if(Constants.user!=null&&Constants.user.currentAnimal!=null&&animal.a_id==Constants.user.currentAnimal.a_id){
							
							getContriTop();
							Animal temp=Constants.user.aniList.get(0);
							if(temp.equals(animal)){
								temp=Constants.user.aniList.get(1);
							}
							final boolean flag=HttpUtil.setDefaultKingdom(context,temp, handleHttpConnectionException);
							if(flag){
								Constants.user.currentAnimal=temp;
								final Animal  an=HttpUtil.joinOrQuitKingdom(context,animal, handleHttpConnectionException, 1);
							    
						    	handleHttpConnectionException.post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										UserStatusUtil.setDefaultKingdom();
										if(an!=null){
									    	animal.is_join=false;
									    	animal.fans--;
										if(Constants.user!=null&&Constants.user.aniList!=null){
											Constants.user.aniList.remove(animal);
										}else{
											Constants.user.aniList=new ArrayList<Animal>();
										
										}
										if(MenuFragment.menuFragment!=null){
											MenuFragment.menuFragment.setViews();
										}
										}else{
											Toast.makeText(context, "退出王国失败", 1000).show();
										}
										if(listener!=null){
											if(an!=null){
												listener.getResult(true);
											}else{
												listener.getResult(false);
											}
											
										}
										
									}
								});
						    
							}else{
handleHttpConnectionException.post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
											Toast.makeText(context, "操作失败", 1000).show();
										if(listener!=null){
												listener.getResult(false);
											
										}
										
									}
								});
							}
							
							return;
							
						}
						
						
						
						
						
						final Animal  an=HttpUtil.joinOrQuitKingdom(context,animal, handleHttpConnectionException, 1);
					    
					    	handleHttpConnectionException.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(an!=null){
								    	animal.is_join=false;
								    	animal.fans--;
									if(Constants.user!=null&&Constants.user.aniList!=null){
										Constants.user.aniList.remove(animal);
									}else{
										Constants.user.aniList=new ArrayList<Animal>();
									
									}
									if(MenuFragment.menuFragment!=null){
										MenuFragment.menuFragment.setViews();
									}
									}else{
										Toast.makeText(context, "退出王国失败", 1000).show();
									}
									if(listener!=null){
										if(an!=null){
											listener.getResult(true);
										}else{
											listener.getResult(false);
										}
										
									}
									
								}
							});
					    
					   
					}
				}).start();
				popupWindow.dismiss();
				
			}
		});
		popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				closePopupTodo();
				blackView.setBackgroundDrawable(null);
			}
		});
		
	}
	public void closePopupTodo(){
		if(context instanceof NewHomeActivity){
			((NewHomeActivity) context).homeFragment.setBlureViewInvisible();
		}
	}
	public void setResultListener(ResultListener listener){
		this.listener=listener;
	}
public static interface ResultListener{
	void getResult(boolean isSuccess);
}
public void getContriTop(){
	Animal[] animals=new Animal[Constants.user.aniList.size()];
	for(int i=0;i<animals.length;i++){
		animals[i]=Constants.user.aniList.get(i);
	}
	Animal temp=null;
	for(int i=0;i<animals.length-1;i++){
		for(int j=i;j<animals.length-1;j++){
			if(animals[j].t_contri<animals[j+1].t_contri){
				temp=animals[j+1];
				animals[j+1]=animals[j];
				animals[j]=temp;
			}
		}
	}
	Constants.user.aniList=new ArrayList<Animal>();
	for(int i=0;i<animals.length;i++){
		Constants.user.aniList.add(animals[i]);
	}
}
}
