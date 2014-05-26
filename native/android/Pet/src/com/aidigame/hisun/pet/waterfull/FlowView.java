package com.aidigame.hisun.pet.waterfull;

import java.io.BufferedInputStream;
import java.io.IOException;

import com.aidigame.hisun.pet.util.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

public class FlowView extends ImageView implements View.OnClickListener,
		View.OnLongClickListener {

	private AnimationDrawable loadingAnimation;
	private FlowTag flowTag;
	private Context context;
	public Bitmap bitmap;
	private ImageLoaderTask task;
	private int columnIndex;// ͼƬ���ڵڼ���
	private int rowIndex;// ͼƬ���ڵڼ���
	private Handler viewHandler;

	public FlowView(Context c, AttributeSet attrs, int defStyle) {
		super(c, attrs, defStyle);
		this.context = c;
		Init();
	}

	public FlowView(Context c, AttributeSet attrs) {
		super(c, attrs);
		this.context = c;
		Init();
	}

	public FlowView(Context c) {
		super(c);
		this.context = c;
		Init();
	}

	private void Init() {

		setOnClickListener(this);
		this.setOnLongClickListener(this);
		setAdjustViewBounds(true);

	}

	@Override
	public boolean onLongClick(View v) {
		Log.d("FlowView", "LongClick");
		Toast.makeText(context, "����" + this.flowTag.getFlowId(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void onClick(View v) {
		Log.d("FlowView", "Click");
		Toast.makeText(context, "����" + this.flowTag.getFlowId(),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * ����ͼƬ
	 */
	public void LoadImage() {
		if (getFlowTag() != null) {

			new LoadImageThread().start();
		}
	}

	/**
	 * ���¼���ͼƬ
	 */
	public void Reload() {
		if (this.bitmap == null && getFlowTag() != null) {

			new ReloadImageThread().start();
		}
	}

	/**
	 * �����ڴ�
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

				BufferedInputStream buf;
				try {
					buf = new BufferedInputStream(flowTag.getAssetManager()
							.open(flowTag.getFileName()));
					bitmap = BitmapFactory.decodeStream(buf);

				} catch (IOException e) {

					e.printStackTrace();
				}

				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap != null) {// �˴����߳̽϶�ʱ����Ϊnull
							int width = bitmap.getWidth();// ��ȡ���Ǹ߶�
							int height = bitmap.getHeight();
							LayoutParams lp = getLayoutParams();
							int layoutHeight=0;
							Matrix matrix=new Matrix();
							float size=flowTag.getItemWidth()/(bitmap.getWidth()*1f);
							matrix.postScale(size,size);
							LogUtil.i("me", "width==="+width+",height===="+height+",size==="+size);
							bitmap=Bitmap.createBitmap(bitmap,0,0,width,height, matrix, true);
							height=bitmap.getHeight();
							width=bitmap.getWidth();
							if(flowTag.halfHeight){
								bitmap=Bitmap.createBitmap(bitmap, 0,height/6, width, height*2/3);
//								bitmap=Bitmap.createBitmap(bitmap, 0,height/4, width, height/2);
                            }else{
                            }
							layoutHeight=bitmap.getHeight();
							if (lp == null) {
								lp = new LayoutParams(flowTag.getItemWidth(),
										layoutHeight);
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

				BufferedInputStream buf;
				try {
					buf = new BufferedInputStream(flowTag.getAssetManager()
							.open(flowTag.getFileName()));
					bitmap = BitmapFactory.decodeStream(buf);
					

				} catch (IOException e) {

					e.printStackTrace();
				}
				// if (bitmap != null) {

				// �˴����ܸ���UI�����ܷ����쳣
				// CalledFromWrongThreadException: Only the original thread that
				// created a view hierarchy can touch its views.
				// Ҳ����ʹ��Handler��Looper����Message������������题

				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap != null) {// �˴����߳̽϶�ʱ����Ϊnull
							int width = bitmap.getWidth();// ��ȡ���Ǹ߶�
							int height = bitmap.getHeight();
                            
							LayoutParams lp = getLayoutParams();
							int layoutHeight=0;
							 layoutHeight = (height * flowTag.getItemWidth())
										/ width;// �����߶�
							Matrix matrix=new Matrix();
							float size=flowTag.getItemWidth()/(bitmap.getWidth()*1f);
							matrix.postScale(size,size);
							LogUtil.i("me", "width==="+width+",height===="+height+",size==="+size);
							bitmap=Bitmap.createBitmap(bitmap,0,0,width,height, matrix, true);
							height=bitmap.getHeight();
							width=bitmap.getWidth();
							if(flowTag.halfHeight){
                            	
								bitmap=Bitmap.createBitmap(bitmap, 0,height/6, width, height*2/3);
                            }else{
                            }
                           
									 
							layoutHeight=bitmap.getHeight();
							if (lp == null) {
								lp = new LayoutParams(flowTag.getItemWidth(),
										layoutHeight);
							}
							setLayoutParams(lp);

							setImageBitmap(bitmap);
							Handler h = getViewHandler();
							Message m = h.obtainMessage(flowTag.what, width,
									layoutHeight, FlowView.this);
							h.sendMessage(m);
						}
					}
				});

				// }

			}

		}
	}
}
