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
	 * ���������ͼƬ ������͸����
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
	 * �����򣬽�����еİ�ɫ�����滻��Ŀ��ͼƬ����ͬ����
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
	 * ���ˮӡͼƬ��ˮӡͼƬΪ͸����
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
				return;//����ʹ�õ�ˮӡͼƬtransparent_wing.jpg�ϴ�ֻ����bmp��СͼƬ����������bmp������
				
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
	 * ���Ŵ�����λ�õ��ƶ���ˮӡͼƷҲ��֮�ƶ�
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
				return;//����ʹ�õ�ˮӡͼƬtransparent_wing.jpg�ϴ�ֻ����bmp��СͼƬ����������bmp������
				
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
	 * ����ʱ����Ч  ʹ���㷨   R=0.393r+0.769g+0.189b
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
	 * ʹ�ø�˹����ʵ��ģ��Ч��  �㷨�����Ÿ����RGBֵ�ֱ����˹�����еĶ�Ӧ����˵ĺͣ�
	 *                      Ȼ���ٳ���һ����Ӧ��ֵ��Ϊ��ǰ���ص��RGBֵ��
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
		int[] gauss={1,2,1,2,4,2,1,2,1};//��˹����
		int gaussIndex=0;
		int delta=16;//ֵ�Ĵ�СӰ��ģ��Ч��
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
	 * ʹ��������˹����ʵ����Ч��  
	 *            ��������˹�����е�������Ӧ���RGBֵ֮���ٳ�����Ӧ��ϵ���ĺ���Ϊ��ǰ���RGBֵ��
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
	 * ����Ч��     �㷨ԭ����ǰһ�����ص��RGBֵ�ֱ��ȥ��ǰ���ص��RGBֵ������127��Ϊ��ǰ���ص��RGBֵ��
	 * ���� ABC�����������ص� ��B��ĸ���Ч�����£�
	 *                              B.r=C.r-B.r+127;
	 *                              B.g=C.g-B.g+127;
	 *                              B.b=C.b-B.b+127;
	 *                               ע��RGBֵ��0~255֮�䡣
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
	 * ��ƬЧ��    
	 * �㷨ԭ������ǰ���ص��RGBֵ�ֱ���255֮����ֵ��Ϊ��ǰ���RGBֵ��
	 * ���� ABC�����������ص� ��B��ĵ�ƬЧ����
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
	 * ����Ч��   
	 * �㷨ԭ��ͼƬ��������ص㰴�ո���Բ�ģ�����Բ�뾶�ı仯�����ص��RGBֵ�ֱ������Ӧ��ֵ��Ϊ��ǰ���RGBֵ��
	 * ���� ABCDE
	 *    FGHIJ
	 *    KLMNO 
	 *    ���ָ��H��Ϊ����Ч�������ģ��뾶Ϊ�������ص㣬��ôG��RGBֵ�ֱ���ϵ�ֵ���F���Ҫ��
	 *    ��ΪRGBֵԽ�󣬾�Խ�ӽ���ɫ������G�㿴������F��Ҫ�ף�Ҳ���Ǿ��������Խ��
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
				int result=(int)(strenth*(1-distance/radius));//С���������
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
	 * ͼƬ����
	 * ����ԭ��������ͼƬ�����ص㰴͸���ȵ��ӣ����������ɫ���ˡ�
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
	 * ����Ч��
	 * ʵ�ֵ�Ч����ԲȦ֮��ͼƬ���ص㲻�䣬ԲȦ֮��ĵ���ģ������
	 * �����õ���ģ��Ч���͹���Ч��������Ƿ�����ԲȦ�ڵ��㷨��
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
	 * ����Ч��
	 * ��30�����ظ߶�Ϊ��λ��ǰ5�����ظ߶����ڵ��е����ص��ȫ��������ɺ�ɫ��
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
				Log.i("me","����");
			}
		}
		bmp.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bmp);
	}
	/**
	 * ��ImageView�л�ȡBitmap����
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
	 * ����ָ���Ŀ�ߣ���frameͼƬ��������
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
