package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.ui.OtherUserTopicActivity;
import com.aidigame.hisun.pet.ui.ShowTopicActivity;
import com.aidigame.hisun.pet.util.LogUtil;

public class ShowTopicsAdapter extends BaseAdapter  {
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
	    	holder.rLayout=(RelativeLayout)convertView.findViewById(R.id.relativeLayout1);
	    	holder.lLayout=(LinearLayout)convertView.findViewById(R.id.heart_linearLayout);
	    	convertView.setTag(holder);
	    }else{
	    	holder=(Holder)convertView.getTag();
	    }
	    Topic topic=topics.get(position);
	    //TODO  35��ǰ   1Сʱǰ    1�� ǰ
	    holder.lLayout.setClickable(true);
	    holder.rLayout.setClickable(true);
	    holder.lLayout.setOnClickListener(new ItemClickListener(holder, position, 2));
	    holder.rLayout.setOnClickListener(new ItemClickListener(holder, position, 1));
	    holder.image.setOnClickListener(new ItemClickListener(holder, position, 3));
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
		RelativeLayout rLayout;
		LinearLayout lLayout;
	}
	class ItemClickListener implements OnClickListener{
        Holder holder;
        int position; 
        int type;//1 �û���Ϣ��2 ���ޣ�3 �鿴topic
        public ItemClickListener(Holder holder,int position,int type){
        	this.holder=holder;
        	this.position=position;
        	this.type=type;
        }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LogUtil.i("me", "type="+type);
			switch (type) {
			case 1:
				Intent intent1=new Intent(context,OtherUserTopicActivity.class);
				intent1.putExtra("topic", topics.get(position));
				context.startActivity(intent1);
				break;
			case 2:
				holder.tv5.setText(""+(Integer.parseInt(""+holder.tv5.getText())+1));
				break;
			case 3:
				Intent intent3=new Intent(context,ShowTopicActivity.class);
				intent3.putExtra("topic", topics.get(position));
				context.startActivity(intent3);
				break;
			}
		}
		
	}

}
