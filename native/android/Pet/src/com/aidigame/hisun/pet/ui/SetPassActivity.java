package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.ShowProgress;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SetPassActivity extends Activity implements OnClickListener{
	public static SetPassActivity setPassActivity;
	ImageView backIv;
	TextView sureTv,titleTv,input_note_tv,wrongNote1,wrongNote2;
	EditText surePassEt,passEt,originEt;
	String passStr,surePassStr,originPassStr;
	boolean canClick=false;
	LinearLayout progressLayout;
	RelativeLayout originPassLayout;
	ShowProgress showProgress;
	Handler handler;
	boolean isModify=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_setpass);
		setPassActivity=this;
		backIv=(ImageView)findViewById(R.id.back);
		sureTv=(TextView)findViewById(R.id.login_sure);
		surePassEt=(EditText)findViewById(R.id.pass_et2);
		passEt=(EditText)findViewById(R.id.pass_et1);
		progressLayout=(LinearLayout)findViewById(R.id.progresslayout);
		titleTv=(TextView)findViewById(R.id.title_tv);
		input_note_tv=(TextView)findViewById(R.id.input_note_tv);
		originEt=(EditText)findViewById(R.id.pass_et3);
		originPassLayout=(RelativeLayout)findViewById(R.id.origin_pass_layout);
		wrongNote1=(TextView)findViewById(R.id.wrong_note1);
		wrongNote2=(TextView)findViewById(R.id.wrong_note2);
		if(Constants.user!=null&&!StringUtil.isEmpty(Constants.user.password)){
			titleTv.setText(""+"更改密码");
			input_note_tv.setText("新密码");
			originPassLayout.setVisibility(View.VISIBLE);
			isModify=true;
		}else{
			isModify=false;
			originPassLayout.setVisibility(View.GONE);
		}
		
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		
		
		backIv.setOnClickListener(this);
		sureTv.setOnClickListener(this);
		originEt.addTextChangedListener(new TextWatcher() {
			
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
				originPassStr=originEt.getText().toString();
				if(StringUtil.isEmpty(originPassStr)){
					wrongNote1.setText("密码不能为空");
					wrongNote1.setVisibility(View.VISIBLE);
				}else{
					if(Constants.user!=null){
						if(!originPassStr.equals(Constants.user.password)){
							wrongNote1.setText("密码不正确");
							wrongNote1.setVisibility(View.VISIBLE);
						}else{
							wrongNote1.setVisibility(View.INVISIBLE);
							if((!StringUtil.isEmpty(passStr))&&passStr.equals(surePassStr)){
								canClick=true;
								sureTv.setBackgroundResource(R.drawable.login_sure_red);
							}
						}
						}
					}
				
			}
		});
		originEt.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					
				}
			}
		});
		surePassEt.addTextChangedListener(new TextWatcher() {
			
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
				if(s.length()>0){
					surePassStr=s.toString();
					if(!StringUtil.isEmpty(passStr)){
						if(!passStr.equals(surePassStr)){
//							Toast.makeText(SetPassActivity.this, "两次填写的密码不一致", Toast.LENGTH_LONG).show();
							wrongNote2.setVisibility(View.VISIBLE);
							return;
						}
						wrongNote2.setVisibility(View.INVISIBLE);
						originPassStr=originEt.getText().toString();
						if(isModify&&(!StringUtil.isEmpty(originPassStr))&&!originPassStr.equals(Constants.user.password)){
							if(!originPassStr.equals(Constants.user.password)){
								wrongNote1.setText("密码不正确");
								wrongNote1.setVisibility(View.VISIBLE);
							}else{
								canClick=true;
								sureTv.setBackgroundResource(R.drawable.login_sure_red);
							}
						}else{
							canClick=true;
							sureTv.setBackgroundResource(R.drawable.login_sure_red);
						}
						
						
					}
				}
			}
		});
		passEt.addTextChangedListener(new TextWatcher() {
			
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
				if(s.length()>0){
					passStr=s.toString();
					if(!StringUtil.isEmpty(surePassStr)){
						if(!surePassStr.equals(passStr)){
//							Toast.makeText(SetPassActivity.this, "两次填写的密码不一致", Toast.LENGTH_LONG).show();
							wrongNote2.setVisibility(View.VISIBLE);
							return;
						}
						
						originPassStr=originEt.getText().toString();
						if(isModify&&(!StringUtil.isEmpty(originPassStr))&&!originPassStr.equals(Constants.user.password)){
							if(!originPassStr.equals(Constants.user.password)){
								wrongNote1.setText("密码不正确");
								wrongNote1.setVisibility(View.VISIBLE);
							}else{
								canClick=true;
								sureTv.setBackgroundResource(R.drawable.login_sure_red);
							}
						}else{
							canClick=true;
							sureTv.setBackgroundResource(R.drawable.login_sure_red);
						}
					}
				}
			}
		});
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


		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				if(isTaskRoot()){
					if(HomeActivity.homeActivity!=null){
						ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
						am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
					}else{
						Intent intent=new Intent(this,HomeActivity.class);
						this.startActivity(intent);
					}
				}
				setPassActivity=null;
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(this)){
					PetApplication.petApp.activityList.remove(this);
				}
				finish();
				System.gc();
				break;
			case R.id.login_sure:
				if(!canClick)return;
				if(showProgress==null){
					showProgress=new ShowProgress(this, progressLayout);
				}else{
					showProgress.showProgress();
				}
				setPass();
				break;
			}
		}


		private void setPass() {
			// TODO Auto-generated method stub
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final boolean flag=HttpUtil.setPassWord(handler, passStr, SetPassActivity.this);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(flag){
								if(!isModify){
									Toast.makeText(SetPassActivity.this, "设置密码成功", Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(SetPassActivity.this, "修改密码成功", Toast.LENGTH_LONG).show();
								}
								
								Constants.user.password=passStr;
								finish();
							}else{
								if(!isModify){
									Toast.makeText(SetPassActivity.this, "设置密码失败", Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(SetPassActivity.this, "修改密码失败", Toast.LENGTH_LONG).show();
								}
							}
							if(showProgress!=null)showProgress.progressCancel();
						}
					});
				}
			}).start();
		}

}
