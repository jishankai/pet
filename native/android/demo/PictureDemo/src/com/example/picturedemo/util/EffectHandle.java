package com.example.picturedemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.picturedemo.R;
import com.example.picturedemo.adapter.HorizontalListViewAdapter;
import com.example.picturedemo.view.HorizontialListView;

public class EffectHandle {
	Context context;
	ImageView imageView;
	PopupWindow popup;
	HorizontialListView horizontalListView;
	HorizontalListViewAdapter adapter;
	Bitmap bitmap;
	public EffectHandle(Context context,ImageView imageView){
		this.context=context;
		this.imageView=imageView;
		initPopup();
	}
	private void initPopup() {
		// TODO Auto-generated method stub
		popup=new PopupWindow(context);
		popup.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popup.setFocusable(true);
		View view=LayoutInflater.from(context).inflate(R.layout.popup_effect_handle, null);
		popup.setContentView(view);
		popup.showAsDropDown(imageView);
		horizontalListView=(HorizontialListView)view.findViewById(R.id.horizontal_listview);
		bitmap=getBitmap();
		adapter=new HorizontalListViewAdapter(context,imageView);
		horizontalListView.setAdapter(adapter);
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				int tag=position;
				HorizontalListViewAdapter.waterFlag=false;
				switch (tag) {
				case 0:
					
					EffectUtil.addFrame2(context, imageView);
					break;
				case 1:
					HorizontalListViewAdapter.waterFlag=true;
					imageView.setOnTouchListener(new OnTouchListener() {
						
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							boolean flag=false;
							if(HorizontalListViewAdapter.waterFlag){
								switch (event.getAction()) {
								case MotionEvent.ACTION_MOVE:
									EffectUtil.touchX=(int)event.getX();
									EffectUtil.touchY=(int)event.getY();
									EffectUtil.addWaterMark2(context, imageView);
									break;
								}
								flag=true;							
							}
							return flag;

						}
					});
					EffectUtil.addWaterMark(context, imageView);
					break;
				case 2:
					EffectUtil.oldTimeEffect(context, imageView);
					break;
				case 3:
					EffectUtil.blurEffectByGauss(context, imageView);
					break;
				case 4:
					EffectUtil.sharpenEffect(context, imageView);
					break;
				case 5:
					EffectUtil.embossEffect(context, imageView);
					break;
				case 6:
					EffectUtil.filmEffect(context, imageView);
					break;
				case 7:
					EffectUtil.sunshineEffect(context, imageView);
					break;
				case 8:
					EffectUtil.overlayEffect(context, imageView);
					break;
				case 9:
					EffectUtil.haloEffect(context, imageView);
					break;
				case 10:
					EffectUtil.striaEffect(context, imageView);
					break;
				case 11:
					break;
				}
			}
		});
		
	}
	private Bitmap getBitmap(){
		imageView.setDrawingCacheEnabled(true);
		bitmap=Bitmap.createBitmap(imageView.getDrawingCache());
		imageView.setDrawingCacheEnabled(false);
		return bitmap;
	}
	public void dismiss(){
		popup.dismiss();
	}

}
