package com.aidigame.hisun.pet.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.MarketGridViewAdapter;
import com.aidigame.hisun.pet.adapter.MarketRealGridViewAdapter;
import com.aidigame.hisun.pet.adapter.MarketGridViewAdapter.ClickGiftListener;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.aidigame.hisun.pet.widget.fragment.UserCenterFragment;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
/**
 * 商城界面
 * @author admin
 *
 */
public class MarketActivity extends Activity implements OnClickListener{
	
	
	
	  public static int from=0;//0,正常的侧边栏滑动；1，从照片详情页进入；2，从王国资料页进入；3，从主界面瀑布流进入
		public static MarketActivity marketActivity;
	    FrameLayout frameLayout;
		View viewTopWhite;
		View popup_parent;
	    ImageView back,giftBox;
		TextView boxNumTV,spinnerTV1,spinnerTV2,spinnerTV3,coinNumTV,moreCoinTV;
		PopupWindow popupWindow1,popupWindow2,popupWindow3;
		GridView gridView,realGridView;
		MarketGridViewAdapter adapter;
		MarketRealGridViewAdapter realAdapter;
		HandleHttpConnectionException handleHttpConnectionException;
		public RelativeLayout black_layout;
		List<Gift> giftList;
		int newGift=0;//新买的礼物
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			UiUtil.setScreenInfo(this);
			setContentView(R.layout.fragment_market);
			marketActivity=this;
			handleHttpConnectionException=HandleHttpConnectionException.getInstance();
			initView();
			
		}
		boolean isBuying=false;
		private void initView() {
			// TODO Auto-generated method stub
			popup_parent=(View)findViewById(R.id.popup_parent);
			back=(ImageView)findViewById(R.id.back);
			gridView=(GridView)findViewById(R.id.market_gridview);
			realGridView=(GridView)findViewById(R.id.market_real_gridview);
			gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			realGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			giftList=new ArrayList<Gift>();
			black_layout=(RelativeLayout)findViewById(R.id.black_layout);
			setBlurImageBackground();
			loadData();
			adapter=new MarketGridViewAdapter(this,giftList);
			gridView.setAdapter(adapter);
			realAdapter=new MarketRealGridViewAdapter(this, giftList);
			realGridView.setAdapter(realAdapter);
		    giftBox=(ImageView)findViewById(R.id.gift_box_iv);
		    boxNumTV=(TextView)findViewById(R.id.textView3);
		    boxNumTV.setVisibility(View.INVISIBLE);
		    spinnerTV1=(TextView)findViewById(R.id.spinner_tv1);
		    spinnerTV2=(TextView)findViewById(R.id.spinner_tv2);
		    spinnerTV3=(TextView)findViewById(R.id.spinner_tv3);
		    coinNumTV=(TextView)findViewById(R.id.coin_num);
		    if(Constants.user!=null){
		    	coinNumTV.setText(""+Constants.user.coinCount);
		    }
		    moreCoinTV=(TextView)findViewById(R.id.more_coin);
		    spinnerTV1.setOnClickListener(this);
		    spinnerTV2.setOnClickListener(this);
		    spinnerTV3.setOnClickListener(this);
		    giftBox.setOnClickListener(this);
		    moreCoinTV.setOnClickListener(this);
		    back.setOnClickListener(this);
		   adapter.setClickGiftListener(new ClickGiftListener() {
			
			@Override
			public void clickGift(final Gift gift) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(MarketActivity.this,popup_parent,black_layout)){
					
					return;
				}
				if(isBuying){
					Toast.makeText(MarketActivity.this, "正在购买礼物", Toast.LENGTH_LONG).show();
					return;
				}
				if(Constants.user.coinCount-gift.price<0){
//					Toast.makeText(MarketActivity.this, "Sorry~余额不足(⊙o⊙)哦~", Toast.LENGTH_LONG).show();
					Intent intent=new Intent(MarketActivity.this,DialogNoteActivity.class);
					intent.putExtra("mode", 10);
					intent.putExtra("info", "Sorry~余额不足(⊙o⊙)哦~");
					startActivity(intent);
					return;
				}
				isBuying=true;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						gift.buyingNum=1;
						final MyUser user=HttpUtil.buyGift(MarketActivity.this,gift, handleHttpConnectionException.getHandler(MarketActivity.this));
						handleHttpConnectionException.getHandler(MarketActivity.this).post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(user!=null){
									newGift++;
									Map<String,String> map=new HashMap<String, String>();
									map.put("level", ""+gift.level);
									map.put("name", ""+gift.name);
									map.put("id", ""+gift.no);
									MobclickAgent.onEvent(MarketActivity.this, "buy_gift",map);
									Toast.makeText(MarketActivity.this, "大人，您购买的"+gift.name+"，小的已经给您送到储物箱了", Toast.LENGTH_LONG).show();
									coinNumTV.setText(""+user.coinCount);
									Constants.user.coinCount=user.coinCount;
									if(UserCenterFragment.userCenterFragment!=null){
								    	UserCenterFragment.userCenterFragment.updatateInfo(true);;
									}
								}else{
									Toast.makeText(MarketActivity.this, "购买礼物失败", Toast.LENGTH_LONG).show();
								}
								if(newGift>0){
									boxNumTV.setText(""+newGift);
									boxNumTV.setVisibility(View.VISIBLE);
									giftBox.setVisibility(View.VISIBLE);
								}
								isBuying=false;
							}
						});
					}
				}).start();
			}
		});
		    realGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(!UserStatusUtil.isLoginSuccess(MarketActivity.this,popup_parent,black_layout)){
						
						return;
					}
					Intent intent=new Intent(MarketActivity.this,GiftInfoActivity.class);
					intent.putExtra("gift", giftList.get(position));
					MarketActivity.this.startActivity(intent);
				}
			});
					
		}
		/**
		 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
		 */

		private void setBlurImageBackground() {
			// TODO Auto-generated method stub
			frameLayout=(FrameLayout)findViewById(R.id.framelayout);
			viewTopWhite=(View)findViewById(R.id.top_white_view);
			
	        gridView.setOnScrollListener(new OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						// TODO Auto-generated method stub
						if(gridView.getFirstVisiblePosition()==0&&gridView.getChildAt(0).getTop()==0){
							viewTopWhite.setVisibility(View.VISIBLE);
						}else{
							if(viewTopWhite.getVisibility()!=View.GONE){
								viewTopWhite.setVisibility(View.GONE);
							}
						}
					}
					
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						// TODO Auto-generated method stub
						
					}
				});
	        realGridView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(realGridView.getFirstVisiblePosition()==0&&realGridView.getChildAt(0).getTop()==0){
						viewTopWhite.setVisibility(View.VISIBLE);
					}else{
						if(viewTopWhite.getVisibility()!=View.GONE){
							viewTopWhite.setVisibility(View.GONE);
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

		private void loadData() {
			// TODO Auto-generated method stub
			InputStream in=null;
			ArrayList<Gift> giftList=StringUtil.getGiftList(MarketActivity.this);
			if(giftList!=null){
				this.giftList=giftList;
			}else{
				this.giftList=new ArrayList<Gift>();
			}
			
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.spinner_tv1:
				showPopupWindow1();
				break;
			case R.id.spinner_tv2:
				showPopupWindow2();
				break;
			case R.id.spinner_tv3:
				showPopupWindow3();
				break;
			case R.id.back:
				
				if(isTaskRoot()){
				    if(HomeActivity.homeActivity!=null){
				    	ActivityManager am=(ActivityManager)HomeActivity.homeActivity.getSystemService(Context.ACTIVITY_SERVICE);
				    	am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
				    }else{
				    	Intent intent=new Intent(this,HomeActivity.class);
				    	this.startActivity(intent);
				    }
				}
				marketActivity=this;
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(this)){
					PetApplication.petApp.activityList.remove(this);
				}
				finish();
				System.gc();
				break;
			case R.id.gift_box_iv:
	        if(!UserStatusUtil.isLoginSuccess(MarketActivity.this,popup_parent,black_layout)){
					
					return;
				}
				Intent intent=new Intent(MarketActivity.this,MyItemActivity.class);
				MarketActivity.this.startActivity(intent);
				break;
			case R.id.more_coin:
				
				break;
			}
		}

		private void showPopupWindow1() {
			// TODO Auto-generated method stub
			View view=LayoutInflater.from(MarketActivity.this).inflate(R.layout.popup_market_1, null);
		    TextView tv1=(TextView)view.findViewById(R.id.textView1);
		    TextView tv2=(TextView)view.findViewById(R.id.textView2);
		    TextView tv3=(TextView)view.findViewById(R.id.textView3);
		    TextView tv4=(TextView)view.findViewById(R.id.textView4);
			popupWindow1=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			popupWindow1.setFocusable(true);
			popupWindow1.setBackgroundDrawable(new BitmapDrawable());
			popupWindow1.setOutsideTouchable(true);
			popupWindow1.showAsDropDown(spinnerTV1, 0, -spinnerTV1.getHeight());
			spinnerTV1.setVisibility(View.INVISIBLE);
			popupWindow1.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					spinnerTV1.setVisibility(View.VISIBLE);
				}
			});
			tv1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					loadData();
					popupWindow1.dismiss();
					spinnerTV1.setVisibility(View.VISIBLE);
					spinnerTV1.setText("全部");
					
					adapter.updateList(giftList);
					adapter.notifyDataSetChanged();
				}
			});
	        tv2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					loadData();
					popupWindow1.dismiss();
					spinnerTV1.setVisibility(View.VISIBLE);
					spinnerTV1.setText("爱心礼物");
					Object[] arrayGifts=(Object[])giftList.toArray();
					Gift temp=null;
					for(int i=0;i<arrayGifts.length-1;i++){
						for(int j=i+1;j<arrayGifts.length;j++){
							temp=(Gift)arrayGifts[i];
							if(((Gift)arrayGifts[i]).price>((Gift)arrayGifts[j]).price){
								arrayGifts[i]=arrayGifts[j];
								arrayGifts[j]=temp;	
							}
						}
					}
					giftList.removeAll(giftList);
					Gift gift=null;
					for(int i=0;i<arrayGifts.length;i++){
						gift=(Gift)arrayGifts[i];
						if(gift.add_rq>0){
							giftList.add(gift);
						}
						
					}
					adapter.updateList(giftList);
					adapter.notifyDataSetChanged();
				}
			});
	        tv3.setOnClickListener(new OnClickListener() {
		
		        @Override
		        public void onClick(View v) {
			    // TODO Auto-generated method stub
		        	loadData();
		        	popupWindow1.dismiss();
					spinnerTV1.setVisibility(View.VISIBLE);
					spinnerTV1.setText("捣乱礼物");
					Object[] arrayGifts=(Object[])giftList.toArray();
					Gift temp=null;
					for(int i=0;i<arrayGifts.length-1;i++){
						for(int j=i+1;j<arrayGifts.length;j++){
							temp=(Gift)arrayGifts[i];
							if(((Gift)arrayGifts[i]).price>((Gift)arrayGifts[j]).price){
								arrayGifts[i]=arrayGifts[j];
								arrayGifts[j]=temp;	
							}
						}
					}
					giftList.removeAll(giftList);
					Gift gift=null;
					for(int i=0;i<arrayGifts.length;i++){
						gift=(Gift)arrayGifts[i];
						if(gift.add_rq<0){
							giftList.add(gift);
						}
						
					}
					adapter.updateList(giftList);
					adapter.notifyDataSetChanged();
		        }
	        });
	        tv4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow1.dismiss();
					spinnerTV1.setVisibility(View.VISIBLE);
					spinnerTV1.setText("推荐");
				}
			});
		}

		private void showPopupWindow2() {
			// TODO Auto-generated method stub
			View view=LayoutInflater.from(MarketActivity.this).inflate(R.layout.popup_market_2, null);
		    TextView tv1=(TextView)view.findViewById(R.id.textView1);
		    TextView tv2=(TextView)view.findViewById(R.id.textView2);
		    TextView tv3=(TextView)view.findViewById(R.id.textView3);
			popupWindow2=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			popupWindow2.setFocusable(true);
			popupWindow2.setBackgroundDrawable(new BitmapDrawable());
			popupWindow2.setOutsideTouchable(true);
			popupWindow2.showAsDropDown(spinnerTV2, 0, -spinnerTV2.getHeight());
			spinnerTV2.setVisibility(View.INVISIBLE);
			popupWindow2.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					spinnerTV2.setVisibility(View.VISIBLE);
				}
			});
			tv1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow2.dismiss();
					spinnerTV2.setVisibility(View.VISIBLE);
					spinnerTV2.setText("价格");
				}
			});
	        tv2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					popupWindow2.dismiss();
					spinnerTV2.setVisibility(View.VISIBLE);
					spinnerTV2.setText("从低到高");
					Object[] arrayGifts=(Object[])giftList.toArray();
					Gift temp=null;
					for(int i=0;i<arrayGifts.length-1;i++){
						for(int j=i+1;j<arrayGifts.length;j++){
							temp=(Gift)arrayGifts[i];
							if(((Gift)arrayGifts[i]).price>((Gift)arrayGifts[j]).price){
								arrayGifts[i]=arrayGifts[j];
								arrayGifts[j]=temp;	
							}
						}
					}
					giftList.removeAll(giftList);
					for(int i=0;i<arrayGifts.length;i++){
						giftList.add((Gift)arrayGifts[i]);
					}
					adapter.updateList(giftList);
					adapter.notifyDataSetChanged();
				}
			});
	        tv3.setOnClickListener(new OnClickListener() {
		
		        @Override
		        public void onClick(View v) {
			    // TODO Auto-generated method stub
		        	
		        	popupWindow2.dismiss();
					spinnerTV2.setVisibility(View.VISIBLE);
					spinnerTV2.setText("从高到底");
					Object[] arrayGifts=(Object[])giftList.toArray();
					Gift temp=null;
					for(int i=0;i<arrayGifts.length-1;i++){
						for(int j=i+1;j<arrayGifts.length;j++){
							temp=(Gift)arrayGifts[i];
							if(((Gift)arrayGifts[i]).price<((Gift)arrayGifts[j]).price){
								arrayGifts[i]=arrayGifts[j];
								arrayGifts[j]=temp;	
							}
						}
					}
					giftList.removeAll(giftList);
					for(int i=0;i<arrayGifts.length;i++){
						giftList.add((Gift)arrayGifts[i]);
					}
					adapter.updateList(giftList);
					adapter.notifyDataSetChanged();
		        }
	        });
		}

		private void showPopupWindow3() {
			// TODO Auto-generated method stub
			View view=LayoutInflater.from(MarketActivity.this).inflate(R.layout.popup_market_3, null);
		    TextView tv1=(TextView)view.findViewById(R.id.textView1);
		    TextView tv2=(TextView)view.findViewById(R.id.textView2);
			popupWindow3=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			popupWindow3.setFocusable(true);
			popupWindow3.setBackgroundDrawable(new BitmapDrawable());
			popupWindow3.setOutsideTouchable(true);
			popupWindow3.showAsDropDown(spinnerTV3, 0, -spinnerTV3.getHeight());
			spinnerTV3.setVisibility(View.INVISIBLE);
			popupWindow3.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					spinnerTV3.setVisibility(View.VISIBLE);
				}
			});
			tv1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow3.dismiss();
					spinnerTV3.setVisibility(View.VISIBLE);
					spinnerTV3.setText("虚拟礼物");
					realGridView.setVisibility(View.GONE);
					gridView.setVisibility(View.VISIBLE);
				}
			});
	        tv2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow3.dismiss();
					spinnerTV3.setVisibility(View.VISIBLE);
					spinnerTV3.setText("现实礼物");
					realGridView.setVisibility(View.VISIBLE);
					gridView.setVisibility(View.GONE);
				}
			});
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
	

}
