package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.util.ArrayList;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.ChargeActivity;
import com.aidigame.hisun.pet.ui.Dialog4Activity;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.TopicView;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class BegPicturesAdapter extends BaseAdapter {
	Activity context;
	ArrayList<PetPicture> pictures;
	Handler handler;
	PopupWindow moreNumWindow;
	public BegPicturesAdapter(Activity context,ArrayList<PetPicture> petPictures){
		this.context=context;
		this.pictures=petPictures;
		handler=HandleHttpConnectionException.getInstance().getHandler(context);
		initMoreNum();
	}
	public void update(ArrayList<PetPicture> pictures){
		this.pictures=pictures;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return pictures.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pictures.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_beg_pictures, null);
			holder=new Holder();
			holder.begIv=(ImageView)convertView.findViewById(R.id.beg_picture);
			holder.heartIv=(ImageView)convertView.findViewById(R.id.give_food_tv);
			holder.foodNumTv=(TextView)convertView.findViewById(R.id.food_num);
			holder.desTv=(TextView)convertView.findViewById(R.id.des_tv);
			holder.timeTv=(TextView)convertView.findViewById(R.id.time_tv);
			holder.giveNumTv=(TextView)convertView.findViewById(R.id.give_num);
			holder.giveLayout=(RelativeLayout)convertView.findViewById(R.id.reward_layout2);
			holder.giveLayout2=(RelativeLayout)convertView.findViewById(R.id.reward_layout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final PetPicture p=pictures.get(position);
		if(position==0||position==1||position==2){
			if((p.create_time*1000+24*3600*1000)<System.currentTimeMillis()){
				holder.giveLayout2.setVisibility(View.GONE);
  			}else{
  				holder.giveLayout2.setVisibility(View.VISIBLE);
  				giveNum=holder.giveNumTv;
  				giveLayout=holder.giveLayout;
  			}
			
		}else{
			holder.giveLayout2.setVisibility(View.GONE);
		}
		
		
		holder.foodNumTv.setText("已挣得口粮："+p.foodNum+"份");
		holder.desTv.setText(""+p.cmt);
		holder.timeTv.setText(""+judgeTime(p.create_time));
		loadTopicImage(holder.begIv, p);
		final TextView tv=holder.foodNumTv;
		if(p.picture_type==1){
			holder.heartIv.setImageResource(R.drawable.give_heart);
		}else{
			 SharedPreferences sp=context.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		     
		    	  String path=sp.getString("p_mid"+p.picture_type+"_pic_path", "");
		    	  if(!StringUtil.isEmpty(path)&&new File(path).exists()){
		    		  holder.heartIv.setImageBitmap(BitmapFactory.decodeFile(path));
		    	  }else{
		    		  holder.heartIv.setImageResource(R.drawable.give_heart);
		    	  }
		}
		holder.giveLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(moreNumWindow!=null){
					moreNumWindow.showAsDropDown(giveLayout, context.getResources().getDimensionPixelSize(R.dimen.one_dip)*28, -context.getResources().getDimensionPixelSize(R.dimen.one_dip)*188);
				}
			}
		});
		holder.heartIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				giveFood(p, tv);
			}
		});
		convertView.setClickable(true);;
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				Intent intent=new Intent(context,NewShowTopicActivity.class);
				intent.putExtra("PetPicture", p);
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	/**
	 * 选择打赏的数目
	 */
	TextView tv4,tv3,tv2,tv1,giveNum;
	RelativeLayout giveLayout;
	private void initMoreNum() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(context).inflate(R.layout.item_more_num, null);
		tv4=(TextView)view.findViewById(R.id.tv4);
		tv3=(TextView)view.findViewById(R.id.tv3);
		tv2=(TextView)view.findViewById(R.id.tv2);
		tv1=(TextView)view.findViewById(R.id.tv1);
		
		
		moreNumWindow=new PopupWindow(view,context.getResources().getDimensionPixelSize(R.dimen.one_dip)*120,LayoutParams.WRAP_CONTENT);
		moreNumWindow.setFocusable(true);
		moreNumWindow.setOutsideTouchable(true);
		moreNumWindow.setBackgroundDrawable(new BitmapDrawable());
		LogUtil.i("mi", "宽度==="+view.getMeasuredWidth());
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv1.setBackgroundDrawable(new BitmapDrawable());
				tv2.setBackgroundDrawable(new BitmapDrawable());
				tv3.setBackgroundDrawable(new BitmapDrawable());
				tv4.setBackgroundDrawable(new BitmapDrawable());
				
				tv1.setBackgroundResource(R.drawable.more_item_bg);
				giveNum.setText(""+1);
				current_give_num=1;
				moreNumWindow.dismiss();
			}
		});
tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv1.setBackgroundDrawable(new BitmapDrawable());
				tv2.setBackgroundDrawable(new BitmapDrawable());
				tv3.setBackgroundDrawable(new BitmapDrawable());
				tv4.setBackgroundDrawable(new BitmapDrawable());
				tv2.setBackgroundResource(R.drawable.more_item_bg);
				giveNum.setText(""+10);
				current_give_num=10;
				moreNumWindow.dismiss();
			}
		});
tv3.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		tv1.setBackgroundDrawable(new BitmapDrawable());
		tv2.setBackgroundDrawable(new BitmapDrawable());
		tv3.setBackgroundDrawable(new BitmapDrawable());
		tv4.setBackgroundDrawable(new BitmapDrawable());
		tv3.setBackgroundResource(R.drawable.more_item_bg);
		giveNum.setText(""+100);
		current_give_num=100;
		moreNumWindow.dismiss();
	}
});
tv4.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		tv1.setBackgroundDrawable(new BitmapDrawable());
		tv2.setBackgroundDrawable(new BitmapDrawable());
		tv3.setBackgroundDrawable(new BitmapDrawable());
		tv4.setBackgroundDrawable(new BitmapDrawable());
		tv4.setBackgroundResource(R.drawable.more_item_bg);
		giveNum.setText(""+1000);
		current_give_num=1000;
		moreNumWindow.dismiss();
	}
});
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
	public void loadTopicImage(final ImageView topic,final PetPicture data){
		
		
		topic.setVisibility(View.VISIBLE);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=StringUtil.getScaleByDPI(context);
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
	    ImageFetcher mImageFetcher=new ImageFetcher(context, 0);
		mImageFetcher.setWidth(0);
		int h=context.getResources().getDimensionPixelSize(R.dimen.one_dip)*100;
		mImageFetcher.IP=mImageFetcher.UPLOAD_THUMBMAIL_IMAGE;
		mImageFetcher.setImageCache(new ImageCache(context, new ImageCacheParams(data.url+"@"+h+"w_"+h+"h_0l.jpg")));
		mImageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
			
			@Override
			public void onComplete(Bitmap bitmap) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void getPath(String path) {
				// TODO Auto-generated method stub
				RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)topic.getLayoutParams();
				if(param==null){
					param=new RelativeLayout.LayoutParams(0,0);
				}
				int w=(int)((context.getResources().getDimensionPixelSize(R.dimen.one_dip)*100)*1f);
				param.width=w;
				param.height=w;
				topic.setLayoutParams(param);
			}
		});
		mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/data.url+"@"+h+"w_"+h+"h_0l.jpg", topic, /*options*/null);
	}
	boolean isGiving=false;
	int current_give_num=1;
	public void giveFood(final PetPicture p,final TextView tv){
		if(PetApplication.myUser!=null){
			 if(PetApplication.myUser.coinCount+PetApplication.myUser.food<current_give_num){
//				 Toast.makeText(homeActivity, "亲，金币不够了，快去充值吧", 1000).show();
				 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
						
						@Override
						public void onClose() {
							// TODO Auto-generated method stub
							isGiving=false;
						}
						
						@Override
						public void onButtonTwo() {
							// TODO Auto-generated method stub
//							giveFood();
							isGiving=false;
							Intent intent=new Intent(context,ChargeActivity.class);
							context.startActivity(intent);
						}
						
						@Override
						public void onButtonOne() {
							// TODO Auto-generated method stub
							isGiving=false;
						}
					};
					 Intent intent=new Intent(context,Dialog4Activity.class);
					 intent.putExtra("mode", 3);
					 intent.putExtra("num", current_give_num);
					 context.startActivity(intent);
					 return;
			 }
			 isGiving=true;
			 if(PetApplication.myUser!=null&&PetApplication.myUser.food>0){
				 doGiveFood(p,tv); 
				 return ;
			 }
			 SharedPreferences sp=context.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			 boolean flag=sp.getBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
			 
			 if(!flag){
				 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
					
					@Override
					public void onClose() {
						// TODO Auto-generated method stub
						isGiving=false;
					}
					
					@Override
					public void onButtonTwo() {
						// TODO Auto-generated method stub
						doGiveFood(p,tv);
					}
					
					@Override
					public void onButtonOne() {
						// TODO Auto-generated method stub
						isGiving=false;
					}
				};
				 Intent intent=new Intent(context,Dialog4Activity.class);
				 intent.putExtra("mode", 2);
				 intent.putExtra("num", current_give_num);
				 context.startActivity(intent);
			 }else{
				 doGiveFood(p,tv); 
			 }
			 
		 }
	}
	/**
	 * 打赏
	 */
	public void doGiveFood(final PetPicture p,final TextView tv){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final boolean flag=HttpUtil.awardApi(handler, p, current_give_num, context);
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						PetApplication.myUser.food=PetApplication.myUser.food-current_give_num;
						if(PetApplication.myUser.food<0)PetApplication.myUser.food=0;
						
						if(flag){
							Animal animal=p.animal;
							if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(animal)){
								int index=PetApplication.myUser.aniList.indexOf(animal);
								PetApplication.myUser.aniList.get(index).foodNum+=current_give_num;
							}
							tv.setText("已挣得口粮："+p.animal.foodNum+"份");
							
						}else{
							 Toast.makeText(context, "亲，数据错误导致打赏失败", 1000).show();
						}
						isGiving=false;
					}
				});
			}
		}).start();
	}
	class Holder{
		ImageView begIv,heartIv;
		TextView foodNumTv,desTv,timeTv,giveNumTv;
		RelativeLayout giveLayout,giveLayout2;
		
	}

}
