package com.aidigame.hisun.pet.adapter;

import java.io.IOException;
import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.TopicView;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;

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
		try {
			giftIV.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	class Holder{
		LinearLayout iv1;
		LinearLayout iv2;
		LinearLayout iv3;
		
	}

}
