package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ActivityListAdapter;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.ActivityJson;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.PullDownView;
import com.aidigame.hisun.pet.view.PullDownView.OnPullDownListener;
/**
 * 活动界面
 * @author admin
 *
 */
public class ScanActivitys extends Activity implements OnPullDownListener{
	public static final int WHAT_DID_LOAD_DATA = 0;
	public static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	public static final int LOAD_ACTIVITY_LIST=301;
	public static final int UPDATE_ADAPTER=302;
	public static ScanActivitys scanActivitys;
	LinearLayout linearLayout;
	PullDownView pullDownView;
	ActivityListAdapter adapter;
	public ActivityJson json;
	int last_id=-1;
	public  Handler mUIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case WHAT_DID_LOAD_DATA:
					pullDownView.notifyDidLoad();
					break;
				
				case WHAT_DID_REFRESH :
					pullDownView.notifyDidRefresh();
					break;
				
				
				case WHAT_DID_MORE:
					// 告诉它获取更多完毕
					ActivityJson jActivityJson=(ActivityJson)msg.obj;
					if(jActivityJson==null)break;
					if(json!=null&&json.datas!=null){
						if(jActivityJson.datas!=null){
						   for(ActivityJson.Data d:jActivityJson.datas){
							   if(!json.datas.contains(d)){
								json.datas.add(d);   
							   }
						   }	
						   adapter.notifyDataSetChanged();
						}
					}else{
						json=jActivityJson;
						if(json.datas!=null){
							adapter.updateList(json.datas);
							adapter.notifyDataSetChanged();
						}
					}
					pullDownView.notifyDidMore();
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		scanActivitys=this;
		setContentView(R.layout.activity_activity);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(HomeActivity.homeActivity!=null){
////					HomeActivity.homeActivity.finish();
//				}else{
					Intent intentHome=new Intent(ScanActivitys.this,HomeActivity.class);
					ScanActivitys.this.startActivity(intentHome);
//				}
				ScanActivitys.this.finish();
			}
		});
		pullDownView=(PullDownView)findViewById(R.id.activity_listview);
		pullDownView.setOnPullDownListener(this);
		linearLayout=(LinearLayout)findViewById(R.id.linearlayout);
		ListView listView=(ListView)pullDownView.getListView();
		ArrayList<ActivityJson.Data>  list=new ArrayList<ActivityJson.Data>();
		adapter=new ActivityListAdapter(this, list);
		listView.setAdapter(adapter);
		pullDownView.enableAutoFetchMore(true, 1);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ScanActivitys.this,DetailActivity.class);
				intent.putExtra("data", json.datas.get(position));
				ScanActivitys.this.startActivity(intent);
			}
		});
		loadData();
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem>0){
//					linearLayout.setVisibility(View.GONE);
				}
				if(firstVisibleItem==0){
					linearLayout.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void loadData() {
		// TODO Auto-generated method stub
		 new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					last_id=-1;
					json=HttpUtil.loadTopicList(ScanActivitys.this,last_id);
					if(json!=null&&json.errorCode==0){
						if(json.datas!=null){
							mUIHandler.sendEmptyMessage(LOAD_ACTIVITY_LIST);
							for(int i=0;i<json.datas.size();i++){
								if(StringUtil.isEmpty(json.datas.get(i).img))continue;
								if(!StringUtil.judgeImageExists(Constants.Picture_Topic_Path+File.separator+json.datas.get(i).img)){
									String path=HttpUtil.downloadImage(Constants.ACTIVITY_IMAGE, json.datas.get(i).img, null, ScanActivitys.this);
									if(path!=null){
										json.datas.get(i).imgPath=path;
									}
								}else{
									json.datas.get(i).imgPath=Constants.Picture_Topic_Path+File.separator+json.datas.get(i).img;
								}
								
							}
							mUIHandler.sendEmptyMessage(UPDATE_ADAPTER);
							mUIHandler.sendEmptyMessage(WHAT_DID_LOAD_DATA);
						}
						
					}
				}
			}).start();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
       
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				last_id=-1;
				ActivityJson json=HttpUtil.loadTopicList(ScanActivitys.this,last_id);
				if(json!=null&&json.errorCode==0){
					if(json.datas!=null){
						for(int i=0;i<json.datas.size();i++){
							if(ScanActivitys.this.json.datas!=null&&ScanActivitys.this.json.datas.contains(json.datas.get(i)))continue;
							if(!StringUtil.judgeImageExists(Constants.Picture_Topic_Path+File.separator+json.datas.get(i).img)){
								String path=HttpUtil.downloadImage(Constants.ACTIVITY_IMAGE, json.datas.get(i).img, null, ScanActivitys.this);
								if(path!=null){
									json.datas.get(i).imgPath=path;
								}
							}else{
								json.datas.get(i).imgPath=Constants.Picture_Topic_Path+File.separator+json.datas.get(i).img;
							}
							
						}
						mUIHandler.sendEmptyMessage(UPDATE_ADAPTER);
						mUIHandler.sendEmptyMessage(WHAT_DID_REFRESH);
					}
					mUIHandler.sendEmptyMessage(WHAT_DID_REFRESH);
					
				}
				mUIHandler.sendEmptyMessage(WHAT_DID_REFRESH);
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
				last_id=json.datas.get(json.datas.size()-1).topic_id;
				ActivityJson json=HttpUtil.loadTopicList(ScanActivitys.this,last_id);
				if(json!=null&&json.errorCode==0){
					if(json.datas!=null){
						for(int i=0;i<json.datas.size();i++){
							if(ScanActivitys.this.json.datas!=null&&ScanActivitys.this.json.datas.contains(json.datas.get(i)))continue;
							if(!StringUtil.judgeImageExists(Constants.Picture_Topic_Path+File.separator+json.datas.get(i).img)){
								String path=HttpUtil.downloadImage(Constants.ACTIVITY_IMAGE, json.datas.get(i).img, null, ScanActivitys.this);
								if(path!=null){
									json.datas.get(i).imgPath=path;
								}
							}else{
								json.datas.get(i).imgPath=Constants.Picture_Topic_Path+File.separator+json.datas.get(i).img;
							}
							
						}
						mUIHandler.sendEmptyMessage(UPDATE_ADAPTER);
						Message msg=mUIHandler.obtainMessage();
						msg.what=WHAT_DID_MORE;
						msg.obj=json;
						mUIHandler.sendMessage(msg);
					}
					
				}
			}
		}).start();
	}

}
