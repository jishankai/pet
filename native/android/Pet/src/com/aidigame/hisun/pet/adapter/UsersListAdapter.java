package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
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
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson.Data;
import com.aidigame.hisun.pet.ui.ChatActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.ui.UsersListActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 类似  围观群众 界面  用户信息列表
 * @author admin
 *
 */
public class UsersListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
    ImageLoader imageLoader;
	Activity context;
	ArrayList<User> list;
	Handler handler;
	int animalType=1;//1,猫；2狗
	HandleHttpConnectionException handleHttpConnectionException;
	public UsersListAdapter(Activity context,ArrayList<User> list,Handler handler,int animalType){
		this.context=context;
		this.list=list;
		this.handler=handler;
		this.animalType=animalType;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
	public void updateList(
			ArrayList<User> temp) {
		// TODO Auto-generated method stub
		this.list=temp;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_users_listview, null);
			holder.icon=(RoundImageView)convertView.findViewById(R.id.icon_circleView);
			holder.name=(TextView)convertView.findViewById(R.id.textView2);
			holder.gender=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.giftType=(ImageView)convertView.findViewById(R.id.imageView2);
			holder.provinceTV=(TextView)convertView.findViewById(R.id.textView3);
			holder.cityTV=(TextView)convertView.findViewById(R.id.textView5);
			holder.sendEmail=(TextView)convertView.findViewById(R.id.textView1);
			holder.layout3=(LinearLayout)convertView.findViewById(R.id.linearlayout3);
//			holder.layout=(LinearLayout)convertView.findViewById(R.id.linearlayout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		LogUtil.i("exception", "position========"+position);
//		if(position<list.size()){
		final User data=list.get(position);
		if(data.senderOrLiker==1){
			holder.giftType.setImageResource(R.drawable.ball_red_gift);
		}else if(data.senderOrLiker==2){
			if(animalType==1){
				holder.giftType.setImageResource(R.drawable.ball_red_fish);
			}else{
				holder.giftType.setImageResource(R.drawable.ball_red_bone);
			}
		}
		
//		holder.icon.setImageBitmap();
		final RoundImageView view=holder.icon;
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if(context instanceof UsersListActivity){
					UsersListActivity u=(UsersListActivity)context;
					if(!UserStatusUtil.isLoginSuccess(u,u.popup_parent,u.black_layout))return;
				}*/
				if(UserDossierActivity.userDossierActivity!=null)UserDossierActivity.userDossierActivity.finish();
				Intent intent=new Intent(context,UserDossierActivity.class);
				intent.putExtra("user", list.get(position));
				context.startActivity(intent);
			}
		});
			loadIcon(holder.icon, data);
		if(data.u_gender==1){
			holder.gender.setImageResource(R.drawable.male1);
		}else if(data.u_gender==2){
			holder.gender.setImageResource(R.drawable.female1);
		}else{
//			holder.gender.setImageResource();
		}
		holder.name.setText(""+data.u_nick);
		holder.provinceTV.setText(""+data.province);
		holder.cityTV.setText(""+data.city);
		holder.layout3.setVisibility(View.VISIBLE);
		/*holder.layout.setClickable(true);
		holder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,UserDossierActivity.class);
				UserImagesJson.Data d=new UserImagesJson.Data();
				d.usr_id=data.user.userId;
				d.user=data.user;
				d.isFriend=data.isFriend;
				intent.putExtra("data", d);
				context.startActivity(intent);
			}
		});*/
		holder.sendEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(context instanceof UsersListActivity){
					UsersListActivity u=(UsersListActivity)context;
					if(!UserStatusUtil.isLoginSuccess(u,u.popup_parent,u.black_layout))return;
				}
				Intent intent=new Intent(context,ChatActivity.class);
				intent.putExtra("user", data);
				context.startActivity(intent);
			}
		});
		
		
//		}
		return convertView;
	}
	public void loadIcon(RoundImageView icon,final User data){
		
		imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+data.u_iconUrl, icon, displayImageOptions);
	}
	class Holder{
		RoundImageView icon;
		ImageView gender,giftType;
		TextView name;
		TextView provinceTV;
		TextView cityTV;
		TextView sendEmail;
		LinearLayout layout3;
//		LinearLayout layout;
	}


}
