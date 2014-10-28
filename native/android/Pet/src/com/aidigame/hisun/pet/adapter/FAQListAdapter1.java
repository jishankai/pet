package com.aidigame.hisun.pet.adapter;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.R.array;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FAQListAdapter1 extends BaseAdapter {
	String[] strArray;
	Context context;
	public FAQListAdapter1(String[] strArray,Context context){
		this.strArray=strArray;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strArray.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return strArray[position];
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_faq_list_1, null);
			holder=new Holder();
			holder.tView=(TextView)convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.tView.setText(""+strArray[position]);
		return convertView;
	}
	class Holder{
		TextView tView;
	}

}
