package com.aidigame.hisun.pet.view;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {

	/**
	 * Graphics Camera used for transforming the matrix of ImageViews
	 */
	private Camera mCamera = new Camera();
    Matrix mMatrix=new Matrix();
	/**
	 * The maximum angle the Child ImageView will be rotated by
	 */
	private int mMaxRotationAngle = 30;//50

	/**
	 * The maximum zoom on the centre Child
	 */
	private int mMaxZoom = -360;//-250  -480 -360

	/**
	 * The Centre of the Coverflow
	 */
	private int mCoveflowCenter;
	Transformation t;

	public GalleryFlow(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
		mMaxZoom=-context.getResources().getDimensionPixelSize(R.dimen.dip_360);
		LogUtil.i("me", "-context.getResources().getDimensionPixelSize(R.dimen.dip_360)="+(-context.getResources().getDimensionPixelSize(R.dimen.dip_360)));
	}

	public GalleryFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);		mMaxZoom=-context.getResources().getDimensionPixelSize(R.dimen.dip_360);
		LogUtil.i("me", "-context.getResources().getDimensionPixelSize(R.dimen.dip_360)="+(-context.getResources().getDimensionPixelSize(R.dimen.dip_360)));
	}

	public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
		mMaxZoom=-context.getResources().getDimensionPixelSize(R.dimen.dip_360);
		LogUtil.i("me", "-context.getResources().getDimensionPixelSize(R.dimen.dip_360)="+(-context.getResources().getDimensionPixelSize(R.dimen.dip_360)));
		
	}

	/**
	 * Get the max rotational angle of the image
	 * 
	 * @return the mMaxRotationAngle
	 */
	public int getMaxRotationAngle() {
		return mMaxRotationAngle;
	}

	/**
	 * Set the max rotational angle of each image
	 * 
	 * @param maxRotationAngle
	 *            the mMaxRotationAngle to set
	 */
	public void setMaxRotationAngle(int maxRotationAngle) {
		mMaxRotationAngle = maxRotationAngle;
	}

	/**
	 * Get the Max zoom of the centre image
	 * 
	 * @return the mMaxZoom
	 */
	public int getMaxZoom() {
		return mMaxZoom;
	}

	/**
	 * Set the max zoom of the centre image
	 * 
	 * @param maxZoom
	 *            the mMaxZoom to set
	 */
	public void setMaxZoom(int maxZoom) {
		mMaxZoom = maxZoom;
	}

	/**
	 * Get the Centre of the Coverflow
	 * 
	 * @return The centre of this Coverflow.
	 */
	private int getCenterOfCoverflow() {
		//Log.e("CoverFlow Width+Height", getWidth() + "*" + getHeight());
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	/**
	 * Get the Centre of the View
	 * 
	 * @return The centre of the given view.
	 */
	private static int getCenterOfView(View view) {
		/*Log.e("ChildView Width+Height", view.getWidth() + "*"
				+ view.getHeight());*/
		return view.getLeft() + view.getWidth() / 2;
	}
	/*@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		// TODO Auto-generated method stub
		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth();
		int rotationAngle = 0;
		if(t!=null){
			t.clear();
			t.setTransformationType(Transformation.TYPE_MATRIX);
	        int w=Constants.screen_width;
			if (childCenter == mCoveflowCenter) {
				transformImageBitmap((ImageView) child, t, 0);
			} else {
				rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
				if (Math.abs(rotationAngle) > mMaxRotationAngle) {
					rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle: mMaxRotationAngle;
				}
				transformImageBitmap((ImageView) child, t, rotationAngle);
			}
		}
		

		return super.drawChild(canvas, child, drawingTime);
	}*/
   
	/**
	 * {@inheritDoc}
	 * 
	 * @see #setStaticTransformationsEnabled(boolean)
	 */
	protected boolean getChildStaticTransformation(View child, Transformation t) {
//        this.t=t;
		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth();
		int rotationAngle = 0;

		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
        int w=Constants.screen_width;
		if (childCenter == mCoveflowCenter) {
//				child.setTag(0);
			
//				child.setBackgroundColor(Color.WHITE);
			transformImageBitmap((ImageView) child, t, 0);
		} else {
			rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
			if (Math.abs(rotationAngle) > mMaxRotationAngle) {
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle: mMaxRotationAngle;
			}
//			child.setTag(rotationAngle);
//			child.setBackgroundDrawable(null);;
			transformImageBitmap((ImageView) child, t, rotationAngle);
		}

		return true;
	}

	/**
	 * This is called during layout when the size of this view has changed. If
	 * you were just added to the view hierarchy, you're called with the old
	 * values of 0.
	 * 
	 * @param w
	 *            Current width of this view.
	 * @param h
	 *            Current height of this view.
	 * @param oldw
	 *            Old width of this view.
	 * @param oldh
	 *            Old height of this view.
	 */
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * Transform the Image Bitmap by the Angle passed
	 * 
	 * @param imageView
	 *            ImageView the ImageView whose bitmap we want to rotate
	 * @param t
	 *            transformation
	 * @param rotationAngle
	 *            the Angle by which to rotate the Bitmap
	 */
	private void transformImageBitmap(ImageView child, Transformation t,
			int rotationAngle) {
		
		mCamera.save();
		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getLayoutParams().height;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);

		
		mCamera.translate(0.0f, 0.0f, 100.0f);

		// As the angle of the view gets less, zoom in
		if (rotation < mMaxRotationAngle) {
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
			/*
			 * scx 取消透明度效果变化
			 */
//			((ImageView) (child)).setAlpha((int) (255 - rotation * 2.5));
		}

		
		mCamera.rotateY(rotationAngle);
//		mCamera.getMatrix(imageMatrix);
		mCamera.getMatrix(mMatrix);
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		
		mCamera.restore();
		
	}
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
    		float velocityY) {
    	// TODO Auto-generated method stub
    	return super.onFling(e1, e2, velocityX, velocityY);
//    	return false;
    }
	@Override  
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {  
	    // TODO Auto-generated method stub  
		 boolean ret;  
		    //Android SDK 4.1  
		    if(android.os.Build.VERSION.SDK_INT >=15){  
		        final float offset = calculateOffsetOfCenter(child);  
		        getTransformationMatrix(child, offset);  
		          
//		        child.setAlpha(1 - Math.abs(offset));  
		          
		        final int saveCount = canvas.save();  
		        canvas.concat(mMatrix);  
		        ret = super.drawChild(canvas, child, drawingTime);  
		        canvas.restoreToCount(saveCount);  
		    }else{  
		        ret = super.drawChild(canvas, child, drawingTime);  
		    }  
		    return ret;  
	}  
	protected float calculateOffsetOfCenter(View view){  
	 
		final int childCenter = getCenterOfView(view);
		final int childWidth = view.getWidth();
		int rotationAngle = 0;
		if (childCenter == mCoveflowCenter) {
			rotationAngle = 0;
		} else {
			rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
			if (Math.abs(rotationAngle) > mMaxRotationAngle) {
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle: mMaxRotationAngle;
			}
		}
		
		
	  return rotationAngle;
//	    return offset;  
	} 
	public void getTransformationMatrix(View child, float offset) {  
		
		
		mCamera.save();
		final int imageHeight = child.getMeasuredHeight() >> 1;
		final int imageWidth = child.getLeft() + (child.getMeasuredWidth() >> 1);
		final int rotation = Math.abs((int)offset);

		
		mCamera.translate(0.0f, 0.0f, 100.0f);

		// As the angle of the view gets less, zoom in
		if (rotation < mMaxRotationAngle) {
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
//			((ImageView) (child)).setAlpha((int) (255 - rotation * 2.5));
		}

		mCamera.rotateY(offset);
		mCamera.getMatrix(mMatrix);
	
		mCamera.restore();
		mMatrix.preTranslate(-(imageWidth ), -(imageHeight ));
		mMatrix.postTranslate((imageWidth ), (imageHeight ));
		
	}  
}
