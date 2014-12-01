package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.BiteActivity;
import com.aidigame.hisun.pet.ui.DialogGiveSbGiftActivity1;
import com.aidigame.hisun.pet.ui.InviteOthersDialogActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.PlayGameActivity;
import com.aidigame.hisun.pet.ui.ShakeActivity;
import com.aidigame.hisun.pet.ui.TouchActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.fragment.MarketFragment;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class HomeMyPetAdapter extends BaseAdapter {
	Activity context;
	ArrayList<Animal> animals;
	DisplayImageOptions displayImageOptions;
	BitmapFactory.Options op;
	Handler handler;
	
	public HomeMyPetAdapter(Activity context,ArrayList<Animal> animals){
		this.context=context;
		this.animals=animals;
		op=new BitmapFactory.Options();
		op.inJustDecodeBounds=false;
		op.inSampleSize=4;
		op.inPreferredConfig=Bitmap.Config.RGB_565;
		op.inPurgeable=true;
		op.inInputShareable=true;
		handler=HandleHttpConnectionException.getInstance().getHandler(context);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		
		
	}
	public void update(ArrayList<Animal> animals){
		this.animals=animals;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return animals.size();
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return animals.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_home_my_pet, null);
			holder=new Holder();
			holder.rankLayout=(LinearLayout)convertView.findViewById(R.id.imageView1);
			holder.jobTv=(TextView)convertView.findViewById(R.id.job_tv);
			holder.contriTv=(TextView)convertView.findViewById(R.id.contri_num_tv);
			holder.pet_icon=(RoundImageView)convertView.findViewById(R.id.pet_icon);
			holder.petNameTv=(TextView)convertView.findViewById(R.id.pet_name_tv);
			holder.modifyEt=(EditText)convertView.findViewById(R.id.announce_et);
			holder.modifyTv=(TextView)convertView.findViewById(R.id.modify_announce_tv);
			holder.hotNumTv=(TextView)convertView.findViewById(R.id.hot_num_tv);
			holder.hotPercentTv=(TextView)convertView.findViewById(R.id.hot_percent_tv);
			holder.shakeIv=(ImageView)convertView.findViewById(R.id.shake_iv);
			holder.shakeTv=(TextView)convertView.findViewById(R.id.shake_tv);
			holder.shakeNumTv=(TextView)convertView.findViewById(R.id.shake_num_tv);
			holder.giftNumtTv=(TextView)convertView.findViewById(R.id.gift_num_tv);
			holder.biteIv=(ImageView)convertView.findViewById(R.id.bite_iv);
			holder.biteTv=(TextView)convertView.findViewById(R.id.bite_tv);
			holder.biteNumTv=(TextView)convertView.findViewById(R.id.bite_num_tv);
			holder.playNumTv=(TextView)convertView.findViewById(R.id.play_num_tv);
			holder.shakeLayout=(LinearLayout)convertView.findViewById(R.id.shake_layout);
			holder.giftLayout=(LinearLayout)convertView.findViewById(R.id.gift_layout);
			holder.biteLayout=(LinearLayout)convertView.findViewById(R.id.bite_layout);
			holder.playLayout=(LinearLayout)convertView.findViewById(R.id.play_layout);
			holder.iv1=(ImageView)convertView.findViewById(R.id.image_iv1);
			holder.iv2=(ImageView)convertView.findViewById(R.id.image_iv2);
			holder.iv3=(ImageView)convertView.findViewById(R.id.image_iv3);
			holder.iv4=(ImageView)convertView.findViewById(R.id.image_iv4);
			holder.inviteTv=(TextView)convertView.findViewById(R.id.invite_tv);
			holder.imageRLayout=(RelativeLayout)convertView.findViewById(R.id.background_image_rlayout);
			holder.searchLayout=(LinearLayout)convertView.findViewById(R.id.search_layout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final Animal animal=animals.get(position);
		holder.modifyTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl, holder.pet_icon,displayImageOptions);
		holder.petNameTv.setText(animal.pet_nickName);
        if("点击创建独一无二的萌宣言吧~".equals(animal.announceStr)){
        	if(Constants.user!=null&&Constants.user.userId==animal.master_id){
        		holder.modifyEt.setText(animal.announceStr);
        	}else{
        		holder.modifyEt.setText(animal.pet_nickName+"暂时沉默中~");
        	}
        }else{
        	holder.modifyEt.setText(animal.announceStr);
        }
		
		holder.modifyEt.setEnabled(false);
		holder.searchLayout.setBackgroundDrawable(null);
		holder.shakeNumTv.setText("还剩"+animal.shake_count+"次");
		holder.giftNumtTv.setText("送了"+animal.send_gift_count+"个");
		
		
		if(Constants.user!=null&&Constants.user.userId==animal.master_id){
			holder.modifyTv.setVisibility(View.VISIBLE);;
		}else{
			holder.modifyTv.setVisibility(View.GONE);;
		}
		holder.iv1.setVisibility(View.INVISIBLE);
		holder.iv2.setVisibility(View.INVISIBLE);
		holder.iv3.setVisibility(View.INVISIBLE);
		holder.iv4.setVisibility(View.INVISIBLE);
		holder.iv1.setImageBitmap(null);
		holder.iv2.setImageBitmap(null);
		holder.iv3.setImageBitmap(null);
		holder.iv4.setImageBitmap(null);
		
		if(animal.picturs!=null){
			if(animal.picturs.size()>=4){
				loadImage(holder.iv4, animal.picturs.get(3).url);
				holder.iv4.setTag(animal.picturs.get(3));
			}
			if(animal.picturs.size()>=3){
				loadImage(holder.iv3, animal.picturs.get(2).url);
				holder.iv3.setTag(animal.picturs.get(2));
			}
			if(animal.picturs.size()>=2){
				loadImage(holder.iv2, animal.picturs.get(1).url);
				holder.iv2.setTag(animal.picturs.get(1));
			}
			if(animal.picturs.size()>=1){
				loadImage(holder.iv1, animal.picturs.get(0).url);
				holder.iv1.setTag(animal.picturs.get(0));
			}
			if(animal.picturs.size()==0){
				holder.iv1.setVisibility(View.GONE);
				holder.iv2.setVisibility(View.GONE);
				holder.iv3.setVisibility(View.GONE);
				holder.iv4.setVisibility(View.GONE);
			}
		}else{
			holder.iv1.setVisibility(View.GONE);
			holder.iv2.setVisibility(View.GONE);
			holder.iv3.setVisibility(View.GONE);
			holder.iv4.setVisibility(View.GONE);
		}
		holder.contriTv.setText("贡献度 "+animal.t_contri);
		holder.contriTv.setTag(animal);
		if(Constants.user!=null&&Constants.user.aniList!=null){
			if(Constants.user.aniList.contains(animal)){
				if(Constants.user.userId==animal.master_id){
					holder.biteIv.setImageResource(R.drawable.claw_touch1);
					holder.biteTv.setText("萌叫叫");
					if(animal.isVoiced==0){
						holder.biteNumTv.setText("还未叫");
					}else{
						holder.biteNumTv.setText("叫过了");
					}
				}else{
					holder.biteIv.setImageResource(R.drawable.claw_bite1);
					holder.biteTv.setText("萌印象");
					if(animal.touch_count==0){
						holder.biteNumTv.setText("还未摸");
					}else{
						holder.biteNumTv.setText("摸过了");
					}
				}
			}else{
				holder.biteIv.setImageResource(R.drawable.claw_bite1);
				holder.biteTv.setText("萌印象");
				if(animal.touch_count==0){
					holder.biteNumTv.setText("还未摸");
				}else{
					holder.biteNumTv.setText("摸过了");
				}
			}
		}
		
		if(!StringUtil.isEmpty(Constants.CON_VERSION)&&"1.0".equals(Constants.CON_VERSION)){
			holder.inviteTv.setVisibility(View.INVISIBLE);
		}else{
			holder.inviteTv.setVisibility(View.VISIBLE);
		}
		
		
		holder.hotNumTv.setText(""+animal.t_rq);
		holder.jobTv.setText(animal.u_rank);
		holder.hotPercentTv.setText(""+animal.percent+"%");
		switch (animal.u_rankCode) {
		case 0:
			holder.rankLayout.setBackgroundResource(R.drawable.rank0);
			break;
		case 1:
			holder.rankLayout.setBackgroundResource(R.drawable.rank1);
			break;
		case 2:
			holder.rankLayout.setBackgroundResource(R.drawable.rank2);
			break;
		case 3:
			holder.rankLayout.setBackgroundResource(R.drawable.rank3);
			break;
		case 4:
			holder.rankLayout.setBackgroundResource(R.drawable.rank4);
			break;
		case 5:
			holder.rankLayout.setBackgroundResource(R.drawable.rank5);
			break;
		case 6:
			holder.rankLayout.setBackgroundResource(R.drawable.rank6);
			break;
		case 7:
			holder.rankLayout.setBackgroundResource(R.drawable.rank7);
			break;
		case 8:
			holder.rankLayout.setBackgroundResource(R.drawable.rank8);
			break;
		}
		MyPetClickListener myPetClickListener=new MyPetClickListener(position, holder);
		holder.pet_icon.setOnClickListener(myPetClickListener);
		holder.modifyTv.setOnClickListener(myPetClickListener);
		holder.modifyEt.addTextChangedListener(myPetClickListener);
		holder.shakeLayout.setOnClickListener(myPetClickListener);
		holder.giftLayout.setOnClickListener(myPetClickListener);
		holder.biteLayout.setOnClickListener(myPetClickListener);
		holder.playLayout.setOnClickListener(myPetClickListener);
		holder.iv1.setOnClickListener(myPetClickListener);
		holder.iv2.setOnClickListener(myPetClickListener);
		holder.iv3.setOnClickListener(myPetClickListener);
		holder.iv4.setOnClickListener(myPetClickListener);
		holder.inviteTv.setOnClickListener(myPetClickListener);
		return convertView;
	}
	public void loadImage(ImageView iv,String url){
		iv.setVisibility(View.VISIBLE);
		ImageFetcher imageFetcher4=new ImageFetcher(context, context.getResources().getDimensionPixelSize(R.dimen.dip_64),context.getResources().getDimensionPixelSize(R.dimen.dip_64));
		imageFetcher4.setImageCache(new ImageCache(context, new ImageCacheParams(url)));
		//imageFetcher4.setWidth(context.getResources().getDimensionPixelSize(R.dimen.dip_64));
		imageFetcher4.loadImage(url, iv, op);
	}
	/**
	 * 用户职位等级   
	 * @param animal
	 * @param holder
	 */
	public void setRank(Animal animal,Holder holder){
		holder.jobTv.setText(animal.job);
		switch (animal.ranking) {
		case 0:
			holder.rankLayout.setBackgroundResource(R.drawable.rank0);
			break;
		case 1:
			holder.rankLayout.setBackgroundResource(R.drawable.rank1);
			break;
		case 2:
			holder.rankLayout.setBackgroundResource(R.drawable.rank2);
			break;
		case 3:
			holder.rankLayout.setBackgroundResource(R.drawable.rank3);
			break;
		case 4:
			holder.rankLayout.setBackgroundResource(R.drawable.rank4);
			break;
		case 5:
			holder.rankLayout.setBackgroundResource(R.drawable.rank5);
			break;
		case 6:
			holder.rankLayout.setBackgroundResource(R.drawable.rank6);
			break;
		case 7:
			holder.rankLayout.setBackgroundResource(R.drawable.rank7);
			break;
		case 8:
			holder.rankLayout.setBackgroundResource(R.drawable.rank8);
			break;

		default:
			break;
		}
	}
	class Holder{
		RoundImageView pet_icon;
		ImageView iv1,iv2,iv3,iv4,shakeIv,biteIv;
		TextView jobTv,contriTv,petNameTv,modifyTv,hotNumTv,hotPercentTv,shakeTv,shakeNumTv,
		         giftNumtTv,biteTv,biteNumTv,playNumTv,inviteTv;
		EditText modifyEt;
		LinearLayout rankLayout,shakeLayout,giftLayout,biteLayout,playLayout,searchLayout;
		RelativeLayout imageRLayout;
	}
	public TextView tv,contriTV;
	
	public void updateTV(String msg,int rq){
		if(tv!=null){
			if("送了".equals(msg)){
				
			}
			tv.setText(msg);
		}
		if(contriTV!=null){
			Animal animal=(Animal)contriTV.getTag();
			animal.t_contri+=rq;
			int a=animals.indexOf(animal);
			animals.remove(a);
			animals.add(a, animal);
			contriTV.setText("贡献度 "+animal.t_contri);
		}
	}
	class  MyPetClickListener implements OnClickListener,TextWatcher{
		int position;
		Holder holder;
		boolean canOver=false;
        public MyPetClickListener(int position,Holder holder){
        	this.position=position;
        	this.holder=holder;
        }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Animal animal=animals.get(position);
			switch (v.getId()) {
			case R.id.pet_icon:
				if(PetKingdomActivity.petKingdomActivity!=null){
					if(PetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!PetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
						PetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
					}
					PetKingdomActivity.petKingdomActivity.loadedImage1=null;
					PetKingdomActivity.petKingdomActivity.finish();
				}
				Intent intent=new Intent(context,PetKingdomActivity.class);
				intent.putExtra("animal",animal);
				context.startActivity(intent);
				break;
			case R.id.modify_announce_tv:
				if(!canOver){
					holder.modifyEt.setFocusable(true);
					holder.modifyEt.setFocusableInTouchMode(true);
					holder.modifyEt.setEnabled(true);
					holder.modifyEt.requestFocus(EditText.FOCUS_FORWARD);
					holder.modifyEt.setSelection(0);
					InputMethodManager im=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
					im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					holder.searchLayout.setBackgroundResource(R.drawable.home_search_frame);
//					holder.modifyEt.setText("");
//					holder.modifyEt.setHint(animal.announceStr);
					if(holder.modifyEt.getText().length()>0)
					holder.modifyEt.setSelection(holder.modifyEt.getText().length());
					canOver=true;
					holder.modifyTv.setText("取消");
					holder.modifyTv.setBackgroundDrawable(null);
				}else{
					if(StringUtil.isEmpty(holder.modifyEt.getText().toString())){
						holder.modifyEt.setText(animal.announceStr);
						holder.modifyTv.setText("");
						holder.modifyTv.setBackgroundResource(R.drawable.modify);
						canOver=false;
						holder.modifyEt.setEnabled(false);
						holder.searchLayout.setBackgroundDrawable(null);
						return;
					}
					holder.modifyEt.setEnabled(false);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							final boolean flag=HttpUtil.modifyPetAnnounceInfo(handler, animals.get(position), holder.modifyEt.getText().toString(), context);
							context.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(flag){
										animals.get(position).announceStr=holder.modifyEt.getText().toString();
										LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)holder.modifyEt.getLayoutParams();
										if(param==null){
											param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

										}
										param.height=LinearLayout.LayoutParams.WRAP_CONTENT;
										param.width=LinearLayout.LayoutParams.WRAP_CONTENT;
										holder.modifyEt.setLayoutParams(param);
									}else{
										holder.modifyEt.setText(animals.get(position).announceStr);
										Toast.makeText(context, "修改失败", Toast.LENGTH_LONG).show();
									}
									holder.modifyTv.setText("");
									holder.modifyTv.setBackgroundResource(R.drawable.modify);
									canOver=false;
									holder.modifyEt.setEnabled(false);
									holder.searchLayout.setBackgroundDrawable(null);
									
								}
							});
						}
					}).start();
					
				}

				break;
			case R.id.shake_layout:
				if(Constants.user!=null&&Constants.user.aniList!=null){
					if(Constants.user.aniList.contains(animal)){
						Intent intent1=new Intent(context,ShakeActivity.class);
						intent1.putExtra("animal",animal);
						context.startActivity(intent1);
					    tv=holder.shakeNumTv;
					    contriTV=holder.contriTv;
					}else{
						Toast.makeText(context, "您还没有捧他", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(context, "您还没有捧他", Toast.LENGTH_LONG).show();
				}
				
				break;
			case R.id.gift_layout:
				Intent intent2=new Intent(context,DialogGiveSbGiftActivity1.class);
				intent2.putExtra("animal",animal);
				context.startActivity(intent2);
				tv=holder.giftNumtTv;
				contriTV=holder.contriTv;
				
				
				DialogGiveSbGiftActivity1 dgb=DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity;
				DialogGiveSbGiftActivity1.dialogGoListener=new DialogGiveSbGiftActivity1.DialogGoListener() {
					
					@Override
					public void toDo() {
						// TODO Auto-generated method stub
						
						Intent intent=null;
//						homeActivity.showMarketFragment(1);
						DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.finish();
						
						NewHomeActivity.homeActivity.finish();
						intent=new Intent(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity,NewHomeActivity.class);
						intent.putExtra("mode", NewHomeActivity.MARKETFRAGMENT);
						DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.startActivity(intent);
						
					}

					@Override
					public void closeDialog() {
						// TODO Auto-generated method stub
						if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)
						DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.finish();
					}
					@Override
					public void lastResult(boolean isSuccess) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void unRegister() {
						// TODO Auto-generated method stub
//						dialog.dismiss();
						if(!UserStatusUtil.isLoginSuccess(context, NewHomeActivity.homeActivity.homeFragment.popupParent, NewHomeActivity.homeActivity.homeFragment.black_layout)){
			        		
			        	};
					}
				};
				
				
				
				
				break;
			case R.id.bite_layout:
				if(Constants.user!=null&&Constants.user.aniList!=null){
					if(Constants.user.aniList.contains(animal)){
						if(Constants.user.userId==animal.master_id){
							Intent intent3=new Intent(context,BiteActivity.class);
							intent3.putExtra("animal",animal);
							context.startActivity(intent3);
							tv=holder.biteNumTv;
							return;
						}
					}
				}
				Intent intent3=new Intent(context,TouchActivity.class);
				intent3.putExtra("animal",animal);
				context.startActivity(intent3);
				tv=holder.biteNumTv;
				break;
			case R.id.play_layout:
				Intent intent4=new Intent(context,PlayGameActivity.class);
				intent4.putExtra("animal",animal);
				context.startActivity(intent4);
				tv=holder.biteNumTv;
				break;
			case R.id.image_iv1:
				/*PetPicture p1=(PetPicture)v.getTag();
				if(ShowTopicActivity.showTopicActivity!=null){
					ShowTopicActivity.showTopicActivity.imageView.setImageDrawable(null);
					if(ShowTopicActivity.showTopicActivity.bmp!=null&&!ShowTopicActivity.showTopicActivity.bmp.isRecycled()){
						ShowTopicActivity.showTopicActivity.bmp.recycle();
					}
					ShowTopicActivity.showTopicActivity.bmp=null;
					ShowTopicActivity.showTopicActivity.finish();
				}
				Intent intent5=new Intent(context,ShowTopicActivity.class);
				intent5.putExtra("PetPicture", p1);
				context.startActivity(intent5);*/
				PetPicture p1=(PetPicture)v.getTag();
				if(NewShowTopicActivity.newShowTopicActivity!=null){
						NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				Intent intent5=new Intent(context,NewShowTopicActivity.class);
				intent5.putExtra("PetPicture", p1);
				context.startActivity(intent5);
				
				break;
			case R.id.image_iv2:
				/*PetPicture p2=(PetPicture)v.getTag();
				if(ShowTopicActivity.showTopicActivity!=null){
					ShowTopicActivity.showTopicActivity.imageView.setImageDrawable(null);
					if(ShowTopicActivity.showTopicActivity.bmp!=null&&!ShowTopicActivity.showTopicActivity.bmp.isRecycled()){
						ShowTopicActivity.showTopicActivity.bmp.recycle();
					}
					ShowTopicActivity.showTopicActivity.bmp=null;
					ShowTopicActivity.showTopicActivity.finish();
				}
				Intent intent6=new Intent(context,ShowTopicActivity.class);
				intent6.putExtra("PetPicture", p2);
				context.startActivity(intent6);*/
				PetPicture p2=(PetPicture)v.getTag();
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.imageView.setImageDrawable(null);
					if(NewShowTopicActivity.newShowTopicActivity.bmp!=null&&!NewShowTopicActivity.newShowTopicActivity.bmp.isRecycled()){
						NewShowTopicActivity.newShowTopicActivity.bmp.recycle();
					}
					NewShowTopicActivity.newShowTopicActivity.bmp=null;
					NewShowTopicActivity.newShowTopicActivity.finish();
				}
				Intent intent6=new Intent(context,NewShowTopicActivity.class);
				intent6.putExtra("PetPicture", p2);
				context.startActivity(intent6);
				break;
			case R.id.image_iv3:
				PetPicture p3=(PetPicture)v.getTag();
				/*if(ShowTopicActivity.showTopicActivity!=null){
					ShowTopicActivity.showTopicActivity.imageView.setImageDrawable(null);
					if(ShowTopicActivity.showTopicActivity.bmp!=null&&!ShowTopicActivity.showTopicActivity.bmp.isRecycled()){
						ShowTopicActivity.showTopicActivity.bmp.recycle();
					}
					ShowTopicActivity.showTopicActivity.bmp=null;
					ShowTopicActivity.showTopicActivity.finish();
				}
				Intent intent7=new Intent(context,ShowTopicActivity.class);
				intent7.putExtra("PetPicture", p3);
				context.startActivity(intent7);*/
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.imageView.setImageDrawable(null);
					if(NewShowTopicActivity.newShowTopicActivity.bmp!=null&&!NewShowTopicActivity.newShowTopicActivity.bmp.isRecycled()){
						NewShowTopicActivity.newShowTopicActivity.bmp.recycle();
					}
					NewShowTopicActivity.newShowTopicActivity.bmp=null;
					NewShowTopicActivity.newShowTopicActivity.finish();
				}
				Intent intent7=new Intent(context,NewShowTopicActivity.class);
				intent7.putExtra("PetPicture", p3);
				context.startActivity(intent7);
				break;
			case R.id.image_iv4:
				/*PetPicture p4=(PetPicture)v.getTag();
				if(ShowTopicActivity.showTopicActivity!=null){
					ShowTopicActivity.showTopicActivity.imageView.setImageDrawable(null);
					if(ShowTopicActivity.showTopicActivity.bmp!=null&&!ShowTopicActivity.showTopicActivity.bmp.isRecycled()){
						ShowTopicActivity.showTopicActivity.bmp.recycle();
					}
					ShowTopicActivity.showTopicActivity.bmp=null;
					ShowTopicActivity.showTopicActivity.finish();
				}
				Intent intent8=new Intent(context,ShowTopicActivity.class);
				intent8.putExtra("PetPicture", p4);
				context.startActivity(intent8);*/
				PetPicture p4=(PetPicture)v.getTag();
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.imageView.setImageDrawable(null);
					if(NewShowTopicActivity.newShowTopicActivity.bmp!=null&&!NewShowTopicActivity.newShowTopicActivity.bmp.isRecycled()){
						NewShowTopicActivity.newShowTopicActivity.bmp.recycle();
					}
					NewShowTopicActivity.newShowTopicActivity.bmp=null;
					NewShowTopicActivity.newShowTopicActivity.finish();
				}
				Intent intent8=new Intent(context,NewShowTopicActivity.class);
				intent8.putExtra("PetPicture", p4);
				context.startActivity(intent8);
				break;
				
			case R.id.invite_tv:
				Intent intent9=new Intent(context,InviteOthersDialogActivity.class);
				intent9.putExtra("animal", animal);
				context.startActivity(intent9);
				break;

			default:
				break;
			}
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(s.length()>0){
//				canOver=true;
				if(canOver){
					holder.modifyTv.setText("完成");
					holder.modifyTv.setBackgroundDrawable(null);;
				}
				
			}else{
//				canOver=false;
				holder.modifyTv.setText("取消");
				holder.modifyTv.setBackgroundDrawable(null);;
			}
		}

		
	}
	

}
