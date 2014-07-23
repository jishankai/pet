package com.aidigame.hisun.pet.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.R.color;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

public class TopicView extends ImageView{
    String path;
    int screen_width;
    int screen_height;
	public TopicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		
	}

	public  void  setImageBitmap(String path,Activity activity){
        screen_height=Constants.screen_height;
        screen_width=Constants.screen_width;
        int bmp_w=0;
        int bmp_h=0;
		this.path=path;
		Bitmap  temp=null;
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=1;
		options.inJustDecodeBounds=true;
		FileInputStream fis=null;
		try {
			fis=new FileInputStream(path);
			temp=BitmapFactory.decodeStream(fis,null, options);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		int width=0;
		bmp_h=options.outHeight;
		bmp_w=options.outWidth;
		if(bmp_w>screen_width){
			width=screen_width;
		}else{
			width=bmp_w;
			
		}
		int height=0;
		
		if(height>screen_height/3){
			height=screen_height/3;
		}else{
			height=bmp_h;
		}
		options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=4;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		fis=null;
		try {
			fis=new FileInputStream(path);
			temp=BitmapFactory.decodeStream(fis,null, options);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		Matrix matrix=new Matrix();
		if(temp==null)return;
//		matrix.postScale(width*1f/temp.getWidth(),height*1f/temp.getHeight() );
		LogUtil.i("exception", "path="+path);
		matrix.postScale(screen_width*1f/temp.getWidth(),screen_width*1f/temp.getWidth());
		Bitmap bmp=Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
		if(!temp.isRecycled()){
			temp.recycle();
		}
		int x=0,y=0;
		if(bmp.getWidth()>screen_width){
			width=screen_width;
			x=(bmp.getWidth()-width)/2;
		}else{
			width=bmp.getWidth();
			x=0;
		}
		if(bmp.getHeight()>screen_height/3){
			height=screen_height/3;
			y=(bmp.getHeight()-height)/2;
		}else{
			height=bmp.getHeight();
			y=0;
		}
		
		temp=Bitmap.createBitmap(bmp, x, y, width, height);
		if(!bmp.isRecycled()){
			LogUtil.i("exception", "图片回收了："+bmp.isRecycled());
			bmp.recycle();
		}
		LogUtil.i("exception", "bmp.getWidth()="+bmp.getWidth()+",bmp.getHeight()="+bmp.getHeight()+",screen_width="+screen_width+"screen_height/3"+screen_height/3);
		setImageBitmap(temp);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		/*Bitmap  bmp=null;
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		if(path==null){
			
			bmp=BitmapFactory.decodeResource(getResources(), R.drawable.a11,options);
		}else{
			bmp=BitmapFactory.decodeFile(path,options);
		}
		int width=0;
		if(bmp.getWidth()>screen_width){
			Matrix matrix=new Matrix();
			matrix.postScale(screen_width/(1f*bmp.getWidth()), screen_width/(1f*bmp.getWidth()));
			bmp=Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			width=screen_width;
		}else{
			width=bmp.getWidth();
		}
		int height=0;
		if(bmp.getHeight()>screen_height/3){
			height=screen_height/3;
		}else{
			height=bmp.getHeight();
		}
		canvas.drawColor(Color.WHITE);
		bmp=Bitmap.createBitmap(bmp, width,( bmp.getHeight()-height)/2, width, height);
		canvas.drawBitmap(bmp, (screen_width-width)/2, (screen_height-height)/2, null);*/
		
	}

}
