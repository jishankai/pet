package com.aidigame.hisun.pet.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class StaticViewPager extends ViewPager {
    boolean canScroll=false;
	public StaticViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void setCanScroll(boolean canScroll){
		this.canScroll=canScroll;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
//		return super.onInterceptTouchEvent(arg0);
		return canScroll;
	}
	

}
