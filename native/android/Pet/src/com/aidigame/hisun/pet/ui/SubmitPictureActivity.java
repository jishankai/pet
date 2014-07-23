package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.XinlangShare;
import com.aviary.android.feather.FeatherActivity;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class SubmitPictureActivity extends Activity implements OnClickListener{
	Button recodeBt,submitBt,closeBt;
	boolean sendPicture=false;
	ImageView imageView,backBt,weixin,xinlang;
	boolean sendToXinlang=false,sendToWeixin=false;
	EditText editText;
	TextView textView;
	LinearLayout progressLayout;
	ShowProgress showProgress;
	public static final int DISMISS_PROGRESS=202;
	public static final int SHOW_PROGRESS=203;
	public static SubmitPictureActivity submitPictureActivity;
	Uri uri;
	String path,finalPath;//path为原始图片，未进行照片编辑
	public static final int UPLOAD_IMAGE_SUCCESS=0;
	public static final int UPLOAD_IMAGE_FAILS=1;
	String info="";
	String lastInfo="";
	String activityName;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPLOAD_IMAGE_SUCCESS:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				UserImagesJson.Data data=(UserImagesJson.Data)msg.obj;
				data.path=path;
				/*SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
				if(sp.getBoolean(Constants.PICTURE_SEND_TO_XINLANG, false)){
					
					String token=sp.getString("xinlangToken", null);
					if(token!=null){
						Constants.accessToken=Oauth2AccessToken.parseAccessToken(token);
						if(Constants.accessToken.isSessionValid()){
							XinlangShare.sharePicture(data, SubmitPictureActivity.this);
						}
					}
					
				}
				if(sp.getBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false)){
					if(Constants.api==null){
						boolean flag=WeixinShare.regToWeiXin(SubmitPictureActivity.this);
						if(!flag){
							Toast.makeText(SubmitPictureActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
							
							
						}
					}
					if(WeixinShare.shareBitmap(data, 2)){
//						Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
						
					}else{
						Toast.makeText(SubmitPictureActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
						
					}
				}else {
					
				}*/
				
				Intent intent3=new Intent(SubmitPictureActivity.this,ShowTopicActivity.class);
				intent3.putExtra("data",data);
				ShowTopicActivity.datas=null;
				SubmitPictureActivity.this.startActivity(intent3);
				SubmitPictureActivity.this.finish();
				break;
			case DISMISS_PROGRESS:
				if(showProgress!=null)
				showProgress.progressCancel();
				break;
			case SHOW_PROGRESS:
				if(showProgress==null){
					showProgress=new ShowProgress(SubmitPictureActivity.this, "图片上传中，请稍等", progressLayout);
				}else{
					showProgress.showProgress();
				}
				
				break;
			case UPLOAD_IMAGE_FAILS:
				sendPicture=false;
				Toast.makeText(SubmitPictureActivity.this, "上传图片失败",  Toast.LENGTH_LONG).show();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_submit_picture);
		initView();
		this.submitPictureActivity=this;
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		backBt=(ImageView)findViewById(R.id.button1);
		recodeBt=(Button)findViewById(R.id.button2);
		submitBt=(Button)findViewById(R.id.button4);
		closeBt=(Button)findViewById(R.id.button3);
		imageView=(ImageView)findViewById(R.id.imageView1);
		editText=(EditText)findViewById(R.id.editText1);
		textView=(TextView)findViewById(R.id.textView2);
		progressLayout=(LinearLayout)findViewById(R.id.progress_linearlayout1);
		uri=getIntent().getData();
		activityName=getIntent().getStringExtra("activity");
		path=getIntent().getStringExtra("path");
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				uri=getIntent().getData();
				loadBitmap(uri);
			}
		});
	}
	boolean isShow=false;
	private void initListener() {
		// TODO Auto-generated method stub
		backBt.setOnClickListener(this);
		recodeBt.setOnClickListener(this);
		submitBt.setOnClickListener(this);
		imageView.setOnClickListener(this);
		closeBt.setOnClickListener(this);
		
		editText.addTextChangedListener(new TextWatcher() {
			String temp;
			Toast toast;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				if(toast!=null){
					toast.cancel();
					toast=null;
				}
				temp=""+s;
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int start=editText.getSelectionStart();
				int stop=editText.getSelectionEnd();
				String str=editText.getText().toString();
				int max=40;
				if(str.getBytes().length>str.length()){
					max=20;
				}else{
					max=40;
				}
				if(str!=null){
					/*info+=str;
					lastInfo=str;
					editText.;*/
//					textView.setText(str);
					if(str.length()<=max){
						if(temp.length()<str.length()){
							if(str.length()==max){
								info=str;
							}
							
							toast=Toast.makeText(SubmitPictureActivity.this, "您已输入"+str.length()+"个字，还可在输入"+(max-str.length())+"个字", 200);
							
							toast.show();
						}
						
					}else{
						if(start>0){
							s.delete(start-1, stop);
							editText.setText(temp);
							if(start>max){
								
							}else{
								editText.setSelection(start);
							}
							
							toast=Toast.makeText(SubmitPictureActivity.this, "您已经输入"+max+"个字...", 200);
							toast.show();
						}
						
					}
				}
					
			}
		});
		LinearLayout weixinLayout=(LinearLayout)findViewById(R.id.weixin_linearlayout);
		LinearLayout xinlangLayout=(LinearLayout)findViewById(R.id.xinlang_linearlayout);
		weixin=(ImageView)findViewById(R.id.weixin1);
		xinlang=(ImageView)findViewById(R.id.xinlang);
		SharedPreferences sp=getSharedPreferences("setup",Context.MODE_WORLD_WRITEABLE);
		sendToWeixin=sp.getBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
		sendToXinlang=sp.getBoolean(Constants.PICTURE_SEND_TO_XINLANG, false);
		if(sendToWeixin){
			weixin.setImageResource(R.drawable.friend_weixin);
		}
		if(sendToXinlang){
			xinlang.setImageResource(R.drawable.xinlang);
		}
		weixinLayout.setClickable(true);
		xinlangLayout.setClickable(true);
		weixinLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sendToWeixin){
					sendToWeixin=false;
					weixin.setImageResource(R.drawable.weixin_gray);
				
				}else{
					if(Constants.api==null){
						boolean flag=WeixinShare.regToWeiXin(SubmitPictureActivity.this);
						if(!flag){
							sendToWeixin=false;
							Toast.makeText(SubmitPictureActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
							
							
						}else{
							sendToWeixin=true;
							weixin.setImageResource(R.drawable.friend_weixin);
							
						}
					}else{
						sendToWeixin=true;
						weixin.setImageResource(R.drawable.friend_weixin);
					}
					
					
				}
				
				
				
				
				
			}
		});
		xinlangLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sendToXinlang){
					sendToXinlang=false;
					xinlang.setImageResource(R.drawable.xinlang_gray);
				}else{
					if(UserStatusUtil.hasXinlangAuth(SubmitPictureActivity.this)){
						sendToXinlang=true;
						xinlang.setImageResource(R.drawable.xinlang);
					}else{
						sendToXinlang=false;
					} 
					
					
				}
				
				
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			
			if(uri!=null){
				Intent intent=new Intent(this,FeatherActivity.class);
				intent.setData(uri);
				intent.putExtra("mode", getIntent().getIntExtra("mode", 0));
				intent.putExtra("from", "SubmitPicture");
				intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, "f6d0dd319088fd5a");
				this.startActivityForResult(intent, 2);
			}
			
			
			break;
		case R.id.button2:
			
			break;
        case R.id.button3:
			this.finish();
			break;
		case R.id.button4:
			//FIXME
			if(sendPicture){
				Toast.makeText(this, "正在发送图片，请稍等", Toast.LENGTH_LONG).show();;
				return ;
			}
			
			if(finalPath==null)return;
			if(!info.contains(lastInfo)){
				info=""+lastInfo;
			}
			 info=editText.getText().toString();
			 if("为您爱宠的靓照写个描述吧~".equals(info)){
				 info="";
			 }
			 if(activityName!=null){
				 info="#"+activityName+"#"+info;
			 }
			 if(null==info)info="";
			 UserImagesJson.Data  data=new UserImagesJson.Data();
			 data.path=finalPath;
			 data.comment=info;
			 
			 if(sendToWeixin){
				 if(WeixinShare.shareBitmap(data, 2)){
//						Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(SubmitPictureActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
					}
					 

							
			 }
			 
				
			 
			 if(sendToXinlang){
				 if(UserStatusUtil.hasXinlangAuth(SubmitPictureActivity.this)){
						XinlangShare.sharePicture(data,SubmitPictureActivity.this);
						
					} 
			 }
			 
			 
			 
			
				 
			
			
			
			 sendPicture=true;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					info = info.replaceAll("\\s+", " ");
					info.trim();
					
					boolean flag=HttpUtil.uploadImage(finalPath,info,handler,SubmitPictureActivity.this);
					
						sendPicture=false;
					
					
				}
			}).start();
			handler.sendEmptyMessage(SHOW_PROGRESS);
//			this.finish();
			
			break;
		case R.id.imageView1:
			
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if(data.getBooleanExtra("cancel", false)){
				this.finish();
				return;
			}
			if(uri!=null){
				getContentResolver().delete(uri, null, null);
			}
			uri=data.getData();
			loadBitmap(uri);
			break;
		case RESULT_CANCELED:
			
			break;
		}
	}
	public void loadBitmap(Uri uri){
		LogUtil.i("me", "uri="+uri.toString()+",path="+path);
		if(uri==null)return;
		Cursor cursor=getContentResolver().query(uri, null, null, null, null);
		if(cursor!=null){
			cursor.moveToFirst();
			String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
			Bitmap bitmap=BitmapFactory.decodeFile(path);
			imageView.setImageBitmap(bitmap);
			finalPath=path;
			cursor.close();
		}
		
		
	}	

}
