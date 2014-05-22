package com.example.picturedemo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.example.picturedemo.ui.TakePictureActivity;

public class MyCamera {
	TakePictureActivity context;
	SurfaceHolder holder;
	Camera camera;
	boolean isTakingPicture=false;
	public MyCamera(TakePictureActivity context,SurfaceHolder holder){
		this.context=context;
		this.holder=holder;
		int cameraNums=Camera.getNumberOfCameras();
		Log.i("me", "ÉãÏñÍ·ÊýÄ¿£º"+cameraNums);
		Log.i("me","Camera.CameraInfo.CAMERA_FACING_BACK:"+Camera.CameraInfo.CAMERA_FACING_BACK);
		Log.i("me","Camera.CameraInfo.CAMERA_FACING_FRONT:"+Camera.CameraInfo.CAMERA_FACING_FRONT);
		camera=Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
		try {
			setCameraDisplayOrientation(context, Camera.CameraInfo.CAMERA_FACING_FRONT, camera);
//			camera.setDisplayOrientation(90);
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void takePicture(){
		if(camera!=null&&!isTakingPicture){
			isTakingPicture=true;
			camera.takePicture(new ShutterCallback() {
				
				@Override
				public void onShutter() {
					// TODO Auto-generated method stub
					Log.i("me", "ShutterCallback");
				}
			}, new PictureCallback() {
				
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					// TODO Auto-generated method stub
					Log.i("me", "PictureCallback raw");
				}
			}, new PictureCallback() {
				
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					// TODO Auto-generated method stub
					Log.i("me", "PictureCallback jpeg");
					isTakingPicture=false;
					
					Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
					Matrix matrix=new Matrix();
					matrix.postRotate(-90);
					Bitmap bmp=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					bitmap.recycle();
					bitmap=null;
//					ByteArrayInputStream bis=new ByteArrayInputStream(data);
					long date=System.currentTimeMillis();
					SimpleDateFormat sdf=new SimpleDateFormat("hh-mm-ss");//yyyy-MM-dd*hh:mm:ss
					String time=sdf.format(date);
					try {
//						String path=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+time+".jpg";
						String path=Environment.getExternalStorageDirectory()+File.separator+time+".jpg";
						String name=time+".jpg";
						Log.i("me", path);
						FileOutputStream fos=new FileOutputStream(path);
						bmp.compress(CompressFormat.JPEG, 100, fos);
						Message msg=context.handler.obtainMessage();
						Bundle bundle=new Bundle();
						bundle.putString("path", path);
						bundle.putString("name", name);
						msg.setData(bundle);
						context.handler.sendMessage(msg);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					camera.startPreview();
				}
			});
		}
	}
	public void stopPreview(){
		if(camera!=null){
			camera.stopPreview();
			isTakingPicture=false;
		}
	}
	public static void setCameraDisplayOrientation(Activity activity,
	         int cameraId, android.hardware.Camera camera) {
	     android.hardware.Camera.CameraInfo info =
	             new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay()
	             .getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	 }

	

}
