package com.aidigame.hisun.imengstar.adapter;

import java.util.ArrayList;

import com.aidigame.hisun.imengstar.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StringAdapter extends BaseAdapter {
	ArrayList<String> strList;
	Context context;
	public StringAdapter(Context context,ArrayList<String> strList ){
		this.context=context;
		this.strList=strList;
	}
	public void update(ArrayList<String> strList){
		this.strList=strList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return strList.get(position);
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
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_list_str, null);
			holder.tv=(TextView)convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.tv.setText(strList.get(position));
		
		return convertView;
	}
	class Holder{
		TextView tv;
	}

}
