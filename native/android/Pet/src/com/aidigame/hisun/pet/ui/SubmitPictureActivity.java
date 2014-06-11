package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aviary.android.feather.FeatherActivity;

public class SubmitPictureActivity extends Activity implements OnClickListener{
	Button recodeBt,submitBt;
	ImageView imageView,backBt;
	EditText editText;
	TextView textView;
	Uri uri;
	String path,finalPath;
	public static final int UPLOAD_IMAGE_SUCCESS=0;
	public static final int UPLOAD_IMAGE_FAILS=1;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPLOAD_IMAGE_SUCCESS:
				Intent intent3=new Intent(SubmitPictureActivity.this,ShowTopicActivity.class);
//				final String info=""+textView.getText();
//				intent3.putExtra("info", ""+textView.getText());
//				intent3.setData(uri);
//				Topic topic=new Topic();
//				topic.bmpPath=finalPath;
				intent3.putExtra("data",(UserImagesJson.Data)msg.obj);
				SubmitPictureActivity.this.startActivity(intent3);
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.activity_submit_picture);
		initView();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		backBt=(ImageView)findViewById(R.id.button1);
		recodeBt=(Button)findViewById(R.id.button2);
		submitBt=(Button)findViewById(R.id.button3);
		imageView=(ImageView)findViewById(R.id.imageView1);
		editText=(EditText)findViewById(R.id.editText1);
		textView=(TextView)findViewById(R.id.textView2);
		uri=getIntent().getData();
		path=getIntent().getStringExtra("path");
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				uri=getIntent().getData();
				loadBitmap(uri);
			}
		});
		
//		imageView.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("path")));
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		backBt.setOnClickListener(this);
		recodeBt.setOnClickListener(this);
		submitBt.setOnClickListener(this);
		imageView.setOnClickListener(this);
		editText.addTextChangedListener(new TextWatcher() {
			
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
				String str=editText.getText().toString();
				if(str!=null)
				textView.setText(str);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			
			if(path!=null){
				Intent intent=new Intent(this,FeatherActivity.class);
				intent.setData(Uri.parse(path));
				intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, "f6d0dd319088fd5a");
				this.startActivityForResult(intent, 2);
			}
			
			
			break;
		case R.id.button2:
			
			break;
		case R.id.button3:
			//FIXME
			if(finalPath==null)return;
//			Intent intent3=new Intent(this,ShowTopicActivity.class);
			final String info=""+textView.getText();
//			intent3.putExtra("info", ""+textView.getText());
//			intent3.setData(uri);
//			Topic topic=new Topic();
//			topic.bmpPath=finalPath;
//			intent3.putExtra("topic", topic);
//			this.startActivity(intent3);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpUtil.uploadImage(finalPath,info,handler);
					/*File file=new File(finalPath);
				     String fileName=file.getName();
				     File newPath=new File(Constants.Picture_Path);
				     if(!newPath.exists()){
				    	 newPath.mkdirs();
				     }
				     FileInputStream fis=null;
						FileOutputStream fos=null;
				     try {
						fis=new FileInputStream(file);
						fos=new FileOutputStream(Constants.Picture_Path+File.separator+fileName);
						byte[] buffer=new byte[1024*10];
						int len=0;
						while((len=fis.read(buffer, 0, 1024*10))!=-1){
							fos.write(buffer, 0, len);
							fos.flush();
						}
						SharedPreferences sp=getSharedPreferences("temp.xml",Context.MODE_WORLD_WRITEABLE);
						Editor editor=sp.edit();
						String info=textView.getText().toString();
						info=info==null?"":info;
						editor.putString(fileName, info);
						editor.putString("comment"+fileName, info);
						editor.commit();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				     LogUtil.i("me", "fileName"+fileName);*/
				}
			}).start();
			this.finish();
			//TODO �������� ��������
			
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
