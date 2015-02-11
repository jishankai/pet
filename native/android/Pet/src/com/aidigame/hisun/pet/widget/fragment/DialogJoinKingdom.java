package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.DialogNoteActivity;
import com.aidigame.hisun.pet.ui.DialogPengTaSuccActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;

/**
 * 用户是否注册弹窗
 * @author admin
 *
 */
public class DialogJoinKingdom {
	View parent,view;
	Context context;
	PopupWindow popupWindow;
	View blackView;
	Animal animal;
	
	TextView noteTv;
	HandleHttpConnectionException handleHttpConnectionException;
	ResultListener listener;
	
	public DialogJoinKingdom(View parent,Context context,View blackView,Animal animal){
		this.parent=parent;
		this.context=context;
		this.blackView=blackView;
		this.animal=animal;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		initView();
	}
	int num=0;
	private void initView() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(context).inflate(R.layout.popup_dialog_join_kingdom, null);
		noteTv=(TextView)view.findViewById(R.id.note_tv);
		
		
		int count=0;
		for(int i=0;i<PetApplication.myUser.aniList.size();i++){
//			if(Constants.user.aniList.get(i).master_id!=Constants.user.userId)
				count++;
		}
		
		
		if(count>10&&count<=20){
			num=(count)*5;
		}else if(count>20){
			num=100;
		}
		if(count>=10){
			
			
			
			String htmlStr1="<html>"
		             +"<body>"
					    +"温馨提示：本次成功捧星会消耗您"
		                +"<font color=\"#fb6137\">"
		                +num
		                +"</font>"
		                +"金币哦~ "
		             +"</body>"
		      + "</html>";
			noteTv.setText(Html.fromHtml(htmlStr1));
		}
		
		
		popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int[] location=new int[2];
		parent.getLocationInWindow(location);
		blackView.setBackgroundResource(R.color.window_black_bagd);
		popupWindow.showAtLocation(blackView, Gravity.CENTER, 0, 0);
//		popupWindow.showAsDropDown(parent, (location[0]+parent.getWidth()/2-view.getMeasuredWidth()/2)/2, -view.getMeasuredHeight()/2);
		view.findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				if(listener!=null)listener.getResult(false);
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
						 * 加入王国
						 */
						final Animal an=HttpUtil.joinOrQuitKingdom(context,animal, handleHttpConnectionException.getHandler(context), 0);
						HttpUtil.animalInfo(context,animal, handleHttpConnectionException.getHandler(context));
					    	
	                        handleHttpConnectionException.getHandler(context).post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(an!=null){
									animal.is_join=true;
							    	animal.fans++;
							    	String[] jobs=StringUtil.getUserJobs();
							    	animal.u_rank=jobs[0];
							    	animal.job=jobs[0];
							    	animal.u_rankCode=1;
							    	PetApplication.myUser.coinCount-=num;
//									Toast.makeText(context, "捧TA成功", 1000).show();
									if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
										if(!PetApplication.myUser.aniList.contains(animal))
											PetApplication.myUser.aniList.add(animal);
									}else{
										PetApplication.myUser.aniList=new ArrayList<Animal>();
										PetApplication.myUser.aniList.add(animal);
									}
									Intent intent=new Intent(context,DialogPengTaSuccActivity.class);
									intent.putExtra("animal", an);
									intent.putExtra("mode", 7);
									context.startActivity(intent);
									
									 }else{
										 Toast.makeText(context, "加入王国失败", 1000).show();
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
				blackView.setBackgroundDrawable(null);
				if(listener!=null)listener.getResult(false);
			}
		});
		
	}
	public void setResultListener(ResultListener listener){
		this.listener=listener;
	}
public static interface ResultListener{
	void getResult(boolean isSuccess);
}
}
