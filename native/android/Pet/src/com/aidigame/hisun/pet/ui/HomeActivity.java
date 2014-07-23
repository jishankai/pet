package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.blur.ScrollableImageView;
import com.aidigame.hisun.pet.blur.TopCenterImageView;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.service.DownloadImagesAsyncTask;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.aidigame.hisun.pet.widget.ShowFocusTopics;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.ShowWaterFull1;
import com.aviary.android.feather.widget.ScrollerRunnable.ScrollableView;
/**
 * 主界面
 * @author scx
 *
 */
public class HomeActivity extends Activity implements OnClickListener{
	TextView randomTv,favoriteTv;
	Button hostBt;
//	LinearLayout linearLayout1;
	LinearLayout waterFullParent;
	public ShowWaterFull1 showWaterFull1;
	LinearLayout camera_album;
	public LinearLayout progressLayout;
	ShowProgress showProgress;
   public static String head_last_str,foot_last_str;
   RelativeLayout relativeLayout1,relativeLayout2,relativeLayout_control1;
   public static RelativeLayout homeRelativeLayout;
//  public ScrollableImageView relativeLayout_control1;
   LinearLayout linearLayout1,linearLayout2;
   TextView head_last_update_time,foot_last_update_time;
   TopCenterImageView blur_view;
  public static  boolean showFooter=false,showHeader=false;
	public ShowFocusTopics showFocusTopics;
    boolean createShowFocusTopics=false;
	public boolean isOnRefresh=false;
	ImageView claw1,claw2;//推荐列表和关注列表有新内容时，出现
	int mode=1;// 1 当前界面显示的是推荐列表；2 当前界面显示的是关注列表
	public static int COMPLETE=0;
	public static HomeActivity homeActivity;
	public  ArrayList<UserImagesJson.Data> datas;
	public ArrayList<User> users=new ArrayList<User>();
	//下载完一张图片
	public  static final int MESSAGE_DOWNLOAD_IMAGE=2;
	//更新瀑布流
	public  static final int UPDATE_WATERFULL=201;
	public static final int DISMISS_PROGRESS=202;
	public static final int SHOW_PROGRESS=203;
	public static final int SHOW_HEADER=204;
	public static final int SHOW_FOOTER=205;
	public static final int HIDE_HEADER=206;
	public static final int HIDE_FOOTER=207;
	public static final int SHOW_BACKGROUND_CONTROL=208;
	public static final int HIDE_BACKGROUND_CONTROL=209;
	public ListView listView;
	public int last_id=-1;
	DownloadImagesAsyncTask asyncTask;
	ShowTopicsAdapter showTopicsAdapter;
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==COMPLETE){
				homeActivity.finish();
			}
			switch (msg.what) {
			case Constants.MESSAGE_DOWNLOAD_IMAGES_LIST:
				final UserImagesJson json=(UserImagesJson)msg.obj;
				asyncTask=new DownloadImagesAsyncTask(handler,HomeActivity.this);
				UserImagesJson.Data[] arr=new UserImagesJson.Data[json.datas.size()];
				
				if(last_id==-1){
					datas.removeAll(datas);
					

					
				}
				if(isOnRefresh&&listView!=null){
					
					if(json.datas!=null&&json.datas.size()>0&&datas.size()>0){
						if(datas.get(0).img_id==json.datas.get(0).img_id){
							
							isOnRefresh=false;
							showFocusTopics.mUIHandler.sendEmptyMessage(ShowFocusTopics.WHAT_DID_REFRESH);
							return;
						}else{
							datas.removeAll(datas);
						}
					}
				}
				if(json.datas!=null){
					for(int i=0;i<json.datas.size();i++){
						if(json.datas.get(i).url==null||"".equals(json.datas.get(i).url))continue;
						HomeActivity.this.datas.add(json.datas.get(i));
						
							if(!StringUtil.judgeImageExists(Constants.Picture_Topic_Path+File.separator+json.datas.get(i).url)){
								arr[i]=json.datas.get(i);
							}else{
								json.datas.get(i).path=Constants.Picture_Topic_Path+File.separator+json.datas.get(i).url;
							}
						
					}
					//下载图片
					
//					new DownloadIconAsyncTask(handler).execute(json);
					//下载用户信息
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String url=Constants.USER_DOWNLOAD_TX;
								ArrayList<UserImagesJson.Data> datas=json.datas;
								User user=null;
								
								for(int j=0;j<datas.size();j++){
									user=new User();
									user.userId=datas.get(j).usr_id;
									if(!users.contains(user)){
										HttpUtil.otherUserInfo(datas.get(j), null,HomeActivity.this);
										if(datas.get(j).user!=null){
											users.add(datas.get(j).user);
										}
									}else{
										int i=users.indexOf(user);
										datas.get(j).user=users.get(i);
									}
//									HttpUtil.otherUserInfo(datas.get(j), null,HomeActivity.this);
									/*if(datas.get(j).user==null){
										//断网获取不到信息
										datas.remove(datas.get(j));
										continue;
									}*/
									handler.sendEmptyMessage(2);
								}
								String  str=null;
								for(int j=0;j<datas.size();j++){
									if(datas.get(j).user==null){
										//断网获取不到信息
										datas.remove(datas.get(j));
										continue;
									}
									if(!StringUtil.judgeImageExists(Constants.Picture_ICON_Path+File.separator+datas.get(j).user.iconUrl)){
										str=HttpUtil.downloadIconImage(url, datas.get(j).user.iconUrl, null,HomeActivity.this);
										if(str!=null){
											File file=new File(Constants.Picture_ICON_Path);
											if(!file.exists()){
												file.mkdirs();
											}
											datas.get(j).user.iconPath=Constants.Picture_ICON_Path+File.separator+datas.get(j).user.iconUrl;
											handler.sendEmptyMessage(UserHomepageActivity.MESSAGE_DOWNLOAD_IMAGE);
											
											LogUtil.i("m","头像下载成功："+datas.get(j).user.iconUrl);
											
										}
									}else{
										datas.get(j).user.iconPath=Constants.Picture_ICON_Path+File.separator+datas.get(j).user.iconUrl;
										handler.sendEmptyMessage(UserHomepageActivity.MESSAGE_DOWNLOAD_IMAGE);
										
									}
									
						}     
								if(last_id==-1){
									progressLayout.setClickable(false);
									if(createShowFocusTopics){
										if(showFocusTopics!=null){
										showFocusTopics.mUIHandler.sendEmptyMessage(ShowFocusTopics.WHAT_DID_LOAD_DATA);
										handler.sendEmptyMessage(DISMISS_PROGRESS);
										if(json!=null&&json.datas!=null&&json.datas.size()>0){
											last_id=json.datas.get(json.datas.size()-1).img_id;
													new Thread(new Runnable() {
														
														@Override
														public void run() {
															// TODO Auto-generated method stub
															HttpUtil.downloadUserHomepage(handler, last_id,1,HomeActivity.this);
														}
													}).start();
										}
										}
									}
									if(showFocusTopics!=null){
										showFocusTopics.mUIHandler.sendEmptyMessage(ShowFocusTopics.WHAT_DID_REFRESH);
									}
								}
								
						}
					}).start();
					asyncTask.execute(arr);
//					
					if(arr.length==0){
						if(showTopicsAdapter!=null)
							showTopicsAdapter.notifyDataSetChanged();
					}
						
						if(isOnRefresh&&listView!=null){
							isOnRefresh=false;
						}
						if(json.datas.size()<=0)return;
						if(showTopicsAdapter!=null)
					showTopicsAdapter.notifyDataSetChanged();
						
				}
				break;
			case MESSAGE_DOWNLOAD_IMAGE:
				if(showFocusTopics!=null){
					showFocusTopics.mUIHandler.sendEmptyMessage(ShowFocusTopics.WHAT_DID_MORE);
					showFocusTopics.onMore=false;
				}
					
				     
				if(showTopicsAdapter!=null)
				showTopicsAdapter.notifyDataSetChanged();
				  
				break;
			case UPDATE_WATERFULL:
				
				new ShowWaterFull1(HomeActivity.this, waterFullParent,null);
				break;
			case DISMISS_PROGRESS:
				if(showProgress!=null)
				showProgress.progressCancel();
				if(showFocusTopics!=null)
					showFocusTopics.addView();
				if(blur_view!=null)blur_view.setVisibility(View.INVISIBLE);
				break;
			case SHOW_PROGRESS:
				if(showProgress==null){
					showProgress=new ShowProgress(HomeActivity.this, Constants.NOTE_MESSAGE_1, progressLayout);
				}else{
					showProgress.showProgress();
				}
				break;
			case SHOW_HEADER:
				showHeader=true;
				handler.sendEmptyMessage(SHOW_BACKGROUND_CONTROL);
				head_last_update_time.setText("上次刷新："+head_last_str);
				linearLayout1.setVisibility(View.VISIBLE);
				break;
			case SHOW_FOOTER:
				showFooter=true;
				foot_last_update_time.setText("上次加载："+foot_last_str);
				linearLayout2.setVisibility(View.VISIBLE);
				break;
			case HIDE_HEADER:
				showHeader=false;
				long time=System.currentTimeMillis();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				head_last_str=sdf.format(new Date(time));
//				head_last_str=StringUtil.timeFormat(new Date().getTime()/1000);
				linearLayout1.setVisibility(View.GONE);
				break;
			case HIDE_FOOTER:
				showFooter=false;
				long time1=System.currentTimeMillis();
				SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				foot_last_str=sdf1.format(new Date(time1));
//				foot_last_str=StringUtil.timeFormat(new Date().getTime()/1000);
				linearLayout2.setVisibility(View.GONE);
				break;
			case SHOW_BACKGROUND_CONTROL:
				relativeLayout_control1.setVisibility(View.VISIBLE);
				break;
            case HIDE_BACKGROUND_CONTROL:
            	relativeLayout_control1.setVisibility(View.GONE);
				break;
				
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		IntentFilter filter=new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver, filter);

		//登陆成功
		UserStatusUtil.downLoadUserInfo(this);
		UiUtil.setScreenInfo(this);
		PetApplication.petApp.activityList.add(this);
		LogUtil.i("exception", "创建HomeActivity");
		setContentView(R.layout.activity_home);
		UiUtil.setWidthAndHeight(this);
		long time=System.currentTimeMillis();
		blur_view=(TopCenterImageView)findViewById(R.id.blur_view);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		head_last_str=sdf.format(new Date(time));
        foot_last_str=head_last_str;
		LogUtil.i("exception", "创建HomeActivity");
		datas=new ArrayList<UserImagesJson.Data>();
		homeActivity=this;
		initView();
		initListener();
		LogUtil.i("me", "Constants.isSuccess="+Constants.isSuccess);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		blur_view.setVisibility(View.INVISIBLE);
	}
	private void initView() {
		// TODO Auto-generated method stub
		randomTv=(TextView)findViewById(R.id.button1);
		favoriteTv=(TextView)findViewById(R.id.button2);
		hostBt=(Button)findViewById(R.id.imageView1);
		Button cameraBt=(Button)findViewById(R.id.imageView2);

		waterFullParent=(LinearLayout)findViewById(R.id.waterfall_parent);
		camera_album=(LinearLayout)findViewById(R.id.camera_album);
		progressLayout=(LinearLayout)findViewById(R.id.progress_parent);
		claw1=(ImageView)findViewById(R.id.imageView3);
		claw2=(ImageView)findViewById(R.id.imageView4);
//        relativeLayout1=(RelativeLayout)findViewById(R.id.relativelaout1);
        relativeLayout2=(RelativeLayout)findViewById(R.id.relativelaout2);
        relativeLayout_control1=(RelativeLayout)findViewById(R.id.relativelayout_control1);
//        relativeLayout_control1=(ScrollableImageView)findViewById(R.id.relativelayout_control1);
//        relativeLayout_control1.setScreenWidth(Constants.screen_width);
        
        homeRelativeLayout=(RelativeLayout)findViewById(R.id.homeactivity_relativelayout);
        linearLayout1=(LinearLayout)findViewById(R.id.linelaout1);
        linearLayout2=(LinearLayout)findViewById(R.id.linelaout2);
        View headView1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.head, null);
       ((TextView) headView1.findViewById(R.id.head_tipsTextView)).setText("正在刷新");
       head_last_update_time=(TextView)headView1.findViewById(R.id.head_lastUpdatedTextView);
        View headView2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.head, null);
        ((TextView) headView2.findViewById(R.id.head_tipsTextView)).setText("正在加载");
        foot_last_update_time=(TextView)headView2.findViewById(R.id.head_lastUpdatedTextView);
        
        head_last_update_time.setText("上次刷新："+HomeActivity.foot_last_str);
        foot_last_update_time.setText("上次加载："+HomeActivity.foot_last_str);
        linearLayout2.setVisibility(View.GONE);
        linearLayout1.setVisibility(View.GONE);
        linearLayout1.addView(headView1);
        linearLayout2.addView(headView2);
        homeActivity.handler.sendEmptyMessage(HomeActivity.SHOW_PROGRESS);
		//图片瀑布流
        showWaterFull1=new ShowWaterFull1(this, waterFullParent,null);
		handler.sendEmptyMessage(HomeActivity.SHOW_PROGRESS);
		//标题
//		linearLayout1=(LinearLayout)findViewById(R.id.linearlayout_title);
//		CreateTitle createTitle=new CreateTitle(this, linearLayout1);
		randomTv.setOnClickListener(this);
		favoriteTv.setOnClickListener(this);
		hostBt.setOnClickListener(this);
		cameraBt.setOnClickListener(this);
	}
	private void initListener(){
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			
			changeColors(R.id.button1);
//			showTopicsAdapter=null;
			listView=null;
			showWaterFull1=new ShowWaterFull1(this, waterFullParent,null);
            showFocusTopics=null;
            handler.sendEmptyMessage(SHOW_BACKGROUND_CONTROL);
			break;
		case R.id.button2:
			if(!UserStatusUtil.isLoginSuccess(this)){
				Bitmap bmp=ImageUtil.getBitmapFromView(homeRelativeLayout,this);
				blur_view.setImageBitmap(bmp);
				blur_view.setAlpha(0.9342857f);//0.9342857
				blur_view.setVisibility(View.VISIBLE);
				return;
			}
			changeColors(R.id.button2);
			showFocusTopics();
			handler.sendEmptyMessage(SHOW_BACKGROUND_CONTROL);
			break;
		case R.id.imageView1:
			
			if(!UserStatusUtil.isLoginSuccess(this)){
				Bitmap bmp=ImageUtil.getBitmapFromView(homeRelativeLayout,this);
				blur_view.setImageBitmap(bmp);
				blur_view.setAlpha(0.9342857f);//0.9342857
				blur_view.setVisibility(View.VISIBLE);
				return;
			}
			
			Intent intent=new Intent(this,MenuActivity.class);
			this.startActivity(intent);
			overridePendingTransition(R.anim.anim_menu_activity, 0);
			
			break;
		case R.id.imageView2:
			Bitmap bmp1=ImageUtil.getBitmapFromView(homeRelativeLayout,this);
			blur_view.setImageBitmap(bmp1);
			blur_view.setAlpha(0.9342857f);//0.9342857
			blur_view.setVisibility(View.VISIBLE);
			if(!UserStatusUtil.isLoginSuccess(this)){
				
				return;
			}
			//使用系统相机
			showCameraAlbum();
			break;
		}
	}
	/**
	 * 关注列表
	 */
	private void showFocusTopics() {
		// TODO Auto-generated method stub
		/*Bitmap bmp1=ImageUtil.getBitmapFromView(homeRelativeLayout,this);
		blur_view.setImageBitmap(bmp1);
		blur_view.setAlpha(0.9342857f);//0.9342857
		blur_view.setVisibility(View.VISIBLE);*/
		last_id=-1;
		progressLayout.setClickable(true);
		createShowFocusTopics=true;
		handler.sendEmptyMessage(SHOW_PROGRESS);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.downloadUserHomepage(handler, last_id,1,HomeActivity.this);
			}
		}).start();
		datas=new ArrayList<UserImagesJson.Data>();
		showFocusTopics=new ShowFocusTopics(this, waterFullParent,datas);
		showFocusTopics.addView();
		showTopicsAdapter=showFocusTopics.getAdapter();
		listView=showFocusTopics.getListView();
	}
	boolean isShowingCameraAlbum=false;
	private void showCameraAlbum() {
		// TODO Auto-generated method stub
		/*Bitmap bmp=ImageUtil.getBitmapFromView(homeRelativeLayout,this);
		blur_view.setImageBitmap(bmp);
		blur_view.setAlpha(0.9342857f);//0.9342857
		blur_view.setVisibility(View.VISIBLE);*/
		isShowingCameraAlbum=true;
		long l1=System.currentTimeMillis();
//		blur_view.setImageBitmap(ImageUtil.fastblur(bmp, 12));
		LogUtil.i("scroll",""+( System.currentTimeMillis()-l1));
		final View view=LayoutInflater.from(this).inflate(R.layout.popup_user_homepage, null);
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
		view.clearAnimation();
		view.setAnimation(animation);
		animation.start();
		
		camera_album.removeAllViews();
		camera_album.addView(view);
		camera_album.setVisibility(View.VISIBLE);
		camera_album.setClickable(true);
		TextView camera=(TextView)view.findViewById(R.id.textView1);
		TextView album=(TextView)view.findViewById(R.id.textView2);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(HomeActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				HomeActivity.this.startActivity(intent2);
				/*Animation animation=AnimationUtils.loadAnimation(HomeActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {*/
						// TODO Auto-generated method stub
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
						blur_view.setVisibility(View.INVISIBLE);
						isShowingCameraAlbum=false;
					/*}
				}, 1000);*/
				
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(HomeActivity.this,AlbumPictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				HomeActivity.this.startActivity(intent2);
				/*Animation animation=AnimationUtils.loadAnimation(HomeActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {*/
						// TODO Auto-generated method stub
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
						blur_view.setVisibility(View.INVISIBLE);
						isShowingCameraAlbum=false;
					/*}
				}, 1000);*/
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation=AnimationUtils.loadAnimation(HomeActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
						blur_view.setVisibility(View.INVISIBLE);
						isShowingCameraAlbum=false;
					}
				}, 1000);
				
			}
		});
	}
	private void changeColors(int id){
		switch (id) {
		case R.id.button1:
			randomTv.setBackgroundColor(getResources().getColor(R.color.white));
			randomTv.setTextColor(getResources().getColor(R.color.orange_red));
			favoriteTv.setBackgroundColor(getResources().getColor(R.color.orange_red));
			favoriteTv.setTextColor(getResources().getColor(R.color.white));
			break;
		case R.id.button2:
			favoriteTv.setBackgroundColor(getResources().getColor(R.color.white));
			favoriteTv.setTextColor(getResources().getColor(R.color.orange_red));
			randomTv.setBackgroundColor(getResources().getColor(R.color.orange_red));
			randomTv.setTextColor(getResources().getColor(R.color.white));
			break;
		}
	}
	int count=0;
	long lastTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		if(isShowingCameraAlbum){
    			camera_album.setVisibility(View.INVISIBLE);
				camera_album.setClickable(false);
				blur_view.setVisibility(View.INVISIBLE);
				isShowingCameraAlbum=false;
				return false;
    		}
    			if(lastTime!=0){
        			long temp=System.currentTimeMillis();
        			if(temp-lastTime>20000){
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
        		this.finish();
        		System.exit(1);
    		
    		
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
	public void showBlurImage(){
		Bitmap bmp=ImageUtil.getBitmapFromView(homeRelativeLayout,this);
		blur_view.setImageBitmap(bmp);
		blur_view.setAlpha(0.9342857f);//0.9342857
		blur_view.setVisibility(View.VISIBLE);
	}

	/**
	 * 登陆
	 */
	public void login(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.login(HomeActivity.this,handler);
			}
		}).start();
			
	}
	
	public BroadcastReceiver receiver=new BroadcastReceiver(){
		public void onReceive(Context context, android.content.Intent intent) {
			ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileInfo=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			//注意wifiInfo与mobileInfo可能为空
			if(mobileInfo!=null&&mobileInfo.isConnected()){
				//连接网络
//				login();
			}else if(wifiInfo!=null&&wifiInfo.isConnected()){
				//连接网络
//				login();
			}else{
				if(ShowDialog.count==0){
					ShowDialog.show(Constants.NOTE_MESSAGE_2, HomeActivity.this);
				}
			}
			/*if(!mobileInfo.isConnected()&&!wifiInfo.isConnected()){
				//未连接网络
			}else{
				//连接网络
			}*/
		};
	};
	
	
	
	
	
	

}
