package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aidigame.hisun.pet.R;

public class GridViewAdapter extends BaseAdapter {
    Context context;
    List<File> list;
	
	public GridViewAdapter(Context context,List<File> list){
		this.context=context;
		if(list!=null){
			this.list=list;
		}else{
			this.list=new ArrayList<File>();
		}
		
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
	    	convertView=LayoutInflater.from(context).inflate(R.layout.item_gridview,null);
	    	holder.imageView=(ImageView)convertView.findViewById(R.id.imageView1);
	    	convertView.setTag(holder);
	    }else{
	    	holder=(Holder)convertView.getTag();
	    }
	    BitmapFactory.Options options=new BitmapFactory.Options();
	    options.inSampleSize=8;
		holder.imageView.setImageBitmap(BitmapFactory.decodeFile(list.get(position).getAbsolutePath(),options));
		return convertView;
	}
	class Holder{
		ImageView imageView;
	}

}
