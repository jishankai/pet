package com.aidigame.hisun.pet.widget;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.bean.User;

public class ShowFocusTopics {
	
	Activity activity;
	LinearLayout parent;
	View showTopicsView;
	ListView listView;
	ShowTopicsAdapter adapter;
	ArrayList<Topic> topics;
	public ShowFocusTopics(Activity activity,LinearLayout parent){
		this.activity=activity;
		this.parent=parent;
		initView();
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
		File[] files=(new File(Environment.getExternalStorageDirectory()+File.separator+"pet")).listFiles();
		for(File f:files){
			topic=new Topic();
			topic.bmpPath=f.getAbsolutePath();
			topic.describe="wwwwwwwwwwwwwww";
			topic.likesNum=11;
			topic.time=34;
			topic.user=new User();
			topic.user.nickName="aaa";
			topic.user.race="df";
			topics.add(topic);
		}
	}

}
