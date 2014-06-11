package com.aidigame.hisun.pet.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.OtherUserTopicActivity;
import com.aidigame.hisun.pet.ui.ShowTopicActivity;
import com.aidigame.hisun.pet.ui.UserHomepageActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.CircleView;
import com.aidigame.hisun.pet.view.TopicView;

public class ShowTopicsAdapter extends BaseAdapter  {
	Activity context;
	ArrayList<UserImagesJson.Data> datas;
	public ShowTopicsAdapter(Context context,ArrayList<UserImagesJson.Data> datas){
		this.context=(Activity)context;
		this.datas=datas;
	}
	public void updateTopics(ArrayList<UserImagesJson.Data> datas){
		this.datas=datas;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
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
	    	holder.icon=(CircleView)convertView.findViewById(R.id.imageView1);
//	    	holder.image=(ImageView)convertView.findViewById(R.id.imageView2);
	    	holder.image=(TopicView)convertView.findViewById(R.id.imageView2);
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
	    UserImagesJson.Data data=datas.get(position);
	    //TODO  35��ǰ   1Сʱǰ    1�� ǰ
	    holder.lLayout.setClickable(true);
	    holder.rLayout.setClickable(true);
	    holder.lLayout.setOnClickListener(new ItemClickListener(holder, position, 2));
	    holder.rLayout.setOnClickListener(new ItemClickListener(holder, position, 1));
	    holder.image.setOnClickListener(new ItemClickListener(holder, position, 3));
	    if(data.user!=null){
	    	holder.tv1.setText(data.user.nickName);
		    holder.tv2.setText(data.user.race);
		    holder.tv3.setText(""+judgeTime(data.create_time));
		    holder.tv4.setText(data.comment);
		    holder.tv5.setText(""+data.likes);
		    BitmapFactory.Options options=new BitmapFactory.Options();
		    options.inSampleSize=8;
		    if(data.user.iconPath!=null){
		    	holder.icon.setImageBitmap(BitmapFactory.decodeFile(data.user.iconPath,options));
		    }else{
		    	holder.icon.setImageBitmap(BitmapFactory.decodeFile(Constants.user.iconPath,options));
		    }
		    	
	    }else{
		    holder.tv1.setText(Constants.user.nickName);
		    holder.tv2.setText(Constants.user.race);
		    holder.tv3.setText(""+judgeTime(data.create_time));
		    holder.tv4.setText(data.comment);
		    holder.tv5.setText(""+data.likes);
		    
		    	holder.icon.setImageBitmap(BitmapFactory.decodeFile(Constants.user.iconPath));
	    }

		   
		    if(data.path==null){
		    	
		    }else{
		    	 BitmapFactory.Options options=new BitmapFactory.Options();
				    options.inSampleSize=4;
				    LogUtil.i("me", "tv1:t1.getwidth="+holder.tv1.getWidth()+"tv1.getheight="+holder.tv1.getHeight()+",tv5:width="+holder.tv5.getWidth()+",height="+holder.tv5.getHeight());
				    holder.image.setImageBitmap(data.path,context);
		    }

		return convertView;
	}
	class Holder{
		CircleView icon;
		TopicView image;
		TextView tv1,tv2,tv3,tv4,tv5;
		RelativeLayout rLayout;
		LinearLayout lLayout;
	}
	public String judgeTime(long time2){
		long time1=System.currentTimeMillis();
		long time=time1-time2;
		/*int[] oldTimes=getTimeArray(time);
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

		/*if(time/(1000)<60){
			return ""+time/(1000)+"秒前";
		}else if(time/(1000*60)<60){
			return ""+time/(1000*60)+"分前";
		}else if(time/(1000*60*60)<24){
			return ""+time/(1000*60*60)+"个小时前";
		}else if(time/(1000*60*60*24)<30){
			return ""+time/(1000*60*60*24)+"天前";
		}else if(time/(1000*60*60*24*30)<12){
			return ""+time/(1000*60*60*24)+"个月前";
		}else if(time/(1000*60*60*24*30*12)<1000){
			return ""+time/(1000*60*60*24*30*12)+"年前";
		}*/
		return "";
	}
	public int[] getTimeArray(long time){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",Locale.ENGLISH);
		String temp=sdf.format(new Date(time));
		String[] strs=temp.split("-");
		int[] times=new int[strs.length];
		for(int i=0;i<strs.length;i++){
			times[i]=Integer.parseInt(strs[i]);
		}
		return times;
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
				if(datas.get(position).user==null||datas.get(position).usr_id==Constants.user.userId){
					Intent intent1=new Intent(context,UserHomepageActivity.class);
					context.startActivity(intent1);
				}else{
					Intent intent1=new Intent(context,OtherUserTopicActivity.class);
					intent1.putExtra("data", datas.get(position));
					context.startActivity(intent1);
				}

				break;
			case 2:
				final UserImagesJson.Data data=datas.get(position);
				data.likes+=1;
				holder.tv5.setText(""+data.likes);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.likeImage(data);
					}
				}).start();
				break;
			case 3:
				Intent intent3=new Intent(context,ShowTopicActivity.class);
				intent3.putExtra("data", datas.get(position));
				context.startActivity(intent3);
				context.finish();
				break;
			}
		}
		
	}

}
