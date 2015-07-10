package com.aidigame.hisun.imengstar.view;


import com.aidigame.hisun.imengstar.blur.Blur;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class EraserView_user_drawPath extends View {
	private int SCREEN_W;

    private int SCREEN_H;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private Path  mPath;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    OnEraserOverListener onEraserOverListener;

	public EraserView_user_drawPath(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setFocusable(true);
        
     
        
	}
	public void setBitmap(Bitmap bmp,int w,int h){
		if(bmp==null){
			bmp=BitmapFactory.decodeResource(getResources(), R.drawable.pet_icon);
		}
		setScreenWH();
		float scalX=w/(bmp.getWidth()*1f);
		float scalY=h/(bmp.getHeight()*1f);
		float scale=scalX<scalY?scalY:scalX;
		Matrix matrix=new Matrix();
		matrix.postScale(scalX, scalX);
		bmp=Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),matrix,true);
	    int bmpH=0;
	    if(h>=bmp.getHeight()){
	    	bmpH=bmp.getHeight();
	    }else{
	    	bmpH=h;
	    }
		bmp=Bitmap.createBitmap(bmp, 0, (bmp.getHeight()-bmpH)/2, bmp.getWidth(), bmpH);
        setBackGround(bmp);
		// 1.if cover is a image,you can open MENU_ITEM_COMMENT bellow
        //Bitmap bm = createBitmapFromSRC();
        // if you want to set cover image's alpha,you can open MENU_ITEM_COMMENT bellow
        //bm = setBitmapAlpha(bm, 100);
        // if you want to scale cover image,you can open MENU_ITEM_COMMENT bellow
        //bm = scaleBitmapFillScreen(bm);

        // 2.if cover is color
//        Bitmap bm = createBitmapFromARGB(0x8800ff00, SCREEN_W, SCREEN_H);
		
      Bitmap  bm=Blur.fastblur(getContext(), bmp, 50);
      SCREEN_H=h;
      SCREEN_W=w;
//        bm = scaleBitmapFillScreen(bm);
      
        setCoverBitmap(bm,bmpH);
        invalidate();
	}
	  private void setScreenWH() {
          // get screen info
          DisplayMetrics dm = new DisplayMetrics();
          dm = this.getResources().getDisplayMetrics();
          // get screen width
          int screenWidth = dm.widthPixels;
          // get screen height
          int screenHeight = dm.heightPixels;

          SCREEN_W = screenWidth;
          SCREEN_H = screenHeight;
      }

      private Bitmap createBitmapFromSRC() {
          return BitmapFactory.decodeResource(getResources(),
                                              R.drawable.pet_icon);
      }

      /**
       * 
       * @param colorARGB should like 0x8800ff00
       * @param width
       * @param height
       * @return
       */
      private Bitmap createBitmapFromARGB(int colorARGB, int width, int height) {
          int[] argb = new int[width * height];

          for (int i = 0; i < argb.length; i++) {

              argb[i] = colorARGB;

          }
          return Bitmap.createBitmap(argb, width, height, Config.ARGB_8888);
      }

      /**
       * 
       * @param bm
       * @param alpha ,and alpha should be like ox00000000-oxff000000
       * @note set bitmap's alpha
       * @return
       */
     /* private Bitmap setBitmapAlpha(Bitmap bm, int alpha) {
          int[] argb = new int[bm.getWidth() * bm.getHeight()];
          bm.getPixels(argb, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm
                  .getHeight());


          for (int i = 0; i < argb.length; i++) {

              argb[i] = ((alpha) | (argb[i] & 0x00FFFFFF));
          }
          return Bitmap.createBitmap(argb, bm.getWidth(), bm.getHeight(),
                                     Config.ARGB_8888);
      }*/
      
      /**
       * 
       * @param bm
       * @param alpha ,alpha should be between 0 and 255
       * @note set bitmap's alpha
       * @return
       */
      private Bitmap setBitmapAlpha(Bitmap bm, int alpha) {
          int[] argb = new int[bm.getWidth() * bm.getHeight()];
          bm.getPixels(argb, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm
                  .getHeight());

          for (int i = 0; i < argb.length; i++) {

              argb[i] = ((alpha << 24) | (argb[i] & 0x00FFFFFF));
          }
          return Bitmap.createBitmap(argb, bm.getWidth(), bm.getHeight(),
                                     Config.ARGB_8888);
      }
      
      /**
       * 
       * @param bm
       * @note if bitmap is smaller than screen, you can scale it fill the screen.
       * @return 
       */
      private Bitmap scaleBitmapFillScreen(Bitmap bm) {
          return Bitmap.createScaledBitmap(bm, SCREEN_W, SCREEN_H, true);
      }

      
      private void setBackGround(Bitmap bmp) {
          setBackgroundDrawable(new BitmapDrawable(bmp));
      }

      /**
       * 
       * @param bm
       * @note set cover bitmap , which  overlay on background. 
       */
      private void setCoverBitmap(Bitmap bm,int h) {
          // setting paint
          mPaint = new Paint();
          mPaint.setAlpha(0);
          mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
          mPaint.setAntiAlias(true);
          
          mPaint.setDither(true);
          mPaint.setStyle(Paint.Style.STROKE);
          mPaint.setStrokeJoin(Paint.Join.ROUND);
          mPaint.setStrokeCap(Paint.Cap.ROUND);
          mPaint.setStrokeWidth(160);
          
          //set path
          mPath =  new Path();

          // converting bitmap into mutable bitmap
          mBitmap = Bitmap.createBitmap(SCREEN_W, h, Config.ARGB_8888);
          mCanvas = new Canvas();
          mCanvas.setBitmap(mBitmap);
          // drawXY will result on that Bitmap
          // be sure parameter is bm, not mBitmap
          mCanvas.drawBitmap(bm, 0, 0, null);
      }

     

      @Override
      protected void onDraw(Canvas canvas) {
    	  if(mBitmap!=null&&mBitmap.getHeight()>0){
    		  canvas.drawBitmap(mBitmap, 0, 0, null);
    		  mCanvas.drawPath(mPath, mPaint);
    	  }
          
         
          super.onDraw(canvas);
      }
      
      private void touch_start(float x, float y) {
    	  if(mPath!=null){
    		  mPath.reset();
              
              mPath.moveTo(x, y);
              mX = x;
              mY = y;
    	  }
         
      }
      private void touch_move(float x, float y) {
    	  if(mPath!=null){
              float dx = Math.abs(x - mX);
              float dy = Math.abs(y - mY);
              if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                  mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                  mX = x;
                  mY = y;
              }
    	  }

      }
      private void touch_up() {
          mPath.lineTo(mX, mY);
          // commit the path to our offscreen
          mCanvas.drawPath(mPath, mPaint);
          // kill this so we don't double draw
          mPath.reset();
          //判断空白区域的面积
          int[] pixels=new int[mBitmap.getWidth()*mBitmap.getHeight()];
          mBitmap.getPixels(pixels, 0, mBitmap.getWidth(), 0, 0, mBitmap.getWidth(), mBitmap.getHeight());
         
          int colorCount=0;
          for(int i=0;i<pixels.length;i++){
        	  if(mPaint.getColor()==pixels[i])colorCount++;
          }
         
          LogUtil.i("scroll", "摸一摸Paint颜色相同的点："+colorCount+",总数目："+getWidth()*getHeight());
          if(colorCount/(pixels.length*1f)>=0.8f){
        	  LogUtil.i("scroll", "摸一摸Paint颜色相同的点："+colorCount);
        	  onEraserOverListener.onEraserOver();
        	  
          }
          
      }
      
      @Override
      public boolean onTouchEvent(MotionEvent event) {
          float x = event.getX();
          float y = event.getY();
          
          switch (event.getAction()) {
              case MotionEvent.ACTION_DOWN:
                  touch_start(x, y);
                  invalidate();
                  break;
              case MotionEvent.ACTION_MOVE:
                  touch_move(x, y);
                  invalidate();
                  break;
              case MotionEvent.ACTION_UP:
                  touch_up();
                  invalidate();
                  break;
          }
          return true;
      }
      public void setOnEraserOverListener(OnEraserOverListener listener){
    	  onEraserOverListener=listener;
      }
      public static interface OnEraserOverListener{
    	  void onEraserOver();
      }
      public void recyleBmp(){
    	  if(mBitmap!=null){
    		  if(!mBitmap.isRecycled())
    		  mBitmap.recycle();
    		  mBitmap=null;
    	  }
    	  setBackgroundDrawable(null);
      }
  }
	


