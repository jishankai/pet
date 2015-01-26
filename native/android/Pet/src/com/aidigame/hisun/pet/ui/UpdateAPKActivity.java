package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.StringAdapter;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.service.DownLoadApkService;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
/**
 * 更新软件 弹出窗
 * @author admin
 *
 */
public class UpdateAPKActivity extends Activity {
      TextView titleTv,cancelTv,sureTv;
      int mode;//1，摇一摇；2，捣捣乱;
      LinearLayout noteLayout;
//      TextView infoTv;
      Handler handler;
      boolean isSending=false;
//      ScrollView scrollview;
      RelativeLayout progress_view_layout,progressLayout;
      View progressView;
      StringAdapter adapter;
      ArrayList<String> list;
      ListView listView;
     public  Handler progressHandler=new Handler(){
    	  public void handleMessage(android.os.Message msg) {
    		  switch (msg.what) {
			case 1:
				//进行中
				long length=msg.arg1;
	    		  int w=progress_view_layout.getMeasuredWidth();
	    		  int width=(int) (length*1f/Constants.apk_size*w);
	    		  RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)progressView.getLayoutParams();
	    		  if(param==null){
	    			  param=new RelativeLayout.LayoutParams(width,RelativeLayout.LayoutParams.WRAP_CONTENT);
	    		  }
	    		  param.width=width;
	    		  progressView.setLayoutParams(param);
				break;
	case 2:
		//完成
		String path=Constants.Picture_Root_Path+File.separator+"pet.apk";
		Intent intent=new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)),  
                "application/vnd.android.package-archive");  
		UpdateAPKActivity.this.startActivity(intent);
		
		
				break;
	case 3:
		//失败
		Toast.makeText(UpdateAPKActivity.this, "更新失败", Toast.LENGTH_LONG).show();
		break;
			}
    		  
    	  };
      };
      
      public static UpdateAPKActivity updateAPKActivity;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	UiUtil.setScreenInfo(this);
    	UiUtil.setWidthAndHeight(this);
    	updateAPKActivity=this;
    	setContentView(R.layout.activity_update_apk);
    	mode=getIntent().getIntExtra("mode", 1);
    	handler=HandleHttpConnectionException.getInstance().getHandler(this);
    	noteLayout=(LinearLayout)findViewById(R.id.note_layout);
//    	infoTv=(TextView)findViewById(R.id.info_tv);
    	progress_view_layout=(RelativeLayout)findViewById(R.id.progress_view_layout);
    	progressLayout=(RelativeLayout)findViewById(R.id.progress_layout);
    	progressView=findViewById(R.id.progress_view);
//    	scrollview=(ScrollView)findViewById(R.id.listview);
    	listView=(ListView)findViewById(R.id.listview);
    	list=new ArrayList<String>();
//    	list.add("1.");
//    	list.add("2.");
//    	list.add("3.");
//    	list.add("4.");
    	adapter=new StringAdapter(this, list);
    	listView.setAdapter(adapter);
    	initView();
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String info=HttpUtil.updateVersionInfo(handler, Constants.VERSION, UpdateAPKActivity.this);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(!StringUtil.isEmpty(info)){
							String[] strs=info.split("&");
							LogUtil.i("mi", "info="+info);
//							infoTv.setText(info);
							if(strs!=null){
								for(int i=0;i<strs.length;i++){
									list.add(strs[i]);
								}
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										adapter.update(list);
										adapter.notifyDataSetChanged();
									}
								});
								
							}
						}
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
    	/*finish();
    	if(NewHomeActivity.homeActivity!=null){
    		NewHomeActivity.homeActivity.finish();
    	}*/
    }

	private void initView() {
		titleTv=(TextView)findViewById(R.id.textView1);
		cancelTv=(TextView)findViewById(R.id.cancel_tv);
		sureTv=(TextView)findViewById(R.id.sure_tv);
		
		/*
		 * 强制更新，只显示 更新按钮
		 * 
		 * 非强制，显示两个按钮
		 */
//		
		if(Constants.VERSION!=null&&StringUtil.canUpdate(this, Constants.VERSION)){
			cancelTv.setVisibility(View.GONE);
//			noteLayout.setVisibility(View.VISIBLE);
		}
		cancelTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 是否强制更新
				 * 否，关掉对话框，回到应用
				 * 是   弹框提示必须更新，如果不更新则程序关闭
				 */
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(UpdateAPKActivity.this)){
					PetApplication.petApp.activityList.remove(UpdateAPKActivity.this);
				}
				finish();
				System.gc();
			}
		});
		sureTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UpdateAPKActivity.this,DownLoadApkService.class);
				UpdateAPKActivity.this.startService(intent);
//				scrollview.setVisibility(View.INVISIBLE);
				noteLayout.setVisibility(View.VISIBLE);
				if(Constants.VERSION!=null&&StringUtil.canUpdate(UpdateAPKActivity.this, Constants.VERSION)){
//					scrollview.setVisibility(View.INVISIBLE);
//					noteLayout.setVisibility(View.VISIBLE);
				}else{
					finish();
				}
				
			}
		});
	}
	boolean isFinish=false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
//		return super.onKeyDown(keyCode, event);
		if(!isFinish){
			return true;
		}else{
			return false;
		}
	}

	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }
}
