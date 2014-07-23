package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.json.MessagJson;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.CircleView;

public class MailListViewAdapter extends BaseAdapter {
	Context context;
	ArrayList<MessagJson.DataSystem> datas;
	public MailListViewAdapter(Context context,ArrayList<MessagJson.DataSystem> datas){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_mail_listview, null);
			holder.body_left=(Button)convertView.findViewById(R.id.body_left);
			holder.body_right=(Button)convertView.findViewById(R.id.body_right);
			holder.icon_left=(CircleView)convertView.findViewById(R.id.icon_left);
			holder.icon_right=(CircleView)convertView.findViewById(R.id.icon_right);
			
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		MessagJson.DataSystem data=datas.get(position);
		if(data.fromUser!=null){
			if(data.fromUser.userId==Constants.user.userId){
				holder.body_left.setVisibility(View.GONE);
				holder.icon_left.setVisibility(View.GONE);
				holder.body_right.setVisibility(View.VISIBLE);
				holder.icon_right.setVisibility(View.VISIBLE);
				holder.body_right.setText(""+(data.body==null?"":data.body));
				if(StringUtil.judgeImageExists(data.fromUser.iconPath)){
					 BitmapFactory.Options options=new BitmapFactory.Options();
					    options.inSampleSize=8;
					    options.inPreferredConfig=Bitmap.Config.RGB_565;
						options.inPurgeable=true;
						options.inInputShareable=true;
						holder.icon_right.setImageBitmap(BitmapFactory.decodeFile(data.fromUser.iconPath, options));
						convertView.setPadding(0, 0, 0, 0);
						holder.icon_right.setPadding(0, 0, 0, 0);
				}
			}else{
				holder.body_right.setVisibility(View.GONE);
				holder.body_right.setVisibility(View.GONE);
				holder.icon_left.setVisibility(View.VISIBLE);
				holder.icon_left.setVisibility(View.VISIBLE);
				holder.body_left.setText(""+(data.body==null?"":data.body));
				if(StringUtil.judgeImageExists(data.fromUser.iconPath)){
					 BitmapFactory.Options options=new BitmapFactory.Options();
					    options.inSampleSize=8;
					    options.inPreferredConfig=Bitmap.Config.RGB_565;
						options.inPurgeable=true;
						options.inInputShareable=true;
						holder.icon_left.setImageBitmap(BitmapFactory.decodeFile(data.fromUser.iconPath, options));
						convertView.setPadding(0, 0, 0, 0);
						holder.icon_left.setPadding(0, 0, 0, 0);
				}
			}
			
			
		}
		
		return convertView;
	}
	class Holder{
		Button body_left;
		Button body_right;
		CircleView icon_left;
		CircleView icon_right;
	}

}
