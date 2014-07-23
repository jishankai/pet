package com.aidigame.hisun.pet.waterfull1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.ShowTopicActivity;
import com.aidigame.hisun.pet.ui.UnregisterNoteActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.widget.ShowWaterFull1;

public class FlowView extends ImageView implements View.OnClickListener,
		View.OnLongClickListener {
    boolean isRecord=false;
    boolean isMove=false;
	private AnimationDrawable loadingAnimation;
	private FlowTag flowTag;
    private LazyScrollView lazyScrollView;
	private Activity context;
	private HomeActivity homeActivity;
	ShowWaterFull1 showWaterFull1;
	public Bitmap bitmap;
	private ImageLoaderTask task;
	private int columnIndex;// 图片属于第几列
	private int rowIndex;// 图片属于第几行
	private Handler viewHandler;
	public Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			Intent intent=new Intent(context,ShowTopicActivity.class);
			if(homeActivity!=null){
				homeActivity.handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
				intent.putExtra("from","waterfull");
			}else {
				intent.putExtra("from", "ScanActivity");
			}
			ShowTopicActivity.datas=showWaterFull1.datas;
			intent.putExtra("data",flowTag.getData());
			context.startActivity(intent);
//			context.finish();
		};
	};
	public FlowView(Context c, AttributeSet attrs, int defStyle) {
		super(c, attrs, defStyle);
		this.context =(Activity) c;
		Init();
	}

	public FlowView(Context c, AttributeSet attrs) {
		super(c, attrs);
		this.context =(Activity) c;
		Init();
	}
	

	public FlowView(Context c) {
		super(c);
		this.context = (Activity)c;
		Init();
	}
    
	private void Init() {
        
//		setOnClickListener(this);
//		this.setOnLongClickListener(this);
		setAdjustViewBounds(true);

	}
	public void setLasyScrollowView(LazyScrollView view){
		this.lazyScrollView=view;
	}
	int downX,downY;
	int upX,upY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
					
//		
		
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean onLongClick(View v) {
		/*Log.d("FlowView", "LongClick");
		Toast.makeText(context, "长按：" + this.flowTag.getFlowId(),
				Toast.LENGTH_SHORT).show();*/
		return false;
	}

	@Override
	public void onClick(View v) {
		Log.d("FlowView", "Click");
		
	}

	/**
	 * 加载图片
	 */
	public void LoadImage() {
		if (getFlowTag() != null) {
			new LoadImageThread().start();
		}
	}

	/**
	 * 重新加载图片
	 */
	public void Reload() {
		if (this.bitmap == null && getFlowTag() != null) {

			new ReloadImageThread().start();
		}
	}

	/**
	 * 回收内存
	 */
	public void recycle() {
		setImageBitmap(null);
		if ((this.bitmap == null) || (this.bitmap.isRecycled()))
			return;
		this.bitmap.recycle();
		this.bitmap = null;
	}

	public FlowTag getFlowTag() {
		return flowTag;
	}

	public void setFlowTag(FlowTag flowTag) {
		this.flowTag = flowTag;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Handler getViewHandler() {
		return viewHandler;
	}

	public FlowView setViewHandler(Handler viewHandler) {
		this.viewHandler = viewHandler;
		return this;
	}

	class ReloadImageThread extends Thread {

		@Override
		public void run() {
			if (flowTag != null) {
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize=4;
				options.inPreferredConfig=Bitmap.Config.RGB_565;
				options.inPurgeable=true;
				options.inInputShareable=true;
//				options.inJustDecodeBounds=true;
				Bitmap bitmap1 = BitmapFactory.decodeFile(flowTag.getData().path,options);
				int width = bitmap1.getWidth();// 
				int height = bitmap1.getHeight();
                
				
				int layoutHeight=0;
				 layoutHeight = (height * flowTag.getItemWidth())
							/ width;// �����߶�
				Matrix matrix=new Matrix();
				float size=flowTag.getItemWidth()/(bitmap1.getWidth()*1f);
				matrix.postScale(size,size);
				LogUtil.i("me", "width==="+width+",height===="+height+",size==="+size);
				if(bitmap1==null)return;
				Bitmap temp=Bitmap.createBitmap(bitmap1,0,0,width,height, matrix, true);
				//图片回收，在有些版本上报异常，此处注释掉，有机会查找原因
				/*if(!bitmap1.isRecycled()){
					bitmap1.recycle();
				}*/
				if(temp.getHeight()*1f/temp.getWidth()>3*1f/2){
                	layoutHeight=(int)(3*1f/2)*temp.getWidth();
                }else{
                	layoutHeight = temp.getHeight();
                }
				bitmap=Bitmap.createBitmap(temp, 0, (temp.getHeight()-layoutHeight)/2, temp.getWidth(), layoutHeight);
				//图片回收，在有些版本上报异常，此处注释掉，有机会查找原因
				/*if(!temp.isRecycled()){
				 temp.recycle();
			 }*/
				if(flowTag.halfHeight){
                	
//					bitmap=Bitmap.createBitmap(bitmap, 0,height/6, width, height*2/3);
                }else{
                }

				(context).runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap != null) {// 此处在线程过多时可能为null
							LayoutParams lp = getLayoutParams();
							if (lp == null) {
								lp = new LayoutParams(flowTag.getItemWidth(),
										bitmap.getHeight());
							}
							setLayoutParams(lp);
							setImageBitmap(bitmap);
						}
					}
				});
			}

		}
	}

	class LoadImageThread extends Thread {
		LoadImageThread() {
		}

		public void run() {

			if (flowTag != null) {
				// if (bitmap != null) {

				// 此处不能直接更新UI，否则会发生异常：
				// CalledFromWrongThreadException: Only the original thread that
				// created a view hierarchy can touch its views.
				// 也可以使用Handler或者Looper发送Message解决这个问题
				if (flowTag != null) {
					if(!StringUtil.judgeImageExists(Constants.Picture_Topic_Path+File.separator+flowTag.getData().url)){
						String path=HttpUtil.downloadImage(Constants.UPLOAD_IMAGE_RETURN_URL, flowTag.getData().url, null,context);
						if(path!=null){
							flowTag.getData().path=path;
						}else{
							//TODO 设置一张默认图片
//							flowTag.getData().path=Constants.Picture_Topic_Path+File.separator+flowTag.getData().url;
						}
					}else{
						//TODO 设置一张默认图片
						flowTag.getData().path=Constants.Picture_Topic_Path+File.separator+flowTag.getData().url;
					}
					BitmapFactory.Options options=new BitmapFactory.Options();
					options.inSampleSize=4;
					options.inPreferredConfig=Bitmap.Config.RGB_565;
					options.inPurgeable=true;
					options.inInputShareable=true;
					if(flowTag.getData().img_id==640){
						LogUtil.i("me","test");
					}
					LogUtil.i("exception", "flowTag.getData().path=="+flowTag.getData().path);
					FileInputStream fis=null;
					Bitmap temp=null;
					
					try {
						fis = new FileInputStream(flowTag.getData().path);
						temp = BitmapFactory.decodeStream(fis,null,options);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						if(fis!=null){
							try {
								fis.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					/*if(temp==null){
						String path=HttpUtil.downloadImage(Constants.UPLOAD_IMAGE_RETURN_URL, flowTag.getData().url, null,context);
						if(path!=null){
							flowTag.getData().path=path;
						}else{
							//TODO 设置一张默认图片
//							flowTag.getData().path=Constants.Picture_Topic_Path+File.separator+flowTag.getData().url;
						}
					}*/
					LogUtil.i("exception", "bitmap=="+(temp==null));
					if(temp==null)return;
					int width = temp.getWidth();// 
					int height = temp.getHeight();
                   
					
					int layoutHeight=0;
					
					 
					Matrix matrix=new Matrix();
					float size=flowTag.getItemWidth()/(temp.getWidth()*1f);
					matrix.postScale(size,size);
					LogUtil.i("me", "width==="+width+",height===="+height+",size==="+size);
					Bitmap temp1=Bitmap.createBitmap(temp,0,0,width,height, matrix, true);
					
					 if(temp1.getHeight()*1f/temp1.getWidth()>3*1f/2){
	                    	layoutHeight=(int)(3*1f/2)*temp1.getWidth();
	                    }else{
	                    	layoutHeight = temp1.getHeight();
	                    }
					 bitmap=Bitmap.createBitmap(temp1, 0, (temp1.getHeight()-layoutHeight)/2, temp1.getWidth(), layoutHeight);
					//图片回收，在有些版本上报异常，此处注释掉，有机会查找原因
					 /* if(!temp.isRecycled()){
							temp.recycle();
							
						}*/
					
					if(flowTag.halfHeight){
                    	
//						bitmap=Bitmap.createBitmap(bitmap, 0,height/6, width, height*2/3);
                    }else{
                    }
				(context).runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap != null) {// 此处在线程过多时可能为null
							int width = bitmap.getWidth();// 获取真实宽高
							int height = bitmap.getHeight();
                            LogUtil.i("exception","flowview的宽度："+getWidth()+",高度："+getHeight());
							LayoutParams lp = getLayoutParams();
							if (lp == null) {
								lp = new LayoutParams(flowTag.getItemWidth(),
										height);
							}
							setLayoutParams(lp);
                            
							setImageBitmap(bitmap);
							LogUtil.i("exception","flowview的宽度："+getWidth()+",高度："+getHeight());
							Handler h = getViewHandler();
							Message m = h.obtainMessage(flowTag.what, width,
									height, FlowView.this);
							h.sendMessage(m);
						}
					}
				});
				//图片回收，在有些版本上报异常，此处注释掉，有机会查找原因
				/* if(!temp1.isRecycled()){
					 temp1.recycle();
				 }*/

				// }

			}

		}
	}
}
	public void setShowWaterFull(ShowWaterFull1 showWaterFull1){
		if(showWaterFull1.homeActivity!=null)
		homeActivity=showWaterFull1.homeActivity;
		this.showWaterFull1=showWaterFull1;
	}

GestureDetector detector=new GestureDetector(new OnGestureListener() {
		boolean hasSend=false;
		float oldDistance;
		boolean stop=false;
		Button button;
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll", "flowview::::onSingleTapUp");
			if(!Constants.isSuccess){
				if(homeActivity!=null){
					homeActivity.showBlurImage();
				}
				Intent intent=new Intent(context,UnregisterNoteActivity.class);

				context.startActivity(intent);
				return false;
			}
			if(homeActivity!=null){
				homeActivity.handler.sendEmptyMessage(HomeActivity.SHOW_PROGRESS);
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpUtil.imageInfo(flowTag.getData(),handler,context);
					if(homeActivity!=null){
						homeActivity.handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
					}
				}
			}).start();
			isMove=false;
			
			return true;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll", "flowview::::onShowPress");
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll", "flowview::::onScroll"+":::distanceY="+distanceY);
			isMove=true;
			if(!hasSend){
				hasSend=true;
				oldDistance=distanceY;
				lazyScrollView.onChanged((int)e2.getX(),(int) e2.getY(),(int) e1.getX(), (int)e1.getY());
			}
			if(oldDistance<0){
				if(distanceY>oldDistance&&distanceY<=0){
					if(!stop){
						lazyScrollView.scrollBy(0, (int) distanceY);
					}else{
//						lazyScrollView.scrollBy(0, (int)0);
					}
					
				}else{

					stop=true;
				}
			}else{
				lazyScrollView.scrollBy(0, (int) distanceY);
			}
//			lazyScrollView.scrollTo((int)e2.getX(),(int)e2.getY());
			
			LogUtil.i("scroll", "scrollby="+distanceY);
			return true;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll", "flowview::::onLongPress");
			/*if(!Constants.isSuccess){
				if(homeActivity!=null){
					homeActivity.showBlurImage();
				}
				Intent intent=new Intent(context,UnregisterNoteActivity.class);

				context.startActivity(intent);
				return;
			}
			if(homeActivity!=null){
				homeActivity.handler.sendEmptyMessage(HomeActivity.SHOW_PROGRESS);
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpUtil.imageInfo(flowTag.getData(),handler,context);
					if(homeActivity!=null){
						homeActivity.handler.sendEmptyMessage(HomeActivity.DISMISS_PROGRESS);
					}
				}
			}).start();
			isMove=false;*/
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll", "flowview::::onFling");
			isMove=true;
//			lazyScrollView.onChanged((int)e2.getX(),(int) e2.getY(),(int) e1.getX(), (int)e2.getY());
//			lazyScrollView.scrollTo((int)e2.getX(),(int)e2.getY());
			if(homeActivity!=null){
				if(lazyScrollView.getScrollY()>10){
					homeActivity.handler.sendEmptyMessage(HomeActivity.HIDE_BACKGROUND_CONTROL);
				}else if(lazyScrollView.getScrollY()<10){
					homeActivity.handler.sendEmptyMessage(HomeActivity.SHOW_BACKGROUND_CONTROL);
				}
			}
			return true;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll", "flowview::::onDown");
			hasSend=false;
			stop=false;
			return true;
		}
	});
	}
