package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ActivityListAdapter;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.ActivityJson;
import com.aidigame.hisun.pet.ui.DetailActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityFragment extends Fragment {
    View menuView;
    NewHomeActivity homeActivity;
    FrameLayout frameLayout;
	View viewTopWhite;
    
    
    public static final int WHAT_DID_LOAD_DATA = 0;
	public static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	public static final int LOAD_ACTIVITY_LIST=301;
	public static final int UPDATE_ADAPTER=302;
	LinearLayout linearLayout;
	PullToRefreshAndMoreView pullDownView;
	ListView listView;
	ActivityListAdapter adapter;
	public ActivityJson json;
	ArrayList<ActivityJson.Data>  list;
	HandleHttpConnectionException handleHttpConnectionException;
	int last_id=-1;
	public  Handler mUIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case WHAT_DID_LOAD_DATA:
					break;
				
				case WHAT_DID_REFRESH :
					break;
				
				
				case WHAT_DID_MORE:
					// 告诉它获取更多完毕
					break;
				case LOAD_ACTIVITY_LIST:
					adapter.updateList(json.datas);
					adapter.notifyDataSetChanged();
					break;
				case UPDATE_ADAPTER:
					adapter.notifyDataSetChanged();
					break;
				
			}
			
		}
		
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		menuView=inflater.inflate(R.layout.fragment_activity, null);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		initView();
		return menuView;
	}
	public void setHomeActivity(NewHomeActivity homeActivity){
		this.homeActivity=homeActivity;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	private void initView() {
		// TODO Auto-generated method stub
		menuView.findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(HomeActivity.homeActivity!=null){
////					HomeActivity.homeActivity.finish();
//				}else{
					/*Intent intentHome=new Intent(homeActivity,HomeActivity.class);
					homeActivity.startActivity(intentHome);*/
				homeActivity.toggle();
//				}
			}
		});
		pullDownView=(PullToRefreshAndMoreView)menuView.findViewById(R.id.activity_listview);
		linearLayout=(LinearLayout)menuView.findViewById(R.id.linearlayout);
		listView=(ListView)pullDownView.getListView();
		
		
		setBlurImageBackground();
		
		list=new ArrayList<ActivityJson.Data>();
		listView.setFocusable(false);
		listView.setFocusableInTouchMode(false);
		adapter=new ActivityListAdapter(homeActivity, list);
		listView.setAdapter(adapter);
		pullDownView.setListener(new PullToRefreshAndMoreListener() {
			ArrayList<ActivityJson.Data>  temp;
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				last_id=-1;
				ActivityJson json=HttpUtil.loadTopicList(homeActivity,last_id,handleHttpConnectionException.getHandler(homeActivity));
				if(json!=null&&json.errorCode==0){
					if(json.datas!=null){
						temp=new ArrayList<ActivityJson.Data>();
						for(int i=0;i<list.size();i++){
							if(!temp.contains(list.get(i))){
								temp.add(list.get(i));
							}
						}
						for(int i=0;i<json.datas.size();i++){
							if(!temp.contains(json.datas.get(i))){
								temp.add(i,json.datas.get(i));
							}
						}
						homeActivity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								adapter.updateList(temp);
								list=temp;
								adapter.notifyDataSetChanged();
								pullDownView.onRefreshFinish();
							}
						});
					}else{
						pullDownView.onRefreshFinish();
					}
					
				}else{
					pullDownView.onRefreshFinish();
				}
			}
			
			@Override
			public void onMore() {
				// TODO Auto-generated method stub
				if(list.size()>0){
					last_id=list.get(list.size()-1).topic_id;
				}else{
					last_id=-1;
				}
				
				ActivityJson json=HttpUtil.loadTopicList(homeActivity,last_id,handleHttpConnectionException.getHandler(homeActivity));
				if(json!=null&&json.errorCode==0){
					if(json.datas!=null){
						temp=new ArrayList<ActivityJson.Data>();
						for(int i=0;i<list.size();i++){
							if(!temp.contains(list.get(i))){
								temp.add(list.get(i));
							}
						}
						for(int i=0;i<json.datas.size();i++){
							if(!temp.contains(json.datas.get(i))){
								temp.add(i,json.datas.get(i));
							}
						}
						homeActivity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								adapter.updateList(temp);
								list=temp;
								adapter.notifyDataSetChanged();
								pullDownView.onMoreFinish();
							}
						});
					}else{
						pullDownView.onMoreFinish();
					}
				}else{
					pullDownView.onMoreFinish();
				}
				
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(homeActivity,DetailActivity.class);
				intent.putExtra("data", json.datas.get(position));
				homeActivity.startActivity(intent);
			}
		});
		loadData();
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)menuView.findViewById(R.id.framelayout);
		viewTopWhite=(View)menuView.findViewById(R.id.top_white_view);
		if(HomeFragment.blurBitmap==null){
			frameLayout.setBackgroundDrawable(homeActivity.getResources().getDrawable(R.drawable.blur));
		}
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(HomeFragment.blurBitmap==null){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				homeActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						frameLayout.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
						frameLayout.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
		 listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0).getTop()==0){
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
		 new Thread(new Runnable() {
			 ArrayList<ActivityJson.Data>  temp;
				@Override
				public void run() {
					// TODO Auto-generated method stub
					last_id=-1;
					json=HttpUtil.loadTopicList(homeActivity,last_id,handleHttpConnectionException.getHandler(homeActivity));
					if(json!=null&&json.errorCode==0){
						if(json.datas!=null){
							temp=new ArrayList<ActivityJson.Data>();
							for(int i=0;i<json.datas.size();i++){
								if(!temp.contains(json.datas.get(i))){
									temp.add(json.datas.get(i));
								}
							}
							homeActivity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									adapter.updateList(temp);
									list=temp;
									adapter.notifyDataSetChanged();
								}
							});
						}
						
					}
				}
			}).start();
	}
	
	

}
