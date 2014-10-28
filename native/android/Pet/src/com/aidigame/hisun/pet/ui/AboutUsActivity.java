package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;

import android.app.Activity;
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
		focusWeiXin.setOnClickListener(this);
		focusXinLang.setOnClickListener(this);
		versionTv.setText("V"+StringUtil.getAPKVersionName(this));
		findViewById(R.id.imageView1).setOnClickListener(this);
	}
	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		viewTopWhite=(View)findViewById(R.id.top_white_view);
		if(HomeFragment.blurBitmap==null){
			frameLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.blur));
		}
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(HomeFragment.blurBitmap==null){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						frameLayout.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
						frameLayout.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
		 /*listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0).getTop()==0){
						viewTopWhite.setVisibility(View.VISIBLE);
					}else{
						if(viewTopWhite.getVisibility()!=View.GONE){
							viewTopWhite.setVisibility(View.GONE);
						}
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
				}
			});*/
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.imageView1:
			this.finish();
			break;

		case R.id.textView4:
			Uri uri=Uri.parse("http://weibo.com/u/5252680717");
			Intent intent=new Intent(Intent.ACTION_VIEW,uri);
			startActivity(intent);
			break;

		case R.id.textView5:
		/*	if(Constants.api==null){
				boolean flag=WeixinShare.regToWeiXin(this);
				if(!flag){
					Toast.makeText(this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
					
					return;
				}
			}*/
//				WeixinShare.shareHttpLink("", "");
			
			
			
			
			/*Intent intent=new Intent(Intent.ACTION_VIEW);
			intent.putExtra(Intent.EXTRA_SUBJECT, "share");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("http://weixin.qq.com/r/uXQmPtnEdIQLrZ1d9yGr"));
			intent.setPackage("com.tencent.mm");
			this.startActivity(intent);*/
			break;
		}
	}

}
