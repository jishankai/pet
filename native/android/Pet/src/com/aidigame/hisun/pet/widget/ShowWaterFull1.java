package com.aidigame.hisun.pet.widget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.ActivityJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.DetailActivity;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.waterfull1.FlowTag;
import com.aidigame.hisun.pet.waterfull1.FlowView;
import com.aidigame.hisun.pet.waterfull1.LazyScrollView;
import com.aidigame.hisun.pet.waterfull1.LazyScrollView.OnScrollListener;


public class ShowWaterFull1 {
	Activity activity;
//	HomeActivity activity;
	LinearLayout parent;
	View waterfullView;
	boolean isShow=true;
    public HomeActivity homeActivity;
    DetailActivity  detailActivity;
    int mode=0;//0,活动最热图片；1，活动最新图片
    ActivityJson.Data activityData;
	public ArrayList<UserImagesJson.Data> datas;
	int real_download_num=0;//每次实际下载的图片数目，如果 低于10，就不在 下载
	//图片列表最后一张的id
	public int  last_id=-1;
	boolean halfHeight=true;
	boolean dismissProgress=false;
	private LazyScrollView waterfall_scroll;
	 LazyScrollView.OnScrollListener listener;
	private LinearLayout waterfall_container;
	private ArrayList<LinearLayout> waterfall_items;
	private Display display;
	private List<String> image_filenames;
	private final String image_path = "images";
	private Handler handler;
	private int item_width;

	private int column_count = 2;// 显示列数
	private int page_count = 25;// 每次加载30张图片

	private int current_page = 0;// 当前页数

	private int[] topIndex;
	private int[] bottomIndex;
	private int[] lineIndex;
	private int[] column_height;// 每列的高度

	private HashMap<Integer, String> pins;

	private int loaded_count = 0;// 已加载数量

	private HashMap<Integer, Integer>[] pin_mark = null;

	private HashMap<Integer, FlowView> iviews;
	private int range;
	int scroll_height;
    boolean hasUpdate=false;
    boolean hasMore=false;
	
	public  ShowWaterFull1(Activity activity,LinearLayout parent,ActivityJson.Data activityData) {
        this.activity=activity;
        this.parent=parent;
        this.activityData=activityData;
        if(activity instanceof HomeActivity)this.homeActivity=(HomeActivity)activity;
        if(activity instanceof DetailActivity){
        	this.detailActivity=(DetailActivity)activity;
        }

        if(homeActivity!=null){
//        	homeActivity.handler.sendEmptyMessage(HomeActivity.SHOW_PROGRESS);
        }
        datas=new ArrayList<UserImagesJson.Data>();
		display = activity.getWindowManager().getDefaultDisplay();
		item_width = display.getWidth() / column_count;// 根据屏幕大小计算每列大小
		InitLayout();
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
				synchronized (datas) {
					if(detailActivity!=null){
						HttpUtil.downloadActivityImagesList(handler, last_id, ShowWaterFull1.this.activityData.topic_id, detailActivity, ShowWaterFull1.this.activityData, mode);
					}else{
						HttpUtil.downloadUserHomepage(handler, last_id, 3,ShowWaterFull1.this.activity);
					}
				}
				/*try {
					Thread.sleep(200);
					synchronized (datas) {
						if(datas.size()>0&&real_download_num==30)
						HttpUtil.downloadUserHomepage(handler, datas.get(datas.size()-1).img_id, 3,ShowWaterFull1.this.activity);
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				/*
				try {
					Thread.sleep(200);
					synchronized (datas) {
						if(datas.size()>0&&real_download_num==10)
						HttpUtil.downloadUserHomepage(handler, datas.get(datas.size()-1).img_id, 3,ShowWaterFull1.this.activity);
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
			}
		}).start();
		

		

	}
    public void setMode(int mode){
    	this.mode=mode;
    }
    public LazyScrollView.OnScrollListener getScrollView(){
    	return listener;
    }
	private void InitLayout() {
		column_height = new int[column_count];
		iviews = new HashMap<Integer, FlowView>();
		pins = new HashMap<Integer, String>();
		pin_mark = new HashMap[column_count];

		this.lineIndex = new int[column_count];
		this.bottomIndex = new int[column_count];
		this.topIndex = new int[column_count];

		for (int i = 0; i < column_count; i++) {
			lineIndex[i] = -1;
			bottomIndex[i] = -1;
			pin_mark[i] = new HashMap();
		}
		waterfullView=LayoutInflater.from(activity).inflate(R.layout.widget_waterfull, null);
		waterfall_scroll = (LazyScrollView)waterfullView. findViewById(R.id.waterfall_scroll);
		if(homeActivity!=null)
		waterfall_scroll.setHomeActivityHandler(homeActivity);
		range = waterfall_scroll.computeVerticalScrollRange();//

        waterfall_scroll.getView();
		
       listener=new OnScrollListener() {

			@Override
			public void onTop() {
				// 滚动到最顶端
				
				Log.i("me", "Scroll to top");
				homeActivity.handler.sendEmptyMessage(HomeActivity.SHOW_HEADER);
				if(homeActivity!=null&&!hasUpdate){
					 new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								last_id=-1;
								if(detailActivity!=null){
									HttpUtil.downloadActivityImagesList(handler, last_id, ShowWaterFull1.this.activityData.topic_id, detailActivity, ShowWaterFull1.this.activityData, mode);
								}else{
									HttpUtil.downloadUserHomepage(handler, last_id, 3,ShowWaterFull1.this.activity);
								}
									
									
							}
						}).start();
					
				}
				
				
				
			}

			@Override
			public void onScroll() {
				
			}
			

			@Override
			public void onBottom() {
				// 滚动到最低端
//				showFooter=true;
//				showHeader=false;
//				HomeActivity.foot_last_str=StringUtil.timeFormat(System.currentTimeMillis());
//				foot_last_update_time.setText("上次加载："+HomeActivity.foot_last_str);
//				linearLayout2.setVisibility(View.VISIBLE);
				if(homeActivity!=null){
					hasMore=true;
					homeActivity.handler.sendEmptyMessage(HomeActivity.SHOW_FOOTER);
					
				}
				AddItemToContainer(current_page++, page_count);
				Log.i("me","currentpage=="+current_page);
			}

			@Override
			public void onAutoScroll(int l, int t, int oldl, int oldt) {

				// Log.d("MainActivity",
				// String.format("%d  %d  %d  %d", l, t, oldl, oldt));

				// Log.d("MainActivity", "range:" + range);
				// Log.d("MainActivity", "range-t:" + (range - t));
				scroll_height = waterfall_scroll.getMeasuredHeight();
				Log.i("me","currentpage==**************"+current_page);
				Log.i("MainActivity", "scroll_height:" + scroll_height);

				if (t > oldt) {// 向下滚动
					if (t > 2 * scroll_height) {// 超过两屏幕后

						for (int k = 0; k < column_count; k++) {

							LinearLayout localLinearLayout = waterfall_items
									.get(k);

							if (pin_mark[k].get(Math.min(bottomIndex[k] + 1,
									lineIndex[k])) <= t + 3 * scroll_height) {// 最底部的图片位置小于当前t+3*屏幕高度

								((FlowView) waterfall_items.get(k)
										.getChildAt(
												Math.min(1 + bottomIndex[k],
														lineIndex[k])))
										.Reload();

								bottomIndex[k] = Math.min(1 + bottomIndex[k],
										lineIndex[k]);

							}
							Log.d("MainActivity",
									"headIndex:" + topIndex[k]
											+ "  footIndex:" + bottomIndex[k]
											+ "  headHeight:"
											+ pin_mark[k].get(topIndex[k]));
							if (pin_mark[k].get(topIndex[k]) < t - 2
									* scroll_height) {// 未回收图片的最高位置<t-两倍屏幕高度

								int i1 = topIndex[k];
								topIndex[k]++;
								((FlowView) localLinearLayout.getChildAt(i1))
										.recycle();
								Log.d("MainActivity", "recycle,k:" + k
										+ " headindex:" + topIndex[k]);

							}
						}

					}
				} else {// 向上滚动

					for (int k = 0; k < column_count; k++) {
						LinearLayout localLinearLayout = waterfall_items.get(k);
						if (pin_mark[k].get(bottomIndex[k]) > t + 3
								* scroll_height) {
							((FlowView) localLinearLayout
									.getChildAt(bottomIndex[k])).recycle();

							bottomIndex[k]--;
						}

						if (pin_mark[k].get(Math.max(topIndex[k] - 1, 0)) >= t
								- 2 * scroll_height) {
							((FlowView) localLinearLayout.getChildAt(Math.max(
									-1 + topIndex[k], 0))).Reload();
							topIndex[k] = Math.max(topIndex[k] - 1, 0);
						}
					}

				}

			}
		};
		waterfall_scroll.setOnScrollListener(listener);
		waterfall_container = (LinearLayout) waterfullView
				.findViewById(R.id.waterfall_container);
		handler = new Handler() {

			@Override
			public void dispatchMessage(Message msg) {

				super.dispatchMessage(msg);
			}

			@Override
			public void handleMessage(Message msg) {

				// super.handleMessage(msg);

				switch (msg.what) {
				case 1:

					FlowView v = (FlowView) msg.obj;
					int w = msg.arg1;
					int h = msg.arg2;
					LogUtil.i("exception","flowview的宽度："+v.getWidth()+",高度："+v.getHeight());
					// Log.d("MainActivity",
					// String.format(
					// "获取实际View高度:%d,ID：%d,columnIndex:%d,rowIndex:%d,filename:%s",
					// v.getHeight(), v.getId(), v
					// .getColumnIndex(), v.getRowIndex(),
					// v.getFlowTag().getFileName()));
					UserImagesJson.Data data = v.getFlowTag().getData();
					String f=data.path;

					// 此处计算列值
					int columnIndex = GetMinValue(column_height);

					v.setColumnIndex(columnIndex);

					column_height[columnIndex] += h;

					pins.put(v.getId(), f);
					iviews.put(v.getId(), v);
					waterfall_items.get(columnIndex).addView(v);
					LogUtil.i("exception","waterfall_items.get("+columnIndex+")的宽度："+waterfall_items.get(columnIndex).getWidth()+",高度："+waterfall_items.get(columnIndex).getHeight());

					lineIndex[columnIndex]++;

					pin_mark[columnIndex].put(lineIndex[columnIndex],
							column_height[columnIndex]);
					bottomIndex[columnIndex] = lineIndex[columnIndex];
//					if(HomeActivity.showFooter){
						if(homeActivity!=null){
							homeActivity.handler.sendEmptyMessage(HomeActivity.HIDE_HEADER);
							
						}
//					}
					if(homeActivity!=null){
							homeActivity.handler.sendEmptyMessage(HomeActivity.HIDE_FOOTER);
							
						
					}
					break;
				case Constants.MESSAGE_DOWNLOAD_IMAGES_LIST:
					UserImagesJson json=(UserImagesJson)msg.obj;
					if(homeActivity!=null){
			        	homeActivity.handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
			        }
					if(last_id==-1&&datas.size()>0){
						if(json!=null&&json.datas!=null&&json.datas.size()>0){
							if(json.datas.get(0).img_id==datas.get(0).img_id){
								if(homeActivity!=null)
									homeActivity.handler.sendEmptyMessage(HomeActivity.HIDE_HEADER);
							
							}else{
								hasUpdate=true;
								homeActivity.handler.sendEmptyMessage(HomeActivity.UPDATE_WATERFULL);
								homeActivity.handler.sendEmptyMessage(HomeActivity.SHOW_HEADER);
								
							}
						}
						return;
					}
					real_download_num=json.datas.size();
					/*for(int i=json.datas.size()-1;i>=0;i--){
						datas.add(json.datas.get(i));
					}*/
					if(json==null||json.datas==null||json.datas.size()==0){
						if(homeActivity!=null)
							homeActivity.handler.sendEmptyMessage(HomeActivity.HIDE_FOOTER);
					}
					for(int i=0;i<=json.datas.size()-1;i++){
						datas.add(json.datas.get(i));
					}
					
					if(current_page==0){
						parent.removeAllViews();
						parent.addView(waterfullView);
						
						if(homeActivity!=null)
							homeActivity.handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
						
						AddItemToContainer(current_page++, page_count);
					}
					
					
					break;
				case 44:
					TextView tView=(TextView)msg.obj;
					tView.setText(""+msg.arg1);
					break;
				case Constants.ERROR_MESSAGE:
					ShowDialog.show((String)msg.obj, activity);
					break;
				}

			}

			@Override
			public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
				return super.sendMessageAtTime(msg, uptimeMillis);
			}
		};

		waterfall_items = new ArrayList<LinearLayout>();

		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(activity);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					item_width, LayoutParams.WRAP_CONTENT);

			if(i==0){
				itemLayout.setPadding(0, 0, 1, 0);
			}else{
				itemLayout.setPadding(1, 0, 0, 0);
			}
			itemLayout.setOrientation(LinearLayout.VERTICAL);

			itemLayout.setLayoutParams(itemParam);
			waterfall_items.add(itemLayout);
			waterfall_container.addView(itemLayout);
			LogUtil.i("exception","waterfall_container的宽度："+waterfall_container.getWidth()+",高度："+waterfall_container.getHeight());
			LogUtil.i("exception","itemLayout的宽度："+itemLayout.getWidth()+",高度："+itemLayout.getHeight());
		}
		// 第一次加载
		AddItemToContainer(current_page, page_count);
	}

	private void AddItemToContainer(int pageindex, int pagecount) {
		int currentIndex = pageindex * pagecount;

		int imagecount = 10000;// image_filenames.size();
		if(datas.size()==0)return;
		for (int i = currentIndex; i < pagecount * (pageindex + 1)
				&& loaded_count < datas.size(); i++) {
			
			AddImage(datas.get(loaded_count),
					(int) Math.ceil(loaded_count / (double) column_count),
					loaded_count);
			loaded_count++;
			boolean flag=loaded_count>=datas.size()-10;
			if(flag){
			         onMore();
				}
		}

	}

	private void AddImage(UserImagesJson.Data data, int rowIndex, int id) {

		FlowView item = new FlowView(activity);
		item.setShowWaterFull(this);
		item.setLasyScrollowView(waterfall_scroll);
		// item.setColumnIndex(columnIndex);
		item.setPadding(0, 0, 0, 4);
		item.setRowIndex(rowIndex);
		item.setId(id);
		item.setViewHandler(this.handler);
		// 多线程参数
		FlowTag param = new FlowTag();
		param.halfHeight=halfHeight;
		halfHeight=!halfHeight;
		param.setFlowId(id);
		param.setData(data);
		param.setItemWidth(item_width);

		item.setFlowTag(param);
		item.LoadImage();
		// waterfall_items.get(columnIndex).addView(item);

	}

	private int GetMinValue(int[] array) {
		int m = 0;
		int length = array.length;
		for (int i = 0; i < length; ++i) {

			if (array[i] < array[m]) {
				m = i;
			}
		}
		return m;
	}
	/**
	 * 加载更多的图片
	 */
	public void onMore(){
		new Thread(new Runnable() {
 			@Override
 			public void run() {
 				// TODO Auto-generated method stub
 				UserImagesJson.Data data=datas.get(datas.size()-1);
// 				synchronized (datas) {
 					
 					if(datas.size()>0){
 						int id=datas.get(datas.size()-1).img_id;
 						if(id!=last_id){
 							last_id=id;
 							if(detailActivity!=null){
 								HttpUtil.downloadActivityImagesList(handler, last_id, ShowWaterFull1.this.activityData.topic_id, detailActivity, ShowWaterFull1.this.activityData, mode);
 							}else{
 								HttpUtil.downloadUserHomepage(handler,last_id , 3,ShowWaterFull1.this.activity);
 							}
 							
 						}
 						
 					}
					
//				}
 				/*try {
					Thread.sleep(200);
					synchronized (datas) {
						if(datas.size()>0&&real_download_num==10)
						HttpUtil.downloadUserHomepage(handler, datas.get(datas.size()-1).img_id, 3,ShowWaterFull1.this.activity);
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 				try {
					Thread.sleep(200);
					synchronized (datas) {
						if(datas.size()>0&&real_download_num==10)
						HttpUtil.downloadUserHomepage(handler, datas.get(datas.size()-1).img_id, 3,ShowWaterFull1.this.activity);
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
 			}
 		}).start();
	}


}
