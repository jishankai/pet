package com.aidigame.hisun.pet.view;

import com.aidigame.hisun.pet.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
/**
 * 圆环进度条
 * @author admin
 *
 */
public class BallProgressView extends View {
    float startAngle=0f,sweepAngle=0f;
    float speed;
    OnProgressStopListener onProgressStopListener;
    boolean isStop=false;
    int recordTime=0;
    Activity activity;
    Bitmap pressBmp,greenBmp;
	public BallProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		pressBmp=BitmapFactory.decodeResource(getResources(),R.drawable.bite_press_to_record);
		greenBmp=BitmapFactory.decodeResource(getResources(),R.drawable.bite_green_circle);
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		
		
//		super.onDraw(canvas);
//		canvas.drawColor(0xffffff);
		Paint paint=new Paint();
		paint.setColor(getResources().getColor(R.color.ball_gray));//e1e1e1
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		int w=getWidth();
		int h=getHeight();
		canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2, paint);
		paint.setColor(getResources().getColor(R.color.ball_blue));
		canvas.drawArc(new RectF(0f, 0f, getWidth(), getHeight()), -90f, startAngle, true, paint);
		/*if(startAngle==0){
			canvas.drawBitmap(pressBmp, (getWidth()-pressBmp.getWidth())/2, (getHeight()-pressBmp.getHeight())/2, null);
			
		}else{
			paint.setColor(getResources().getColor(R.color.ball_blue));
			canvas.drawArc(new RectF(0f, 0f, getWidth(), getHeight()), -90f, startAngle, true, paint);
			
			canvas.drawBitmap(greenBmp, (getWidth()-greenBmp.getWidth())/2, (getHeight()-greenBmp.getHeight())/2, null);
			paint.setColor(Color.WHITE);
			int textSize=getResources().getDimensionPixelSize(R.dimen.bite_textsize);
			paint.setTextSize(textSize);
			
			canvas.drawText("松开结束", (getWidth()-textSize*4)/2, (getHeight()-textSize)/2, paint);
		}*/
		
		
	}
	boolean ispause=false;
	public void setPause(boolean flag){
		ispause=flag;
	}
	/**
	 * 停止进度条
	 * @param flag
	 */
	public void setStop(boolean flag){
		isStop=flag;
	}
	public void setOnProgressStopListener(OnProgressStopListener listener){
		this.onProgressStopListener=listener;
	}
	/**
	 * 设置速度和初始化起始角度
	 * @param speed
	 */
	int count=0;
	int time=0;
	public void setSpeed(int time,Activity a){
		startAngle=0f;
		this.time=time;
		this.speed=360f/(time*20f);
		this.activity=a;
		isStop=false;
		count=0;
		if(thread!=null&&thread.isAlive()){
			thread.stop();
		}
		newThread();
		thread.start();
		invalidate();
	}
	public void newThread(){
		thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (startAngle<=360f) {
					while(ispause){
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(isStop){
						if(onProgressStopListener!=null)
							onProgressStopListener.onStop();
						return;
					}
						
					startAngle=startAngle+BallProgressView.this.speed;
					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					postInvalidate();
					count++;
					if(onProgressStopListener!=null)
					onProgressStopListener.onProgress(count*50/1000);
				}
				startAngle=360f;
				postInvalidate();
				if(onProgressStopListener!=null){
	                onProgressStopListener.onProgress(time);
					onProgressStopListener.onStop();
				}
				
				
			}
		});
	}
	Thread thread;
	public static interface OnProgressStopListener{
		void onStop();//进度条结束时调用
		void onProgress(int time);//进度条更新时调用 更新录制时间 单位秒
	}
	public void reset(){
		startAngle=0f;
		invalidate();
	}

}
