package com.example.picturedemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.picturedemo.R;
import com.example.picturedemo.util.MyCamera;

public class TakePictureActivity extends Activity implements SurfaceHolder.Callback{
	SurfaceView view;
	SurfaceHolder holder;
	MyCamera myCamera;
	Intent data;
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			Bundle bundle=msg.getData();
			data.putExtras(bundle);
			setResult(2, data);;
			finish();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_picture);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		view=(SurfaceView)findViewById(R.id.surfaceview);
		holder=view.getHolder();
		holder.addCallback(this);
		data=getIntent();
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==MotionEvent.ACTION_UP){
			myCamera.takePicture();
		}
		return true;
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		myCamera=new MyCamera(this, holder);
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		myCamera.stopPreview();
	}
	

}
