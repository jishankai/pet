package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.StringUtil;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 经验，金币，贡献增加弹出 窗
 * @author admin
 *
 */
public class AddGoldContriExpActivity extends Activity {
	private  ImageView imageView;
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_exp_gold_contribute_add);
		imageView=(ImageView)findViewById(R.id.imageView1);
		textView=(TextView)findViewById(R.id.textView1);
		int mode=getIntent().getIntExtra("moe", 0);//0，经验；1，金币 ；2，贡献
		int num=getIntent().getIntExtra("num", 1);
		textView.setText(""+num);
		switch (mode) {
		case 0:
			imageView.setImageResource(R.drawable.exp_star);;
			break;

		case 1:
			imageView.setImageResource(R.drawable.gold_image);;
			break;

		case 2:
			imageView.setImageResource(R.drawable.contribute_heart);;
			break;
		}
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AddGoldContriExpActivity.this.finish();
			}
		}, 2000);
	}
	   @Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }

}
