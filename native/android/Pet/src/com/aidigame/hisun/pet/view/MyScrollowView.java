package com.aidigame.hisun.pet.view;

import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.waterfull1.LazyScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollowView extends ScrollView{
    LazyScrollView.OnScrollListener listener;
	public MyScrollowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void setLasyView(LazyScrollView.OnScrollListener view){
		this.listener=view;
	}
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if(listener!=null)listener.onAutoScroll(l, t, oldl, oldt);
		
	}
	public MyScrollowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public MyScrollowView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		LogUtil.i("exception","getTop="+getTop()+";getBottom"+getBottom()+"ev.getY="+ev.getY());
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(ev);
	}

}
