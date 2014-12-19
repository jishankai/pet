package com.aidigame.hisun.pet.widget.fragment;


import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.LoginJson;
import com.aidigame.hisun.pet.ui.AccountActivity;
import com.aidigame.hisun.pet.ui.ChargeActivity;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.pet.ui.ExchangeActivity;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.MarketActivity;
import com.aidigame.hisun.pet.ui.MessageActivity;
import com.aidigame.hisun.pet.ui.MyItemActivity;
import com.aidigame.hisun.pet.ui.PlayGameActivity;
import com.aidigame.hisun.pet.ui.SetupActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
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
	HomeActivity homeActivity;
	Handler handler;
	View view;
	ImageView setupIv,genderIv;
	RoundImageView userIcon;
	TextView userNameTv,userCityTv,messageNumTv,loginTv;
	public TextView goldNumTv;
	RelativeLayout chargeLayout,goldLayout;
	LinearLayout messageLayout,marketLayout,exchangeLayout,giveLayout,giftLayout,accountLayout;
	
	View popupParent;
	RelativeLayout black_layout;
	public static UserCenterFragment userCenterFragment;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		homeActivity=HomeActivity.homeActivity;
		handler=HandleHttpConnectionException.getInstance().getHandler(homeActivity);
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
		initListener();
		if(Constants.user!=null){
			updatateInfo();
			
		}else{
			goldLayout.setVisibility(View.GONE);
			loginTv.setVisibility(View.VISIBLE);
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
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_center_login:
			/*Intent intentLogin=new Intent(homeActivity,ChoseAcountTypeActivity.class);
			homeActivity.startActivity(intentLogin);*/
			 if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
					
					return;
				}
			break;
		case R.id.message_layout:
			if(MessageActivity.messageActivity!=null){
				MessageActivity.messageActivity.finish();
			}
			Intent intent1=new Intent(homeActivity,MessageActivity.class);
			homeActivity.startActivity(intent1);
			messageNumTv.setVisibility(View.INVISIBLE);
			break;
		case R.id.market_layout:
			if(MarketActivity.marketActivity!=null){
				MarketActivity.marketActivity.finish();
			}
			Intent intent2=new Intent(homeActivity,MarketActivity.class);
			this.startActivity(intent2);
			break;
		case R.id.give_layout:
            if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
				
				return;
			}
			if(Constants.user!=null&&Constants.user.currentAnimal!=null){
				Intent intent=new Intent(homeActivity,PlayGameActivity.class);
				intent.putExtra("animal", Constants.user.currentAnimal);
				homeActivity.startActivity(intent);
			}
			break;
		case R.id.gift_layout:
			if(MyItemActivity.myItemActivity!=null){
				MyItemActivity.myItemActivity.finish();
			}
			Intent intent3=new Intent(homeActivity,MyItemActivity.class);
			homeActivity.startActivity(intent3);
			break;
		case R.id.exchange_layout:
			 if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
					
					return;
				}
			 if(ExchangeActivity.exchangeActivity!=null){
				 ExchangeActivity.exchangeActivity.finish();
				 ExchangeActivity.exchangeActivity=null;
			 }
			 Intent intent4=new Intent(homeActivity,ExchangeActivity.class);
			 homeActivity.startActivity(intent4);
			break;
		case R.id.account_layout:
            if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
				
				return;
			}
			if(AccountActivity.accountActivity!=null){
				AccountActivity.accountActivity.finish();
			}
			Intent intent7=new Intent(homeActivity,AccountActivity.class);
			homeActivity.startActivity(intent7);
			break;
		case R.id.charge_layout:
			

			 if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
					
					return;
				}
			 if(ChargeActivity.chargeActivity!=null){
				 ChargeActivity.chargeActivity.finish();
				 ChargeActivity.chargeActivity=null;
			 }
			 Intent intent6=new Intent(homeActivity,ChargeActivity.class);
			 homeActivity.startActivity(intent6);
			break;
		case R.id.show_topic_usericon:
			 if(!UserStatusUtil.isLoginSuccess(homeActivity,popupParent,black_layout)){
					
					return;
				}
			if(UserDossierActivity.userDossierActivity!=null){
				if(UserDossierActivity.userDossierActivity.loadedImage1!=null&&!UserDossierActivity.userDossierActivity.loadedImage1.isRecycled()){
					UserDossierActivity.userDossierActivity.loadedImage1.recycle();
				}
				UserDossierActivity.userDossierActivity.loadedImage1=null;
				UserDossierActivity.userDossierActivity.finish();
			}
			Intent intent=new Intent(homeActivity,UserDossierActivity.class);
			intent.putExtra("user",Constants.user);
			homeActivity.startActivity(intent);
			break;
		case R.id.setup_iv:
			if(SetupActivity.setupActivity!=null){
				SetupActivity.setupActivity.finish();
			}
			Intent intent5=new Intent(homeActivity,SetupActivity.class);
			homeActivity.startActivity(intent5);
			break;

		default:
			break;
		}
	}
	public void updatateInfo() {
		// TODO Auto-generated method stub
		
		if(Constants.user!=null){
			goldLayout.setVisibility(View.VISIBLE);
			loginTv.setVisibility(View.GONE);
			loadIcon();
			if(!StringUtil.isEmpty(Constants.user.u_nick)){
				userNameTv.setText(Constants.user.u_nick);
			}
			if(!StringUtil.isEmpty(Constants.user.city)){
				userCityTv.setText(""+Constants.user.province+" | "+Constants.user.city);
			}
			genderIv.setVisibility(View.VISIBLE);
			if(Constants.user.u_gender==1){
				genderIv.setImageResource(R.drawable.male1);
			}else{
				genderIv.setImageResource(R.drawable.female1);
			}
			if(Constants.user.coinCount>=0){
				goldNumTv.setText(""+Constants.user.coinCount);
			}else{
				goldNumTv.setText("0");
			}
		}else{
			goldLayout.setVisibility(View.GONE);
			loginTv.setVisibility(View.VISIBLE);
		}
		getNewsNum();
	}
	public void loadIcon(){
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
		        .decodingOptions(options)
                .build();
		ImageLoader imageLoader2=ImageLoader.getInstance();
		imageLoader2.displayImage(Constants.USER_DOWNLOAD_TX+Constants.user.u_iconUrl, userIcon, displayImageOptions2);
	}
	public void getNewsNum(){
		//获取消息和活动数目
		new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						LogUtil.i("mi","用户个人中心获取用户信息");
						final int mail_count=StringUtil.getNewMessageNum(homeActivity,handler);
						  final ArrayList<Animal> temp=HttpUtil.usersKingdom(homeActivity,Constants.user, 1, handler);
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
								if(temp!=null){
									Constants.user.aniList=temp;
								}
							}
						});
						
					}
				}).start();
	}
}
