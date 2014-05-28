package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.util.LogUtil;

public class ShowTopicsAdapter extends BaseAdapter {
	Context context;
	ArrayList<Topic> topics;
	public ShowTopicsAdapter(Context context,ArrayList<Topic> topics){
		this.context=context;
		this.topics=topics;
	}
	public void updateTopics(ArrayList<Topic> topics){
		this.topics=topics;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return topics.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return topics.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder  holder=null;
	    if(convertView==null){
	    	holder=new Holder();
	    	convertView=LayoutInflater.from(context).inflate(R.layout.item_listview_topic, null);
	    	holder.icon=(ImageView)convertView.findViewById(R.id.imageView1);
	    	holder.image=(ImageView)convertView.findViewById(R.id.imageView2);
	    	holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
	    	holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
	    	holder.tv3=(TextView)convertView.findViewById(R.id.textView3);
	    	holder.tv4=(TextView)convertView.findViewById(R.id.textView4);
	    	holder.tv5=(TextView)convertView.findViewById(R.id.textView5);
	    	convertView.setTag(holder);
	    }else{
	    	holder=(Holder)convertView.getTag();
	    }
	    Topic topic=topics.get(position);
	    //TODO  35分前   1小时前    1天 前
	    holder.tv1.setText(topic.user.nickName);
	    holder.tv2.setText(topic.user.race);
	    holder.tv3.setText(""+topic.time+"分前");
	    holder.tv4.setText(topic.describe);
	    holder.tv5.setText(""+topic.likesNum);
//	    holder.icon.setImageBitmap(BitmapFactory.decodeFile(topic.user.iconPath));
	    BitmapFactory.Options options=new BitmapFactory.Options();
	    options.inSampleSize=4;
	    LogUtil.i("me", "tv1:t1.getwidth="+holder.tv1.getWidth()+"tv1.getheight="+holder.tv1.getHeight()+",tv5:width="+holder.tv5.getWidth()+",height="+holder.tv5.getHeight());
	    holder.image.setImageBitmap(BitmapFactory.decodeFile(topic.bmpPath,options));
		return convertView;
	}
	class Holder{
		ImageView icon;
		ImageView image;
		TextView tv1,tv2,tv3,tv4,tv5;
	}

}
