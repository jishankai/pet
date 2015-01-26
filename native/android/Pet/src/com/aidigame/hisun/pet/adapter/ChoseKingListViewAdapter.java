package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.KingdomCard;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.pet.ui.ChoseKingActivity;
import com.aidigame.hisun.pet.ui.DialogNoteActivity;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.NewRegisterActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.fragment.DialogNote;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom.ResultListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 选择王国列表
 * @author admin
 *
 */
public class ChoseKingListViewAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
	DisplayImageOptions displayImageOptions2;//显示图片的格式
	DisplayImageOptions displayImageOptions3;//显示图片的格式
	ArrayList<Animal> list;
	ChoseKingActivity context;
	LinearLayout currentShowInfoLayout;
	View hidenLineView;
	HandleHttpConnectionException handleHttpConnectionException;
	boolean isBind;
	int mode;
	int from;//默认值为0，进行注册；1，已经注册过
	MyUser user;
	public ChoseKingListViewAdapter(ChoseKingActivity context,ArrayList<Animal> list,int mode,int from,boolean isBind,MyUser user){
		this.context=context;
		this.list=list;
		this.mode=mode;
		this.from=from;
		this.isBind=isBind;
		this.user=user;
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
		        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		displayImageOptions2=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.image_default)
	            
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		displayImageOptions3=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
	            
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
	public void updateList(ArrayList<Animal> list){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_chose_king_listview, null);
			holder.image1=(ImageView)convertView.findViewById(R.id.imageView2);
			holder.image2=(ImageView)convertView.findViewById(R.id.imageView3);
			holder.image3=(ImageView)convertView.findViewById(R.id.imageView4);
			holder.image4=(ImageView)convertView.findViewById(R.id.imageView5);
			holder.infoLayout=(LinearLayout)convertView.findViewById(R.id.more_info_linearlayout);
			holder.briefInfoLayout=(LinearLayout)convertView.findViewById(R.id.brief_info_layout);
			holder.briefInfoLayout.setClickable(true);
			holder.join=(TextView)convertView.findViewById(R.id.textView1);
			holder.line=convertView.findViewById(R.id.gray_line);
			holder.petAge=(TextView)convertView.findViewById(R.id.textView5);
			holder.petIcon=(RoundImageView)convertView.findViewById(R.id.icon_circleView);
			holder.petName=(TextView)convertView.findViewById(R.id.textView2);
			holder.petRace=(TextView)convertView.findViewById(R.id.textView3);
			holder.petSex=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.userIcon=(RoundImageView)convertView.findViewById(R.id.user_icon_circle);
//			holder.userJob=(TextView)convertView.findViewById(R.id.textView9);
//			holder.cityTV=(TextView)convertView.findViewById(R.id.textView8);
			holder.provinceTV=(TextView)convertView.findViewById(R.id.textView10);
			holder.t_rq_tv=(TextView)convertView.findViewById(R.id.t_rq_tv);
			holder.fansTV=(TextView)convertView.findViewById(R.id.fans_tv);
			holder.userName=(TextView)convertView.findViewById(R.id.textView7);
			holder.userSex=(ImageView)convertView.findViewById(R.id.imageView6);
			holder.imagesLayout=(LinearLayout)convertView.findViewById(R.id.images_layout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final Animal data=list.get(position);
		holder.petName.setText(data.pet_nickName);
		holder.t_rq_tv.setText(""+data.t_rq);
		holder.fansTV.setText(""+data.fans);
		if(data.a_gender==1){
			holder.petSex.setImageResource(R.drawable.male1);
		}else if(data.a_gender==2){
			holder.petSex.setImageResource(R.drawable.female1);
		}
		holder.join.setBackgroundResource(R.drawable.support_green);
		holder.join.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(from==1){
					
					int num=0;
					int count=0;
					for(int i=0;i<Constants.user.aniList.size();i++){
//						if(Constants.user.aniList.get(i).master_id!=Constants.user.userId)
							count++;
					}
					
					
					if(count>=10&&count<=20){
						num=(count)*5;
					}else if(count>20){
						num=100;
					}
					
					if(Constants.user.coinCount<num){
//						DialogNote dialog=new DialogNote(context.popupParent, context, context.black_layout, 1);
						Intent intent=new Intent(context,DialogNoteActivity.class);
						intent.putExtra("mode", 10);
						intent.putExtra("info", "钱包君告急！挣够金币再来捧萌星吧");
						context.startActivity(intent);
					}else{
					
					
					DialogJoinKingdom dialog=new DialogJoinKingdom(context.popupParent, context, context.black_layout,data);
					dialog.setResultListener(new ResultListener() {
						
						@Override
						public void getResult(boolean isSuccess) {
							// TODO Auto-generated method stub
							if(isSuccess){
								if(HomeActivity.homeActivity!=null&&HomeActivity.homeActivity.myPetFragment!=null){
									HomeActivity.homeActivity.myPetFragment.homeMyPet.refresh();
								}
								context.finish();
								if(ChoseAcountTypeActivity.choseAcountTypeActivity!=null){
									ChoseAcountTypeActivity.choseAcountTypeActivity.finish();
									ChoseAcountTypeActivity.choseAcountTypeActivity=null;
								}
							}
						}
					});
					}
					return;
				}
				Intent intent=new Intent(context,NewRegisterActivity.class);
				intent.putExtra("mode", mode);
				intent.putExtra("animal", data);
				intent.putExtra("isBind", isBind);
				if(isBind)intent.putExtra("user", user);
				context.startActivity(intent);
			}
		});
		if(Constants.user!=null&&Constants.user.aniList!=null&&Constants.user.aniList.contains(data)){
			holder.join.setBackgroundResource(R.drawable.support_gray);
			holder.join.setClickable(false);
		}
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+data.pet_iconUrl, holder.petIcon, displayImageOptions);
		holder.petName.setText(""+data.pet_nickName);
		holder.petAge.setText(""+data.a_age_str);
		holder.petRace.setText(""+data.race);
		final LinearLayout temp=holder.infoLayout;
		final View lineView=holder.line;
		holder.briefInfoLayout.setTag(holder);
		final LinearLayout imagsLayout=holder.imagesLayout;
		holder.briefInfoLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				if(currentShowInfoLayout==temp){
					currentShowInfoLayout.setVisibility(View.GONE);
					if(hidenLineView!=null){
						hidenLineView.setVisibility(View.VISIBLE);
					}
					hidenLineView=null;
					currentShowInfoLayout=null;
					return;
				}
				if(currentShowInfoLayout!=null){
					currentShowInfoLayout.setVisibility(View.GONE);
				}
				if(hidenLineView!=null){
					hidenLineView.setVisibility(View.VISIBLE);
				}
				temp.setVisibility(View.VISIBLE);
				lineView.setVisibility(View.GONE);
				currentShowInfoLayout=temp;
				hidenLineView=lineView;
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final Holder holder=(Holder)v.getTag(); 
						final KingdomCard  card=HttpUtil.cardApi(context,data.a_id,handleHttpConnectionException.getHandler(context));
						
						handleHttpConnectionException.getHandler(context).post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ImageLoader imageLoader=ImageLoader.getInstance();
								if(card!=null){
									if(card.list!=null){
										if(card.list.size()>=1){
											holder.image1.setTag(card.list.get(0));
											imageLoader.displayImage(Constants.UPLOAD_IMAGE_RETURN_URL+card.list.get(0).url, holder.image1, displayImageOptions2,new MyImageLoaderListener(imagsLayout));
										}
										if(card.list.size()>=2){
											holder.image1.setTag(card.list.get(0));
											imageLoader.displayImage(Constants.UPLOAD_IMAGE_RETURN_URL+card.list.get(1).url, holder.image2, displayImageOptions2,new MyImageLoaderListener(imagsLayout));
										}
										if(card.list.size()>=3){
											holder.image1.setTag(card.list.get(0));
											imageLoader.displayImage(Constants.UPLOAD_IMAGE_RETURN_URL+card.list.get(2).url, holder.image3, displayImageOptions2,new MyImageLoaderListener(imagsLayout));
										}
										if(card.list.size()>=4){
											holder.image1.setTag(card.list.get(0));
											imageLoader.displayImage(Constants.UPLOAD_IMAGE_RETURN_URL+card.list.get(3).url, holder.image4, displayImageOptions2,new MyImageLoaderListener(imagsLayout));
										}
										
									}
								}
								if(card.user!=null){
									imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+card.user.u_iconUrl, holder.userIcon, displayImageOptions3);
									if(card.user.u_gender==1){
										holder.userSex.setImageResource(R.drawable.male1);
									}else{
										holder.userSex.setImageResource(R.drawable.female1);
									}
//									holder.cityTV.setText(card.user.city);
									holder.provinceTV.setText(card.user.province+" | "+card.user.city);
									holder.userName.setText(""+card.user.u_nick);
								}
							}
						});
					}
				}).start();
				
			}
		});
		return convertView;
	}
	class Holder{
		RoundImageView  petIcon,userIcon;
		ImageView  petSex,userSex,image1,image2,image3,image4;
		TextView petName,userName,petRace,petAge,/*cityTV,*/provinceTV/*,userJob*/,fansTV,t_rq_tv;
		TextView join;
		View line;
		LinearLayout infoLayout,briefInfoLayout,imagesLayout;
	}
	boolean isRecord=false;
	int imageWidth=0;
	class MyImageLoaderListener implements ImageLoadingListener{
		LinearLayout parent;
        public MyImageLoaderListener(LinearLayout parent){
        	this.parent=parent;
        }
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub
			int width=0;
			if(!isRecord){
				isRecord=true;
				parent.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				width=parent.getMeasuredWidth()/2;
				imageWidth=width;
			}else{
				width=imageWidth;
			}
			Bitmap loadedImage=BitmapFactory.decodeResource(context.getResources(), R.drawable.image_default);
			
			int height=(int)(width*2f/3f);
			float scale1=width*1f/(loadedImage.getWidth()*1f);
			float scale2=height*1f/(loadedImage.getHeight()*1f);
			float scale=scale1<scale2?scale2:scale1;
			Matrix matrix=new Matrix();
			matrix.postScale(scale, scale);
			loadedImage=Bitmap.createBitmap(loadedImage,0, 0, loadedImage.getWidth(), loadedImage.getHeight(),matrix,true);
			loadedImage=Bitmap.createBitmap(loadedImage, (loadedImage.getWidth()-width)/2, (loadedImage.getHeight()-height)/2, width, height);
			ImageView v=(ImageView)view;
			v.setImageBitmap(loadedImage);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub
			int width=0;
			if(!isRecord){
				isRecord=true;
				parent.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				width=parent.getMeasuredWidth()/2;
				imageWidth=width;
			}else{
				width=imageWidth;
			}
			
			
			int height=(int)(width*2f/3f);
			float scale1=width*1f/(loadedImage.getWidth()*1f);
			float scale2=height*1f/(loadedImage.getHeight()*1f);
			float scale=scale1<scale2?scale2:scale1;
			Matrix matrix=new Matrix();
			matrix.postScale(scale, scale);
			loadedImage=Bitmap.createBitmap(loadedImage,0, 0, loadedImage.getWidth(), loadedImage.getHeight(),matrix,true);
			loadedImage=Bitmap.createBitmap(loadedImage, (loadedImage.getWidth()-width)/2, (loadedImage.getHeight()-height)/2, width, height);
			ImageView v=(ImageView)view;
			v.setImageBitmap(loadedImage);
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
