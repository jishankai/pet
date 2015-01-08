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
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson.Data;
import com.aidigame.hisun.pet.ui.ChatActivity;
import com.aidigame.hisun.pet.ui.NewPetKingdomActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.fragment.UserCenterFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 设置    设置最爱萌星界面
 * @author admin
 *
 */
public class ChosePetListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
    ImageLoader imageLoader;
	Activity context;
	ArrayList<Animal> list;
	Handler handler;
	HandleHttpConnectionException handleHttpConnectionException;
	public ChosePetListAdapter(Activity context,ArrayList<Animal> list,Handler handler){
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
	            .showImageOnLoading(R.drawable.pet_icon)
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_chose_pet_listview, null);
			holder.icon=(RoundImageView)convertView.findViewById(R.id.icon_circleView);
			holder.name=(TextView)convertView.findViewById(R.id.textView2);
			holder.gender=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.hatIv=(ImageView)convertView.findViewById(R.id.imageView2);
			holder.raceTv=(TextView)convertView.findViewById(R.id.textView3);
			holder.ageTv=(TextView)convertView.findViewById(R.id.textView5);
			holder.setTv=(TextView)convertView.findViewById(R.id.textView1);
//			holder.layout=(LinearLayout)convertView.findViewById(R.id.linearlayout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		if(position==0){
			holder.hatIv.setVisibility(View.VISIBLE);
		}else{
			holder.hatIv.setVisibility(View.INVISIBLE);
		}
		LogUtil.i("exception", "position========"+position);
//		if(position<list.size()){
		final Animal data=list.get(position);
		
		
//		holder.icon.setImageBitmap();
		final RoundImageView view=holder.icon;
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.animalInfo(context,data, handleHttpConnectionException.getHandler(context));
						Intent intent=new Intent(context,NewPetKingdomActivity.class);
						intent.putExtra("animal", data);
						context.startActivity(intent);
					}
				}).start();
				
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
		holder.ageTv.setText(data.a_age_str);
		if(Constants.user!=null&&Constants.user.currentAnimal!=null&&Constants.user.currentAnimal.a_id==data.a_id){
			holder.hatIv.setVisibility(View.VISIBLE);
			holder.setTv.setVisibility(View.VISIBLE);
			holder.setTv.setText("已是最爱");
			holder.setTv.setBackgroundResource(R.drawable.button);
			
		}else{
			holder.hatIv.setVisibility(View.INVISIBLE);
			holder.setTv.setVisibility(View.VISIBLE);
			holder.setTv.setBackgroundResource(R.drawable.button_green);
			holder.setTv.setText("最爱萌星");
		}
		holder.name.setText(""+data.pet_nickName);
		holder.raceTv.setText(""+data.race);
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
		holder.setTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.user!=null&&Constants.user.currentAnimal!=null&&Constants.user.currentAnimal.a_id==data.a_id){
					Toast.makeText(context, "已经设置为最爱", Toast.LENGTH_LONG).show();
					
				}else{
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final boolean flag=HttpUtil.setDefaultKingdom(context,data, handleHttpConnectionException.getHandler(context));
						Animal animal=HttpUtil.animalInfo(context,data, handleHttpConnectionException.getHandler(context));
						if(animal!=null&&animal.master_id!=0){
							Constants.user.currentAnimal=animal;
							handleHttpConnectionException.getHandler(context).post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									ChosePetListAdapter.this.notifyDataSetChanged();
									if(UserCenterFragment.userCenterFragment!=null){
										UserCenterFragment.userCenterFragment.updatateInfo();
									}
								}
							});
						}
						
					}
				}).start();
				}
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
		ImageView gender,hatIv;
		TextView name;
		TextView raceTv;
		TextView ageTv;
		TextView setTv;
	}


}
