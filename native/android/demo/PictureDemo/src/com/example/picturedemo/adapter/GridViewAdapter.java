package com.example.picturedemo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.picturedemo.R;
import com.example.picturedemo.bean.Picture;

public class GridViewAdapter extends BaseAdapter {
	Context context;
	ArrayList<Picture> list;
	LayoutInflater inflater;
	public GridViewAdapter(Context context,ArrayList<Picture> list){
		this.context=context;
		this.list=list;
		inflater=LayoutInflater.from(context);
	}
	public void update(ArrayList<Picture> list){
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
			convertView=inflater.inflate(R.layout.item_gridview, null);
			holder=new Holder();
			holder.imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		String path=list.get(position).path;
		Bitmap bitmap=null;
		BitmapFactory.Options option=new BitmapFactory.Options();
		option.inJustDecodeBounds=false;
		option.inSampleSize=4;
		bitmap=BitmapFactory.decodeFile(path, option);
		holder.imageView.setImageBitmap(bitmap);
		bitmap=null;
		return convertView;
	}
	class Holder{
		public ImageView imageView;
	}

}
