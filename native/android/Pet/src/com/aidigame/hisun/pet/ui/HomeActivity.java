package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.CreateTitle;
import com.aidigame.hisun.pet.widget.ShowFocusTopics;
import com.aidigame.hisun.pet.widget.ShowWaterFull;
/**
 * ������
 * @author scx
 *
 */
public class HomeActivity extends Activity implements OnClickListener{
	TextView randomTv,favoriteTv;
	Button hostBt,cameraBt;
	LinearLayout linearLayout1;
	LinearLayout waterFullParent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_home);
		initView();
		initListener();
		
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		randomTv=(TextView)findViewById(R.id.button1);
		favoriteTv=(TextView)findViewById(R.id.button2);
		hostBt=(Button)findViewById(R.id.imageView1);
		cameraBt=(Button)findViewById(R.id.imageView2);
		linearLayout1=(LinearLayout)findViewById(R.id.linearlayout_title);
		waterFullParent=(LinearLayout)findViewById(R.id.waterfall_parent);
		//��ʾ�ٲ���
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
			new ShowWaterFull(this, waterFullParent);

			break;
		case R.id.button2:
			changeColors(R.id.button2);
			new ShowFocusTopics(this, waterFullParent,2);
			break;
		case R.id.imageView1:
			Intent intent1=new Intent(this,UserHomepageActivity.class);
			this.startActivity(intent1);
			break;
		case R.id.imageView2:
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
	
	
	
	
	
	
	
	
	

}
