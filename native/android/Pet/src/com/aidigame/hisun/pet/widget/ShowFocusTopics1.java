package com.aidigame.hisun.pet.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter2;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.OtherUserTopicActivity;
import com.aidigame.hisun.pet.ui.UserHomepageActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.MyListView;
import com.aidigame.hisun.pet.view.PullDownView;
import com.aidigame.hisun.pet.view.PullDownView.OnPullDownListener;

public class ShowFocusTopics1 implements OnPullDownListener{
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	int mode;//1 个人主页;2 其他人主页；
	Activity activity;
	LinearLayout parent;
	View showTopicsView;
	PullDownView pullDownView;
	ListView listView;
	ShowTopicsAdapter2 adapter;
	public ArrayList<UserImagesJson.Data> datas;
	public ShowFocusTopics1(Activity activity,LinearLayout parent,ArrayList<UserImagesJson.Data> datas,int mode){
		this.activity=activity;
		this.parent=parent;
		this.mode=mode;
		this.datas=datas;
		setWidthAndHeight(activity);
		initView();
		initListener();
	}
	public ShowTopicsAdapter2 getAdapter(){
		return adapter;
	};
	public ListView getListView(){
		return listView;
	}
	private void initView() {
		// TODO Auto-generated method stub
		showTopicsView=LayoutInflater.from(activity).inflate(R.layout.widget_show_topics, null);
		pullDownView=(PullDownView)showTopicsView.findViewById(R.id.pull_down_view);
		pullDownView.setOnPullDownListener(this);
		listView=pullDownView.getListView();
		adapter=new ShowTopicsAdapter2(activity,datas,mode);
		listView.setAdapter(adapter);
		pullDownView.setActivity(activity,0);
		pullDownView.enableAutoFetchMore(true, 1);
		Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
		msg.sendToTarget();
	}
	private void loadData() {
		// TODO Auto-generated method stub
		
	}
    public void addView(){
    	listView.setAdapter(adapter);
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
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(activity instanceof UserHomepageActivity){
					UserHomepageActivity ha=(UserHomepageActivity)activity;
					ha.lastImage_id=-1;
					ha.isOnRefresh=true;
					HttpUtil.downloadUserHomepage(ha.handler,ha.lastImage_id,0,activity);
					LogUtil.i("exception", "执行更行==================");
				}
				if(activity instanceof OtherUserTopicActivity){
					OtherUserTopicActivity ha=(OtherUserTopicActivity)activity;
					
					ha.isOnRefresh=true;
						ha.last_id=-1;
						HttpUtil.downloadOtherUserHomepage(ha.handler, ha.last_id,ha.data,activity);
					
				}
				mUIHandler.sendEmptyMessage(WHAT_DID_REFRESH);
			}
		}).start();
	}
	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		addMore();
	}
	private Handler mUIHandler = new Handler(){

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
				if(activity instanceof UserHomepageActivity){
					UserHomepageActivity ha=(UserHomepageActivity)activity;
					UserImagesJson.Data data=ha.datas.get(ha.datas.size()-1);
					ha.lastImage_id=data.img_id;
					HttpUtil.downloadUserHomepage(ha.handler, data.img_id,0,activity);
					LogUtil.i("exception", "执行更行==================");
				}
				if(activity instanceof OtherUserTopicActivity){
					OtherUserTopicActivity ha=(OtherUserTopicActivity)activity;
					
					
					if(ha.datas.size()>0){
						UserImagesJson.Data data=ha.datas.get(ha.datas.size()-1);
						ha.last_id=data.img_id;
						HttpUtil.downloadOtherUserHomepage(ha.handler, data.img_id,data,activity);
					}
				}
				mUIHandler.sendEmptyMessage(WHAT_DID_MORE);
			}
		}).start();
	}

}
