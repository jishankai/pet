package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.GalleryFlow;
import com.aidigame.hisun.pet.view.TopicView;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/*
 * Copyright (C) 2010 Neil Davies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This code is base on the Android Gallery widget and was Created 
 * by Neil Davies neild001 'at' gmail dot com to be a Coverflow widget
 * 
 * @author Neil Davies
 */
public class ImageAdapter1 extends BaseAdapter {
	int mGalleryItemBackground;
	private Activity mContext;
    int total=1999999999;
	ArrayList<PetPicture> list;
	/*ImageFetcher mImageFetcher1;
	ImageFetcher mImageFetcher2;
	ImageFetcher mImageFetcher3;
	ImageFetcher mImageFetcher4;
	ImageFetcher mImageFetcher5;*/
	BitmapFactory.Options options;
	private ImageView[] mImages;
    String[] urls;
	public ImageAdapter1(Activity c, ArrayList<PetPicture> list) {
		mContext = c;
		/*mImageFetcher1=new ImageFetcher(c, 240,240);
		mImageFetcher2=new ImageFetcher(c, 240,240);
		mImageFetcher3=new ImageFetcher(c, 240,240);
		mImageFetcher4=new ImageFetcher(c, 240,240);
		mImageFetcher5=new ImageFetcher(c, 240,240);*/
		this.list = list;
		urls=new String[list.size()];
		options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=StringUtil.getScaleByDPI((Activity)c);
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
//		loadData();
	}
	String url1,url2,url3,url4,url5;
	
	int count=0;
	boolean flag=false;
	public void loadData(){/*
		for(int i=0;i<list.size();i++){
			if(i==0)url1=list.get(i).url;
			if(i==1)url2=list.get(i).url;
			if(i==2)url3=list.get(i).url;
			if(i==3)url4=list.get(i).url;
			if(i==4)url5=list.get(i).url;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<list.size();i++){
				while(flag){
					LogUtil.i("me", "count==========="+count+",list.size="+list.size());
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				flag=true;
					url1=list.get(count).url;
						mImageFetcher1.setWidth(240);
						mImageFetcher1.setImageCache(new ImageCache(mContext, new ImageCacheParams(url1)));
						
						mImageFetcher1.setLoadCompleteListener(new LoadCompleteListener() {
							
							@Override
							public void onComplete(Bitmap bitmap) {
								// TODO Auto-generated method stub
								LogUtil.i("scx", "count==========="+count+",list.size="+list.size());
								urls[count]=mImageFetcher1.getFilePath(mContext, url1);
								count++;
								flag=false;
								
							}
							@Override
							public void getPath(String path) {
								// TODO Auto-generated method stub
								
							}
						});
						options.inSampleSize=16;
						if(url1.contains("@")){
			            	int a=url1.indexOf("@");
			            	int b=url1.lastIndexOf("@");
			            	int lenth=Integer.parseInt(url1.substring(a+1, b));
			            	if(lenth>1024*100){
			            		options.inSampleSize=4;
			            	}else{
			            		options.inSampleSize=StringUtil.getScaleByDPI(mContext);;
			            	}
			            }else{
			        		options.inSampleSize=StringUtil.getScaleByDPI(mContext);;
			        	}
						mImageFetcher1.loadImage(url1, new ImageView(mContext), options);
					
				}
				mContext.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						notifyDataSetChanged();
					}
				});
			}
		}).start();
	*/}



	public int getCount() {
//		return mImageIds.length;
		if(list.size()==0)return 0;
		return total;
	}

	public Object getItem(int position) {
		return list.get(position%list.size());
	}

	public long getItemId(int position) {
		return position;
	}

	
	
	public View getView(int position, View convertView, ViewGroup parent) {

		// Use this code if you want to load from resources
		/*
		 * ImageView i = new ImageView(mContext);
		 * i.setImageResource(mImageIds[position]); i.setLayoutParams(new
		 * CoverFlow.LayoutParams(350,350));
		 * i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		 * 
		 * //Make sure we set anti-aliasing otherwise we get jaggies
		 * BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
		 * drawable.setAntiAlias(true); return i;
		 */
		ImageView iv=null;
		if(convertView==null){
	}else{
		iv=(ImageView)convertView;
		iv.setImageBitmap(null);
		iv=null;
	}
		
		
		
		BitmapFactory.Options options=new BitmapFactory.Options();
//		options.inSampleSize=StringUtil.getScaleByDPIget4(mContext,list.get(position%list.size()).petPicture_path);
		
//		Bitmap bmp=BitmapFactory.decodeFile(list.get(position%list.size()).petPicture_path,options);
		Bitmap bmp=BitmapFactory.decodeFile(list.get(position%list.size()).petPicture_path/*,options*/);
		final PetPicture p=list.get(position%list.size());
		
		final ImageView imageview=new ImageView(mContext);
	
		
//		final ImageView imageview=(ImageView)LayoutInflater.from(mContext).inflate(R.layout.item_imageview, null);
		/*if(urls[position%urls.length]!=null){
			*/
			imageview.setScaleType(ScaleType.CENTER_CROP);
			imageview.setLayoutParams(new GalleryFlow.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.dip_240), mContext.getResources().getDimensionPixelSize(R.dimen.dip_240)));
//			imageview.setLayoutParams(new GalleryFlow.LayoutParams(240, 240));
			LogUtil.i("me", "mContext.getResources().getDimensionPixelSize(R.dimen.dip_240)="+mContext.getResources().getDimensionPixelSize(R.dimen.dip_240));
			imageview.setImageBitmap(bmp);
//		}
		
		/*Bitmap originalImage = BitmapFactory.decodeResource(mContext
				.getResources(), mImageIds[position%mImageIds.length],options);*/
		/*imageview.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Integer i=(Integer)imageview.getTag();
				if(i==null)return;
				if(i==0){
					imageview.setPadding(3, 3, 3, 3);
					imageview.setBackgroundColor(Color.WHITE);
				}else{
					imageview.setPadding(0, 0, 0, 0);
					imageview.setBackgroundDrawable(null);;
				}
			}
		});*/
//		imageview.setImageBitmap(originalImage);
		return imageview;
//		return mImages[position];
	}

	/**
	 * Returns the size (0.0f to 1.0f) of the views depending on the 'offset' to
	 * the center.
	 */
	public float getScale(boolean focused, int offset) {
		/* Formula: 1 / (2 ^ offset) */
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}
//	public void loadTopicImage(final ImageView topic,final PetPicture data,final int position){
//		mImageFetcher.setWidth(240);
//		mImageFetcher.setImageCache(new ImageCache(mContext, new ImageCacheParams(data.url)));
//		mImageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
//			
//			@Override
//			public void onComplete(Bitmap bitmap) {
//				// TODO Auto-generated method stub
//				urls[position]=mImageFetcher.getFilePath(mContext, data.url);
//				/*topic.setLayoutParams(new GalleryFlow.LayoutParams(240, 240));*/
//				notifyDataSetChanged();
//			}
//		});
//		mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/data.url, topic, options);
//		
//	}

}
