package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.fragment.BegFoodFragment;
import com.aidigame.hisun.pet.widget.fragment.DiscoveryFragment;
import com.aidigame.hisun.pet.widget.fragment.MyPetFragment;
import com.aidigame.hisun.pet.widget.fragment.UserCenterFragment;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends FragmentActivity implements OnClickListener{
	public static int HOME_MY_PET=1;
	public static int HOME_BEG_FOOD=2;
	public static int HOME_DISCORY=3;
	public static int HOME_USER_CENTER=4;
	public MyPetFragment myPetFragment;
	BegFoodFragment begFoodFragment;
	public DiscoveryFragment discoveryFragment;
	UserCenterFragment userCenterFragment;
	FrameLayout begFrameLayout,frameLayout;
	ImageView petIV,begIV,discoveryIV,otherIV,guideIv2,guideIv3;
	int current_show=HOME_BEG_FOOD;
	public static HomeActivity homeActivity;
	View popupParent;
	RelativeLayout black_layout;
	public RelativeLayout rootLayout;
	TextView messageNumTv;
	Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_home_activity);
		guideIv2=(ImageView)findViewById(R.id.guide2);
		guideIv3=(ImageView)findViewById(R.id.guide3);
		SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		Editor e=sp.edit();
		boolean guide2=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE2, true);
		if(guide2){
			guideIv2.setVisibility(View.VISIBLE);
			e.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE2, false);
			e.commit();
		}else{
			guideIv2.setVisibility(View.GONE);
		}
		guideIv2.setOnClickListener(this);
		guideIv3.setOnClickListener(this);
		
		rootLayout=(RelativeLayout)findViewById(R.id.root_layout);
		messageNumTv=(TextView)findViewById(R.id.message_tv);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		homeActivity=this;
		/*if(FirstPageActivity.firstPageActivity!=null){
			ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
			am.moveTaskToFront(FirstPageActivity.firstPageActivity.getTaskId(), 0);
			rootLayout.setVisibility(View.INVISIBLE);
		}*/
		
		
		popupParent=findViewById(R.id.popup_parent);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		
		begFrameLayout=(FrameLayout)findViewById(R.id.fragment_framelayout_beg);
		frameLayout=(FrameLayout)findViewById(R.id.fragment_framelayout);
		
		showBegFoodFragment();
		initBottomTab();
		if(Constants.isSuccess){
			getNewsNum();
		}
	}


	private void showHomeMyPetFragment() {
		// TODO Auto-generated method stub
		
		SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		Editor e=sp.edit();
		boolean guide3=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE3, true);
		if(guide3){
			guideIv3.setVisibility(View.VISIBLE);
			e.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE3, false);
			e.commit();
		}else{
			guideIv3.setVisibility(View.GONE);
		}
		
		
		begFrameLayout.setVisibility(View.GONE);
		frameLayout.setVisibility(View.VISIBLE);
		if(myPetFragment==null){
			myPetFragment=new MyPetFragment();
		}
		FragmentManager  fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.fragment_framelayout, myPetFragment, "HOME_MY_PET");
		ft.commit();
		current_show=HOME_MY_PET;
	}
	private void showBegFoodFragment(){
		frameLayout.setVisibility(View.GONE);
		begFrameLayout.setVisibility(View.VISIBLE);
		if(begFoodFragment==null){
			begFoodFragment=new BegFoodFragment();
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.fragment_framelayout_beg, begFoodFragment, "HOME_BEG_FOOD");
		ft.commit();
		current_show=HOME_BEG_FOOD;
	}
	private void showUserCenterFragment(){
		frameLayout.setVisibility(View.GONE);
		begFrameLayout.setVisibility(View.VISIBLE);
		if(userCenterFragment==null){
			userCenterFragment=new UserCenterFragment();
		}else{
			userCenterFragment.updatateInfo();;
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.fragment_framelayout_beg, userCenterFragment, "HOME_USER_CENTER");
		ft.commit();
		current_show=HOME_USER_CENTER;
	}
	/**
	 * 
	 */
	private void showDiscoveryPetFragment() {
		// TODO Auto-generated method stub
		begFrameLayout.setVisibility(View.GONE);
		frameLayout.setVisibility(View.VISIBLE);
		if(discoveryFragment==null){
			discoveryFragment=new DiscoveryFragment();
		}
		FragmentManager  fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.fragment_framelayout, discoveryFragment, "HOME_DISCORY");
		ft.commit();
		current_show=HOME_DISCORY;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bottom_iv_my_pet:
			if(!UserStatusUtil.isLoginSuccess(this,popupParent,black_layout)){
				return;
		 }
			clickTab(R.id.bottom_iv_my_pet);
			break;
		case R.id.bottom_iv_beg_food:
			clickTab(R.id.bottom_iv_beg_food);
			break;
		case R.id.bottom_iv_discovery:
			clickTab(R.id.bottom_iv_discovery);
			break;
		case R.id.bottom_iv_other:
			clickTab(R.id.bottom_iv_other);
			break;
		case R.id.guide2:
			guideIv2.setVisibility(View.GONE);
			break;
		case R.id.guide3:
			guideIv3.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}
    /**
     * 底部按钮相关功能
     */
	private void initBottomTab() {
		// TODO Auto-generated method stub
		petIV=(ImageView)findViewById(R.id.bottom_iv_my_pet);
		begIV=(ImageView)findViewById(R.id.bottom_iv_beg_food);
		discoveryIV=(ImageView)findViewById(R.id.bottom_iv_discovery);
		otherIV=(ImageView)findViewById(R.id.bottom_iv_other);
		
		petIV.setOnClickListener(this);
		begIV.setOnClickListener(this);
		discoveryIV.setOnClickListener(this);
		otherIV.setOnClickListener(this);
	}
	/**
	 * 点击底部Tab
	 * @param id
	 */
	public void clickTab(final int id){
		/*
		 * 开始播放动画
		 */
		Animation anim=AnimationUtils.loadAnimation(this, R.anim.anim_joggling);
		if(Constants.isSuccess){
			getNewsNum();
		}
		switch (id) {
		case R.id.bottom_iv_my_pet:
			petIV.clearAnimation();
			petIV.setAnimation(anim);
			petIV.startAnimation(anim);
			
			break;
		case R.id.bottom_iv_beg_food:
			begIV.clearAnimation();
			begIV.setAnimation(anim);
			begIV.startAnimation(anim);
			break;
		case R.id.bottom_iv_discovery:
			discoveryIV.clearAnimation();
			discoveryIV.setAnimation(anim);
			discoveryIV.startAnimation(anim);
			break;
		case R.id.bottom_iv_other:
			otherIV.clearAnimation();
			otherIV.setAnimation(anim);
			otherIV.startAnimation(anim);
			break;
		}
		switch (id) {
		case R.id.bottom_iv_my_pet:
			if(current_show==HOME_MY_PET){
				if(myPetFragment!=null){
					myPetFragment.homeMyPet.refresh();
				}
				return;
			}
			break;
		case R.id.bottom_iv_beg_food:
			if(current_show==HOME_BEG_FOOD){
				if(begFoodFragment!=null)begFoodFragment.refreshList();
				return;
			}
			break;
		case R.id.bottom_iv_discovery:
			if(current_show==HOME_DISCORY){
				if(discoveryFragment!=null){
					discoveryFragment.refresh();
				}
				return;
			}
			break;
		case R.id.bottom_iv_other:
			if(current_show==HOME_USER_CENTER)return;
			break;
		}
		
		petIV.setImageResource(R.drawable.bottom_my_pet_normal);
		begIV.setImageResource(R.drawable.bottom_beg_food_normal);
		discoveryIV.setImageResource(R.drawable.bottom_discovery_normal);
		otherIV.setImageResource(R.drawable.bottom_other_normal);
		
		
		
		
		
		
		switch (id) {
		case R.id.bottom_iv_my_pet:
			
			showHomeMyPetFragment();
			petIV.setImageResource(R.drawable.bottom_my_pet_chose);
			break;
		case R.id.bottom_iv_beg_food:
			showBegFoodFragment();
			begIV.setImageResource(R.drawable.bottom_beg_food_chose);
			break;
		case R.id.bottom_iv_discovery:
			showDiscoveryPetFragment();
			discoveryIV.setImageResource(R.drawable.bottom_discovery_chose);
			break;
		case R.id.bottom_iv_other:
			showUserCenterFragment();
			otherIV.setImageResource(R.drawable.bottom_other_chose);
			break;
		}
		anim.setAnimationListener(new AnimationListener() {
			
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
				
				
			}
		});
		
		
		
		
		
	}
    
	
	   @Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   	if(Constants.isSuccess){
			getNewsNum();
		}
	   }
	      
	      
	  	int count=0;
		long lastTime=0;
		 @Override
		    public boolean onKeyDown(int keyCode, KeyEvent event) {
		    	// TODO Auto-generated method stub
		    	if(keyCode==KeyEvent.KEYCODE_BACK){
		    		
		    			if(lastTime!=0){
		        			long temp=System.currentTimeMillis();
		        			if(temp-lastTime>2000){
		        				count=0;
		        				lastTime=temp;
		        			}
		        		}else{
		        			
		        		}
		        		if(count<1){
		        			Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
		        			count++;
		        			lastTime=System.currentTimeMillis();
		        			return true;
		        		}
		        		SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		        		Editor editor=sp.edit();
		        		editor.putBoolean("isRegister", Constants.isSuccess);
		        		editor.putString("SID", Constants.SID);
		        		editor.putString("real_version", Constants.realVersion);
		        		if(Constants.user!=null){
		        			
			        		editor.putInt("gold", Constants.user.coinCount);
			        		editor.putInt("exp", Constants.user.exp);
			        		editor.putInt("usr_id", Constants.user.userId);
			        		editor.putInt("lv", Constants.user.lv);
			        		editor.putString("name", Constants.user.u_nick);
			        		editor.putString("url", Constants.user.u_iconUrl);
			        		editor.putString("city", Constants.user.city);
			        		editor.putString("province", Constants.user.province);
			        		editor.putInt("usr_gender", Constants.user.u_gender);
			        		editor.putInt("locationCode", Constants.user.locationCode);
			        		if(Constants.user.currentAnimal!=null){
			        			editor.putString("job", Constants.user.rank);
			        			editor.putInt("rankCode", Constants.user.rankCode);
			        			editor.putLong("a_id", Constants.user.currentAnimal.a_id);
				        		editor.putString("a_nick", Constants.user.currentAnimal.pet_nickName);
				        		editor.putString("a_race", Constants.user.currentAnimal.race);
				        		editor.putString("a_age_str", Constants.user.currentAnimal.a_age_str);
				        		editor.putString("a_url", Constants.user.currentAnimal.pet_iconUrl);
				        		editor.putInt("a_age",  Constants.user.currentAnimal.a_age);
				        		editor.putInt("a_type", Constants.user.currentAnimal.type);
				        		editor.putInt("master_id", Constants.user.currentAnimal.master_id);
			        		}
			        		
			        		
			        		
		        		}
		        		editor.commit();
		        		for(int i=0;i<PetApplication.petApp.activityList.size();i++){
		        			Activity activity=PetApplication.petApp.activityList.get(i);
		        			if(activity!=null){
		        				if(activity instanceof HomeActivity){
		        					continue;
		        				}
		        				activity.finish();
		        			}
		        		}
		        	    this.finish();
		        	    MobclickAgent.onKillProcess(homeActivity);
		        		System.exit(0);
		    		
		    		
		    	}
		    	return super.onKeyDown(keyCode, event);
		    }
		 
		 public void getNewsNum(){
				//获取消息和活动数目
				new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								LogUtil.i("mi","用户个人中心获取用户信息");
								final int mail_count=StringUtil.getNewMessageNum(homeActivity,handler);
								homeActivity.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										
										if(mail_count!=0){
											messageNumTv.setVisibility(View.VISIBLE);
											messageNumTv.setText(""+(mail_count));
											
										}else{
											messageNumTv.setVisibility(View.INVISIBLE);
										}
									}
								});
								
							}
						}).start();
			}
		 

	      
}
