package com.aidigame.hisun.pet.ui;

import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.CircleView;

public class RegisterActivity extends Activity {
	EditText et1,et4;
	EditText sp1;
	TextView sp2,noteView;
	ImageView femaleView,maleView;
	Button bt1;
	CircleView icon;
	ImageView imageView;
	LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
	int classs=-1;
	String path;
	int gender=-1;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constants.REGISTER_SUCCESS:
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.login(RegisterActivity.this, handler);
					    
					}
				}).start();
				
				break;
			case Constants.LOGIN_SUCCESS:
				Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
				RegisterActivity.this.startActivity(intent);
				RegisterActivity.this.finish();
				break;
			case Constants.REGISTER_FAIL:
				String error=(String)msg.obj;
				if(error!=null)
				Toast.makeText(RegisterActivity.this, error, 3000).show();
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
		setContentView(R.layout.activity_register);
		
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		et1=(EditText)findViewById(R.id.editText1);
		sp1=(EditText)findViewById(R.id.editText2);
		sp2=(TextView)findViewById(R.id.editText3);
		et4=(EditText)findViewById(R.id.editText4);
		bt1=(Button)findViewById(R.id.button1);
		noteView=(TextView)findViewById(R.id.note_textView);
		imageView=(ImageView)findViewById(R.id.chose_class_imageview);
		icon=(CircleView)findViewById(R.id.imageView1);
		femaleView=(ImageView)findViewById(R.id.imageview_female);
		maleView=(ImageView)findViewById(R.id.imageview_male);
		Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.camera1);
		icon.setImageBitmap(bmp);
		linearLayout1=(LinearLayout)findViewById(R.id.linearLayout1);
		linearLayout2=(LinearLayout)findViewById(R.id.linearLayout2);
		linearLayout3=(LinearLayout)findViewById(R.id.linearLayout3);
		linearLayout4=(LinearLayout)findViewById(R.id.linearLayout4);
	}
	private void initListener() {
		// TODO Auto-generated method stub
		femaleView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender=2;
			}
		});
		maleView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender=1;
			}
		});
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegisterActivity.this,ChoseClassActivity.class);
			
				RegisterActivity.this.startActivityForResult(intent,1);
				invisible();
			}
		});
		icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegisterActivity.this,TakePictureActivity.class);
				intent.putExtra("mode", TakePictureActivity.TAKE_PICTURE_GET_PATH);
				RegisterActivity.this.startActivityForResult(intent,2);
				
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name=null;
				name=et1.getText().toString();
				name=new String(name.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
				String code=sp1.getText().toString();
				int position2=classs;
				String genderStr=null;
				if(gender==-1){
					genderStr=null;
				}else{
					genderStr=""+gender;
				}
				
				String age=et4.getText().toString();
//				if(isEmpty(code))return;
				if(path==null){
					Toast.makeText(RegisterActivity.this, "请选择头像", 5000).show();
					return;
				}
				if(classs==-1){
					Toast.makeText(RegisterActivity.this, "请填写来自哪？", 5000).show();
					return;
				}
				if(isEmpty(name)){
					Toast.makeText(RegisterActivity.this, "请填写用户名！", 5000).show();
					return;
				}
				if(isEmpty(genderStr)){
					Toast.makeText(RegisterActivity.this, "请选择性别", 5000).show();
					return;
				}
				if(isEmpty(age)){
					Toast.makeText(RegisterActivity.this, "请填写年龄", 5000).show();
					return;
				}
				final User user=new User();
				user.nickName=name;
				user.classs=classs;
				user.gender=gender;
				user.age=age;
				user.uid=code;
				user.iconPath=path;
				PetApplication.petApp.user=user;
				SharedPreferences sp=getSharedPreferences("temp.xml",Context.MODE_WORLD_WRITEABLE);
				Editor editor=sp.edit();
				editor.putString("icon", path);
				editor.commit();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.register(handler,user);
					}
				}).start();
			}
		});
	}
	public boolean isEmpty(String str){
		if(str==null||"".equals(str)||" ".equals(str)){
			return true;
		}
		return false;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		visible();
		LogUtil.i("me","onActivityResult"+"："+(data==null));
		switch (resultCode) {
		case RESULT_OK:
			if(requestCode==1){
				classs=data.getIntExtra("classs", 1);
				String raceName=data.getStringExtra("raceName");
				SharedPreferences sp=getSharedPreferences("temp.xml",Context.MODE_WORLD_WRITEABLE);
				Editor editor=sp.edit();
				editor.putString("race", raceName);
				editor.commit();
				if (raceName==null) {
					return;
				}
				sp2.setText(raceName);
			}else{
				path=data.getStringExtra("path");
				icon.setImageBitmap(BitmapFactory.decodeFile(path));
			}
			
			break;
		}
	}
	private void invisible(){
		imageView.setVisibility(View.INVISIBLE);
		bt1.setVisibility(View.INVISIBLE);
		linearLayout1.setVisibility(View.INVISIBLE);
		linearLayout2.setVisibility(View.INVISIBLE);
		linearLayout3.setVisibility(View.INVISIBLE);
		linearLayout4.setVisibility(View.INVISIBLE);
		noteView.setVisibility(View.INVISIBLE);
		icon.setVisibility(View.INVISIBLE);
	}
	private void visible(){
		imageView.setVisibility(View.VISIBLE);
		bt1.setVisibility(View.VISIBLE);
		linearLayout1.setVisibility(View.VISIBLE);
		linearLayout2.setVisibility(View.VISIBLE);
		linearLayout3.setVisibility(View.VISIBLE);
		linearLayout4.setVisibility(View.VISIBLE);
		noteView.setVisibility(View.VISIBLE);
		icon.setVisibility(View.VISIBLE);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			HomeActivity.homeActivity.handler.sendEmptyMessage(HomeActivity.COMPLETE);
			
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	

}
