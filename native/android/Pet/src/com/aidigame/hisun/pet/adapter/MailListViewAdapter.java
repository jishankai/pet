package com.aidigame.hisun.pet.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.TalkMessage;
import com.aidigame.hisun.pet.bean.TalkMessage.Msg;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.ui.ReceiverAddressActivity;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * 聊天界面    消息列表
 * @author admin
 *
 */
public class MailListViewAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
	Context context;
	TalkMessage datas;
	public MailListViewAdapter(Context context,TalkMessage datas){
		this.context=context;
		this.datas=datas;
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
	            .showImageOnFail(R.drawable.user_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
	public void update(TalkMessage datas){
		this.datas=datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.msgList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.msgList.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_mail_listview, null);
			holder.body_left=(Button)convertView.findViewById(R.id.body_left);
			holder.body_right=(Button)convertView.findViewById(R.id.body_right);
			holder.icon_left=(RoundImageView)convertView.findViewById(R.id.icon_left);
			holder.icon_right=(RoundImageView)convertView.findViewById(R.id.icon_right);
			holder.timeTv=(TextView)convertView.findViewById(R.id.time_tv);
			holder.arrowView1=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.arrowView2=(ImageView)convertView.findViewById(R.id.imageView2);
			holder.layout1=(LinearLayout)convertView.findViewById(R.id.layout1);
			holder.layout2=(LinearLayout)convertView.findViewById(R.id.layout2);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final Msg data=datas.msgList.get(position);
		if(data.showTime){
			holder.timeTv.setVisibility(View.VISIBLE);;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			holder.timeTv.setText(""+sdf.format(new Date(data.time*1000)));
		}else{
			holder.timeTv.setVisibility(View.GONE);;
		}
		
			if(data.from==Constants.user.userId){
				holder.layout2.setVisibility(View.VISIBLE);
				holder.layout1.setVisibility(View.GONE);
				holder.body_left.setVisibility(View.GONE);
				holder.icon_left.setVisibility(View.GONE);
				holder.body_right.setVisibility(View.VISIBLE);
				holder.icon_right.setVisibility(View.VISIBLE);
				holder.body_right.setText(""+/*(StringUtil.isEmpty(data.content)?"":*/data.content/*)*/);
				ImageLoader imageLoader=ImageLoader.getInstance();
				imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+Constants.user.u_iconUrl, holder.icon_right, displayImageOptions);
				holder.arrowView2.setVisibility(View.GONE);
			}else{
				holder.layout1.setVisibility(View.VISIBLE);
				holder.layout2.setVisibility(View.GONE);
				holder.body_right.setVisibility(View.GONE);
				holder.icon_right.setVisibility(View.GONE);
				holder.icon_left.setVisibility(View.VISIBLE);
				holder.body_left.setVisibility(View.VISIBLE);
				if(!StringUtil.isEmpty(data.content)){
					if(data.content.contains("[address]")){
						String[] strs=data.content.split("[address]");
						
							holder.body_left.setText(""+/*(StringUtil.isEmpty(data.content)?"":*/data.content.substring(9)/*)*/);
							holder.arrowView1.setVisibility(View.VISIBLE); 
						
					}else{
						holder.body_left.setText(""+/*(StringUtil.isEmpty(data.content)?"":*/data.content/*)*/);
						holder.arrowView1.setVisibility(View.GONE); 
					}
				}
				
				
				if(data.from==1){
					/*
					 * 汪星大使
					 */
					 holder.arrowView1.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(!StringUtil.isEmpty(data.content)&&data.content.contains("[address]")){
									
									Intent intent=new Intent(context,ReceiverAddressActivity.class);
									context.startActivity(intent);
								}
								
							}
						});
					holder.icon_left.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.wangwang));
				}else if(data.from==2){
					holder.icon_left.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.miaomiao));
				    holder.arrowView1.setVisibility(View.VISIBLE); 
				    holder.arrowView1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
							PetPicture petPicture=new PetPicture();
							petPicture.img_id=(int)data.img_id;
							if(NewShowTopicActivity.newShowTopicActivity!=null){
								NewShowTopicActivity.newShowTopicActivity.recyle();
							}
							Intent intent=new Intent(context,NewShowTopicActivity.class);
							intent.putExtra("PetPicture", petPicture);
							context.startActivity(intent);
						}
					});
				}else if(data.from==3){
					  holder.icon_left.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.xiaoge));
				  }else{
					ImageLoader imageLoader=ImageLoader.getInstance();
					imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+datas.usr_tx, holder.icon_left, displayImageOptions);
				}
				
			}
			
		
		return convertView;
	}
	class Holder{
		Button body_left;
		Button body_right;
		TextView timeTv;
		RoundImageView icon_left;
		RoundImageView icon_right;
		ImageView arrowView1,arrowView2;
		LinearLayout layout1,layout2;
	}

}
