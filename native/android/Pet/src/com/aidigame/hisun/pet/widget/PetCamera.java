package com.aidigame.hisun.pet.widget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.ui.HandlePictureActivity;
import com.aidigame.hisun.pet.ui.TakePictureActivity;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;

public class PetCamera {
	Camera camera;
	TakePictureActivity context;
	SurfaceHolder holder;
	SurfaceView surfaceView;
	boolean isTakingPicture=false;
	boolean isViewing=false;
	public PetCamera(TakePictureActivity context,SurfaceHolder holder,SurfaceView surfaceView){
		this.context=context;
		this.holder=holder;
		this.surfaceView=surfaceView;
		camera=Camera.open();
		
	}
	/**
	 * 预览
	 */
	public void startPreview(){
		if(camera!=null&&!isViewing){
			try {
				Camera.Parameters param=camera.getParameters();
//				param.setPictureFormat(ImageFormat.JPEG);
//				param.setRotation(90);
//				List<Size> sizes=param.getSupportedVideoSizes();
				int width=surfaceView.getWidth();
				int height=surfaceView.getHeight();
				
			    LogUtil.i("me", "surfaceView.getWidth()="+surfaceView.getWidth()+";surfaceView.getHeight()"+surfaceView.getHeight());
				param.setPreviewSize(320, 240);
				LogUtil.i("me", "param.flatten():"+param.flatten());
				camera.setParameters(param);
				camera.setDisplayOrientation(90);
				camera.setPreviewDisplay(holder);
				camera.startPreview();
				isViewing=true;
			} catch (IOException e) {
//				 TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 拍照
	 */
	public void takePicture(){
		if(camera!=null&&!isTakingPicture){
			
			camera.takePicture(new ShutterCallback() {
				
				@Override
				public void onShutter() {
					// TODO Auto-generated method stub
					
				}
			}, new PictureCallback() {
				
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					// TODO Auto-generated method stub
					
				}
			}, new PictureCallback() {
				
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					// TODO Auto-generated method stub
					Bitmap bmp=BitmapFactory.decodeByteArray(data, 0, data.length);
					bmp=ImageUtil.rotateImage(bmp, 90);
					HandlePictureActivity.handlingBmp=bmp;
				    Message msg=context.handler.obtainMessage();
				    msg.what=context.TAKE_PICTURE_COMPLETED;
					context.handler.sendMessage(msg);
				}
			});
		}
	}
	/**
	 * 释放占用的相机资源
	 */
	public void stopCamera(){
		if(camera!=null){
			isViewing=false;
			camera.release();
			camera=null;
		}
	}


}
