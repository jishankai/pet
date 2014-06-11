package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.service.DownloadImagesAsyncTask;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.ShowFocusTopics;
import com.aidigame.hisun.pet.widget.ShowWaterFull;
/**
 * ������������������
 * @author scx
 *
 */
public class HomeActivity extends Activity implements OnClickListener{
	TextView randomTv,favoriteTv;
	Button hostBt,cameraBt;
	LinearLayout linearLayout1;
	LinearLayout waterFullParent;
	public static int COMPLETE=0;
	public static HomeActivity homeActivity;
	public ArrayList<UserImagesJson.Data> datas;
	//下载完一张图片
	public  static final int MESSAGE_DOWNLOAD_IMAGE=2;
	ListView listView;
	int last_id=-1;
	DownloadImagesAsyncTask asyncTask;
	ShowTopicsAdapter showTopicsAdapter;
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==COMPLETE){
				homeActivity.finish();
			}
			switch (msg.what) {
			case Constants.MESSAGE_DOWNLOAD_IMAGES_LIST:
				final UserImagesJson json=(UserImagesJson)msg.obj;
				asyncTask=new DownloadImagesAsyncTask(handler);
				UserImagesJson.Data[] arr=new UserImagesJson.Data[json.datas.size()];
				if(json.datas!=null){
					for(int i=0;i<json.datas.size();i++){
						HomeActivity.this.datas.add(json.datas.get(i));
						if(!new File(Constants.Picture_Topic_Path+File.separator+json.datas.get(i).url).exists()){
							arr[i]=json.datas.get(i);
						}else{
							json.datas.get(i).path=Constants.Picture_Topic_Path+File.separator+json.datas.get(i).url;
						}
					}
					//下载图片
					asyncTask.execute(arr);
//					new DownloadIconAsyncTask(handler).execute(json);
					//下载用户信息
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String url=Constants.USER_DOWNLOAD_TX;
								ArrayList<UserImagesJson.Data> datas=json.datas;
								for(int j=0;j<datas.size();j++){
									HttpUtil.otherUserInfo(datas.get(j), null);
									handler.sendEmptyMessage(2);
								}
								for(int j=0;j<datas.size();j++){
									if(!new File(Constants.Picture_ICON_Path+File.separator+datas.get(j).user.iconUrl).exists()){
										boolean  flag=HttpUtil.downloadIconImage(url, datas.get(j).user.iconUrl, null);
										if(flag){
											File file=new File(Constants.Picture_ICON_Path);
											if(!file.exists()){
												file.mkdirs();
											}
											datas.get(j).user.iconPath=Constants.Picture_ICON_Path+File.separator+datas.get(j).user.iconUrl;
											
											handler.sendEmptyMessage(UserHomepageActivity.MESSAGE_DOWNLOAD_IMAGE);
											LogUtil.i("m","头像下载成功："+datas.get(j).user.iconUrl);
											
										}
									}else{
										datas.get(j).user.iconPath=Constants.Picture_ICON_Path+File.separator+datas.get(j).user.iconUrl;
										handler.sendEmptyMessage(UserHomepageActivity.MESSAGE_DOWNLOAD_IMAGE);
									}
									
						}
						}
					}).start();
					showTopicsAdapter.notifyDataSetChanged();
				}
				break;
			case MESSAGE_DOWNLOAD_IMAGE:
				showTopicsAdapter.notifyDataSetChanged();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		LogUtil.i("exception", "创建HomeActivity");
		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.activity_home);
		datas=new ArrayList<UserImagesJson.Data>();
		homeActivity=this;
		initView();
		initListener();
		//登陆成功
		if(Constants.isSuccess){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//加载用户信息
					HttpUtil.info(null,HomeActivity.this);
				}
			}).start();
		}

		
	}
	private void initView() {
		// TODO Auto-generated method stub
		randomTv=(TextView)findViewById(R.id.button1);
		favoriteTv=(TextView)findViewById(R.id.button2);
		hostBt=(Button)findViewById(R.id.imageView1);
		cameraBt=(Button)findViewById(R.id.imageView2);
		linearLayout1=(LinearLayout)findViewById(R.id.linearlayout_title);
		waterFullParent=(LinearLayout)findViewById(R.id.waterfall_parent);
		//����������������������
		new ShowWaterFull(this, waterFullParent);
//		CreateTitle createTitle=new CreateTitle(this, linearLayout1);
	}
	private void initListener(){
		randomTv.setOnClickListener(this);
		favoriteTv.setOnClickListener(this);
		hostBt.setOnClickListener(this);
		cameraBt.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			changeColors(R.id.button1);
			showTopicsAdapter=null;
			listView=null;
			new ShowWaterFull(this, waterFullParent);
            
			break;
		case R.id.button2:
			if(!judgeLoginIsSuccess())return;
			changeColors(R.id.button2);
//			judgeLoginIsSuccess();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpUtil.downloadUserHomepage(handler, last_id,1);
				}
			}).start();
			ShowFocusTopics showFocusTopics=new ShowFocusTopics(this, waterFullParent,datas);
			showTopicsAdapter=showFocusTopics.getAdapter();
			listView=showFocusTopics.getListView();
			listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&view.getLastVisiblePosition()==view.getCount()-1){
						
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								UserImagesJson.Data data=datas.get(datas.size()-1);
								HttpUtil.downloadUserHomepage(handler, data.img_id,1);
							}
						}).start();
						
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		case R.id.imageView1:
			if(!judgeLoginIsSuccess())return;
			Intent intent1=new Intent(this,UserHomepageActivity.class);
			this.startActivity(intent1);
			break;
		case R.id.imageView2:
			if(!judgeLoginIsSuccess())return;
			Intent intent2=new Intent(this,TakePictureActivity.class);
			this.startActivity(intent2);
//			this.finish();
			break;
		}
	}
	private void changeColors(int id){
		switch (id) {
		case R.id.button1:
			randomTv.setBackgroundColor(getResources().getColor(R.color.white));
			randomTv.setTextColor(getResources().getColor(R.color.orange_red));
			favoriteTv.setBackgroundColor(getResources().getColor(R.color.orange_red));
			favoriteTv.setTextColor(getResources().getColor(R.color.white));
			break;
		case R.id.button2:
			favoriteTv.setBackgroundColor(getResources().getColor(R.color.white));
			favoriteTv.setTextColor(getResources().getColor(R.color.orange_red));
			randomTv.setBackgroundColor(getResources().getColor(R.color.orange_red));
			randomTv.setTextColor(getResources().getColor(R.color.white));
			break;
		}
	}
	public boolean judgeLoginIsSuccess(){
		boolean flag=false;
		LogUtil.i("me", "判断是否登录成功="+Constants.isSuccess);
		if(Constants.isSuccess){
			flag=true;
		}else{
			Intent intent=new Intent(this,UnregisterNoteActivity.class);
			this.startActivity(intent);
		}
		return flag;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.i("exception", "销毁HomeActivity");
	}
	
	
	
	
	
	
	
	
	

}
