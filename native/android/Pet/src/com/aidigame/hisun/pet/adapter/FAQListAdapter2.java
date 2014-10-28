package com.aidigame.hisun.pet.adapter;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.R.array;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FAQListAdapter2 extends BaseAdapter {
	String[] strArray1;
	String[] strArray2;
	Context context;
	public FAQListAdapter2(String[] strArray1,String[] strArray2,Context context){
		this.strArray1=strArray1;
		this.strArray2=strArray2;
		this.context=context;
	}
	public void update(String[] strArray1,String[] strArray2){
		this.strArray1=strArray1;
		this.strArray2=strArray2;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strArray1.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return strArray1[position];
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_faqlist_2, null);
			holder=new Holder();
			holder.tView1=(TextView)convertView.findViewById(R.id.textView1);
			holder.tView2=(TextView)convertView.findViewById(R.id.textView2);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.tView1.setText(""+strArray1[position]);
		holder.tView2.setText(""+strArray2[position]);
		return convertView;
	}
	class Holder{
		TextView tView1;
		TextView tView2;
	}

}
