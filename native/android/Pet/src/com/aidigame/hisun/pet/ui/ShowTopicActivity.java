package com.aidigame.hisun.pet.ui;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.CircleView;
import com.aidigame.hisun.pet.widget.CreateTitle;

public class ShowTopicActivity extends Activity implements OnClickListener{
	CircleView bt2;
	TextView tv1,tv2,tv3,tv4;
	ImageView imageView,bt4,bt1,bt3;
	LinearLayout linearLayout1;
	LinearLayout linearLayout2;
	RelativeLayout relativeLayout1;
	boolean judgeFlag=true;
	boolean hiden=false;
	CreateTitle createTitle;
	public static final int DOWNLOAD_IMAGE_TX=2;
	
	LinearLayout shareLayout;
	TextView tv5;
	ImageView imageView2,imageView3,imageView4;
	
	
	
	UserImagesJson.Data data;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			String path=(String)msg.obj;
			if(path!=null){
				data.path=path;
				loadBitmap(data);
				
			}
			if(msg.what==DOWNLOAD_IMAGE_TX){
				String pathStr=(String)msg.obj;
				if(pathStr!=null){
					data.user.iconPath=pathStr;
					BitmapFactory.Options options=new BitmapFactory.Options();
					options.inSampleSize=4;
					bt2.setImageBitmap(BitmapFactory.decodeFile(pathStr,options));
				}
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.activity_show_topic);
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		bt1=(ImageView)findViewById(R.id.button1);
		bt3=(ImageView)findViewById(R.id.button3);
		bt2=(CircleView)findViewById(R.id.button2);
		bt4=(ImageView)findViewById(R.id.button4);
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv3=(TextView)findViewById(R.id.textView3);
		tv4=(TextView)findViewById(R.id.textView4);
		imageView=(ImageView)findViewById(R.id.imageView1);
		data=(UserImagesJson.Data)getIntent().getSerializableExtra("data");
		if(data.user!=null){
			tv1.setText(""+data.user.nickName);
			tv2.setText(""+data.user.race);
			tv4.setText(""+data.likes);
			if(!new File(Constants.Picture_ICON_Path+File.separator+data.user.iconUrl).exists()){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(data.user.iconUrl!=null)
						HttpUtil.downloadIconImage(Constants.USER_DOWNLOAD_TX, data.user.iconUrl, handler);
					}
				}).start();
			}else{
				data.user.iconPath=Constants.Picture_ICON_Path+File.separator+data.user.iconUrl;
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize=4;
				bt2.setImageBitmap(BitmapFactory.decodeFile(data.user.iconPath,options));
			}
			
		}/*else{
			tv1.setText(""+Constants.user.nickName);
			tv2.setText(""+Constants.user.race);
			bt2.setImageBitmap(BitmapFactory.decodeFile(Constants.user.iconPath));
		}*/
		
		if(!new File(Constants.Picture_Topic_Path+File.separator+data.url).exists()){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpUtil.downloadImage(Constants.UPLOAD_IMAGE_RETURN_URL, data.url, handler);
				}
			}).start();
		}else{
			data.path=Constants.Picture_Topic_Path+File.separator+data.url;
			loadBitmap(data);
		}
		

		linearLayout1=(LinearLayout)findViewById(R.id.linearLayout1);
		linearLayout2=(LinearLayout)findViewById(R.id.linearlayout2);
		relativeLayout1=(RelativeLayout)findViewById(R.id.relativeLayout1);
//		createTitle=new CreateTitle(this,linearLayout1);
		
		
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
			if(data.user==null||data.usr_id==Constants.user.userId){
				Intent intent1=new Intent(this,UserHomepageActivity.class);
				this.startActivity(intent1);
				this.finish();
			}else{
				Intent intent1=new Intent(this,OtherUserTopicActivity.class);
				intent1.putExtra("data", data);
				this.startActivity(intent1);
				this.finish();
			}
			break;
		/*case R.id.textView1:
			
			
		case R.id.textView2:

			break;*/
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
	public void loadBitmap(UserImagesJson.Data data){
		
		if(data!=null){
			Bitmap bitmap=BitmapFactory.decodeFile(data.path);
			imageView.setImageBitmap(bitmap);
			tv3.setText(data.comment);
		}
		
	}
}
