package com.aidigame.hisun.pet.view;

import com.aidigame.hisun.pet.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CircleView extends ImageView {

	public CircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CircleView(Context context) {
		super(context);
		init();
	}

	private Bitmap bitmap;
	private Rect bitmapRect = new Rect();
	private PaintFlagsDrawFilter pdf = new PaintFlagsDrawFilter(0,
			Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	private Paint paint = new Paint();
	{
		paint.setStyle(Paint.Style.STROKE);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);// 反锯齿效果
	}
	private Bitmap mDstB = null;
	private PorterDuffXfermode xfermode = new PorterDuffXfermode(
			PorterDuff.Mode.MULTIPLY);

	private void init() {
		try {
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				setLayerType(LAYER_TYPE_SOFTWARE, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mDstB = makeDst(getMeasuredWidth(), getMeasuredHeight());
	}

	public void setImageBitmap(Bitmap path) {
		this.bitmap=path;
		invalidate();
	}

	private Bitmap makeDst(int w, int h) {
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.parseColor("#ffffffff"));
		c.drawOval(new RectF(0, 0, w, h), p);
		return bm;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=8;

		if (null == bitmap) {
			options.inSampleSize=1;
			bitmap = BitmapFactory
					.decodeResource(getResources(), R.drawable.pet_icon,options);
		}
		if (null == mDstB) {
			mDstB = makeDst(getMeasuredWidth(), getMeasuredHeight());
		}

		bitmapRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
		canvas.save();
		canvas.setDrawFilter(pdf);
		canvas.drawBitmap(mDstB, 0, 0, paint);
		paint.setXfermode(xfermode);
		if(bitmap!=null)
		canvas.drawBitmap(bitmap, null, bitmapRect, paint);
		paint.setXfermode(null);
		canvas.restore();
		
	}
}
