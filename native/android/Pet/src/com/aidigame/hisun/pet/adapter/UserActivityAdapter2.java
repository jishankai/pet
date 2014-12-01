package com.aidigame.hisun.pet.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.NewRegisterActivity;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.TopicView;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 用户参加的活动列表
 */
public class UserActivityAdapter2 extends BaseAdapter  {
	DisplayImageOptions displayImageOptions;//显示图片的格式
    ImageLoader imageLoader;
	Activity context;
	ArrayList<PetPicture> petPictures;
	int mode;//1 个人主页;2其他人主页 
	HandleHttpConnectionException handleHttpConnectionException;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==Constants.ERROR_MESSAGE){
				ShowDialog.show((String)msg.obj, context);
			}else{
				TextView tv=(TextView)msg.obj;
				tv.setText(""+msg.arg1);
				notifyDataSetChanged();
			}

		};
	};
	public UserActivityAdapter2(Context context,ArrayList<PetPicture> petPictures,int mode){
		this.context=(Activity)context;
		this.petPictures=petPictures;
		this.mode=mode;
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
	        .cacheInMemory(true)
	        .cacheOnDisc(true)
	        .bitmapConfig(Bitmap.Config.RGB_565)
	        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	        .decodingOptions(options)
            .build();
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
	    	holder.tv4=(TextView)convertView.findViewById(R.id.textView4);
	    	holder.tv6=(TextView)convertView.findViewById(R.id.textView6);
	    	convertView.setTag(holder);
	    }else{
	    	holder=(Holder)convertView.getTag();
	    }
	    
	   final  PetPicture data=petPictures.get(position);
	   
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	    
	           holder.tv4.setText(sdf.format(new java.util.Date(petPictures.get(position).create_time*1000)));
		    	holder.tv6.setText("#"+petPictures.get(position).topic_name+"#");
	           convertView.setVisibility(View.VISIBLE);
		    	Object isFirst=holder.image.getTag();
				    loadTopicImage(holder.image,data);

		return convertView;
	}
	public void loadTopicImage(TopicView topic,final PetPicture data){
		imageLoader =ImageLoader.getInstance();
    	imageLoader.displayImage(Constants.UPLOAD_IMAGE_RETURN_URL+data.url, topic, displayImageOptions, new ImageLoadingListener() {
			
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
		TopicView image;
		TextView tv4,tv6;
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
		return sb.toString();
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
				break;
			case 3:
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				Intent intent3=new Intent(context,NewShowTopicActivity.class);
				intent3.putExtra("PetPicture", petPictures.get(position));
				if(mode==1){
					intent3.putExtra("from", "UserHomepageActivity");
				}else{
					intent3.putExtra("from", "OtherUserTopicActivity");
					if(NewShowTopicActivity.newShowTopicActivity!=null)
						NewShowTopicActivity.newShowTopicActivity.finish();
				}
				context.startActivity(intent3);
              
				
//				context.finish();
				break;
			}
		}
		
	}

}
