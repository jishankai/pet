package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.ui.ExchangeActivity;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class GalleryAdapter extends BaseAdapter {
	ArrayList<Animal> animals;
	Context context;
	BitmapFactory.Options options;
	DisplayImageOptions displayImageOptions1;
	ClickViewListener listener;
	public GalleryAdapter(Context context,ArrayList<Animal> animals){
		this.context=context;
		this.animals=animals;
options=new BitmapFactory.Options();
		
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions1=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
	public void update(ArrayList<Animal> animals){
		this.animals=animals;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return animals.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return animals.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_gallery_view, null);
			holder.iv=(RoundImageView)convertView.findViewById(R.id.roundImage_one_border);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		ImageLoader imageLoader1=ImageLoader.getInstance();
		imageLoader1.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animals.get(position).pet_iconUrl, holder.iv, displayImageOptions1);
		holder.iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener!=null){
					listener.onClick(position);
				}
				if(ExchangeActivity.exchangeActivity!=null){
				   
				    ExchangeActivity.exchangeActivity.hideIcons(true);
				}
			}
		});
		return convertView;
	}
	class Holder{
		RoundImageView iv;
	}
	public void setClickViewListener(ClickViewListener clickViewListener){
		this.listener=clickViewListener;
	}
	public static interface ClickViewListener{
		void onClick(int position);
	}

}
