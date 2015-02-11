package com.aidigame.hisun.pet.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.NewPetKingdomActivity;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.TopicView;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 王国资料界面    发布的图片列表
 */
public class ShowTopicsAdapter2 extends BaseAdapter  {
	Activity context;
	ArrayList<PetPicture> petPictures;
	BitmapFactory.Options options;
	ImageFetcher mImageFetcher;
	int mode;//1 个人主页;2其他人主页 
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
	public ShowTopicsAdapter2(Context context,ArrayList<PetPicture> petPictures,int mode){
		this.context=(Activity)context;
		this.petPictures=petPictures;
		this.mode=mode;
	options=new BitmapFactory.Options();
	options.inJustDecodeBounds=false;
	options.inSampleSize=StringUtil.getScaleByDPI(this.context);
	options.inPreferredConfig=Bitmap.Config.RGB_565;
	options.inPurgeable=true;
	options.inInputShareable=true;
	mImageFetcher=new ImageFetcher(context, Constants.screen_width);
	}
	public void updateTopics(ArrayList<PetPicture> petPictures){
		this.petPictures=petPictures;
		this.notifyDataSetInvalidated();
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		LogUtil.i("scroll", "getView:："+"position="+position);
		Holder  holder=null;
	    if(convertView==null){
	    	holder=new Holder();
	    	convertView=LayoutInflater.from(context).inflate(R.layout.item_listview_topic1, null);
	    	holder.image=(TopicView)convertView.findViewById(R.id.imageView2);
	    	holder.heart=(ImageView)convertView.findViewById(R.id.imageView3);
	    	holder.tv4=(TextView)convertView.findViewById(R.id.textView4);
	    	holder.tv5=(TextView)convertView.findViewById(R.id.textView5);
	    	holder.lLayout=(LinearLayout)convertView.findViewById(R.id.heart_linearLayout);
	    	convertView.setTag(holder);
	    }else{
	    	holder=(Holder)convertView.getTag();
	    }
	    PetPicture data=petPictures.get(position);
	    //TODO  
	    if(PetApplication.myUser!=null&&data.likers!=null&&data.likers.contains(""+PetApplication.myUser.userId)){
	    	holder.heart.setImageResource(R.drawable.bone_red);
	    	 holder.tv5.setTextColor(context.getResources().getColor(R.color.orange_red));
		}else{
			holder.heart.setImageResource(R.drawable.bone_white);
			holder.tv5.setTextColor(Color.WHITE);
		}
		    	convertView.setVisibility(View.VISIBLE);
		    	Object isFirst=holder.image.getTag();
				    loadTopicImage(holder.image,data);
				    holder.lLayout.setClickable(true);
				    holder.lLayout.setOnClickListener(new ItemClickListener(holder, position, 2));
				    holder.image.setOnClickListener(new ItemClickListener(holder, position, 3));
				    if(data.animal!=null){
					    holder.tv4.setText(""+judgeTime(data.create_time));
					    holder.tv5.setText(""+data.likes);
					    	
				    }else{
					    holder.tv4.setText(""+judgeTime(data.create_time));
					    holder.tv5.setText(""+data.likes);
					    
					    	
				    }

		return convertView;
	}
	public void loadTopicImage(TopicView topic,final PetPicture data){
		mImageFetcher.setWidth(Constants.screen_width);
		mImageFetcher.setImageCache(new ImageCache(context, new ImageCacheParams(data.url)));
		mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/data.url, topic, options);
	}
	class Holder{
		TopicView image;
		ImageView heart;
		TextView tv4,tv5;
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
			sb.append( str+time/(60)+"分钟");
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
//                context.finish();
				break;
			case 2:
				if(context instanceof NewPetKingdomActivity){
					NewPetKingdomActivity p=(NewPetKingdomActivity)context;
					if(!UserStatusUtil.isLoginSuccess(context, p.popupParent, p.black_layout)){
						return;
					}
					
				}
				
				final PetPicture petPicture=petPictures.get(position);
				final TextView tv=holder.tv5;
				final ImageView iv=holder.heart;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						boolean flag=HttpUtil.likeImage(petPicture,handler,context);
						if(flag){
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									tv.setText(""+petPicture.likes);
									tv.setTextColor(context.getResources().getColor(R.color.orange_red));
									iv.setImageResource(R.drawable.bone_red);
								}
							});
							
						}
					}
				}).start();
				break;
			case 3:
				/*if(ShowTopicActivity.showTopicActivity!=null){
					if(ShowTopicActivity.showTopicActivity.getBitmap()!=null){
						if(!ShowTopicActivity.showTopicActivity.getBitmap().isRecycled())
							ShowTopicActivity.showTopicActivity.getBitmap().recycle();
					}
					ShowTopicActivity.showTopicActivity.imageView.setImageDrawable(null);
					ShowTopicActivity.showTopicActivity.finish();
				}
				Intent intent3=new Intent(context,ShowTopicActivity.class);
				intent3.putExtra("PetPicture", petPictures.get(position));
				intent3.putExtra("from_w", 2);
				ShowTopicActivity.petPictures=petPictures;
				context.startActivity(intent3);*/
				
				
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				Intent intent6=new Intent(context,NewShowTopicActivity.class);
				intent6.putExtra("PetPicture", petPictures.get(position));
				intent6.putExtra("from_w", 2);
				context.startActivity(intent6);
				
				break;
			}
		}
		
	}

}
