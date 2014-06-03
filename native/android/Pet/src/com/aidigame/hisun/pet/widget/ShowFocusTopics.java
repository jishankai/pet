package com.aidigame.hisun.pet.widget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;

public class ShowFocusTopics {
	int mode;
	Activity activity;
	LinearLayout parent;
	View showTopicsView;
	ListView listView;
	ShowTopicsAdapter adapter;
	ArrayList<Topic> topics;
	public ShowFocusTopics(Activity activity,LinearLayout parent,int mode){
		this.activity=activity;
		this.parent=parent;
		this.mode=mode;
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		showTopicsView=LayoutInflater.from(activity).inflate(R.layout.widget_show_topics, null);
		parent.removeAllViews();
		parent.addView(showTopicsView);
		listView=(ListView)showTopicsView.findViewById(R.id.listView1);
		loadData();
		adapter=new ShowTopicsAdapter(activity,topics);
		listView.setAdapter(adapter);
	}
	private void loadData() {
		// TODO Auto-generated method stub
		topics=new ArrayList<Topic>();
		Topic topic=null;
		File[] files=null;
		if(mode==1){
			File file=new File(Constants.Picture_Path);
			if(!file.exists()){
				files=(new File(Environment.getExternalStorageDirectory()+File.separator+"pet")).listFiles();
			}else{
				files=(new File(Constants.Picture_Path)).listFiles();
			}
			
		}else{
			files=(new File(Environment.getExternalStorageDirectory()+File.separator+"pet")).listFiles();
		}
		Context context=activity;
		SharedPreferences sp=activity.getPreferences(Context.MODE_WORLD_WRITEABLE);
		HashMap<String,String> map=(HashMap<String,String>)sp.getAll();
		for(File f:files){
			topic=new Topic();
			topic.bmpPath=f.getAbsolutePath();
			if(mode==1){
				topic.describe=map.get(f.getName());
			}else{
				topic.describe="wwwwwwwwwwwwwww";
			}
			
			topic.likesNum=11;
			topic.time=34;
			topic.user=new User();
			topic.user.nickName="aaa";
			topic.user.race="df";
			topics.add(topic);
		}
	}
	private void initListener() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				/*switch (view.getId()) {
				case R.id.relativeLayout1:
					LogUtil.i("me", "R.id.relativeLayout1");
					break;
				case R.id.imageView2:
					LogUtil.i("me", "R.id.R.id.imageView2");
					break;
				case R.id.heart_linearLayout:
					LogUtil.i("me", "R.id.R.id.imageView2");
					break;
				}*/
			}
		});
	}

}
