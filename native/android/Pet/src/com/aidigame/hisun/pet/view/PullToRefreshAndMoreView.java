package com.aidigame.hisun.pet.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.util.LogUtil;

public class PullToRefreshAndMoreView extends LinearLayout implements OnTouchListener{
	public static final int STATUS_PULL_DOWN_TO_REFRESH=0;//下拉可以进行刷新
	public static final int STATUS_PULL_DOWN_RELEASE_TO_REFRESH=1;//下拉    释放立即进行刷新
	public static final int STATUS_PULL_DOWN_REFRESHING=2;//下拉   正在进行刷新
	public static final int STATUS_PULL_DOWN_REFRESH_FINISHED=3;//下拉   刷新完毕
	public static final int STATUS_PULL_UP_TO_MORE=4;//上拉    进行加载
	public static final int STATUS_PULL_UP_RELEASE_TO_MORE=5;//上拉   释放立即进行加载
	public static final int STATUS_PULL_UP_LOADING_MORE=6;//上拉   正在进行加载
	public static final int STATUS_PULL_UP_LOADING_FINISHED=7;//上拉  加载完毕
	
	
	/*
	 * 使用的View控件
	 */
	private View header;
	private ImageView header_arrow;
	private ProgressBar header_progressBar;
	private TextView header_description;
	private TextView header_update_at;
	private MarginLayoutParams header_marLayoutParams;
	private View footer;
	private ImageView footer_arrow;
	private ProgressBar footer_progressBar;
	private TextView footer_description;
	private TextView footer_update_at;
	private MarginLayoutParams footer_marLayoutParams;
	private ListView listView;
	
	
	private int touchSlop;//判断是否是滑动的最小距离
	private int headerScrollSpeed=-25;//header 向上自动回滚速度
	private int footerScrollSpeed=-25;//footer 向下自动回滚速度
	private int hideHeaderHeight;//值为header的高度    marginTop的值 ，初始是个负值，  header在屏幕外，不可见
	private int hideFooterHeight;//值为footer的高度   marginBottom的值 ，初始是个负值，footer在屏幕外，不可见
	private long lastUpdateRefreshAtTime=-1;//
	private long lastUpdateMoreAtTime=-1;
	public int viewHeight;//整个PullToRefreshAndMoreView 的高度
	private float yDown,xDown;//手指下落处的y坐标̬
    private int footCurrentStatus=STATUS_PULL_UP_LOADING_FINISHED;//当前footer的状态
    private int currentStatus=STATUS_PULL_DOWN_REFRESH_FINISHED;//当前header的状态
	private boolean ableToPullRefresh=false;
	private boolean ableToPullMore=false;
    private PullToRefreshAndMoreListener listener;
    private boolean isLoadOnce=false;
    private int lastStatus;
    
    
    public static final long ONE_MINUTE=60*1000;//一分钟的毫秒值
    public static final long ONE_HOUR=60*ONE_MINUTE;//一小时的毫秒值
    public static final long ONE_DAY=24*ONE_HOUR;//一天的毫秒值
    public static final long ONE_MONTH=30*ONE_DAY;//一个月的毫秒值
    public static final long ONE_YEAR=12*ONE_MONTH;//一年的毫秒值
	public PullToRefreshAndMoreView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_and_more, this);
		this.setOrientation(LinearLayout.VERTICAL);
		initView();
	}



	private void initView() {
		// TODO Auto-generated method stub
		header=findViewById(R.id.header);
		footer=findViewById(R.id.footer);
		listView=(ListView)findViewById(R.id.listview);
		header_arrow=(ImageView)findViewById(R.id.header_arrow);
		footer_arrow=(ImageView)findViewById(R.id.footer_arrow);
		header_progressBar=(ProgressBar)findViewById(R.id.header_progressBar);
		footer_progressBar=(ProgressBar)findViewById(R.id.footer_progressBar);
		header_description=(TextView)findViewById(R.id.header_description);
		header_update_at=(TextView)findViewById(R.id.header_update_at);
		footer_description=(TextView)findViewById(R.id.footer_description);
		footer_update_at=(TextView)findViewById(R.id.footer_update_at);
		
		listView.setOnTouchListener(this);
		touchSlop=ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	/**
	 * 当PullToRefreshAndMoreView的界面发生改变时调用此方法，
	 * isLoadOnce保证只在界面刚创建完后进行初始化操作，之后界面发生变化时不再调用。
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if(!isLoadOnce){
			header_marLayoutParams=(MarginLayoutParams)header.getLayoutParams();
			hideHeaderHeight=-header.getHeight();
			
			header_marLayoutParams.topMargin=hideHeaderHeight;
			header.setLayoutParams(header_marLayoutParams);
//			header.layout(0, hideHeaderHeight, header.getWidth(), 0);
			footer_marLayoutParams=(MarginLayoutParams)footer.getLayoutParams();
			hideFooterHeight=-footer.getHeight();
			footer_marLayoutParams.bottomMargin=hideFooterHeight;
			footer.setLayoutParams(footer_marLayoutParams);
//			header.layout(0,getHeight(), header.getWidth(), getHeight()-hideFooterHeight);
			viewHeight=getHeight();
			isLoadOnce=true;
		}
		
	}
	MotionEvent lastMotionEvent;
	int distance=0;
	boolean hasChange=false;
	public boolean canPullRefresh=true;//是否需要活动刷新操作,默认为true
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(canPullRefresh){
			judgeIsAbleToPull();
		}
		
		boolean refresh=false;
		boolean more=false;
		
		if(ableToPullRefresh){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				yDown=event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				float move=event.getY();
				if(yDown==0){
					yDown=event.getY();
				}
				distance=(int)move-(int)yDown;
				if(Math.abs(distance)<touchSlop){
					refresh=false;
					break;
				}
				if(distance>0&&footCurrentStatus==STATUS_PULL_UP_LOADING_FINISHED){
					
					if(header_marLayoutParams.topMargin<0){
						currentStatus=STATUS_PULL_DOWN_TO_REFRESH;
						hasChange=true;
					}else{
						currentStatus=STATUS_PULL_DOWN_RELEASE_TO_REFRESH;
						hasChange=true;
					}
					int topMargin=distance/2+hideHeaderHeight;
					header_marLayoutParams.topMargin=topMargin;
					header.setLayoutParams(header_marLayoutParams);
					
					refresh=true;
				}
				break;

			case MotionEvent.ACTION_UP:
				if(distance!=0&&!hasChange){
                    if(distance>0&&footCurrentStatus==STATUS_PULL_UP_LOADING_FINISHED){
					
					if(header_marLayoutParams.topMargin<0){
						currentStatus=STATUS_PULL_DOWN_TO_REFRESH;
						hasChange=true;
					}else{
						currentStatus=STATUS_PULL_DOWN_RELEASE_TO_REFRESH;
						hasChange=true;
					}
					int topMargin=distance/2+hideHeaderHeight;
					header_marLayoutParams.topMargin=topMargin;
					header.setLayoutParams(header_marLayoutParams);
					
					refresh=true;
				    }
				}
                   
                    	 if(footCurrentStatus==STATUS_PULL_UP_LOADING_FINISHED){
         					if(header_marLayoutParams.topMargin>=-hideHeaderHeight){
         						currentStatus=STATUS_PULL_DOWN_RELEASE_TO_REFRESH;
         						hasChange=true;
         					}
         				}
                    
                   
				if((currentStatus==STATUS_PULL_DOWN_RELEASE_TO_REFRESH||currentStatus==STATUS_PULL_DOWN_TO_REFRESH)&&footCurrentStatus==STATUS_PULL_UP_LOADING_FINISHED){
					if(currentStatus==STATUS_PULL_DOWN_TO_REFRESH){
						/*
						 * 隐藏header，分两种情况
						 * 1.下拉刷新时，下拉高度不够，执行此操作
						 * 2.下拉刷新完毕，将header隐藏，执行此操作*/
						 
						new HideHeaderTask().execute();
					}else if(currentStatus==STATUS_PULL_DOWN_RELEASE_TO_REFRESH){
						
						/* * header回滚到PullToRefreshAndMoreView的最顶部
						 * 执行下拉动作时，下拉高度超过设定值（-hideHeaderHeight）,header向回滚动，执行此操作*/
						 
						new HeaderScrollToTopTask().execute();
						LogUtil.i("pull","count=");
					}
					listView.setPressed(false);
					listView.setFocusable(false);
					listView.setFocusableInTouchMode(false);
					listView.setEnabled(false);
					listView.setClickable(false);
					hasChange=false;
					distance=0;
//					refresh=true;
				}
				
				
				break;
			}
			if(currentStatus==STATUS_PULL_DOWN_TO_REFRESH||currentStatus==STATUS_PULL_DOWN_RELEASE_TO_REFRESH){
				
				updateHeaderView();
				lastStatus=currentStatus;
//				return refresh;
			}
			
			
			
		}
		if(ableToPullMore){
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				yDown=event.getY();
				distance=0;
				break;
			case MotionEvent.ACTION_MOVE:
				if(yDown==0){
					yDown=event.getY();
				}
				float move=event.getY();
				distance=(int)move-(int)yDown;
				if(Math.abs(distance)<touchSlop){
					more=false;
					break;
				}
				if(distance<0&&currentStatus==STATUS_PULL_DOWN_REFRESH_FINISHED){
					
					
						if(footer_marLayoutParams.bottomMargin<0){
							footCurrentStatus=STATUS_PULL_UP_TO_MORE;
							hasChange=true;
						}else{
							footCurrentStatus=STATUS_PULL_UP_RELEASE_TO_MORE;
							hasChange=true;
						}
						footer_marLayoutParams.bottomMargin=-distance/2+hideFooterHeight;
						footer.setLayoutParams(footer_marLayoutParams);
						more=true;
					
				}
				break;

			case MotionEvent.ACTION_UP:
				//上滑加载更多时，事件容易丢失，在做次判断
				if(distance!=0&&!hasChange){
					if(distance<0&&currentStatus==STATUS_PULL_DOWN_REFRESH_FINISHED){
						
						
						if(footer_marLayoutParams.bottomMargin<0){
							footCurrentStatus=STATUS_PULL_UP_TO_MORE;
							hasChange=true;
						}else{
							footCurrentStatus=STATUS_PULL_UP_RELEASE_TO_MORE;
							hasChange=true;
						}
						footer_marLayoutParams.bottomMargin=-distance/2+hideFooterHeight;
						footer.setLayoutParams(footer_marLayoutParams);
						more=true;
					
				}
				}
				
				//上滑加载更多时，事件容易丢失，在做次判断
				if(currentStatus==STATUS_PULL_DOWN_REFRESH_FINISHED){
					if(footer_marLayoutParams.bottomMargin>-hideFooterHeight){
						footCurrentStatus=STATUS_PULL_UP_RELEASE_TO_MORE;
						hasChange=true;
					}
				}
				
				if((footCurrentStatus==STATUS_PULL_UP_RELEASE_TO_MORE||footCurrentStatus==STATUS_PULL_UP_TO_MORE)&&currentStatus==STATUS_PULL_DOWN_REFRESH_FINISHED){
					if(footCurrentStatus==STATUS_PULL_UP_TO_MORE){
						
						/* * 隐藏footer，分两种情况
						 * 1.上拉加载时，上拉高度不够，执行此操作
						 * 2.上拉加载完毕，将footer隐藏，执行此操作*/
						 
						
						new HideFooterTask().execute();
					}else if(footCurrentStatus==STATUS_PULL_UP_RELEASE_TO_MORE){
						//消除footer和listview的空隙，让listview滚动到最底部
						
						listView.scrollTo((int)(header.getX()),(int)( header.getY()));;
						
						/*
						 * footer回滚到PullToRefreshAndMoreView的最底部
						 * 执行上拉动作时，上拉高度超过设定值（-hideFooterHeight）,footer向回滚动，执行此操作*/
						 
						new FooterScrollToBottomTask().execute();
					}
					listView.setPressed(false);
					listView.setFocusable(false);
					listView.setFocusableInTouchMode(false);
					listView.setEnabled(false);
					listView.setClickable(false);
//					more=true;
				}
				hasChange=false;
				distance=0;
				break;
			}
			 if(footCurrentStatus==STATUS_PULL_UP_RELEASE_TO_MORE||footCurrentStatus==STATUS_PULL_UP_TO_MORE){
					
	            	updateFooterView();
	            	lastStatus=currentStatus;
//	            	 return more;
				}
			
		}
		if(currentStatus==STATUS_PULL_DOWN_REFRESHING||footCurrentStatus==STATUS_PULL_UP_LOADING_MORE){
			listView.setEnabled(false);
			return true;
		}else{
			//当到达底部时，footer随着滑动向上移动，listView的内容也随着移动,当下拉刷新时，不让listview滚动
			if(currentStatus==STATUS_PULL_DOWN_TO_REFRESH||currentStatus==STATUS_PULL_DOWN_RELEASE_TO_REFRESH){
				
			}else{
//				this.onInterceptTouchEvent(event);
				//TODO
				listView.onTouchEvent(event);
			}
		}
		if(more)return more;
		if(refresh)return refresh;
		return false;
	}
	private void judgeIsAbleToPull() {
		// TODO Auto-generated method stub
		View firstChildView=listView.getChildAt(0);
		int h1=com.aidigame.hisun.pet.constant.Constants.screen_height;
		int h2=listView.getBottom();
		int h3=listView.getHeight();
		int h4=listView.getTop();
		LogUtil.i("pull", "Constants.screen_height="+h1+",bottom="+h2+",top="+h4+",height="+h3+",viewHeight="+viewHeight);
		if(footCurrentStatus!=STATUS_PULL_UP_LOADING_FINISHED||currentStatus!=STATUS_PULL_DOWN_REFRESH_FINISHED)return;
		if(firstChildView!=null){
			int top=firstChildView.getTop();
			int firstPosition=listView.getFirstVisiblePosition();
			int lastPosition=listView.getLastVisiblePosition();
			View lastChildView=listView.getChildAt(listView.getLastVisiblePosition()-firstPosition);
			if(lastChildView==null)return;
			int bottom=lastChildView.getBottom();
			if(firstPosition==0&&top==0){
				ableToPullRefresh=true;
			}else{
				ableToPullRefresh=false;
				header_marLayoutParams.topMargin=hideHeaderHeight;
				header.setLayoutParams(header_marLayoutParams);
			}
			if(lastPosition==listView.getCount()-1&&bottom<=listView.getBottom()){
					ableToPullMore=true;
			}else{
				ableToPullMore=false;
				footer_marLayoutParams.bottomMargin=hideFooterHeight;
				footer.setLayoutParams(footer_marLayoutParams);
			}
		}else{
			ableToPullRefresh=true;
			ableToPullMore=false;
		}
	}
	private void updateHeaderView(){
		Log.i("me", "updateHeaderView=========="+currentStatus);
		if(lastStatus==currentStatus)return;
		if(currentStatus==STATUS_PULL_DOWN_TO_REFRESH){
			header_description.setText("下拉可以刷新");
			header_arrow.setVisibility(View.VISIBLE);
			header_progressBar.setVisibility(View.GONE);
			changeHeaderArrowDirection();
			lastUpdateRefreshAtTime=refreshUpdatedAtTime(lastUpdateRefreshAtTime, header_update_at);
		}else if(currentStatus==STATUS_PULL_DOWN_RELEASE_TO_REFRESH){
			header_description.setText("释放立即刷新");
			header_arrow.setVisibility(View.VISIBLE);
			header_progressBar.setVisibility(View.GONE);
			changeHeaderArrowDirection();
			Log.i("me", "STATUS_PULL_DOWN_RELEASE_TO_REFRESH==========");
		}else if(currentStatus==STATUS_PULL_DOWN_REFRESHING){
			header_description.setText("正在刷新");
			header_arrow.setVisibility(View.GONE);
			header_progressBar.setVisibility(View.VISIBLE);
			listView.setPressed(false);
			listView.setFocusable(false);
			listView.setFocusableInTouchMode(false);
			listView.setEnabled(false);
			listView.setClickable(false);
		}
		lastStatus=currentStatus;
	}
	private void changeHeaderArrowDirection(){
		Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
		Matrix matrix=new Matrix();
		float degrees=0f;
		if(currentStatus==STATUS_PULL_DOWN_TO_REFRESH){
			degrees=0f;
		}else if(currentStatus==STATUS_PULL_DOWN_RELEASE_TO_REFRESH){
			degrees=180f;
		}
		matrix.postRotate(degrees, bmp.getWidth()/2, bmp.getHeight()/2);
		bmp=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
		header_arrow.setImageBitmap(bmp);
	}
	private void updateFooterView(){
		if(lastStatus==footCurrentStatus)return;
		if(footCurrentStatus==STATUS_PULL_UP_TO_MORE){
			footer_description.setText("上拉进行加载");
			footer_arrow.setVisibility(View.VISIBLE);
			footer_progressBar.setVisibility(View.GONE);
			changeFooterArrowDirection();
			lastUpdateMoreAtTime=refreshUpdatedAtTime(lastUpdateMoreAtTime, footer_update_at);
			
		}else if(footCurrentStatus==STATUS_PULL_UP_RELEASE_TO_MORE){
			footer_description.setText("释放立即加载");
			footer_arrow.setVisibility(View.VISIBLE);
			footer_progressBar.setVisibility(View.GONE);
			changeFooterArrowDirection();
		}else if(footCurrentStatus==STATUS_PULL_UP_LOADING_MORE){
			footer_description.setText("正在加载");
			footer_arrow.setVisibility(View.GONE);
			footer_progressBar.setVisibility(View.VISIBLE);
		}
		lastStatus=footCurrentStatus;
	}
	private void changeFooterArrowDirection(){
		Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
		Matrix matrix=new Matrix();
		float degrees=0f;
		if(footCurrentStatus==STATUS_PULL_UP_TO_MORE){
			degrees=180f;
		}else if(footCurrentStatus==STATUS_PULL_UP_RELEASE_TO_MORE){
			degrees=0f;
		}
		matrix.postRotate(degrees, bmp.getWidth()/2, bmp.getHeight()/2);
		bmp=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
		footer_arrow.setImageBitmap(bmp);
	}
	private long refreshUpdatedAtTime(long time,TextView tv) {
		// TODO Auto-generated method stub
		long currentTime=System.currentTimeMillis();
		long useTime=currentTime-time;
		long timeFormat=0;
		String updateString="";
		String strFormat="上次更新于%1$s前";
		String value="";
		if(time==-1){
			updateString="还未进行更新";
		}else if(useTime<0){
			updateString="时间出错";
		}else if(useTime<ONE_MINUTE){
			timeFormat=useTime/ONE_MINUTE;
			value=timeFormat+"分钟";
			updateString=String.format(strFormat, value);
		}else if(useTime<ONE_HOUR){
			timeFormat=useTime/ONE_HOUR;
			value=timeFormat+"小时";
			updateString=String.format(strFormat, value);
		}else if(useTime<ONE_DAY){
			timeFormat=useTime/ONE_DAY;
			value=timeFormat+"天";
			updateString=String.format(strFormat, value);
		}else if(useTime<ONE_MONTH){
			timeFormat=useTime/ONE_MONTH;
			value=timeFormat+"月";
			updateString=String.format(strFormat, value);
		}else if(useTime<ONE_YEAR){
			timeFormat=useTime/ONE_YEAR;
			value=timeFormat+"年";
			updateString=String.format(strFormat, value);
		}
		
		tv.setText(updateString);
		return currentTime;
	}


    public void sleep(long time){
    	try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public void setListener(PullToRefreshAndMoreListener l){
		listener=l;
	}
	public void onRefreshFinish(){
		
		if(currentStatus==STATUS_PULL_DOWN_REFRESH_FINISHED){
			onMoreFinish();
		}
		currentStatus=STATUS_PULL_DOWN_REFRESH_FINISHED;
		new HideHeaderTask().execute();
	}
	public void onMoreFinish(){
		footCurrentStatus=STATUS_PULL_UP_LOADING_FINISHED;
		new HideFooterTask().execute();
	}
	public void setHeaderAndFooterInvisible(){
		if(footer_marLayoutParams==null||header_marLayoutParams==null){
			return;
		}
		footer_marLayoutParams.bottomMargin=hideFooterHeight;
		footer.setLayoutParams(footer_marLayoutParams);
		header_marLayoutParams.topMargin=hideHeaderHeight;
		header.setLayoutParams(footer_marLayoutParams);
	}
	public ListView getListView(){
		return listView;
	}
	/**
	 * 在使用此自定义控件时，必须实现此接口，
	 * 在onRefresh方法中实现上拉刷新的数据更新，
	 * 在onMore方法中实现下拉加载的数据更新，
	 * @author scx
	 *
	 */
	public interface PullToRefreshAndMoreListener{
		void onRefresh();
		void onMore();
	}
	private class HideHeaderTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int topMargin=header_marLayoutParams.topMargin;
			while(true){
				if(topMargin<=hideHeaderHeight){
					topMargin=hideHeaderHeight;
					break;
				}
				topMargin+=headerScrollSpeed;
			    publishProgress(topMargin);
			    sleep(20);
			}
			return topMargin;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			header_marLayoutParams.topMargin=values[0];
			header.setLayoutParams(header_marLayoutParams);
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			header_marLayoutParams.topMargin=result;
			header.setLayoutParams(header_marLayoutParams);
			currentStatus=STATUS_PULL_DOWN_REFRESH_FINISHED;
//			listView.setPressed(true);
			listView.setFocusable(false);
			listView.setFocusableInTouchMode(false);
			listView.setEnabled(true);
			listView.setClickable(true);
			ableToPullRefresh=false;
		}
		
	}
    private class HeaderScrollToTopTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int topMargin=header_marLayoutParams.topMargin;
			while(true){
				if(topMargin<=0){
					topMargin=0;
					break;
				}
				topMargin+=headerScrollSpeed;
				publishProgress(topMargin);
				sleep(20);
			}
			publishProgress(topMargin);
			currentStatus=STATUS_PULL_DOWN_REFRESHING;
			if(listener!=null){
				listener.onRefresh();
			}
			return null;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			header_marLayoutParams.topMargin=values[0];
			header.setLayoutParams(header_marLayoutParams);
			updateHeaderView();
		}
    	
    }
	private class HideFooterTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int bottomMargin=footer_marLayoutParams.bottomMargin;
			while(true){
				if(bottomMargin<=hideFooterHeight){
					bottomMargin=hideHeaderHeight;
					break;
				}
				bottomMargin+=footerScrollSpeed;
			    publishProgress(bottomMargin);
			    sleep(20);
			}
			return bottomMargin;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			footer_marLayoutParams.bottomMargin=values[0];
			footer.setLayoutParams(footer_marLayoutParams);
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			footer_marLayoutParams.bottomMargin=result;
			footer.setLayoutParams(footer_marLayoutParams);
			footCurrentStatus=STATUS_PULL_UP_LOADING_FINISHED;
//			listView.setPressed(true);
			listView.setFocusable(true);
			listView.setFocusableInTouchMode(true);
			listView.setEnabled(true);
//			listView.setClickable(true);
			ableToPullMore=false;
		}
		
	}
    private class FooterScrollToBottomTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int bottomMargin=footer_marLayoutParams.bottomMargin;
			while(true){
				if(bottomMargin<=0){
					bottomMargin=0;
					break;
				}
				bottomMargin+=footerScrollSpeed;
				publishProgress(bottomMargin);
				sleep(20);
			}
			publishProgress(bottomMargin);
			footCurrentStatus=STATUS_PULL_UP_LOADING_MORE;
			
			if(listener!=null){
				listener.onMore();
			}
			return null;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			footer_marLayoutParams.bottomMargin=values[0];
			footer.setLayoutParams(footer_marLayoutParams);
			
			updateFooterView();
		}
    	
    }

}
