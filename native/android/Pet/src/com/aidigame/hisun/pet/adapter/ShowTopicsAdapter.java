package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.ShowTopicActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.view.TopicView;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.aviary.android.feather.library.graphics.drawable.FakeBitmapDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 主页 关注图片列表
 * @author admin
 *
 */
public class ShowTopicsAdapter extends BaseAdapter  {
    DisplayImageOptions displayImageOptions;//显示图片的格式

	DisplayImageOptions displayImageOptions2;//显示图片的格式
    ImageLoader imageLoader;
	Activity context;
//	ArrayList<UserImagesJson.Data> datas;
	ArrayList<PetPicture> petPictures;
	int mode;//1 个人主页和其他人主页 不需要用户信息，2 关注列表
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==Constants.ERROR_MESSAGE){
				ShowDialog.show((String)msg.obj, context);
			}else{
				/*TextView tv=(TextView)msg.obj;
				tv.setText(""+msg.arg1);
				notifyDataSetChanged();*/
			}

		};
	};
	public ShowTopicsAdapter(Context context,ArrayList<PetPicture> petPictures){
		this.context=(Activity)context;
		this.petPictures=petPictures;
		this.mode=mode;
		 //显示没有图片
	    Bitmap nobmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.noimg);
		Matrix matrix=new Matrix();
		matrix.postScale(Constants.screen_width/(nobmp.getWidth()*1f), Constants.screen_width/(nobmp.getWidth()*1f));
		nobmp=Bitmap.createBitmap(nobmp, 0, 0, nobmp.getWidth(), nobmp.getHeight(),matrix,true);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=4;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
                .showImageOnLoading(new BitmapDrawable(nobmp))
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		
		
		
		BitmapFactory.Options ops=new BitmapFactory.Options();
		ops.inJustDecodeBounds=false;
		ops.inSampleSize=8;
		ops.inPreferredConfig=Bitmap.Config.RGB_565;
		ops.inPurgeable=true;
		ops.inInputShareable=true;
		displayImageOptions2=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.noimg)
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(ops)
                .build();
	}
	public void updateTopics(ArrayList<PetPicture> petPictures){
		this.petPictures=petPictures;
		this.notifyDataSetChanged();
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		LogUtil.i("scroll","datas大小========="+petPictures.size());
		return petPictures.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return petPictures.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder  holder=null;
		LogUtil.i("scroll","showTopicsAdapter.getView========="+position);
	    if(convertView==null){
	    	holder=new Holder();
	    	convertView=LayoutInflater.from(context).inflate(R.layout.item_listview_topic, null);
	    	holder.icon=(RoundImageView)convertView.findViewById(R.id.imageView1);
	    	holder.heart=(ImageView)convertView.findViewById(R.id.imageView3);
	    	holder.image=(TopicView)convertView.findViewById(R.id.imageView2);
	    	holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
	    	holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
	    	holder.tv3=(TextView)convertView.findViewById(R.id.textView3);
	    	holder.tv4=(TextView)convertView.findViewById(R.id.textView4);
	    	holder.tv5=(TextView)convertView.findViewById(R.id.textView5);
	    	holder.rLayout=(RelativeLayout)convertView.findViewById(R.id.relativeLayout1);
	    	holder.lLayout=(LinearLayout)convertView.findViewById(R.id.heart_linearLayout);
	    	convertView.setTag(holder);
	    }else{
	    	holder=(Holder)convertView.getTag();
	    }
	    final PetPicture data=petPictures.get(position);
	    //TODO  35��ǰ   1Сʱǰ    1�� ǰ
	    holder.lLayout.setClickable(true);
	    holder.rLayout.setClickable(true);
	    holder.lLayout.setOnClickListener(new ItemClickListener(holder, position, 2));
	    holder.rLayout.setOnClickListener(new ItemClickListener(holder, position, 1));
	    holder.image.setOnClickListener(new ItemClickListener(holder, position, 3));
	    Object isFirst=holder.image.getTag();
	   
	    loadTopicImage(holder.image,data);
	    if(Constants.user!=null&&data.likers!=null&&data.likers.contains(""+Constants.user.userId)){
	    	if(data.animal.type>200&&data.animal.type<300){
	    		holder.heart.setImageResource(R.drawable.bone_red);
			}else if(data.animal.type>100&&data.animal.type<200){
				holder.heart.setImageResource(R.drawable.fish_red);
			}
	    	holder.tv5.setTextColor(context.getResources().getColor(R.color.orange_red));;
		}else{
			if(data.animal.type>200&&data.animal.type<300){
	    		holder.heart.setImageResource(R.drawable.bone_white);
			}else if(data.animal.type>100&&data.animal.type<200){
				holder.heart.setImageResource(R.drawable.fish_white);
			}
			holder.tv5.setTextColor(Color.WHITE);
		}
	    if(data.animal!=null){
	    	holder.tv1.setText(""+data.animal.pet_nickName);
		    holder.tv3.setText(""+judgeTime(data.create_time));
		    holder.tv4.setText(data.cmt);
		    holder.tv5.setText(""+data.likes);
		   BitmapFactory.Options options=new BitmapFactory.Options();
		    options.inSampleSize=8;
		    options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			loadIcon(holder.icon, data);
		   holder.tv2.setText(""+data.animal.race);
			    
		    
		    holder.tv3.setText(""+judgeTime(data.create_time));
		    holder.tv4.setText(data.cmt);
		    holder.tv5.setText(""+data.likes);

		    	
	    }else{
		   
	    }
		return convertView;
	}
	public void loadTopicImage(TopicView topic,final PetPicture data){
		imageLoader =ImageLoader.getInstance();
		
    	imageLoader.displayImage(Constants.UPLOAD_IMAGE_RETURN_URL+data.url, topic, displayImageOptions2, new ImageLoadingListener() {
			
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
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void loadIcon(RoundImageView icon,final PetPicture data){
		
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+data.animal.pet_iconUrl, icon, displayImageOptions, new ImageLoadingListener() {
			
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
				
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	class Holder{
		RoundImageView icon;
		TopicView image;
		ImageView heart;
		TextView tv1,tv2,tv3,tv4,tv5;
		RelativeLayout rLayout;
		LinearLayout lLayout;
	}
	public String judgeTime(long time2){
		long time1=System.currentTimeMillis()/1000;
		long time=time1-time2;
         String str="";
         StringBuffer sb=new StringBuffer();
         sb.append("");
         int mode=0;
         if(time<0){
        	 time=-time;
        	 mode=1;
        	 sb.append("未来");
         }
		if(time<60){
			sb.append( str+time+"秒");
		}else if(time/(60)<60){
			sb.append( str+time/(60)+"分");
		}else if(time/(60*60)<24){
			sb.append(  str+time/(60*60)+"个小时");
		}else if(time/(60*60*24)<30){
			sb.append(  str+time/(60*60*24)+"天");
		}else if(time/(60*60*24*30)<12){
			sb.append(  str+time/(60*60*24)+"个月");
		}else if(time/(60*60*24*30*12)<1000){
			sb.append( str+time/(60*60*24*30*12)+"年");
		}
		if(mode==0){
			sb.append("前");
		}else{
			sb.append("后");
		}
		if(time<60){
			return "刚刚";
		}else{
			return sb.toString();
		}
	}
	public int[] getTimeArray(long time){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String temp=sdf.format(new Date(time));
		String[] strs=temp.split("-");
		int[] times=new int[strs.length];
		for(int i=0;i<strs.length;i++){
			times[i]=Integer.parseInt(strs[i]);
		}
		return times;
	}
	class ItemClickListener implements OnClickListener{
        Holder holder;
        int position; 
        int type;//1 �û���Ϣ��2 ���ޣ�3 �鿴topic
        public ItemClickListener(Holder holder,int position,int type){
        	this.holder=holder;
        	this.position=position;
        	this.type=type;
        }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LogUtil.i("me", "type="+type);
			switch (type) {
			case 1:
				/*if(datas.get(position).user==null||datas.get(position).usr_id==Constants.user.userId){
					Intent intent1=new Intent(context,UserHomepageActivity.class);
					if(context instanceof HomeActivity)
						intent1.putExtra("from", "HomeActivity");
					context.startActivity(intent1);
				}else{
					Intent intent1=new Intent(context,OtherUserTopicActivity.class);
					intent1.putExtra("data", datas.get(position));
					if(context instanceof HomeActivity)
						intent1.putExtra("from", "HomeActivity");
					context.startActivity(intent1);
				}*/
				if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.finish();
				Intent intent1=new Intent(context,PetKingdomActivity.class);
				intent1.putExtra("animal", petPictures.get(position).animal);
				intent1.putExtra("from", "HomeActivity");
				context.startActivity(intent1);
//                context.finish();
				break;
			case 2:
				final PetPicture data=petPictures.get(position);
				final TextView tv=holder.tv5;
				final ImageView iv=holder.heart;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						boolean flag=HttpUtil.likeImage(petPictures.get(position),handler);
						if(flag){
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									tv.setText(""+data.likes);
									tv.setTextColor(context.getResources().getColor(R.color.orange_red));
//									iv.setImageResource(R.drawable.fish_red);
									if(data.animal.type>200&&data.animal.type<300){
										iv.setImageResource(R.drawable.bone_red);
									}else if(data.animal.type>100&&data.animal.type<200){
										iv.setImageResource(R.drawable.fish_red);
									}
								}
							});
						}
					}
				}).start();
				break;
			case 3:
				if(ShowTopicActivity.showTopicActivity!=null)ShowTopicActivity.showTopicActivity.finish();
				ShowTopicActivity.petPictures=petPictures;
				Intent intent3=new Intent(context,ShowTopicActivity.class);
				intent3.putExtra("PetPicture", petPictures.get(position));
				intent3.putExtra("mode",2);
				context.startActivity(intent3);
				
				
//				context.finish();
				break;
			}
		}
		
	}

}
