package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.view.CircleView;

public class FocusAndFansAdapter extends BaseAdapter {
	Context context;
	ArrayList<User> list;
	int mode;
	public FocusAndFansAdapter(Context context,ArrayList<User> list,int mode){
		this.context=context;
		this.list=list;
		this.mode=mode;
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_focus_listview, null);
			holder.icon=(CircleView)convertView.findViewById(R.id.icon_circleView);
			holder.name=(TextView)convertView.findViewById(R.id.textView2);
			holder.gender=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.race=(TextView)convertView.findViewById(R.id.textView3);
			holder.age=(TextView)convertView.findViewById(R.id.textView5);
			holder.add=(TextView)convertView.findViewById(R.id.textView1);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final User user=list.get(position);
//		holder.icon.setImageBitmap();
		if(user.gender==1){
			holder.gender.setImageResource(R.drawable.male);
		}else if(user.gender==2){
			holder.gender.setImageResource(R.drawable.female);
		}else{
//			holder.gender.setImageResource();
		}
		holder.name.setText(""+user.nickName);
		holder.race.setText(""+user.race);
		holder.age.setText(""+user.age);
		final TextView tv=holder.add;
		if(user.focus==0){
			holder.add.setBackgroundResource(R.color.white);
			holder.add.setTextColor(Color.BLACK);
			holder.add.setText(""+"取消关注");
		}else{
			holder.add.setBackgroundResource(R.color.green_light);
			holder.add.setTextColor(Color.WHITE);
			holder.add.setText(""+"关注");
		}
		holder.add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				user.focus=(user.focus+1)%2;
				if(user.focus==0){
					tv.setBackgroundResource(R.color.white);
					tv.setTextColor(Color.BLACK);
					tv.setText(""+"取消关注");
				}else{
					tv.setBackgroundResource(R.color.green_light);
					tv.setTextColor(Color.WHITE);
					tv.setText(""+"关注");
				}
			}
		});
		return convertView;
	}
	class Holder{
		CircleView icon;
		ImageView gender;
		TextView name;
		TextView race;
		TextView age;
		TextView add;
	}

}
