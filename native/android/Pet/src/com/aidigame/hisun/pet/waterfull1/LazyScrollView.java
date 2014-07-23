package com.aidigame.hisun.pet.waterfull1;

import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ScrollView;

public class LazyScrollView extends ScrollView {
	HomeActivity homeActivity;
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		onScrollListener.onAutoScroll(l, t, oldl, oldt);
	}
    public void onChanged(int l,int t,int oldl,int oldt){
    	if (view != null && onScrollListener != null) {
			handler.sendMessageDelayed(handler.obtainMessage(1), 200);
		}
//    	onScrollChanged(l, t, oldl, oldt);
    }
    
	private static final String tag = "LazyScrollView";
	private Handler handler;
	private View view;

	public LazyScrollView(Context context) {
		super(context);

	}

	public LazyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public LazyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	// 这个获得总的高度
	public int computeVerticalScrollRange() {
		return super.computeHorizontalScrollRange();
	}

	public int computeVerticalScrollOffset() {
		return super.computeVerticalScrollOffset();
	}
    public void setHomeActivityHandler(HomeActivity  homeActivity){
    	this.homeActivity=homeActivity;
    }
    boolean hasShow=false;
	private void init() {

		this.setOnTouchListener(onTouchListener);
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// process incoming messages here
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					
					if (view.getMeasuredHeight() - 20 <= getScrollY()
							+ getHeight()) {
						if (onScrollListener != null) {
							onScrollListener.onBottom();
						}

					} else if (getScrollY() == 0) {
						if (onScrollListener != null) {
							onScrollListener.onTop();
						}
					} else {
						if (onScrollListener != null) {
							onScrollListener.onScroll();
						}
						/*if(homeActivity!=null){
							Bitmap bmp=ImageUtil.getBitmapFromView(homeActivity.homeRelativeLayout,homeActivity);
							homeActivity.relativeLayout_control1.setoriginalImage(bmp);
							homeActivity.relativeLayout_control1.handleScroll(homeActivity.relativeLayout_control1.getHeight()/2);
						}*/
							
						
					}
					break;
				default:
					break;
				}
			}
		};

	}

	OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
            boolean flag=false;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
//				flag=true;
				break;
			case MotionEvent.ACTION_MOVE:
				
				
				break;
			case MotionEvent.ACTION_UP:
				if (view != null && onScrollListener != null) {
					handler.sendMessageDelayed(handler.obtainMessage(1), 200);
				}
				break;

			default:
				break;
			}
			return flag;
//			return detector.onTouchEvent(event);
		}

	};

	/**
	 * 获得参考的View，主要是为了获得它的MeasuredHeight，然后和滚动条的ScrollY+getHeight作比较。
	 */
	public void getView() {
		this.view = getChildAt(0);
		if (view != null) {
			init();
		}
	}

	/**
	 * 定义接口
	 * 
	 * @author admin
	 * 
	 */
	public interface OnScrollListener {
		void onBottom();

		void onTop();

		void onScroll();

		void onAutoScroll(int l, int t, int oldl, int oldt);
	}

	private OnScrollListener onScrollListener;

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}
/*	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean flag=super.dispatchTouchEvent(ev);
		
		LogUtil.i("me", "Lasyscrollow:::dispatchTouchEvent");
		detector.onTouchEvent(ev);
		if(issingleTab){
			issingleTab=false;
			return false;
		}
		return super.dispatchTouchEvent(ev);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		LogUtil.i("me", "Lasyscrollow:::onInterceptTouchEvent");
		if(issingleTab){
			return false;
			
		}
		return super.onInterceptTouchEvent(ev);
	}*/
	boolean isMove=false;
	public static boolean  issingleTab=false;
GestureDetector detector=new GestureDetector(new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			issingleTab=true;
			LogUtil.i("scroll","onSingleTapUp=");
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			issingleTab=true;
			LogUtil.i("scroll","onShowPress=");
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll","distanceY="+ distanceY+";;issingleTab="+issingleTab);
			issingleTab=false;
			return true;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			issingleTab=true;
			LogUtil.i("scroll","onLongPress=");
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			issingleTab=false;
			LogUtil.i("scroll","velocityY="+ velocityY+";;issingleTab="+issingleTab);
			return true;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll","onDown=");
			hasShow=false;
			return true;
		}
	});
}
