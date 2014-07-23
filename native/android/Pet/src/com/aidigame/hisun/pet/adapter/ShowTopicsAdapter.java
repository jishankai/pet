package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.OtherUserTopicActivity;
import com.aidigame.hisun.pet.ui.ShowTopicActivity;
import com.aidigame.hisun.pet.ui.UserHomepageActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.CircleView;
import com.aidigame.hisun.pet.view.TopicView;
import com.aidigame.hisun.pet.widget.ShowDialog;

public class ShowTopicsAdapter extends BaseAdapter  {
	Activity context;
	ArrayList<UserImagesJson.Data> datas;
	int mode;//1 个人主页和其他人主页 不需要用户信息，2 关注列表
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
	public ShowTopicsAdapter(Context context,ArrayList<UserImagesJson.Data> datas){
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
	    	convertView=LayoutInflater.from(context).inflate(R.layout.item_listview_topic, null);
	    	holder.icon=(CircleView)convertView.findViewById(R.id.imageView1);
	    	holder.heart=(ImageView)convertView.findViewById(R.id.imageView3);
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
	    final UserImagesJson.Data data=datas.get(position);
	    //TODO  35��ǰ   1Сʱǰ    1�� ǰ
	    holder.lLayout.setClickable(true);
	    holder.rLayout.setClickable(true);
	    holder.lLayout.setOnClickListener(new ItemClickListener(holder, position, 2));
	    holder.rLayout.setOnClickListener(new ItemClickListener(holder, position, 1));
	    holder.image.setOnClickListener(new ItemClickListener(holder, position, 3));
	    if(mode==1){
	    	
	    }
	    if(data.path!=null){
			   if(!StringUtil.judgeImageExists(data.path)){
			    	
			    }else{
					    LogUtil.i("me", "tv1:t1.getwidth="+holder.tv1.getWidth()+"tv1.getheight="+holder.tv1.getHeight()+",tv5:width="+holder.tv5.getWidth()+",height="+holder.tv5.getHeight());
					    holder.image.setImageBitmap(data.path,context);
			    } 
		   }
	    if(data.likersString!=null&&data.likersString.contains(""+Constants.user.userId)){
			holder.heart.setImageResource(R.drawable.heart_red);
		}else{
			holder.heart.setImageResource(R.drawable.heart_white);
		}
	    if(data.user!=null){
	    	holder.tv1.setText(""+data.user.nickName);
		    holder.tv3.setText(""+judgeTime(data.create_time));
		    holder.tv4.setText(data.comment);
		    holder.tv5.setText(""+data.likes);
		    BitmapFactory.Options options=new BitmapFactory.Options();
		    options.inSampleSize=8;
		    options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
		    if(data.user.iconPath!=null){
		    	holder.icon.setImageBitmap(BitmapFactory.decodeFile(data.user.iconPath,options));
		    }else{
		    	
		    }
		    final SharedPreferences sp=context.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
			   final TextView t= holder.tv2;
			   LogUtil.i("exception", "data.user.race="+(data.user==null));
			    if( sp.contains(data.user.race)){
				  t.setText(""+sp.getString(data.user.race, ""));
			   }else{
				   new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(Integer.parseInt(data.user.race)>301)return;
						boolean flag=HttpUtil.getRaceType(context);
						if(flag){
	                           context.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									t.setText(sp.getString(data.user.race, ""));
								}
							});
						}
					}
				}).start();
		   }
		    
		    holder.tv3.setText(""+judgeTime(data.create_time));
		    holder.tv4.setText(data.comment);
		    holder.tv5.setText(""+data.likes);

		    	
	    }else{
//		    holder.tv1.setText(""+Constants.user.nickName);
		   
	    }

		 
		   

		return convertView;
	}
	class Holder{
		CircleView icon;
		TopicView image;
		ImageView heart;
		TextView tv1,tv2,tv3,tv4,tv5;
		RelativeLayout rLayout;
		LinearLayout lLayout;
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
					if(context instanceof HomeActivity)
						intent1.putExtra("from", "HomeActivity");
					context.startActivity(intent1);
				}else{
					Intent intent1=new Intent(context,OtherUserTopicActivity.class);
					intent1.putExtra("data", datas.get(position));
					if(context instanceof HomeActivity)
						intent1.putExtra("from", "HomeActivity");
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
						HttpUtil.likeImage(datas.get(position),tv,handler);
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
								ShowTopicActivity.datas=datas;
								Intent intent3=new Intent(context,ShowTopicActivity.class);
								intent3.putExtra("data", datas.get(position));
								intent3.putExtra("from", "focus_list");
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
