package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.MessageListAdapter;
import com.aidigame.hisun.pet.bean.TalkMessage;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.MessagJson;
import com.aidigame.hisun.pet.service.BlurImageBroadcastReceiver;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.LinearLayoutForListView;
import com.miloisbadboy.view.PullToRefreshView;
import com.miloisbadboy.view.PullToRefreshView.OnFooterRefreshListener;
import com.miloisbadboy.view.PullToRefreshView.OnHeaderRefreshListener;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 用户的消息列表
 * @author admin
 *
 */
public class MessageActivity extends Activity {
	    MessagJson json_system;
		FrameLayout frameLayout;
		View viewTopWhite;
		PullToRefreshView pullToRefreshView;
		
		LinearLayoutForListView listView;
		boolean showSystem_Mail=false;
		ArrayList<TalkMessage> datasMail;
		MessageListAdapter mailAdapter;
		HandleHttpConnectionException handleHttpConnectionException;
		public static MessageActivity messageActivity;
		LinearLayout progressLayout;
		
		TextView tv1;

		int position=-1;
		boolean isDeleting=false;
		int last_id=-1;
	    BlurImageBroadcastReceiver broadcastReceiver;
	    
	    
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	// TODO Auto-generated method stub
	    	super.onCreate(savedInstanceState);
	    	UiUtil.setScreenInfo(this);
	    	UiUtil.setWidthAndHeight(this);
	    	setContentView(R.layout.fragment_message);
			handleHttpConnectionException=HandleHttpConnectionException.getInstance();
			messageActivity=this;
			initView();
	    }
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			StringUtil.saveTalkHistory(datasMail, this);
		}
		private void initView() {
			// TODO Auto-generated method stub
			listView=(LinearLayoutForListView)findViewById(R.id.pulltorefreshandmore);
			pullToRefreshView=(PullToRefreshView)findViewById(R.id.pullToRefreshView);
			progressLayout=(LinearLayout)findViewById(R.id.progress_layout);
			pullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
				
				@Override
				public void onHeaderRefresh(PullToRefreshView view) {
					// TODO Auto-generated method stub
					datasMail=StringUtil.getTalkHistory(MessageActivity.this);
					 new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								final ArrayList<TalkMessage> system=HttpUtil.getTalkList(MessageActivity.this,-1, handleHttpConnectionException.getHandler(MessageActivity.this));
								
								
								MessageActivity.this.runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											if(system!=null&&system.size()>0){
											TalkMessage talkMessage=null;
											TalkMessage tm=null;
											for(int i=0;i<system.size();i++){
												if(datasMail.contains(system.get(i))){
													talkMessage=system.get(i);
													tm=datasMail.get(datasMail.indexOf(talkMessage));
													tm.usr_name=system.get(i).usr_name;
													tm.usr_tx=system.get(i).usr_tx;
													tm.old_new_msg_num+=system.get(i).new_msg;
													for(int j=0;j<talkMessage.msgList.size();j++){
														if(!tm.msgList.contains(talkMessage.msgList.get(j))){
															tm.msgList.add(talkMessage.msgList.get(j));
														}
													}
												}else{
													system.get(i).old_new_msg_num=system.get(i).new_msg;
													datasMail.add(system.get(i));
												}
											}
											TalkMessage[] array=new TalkMessage[datasMail.size()];
											for(int i=0;i<array.length;i++){
												array[i]=datasMail.get(i);
											}
											
											for(int i=0;i<array.length-1;i++){
												for(int j=i;j<array.length-1;j++){
													if(array[j].msgList.size()>0&&array[j+1].msgList.size()>0){
														if(array[j].msgList.get(array[j].msgList.size()-1).time<array[j+1].msgList.get(array[j+1].msgList.size()-1).time){
															talkMessage=array[j];
															array[j]=array[j+1];
															array[j+1]=talkMessage;
														}
													}else if(array[j].msgList.size()>0){
														
													}else if(array[j+1].msgList.size()>0){
														talkMessage=array[j];
														array[j]=array[j+1];
														array[j+1]=talkMessage;
													}
												}
											}
											ArrayList<TalkMessage> msgs=new ArrayList<TalkMessage>();
											for(int i=0;i<array.length;i++){
												msgs.add(array[i]);
											}
											/*
											 * 有新消息放前边
											 */
											int count=-1;
											for(int i=0;i<msgs.size();i++){
												talkMessage=msgs.get(i);
												if(talkMessage.old_new_msg_num>0){
													count++;
													msgs.remove(i);
													msgs.add(count, talkMessage);
												}
											}
											datasMail=msgs;
											mailAdapter=new MessageListAdapter(MessageActivity.this, datasMail,progressLayout);
											listView.setAdapter(mailAdapter);
										}
											pullToRefreshView.onHeaderRefreshComplete();
									}
										
									});
								
							}
						}).start();
				}
			});
			pullToRefreshView.setOnFooterRefreshListener(new OnFooterRefreshListener() {
				
				@Override
				public void onFooterRefresh(PullToRefreshView view) {
					// TODO Auto-generated method stub
					view.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							pullToRefreshView.onFooterRefreshComplete();
						}
					}, 500);
				}
			});
			frameLayout=(FrameLayout)findViewById(R.id.framelayout);
			
			
			broadcastReceiver=new BlurImageBroadcastReceiver(frameLayout, MessageActivity.this);
			IntentFilter filter=new IntentFilter(BlurImageBroadcastReceiver.BLUR_BITMAP_CHANGED);
//			homeActivity.registerReceiver(broadcastReceiver, filter);
			viewTopWhite=(View)findViewById(R.id.top_white_view);
			
			findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(isTaskRoot()){
						if(HomeActivity.homeActivity!=null){
							ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
							am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
						}else{
							Intent intent=new Intent(MessageActivity.this,HomeActivity.class);
							MessageActivity.this.startActivity(intent);
						}
					}
					MessageActivity.this.finish();
					
				}
			});
			
			
			tv1=(TextView)findViewById(R.id.textView1);
			tv1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					loadMailData();
					
					tv1.setBackgroundResource(R.drawable.button_pink);
				}
			});
			
			loadMailData();
		}

		
		/**
		 * 私人信息
		 */
		public void loadMailData(){
			last_id=-1;
			position=-1;
			showSystem_Mail=false;
			datasMail=new ArrayList<TalkMessage>();
			
			/*
			 * 加载本地聊天历史
			 */
			datasMail=StringUtil.getTalkHistory(MessageActivity.this);
			
			mailAdapter=new MessageListAdapter(MessageActivity.this, datasMail,progressLayout);
			listView.setAdapter(mailAdapter);
			 new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final ArrayList<TalkMessage> system=HttpUtil.getTalkList(MessageActivity.this,-1, handleHttpConnectionException.getHandler(MessageActivity.this));
						
						if(system!=null&&system.size()>0){
							MessageActivity.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									TalkMessage talkMessage=null;
									TalkMessage tm=null;
									for(int i=0;i<system.size();i++){
										if(datasMail.contains(system.get(i))){
											talkMessage=system.get(i);
											tm=datasMail.get(datasMail.indexOf(talkMessage));
											tm.usr_name=system.get(i).usr_name;
											tm.usr_tx=system.get(i).usr_tx;
											tm.old_new_msg_num+=system.get(i).new_msg;
											for(int j=0;j<talkMessage.msgList.size();j++){
												if(!tm.msgList.contains(talkMessage.msgList.get(j))){
													tm.msgList.add(talkMessage.msgList.get(j));
												}
											}
										}else{
											system.get(i).old_new_msg_num=system.get(i).new_msg;
											datasMail.add(system.get(i));
										}
									}
									TalkMessage[] array=new TalkMessage[datasMail.size()];
									for(int i=0;i<array.length;i++){
										array[i]=datasMail.get(i);
									}
									
									for(int i=0;i<array.length-1;i++){
										for(int j=i;j<array.length-1;j++){
											if(array[j].msgList.size()>0&&array[j+1].msgList.size()>0){
												if(array[j].msgList.get(array[j].msgList.size()-1).time<array[j+1].msgList.get(array[j+1].msgList.size()-1).time){
													talkMessage=array[j];
													array[j]=array[j+1];
													array[j+1]=talkMessage;
												}
											}else if(array[j].msgList.size()>0){
												
											}else if(array[j+1].msgList.size()>0){
												talkMessage=array[j];
												array[j]=array[j+1];
												array[j+1]=talkMessage;
											}
										}
									}
									ArrayList<TalkMessage> msgs=new ArrayList<TalkMessage>();
									for(int i=0;i<array.length;i++){
										msgs.add(array[i]);
									}
									int count=-1;
									/*
									 * 有新消息放前边
									 */
									for(int i=0;i<msgs.size();i++){
										talkMessage=msgs.get(i);
										if(talkMessage.old_new_msg_num>0){
											count++;
											msgs.remove(i);
											msgs.add(count, talkMessage);
										}
									}
									datasMail=msgs;
									mailAdapter=new MessageListAdapter(MessageActivity.this, datasMail,progressLayout);
									listView.setAdapter(mailAdapter);
								}
							});
						}
					}
				}).start();
		/*	pullToRefreshAndMoreView.setListener(new PullToRefreshAndMoreListener() {
				
				@Override
				public void onRefresh() {
//					// TODO Auto-generated method stub

					pullToRefreshAndMoreView.onRefreshFinish();
				}
				
				@Override
				public void onMore() {
//					// TODO Auto-generated method stub
	//	
					pullToRefreshAndMoreView.onMoreFinish();
				}
			});*/
		}
		public void notifyDataChanged(ArrayList<TalkMessage> datasMail){
			this.datasMail=datasMail;
			mailAdapter=new MessageListAdapter(MessageActivity.this, datasMail,progressLayout);
			listView.setAdapter(mailAdapter);
		}
		public void updateData(){
			datasMail=StringUtil.getTalkHistory(MessageActivity.this);
			mailAdapter.update(datasMail);
			mailAdapter.notifyDataSetChanged();
			 new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final ArrayList<TalkMessage> system=HttpUtil.getTalkList(MessageActivity.this,-1, handleHttpConnectionException.getHandler(MessageActivity.this));
						
						if(system!=null&&system.size()>0){
							MessageActivity.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									TalkMessage talkMessage=null;
									TalkMessage tm=null;
									for(int i=0;i<system.size();i++){
										if(datasMail.contains(system.get(i))){
											talkMessage=system.get(i);
											tm=datasMail.get(datasMail.indexOf(talkMessage));
											tm.usr_name=system.get(i).usr_name;
											tm.usr_tx=system.get(i).usr_tx;
											tm.old_new_msg_num+=system.get(i).new_msg;
											for(int j=0;j<talkMessage.msgList.size();j++){
												if(!tm.msgList.contains(talkMessage.msgList.get(j))){
													tm.msgList.add(talkMessage.msgList.get(j));
												}
											}
										}else{
											system.get(i).old_new_msg_num=system.get(i).new_msg;
											datasMail.add(system.get(i));
										}
									}
									TalkMessage[] array=new TalkMessage[datasMail.size()];
									for(int i=0;i<array.length;i++){
										array[i]=datasMail.get(i);
									}
									
									for(int i=0;i<array.length-1;i++){
										for(int j=i;j<array.length-1;j++){
											if(array[j].msgList.size()>0&&array[j+1].msgList.size()>0){
												if(array[j].msgList.get(array[j].msgList.size()-1).time<array[j+1].msgList.get(array[j+1].msgList.size()-1).time){
													talkMessage=array[j];
													array[j]=array[j+1];
													array[j+1]=talkMessage;
												}
											}else if(array[j].msgList.size()>0){
												
											}else if(array[j+1].msgList.size()>0){
												talkMessage=array[j];
												array[j]=array[j+1];
												array[j+1]=talkMessage;
											}
										}
									}
									ArrayList<TalkMessage> msgs=new ArrayList<TalkMessage>();
									for(int i=0;i<array.length;i++){
										msgs.add(array[i]);
									}
									/*
									 * 有新消息放前边
									 */
									int count=-1;
									for(int i=0;i<msgs.size();i++){
										talkMessage=msgs.get(i);
										if(talkMessage.old_new_msg_num>0){
											count++;
											msgs.remove(i);
											msgs.add(count, talkMessage);
										}
									}
									datasMail=msgs;
									mailAdapter.update(datasMail);
									mailAdapter.notifyDataSetChanged();
								}
							});
						}
					}
				}).start();
		}
		
		public void updateList(ArrayList<TalkMessage> talks){
			datasMail=talks;
			mailAdapter.update(datasMail);
			mailAdapter.notifyDataSetChanged();
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
