package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.R.anim;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.NewPetKingdomActivity;
import com.aidigame.hisun.pet.ui.PopularRankListActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aviary.android.feather.headless.filters.IFilter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * 人气榜界面，listview适配器
 * @author admin
 *
 */
public class PopularRankListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
	PopularRankListActivity  context;
    ArrayList<Animal> list;
    HandleHttpConnectionException handleHttpConnectionException;
    int mode;//1,长列表；2，短列表
    Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		
    	};
    };
    public PopularRankListAdapter(PopularRankListActivity context,ArrayList<Animal> list,int mode){
    	this.context=context;
    	this.list=list;
    	this.mode=mode;
    	handleHttpConnectionException=HandleHttpConnectionException.getInstance();
    	BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
    }
    public void updateData(ArrayList<Animal> list){
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
	public int h,w,layoutH,arrowH;
	boolean flag=false;
   public boolean arrowCanShow=true;
   public  ImageView currentArrowImageView;
   public int index;
   RoundImageView  firCircleView;
   LinearLayout firstLinearLayout;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_contribute_list_, null);
			holder=new Holder();
			holder.icon=(RoundImageView)convertView.findViewById(R.id.icon_circleView);
			holder.arrowIV=(ImageView)convertView.findViewById(R.id.imageView3);
			holder.rankNoIV=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.trendIV=(ImageView)convertView.findViewById(R.id.imageView2);
			holder.nameTV=(TextView)convertView.findViewById(R.id.textView1);
			holder.contributeNumTV=(TextView)convertView.findViewById(R.id.textView2);
			holder.rankNoTV=(TextView)convertView.findViewById(R.id.textView3);
			holder.layout=(LinearLayout)convertView.findViewById(R.id.layout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		if(position==0){
			firCircleView=holder.icon;
			firstLinearLayout=holder.layout;
		}
		final Animal people=list.get(position);
		
		
		if(position==0){
			holder.rankNoIV.setVisibility(View.VISIBLE);
			holder.rankNoIV.setImageResource(R.drawable.gold_rank_list);
			holder.rankNoTV.setVisibility(View.GONE);
		}else
		if(position==1){
			holder.rankNoIV.setVisibility(View.VISIBLE);
			holder.rankNoIV.setImageResource(R.drawable.silver_rank_list);
			holder.rankNoTV.setVisibility(View.GONE);
		}else
		if(position==2){
			holder.rankNoIV.setVisibility(View.VISIBLE);
			holder.rankNoIV.setImageResource(R.drawable.copper_rank_list);
			holder.rankNoTV.setVisibility(View.GONE);
		}else{
			holder.rankNoIV.setVisibility(View.GONE);
			holder.rankNoTV.setVisibility(View.VISIBLE);
			if(people.ranking<10){
				holder.rankNoTV.setText(" "+(position+1));
			}else{
				holder.rankNoTV.setText(""+(position+1));
			}
			
		}
		holder.contributeNumTV.setText(""+people.rq);
		final LinearLayout llt=holder.layout;
		holder.arrowIV.setVisibility(View.GONE);
		if(position==3){
			if(list.size()>6&&list.get(5).showArrow){
				holder.arrowIV.setVisibility(View.VISIBLE);
				holder.arrowIV.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						context.isAllData=true;
						context.peopleList.get(context.peopleList.indexOf(people)).showArrow=false;
						v.setVisibility(View.GONE);
						PopularRankListAdapter.this.updateData(context.peopleList);
						PopularRankListAdapter.this.notifyDataSetChanged();
					}
				});
			}
		}
		holder.nameTV.setText(""+people.pet_nickName);
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+people.pet_iconUrl, holder.icon, displayImageOptions);
		
		switch (people.change) {
		case 1:
			holder.trendIV.setVisibility(View.VISIBLE);
			holder.trendIV.setImageResource(R.drawable.arrow_rank_list_green);
			break;
		case 0:
//			holder.trendIV.setImageResource(R.drawable.arrow_rank_list_equal);
			holder.trendIV.setVisibility(View.GONE);
			break;
		case -1:
			holder.trendIV.setVisibility(View.VISIBLE);
			holder.trendIV.setImageResource(R.drawable.arrow_rank_list_red);
			break;
		}
		RoundImageView iv=holder.icon;
		 /*ViewTreeObserver otb=holder.icon.getViewTreeObserver();
		 otb.addOnPreDrawListener(new MyOnPreeDrawListener(holder,people));*/
		 if(!flag){
			 holder.icon.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			 w=holder.icon.getMeasuredWidth();
 			h=holder.icon.getMeasuredHeight();
 			flag=true;
		 }
//		 if(people.hasJoinOrCreate/*&&(people.ranking<7)*/&&people.isScale){
//				LayoutParams params=holder.icon.getLayoutParams();
//				if(params==null){
//					params=new LinearLayout.LayoutParams((int)(w*0.9f),(int)(h*0.9f));
//				}else{
//
//					params.height=(int)(h*0.9f);
//					params.width=(int)(w*0.9f);
//				}
//					holder.icon.setLayoutParams(params);
//					holder.layout.setBackgroundResource(R.color.white);
//					
//			}else {
//				LayoutParams params=holder.icon.getLayoutParams();
//				if(params==null){
//					params=new LinearLayout.LayoutParams((int)(w*0.6f),(int)(h*0.6f));
//				}else{
//
//					params.height=(int)(w*0.6f);
//					params.width=(int)(w*0.6f);
//				}
//				holder.icon.setLayoutParams(params);
//				
//				holder.layout.setBackgroundDrawable(null);
//				
//				
//			}
		 holder.icon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							final Animal user=HttpUtil.animalInfo(context,people, handleHttpConnectionException.getHandler(context));
							handleHttpConnectionException.getHandler(context).post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Intent intent=new Intent(context,NewPetKingdomActivity.class);
									intent.putExtra("animal", user);
									if(NewPetKingdomActivity.petKingdomActivity!=null){
										if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null){
											if(!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
												NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
												NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
											}
											NewPetKingdomActivity.petKingdomActivity.linearLayout2.setBackgroundDrawable(null);
										}
										NewPetKingdomActivity.petKingdomActivity.finish();
										NewPetKingdomActivity.petKingdomActivity=null;
									}
									context.startActivity(intent);
								}
							});
						}
					}).start();
					
				}
			});
		
		return convertView;
	}
	class Holder{
		LinearLayout layout;
		RoundImageView icon;
		TextView nameTV,contributeNumTV,rankNoTV;
		ImageView rankNoIV,trendIV,arrowIV;
		boolean isFocus;//convertview是否为选中状态（突出，显示当前用户排名）
	}
	class MyOnPreeDrawListener implements OnPreDrawListener{
		Holder holder;
		Animal animal;
		public MyOnPreeDrawListener(Holder holder,Animal animal){
			this.holder=holder;
			this.animal=animal;
		}

		@Override
		public boolean onPreDraw() {
			// TODO Auto-generated method stub
            if(!flag){
            	w=holder.icon.getMeasuredWidth();
    			h=holder.icon.getMeasuredHeight();
    			flag=true;
            }
			
			if(animal.hasJoinOrCreate&&(animal.ranking<7)){
				LayoutParams params=holder.icon.getLayoutParams();
				if(params==null){
					params=new LinearLayout.LayoutParams((int)(w*0.3f+w),(int)(h*0.3f+h));
				}else{

					params.height=(int)(h*0.3f+h);
					params.width=(int)(w*0.3f+h);
				}
					holder.icon.setLayoutParams(params);
					holder.layout.setBackgroundResource(R.color.white);
					return false;
			}else{
				LayoutParams params=firCircleView.getLayoutParams();
				if(params==null){
					params=new LinearLayout.LayoutParams(w,h);
				}else{

					params.height=h;
					params.width=w;
				}
				holder.icon.setLayoutParams(params);
				
				holder.layout.setBackgroundDrawable(null);
				return false;
				
			}
			
		
//			return true;
		}
		
	}

}
