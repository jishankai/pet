package com.aidigame.hisun.pet.widget.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomePetPictureAdapter;
import com.aidigame.hisun.pet.adapter.HomeSearchListAdapter;
import com.aidigame.hisun.pet.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.blur.TopCenterImageView;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.LoginJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.service.BlurImageBroadcastReceiver;
import com.aidigame.hisun.pet.ui.AlbumPictureBackground;
import com.aidigame.hisun.pet.ui.DialogGiveSbGiftActivity1;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.NewPetKingdomActivity;
import com.aidigame.hisun.pet.ui.PlayGameActivity;
import com.aidigame.hisun.pet.ui.PopularRankListActivity;
import com.aidigame.hisun.pet.ui.ShakeActivity;
import com.aidigame.hisun.pet.ui.TakePictureBackground;
import com.aidigame.hisun.pet.ui.TouchActivity;
import com.aidigame.hisun.pet.ui.UserCardActivity;
import com.aidigame.hisun.pet.ui.UsersListActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.aidigame.hisun.pet.widget.HomeMyPet;
import com.aidigame.hisun.pet.widget.HomePetPictures;
import com.aidigame.hisun.pet.widget.PLAWaterfull;
import com.aidigame.hisun.pet.widget.ShakeSensor;
import com.aidigame.hisun.pet.widget.ShakeSensor.OnShakeLisener;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.umeng.analytics.MobclickAgent;

public class DiscoveryFragment extends Fragment implements OnClickListener{
	
	public View menuView,popupParent;//主界面,弹出框所在位置相关父控件
    HomeActivity homeActivity;
    public static DiscoveryFragment homeFragment;
    HandleHttpConnectionException handleHttpConnectionException;
    
    TextView randomTv,favoriteTv,squareTv;//推荐，关注，广场
	public ViewPager viewPager;//列表界面，三个图片列表
	public ArrayList<View> viewList;//viewPager中加载的所有view
	HomeViewPagerAdapter homeViewPagerAdapter;//viewPager的适配器
	RelativeLayout waterFullParent;
	PullToRefreshAndMoreView focusListView;//显示关注列表，应该将其提取出来，单独处理
	LinearLayout camera_album;//显示获取照片界面
	public LinearLayout progressLayout;//显示加载进度条界面
	public ShowProgress showProgress;
   public static String head_last_str,foot_last_str;
   RelativeLayout relativeLayout1,relativeLayout2,relativeLayout_control1;
  public RelativeLayout black_layout;
   public static RelativeLayout homeRelativeLayout;
   public static int currentPosition=0;/*//0 我的萌星；1 最新萌照；2梦醒推荐,3搜索界面
*/   LinearLayout blur_view;
   
   public PLAWaterfull plaWaterfull2;
  
   
   TextView spinnerTv,searchOrCancelTv;
   EditText searchInputEt;
   ImageView searchIv,rqRankIv;
   LinearLayout searchLayout,tabLayout;
   XListView searchListview;
   HomeSearchListAdapter homeSearchListAdapter;
   ArrayList<Animal>  searchAnimals;
   ArrayList<MyUser> searchUsers;
   PopupWindow popupWindow;
	int searchMode=1;//1,搜索萌星；2，搜索用户
   
   
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
	
	public LinearLayout view3;
	//相册例子
	 View view2;
	 HomePetPictures homePetPictures;
	public ListView listView;
	public int last_id=-1;
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
				break;
			case SHOW_FOOTER:
				showFooter=true;
				break;
			case HIDE_HEADER:
				showHeader=false;
				
				 
			        
				break;
			case HIDE_FOOTER:
				showFooter=false;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		menuView=inflater.inflate(R.layout.fragment_discovery, null);
		homeFragment=this;
		homeActivity=HomeActivity.homeActivity;
		LogUtil.i("mi", "homefragment====="+"onCreateView()");
		return menuView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		//登陆成功
		LogUtil.i("mi", "homefragment====="+"onViewCreated()");
		//TODO
//		UserStatusUtil.downLoadUserInfo(homeActivity);
		homeActivity=HomeActivity.homeActivity;
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
		LogUtil.i("mi", "homefragment====="+"initView()");
		// TODO Auto-generated method stub
        homeRelativeLayout=(RelativeLayout)menuView.findViewById(R.id.homeactivity_right_relativelayout);
        
        popupParent=menuView.findViewById(R.id.popup_parent);
		randomTv=(TextView)menuView.findViewById(R.id.button1);
		favoriteTv=(TextView)menuView.findViewById(R.id.button2);
		squareTv=(TextView)menuView.findViewById(R.id.button3);
		black_layout=(RelativeLayout)menuView.findViewById(R.id.black_layout);
		rqRankIv=(ImageView)menuView.findViewById(R.id.rq_rank_iv);
		
		
		
		  
		   
		   initSearchViews();
		   
		   
		   
		   
		   
//		focusListView=(PullToRefreshAndMoreView)menuView.findViewById(R.id.focus_picture_parent);
		camera_album=(LinearLayout)menuView.findViewById(R.id.camera_album);
		progressLayout=(LinearLayout)menuView.findViewById(R.id.progress_parent);
		showProgress=new ShowProgress(homeActivity, progressLayout);
		showProgress.progressCancel();
//		claw1=(ImageView)menuView.findViewById(R.id.imageView3);
//		claw2=(ImageView)menuView.findViewById(R.id.imageView4);
        relativeLayout2=(RelativeLayout)menuView.findViewById(R.id.relativelaout2);
        relativeLayout_control1=(RelativeLayout)menuView.findViewById(R.id.relativelayout_control1);
        viewPager=(ViewPager)menuView.findViewById(R.id.viewpager);
       
       
        view3=(LinearLayout)LayoutInflater.from(homeActivity).inflate(R.layout.widget_linearlayout,null);
        homePetPictures=new HomePetPictures(homeActivity);
       plaWaterfull2= new PLAWaterfull(homeActivity, view3,3);
       
      
       view2=homePetPictures.getView();
        viewList=new ArrayList<View>();
        viewList.add(view3);
        viewList.add(view2);
        homeViewPagerAdapter=new HomeViewPagerAdapter(viewList);
        viewPager.setAdapter(homeViewPagerAdapter);
		//标题
		randomTv.setOnClickListener(this);
		favoriteTv.setOnClickListener(this);
		squareTv.setOnClickListener(this);
		rqRankIv.setOnClickListener(this);
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
					
					
					break;
				}
				switch (arg0) {
				case 0:
					
					break;
					

				default:
					
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
		 viewPager.setCurrentItem(0);
		 changeColors(0);
	}
	/**
	 * 搜索相关View的初始化，并设定事件监听器
	 */
	boolean canSearch=false;
	boolean isSearching=false;
    long aid=-1;
    int page=0;
    String name;
	private void initSearchViews() {
		// TODO Auto-generated method stub
		 spinnerTv=(TextView)menuView.findViewById(R.id.spinnerTv);
		   searchInputEt=(EditText)menuView.findViewById(R.id.searchInputEt);
		   searchOrCancelTv=(TextView)menuView.findViewById(R.id.searchOrCancelTv);
		   searchIv=(ImageView)menuView.findViewById(R.id.search_iv);
		   searchLayout=(LinearLayout)menuView.findViewById(R.id.search_llayout);
		   tabLayout=(LinearLayout)menuView.findViewById(R.id.tab_llayout);
		   searchListview=(XListView)menuView.findViewById(R.id.listview);
		   searchListview.setSelector(new BitmapDrawable());
		   searchIv.setOnClickListener(this);
		   searchOrCancelTv.setOnClickListener(this);
		   spinnerTv.setOnClickListener(this);
		   searchInputEt.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub
						InputMethodManager m = (InputMethodManager)   
								homeActivity.getSystemService(Context.INPUT_METHOD_SERVICE);   
								m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
								
									if(isSearching){
										Toast.makeText(homeActivity, "正在搜索，请稍后", Toast.LENGTH_LONG).show();
										return true;
									}
									searchPetOrUser(false);
									aid=-1;
									page=0;
								
						return true;
				}
			});
		   searchInputEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					canSearch=true;
					searchOrCancelTv.setText("搜索");
				}else{
					canSearch=false;
					searchOrCancelTv.setText("取消");
				}
			}
		});
	       searchListview.setOnItemClickListener(new com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener() {

	    	   @Override
				public void onItemClick(PLA_AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Object o=parent.getItemAtPosition(position);
				if(o instanceof MyUser){
					MyUser u=(MyUser)o;
					if(UserCardActivity.userCardActivity!=null){
						if(PetApplication.petApp.activityList.contains(UserCardActivity.userCardActivity)){
							PetApplication.petApp.activityList.remove(UserCardActivity.userCardActivity);
						}
						UserCardActivity.userCardActivity.finish();
						UserCardActivity.userCardActivity=null;
						System.gc();
					}
					Intent intent=new Intent(homeActivity,UserCardActivity.class);
					intent.putExtra("user", u);
					homeActivity.startActivity(intent);
				}else{
					Animal a=(Animal)o;
					if(NewPetKingdomActivity.petKingdomActivity!=null){
						if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
							NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
							NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
						}
						NewPetKingdomActivity.petKingdomActivity.finish();
						NewPetKingdomActivity.petKingdomActivity=null;
					}
					Intent intent=new Intent(homeActivity,NewPetKingdomActivity.class);
					intent.putExtra("animal", a);
					homeActivity.startActivity(intent);
				}
			}
		});
	       spinnerTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 showPopupWindow1();
			}
		});
	       searchListview.setPullRefreshEnable(false);
	       searchListview.setPullLoadEnable(true);
	       searchListview.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if(searchMode==1){
					if(searchAnimals!=null&&searchAnimals.size()>0){
						aid=searchAnimals.get(searchAnimals.size()-1).a_id;
					}
					searchPetOrUser(true); 
				}else{
					page++;
					 searchPetOrUser(true); 
				}
			}
		});
	}
	boolean creatingBlurImage=false;
	boolean showSearch=false;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			if(plaWaterfull2!=null){
				plaWaterfull2.loadData();
			}else{
				plaWaterfull2=new PLAWaterfull(homeActivity, view3, 3);
			}
			viewPager.setCurrentItem(0);
			break;
		case R.id.button2:
			if(homePetPictures!=null){
				homePetPictures.refresh();
			}
			/*new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
				}
			}).start();*/
			viewPager.setCurrentItem(1);
			break;
		case R.id.button3:
			if(plaWaterfull2!=null){
				plaWaterfull2.loadData();
			}else{
				plaWaterfull2=new PLAWaterfull(homeActivity, view3, 3);
			}
			viewPager.setCurrentItem(1);
//			changeColors(R.id.button3);
			break;
		case R.id.waterfall_parent:
			LogUtil.i("me","home_right_layout点击事件");
			break;
		case R.id.search_iv:
			if(/*!showSearch*/true){
				showSearch=true;
				tabLayout.setVisibility(View.INVISIBLE);
				searchLayout.setVisibility(View.VISIBLE);
				searchIv.setVisibility(View.INVISIBLE);
				searchInputEt.setEnabled(true);
				searchInputEt.setFocusable(true);
				searchInputEt.setFocusableInTouchMode(true);
				searchInputEt.requestFocus(EditText.FOCUS_FORWARD);
				searchInputEt.setSelection(0);
				InputMethodManager im=(InputMethodManager)homeActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				
			}/*else{
				showSearch=false;
				tabLayout.setVisibility(View.VISIBLE);
				searchLayout.setVisibility(View.INVISIBLE);
				searchIv.setVisibility(View.VISIBLE);
				
				InputMethodManager im=(InputMethodManager)homeActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				if(im.isActive())
				im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}*/
			
			break;
		case R.id.searchOrCancelTv:
			
			if(canSearch){
				if(isSearching){
					Toast.makeText(homeActivity, "正在搜索，请稍后", Toast.LENGTH_LONG).show();
					return;
				}
				
				/*if(homeActivity.getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

				 {
				 //隐藏软键盘
				 homeActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				 homeActivity.getWindow().getAttributes().softInputMode=WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;
					
				 }*/
				InputMethodManager im=(InputMethodManager)homeActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				boolean flag=im.isActive();
//				im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				im.hideSoftInputFromWindow(searchInputEt.getWindowToken(), 0);
			    
			
				searchPetOrUser(false);
				aid=-1;
				page=0;
			}else{
				tabLayout.setVisibility(View.VISIBLE);
				searchLayout.setVisibility(View.INVISIBLE);
				searchIv.setVisibility(View.VISIBLE);
				viewPager.setVisibility(View.VISIBLE);
				searchListview.setVisibility(View.INVISIBLE);
				InputMethodManager im=(InputMethodManager)homeActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				boolean flag=im.isActive();
//				im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				im.hideSoftInputFromWindow(searchInputEt.getWindowToken(), 0);
			}
			break;
		case R.id.title_tv:
			if(!showSearch){
				showSearch=true;
				tabLayout.setVisibility(View.INVISIBLE);
				searchLayout.setVisibility(View.VISIBLE);
				searchIv.setVisibility(View.INVISIBLE);
				searchInputEt.setEnabled(true);
				searchInputEt.setFocusable(true);
				searchInputEt.setFocusableInTouchMode(true);
				searchInputEt.requestFocus(EditText.FOCUS_FORWARD);
				searchInputEt.setSelection(0);
				
				InputMethodManager im1=(InputMethodManager)homeActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				
					im1.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				
			}else{
				showSearch=false;
				tabLayout.setVisibility(View.VISIBLE);
				searchLayout.setVisibility(View.INVISIBLE);
				searchIv.setVisibility(View.VISIBLE);
				
				InputMethodManager im=(InputMethodManager)homeActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				if(im.isActive())
				im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				
			}
			
			
			break;
		case R.id.rq_rank_iv:
			if(PopularRankListActivity.popularRankListActivity!=null){
				PopularRankListActivity.popularRankListActivity.finish();
				PopularRankListActivity.popularRankListActivity=null;
			}
			Intent intent=new Intent(homeActivity,PopularRankListActivity.class);
			homeActivity.startActivity(intent);
			break;
		
		
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtil.i("mi", "homefragment====="+"onStart");
	}
	
	public void refresh(){
		if(currentPosition==0){
			if(plaWaterfull2!=null){
				plaWaterfull2.onRefresh();
			}
		}else{
			if(homePetPictures!=null){
				homePetPictures.refresh();
			}
		}
	}
	
	/**
	 * 搜索用户或萌星
	 */
	private void searchPetOrUser(boolean isMore) {
		// TODO Auto-generated method stub
		
	    if(!isMore){
	    	name=searchInputEt.getEditableText().toString();
			if(StringUtil.isEmpty(name)){
				if(searchMode==1){
					Toast.makeText(homeActivity, "萌星名称不能为空", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(homeActivity, "经纪人名称不能为空", Toast.LENGTH_LONG).show();
				}
				
			}
	    }else{
	    	if(StringUtil.isEmpty(name)){
				if(searchMode==1){
					Toast.makeText(homeActivity, "萌星名称不能为空", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(homeActivity, "经纪人名称不能为空", Toast.LENGTH_LONG).show();
				}
				return;
			}
	    	
	    	
	    }
	    if(showProgress!=null){
			showProgress.showProgress();
		}else{
			showProgress=new ShowProgress(homeActivity, progressLayout);
		}
	    MobclickAgent.onEvent(homeActivity, "search");
		isSearching=true;
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(searchMode==1){
					final ArrayList<Animal> animals=HttpUtil.searchUserOrPet(homeActivity,name, aid, handleHttpConnectionException.getHandler(homeActivity));
					homeActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showProgress.progressCancel();
							if(animals!=null){
								searchListview.setVisibility(View.VISIBLE);
								viewPager.setVisibility(View.INVISIBLE);
								if(aid!=-1){
									for(int i=0;i<animals.size();i++){
										searchAnimals.add(animals.get(i));
									}
								}else{
									searchAnimals=animals;
								}
								
								if(homeSearchListAdapter==null){
									homeSearchListAdapter=new HomeSearchListAdapter(homeActivity, animals, null, handler, searchMode);
									searchListview.setAdapter(homeSearchListAdapter);
								}else{
									
									homeSearchListAdapter.updateList(animals,null,searchMode);
									homeSearchListAdapter.notifyDataSetChanged();
								}
								
							}else{
								if(aid==-1)
								Toast.makeText(homeActivity, "没有搜索到名字为 "+name+" 的宠物", Toast.LENGTH_LONG).show();
							}
							searchListview.stopLoadMore();
							searchInputEt.setText("");
							canSearch=false;
							isSearching=false;
						}
					});
				}else{
					final ArrayList<MyUser> users=HttpUtil.searchUser(homeActivity,name, page, handleHttpConnectionException.getHandler(homeActivity));
					homeActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showProgress.progressCancel();
							if(users!=null){
								searchListview.setVisibility(View.VISIBLE);
								viewPager.setVisibility(View.INVISIBLE);
								if(page!=0){
									for(int i=0;i<users.size();i++){
										searchUsers.add(users.get(i));
									}
								}else{
									searchUsers=users;
								}
								
								if(homeSearchListAdapter==null){
									homeSearchListAdapter=new HomeSearchListAdapter(homeActivity, null, users, handler, searchMode);
									searchListview.setAdapter(homeSearchListAdapter);
								}else{
									
									homeSearchListAdapter.updateList(null,users,searchMode);
									homeSearchListAdapter.notifyDataSetChanged();
								}
								
							}else{
								if(page==0)
								Toast.makeText(homeActivity, "没有搜索到名字为 "+name+" 的经纪人", Toast.LENGTH_LONG).show();
							}
							searchListview.stopLoadMore();
							searchInputEt.setText("");
							canSearch=false;
							isSearching=false;
						}
					});
				}
				
			}
		}).start();
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
			/*randomTv.setBackgroundResource(R.drawable.tab_home_left_white);
			randomTv.setTextColor(getResources().getColor(R.color.orange_red));
			favoriteTv.setBackgroundResource(R.drawable.tab_home_right_white);
			favoriteTv.setTextColor(getResources().getColor(R.color.orange_red));
			squareTv.setTextColor(getResources().getColor(R.color.white));
			squareTv.setBackgroundResource(R.drawable.tab_home_middle_red);*/
			randomTv.setBackgroundResource(R.drawable.tab_home_left_white);
			randomTv.setTextColor(getResources().getColor(R.color.orange_red));
			favoriteTv.setBackgroundResource(R.drawable.tab_home_right_red);
			favoriteTv.setTextColor(getResources().getColor(R.color.white));
			squareTv.setTextColor(getResources().getColor(R.color.orange_red));
			squareTv.setBackgroundResource(R.drawable.tab_home_middle_white);
			
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
	
	

	private void showPopupWindow1() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(homeActivity).inflate(R.layout.home_search_popup, null);
	    TextView tv1=(TextView)view.findViewById(R.id.textView1);
	    TextView tv2=(TextView)view.findViewById(R.id.textView2);
		popupWindow=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAsDropDown(searchLayout, 0, 5);
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchMode=1;
				popupWindow.dismiss();
				spinnerTv.setText("萌星");
			}
		});
        tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchMode=2;
				popupWindow.dismiss();
				spinnerTv.setText("经纪人");
			}
		});
	}
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }

	

}
