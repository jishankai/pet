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
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson.Data;
import com.aidigame.hisun.pet.ui.OtherUserTopicActivity;
import com.aidigame.hisun.pet.ui.UserHomepageActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.CircleView;

public class FocusAndFansAdapter extends BaseAdapter {
	Activity context;
	ArrayList<UserJson.Data> list;
	int mode;//1 关注类表；2粉丝列表；3 其他用户列表（如对图片点赞的人的列表）
	Handler handler;
	public FocusAndFansAdapter(Activity context,ArrayList<UserJson.Data> list,int mode,Handler handler){
		this.context=context;
		this.list=list;
		this.mode=mode;
		this.handler=handler;
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_focus_listview, null);
			holder.icon=(CircleView)convertView.findViewById(R.id.icon_circleView);
			holder.name=(TextView)convertView.findViewById(R.id.textView2);
			holder.gender=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.race=(TextView)convertView.findViewById(R.id.textView3);
			holder.age=(TextView)convertView.findViewById(R.id.textView5);
			holder.add=(TextView)convertView.findViewById(R.id.textView1);
			holder.layout=(LinearLayout)convertView.findViewById(R.id.linearlayout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		LogUtil.i("exception", "position========"+position);
//		if(position<list.size()){
			final UserJson.Data data=list.get(position);
		
		
//		holder.icon.setImageBitmap();
		final CircleView view=holder.icon;
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * public int likes;
		public String url;
		public String files;
		public int usr_id;
		public String comment;
		public long create_time;
		public int img_id;
		public String update_time;
		public String path;//图片下载到本地 之后的保存地址
		public User  user;
		public boolean isUser=true;
		public boolean isFriend;
				 */
				Intent intent=new Intent(context,OtherUserTopicActivity.class);
				UserImagesJson.Data d=new UserImagesJson.Data();
				d.usr_id=data.user.userId;
				d.user=data.user;
				d.isFriend=data.isFriend;
				intent.putExtra("data", d);
				context.startActivity(intent);
			}
		});
		if(StringUtil.judgeImageExists(Constants.Picture_ICON_Path+File.separator+data.user.iconUrl)){
			data.user.iconPath=Constants.Picture_ICON_Path+File.separator+data.user.iconUrl;
			BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inSampleSize=8;
			opts.inPreferredConfig=Bitmap.Config.RGB_565;
			opts.inPurgeable=true;
			opts.inInputShareable=true;
			FileInputStream fis=null;
			try {
				fis=new FileInputStream(Constants.Picture_ICON_Path+File.separator+data.user.iconUrl);
				holder.icon.setImageBitmap(BitmapFactory.decodeStream(fis,null,opts));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String path=HttpUtil.downloadIconImage(Constants.USER_DOWNLOAD_TX, data.user.iconUrl, null,context);
					if(path==null)return;
					data.user.iconPath=path;
					context.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							BitmapFactory.Options opts=new BitmapFactory.Options();
							opts.inSampleSize=8;
							view.setImageBitmap(BitmapFactory.decodeFile(data.user.iconPath, opts));
							FileInputStream fis=null;
							try {
								fis=new FileInputStream(data.user.iconPath);
								view.setImageBitmap(BitmapFactory.decodeStream(fis,null,opts));
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally{
								if(fis!=null){
									try {
										fis.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					});
				}
			}).start();
		}
		if(data.user.gender==1){
			holder.gender.setImageResource(R.drawable.male);
		}else if(data.user.gender==2){
			holder.gender.setImageResource(R.drawable.female);
		}else{
//			holder.gender.setImageResource();
		}
		holder.name.setText(""+data.user.nickName);
		holder.layout.setClickable(true);
		holder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,OtherUserTopicActivity.class);
				UserImagesJson.Data d=new UserImagesJson.Data();
				d.usr_id=data.user.userId;
				d.user=data.user;
				d.isFriend=data.isFriend;
				intent.putExtra("data", d);
				context.startActivity(intent);
			}
		});
		final TextView t=holder.race;
		 final SharedPreferences sp=context.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		 
		    if( sp.contains(data.user.race)){
			  t.setText(""+sp.getString(data.user.race, ""));
		   }else{
			   new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(Integer.parseInt(data.user.race)>301)return;
					boolean flag=HttpUtil.getRaceType(context);
					if(flag){
                        context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								t.setText(sp.getString(data.user.race, ""));
							}
						});
					}
				}
			}).start();
		   }
		holder.age.setText(""+data.user.age+"岁");
		final TextView tv=holder.add;
		if(mode==3){
			holder.add.setVisibility(View.INVISIBLE);
		}
		if(data.isFriend){
			holder.add.setBackgroundResource(R.color.white);
			holder.add.setTextColor(Color.BLACK);
			holder.add.setText(""+"取消关注");
		}else{
			holder.add.setBackgroundResource(R.color.green_light);
			holder.add.setTextColor(Color.WHITE);
			holder.add.setText(""+"关注");
		}
		holder.add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final UserImagesJson.Data d=new UserImagesJson.Data();
				d.usr_id=data.user.userId;
				if(data.isFriend){
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							boolean flag=HttpUtil.userDeleteFollow(d, null,context);
							if(!flag)return;
//							Constants.user.follow-=1;
							data.isFriend=false;
							if(mode==1){
								list.remove(data);
								if(handler!=null);
								handler.sendEmptyMessage(UserHomepageActivity.MESSAGE_SHOW_FOLLOWING);
							}else if(mode==2){
								if(handler!=null);
								Constants.user.follow-=1;
								if(Constants.user.follow<0)Constants.user.follow=0;
								handler.sendEmptyMessage(UserHomepageActivity.MESSAGE_SHOW_FOLLOWER);
							}
							
							context.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									FocusAndFansAdapter.this.notifyDataSetChanged();
								}
							});
						}
					}).start();
				}else{
                   new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							boolean flag=HttpUtil.userAddFollow(d, null,context);
							if(!flag)return;
//							Constants.user.follow+=1;
							if(mode==2){
								data.isFriend=true;
								Constants.user.follow++;
								if(Constants.user.follow<0)Constants.user.follow=0;
								if(handler!=null);
								handler.sendEmptyMessage(UserHomepageActivity.MESSAGE_SHOW_FOLLOWER);
							}else if(mode==1){
								if(handler!=null);
								handler.sendEmptyMessage(UserHomepageActivity.MESSAGE_SHOW_FOLLOWER);
							}
							
                            context.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									FocusAndFansAdapter.this.notifyDataSetChanged();
								}
							});
						}
					}).start();
				}
			}
		});
//		}
		return convertView;
	}
	class Holder{
		CircleView icon;
		ImageView gender;
		TextView name;
		TextView race;
		TextView age;
		TextView add;
		LinearLayout layout;
	}

}
