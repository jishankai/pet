package com.aidigame.hisun.pet.view;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.R.color;
import com.aidigame.hisun.pet.constant.Constants;

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
		this.path=path;
		Bitmap  bmp=null;
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=1;
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
		bmp=Bitmap.createBitmap(bmp, 0,( bmp.getHeight()-height)/2, width, height);
		setImageBitmap(bmp);
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
