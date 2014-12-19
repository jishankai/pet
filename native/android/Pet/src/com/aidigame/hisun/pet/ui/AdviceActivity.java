package com.aidigame.hisun.pet.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
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
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Conversation.SyncListener;
import com.umeng.fb.model.DevReply;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.UserInfo;
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
	boolean isClickable=false,contactFlag=false;
	LinearLayout progressLayout;
	ShowProgress showProgress;
	boolean isSendingMail=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_advice);
		submit=(Button) findViewById(R.id.button1);
		progressLayout=(LinearLayout)findViewById(R.id.progresslayout);
		FeedbackAgent f=new FeedbackAgent(AdviceActivity.this);
		UserInfo uInfo=f.getUserInfo();
		if(uInfo!=null)
		contact=uInfo.getContact().get("plain");
		
		adviceET=(EditText)findViewById(R.id.editText1);
		contactET=(EditText)findViewById(R.id.editText2);
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isTaskRoot()){
					if(HomeActivity.homeActivity!=null){
						ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
						am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
					}else{
						Intent intent=new Intent(AdviceActivity.this,HomeActivity.class);
						AdviceActivity.this.startActivity(intent);
					}
				}
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
				if(s.length()<=0){
					contactFlag=false;
					submit.setBackgroundResource(R.drawable.button_gray);
					return;
				}
				advice=adviceET.getText().toString();
				contact=contactET.getText().toString();
				isClickable=true;
				if(!StringUtil.isEmpty(advice)&&!StringUtil.isEmpty(contact)){
					
					if(isClickable&&contactFlag)
					submit.setBackgroundResource(R.drawable.button);
				}
			}
		});;
		if(!StringUtil.isEmpty(contact)){
			contactET.setText(contact);
			contactFlag=true;
			contactET.setEnabled(false);
		}else{
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
						if(s.length()<=0){
							contactFlag=false;
							submit.setBackgroundResource(R.drawable.button_gray);
							return;
						}
						advice=adviceET.getText().toString();
						contact=contactET.getText().toString();
						contactFlag=true;
						if(!StringUtil.isEmpty(advice)&&!StringUtil.isEmpty(contact)){
							
							if(contactFlag&&isClickable)
							submit.setBackgroundResource(R.drawable.button);
						}
					}
				});;
		}
          
         submit.setOnClickListener(new OnClickListener() {
			Toast toast;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isClickable&&contactFlag){
					if(showProgress==null){
						showProgress=new ShowProgress(AdviceActivity.this, progressLayout);
					}else{
						showProgress.showProgress();
					}
					
					FeedbackAgent f=new FeedbackAgent(AdviceActivity.this);
					Conversation c=f.getDefaultConversation();
					c.addUserReply(advice);
					c.sync(new SyncListener() {
						
						@Override
						public void onSendUserReply(List<Reply> arg0) {
							// TODO Auto-generated method stub
							LogUtil.i("mi","发送用户反馈信息");
							Toast.makeText(AdviceActivity.this, "意见信息发送成功", Toast.LENGTH_LONG).show();
							showProgress.progressCancel();
						}
						
						@Override
						public void onReceiveDevReply(List<DevReply> arg0) {
							// TODO Auto-generated method stub
							LogUtil.i("mi","huo去服务器返回信息");
						}
					});
					
					UserInfo ui=f.getUserInfo();
					if(ui==null)ui=new UserInfo();
					Map<String,String> contactMap=ui.getContact();
					if(contactMap==null)contactMap=new HashMap<String,String>();
					contactMap.put("plain", contact);
					ui.setContact(contactMap);
					f.setUserInfo(ui);
					
					
					
					
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


}
