package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.UiUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ChargeActivity extends Activity implements OnClickListener{
	public static ChargeActivity chargeActivity;
	ImageView backIv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_charge);
		chargeActivity=this;
		backIv=(ImageView)findViewById(R.id.back_iv);
		backIv.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_iv:
			finish();
			chargeActivity=null;
			break;

		default:
			break;
		}
	}

}
