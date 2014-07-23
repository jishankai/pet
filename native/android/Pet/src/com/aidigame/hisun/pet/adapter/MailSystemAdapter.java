package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.MessagJson;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.CircleView;

public class MailSystemAdapter extends BaseAdapter {
	Context context;
	ArrayList<MessagJson.DataSystem> dataSystemss;
	public MailSystemAdapter(Context context,ArrayList<MessagJson.DataSystem> dataSystemss){
		this.context=context;
		this.dataSystemss=dataSystemss;
	}
	public void update(ArrayList<MessagJson.DataSystem> dataSystemss){
		this.dataSystemss=dataSystemss;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataSystemss.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataSystemss.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_mail_system, null);
			holder.circleView=(CircleView)convertView.findViewById(R.id.circleview);
			holder.gender=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.name=(TextView)convertView.findViewById(R.id.textView1);
			holder.body=(TextView)convertView.findViewById(R.id.textView2);
			holder.time=(TextView)convertView.findViewById(R.id.textView3);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final MessagJson.DataSystem dataSystem=dataSystemss.get(position);
		if(dataSystem.fromUser!=null){
			if(dataSystem.fromUser.gender==1){
				holder.gender.setImageResource(R.drawable.male1);
			}else{
				holder.gender.setImageResource(R.drawable.female1);
			}
			if(dataSystem.fromUser.nickName!=null){
				holder.name.setText(dataSystem.fromUser.nickName);
			}
			if(StringUtil.judgeImageExists(Constants.Picture_ICON_Path+File.separator+dataSystem.fromUser.iconUrl)){
				dataSystem.fromUser.iconPath=Constants.Picture_ICON_Path+File.separator+dataSystem.fromUser.iconUrl;
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize=8;
				options.inPreferredConfig=Bitmap.Config.RGB_565;
				options.inPurgeable=true;
				options.inInputShareable=true;
				holder.circleView.setImageBitmap(BitmapFactory.decodeFile(dataSystem.fromUser.iconPath,options));
			}else{
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String pathString=HttpUtil.downloadIconImage(Constants.USER_DOWNLOAD_TX, dataSystem.fromUser.iconUrl, null, (Activity)context);
						if(pathString!=null){
							dataSystem.fromUser.iconPath=pathString;
							((Activity)context).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									notifyDataSetChanged();
								}
							});
						}
					}
				}).start();
			}
		}
		if(dataSystem.body!=null){
			holder.body.setText(dataSystem.body);
		}
		holder.time.setText(judgeTime(dataSystem.create_time));
		return convertView;
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
	class Holder{
		CircleView circleView;
		ImageView gender;
		TextView name;
		TextView body;
		TextView time;
	}

}
