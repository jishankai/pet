package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.CreateTitle;

public class ShowTopicActivity extends Activity implements OnClickListener{
	Button bt1,bt2,bt3,bt4;
	TextView tv1,tv2,tv3,tv4;
	ImageView imageView;
	LinearLayout linearLayout1;
	LinearLayout linearLayout2;
	RelativeLayout relativeLayout1;
	boolean judgeFlag=true;
	boolean hiden=false;
	CreateTitle createTitle;
	
	
	LinearLayout shareLayout;
	TextView tv5;
	ImageView imageView2,imageView3,imageView4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_show_topic);
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		bt1=(Button)findViewById(R.id.button1);
		bt2=(Button)findViewById(R.id.button2);
		bt3=(Button)findViewById(R.id.button3);
		bt4=(Button)findViewById(R.id.button4);
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv3=(TextView)findViewById(R.id.textView3);
		tv4=(TextView)findViewById(R.id.textView4);
		
		imageView=(ImageView)findViewById(R.id.imageView1);
		loadBitmap(getIntent().getData());
		tv3.setText(getIntent().getStringExtra("info"));
		linearLayout1=(LinearLayout)findViewById(R.id.linearLayout1);
		linearLayout2=(LinearLayout)findViewById(R.id.linearlayout2);
		relativeLayout1=(RelativeLayout)findViewById(R.id.relativeLayout1);
		createTitle=new CreateTitle(this,linearLayout1);
		
		
		shareLayout=(LinearLayout)findViewById(R.id.share_linearlayout);
		tv5=(TextView)findViewById(R.id.textView7);
		imageView2=(ImageView)findViewById(R.id.imageView2);
		imageView3=(ImageView)findViewById(R.id.imageView3);
		imageView4=(ImageView)findViewById(R.id.imageView4);
		
	}
	private void initListener(){
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
//		bt4.setOnClickListener(this);
		imageView.setOnClickListener(this);
		linearLayout2.setOnClickListener(this);
		
		tv5.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView3.setOnClickListener(this);
		imageView4.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Intent intent=new Intent(this,HomeActivity.class);
			this.startActivity(intent);
			this.finish();
			break;
		case R.id.button2:
			
			break;
		case R.id.textView1:
			
			break;
		case R.id.textView2:
			
			break;
		case R.id.button3:
			shareLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.button4:
			
			break;
		case R.id.imageView1:
			if(!hiden){
				linearLayout1.setVisibility(View.INVISIBLE);
				linearLayout2.setVisibility(View.INVISIBLE);
				relativeLayout1.setVisibility(View.INVISIBLE);
				tv3.setVisibility(View.INVISIBLE);
				hiden=true;
			}else{
				linearLayout1.setVisibility(View.VISIBLE);
				linearLayout2.setVisibility(View.VISIBLE);
				relativeLayout1.setVisibility(View.VISIBLE);
				tv3.setVisibility(View.VISIBLE);
				hiden=false;
			}
			break;
		case R.id.linearlayout2:
			if(judgeFlag){
				String num=""+tv4.getText();
				num=""+(Integer.parseInt(num)+1);	
				tv4.setText(num);
				judgeFlag=false;
			}
			LogUtil.i("me","linearlayout2");

			break;
		case R.id.textView7:
			shareLayout.setVisibility(View.INVISIBLE);
			
			break;
		case R.id.imageView2:
			
			break;
		case R.id.imageView3:
			
			break;
		case R.id.imageView4:
			
			break;
		}
	}
	public void loadBitmap(Uri uri){
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
