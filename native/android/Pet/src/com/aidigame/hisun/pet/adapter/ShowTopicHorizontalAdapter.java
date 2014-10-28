package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * 照片详情页面    点赞或送礼物头像列表
 * @author admin
 *
 */
public class ShowTopicHorizontalAdapter extends BaseAdapter {
	Activity context;
	DisplayImageOptions displayImageOptions;//显示图片的格式
	ArrayList<String>urls;
	boolean isAnimal;
	PetPicture petPicture;
	boolean[] type;
	public ShowTopicHorizontalAdapter(Activity context,ArrayList<String> urls,boolean isAnimal){
		this.context=context;
		this.urls=urls;
		this.isAnimal=isAnimal;
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
    public void updateAdapter(ArrayList<String> urls){
    	this.urls=urls;
    }
    public void setPetPicture(PetPicture petPicture){
    	this.petPicture=petPicture;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(petPicture!=null){
			urls=new ArrayList<String>();
			int count1=0;
			int count2=0;
			if(petPicture.gift_txUrlList!=null){
				for(int i=0;i<petPicture.gift_txUrlList.size();i++){
					urls.add(petPicture.gift_txUrlList.get(i));
				}
				count1=petPicture.gift_txUrlList.size();
			}
			if(petPicture.like_txUrlList!=null){
				for(int i=0;i<petPicture.like_txUrlList.size();i++){
					urls.add(petPicture.like_txUrlList.get(i));
				}
				count2=petPicture.like_txUrlList.size();
			}
			type=new boolean[count1+count2];
			for(int i=0;i<count1;i++){
				type[i]=true;
			}
			for(int i=count1;i<count1+count2;i++){
				type[i]=false;
			}
		}
		return urls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return urls.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_show_topic_listview,null);
			holder=new Holder();
			holder.circleView=(RoundImageView)convertView.findViewById(R.id.item_circleView);
			holder.typeIV=(ImageView)convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final String url=urls.get(position);
		final RoundImageView view=holder.circleView;
		if(type!=null){
			if(type[position]){
				holder.typeIV.setImageResource(R.drawable.ball_red_gift);
			}else{
				if(petPicture!=null){
					if(petPicture.animal.type>200&&petPicture.animal.type<300){
						holder.typeIV.setImageResource(R.drawable.ball_red_bone);
					}else if(petPicture.animal.type>100&&petPicture.animal.type<200){
						holder.typeIV.setImageResource(R.drawable.ball_red_fish);
					}
				}
				
			}
		}
		
		
		ImageLoader imageLoader=ImageLoader.getInstance();
		if(isAnimal){
			holder.typeIV.setVisibility(View.INVISIBLE);
			imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+url, holder.circleView, displayImageOptions);
		}else{
			imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+url, holder.circleView, displayImageOptions);
		}
		
		/*if(new File(path).exists()){
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=8;
			holder.circleView.setImageBitmap(BitmapFactory.decodeFile(path,options));
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final String path=HttpUtil.downloadIconImage(Constants.USER_DOWNLOAD_TX, url, null,context);
					if(path!=null){
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								BitmapFactory.Options options=new BitmapFactory.Options();
								options.inSampleSize=8;
								view.setImageBitmap(BitmapFactory.decodeFile(path,options));
							}
						});
					}
				}
			}).start();
		}*/
		return convertView;
	}
	class Holder{
		RoundImageView circleView;
		ImageView typeIV;
	}

}
