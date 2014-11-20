package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.aidigame.hisun.pet.widget.fragment.ActivityFragment;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
import com.aidigame.hisun.pet.widget.fragment.MarketFragment;
import com.aidigame.hisun.pet.widget.fragment.MenuFragment;
import com.aidigame.hisun.pet.widget.fragment.MessageFragment;
import com.aidigame.hisun.pet.widget.fragment.SetupFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;
/**
 * 主界面
 * @author scx
 *
 */
public class NewHomeActivity extends com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity{
	public static final int HOMEFRAGMENT=1;
	public static final int MARKETFRAGMENT=2;
	public static final int MESSAGEFRAGMENT=3;
	public static final int ACTIVITYFRAGMENT=4;
	public static final int SETUPFRAGMENT=5;
	public MenuFragment menuFragment;
	public HomeFragment homeFragment;
	public MessageFragment messageFragment;
	public ActivityFragment activityFragment;
	public SetupFragment setupFragment;
	public MarketFragment marketFragment;
	public static NewHomeActivity homeActivity;
	public int currentFragment=1;
	HandleHttpConnectionException handleHttpConnectionException;
	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		MobclickAgent.updateOnlineConfig(this);
		
		
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		login();
		
		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.home_menu_frame);
		homeActivity=this;
		IntentFilter filter=new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		homeActivity.registerReceiver(receiver, filter);
		initSlidingMenu();
		Intent intent=getIntent();
		if(intent.getIntExtra("mode", HOMEFRAGMENT)==MARKETFRAGMENT){
			
		}else{
			showHomeFragment();
		}
		
		if(Constants.isSuccess){
		}else{
			if(homeFragment!=null){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
					  while(homeFragment.menuView==null){
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					  }
					  runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								homeFragment.initArcView();
							}
					});
					}
				}).start();
			}
		}
	}
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	int mode=getIntent().getIntExtra("mode", -1);
		switch (mode) {
		case HOMEFRAGMENT:
			showHomeFragment(1);
			break;
		case MARKETFRAGMENT:
			showMarketFragment(1);
			break;
		case MESSAGEFRAGMENT:
			showMessageFragment(1);
			break;
		case ACTIVITYFRAGMENT:
			showActivityFragment(1);
			break;
		case SETUPFRAGMENT:
			showSetupFragment(1);
			break;
		}
    }
	public void showHomeFragment() {
		// TODO Auto-generated method stub
		if(homeFragment==null){
			homeFragment=new HomeFragment();
			homeFragment.setHomeActivity(this);
		}else{
			toggle();
		}
		
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.home_menu_frame, homeFragment,"HomeFragment");
		ft.commit();
		currentFragment=HOMEFRAGMENT;
		Intent intent=getIntent();
		if(intent!=null)intent.putExtra("mode", HOMEFRAGMENT);
	}
	public void showMessageFragment(){
		
		if(messageFragment==null){
			messageFragment=new MessageFragment();
			messageFragment.setHomeActivity(this);
		}else{
			messageFragment.updateData();
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.home_menu_frame, messageFragment, "MessageFragment");
		ft.commit();
		toggle();
		currentFragment=MESSAGEFRAGMENT;
		Intent intent=getIntent();
		if(intent!=null)intent.putExtra("mode", MESSAGEFRAGMENT);
	}
	public void showActivityFragment(){
		if(activityFragment==null){
			activityFragment=new ActivityFragment();
			activityFragment.setHomeActivity(this);
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.home_menu_frame, activityFragment, "ActivityFragment");
		ft.commit();
		toggle();
		currentFragment=ACTIVITYFRAGMENT;
		Intent intent=getIntent();
		if(intent!=null)intent.putExtra("mode", ACTIVITYFRAGMENT);
	}
	public void showSetupFragment(){
		if(setupFragment==null){
			setupFragment=new SetupFragment();
			setupFragment.setHomeActivity(this);
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.home_menu_frame, setupFragment, "SetupFragment");
		ft.commit();
		toggle();
		currentFragment=SETUPFRAGMENT;
		Intent intent=getIntent();
		if(intent!=null)intent.putExtra("mode", SETUPFRAGMENT);
	}
	public void showMarketFragment(){
		if(marketFragment==null){
			marketFragment=new MarketFragment();
			marketFragment.setHomeActivity(this);
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.home_menu_frame, marketFragment, "SetupFragment");
		ft.commit();
		toggle();
		currentFragment=MARKETFRAGMENT;
		Intent intent=getIntent();
		if(intent!=null)intent.putExtra("mode", MARKETFRAGMENT);
	}
	public void showHomeFragment(int i) {
		// TODO Auto-generated method stub
		if(homeFragment==null){
			homeFragment=new HomeFragment();
			homeFragment.setHomeActivity(this);
		}else{
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.home_menu_frame, homeFragment,"HomeFragment");
		ft.commit();
		currentFragment=HOMEFRAGMENT;
		Intent intent=getIntent();
		if(intent!=null)intent.putExtra("mode", HOMEFRAGMENT);
	}
	public void showMessageFragment(int i){
		
		if(messageFragment==null){
			messageFragment=new MessageFragment();
			messageFragment.setHomeActivity(this);
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.home_menu_frame, messageFragment, "MessageFragment");
		ft.commit();
		currentFragment=MARKETFRAGMENT;
		Intent intent=getIntent();
		if(intent!=null)intent.putExtra("mode", MARKETFRAGMENT);
	}
	public void showActivityFragment(int i){
		if(activityFragment==null){
			activityFragment=new ActivityFragment();
			activityFragment.setHomeActivity(this);
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.home_menu_frame, activityFragment, "ActivityFragment");
		ft.commit();
		currentFragment=ACTIVITYFRAGMENT;
		Intent intent=getIntent();
		if(intent!=null)intent.putExtra("mode", ACTIVITYFRAGMENT);
	}
	public void showSetupFragment(int i){
		if(setupFragment==null){
			setupFragment=new SetupFragment();
			setupFragment.setHomeActivity(this);
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.home_menu_frame, setupFragment, "SetupFragment");
		ft.commit();
		toggle();
		currentFragment=SETUPFRAGMENT;
		Intent intent=getIntent();
		if(intent!=null)intent.putExtra("mode", SETUPFRAGMENT);
	}
	public void showMarketFragment(int i){
		
		if(marketFragment==null){
			marketFragment=new MarketFragment();
			marketFragment.setHomeActivity(this);
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.home_menu_frame, marketFragment, "SetupFragment");
		ft.commit();
		currentFragment=MARKETFRAGMENT;
		Intent intent=getIntent();
		if(intent!=null)intent.putExtra("mode", MARKETFRAGMENT);
	}

	/**
	 * 初始化滑动菜单
	 */
	private void initSlidingMenu() {
		// 设置滑动菜单的视图
		setBehindContentView(R.layout.menu_frame);
		menuFragment=new MenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame,menuFragment,"MenuFragment" ).commit();		
		
		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动阴影的图像资源
		sm.setShadowDrawable(R.drawable.shadow);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);		
	}
	int count=0;
	long lastTime=0;
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	// TODO Auto-generated method stub
	    	if(keyCode==KeyEvent.KEYCODE_BACK){
	    		 if(currentFragment!=HOMEFRAGMENT){
	    			 LogUtil.i("me", "返回键，按了两次");
//	    			 toggle();
	    			 return false;
	    		 }
	    		/*if(isShowingCameraAlbum){
	    			camera_album.setVisibility(View.INVISIBLE);
					camera_album.setClickable(false);
					blur_view.setVisibility(View.INVISIBLE);
					isShowingCameraAlbum=false;
					return false;
	    		}*/
	    			if(lastTime!=0){
	        			long temp=System.currentTimeMillis();
	        			if(temp-lastTime>2000){
	        				count=0;
	        				lastTime=temp;
	        			}
	        		}else{
	        			
	        		}
	        		if(count<1){
	        			Toast.makeText(this, "再按一次，退出程序", Toast.LENGTH_SHORT).show();
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
	        				if(activity instanceof NewHomeActivity){
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
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
		}
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			LogUtil.i("exception", "销毁HomeActivity");
			unregisterReceiver(receiver);
		}
		/**
		 * 登陆
		 */
		public void login(){
			LogUtil.i("me", "执行登陆");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					SharedPreferences sPreferences=NewHomeActivity.this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
					
					String SID=sPreferences.getString("SID", null);
				   
					if(StringUtil.isEmpty(SID)){
						/*
						 * 防止用户频繁删除安装软件，将Sid清除，每次先判断
						 */
						getSIDAndUserID();
						
					}else{
						Constants.SID=SID;
						Constants.isSuccess=sPreferences.getBoolean("isRegister", false);
						if(StringUtil.isEmpty(Constants.realVersion)){
							Constants.realVersion=sPreferences.getString("real_version", "");
						}
						if(Constants.isSuccess){
							Constants.user=new User();
							Constants.user.userId=sPreferences.getInt("usr_id", 0);
							Constants.user.u_nick=sPreferences.getString("name", "游荡的两脚兽");
							Constants.user.coinCount=sPreferences.getInt("gold", 500);
							Constants.user.lv=sPreferences.getInt("lv", 0);
							Constants.user.u_iconUrl=sPreferences.getString("url", "");
							Constants.user.city=sPreferences.getString("city", "");
							Constants.user.province=sPreferences.getString("province", "");
							Constants.user.locationCode=sPreferences.getInt("locationCode", 1000);
							Constants.user.u_gender=sPreferences.getInt("usr_gender", 1);
							Constants.user.currentAnimal=new Animal();
							Constants.user.rank=sPreferences.getString("job", "陌生人");
							Constants.user.rankCode=sPreferences.getInt("rankCode", -1);
							Constants.user.currentAnimal.a_id=sPreferences.getLong("a_id", 0);
							Constants.user.currentAnimal.race=sPreferences.getString("a_race", "");
							Constants.user.currentAnimal.a_age_str=sPreferences.getString("a_age_str", "");
							Constants.user.currentAnimal.pet_iconUrl=sPreferences.getString("a_url", "");
							Constants.user.currentAnimal.a_age=sPreferences.getInt("a_age", 0);
							Constants.user.currentAnimal.type=sPreferences.getInt("a_type", 101);
							Constants.user.currentAnimal.pet_nickName=sPreferences.getString("a_nick", "");
							Constants.user.currentAnimal.master_id=sPreferences.getInt("master_id", 0);
							
						}
						if(Constants.user==null||Constants.user.userId<=0||Constants.user.currentAnimal==null||Constants.user.currentAnimal.a_id<=0||Constants.user.coinCount<0||Constants.user.rankCode<0||Constants.user.lv<0||Constants.user.currentAnimal.master_id<=0){
							getSIDAndUserID();
						}else{
							if("-1".equals(Constants.user.rank)){
								getSIDAndUserID();
							}else{
								update();
								
								
								/*
								 * 更新用户信息
								 */
								getSIDAndUserID();
							}
						}
						
					}
					
				}
			}).start();
				
		}
		public void getSIDAndUserID(){
			String SID=HttpUtil.getSID(this,handleHttpConnectionException.getHandler(NewHomeActivity.this));
			SharedPreferences sPreferences=NewHomeActivity.this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
			if(!StringUtil.isEmpty(SID)){
				Constants.SID=SID;
				update();
				
			}else{
				boolean flag=HttpUtil.login(this,handleHttpConnectionException.getHandler(homeActivity));
				if(flag){
					SID=HttpUtil.getSID(this,handleHttpConnectionException.getHandler(NewHomeActivity.this));
					update();
				}
				
			}
			
			//TODO 版本更新
			
			if(!StringUtil.isEmpty(Constants.CON_VERSION)&&!"1.0".equals(Constants.CON_VERSION)){
				
			}
				String version=StringUtil.getAPKVersionName(homeActivity);
				if(Constants.realVersion!=null&&StringUtil.canUpdate(homeActivity, Constants.realVersion)){
					Intent intent=new Intent(NewHomeActivity.this,UpdateAPKActivity.class);
					NewHomeActivity.this.startActivity(intent);
				}
			
			
		}
		/**
		 * 用户信息下载成功
		 */
		public void update(){
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(HomeFragment.homeFragment!=null)HomeFragment.homeFragment.initArcView();
//					if(MenuFragment.menuFragment!=null)MenuFragment.menuFragment.setViews();
				}
			});
		}
		public BroadcastReceiver receiver=new BroadcastReceiver(){
			public void onReceive(Context context, android.content.Intent intent) {
				ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo wifiInfo=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo mobileInfo=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				//注意wifiInfo与mobileInfo可能为空
				if(mobileInfo!=null&&mobileInfo.isConnected()){
					//连接网络
//					login();
				}else if(wifiInfo!=null&&wifiInfo.isConnected()){
					//连接网络
//					login();
				}else{
					if(ShowDialog.count==0){
						ShowDialog.show(Constants.NOTE_MESSAGE_2, homeActivity);
					}
				}
				/*if(!mobileInfo.isConnected()&&!wifiInfo.isConnected()){
					//未连接网络
				}else{
					//连接网络
				}*/
			};
		};
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
		   }
}
