package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.nio.charset.Charset;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.CircleView;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.aidigame.hisun.pet.widget.ShowProgress;
public class RegisterActivity extends Activity {
	EditText et1,et4;
	EditText sp1;
	TextView sp2,noteView;
	ImageView femaleView,maleView;
	Button bt1;
	boolean isClickable=false;
	CircleView icon;
	ImageView imageView;
	LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
	LinearLayout camera_album;
	ShowProgress showProgress;
	LinearLayout progressLayout;
	public static final int DISMISS_PROGRESS=202;
	public static final int SHOW_PROGRESS=203;
	int classs=-1;
	String path;
	int gender=2;//1 公，2母
    String race;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constants.REGISTER_SUCCESS:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						HttpUtil.login(RegisterActivity.this, handler);
					    
					}
				}).start();
				Intent intent1=new Intent(RegisterActivity.this,HomeActivity.class);
				RegisterActivity.this.startActivity(intent1);
				RegisterActivity.this.finish();
				break;
			case Constants.LOGIN_SUCCESS:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
				RegisterActivity.this.startActivity(intent);
				RegisterActivity.this.finish();
				break;
			case Constants.REGISTER_FAIL:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				String error=(String)msg.obj;
				if(error!=null){
					ShowDialog.show(error,RegisterActivity.this);
				}else{
					ShowDialog.show("用户名重复,注册 失败",RegisterActivity.this);
				}
				
				break;
			case DISMISS_PROGRESS:
				if(showProgress!=null)
				showProgress.progressCancel();
				break;
			case SHOW_PROGRESS:
				if(showProgress==null){
					showProgress=new ShowProgress(RegisterActivity.this, "数据上传中，请稍等", progressLayout);
				}else{
					showProgress.showProgress();
				}
				
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
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
		progressLayout=(LinearLayout)findViewById(R.id.progress_linearlayout1);
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
		camera_album=(LinearLayout)findViewById(R.id.album_camera_register);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		sp2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegisterActivity.this,ChoseClassActivity.class);
				
				RegisterActivity.this.startActivityForResult(intent,1);
				invisible();
			}
		});
		femaleView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender=2;
				femaleView.setImageResource(R.drawable.female);
				maleView.setImageResource(R.drawable.male_gray);
			}
		});
		maleView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender=1;
				femaleView.setImageResource(R.drawable.female_gray);
				maleView.setImageResource(R.drawable.male);
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
				showCameraAlbum();
			}


		});
		et4.addTextChangedListener(new TextWatcher() {
			
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
				judgeComplete();
			}
		});
		et1.addTextChangedListener(new TextWatcher() {
			
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
				
				judgeComplete();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isClickable)return;
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
				if(race==null){
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
				user.race=race;
				user.age=age;
				user.uid=code;
				user.iconPath=path;
				PetApplication.petApp.user=user;
				SharedPreferences sp=getSharedPreferences("temp.xml",Context.MODE_WORLD_WRITEABLE);
				Editor editor=sp.edit();
				editor.putString("icon", path);
				editor.commit();
				handler.sendEmptyMessage(SHOW_PROGRESS);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.register(handler,user,RegisterActivity.this);
					}
				}).start();
			}
		});
	}
	private void showCameraAlbum() {
		// TODO Auto-generated method stub
		final View view=LayoutInflater.from(this).inflate(R.layout.popup_user_homepage, null);
		camera_album.removeAllViews();
		camera_album.addView(view);
		camera_album.setVisibility(View.VISIBLE);
		camera_album.setClickable(true);
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
		view.clearAnimation();
		view.setAnimation(animation);
		animation.start();
		TextView camera=(TextView)view.findViewById(R.id.textView1);
		TextView album=(TextView)view.findViewById(R.id.textView2);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(RegisterActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_REGISTER);
				camera_album.setVisibility(View.INVISIBLE);
				RegisterActivity.this.startActivityForResult(intent2, 12);
				camera_album.setClickable(false);
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegisterActivity.this,AlbumPictureBackground.class);
				intent.putExtra("mode", TakePictureBackground.MODE_REGISTER);
				RegisterActivity.this.startActivityForResult(intent, 12);
				camera_album.setVisibility(View.INVISIBLE);
				camera_album.setClickable(false);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation=AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
					}
				}, 1000);
				
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
				race=data.getStringExtra("race");
				String raceName=data.getStringExtra("raceName");
				SharedPreferences sp=getSharedPreferences("temp.xml",Context.MODE_WORLD_WRITEABLE);
				Editor editor=sp.edit();
				editor.putString("race", raceName);
				editor.commit();
				if (raceName==null) {
					return;
				}
				sp2.setText(raceName);
				
				judgeComplete();
			}else if(requestCode==12){
				path=data.getStringExtra("path");
				Intent intent=new Intent(this,com.aviary.android.feather.FeatherActivity.class);
				intent.setData(Uri.parse("file://"+path));
				intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
				startActivityForResult(intent, 2); 
				
			}else if(requestCode==2){
				if(data.getData()!=null)
				loadBitmap(data.getData());
				judgeComplete();
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
		femaleView.setVisibility(View.INVISIBLE);
		maleView.setVisibility(View.INVISIBLE);
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
		femaleView.setVisibility(View.VISIBLE);
		maleView.setVisibility(View.VISIBLE);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
//			HomeActivity.homeActivity.handler.sendEmptyMessage(HomeActivity.COMPLETE);
			Intent intent=new Intent(this,HomeActivity.class);
			this.startActivity(intent);
			this.finish();
			
		}
		return super.onKeyDown(keyCode, event);
	}
	public void loadBitmap(Uri uri){
		LogUtil.i("me", "uri="+uri.toString()+",path="+path);
		Cursor cursor=getContentResolver().query(uri, null, null, null, null);
		if(cursor!=null){
			cursor.moveToFirst();
			path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=4;
			icon.setImageBitmap(BitmapFactory.decodeFile(path,options));
			cursor.close();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	public void judgeComplete(){
		String name=null;
		name=et1.getText().toString();
		
		int position2=classs;
		String genderStr=null;
		if(gender==-1){
			genderStr=null;
		}else{
			genderStr=""+gender;
		}
		
		String age=et4.getText().toString();
//		if(isEmpty(code))return;
		if(path==null){
			
			return;
		}
		if(race==null){
			
			return;
		}
		if(isEmpty(name)){
			
			return;
		}
		if(isEmpty(genderStr)){
			
			return;
		}
		if(isEmpty(age)){
			
			return;
		}
		isClickable=true;
		bt1.setBackgroundResource(R.drawable.button);
	}
	

}
