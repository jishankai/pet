package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.ui.TopicListActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 话题列表
 * @author admin
 *
 */
public class TopicListAdapter extends BaseAdapter {
	Context context;
	ArrayList<Topic> list;
	public TopicListAdapter(Context context,ArrayList<Topic> list){
		this.context=context;
		this.list=list;
	}
	public void updateList(ArrayList<Topic> list){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_topic_list, null);
			holder=new Holder();
			holder.flagIV=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.nameTV=(TextView)convertView.findViewById(R.id.textView1);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.nameTV.setText(list.get(position).topic);
		if(list.get(position).topic_id!=-1){
			holder.flagIV.setVisibility(View.VISIBLE);
		}else{
			holder.flagIV.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	class Holder{
		TextView nameTV;
		ImageView flagIV;
	}

}
