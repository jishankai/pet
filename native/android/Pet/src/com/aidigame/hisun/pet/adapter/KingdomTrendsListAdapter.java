package com.aidigame.hisun.pet.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.PetNews;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 王国资料界面  最新动态列表，适配器
 * @author admin
 *
 */
public class KingdomTrendsListAdapter extends BaseAdapter {
	ArrayList<PetNews> list;
	DisplayImageOptions displayImageOptions;//显示图片的格式
	Context context;
	Animal animal;
	ArrayList<Gift> giftList;
	String lastJob;
	public KingdomTrendsListAdapter(ArrayList<PetNews> list,Context context,Animal animal){
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
		        .decodingOptions(options)
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
		PetNews petNews=list.get(position);
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
					    +"路人"
		                +"<font color=\"#fb6137\">"
		                +petNews.u_name+" "
		                +"</font>"
		                +"加入了 "
		                +"<font color=\"#fb6137\">"
		                +animal.pet_nickName+" "
		                +"</font>"
		                +"联萌，成为了一枚新出炉的"
		                +"<font color=\"#fb6137\">"
		                +lastJob
		                +"</font>"
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
		                +"发布了一张萌主 "
		                +"<font color=\"#fb6137\">"
		                +animal.pet_nickName+" "
		                +"</font>"
		                +"的照片。"
		             +"</body>"
		      + "</html>";
			holder.imagesDesTv.setText(Html.fromHtml(htmlStr1));
			holder.imagesTimeTv.setText(timeString);
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Constants.UPLOAD_IMAGE_RETURN_URL+petNews.img_url, holder.imageIv, displayImageOptions);
			
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
			                +"送了"
			                +"<font color=\"#fb6137\">"
			                +animal.pet_nickName+" "
			                +"</font>"
			                +"一个"
			                +"<font color=\"#fb6137\">"
			                +gift.name+" "
			                +"</font>"
			                +","
			                +"<font color=\"#fb6137\">"
			                +animal.pet_nickName+" "
			                +"</font>"
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
			                +"对 "
			                +"<font color=\"#fb6137\">"
			                +animal.pet_nickName+" "
			                +"</font>"
			                +"扔了一个"
			                +"<font color=\"#fb6137\">"
			                +gift.name+" "
			                +"</font>"
			                +","
			                +"<font color=\"#fb6137\">"
			                +animal.pet_nickName+" "
			                +"</font>"
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
					    +"萌主"
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
		                +"在逗一逗游戏中 获得了 "
		                +"<font color=\"#fb6137\">"
		                +"99 "
		                +"</font>"
		                +"的高分，实在太厉害啦！"
		             +"</body>"
		      + "</html>";
			holder.playWithDesTv.setText(Html.fromHtml(htmlStr1));
			holder.playWithTimeTv.setText(timeString);
		}else if(petNews.type==7){
			Gift gift=new Gift();
			gift.no=petNews.item_id;
			int index=giftList.indexOf(gift);
			gift=giftList.get(index);
			holder.makeTroubleLayout.setVisibility(View.VISIBLE);
			htmlStr1="<html>"
		             +"<body>"
					    +petNews.job
					    +""
		                +"<font color=\"#fb6137\">"
		                +petNews.u_name+" "
		                +"</font>"
		                +"对 "
		                +"<font color=\"#fb6137\">"
		                +animal.pet_nickName+" "
		                +"</font>"
		                +"扔了一个"
		                +"<font color=\"#fb6137\">"
		                +gift.name+" "
		                +"</font>"
		                +","
		                +"<font color=\"#fb6137\">"
		                +animal.pet_nickName+" "
		                +"</font>"
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
		
		
		
		
		
		
		
		
		
	
		return convertView;
	}
	class Holder{
		LinearLayout fansLayout,kingdomLayout,imageLayout,makeTroubleLayout,sendGiftLayout,biteLayout,playWithLayout;
		TextView fansDesTv,kingdomDesTv,imagesDesTv,fansTimeTv,kingdomTimeTv,imagesTimeTv,
		         makeTroubleDesTv,makeTroubleTimeTv,sendGiftDesTv,sendGiftTimeTv,
		         biteDesTv,biteTimeTv,playWithDesTv,playWithTimeTv;
		ImageView imageIv,playWithIV,sendGiftIv,biteIv;
	}

}
