package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.ui.UserCardActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * 王国资料界面  臣民列表
 * @author admin
 *
 */
public class KingdomPeoplesAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
    ImageLoader imageLoader;
	Activity context;
	ArrayList<MyUser> list;
	HandleHttpConnectionException handleHttpConnectionException;
	public KingdomPeoplesAdapter(Activity context,ArrayList<MyUser> list){
		this.context=context;
		this.list=list;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
	public void updateList(ArrayList<MyUser> temp) {
		// TODO Auto-generated method stub
		this.list=temp;
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_kingdom_people_listview, null);
			holder.roundImageView=(RoundImageView)convertView.findViewById(R.id.roundImage_one_border);
			holder.name=(TextView)convertView.findViewById(R.id.textView2);
			holder.gender=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.job=(TextView)convertView.findViewById(R.id.textView1);
			holder.province=(TextView)convertView.findViewById(R.id.textView3);
			holder.city=(TextView)convertView.findViewById(R.id.textView4);
			holder.contribute=(TextView)convertView.findViewById(R.id.textView5);
			holder.layout=(LinearLayout)convertView.findViewById(R.id.linearLayout1);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		LogUtil.i("exception", "position========"+position);
		final MyUser data=list.get(position);
		final RoundImageView view=holder.roundImageView;
		/*view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});*/
		 BitmapFactory.Options options=new BitmapFactory.Options();
		    options.inSampleSize=8;
		    options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			loadIcon(holder.roundImageView, data);
		if(data.u_gender==1){
			holder.gender.setImageResource(R.drawable.male1);
		}else if(data.u_gender==2){
			holder.gender.setImageResource(R.drawable.female1);
		}else{
//			holder.gender.setImageResource();
		}
		holder.province.setText(data.province);
		holder.city.setText(data.city);
		holder.name.setText(""+data.u_nick);
		holder.layout.setClickable(true);
		holder.contribute.setText(""+data.t_contri);
		holder.job.setText(data.rank);
		holder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(context,UserCardActivity.class);
				intent.putExtra("user", data);
				intent.putExtra("from", 1);
				context.startActivity(intent);
				
			}
		});
		
//		}
		return convertView;
	}
	public void loadIcon(RoundImageView icon,final MyUser data){
		
		imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+data.u_iconUrl, icon, displayImageOptions);
	}
	class Holder{
		RoundImageView roundImageView;
		ImageView gender;
		TextView name;
		TextView job;
		TextView province;
		TextView city;
		TextView contribute;
		LinearLayout layout;
	}


}
