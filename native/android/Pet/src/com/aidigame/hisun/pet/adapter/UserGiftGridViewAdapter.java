package com.aidigame.hisun.pet.adapter;

import java.io.IOException;
import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Gift;

import android.content.Context;
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
		try {
			holder.giftIV.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	class Holder{
		TextView addLikeTV;
		TextView giftNameTV;
		TextView giftNumTV;
		ImageView giftIV;
	    RelativeLayout boxLayout;
	}

}
