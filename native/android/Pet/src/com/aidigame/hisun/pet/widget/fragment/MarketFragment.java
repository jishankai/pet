package com.aidigame.hisun.pet.widget.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R.menu;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.MarketGridViewAdapter;
import com.aidigame.hisun.pet.adapter.MarketGridViewAdapter.ClickGiftListener;
import com.aidigame.hisun.pet.adapter.MarketRealGridViewAdapter;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.DialogGiveSbGiftActivity1;
import com.aidigame.hisun.pet.ui.GiftInfoActivity;
import com.aidigame.hisun.pet.ui.MyItemActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.umeng.analytics.MobclickAgent;
/**
 * 礼物商城界面
 * @author admin
 *
 */
public class MarketFragment extends Fragment implements PullToRefreshAndMoreListener,OnClickListener{
    public static int from=0;//0,正常的侧边栏滑动；1，从照片详情页进入；2，从王国资料页进入；3，从主界面瀑布流进入
	View view;
    NewHomeActivity homeActivity;
    FrameLayout frameLayout;
	View viewTopWhite;
	View popup_parent;
    ImageView back,giftBox;
	TextView boxNumTV,spinnerTV1,spinnerTV2,spinnerTV3,coinNumTV,moreCoinTV;
	PopupWindow popupWindow1,popupWindow2,popupWindow3;
//	PullToRefreshAndMoreView pullToRefreshAndMoreView;
	GridView gridView,realGridView;
	MarketGridViewAdapter adapter;
	MarketRealGridViewAdapter realAdapter;
	HandleHttpConnectionException handleHttpConnectionException;
	public RelativeLayout black_layout;
	List<Gift> giftList;
	int newGift=0;//新买的礼物
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.fragment_market, null);
		homeActivity=NewHomeActivity.homeActivity;
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		homeActivity=NewHomeActivity.homeActivity;
		initView();
	}
	public void setHomeActivity(NewHomeActivity activity){
		this.homeActivity=activity;
	}
	boolean isBuying=false;
	private void initView() {
		// TODO Auto-generated method stub
		/*pullToRefreshAndMoreView=(PullToRefreshAndMoreView)view.findViewById(R.id.activity_listview);
		pullToRefreshAndMoreView.setListener(this);
		listView=pullToRefreshAndMoreView.getListView();*/
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.marketList(-1, handleHttpConnectionException.getHandler(homeActivity));
			}
		}).start();*/
		popup_parent=(View)view.findViewById(R.id.popup_parent);
		back=(ImageView)view.findViewById(R.id.back);
		gridView=(GridView)view.findViewById(R.id.market_gridview);
		realGridView=(GridView)view.findViewById(R.id.market_real_gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		realGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		giftList=new ArrayList<Gift>();
		black_layout=(RelativeLayout)view.findViewById(R.id.black_layout);
		setBlurImageBackground();
		loadData();
		adapter=new MarketGridViewAdapter(homeActivity,giftList);
		gridView.setAdapter(adapter);
		realAdapter=new MarketRealGridViewAdapter(homeActivity, giftList);
		realGridView.setAdapter(realAdapter);
	    giftBox=(ImageView)view.findViewById(R.id.gift_box_iv);
	    boxNumTV=(TextView)view.findViewById(R.id.textView3);
	    boxNumTV.setVisibility(View.INVISIBLE);
//	    giftBox.setVisibility(View.INVISIBLE);
	    spinnerTV1=(TextView)view.findViewById(R.id.spinner_tv1);
	    spinnerTV2=(TextView)view.findViewById(R.id.spinner_tv2);
	    spinnerTV3=(TextView)view.findViewById(R.id.spinner_tv3);
	    coinNumTV=(TextView)view.findViewById(R.id.coin_num);
	    if(Constants.user!=null){
	    	coinNumTV.setText(""+Constants.user.coinCount);
	    }
	    moreCoinTV=(TextView)view.findViewById(R.id.more_coin);
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
			if(!UserStatusUtil.isLoginSuccess(homeActivity,popup_parent,black_layout)){
				
				return;
			}
			if(isBuying){
				Toast.makeText(homeActivity, "正在购买礼物", Toast.LENGTH_LONG).show();
				return;
			}
			if(Constants.user.coinCount-gift.price<0){
				Toast.makeText(homeActivity, "Sorry~余额不足(⊙o⊙)哦~", Toast.LENGTH_LONG).show();
				return;
			}
			isBuying=true;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					gift.buyingNum=1;
					final User user=HttpUtil.buyGift(homeActivity,gift, handleHttpConnectionException.getHandler(homeActivity));
					handleHttpConnectionException.getHandler(homeActivity).post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(user!=null){
								newGift++;
								Map<String,String> map=new HashMap<String, String>();
								map.put("level", ""+gift.level);
								map.put("name", ""+gift.name);
								map.put("id", ""+gift.no);
								MobclickAgent.onEvent(homeActivity, "buy_gift",map);
								Toast.makeText(homeActivity, "大人，您购买的"+gift.name+"，小的已经给您送到储物箱了", Toast.LENGTH_LONG).show();
								coinNumTV.setText(""+user.coinCount);
								Constants.user.coinCount=user.coinCount;
								if(MenuFragment.menuFragment!=null){
									MenuFragment.menuFragment.setViews();
								}
							}else{
								Toast.makeText(homeActivity, "购买礼物失败", Toast.LENGTH_LONG).show();
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
/*	    gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(homeActivity,popup_parent,black_layout)){
					
					return;
				}
				if(isBuying){
					Toast.makeText(homeActivity, "正在购买礼物", Toast.LENGTH_LONG).show();
					return;
				}
				if(Constants.user.coinCount-giftList.get(arg2).price<0){
					Toast.makeText(homeActivity, "Sorry~余额不足(⊙o⊙)哦~", Toast.LENGTH_LONG).show();
					return;
				}
				isBuying=true;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						giftList.get(arg2).buyingNum=1;
						final User user=HttpUtil.buyGift(homeActivity,giftList.get(arg2), handleHttpConnectionException.getHandler(homeActivity));
						handleHttpConnectionException.getHandler(homeActivity).post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(user!=null){
									newGift++;
									Toast.makeText(homeActivity, "大人，您购买的"+giftList.get(arg2).name+"，小的已经给您送到储物箱了", Toast.LENGTH_LONG).show();
									coinNumTV.setText(""+user.coinCount);
									Constants.user.coinCount=user.coinCount;
									if(MenuFragment.menuFragment!=null){
										MenuFragment.menuFragment.setViews();
									}
								}else{
									Toast.makeText(homeActivity, "购买礼物失败", Toast.LENGTH_LONG).show();
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
		});*/
	    realGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(homeActivity,popup_parent,black_layout)){
					
					return;
				}
				Intent intent=new Intent(homeActivity,GiftInfoActivity.class);
				intent.putExtra("gift", giftList.get(position));
				homeActivity.startActivity(intent);
			}
		});
				
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)view.findViewById(R.id.framelayout);
		viewTopWhite=(View)view.findViewById(R.id.top_white_view);
		
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
		ArrayList<Gift> giftList=StringUtil.getGiftList(homeActivity);
		if(giftList!=null){
			this.giftList=giftList;
		}else{
			this.giftList=new ArrayList<Gift>();
		}
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
//		pullToRefreshAndMoreView.onRefreshFinish();
	}

	@Override
	public void onMore() {
		// TODO Auto-generated method stub
//		pullToRefreshAndMoreView.onMoreFinish();
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
			
			if(NewShowTopicActivity.newShowTopicActivity!=null&&from==1){
				NewHomeActivity.homeActivity.showHomeFragment(1);
				ActivityManager am=(ActivityManager)homeActivity.getSystemService(Context.ACTIVITY_SERVICE);
				if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)
				am.moveTaskToFront(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.getTaskId(), 0);
				
				return;
			}else if(PetKingdomActivity.petKingdomActivity!=null&&from==2){
				NewHomeActivity.homeActivity.showHomeFragment(1);
                ActivityManager am=(ActivityManager)homeActivity.getSystemService(Context.ACTIVITY_SERVICE);
                if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)
				am.moveTaskToFront(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.getTaskId(), 0);
				
				return;
			}else if(from==3){
				homeActivity.showHomeFragment(1);
				return;
			}
			homeActivity.toggle();
			break;
		case R.id.gift_box_iv:
        if(!UserStatusUtil.isLoginSuccess(homeActivity,popup_parent,black_layout)){
				
				return;
			}
			Intent intent=new Intent(homeActivity,MyItemActivity.class);
			homeActivity.startActivity(intent);
			break;
		case R.id.more_coin:
			
			break;
		}
	}

	private void showPopupWindow1() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(homeActivity).inflate(R.layout.popup_market_1, null);
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
				/*Object[] arrayGifts=(Object[])giftList.toArray();
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
				adapter.notifyDataSetChanged();*/
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
		View view=LayoutInflater.from(homeActivity).inflate(R.layout.popup_market_2, null);
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
		View view=LayoutInflater.from(homeActivity).inflate(R.layout.popup_market_3, null);
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
	
	

}
