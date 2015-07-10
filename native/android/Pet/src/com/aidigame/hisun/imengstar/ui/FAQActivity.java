package com.aidigame.hisun.imengstar.ui;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.R;
/**
 * 常见问题界面
 * @author admin
 *
 */
public class FAQActivity extends BaseActivity {
	 FrameLayout frameLayout;
	 View viewTopWhite;
	 
	 
	 
	 TextView commonTv,sendAdviceTv;
	 WebView webView;
	 String url="http://"+Constants.IP+Constants.URL_ROOT+"r=site/faq";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);
		
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		commonTv=(TextView)findViewById(R.id.common_tv);
		sendAdviceTv=(TextView)findViewById(R.id.textView1);
		webView=(WebView)findViewById(R.id.webview);
		webView.loadUrl(url);
		WebSettings webSettings=webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
		});
		sendAdviceTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(FAQActivity.this,AdviceActivity.class);
				FAQActivity.this.startActivity(intent);
				FAQActivity.this.finish();
			}
		});
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					finish();
				/*if(listView1.getVisibility()==View.GONE){
					listView1.setVisibility(View.VISIBLE);
					commonTv.setVisibility(View.GONE);
				}else{
					if(isTaskRoot()){
						if(HomeActivity.homeActivity!=null){
							ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
							am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
						}else{
							Intent intent=new Intent(FAQActivity.this,HomeActivity.class);
							FAQActivity.this.startActivity(intent);
						}
					}
					
					if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(FAQActivity.this)){
						PetApplication.petApp.activityList.remove(FAQActivity.this);
					}
					FAQActivity.this.finish();
					System.gc();
				}*/
			}
		});
		
		
		setBlurImageBackground();
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		viewTopWhite=(View)findViewById(R.id.top_white_view);
		
        
	}
	

}
