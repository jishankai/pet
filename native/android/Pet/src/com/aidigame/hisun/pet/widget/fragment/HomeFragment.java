package com.aidigame.hisun.pet.widget.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.blur.TopCenterImageView;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.service.BlurImageBroadcastReceiver;
import com.aidigame.hisun.pet.ui.AlbumPictureBackground;
import com.aidigame.hisun.pet.ui.BiteActivity;
import com.aidigame.hisun.pet.ui.DialogGiveSbGiftActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.PlayGameActivity;
import com.aidigame.hisun.pet.ui.ShakeActivity;
import com.aidigame.hisun.pet.ui.TakePictureBackground;
import com.aidigame.hisun.pet.ui.TouchActivity;
import com.aidigame.hisun.pet.ui.UsersListActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.aidigame.hisun.pet.widget.PLAWaterfull;
import com.aidigame.hisun.pet.widget.ShakeSensor;
import com.aidigame.hisun.pet.widget.ShakeSensor.OnShakeLisener;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.fragment.ClawStyleFunction.ClawFunctionChoseListener;

public class HomeFragment extends Fragment implements OnClickListener{
	
	public View menuView,popupParent;//主界面,弹出框所在位置相关父控件
    NewHomeActivity homeActivity;
    public static Bitmap blurBitmap;//毛玻璃图片
    public static HomeFragment homeFragment;
    public Button cameraBt;
    
    HandleHttpConnectionException handleHttpConnectionException;
    
    TextView randomTv,favoriteTv,squareTv;//推荐，关注，广场
	public ViewPager viewPager;//列表界面，三个图片列表
	ArrayList<View> viewList;//viewPager中加载的所有view
	HomeViewPagerAdapter homeViewPagerAdapter;//viewPager的适配器
	Button hostBt;//打开左侧抽屉按钮
	RelativeLayout waterFullParent;
	PullToRefreshAndMoreView focusListView;//显示关注列表，应该将其提取出来，单独处理
	LinearLayout camera_album;//显示获取照片界面
	public LinearLayout progressLayout;//显示加载进度条界面
	ShowProgress showProgress;
   public static String head_last_str,foot_last_str;
   RelativeLayout relativeLayout1,relativeLayout2,relativeLayout_control1;
  public RelativeLayout black_layout;
   public static RelativeLayout homeRelativeLayout;
   public static int currentPosition=0;//0 推荐；1 关注；2广场
   LinearLayout linearLayout1,linearLayout2;
   TextView head_last_update_time,foot_last_update_time;
   LinearLayout blur_view;
   
   PLAWaterfull plaWaterfull1,plaWaterfull2;
  
   
   
  public static  boolean showFooter=false,showHeader=false;
    boolean createShowFocusTopics=false;
	public boolean isOnRefresh=false;
	
	public static int COMPLETE=0;
	

	public ArrayList<PetPicture> petPictures;
	public ArrayList<Animal> animals=new ArrayList<Animal>();
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
	
	LinearLayout view3,view1;
	 PullToRefreshAndMoreView view2;
	public ListView listView;
	public int last_id=-1;
	ShowTopicsAdapter showTopicsAdapter;
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==COMPLETE){
				homeActivity.finish();
			}
			switch (msg.what) {
			case Constants.MESSAGE_DOWNLOAD_IMAGES_LIST:
				break;
			case MESSAGE_DOWNLOAD_IMAGE:
				break;
			case UPDATE_WATERFULL:
				/*if(msg.arg1==2){
					new ShowWaterFull1(homeActivity, view1, null,2);
				}else{
					new ShowWaterFull1(homeActivity, view3, null,3);
				}*/
//				new ShowWaterFull1(homeActivity, waterFullParent,null);
				break;
			case DISMISS_PROGRESS:
				if(showProgress!=null)
				showProgress.progressCancel();
				if(blur_view!=null)blur_view.setVisibility(View.INVISIBLE);
				break;
			case SHOW_PROGRESS:
				if(showProgress==null){
					showProgress=new ShowProgress(homeActivity,  progressLayout);
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
   public void setHomeActivity(NewHomeActivity homeActivity){
	   this.homeActivity=homeActivity;
   }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		menuView=inflater.inflate(R.layout.fragment_home, null);
		homeFragment=this;
		return menuView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		//登陆成功
		//TODO
//		UserStatusUtil.downLoadUserInfo(homeActivity);
		
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		
		
		LogUtil.i("exception", "创建HomeActivity");
		long time=System.currentTimeMillis();
		blur_view=(LinearLayout)menuView.findViewById(R.id.blur_view);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		head_last_str=sdf.format(new Date(time));
        foot_last_str=head_last_str;
		LogUtil.i("exception", "创建HomeActivity");
		petPictures=new ArrayList<PetPicture>();
		
		initView();
		LogUtil.i("me", "Constants.isSuccess="+Constants.isSuccess);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		blur_view.setVisibility(View.INVISIBLE);
	}
	public void setBlureViewInvisible(){
		blur_view.setVisibility(View.INVISIBLE);
	}
	public void setNewWaterFull(){
		
	}
	boolean isFirstClaw=true;
	private void initView() {
		// TODO Auto-generated method stub
        homeRelativeLayout=(RelativeLayout)menuView.findViewById(R.id.homeactivity_right_relativelayout);
        
        popupParent=menuView.findViewById(R.id.popup_parent);
		randomTv=(TextView)menuView.findViewById(R.id.button1);
		favoriteTv=(TextView)menuView.findViewById(R.id.button2);
		squareTv=(TextView)menuView.findViewById(R.id.button3);
		hostBt=(Button)menuView.findViewById(R.id.imageView1);
		black_layout=(RelativeLayout)menuView.findViewById(R.id.black_layout);
		
		cameraBt=(Button)menuView.findViewById(R.id.imageView2);
        
		
		//添加底部爪型弹出按钮
		if(!isFirstClaw){
			initArcView();
		}else{
			isFirstClaw=false;
		}
		
		
		
		
//		focusListView=(PullToRefreshAndMoreView)menuView.findViewById(R.id.focus_picture_parent);
		camera_album=(LinearLayout)menuView.findViewById(R.id.camera_album);
		progressLayout=(LinearLayout)menuView.findViewById(R.id.progress_parent);
//		claw1=(ImageView)menuView.findViewById(R.id.imageView3);
//		claw2=(ImageView)menuView.findViewById(R.id.imageView4);
        relativeLayout2=(RelativeLayout)menuView.findViewById(R.id.relativelaout2);
        relativeLayout_control1=(RelativeLayout)menuView.findViewById(R.id.relativelayout_control1);
        viewPager=(ViewPager)menuView.findViewById(R.id.viewpager);
        view1=(LinearLayout)LayoutInflater.from(homeActivity).inflate(R.layout.widget_linearlayout,null);
        view2=(PullToRefreshAndMoreView)LayoutInflater.from(homeActivity).inflate(R.layout.widget_pulltorefreshandmoreview, null);
        view3=(LinearLayout)LayoutInflater.from(homeActivity).inflate(R.layout.widget_linearlayout,null);
       plaWaterfull1= new PLAWaterfull(homeActivity, view1,2);
       plaWaterfull2= new PLAWaterfull(homeActivity, view3,3);
        
        view2.setPadding(0, homeActivity.getResources().getDimensionPixelSize(R.dimen.view_padding), 0, 0);
        focusListView=view2;
        showFocusTopics();
        viewList=new ArrayList<View>();
        viewList.add(view1);
        
        viewList.add(view3);
        viewList.add(view2);
        homeViewPagerAdapter=new HomeViewPagerAdapter(viewList);
        viewPager.setAdapter(homeViewPagerAdapter);
//        int page=homeActivity.getIntent().getIntExtra("page", 0);
//        viewPager.setCurrentItem(page);
        
        linearLayout1=(LinearLayout)menuView.findViewById(R.id.linelaout1);
        linearLayout2=(LinearLayout)menuView.findViewById(R.id.linelaout2);
        View headView1 = (LinearLayout) LayoutInflater.from(homeActivity).inflate(R.layout.head, null);
       ((TextView) headView1.findViewById(R.id.head_tipsTextView)).setText("正在刷新");
       head_last_update_time=(TextView)headView1.findViewById(R.id.head_lastUpdatedTextView);
        View headView2 = (LinearLayout) LayoutInflater.from(homeActivity).inflate(R.layout.head, null);
        ((TextView) headView2.findViewById(R.id.head_tipsTextView)).setText("正在加载");
        foot_last_update_time=(TextView)headView2.findViewById(R.id.head_lastUpdatedTextView);
        
        head_last_update_time.setText("上次刷新："+foot_last_str);
        foot_last_update_time.setText("上次加载："+foot_last_str);
        linearLayout2.setVisibility(View.GONE);
        linearLayout1.setVisibility(View.GONE);
        linearLayout1.addView(headView1);
        linearLayout2.addView(headView2);
//        handler.sendEmptyMessage(SHOW_PROGRESS);
//		
//		handler.sendEmptyMessage(SHOW_PROGRESS);
		//标题
		randomTv.setOnClickListener(this);
		favoriteTv.setOnClickListener(this);
		squareTv.setOnClickListener(this);
		hostBt.setOnClickListener(this);
		cameraBt.setOnClickListener(this);
		
		/*waterFullParent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});*/
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				LogUtil.i("scroll", "viewpager=====onPageSelected");
				currentPosition=arg0;
				changeColors(arg0);
				switch (arg0) {
				case 0:
					
					break;
				case 1:
					
					break;
				case 2:
					
					
					if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
						
					/*	if(blurBitmap!=null){
							blur_view.setImageBitmap(blurBitmap);
							blur_view.setAlpha(0.9342857f);//0.9342857
							blur_view.setVisibility(View.VISIBLE);
						}else{
							Bitmap bmp1=ImageUtil.getBitmapFromView(homeRelativeLayout,homeActivity);
							blur_view.setImageBitmap(bmp1);
							blur_view.setAlpha(0.9342857f);//0.9342857
							blur_view.setVisibility(View.VISIBLE);
							blurBitmap=bmp1;
						}*/
					}
					break;
				}
				switch (arg0) {
				case 0:
					homeActivity.getSlidingMenu().setTouchModeAbove(com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
					

				default:
					homeActivity.getSlidingMenu().setTouchModeAbove(com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				LogUtil.i("scroll", "viewpager=====onPageScrolled");
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				LogUtil.i("scroll", "viewpager=====onPageScrollStateChanged");
				if(currentPosition!=arg0){
					currentPosition=viewPager.getCurrentItem();
					changeColors(currentPosition);
				}
			}
		});
		//截取毛玻璃图片保存在本地
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(10000);
					
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							blurBitmap=ImageUtil.getBitmapFromView(viewList.get(0),homeActivity);
						}
					});
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		})/*.start()*/;
//		judgeAcountType();
	}
	/**
	 * 底部四个功能按钮
	 */
	ClawStyleFunction csf;
	public void initArcView() {
		// TODO Auto-generated method stub
		waterFullParent=(RelativeLayout)menuView.findViewById(R.id.waterfall_parent);
		final RelativeLayout claw_layout=(RelativeLayout)menuView.findViewById(R.id.claw_layout);
		if(Constants.isSuccess){
			
			if(Constants.user!=null&&Constants.user.currentAnimal!=null){
				
							Constants.user.currentAnimal.hasJoinOrCreate=true;
							if(Constants.user.currentAnimal.master_id==Constants.user.userId){
								csf=new ClawStyleFunction(homeActivity,true,true);
								cameraBt.setVisibility(View.VISIBLE);
							}else{
								csf=new ClawStyleFunction(homeActivity,false,true);
								cameraBt.setVisibility(View.INVISIBLE);
							}
							
							View arcView=csf.getView();
							arcView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
							claw_layout.addView(arcView);
							csf.setClawFunctionChoseListener(new ClawFunctionChoseListener() {
								
								@Override
								public void clickItem4() {
									// TODO Auto-generated method stub
									if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
//									    setBlurImageBackground();
										return ;
									}
									Intent intent=new Intent(homeActivity,PlayGameActivity.class);
									intent.putExtra("animal", Constants.user.currentAnimal);
									homeActivity.startActivity(intent);
								}
								
								@Override
								public void clickItem3() {
									// TODO Auto-generated method stub
									if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
//									    setBlurImageBackground();
										return ;
									}
									if(Constants.user.currentAnimal.master_id==Constants.user.userId){
										Intent intent=new Intent(homeActivity,BiteActivity.class);
										intent.putExtra("animal", Constants.user.currentAnimal);
										homeActivity.startActivity(intent);
									}else{
										Intent intent=new Intent(homeActivity,TouchActivity.class);
										intent.putExtra("animal", Constants.user.currentAnimal);
										homeActivity.startActivity(intent);
									}
									
								}
								
								@Override
								public void clickItem2() {
									// TODO Auto-generated method stub
									if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
//									    setBlurImageBackground();
										return ;
									}
									/*DialogGiveSbGift dgb=new DialogGiveSbGift(homeActivity,Constants.user.currentAnimal);
									final AlertDialog dialog=new AlertDialog.Builder(homeActivity).setView(dgb.getView())
											.show();*/
									if(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.finish();
									Intent intent=new Intent(homeActivity,DialogGiveSbGiftActivity.class);
									intent.putExtra("animal", Constants.user.currentAnimal);
									homeActivity.startActivity(intent);
									DialogGiveSbGiftActivity dgb=DialogGiveSbGiftActivity.dialogGiveSbGiftActivity;
									DialogGiveSbGiftActivity.dialogGoListener=new DialogGiveSbGiftActivity.DialogGoListener() {
										
										@Override
										public void toDo() {
											// TODO Auto-generated method stub
											
											Intent intent=null;
//											homeActivity.showMarketFragment(1);
											DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.finish();
											
											NewHomeActivity.homeActivity.finish();
											intent=new Intent(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity,NewHomeActivity.class);
											intent.putExtra("mode", NewHomeActivity.MARKETFRAGMENT);
											DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.startActivity(intent);
										}
										
										@Override
										public void closeDialog() {
											// TODO Auto-generated method stub
											DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.finish();
										}
										@Override
										public void lastResult(boolean isSuccess) {
											// TODO Auto-generated method stub
											
										}
										public void unRegister() {
											// TODO Auto-generated method stub
											if(!UserStatusUtil.isLoginSuccess(homeActivity, popupParent,black_layout)){
								        		
								        	};
										}
									};
								}
								
								@Override
								public void clickItem1() {
									// TODO Auto-generated method stub
									if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
//									    setBlurImageBackground();
										return ;
									}
									Intent intent=new Intent(homeActivity,ShakeActivity.class);
									intent.putExtra("animal", Constants.user.currentAnimal);
									homeActivity.startActivity(intent);
								}
							});
			}
						
		}else{
			csf=new ClawStyleFunction(homeActivity,true,true);
			View arcView=csf.getView();
			cameraBt.setVisibility(View.VISIBLE);
			arcView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
			claw_layout.addView(arcView);
			csf.setClawFunctionChoseListener(new ClawFunctionChoseListener() {
				
				@Override
				public void clickItem4() {
					// TODO Auto-generated method stub
					if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
//					    setBlurImageBackground();
						return ;
					}
					Intent intent=new Intent(homeActivity,TouchActivity.class);
					intent.putExtra("animal", Constants.user.currentAnimal);
					homeActivity.startActivity(intent);
				}
				
				@Override
				public void clickItem3() {
					// TODO Auto-generated method stub
					if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
//					    setBlurImageBackground();
						return ;
					}
					Intent intent=new Intent(homeActivity,BiteActivity.class);
					intent.putExtra("animal", Constants.user.currentAnimal);
					homeActivity.startActivity(intent);
				}
				
				@Override
				public void clickItem2() {
					// TODO Auto-generated method stub
					if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
//					    setBlurImageBackground();
						return ;
					}
					/*DialogGiveSbGift dgb=new DialogGiveSbGift(homeActivity,Constants.user.currentAnimal);
					final AlertDialog dialog=new AlertDialog.Builder(homeActivity).setView(dgb.getView())
							.show();*/
					if(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.finish();
					Intent intent=new Intent(homeActivity,DialogGiveSbGiftActivity.class);
					intent.putExtra("animal", Constants.user.currentAnimal);
					homeActivity.startActivity(intent);
					DialogGiveSbGiftActivity dgb=DialogGiveSbGiftActivity.dialogGiveSbGiftActivity;
					DialogGiveSbGiftActivity.dialogGoListener=new DialogGiveSbGiftActivity.DialogGoListener() {
						
						@Override
						public void toDo() {
							// TODO Auto-generated method stub
							
							Intent intent=null;
//							homeActivity.showMarketFragment(1);
							DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.finish();
							
							NewHomeActivity.homeActivity.finish();
							intent=new Intent(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity,NewHomeActivity.class);
							intent.putExtra("mode", NewHomeActivity.MARKETFRAGMENT);
							DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.startActivity(intent);
						}
						
						@Override
						public void closeDialog() {
							// TODO Auto-generated method stub
							DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.finish();
						}
						@Override
						public void lastResult(boolean isSuccess) {
							// TODO Auto-generated method stub
							
						}
						public void unRegister() {
							// TODO Auto-generated method stub
							if(!UserStatusUtil.isLoginSuccess(homeActivity, popupParent,black_layout)){
				        		
				        	};
						}
					};
					
				}
				
				@Override
				public void clickItem1() {
					// TODO Auto-generated method stub
					if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
//					    setBlurImageBackground();
						return ;
					}
					Intent intent=new Intent(homeActivity,ShakeActivity.class);
					intent.putExtra("animal", Constants.user.currentAnimal);
					homeActivity.startActivity(intent);
				}
			});
		}
		
		
		
	}
	boolean creatingBlurImage=false;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			if(plaWaterfull1!=null){
				plaWaterfull1.loadData();
			}else{
				plaWaterfull1=new PLAWaterfull(homeActivity, view1, 2);
			}
			viewPager.setCurrentItem(0);
			break;
		case R.id.button2:
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					refreshTopics();
				}
			}).start();
			viewPager.setCurrentItem(2);
			break;
		case R.id.button3:
			if(plaWaterfull2!=null){
				plaWaterfull2.loadData();
			}else{
				plaWaterfull2=new PLAWaterfull(homeActivity, view3, 3);
			}
			viewPager.setCurrentItem(1);
			changeColors(R.id.button3);
			break;
		case R.id.imageView1:
			if(MenuFragment.menuFragment!=null){
				MenuFragment.menuFragment.hideSearch();
				MenuFragment.menuFragment.setViews();
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					/*if(Constants.user==null){
						UserStatusUtil.downLoadUserInfo(homeActivity);
					}*/
					if(!creatingBlurImage)return;
					creatingBlurImage=true;
					Bitmap bmp=ImageUtil.getBitmapFromView(homeRelativeLayout,homeActivity);
					blurBitmap=bmp;
					Intent intent=new Intent(BlurImageBroadcastReceiver.BLUR_BITMAP_CHANGED);
					intent.putExtra("blur", bmp);
//					homeActivity.sendStickyBroadcast(intent);;
					creatingBlurImage=false;
					
					/*homeActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub

							if(!UserStatusUtil.isLoginSuccess(homeActivity)){
								
								blur_view.setImageBitmap(blurBitmap);
								blur_view.setAlpha(0.9342857f);//0.9342857
								blur_view.setVisibility(View.VISIBLE);
								
							}
							
						}
					});*/
				}
			}).start();
			
			homeActivity.toggle();
			break;
		case R.id.imageView2:
          if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
				
				return;
			}
        
			if(blurBitmap!=null){
				blur_view.setBackgroundDrawable(new BitmapDrawable(blurBitmap));
				blur_view.setAlpha(0.9342857f);//0.9342857
//				blur_view.setVisibility(View.VISIBLE);
			}else{
				Bitmap bmp1=ImageUtil.getBitmapFromView(homeRelativeLayout,homeActivity);
				blur_view.setBackgroundDrawable(new BitmapDrawable(blurBitmap));
				blur_view.setAlpha(0.9342857f);//0.9342857
//				blur_view.setVisibility(View.VISIBLE);
				blurBitmap=bmp1;
				/*Bitmap bmp1=BitmapFactory.decodeResource(getResources(), R.drawable.blur);
				blur_view.setBackgroundDrawable(new BitmapDrawable(bmp1));
				blur_view.setAlpha(0.9342857f);//0.9342857
				blur_view.setVisibility(View.VISIBLE);
				blurBitmap=bmp1;*/
				
			}
			
			
			if(Constants.user!=null&&Constants.user.currentAnimal!=null){
				if(Constants.user.userId==Constants.user.currentAnimal.master_id){
					cameraBt.setVisibility(View.VISIBLE);
				}else{
					blur_view.setVisibility(View.INVISIBLE);
					Toast.makeText(homeActivity, "只有宠物主人才可以上传照片", Toast.LENGTH_LONG).show();
					return;
				}
			}
			
			//使用系统相机
			showCameraAlbum();
			break;
		case R.id.waterfall_parent:
			LogUtil.i("me","home_right_layout点击事件");
			break;
		}
	}
	boolean isFirst=true;
	public void refreshTopics(){
		final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
		last_id=-1;
		UserImagesJson json=HttpUtil.downloadPetkingdomImages(handler, last_id,1,homeActivity,-1);
		if(json!=null){
			if(json.petPictures!=null&&json.petPictures.size()>0){
				PetPicture data=null;
				
				for(int i=0;i<json.petPictures.size();i++){
					data=json.petPictures.get(i);
					if(!temp.contains(data)){
					temp.add(i,data);
					}
				 }
				
				homeActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showTopicsAdapter.updateTopics(temp);
						petPictures=temp;
						showTopicsAdapter.notifyDataSetChanged();
						
						
					}
				});
			}
		}else{
			//TODO 下载失败
			
		}
	}
	public void refreshFocusTopics(){
		final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
		last_id=-1;
		UserImagesJson json=HttpUtil.downloadPetkingdomImages(handler, last_id,1,homeActivity,-1);
		if(json!=null){
			if(json.petPictures!=null&&json.petPictures.size()>0){
				PetPicture data=null;
				for(int i=0;i<json.petPictures.size();i++){
					data=json.petPictures.get(i);
					if(!temp.contains(data)){
					temp.add(i,data);
					}
				 }
				homeActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showTopicsAdapter.updateTopics(temp);
						petPictures=temp;
						showTopicsAdapter.notifyDataSetChanged();
						
						focusListView.onRefreshFinish();
					}
				});
				focusListView.onRefreshFinish();
			}else{
				focusListView.onRefreshFinish();
			}
		}else{
			//TODO 下载失败
			
			focusListView.onRefreshFinish();
		}
	}
	/**
	 * 关注列表
	 */
	private void showFocusTopics() {
		// TODO Auto-generated method stub

		focusListView.setVisibility(View.VISIBLE);
		
		if(isFirst){
			last_id=-1;
			isFirst=false;
			listView=focusListView.getListView();
			petPictures=new ArrayList<PetPicture>();
			showTopicsAdapter=new ShowTopicsAdapter(homeActivity, petPictures);
			listView.setAdapter(showTopicsAdapter);
			focusListView.setHeaderAndFooterInvisible();
		}
		LogUtil.i("scroll", "focusListView===="+focusListView.getWidth()+","+focusListView.getHeight()+",listView==="+listView.getWidth()+","+listView.getHeight()+",focusTop="+focusListView.getTop()+"focusBottom="+focusListView.getBottom()+",listviewTop="+listView.getTop()+"listViewBottom="+listView.getBottom());
		focusListView.setClickable(true);
		focusListView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.i("scroll", "点击focusListView");
			}
		});
		/*if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent)){
			return;
		}*/
		new Thread(new Runnable() {
        	ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				UserImagesJson json=HttpUtil.downloadPetkingdomImages(handler, last_id,1,homeActivity,-1);
				if(json!=null){
					if(json.petPictures!=null&&json.petPictures.size()>0){
						PetPicture data=null;
						
						for(int i=0;i<json.petPictures.size();i++){
							data=json.petPictures.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						 }
						
						homeActivity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								petPictures=temp;
								showTopicsAdapter.updateTopics(temp);
								showTopicsAdapter.notifyDataSetChanged();
								
								LogUtil.i("scroll","datas大小========="+petPictures.size());
								
							}
						});
					}
				}else{
					//TODO 下载失败
				}
			}
		}).start();
		
		
		focusListView.setListener(new PullToRefreshAndMoreListener() {
			ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				refreshFocusTopics();
			}
			
			@Override
			public void onMore() {
				// TODO Auto-generated method stub
				if(petPictures.size()>0){
					last_id=petPictures.get(petPictures.size()-1).img_id;
				}else{
					last_id=-1;
				}
				UserImagesJson json=HttpUtil.downloadPetkingdomImages(handler, last_id,1,homeActivity,-1);
				if(json!=null){
					if(json.datas!=null&&json.datas.size()>0){
						PetPicture data=null;
						temp=new ArrayList<PetPicture>();
						for(int i=0;i<petPictures.size();i++){
							temp.add(petPictures.get(i));
						}
						for(int i=0;i<json.petPictures.size();i++){
							data=json.petPictures.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						 }
						homeActivity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								showTopicsAdapter.updateTopics(temp);
								petPictures=temp;
								showTopicsAdapter.notifyDataSetChanged();
								focusListView.onMoreFinish();
							}
						});
						focusListView.onMoreFinish();
					}
					focusListView.onMoreFinish();
				}else{
					//TODO 下载失败
					
					focusListView.onMoreFinish();
				}
			}
		});
		
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState==SCROLL_STATE_IDLE){
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0)!=null&&listView.getChildAt(0).getBottom()==listView.getChildAt(0).getMeasuredHeight()){
						view2.setPadding(0, homeActivity.getResources().getDimensionPixelSize(R.dimen.view_padding), 0, 0);
					}else{
						view2.setPadding(0, 0, 0, 0);
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
		final View view=LayoutInflater.from(homeActivity).inflate(R.layout.popup_user_homepage, null);
		Animation animation=AnimationUtils.loadAnimation(homeActivity, R.anim.anim_translate_showtopic_addcommentlayout_in);
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
				Intent intent2=new Intent(homeActivity,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				intent2.putExtra("animal", Constants.user.currentAnimal);
				homeActivity.startActivity(intent2);
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
				Intent intent2=new Intent(homeActivity,AlbumPictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				intent2.putExtra("animal", Constants.user.currentAnimal);
				homeActivity.startActivity(intent2);
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
				Animation animation=AnimationUtils.loadAnimation(homeActivity, R.anim.anim_translate_showtopic_addcommentlayout_out);
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
				}, 300);
				
			}
		});
	}
	public void changeColors(int id){
		handler.sendEmptyMessage(HIDE_FOOTER);
		handler.sendEmptyMessage(HIDE_HEADER);
		switch (id) {
		case 0:
			randomTv.setBackgroundResource(R.drawable.tab_home_left_red);
			randomTv.setTextColor(getResources().getColor(R.color.white));
			favoriteTv.setBackgroundResource(R.drawable.tab_home_right_white);
			favoriteTv.setTextColor(getResources().getColor(R.color.orange_red));
			squareTv.setTextColor(getResources().getColor(R.color.orange_red));
			squareTv.setBackgroundResource(R.drawable.tab_home_middle_white);
			break;
		case 1:
			randomTv.setBackgroundResource(R.drawable.tab_home_left_white);
			randomTv.setTextColor(getResources().getColor(R.color.orange_red));
			favoriteTv.setBackgroundResource(R.drawable.tab_home_right_white);
			favoriteTv.setTextColor(getResources().getColor(R.color.orange_red));
			squareTv.setTextColor(getResources().getColor(R.color.white));
			squareTv.setBackgroundResource(R.drawable.tab_home_middle_red);
			
			break;
		case 2:
			randomTv.setBackgroundResource(R.drawable.tab_home_left_white);
			randomTv.setTextColor(getResources().getColor(R.color.orange_red));
			favoriteTv.setBackgroundResource(R.drawable.tab_home_right_red);
			favoriteTv.setTextColor(getResources().getColor(R.color.white));
			squareTv.setTextColor(getResources().getColor(R.color.orange_red));
			squareTv.setBackgroundResource(R.drawable.tab_home_middle_white);
			break;
		}
	}

   


	public void showBlurImage(){
		Bitmap bmp=ImageUtil.getBitmapFromView(homeRelativeLayout,homeActivity);
		blur_view.setBackgroundDrawable(new BitmapDrawable(bmp));
		blur_view.setAlpha(0.9342857f);//0.9342857
		blur_view.setVisibility(View.VISIBLE);
	}

	/**
	 * 登陆
	 *//*
	public void login(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.login(handler);
			}
		}).start();
			
	}*/
	/**
	 * 判断用户是创建王国还是认养宠物
	 */
	public void judgeAcountType(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(Constants.isSuccess){
					while(Constants.user==null||Constants.user.currentAnimal==null){
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					homeActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(Constants.user.userId==Constants.user.currentAnimal.master_id){
								cameraBt.setVisibility(View.VISIBLE);
							}else{
								cameraBt.setVisibility(View.INVISIBLE);
							}
						}
					});
					
				}
			}
		}).start();
	}
    public void setBlurImageBackground(){
    	Bitmap bmp=ImageUtil.getBitmapFromView(homeRelativeLayout,homeActivity);
    	blur_view.setBackgroundDrawable(new BitmapDrawable(bmp));
		blur_view.setAlpha(0.9342857f);//0.9342857
		blur_view.setVisibility(View.VISIBLE);
    }

	

}
