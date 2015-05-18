package com.aidigame.hisun.imengstar.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class BlurImageBroadcastReceiver extends BroadcastReceiver {
	Context context;
	View view;
	public static final String BLUR_BITMAP_CHANGED="BLUR_BITMAP_CHANGED";
    public BlurImageBroadcastReceiver(View view,Context context){
    	this.context=context;
    	this.view=view;
    }
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bitmap bmp=(Bitmap)intent.getParcelableExtra("blur");
		view.setBackgroundDrawable(new BitmapDrawable(bmp));
		view.setAlpha(0.9342857f);

	}

}
