package com.aidigame.hisun.pet.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ChartletBmp {
	public int width;
	public int height;
	public Bitmap bitmap;
	public int centerX;
	public int centerY;
	public int x;
	public int y;
	public float degrees;
	public ChartletBmp(Bitmap bitmap,int centerX,int centerY,float degrees){
		this.bitmap=bitmap;
		this.centerY=centerY;
		this.centerX=centerX;
		this.width=bitmap.getWidth();
		this.height=bitmap.getHeight();
		x=centerX-width/2;
		y=centerY-height/2;
		this.degrees=degrees;
	}
	public void draw(Canvas canvas){
		canvas.drawBitmap(bitmap, x, y, null);
	}

}
