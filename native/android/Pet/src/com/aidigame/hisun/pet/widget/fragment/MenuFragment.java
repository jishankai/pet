package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.SearchPetListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.LoginJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.ChoseStarActivity;
import com.aidigame.hisun.pet.ui.InviteOthersDialogActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.PopularRankListActivity;
import com.aidigame.hisun.pet.ui.TopicListActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.HorizontalListView2;
import com.aidigame.hisun.pet.view.OneByOneGallery;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aviary.android.feather.async_tasks.DownloadImageAsyncTask.OnImageDownloadListener;
import com.aviary.android.feather.library.utils.ImageSizes;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
public class MenuFragment extends Fragment implements OnClickListener{
	ImageLoader imageLoader;
	DisplayImageOptions displayImageOptions,displayImageOptions1;
	ScrollView scrollView;
	
	public static MenuFragment menuFragment;
	RoundImageView round1,round2,round3;
	ImageView hat_king1,hat_king2,hat_king3;
	TextView rqTv;
	LinearLayout infoLayout;
	public View popup_parent;
	public RelativeLayout black_layout;
	HandleHttpConnectionException handleHttpConnectionException;
	View menuView;
	NewHomeActivity homeActivity;
	RoundImageView userIcon;
	TextView userNameTve,mailText,activityText,jobTv,levelTv;
	RelativeLayout homeLayout,marketLayout,messageLayout,activityLayout,no_pet_layout;
	public TextView goldTv;
	
	LinearLayout setupLayout;
	public static final  int USER_INFO=1;
//	ViewPager viewPager;
//	OneByOneGallery  gallery;
	HorizontalListView2 gallery;
	ImageView preIv,nextIv,messageIv,sexIv,clearInputIv;
	BaseAdapter galleryAdapter;
	ArrayList<Animal> animalList;

	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case USER_INFO:
				if(Constants.user==null){
					handler.sendEmptyMessageDelayed(USER_INFO, 100);
				}else{
					setViews();
				}
				break;
		}}};
	
	public MenuFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		homeActivity=NewHomeActivity.homeActivity;
		menuView=inflater.inflate(R.layout.fragment_menu, null);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		menuFragment=this;
		
		initView();
		return menuView;
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		homeActivity=NewHomeActivity.homeActivity;
		LogUtil.i("me", "onViewCreated(View view, Bundle savedInstanceState)");
		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtil.i("me", "onViewCreated(View view, Bun============================*********************dle savedInstanceState)");
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtil.i("me", "onViewCreated(View view, Bun============================*********************dle savedInstanceState)");
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		
		
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	boolean hasShow=false;
	private void initView() {
		// TODO Auto-generated method stub
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=4;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		displayImageOptions1=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
	            .showImageOnFail(R.drawable.user_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(Constants.isSuccess){
					while(Constants.user==null){
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					final ArrayList<Animal> temp=HttpUtil.usersKingdom(homeActivity,Constants.user, 1, handleHttpConnectionException.getHandler(homeActivity));
					homeActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(temp!=null){
								animalList=temp;
								
							    Constants.user.aniList=animalList;
							    hasShow=false;
								galleryAdapter.notifyDataSetChanged();
								if(animalList.size()>2){
									gallery.setSelection(1);
								}else if(animalList.size()>3){
									gallery.setSelection(2);
								}
							}
							setViews();
							
						}
					});
				}
				
			}
		}).start();
		
		setBlurImageBackground(); 
		popup_parent=(View)menuView.findViewById(R.id.popup_parent);
		black_layout=(RelativeLayout)menuView.findViewById(R.id.black_layout);
		userIcon=(RoundImageView)menuView.findViewById(R.id.imageView_left_3);
		userNameTve=(TextView)menuView.findViewById(R.id.textView1);
		messageLayout=(RelativeLayout)menuView.findViewById(R.id.message_linearLayout_left);
		activityLayout=(RelativeLayout)menuView.findViewById(R.id.activity_linearLayout_left);
		homeLayout=(RelativeLayout)menuView.findViewById(R.id.home_linearLayout_left);
		marketLayout=(RelativeLayout)menuView.findViewById(R.id.market_linearLayout_left);
		setupLayout=(LinearLayout)menuView.findViewById(R.id.setup_linearLayout_left);
		mailText=(TextView)menuView.findViewById(R.id.message_text);
		activityText=(TextView)menuView.findViewById(R.id.activity_text_left);
//		viewPager=(ViewPager)menuView.findViewById(R.id.menu_viewpager);
		gallery=(HorizontalListView2)menuView.findViewById(R.id.gallery);
		preIv=(ImageView)menuView.findViewById(R.id.left_viewpager);
		nextIv=(ImageView)menuView.findViewById(R.id.right_viewpager);
		rqTv=(TextView)menuView.findViewById(R.id.rq_tv);

		
		round1=(RoundImageView)menuView.findViewById(R.id.round1);
		round2=(RoundImageView)menuView.findViewById(R.id.round2);
		round3=(RoundImageView)menuView.findViewById(R.id.round3);
		hat_king1=(ImageView)menuView.findViewById(R.id.hat_king1);
		hat_king2=(ImageView)menuView.findViewById(R.id.hat_king2);
		hat_king3=(ImageView)menuView.findViewById(R.id.hat_king3);
		
		preIv.setOnClickListener(this);
		nextIv.setOnClickListener(this);
		no_pet_layout=(RelativeLayout)menuView.findViewById(R.id.no_pet_layout);
		no_pet_layout.setOnClickListener(this);
		rqTv.setOnClickListener(this);
		animalList=new ArrayList<Animal>();
		sexIv=(ImageView)menuView.findViewById(R.id.imageView8);
		levelTv=(TextView)menuView.findViewById(R.id.level_tv);
		goldTv=(TextView)menuView.findViewById(R.id.gold_coin_tv);
		jobTv=(TextView)menuView.findViewById(R.id.textView2);
		
		galleryAdapter=new BaseAdapter() {
			
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				Holder holder=null;
				if(convertView==null){
					convertView=LayoutInflater.from(homeActivity).inflate(R.layout.item_gallery_view,null);
					holder=new Holder();
					holder.roundImageView=(RoundImageView)convertView.findViewById(R.id.roundImage_one_border);
					holder.hat_king=(ImageView)convertView.findViewById(R.id.hat_king);
					convertView.setTag(holder);
				}else{
					holder=(Holder)convertView.getTag();
				}
				ImageLoader imageLoader=ImageLoader.getInstance();
				imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animalList.get(position%animalList.size()).pet_iconUrl, holder.roundImageView, displayImageOptions);
				holder.roundImageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
				if(Constants.user.currentAnimal!=null){
					if(Constants.user.currentAnimal.a_id==animalList.get(position%animalList.size()).a_id){
						holder.hat_king.setVisibility(View.VISIBLE);
					}else{
						holder.hat_king.setVisibility(View.INVISIBLE);
					}
				}
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				
				if(animalList.size()>3){
					preIv.setVisibility(View.VISIBLE);
					nextIv.setVisibility(View.VISIBLE);
					round1.setVisibility(View.INVISIBLE);
					round2.setVisibility(View.INVISIBLE);
					round3.setVisibility(View.INVISIBLE);
					hat_king1.setVisibility(View.INVISIBLE);
					hat_king2.setVisibility(View.INVISIBLE);
					hat_king2.setVisibility(View.INVISIBLE);
					gallery.setVisibility(View.VISIBLE);
					gallery.setSelection(0);
					hasShow=false;
					return 1990000009;
				}else{
					
					gallery.setVisibility(View.INVISIBLE);
					if(animalList.size()==0){
						hasShow=false;
						hat_king1.setVisibility(View.INVISIBLE);
						hat_king2.setVisibility(View.INVISIBLE);
						hat_king3.setVisibility(View.INVISIBLE);
						preIv.setVisibility(View.INVISIBLE);
						nextIv.setVisibility(View.INVISIBLE);
					}else if(animalList.size()==1){
						if(!hasShow){
							hasShow=true;
							hat_king1.setVisibility(View.INVISIBLE);
							hat_king2.setVisibility(View.INVISIBLE);
							hat_king3.setVisibility(View.INVISIBLE);
							round1.setVisibility(View.INVISIBLE);
							round2.setVisibility(View.INVISIBLE);
							round3.setVisibility(View.INVISIBLE);
							preIv.setVisibility(View.INVISIBLE);
							nextIv.setVisibility(View.INVISIBLE);
							round1.setVisibility(View.VISIBLE);
							hat_king1.setVisibility(View.VISIBLE);
							ImageLoader imageLoader=ImageLoader.getInstance();
							imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animalList.get(0).pet_iconUrl, round1, displayImageOptions,new MyOnImageLoader());
							round1.setTag(animalList.get(0));
							round1.setOnClickListener(new MyOnClickListener());
						}
						
						
					}else if(animalList.size()==2){
						if(!hasShow){
							hasShow=true;
							hat_king1.setVisibility(View.INVISIBLE);
							hat_king2.setVisibility(View.INVISIBLE);
							hat_king3.setVisibility(View.INVISIBLE);
							preIv.setVisibility(View.INVISIBLE);
							nextIv.setVisibility(View.INVISIBLE);
							round1.setVisibility(View.INVISIBLE);
							round2.setVisibility(View.INVISIBLE);
							round3.setVisibility(View.INVISIBLE);
							round2.setVisibility(View.VISIBLE);
							round3.setVisibility(View.VISIBLE);
							round2.setTag(animalList.get(0));
							round3.setTag(animalList.get(1));
							if(Constants.user.currentAnimal!=null&&Constants.user.currentAnimal.a_id==animalList.get(0).a_id){
								hat_king2.setVisibility(View.VISIBLE);
							}else if(Constants.user.currentAnimal!=null&&Constants.user.currentAnimal.a_id==animalList.get(1).a_id){
								hat_king3.setVisibility(View.VISIBLE);
							}
							ImageLoader imageLoader=ImageLoader.getInstance();
							imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animalList.get(0).pet_iconUrl, round2, displayImageOptions,new MyOnImageLoader());
							imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animalList.get(1).pet_iconUrl, round3, displayImageOptions,new MyOnImageLoader());
							round2.setOnClickListener(new MyOnClickListener());
							round3.setOnClickListener(new MyOnClickListener());
						}
						
					}else if(animalList.size()==3){
						hasShow=false;
						hat_king1.setVisibility(View.INVISIBLE);
						hat_king2.setVisibility(View.INVISIBLE);
						hat_king3.setVisibility(View.INVISIBLE);
						preIv.setVisibility(View.INVISIBLE);
						nextIv.setVisibility(View.INVISIBLE);
						round1.setVisibility(View.INVISIBLE);
						round2.setVisibility(View.INVISIBLE);
						round3.setVisibility(View.INVISIBLE);
						gallery.setVisibility(View.VISIBLE);
					}
					return animalList.size();
				}
				
			}
			class Holder {
				RoundImageView roundImageView;
				ImageView hat_king;
			}
		};
		gallery.setAdapter(galleryAdapter);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				 final Animal animal=animalList.get(position%animalList.size());
				 if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.finish();
						Intent intent=new Intent(homeActivity,PetKingdomActivity.class);
						intent.putExtra("animal", animal);
						homeActivity.startActivity(intent);
			}
		});
//		gallery.setSelection(1);
		
		messageIv=(ImageView)menuView.findViewById(R.id.imageView7);
		messageIv.setOnClickListener(this);
		homeLayout.setOnClickListener(this);
		marketLayout.setOnClickListener(this);
		messageLayout.setOnClickListener(this);
		activityLayout.setOnClickListener(this);
		setupLayout.setOnClickListener(this);
		userIcon.setOnClickListener(this);
		
		
		if(Constants.user==null){
			handler.sendEmptyMessageDelayed(USER_INFO, 100);
		}else{
			setViews();
		}
		
		infoLayout=(LinearLayout)menuView.findViewById(R.id.info_layout);
		
		
		if(!Constants.isSuccess){
			levelTv.setVisibility(View.INVISIBLE);
			sexIv.setVisibility(View.INVISIBLE);
			mailText.setVisibility(View.INVISIBLE);
		}
		
	}
	/**
	 * 界面初始化，头像，昵称，性别 等 
	 */
	public void setViews(){
		if(Constants.user!=null&&Constants.user.aniList!=null&&Constants.user.aniList.size()>0){
			animalList=Constants.user.aniList;
			galleryAdapter.notifyDataSetChanged();
		}
		
		if(Constants.user!=null){
			userNameTve.setText(Constants.user.u_nick);//昵称//种族
			
			imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+Constants.user.u_iconUrl, userIcon, displayImageOptions1,new ImageLoadingListener(){

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
			});
			sexIv.setVisibility(View.VISIBLE);
			if(Constants.user.u_gender==1){
				sexIv.setImageResource(R.drawable.male1);
			}else{
				sexIv.setImageResource(R.drawable.female1);
			}
			goldTv.setText(""+Constants.user.coinCount);
			if(Constants.user.currentAnimal!=null){
				jobTv.setText(""+Constants.user.currentAnimal.pet_nickName+"的"+Constants.user.rank);
				levelTv.setText("Lv."+Constants.user.lv);
				if(Constants.user.aniList!=null){
					animalList=Constants.user.aniList;
					hasShow=false;
					galleryAdapter.notifyDataSetChanged();
				}
				
			}
		}
	
		if(!Constants.isSuccess){
			levelTv.setVisibility(View.INVISIBLE);
			sexIv.setVisibility(View.INVISIBLE);
			mailText.setVisibility(View.INVISIBLE);
			no_pet_layout.setVisibility(View.VISIBLE);
		}else{
			no_pet_layout.setVisibility(View.INVISIBLE);
		}
	
	}
	
	public void getNewsNum(){
		//获取消息和活动数目
		new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						final int mail_count=StringUtil.getNewMessageNum(homeActivity,handleHttpConnectionException.getHandler(homeActivity));
//						final ArrayList<Topic> tempArrayList=HttpUtil.imageTopicApi(handleHttpConnectionException.getHandler(homeActivity));
						final LoginJson.Data data=HttpUtil.getMailAndActivityNum(handleHttpConnectionException.getHandler(homeActivity),homeActivity);
						homeActivity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								if(mail_count!=0){
									mailText.setVisibility(View.VISIBLE);
									mailText.setText(""+(mail_count));
									if(HomeFragment.homeFragment!=null){
										HomeFragment.homeFragment.message_num_tv.setText(""+(mail_count));
									}
								}else{
									mailText.setVisibility(View.INVISIBLE);
								}
								if(data!=null&&data.topic_count>0){
									activityText.setVisibility(View.VISIBLE);
									String temp=""+data.topic_count;
									activityText.setText(temp);
									
								}else{
									activityText.setVisibility(View.INVISIBLE);
								}
							}
						});
						
					}
				}).start();
	}
	
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		scrollView=(ScrollView)menuView.findViewById(R.id.scrollview);
		
	}

   int count=0;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.message_linearLayout_left:
			/*
			 * 穿越，喵星汪星，星球变换
			 */
			Intent intent=new Intent(homeActivity,ChoseStarActivity.class);
			intent.putExtra("mode", 1);//表明从策划Menu  “穿越” 条目进入此界面
			homeActivity.startActivity(intent);
			/*SharedPreferences sp=homeActivity.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME,Context.MODE_WORLD_WRITEABLE);
			String starStr=sp.getString(Constants.CURRENT_STAR, "cat");
			SharedPreferences.Editor editor=sp.edit();
			if("cat".equals(starStr)){
				editor.putString(Constants.CURRENT_STAR, "dog");
			}else{
				editor.putString(Constants.CURRENT_STAR, "cat");
			}
			editor.commit();*/
			break;
		case R.id.activity_linearLayout_left:
			homeActivity.showActivityFragment();
			break;
		case R.id.setup_linearLayout_left:
			homeActivity.showSetupFragment();
			break;
		case R.id.home_linearLayout_left:
			if(homeActivity.homeFragment==null){
				homeActivity.showHomeFragment();
				homeActivity.toggle();
			}else{
				homeActivity.showHomeFragment();
			}
			
			break;
		case R.id.market_linearLayout_left:
			homeActivity.showMarketFragment();
			break;
		case R.id.imageView_left_3:
           if(!UserStatusUtil.isLoginSuccess(homeActivity,popup_parent,black_layout)){
				
				return;
			}
           if(Constants.user==null){
        	   Toast.makeText(homeActivity, "正在更新信息", Toast.LENGTH_LONG).show();
        	   return;
           }
           if(UserDossierActivity.userDossierActivity!=null)UserDossierActivity.userDossierActivity.finish();
			Intent intent4 =new Intent(homeActivity,UserDossierActivity.class);
			UserImagesJson.Data data=new UserImagesJson.Data();
			data.user=Constants.user;
			intent4.putExtra("user", Constants.user);
			homeActivity.startActivity(intent4);
			break;
		case R.id.left_viewpager:
			int a=gallery.getFirstVisiblePosition();
			a--;
//			gallery.setSelection(a);
			int width=(int)(gallery.getChildAt(0).getWidth()+(gallery.getChildAt(1).getX()-gallery.getChildAt(0).getX()-gallery.getChildAt(0).getWidth())/2);
			gallery.moveToNext(-width);
			break;
		case R.id.right_viewpager:
			width=(int)(gallery.getChildAt(0).getWidth()+(gallery.getChildAt(1).getX()-gallery.getChildAt(0).getX()-gallery.getChildAt(0).getWidth())/2);
			int w=gallery.getWidth();
//			gallery.scrollBy((int)gallery.getChildAt(0).getWidth(), 0);
			gallery.moveToNext(width);

			break;
		
		case R.id.imageView7:
            if(!UserStatusUtil.isLoginSuccess(homeActivity,popup_parent,black_layout)){
				return;
			}
			homeActivity.showMessageFragment();
			mailText.setVisibility(View.INVISIBLE);
			MobclickAgent.onEvent(homeActivity, "message");
			mailText.setText(""+0);
			if(HomeFragment.homeFragment!=null){
				HomeFragment.homeFragment.message_num_tv.setVisibility(View.INVISIBLE);
				HomeFragment.homeFragment.message_num_tv.setText("0");;
			}
			break;
	
		case R.id.no_pet_layout:
			if(!UserStatusUtil.isLoginSuccess(homeActivity,popup_parent,black_layout)){
				
			}
			break;
		case R.id.rq_tv:
			Intent intent5=new Intent(homeActivity,PopularRankListActivity.class);
			homeActivity.startActivity(intent5);
			MobclickAgent.onEvent(homeActivity, "rank");
			break;
		}
	}
	public class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			 final Animal animal=(Animal)arg0.getTag();
			 new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpUtil.animalInfo(homeActivity,animal, handleHttpConnectionException.getHandler(homeActivity));
					if(!StringUtil.isEmpty(animal.race)){
						Intent intent=new Intent(homeActivity,PetKingdomActivity.class);
						intent.putExtra("animal", animal);
						homeActivity.startActivity(intent);
					}
				}
			}).start();
				
		}
	}
	public class MyOnImageLoader implements ImageLoadingListener{

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll", "onLoadingStarted");
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll", "onLoadingFailed");
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll", "onLoadingComplete");
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub
			ImageLoader imageLoader=ImageLoader.getInstance();
			ImageView v=(ImageView)view;
			Animal animal=(Animal)view.getTag();
			imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl, v, displayImageOptions);
		}
		
	}
	
	
	


	
	

}
