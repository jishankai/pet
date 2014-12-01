package com.aidigame.hisun.pet.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class RotatView extends View {
	Bitmap bmp;

	public RotatView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public RotatView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RotatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawBitmap(bmp, null, null);
	}
	public void setBitmap(Bitmap bmp){
		bmp=this.bmp;
		invalidate();
		
	}
	boolean running=true;
	public void startAnim(){
            new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(running){
					
				}
			}
		}).start();
	}
	public void stopAnim(){
		running=false;
	}
	

}
