package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
/**
 * 意见反馈
 * @author admin
 *
 */
public class AdviceActivity extends Activity {
	LinearLayout frameLayout;
	
	
	Button submit;
	EditText adviceET;
	EditText contactET;
	String advice,contact;
	boolean isClickable=false;
	boolean isSendingMail=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_advice);
		setBlurImageBackground(); 
		submit=(Button) findViewById(R.id.button1);
		adviceET=(EditText)findViewById(R.id.editText1);
		contactET=(EditText)findViewById(R.id.editText2);
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AdviceActivity.this.finish();
			}
		});
		adviceET.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				advice=adviceET.getText().toString();
				contact=contactET.getText().toString();
				if(StringUtil.isEmpty(advice)&&StringUtil.isEmpty(contact)){
					/*isClickable=true;
					submit.setBackgroundResource(R.drawable.button);*/
				}
			}
		});;
           contactET.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				advice=adviceET.getText().toString();
				contact=contactET.getText().toString();
				if(!StringUtil.isEmpty(advice)&&!StringUtil.isEmpty(contact)){
					isClickable=true;
					submit.setBackgroundResource(R.drawable.button);
				}
			}
		});;
         submit.setOnClickListener(new OnClickListener() {
			Toast toast;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isClickable){
					Intent intent=new Intent(Intent.ACTION_SEND);
					intent.setType("message/rfc822");
					intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"shichunxiang0@sina.cn"});
					
					intent.putExtra(Intent.EXTRA_SUBJECT, "提个意见");
					intent.putExtra(Intent.EXTRA_TEXT, advice+"/r/n"+contact);
					AdviceActivity.this.startActivity(Intent.createChooser(intent, "发送"));
					/*if(isSendingMail){
						if(toast!=null)toast.cancel();
						toast=Toast.makeText(AdviceActivity.this, "正在发送邮件", Toast.LENGTH_LONG);
						toast.show();
						return;
					}
					isSendingMail=true;
					final Mail mail=new Mail("shichunxiang0@sina.cn","android123456");
					String[] toMail=new String[]{"994381607@qq.com"};
					mail.setTo(toMail);
					mail.setFrom("shichunxiang");
					mail.setSubject("意见反馈");
					mail.setBody(advice+"/r/n"+contact);
//						 m.addAttachment("/sdcard/filelocation"); 
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								try {
									final boolean flag=mail.send();
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											if(flag){
												if(toast!=null)toast.cancel();
												toast=Toast.makeText(AdviceActivity.this, "邮件发送成功", Toast.LENGTH_LONG);
												toast.show();
												isSendingMail=false;
											}else{
												if(toast!=null)toast.cancel();
												toast=Toast.makeText(AdviceActivity.this, "邮件发送失败", Toast.LENGTH_LONG);
												toast.show();
												isSendingMail=false;
											}
										}
									});
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									isSendingMail=false;
								}
							}
						}).start();*/
				}
			}
		});
	
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(LinearLayout)findViewById(R.id.framelayout);
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
	}


}
