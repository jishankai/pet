package com.aidigame.hisun.imengstar.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.aidigame.hisun.imengstar.bean.Gift;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.util.ImageUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
import android.widget.TextView;
/**
 * 用户礼物栏列表
 * @author admin
 *
 */
public class UserGiftGridViewAdapter extends BaseAdapter {
	Context context;
	ArrayList<Gift> list;
    public UserGiftGridViewAdapter(Context context,ArrayList<Gift> list){
    	this.context=context;
    	this.list=list;
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
    public void update(ArrayList<Gift> list){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_user_gift_box_gridview, null);
			holder=new Holder();
			holder.addLikeTV=(TextView)convertView.findViewById(R.id.textView4);
			holder.giftNameTV=(TextView)convertView.findViewById(R.id.textView1);
			holder.giftNumTV=(TextView)convertView.findViewById(R.id.textView2);
			holder.giftIV=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.boxLayout=(RelativeLayout)convertView.findViewById(R.id.box_layout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		Gift gift=list.get(position);
		loadImage(holder.giftIV, gift);
		/*try {
			holder.giftIV.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if(gift.add_rq>0){
			holder.addLikeTV.setText("+ "+gift.add_rq);
			holder.boxLayout.setBackgroundResource(R.drawable.gift_box_background);
		}else{
			holder.addLikeTV.setText("- "+(-gift.add_rq));
			holder.boxLayout.setBackgroundResource(R.drawable.gift_box_background_subtract);
		}
		
		holder.giftNameTV.setText(gift.name);
		holder.giftNumTV.setText(""+gift.boughtNum);
		return convertView;
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
		TextView addLikeTV;
		TextView giftNameTV;
		TextView giftNumTV;
		ImageView giftIV;
	    RelativeLayout boxLayout;
	}

}
