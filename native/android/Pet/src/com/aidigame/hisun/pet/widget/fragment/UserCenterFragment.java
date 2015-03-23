package com.aidigame.hisun.pet.widget.fragment;


import java.util.ArrayList;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.LoginJson;
import com.aidigame.hisun.pet.huanxin.DemoHXSDKHelper;
import com.aidigame.hisun.pet.ui.AccountActivity;
import com.aidigame.hisun.pet.ui.ChargeActivity;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.pet.ui.ExchangeActivity;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.MarketActivity;
import com.aidigame.hisun.pet.ui.ModifyPetInfoActivity;
import com.aidigame.hisun.pet.ui.MyItemActivity;
import com.aidigame.hisun.pet.ui.PlayGameActivity;
import com.aidigame.hisun.pet.ui.SetupActivity;
import com.aidigame.hisun.pet.ui.UserCardActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 用户个人中心
 * @author admin
 *
 */
public class UserCenterFragment extends Fragment implements OnClickListener{
	
	private  Handler handler;
	private  View view;
	private  ImageView setupIv,genderIv,modifyIv;
	private  RoundImageView userIcon;
	private  TextView userNameTv,userCityTv,loginTv;
	public TextView goldNumTv,messageNumTv;
	private  RelativeLayout chargeLayout,goldLayout;
	private  LinearLayout messageLayout,marketLayout,exchangeLayout,giveLayout,giftLayout,accountLayout;
	
	private  View popupParent;
	private  RelativeLayout black_layout;
	
	public static UserCenterFragment userCenterFragment;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		handler=HandleHttpConnectionException.getInstance().getHandler(getActivity());
		view=inflater.inflate(R.layout.fragment_user_center, null);
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
		messageNumTv=(TextView)view.findViewById(R.id.message_tv);
		messageLayout=(LinearLayout)view.findViewById(R.id.message_layout);
		marketLayout=(LinearLayout)view.findViewById(R.id.market_layout);
		giveLayout=(LinearLayout)view.findViewById(R.id.give_layout);
		giftLayout=(LinearLayout)view.findViewById(R.id.gift_layout);
		exchangeLayout=(LinearLayout)view.findViewById(R.id.exchange_layout);
		accountLayout=(LinearLayout)view.findViewById(R.id.account_layout);
		chargeLayout=(RelativeLayout)view.findViewById(R.id.charge_layout);
		goldLayout=(RelativeLayout)view.findViewById(R.id.gold_layout);
		loginTv=(TextView)view.findViewById(R.id.user_center_login);
		modifyIv=(ImageView)view.findViewById(R.id.modify_iv);
		
		if(Constants.CON_VERSION.equals(StringUtil.getAPKVersionName(getActivity()))){
			
		}else{
			chargeLayout.setVisibility(View.VISIBLE);
		}
		
		
		initListener();
		if(PetApplication.myUser!=null){
			updatateInfo(true);
		}else{
			goldLayout.setVisibility(View.GONE);
			loginTv.setVisibility(View.VISIBLE);
		}
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
		giveLayout.setOnClickListener(this);
		marketLayout.setOnClickListener(this);
		giftLayout.setOnClickListener(this);
		exchangeLayout.setOnClickListener(this);
		accountLayout.setOnClickListener(this);
		chargeLayout.setOnClickListener(this);
		loginTv.setOnClickListener(this);
		modifyIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_center_login:
			/*Intent intentLogin=new Intent(homeActivity,ChoseAcountTypeActivity.class);
			homeActivity.startActivity(intentLogin);*/
			 if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					
					return;
				}
			break;
		case R.id.message_layout:
			/*if(MessageActivity.messageActivity!=null){
				MessageActivity.messageActivity.finish();
				MessageActivity.messageActivity=null;
			}
			Intent intent1=new Intent(homeActivity,MessageActivity.class);
			homeActivity.startActivity(intent1);
			messageNumTv.setVisibility(View.INVISIBLE);*/
			
			startActivity(new Intent(getActivity(), com.aidigame.hisun.pet.huanxin.MainActivity.class));
			
			break;
		case R.id.market_layout:
			if(MarketActivity.marketActivity!=null){
				
				MarketActivity.marketActivity.finish();
				MarketActivity.marketActivity=null;
			}
			Intent intent2=new Intent(getActivity(),MarketActivity.class);
			this.startActivity(intent2);
			break;
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
			if(MyItemActivity.myItemActivity!=null){
				MyItemActivity.myItemActivity.finish();
				MyItemActivity.myItemActivity=null;
			}
			Intent intent3=new Intent(getActivity(),MyItemActivity.class);
			getActivity().startActivity(intent3);
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
		case R.id.account_layout:
            if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
				
				return;
			}
			if(AccountActivity.accountActivity!=null){
				AccountActivity.accountActivity.finish();
				AccountActivity.accountActivity=null;
			}
			Intent intent7=new Intent(getActivity(),AccountActivity.class);
			getActivity().startActivity(intent7);
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

		default:
			break;
		}
	}
	public void updatateInfo(boolean loadUserInfo) {
		// TODO Auto-generated method stub
		
		if(PetApplication.myUser!=null){
			if(goldLayout==null)return;
			goldLayout.setVisibility(View.VISIBLE);
			loginTv.setVisibility(View.GONE);
			loadIcon();
			if(!StringUtil.isEmpty(PetApplication.myUser.u_nick)){
				userNameTv.setText(PetApplication.myUser.u_nick);
			}
			if(!StringUtil.isEmpty(PetApplication.myUser.city)){
				userCityTv.setText(""+PetApplication.myUser.province+" | "+PetApplication.myUser.city);
			}
			genderIv.setVisibility(View.VISIBLE);
			if(PetApplication.myUser.u_gender==1){
				genderIv.setImageResource(R.drawable.male1);
			}else{
				genderIv.setImageResource(R.drawable.female1);
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
			goldLayout.setVisibility(View.GONE);
			loginTv.setVisibility(View.VISIBLE);
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
		imageLoader2.displayImage(Constants.USER_THUBMAIL_DOWNLOAD_TX+PetApplication.myUser.u_iconUrl+"@"+w+"w_"+w+"h_0l.jpg", userIcon, displayImageOptions2);
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
}
