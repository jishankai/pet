package com.aidigame.hisun.pet.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.R.color;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class TopicView extends ImageView{
    String path;
    int screen_width;
    int screen_height;
	public TopicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setScaleType(ScaleType.CENTER_CROP);
	}


	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		/*int width=bm.getWidth();
		int height=bm.getHeight();
		Matrix matrix=new Matrix();
		matrix.postScale(Constants.screen_width/(width*1f), Constants.screen_width/(width*1f));
		int scaleHeight=(int)((Constants.screen_width/width*1f)*height);
		if(scaleHeight>Constants.screen_height/3){
			bm=Bitmap.createBitmap(bm, 0,0,width,height,matrix,true);
			//TODO 截取高度的中间部分，高度为屏幕的1/3
//			bm=Bitmap.createBitmap(bm, 0, (scaleHeight-Constants.screen_height/3)/2, bm.getWidth(), Constants.screen_height/3);
		}else{
			bm=Bitmap.createBitmap(bm,0,0,width,height,matrix,true);
		}*/
		BitmapDrawable drawable=new BitmapDrawable(bm);
		int height=Constants.screen_width/drawable.getMinimumWidth()*drawable.getMinimumHeight();
		LayoutParams lp=getLayoutParams();
		if(lp==null){
		   lp=new LayoutParams(Constants.screen_width,height);	
		}
		lp.height=height;
		lp.width=Constants.screen_width;
		setLayoutParams(lp);
		
		super.setImageBitmap(bm);
	}
}
