package com.aidigame.hisun.pet.widget.fragment;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
import com.aidigame.hisun.pet.ui.DialogGiveSbGiftActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;

/**
 * 送礼物弹出框，界面内容
 * @author admin
 *
 */
public class NineGiftBox {
	View view;
	Context context;
	ArrayList<Gift> giftList;
	LinearLayout[] layouts=new LinearLayout[9];
	HandleHttpConnectionException handleHttpConnectionException;
	boolean isSendingGift=false;
	SendGiftResultListener listener;
	View progress;
	public NineGiftBox(Context context,ArrayList<Gift> giftList,View progress){
		view=LayoutInflater.from(context).inflate(R.layout.widget_nine_gift_box, null);
		this.context=context;
		this.giftList=giftList;
		this.progress=progress;
		ViewPager.LayoutParams params=new ViewPager.LayoutParams();
		params.width=params.MATCH_PARENT;
		params.height=params.MATCH_PARENT;
		view.setLayoutParams(params);
		
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		layouts[0]=(LinearLayout)view.findViewById(R.id.layout1);
		layouts[1]=(LinearLayout)view.findViewById(R.id.layout2);
		layouts[2]=(LinearLayout)view.findViewById(R.id.layout3);
		layouts[3]=(LinearLayout)view.findViewById(R.id.layout4);
		layouts[4]=(LinearLayout)view.findViewById(R.id.layout5);
		layouts[5]=(LinearLayout)view.findViewById(R.id.layout6);
		layouts[6]=(LinearLayout)view.findViewById(R.id.layout7);
		layouts[7]=(LinearLayout)view.findViewById(R.id.layout8);
		layouts[8]=(LinearLayout)view.findViewById(R.id.layout9);
		Gift gift=null;
		for(int i=0;i<giftList.size();i++){
			gift=giftList.get(i);
			if(i<layouts.length){
				if(gift.boughtNum>0){
					//已经买过
					layouts[i].removeAllViews();
					View v=getBoughtView(gift,i);
					LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
					params.gravity=Gravity.CENTER;
					
					params.rightMargin=0;
					v.setLayoutParams(params);
					layouts[i].addView(v);
					layouts[i].setClickable(true);
				}else{
					//没有买过
					layouts[i].removeAllViews();
					View v=getMarketView(gift,i);
					LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
					params.gravity=Gravity.CENTER;
					
					params.rightMargin=0;
					v.setLayoutParams(params);
					layouts[i].addView(v);
				}
			}
			
		}
	}
	/**
	 * 用户还有此类礼物
	 * @param gift
	 */
	private View getBoughtView(final Gift gift,int position) {
		// TODO Auto-generated method stub
		View giftView=LayoutInflater.from(context).inflate(R.layout.item_user_gift_box_dialog_gridview, null);
		RelativeLayout boxLayout=(RelativeLayout)giftView.findViewById(R.id.box_layout);
		TextView addLikeTV=(TextView)giftView.findViewById(R.id.textView4);
		TextView giftNameTV=(TextView)giftView.findViewById(R.id.textView1);
		TextView giftNumTV=(TextView)giftView.findViewById(R.id.textView2);
		ImageView giftIV=(ImageView)giftView.findViewById(R.id.imageView1);
		try {
			giftIV.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RelativeLayout giftLayout=(RelativeLayout)giftView.findViewById(R.id.box_layout);
		if(gift.add_rq>0){
			addLikeTV.setText("+ "+gift.add_rq);
			boxLayout.setBackgroundResource(R.drawable.gift_box_background);
		}else{
			addLikeTV.setText("- "+(-gift.add_rq));
			boxLayout.setBackgroundResource(R.drawable.gift_box_background_subtract);
		}
		
		giftNameTV.setText(gift.name);
		giftNumTV.setText(""+gift.boughtNum);
		giftLayout.setTag(position);
		giftLayout.setOnClickListener(new GiftClickListener(gift));
		return giftView;
	}
	/**
	 * 用户没有此类礼物
	 * @param gift
	 */
	private View getMarketView(final Gift gift,int position) {
		// TODO Auto-generated method stub
		View giftView=LayoutInflater.from(context).inflate(R.layout.item_market_dialog_gridview, null);
		
		TextView addLikeTV=(TextView)giftView.findViewById(R.id.textView4);
		TextView giftNameTV=(TextView)giftView.findViewById(R.id.textView1);
		TextView giftPriceTV=(TextView)giftView.findViewById(R.id.textView2);
		TextView giftStatusTV=(TextView)giftView.findViewById(R.id.textView5);
		ImageView giftIV=(ImageView)giftView.findViewById(R.id.imageView1);
		try {
			giftIV.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RelativeLayout giftLayout=(RelativeLayout)giftView.findViewById(R.id.box_layout);
		if(gift.add_rq>0){
			giftLayout.setBackgroundResource(R.drawable.gift_box_background);
			addLikeTV.setText("+ "+gift.add_rq);
		}else{
			addLikeTV.setText("- "+(-gift.add_rq));
			giftLayout.setBackgroundResource(R.drawable.gift_box_background_subtract);
		}
		
		giftNameTV.setText(gift.name);
		giftPriceTV.setText(""+gift.price);
		if(StringUtil.isEmpty(gift.status)){
		    giftStatusTV.setVisibility(View.INVISIBLE);
		   
		}else{
			giftStatusTV.setVisibility(View.VISIBLE);
			giftStatusTV.setText(gift.status);
			
		}
		giftLayout.setTag(position);
		giftLayout.setOnClickListener(new GiftClickListener(gift));
		return giftView;
	}
	public void changeView(View view,Gift gift){
		Object  o=view.getTag();
		int position=(Integer)o;
		if(gift.boughtNum==0){
			layouts[position].removeAllViews();
			View v=getMarketView(gift,position);
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
			params.gravity=Gravity.CENTER;
			
			params.rightMargin=0;
			v.setLayoutParams(params);
			layouts[position].addView(v);
		}else{
			TextView giftNumTV=(TextView)view.findViewById(R.id.textView2);
			giftNumTV.setText(""+gift.boughtNum);
		}
		
	}
	public View getView(){
		return view;
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
				if(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.showProgress.showProgress();;
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
										Toast.makeText(context, "Sorry~余额不足(⊙o⊙)哦~", Toast.LENGTH_LONG).show();
										if(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.showProgress.progressCancel();;
									}
								});
							}else{
								gift.buyingNum=1;
								User user=HttpUtil.buyGift(context,gift, handleHttpConnectionException.getHandler(context));
								if(Constants.user.coinCount>user.coinCount){
									Constants.user.coinCount=user.coinCount;
									handleHttpConnectionException.getHandler(context).post(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											if(MenuFragment.menuFragment!=null){
												MenuFragment.menuFragment.goldTv.setText(""+Constants.user.coinCount);
											}
//											Toast.makeText(context, "购买了一个"+gift.name, Toast.LENGTH_LONG).show();
										}
									});
									user=HttpUtil.sendGift(context,gift, handleHttpConnectionException.getHandler(context));
									if(user!=null){
										UserStatusUtil.checkUserExpGoldLvRankChange(user, (Activity)context, progress);
										handleHttpConnectionException.getHandler(context).post(new Runnable() {
											
											@Override
											public void run() {
												// TODO Auto-generated method stub
												if(gift.add_rq>0){
													Toast.makeText(context, "您送给"+gift.animal.pet_nickName+"一个"+gift.name+","+gift.animal.pet_nickName+gift.effect_des, Toast.LENGTH_LONG).show();
												}else{
													Toast.makeText(context, "您对"+gift.animal.pet_nickName+"扔了一个"+gift.name+","+gift.animal.pet_nickName+gift.effect_des, Toast.LENGTH_LONG).show();
												}
												isSendingGift=false;
												if(listener!=null)listener.lastResult(true,null);
												if(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.showProgress.progressCancel();
											}
										});
									}else{
										if(listener!=null)listener.lastResult(false,null);
										isSendingGift=false;
										handleHttpConnectionException.getHandler(context).post(new Runnable() {
											
											@Override
											public void run() {
												// TODO Auto-generated method stub
												if(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.showProgress.progressCancel();
												
											}
										});
									}
								}
							}
							
						}else{
							
							User user=HttpUtil.sendGift(context,gift, handleHttpConnectionException.getHandler(context));
							if(user!=null){
								UserStatusUtil.checkUserExpGoldLvRankChange(user, (Activity)context, progress);
								handleHttpConnectionException.getHandler(context).post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										if(gift.add_rq>0){
											Toast.makeText(context, "您送给"+gift.animal.pet_nickName+"一个"+gift.name+","+gift.animal.pet_nickName+gift.effect_des, Toast.LENGTH_LONG).show();
										}else{
											Toast.makeText(context,"您对"+gift.animal.pet_nickName+"扔了一个"+gift.name+","+gift.animal.pet_nickName+gift.effect_des, Toast.LENGTH_LONG).show();
										}
										gift.boughtNum--;
										changeView(v, gift);
										if(listener!=null)listener.lastResult(true,gift);
										isSendingGift=false;
										if(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.showProgress.progressCancel();
									}
								});
							}else{
								isSendingGift=false;
								if(listener!=null)listener.lastResult(false,null);
								handleHttpConnectionException.getHandler(context).post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										if(DialogGiveSbGiftActivity.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity.dialogGiveSbGiftActivity.showProgress.progressCancel();
										
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
