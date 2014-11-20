package com.aidigame.hisun.pet.ui;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.MailListViewAdapter;
import com.aidigame.hisun.pet.bean.TalkMessage;
import com.aidigame.hisun.pet.bean.TalkMessage.Msg;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.MessagJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.AudioRecordAndPlayer;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
import com.aidigame.hisun.pet.widget.fragment.MessageFragment;
/**
 * 聊天界面
 * @author admin
 *
 */
public class ChatActivity extends Activity implements OnClickListener{
	FrameLayout frameLayout;
	View viewTopWhite;
	
	
	ImageView back;
	TextView whoTv,sendTv;
	ListView listView;
	EditText editText;
	boolean isSendingMail=false;
	User user;
	int toId;
	HandleHttpConnectionException handleHttpConnectionException;
	TalkMessage mailList=new TalkMessage() ;
	ArrayList<TalkMessage> talks=new ArrayList<TalkMessage>();
	MailListViewAdapter mailListViewAdapter=new MailListViewAdapter(this, mailList);
	Thread chatThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_chat);
        user=(User)getIntent().getSerializableExtra("user");
        talks=StringUtil.getTalkHistory(this);
       
        if(user==null){
        	mailList=(TalkMessage)getIntent().getSerializableExtra("msg");
        	
        	user=new User();
        	user.userId=mailList.usr_id;
        	user.u_nick=mailList.usr_name;
        	user.u_iconUrl=mailList.usr_name;
        }else{
        	mailList.usr_id=user.userId;
        	mailList.usr_name=user.u_nick;
        	mailList.usr_tx=user.u_iconUrl;
        }
        
        toId=user.userId;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		initView();
		
		
	}
    boolean stopThread=false;
	private void initView() {
		// TODO Auto-generated method stub
		chatThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int talk_id=HttpUtil.getTalk_id(ChatActivity.this,mailList.usr_id,handleHttpConnectionException.getHandler(ChatActivity.this));
				if(talk_id==-1){
					ChatActivity.this.finish();
					return;
				}
				mailList.position=talk_id;
				if(talks.contains(mailList)){
					
		        	int index=talks.indexOf(mailList);
		        	mailList=talks.get(index);
		        }else{
		        	talks.add(mailList);
		        }
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mailListViewAdapter.update(mailList);
						mailListViewAdapter.notifyDataSetChanged();
					}
				});
				while(!stopThread){
					
					final ArrayList<TalkMessage> msgs=HttpUtil.getTalkList(ChatActivity.this,-1, handleHttpConnectionException.getHandler(ChatActivity.this));
					TalkMessage talkMessage=null;
					if(msgs!=null&&msgs.size()>0){
						
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								int index=-1;
								for(int i=0;i<msgs.size();i++){
									
									if(talks.contains(msgs.get(i))){
										index=talks.indexOf(msgs.get(i));
										talks.get(index).usr_tx=msgs.get(i).usr_tx;
										talks.get(index).usr_name=msgs.get(i).usr_name;
										for(int j=0;j<msgs.get(i).msgList.size();j++){
											if(!talks.get(index).msgList.contains(msgs.get(i).msgList.get(j))){
												talks.get(index).msgList.add(msgs.get(i).msgList.get(j));
											}
										}
									}else{
										talks.add(msgs.get(i));
									}
							    }
								//排序
								mailList.sortMsgList();
								mailListViewAdapter.update(mailList);
								mailListViewAdapter.notifyDataSetChanged();
								if(mailList.msgList.size()>1)
								listView.setSelection(mailList.msgList.size()-1);
//								listView.smoothScrollToPosition(mailList.msgList.size()-1);
									
//								
								
							}
						});
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		chatThread.start();
		back=(ImageView)findViewById(R.id.back);
		whoTv=(TextView)findViewById(R.id.who_tv);
		whoTv.setText("和"+user.u_nick+"的聊天");
		sendTv=(TextView)findViewById(R.id.send_comment);
		listView=(ListView)findViewById(R.id.mail_listview);
		editText=(EditText)findViewById(R.id.edit_comment);
		mailListViewAdapter=new MailListViewAdapter(this, mailList);
		listView.setAdapter(mailListViewAdapter);
		handleHttpConnectionException.getHandler(ChatActivity.this).postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				listView.setSelection(mailList.msgList.size()-1);
			}
		},100);
		setBlurImageBackground();
	    
	    back.setOnClickListener(this);
	    sendTv.setOnClickListener(this);
	    editText.setEnabled(true);
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	mailList.old_new_msg_num=0;
    	if(chatThread!=null&&chatThread.isAlive()){
			stopThread=true;
		}
    	StringUtil.saveTalkHistory(talks, this);
    	if(MessageFragment.messageFragment!=null){
    		MessageFragment.messageFragment.updateList(talks);
    	}
    	super.onDestroy();
    	
    }
	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		viewTopWhite=(View)findViewById(R.id.top_white_view);
		
		 listView.setOnScrollListener(new OnScrollListener() {
				
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
			});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			if(chatThread!=null&&chatThread.isAlive()){
				stopThread=true;
			}
			if(NewHomeActivity.homeActivity!=null){
				ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
				am.moveTaskToFront(NewHomeActivity.homeActivity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
			}else{
				Intent intent=new Intent(ChatActivity.this,NewHomeActivity.class);
				ChatActivity.this.startActivity(intent);
			}
			this.finish();
			break;
		case R.id.send_comment:
			sendMail();
			break;
		}
	}
	
	public void sendMail(){
		LogUtil.i("scroll","send_click");
		if(isSendingMail){
			Toast.makeText(ChatActivity.this, "正在发送私信", Toast.LENGTH_LONG).show();
			return;
		}
		String comment=editText.getText().toString();
		comment=comment.trim();
		if(StringUtil.isEmpty(comment)){
			Toast.makeText(ChatActivity.this, "发送私信内容不能不空。", Toast.LENGTH_LONG).show();
			return;
		}
		final Msg dataSystem=new Msg();
		dataSystem.content=comment;
		dataSystem.time=System.currentTimeMillis()/1000;
		dataSystem.from=Constants.user.userId;
		isSendingMail=true;
		new Thread(new Runnable() {
			Toast toast;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//测试发送消息api
				final boolean flag=HttpUtil.sendMail(toId, dataSystem.content);
//				final int talk_id=HttpUtil.sendMail(toId, dataSystem.content);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(flag){
//							if(mailList.position==-1)mailList.position=talk_id;
							mailList.msgList.add(dataSystem);
							mailList.sortMsgList();
							mailListViewAdapter.update(mailList);
							mailListViewAdapter.notifyDataSetChanged();
							if(toast!=null){
								toast.cancel();
							}
							mailList.new_msg++;
							toast=Toast.makeText(ChatActivity.this, "发送私信成功。", Toast.LENGTH_LONG);
							toast.show();
							editText.setText("");
							getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
							if(mailList.msgList.size()>1)
							listView.smoothScrollToPosition(mailList.msgList.size()-1);
						}else{
							if(toast!=null){
								toast.cancel();
							}
							toast=Toast.makeText(ChatActivity.this, "发送私信失败。", Toast.LENGTH_LONG);
							toast.show();
						}
						isSendingMail=false;
					}
				});
				
			}
		}).start();
	
		
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
