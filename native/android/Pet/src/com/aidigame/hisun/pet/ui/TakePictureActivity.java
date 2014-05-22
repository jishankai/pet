package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.CreateTitle;
import com.aidigame.hisun.pet.widget.PetCamera;

public class TakePictureActivity extends Activity implements OnClickListener,SurfaceHolder.Callback{
	LinearLayout titleLinearLayout;
	SurfaceView surfaceView;
	SurfaceHolder holder;
	Button albumBt,takePictureBt,cancelBt;
	//创建顶部标题栏
	CreateTitle createTitle;
	PetCamera petCamera;
	public static final int TAKE_PICTURE_COMPLETED=0;//完成拍照
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case TAKE_PICTURE_COMPLETED:
//				String path=(String)msg.obj;
//				albumBt.setText("");
				Intent intent=new Intent(TakePictureActivity.this,HandlePictureActivity.class);
				TakePictureActivity.this.startActivity(intent);
				TakePictureActivity.this.finish();
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_take_picture);
		initView();
		initListener();
		createTitle=new CreateTitle(this, titleLinearLayout);
		holder.addCallback(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		titleLinearLayout=(LinearLayout)findViewById(R.id.linearlayout_title);
		surfaceView=(SurfaceView)findViewById(R.id.surfaceView1);
		albumBt=(Button)findViewById(R.id.button1);
		takePictureBt=(Button)findViewById(R.id.button2);
		cancelBt=(Button)findViewById(R.id.button3);
		holder=surfaceView.getHolder();
		//2.33之前的版本调用camera必须加这句话，不然报错
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		petCamera=new PetCamera(this, holder,surfaceView);
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
			
			break;
		case R.id.button2:
			petCamera.takePicture();
			break;
		case R.id.button3:
			
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

}
