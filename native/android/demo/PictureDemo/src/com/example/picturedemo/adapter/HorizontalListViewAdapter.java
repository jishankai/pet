package com.example.picturedemo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.example.picturedemo.R;
import com.example.picturedemo.util.EffectUtil;

public class HorizontalListViewAdapter extends BaseAdapter{
	Context context;
	ArrayList<String> list;
	ImageView imageView;
	public static boolean waterFlag=false;
	public HorizontalListViewAdapter(Context context,ImageView imageView){
		this.context=context;
		this.imageView=imageView;
		list=new ArrayList<String>();
		list.add("添加相框");
		list.add("涂鸦水印");
		list.add("怀旧效果");
		list.add("模糊效果");
		list.add("锐化效果");
		list.add("浮雕效果");
		list.add("底片效果");
		list.add("光照效果");
		list.add("图片叠加");
		list.add("光晕效果");
		list.add("条纹效果");
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
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_horizontal_listview, null);
			holder.button=(Button)convertView.findViewById(R.id.button1);
			holder.button.setTag(position);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		Button button=holder.button;
		button.setText(list.get(position));
		return convertView;
	}
	class Holder{
		public Button button;
	}
	

}
