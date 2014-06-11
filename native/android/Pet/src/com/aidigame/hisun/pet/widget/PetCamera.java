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
	int mode;//0,需要透镜处理；1，不需要透镜处理
	public PetCamera(TakePictureActivity context,SurfaceHolder holder,SurfaceView surfaceView,int mode){
		this.context=context;
		this.holder=holder;
		this.surfaceView=surfaceView;
		camera=Camera.open();
		this.mode=mode;
	}
	/**
	 * Ԥ��
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
	 * ����
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
					BitmapFactory.Options options=new BitmapFactory.Options();
					options.inSampleSize=4;
					Bitmap bmp=BitmapFactory.decodeByteArray(data,0,data.length,options);
					bmp=ImageUtil.rotateImage(bmp, 90);
					HandlePictureActivity.handlingBmp=bmp;
				    Message msg=context.handler.obtainMessage();
				    
				    msg.what=mode;
					context.handler.sendMessage(msg);
				}
			});
		}
	}
	/**
	 * �ͷ�ռ�õ������Դ
	 */
	public void stopCamera(){
		if(camera!=null){
			isViewing=false;
			camera.release();
			camera=null;
		}
	}


}
