package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.huanxin.ChatActivity;
import com.aidigame.hisun.pet.huanxin.CommonUtils;
import com.aidigame.hisun.pet.huanxin.Constant;
import com.aidigame.hisun.pet.huanxin.DemoHXSDKHelper;
import com.aidigame.hisun.pet.huanxin.User;
import com.aidigame.hisun.pet.huanxin.UserDao;
import com.aidigame.hisun.pet.ui.Dialog4Activity.Dialog3ActivityListener;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.fragment.BegFoodFragment;
import com.aidigame.hisun.pet.widget.fragment.DiscoveryFragment;
import com.aidigame.hisun.pet.widget.fragment.MyPetFragment;
import com.aidigame.hisun.pet.widget.fragment.UserCenterFragment;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.NotificationCompat;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;
import com.easemob.util.HanziToPinyin;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
	 protected NotificationManager notificationManager;
	 private static final int notifiId = 11;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		homeActivity=this;
		final boolean getinfo=getIntent().getBooleanExtra("getinfo", false);
		if(getinfo){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					getSIDAndUserID();
				}
			}).start();
		}
		setContentView(R.layout.activity_home_activity);
		  notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
		
		
		
		
		popupParent=findViewById(R.id.popup_parent);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		
		begFrameLayout=(FrameLayout)findViewById(R.id.fragment_framelayout_beg);
		frameLayout=(FrameLayout)findViewById(R.id.fragment_framelayout);
		
		showBegFoodFragment();
		initBottomTab();
		if(msgReceiver!=null){
			unregisterReceiver(msgReceiver);
			msgReceiver=null;
		}
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
			intentFilter.setPriority(3);
			registerReceiver(msgReceiver, intentFilter);
			
			
			
			// 注册一个离线消息的BroadcastReceiver
			 /*IntentFilter offlineMessageIntentFilter = new
			 IntentFilter(EMChatManager.getInstance()
			 .getOfflineMessageBroadcastAction());
			 registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);*/
			
		if(Constants.isSuccess){
//			getNewsNum();
			
			if(Constants.user!=null&&!DemoHXSDKHelper.getInstance().isLogined()){
				initEMChatLogin();
			}else{
				int count=EMChatManager.getInstance().getUnreadMsgsCount();
				if(count==0){
					messageNumTv.setVisibility(View.INVISIBLE);
				}else{
					messageNumTv.setVisibility(View.VISIBLE);
					messageNumTv.setText(""+count);
				}
			}
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
		 if(discoveryFragment!=null){
	        	ft.remove(discoveryFragment);
	        	
	        	discoveryFragment=null;
	        	
			}
	        if(begFoodFragment!=null){
	        	ft.remove(begFoodFragment);
	        	begFoodFragment=null;
	        }
		ft.commit();
	    System.gc();
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
        if(discoveryFragment!=null){
        	ft.remove(discoveryFragment);
        	
        	discoveryFragment=null;
        	
		}
        if(myPetFragment!=null){
        	ft.remove(myPetFragment);
        	myPetFragment=null;
        }
		
		ft.commit();
		System.gc();
		current_show=HOME_BEG_FOOD;
	}
	private void showUserCenterFragment(){
		frameLayout.setVisibility(View.GONE);
		begFrameLayout.setVisibility(View.VISIBLE);
		
		SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		Editor e=sp.edit();
		boolean guide5=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE5, true);
		if(guide5){
			guideIv2.setImageResource(R.drawable.guide5);
			guideIv2.setVisibility(View.VISIBLE);
			e.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE5, false);
			e.commit();
		}else{
			guideIv2.setVisibility(View.GONE);
		}
		
		
		if(userCenterFragment==null){
			userCenterFragment=new UserCenterFragment();
		}else{
			userCenterFragment.updatateInfo(true);;
		}
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.fragment_framelayout_beg, userCenterFragment, "HOME_USER_CENTER");
		ft.commit();
		System.gc();
		current_show=HOME_USER_CENTER;
	}
	/**
	 * 
	 */
	private void showDiscoveryPetFragment() {
		// TODO Auto-generated method stub
		begFrameLayout.setVisibility(View.GONE);
		frameLayout.setVisibility(View.VISIBLE);
		
		SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		Editor e=sp.edit();
		boolean guide6=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE6, true);
		if(guide6){
			guideIv2.setImageResource(R.drawable.guide6);
			guideIv2.setVisibility(View.VISIBLE);
			e.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE6, false);
			e.commit();
		}else{
			guideIv2.setVisibility(View.GONE);
		}
		
		
		if(discoveryFragment==null){
			discoveryFragment=new DiscoveryFragment();
		}
		FragmentManager  fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.fragment_framelayout, discoveryFragment, "HOME_DISCORY");
		 if(myPetFragment!=null){
	        	ft.remove(myPetFragment);
	        	
	        	myPetFragment=null;
	        	
			}
	        if(begFoodFragment!=null){
	        	ft.remove(begFoodFragment);
	        	begFoodFragment=null;
	        }
		ft.commit();
		System.gc();
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
		/*if(Constants.isSuccess){
			getNewsNum();
		}*/
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
				if(myPetFragment!=null&&myPetFragment.homeMyPet!=null){
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
	
	
	public void getSIDAndUserID(){
		String SID=HttpUtil.getSID(this,handler);
		if("repate".equals(SID)){
			Intent intent=new Intent(this,Dialog4Activity.class);
			
			Dialog4Activity.listener=new Dialog3ActivityListener() {
				
				@Override
				public void onClose() {
					// TODO Auto-generated method stub
					finish();
				}
				
				@Override
				public void onButtonTwo() {
					// TODO Auto-generated method stub
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Dialog4Activity.canClose=false;
							getSIDAndUserID();
						}
					}).start();
				}
				
				@Override
				public void onButtonOne() {
					// TODO Auto-generated method stub
					finish();
				}
			};
			intent.putExtra("mode", 5);
			startActivity(intent);
			
			return;
		}
		
		if(!StringUtil.isEmpty(SID)){
			Constants.SID=SID;
			
		}else{
			boolean flag=HttpUtil.login(this,handler);
			if(flag){
				SID=HttpUtil.getSID(this,handler);
			}
			
		}
		
		//TODO 版本更新
		
		if(!StringUtil.isEmpty(Constants.CON_VERSION)&&!"1.0".equals(Constants.CON_VERSION)){
			
		}
			String version=StringUtil.getAPKVersionName(this);
			if(Constants.realVersion!=null&&StringUtil.canUpdate(this, Constants.realVersion)){
				Intent intent=new Intent(HomeActivity.this,UpdateAPKActivity.class);
				HomeActivity.this.startActivity(intent);
			}
		
		
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
//			getNewsNum();
			
			if(Constants.user!=null&&!DemoHXSDKHelper.getInstance().isLogined()){
//				initEMChatLogin();
			}else if(DemoHXSDKHelper.getInstance().isLogined()){
				int count=EMChatManager.getInstance().getUnreadMsgsCount();
				if(count==0){
					messageNumTv.setVisibility(View.INVISIBLE);
				}else{
					messageNumTv.setVisibility(View.VISIBLE);
					messageNumTv.setText(""+count);
				}
			}
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
		        		homeActivity=null;
		        	    this.finish();
		        	   EMChatOptions chatOptions = EMChatManager.getInstance().getChatOptions();
		        		if (!chatOptions.getNotificationEnable()) { 
		        	 try {
		       		 unregisterReceiver(offlineMessageReceiver);
		       		 } catch (Exception e) {
		       		 }
		        		}  
		        	    
		        	    MobclickAgent.onKillProcess(homeActivity);
//		        		System.exit(0);
		    		
		    		
		    	}
		    	return super.onKeyDown(keyCode, event);
		    }
		 
		 
		 
		/* public void getNewsNum(){
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
			}*/
		 /**
		  * 环信登陆
		  */private NewMessageBroadcastReceiver msgReceiver;
		 			public  void initEMChatLogin() {
		 				// TODO Auto-generated method stub
		 			// 注册一个接收消息的BroadcastReceiver
		 				
		 				
		 				EMChatManager.getInstance().login(""+Constants.user.userId, Constants.user.code, new EMCallBack() {

		 					@Override
		 					public void onSuccess() {
		 						//umeng自定义事件，开发者可以把这个删掉
//		 						loginSuccess2Umeng(start);
		 						
		 						/*if (!progressShow) {
		 							return;
		 						}*/
		 						// 登陆成功，保存用户名密码
		 						PetApplication.setUserName(""+Constants.user.userId);
		 						PetApplication.setPassword(Constants.user.code);
		 						/*runOnUiThread(new Runnable() {
		 							public void run() {
		 								pd.setMessage("正在获取好友和群聊列表...");
		 							}
		 						});*/
		 						try {
		 							// ** 第一次登录或者之前logout后，加载所有本地群和回话
		 							// ** manually load all local groups and
		 							// conversations in case we are auto login
		 							EMGroupManager.getInstance().loadAllGroups();
		 							EMChatManager.getInstance().loadAllConversations();

		 							// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
		 							List<String> usernames = EMContactManager.getInstance().getContactUserNames();
		 							EMLog.d("roster", "contacts size: " + usernames.size());
		 							Map<String, User> userlist = new HashMap<String, User>();
		 							for (String username : usernames) {
		 								User user = new User();
		 								user.setUsername(username);
		 								setUserHearder(username, user);
		 								userlist.put(username, user);
		 							}
		 							// 添加user"申请与通知"
		 							User newFriends = new User();
		 							newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		 							newFriends.setNick("申请与通知");
		 							newFriends.setHeader("");
		 							userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		 							// 添加"群聊"
		 							User groupUser = new User();
		 							groupUser.setUsername(Constant.GROUP_USERNAME);
		 							groupUser.setNick("群聊");
		 							groupUser.setHeader("");
		 							userlist.put(Constant.GROUP_USERNAME, groupUser);

		 							// 存入内存
		 							PetApplication.setContactList(userlist);
		 							// 存入db
		 							UserDao dao = new UserDao(HomeActivity.this);
		 							List<User> users = new ArrayList<User>(userlist.values());
		 							dao.saveContactList(users);

		 							// 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
		 							EMGroupManager.getInstance().getGroupsFromServer();
		 						} catch (Exception e) {
		 							e.printStackTrace();
		 						}
		 						//更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
		 						boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(Constants.user.u_nick);
		 						if (/*!updatenick*/true) {
		 							EMLog.e("LoginActivity", "update current user nick fail"+updatenick);
		 						}
		 						final int count=EMChatManager.getInstance().getUnreadMsgsCount();
		 						runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										if(count==0){
				 							messageNumTv.setVisibility(View.INVISIBLE);
				 							if(userCenterFragment!=null){
				 								userCenterFragment.messageNumTv.setVisibility(View.INVISIBLE);
				 							}
				 						}else{
				 							messageNumTv.setVisibility(View.VISIBLE);
				 							messageNumTv.setText(""+count);
				 							if(userCenterFragment!=null){
				 								userCenterFragment.messageNumTv.setVisibility(View.VISIBLE);
				 								userCenterFragment.messageNumTv.setText(""+count);
				 							}
				 						}
									}
								});
		 						

		 						/*if (!LoginActivity.this.isFinishing())
		 							pd.dismiss();*/
		 						// 进入主页面
//		 						startActivity(new Intent(HomeActivity.this, MainActivity.class));
//		 						finish();
		 					}

		 					@Override
		 					public void onProgress(int progress, String status) {

		 					}

		 					@Override
		 					public void onError(final int code, final String message) {
		 						/*loginFailure2Umeng(start,code,message);

		 						if (!progressShow) {
		 							return;
		 						}
		 						runOnUiThread(new Runnable() {
		 							public void run() {
		 								pd.dismiss();
		 								Toast.makeText(getApplicationContext(), "登录失败: " + message, 0).show();

		 							}
		 						});*/
		 					}
		 					/**
		 					 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
		 					 * 
		 					 * @param username
		 					 * @param user
		 					 */
		 					protected void setUserHearder(String username, User user) {
		 						String headerName = null;
		 						if (!TextUtils.isEmpty(user.getNick())) {
		 							headerName = user.getNick();
		 						} else {
		 							headerName = user.getUsername();
		 						}
		 						if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
		 							user.setHeader("");
		 						} else if (Character.isDigit(headerName.charAt(0))) {
		 							user.setHeader("#");
		 						} else {
		 							user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
		 							char header = user.getHeader().toLowerCase().charAt(0);
		 							if (header < 'a' || header > 'z') {
		 								user.setHeader("#");
		 							}
		 						}
		 					}
		 				});
		 			}
		 			/**
		 			 * 新消息广播接收者
		 			 * 
		 			 * 
		 			 */
		 			private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		 				@Override
		 				public void onReceive(Context context, Intent intent) {
		 					// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
		 					LogUtil.i("news", "接受新消息广播");

		 					String from = intent.getStringExtra("from");
		 					// 消息id
		 					String msgId = intent.getStringExtra("msgid");
		 					EMMessage message = EMChatManager.getInstance().getMessage(msgId);
		 					// 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
		 					if (ChatActivity.activityInstance != null) {
		 						if (message.getChatType() == ChatType.GroupChat) {
		 							if (message.getTo().equals(ChatActivity.activityInstance.getToChatUsername()))
		 								return;
		 						} else {
		 							if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
		 								return;
		 						}
		 					}
		 					
		 					// 注销广播接收者，否则在ChatActivity中会收到这个广播
//		 					abortBroadcast();
		 					
		 					EMChatOptions chatOptions = EMChatManager.getInstance().getChatOptions();
		 					if(chatOptions.getNotificationEnable())
		 					notifyNewMessage(message);

		 					// 刷新bottom bar消息未读数
		 				int count=	EMChatManager.getInstance().getUnreadMsgsCount();
		 				if(count==0){
		 					messageNumTv.setVisibility(View.INVISIBLE);
		 					if(UserCenterFragment.userCenterFragment!=null){
		 						UserCenterFragment.userCenterFragment.messageNumTv.setVisibility(View.INVISIBLE);
		 				        UserCenterFragment.userCenterFragment.messageNumTv.setText(""+0);
		 					}
		 				}else{
		 					messageNumTv.setVisibility(View.VISIBLE);
		 					messageNumTv.setText(""+count);
		 					if(UserCenterFragment.userCenterFragment!=null){
		 						UserCenterFragment.userCenterFragment.messageNumTv.setVisibility(View.VISIBLE);
		 				        UserCenterFragment.userCenterFragment.messageNumTv.setText(""+count);
		 					}
		 				}

		 				}
		 			}
		 		    /**
		 		     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
		 		     * 如果不需要，注释掉即可
		 		     * @param message
		 		     */
		 		    protected void notifyNewMessage(EMMessage message) {
		 		        //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
		 		        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
		 		        if(!EasyUtils.isAppRunningForeground(this)){
		 		            return;
		 		        }
		 		        
		 		        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		 		                .setSmallIcon(getApplicationInfo().icon)
		 		                .setWhen(System.currentTimeMillis()).setAutoCancel(true);
		 		        
		 		        String ticker = CommonUtils.getMessageDigest(message, this);
		 		        if(message.getType() == Type.TXT)
		 		            ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
		 		        //设置状态栏提示
//		 		        mBuilder.setTicker(message.getFrom()+": " + ticker);
		 		       try {
						mBuilder.setTicker(message.getStringAttribute("nickname")+": " + ticker);
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

		 		        Notification notification = mBuilder.build();
		 		        notificationManager.notify(notifiId, notification);
		 		        notificationManager.cancel(notifiId);
		 		    }
		 		    
		 		    
		 		    
		 			/**
		 			 * 离线消息BroadcastReceiver sdk 登录后，服务器会推送离线消息到client，这个receiver，是通知UI
		 			 * 有哪些人发来了离线消息 UI 可以做相应的操作，比如下载用户信息
		 			 */
		 			 private BroadcastReceiver offlineMessageReceiver = new
		 			 BroadcastReceiver() {
		 			
		 			 @Override
		 			 public void onReceive(Context context, Intent intent) {
		 			 String[] users = intent.getStringArrayExtra("fromuser");
		 			 String[] groups = intent.getStringArrayExtra("fromgroup");
		 			 if (users != null) {
		 			 for (String user : users) {
		 			 System.out.println("收到user离线消息：" + user);
		 			 }
		 			 }
		 			 if (groups != null) {
		 			 for (String group : groups) {
		 			 System.out.println("收到group离线消息：" + group);
		 			 }
		 			 }
		 			 }
		 			 };
	
	      
}
