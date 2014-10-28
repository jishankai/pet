package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 活动列表
 * @author admin
 *
 */
public class ActivityListAdapter extends BaseAdapter {
	ImageLoader imageLoader;
	DisplayImageOptions displayImageOptions;
	Context context;
	ArrayList<ActivityJson.Data> list;
	public ActivityListAdapter(Context context,ArrayList<ActivityJson.Data> list){
		this.context=context;
		this.list=list;
		//显示没有图片
	    Bitmap nobmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.noimg);
		Matrix matrix=new Matrix();
		matrix.postScale(Constants.screen_width/(nobmp.getWidth()*1f), Constants.screen_width/(nobmp.getWidth()*1f));
		nobmp=Bitmap.createBitmap(nobmp, 0, 0, nobmp.getWidth(), nobmp.getHeight(),matrix,true);
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inJustDecodeBounds=false;
		opts.inSampleSize=2;
		opts.inPreferredConfig=Bitmap.Config.RGB_565;
		opts.inPurgeable=true;
		opts.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions.Builder()
		                    .showImageOnLoading(new BitmapDrawable(nobmp))
		                    .cacheInMemory(true)
		                    .cacheOnDisc(true)
		                    .bitmapConfig(Config.RGB_565)
		                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		                    .decodingOptions(opts)
		                    .build();
		                    
	}
	public  void updateList(ArrayList<ActivityJson.Data> list) {
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
	    final ImageView temp=holder.advertiseImage;
	    imageLoader=ImageLoader.getInstance();
	    imageLoader.loadImage(Constants.ACTIVITY_IMAGE+data.img,  displayImageOptions, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				Matrix matrix=new Matrix();
				matrix.postScale(Constants.screen_width/(loadedImage.getWidth()*1f), Constants.screen_width/(loadedImage.getWidth()*1f));
				loadedImage=Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);
				temp.setImageBitmap(loadedImage);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
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
