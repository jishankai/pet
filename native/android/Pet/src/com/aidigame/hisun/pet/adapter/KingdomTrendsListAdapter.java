package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.PetNews;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.ui.PetTrendsActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * 王国资料界面  最新动态列表，适配器
 * @author admin
 *
 */
public class KingdomTrendsListAdapter extends BaseAdapter {
	ArrayList<PetNews> list;
	DisplayImageOptions displayImageOptions;//显示图片的格式
	PetTrendsActivity context;
	Animal animal;
	ArrayList<Gift> giftList;
	String lastJob;
	public KingdomTrendsListAdapter(ArrayList<PetNews> list,PetTrendsActivity context,Animal animal){
		this.list=list;
		this.context=context;
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		this.animal=animal;
		lastJob=StringUtil.getUserJobs()[0];
		giftList=StringUtil.getGiftList(context);
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.noimg)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		        .decodingOptions(options)
                .build();
	}
	public void update(ArrayList<PetNews> list){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_kingdom_trends, null);
			holder=new Holder();
			holder.fansLayout=(LinearLayout)convertView.findViewById(R.id.trends_fans_layout);
			holder.kingdomLayout=(LinearLayout)convertView.findViewById(R.id.trends_join_kingdom_layout);
			holder.imageLayout=(LinearLayout)convertView.findViewById(R.id.trends_publish_images_layout);
			holder.makeTroubleLayout=(LinearLayout)convertView.findViewById(R.id.trends_make_trouble_layout);
			holder.sendGiftLayout=(LinearLayout)convertView.findViewById(R.id.trends_send_gift_layout);
			holder.biteLayout=(LinearLayout)convertView.findViewById(R.id.trends_bite_layout);
			holder.playWithLayout=(LinearLayout)convertView.findViewById(R.id.trends_play_with_layout);
			holder.fansDesTv=(TextView)convertView.findViewById(R.id.fans_describe_tv);
			holder.fansTimeTv=(TextView)convertView.findViewById(R.id.fans_time_tv);
			holder.kingdomDesTv=(TextView)convertView.findViewById(R.id.join_kingdom_describe_tv);
			holder.kingdomTimeTv=(TextView)convertView.findViewById(R.id.join_kingdom_time_tv);
			holder.imagesDesTv=(TextView)convertView.findViewById(R.id.publish_images_describe_tv);
			holder.imagesTimeTv=(TextView)convertView.findViewById(R.id.publish_images_time_tv);
		    holder.imageIv=(ImageView)convertView.findViewById(R.id.publish_image_iv);
		   /* makeTroubleDesTv,makeTroubleTimeTv,sendGiftDesTv,sendGiftTimeTv,
	         biteDesTv,biteTimeTv,playWithDesTv,playWithTimeTv;
	         playWithIV,sendGiftIv,biteIv;*/
		    holder.makeTroubleDesTv=(TextView)convertView.findViewById(R.id.make_trouble_describe_tv);
		    holder.makeTroubleTimeTv=(TextView)convertView.findViewById(R.id.make_trouble_time_tv);
		    holder.sendGiftDesTv=(TextView)convertView.findViewById(R.id.send_gift_describe_tv);
		    holder.sendGiftTimeTv=(TextView)convertView.findViewById(R.id.send_gift_time_tv);
		    holder.biteDesTv=(TextView)convertView.findViewById(R.id.bite_describe_tv);
		    holder.biteTimeTv=(TextView)convertView.findViewById(R.id.bite_time_tv);
		    holder.playWithDesTv=(TextView)convertView.findViewById(R.id.play_with_describe_tv);
		    holder.playWithTimeTv=(TextView)convertView.findViewById(R.id.play_with_time_tv);
		    holder.playWithIV=(ImageView)convertView.findViewById(R.id.play_with_iv);
		    holder.sendGiftIv=(ImageView)convertView.findViewById(R.id.send_gift_iv);
		    holder.biteIv=(ImageView)convertView.findViewById(R.id.bite_iv);
		    convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final PetNews petNews=list.get(position);
		holder.biteLayout.setVisibility(View.GONE);
		holder.fansLayout.setVisibility(View.GONE);
		holder.imageLayout.setVisibility(View.GONE);
		holder.kingdomLayout.setVisibility(View.GONE);
		holder.makeTroubleLayout.setVisibility(View.GONE);
		holder.playWithLayout.setVisibility(View.GONE);
		holder.sendGiftLayout.setVisibility(View.GONE);
		String htmlStr1="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String timeString=sdf.format(new Date(petNews.create_time));
		timeString=judgeTime(petNews.create_time);
		//萌星<宠物昵称>信心满满地参加了<活动名称>大赛，明日之星就是TA！  参加活动
		if(petNews.type==1){
			holder.fansLayout.setVisibility(View.VISIBLE);
			htmlStr1="<html>"
		             +"<body>"
		                +"<font color=\"#fb6137\">"
		                +petNews.u_name+" "
		                +"</font>"
		                +"成为了 "
		                +"<font color=\"#fb6137\">"
		                +animal.pet_nickName+" "
		                +"</font>"
		                +" 的粉丝"
		             +"</body>"
		      + "</html>";
            holder.fansDesTv.setText(Html.fromHtml(htmlStr1));
            holder.fansTimeTv.setText(timeString);
		}else if(petNews.type==2){
			htmlStr1="<html>"
		             +"<body>"
//					    +"路人"
		                +"<font color=\"#fb6137\">"
		                +petNews.u_name+" "
		                +"</font>"
		                +"被萌星 "
		                +"<font color=\"#fb6137\">"
		                +animal.pet_nickName+" "
		                +"</font>"
		                +"的魅力折服，路人转粉啦~"
//		                +"<font color=\"#fb6137\">"
//		                +lastJob
//		                +"</font>"
		             +"</body>"
		      + "</html>";
			holder.kingdomLayout.setVisibility(View.VISIBLE);
			holder.kingdomDesTv.setText(Html.fromHtml(htmlStr1));
			holder.kingdomTimeTv.setText(timeString);
		}else if(petNews.type==3){
			holder.imageLayout.setVisibility(View.VISIBLE);
			htmlStr1="<html>"
		             +"<body>"
					   +"经纪人"
		                +"<font color=\"#fb6137\">"
		                +petNews.u_name+" "
		                +"</font>"
		                +"发布了一张萌星 "
		                +"<font color=\"#fb6137\">"
		                +animal.pet_nickName+" "
		                +"</font>"
		                +"的照片。"
		             +"</body>"
		      + "</html>";
			holder.imagesDesTv.setText(Html.fromHtml(htmlStr1));
			holder.imagesTimeTv.setText(timeString);
//			ImageLoader imageLoader=ImageLoader.getInstance();
			int h=context.getResources().getDimensionPixelSize(R.dimen.one_dip)*90;
//			imageLoader.displayImage(Constants.UPLOAD_IMAGE_THUBMAIL_IMAG+petNews.img_url+"@"+h+"h_0l.jpg", holder.imageIv, displayImageOptions);
			
			
			 ImageFetcher mImageFetcher=new ImageFetcher(context, 0);
				mImageFetcher.setWidth(0);
				mImageFetcher.IP=mImageFetcher.UPLOAD_THUMBMAIL_IMAGE;
				mImageFetcher.setImageCache(new ImageCache(context, new ImageCacheParams(Constants.UPLOAD_IMAGE_THUBMAIL_IMAG+petNews.img_url+"@"+h+"h_0l.jpg")));
				mImageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
					
					@Override
					public void onComplete(Bitmap bitmap) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void getPath(String path) {
						// TODO Auto-generated method stub
						File f=new File(path);
						for(int i=0;i<list.size();i++){
							if(list.get(i).type==3&&f.getName().contains(list.get(i).img_url)){
								list.get(i).petPicture_path=f.getName();
							}
						}
						
					}
				});
				
				mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/petNews.img_url+"@"+h+"h_0l.jpg", holder.imageIv, /*options*/null);
			
			
			
			
			
		}else if(petNews.type==4){
			Gift gift=new Gift();
			gift.no=petNews.item_id;
			int index=giftList.indexOf(gift);
			gift=giftList.get(index);
			holder.sendGiftLayout.setVisibility(View.VISIBLE);
			if(gift.add_rq>0){
				htmlStr1="<html>"
			             +"<body>"
			                +petNews.job
						    +""
			                +"<font color=\"#fb6137\">"
			                +petNews.u_name+" "
			                +"</font>"
			                +"精心挑选了一个"
			                +"<font color=\"#fb6137\">"
			                +gift.name+" "
			                +"</font>"
			                +"送给"
			                +"<font color=\"#fb6137\">"
			                +animal.pet_nickName+" "
			                +"</font>"
			                +"，"
			                +gift.effect_des
			                +"人气"
			                +"<font color=\"#fb6137\">"
			                +"+"+gift.add_rq+" "
			                +"</font>"
			             +"</body>"
			      + "</html>";
			}else{
				htmlStr1="<html>"
			             +"<body>"
			                +petNews.job
						    +""
			                +"<font color=\"#fb6137\">"
			                +petNews.u_name+" "
			                +"</font>"
			                +"腹黑一笑，对 "
			                +"<font color=\"#fb6137\">"
			                +animal.pet_nickName+" "
			                +"</font>"
			                +"扔了一个"
			                +"<font color=\"#fb6137\">"
			                +gift.name+" "
			                +"</font>"
			                +","
			                +gift.effect_des
			                +"人气"
			                +"<font color=\"#fb6137\">"
			                +""+gift.add_rq+" "
			                +"</font>"
			             +"</body>"
			      + "</html>";
			}
			
			holder.sendGiftDesTv.setText(Html.fromHtml(htmlStr1));
			holder.sendGiftTimeTv.setText(timeString);
		}else if(petNews.type==5){
			holder.biteLayout.setVisibility(View.VISIBLE);
			htmlStr1="<html>"
		             +"<body>"
					    +"萌星"
		                +"<font color=\"#fb6137\">"
		                +animal.pet_nickName+" "
		                +"</font>"
		                +"今天心情很nice~乖巧地叫了一声。 "
		             +"</body>"
		      + "</html>";
			holder.biteDesTv.setText(Html.fromHtml(htmlStr1));
			holder.biteTimeTv.setText(timeString);
		}else if(petNews.type==6){
			holder.playWithLayout.setVisibility(View.VISIBLE);
			htmlStr1="<html>"
		             +"<body>"
					    +petNews.job
		                +"<font color=\"#fb6137\">"
		                +petNews.u_name
		                +" "
		                +"</font>"
		                +"在游乐园中为萌星 "
		                +"<font color=\"#fb6137\">"
		                +animal.pet_nickName
		                +"</font>"
		                +"消灭了"
		                +"<font color=\"#fb6137\">"
		                +"99 "
		                +"</font>"
		                +"只虫子！"
		             +"</body>"
		      + "</html>";
			holder.playWithDesTv.setText(Html.fromHtml(htmlStr1));
			holder.playWithTimeTv.setText(timeString);
		}else if(petNews.type==7){
			Gift gift=new Gift();
			gift.no=petNews.item_id;
			int index=giftList.indexOf(gift);
			if(index>=0){
			gift=giftList.get(index);
			holder.makeTroubleLayout.setVisibility(View.VISIBLE);
			htmlStr1="<html>"
		             +"<body>"
		                +petNews.job
					    +""
		                +"<font color=\"#fb6137\">"
		                +petNews.u_name+" "
		                +"</font>"
		                +"腹黑一笑，对 "
		                +"<font color=\"#fb6137\">"
		                +animal.pet_nickName+" "
		                +"</font>"
		                +"扔了一个"
		                +"<font color=\"#fb6137\">"
		                +gift.name+" "
		                +"</font>"
		                +","
		                +gift.effect_des
		                +"人气"
		                +"<font color=\"#fb6137\">"
		                +""+gift.add_rq+" "
		                +"</font>"
		             +"</body>"
		      + "</html>";
			holder.makeTroubleDesTv.setText(Html.fromHtml(htmlStr1));
			holder.makeTroubleTimeTv.setText(timeString);
			}
			
			
			
		}
		
		
		holder.sendGiftIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				context.clickItem2();
			}
		});
		holder.biteIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				context.clickItem3();
			}
		});
		holder.playWithIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				context.clickItem4();
			}
		});
		
		holder.imageLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PetPicture pp=new PetPicture();
				pp.img_id=petNews.img_id;
				pp.url=petNews.img_url;
				pp.petPicture_path=petNews.petPicture_path;
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				Intent intent=new Intent(context,NewShowTopicActivity.class);
				intent.putExtra("PetPicture", pp);
				context.startActivity(intent);
			}
		});
		
		
		
		
	
		return convertView;
	}
	class Holder{
		LinearLayout fansLayout,kingdomLayout,imageLayout,makeTroubleLayout,sendGiftLayout,biteLayout,playWithLayout;
		TextView fansDesTv,kingdomDesTv,imagesDesTv,fansTimeTv,kingdomTimeTv,imagesTimeTv,
		         makeTroubleDesTv,makeTroubleTimeTv,sendGiftDesTv,sendGiftTimeTv,
		         biteDesTv,biteTimeTv,playWithDesTv,playWithTimeTv;
		ImageView imageIv,playWithIV,sendGiftIv,biteIv;
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
			sb.append(  str+time/(60*60*24*30)+"个月");
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

}
