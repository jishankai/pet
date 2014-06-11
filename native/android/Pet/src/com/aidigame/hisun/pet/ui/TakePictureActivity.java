package com.aidigame.hisun.pet.ui;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.CreateTitle;
import com.aidigame.hisun.pet.widget.PetCamera;
//import com.aviary.android.feather.library.Constants;

public class TakePictureActivity extends Activity implements OnClickListener,SurfaceHolder.Callback{
	LinearLayout titleLinearLayout;
	SurfaceView surfaceView;
	SurfaceHolder holder;
	Button albumBt,takePictureBt,cancelBt;
	//标题栏
	CreateTitle createTitle;
	PetCamera petCamera;
	String path;
	int mode;
	public static final int TAKE_PICTURE_COMPLETED=0;//拍完照片，使用透镜进行处理
	public static final int TAKE_PICTURE_GET_PATH=1;//拍完照片，需要拿到相片保存的路径。
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			String temp=ImageUtil.compressImage(HandlePictureActivity.handlingBmp, 50);
			
			switch (msg.what) {
			case TAKE_PICTURE_COMPLETED:
				path="file://"+temp;
				LogUtil.i("me", "ImageUtil.compressImage(HandlePictureActivity.handlingBmp::::"+path);
				Intent intent=new Intent(TakePictureActivity.this,com.aviary.android.feather.FeatherActivity.class);
				intent.setData(Uri.parse(path));
				intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
				startActivityForResult(intent, 1);  
				break;
			case TAKE_PICTURE_GET_PATH:
				LogUtil.i("me", "msg.what================"+msg.what);
				Intent intent1=getIntent();
				intent1.putExtra("path", temp);
				setResult(RESULT_OK,intent1);
				TakePictureActivity.this.finish();
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
		setContentView(R.layout.activity_take_picture);
		mode=getIntent().getIntExtra("mode", 0);
		initView();
		initListener();
//		createTitle=new CreateTitle(this, titleLinearLayout);
		holder.addCallback(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		titleLinearLayout=(LinearLayout)findViewById(R.id.linearlayout_title);
		surfaceView=(SurfaceView)findViewById(R.id.surfaceView1);
		albumBt=(Button)findViewById(R.id.button1);
		//只是拍照，相册不可见
		if(mode!=0){
			albumBt.setVisibility(View.INVISIBLE);
		}
		takePictureBt=(Button)findViewById(R.id.button2);
		cancelBt=(Button)findViewById(R.id.button3);
		holder=surfaceView.getHolder();
		//2.33版本调用相机，必须进行设置，不然报错
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		petCamera=new PetCamera(this, holder,surfaceView,getIntent().getIntExtra("mode", 0));
	}
	private void initListener() {
		// TODO Auto-generated method stub
		albumBt.setOnClickListener(this);
		takePictureBt.setOnClickListener(this);
		cancelBt.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Intent intent1=new Intent(this,PictureAlbumActivity.class);
			this.startActivityForResult(intent1, 2);
			this.startActivity(intent1);
			this.finish();
			break;
		case R.id.button2:
			petCamera.takePicture();
			break;
		case R.id.button3:
			Intent intent=new Intent(this,HomeActivity.class);
			this.startActivity(intent);
			this.finish();
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.i("me", "takepicture***onactivityresult");
		switch (resultCode) {
		case RESULT_CANCELED:
			
			break;
		case RESULT_OK:
			Uri uri=data.getData();
			Intent intent=new Intent(this,SubmitPictureActivity.class);
			intent.setData(uri);
			intent.putExtra("path", path);
			this.startActivity(intent);
			this.finish();
			break;
			
		
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		petCamera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		petCamera.stopCamera();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.i("me", "TakePictureActivity关闭了");
	}

}
