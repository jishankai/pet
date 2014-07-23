package com.aidigame.hisun.pet.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.OtherUserTopicActivity;
import com.aidigame.hisun.pet.ui.ShowTopicActivity;
import com.aidigame.hisun.pet.ui.UserHomepageActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.TopicView;
import com.aidigame.hisun.pet.widget.ShowDialog;

public class ShowTopicsAdapter2 extends BaseAdapter  {
	Activity context;
	ArrayList<UserImagesJson.Data> datas;
	int mode;//1 个人主页;2其他人主页 
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==Constants.ERROR_MESSAGE){
				ShowDialog.show((String)msg.obj, context);
			}else{
				TextView tv=(TextView)msg.obj;
				tv.setText(""+msg.arg1);
				notifyDataSetChanged();
			}

		};
	};
	public ShowTopicsAdapter2(Context context,ArrayList<UserImagesJson.Data> datas,int mode){
		this.context=(Activity)context;
		this.datas=datas;
		this.mode=mode;
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
	    	convertView=LayoutInflater.from(context).inflate(R.layout.item_listview_topic1, null);
	    	holder.image=(TopicView)convertView.findViewById(R.id.imageView2);
	    	holder.heart=(ImageView)convertView.findViewById(R.id.imageView3);
	    	holder.tv4=(TextView)convertView.findViewById(R.id.textView4);
	    	holder.tv5=(TextView)convertView.findViewById(R.id.textView5);
	    	holder.lLayout=(LinearLayout)convertView.findViewById(R.id.heart_linearLayout);
	    	convertView.setTag(holder);
	    }else{
	    	holder=(Holder)convertView.getTag();
	    }
	    UserImagesJson.Data data=datas.get(position);
	    //TODO  
	    if(data.likersString!=null&&data.likersString.contains(""+Constants.user.userId)){
			holder.heart.setImageResource(R.drawable.heart_white);
		}else{
			holder.heart.setImageResource(R.drawable.heart_red);
		}
		    if(data.path==null){
		    	convertView.setVisibility(View.GONE);
		    }else{
		    	convertView.setVisibility(View.VISIBLE);
				    holder.image.setImageBitmap(data.path,context);
				    holder.lLayout.setClickable(true);
				    holder.lLayout.setOnClickListener(new ItemClickListener(holder, position, 2));
				    holder.image.setOnClickListener(new ItemClickListener(holder, position, 3));
				    if(data.user!=null){
					    holder.tv4.setText(""+judgeTime(data.create_time));
					    holder.tv5.setText(""+data.likes);
					    	
				    }else{
					    holder.tv4.setText(""+judgeTime(data.create_time));
					    holder.tv5.setText(""+data.likes);
					    
					    	
				    }
		    }

		return convertView;
	}
	class Holder{
		TopicView image;
		ImageView heart;
		TextView tv4,tv5;
		LinearLayout lLayout;
	}
	public String judgeTime(long time2){
		long time1=System.currentTimeMillis()/1000;
		long time=time1-time2;
		/*int[] oldTimes=getTimeArray(time2*1000);
		int[] nowTimes=getTimeArray(time1);*/
		/*for(int i=0;i<oldTimes.length;i++){
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
	public int[] getTimeArray(long time){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
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
//                context.finish();
				break;
			case 2:
				final UserImagesJson.Data data=datas.get(position);
				final TextView tv=holder.tv5;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.likeImage(data,tv,handler);
					}
				}).start();
				break;
			case 3:
               new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.imageInfo(datas.get(position),null,context);
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Intent intent3=new Intent(context,ShowTopicActivity.class);
								intent3.putExtra("data", datas.get(position));
								if(mode==1){
									intent3.putExtra("from", "UserHomepageActivity");
								}else{
									intent3.putExtra("from", "OtherUserTopicActivity");
									if(ShowTopicActivity.showTopicActivity!=null)
										ShowTopicActivity.showTopicActivity.finish();
								}
								ShowTopicActivity.datas=datas;
								context.startActivity(intent3);
							}
						});
					}
				}).start();
				
//				context.finish();
				break;
			}
		}
		
	}

}
