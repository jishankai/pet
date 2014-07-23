package com.aidigame.hisun.pet.widget;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.FocusAndFansAdapter;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.ui.UserHomepageActivity;
import com.aidigame.hisun.pet.view.PullDownView;
import com.aidigame.hisun.pet.view.PullDownView.OnPullDownListener;

public class ShowFocusAndFansList implements OnPullDownListener{
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	UserHomepageActivity activity;
	LinearLayout parent;
	PullDownView pullDownView;
	ListView listView;
	int mode;
	public ShowFocusAndFansList(UserHomepageActivity activity,LinearLayout parent,int mode){
		this.activity=activity;
		this.parent=parent;
		this.mode=mode;
		if(mode==1){
			showFocus(parent);
		}else{
			showFans(parent);
		}
		pullDownView.setOnPullDownListener(this);
		pullDownView.enableAutoFetchMore(true, 1);
		pullDownView.setActivity(activity,1);
		Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
		msg.sendToTarget();
	}
	   /**
     * 显示用户关注的人群
     * @param linearLayout
     */
	private void showFocus(LinearLayout linearLayout){
//		showFocusTopics=null;
//		activity.handler.sendEmptyMessage(activity.SHOW_PROGRESS);
		View view=LayoutInflater.from(activity).inflate(R.layout.widget_show_topics, null);
		pullDownView=(PullDownView)view.findViewById(R.id.pull_down_view);
		activity.last_focus_id=-1;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.followList(activity.handler, activity.last_focus_id,1,activity);
			}
		}).start();
		
		activity.listView=(ListView)pullDownView.getListView();
		listView=activity.listView;
		linearLayout.removeAllViews();
		linearLayout.addView(view);
		activity.userDatas=new ArrayList<UserJson.Data>();
		activity.focusAdapter=new FocusAndFansAdapter(activity, activity.userDatas, 1,activity.handler);
		listView.setAdapter(activity.focusAdapter);
		
	}
	/**
	 * 显示粉丝
	 * @param linearLayout
	 */
	private void showFans(LinearLayout linearLayout){
//		focusAdapterMode=2;
//		showFocusTopics=null;
//		activity.handler.sendEmptyMessage(activity.SHOW_PROGRESS);
		activity.view=LayoutInflater.from(activity).inflate(R.layout.widget_show_topics, null);
		pullDownView=(PullDownView)activity.view.findViewById(R.id.pull_down_view);
		activity.listView=(ListView)pullDownView.getListView();
		activity.last_focus_id=-1;
		listView=activity.listView;
         new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.followList(activity.handler, activity.last_focus_id,2,activity);
			}
		}).start();
		linearLayout.removeAllViews();
		linearLayout.addView(activity.view);
       
		activity.userDatas=new ArrayList<UserJson.Data>();
		activity.focusAdapter=new FocusAndFansAdapter(activity, activity.userDatas, 2,activity.handler);
		listView.setAdapter(activity.focusAdapter);
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				synchronized (activity.userDatas) {
					activity.last_focus_id=-1;
					
					if(mode==1){
						HttpUtil.followList(activity.handler, activity.last_focus_id,1,activity);
					}
					if(mode==2){
						HttpUtil.followList(activity.handler, activity.last_focus_id,2,activity);
					}
					
					mUIHandler.sendEmptyMessage(WHAT_DID_REFRESH);
				}
				
			}
		}).start();
			
	}
	@Override
	public void onMore() {
		// TODO Auto-generated method stub
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(activity.userDatas.size()>0){
//					activity.last_focus_id=activity.userDatas.get(activity.userDatas.size()-1).user.userId;

					if(mode==1){
						HttpUtil.followList(activity.handler, activity.last_focus_id,1,activity);
					}
					if(mode==2){
						HttpUtil.followList(activity.handler, activity.last_focus_id,2,activity);
					}
				}
				mUIHandler.sendEmptyMessage(WHAT_DID_MORE);
				
			}
		}).start();
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
//					activity.userDatas.removeAll(activity.userDatas);
					activity.focusAdapter.notifyDataSetChanged();
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
}
