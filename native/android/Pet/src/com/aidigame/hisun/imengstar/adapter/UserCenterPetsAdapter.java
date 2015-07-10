package com.aidigame.hisun.imengstar.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.ui.ActivityWebActivity;
import com.aidigame.hisun.imengstar.ui.NewPetKingdomActivity;
import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class UserCenterPetsAdapter extends BaseAdapter {
	private ArrayList<Animal> animals;
	private Context context;
	DisplayImageOptions displayImageOptions;
	public UserCenterPetsAdapter(Context context,ArrayList<Animal> animals){
		this.context=context;
		this.animals=animals;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		        .decodingOptions(options)
                .build();
	}
public void update(ArrayList<Animal> a){
	this.animals=a;
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
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_user_center_pets, null);
			holder.layout=(LinearLayout)convertView.findViewById(R.id.root_layout);
			holder.roundImageView=(RoundImageView)convertView.findViewById(R.id.show_topic_usericon);
			holder.nameT=(TextView)convertView.findViewById(R.id.name_tv);
			holder.rankIv=(ImageView)convertView.findViewById(R.id.rank_iv);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final Animal animal=animals.get(position);
		holder.nameT.setText(animal.pet_nickName);
		ImageLoader imageLoader=ImageLoader.getInstance();
		int w=context.getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
		imageLoader.displayImage(Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+animal.pet_iconUrl+"@"+w+"w_"+w+"h_0l.jpg", holder.roundImageView,displayImageOptions);
		switch (animal.u_rankCode) {
		case 0:
			holder.rankIv.setImageResource(R.drawable.title_1);
			break;
		case 1:
			holder.rankIv.setImageResource(R.drawable.title_2);
			break;
		case 2:
			holder.rankIv.setImageResource(R.drawable.title_3);
			break;
		case 3:
			holder.rankIv.setImageResource(R.drawable.title_4);
			break;
		case 4:
			holder.rankIv.setImageResource(R.drawable.title_5);
			break;
		case 5:
			holder.rankIv.setImageResource(R.drawable.title_6);
			break;
		case 6:
			holder.rankIv.setImageResource(R.drawable.title_7);
			break;
		case 7:
			holder.rankIv.setImageResource(R.drawable.title_8);
			break;
		case 8:
			holder.rankIv.setImageResource(R.drawable.title_9);
			break;
		}
		holder.rankIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,ActivityWebActivity.class);
				intent.putExtra("mode", 1);
				context.startActivity(intent);
			}
		});
		holder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(NewPetKingdomActivity.petKingdomActivity!=null){
					if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
						NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
					}
					NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
					NewPetKingdomActivity.petKingdomActivity.finish();
					NewPetKingdomActivity.petKingdomActivity=null;
				}
				Intent intent=new Intent(context,NewPetKingdomActivity.class);
				intent.putExtra("animal",animal);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	class Holder{
		LinearLayout layout;
		RoundImageView roundImageView;
		TextView nameT;
		ImageView rankIv;
	}

}
