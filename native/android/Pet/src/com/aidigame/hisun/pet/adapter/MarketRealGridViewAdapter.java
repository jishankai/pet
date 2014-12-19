package com.aidigame.hisun.pet.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.PSource;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ToneGenerator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
/**
 * 商城 现实礼物列表
 * @author admin
 *
 */
public class MarketRealGridViewAdapter extends BaseAdapter {
	Activity context;
	List<Gift> list;
	ImageFetcher mImageFetcher;
    public MarketRealGridViewAdapter(Activity context,List<Gift> list){
    	this.context=context;
    	this.list=list;
    	 mImageFetcher = new ImageFetcher(context, Constants.screen_width);
    }
    public void updateList(List<Gift> list){
    	this.list=list;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_market_real_gridview, null);
			holder=new Holder();
			holder.addlike=(TextView)convertView.findViewById(R.id.textView4);
			holder.name=(TextView)convertView.findViewById(R.id.textView1);
			holder.price=(TextView)convertView.findViewById(R.id.textView2);
			holder.status=(TextView)convertView.findViewById(R.id.textView5);
			holder.imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.boxLayout=(RelativeLayout)convertView.findViewById(R.id.box_layout);
			convertView.setTag(holder);	
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.name.setText(list.get(position).name);
		holder.price.setText(""+list.get(position).price);
//		holder.addlike.setText("+"+list.get(position).add_rq);
//		holder.status.setText(list.get(position).status);
		
		displayImage(holder.imageView,list.get(position).smallImage);
		
		/*try {
			holder.imageView.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+list.get(position).no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};*/
		return convertView;
	}
	private void displayImage(ImageView imageview,String url) {
		// TODO Auto-generated method stub
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=1;
		LogUtil.i("me", "照片详情页面Topic图片缩放比例"+StringUtil.topicImageGetScaleByDPI(context));
	/*	if(StringUtil.topicImageGetScaleByDPI(context)>=2){
			options.inPreferredConfig=Bitmap.Config.ARGB_4444;
		}else{
			options.inPreferredConfig=Bitmap.Config.ARGB_8888;
		}*/
		
		options.inPurgeable=true;
		options.inInputShareable=true;
		mImageFetcher.itemUrl="item/";
		mImageFetcher.setWidth((Constants.screen_width-context.getResources().getDimensionPixelSize(R.dimen.one_dip)*70)/2);
		mImageFetcher.setImageCache(new ImageCache(context, new ImageCacheParams(url)));
		
		mImageFetcher.loadImage(url, imageview,options);
	}
	class Holder{
		TextView name,addlike,status,price;
		ImageView imageView;
		RelativeLayout boxLayout;
	}

}
