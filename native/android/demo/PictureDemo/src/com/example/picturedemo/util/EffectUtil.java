package com.example.picturedemo.util;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.widget.ImageView;

public class EffectUtil {
	public static int touchX=0,touchY=0;
	public static Bitmap bmpWaterMark;
	/**
	 * 添加相框，相框图片 必须是透明的
	 * @param context
	 * @param imageView
	 */
	public static void  addFrame(Context context,ImageView imageView){
		Bitmap bmp=getBitmap(imageView);
		try {
			Bitmap frame=BitmapFactory.decodeStream(context.getAssets().open("picture_frame_01.png"));
			
			frame=scaleFramePicture(frame, bmp.getWidth(), bmp.getHeight());
			LayerDrawable layer=new LayerDrawable(new Drawable[]{new BitmapDrawable(bmp),new BitmapDrawable(frame)});
			Bitmap newBmp=layerDrawable2Bitmap(layer);
			bmp=null;
			frame=null;
			imageView.setImageBitmap(newBmp);
			newBmp=null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 添加相框，将相框中的白色区域替换成目标图片的相同区域
	 * @param context
	 * @param imageView
	 */
	public static void addFrame2(Context context,ImageView imageView){
		Bitmap bmp=getBitmap(imageView);
		try {
			Bitmap frame=BitmapFactory.decodeStream(context.getAssets().open("picture_frame_01.jpg"));
//			Bitmap frame=BitmapFactory.decodeStream(context.getAssets().open("transparent_wing.jpg"));
			frame=scaleFramePicture(frame, bmp.getWidth(), bmp.getHeight());
			int bmpR=0,bmpG=0,bmpB=0,bmpA=0;
			int frameR=0,frameG=0,frameB=0,frameA=0;
			int bmpPixel=0,framePixel=0;
			int newR=0,newG=0,newB=0,newA=0;
			int newPixel=0;
			float alpha=0f;
			for(int i=0;i<bmp.getWidth();i++){
				for(int j=0;j<bmp.getHeight();j++){
					bmpPixel=bmp.getPixel(i, j);
					framePixel=frame.getPixel(i, j);
					bmpR=Color.red(bmpPixel);
					bmpG=Color.green(bmpPixel);
					bmpB=Color.blue(bmpPixel);
					bmpA=Color.alpha(bmpPixel);
					frameR=Color.red(framePixel);
					frameG=Color.green(framePixel);
					frameB=Color.blue(framePixel);
					frameA=Color.alpha(framePixel);
					if(frameR>240&&frameG>240&&frameB>240){
						alpha=1f;
					}else{
						alpha=0f;
					}
					newR=(int)(bmpR*alpha+frameR*(1-alpha));
					newG=(int)(bmpG*alpha+frameG*(1-alpha));
					newB=(int)(bmpB*alpha+frameB*(1-alpha));
					newA=(int)(bmpA*alpha+frameA*(1-alpha));
					newPixel=Color.argb(newA, newR, newG, newB);
					frame.setPixel(i, j, newPixel);
				}
			}
			imageView.setImageBitmap(frame);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 添加水印图片，水印图片为透明的
	 * @param context
	 * @param imageView
	 */
	public static void addWaterMark(Context context,ImageView imageView){
		bmpWaterMark=getBitmap(imageView);
		try {
			Bitmap waterMark=BitmapFactory.decodeStream(context.getAssets().open("transparent_wing.jpg"));
			int width=bmpWaterMark.getWidth()>waterMark.getWidth()?bmpWaterMark.getWidth():waterMark.getWidth();
			int height=bmpWaterMark.getHeight()>waterMark.getHeight()?bmpWaterMark.getHeight():waterMark.getHeight();
			Bitmap newBmp=Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
			Canvas canvas=new Canvas(newBmp);
			if(bmpWaterMark.getWidth()==width){
				return;//由于使用的水印图片transparent_wing.jpg较大，只处理bmp是小图片的情况，大的bmp不处理。
				
			}else{
				canvas.drawBitmap(waterMark, 0,0, null);
				if(touchX==0&&touchY==0){
					canvas.drawBitmap(bmpWaterMark, (waterMark.getWidth()-bmpWaterMark.getWidth())/2, 0, null);
				}else{
					canvas.drawBitmap(bmpWaterMark, touchX, touchY, null);
				}
				
			}
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			imageView.setImageBitmap(newBmp);
			waterMark=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 随着触碰点位置的移动，水印图品也随之移动
	 * @param context
	 * @param imageView
	 */
	public static void addWaterMark2(Context context,ImageView imageView){
		Bitmap bmp=bmpWaterMark;
		try {
			Bitmap waterMark=BitmapFactory.decodeStream(context.getAssets().open("transparent_wing.jpg"));
			int width=bmp.getWidth()>waterMark.getWidth()?bmp.getWidth():waterMark.getWidth();
			int height=bmp.getHeight()>waterMark.getHeight()?bmp.getHeight():waterMark.getHeight();
			Bitmap newBmp=Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
			Canvas canvas=new Canvas(newBmp);
			if(bmp.getWidth()==width){
				return;//由于使用的水印图片transparent_wing.jpg较大，只处理bmp是小图片的情况，大的bmp不处理。
				
			}else{
				canvas.drawBitmap(waterMark, 0,0, null);
				if(touchX==0&&touchY==0){
					canvas.drawBitmap(bmp, (waterMark.getWidth()-bmp.getWidth())/2, 0, null);
				}else{
					canvas.drawBitmap(bmp, touchX, touchY, null);
				}
				
			}
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			imageView.setImageBitmap(newBmp);
			bmp=null;
			waterMark=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 怀旧时光特效  使用算法   R=0.393r+0.769g+0.189b
	 *                 G=0.349r+0.686g+0.168b
	 *                 B=0.272r+0.534g+0.131b
	 * @param context
	 * @param imageView
	 */
	public static void oldTimeEffect(Context context,ImageView imageView){
		Bitmap bmp=getBitmap(imageView);
		int width=bmp.getWidth();
		int height=bmp.getHeight();
		int pixel=0;
		int pixR=0,pixG=0,pixB=0;
		int newR=0,newG=0,newB=0;
		int[] pixels=new int[width*height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				pixel=pixels[i*width+j];
				pixR=Color.red(pixel);
				pixG=Color.green(pixel);
				pixB=Color.blue(pixel);
				newR=(int)(0.393*pixR+0.769*pixG+0.189*pixB);
				newG=(int)(0.349*pixR+0.686*pixG+0.168*pixB);
				newB=(int)(0.271*pixR+0.534*pixG+0.131*pixB);
				int newColor=Color.argb(255, newR>255?255:newR, newG>255?255:newG, newB>255?255:newB);
				pixels[i*width+j]=newColor;
			}
		}
		bmp.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bmp);
	}
	/**
	 * 使用高斯矩阵实现模糊效果  算法：将九个点的RGB值分别与高斯矩阵中的对应项相乘的和，
	 *                      然后再除以一个相应的值作为当前像素点的RGB值。
	 * @param context
	 * @param imageView
	 */
	public static void blurEffectByGauss(Context context,ImageView imageView){
		Bitmap bitmap=getBitmap(imageView);
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] pixels=new int[width*height];
		int pixR=0,pixG=0,pixB=0;
		int newR=0,newG=0,newB=0;
		int pixel=0;
		int newPixel=0;
		int[] gauss={1,2,1,2,4,2,1,2,1};//高斯矩阵
		int gaussIndex=0;
		int delta=16;//值的大小影响模糊效果
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		for(int i=1;i<height-1;i++){
			for(int j=1;j<width-1;j++){
				gaussIndex=0;
				for(int m=-1;m<=1;m++){
					for(int n=-1;n<=1;n++){
						pixel=pixels[(m+i)*width+(n+j)];
						pixR=Color.red(pixel);
						pixG=Color.green(pixel);
						pixB=Color.blue(pixel);
						newR+=pixR*gauss[gaussIndex];
						newG+=pixG*gauss[gaussIndex];
						newB+=pixB*gauss[gaussIndex];
						gaussIndex++;
					}
				}
				
				newR=newR/delta;
				newG=newG/delta;
				newB=newB/delta;
				newPixel=Color.argb(255, Math.min(255, newR), Math.min(255,newG), Math.min(255, newB));
				pixels[i*width+j]=newPixel;
				newR=0;
				newG=0;
				newB=0;
			}
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bitmap);
	}
	/**
	 * 使用拉普拉斯矩阵实现锐化效果  
	 *            将拉普拉斯矩阵中的项与相应点的RGB值之积再乘以相应的系数的和作为当前点的RGB值。
	 *            E.r=A.r*laplacian[0]*delta+B.r*laplacian[1]*delta
	 *            +C.r*laplacian[2]*delta+D.r *laplacian[3]*delta
	 *            +E.r*laplacian[4]*delta+F.r*laplacian[5]*delta
	 *            +G.r*laplacian[6]*delta+H.r*laplacian[7]*delta
	 *            +I.r*laplacian[8]*delta;
	 * @param context
	 * @param imageView
	 */
	public static void sharpenEffect(Context context,ImageView imageView){
		Bitmap bmp=getBitmap(imageView);
		int width=bmp.getWidth();
		int height=bmp.getHeight();
		int[] pixels=new int[width*height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pixR=0,pixG=0,pixB=0;
		int pixel=0;
		int newR=0,newG=0,newB=0;
		int newPixel=0;
		float delta=0.3f;
		int[] laplacian={-1,-1,-1,-1,9,-1,-1,-1,-1};
		int laplacianIndex=0;
		for(int i=1;i<height-1;i++){
			for(int j=1;j<width-1;j++){
				laplacianIndex=0;
				for(int m=-1;m<=1;m++){
					for(int n=-1;n<=1;n++){
						pixel=pixels[(m+i)*width+(n+j)];
						pixR=Color.red(pixel);
						pixG=Color.green(pixel);
						pixB=Color.blue(pixel);
						newR+=pixR*delta*laplacian[laplacianIndex];
						newG+=pixG*delta*laplacian[laplacianIndex];
						newB+=pixB*delta*laplacian[laplacianIndex];
						laplacianIndex++;
					}
				}
				newR=Math.min(255, Math.max(0, newR));
				newG=Math.min(255, Math.max(0, newG));
				newB=Math.min(255, Math.max(0, newB));
				newPixel=Color.argb(255, newR, newG, newB);
				pixels[i*width+j]=newPixel;
				newR=0;
				newG=0;
				newB=0;
			}
		}
		bmp.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bmp);
	}
	/**
	 * 浮雕效果     算法原理：用前一个像素点的RGB值分别减去当前像素点的RGB值并加上127作为当前像素点的RGB值。
	 * 例： ABC三个相邻像素点 求B点的浮雕效果如下：
	 *                              B.r=C.r-B.r+127;
	 *                              B.g=C.g-B.g+127;
	 *                              B.b=C.b-B.b+127;
	 *                               注意RGB值在0~255之间。
	 * @param context
	 * @param imageView
	 */
	public static void embossEffect(Context context,ImageView imageView){
		Bitmap bmp=getBitmap(imageView);
		int width=bmp.getWidth();
		int height=bmp.getHeight();
		int[] pixels=new int[width*height];
		int pixR=0,pixG=0,pixB=0;
		int pixel=0;
		int pixelF=0;
		int newR=0,newG=0,newB=0;
		int newPixel=0;
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for(int i=0;i<height-1;i++){
			for(int j=0;j<width-1;j++){
				pixel=pixels[i*width+j];
				pixR=Color.red(pixel);
				pixG=Color.green(pixel);
				pixB=Color.blue(pixel);
				pixelF=pixels[i*width+j+1];
				newR=Color.red(pixelF)-pixR+127;
				newG=Color.green(pixelF)-pixG+127;
				newB=Color.blue(pixelF)-pixB+127;
				newR=Math.min(255, Math.max(0, newR));
				newG=Math.min(255, Math.max(0, newG));
				newB=Math.min(255, Math.max(0, newB));
				newPixel=Color.argb(255, newR, newG, newB);
				pixels[i*width+j]=newPixel;
			}
		}
		bmp.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bmp);
	}
	/**
	 * 底片效果    
	 * 算法原理：将当前像素点的RGB值分别与255之差后的值作为当前点的RGB值。
	 * 例： ABC三个相邻像素点 求B点的底片效果：
	 *                           B.r=255-B.r;
	 *                           B.g=255-B.g;
	 *                           B.b=255-B.b
	 * @param context
	 * @param imageView
	 */
	public static void filmEffect(Context context,ImageView imageView){
		Bitmap bitmap=getBitmap(imageView);
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int[] pixels=new int[width*height];
		int pixR=0,pixG=0,pixB=0;
		int pixel;
		int newR=0,newG=0,newB=0;
		int newPixel;
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				pixel=pixels[i*width+j];
				pixR=Color.red(pixel);
				pixG=Color.green(pixel);
				pixB=Color.blue(pixel);
				newR=255-pixR;
				newG=255-pixG;
				newB=255-pixB;
				newPixel=Color.argb(255, newR, newG, newB);
				pixels[width*i+j]=newPixel;
			}
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bitmap);
	}
	/**
	 * 光照效果   
	 * 算法原理：图片上面的像素点按照给定圆心，按照圆半径的变化，像素点的RGB值分别加上相应的值作为当前点的RGB值。
	 * 例： ABCDE
	 *    FGHIJ
	 *    KLMNO 
	 *    如果指定H点为光照效果的中心，半径为两个像素点，那么G点RGB值分别加上的值会比F点的要大，
	 *    因为RGB值越大，就越接近白色，所以G点看起来比F点要白，也就是距光照中心越近
	 * @param context
	 * @param imageView
	 */
	public static void sunshineEffect(Context context,ImageView imageView){
		Bitmap bmp=getBitmap(imageView);
		int width=bmp.getWidth();
		int height=bmp.getHeight();
		int radius=Math.min(width, height)/2;
		int centerX=width/2;
		int centerY=height/2;
		float strenth=100f;
		int[] pixels=new int[width*height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pixel=0;
		int newPixel=0;
		int pixR=0,pixG=0,pixB=0;
		int newR=0,newG=0,newB=0;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				pixel=pixels[i*width+j];
				float distance=(float)Math.sqrt(Math.pow(centerY-i, 2)+Math.pow(centerX-j, 2));
				int result=(int)(strenth*(1-distance/radius));//小数点的问题
				if(distance<=radius){
					newR=Color.red(pixel)+result;
					newG=Color.green(pixel)+result;
					newB=Color.blue(pixel)+result;
					newR=Math.min(255,Math.max(0, newR));
					newG=Math.min(255,Math.max(0, newG));
					newB=Math.min(255,Math.max(0, newB));
					newPixel=Color.argb(255,newR, newG, newB);
					pixels[width*i+j]=newPixel;
				}
				
			}
		}
		bmp.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bmp);
	}
	/**
	 * 图片叠加
	 * 叠加原理是两张图片的像素点按透明度叠加，不会进行颜色过滤。
	 * @param context
	 * @param imageView
	 */
	public static void overlayEffect(Context context,ImageView imageView){
		Bitmap bmp=getBitmap(imageView);
		try {
			Bitmap overlay=BitmapFactory.decodeStream(context.getAssets().open("beauty_girl_01.jpg"));
			int width=bmp.getWidth();
			int height=bmp.getHeight();
			overlay=scaleFramePicture(overlay, width, height);
			int[] bmpPixels=new int[width*height];
			int[] overlayPixels=new int[width*height];
			bmp.getPixels(bmpPixels, 0, width, 0, 0, width, height);
			overlay.getPixels(overlayPixels, 0, width, 0, 0, width, height);
			int bmpR=0,bmpG=0,bmpB=0,bmpA=0;
			int overlayR=0,overlayG=0,overlayB=0,overlayA=0;
			int bmpPixel=0,overlayPixel=0;
			int newR=0,newG=0,newB=0,newA=0;
			int newPixel=0;
			float alpha=0.5f;
			for(int i=0;i<height;i++){
				for(int j=0;j<width;j++){
					bmpPixel=bmpPixels[i*width+j];
					overlayPixel=overlayPixels[i*width+j];
					bmpR=Color.red(bmpPixel);
					bmpG=Color.green(bmpPixel);
					bmpB=Color.blue(bmpPixel);
					bmpA=Color.alpha(bmpPixel);
					overlayR=Color.red(overlayPixel);
					overlayG=Color.green(overlayPixel);
					overlayB=Color.blue(overlayPixel);
					overlayA=Color.alpha(overlayPixel);
					newR=(int)(alpha*bmpR+(1-alpha)*overlayR);
					newG=(int)(alpha*bmpG+(1-alpha)*overlayG);
					newB=(int)(alpha*bmpB+(1-alpha)*overlayB);
					newA=(int)(alpha*bmpA+(1-alpha)*overlayA);
					newPixel=Color.argb(newA, newR, newG, newB);
					bmpPixels[i*width+j]=newPixel;
				}
			}
			bmp.setPixels(bmpPixels, 0, width, 0, 0, width, height);
			imageView.setImageBitmap(bmp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 光晕效果
	 * 实现的效果是圆圈之内图片像素点不变，圆圈之外的点做模糊处理。
	 * 所以用到了模糊效果和光照效果里面的是否是在圆圈内的算法。
	 * @param context
	 * @param imageView
	 */
	public static void haloEffect(Context context,ImageView imageView){
		Bitmap bmp=getBitmap(imageView);
		int width=bmp.getWidth();
		int height=bmp.getHeight();
		int[] pixels=new int[width*height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pixR=0,pixG=0,pixB=0;
		int newR=0,newG=0,newB=0;
		int pixel=0,newPixel=0;
		int centerX=width/2;
		int centerY=height/2;
		int radius=Math.min(width, height)/3;
		int[] gauss={1,2,1,2,4,2,1,2,1};
		int gaussIndex=0;
		int delta=18;
		for(int i=1;i<height-1;i++){
			for(int j=1;j<width-1;j++){
				float distance=(float)Math.sqrt(Math.pow(centerY-i, 2)+Math.pow(centerX-j, 2));
				if(distance>=radius){
					gaussIndex=0;
					for(int m=-1;m<=1;m++){
						for(int n=-1;n<=1;n++){
							pixel=pixels[(i+m)*width+n+j];
							pixR=Color.red(pixel);
							pixG=Color.green(pixel);
							pixB=Color.blue(pixel);
							newR+=pixR*gauss[gaussIndex];
							newG+=pixG*gauss[gaussIndex];
							newB+=pixB*gauss[gaussIndex];
							gaussIndex++;
						}
					}
					newR=newR/delta;
					newG=newG/delta;
					newB=newB/delta;
					newR=Math.min(255, Math.max(0, newR));
					newG=Math.min(255, Math.max(0, newG));
					newB=Math.min(255, Math.max(0, newB));
					newPixel=Color.argb(255, newR, newG, newB);
					pixels[i*width+j]=newPixel;
				}
				
			}
			bmp.setPixels(pixels, 0, width, 0, 0, width, height);
			imageView.setImageBitmap(bmp);
		}
	}
	/**
	 * 条纹效果
	 * 以30个像素高度为单位，前5个像素高度所在的行的像素点会全部被处理成黑色。
	 * @param context
	 * @param imageView
	 */
	public static void striaEffect(Context context,ImageView imageView){
		Bitmap bmp=getBitmap(imageView);
		int width=bmp.getWidth();
		int height=bmp.getHeight();
		int[] pixels=new int[width*height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int delta=30;
		int blackHeight=5;
		int newR=0,newG=0,newB=0;
		int newPixel=Color.argb(255, newR, newG, newB);
		for(int i=0;i<height;i++){
			if(i%delta<blackHeight){
				for(int j=0;j<width;j++){
					pixels[i*width+j]=newPixel;
				}
				Log.i("me","条纹");
			}
		}
		bmp.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bmp);
	}
	/**
	 * 从ImageView中获取Bitmap对象。
	 * @param imageView
	 * @return
	 */
	public static Bitmap getBitmap(ImageView imageView){
		imageView.setDrawingCacheEnabled(true);
		Bitmap bitmap=Bitmap.createBitmap(imageView.getDrawingCache());
		imageView.setDrawingCacheEnabled(false);
		return bitmap;
	}
	/**
	 * 按照指定的宽高，对frame图片进行缩放
	 * @param frame
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap scaleFramePicture(Bitmap frame,int width,int height){
		Matrix matrix=new Matrix();
		float scaleX=width*1f/frame.getWidth();
		float scaleY=height*1f/frame.getHeight();
		matrix.postScale(scaleX, scaleY);
		return Bitmap.createBitmap(frame, 0, 0, frame.getWidth(),frame.getHeight(), matrix, true);
	}
	/**
	 * 
	 * @param layer
	 * @return
	 */
	public static Bitmap layerDrawable2Bitmap(LayerDrawable layer){
		Bitmap bmp=Bitmap.createBitmap(layer.getIntrinsicWidth(), layer.getIntrinsicHeight(),
				                       layer.getOpacity()!=PixelFormat.OPAQUE?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565);
		Canvas canvas=new Canvas(bmp);
		layer.setBounds(0, 0, layer.getIntrinsicWidth(), layer.getIntrinsicHeight());
		layer.draw(canvas);
		return bmp;
	}

}
