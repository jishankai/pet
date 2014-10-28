package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.MessageListAdapter;
import com.aidigame.hisun.pet.bean.TalkMessage;
import com.aidigame.hisun.pet.bean.TalkMessage.Msg;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.MessagJson;
import com.aidigame.hisun.pet.service.BlurImageBroadcastReceiver;
import com.aidigame.hisun.pet.ui.ChatActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;

public class MessageFragment extends Fragment {
    View menuView;
    NewHomeActivity homeActivity;
    MessagJson json_system;
	FrameLayout frameLayout;
	View viewTopWhite;
	
//	PullToRefreshAndMoreView pullToRefreshAndMoreView;
	PullAndLoadListView listView;
	boolean showSystem_Mail=false;
	ArrayList<TalkMessage> datasMail;
	MessageListAdapter mailAdapter;
	HandleHttpConnectionException handleHttpConnectionException;
	
	
	
	TextView tv1,tv2;

	int position=-1;
	boolean isDeleting=false;
	int last_id=-1;
    BlurImageBroadcastReceiver broadcastReceiver;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		menuView=inflater.inflate(R.layout.fragment_message, null);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		return menuView;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initView();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		homeActivity.unregisterReceiver(broadcastReceiver);
	}
    public void setHomeActivity(NewHomeActivity homeActivity){
    	this.homeActivity=homeActivity;
    }
	private void initView() {
		// TODO Auto-generated method stub
//		pullToRefreshAndMoreView=(PullToRefreshAndMoreView)menuView.findViewById(R.id.pulltorefreshandmore);
//		pullToRefreshAndMoreView.canPullRefresh=false;
//		listView=pullToRefreshAndMoreView.getListView();
		listView=(PullAndLoadListView)menuView.findViewById(R.id.pulltorefreshandmore);
		listView.setDivider(null);
		
		frameLayout=(FrameLayout)menuView.findViewById(R.id.framelayout);
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
				homeActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						frameLayout.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
						frameLayout.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
		
		broadcastReceiver=new BlurImageBroadcastReceiver(frameLayout, homeActivity);
		IntentFilter filter=new IntentFilter(BlurImageBroadcastReceiver.BLUR_BITMAP_CHANGED);
//		homeActivity.registerReceiver(broadcastReceiver, filter);
		viewTopWhite=(View)menuView.findViewById(R.id.top_white_view);
		
		menuView.findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				homeActivity.toggle();
			}
		});
		
		
		tv1=(TextView)menuView.findViewById(R.id.textView1);
		tv2=(TextView)menuView.findViewById(R.id.textView2);
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadMailData();
				
				tv1.setBackgroundResource(R.drawable.button_pink);
				tv2.setBackgroundDrawable(null);
			}
		});
		tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadSystemMailData();
				tv2.setBackgroundResource(R.drawable.button_pink);
				tv1.setBackgroundDrawable(null);
			}
		});
		
		listView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				datasMail=StringUtil.getTalkHistory(homeActivity);
//				mailAdapter.update(datasMail);
//				mailAdapter.notifyDataSetChanged();
//				listView.onRefreshComplete();
				 new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							final ArrayList<TalkMessage> system=HttpUtil.getTalkList(homeActivity,-1, handleHttpConnectionException.getHandler(homeActivity));
							
							
								homeActivity.runOnUiThread(new Runnable() {
									
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
										mailAdapter.update(datasMail);
										mailAdapter.notifyDataSetChanged();
									}
										listView.onRefreshComplete();
								}
								});
							
						}
					}).start();
			}
		});
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				listView.onLoadMoreComplete();
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
		datasMail=StringUtil.getTalkHistory(homeActivity);
		
		mailAdapter=new MessageListAdapter(homeActivity, datasMail);
		listView.setAdapter(mailAdapter);
		listView.setDivider(null);
		 new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final ArrayList<TalkMessage> system=HttpUtil.getTalkList(homeActivity,-1, handleHttpConnectionException.getHandler(homeActivity));
					
					if(system!=null&&system.size()>0){
						homeActivity.runOnUiThread(new Runnable() {
							
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
								mailAdapter.update(datasMail);
								mailAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}).start();
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
	/*	pullToRefreshAndMoreView.setListener(new PullToRefreshAndMoreListener() {
			
			@Override
			public void onRefresh() {
//				// TODO Auto-generated method stub

				pullToRefreshAndMoreView.onRefreshFinish();
			}
			
			@Override
			public void onMore() {
//				// TODO Auto-generated method stub
//	
				pullToRefreshAndMoreView.onMoreFinish();
			}
		});*/
	}
	public void updateData(){
		datasMail=StringUtil.getTalkHistory(homeActivity);
		mailAdapter.update(datasMail);
		mailAdapter.notifyDataSetChanged();
		 new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final ArrayList<TalkMessage> system=HttpUtil.getTalkList(homeActivity,-1, handleHttpConnectionException.getHandler(homeActivity));
					
					if(system!=null&&system.size()>0){
						homeActivity.runOnUiThread(new Runnable() {
							
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
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		StringUtil.saveTalkHistory(datasMail, homeActivity);
	   /* SharedPreferences sp=homeActivity.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
    	Editor editor=sp.edit();
    	if(datasMail.size()>0){
    		String numString=sp.getString("talk_num", null);
        	if(!StringUtil.isEmpty(numString)){
        		String[] strs=numString.split(",");
        		boolean has=false;
        		for(int j=0;j<datasMail.size();j++){
        			
        		
        		for(int i=0;i<strs.length;i++){
        			if(!strs[i].equals(""+datasMail.get(j).position)){
        				numString+=""+datasMail.get(j).position+",";
        				break;
        			}
        		}
        		}
        	}else{
        		numString="";
        		for(int i=0;i<datasMail.size();i++){
        			numString+=""+datasMail.get(i).position+",";
        		}
        		
        	}
        	editor.putString("talk_num", numString);
        	try {
            	JSONObject jo=new JSONObject();
            	JSONObject j1=new JSONObject();
            	JSONArray ja=new JSONArray();
            	TalkMessage mailList=null;
            	String json1=null;
            	for(int j=0;j<datasMail.size();j++){
            		mailList=datasMail.get(j);
            		if(mailList.position==0)continue;
            		for(int i=0;i<mailList.msgList.size();i++){
                		j1=new JSONObject();
                		
            				j1.put("time", mailList.msgList.get(i).time);
            				j1.put("from", mailList.msgList.get(i).from);
            				j1.put("content", mailList.msgList.get(i).content);
            				ja.put(j1);
            			
                	}
                	jo.put("msg", ja);
                	jo.put("usr_id", mailList.usr_id);
                	jo.put("usr_name", mailList.usr_name);
                	jo.put("usr_tx", mailList.usr_tx);
                	editor.putString("talk_"+mailList.position,jo.toString());
            	}
            	} catch (JSONException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        	editor.commit();
    	}*/
	}
    /**
     * 系统消息
     */
	private void loadSystemMailData() {
//		// TODO Auto-generated method stub
//		last_id=-1;
//		position=-1;
//		showSystem_Mail=true;
//		requestList=new ArrayList<MessagJson.DataSystem>();
//		requestAdapter=new MessageListAdapter(homeActivity, requestList);
//		listView.setAdapter(requestAdapter);
//		listView.setDivider(null);
//		 new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					final MessagJson system=HttpUtil.getMailList(-1, false);
//					
//					if(system!=null&&system.dataSystems!=null){
//						homeActivity.runOnUiThread(new Runnable() {
//							
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								for(int i=0;i<system.dataSystems.size();i++){
//									system.dataSystems.get(i).isRequest=true;
//								}
//								requestAdapter.update(system.dataSystems);
//								requestList=system.dataSystems;
//								requestAdapter.notifyDataSetChanged();
//							}
//						});
//					}
//				}
//			}).start();
//		pullToRefreshAndMoreView.setListener(new PullToRefreshAndMoreListener() {
//			
//			@Override
//			public void onRefresh() {
//				// TODO Auto-generated method stub
//				last_id=-1;
//				final MessagJson system=HttpUtil.getMailList(-1, true);
//				if(system!=null&&system.dataSystems!=null){
//					if(system.dataSystems.size()>0){
//						if(requestList.size()>0&&requestList.get(0).mail_id.equals(system.dataSystems.get(0).mail_id)){
//							
//							pullToRefreshAndMoreView.onRefreshFinish();
//							return;
//						}
//					}
//					homeActivity.runOnUiThread(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							requestAdapter.update(system.dataSystems);
//							requestList=system.dataSystems;
//							requestAdapter.notifyDataSetChanged();
//							pullToRefreshAndMoreView.onRefreshFinish();
//						}
//					});
//				}else{
//					pullToRefreshAndMoreView.onRefreshFinish();
//				}
//			}
//			
//			@Override
//			public void onMore() {
//				// TODO Auto-generated method stub
//				if(requestList.size()>0){
//					last_id=Integer.parseInt(requestList.get(requestList.size()-1).mail_id);
//				}else{
//					last_id=-1;
//				}
//				final MessagJson system=HttpUtil.getMailList(-1, true);
//				if(system!=null&&system.dataSystems!=null){
//					for(int i=0;i<system.dataSystems.size();i++){
//						if(!requestList.contains(system.dataSystems.get(i))){
//							requestList.add(system.dataSystems.get(i));
//						}
//					}
//					
//					homeActivity.runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							requestAdapter.update(requestList);
//							requestAdapter.notifyDataSetChanged();
//							pullToRefreshAndMoreView.onMoreFinish();
//						}
//					});
//				}else{
//					pullToRefreshAndMoreView.onMoreFinish();
//				}
//			}
//		});
	}
	
	

}
