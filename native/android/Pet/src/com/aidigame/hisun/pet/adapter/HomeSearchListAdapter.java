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
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson.Data;
import com.aidigame.hisun.pet.ui.NewPetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserCardActivity;
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
 * 主页   搜索结果展示    用户或宠物
 * @author admin
 *
 */
public class HomeSearchListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
	DisplayImageOptions displayImageOptions2;//显示图片的格式
    ImageLoader imageLoader;
	Activity context;
	ArrayList<Animal> animalList;
	ArrayList<MyUser> userList;
    int mode;//1,萌星列表；2，用户列表
	HandleHttpConnectionException handleHttpConnectionException;
	public HomeSearchListAdapter(Activity context,ArrayList<Animal> list,ArrayList<MyUser> userList,int mode){
		this.context=context;
		

		this.mode=mode;
		if(mode==1){
			this.animalList=list;
		}else{
			this.userList=userList;
		}
		
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
//		        .decodingOptions(options)
                .build();
		displayImageOptions2=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		        .decodingOptions(options)
                .build();
	}
	public void updateList(
			ArrayList<Animal> temp,ArrayList<MyUser> userList,int mode) {
		// TODO Auto-generated method stub
		this.mode=mode;
		if(mode==1){
			this.animalList=temp;
		}else{
			this.userList=userList;
		}
		
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mode==1){
			return animalList.size();
		}else{
			return userList.size();
		}
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(mode==1){
			return animalList.get(position);
		}else{
			return userList.get(position);
		}
		
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
			holder.hatIv=(ImageView)convertView.findViewById(R.id.imageView2);
			holder.raceTv=(TextView)convertView.findViewById(R.id.textView3);
			holder.ageTv=(TextView)convertView.findViewById(R.id.textView5);
			holder.setTv=(TextView)convertView.findViewById(R.id.textView1);
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

		
		
		
//		holder.icon.setImageBitmap();
		if(mode==1){
			Animal data=animalList.get(position);
			loadIcon(holder.icon, data);
			if(data.a_gender==1){
				holder.gender.setImageResource(R.drawable.male1);
			}else if(data.a_gender==2){
				holder.gender.setImageResource(R.drawable.female1);
			}else{
//				holder.gender.setImageResource();
			}
			holder.name.setText(""+data.pet_nickName);
			holder.raceTv.setText(""+data.race);
			holder.ageTv.setText(data.a_age_str);
		}else{
			MyUser data=userList.get(position);
			loadIcon(holder.icon, data);
			if(data.u_gender==1){
				holder.gender.setImageResource(R.drawable.male1);
			}else if(data.a_gender==2){
				holder.gender.setImageResource(R.drawable.female1);
			}else{
//				holder.gender.setImageResource();
			}
			holder.name.setText(""+data.u_nick);
			holder.raceTv.setText(""+data.province);
			holder.ageTv.setText(data.city);
		}
			

			holder.hatIv.setVisibility(View.INVISIBLE);
			holder.setTv.setVisibility(View.VISIBLE);
			holder.setTv.setBackgroundResource(R.drawable.button_green);
			holder.setTv.setText("访问");
			holder.setTv.setVisibility(View.INVISIBLE);

		final RoundImageView view=holder.icon;
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mode==1){
					if(NewPetKingdomActivity.petKingdomActivity!=null){
						if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
							NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
							NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
						}
						NewPetKingdomActivity.petKingdomActivity.finish();
						NewPetKingdomActivity.petKingdomActivity=null;
					}
					Intent intent=new Intent(context,NewPetKingdomActivity.class);
					intent.putExtra("animal", animalList.get(position));
					context.startActivity(intent);
				}else{
					if(UserCardActivity.userCardActivity!=null){
						if(PetApplication.petApp.activityList.contains(UserCardActivity.userCardActivity)){
							PetApplication.petApp.activityList.remove(UserCardActivity.userCardActivity);
						}
						UserCardActivity.userCardActivity.finish();
						UserCardActivity.userCardActivity=null;
						System.gc();
					}
					Intent intent=new Intent(context,UserCardActivity.class);
					intent.putExtra("user", userList.get(position));
					context.startActivity(intent);
				}

			}
		});
		
		
//		}
		return convertView;
	}
	public void loadIcon(RoundImageView icon,final Animal data){
		
		imageLoader=ImageLoader.getInstance();
		int w=context.getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
		imageLoader.displayImage(Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+data.pet_iconUrl+"@"+w+"w_"+w+"h_0l.jpg", icon, displayImageOptions);
	}
public void loadIcon(RoundImageView icon,final MyUser data){
		
		imageLoader=ImageLoader.getInstance();
		int w=context.getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
		imageLoader.displayImage(Constants.USER_THUBMAIL_DOWNLOAD_TX+data.u_iconUrl+"@"+w+"w_"+w+"h_0l.jpg", icon, displayImageOptions2);
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
