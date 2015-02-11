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

import org.apache.http.conn.routing.RouteInfo.LayerType;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.ChargeActivity;
import com.aidigame.hisun.pet.ui.Dialog4Activity;
import com.aidigame.hisun.pet.ui.DialogNoteActivity;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.ui.NewPetKingdomActivity;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.ui.UserCardActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.GalleryFlow;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.view.TopicView;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.aidigame.hisun.pet.widget.fragment.DialogNote;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom.ResultListener;
import com.aviary.android.feather.library.graphics.drawable.FakeBitmapDrawable;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.example.android.bitmapfun.util.ImageFetcher;
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
public class HomePetPictureAdapter extends BaseAdapter  {
    DisplayImageOptions displayImageOptions;//显示图片的格式
    ImageFetcher mImageFetcher;
	DisplayImageOptions displayImageOptions2;//显示图片的格式
    ImageLoader imageLoader;
	Activity context;
	BitmapFactory.Options options;
	
	ArrayList<Animal> pp2;
	
	
	int mode;//1 个人主页和其他人主页 不需要用户信息，2 关注列表
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==Constants.ERROR_MESSAGE){
				ShowDialog.show((String)msg.obj, context);
			}else{
				
			}

		};
	};
	public HomePetPictureAdapter(Context context,ArrayList<Animal> animal){
		this.context=(Activity)context;
		this.pp2=animal;
		this.mode=mode;
		 //显示没有图片
		options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=StringUtil.getScaleByDPI((Activity)context);
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		
		
		mImageFetcher=new ImageFetcher(context, Constants.screen_width);
		
		BitmapFactory.Options ops=new BitmapFactory.Options();
		ops.inJustDecodeBounds=false;
		ops.inSampleSize=8;
		ops.inPreferredConfig=Bitmap.Config.RGB_565;
		ops.inPurgeable=true;
		ops.inInputShareable=true;
		displayImageOptions2=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(ops)
                .build();
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
	            .showImageOnFail(R.drawable.user_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(ops)
                .build();
	}
	public void updateTopics(ArrayList<Animal> animals){
		this.pp2=animals;
		this.notifyDataSetChanged();
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pp2.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pp2.get(position);
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
	    	convertView=LayoutInflater.from(context).inflate(R.layout.item_home_pet_pictures, null);
	    	holder.icon=(RoundImageView)convertView.findViewById(R.id.imageView1);
	    	holder.user_icon=(RoundImageView)convertView.findViewById(R.id.imageView2);
	    	holder.gallery=(GalleryFlow)convertView.findViewById(R.id.gallery_flow);
	    	holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
	    	holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
	    	holder.rqTv=(TextView)convertView.findViewById(R.id.rq_tv);
	    	holder.perTv=(TextView)convertView.findViewById(R.id.percent_tv);
	    	holder.supportIv=(ImageView)convertView.findViewById(R.id.support_iv);
	    	holder.rLayout1=(RelativeLayout)convertView.findViewById(R.id.relativeLayout1);
	    	convertView.setTag(holder);
	    }else{
	    	holder=(Holder)convertView.getTag();
	    }
	     
	    
	   /* if(position==0){
	    	LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)holder.rLayout1.getLayoutParams();
	    	if(param==null){
	    		param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    	}
	    	param.topMargin=context.getResources().getDimensionPixelSize(R.dimen.dip_38);
	    	holder.rLayout1.setLayoutParams(param);
	    }else{
	    	LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)holder.rLayout1.getLayoutParams();
	    	if(param==null){
	    		param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    	}
	    	param.topMargin=0;
	    	holder.rLayout1.setLayoutParams(param);
	    }*/
	    final Animal data=pp2.get(position);
	    
	    	holder.tv1.setText(""+data.pet_nickName);
		   BitmapFactory.Options options=new BitmapFactory.Options();
		    options.inSampleSize=8;
		    options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			loadIcon(holder.icon, data);
			loadUserIcon(holder.user_icon,data);
		   holder.tv2.setText("经纪人 - "+data.u_name);
		   if(data.hasJoinOrCreate){
			   holder.supportIv.setImageResource(R.drawable.support_gray);
				holder.supportIv.setClickable(false);
//			   holder.supportIv.setVisibility(View.INVISIBLE);
		   }else{
			   holder.supportIv.setImageResource(R.drawable.support_green);
				holder.supportIv.setClickable(true);
			   holder.supportIv.setVisibility(View.VISIBLE);
			   
			   
			   final ImageView supportIV=holder.supportIv;
			    holder.supportIv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(UserStatusUtil.isLoginSuccess(HomeActivity.homeActivity,HomeActivity.homeActivity.discoveryFragment.popupParent,HomeActivity.homeActivity.discoveryFragment.black_layout)){
							
							if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(data) ){
//								pengta_tv.setVisibility(View.GONE);
								Intent intent=new Intent(context,DialogNoteActivity.class);
								intent.putExtra("mode", 10);
								intent.putExtra("info", "您已经捧TA了");
								context.startActivity(intent);
								supportIV.setImageResource(R.drawable.support_gray);
								supportIV.setClickable(false);
								return ;
							}
							
							
							int num=0;
							int count=0;
							for(int i=0;i<PetApplication.myUser.aniList.size();i++){
//								if(Constants.user.aniList.get(i).master_id!=Constants.user.userId)
									count++;
							}
							
							
							if(count>=10&&count<=20){
								num=(count)*5;
							}else if(count>20){
								num=100;
							}
							
							if(PetApplication.myUser.coinCount<num){
//								DialogNote dialog=new DialogNote(HomeActivity.homeActivity.discoveryFragment.popupParent, HomeActivity.homeActivity, HomeActivity.homeActivity.discoveryFragment.black_layout, 1);
								/*Intent intent=new Intent(context,DialogNoteActivity.class);
								intent.putExtra("mode", 10);
								intent.putExtra("info", "钱包君告急！挣够金币再来捧萌星吧");
								context.startActivity(intent);*/
								
								Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
									
									@Override
									public void onClose() {
										// TODO Auto-generated method stub
									}
									
									@Override
									public void onButtonTwo() {
										// TODO Auto-generated method stub
										Intent intent=new Intent(context,ChargeActivity.class);
										context.startActivity(intent);
									}
									
									@Override
									public void onButtonOne() {
										// TODO Auto-generated method stub
									}
								};
								 Intent intent=new Intent(context,Dialog4Activity.class);
								 intent.putExtra("mode", 8);
								 intent.putExtra("num", num);
								 context.startActivity(intent);
							return;
								
						}
						
						
						DialogJoinKingdom dialog=new DialogJoinKingdom(HomeActivity.homeActivity.discoveryFragment.popupParent, HomeActivity.homeActivity, HomeActivity.homeActivity.discoveryFragment.black_layout, data);
						dialog.setResultListener(new ResultListener() {
							
							@Override
							public void getResult(boolean isSuccess) {
								// TODO Auto-generated method stub
								if(isSuccess){
									supportIV.setImageResource(R.drawable.support_gray);
									supportIV.setClickable(false);
								}
								
								data.hasJoinOrCreate=isSuccess;
//								supportIV.setVisibility(View.INVISIBLE);
								/*if(isSuccess){
									p.setPeoplesNum(data.fans,isSuccess);
								}*/
							}
						});
						}
						
					}
				});
			   
			   
		   }
		
		
		   holder.perTv.setText(""+data.percent+"%");
		   holder.rqTv.setText(""+data.fans);
		   final GalleryFlow gallery=holder.gallery;
		  holder.gallery.setAdapter(new ImageAdapter1(context, new ArrayList<PetPicture>()));
		   new Thread(new Runnable() {
			  ArrayList<PetPicture> list=new ArrayList<PetPicture>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				for(int i=0;i<data.picturs.size();i++){
					String path=null;
					if(new File(Constants.Picture_Topic_Path+File.separator+data.picturs.get(i).url).exists()){
						path=Constants.Picture_Topic_Path+File.separator+data.picturs.get(i).url;
					}else{
						path=HttpUtil.downloadImage(Constants.UPLOAD_IMAGE_RETURN_URL,data.picturs.get(i).url,null,context);
					}
					
					if(!StringUtil.isEmpty(path)){
						data.picturs.get(i).petPicture_path=path;
						list.add(data.picturs.get(i));
					}
				}
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(list.size()>0){
							ImageAdapter1 adapter=new ImageAdapter1(context, list);
							gallery.setAdapter(adapter);
							gallery.setSelection(2);
							gallery.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									/*if(ShowTopicActivity.showTopicActivity!=null){
										if(ShowTopicActivity.showTopicActivity.bmp!=null&&!ShowTopicActivity.showTopicActivity.bmp.isRecycled()){
											ShowTopicActivity.showTopicActivity.bmp.recycle();
										}
										ShowTopicActivity.showTopicActivity.finish();
									}
									PetPicture p=(PetPicture)gallery.getItemAtPosition(position);
									Intent intent=new Intent(context,ShowTopicActivity.class);
									intent.putExtra("PetPicture", p);
									context.startActivity(intent);*/
									
									if(NewShowTopicActivity.newShowTopicActivity!=null){
										NewShowTopicActivity.newShowTopicActivity.recyle();
									}
									PetPicture p=(PetPicture)gallery.getItemAtPosition(position);
									Intent intent6=new Intent(context,NewShowTopicActivity.class);
									intent6.putExtra("PetPicture", p);
									context.startActivity(intent6);
								}
							});
						}
						
					}
				});
				
			}
		}).start();
		   
		   
		   holder.icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(NewPetKingdomActivity.petKingdomActivity!=null){
					if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
						NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
					}
					NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
					NewPetKingdomActivity.petKingdomActivity.finish();
					NewPetKingdomActivity.petKingdomActivity=null;
				}
				Intent intent=new Intent(context,NewPetKingdomActivity.class);
				intent.putExtra("animal",data);
				context.startActivity(intent);
			}
		});
		   holder.user_icon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/*if(UserDossierActivity.userDossierActivity!=null){
						if(UserDossierActivity.userDossierActivity.loadedImage1!=null&&!UserDossierActivity.userDossierActivity.loadedImage1.isRecycled()){
							UserDossierActivity.userDossierActivity.loadedImage1.recycle();
						}
						UserDossierActivity.userDossierActivity.loadedImage1=null;
						UserDossierActivity.userDossierActivity.finish();
					}
					User user=new User();
					user.userId=data.master_id;
					user.u_iconUrl=data.u_tx;
					Intent intent=new Intent(context,UserDossierActivity.class);
					intent.putExtra("user",user);
					context.startActivity(intent);*/
					MyUser user=new MyUser();
					user.userId=data.master_id;
					user.u_iconUrl=data.u_tx;
					Intent intent=new Intent(context,UserCardActivity.class);
					intent.putExtra("user",user);
					context.startActivity(intent);
				}
			});
		   
		   
		  
		return convertView;
	}
	public void loadIcon(RoundImageView icon,final Animal  data){
		imageLoader =ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+data.pet_iconUrl, icon, displayImageOptions2, new ImageLoadingListener() {
			
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
	public void loadUserIcon(RoundImageView icon,final Animal  data){
		imageLoader =ImageLoader.getInstance();
		imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+data.u_tx, icon, displayImageOptions, new ImageLoadingListener() {
			
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
		RoundImageView icon,user_icon;
		GalleryFlow gallery;
		TextView tv1,tv2,rqTv,perTv;
		RelativeLayout rLayout1;
		ImageView supportIv;
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
	/*class ItemClickListener implements OnClickListener{
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
				if(datas.get(position).user==null||datas.get(position).usr_id==Constants.user.userId){
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
				}
				if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.finish();
				Intent intent1=new Intent(context,PetKingdomActivity.class);
				intent1.putExtra("animal", petPictures.get(position).animal);
				intent1.putExtra("from", "HomeActivity");
				context.startActivity(intent1);
//                context.finish();
				break;
			case 2:
				
				break;
			case 3:
				if(ShowTopicActivity.showTopicActivity!=null){
					if(ShowTopicActivity.showTopicActivity.getBitmap()!=null){
						if(!ShowTopicActivity.showTopicActivity.getBitmap().isRecycled())
							ShowTopicActivity.showTopicActivity.getBitmap().recycle();
					}
					ShowTopicActivity.showTopicActivity.finish();
				}
				ShowTopicActivity.petPictures=pp2.get(position).picturs;
				Intent intent3=new Intent(context,ShowTopicActivity.class);
				intent3.putExtra("PetPicture", petPictures.get(position));
				intent3.putExtra("mode",2);
				context.startActivity(intent3);
				
				
//				context.finish();
				break;
			}
		}
		
	}
*/ 
	
	

}
