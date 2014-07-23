package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.view.CircleView;

public class ShowTopicHorizontalAdapter extends BaseAdapter {
	Activity context;
	ArrayList<String>urls;
	public ShowTopicHorizontalAdapter(Activity context,ArrayList<String> urls){
		this.context=context;
		this.urls=urls;
	}
    public void updateAdapter(ArrayList<String> urls){
    	this.urls=urls;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return urls.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_show_topic_listview,null);
			holder=new Holder();
			holder.circleView=(CircleView)convertView.findViewById(R.id.item_circleView);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		String path=Constants.Picture_ICON_Path+File.separator+urls.get(position);
		final String url=urls.get(position);
		final CircleView view=holder.circleView;
		if(new File(path).exists()){
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=8;
			holder.circleView.setImageBitmap(BitmapFactory.decodeFile(path,options));
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final String path=HttpUtil.downloadIconImage(Constants.USER_DOWNLOAD_TX, url, null,context);
					if(path!=null){
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								BitmapFactory.Options options=new BitmapFactory.Options();
								options.inSampleSize=8;
								view.setImageBitmap(BitmapFactory.decodeFile(path,options));
							}
						});
					}
				}
			}).start();
		}
		return convertView;
	}
	class Holder{
		CircleView circleView;
	}

}
