package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;

public class IntroduceActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_introduce);
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		LogUtil.i("me", "x="+event.getX()+",y="+event.getY());
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int x=(int)event.getX();
			int y=(int)event.getY();
			if(x>15&&x<500&&y>570&&y<640){
				Intent intent=new Intent(this,HomeActivity.class);
				this.startActivity(intent);
				this.finish();
			}
			break;

		default:
			break;
		}
		return true;
	}

}
