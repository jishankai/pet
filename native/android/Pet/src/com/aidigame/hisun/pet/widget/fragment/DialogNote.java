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

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;

/**
 * 用户是否注册弹窗
 * @author admin
 *
 */
public class DialogNote {
	View parent,view;
	Activity context;
	PopupWindow popupWindow;
	View blackView;
	int mode=0;//0,注册提示；1,加入王国超过10个提示，金币不足；2，取消关注提示；3，使用邀请码超过十个,4加入王国超过10个提示，消耗金币
	TextView tv1,tv2,tv3,tv4,noteTv;
	Animal animal;//取消关注的宠物
	ResultListener listener;
	public DialogNote(View parent,Activity context,View blackView,int mode){
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
		noteTv=(TextView)view.findViewById(R.id.note_tv);
		tv4=(TextView)view.findViewById(R.id.textView4);
		tv4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mode==0||mode==4){
					Intent intent=new Intent(context,ChoseAcountTypeActivity.class);
					if(mode==4){
						intent.putExtra("from", 1);
					}
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
		case 3:
			joinKingdomNote2();
			break;
		case 4:
			joinKingdomNote3();
			break;
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
		tv1.setText("本次成功捧星或创建萌星");
		int num=0;
		if(PetApplication.myUser.aniList.size()>=10&&PetApplication.myUser.aniList.size()<=20){
			num=(PetApplication.myUser.aniList.size()+1)*5;
		}else{
			num=100;
		}
		tv2.setText("需要消耗"+num+"金币哦~");
		tv3.setText("您的余额不足~");
		tv4.setText("哎~好吧");
	}
	private void joinKingdomNote3(){
		tv1.setText("本次成功捧星或创建萌星");
		int num=0;
		if(PetApplication.myUser.aniList.size()>=10&&PetApplication.myUser.aniList.size()<=20){
			num=(PetApplication.myUser.aniList.size()+1)*5;
		}else{
			num=100;
		}
		tv2.setText("需要消耗"+num+"金币哦~");
		tv3.setText("您的余额不足~");
		tv3.setVisibility(View.GONE);
		tv4.setText("没问题");
	}
	/**
	 * 使用邀请码  超过十个
	 */
	private void joinKingdomNote2(){
		tv1.setText("暂不能使用邀请码~");
		tv2.setText("星球法则是最多捧10个萌星~");
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
