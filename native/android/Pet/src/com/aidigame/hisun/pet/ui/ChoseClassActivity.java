package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ClassListViewAdapter;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;

public class ChoseClassActivity extends Activity {
	ListView listView;
	ArrayList<String> list;
	ClassListViewAdapter adapter;
	TextView tv;
	int classs;
	int race;
	boolean choseRace=false;
	String classsName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_chose_class);
		tv=(TextView)findViewById(R.id.textView1);
		listView=(ListView)findViewById(R.id.listView1);
		addClassName();
		adapter=new ClassListViewAdapter(this, list);
		listView.setAdapter(adapter);
		initListener();
		
	}

	public void addClassName(){
		list=new ArrayList<String>();
		list.add("喵星");
		list.add("汪星");
		list.add("其他星");
	}
	private void initListener() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(choseRace){
					choseRace(position+1);
					choseRace=false;
				}else{
					choseClass(position);
					choseRace=true;
				}
			}

		});
	}
	/**
	 * 选择种族  1喵星或2汪星
	 * @param position
	 */
	private void choseClass(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
            classsName=list.get(position);
			classs=1;
			tv.setVisibility(View.INVISIBLE);
			addCatRaceName();
			break;
		case 1:
			classsName=list.get(position);
			classs=2;
			tv.setVisibility(View.INVISIBLE);
			addDogRaceName();
			break;
		case 2:
			classs=3;
			classsName=list.get(position);
			tv.setVisibility(View.INVISIBLE);
			addOtherName();
			break;
		}
		adapter.update(list);
		adapter.notifyDataSetChanged();
	}
	private void addOtherName() {
		// TODO Auto-generated method stub
		list=new ArrayList<String>();
        SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
        if(sp.getBoolean("race", false)){
        	for(int i=1;i<=sp.getInt("other", 0);i++){
        		if(i<10){
        			list.add(sp.getString("30"+i, ""));
        		}else{
        			list.add(sp.getString("3"+i, ""));
        		}
        	}
        }
	}

	/**
	 * 选择 类别     如 萨摩耶犬
	 * @param position
	 */
	private void choseRace(int position) {
		// TODO Auto-generated method stub
		//TODO   界面跳转 确认选择  
		Intent intent=getIntent();
		String temp=null;
		if(position<10){
			temp=""+classs+"0"+position;
		}else{
			temp=""+classs+""+position;
		}
		intent.putExtra("classs", classs);
		intent.putExtra("race", temp);
		intent.putExtra("classsName", classsName);
		intent.putExtra("raceName", list.get(--position));
		setResult(RESULT_OK,intent);
		this.finish();
	}
	private void addCatRaceName() {
		// TODO Auto-generated method stub
		list=new ArrayList<String>();
        SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
        if(sp.getBoolean("race", false)){
        	for(int i=1;i<=sp.getInt("cat", 0);i++){
        		if(i<10){
        			list.add(sp.getString("10"+i, ""));
        		}else{
        			list.add(sp.getString("1"+i, ""));
        		}
        	}
        }
		
	}

	private void addDogRaceName() {
		// TODO Auto-generated method stub
		list=new ArrayList<String>();
		 SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
	        if(sp.getBoolean("race", false)){
	        	for(int i=1;i<=sp.getInt("dog", 0);i++){
	        		if(i<10){
	        			list.add(sp.getString("20"+i, ""));
	        		}else{
	        			list.add(sp.getString("2"+i, ""));
	        		}
	        	}
	        }
		
	}
}
