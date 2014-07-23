package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.MessagJson;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.CircleView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.style.UpdateAppearance;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageListAdapter extends BaseAdapter {
	Context context;
	ArrayList<MessagJson.DataSystem> datas;
	
	public MessageListAdapter(Context context,ArrayList<MessagJson.DataSystem> datas){
		this.context=context;
		this.datas=datas;
	}
	public void update(ArrayList<MessagJson.DataSystem> datas){
		this.datas=datas;
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
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_message_list, null);
			holder.icon=(CircleView)convertView.findViewById(R.id.imageView1);
			holder.nameTv=(TextView)convertView.findViewById(R.id.textView1);
			holder.timeTv=(TextView)convertView.findViewById(R.id.textView2);
			holder.textTv=(TextView)convertView.findViewById(R.id.textView3);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final MessagJson.DataSystem data=datas.get(position);
		if(data.fromUser!=null){
			holder.nameTv.setText(data.fromUser.nickName);
			holder.timeTv.setText(data.update_time);
			holder.textTv.setText(data.body);
			if(StringUtil.judgeImageExists(Constants.Picture_ICON_Path+File.separator+data.fromUser.iconUrl)){
				data.fromUser.iconPath=Constants.Picture_ICON_Path+File.separator+data.fromUser.iconUrl;
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize=8;
				options.inPreferredConfig=Bitmap.Config.RGB_565;
				options.inPurgeable=true;
				options.inInputShareable=true;
				holder.icon.setImageBitmap(BitmapFactory.decodeFile(data.fromUser.iconPath,options));
			}else{
                new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String pathString=HttpUtil.downloadIconImage(Constants.USER_DOWNLOAD_TX, data.fromUser.iconUrl, null, (Activity)context);
						if(pathString!=null){
							data.fromUser.iconPath=pathString;
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
		
		return convertView;
	}
	class Holder{
		CircleView icon;
		TextView nameTv,timeTv,textTv;
	}

}
