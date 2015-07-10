package com.aidigame.hisun.imengstar.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.R;

public class ImageUtil {
	/**
	 * 对图片进行缩放
	 * @param path 
	 * @param size  
	 * @return      
	 */
	public static Bitmap scaleImage(String path,int size){
		Options options=new BitmapFactory.Options();
		options.inSampleSize=size;
		return BitmapFactory.decodeFile(path, options);
	}
	public static Bitmap scaleImage(Bitmap bmp,float sizeX,float sizeY){
		Matrix matrix=new Matrix();
		matrix.postScale(sizeX, sizeY);
		return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
	}
	/**
	 * 将给定的图片以quality质量进行压缩，保存到sd卡中
	 * @param bmp  
	 * @param quality  0-100
	 * @return  path   图片的路径名
	 */
	public static String compressImage(Bitmap bmp,int quality){
		long time=System.currentTimeMillis();
		boolean flag=false;
		File file=new File(Environment.getExternalStorageDirectory()
			    +File.separator+"pet");
		if(!file.exists()){
			file.mkdir();
		}
		String path=Environment.getExternalStorageDirectory()
				    +File.separator+"pet"+File.separator
				    +time+".jpg";
//		HandlePictureActivity.originPicturePath=path;
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(new File(path));
			flag=bmp.compress(CompressFormat.JPEG, quality, fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
		if(flag){
			return path;
		}else{
			return null;
		} 
	}
	public static String compressImage(Bitmap bmp,int quality,String path){
		boolean flag=false;
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(new File(path));
			flag=bmp.compress(CompressFormat.JPEG, quality, fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
		if(flag){
			return path;
		}else{
			return null;
		} 
	}
	/**
	 * 对图片进行旋转
	 * @param bmp
	 * @param degree
	 * @return
	 */
	public static Bitmap rotateImage(Bitmap bmp,float degree){
		Matrix matrix=new Matrix();
		matrix.postRotate(90);
		bmp=Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		return bmp;
	}
	/**
	 * 根据给定的View空间的宽高，对图片进行缩放
	 * @param bmp
	 * @param view
	 * @return
	 */
	public static Bitmap scaleImageByView(Bitmap bmp,View view){
		int width=bmp.getWidth();
		int height=bmp.getHeight();
		float scaleX=view.getWidth()/(width*1f);
		float scaleY=view.getHeight()/(height*1f);
		LogUtil.i("me", "view.getWidth()="+view.getWidth()+",view.getHeight()="+view.getHeight()+";width="+width+",height="+height);
		float scale=scaleX>scaleY?scaleX:scaleY;
		bmp=scaleImage(bmp, scaleX, scaleY);
		return bmp;
	}
	/**
	 * 获取ImageView中的图片，（对ImageView所在的区域进行截图）
	 * @param imageView
	 * @return
	 */
	public static Bitmap getBitmapFromImageView(ImageView imageView){
		imageView.setDrawingCacheEnabled(true);
		Bitmap bmp=Bitmap.createBitmap(imageView.getDrawingCache());
		imageView.setDrawingCacheEnabled(false);
		return bmp;
	}
	/**
	 * �˾�  ��ɫ����
	 * @param bmp
	 * @param alpha
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static Bitmap addColor(Bitmap bmp,int alpha,int red,int green,int blue){
		int[] pixels=new int[bmp.getWidth()*bmp.getHeight()];
		int width=bmp.getWidth();
		int height=bmp.getHeight();
		float delta=0.3f;
		
		Bitmap newBmp=Bitmap.createBitmap(width,height,bmp.getConfig()!=Bitmap.Config.RGB_565?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565);
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pixA=0,pixR=0,pixG=0,pixB=0;
		int pixel=0;
		int newPixA=0,newPixR=0,newPixG=0,newPixB=0;
		int newPixel=0;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				pixel=pixels[i*width+j];
				pixA=Color.alpha(pixel);
				pixR=Color.red(pixel);
				pixG=Color.green(pixel);
				pixB=Color.blue(pixel);
				newPixA=(int)((1-delta)*pixA+delta*alpha);
				newPixR=(int)((1-delta)*pixR+delta*red);
				newPixG=(int)((1-delta)*pixG+delta*green);
				newPixB=(int)((1-delta)*pixB+delta*blue);
				newPixA=Math.max(0, Math.min(255, newPixA));
				newPixR=Math.max(0, Math.min(255, newPixR));
				newPixG=Math.max(0, Math.min(255, newPixG));
				newPixB=Math.max(0, Math.min(255, newPixB));
				newPixel=Color.argb(newPixA, newPixR, newPixG, newPixB);
				pixels[i*width+j]=newPixel;
			}
		}
		newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
		return newBmp;
	}
	/**
	 * 删除指定路径下的所有文件、文件夹
	 * @param path
	 */
	public static void deleteFile(String path){
		if(path==null)return;
		File file=new File(path);
		if(!file.exists()){
			path=null;
			return;
		}
		File[] files=file.listFiles();
		for(File f:files){
			if(f.isDirectory()){
				deleteFile(f.getAbsolutePath());
			}else{
				f.delete();
			}
			
		}
		path=null;
	}
	/**
	 * 插图
	 */
	public static Bitmap chartlet(Bitmap dest,int x,int y,Bitmap chartlet){
		Bitmap bmp=Bitmap.createBitmap(dest.getWidth(), dest.getHeight(), Bitmap.Config.RGB_565);
		Canvas canvas=new Canvas(bmp);
		canvas.drawBitmap(dest, 0, 0, null);
		if(x==0&&y==0){
			canvas.drawBitmap(chartlet, (dest.getWidth()-chartlet.getWidth())/2, (dest.getHeight()-chartlet.getHeight())/2, null);
		}else{
			canvas.drawBitmap(chartlet, x, y, null);
		}
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		dest=null;
		chartlet=null;
		return bmp;
	}

	public static void drawBitmap(SurfaceHolder holder,Bitmap bmp){
		Canvas canvas=holder.lockCanvas();
		canvas.drawBitmap(bmp, 0, 0, null);
//		HandlePictureActivity.handlingBmp=bmp;
		holder.unlockCanvasAndPost(canvas);
	}
	public static Bitmap getBitmapFromSurfaceView(SurfaceView view){
		int width=view.getWidth();
		int height=view.getHeight();
		Bitmap bmp=Bitmap.createBitmap(width,height,Config.RGB_565);
		Canvas canvas=new Canvas(bmp);
		view.draw(canvas);
		return bmp;
	}
	public static Bitmap getImageFromView(View view){
		int width=view.getWidth();
		int height=view.getHeight();
		Bitmap bmp=Bitmap.createBitmap(width,height,Config.ARGB_8888);
		Canvas canvas=new Canvas(bmp);
		view.draw(canvas);
		return bmp;
	}
	public static Bitmap getBitmapFromView(View view,Context context){
		long l1=System.currentTimeMillis();
		Bitmap bmp=Bitmap.createBitmap(view.getWidth(),view.getHeight(),Config.ARGB_8888);
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		
		Canvas canvas=new Canvas(bmp);
		view.draw(canvas);
//		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
//		byte[] pixls=baos.toByteArray();
//		BitmapFactory.Options opts=new BitmapFactory.Options();
//		opts.inSampleSize=2;
		LogUtil.i("scroll",""+( System.currentTimeMillis()-l1));
//		return BitmapFactory.decodeByteArray(pixls, 0,baos.size(), opts);
		Matrix matrix=new Matrix();
		matrix.postScale(0.4f, 0.4f);
		bmp=Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		Bitmap newImg = com.aidigame.hisun.imengstar.blur.Blur.fastblur(context, bmp, 30);//18,50
		
		return newImg;
	}
	/**
	 * 压缩图片为png格式的
	 * @param bitmap
	 * @return
	 */
	public static String compressImage(Bitmap bitmap,String end){
		String fileName=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+"_"+end+".jpg";
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(fileName);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			return fileName;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public static String compressImageByName(Bitmap bitmap,String name){
		String fileName=Constants.Picture_Root_Path+File.separator+name+".jpg";
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(fileName);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			return fileName;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}


}
