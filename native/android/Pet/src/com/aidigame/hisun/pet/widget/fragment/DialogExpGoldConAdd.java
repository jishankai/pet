package com.aidigame.hisun.pet.widget.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.LogUtil;

/**
 * 提示弹出框，经验+1，金币+1，贡献+1
 * @author admin
 *
 */
public class DialogExpGoldConAdd {
	Context context;
	int mode;//1,加经验；2，加金币；3，需要增加的数目
	int num;
	AlertDialog alertDialog;
	PopupWindow popupWindow;
	View view,parent;
	Toast  toast;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				AlphaAnimation alphaAnimation=new AlphaAnimation(1f, 0);
				alphaAnimation.setDuration(1000);
				alphaAnimation.setFillAfter(true);
				view.clearAnimation();
				view.startAnimation(alphaAnimation);
				alphaAnimation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
//						alertDialog.dismiss();
						popupWindow.dismiss();
					}
				});
			}
		};
	};
	public DialogExpGoldConAdd(Context context,int mode,int num,View parent){
		this.context=context;
		this.mode=mode;
		this.num=num;
		this.parent=parent;
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(context).inflate(R.layout.dialog_exp_gold_contribute_add, null);
		TextView tv=(TextView)view.findViewById(R.id.textView1);
		ImageView iv=(ImageView)view.findViewById(R.id.imageView1);
		view.setVisibility(View.VISIBLE);
		switch (mode) {
		case 1:
			iv.setImageResource(R.drawable.exp_star);
			break;
		case 2:
			iv.setImageResource(R.drawable.gold_big);
			break;
		case 3:
			iv.setImageResource(R.drawable.contribute_heart);
			break;
		}
		if(num<=0){
			view.setVisibility(View.INVISIBLE);
			return;
		}
		tv.setText("+ "+num);
		/*AlertDialog.Builder builder=new AlertDialog.Builder(context);
		alertDialog=builder.setView(view)
				    .setInverseBackgroundForced(true)
				    .show();*/
		popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		toast=new Toast(context);
		toast.setView(view);
		toast.show();
		toast.setGravity(android.view.Gravity.CENTER, 0, 0);
		
//		popupWindow.showAsDropDown(parent,(parent.getMeasuredWidth()- view.getMeasuredWidth())/2, (parent.getMeasuredHeight()-view.getMeasuredHeight())/2);
		LogUtil.i("scroll", "(parent.getMeasuredWidth()- view.getMeasuredWidth())/2="+(parent.getMeasuredWidth()- view.getMeasuredWidth())/2+", (parent.getMeasuredHeight()-view.getMeasuredHeight())/2="+ (parent.getMeasuredHeight()-view.getMeasuredHeight())/2);
//		popupWindow.showAsDropDown(parent,(parent.getMeasuredWidth()- view.getMeasuredWidth())/2, (parent.getMeasuredHeight()-view.getMeasuredHeight())/2);
//		popupWindow.showAtLocation(parent,android.view.Gravity.CENTER,(parent.getMeasuredWidth()- view.getMeasuredWidth())/2, (parent.getMeasuredHeight()-view.getMeasuredHeight())/2);
		AlphaAnimation alphaAnimation=new AlphaAnimation(0, 1f);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setFillAfter(true);
		view.clearAnimation();
//		view.startAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				view.setVisibility(View.INVISIBLE);
				Message msg=handler.obtainMessage();
				msg.what=1;
				handler.sendMessageDelayed(msg, 3000);
			}
		});
	}
	public boolean isVisible(){
		if(view.getVisibility()==view.VISIBLE)return true;
		return false;
	}

}
