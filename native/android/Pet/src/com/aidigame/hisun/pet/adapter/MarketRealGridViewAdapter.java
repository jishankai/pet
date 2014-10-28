package com.aidigame.hisun.pet.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.widget.fragment.MarketFragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ToneGenerator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 商城 现实礼物列表
 * @author admin
 *
 */
public class MarketRealGridViewAdapter extends BaseAdapter {
	Context context;
	List<Gift> list;
    public MarketRealGridViewAdapter(Context context,List<Gift> list){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_market_real_gridview, null);
			holder=new Holder();
			holder.addlike=(TextView)convertView.findViewById(R.id.textView4);
			holder.name=(TextView)convertView.findViewById(R.id.textView1);
			holder.price=(TextView)convertView.findViewById(R.id.textView2);
			holder.status=(TextView)convertView.findViewById(R.id.textView5);
			holder.imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);	
		}else{
			holder=(Holder)convertView.getTag();
		}
//		holder.name.setText(list.get(position).name);
		holder.price.setText(""+list.get(position).price);
		holder.addlike.setText("+"+list.get(position).add_rq);
		holder.status.setText(list.get(position).status);
		try {
			holder.imageView.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+list.get(position).no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return convertView;
	}
	class Holder{
		TextView name,addlike,status,price;
		ImageView imageView;
	}

}
