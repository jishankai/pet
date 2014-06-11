package com.aidigame.hisun.pet.widget.fragment;
import java.io.File;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HorizontalListViewAdapter;
import com.aidigame.hisun.pet.bean.ChartletBmp;
import com.aidigame.hisun.pet.ui.HandlePictureActivity;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.HorizontialListView;
/**
 * �Զ���Fragment  ʵ������fragment�Ĺ���
 * @author admin
 *
 */
/**
 * �Զ���Fragment  ʵ������fragment�Ĺ���
 * @author admin
 *
 */
public class HorizontalListViewFragment {
	LinearLayout parentView;
	View view;
	HandlePictureActivity context;
	ImageView imageView;
	SurfaceView surfaceView;
	public int xCanvas=0,yCanvas=0;//surfaceView �ɻ��ƵĿ�ߡ�
	HorizontialListView horizontialListView;
	HorizontalListViewAdapter adapter;
	ArrayList<String> path;
	ArrayList<String> pictureName;
	int id;
	public static Bitmap chartletTypeBmp;//ѡ�е���ͼ����
	String chartletTypeBmpPath=null;
	public static Bitmap chartletBmpFocus;//���ڴ������ͼ
	public static Bitmap frameBmp;//���
	public static Bitmap controlBmp;//������ �����ţ���ת
	public static int chartletBmpFocus_centerX=0,chartletBmpFocus_centerY=0;
	public static int controlBmp_centerX_origin=0,controlBmp_centerY_origin=0;
	public static int controlBmp_centerX=0,controlBmp_centerY=0;
	public static int distanceBetween2Center=0;
	public ArrayList<ChartletBmp> list=new ArrayList<ChartletBmp>();
	public static final int  MODE_SCALE=0;
	public static final int  MODE_ROTATE=1;
	public static final int MODE_TRANSLATE=2;
	public static int current_mode=-1;
	public float degrees=0f;
	public float moveCount=0;
	public HorizontalListViewFragment(HandlePictureActivity context,LinearLayout view,int id,ImageView imageView,SurfaceView surfaceView){
		this.context=context;
		this.parentView=view;
		this.imageView=imageView;
		this.surfaceView=surfaceView;
		parentView.removeAllViews();
	    this.id=id;
		initView();
		setListener();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(30);
					xCanvas=HorizontalListViewFragment.this.imageView.getWidth();
					int[] xy=new int[2];
					HorizontalListViewFragment.this.parentView.getLocationOnScreen(xy);
				    yCanvas=xy[1];
				    HorizontalListViewFragment.this.imageView.getLocationOnScreen(xy);
				    yCanvas=yCanvas-xy[1];
				    LogUtil.i("me", "xCanvas="+xCanvas+",yCanvas="+yCanvas+";");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(context).inflate(R.layout.fragment_function_123, null);
		parentView.addView(view);
		horizontialListView=(HorizontialListView)view.findViewById(R.id.horizontal_listview);
		loadData();
		adapter=new HorizontalListViewAdapter(context, path, pictureName);
		horizontialListView.setAdapter(adapter);
	}

	private void loadData(){
		switch (id) {
		case 1:
			loadFunction1Data();
			break;
        case 2:
        	loadFunction2Data();
			break;
        case 3:
        	loadFunction3Data();
	        break;
		}
	}

	private void loadFunction1Data() {
		// TODO Auto-generated method stub
		path=new ArrayList<String>();
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");

		pictureName=new ArrayList<String>();
		pictureName.add("Normal");
		pictureName.add("Maple");
		pictureName.add("Finch");
		pictureName.add("Alda");
		pictureName.add("Moss");
		
		pictureName.add("function6");
		pictureName.add("function7");
	}
	private void loadFunction2Data() {
		// TODO Auto-generated method stub
		path=new ArrayList<String>();
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"beauty_info_animation_eye_11.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"beauty_info_animation_eye_11.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"beauty_info_animation_eye_11.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"beauty_info_animation_eye_11.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"beauty_info_animation_eye_11.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"beauty_info_animation_eye_11.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"beauty_info_animation_eye_11.png");
		pictureName=new ArrayList<String>();

		pictureName.add("����1");
		pictureName.add("����2");
		pictureName.add("����3");
		pictureName.add("����4");
		pictureName.add("����5");
		pictureName.add("function6");
		pictureName.add("function7");
	}
	private void loadFunction3Data() {
		// TODO Auto-generated method stub
		path=new ArrayList<String>();
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");
		path.add(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"ic_launcher.png");

		pictureName=new ArrayList<String>();
		pictureName.add("�߿�1");
		pictureName.add("�߿�2");
		pictureName.add("�߿�3");
		pictureName.add("�߿�4");
		pictureName.add("�߿�5");
		
		pictureName.add("function6");
		pictureName.add("function7");
	}
	private void setListener(){
		horizontialListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String path=(String)parent.getItemAtPosition(position);
				switch (HorizontalListViewFragment.this.id) {
				case 1:
					choseColor(position);
					break;
				case 2:
					chartletTypeBmpPath=path;
					chartletBmpFocus_centerX=xCanvas/2;
			        chartletBmpFocus_centerY=yCanvas/2;
					chartlet(path);
					break;
				case 3:
					
					break;
				}
			}
		});
		surfaceView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(!context.isMovingChartlet||chartletTypeBmp==null)break;
					judgeTouchEvent(event);
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				case MotionEvent.ACTION_MOVE:
					if(current_mode==MODE_SCALE){
						/*if(moveCount%50==0)*/
						scaleChartlet(event);
						moveCount++;
					
					}else if(current_mode==MODE_TRANSLATE){
						translateChartlet(event);
					}
					break;
				}
				return true;
			}

		});
		
	}

	private void choseColor(int position) {
		// TODO Auto-generated method stub
		//TODO �趨��ɫ��ֵ
		Bitmap bmp=Bitmap.createBitmap(HandlePictureActivity.handlingBmp);
		switch (position) {
		case 0:
			
			break;
		case 1:
			
			bmp=ImageUtil.addColor(bmp,00, 240, 230,140);
			break;
		case 2:
			bmp=ImageUtil.addColor(bmp, 00,255, 228, 196);
			break;
		case 3:
			bmp=ImageUtil.addColor(bmp, 0, 143, 188, 143);
			break;
		case 4:
			bmp=ImageUtil.addColor(bmp, 0, 255, 105, 180);
			break;
		case 5:
			bmp=ImageUtil.addColor(bmp, 0, 186, 85, 211);
			break;
		case 6:
			
			break;
		}
		imageView.setImageBitmap(bmp);
	}
	private void chartlet(String path) {
		// TODO Auto-generated method stub
		Bitmap bmp=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"beauty_info_animation_eye_11.png");
        chartletTypeBmp=bmp;
        float sizeX=72/(bmp.getWidth()*1f);
        float sizeY=72/(bmp.getHeight()*1f);
        float size=Math.max(sizeX, sizeY);
        chartletBmpFocus=ImageUtil.scaleImage(chartletTypeBmp, size, size);
        frameBmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.camera_take_touch_frame);
        controlBmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.take_img_box02);
        sizeX=72/(frameBmp.getWidth()*1f);
        sizeY=72/(frameBmp.getHeight()*1f);
        size=Math.max(sizeX, sizeY);
        frameBmp=ImageUtil.scaleImage(frameBmp, size, size);
        
        Canvas canvas=surfaceView.getHolder().lockCanvas();
        canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
        for(ChartletBmp c:list){
        	c.draw(canvas);
        }
        canvas.drawBitmap(chartletBmpFocus, chartletBmpFocus_centerX-chartletBmpFocus.getWidth()/2,chartletBmpFocus_centerY-chartletBmpFocus.getHeight()/2 , null);
        canvas.drawBitmap(frameBmp, chartletBmpFocus_centerX-chartletBmpFocus.getWidth()/2, chartletBmpFocus_centerY-chartletBmpFocus.getHeight()/2, null);
        controlBmp_centerX_origin=chartletBmpFocus_centerX+chartletBmpFocus.getWidth()/2+controlBmp.getWidth()/2;
        controlBmp_centerY_origin=chartletBmpFocus_centerY-chartletBmpFocus.getHeight()/2-controlBmp.getHeight()/2;
        controlBmp_centerX=controlBmp_centerX_origin;
        controlBmp_centerY=controlBmp_centerY_origin;
        distanceBetween2Center=(int)Math.sqrt((chartletBmpFocus_centerX-controlBmp_centerX)*(chartletBmpFocus_centerX-controlBmp_centerX)+(chartletBmpFocus_centerX-controlBmp_centerX)*(chartletBmpFocus_centerX-controlBmp_centerX));
        canvas.drawBitmap(controlBmp, chartletBmpFocus_centerX+chartletBmpFocus.getWidth()/2, chartletBmpFocus_centerY-chartletBmpFocus.getHeight()/2-controlBmp.getHeight(), null);
        surfaceView.getHolder().unlockCanvasAndPost(canvas);
	}

	private void judgeTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x=(int) event.getX();
		int y=(int) event.getY();
		if(chartletBmpFocus==null){
			chartletBmpFocus_centerX=(int) event.getX();
			chartletBmpFocus_centerY=(int) event.getY();
			chartlet(chartletTypeBmpPath);
		}else
		if(x>=controlBmp_centerX-controlBmp.getWidth()&&x<=controlBmp_centerX+controlBmp.getWidth()
		   &&y>=controlBmp_centerY-controlBmp.getHeight()&&y<=controlBmp_centerY+controlBmp.getHeight()){
			current_mode=0;
			LogUtil.i("me", "������������");
		}else if(x>=chartletBmpFocus_centerX-chartletBmpFocus.getWidth()/2&&x<=chartletBmpFocus_centerX+chartletBmpFocus.getWidth()/2
				&&y>=chartletBmpFocus_centerX-chartletBmpFocus.getHeight()/2&&y<=chartletBmpFocus_centerY+chartletBmpFocus.getHeight()/2){
			current_mode=MODE_TRANSLATE;
		}else if(y>=yCanvas||x>=xCanvas){
			current_mode=-1;
		}else {
			//TODO
			current_mode=-1;
			if(chartletBmpFocus!=null){
				ChartletBmp bmp=new ChartletBmp(chartletBmpFocus, chartletBmpFocus_centerX, chartletBmpFocus_centerY,0f);
				list.add(bmp);
				chartletBmpFocus=null;
				Canvas canvas=surfaceView.getHolder().lockCanvas();
				canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
				for(ChartletBmp c:list){
					c.draw(canvas);
				}
				surfaceView.getHolder().unlockCanvasAndPost(canvas);
			}

		}
			
	}
	private void scaleChartlet(MotionEvent event){
		int x=(int) event.getX();
		int y=(int) event.getY();
//		int mX=(x+chartletBmpFocus_centerX)/2;
//		int mY=(y+chartletBmpFocus_centerY)/2;
//		float degrees=(float) Math.acos(Math.sqrt((mX-chartletBmpFocus_centerX)*(mX-chartletBmpFocus_centerX)+(mY-chartletBmpFocus_centerY)*(mY-chartletBmpFocus_centerY))*1f/distanceBetween2Center);
		controlBmp_centerX=x;
		controlBmp_centerY=y;
		int distance=(int)Math.sqrt((x-chartletBmpFocus_centerX)*(x-chartletBmpFocus_centerX)+((y-chartletBmpFocus_centerY)*(y-chartletBmpFocus_centerY)));
		int width=(int)Math.sqrt(distance*distance/2)*2;
		float sizeX=width/(frameBmp.getWidth()*1f);
		float sizeY=width/(frameBmp.getHeight()*1f);
		float size=Math.max(sizeX, sizeY);
		Bitmap frameBmpTemp=ImageUtil.scaleImage(frameBmp, size, size);
		sizeX=width/(chartletTypeBmp.getWidth()*1f);
		sizeY=width/(chartletTypeBmp.getHeight()*1f);
		size=Math.max(sizeX, sizeY);
		chartletBmpFocus=ImageUtil.scaleImage(chartletTypeBmp, size, size);
		LogUtil.i("me1", "chartletBmpFocus.getWidth()="+chartletBmpFocus.getWidth()+",chartletBmpFocus.getHeight()"+chartletBmpFocus.getHeight());
//		chartletBmpFocus=Bitmap.createBitmap(chartletBmpFocus, 0, 0, chartletBmpFocus.getWidth(), chartletBmpFocus.getHeight(), matrix, true);
		Canvas canvas=surfaceView.getHolder().lockCanvas();

		canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR );
        for(ChartletBmp c:list){
        	c.draw(canvas);
        }
		canvas.drawBitmap(frameBmpTemp, chartletBmpFocus_centerX-width/2, chartletBmpFocus_centerY-width/2, null);
		canvas.drawBitmap(chartletBmpFocus, chartletBmpFocus_centerX-width/2, chartletBmpFocus_centerY-width/2, null);
		canvas.drawBitmap(controlBmp, chartletBmpFocus_centerX+width/2, chartletBmpFocus_centerY-width/2-controlBmp.getHeight()/2, null);
		controlBmp_centerX=chartletBmpFocus_centerX+chartletBmpFocus.getWidth()/2+controlBmp.getWidth()/2;
        controlBmp_centerY=chartletBmpFocus_centerY-chartletBmpFocus.getHeight()/2-controlBmp.getHeight()/2;
//		canvas.drawBitmap(controlBmp, controlBmp_centerX-controlBmp.getWidth()/2, controlBmp_centerY-controlBmp.getHeight()/2, null);
		surfaceView.getHolder().unlockCanvasAndPost(canvas);
	}
	private void translateChartlet(MotionEvent event){
		int x=(int)event.getX();
		int  y=(int)event.getY();
		controlBmp_centerX_origin=controlBmp_centerX_origin+x-chartletBmpFocus_centerX;
		controlBmp_centerY_origin=controlBmp_centerY_origin+y-chartletBmpFocus_centerY;
		controlBmp_centerX=controlBmp_centerX_origin;
				controlBmp_centerY=controlBmp_centerY_origin;
		chartletBmpFocus_centerX=x;
		chartletBmpFocus_centerY=y;
		Canvas canvas=surfaceView.getHolder().lockCanvas();

		canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR );
        for(ChartletBmp c:list){
        	c.draw(canvas);
        }
		canvas.drawBitmap(chartletBmpFocus, chartletBmpFocus_centerX-chartletBmpFocus.getWidth()/2, chartletBmpFocus_centerY-chartletBmpFocus.getHeight()/2, null);
		float sizeX=chartletBmpFocus.getWidth()/2/(frameBmp.getWidth()*1f);
		float sizeY=chartletBmpFocus.getHeight()/(frameBmp.getHeight()*1f);
		float size=Math.max(sizeX, sizeY);
		Bitmap frameBmpTemp=ImageUtil.scaleImage(frameBmp, size, size);
		canvas.drawBitmap(frameBmpTemp,  chartletBmpFocus_centerX-chartletBmpFocus.getWidth()/2, chartletBmpFocus_centerY-chartletBmpFocus.getHeight()/2, null);
//		canvas.drawBitmap(controlBmp, controlBmp_centerX-controlBmp.getWidth()/2, controlBmp_centerY-controlBmp.getHeight()/2, null);
		canvas.drawBitmap(controlBmp, chartletBmpFocus_centerX+chartletBmpFocus.getWidth()/2, chartletBmpFocus_centerY-chartletBmpFocus.getHeight()/2-controlBmp.getHeight()/2, null);
		controlBmp_centerX=chartletBmpFocus_centerX+chartletBmpFocus.getWidth()/2+controlBmp.getWidth()/2;
        controlBmp_centerY=chartletBmpFocus_centerY-chartletBmpFocus.getHeight()/2-controlBmp.getHeight()/2;
		surfaceView.getHolder().unlockCanvasAndPost(canvas);
	}

}
