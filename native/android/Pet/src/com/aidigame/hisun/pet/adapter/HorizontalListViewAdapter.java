package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;

public class HorizontalListViewAdapter extends BaseAdapter {
	Context context;
	ArrayList<String> path;
	ArrayList<String> pictureName;
	public HorizontalListViewAdapter(Context context,ArrayList<String> path,ArrayList<String> pictureName){
		this.context=context;
		this.path=path;
		this.pictureName=pictureName;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return path.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return path.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_horizontal_listview, null);
			holder=new Holder();
			holder.imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.textView=(TextView)convertView.findViewById(R.id.textView1);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		Bitmap bmp=BitmapFactory.decodeFile(path.get(position),options);
		holder.imageView.setImageBitmap(bmp);
		bmp=null;
		holder.textView.setText(pictureName.get(position));
		return convertView;
	}
	class Holder{
		ImageView imageView;
		TextView textView;
	}

}
