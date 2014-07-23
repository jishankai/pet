package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.FocusAndFansAdapter;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.util.UiUtil;

public class UsersListActivity extends Activity {
	FocusAndFansAdapter adapter;
	ArrayList<UserJson.Data> list;
	ListView  listView;
	String likerString;
	ImageView back;
	TextView title;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				UserJson json=(UserJson)msg.obj;
				if(json!=null&&json.datas!=null&&json.datas.size()>0){
					for(UserJson.Data data:json.datas){
						list.add(data);
					}
					adapter.notifyDataSetChanged();
				}
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_users_list);
		listView=(ListView)findViewById(R.id.users_list_listview);
		back=(ImageView)findViewById(R.id.imageView1);
		title=(TextView)findViewById(R.id.textView1);
		title.setText(getIntent().getStringExtra("title"));
		likerString=getIntent().getStringExtra("likers");
		list=new ArrayList<UserJson.Data>();
		adapter=new FocusAndFansAdapter(this, list, 3,null);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UsersListActivity.this,OtherUserTopicActivity.class);
				
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UsersListActivity.this.finish();
			}
		});
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.getOthersList(likerString, handler,UsersListActivity.this);
			}
		}).start();
	}

}
