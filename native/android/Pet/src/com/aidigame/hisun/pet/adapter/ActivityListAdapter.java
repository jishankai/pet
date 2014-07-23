package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.json.ActivityJson;
import com.aidigame.hisun.pet.http.json.ActivityJson.Data;
import com.aidigame.hisun.pet.util.StringUtil;

public class ActivityListAdapter extends BaseAdapter {
	Context context;
	ArrayList<ActivityJson.Data> list;
	public ActivityListAdapter(Context context,ArrayList<ActivityJson.Data> list){
		this.context=context;
		this.list=list;
		loadData();
	}
	public  void updateList(ArrayList<ActivityJson.Data> list) {
		this.list=list;
	}
	public void  loadData(){/*
		ActivityJson json=new ActivityJson();
		json.data=new ActivityJson.Data();
		json.data.gold="一等奖：宠物贝贝摄影写真；二等奖：宝贝牌狗窝";
		json.data.time="2014-6-21至2014-6-29";
		json.data.total="189/200人";
		json.data.url=R.drawable.signing;
		json.data.status=1;
		json.data.name="有情狗终成眷属";
		list.add(json);
		json=new ActivityJson();
		json.data=new ActivityJson.Data();
		json.data.gold="一等奖：宠物贝贝摄影写真；二等奖：睡的香牌狗窝";
		json.data.time="2014-6-21至2014-6-29";
		json.data.total="199/300人";
		json.data.url=R.drawable.signing;
		json.data.status=1;
		json.data.name="谁最靓？";
		list.add(json);
		json=new ActivityJson();
		json.data=new ActivityJson.Data();
		json.data.gold="一等奖：滴水不沾宠物靴；二等奖：宝贝牌狗窝";
		json.data.time="2014-5-20至2014-6-1";
		json.data.total="189/200人";
		json.data.url=R.drawable.signed;
		json.data.status=2;
		json.data.name="有我萌吗？";
		list.add(json);
		json=new ActivityJson();
		json.data=new ActivityJson.Data();
		json.data.gold="一等奖：不怕黑牌宠物遮阳伞；二等奖：宝贝牌狗窝";
		json.data.time="2014-3-21至2014-4-29";
		json.data.total="189/200人";
		json.data.url=R.drawable.signed;
		json.data.status=2;
		json.data.name="有情狗终成眷属";
		list.add(json);
		json=new ActivityJson();
		json.data=new ActivityJson.Data();
		json.data.gold="一等奖：宠物贝贝摄影写真；二等奖：宝贝牌狗窝";
		json.data.time="2014-3-11至2014-4-19";
		json.data.total="189/200人";
		json.data.url=R.drawable.signed;
		json.data.status=2;
		json.data.name="有情狗终成眷属";
		list.add(json);
	*/}

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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_activity_listview,null);
			holder.advertiseImage=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.joinImage=(ImageView)convertView.findViewById(R.id.imageView2);
			holder.activityAame=(TextView)convertView.findViewById(R.id.textView1);
			holder.activityTime=(TextView)convertView.findViewById(R.id.textView2);
			holder.activityAward=(TextView)convertView.findViewById(R.id.textView3);
			holder.activityJoinNum=(TextView)convertView.findViewById(R.id.textView4);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		ActivityJson.Data data=list.get(position);
		if(data.imgPath!=null&&StringUtil.judgeImageExists(data.imgPath)){
			BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inJustDecodeBounds=true;
			opts.inSampleSize=2;
			BitmapFactory.decodeFile(data.imgPath, opts);
			Matrix matrix=new Matrix();
			matrix.postScale(Constants.screen_width/(opts.outWidth*1f), Constants.screen_width/(opts.outWidth*1f));
			opts.inJustDecodeBounds=false;
			opts.inSampleSize=2;
			Bitmap bmp=BitmapFactory.decodeFile(data.imgPath,opts);
			Bitmap temp=Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			if(!bmp.isRecycled()){
				bmp.recycle();
			}
			holder.advertiseImage.setImageBitmap(temp);
		}else{
			
		}
		
		if(data.end_time*1000>System.currentTimeMillis()){
			holder.joinImage.setImageResource(R.drawable.activity_green);
//			holder.joinImage.setClickable(true);
		}else{
			holder.joinImage.setImageResource(R.drawable.activity_right);
//			holder.joinImage.setClickable(false);
		}
		if(data.topic!=null)
		holder.activityAame.setText(data.topic);
		if(data.reward!=null)
		holder.activityAward.setText(data.reward);
		holder.activityJoinNum.setText(""+data.people+"人");
		holder.activityTime.setText(""+StringUtil.timeFormat(data.start_time)+"至"+StringUtil.timeFormat(data.end_time));
		return convertView;
	}
	class Holder{
		ImageView advertiseImage;
		ImageView joinImage;
		TextView activityAame,activityTime,activityAward,activityJoinNum;
	}

}
