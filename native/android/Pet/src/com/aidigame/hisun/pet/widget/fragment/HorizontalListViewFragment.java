package com.aidigame.hisun.pet.widget.fragment;
import java.io.File;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HorizontalListViewAdapter;
import com.aidigame.hisun.pet.ui.HandlePictureActivity;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.HorizontialListView;
/**
 * 自定义Fragment  实现类似fragment的功能
 * @author admin
 *
 */
public class HorizontalListViewFragment {
	LinearLayout parentView;
	RelativeLayout relativeLayout;
	View view;
	HandlePictureActivity context;
	ImageView imageView;
	HorizontialListView horizontialListView;
	HorizontalListViewAdapter adapter;
	ArrayList<String> path;
	ArrayList<String> pictureName;
	int id;
	int  Mode_Move=0;
	int Mode_Scale=1;
	int Mode_Translate=2;
	int Mode_X=0;
	int Mode_Y=0;
	public static int deltaX=0,deltaY=0;
	public static int Mode_Distance=0;
	public static int radius=0;
	public static int touchCenterX=0,touchCenterY=0;
	public static int pictureCenterX=0,pictureCenterY=0;
	boolean isMoving=false;
	public static Bitmap chartletBmp;
	public static Bitmap chartletFocusBmp;
	public HorizontalListViewFragment(HandlePictureActivity context,LinearLayout view,int id,ImageView imageView){
		this.context=context;
		this.parentView=view;
		this.imageView=imageView;
		parentView.removeAllViews();
	    this.id=id;
		initView();
		setListener();
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

		pictureName.add("分类1");
		pictureName.add("分类2");
		pictureName.add("分类3");
		pictureName.add("分类4");
		pictureName.add("分类5");
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
		pictureName.add("边框1");
		pictureName.add("边框2");
		pictureName.add("边框3");
		pictureName.add("边框4");
		pictureName.add("边框5");
		
		pictureName.add("function6");
		pictureName.add("function7");
	}
	private void setListener(){
		horizontialListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			 if(context.actionPicturePath==null){
					long time=System.currentTimeMillis();
					context.actionPicturePath=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+""+time;
					File file=new File(context.actionPicturePath);
					file.mkdirs();
					LogUtil.i("me","path====="+path);				
			}
			LogUtil.i("me","path====="+path);
			String temp=context.actionPicturePath+File.separator+context.actionCount+".jpg";
			context.actionCount++;
			Bitmap bmp=ImageUtil.getBitmapFromImageView(imageView);
			ImageUtil.compressImage(bmp, 100,temp);
				String path=(String)parent.getItemAtPosition(position);
				switch (HorizontalListViewFragment.this.id) {
				case 1:
					choseColor(position);
					break;
				case 2:
					chartlet(path);
					break;
				case 3:
					
					break;
				}
			}



		});
		imageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (context.isMovingChartlet) {
					LogUtil.i("me", "imageview---------");
					chartletMove(event);
					return true;
				}
				return false;
			}

			private void chartletMove(MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
                    if(chartletFocusBmp==null){
                    	if(chartletBmp==null)return;
                    	Mode_X=(int) event.getX();
                    	Mode_Y=(int) event.getY();
                    	chartletFocusBmp=ImageUtil.chartletFocusBmp(chartletBmp, context, 0);
                    	Bitmap bmp=ImageUtil.chartlet(HandlePictureActivity.handlingBmp, Mode_X, Mode_Y, chartletFocusBmp);
                    	imageView.setImageBitmap(bmp);
                    	touchCenterX=Mode_X+deltaX;
                    	touchCenterY=Mode_Y+deltaY;
                    	Mode_X=touchCenterX;
                    	Mode_Y=touchCenterY;
                    	Mode_Distance=(int)Math.sqrt((Mode_X-pictureCenterX)*(Mode_X-pictureCenterX)+(Mode_Y-pictureCenterY)*(Mode_Y-pictureCenterY));
                    	LogUtil.i("me", "Mode_Distance"+Mode_Distance);
                    }else{
                    	int distance=touchPointBetweenCenter(event);
                    	if(distance<=radius){
                    		isMoving=true;
                    		touchCenterX=(int) event.getX();
                    		touchCenterY=(int) event.getY();
                    	}else{
                    		Bitmap bmp=ImageUtil.chartlet(HandlePictureActivity.handlingBmp, Mode_X, Mode_Y+deltaY, chartletBmp);
                        	imageView.setImageBitmap(bmp);
                        	chartletFocusBmp=null;
                    	}
                    	
                    }
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_MOVE:
					if(isMoving){
						
					}

					break;
				}
			}

			private int touchPointBetweenCenter(MotionEvent event) {
				// TODO Auto-generated method stub
				int x=(int) event.getX();
				int y=(int) event.getY();
				int distance=(int)Math.sqrt((x-touchCenterX)*(x-touchCenterX)+(y-touchCenterY)*(y-touchCenterY));
				
				return distance;
			}
		});
	}

	private void choseColor(int position) {
		// TODO Auto-generated method stub
		//TODO 设定颜色的值
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
//		bmp=ImageUtil.scaleImage(bmp, 0.5f, 0.5f);
		context.currentChartletPath=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"beauty_info_animation_eye_11.png";
		chartletBmp=bmp;
		if(bmp==null)return;
	  /*  final Bitmap  bmp1=ImageUtil.chartlet(HandlePictureActivity.handlingBmp, 0, 0,bmp);
//		bmp.recycle();
		bmp=null;
//		HandlePictureActivity.handlingBmp=bmp1;
	    imageView.setImageBitmap(bmp1);*/
	}

}
