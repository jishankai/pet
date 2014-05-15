package com.example.picturedemo.util;
/**
 * 显示颜色亮度PopupWindow，并与用户进行界面交互。
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.picturedemo.R;

public class ColorHandle implements OnSeekBarChangeListener{
	Context context;
	ImageView imageView;
	PopupWindow popup;
	SeekBar saturationBar,hueBar,luminBar;
	int currentFlag=-1;//0:饱和度调节，1：色相调节，2：亮度调节
	Bitmap bitmap;
	float mSaturationValue=1f,mHueValue=1f,mLuminValue=1f;
	public ColorHandle(Context context,ImageView imageView){
		this.context=context;
		this.imageView=imageView;
		initPopupWindow();
		initSeekBarListener();
	}

	private void initPopupWindow() {
		// TODO Auto-generated method stub
		popup=new PopupWindow(context);
		popup.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popup.setFocusable(true);
		View view=LayoutInflater.from(context).inflate(R.layout.popup_color_handle, null);
		popup.setContentView(view);
		saturationBar=(SeekBar)view.findViewById(R.id.seekBar1);
		hueBar=(SeekBar)view.findViewById(R.id.seekBar2);
		luminBar=(SeekBar)view.findViewById(R.id.seekBar3);
		saturationBar.setMax(255);
		saturationBar.setProgress(127);
		hueBar.setMax(255);
		hueBar.setProgress(127);
		luminBar.setMax(255);
		luminBar.setProgress(127);
		popup.showAsDropDown(imageView);
		imageView.setDrawingCacheEnabled(true);
		bitmap=Bitmap.createBitmap(imageView.getDrawingCache());
		imageView.setDrawingCacheEnabled(false);
	}
	private void initSeekBarListener() {
		// TODO Auto-generated method stub
		saturationBar.setOnSeekBarChangeListener(this);
		saturationBar.setTag(1);
		hueBar.setOnSeekBarChangeListener(this);
		hueBar.setTag(2);
		luminBar.setOnSeekBarChangeListener(this);
		luminBar.setTag(3);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Log.i("me","process="+progress);
		switch (seekBar.getId()) {
		case R.id.seekBar1:
			currentFlag=0;
			mSaturationValue=progress*1f/127;
			break;
		case R.id.seekBar2:
			currentFlag=1;
			mHueValue=progress*1f/127;
			break;
		case R.id.seekBar3:
			currentFlag=2;
			mLuminValue=(progress-127)*1f/127*180;
			break;
		}
		ColorMatrix saturationMatrix=new ColorMatrix();
		saturationMatrix.setSaturation(mSaturationValue);
		ColorMatrix hueMatrix=new ColorMatrix();
		hueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1);
		ColorMatrix luminMatrix=new ColorMatrix();
		luminMatrix.setRotate(0, mLuminValue);
		luminMatrix.setRotate(1, mLuminValue);
		luminMatrix.setRotate(2, mLuminValue);
		ColorMatrix allMatrix=new ColorMatrix();
		allMatrix.postConcat(saturationMatrix);
		allMatrix.postConcat(hueMatrix);
		allMatrix.postConcat(luminMatrix);
		Bitmap temp=Bitmap.createBitmap(bitmap);
		Canvas canvas=new Canvas(temp);
		Paint paint=new Paint();
		paint.setAntiAlias(true);
		paint.setColorFilter(new ColorMatrixColorFilter(allMatrix));
		
		canvas.drawBitmap(temp,0,0,paint);
		imageView.setImageBitmap(temp);
		temp=null;
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	public void dismiss(){
		popup.dismiss();
	}
}

