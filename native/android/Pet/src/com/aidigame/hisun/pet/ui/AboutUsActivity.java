package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.WeixinShare;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

public class AboutUsActivity extends Activity implements OnClickListener{
	FrameLayout frameLayout;
	View viewTopWhite;
	TextView agreementTV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_about_us);
		setBlurImageBackground();
		TextView  versionTv=(TextView)findViewById(R.id.textView3);
		TextView focusXinLang=(TextView)findViewById(R.id.textView4);
		TextView focusWeiXin=(TextView)findViewById(R.id.textView5);
		TextView mailBox=(TextView)findViewById(R.id.textView8);
		TextView copyRight=(TextView)findViewById(R.id.textView10);
		agreementTV=(TextView)findViewById(R.id.textView6);
		focusWeiXin.setOnClickListener(this);
		focusXinLang.setOnClickListener(this);
		agreementTV.setOnClickListener(this);
		versionTv.setText("V"+StringUtil.getAPKVersionName(this));
		findViewById(R.id.imageView1).setOnClickListener(this);
	}
	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		viewTopWhite=(View)findViewById(R.id.top_white_view);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.imageView1:
			if(isTaskRoot()){
				if(HomeActivity.homeActivity!=null){
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
				}else{
					Intent intent=new Intent(this,HomeActivity.class);
					this.startActivity(intent);
				}
			}
			this.finish();
			break;

		case R.id.textView4:
			Uri uri=Uri.parse("http://weibo.com/u/5252680717");
			Intent intent=new Intent(Intent.ACTION_VIEW,uri);
			startActivity(intent);
			break;

		case R.id.textView5:
		
			break;
		case R.id.textView6:
			Intent intent2=new Intent(this,WarningDialogActivity.class);
			intent2.putExtra("mode", 7);
			this.startActivity(intent2);
			break;
		}
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
