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

import com.aidigame.hisun.pet.R;
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
	//��������������
	CreateTitle createTitle;
	PetCamera petCamera;
	String path;
	public static final int TAKE_PICTURE_COMPLETED=0;//�������
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case TAKE_PICTURE_COMPLETED:
				path="file://"+ImageUtil.compressImage(HandlePictureActivity.handlingBmp, 50);
				LogUtil.i("me", "ImageUtil.compressImage(HandlePictureActivity.handlingBmp::::"+path);
				Intent intent=new Intent(TakePictureActivity.this,com.aviary.android.feather.FeatherActivity.class);
				intent.setData(Uri.parse(path));
				intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, "f6d0dd319088fd5a");
				startActivityForResult(intent, 1);                                                    
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
//		createTitle=new CreateTitle(this, titleLinearLayout);
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
		//2.33֮ǰ�İ汾����camera�������仰����Ȼ����
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
			Intent intent1=new Intent(this,PictureAlbumActivity.class);
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

}
