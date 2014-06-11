package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.service.DownloadImagesAsyncTask;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.ShowFocusTopics;

public class OtherUserTopicActivity extends Activity implements OnClickListener{
	ImageView imageView1,imageView2,
    imageView4,imageView5,imageView6,
    imageView7;
    TextView tv3,tv2,tv1;
    View imageView3;
    UserImagesJson.Data data;
    ArrayList<UserImagesJson.Data> datas;
    ShowTopicsAdapter showTopicsAdapter;
    LinearLayout bottomLinearLayout1,bottomLinearLayout2;
    public static final int ADD_SUCCESS=1;//添加关注成功
    public static final int ADD_FAIL=2;
    public static final int DELETE_SUCCESS=3;//删除关注成功
    public static final int DELETE_FAIL=4;
    int last_id=-1;
    DownloadImagesAsyncTask asyncTask;
    Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case  Constants.MESSAGE_DOWNLOAD_IMAGES_LIST:
                if(datas==null){
					
				}
				UserImagesJson json=(UserImagesJson)msg.obj;
				if(json.datas!=null){
					
					asyncTask=new DownloadImagesAsyncTask(handler);
					UserImagesJson.Data[] arr=new UserImagesJson.Data[json.datas.size()];
					
					for(int i=0;i<json.datas.size();i++){
						final UserImagesJson.Data data=json.datas.get(i);
						data.user=OtherUserTopicActivity.this.data.user;
						datas.add(data);
					
						if(!new File(Constants.Picture_Topic_Path+File.separator+data.url).exists()){
							arr[i]=data;
						}else{
							data.path=Constants.Picture_Topic_Path+File.separator+data.url;
						}
					}
					LogUtil.i("me", "arr[].length="+arr.length);
					//下载图片
					asyncTask.execute(arr);
					showTopicsAdapter.notifyDataSetChanged();
					
				}
				break;
			case ADD_SUCCESS:
				imageView4.setImageResource(R.drawable.focus_delete);
				data.isFriend=true;
				break;
			case ADD_FAIL:
				
				break;
			case DELETE_SUCCESS:
				imageView4.setImageResource(R.drawable.focus_add);
				data.isFriend=false;
				break;
			case  DELETE_FAIL:
				
				break;
			}
    	};
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.activity_other_user_topic);
		datas=new ArrayList<UserImagesJson.Data>();
		data=(UserImagesJson.Data)getIntent().getSerializableExtra("data");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.downloadOtherUserHomepage(handler, last_id, data);
			}
		}).start();
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		imageView1=(ImageView)findViewById(R.id.imageView1);
		imageView2=(ImageView)findViewById(R.id.imageView2);
		imageView3=(View)findViewById(R.id.imageView3);
		imageView4=(ImageView)findViewById(R.id.imageView4);
		imageView5=(ImageView)findViewById(R.id.imageView5);
		imageView6=(ImageView)findViewById(R.id.imageView6);
		imageView7=(ImageView)findViewById(R.id.imageView7);
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv3=(TextView)findViewById(R.id.textView3);
		tv1.setText(""+data.user.nickName);
		
		tv2.setText(""+(data.user.classs==1?"汪星人":"喵星人"));
		tv3.setText(""+data.user.age+"岁");
		bottomLinearLayout1=(LinearLayout)findViewById(R.id.bottom_linearlayout1);
		bottomLinearLayout2=(LinearLayout)findViewById(R.id.bottom_linearlayout2);
		if(data.isFriend){
			imageView4.setImageResource(R.drawable.focus_delete);
		}else{
			imageView4.setImageResource(R.drawable.focus_add);
		}
		if(data.user.gender==1){
			//公
			imageView5.setImageResource(R.drawable.male);
		}else if(data.user.gender==2){
			//母
			imageView5.setImageResource(R.drawable.female);
		}else{
			
		}
		showTopics();
//		imageView5.setImageResource();//��������������������
//		tv1.setText(topic.user.nickName);//��������������������
//		tv2.setText(topic.user.race);//������������������������
//		tv3.setText(topic.user.age);//������������������������
		
	}
	private void initListener() {
		// TODO Auto-generated method stub
		imageView1.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView4.setOnClickListener(this);
		imageView6.setOnClickListener(this);
		imageView7.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.imageView1:
			this.finish();
			break;
		case R.id.imageView2:
			
			break;
		case R.id.imageView4:
			addOrRemoveFocus();
			break;
		case R.id.imageView6:
			
			break;
		case R.id.imageView7:
			
			break;
		}
	}
	private void addOrRemoveFocus() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(this).inflate(R.layout.item_add_or_remove_focus, null);
		bottomLinearLayout2.removeAllViews();
		bottomLinearLayout2.setVisibility(View.VISIBLE);
		bottomLinearLayout2.addView(view);
		TextView add=(TextView)view.findViewById(R.id.textView1);
		if(data.isFriend){
			add.setText("取消关注");
		}else{
			add.setText("关注");
		}
		TextView cancel=(TextView)view.findViewById(R.id.textView2);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(data.isFriend){
							HttpUtil.userDeleteFollow(data, handler);
						}else{
							HttpUtil.userAddFollow(data, handler);
						}
						
					}
				}).start();
			}
		});
	    cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
			}
		});
	}
	private void showTopics() {
		// TODO Auto-generated method stub
//		imageView6.setImageResource();
//		imageView7.setImageResource();
/*		View view=LayoutInflater.from(this).inflate(R.layout.item_user_topics, null);
		bottomLinearLayout1.removeAllViews();
		bottomLinearLayout1.addView(view);
		TextView pictures=(TextView)view.findViewById(R.id.textView1);
		TextView focus=(TextView)view.findViewById(R.id.textView3);
		TextView fans=(TextView)view.findViewById(R.id.textView5);
		LinearLayout listViewLinearLayout=(LinearLayout)view.findViewById(R.id.listview_linearLayout);*/
		
		
		//TOD0
		ShowFocusTopics showFocusTopics=new ShowFocusTopics(this, bottomLinearLayout1,datas);
		showTopicsAdapter=showFocusTopics.getAdapter();
		ListView listView=showFocusTopics.getListView();
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
							HttpUtil.downloadOtherUserHomepage(handler, data.img_id,data);
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
	}

}
