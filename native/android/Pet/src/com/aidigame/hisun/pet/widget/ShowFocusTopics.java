package com.aidigame.hisun.pet.widget;

import java.util.ArrayList;

import android.app.Activity;
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

public class ShowFocusTopics {
	int mode;
	Activity activity;
	LinearLayout parent;
	View showTopicsView;
	ListView listView;
	ShowTopicsAdapter adapter;
	ArrayList<UserImagesJson.Data> datas;
	public ShowFocusTopics(Activity activity,LinearLayout parent,ArrayList<UserImagesJson.Data> datas){
		this.activity=activity;
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
		parent.removeAllViews();
		parent.addView(showTopicsView);
		listView=(ListView)showTopicsView.findViewById(R.id.listView1);
		adapter=new ShowTopicsAdapter(activity,datas);
		listView.setAdapter(adapter);
	}
	private void loadData() {
		// TODO Auto-generated method stub
		
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
	public void setWidthAndHeight(Activity activity){
		DisplayMetrics dm=new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constants.screen_width=dm.widthPixels;
		Constants.screen_height=dm.heightPixels;
	}

}
