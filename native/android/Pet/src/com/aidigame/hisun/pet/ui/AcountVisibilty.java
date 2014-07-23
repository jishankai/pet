package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.UiUtil;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AcountVisibilty extends Activity implements OnClickListener{
	ImageView back,acount_1,acount_2,acount_3;
	RelativeLayout relativeLayout1,relativeLayout2,relativeLayout3;
	ImageView acount_image1,acount_image2,acount_image3;
	int acount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_acount_show);
		back=(ImageView) findViewById(R.id.imageView1);
		acount_1=(ImageView) findViewById(R.id.acount_1);
		acount_2=(ImageView) findViewById(R.id.acount_2);
		acount_3=(ImageView) findViewById(R.id.acount_3);
		acount_image1=(ImageView) findViewById(R.id.acount_visibilty_imageView2);
		acount_image2=(ImageView) findViewById(R.id.acount_visibilty_imageView3);
		acount_image3=(ImageView) findViewById(R.id.acount_visibilty_imageView4);
		relativeLayout1=(RelativeLayout)findViewById(R.id.acount_visibilty_relativelayout1);
		relativeLayout2=(RelativeLayout)findViewById(R.id.acount_visibilty_relativelayout2);
		relativeLayout3=(RelativeLayout)findViewById(R.id.acount_visibilty_relativelayout3);
		
		relativeLayout1.setClickable(true);
		relativeLayout2.setClickable(true);
		relativeLayout3.setClickable(true);
		acount=getIntent().getIntExtra("acount", 1);
		SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		acount_1.setVisibility(View.INVISIBLE);
		acount_2.setVisibility(View.INVISIBLE);
		acount_3.setVisibility(View.INVISIBLE);
		setImageGray();
		switch (sp.getInt(Constants.ACOUNT_VISIVAL_AREAR, 2)) {
		case 1:
			acount_1.setVisibility(View.VISIBLE);
			acount_image1.setImageResource(R.drawable.all_people_yellow);
			break;
		case 2:
			acount_2.setVisibility(View.VISIBLE);
			acount_image2.setImageResource(R.drawable.private_yellow);
			break;
		case 3:
			acount_3.setVisibility(View.VISIBLE);
			acount_image3.setImageResource(R.drawable.lock_yellow);
			break;
		}
		initListener();
	}
	private void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(this);
		/*acount_1.setOnClickListener(this);
		acount_2.setOnClickListener(this);
		acount_3.setOnClickListener(this);*/
		relativeLayout1.setOnClickListener(this);
		relativeLayout2.setOnClickListener(this);
		relativeLayout3.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		Editor editor=sp.edit();
		switch (v.getId()) {
		case R.id.acount_visibilty_relativelayout1:
			setImageGray();
			acount_image1.setImageResource(R.drawable.all_people_yellow);
			acount_1.setVisibility(View.VISIBLE);
			acount_2.setVisibility(View.INVISIBLE);
			acount_3.setVisibility(View.INVISIBLE);
			editor.putInt(Constants.ACOUNT_VISIVAL_AREAR, 1);
			break;
		case R.id.acount_visibilty_relativelayout2:
			setImageGray();
			acount_image2.setImageResource(R.drawable.private_yellow);
			acount_1.setVisibility(View.INVISIBLE);
			acount_2.setVisibility(View.VISIBLE);
			acount_3.setVisibility(View.INVISIBLE);
			editor.putInt(Constants.ACOUNT_VISIVAL_AREAR, 2);
			break;
		case R.id.acount_visibilty_relativelayout3:
			setImageGray();
			acount_image3.setImageResource(R.drawable.lock_yellow);
			acount_1.setVisibility(View.INVISIBLE);
			acount_2.setVisibility(View.INVISIBLE);
			acount_3.setVisibility(View.VISIBLE);
			editor.putInt(Constants.ACOUNT_VISIVAL_AREAR, 3);
			break;
		case R.id.imageView1:
			this.finish();
			break;
		case R.id.acount_1:
	
			break;
		case R.id.acount_2:
			
			break;
		case R.id.acount_3:
			
			break;
		}
		editor.commit();

	}
	private void setImageGray(){
		acount_image1.setImageResource(R.drawable.all_people_gray);
		acount_image2.setImageResource(R.drawable.private_gray);
		acount_image3.setImageResource(R.drawable.lock_gray);
	}

}
