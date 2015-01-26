package com.aidigame.hisun.pet.ui;

import u.aly.bu;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 弹出框样式3
 * @author admin
 *
 */
public class Dialog4Activity extends Activity implements OnClickListener{
	ImageView closeIv;
	int mode=1;//1,注册还是绑定弹窗提示；2，打赏提示;3,充值提示;4,兑换提示;5,网络异常，提示框;6,环信账号在其他手机登陆提示
	TextView note1Tv,note2Tv,button1,button2;
	LinearLayout regLayout,giveLayout;
	public static Dialog3ActivityListener listener;
	boolean isWeixin=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_dialog4);
		mode=getIntent().getIntExtra("mode", 1);
		closeIv=(ImageView)findViewById(R.id.close);
		note1Tv=(TextView)findViewById(R.id.note_tv1);
		note2Tv=(TextView)findViewById(R.id.note_tv2);
		button1=(TextView)findViewById(R.id.button1);
		button2=(TextView)findViewById(R.id.button2);
		regLayout=(LinearLayout)findViewById(R.id.reglayout);
		giveLayout=(LinearLayout)findViewById(R.id.give_layout);
		if(mode==1){
			isWeixin=getIntent().getBooleanExtra("isWeixin", false);
			if(isWeixin){
				note1Tv.setText("您当前微信账号还未绑定");
				note2Tv.setText("使用微信账号注册或与当前账号绑定");
			}else{
				note1Tv.setText("您当前微博账号还未绑定");
				note2Tv.setText("使用微博账号注册或与当前账号绑定");
			}
		}
		
		if(mode==2){
			initview2();
		}else if(mode==3){
			initview3();
		}else if(mode==4){
			initView4();
		}else if(mode==5){
			initView5();
		}else if(mode==6){
			initView6();
		}
		
		
		closeIv.setOnClickListener(this);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		
	}
	/**
	 * 环信在其他手机登陆，账号被挤掉
	 */
	private void initView6() {
		// TODO Auto-generated method stub
		button1.setText("返回");
		button2.setText("一键登录");
		note1Tv.setText("发消息要先登录哦~");
		note2Tv.setVisibility(View.GONE);
	}
	/**
	 * 网络异常提示
	 */
	private void initView5() {
		// TODO Auto-generated method stub
		note1Tv.setText("网络异常，请重试~");
		note2Tv.setVisibility(View.GONE);
		button1.setText("离开");
		button2.setText("重试");
		button1.setBackgroundResource(R.drawable.dialog_red_button);
	}
	/**
	 * 兑换提示
	 */
	private void initView4() {
		// TODO Auto-generated method stub
		int num=getIntent().getIntExtra("num", 1);
		String name=getIntent().getStringExtra("name");
		 
		 
		 button1.setText("再想想");
		 button2.setText("没问题");
		regLayout.setVisibility(View.GONE);
		giveLayout.setVisibility(View.VISIBLE);
		TextView tv1=(TextView)findViewById(R.id.give_tv1);
		TextView tv2=(TextView)findViewById(R.id.give_tv2);
		final ImageView iv2=(ImageView)findViewById(R.id.give_iv1);
		TextView tv3=(TextView)findViewById(R.id.give_tv3);
		TextView tv4=(TextView)findViewById(R.id.note3);
		ImageView iv3=(ImageView)findViewById(R.id.gold_iv);
		iv3.setImageResource(R.drawable.exchange_food_red);
		tv1.setText("确认支付");
		tv2.setVisibility(View.GONE);
		tv3.setText(""+(num));
		tv4.setText("兑换"+name+"吗？");
		iv2.setVisibility(View.GONE);
		iv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
				boolean flag=sp.getBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
				Editor e=sp.edit();
				if(!flag){
					e.putBoolean(Constants.GIVE_FOOD_NOTE_SHOW, true);
					iv2.setImageResource(R.drawable.atuser_list_checked);
				}else{
					iv2.setImageResource(R.drawable.atuser_list_unchecked);
					e.putBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
				}
				e.commit();
			}
		});
	}
	/**
	 * 打赏提示框
	 */
	 private void initview2() {
		// TODO Auto-generated method stub
		 int num=getIntent().getIntExtra("num", 1);
		 int goldNum=num-Constants.user.food;
		 if(goldNum<0)goldNum=0;
		 
		 
		 button1.setText("再想想");
		 button2.setText("没问题");
		regLayout.setVisibility(View.GONE);
		giveLayout.setVisibility(View.VISIBLE);
		TextView tv1=(TextView)findViewById(R.id.give_tv1);
		final ImageView iv2=(ImageView)findViewById(R.id.give_iv1);
		TextView tv3=(TextView)findViewById(R.id.give_tv3);
		tv1.setText("本次打赏"+num+"份口粮");
		tv3.setText(""+(goldNum));
		iv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
				boolean flag=sp.getBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
				Editor e=sp.edit();
				if(!flag){
					e.putBoolean(Constants.GIVE_FOOD_NOTE_SHOW, true);
					iv2.setImageResource(R.drawable.atuser_list_checked);
				}else{
					iv2.setImageResource(R.drawable.atuser_list_unchecked);
					e.putBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
				}
				e.commit();
			}
		});
	}
	 /**
		 * 充值提示
		 */
		 private void initview3() {
			// TODO Auto-generated method stub
			 int num=getIntent().getIntExtra("num", 1);
			 int goldNum=num-Constants.user.food;
			 if(goldNum<0)goldNum=0;
			 
			 
			 button1.setText("再想想");
			 button2.setText("去充值");
			 
			 button1.setText("好吧~");
			 button2.setVisibility(View.GONE);
			 
			 
			 
			regLayout.setVisibility(View.GONE);
			giveLayout.setVisibility(View.VISIBLE);
			TextView tv1=(TextView)findViewById(R.id.give_tv1);
			final ImageView iv2=(ImageView)findViewById(R.id.give_iv1);
			TextView tv3=(TextView)findViewById(R.id.give_tv3);
			TextView tv4=(TextView)findViewById(R.id.note3);
			tv1.setText("本次打赏"+num+"份口粮");
			tv3.setText(""+(goldNum));
//			tv4.setText("先去充值吧~");
			tv4.setText("先去应用挣钱吧~");
			iv2.setVisibility(View.GONE);
			iv2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
					boolean flag=sp.getBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
					Editor e=sp.edit();
					if(!flag){
						e.putBoolean(Constants.GIVE_FOOD_NOTE_SHOW, true);
						iv2.setImageResource(R.drawable.atuser_list_checked);
					}else{
						iv2.setImageResource(R.drawable.atuser_list_unchecked);
						e.putBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
					}
					e.commit();
				}
			});
		}
	@Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	public static boolean canClose=true;
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(listener!=null&&canClose){
			listener.onClose();
		}
		canClose=true;
	}
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			switch (v.getId()) {
			case R.id.close:
				if(listener!=null){
					listener.onClose();
				}
				break;
			case R.id.button2:
				if(listener!=null){
					listener.onButtonTwo();
				}
				break;
			case R.id.button1:
				if(listener!=null){
					listener.onButtonOne();
				}
				break;

			default:
				break;
				
			}
			if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(this)){
				PetApplication.petApp.activityList.remove(this);
			}
			System.gc();
			
		}
		public static void setDialog3ActivityListener(Dialog3ActivityListener listener){
			Dialog4Activity.listener=listener;
		}
		public static interface Dialog3ActivityListener{
			void onClose();
			void onButtonOne();
			void onButtonTwo();
		}
}
