package com.example.picturedemo.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.example.picturedemo.R;
import com.example.picturedemo.adapter.GridViewAdapter;
import com.example.picturedemo.bean.Picture;
import com.example.picturedemo.util.ScanUtil;

public class ChosePictureActivity extends Activity implements OnClickListener{
	GridView gridView;
	GridViewAdapter adapter;
	Button sureBt,cancelBt;
	Intent data;
	int position=-1;
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				adapter.update(MainActivity.list);
				adapter.notifyDataSetChanged();
				
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        
		setContentView(R.layout.activity_chose_picture);
		data=getIntent();
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		gridView=(GridView)findViewById(R.id.gridLayout1);
		sureBt=(Button)findViewById(R.id.button1);
		cancelBt=(Button)findViewById(R.id.button2);
		if(MainActivity.list==null){
			adapter=new GridViewAdapter(this, new ArrayList<Picture>());
		}else{
			adapter=new GridViewAdapter(this, MainActivity.list);
		}
		
		gridView.setAdapter(adapter);
		
	}
	private void initListener() {
		// TODO Auto-generated method stub
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ChosePictureActivity.this.position=position;
				data.putExtra("position", position);
				setResult(0, data);
				ChosePictureActivity.this.finish();
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.button1:
			break;
		case R.id.button2:
			position=-1;
			break;
		}
		data.putExtra("position", position);
		setResult(10, data);
		this.finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==KeyEvent.KEYCODE_BACK){
			position=-1;
			data.putExtra("position", position);
			setResult(1,data);
		}
		this.finish();
		return true;
	}

}
