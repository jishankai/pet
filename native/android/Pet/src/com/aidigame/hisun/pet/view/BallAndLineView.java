package com.aidigame.hisun.pet.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.pet.ui.ChoseStarActivity;
import com.aidigame.hisun.pet.util.LogUtil;
/**
 * 星球选择页面，星球展示view
 * 效果，星球与红线从顶部开始下落，到达底部后，星球开始抖动，抖动距离逐渐减小，直到停止
 * @author admin
 *
 */
public class BallAndLineView extends View implements OnTouchListener{
    public int imageId;
    public Bitmap redLine;
    Bitmap image;
    int clickTop;//星球点击事件的响应范围的最顶部
    int clickLeft;//星球点击事件的响应范围的最左边
    ChoseStarActivity activity;//使用此控件的Activity
    int height,maxHeight;//maxHeight控件被赋予的高度
    int speed;//控件下落的速度
    int currentHeight;//控件的绘制的当前高度(动画播出时，一直变化，从0到maxHeight),
    boolean animStart=false;
	public BallAndLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setOnTouchListener(this);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		if(w<20){
			w=Constants.screen_width/3;
			h=getHeight();
			LayoutParams params=getLayoutParams();
			params.width=w;
			this.setLayoutParams(params);
			
			redLine=BitmapFactory.decodeResource(getResources(), R.drawable.line_red);
			Matrix matrix=new Matrix();
			
			matrix.postScale(w/18/(redLine.getWidth()*1f), (h-(w/2))/(redLine.getHeight()*1f));
			
			redLine=redLine.createBitmap(redLine, 0, 0, redLine.getWidth(), redLine.getHeight(), matrix, true);
			if(image==null){
				image=BitmapFactory.decodeResource(getResources(), imageId);
				//此处matrix必须重新创建一个，不然，设置的缩放比例不起作用
				matrix=new Matrix();
				if(imageId==R.drawable.dog_star){
					float a=w/(image.getWidth()*1f)-0.2f;
					matrix.postScale(w/(image.getWidth()*1f)-0.2f, a);
				}else{
					float a=w/(image.getWidth()*1f)-0.13f;
					matrix.postScale(w/(image.getWidth()*1f)-0.13f, a);
				}
				image=Bitmap.createBitmap(image,0,0,image.getWidth(),image.getHeight(),matrix,true);
				if(!animStart){
					maxHeight=h;
					animStart=true;
					
//					speed=(h)/90;
					speed=8;
					if(imageId==R.drawable.dog_star){
						LogUtil.i("scroll", "选中猫星");
						speed=(h)/40;
					}
					currentHeight=-h;
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							while(currentHeight<-redLine.getWidth()*2){
								
								try {
									Thread.sleep(60);
										currentHeight+=speed;
										speed=(int) (speed*(1.3f));//速度逐渐增加。
										if(currentHeight>=-redLine.getWidth()*2){
											currentHeight=-redLine.getWidth()*2;
										}
										postInvalidate();
									
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							//星球开始抖动
							float distance=redLine.getWidth()*2;//上下弹动时，远离中心线的最大距离
							float currentMaxDistance=distance;
							float currentDistance=0;
							float speed=currentMaxDistance/4;
							float temp=currentHeight;
							while(currentMaxDistance>0){
								//小球在中心线开始下落，一直移动currentMaxDistance距离
								while(currentDistance<currentMaxDistance){
									currentDistance+=speed;
									try {
										Thread.sleep(60);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if(currentDistance>currentMaxDistance){
										currentDistance=currentMaxDistance;
										
									}
									currentHeight=(int) (temp+currentDistance);
									if(currentHeight>0){
										currentHeight=0;
									}
									postInvalidate();
								}
								temp=currentHeight;
								//小球从最低点向回滚动，一直到距离中心线-currentMaxDistance距离
								while(currentDistance>-currentMaxDistance){
									currentDistance-=speed;
									if(currentDistance<-currentMaxDistance){
										currentDistance=-currentMaxDistance;
									}
									try {
										Thread.sleep(60);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									currentHeight=(int) (temp+currentDistance);
									if(currentHeight>0){
										currentHeight=0;
									}
									postInvalidate();
								}
								temp=currentHeight;
								//小球从最高位置（距离中心线-currentMaxDistance距离）下落，知道中心线位置
								while(currentDistance<0){
									currentDistance+=speed;
									if(currentDistance>0){
										currentDistance=0;
									}
									try {
										Thread.sleep(60);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									currentHeight=(int) (temp+currentDistance);
									if(currentHeight>0){
										currentHeight=0;
									}
									postInvalidate();
								}
								//小球抖动的最高点和最低点间的距离减小
								temp=currentHeight;
								currentMaxDistance-=speed;
								if(currentMaxDistance<0){
									currentMaxDistance=0;
								}
							}
							
						}
					}).start();
				}
			}
			
		}
		
		super.onSizeChanged(w, h, oldw, oldh);
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int width=getWidth();
		height=getHeight();
		if(image==null)return;
		canvas.drawBitmap(redLine, 0, currentHeight, null);
		Paint paint=new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);
		paint.setColor(getResources().getColor(R.color.orange_red));
		canvas.drawCircle(width/2,currentHeight+( height-(width/2)), width/2, paint);
		if(imageId==R.drawable.dog_star){
			canvas.drawBitmap(image,(width-image.getWidth())/2, currentHeight+( height-width+(width-image.getHeight())/2), null);
			clickTop=currentHeight+( height-width+(width-image.getHeight())/2);
			clickLeft=(width-image.getWidth())/2;
		}else{
			canvas.drawBitmap(image, (width-image.getWidth()),currentHeight+(  height-width+(width-image.getWidth())/2), null);
			clickTop=currentHeight+( height-width+(width-image.getWidth())/2);
			clickLeft=(width-image.getWidth());
		}
		
	}
	public void setImageId(int id,ChoseStarActivity activity){
		imageId=id;
		this.activity=activity;
		invalidate();
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			float x=event.getX();
			float y=event.getY();
			if(x>clickLeft&&x<clickLeft+image.getWidth()&&y>clickTop&&y<clickTop+image.getHeight()){
				int mode=2;
				if(imageId==R.drawable.cat_star){
					LogUtil.i("scroll", "选中猫星");
					mode=2;
				}else{
					LogUtil.i("scroll", "选中汪星");
					mode=1;
				}
//				Intent intent=new Intent(activity,ChoseAcountTypeActivity.class);
				Intent intent=new Intent(activity,FirstPageActivity.class);
				intent.putExtra("mode", mode);
				activity.startActivity(intent);
				activity.finish();
			}
			break;
		}
		return true;
	}
	
	

}
