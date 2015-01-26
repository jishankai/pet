package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson.Data;
import com.aidigame.hisun.pet.ui.NewPetKingdomActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 类似  围观群众 界面  宠物信息列表
 * @author admin
 *
 */
public class AnimalsListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
    ImageLoader imageLoader;
	Activity context;
	ArrayList<Animal> list;
	Handler handler;
	HandleHttpConnectionException handleHttpConnectionException;
	public AnimalsListAdapter(Activity context,ArrayList<Animal> list,Handler handler){
		this.context=context;
		this.list=list;
		this.handler=handler;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
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
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
	public void updateList(
			ArrayList<Animal> temp) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_users_listview, null);
			holder.icon=(RoundImageView)convertView.findViewById(R.id.icon_circleView);
			holder.name=(TextView)convertView.findViewById(R.id.textView2);
			holder.gender=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.giftType=(ImageView)convertView.findViewById(R.id.imageView2);
			holder.provinceTV=(TextView)convertView.findViewById(R.id.textView3);
			holder.cityTV=(TextView)convertView.findViewById(R.id.textView5);
			holder.sendEmail=(TextView)convertView.findViewById(R.id.textView1);
//			holder.layout=(LinearLayout)convertView.findViewById(R.id.linearlayout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		LogUtil.i("exception", "position========"+position);
//		if(position<list.size()){
		final Animal data=list.get(position);
		holder.giftType.setVisibility(View.INVISIBLE);
		if(data.is_follow){
//			holder.sendEmail.setBackgroundResource(R.drawable.button_green_right);;
		}else{
//			holder.sendEmail.setBackgroundResource(R.drawable.button_green_add);;
		}
		
//		holder.icon.setImageBitmap();
		final RoundImageView view=holder.icon;
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animal animal=list.get(position);
				Intent intent=new Intent(context,NewPetKingdomActivity.class);
				intent.putExtra("animal", animal);
				context.startActivity(intent);
			
				
			}
		});
			loadIcon(holder.icon, data);
		if(data.a_gender==1){
			holder.gender.setImageResource(R.drawable.male1);
		}else if(data.a_gender==2){
			holder.gender.setImageResource(R.drawable.female1);
		}else{
//			holder.gender.setImageResource();
		}
		holder.name.setText(""+data.pet_nickName);
		holder.provinceTV.setText(""+data.race);
		holder.cityTV.setText(""+data.a_age_str);
		/*holder.layout.setClickable(true);
		holder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,UserDossierActivity.class);
				UserImagesJson.Data d=new UserImagesJson.Data();
				d.usr_id=data.user.userId;
				d.user=data.user;
				d.isFriend=data.isFriend;
				intent.putExtra("data", d);
				context.startActivity(intent);
			}
		});*/
		
		holder.sendEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final TextView tv=(TextView)v;
						boolean flag=false;
						if(data.is_follow){
							flag=HttpUtil.userDeleteFollow(data, null,context);
							if(flag){
								data.is_follow=false;
								handleHttpConnectionException.getHandler(context).post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										
										Toast.makeText(context, "取消关注成功", Toast.LENGTH_SHORT).show();
//										tv.setBackgroundResource(R.drawable.button_green_add);;
										data.is_follow=false;
									}
								});
							}else{
								/*
								 * 取消关注失败
								 */
							}
						}else{
							flag=HttpUtil.userAddFollow(data, null,context);
							if(flag){
								data.is_follow=true;
								handleHttpConnectionException.getHandler(context).post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(context, "添加关注成功", Toast.LENGTH_SHORT).show();
//										tv.setBackgroundResource(R.drawable.button_green_right);;
										data.is_follow=true;
									}
								});
							}else{
								/*
								 * 添加关注失败
								 */
							}
						}
					}
				}).start();
			}
		});
		
		
//		}
		return convertView;
	}
	public void loadIcon(RoundImageView icon,final Animal data){
		
		imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+data.pet_iconUrl, icon, displayImageOptions);
	}
	
	class Holder{
		RoundImageView icon;
		ImageView gender,giftType;
		TextView name;
		TextView provinceTV;
		TextView cityTV;
		TextView sendEmail;
		
//		LinearLayout layout;
	}


}
