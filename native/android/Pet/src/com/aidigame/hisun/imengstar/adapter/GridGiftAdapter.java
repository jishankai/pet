package com.aidigame.hisun.imengstar.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.aidigame.hisun.imengstar.bean.Gift;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.util.ImageUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.R;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class GridGiftAdapter extends BaseAdapter {
	Activity context;
	ArrayList<Gift> gifts;
	
	public GridGiftAdapter(Activity context,ArrayList<Gift> gifts){
		this.context=context;
		this.gifts=gifts;
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
//		options.inSampleSize=4;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.noimg)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
	public void update(ArrayList<Gift> gifts){
		this.gifts=gifts;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int a=gifts.size()%3;
		if(a==0){
			return gifts.size()/3;
		}else{
			return gifts.size()/3+1;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_pet_grid_gift, null);
			holder=new Holder();
			holder.iv1=(LinearLayout)convertView.findViewById(R.id.iv1);
			holder.iv2=(LinearLayout)convertView.findViewById(R.id.iv2);
			holder.iv3=(LinearLayout)convertView.findViewById(R.id.iv3);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.iv1.setVisibility(View.GONE);
		holder.iv2.setVisibility(View.GONE);
		holder.iv3.setVisibility(View.GONE);
		int index=position*3;
		if(index<gifts.size()){
			loadTopicImage(holder.iv1, gifts.get(index));
		}
		if((index+1)<gifts.size()){
			loadTopicImage(holder.iv2, gifts.get(index+1));
		}
		if((index+2)<gifts.size()){
			loadTopicImage(holder.iv3, gifts.get(index+2));
		}
		
		return convertView;
	}
	public void loadTopicImage(final LinearLayout parent,final Gift gift){
		parent.removeAllViews();
		parent.setVisibility(View.VISIBLE);
		LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)parent.getLayoutParams();
		if(param==null){
			param=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		int w=(int)((Constants.screen_width-context.getResources().getDimensionPixelSize(R.dimen.one_dip)*5)*1f/3f);
		param.width=w;
//		param.height=w;
		parent.setLayoutParams(param);
		View view=LayoutInflater.from(context).inflate(R.layout.item_user_gift_box_gridview, null);
		TextView addLikeTV=(TextView)view.findViewById(R.id.textView4);
		TextView giftNameTV=(TextView)view.findViewById(R.id.textView1);
		TextView giftNumTV=(TextView)view.findViewById(R.id.textView2);
		ImageView giftIV=(ImageView)view.findViewById(R.id.imageView1);
		RelativeLayout boxLayout=(RelativeLayout)view.findViewById(R.id.box_layout);
		/*try {
			giftIV.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		loadImage(giftIV, gift);
		if(gift.add_rq>0){
			addLikeTV.setText("+ "+gift.add_rq);
			boxLayout.setBackgroundResource(R.drawable.gift_box_background);
		}else{
			addLikeTV.setText("- "+(-gift.add_rq));
			boxLayout.setBackgroundResource(R.drawable.gift_box_background_subtract);
		}
		
		giftNameTV.setText(gift.name);
		giftNumTV.setText(""+gift.boughtNum);
		parent.addView(view);
	}
	DisplayImageOptions displayImageOptions;
	public void loadImage(final ImageView icon,final Gift gift){
			
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(gift.detail_image_url, icon, displayImageOptions,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					String name="gift"+gift.no+"_";
					if(loadedImage!=null&&StringUtil.isEmpty(gift.detail_image_path)){
					   File f=new File(Constants.Picture_Root_Path+File.separator+name+".jpg");
					   if(f.exists()){
						   gift.detail_image_path=Constants.Picture_Root_Path+File.separator+name+".jpg";
					   }else{
						   String path=ImageUtil.compressImageByName(loadedImage, name);
							if(!StringUtil.isEmpty(path)){
								gift.detail_image_path=path;
							}else{
								gift.detail_image_path=ImageUtil.compressImage(loadedImage, name);
							}
					   }
					
					
					/*BitmapFactory.Options options=new BitmapFactory.Options();
					options.inSampleSize=16;
					icon.setImageBitmap(BitmapFactory.decodeFile(article.share_path, options));*/
					}
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	class Holder{
		LinearLayout iv1;
		LinearLayout iv2;
		LinearLayout iv3;
		
	}

}
