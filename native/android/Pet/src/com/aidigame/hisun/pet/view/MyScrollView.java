package com.aidigame.hisun.pet.view;

import com.aidigame.hisun.pet.ui.NewShowTopicActivity.MyGestureDector;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;
/**
 * ���͵�ַ:http://blog.csdn.net/xiaanming
 * 
 * @author xiaanming
 *
 */
public class MyScrollView extends ScrollView {
	private OnScrollListener onScrollListener;
	GestureDetector gestrue;
	/**
	 * ��Ҫ�������û���ָ�뿪MyScrollView��MyScrollView���ڼ���������������������Y�ľ��룬Ȼ�����Ƚ�
	 */
	private int lastScrollY;
	
	public MyScrollView(Context context) {
		this(context, null);
	}
	
	public MyScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * ���ù����ӿ�
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}


	/**
	 * �����û���ָ�뿪MyScrollView��ʱ���ȡMyScrollView������Y���룬Ȼ��ص���onScroll������
	 */
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int scrollY = MyScrollView.this.getScrollY();
			
			//��ʱ�ľ���ͼ�¼�µľ��벻��ȣ��ڸ�5�����handler������Ϣ
			if(lastScrollY != scrollY){
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 5);  
			}
			if(onScrollListener != null){
				onScrollListener.onScroll(scrollY);
			}
			
		};

	}; 

	/**
	 * ��дonTouchEvent�� ���û�������MyScrollView�����ʱ��
	 * ֱ�ӽ�MyScrollView������Y�������ص���onScroll�����У����û�̧���ֵ�ʱ��
	 * MyScrollView���ܻ��ڻ��������Ե��û�̧�������Ǹ�5�����handler������Ϣ����handler����
	 * MyScrollView�����ľ���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(onScrollListener != null){
			onScrollListener.onScroll(lastScrollY = this.getScrollY());
		}
		switch(ev.getAction()){
		case MotionEvent.ACTION_UP:
	         handler.sendMessageDelayed(handler.obtainMessage(), 5);  
			break;
		}
		return super.onTouchEvent(ev);
	}
	public void setGestureDector(GestureDetector dector){
		this.gestrue=dector;
	}
  /*  @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	// TODO Auto-generated method stub
//    	return super.onInterceptTouchEvent(ev);
    	return false;
    }*/

	/**
	 * 
	 * �����Ļص��ӿ�
	 * 
	 * @author xiaanming
	 *
	 */
	public interface OnScrollListener{
		/**
		 * �ص������� ����MyScrollView������Y�������
		 * @param scrollY
		 * 				��
		 */
		public void onScroll(int scrollY);
	}
	
	

}
