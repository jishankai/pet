package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.ui.NewPetKingdomActivity;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class UserPetsAdapter extends BaseAdapter {
	Activity context;
	ArrayList<Animal> animals;
	User user;
	DisplayImageOptions displayImageOptions;//显示图片的格式
    ImageLoader imageLoader;
	public UserPetsAdapter(Activity context,ArrayList<Animal> animals,User user){
		this.context=context;
		this.animals=animals;
		this.user=user;
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
	
	public void update(ArrayList<Animal> animals){
		this.animals=animals;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return animals.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return animals.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_user_card_pets_icon, null);
			holder=new Holder();
			holder.hat=(ImageView)convertView.findViewById(R.id.hat);
			holder.icon=(ImageView)convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		Animal animal=animals.get(position);
		if(animal.master_id==user.userId){
			holder.hat.setVisibility(View.VISIBLE);
		}else{
			holder.hat.setVisibility(View.GONE);
		}
		loadIcon(holder.icon, animal);
		
		return convertView;
	}
public void loadIcon(ImageView icon,final Animal data){
		icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if(NewPetKingdomActivity.petKingdomActivity!=null){
					if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
						NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
					}
					NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
					NewPetKingdomActivity.petKingdomActivity.setPetInfo(data);
					ActivityManager am=context.getSystemService(Context.ACTIVITY_SERVICE);
				}*/
				Intent intent=new Intent(context,NewPetKingdomActivity.class);
				intent.putExtra("animal",data);
				context.startActivity(intent);
				context.finish();
			}
		});
		imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+data.pet_iconUrl, icon, displayImageOptions);
	}
	class Holder{
		ImageView icon;
		ImageView hat;
	}

}
