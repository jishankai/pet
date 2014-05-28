package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aviary.android.feather.FeatherActivity;

public class SubmitPictureActivity extends Activity implements OnClickListener{
	Button backBt,recodeBt,submitBt;
	ImageView imageView;
	EditText editText;
	TextView textView;
	Uri uri;
	String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_picture);
		initView();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		backBt=(Button)findViewById(R.id.button1);
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
			Intent intent3=new Intent(this,ShowTopicActivity.class);
			intent3.putExtra("info", ""+textView.getText());
			intent3.setData(uri);
			this.startActivity(intent3);
			this.finish();
			//TODO É¾³ý ÕÕÆ¬
			
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
			cursor.close();
		}
		
		
	}	

}
