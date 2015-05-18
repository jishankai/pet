package com.aidigame.hisun.imengstar.ui;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.R;

public class TakePictureBackground extends Activity {
	public static final int MODE_REGISTER=30;//注册
	public static final int MODE_CHANGE_ICON=31;//更改头像
	public static final int MODE_TOPIC=32;//发表图片
	public static final int  MODE_ACTIVITY=33;//参加活动
	public static final int  MODE_BEG_FOOD=34;//参加活动
	int mode=-1;
	String filename;
	String activityName;
	Animal animal;
	int isBeg;
	private long star_id;
	private int topic_id=-1;
	private String topic_name;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			if(new File(Constants.Picture_Camera+File.separator+filename).exists()){
				if(msg.obj!=null){
					Uri uri=(Uri)msg.obj;
					Intent intent3=new Intent(TakePictureBackground.this,SubmitPictureActivity.class);
					intent3.setData(uri);
					intent3.putExtra("mode", 1);
					intent3.putExtra("isBeg", isBeg);
					intent3.putExtra("path", Constants.Picture_Camera+File.separator+filename);
					intent3.putExtra("activity", activityName);
					intent3.putExtra("topic_id", getIntent().getIntExtra("topic_id", -1));
					intent3.putExtra("topic_name", getIntent().getStringExtra("topic_name") );
					intent3.putExtra("animal", animal);
					intent3.putExtra("star_id", getIntent().getLongExtra("star_id", -1));
					TakePictureBackground.this.startActivity(intent3);
				}
				
				
				
				TakePictureBackground.this.finish();
				System.gc();
			}else{
				handler.sendMessageAtTime(msg, 50);
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		LogUtil.i("me", "创建TakePictureBackground");
		setContentView(R.layout.activity_take_picture_background);
		mode=getIntent().getIntExtra("mode", -1);
		activityName=getIntent().getStringExtra("activity");
		animal=(Animal)getIntent().getSerializableExtra("animal");
		isBeg=getIntent().getIntExtra("isBeg",0);
		star_id=getIntent().getLongExtra("star_id", -1);
		topic_id=getIntent().getIntExtra("topic_id", -1);
		topic_name=getIntent().getStringExtra("topic_name");
		switch (mode) {
		case -1:
			
			
			this.finish();
			System.gc();
			break;
		}
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file=new File(Constants.Picture_Camera);
		if(!file.exists()){
			file.mkdirs();
		}
		filename=""+System.currentTimeMillis()+".png";
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.Picture_Camera,filename)));
		this.startActivityForResult(intent, 22);
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){

//				if(new File(Constants.Picture_Camera+File.separator+filename).exists()){

//				}
			if(requestCode==11){
				LogUtil.i("me", "msg.what================");
				if(data.getBooleanExtra("cancel", false)){
					Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file=new File(Constants.Picture_Camera);
					if(!file.exists()){
						file.mkdirs();
					}
					filename=""+System.currentTimeMillis()+".png";
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.Picture_Camera,filename)));
					this.startActivityForResult(intent, 22);
					return;
				}else{
					Message msg=handler.obtainMessage();
					msg.obj=data.getData();
					handler.sendMessageAtTime(msg, 50);
					return;
				}
				
			}
			switch (mode) {
			case MODE_REGISTER:
				LogUtil.i("me", "msg.what================");
				Intent intent1=getIntent();
				intent1.putExtra("path", Constants.Picture_Camera+File.separator+filename);
				setResult(RESULT_OK,intent1);
				this.finish();
				break;
			case MODE_CHANGE_ICON:
				LogUtil.i("me", "msg.what================");
				Intent intent2=getIntent();
				intent2.putExtra("path", Constants.Picture_Camera+File.separator+filename);
				setResult(RESULT_OK,intent2);
				this.finish();
				break;
			case MODE_TOPIC:
				LogUtil.i("me",""+Constants.Picture_Camera+File.separator+filename);
				if(!new File(Constants.Picture_Camera+File.separator+filename).exists()){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(100);
								/*Intent intent=new Intent(TakePictureBackground.this,com.aviary.android.feather.FeatherActivity.class);
								intent.putExtra("mode", 1);
								intent.setData(Uri.parse("file://"+Constants.Picture_Camera+File.separator+filename));
								intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
								startActivityForResult(intent, 11); */
								
								
								Intent intent3=new Intent(TakePictureBackground.this,SubmitPictureActivity.class);
								intent3.setData(Uri.parse("file://"+Constants.Picture_Camera+File.separator+filename));
								intent3.putExtra("mode", 1);
								intent3.putExtra("isBeg", isBeg);
								intent3.putExtra("path", Constants.Picture_Camera+File.separator+filename);
								intent3.putExtra("activity", activityName);
								intent3.putExtra("topic_id", getIntent().getIntExtra("topic_id", -1));
								intent3.putExtra("topic_name", getIntent().getStringExtra("topic_name") );
								intent3.putExtra("animal", animal);
								TakePictureBackground.this.startActivity(intent3);
								
								
								TakePictureBackground.this.finish();
								
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}else{
					/*Intent intent=new Intent(this,com.aviary.android.feather.FeatherActivity.class);
					intent.putExtra("mode", 1);
					intent.setData(Uri.parse("file://"+Constants.Picture_Camera+File.separator+filename));
					intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
					startActivityForResult(intent, 11); */
					Intent intent3=new Intent(TakePictureBackground.this,SubmitPictureActivity.class);
					intent3.setData(Uri.parse("file://"+Constants.Picture_Camera+File.separator+filename));
					intent3.putExtra("mode", 1);
					intent3.putExtra("isBeg", isBeg);
					intent3.putExtra("path", Constants.Picture_Camera+File.separator+filename);
					intent3.putExtra("activity", activityName);
					intent3.putExtra("topic_id", topic_id);
					intent3.putExtra("topic_name", topic_name);
					intent3.putExtra("animal", animal);
					TakePictureBackground.this.startActivity(intent3);
					
					TakePictureBackground.this.finish();
					
				}
				
				
				break;
			case MODE_ACTIVITY:
				LogUtil.i("me",""+Constants.Picture_Camera+File.separator+filename);
				if(!new File(Constants.Picture_Camera+File.separator+filename).exists()){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(100);
								/*Intent intent=new Intent(TakePictureBackground.this,com.aviary.android.feather.FeatherActivity.class);
								intent.putExtra("mode", 1);
								intent.setData(Uri.parse("file://"+Constants.Picture_Camera+File.separator+filename));
								intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
								startActivityForResult(intent, 11); */
								
								
								Intent intent3=new Intent(TakePictureBackground.this,SubmitPictureActivity.class);
								intent3.setData(Uri.parse("file://"+Constants.Picture_Camera+File.separator+filename));
								intent3.putExtra("mode", 1);
								intent3.putExtra("isBeg", isBeg);
								intent3.putExtra("path", Constants.Picture_Camera+File.separator+filename);
								intent3.putExtra("activity", activityName);
								intent3.putExtra("topic_id", getIntent().getIntExtra("topic_id", -1));
								intent3.putExtra("topic_name", getIntent().getStringExtra("topic_name") );
								intent3.putExtra("animal", animal);
								TakePictureBackground.this.startActivity(intent3);
								
								
								TakePictureBackground.this.finish();
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}else{
					/*Intent intent=new Intent(this,com.aviary.android.feather.FeatherActivity.class);
					intent.putExtra("mode", 1);
					intent.setData(Uri.parse("file://"+Constants.Picture_Camera+File.separator+filename));
					intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
					startActivityForResult(intent, 11); */
					Intent intent3=new Intent(TakePictureBackground.this,SubmitPictureActivity.class);
					intent3.setData(Uri.parse("file://"+Constants.Picture_Camera+File.separator+filename));
					intent3.putExtra("mode", 1);
					intent3.putExtra("isBeg", isBeg);
					intent3.putExtra("path", Constants.Picture_Camera+File.separator+filename);
					intent3.putExtra("activity", activityName);
					intent3.putExtra("topic_id", getIntent().getIntExtra("topic_id", -1));
					intent3.putExtra("topic_name", getIntent().getStringExtra("topic_name") );
					intent3.putExtra("animal", animal);
					intent3.putExtra("star_id", star_id);
					TakePictureBackground.this.startActivity(intent3);
					
					
					TakePictureBackground.this.finish();
				}
				break;
			}
			
		}else{
			this.finish();
		}

		

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
