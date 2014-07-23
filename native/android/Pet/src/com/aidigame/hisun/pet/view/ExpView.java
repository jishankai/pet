package com.aidigame.hisun.pet.view;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.LogUtil;

public class ExpView extends ImageView {
    float progress;
    int mode;
	public ExpView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void setProgress(final int progress,final int max,final Activity activity){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				this.progress=progress;
//				this.mode=mode;
				float height=0;
				float width=getWidth();
				setDrawingCacheEnabled(true);
				int count=0;
				while(getDrawingCache()==null){
					count++;
//					LogUtil.i("me", "循环次数："+count);
				}
				height=0;
				width=getWidth();
				Bitmap bmp=Bitmap.createBitmap(getDrawingCache());
				setDrawingCacheEnabled(false);
				Canvas canvas=new Canvas(bmp);
//				if(mode==1){
					height=((progress*1f/(max/2*1.0f))*getHeight());
				/*}else if(mode==3){
					height=(0.5f+(progress*1f/(max2*1f/2))/2)*this.getHeight();
				}else if(mode==2){
					height=1;
				}*/
					if(height==0)return;
				Bitmap bmp2=BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.line_red));
				Matrix matrix=new Matrix();
				matrix.postScale(width/bmp2.getWidth(), height/bmp2.getHeight());
				if(bmp2.getWidth()==0||bmp2.getHeight()==0)return;
				
				final Bitmap temp=Bitmap.createBitmap(bmp2, 0, 0, bmp2.getWidth(), bmp2.getHeight(), matrix, true);
				if(!bmp2.isRecycled()){
					bmp2.recycle();
				}
				canvas.save();
				canvas.drawBitmap(temp, 0, 0, null);
				canvas.restore();
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						setImageBitmap(temp);
					}
				});
				
			}
		}).start();
		
	}
	public ExpView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public ExpView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

	}

}
