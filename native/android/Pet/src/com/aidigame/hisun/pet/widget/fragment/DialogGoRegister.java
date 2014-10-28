package com.aidigame.hisun.pet.widget.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;

/**
 * 用户是否注册弹窗
 * @author admin
 *
 */
public class DialogGoRegister {
	View parent,view;
	Activity context;
	PopupWindow popupWindow;
	View blackView;
	int mode=0;//0,注册提示；1,加入王国超过10个提示；2，取消关注提示
	TextView tv1,tv2,tv3,tv4;
	Animal animal;//取消关注的宠物
	ResultListener listener;
	public DialogGoRegister(View parent,Activity context,View blackView,int mode){
		this.parent=parent;
		this.context=context;
		this.blackView=blackView;
		this.mode=mode;
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(context).inflate(R.layout.popup_dialog_go_register, null);
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
		tv1=(TextView)view.findViewById(R.id.textView1);
		tv2=(TextView)view.findViewById(R.id.textView2);
		tv3=(TextView)view.findViewById(R.id.textView3);
		
		tv4=(TextView)view.findViewById(R.id.textView4);
		tv4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mode==0){
					Intent intent=new Intent(context,ChoseAcountTypeActivity.class);
					context.startActivity(intent);
					
				}
				//取消关注
				if(mode==2){
					deleteFocus();
				}
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
		switch (mode) {
		case 1:
			joinKingdomNote();
			break;
		case 2:
			cancelFocus();
			break;
		}
	}
	public void closePopupTodo(){
		if(context instanceof NewHomeActivity){
			((NewHomeActivity) context).homeFragment.setBlureViewInvisible();
		}
	}
	/**
	 * 需要被取消关注的宠物
	 * @param animal
	 */
	public void setAnimal(Animal animal){
		this.animal=animal;
	}
	/**
	 * 加入王国超过10个弹窗提示
	 */
	private void joinKingdomNote(){
		tv1.setText("星球法则是最多捧10个萌星~");
		tv2.setText("萌星们都那么可爱，");
		tv3.setText("专一点嘛~");
		tv4.setText("哎~好吧");
	}
	/**
	 * 取消关注弹窗提示
	 */
	private void cancelFocus(){
		tv1.setText("亲爱的真的忍心取消关注我么？");
		tv2.setText("这是真的么~");
		tv3.setText("是么~");
		tv4.setText("额~是的");
	}
	/**
	 * 取消关注
	 */
	private void deleteFocus(){
		if(animal!=null){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final boolean flag=HttpUtil.userDeleteFollow(animal, null,context);
					
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(flag){
									animal.is_follow=false;
								Toast.makeText(context, "取消关注成功", Toast.LENGTH_SHORT).show();
								animal.is_follow=false;
								animal.followers--;
								if(listener!=null){
									listener.getResult(true);
								}
								}else{
									
//									 * 取消关注失败
									Toast.makeText(context, "取消关注失败", Toast.LENGTH_SHORT).show();
								}
							}
						});
					
				}
			}).start();
		}
	}
	public void setListener(ResultListener listener){
		this.listener=listener;
	}
	public static interface ResultListener{
		void getResult(boolean isSuccess);
	}

}
