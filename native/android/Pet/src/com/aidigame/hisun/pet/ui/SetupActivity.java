package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.UiUtil;

public class SetupActivity extends Activity implements OnClickListener{
	ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.activity_setup);
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		back=(ImageView)findViewById(R.id.imageView1);
	}
	private void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageView1:
			this.finish();
			break;
		}
	}

}
