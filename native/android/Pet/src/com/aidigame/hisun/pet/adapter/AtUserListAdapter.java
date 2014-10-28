package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * @用户列表
 * @author admin
 *
 */
public class AtUserListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
    ImageLoader imageLoader;
	Context context;
	ArrayList<User> list;
	public AtUserListAdapter(Context context,ArrayList<User> list){
		this.context=context;
		this.list=list;
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.noimg)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
	public void updateList(ArrayList<User> list){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_atuser_list, null);
			holder=new Holder();
			holder.icon=(RoundImageView)convertView.findViewById(R.id.icon_circleView);
			holder.flagIV=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.nameTV=(TextView)convertView.findViewById(R.id.textView1);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+list.get(position).u_iconUrl, holder.icon, displayImageOptions);
		holder.nameTV.setText(list.get(position).u_nick);
		if(list.get(position).isSelected){
			holder.flagIV.setImageResource(R.drawable.atuser_list_checked);
		}else{
			holder.flagIV.setImageResource(R.drawable.atuser_list_unchecked);
		}
		final ImageView temp=holder.flagIV;
		temp.setTag(list.get(position));
		temp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				User data=(User)v.getTag();
				if(data.isSelected){
					data.isSelected=false;
					temp.setImageResource(R.drawable.atuser_list_unchecked);
				}else{
					data.isSelected=true;
					temp.setImageResource(R.drawable.atuser_list_checked);
				}
			}
		});
		return convertView;
	}
	class Holder{
		RoundImageView icon;
		TextView nameTV;
		ImageView flagIV;
	}

}
