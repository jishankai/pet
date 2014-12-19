package com.aidigame.hisun.pet.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.fragment.FourGiftBox;
import com.aidigame.hisun.pet.widget.fragment.FourGiftBox.SendGiftResultListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 给人送礼物，弹出框
 * @author admin
 *
 */
public class DialogGiveSbGiftActivity1 extends Activity{
	LinearLayout rootLayout;
	public LinearLayout black_layout;
	public ShowProgress showProgress;
	Context context;
	ArrayList<Gift> giftList;
	TextView toWhoTV,goShoppngTV;
	ImageView closeIV;
	public static DialogGiveSbGiftActivity1 dialogGiveSbGiftActivity;
	ImageView[] ivs=new ImageView[8];
	ViewPager viewPager;
	ArrayList<View> viewList;
	HomeViewPagerAdapter adapter;
	public static DialogGoListener dialogGoListener;
	public RelativeLayout progressLayout;
	HandleHttpConnectionException handleHttpConnectionException;
	Animal animal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.context=this;
		dialogGiveSbGiftActivity=this;
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.dialog_give_sb_gift);
		
		MobclickAgent.onEvent(this, "gift_button");
		giftList=new ArrayList<Gift>();
		this.animal=(Animal)getIntent().getSerializableExtra("animal");
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		toWhoTV=(TextView)findViewById(R.id.textView1);
		toWhoTV.setText("给"+animal.pet_nickName+"送个礼物");
		closeIV=(ImageView)findViewById(R.id.imageView1);
		goShoppngTV=(TextView)findViewById(R.id.go_shopping_tv);
		rootLayout=(LinearLayout)findViewById(R.id.root_layout);
		progressLayout=(RelativeLayout)findViewById(R.id.progresslayout);
		black_layout=(LinearLayout)findViewById(R.id.black_layout);
		showProgress=new ShowProgress(this, black_layout);
		ivs[0]=(ImageView)findViewById(R.id.imageView2);
		ivs[1]=(ImageView)findViewById(R.id.imageView3);
		ivs[2]=(ImageView)findViewById(R.id.imageView4);
		ivs[3]=(ImageView)findViewById(R.id.imageView5);
		ivs[4]=(ImageView)findViewById(R.id.imageView6);
		ivs[5]=(ImageView)findViewById(R.id.imageView7);
		ivs[6]=(ImageView)findViewById(R.id.imageView8);
		ivs[7]=(ImageView)findViewById(R.id.imageView9);
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		viewList=new ArrayList<View>();
		loadData();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				LogUtil.i("scroll", "onPageSelected");
				for(int i=0;i<ivs.length;i++){
					if(i<viewList.size()){
						if(i==arg0){
							ivs[i].setImageResource(R.drawable.point_red);
						}else{
							ivs[i].setImageResource(R.drawable.point_gray);
						}
						ivs[i].setVisibility(View.VISIBLE);
					}else{
						ivs[i].setVisibility(View.GONE);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				LogUtil.i("scroll", "onPageScrolled");
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				LogUtil.i("scroll", "onPageScrollStateChanged");
			}
		});
		goShoppngTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(dialogGoListener!=null){
					dialogGoListener.toDo();
				}
				
			}
		});
		closeIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogGiveSbGiftActivity1.this.finish();
				DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity=null;
				if(dialogGoListener!=null){
					dialogGoListener.closeDialog();
				}
				
			}
		});
		
		
	}
	private void loadData() {
		// TODO Auto-generated method stub
		InputStream in=null;
		ArrayList<Gift> list=StringUtil.getGiftList(context);
		if(giftList!=null){
			this.giftList=list;
		}else{
			this.giftList=new ArrayList<Gift>();
		}
		if(Constants.user!=null){
			 new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final ArrayList<Gift> temp1=HttpUtil.userItems(DialogGiveSbGiftActivity1.this,Constants.user, -1, handleHttpConnectionException.getHandler(context));
		                handleHttpConnectionException.getHandler(context).post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ArrayList<Gift> temp=temp1;
								if(temp!=null){
									for(int i=0;i<giftList.size();i++){
										if(!temp.contains(giftList.get(i))){
											temp.add(giftList.get(i));
										}
									}
								}else{
									temp=giftList;
								}
								giftList=temp;
								giftNumChange(giftList);
								showProgress.progressCancel();
							}
						});
					}
				}).start();
		}else{
			showProgress.progressCancel();
			giftNumChange(giftList);
		}
      
	}
	public void setDialogGoListener(DialogGoListener listener){
		dialogGoListener=listener;
	}
	public void giftNumChange(final ArrayList<Gift> giftList){
		viewList=new ArrayList<View>();
		ArrayList<Gift> tempList=null;
		for(int i=0;i<giftList.size();i++){
			if(i%4==0){
				if(i!=0){
					FourGiftBox ngb=new FourGiftBox(context, tempList);
					ngb.setSendGiftResultListener(new SendGiftResultListener() {
						
						@Override
						public void lastResult(boolean isSuccess,Gift gift) {
							// TODO Auto-generated method stub
							if(dialogGoListener!=null){
								dialogGoListener.lastResult(isSuccess);;
								if(isSuccess&&gift!=null){
									int index=giftList.indexOf(gift);
									giftList.add(index, gift);
									giftNumChange(giftList);
								}
							}
						}

						@Override
						public void unRegister() {
							// TODO Auto-generated method stub
							DialogGiveSbGiftActivity1.this.finish();
							if(dialogGoListener!=null){
								dialogGoListener.unRegister();
							}
							
						}
					});
					viewList.add(ngb.getView());
				}
				tempList=new ArrayList<Gift>();
			}
			giftList.get(i).aid=animal.a_id;
			giftList.get(i).animal=animal;
			giftList.get(i).img_id=animal.img_id;
			giftList.get(i).hasSendNum=animal.send_gift_count;
			tempList.add(giftList.get(i));
		}
		if(tempList.size()>0){
			FourGiftBox ngb=new FourGiftBox(context, tempList);
			ngb.setSendGiftResultListener(new SendGiftResultListener() {
				
				@Override
				public void lastResult(boolean isSuccess,Gift gift) {
					// TODO Auto-generated method stub
					if(dialogGoListener!=null){
						dialogGoListener.lastResult(isSuccess);;
						if(isSuccess&&gift!=null){
							int index=giftList.indexOf(gift);
							giftList.add(index, gift);
//							giftNumChange(giftList);
						}
					}
				}
				@Override
				public void unRegister() {
					// TODO Auto-generated method stub
					if(dialogGoListener!=null){
						dialogGoListener.unRegister();
					}
				}
			});
			viewList.add(ngb.getView());
		}
		for(int i=0;i<ivs.length;i++){
			if(i<viewList.size()){
				ivs[i].setVisibility(View.VISIBLE);
			}else{
				ivs[i].setVisibility(View.GONE);
			}
		}
		if(adapter!=null){
//			viewPager.removeAllViews();
//			adapter.update(viewList);
//			adapter.notifyDataSetChanged();
		}else{
			adapter=new HomeViewPagerAdapter(viewList);
			viewPager.setAdapter(adapter);
		}
		
//		setBlurImageBackground();
	}
	/**
	 *对话框，点击消失时，要做的操作
	 * @author admin
	 *
	 */
	public static interface DialogGoListener{
		void toDo();
		void closeDialog();
		void lastResult(boolean isSuccess);
		void unRegister();
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
	   }
	      public void setInvisible(boolean flag){
	    	  if(flag){
	    		  rootLayout.setVisibility(View.INVISIBLE);
	    	  }else{
	    		  rootLayout.setVisibility(View.VISIBLE);
	    	  }
	      }
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity=null;
			super.onDestroy();
		}
	     

}
