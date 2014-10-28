package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * PopuWindow 中ListView  显示种族列表
 * @author admin
 *
 */
public class PopularWindowAdapter extends BaseAdapter {
    Context context;
   String[] list;
	
	public PopularWindowAdapter(Context context,String[] list){
		this.list=list;
		this.context=context;
	}
	public void update(String[] list){
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list[position];
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_popup_popular_list, null);
			holder=new Holder();
			holder.tv=(TextView)convertView.findViewById(R.id.textView1);
			holder.line=convertView.findViewById(R.id.line);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		/*if(position==0){
			holder.line.setVisibility(View.GONE);
		}else{
			holder.line.setVisibility(View.VISIBLE);
		}*/
		holder.tv.setText(""+list[position]);
		return convertView;
	}
	class Holder{
		TextView tv;
		View line;
	}

}
