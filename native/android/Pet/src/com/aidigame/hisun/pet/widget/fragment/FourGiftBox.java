package com.aidigame.hisun.pet.widget.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.DialogGiveSbGiftActivity1;
import com.aidigame.hisun.pet.ui.DialogGiveSbGiftResultActivity;
import com.aidigame.hisun.pet.ui.DialogNoteActivity;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 送礼物弹出框，界面内容
 * @author admin
 *
 */
public class FourGiftBox {
	View view;
	Context context;
	ArrayList<Gift> giftList;
	HandleHttpConnectionException handleHttpConnectionException;
	boolean isSendingGift=false;
	SendGiftResultListener listener;
	
	RelativeLayout tabLayout1,tabLayout2,giftSignRlayout1,giftSignRlayout2,giftSignRlayout3,giftSignRlayout4;
	ImageView giftIV1,giftIV2,giftIV3,giftIV4;
	TextView  giftNameTv1,giftRqTv1,giftNumTv1,giftPriceTv1,
	          giftNameTv2,giftRqTv2,giftNumTv2,giftPriceTv2,
	          giftNameTv3,giftRqTv3,giftNumTv3,giftPriceTv3,
	          giftNameTv4,giftRqTv4,giftNumTv4,giftPriceTv4;
	LinearLayout has_gift_llayout1,no_gift_llayout1,
	             has_gift_llayout2,no_gift_llayout2,
	             has_gift_llayout3,no_gift_llayout3,
	             has_gift_llayout4,no_gift_llayout4,
	             llayout1,llayout2,llayout3,llayout4;
	
	
	
	public FourGiftBox(Context context,ArrayList<Gift> giftList){
		view=LayoutInflater.from(context).inflate(R.layout.widget_four_gift_box, null);
		this.context=context;
		this.giftList=giftList;
		ViewPager.LayoutParams params=new ViewPager.LayoutParams();
		params.width=params.MATCH_PARENT;
		params.height=params.MATCH_PARENT;
		view.setLayoutParams(params);
		
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		tabLayout1=(RelativeLayout)view.findViewById(R.id.tab_layout1);
		tabLayout2=(RelativeLayout)view.findViewById(R.id.tab_layout2);
		tabLayout1.setVisibility(View.INVISIBLE);
		tabLayout2.setVisibility(View.INVISIBLE);
		
		giftSignRlayout1=(RelativeLayout)view.findViewById(R.id.gift_sign_layout1);
		giftSignRlayout2=(RelativeLayout)view.findViewById(R.id.gift_sign_layout2);
		giftSignRlayout3=(RelativeLayout)view.findViewById(R.id.gift_sign_layout3);
		giftSignRlayout4=(RelativeLayout)view.findViewById(R.id.gift_sign_layout4);
		
		
		giftIV1=(ImageView)view.findViewById(R.id.gift_image_iv1);
		giftIV2=(ImageView)view.findViewById(R.id.gift_image_iv2);
		giftIV3=(ImageView)view.findViewById(R.id.gift_image_iv3);
		giftIV4=(ImageView)view.findViewById(R.id.gift_image_iv4);
		
		
		giftNameTv1=(TextView)view.findViewById(R.id.gift_name1);
		giftNameTv2=(TextView)view.findViewById(R.id.gift_name2);
		giftNameTv3=(TextView)view.findViewById(R.id.gift_name3);
		giftNameTv4=(TextView)view.findViewById(R.id.gift_name4);
		
		giftRqTv1=(TextView)view.findViewById(R.id.rq_num1);
		giftRqTv2=(TextView)view.findViewById(R.id.rq_num2);
		giftRqTv3=(TextView)view.findViewById(R.id.rq_num3);
		giftRqTv4=(TextView)view.findViewById(R.id.rq_num4);
		
		
		
		giftNumTv1=(TextView)view.findViewById(R.id.gift_num1);
		giftNumTv2=(TextView)view.findViewById(R.id.gift_num2);
		giftNumTv3=(TextView)view.findViewById(R.id.gift_num3);
		giftNumTv4=(TextView)view.findViewById(R.id.gift_num4);
		
		
		giftPriceTv1=(TextView)view.findViewById(R.id.gold_num1);
		giftPriceTv2=(TextView)view.findViewById(R.id.gold_num2);
		giftPriceTv3=(TextView)view.findViewById(R.id.gold_num3);
		giftPriceTv4=(TextView)view.findViewById(R.id.gold_num4);
		
		
		has_gift_llayout1=(LinearLayout)view.findViewById(R.id.has_gift_layout1);
		no_gift_llayout1=(LinearLayout)view.findViewById(R.id.no_gift_layout1);
		has_gift_llayout2=(LinearLayout)view.findViewById(R.id.has_gift_layout2);
		no_gift_llayout2=(LinearLayout)view.findViewById(R.id.no_gift_layout2);
		has_gift_llayout3=(LinearLayout)view.findViewById(R.id.has_gift_layout3);
		no_gift_llayout3=(LinearLayout)view.findViewById(R.id.no_gift_layout3);
		has_gift_llayout4=(LinearLayout)view.findViewById(R.id.has_gift_layout4);
		no_gift_llayout4=(LinearLayout)view.findViewById(R.id.no_gift_layout4);
		
		
		llayout1=(LinearLayout)view.findViewById(R.id.layout1);
		llayout2=(LinearLayout)view.findViewById(R.id.layout2);
		llayout3=(LinearLayout)view.findViewById(R.id.layout3);
		llayout4=(LinearLayout)view.findViewById(R.id.layout4);
		
		if(giftList.size()>=2){
			initGift1(giftList.get(0));
			initGift2(giftList.get(1));
			tabLayout1.setVisibility(View.VISIBLE);
		}
		
		if(giftList.size()==4){
			initGift3(giftList.get(2));
			initGift4(giftList.get(3));
			tabLayout2.setVisibility(View.VISIBLE);
		}
		initListener();
		
	}

	public void changeView(View view,Gift gift){
		switch (view.getId()) {
		case R.id.layout1:
			initGift1(gift);
			break;
		case R.id.layout2:
			initGift2(gift);
			break;
		case R.id.layout3:
			initGift3(gift);
			break;
		case R.id.layout4:
			initGift4(gift);
			break;
		}
		
	}
	public View getView(){
		return view;
	}
	
	public void initGift1(Gift gift){
		try {
			giftIV1.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		giftNameTv1.setText(gift.name);
		if(gift.add_rq>0){
			giftRqTv1.setText("+"+gift.add_rq);
			giftSignRlayout1.setBackgroundResource(R.drawable.gift_sign_good);
		}else{
			giftRqTv1.setText(""+gift.add_rq);
			giftSignRlayout1.setBackgroundResource(R.drawable.gift_sign_bad);
		}
		
		if(gift.boughtNum>0){
			has_gift_llayout1.setVisibility(View.VISIBLE);
			no_gift_llayout1.setVisibility(View.GONE);
			giftNumTv1.setText(""+gift.boughtNum);
		}else{
			has_gift_llayout1.setVisibility(View.GONE);
			no_gift_llayout1.setVisibility(View.VISIBLE);
			giftPriceTv1.setText(""+gift.price);
		}
	}
	public void initGift2(Gift gift){
		try {
			giftIV2.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		giftNameTv2.setText(gift.name);
		if(gift.add_rq>0){
			giftRqTv2.setText("+"+gift.add_rq);
			giftSignRlayout2.setBackgroundResource(R.drawable.gift_sign_good);
		}else{
			giftRqTv2.setText(""+gift.add_rq);
			giftSignRlayout2.setBackgroundResource(R.drawable.gift_sign_bad);
		}
		
		if(gift.boughtNum>0){
			has_gift_llayout2.setVisibility(View.VISIBLE);
			no_gift_llayout2.setVisibility(View.GONE);
			giftNumTv2.setText(""+gift.boughtNum);
		}else{
			has_gift_llayout2.setVisibility(View.GONE);
			no_gift_llayout2.setVisibility(View.VISIBLE);
			giftPriceTv2.setText(""+gift.price);
		}
	}
	public void initGift3(Gift gift){
		try {
			giftIV3.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		giftNameTv3.setText(gift.name);
		if(gift.add_rq>0){
			giftRqTv3.setText("+"+gift.add_rq);
			giftSignRlayout3.setBackgroundResource(R.drawable.gift_sign_good);
		}else{
			giftRqTv3.setText(""+gift.add_rq);
			giftSignRlayout3.setBackgroundResource(R.drawable.gift_sign_bad);
		}
		
		if(gift.boughtNum>0){
			has_gift_llayout3.setVisibility(View.VISIBLE);
			no_gift_llayout3.setVisibility(View.GONE);
			giftNumTv3.setText(""+gift.boughtNum);
		}else{
			has_gift_llayout3.setVisibility(View.GONE);
			no_gift_llayout3.setVisibility(View.VISIBLE);
			giftPriceTv3.setText(""+gift.price);
		}
	}
	public void initGift4(Gift gift){
		try {
			giftIV4.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		giftNameTv4.setText(gift.name);
		if(gift.add_rq>0){
			giftRqTv4.setText("+"+gift.add_rq);
			giftSignRlayout4.setBackgroundResource(R.drawable.gift_sign_good);
		}else{
			giftRqTv4.setText(""+gift.add_rq);
			giftSignRlayout4.setBackgroundResource(R.drawable.gift_sign_bad);
		}
		
		if(gift.boughtNum>0){
			has_gift_llayout4.setVisibility(View.VISIBLE);
			no_gift_llayout4.setVisibility(View.GONE);
			giftNumTv4.setText(""+gift.boughtNum);
		}else{
			has_gift_llayout4.setVisibility(View.GONE);
			no_gift_llayout4.setVisibility(View.VISIBLE);
			giftPriceTv4.setText(""+gift.price);
		}
	}
	private void initListener() {
		// TODO Auto-generated method stub
		if(giftList.size()>=2){
			llayout1.setOnClickListener(new GiftClickListener(giftList.get(0)));
			llayout2.setOnClickListener(new GiftClickListener(giftList.get(1)));
		}
		if(giftList.size()==4){
			llayout3.setOnClickListener(new GiftClickListener(giftList.get(2)));
			llayout4.setOnClickListener(new GiftClickListener(giftList.get(3)));
		}
	}
	
	
	class GiftClickListener implements OnClickListener{
		Gift gift;
		public GiftClickListener(Gift gift) {
			// TODO Auto-generated constructor stub
			this.gift=gift;
		}

		@Override
		public void onClick(final View v) {
			// TODO Auto-generated method stub
			if(!Constants.isSuccess){
				if(listener!=null){
					listener.unRegister();
				}
				return;
			}
			if(!isSendingGift){
				isSendingGift=true;
				if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.showProgress.showProgress();;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(gift.boughtNum==0){
							if(Constants.user.coinCount-gift.price<0){
								//金币不够，跳充值界面
								handleHttpConnectionException.getHandler(context).post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
//										Toast.makeText(context, "Sorry~余额不足(⊙o⊙)哦~", Toast.LENGTH_LONG).show();
										Intent intent=new Intent(context,DialogNoteActivity.class);
										intent.putExtra("mode", 10);
										intent.putExtra("info", "Sorry~余额不足(⊙o⊙)哦~");
										context.startActivity(intent);
										if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.showProgress.progressCancel();;
									}
								});
							}else{
								gift.buyingNum=1;
								User user=HttpUtil.buyGift(context,gift, handleHttpConnectionException.getHandler(context));
								if(user!=null){
									Map<String,String> map=new HashMap<String, String>();
									map.put("level", ""+gift.level);
									map.put("name", ""+gift.name);
									map.put("id", ""+gift.no);
									MobclickAgent.onEvent(context, "buy_gift",map);
									if(Constants.user.coinCount>user.coinCount){
										Constants.user.coinCount=user.coinCount;
										handleHttpConnectionException.getHandler(context).post(new Runnable() {
											
											@Override
											public void run() {
												// TODO Auto-generated method stub
												if(UserCenterFragment.userCenterFragment!=null){
													UserCenterFragment.userCenterFragment.goldNumTv.setText(""+Constants.user.coinCount);
												}
//												Toast.makeText(context, "购买了一个"+gift.name, Toast.LENGTH_LONG).show();
											}
										});
										user=HttpUtil.sendGift(context,gift, handleHttpConnectionException.getHandler(context));
										if(user!=null){
//											UserStatusUtil.checkUserExpGoldLvRankChange(user, (Activity)context, progress);
											handleHttpConnectionException.getHandler(context).post(new Runnable() {
												
												@Override
												public void run() {
													// TODO Auto-generated method stub
													if(gift.add_rq>0){
														
//														Toast.makeText(context, "您送给"+gift.animal.pet_nickName+"一个"+gift.name+","+gift.animal.pet_nickName+gift.effect_des, Toast.LENGTH_LONG).show();
													}else{
//														Toast.makeText(context, "您对"+gift.animal.pet_nickName+"扔了一个"+gift.name+","+gift.animal.pet_nickName+gift.effect_des, Toast.LENGTH_LONG).show();
													}
													gift.hasSendNum++;
													gift.animal.send_gift_count++;
													if(HomeActivity.homeActivity!=null&&HomeActivity.homeActivity.myPetFragment!=null){
														HomeActivity.homeActivity.myPetFragment.homeMyPet.adapter.updateTV("送了"+gift.animal.send_gift_count+"个",gift.add_rq);
													}
													isSendingGift=false;
													MobclickAgent.onEvent(context, "gift_suc");
													Map<String,String> map=new HashMap<String, String>();
													map.put("level", ""+gift.level);
													map.put("name", ""+gift.name);
													map.put("id", ""+gift.no);
													MobclickAgent.onEvent(context, "send_gift",map);
													Intent intent=new Intent(context,DialogGiveSbGiftResultActivity.class);
													intent.putExtra("gift", gift);
													intent.putExtra("animal", gift.animal);
													if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null){
														DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.setInvisible(true);;
													}
													context.startActivity(intent);
													if(listener!=null)listener.lastResult(true,null);
													if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.showProgress.progressCancel();
												}
											});
										}else{
											if(listener!=null)listener.lastResult(false,null);
											isSendingGift=false;
											handleHttpConnectionException.getHandler(context).post(new Runnable() {
												
												@Override
												public void run() {
													// TODO Auto-generated method stub
													if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.showProgress.progressCancel();
													
												}
											});
										}
									}
								}else{
									isSendingGift=false;
									if(listener!=null)listener.lastResult(false,null);
									handleHttpConnectionException.getHandler(context).post(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.showProgress.progressCancel();
											
										}
									});
								}
								
							}
							
						}else{
							
							User user=HttpUtil.sendGift(context,gift, handleHttpConnectionException.getHandler(context));
							if(user!=null){
//								UserStatusUtil.checkUserExpGoldLvRankChange(user, (Activity)context, progress);
								handleHttpConnectionException.getHandler(context).post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										if(gift.add_rq>0){
//											Toast.makeText(context, "您送给"+gift.animal.pet_nickName+"一个"+gift.name+","+gift.animal.pet_nickName+gift.effect_des, Toast.LENGTH_LONG).show();
										}else{
//											Toast.makeText(context,"您对"+gift.animal.pet_nickName+"扔了一个"+gift.name+","+gift.animal.pet_nickName+gift.effect_des, Toast.LENGTH_LONG).show();
										}
										gift.boughtNum--;
										changeView(v, gift);
										gift.hasSendNum++;
										if(HomeActivity.homeActivity!=null&&HomeActivity.homeActivity.myPetFragment!=null){
											HomeActivity.homeActivity.myPetFragment.homeMyPet.adapter.updateTV("送了"+gift.hasSendNum+"个",gift.add_rq);
										}
										MobclickAgent.onEvent(context, "gift_suc");
										Map<String,String> map=new HashMap<String, String>();
										map.put("level", ""+gift.level);
										map.put("name", ""+gift.name);
										map.put("id", ""+gift.no);
										MobclickAgent.onEvent(context, "send_gift",map);
										Intent intent=new Intent(context,DialogGiveSbGiftResultActivity.class);
										intent.putExtra("gift", gift);
										intent.putExtra("animal", gift.animal);
										context.startActivity(intent);
										if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null){
											DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.setInvisible(true);;
										}
										if(listener!=null)listener.lastResult(true,gift);
										isSendingGift=false;
										if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.showProgress.progressCancel();
									}
								});
							}else{
								isSendingGift=false;
								if(listener!=null)listener.lastResult(false,null);
								handleHttpConnectionException.getHandler(context).post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.showProgress.progressCancel();
										
									}
								});
							}
						}
						
					}
				}).start();
			}
		}
		
	}
	public void setSendGiftResultListener(SendGiftResultListener listener){
		this.listener=listener;
	}
	public static interface SendGiftResultListener{
		void lastResult(boolean isSuccess,Gift gift);
		void unRegister();
	}


}
