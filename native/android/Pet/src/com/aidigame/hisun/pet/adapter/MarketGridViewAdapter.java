package com.aidigame.hisun.pet.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.widget.fragment.MarketFragment;

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
 * 商城 虚拟礼物列表
 * @author admin
 *
 */
public class MarketGridViewAdapter extends BaseAdapter {
	Context context;
	List<Gift> list;
    public MarketGridViewAdapter(Context context,List<Gift> list){
    	this.context=context;
    	this.list=list;
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_market_gridview, null);
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
		if(list.get(position).add_rq>0){
			holder.addlike.setText("+ "+list.get(position).add_rq);
			holder.boxLayout.setBackgroundResource(R.drawable.gift_box_background);
		}else{
			holder.addlike.setText("- "+(-list.get(position).add_rq));
			holder.boxLayout.setBackgroundResource(R.drawable.gift_box_background_subtract);
		}
		
		if(StringUtil.isEmpty(list.get(position).status)){
			holder.status.setVisibility(View.INVISIBLE);
		}else{
			holder.status.setText(list.get(position).status);
		}
		
		try {
			holder.imageView.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+list.get(position).no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	class Holder{
		TextView name,addlike,status,price;
		ImageView imageView;
		RelativeLayout boxLayout;
	}

}
