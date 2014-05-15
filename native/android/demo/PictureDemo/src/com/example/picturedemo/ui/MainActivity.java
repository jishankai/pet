package com.example.picturedemo.ui;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.picturedemo.R;
import com.example.picturedemo.adapter.HorizontalListViewAdapter;
import com.example.picturedemo.bean.Picture;
import com.example.picturedemo.util.ColorHandle;
import com.example.picturedemo.util.EffectHandle;
import com.example.picturedemo.util.ScanUtil;

public class MainActivity extends Activity implements OnClickListener{
	ImageView imageView;
	Button simpleBt,colorBt,effectBt,choseBt;
	RelativeLayout parentRelativelayout;
	TextView textView;
	PopupWindow popup;
	boolean flagSimple=false,flagColor=false,flagEffect=false;
	/*
	 * 图片简单处理，弹出PopupWindow中包含的控件，以及需要的属性。
	 */
	Button simpleBt1,simpleBt2,simpleBt3,simpleBt4,simpleBt5,simpleBt6;
	float scaleFloat=1.0f,rotateFloat=0.0f;
	
	/*
	 * 图片颜色和亮度处理
	 */
	ColorHandle colorHandle;
	/*
	 * 特效处理
	 */
	EffectHandle effectHandle;

	public static ArrayList<Picture> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MainActivity.list=ScanUtil.getPictures(MainActivity.this);
			}
		}).start();
        initView();
        initListener();
        
    }

	private void initView() {
		// TODO Auto-generated method stub
		imageView=(ImageView)findViewById(R.id.imageView1);
		simpleBt=(Button)findViewById(R.id.button1);
		colorBt=(Button)findViewById(R.id.button2);
		effectBt=(Button)findViewById(R.id.button3);
		choseBt=(Button)findViewById(R.id.button4);
		textView=(TextView)findViewById(R.id.textView1);
		parentRelativelayout=(RelativeLayout)findViewById(R.id.imageview_parent);
		
		
	}


    private void initListener() {
		// TODO Auto-generated method stub
		simpleBt.setOnClickListener(this);
		colorBt.setOnClickListener(this);
		effectBt.setOnClickListener(this);
		choseBt.setOnClickListener(this);
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		HorizontalListViewAdapter.waterFlag=false;	//取消ImageView的触碰事件	
		switch (v.getId()) {
		case R.id.button1:
			showSimpleHandle();
			break;
		case R.id.button2:
			showColorHandle();
			break;
		case R.id.button3:
			showEffectHandle();
			break;
		case R.id.button4:

			Intent intent=new Intent(this,ChosePictureActivity.class);
			this.startActivityForResult(intent, 0);
			break;
		}
	}



	//简单处理图片，缩放、旋转、反转
	private void showSimpleHandle() {
		// TODO Auto-generated method stub
		if(flagSimple){
			flagSimple=false;
			if(popup!=null&&popup.isShowing()){
				popup.dismiss();
			}
		}else{
			flagSimple=true;
			
			View view=LayoutInflater.from(this).inflate(R.layout.popup_simple_handle, null);
			popup=new PopupWindow(this);
			popup.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			popup.setContentView(view);
			popup.setFocusable(true);
			simpleBt1=(Button)view.findViewById(R.id.button1);
			simpleBt2=(Button)view.findViewById(R.id.button2);
			simpleBt3=(Button)view.findViewById(R.id.button3);
			simpleBt4=(Button)view.findViewById(R.id.button4);
			simpleBt5=(Button)view.findViewById(R.id.button5);
			simpleBt6=(Button)view.findViewById(R.id.button6);
			popup.showAsDropDown(imageView);
			simpleBtsSetListener();
		}
	}

	private void simpleBtsSetListener() {
		// TODO Auto-generated method stub
		imageView.setMaxWidth(parentRelativelayout.getWidth());
		imageView.setMaxHeight(parentRelativelayout.getHeight());
		//放大
		simpleBt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bitmap bitmap=getBitmap();
				if(bitmap!=null){
					if(scaleFloat<1)scaleFloat=1.0f;
					Matrix matrix=new Matrix();
					scaleFloat=1.2f;
					matrix.postScale(scaleFloat, scaleFloat);
					
					bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					if(bitmap.getHeight()>parentRelativelayout.getHeight())return;
					imageView.setImageBitmap(bitmap);
					bitmap=null;
					Log.i("me","放大图片");
				}
			}
		});
		//缩小
		simpleBt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Matrix matrix=new Matrix();
				Bitmap bitmap=getBitmap();
				if(bitmap==null)return;
				if(scaleFloat>1)scaleFloat=1.0f;
				scaleFloat=0.9f;
				matrix.postScale(scaleFloat, scaleFloat);
				
				if(scaleFloat<=0.3)return;
				bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				imageView.setImageBitmap(bitmap);
				bitmap=null;
			}
		});
		simpleBt3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Matrix matrix=new Matrix();
				Bitmap bitmap=getBitmap();
				if(bitmap==null)return;
				rotateFloat=25f;
				matrix.postRotate(rotateFloat);
				bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				imageView.setImageBitmap(bitmap);
				bitmap=null;
			}
		});
        simpleBt4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Matrix matrix=new Matrix();
				Bitmap bitmap=getBitmap();
				if(bitmap==null)return;
				rotateFloat=-25f;
				matrix.postRotate(rotateFloat);
				bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				imageView.setImageBitmap(bitmap);
				bitmap=null;
			}
		});
        //左右反转
        simpleBt5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Matrix matrix=new Matrix();
				Bitmap bitmap=getBitmap();
				if(bitmap==null)return;
				float[] floats=new float[]{-1f,0f,0f,0f,1f,0f,0f,0f,1f};
				matrix.setValues(floats);;
				bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				imageView.setImageBitmap(bitmap);
				bitmap=null;
			}
		}); 
        //上下反转
        simpleBt6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Matrix matrix=new Matrix();
				Bitmap bitmap=getBitmap();
				if(bitmap==null)return;
				float[] floats=new float[]{1f,0f,0f,0f,-1f,0f,0f,0f,1f};
				matrix.setValues(floats);;
				bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				imageView.setImageBitmap(bitmap);
				bitmap=null;
			}
		});        
        
	}
	//图片饱和度，色相，亮度调节
	private void showColorHandle() {
		// TODO Auto-generated method stub
		if(flagColor){
			flagColor=false;
			if(colorHandle!=null){
				colorHandle.dismiss();
			}
		}else{
			flagColor=true;
			colorHandle=new ColorHandle(this, imageView);
		}
	}
	public Bitmap getBitmap(){
		Bitmap bitmap=null;
		imageView.setDrawingCacheEnabled(true);
		bitmap=Bitmap.createBitmap(imageView.getDrawingCache());
		imageView.setDrawingCacheEnabled(false);
		return bitmap;
	}
    //特效处理
	private void showEffectHandle() {
		// TODO Auto-generated method stub
		if(flagEffect){
			flagEffect=false;
			if(effectHandle!=null){
				effectHandle.dismiss();
			}
		}else{
			flagEffect=true;
			effectHandle=new EffectHandle(this, imageView);
		}
	}
	//获取选中的图片相关信息，并显示在界面上。
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==0&&data!=null){
			int position=data.getIntExtra("position", -1);
			Log.i("me", "onActivityResult"+":"+position);
			if(position!=-1){
				String path=list.get(position).path;
				Bitmap bitmap=BitmapFactory.decodeFile(path);
				imageView.setImageBitmap(bitmap);;
				Log.i("me", ""+list.get(position).size);
				textView.setText(list.get(position).fullName+"  大小:"+(list.get(position).size)/1024f+" kb");
				bitmap=null;
			}
		}
	}
    
}
