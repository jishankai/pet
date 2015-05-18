package com.aidigame.hisun.imengstar.adapter;

import java.io.File;
import java.util.ArrayList;

import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.ui.NewShowTopicActivity;
import com.aidigame.hisun.imengstar.ui.RecommendActivity;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.R;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecommendGridPictureAdapter extends BaseAdapter {
	Activity context;
	ArrayList<PetPicture> pictures;
	private ClickRecommendListener listener;
	private long star_id;
	private int gold;
	private String star_title;
	private long end_time;
	public RecommendGridPictureAdapter(Activity context,ArrayList<PetPicture> petPictures,long star_id,int gold,String star_title,long end_time){
		this.context=context;
		this.pictures=petPictures;
		this.star_id=star_id;
		this.gold=gold;
		this.star_title=star_title;
		this.end_time=end_time;
	}
	public void update(ArrayList<PetPicture> pictures){
		this.pictures=pictures;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int a=pictures.size()%3;
		if(a==0){
			return pictures.size()/3;
		}else{
			return pictures.size()/3+1;
		}
//		return pictures.size();
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_recommend_grid_picture, null);
			holder=new Holder();
			holder.iv1=(ImageView)convertView.findViewById(R.id.iv1);
			holder.iv2=(ImageView)convertView.findViewById(R.id.iv2);
			holder.iv3=(ImageView)convertView.findViewById(R.id.iv3);
			holder.iv1_layout=(LinearLayout)convertView.findViewById(R.id.iv1_layout);
			holder.iv2_layout=(LinearLayout)convertView.findViewById(R.id.iv2_layout);
			holder.iv3_layout=(LinearLayout)convertView.findViewById(R.id.iv3_layout);
			holder.tv1=(TextView)convertView.findViewById(R.id.tv1);
			holder.tv2=(TextView)convertView.findViewById(R.id.tv2);
			holder.tv3=(TextView)convertView.findViewById(R.id.tv3);
			holder.pets_layout=(LinearLayout)convertView.findViewById(R.id.pets_layout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		if(position==0){
			holder.pets_layout.setVisibility(View.VISIBLE);
			if(listener!=null){
				listener.showPetsList(holder.pets_layout);
			}
		}else{
			holder.pets_layout.setVisibility(View.GONE);
		}
		holder.iv1.setVisibility(View.GONE);
		holder.iv2.setVisibility(View.GONE);
		holder.iv3.setVisibility(View.GONE);
		holder.iv1_layout.setVisibility(View.GONE);
		holder.iv2_layout.setVisibility(View.GONE);
		holder.iv3_layout.setVisibility(View.GONE);
		
		final int index=position*3;
		if(index<pictures.size()){
			if(!StringUtil.isEmpty(pictures.get(index).url)){
			loadTopicImage(holder.iv1, pictures.get(index));
			holder.iv1_layout.setVisibility(View.VISIBLE);
			holder.tv1.setText(""+pictures.get(index).stars);
			final TextView tv1=holder.tv1;
			holder.iv1_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(listener!=null){
						listener.clickRecommend(tv1,pictures.get(index));
					}
				}
			});
			}else{
				holder.iv1_layout.setVisibility(View.GONE);
			}
			
		}
		if((index+1)<pictures.size()){
			loadTopicImage(holder.iv2, pictures.get(index+1));
			holder.iv2_layout.setVisibility(View.VISIBLE);
			holder.iv1_layout.setVisibility(View.VISIBLE);
			holder.tv2.setText(""+pictures.get(index+1).stars);
			final TextView tv2=holder.tv2;
			holder.iv2_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(listener!=null){
						listener.clickRecommend(tv2,pictures.get(index+1));
					}
				}
			});
			
		}
		if((index+2)<pictures.size()){
			loadTopicImage(holder.iv3, pictures.get(index+2));
			holder.iv3_layout.setVisibility(View.VISIBLE);
			holder.iv1_layout.setVisibility(View.VISIBLE);
			holder.tv3.setText(""+pictures.get(index+2).stars);
			final TextView tv3=holder.tv3;
			holder.iv3_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(listener!=null){
						listener.clickRecommend(tv3,pictures.get(index+2));
					}
				}
			});
		}
		
		return convertView;
	}
	public void setClickRecommendListener(ClickRecommendListener listenenr){
		this.listener=listenenr;
	}
	public void loadTopicImage(final ImageView topic,final PetPicture data){
		topic.setVisibility(View.VISIBLE);
		
		topic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(System.currentTimeMillis()/1000>end_time){
					if(NewShowTopicActivity.newShowTopicActivity!=null){
						NewShowTopicActivity.newShowTopicActivity.recyle();
					}
					Intent intent=new Intent(context,NewShowTopicActivity.class);
				
					intent.putExtra("PetPicture",data);
//					intent.putExtra("mode", flowTag.mode);
					context.startActivity(intent);
				}else{
				
				
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				Intent intent=new Intent(context,RecommendActivity.class);
				intent.putExtra("PetPicture", data);
				intent.putExtra("star_id", star_id);
				intent.putExtra("gold", gold);
				intent.putExtra("star_title", star_title);
				context.startActivity(intent);
				}
			}
		});
		
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=StringUtil.getScaleByDPIget4(context,data.url);
		LogUtil.i("me", "缩放比例="+options.inSampleSize);
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
	    ImageFetcher mImageFetcher=new ImageFetcher(context, 0);
		mImageFetcher.setWidth(0);
		mImageFetcher.IP=mImageFetcher.UPLOAD_THUMBMAIL_IMAGE;
		mImageFetcher.setImageCache(new ImageCache(context, new ImageCacheParams(data.url+"@"+Constants.screen_width/3+"w_"+Constants.screen_width/3+"h_"+"0l.jpg")));
		mImageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
			
			@Override
			public void onComplete(Bitmap bitmap) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void getPath(String path) {
				// TODO Auto-generated method stub
				File f=new File(path);
				for(int i=0;i<pictures.size();i++){
					if(f.getName().contains(pictures.get(i).url)){
						pictures.get(i).petPicture_path=f.getName();
					}
				}
				
				
				RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)topic.getLayoutParams();
				if(param==null){
					param=new RelativeLayout.LayoutParams(0,0);
				}
				int w=(int)((Constants.screen_width-context.getResources().getDimensionPixelSize(R.dimen.one_dip)*5)*1f/3f);
				param.width=w;
				param.height=w;
				topic.setLayoutParams(param);
			}
		});
		
		LogUtil.i("me", "******************************loadImage******************"+data.url+"@"+Constants.screen_width/3+"w_0l_50q.jpg");
		Log.i("me", "adapter ******url="+data.url+"@"+Constants.screen_width/3+"w_0l_50q");
		mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/data.url+"@"+Constants.screen_width/3+"w_0l.jpg", topic, /*options*/null);
	}
	class Holder{
		ImageView iv1;
		ImageView iv2;
		ImageView iv3;
		LinearLayout iv1_layout,iv2_layout,iv3_layout,pets_layout;
		TextView tv1,tv2,tv3;
		
		
	}
    public static interface ClickRecommendListener{
    	void clickRecommend(TextView tv,PetPicture petPicture);
    	void showPetsList(LinearLayout  petsLayout);
    }
}
