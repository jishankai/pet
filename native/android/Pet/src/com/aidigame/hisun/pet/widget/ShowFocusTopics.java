package com.aidigame.hisun.pet.widget;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.MyListView;
import com.aidigame.hisun.pet.view.PullDownView;
import com.aidigame.hisun.pet.view.PullDownView.OnPullDownListener;


public class ShowFocusTopics implements OnPullDownListener{
	public static final int WHAT_DID_LOAD_DATA = 0;
	public static final int WHAT_DID_REFRESH = 1;
	public static final int WHAT_DID_MORE = 2;
	int mode;//1 个人主页和其他人主页；2 关注列表
	Activity activity;
    HomeActivity homeActivity;
	LinearLayout parent;
	View showTopicsView;
	PullDownView pullDownView;
	ListView listView;
	ShowTopicsAdapter adapter;
	public ArrayList<UserImagesJson.Data> datas;
	public ShowFocusTopics(Activity activity,LinearLayout parent,ArrayList<UserImagesJson.Data> datas){
		this.activity=activity;
		if(activity instanceof HomeActivity)homeActivity=(HomeActivity)activity;
		this.parent=parent;
		this.mode=mode;
		this.datas=datas;
		setWidthAndHeight(activity);
		initView();
		initListener();
	}
	public ShowTopicsAdapter getAdapter(){
		return adapter;
	};
	public ListView getListView(){
		return listView;
	}
	private void initView() {
		// TODO Auto-generated method stub
		showTopicsView=LayoutInflater.from(activity).inflate(R.layout.widget_show_topics, null);
		
		pullDownView=(PullDownView)showTopicsView.findViewById(R.id.pull_down_view);
		pullDownView.setHomeActivity(homeActivity);
		pullDownView.setOnPullDownListener(this);
		
		listView=pullDownView.getListView();
		
		adapter=new ShowTopicsAdapter(activity,datas);
		listView.setAdapter(adapter);
		pullDownView.enableAutoFetchMore(true, 1);
		Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
		msg.sendToTarget();
	}
	private void loadData() {
		// TODO Auto-generated method stub
		
	}
	public void addView(){
		parent.removeAllViews();
		parent.addView(showTopicsView);
	}
	private void initListener() {
		// TODO Auto-generated method stub

	}
	public void setWidthAndHeight(Activity activity){
		DisplayMetrics dm=new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constants.screen_width=dm.widthPixels;
		Constants.screen_height=dm.heightPixels;
	}
	public boolean onRefresh=false;
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if(onRefresh)return;
		onRefresh=true;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(activity instanceof HomeActivity){
					HomeActivity ha=(HomeActivity)activity;
					ha.progressLayout.setClickable(true);
					ha.handler.sendEmptyMessage(HomeActivity.SHOW_BACKGROUND_CONTROL);
				ha.last_id=-1;
				ha.isOnRefresh=true;
				HttpUtil.downloadUserHomepage(ha.handler, ha.last_id,1,activity);
//				mUIHandler.sendEmptyMessage(WHAT_DID_REFRESH);
				}
			}
		}).start();
		
	}
	public boolean onMore=false;
	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		if(onMore){
			return;
		}
		onMore=true;
		addMore();
		
	}
	public  Handler mUIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case WHAT_DID_LOAD_DATA:{
					pullDownView.notifyDidLoad();
					break;
				}
				case WHAT_DID_REFRESH :{
					pullDownView.notifyDidRefresh();
					break;
				}
				
				case WHAT_DID_MORE:{
					// 告诉它获取更多完毕
					pullDownView.notifyDidMore();
					break;
				}
			}
			
		}
		
	};
	public void addMore(){
		 new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(activity instanceof HomeActivity){
						HomeActivity ha=(HomeActivity)activity;
						UserImagesJson.Data data=ha.datas.get(ha.datas.size()-1);
						ha.last_id=data.img_id;
						HttpUtil.downloadUserHomepage(ha.handler, data.img_id,1,activity);
						LogUtil.i("exception", "执行更行==================");
						
					}
				}
			}).start();
	}

}
