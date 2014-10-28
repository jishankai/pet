package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PlayGameActivity extends Activity {
	WebView webView;
	String url="http://"+Constants.IP+Constants.URL_ROOT+"r=game/2048&sig=";
	Animal animal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_play_game);
		animal=(Animal)getIntent().getSerializableExtra("animal");
		webView=(WebView)findViewById(R.id.webview);
		String sig=HttpUtil.getMD5Value("aid="+animal.a_id);
		url=url+sig+"&SID="+Constants.SID+"&aid="+animal.a_id;
		LogUtil.i("me", "逗一逗url="+url);
		webView.loadUrl(url);
		
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}
		});
		/**
		 * 必须得加
		 */
		WebSettings webSettings = webView.getSettings(); 
		webSettings.setJavaScriptEnabled(true); 
		
		
	}

}
