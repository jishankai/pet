package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.http.json.UserImagesJson;

import android.content.Context;
import android.provider.ContactsContract.Contacts.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentListViewAdapter extends BaseAdapter{
	Context context;
	ArrayList<UserImagesJson.Comments> listComment;
	public CommentListViewAdapter(Context context,ArrayList<UserImagesJson.Comments> listComment){
		this.context=context;
		this.listComment=listComment;
	}
	public void update(ArrayList<UserImagesJson.Comments> listComment){
		this.listComment=listComment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listComment.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listComment.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_comment_listview, null);
			holder.nameTextView=(TextView)convertView.findViewById(R.id.textView1);
			holder.body=(TextView)convertView.findViewById(R.id.textView2);
			holder.time=(TextView)convertView.findViewById(R.id.textView3);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		UserImagesJson.Comments comments=listComment.get(position);
		holder.nameTextView.setText(""+(comments.name==null?"":comments.name)+":");
		holder.body.setText(""+(comments.body==null?"":comments.body));
		holder.time.setText(""+judgeTime(comments.create_time));
		return convertView;
	}
	class Holder{
		TextView nameTextView;
		TextView body;
		TextView time;
	}
	public String judgeTime(long time2){
		long time1=System.currentTimeMillis()/1000;
		long time=time1-time2;
		/*int[] oldTimes=getTimeArray(time2*1000);
		int[] nowTimes=getTimeArray(time1);
		for(int i=0;i<oldTimes.length;i++){
			int t=0;
			switch (i) {
			case 0:
				t=nowTimes[0]-oldTimes[0];
				if(t<=0){
					continue;
				}else{
					return t+"年前";
				}	
			case 1:
				t=nowTimes[1]-oldTimes[1];
				if(t<=0){
					continue;
				}else{
					return t+"个月前";
				}	
			case 2:
				t=nowTimes[2]-oldTimes[2];
				if(t<=0){
					continue;
				}else{
					return t+"天前";
				}	
			case 3:
				t=nowTimes[3]-oldTimes[3];
				if(t<=0){
					continue;
				}else{
					return t+"个小时前";
				}	
			case 4:
				t=nowTimes[4]-oldTimes[4];
				if(t<=0){
					continue;
				}else{
					return t+"分钟前";
				}	
			case 5:
				t=nowTimes[5]-oldTimes[5];
				if(t<0){
					continue;
				}else{
					return t+"秒前";
				}
				
			}
		}*/
         String str="";
         StringBuffer sb=new StringBuffer();
         sb.append("");
         int mode=0;
         if(time<0){
        	 time=-time;
        	 mode=1;
        	 sb.append("未来");
         }
		if(time<60){
			sb.append( str+time+"秒");
		}else if(time/(60)<60){
			sb.append( str+time/(60)+"分");
		}else if(time/(60*60)<24){
			sb.append(  str+time/(60*60)+"个小时");
		}else if(time/(60*60*24)<30){
			sb.append(  str+time/(60*60*24)+"天");
		}else if(time/(60*60*24*30)<12){
			sb.append(  str+time/(60*60*24)+"个月");
		}else if(time/(60*60*24*30*12)<1000){
			sb.append( str+time/(60*60*24*30*12)+"年");
		}
		if(mode==0){
			sb.append("前");
		}else{
			sb.append("后");
		}
		return sb.toString();
	}

}
