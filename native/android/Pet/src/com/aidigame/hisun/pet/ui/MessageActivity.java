package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.MailSystemAdapter;
import com.aidigame.hisun.pet.adapter.MessageListAdapter;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.MessagJson;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.PullDownView;
import com.aidigame.hisun.pet.view.PullDownView.OnPullDownListener;

public class MessageActivity extends Activity implements OnPullDownListener{
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	public static final int UPDATE_MAIL_SYSTEM=3;
	ListView listView;
	PullDownView pullDownView1,pullDownView2;
	MessageListAdapter adapter;
	ArrayList<MessagJson.DataSystem> datas;
	MessagJson json_system;
	ArrayList<MessagJson.DataSystem> dataSystems;
	MailSystemAdapter mailSystemAdapter;
	TextView tv1,tv2;
	boolean showSystem_Mail=false;
	int position=-1;
	boolean isDeleting=false;
	int last_id=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_message);
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if(HomeActivity.homeActivity!=null){
//					HomeActivity.homeActivity.finish();
				}else{*/
					Intent intentHome=new Intent(MessageActivity.this,HomeActivity.class);
					MessageActivity.this.startActivity(intentHome);
//				}
				MessageActivity.this.finish();
			}
		});
        findViewById(R.id.imageView2).setOnClickListener(new OnClickListener() {
			Toast toast;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(position==-1)return;
				if(isDeleting){
					if(toast!=null)toast.cancel();
					toast=Toast.makeText(MessageActivity.this, "正在删除消息", Toast.LENGTH_LONG);
					toast.show();
					return;
				}
				isDeleting=true;
				MessagJson.DataSystem data=null;
				if(showSystem_Mail){
					if(dataSystems!=null&&listView!=null&&position!=-1){
						
						data=(MessagJson.DataSystem)listView.getItemAtPosition(position);
						
					}
				}else{
                     if(datas!=null&&listView!=null&&position!=-1){
						
						data=(MessagJson.DataSystem)listView.getItemAtPosition(position);
						
					}
				}
				    
					final MessagJson.DataSystem dataSystem=data;
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								boolean flag=HttpUtil.deleteMail(dataSystem);
								if(flag){
									runOnUiThread(new Runnable() {
										public void run() {
											if(showSystem_Mail){
											dataSystems.remove(dataSystem);
											mailSystemAdapter.notifyDataSetChanged();
											}else{
												datas.remove(dataSystem);
												adapter.notifyDataSetChanged();
											}
											isDeleting=false;
											position=-1;
										}
									});
									
									
								}else{
									isDeleting=false;
								}
							}
						}).start();
				
			}
		});
		
		
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadMailData();
				
				tv1.setTextColor(getResources().getColor(R.color.orange_red));
				tv2.setTextColor(getResources().getColor(R.color.gray_deep));
			}
		});
		tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadSystemMailData();
				
				tv2.setTextColor(getResources().getColor(R.color.orange_red));
				tv1.setTextColor(getResources().getColor(R.color.gray_deep));
			}
		});
		loadMailData();
		
	}
	private void loadMailData(){
		showSystem_Mail=false;
		last_id=-1;
		position=-1;
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final MessagJson system=HttpUtil.getMailList(-1, false);
				if(system!=null&&system.dataSystems!=null){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							adapter.update(system.dataSystems);
							datas=system.dataSystems;
							mUIHandler.sendEmptyMessage(UPDATE_MAIL_SYSTEM);
						}
					});
				}
			}
		}).start();
		pullDownView1=(PullDownView)findViewById(R.id.pull_down_view);
		pullDownView1.setVisibility(View.VISIBLE);
		if(pullDownView2!=null)
		pullDownView2.setVisibility(View.GONE);
		pullDownView1.setActivity(this,0);
		datas=new ArrayList<MessagJson.DataSystem>();
		pullDownView1.setOnPullDownListener(this);
		listView=null;
		listView=pullDownView1.getListView();
		adapter=new MessageListAdapter(this, datas);
		listView.setAdapter(adapter);
		pullDownView1.enableAutoFetchMore(true, 1);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(view.isSelected()){
					view.setSelected(false);
				}else{
					view.setSelected(true);
					MessageActivity.this.position=position;
				}
				
			}
		});
		Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
		msg.sendToTarget();
	}

	private void loadSystemMailData() {
		// TODO Auto-generated method stub
		position=-1;
		last_id=-1;
		pullDownView2=(PullDownView)findViewById(R.id.pull_down_view2);
		pullDownView2.setVisibility(View.VISIBLE);
		if(pullDownView1!=null)
		pullDownView1.setVisibility(View.GONE);
		pullDownView2.setActivity(this,0);
		showSystem_Mail=true;
		dataSystems=new ArrayList<MessagJson.DataSystem>();
		listView=null;
		pullDownView2.setOnPullDownListener(this);
		listView=pullDownView2.getListView();
		mailSystemAdapter=new MailSystemAdapter(this, dataSystems);
		listView.setAdapter(mailSystemAdapter);
		pullDownView2.enableAutoFetchMore(true, 1);
		Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
		msg.sendToTarget();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final MessagJson system=HttpUtil.getMailList(-1, true);
				
				if(system!=null&&system.dataSystems!=null){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mailSystemAdapter.update(system.dataSystems);
							dataSystems=system.dataSystems;
							mUIHandler.sendEmptyMessage(UPDATE_MAIL_SYSTEM);
						}
					});
				}
			}
		}).start();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(view.isSelected()){
					view.setSelected(false);
				}else{
					view.setSelected(true);
					MessageActivity.this.position=position;
				}
				
			}
		});
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		last_id=-1;
            new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final MessagJson system=HttpUtil.getMailList(-1, showSystem_Mail);
				
				if(system!=null&&system.dataSystems!=null){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							if(showSystem_Mail){
								if(mailSystemAdapter!=null)
								mailSystemAdapter.update(system.dataSystems);
								dataSystems=system.dataSystems;
								mUIHandler.sendEmptyMessage(UPDATE_MAIL_SYSTEM);
							}else{
								if(adapter!=null){
									adapter.update(system.dataSystems);
									adapter.notifyDataSetChanged();
								}
								
							}
							mUIHandler.sendEmptyMessage(WHAT_DID_REFRESH);
							
						}
					});
				}
			}
		}).start();
		
	}
	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		if(showSystem_Mail){
			if(dataSystems!=null&&dataSystems.size()>=1){
				last_id=Integer.parseInt(dataSystems.get(dataSystems.size()-1).mail_id);
			}
		}else{
			if(datas!=null&&datas.size()>=1){
				last_id=Integer.parseInt(datas.get(datas.size()-1).mail_id);
			}
		}
		
		   new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final MessagJson system=HttpUtil.getMailList(last_id, showSystem_Mail);
					
					if(system!=null&&system.dataSystems!=null){
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								if(showSystem_Mail){
									if(mailSystemAdapter!=null)
									mailSystemAdapter.update(system.dataSystems);
									if(dataSystems!=null){
										for(int i=0;i<system.dataSystems.size();i++){
											if(!dataSystems.contains(system.dataSystems.get(i))){
												dataSystems.add(system.dataSystems.get(i));
											}
										}
									}
									dataSystems=system.dataSystems;
									mUIHandler.sendEmptyMessage(UPDATE_MAIL_SYSTEM);
								}else{
									if(adapter!=null){
										if(datas!=null){
											for(int i=0;i<system.dataSystems.size();i++){
												if(!datas.contains(system.dataSystems.get(i))){
													datas.add(system.dataSystems.get(i));
												}
											}
										}
										adapter.update(datas);
										adapter.notifyDataSetChanged();
									}
									
								}
								mUIHandler.sendEmptyMessage(WHAT_DID_MORE);
								
							}
						});
					}else{
						mUIHandler.sendEmptyMessage(WHAT_DID_MORE);
					}
				}
			}).start();
	}
	private Handler mUIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case WHAT_DID_LOAD_DATA:{
					if(showSystem_Mail){
						pullDownView2.notifyDidLoad();
					}else{
						pullDownView1.notifyDidLoad();
					}

					
					break;
				}
				case WHAT_DID_REFRESH :{
                    if(showSystem_Mail){
                    	pullDownView2.notifyDidRefresh();
					}else{
						pullDownView1.notifyDidRefresh();
					}
					
					break;
				}
				
				case WHAT_DID_MORE:{
					// 告诉它获取更多完毕
                    if(showSystem_Mail){
                    	pullDownView2.notifyDidMore();
					}else{
						pullDownView1.notifyDidMore();
					}
					
					break;
				}
				case UPDATE_MAIL_SYSTEM:
					if(showSystem_Mail&&mailSystemAdapter!=null){
						mailSystemAdapter.notifyDataSetChanged();
					}
					if(!showSystem_Mail&&adapter!=null){
						adapter.notifyDataSetChanged();
					}
					
					break;
			}
			
		}
		
	};

}
