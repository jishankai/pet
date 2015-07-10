package com.aidigame.hisun.imengstar.widget.fragment;


import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import me.maxwin.view.XListViewHeader;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.EarnMoneyTaskListAdapter;
import com.aidigame.hisun.imengstar.adapter.UserCenterPetsAdapter;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.blur.Blur;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.huanxin.DemoHXSDKHelper;
import com.aidigame.hisun.imengstar.ui.ChargeActivity;
import com.aidigame.hisun.imengstar.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.imengstar.ui.ExchangeActivity;
import com.aidigame.hisun.imengstar.ui.MarketActivity;
import com.aidigame.hisun.imengstar.ui.ModifyPetInfoActivity;
import com.aidigame.hisun.imengstar.ui.MyItemActivity;
import com.aidigame.hisun.imengstar.ui.PlayGameActivity;
import com.aidigame.hisun.imengstar.ui.SetupActivity;
import com.aidigame.hisun.imengstar.ui.UserCardActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.R;
import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 用户个人中心
 * @author admin
 *
 */
public class NewUserCenterFragment extends Fragment implements OnClickListener,IXListViewListener{
	public static final int MY_PET=0;
	public static final int EARN_GOLD=1;
	public static final int ACCOUNT=2;
	public int current_show=MY_PET;
	private  Handler handler;
	private  View view,lineView1,lineView2,lineView3;
	private  ImageView setupIv,genderIv,plusIv;
	private  RoundImageView userIcon;
	private  TextView userNameTv,userCityTv,genderTv;
	public TextView goldNumTv,messageNumTv;
	private  RelativeLayout messageLayout,exchangeLayout,chargeLayout,giftLayout/*,goldLayout*/;
	private  LinearLayout myPetLayout,earnLayout,accountLayout,blurLayout,fragmentLayout/*,marketLayout,giveLayout*/;
	private XListView xListView;
	private  View popupParent;
	private  RelativeLayout black_layout;
	private SetupFragment setupFragment;
	private EarnMoneyTaskListAdapter earnMoneyTaskListAdapter;
	public static NewUserCenterFragment userCenterFragment;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		handler=HandleHttpConnectionException.getInstance().getHandler(getActivity());
		view=inflater.inflate(R.layout.fragment_new_user_center, null);
		userCenterFragment=this;
		initView();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	private void initView() {
		// TODO Auto-generated method stub
		popupParent=view.findViewById(R.id.popup_parent);
		black_layout=(RelativeLayout)view.findViewById(R.id.black_layout);
		
		setupIv=(ImageView)view.findViewById(R.id.setup_iv);
		genderIv=(ImageView)view.findViewById(R.id.gender_iv);
		userIcon=(RoundImageView)view.findViewById(R.id.show_topic_usericon);
		userNameTv=(TextView)view.findViewById(R.id.user_name);
		userCityTv=(TextView)view.findViewById(R.id.user_city);
		goldNumTv=(TextView)view.findViewById(R.id.gold_num_tv);
		genderTv=(TextView)view.findViewById(R.id.gender_tv);
		messageNumTv=(TextView)view.findViewById(R.id.message_tv);
		messageLayout=(RelativeLayout)view.findViewById(R.id.message_layout);
		giftLayout=(RelativeLayout)view.findViewById(R.id.gift_layout);
		exchangeLayout=(RelativeLayout)view.findViewById(R.id.exchange_layout);
		accountLayout=(LinearLayout)view.findViewById(R.id.account_layout);
		chargeLayout=(RelativeLayout)view.findViewById(R.id.charge_layout);
		blurLayout=(LinearLayout)view.findViewById(R.id.linearLayout3);
		myPetLayout=(LinearLayout)view.findViewById(R.id.my_pet_layout);
		earnLayout=(LinearLayout)view.findViewById(R.id.earn_layout);
		lineView1=view.findViewById(R.id.line1);
		lineView2=view.findViewById(R.id.line2);
		lineView3=view.findViewById(R.id.line3);
		xListView=(XListView)view.findViewById(R.id.listview);
		plusIv=(ImageView)view.findViewById(R.id.back);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		fragmentLayout=(LinearLayout)view.findViewById(R.id.fragment_layout);
//		if(Constants.CON_VERSION.equals(StringUtil.getAPKVersionName(getActivity()))){
//			
//		}else{
			chargeLayout.setVisibility(View.VISIBLE);
//		}
		
		
		initListener();
		if(PetApplication.myUser!=null){
			updatateInfo(true);
		}else{
		}
		clickTab1();
		pullRefresh();
	}

    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
     	if(PetApplication.isSuccess){
			
			if(PetApplication.myUser!=null&&!DemoHXSDKHelper.getInstance().isLogined()){
				
			}else{
				int count=EMChatManager.getInstance().getUnreadMsgsCount();
				if(count==0){
					messageNumTv.setVisibility(View.INVISIBLE);
				}else{
					messageNumTv.setVisibility(View.VISIBLE);
					messageNumTv.setText(""+count);
				}
			}
			
			if(PetApplication.myUser!=null){
				updatateInfo(true);
			}
			
		}
    }

	private void initListener() {
		// TODO Auto-generated method stub
		userIcon.setOnClickListener(this);
		setupIv.setOnClickListener(this);
		messageLayout.setOnClickListener(this);
		giftLayout.setOnClickListener(this);
		exchangeLayout.setOnClickListener(this);
		accountLayout.setOnClickListener(this);
		chargeLayout.setOnClickListener(this);
		myPetLayout.setOnClickListener(this);
		earnLayout.setOnClickListener(this);
		plusIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*case R.id.user_center_login:
			Intent intentLogin=new Intent(homeActivity,ChoseAcountTypeActivity.class);
			homeActivity.startActivity(intentLogin);
			 if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					
					return;
				}
			break;*/
		case R.id.message_layout:
			/*if(MessageActivity.messageActivity!=null){
				MessageActivity.messageActivity.finish();
				MessageActivity.messageActivity=null;
			}
			Intent intent1=new Intent(homeActivity,MessageActivity.class);
			homeActivity.startActivity(intent1);
			messageNumTv.setVisibility(View.INVISIBLE);*/
			 if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					
					return;
				}
			startActivity(new Intent(getActivity(), com.aidigame.hisun.imengstar.huanxin.MainActivity.class));
			
			break;
	/*	case R.id.market_layout:
			if(MarketActivity.marketActivity!=null){
				
				MarketActivity.marketActivity.finish();
				MarketActivity.marketActivity=null;
			}
			Intent intent2=new Intent(getActivity(),MarketActivity.class);
			this.startActivity(intent2);
			break;*/
		case R.id.give_layout:
            if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
				
				return;
			}
			if(PetApplication.myUser!=null&&PetApplication.myUser.currentAnimal!=null){
				Intent intent=new Intent(getActivity(),PlayGameActivity.class);
				intent.putExtra("animal", PetApplication.myUser.currentAnimal);
				getActivity().startActivity(intent);
			}
			break;
		case R.id.gift_layout:
            if(MarketActivity.marketActivity!=null){
				
				MarketActivity.marketActivity.finish();
				MarketActivity.marketActivity=null;
			}
			Intent intent3=new Intent(getActivity(),MarketActivity.class);
			this.startActivity(intent3);
			/* if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					
					return;
				}
			if(MyItemActivity.myItemActivity!=null){
				MyItemActivity.myItemActivity.finish();
				MyItemActivity.myItemActivity=null;
			}
			Intent intent3=new Intent(getActivity(),MyItemActivity.class);
			getActivity().startActivity(intent3);*/
			break;
		case R.id.exchange_layout:
			 if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					
					return;
				}
			 if(ExchangeActivity.exchangeActivity!=null){
				 ExchangeActivity.exchangeActivity.finish();
				 ExchangeActivity.exchangeActivity=null;
			 }
			 Intent intent4=new Intent(getActivity(),ExchangeActivity.class);
			 getActivity().startActivity(intent4);
			break;
		case R.id.charge_layout:
			

			 if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					
					return;
				}
			 if(ChargeActivity.chargeActivity!=null){
				 ChargeActivity.chargeActivity.finish();
				 ChargeActivity.chargeActivity=null;
			 }
			 Intent intent6=new Intent(getActivity(),ChargeActivity.class);
			 getActivity().startActivity(intent6);
			break;
		case R.id.show_topic_usericon:
			 if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					
					return;
				}
			/*if(UserDossierActivity.userDossierActivity!=null){
				if(UserDossierActivity.userDossierActivity.loadedImage1!=null&&!UserDossierActivity.userDossierActivity.loadedImage1.isRecycled()){
					UserDossierActivity.userDossierActivity.loadedImage1.recycle();
				}
				UserDossierActivity.userDossierActivity.loadedImage1=null;
				UserDossierActivity.userDossierActivity.finish();
			}
			Intent intent=new Intent(homeActivity,UserDossierActivity.class);
			intent.putExtra("user",Constants.user);
			homeActivity.startActivity(intent);*/
			 Intent intent=new Intent(getActivity(),UserCardActivity.class);
				intent.putExtra("user",PetApplication.myUser);
				getActivity().startActivity(intent);
			break;
		case R.id.setup_iv:
			if(SetupActivity.setupActivity!=null){
				SetupActivity.setupActivity.finish();
				SetupActivity.setupActivity=null;
			}
			Intent intent5=new Intent(getActivity(),SetupActivity.class);
			getActivity().startActivity(intent5);
			break;
		case R.id.modify_iv:
			 if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					
					return;
				}
			 Intent intent8=new Intent(getActivity(),ModifyPetInfoActivity.class);
				intent8.putExtra("mode", 2);
				this.startActivity(intent8);
			break;
		case R.id.my_pet_layout:
			if(current_show==MY_PET){
				 if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
						
						return;
					}
				pullRefresh();
			}else{
				clickTab1();
			}
			 
			break;
		case R.id.earn_layout:
			 clickTab2();
			break;
		case R.id.account_layout:
            if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
				
				return;
			}
            clickTab3();
			/*if(AccountActivity.accountActivity!=null){
				AccountActivity.accountActivity.finish();
				AccountActivity.accountActivity=null;
			}
			Intent intent7=new Intent(getActivity(),AccountActivity.class);
			getActivity().startActivity(intent7);*/
			break;
		case R.id.back:
			joinKingdom();
			break;

		default:
			break;
		}
	}
	public void updatateInfo(boolean loadUserInfo) {
		// TODO Auto-generated method stub
		
		if(PetApplication.myUser!=null){
			loadIcon();
			if(!StringUtil.isEmpty(PetApplication.myUser.u_nick)){
				userNameTv.setText(PetApplication.myUser.u_nick);
			}
			if(!StringUtil.isEmpty(PetApplication.myUser.city)){
				userCityTv.setText(""+PetApplication.myUser.province+"  "+PetApplication.myUser.city);
			}
			genderIv.setVisibility(View.VISIBLE);
			if(PetApplication.myUser.u_gender==1){
				genderIv.setImageResource(R.drawable.gender_boy);
				genderTv.setText("男");
			}else{
				genderIv.setImageResource(R.drawable.gender_girl);
				genderTv.setText("女");
			}
			if(PetApplication.myUser.coinCount>=0){
				goldNumTv.setText(""+PetApplication.myUser.coinCount);
			}else{
				goldNumTv.setText("0");
			}
			getNewsNum(loadUserInfo);
			if(PetApplication.isSuccess){
				
				if(PetApplication.myUser!=null&&!DemoHXSDKHelper.getInstance().isLogined()){
					
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
		}else{
		}
		
	}
	private   void loadIcon(){
BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		DisplayImageOptions displayImageOptions2=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
	            .showImageOnFail(R.drawable.user_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		        .decodingOptions(options)
                .build();
		ImageLoader imageLoader2=ImageLoader.getInstance();
		if(getActivity()!=null){
		int w=getActivity().getResources().getDimensionPixelSize(R.dimen.one_dip)*72;
		imageLoader2.displayImage(Constants.USER_THUBMAIL_DOWNLOAD_TX+PetApplication.myUser.u_iconUrl+"@"+w+"w_"+w+"h_0l.jpg", userIcon, displayImageOptions2,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
			}
			
			public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
				// TODO Auto-generated method stub
				
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						/*添加毛玻璃效果
						 *首先要将Bitmap的Config转化为Config.ARGB_8888类型的 
						 */
//						loadedImage=Bitmap.createBitmap(pixels, loadedImage.getWidth(), loadedImage.getHeight(), Config.ARGB_8888);
						LogUtil.i("mi", "图片像素数："+loadedImage.getByteCount());
						Matrix matrix=new Matrix();
						matrix.setScale(0.4f, 0.4f);
						Bitmap  bmp=loadedImage.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);
//						int[] pixels=new int[bmp.getWidth()*bmp.getHeight()];
//						bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
//						
//						
//						bmp=Bitmap.createBitmap(pixels, bmp.getWidth(), bmp.getHeight(), Config.ARGB_8888);
						
						LogUtil.i("mi", "图片像素数："+bmp.getByteCount());
						
						final Bitmap loadedImage1=Blur.fastblur(getActivity(), bmp, 18);
				        if(bmp!=null&&!bmp.isRecycled()){
				        	bmp.recycle();
				        	bmp=null;
				        }
				        if(getActivity()!=null)
							getActivity().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									final BitmapDrawable bitmapDrawable=new BitmapDrawable(loadedImage1);
									int height=Constants.screen_width/bitmapDrawable.getMinimumWidth()*bitmapDrawable.getMinimumHeight();
									if(bitmapDrawable!=null){
										/*LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)blurLayout.getLayoutParams();
										if(lp==null){
										   lp=new LinearLayout.LayoutParams(Constants.screen_width,height);	
										}
										blurLayout.setLayoutParams(lp);*/
										blurLayout.setBackgroundDrawable(bitmapDrawable);
										blurLayout.setAlpha(0.9342857f);
									}
									/*linearLayout2.setBackgroundDrawable(bitmapDrawable);
									linearLayout2.setAlpha(0.9342857f);*/
								}
							});
							
					}
				}).start();
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
			}
		});
		}
		}
	private   void getNewsNum(final boolean loadUserInfo){
		//获取消息和活动数目
		new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						LogUtil.i("mi","用户个人中心获取用户信息");
//						final int mail_count=StringUtil.getNewMessageNum(homeActivity,handler);
						  final ArrayList<Animal> temp=HttpUtil.usersKingdom(getActivity(),PetApplication.myUser, 1, handler);
						  final MyUser u=HttpUtil.info(getActivity(), handler, PetApplication.myUser.userId);
						 LogUtil.i("mii", "getActivity()是否为空："+(getActivity()==null));
						 if(u!=null){
							 PetApplication.myUser.coinCount=u.coinCount;
								PetApplication.myUser.u_gender=u.u_gender;
								PetApplication.myUser.u_nick=u.u_nick;
								PetApplication.myUser.u_iconUrl=u.u_iconUrl;
								PetApplication.myUser.u_age=u.u_age;
						 }else{
							 return;
						 }
						
						 
						 
						  if(getActivity()==null)return;
						  getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								/*if(mail_count!=0){
									messageNumTv.setVisibility(View.VISIBLE);
									messageNumTv.setText(""+(mail_count));
									
								}else{
									messageNumTv.setVisibility(View.INVISIBLE);
								}*/
								u.currentAnimal=PetApplication.myUser.currentAnimal;
								u.aniList=PetApplication.myUser.aniList;
								PetApplication.myUser=u;
								if(temp!=null){
									PetApplication.myUser.aniList=temp;
								}
								if(loadUserInfo){
									updatateInfo(false);
								}
								
							}
						});
						
					}
				}).start();
	}
	private UserCenterPetsAdapter userCenterPetsAdapter;
	private ArrayList<Animal> animals=new ArrayList<Animal>();
	public void clickTab1(){
		lineView2.setVisibility(View.GONE);
		lineView3.setVisibility(View.GONE);
		lineView1.setVisibility(View.VISIBLE);
		fragmentLayout.setVisibility(View.GONE);
		xListView.setVisibility(View.VISIBLE);
		userCenterPetsAdapter=new UserCenterPetsAdapter(getActivity(), animals);
		xListView.setAdapter(userCenterPetsAdapter);
		fragmentLayout.setVisibility(View.GONE);
		current_show=MY_PET;
	}
	public void clickTab2(){
		lineView1.setVisibility(View.GONE);
		lineView3.setVisibility(View.GONE);
		lineView2.setVisibility(View.VISIBLE);
		fragmentLayout.setVisibility(View.GONE);
		xListView.setVisibility(View.GONE);
		xListView.setVisibility(View.VISIBLE);
		fragmentLayout.setVisibility(View.GONE);
		current_show=EARN_GOLD;
		ArrayList<Integer> tasks=new ArrayList<Integer>();
		int random=0;
		for(int i=0;i<20;i++){
			random=(int)(Math.random()*4);
			tasks.add(random);
			LogUtil.i("mi", "random="+random);
		}
		earnMoneyTaskListAdapter=new EarnMoneyTaskListAdapter(getActivity(), tasks);
		xListView.setAdapter(earnMoneyTaskListAdapter);
	}
	public void clickTab3(){
		lineView1.setVisibility(View.GONE);
		lineView2.setVisibility(View.GONE);
		lineView3.setVisibility(View.VISIBLE);
		xListView.setVisibility(View.GONE);
		current_show=ACCOUNT;
		fragmentLayout.setVisibility(View.VISIBLE);
		if(setupFragment==null){
			setupFragment=new SetupFragment();
		}
		FragmentTransaction ft=getFragmentManager().beginTransaction();
	
		ft.replace(R.id.fragment_layout, setupFragment);
		ft.commit();
	}
	boolean isRefresh=false;
	 public void pullRefresh(){
		 isRefresh=true;
		 if(current_show==MY_PET&&userCenterPetsAdapter!=null){
			 userCenterPetsAdapter.update(new ArrayList<Animal>());
			 userCenterPetsAdapter.notifyDataSetChanged();
		 }
			if(xListView!=null){
	    	xListView.updateHeaderHeight(xListView.mHeaderViewHeight);
	    	xListView.mHeaderView.setVisibility(View.VISIBLE);
	    	xListView.mPullRefreshing = true;
	    	xListView.mEnablePullRefresh=true;
	    	xListView.mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
				if (xListView.mListViewListener != null) {
					xListView.mListViewListener.onRefresh();
				}
				xListView.resetHeaderHeight();
			}
	    }

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		 new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(current_show==MY_PET){
					if(PetApplication.myUser==null)return;
					final ArrayList<Animal> temp=HttpUtil.usersKingdom(getActivity(),PetApplication.myUser,0,handler);
//					final ArrayList<Animal> temp=HttpUtil.myPetCard(handler,getActivity());
					if(getActivity()!=null)
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(temp!=null&&temp.size()>0){
								animals=new ArrayList<Animal>();
								for(int i=0;i<temp.size();i++){
									if(temp.get(i).master_id==PetApplication.myUser.userId){
										animals.add(temp.get(i));
									}
								}
								for(int i=0;i<temp.size();i++){
									if(!animals.contains(temp.get(i))){
										animals.add(temp.get(i));
									}
								}
								
								
								userCenterPetsAdapter.update(animals);
								animals=animals;
								userCenterPetsAdapter.notifyDataSetChanged();
							}
							
							xListView.stopRefresh();
							
						}
					});
					}else{
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								xListView.stopRefresh();
							}
						});
					}
				}
			}).start();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(current_show==MY_PET){
//				final ArrayList<Animal> temp=HttpUtil.myPetCard(handler,getActivity());
					final ArrayList<Animal> temp=HttpUtil.usersKingdom(getActivity(),PetApplication.myUser,0,handler);
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null&&temp.size()>0){
							animals=new ArrayList<Animal>();
							for(int i=0;i<temp.size();i++){
								if(temp.get(i).master_id==PetApplication.myUser.userId){
									animals.add(temp.get(i));
								}
							}
							for(int i=0;i<temp.size();i++){
								if(!animals.contains(temp.get(i))){
									animals.add(temp.get(i));
								}
							}
							
							
							userCenterPetsAdapter.update(animals);
							animals=animals;
							userCenterPetsAdapter.notifyDataSetChanged();
						}
						
						xListView.stopLoadMore();
						
					}
					
				});
				}else{
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							xListView.stopLoadMore();;
						}
					});
				}
			}
		}).start();
	}
	private   void joinKingdom(){
		 if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
				return;
		}
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
			int num=0;
			if(PetApplication.myUser.aniList.size()>=10&&PetApplication.myUser.aniList.size()<=20){
				num=(PetApplication.myUser.aniList.size()+1)*5;
			}else  if(PetApplication.myUser.aniList.size()>20){
				num=100;
			}
			
			if(/*Constants.user.coinCount>=num*/true){
				Intent intent=new Intent(getActivity(),ChoseAcountTypeActivity.class);
				intent.putExtra("from", 1);
				getActivity().startActivity(intent);
//				DialogGoRegister dialog=new DialogGoRegister(popup_parent, homeActivity, black_layout, 4);
			}else{
				
				DialogNote dialog=new DialogNote(popupParent, getActivity(), black_layout, 1);
			}
			
		}
	}

}
