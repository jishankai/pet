package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.umeng.socialize.controller.impl.w;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChargeActivity extends Activity implements OnClickListener{
	public static ChargeActivity chargeActivity;
	private ImageView backIv;
	private WebView webView;
	private LinearLayout rootLayout;
	int mode=1;//1,充值；2，买周边
	Animal animal;
	private TextView titleTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_charge);
		webView=(WebView)findViewById(R.id.webview);
		WebSettings webSettings=webView.getSettings();
		
		animal=(Animal)getIntent().getSerializableExtra("animal");
		mode=getIntent().getIntExtra("mode", 1);
		
		titleTv=(TextView)findViewById(R.id.title);
		
		rootLayout=(LinearLayout)findViewById(R.id.root_layout);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		rootLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.blur, options)));
		
		webSettings.setJavaScriptEnabled(true);
		if(mode==1){
			webView.loadUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=alipay/pay"+"&SID="+PetApplication.SID);//      http://tb.cn/dHI4DJy
			LogUtil.i("mi","充值地址：=="+"http://"+Constants.IP+Constants.URL_ROOT+"r=alipay/pay"+"&SID="+PetApplication.SID);
			webView.setWebViewClient(new WebViewClient(){
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO Auto-generated method stub
					view.loadUrl(url);
					return true;
				}
			});
		}else{
			titleTv.setText("买周边");
			webView.loadUrl(animal.tburl+"");//      http://tb.cn/dHI4DJy
			LogUtil.i("mi","买周边：=="+animal.tburl);
			webView.setWebViewClient(new WebViewClient(){
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO Auto-generated method stub
					view.loadUrl(url);
					return true;
				}
			});
		}
		
	
		
		chargeActivity=this;
		backIv=(ImageView)findViewById(R.id.back_iv);
		backIv.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_iv:
			
			chargeActivity=null;
			if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(this)){
				PetApplication.petApp.activityList.remove(this);
			}
			finish();
			System.gc();
			break;

		default:
			break;
		}
	}

}
