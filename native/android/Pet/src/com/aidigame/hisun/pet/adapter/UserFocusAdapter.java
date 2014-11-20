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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.UserKingdomListAdapter.Holder;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson.Data;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.fragment.DialogGoRegister;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 用户关注的王国列表，适配器
 * @author admin
 *
 */
public class UserFocusAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
    ImageLoader imageLoader;
	UserDossierActivity context;
	ArrayList<Animal> list;
	int mode;//1 关注类表；2粉丝列表；3 其他用户列表（如对图片点赞的人的列表）
	Handler handler;
	HandleHttpConnectionException handleHttpConnectionException;
	User user;
	public UserFocusAdapter(UserDossierActivity context,ArrayList<Animal> list,int mode,Handler handler,User user){
		this.context=context;
		this.list=list;
		this.mode=mode;
		this.user=user;
		this.handler=handler;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.noimg)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		touchSlop=ViewConfiguration.get(context).getScaledTouchSlop();
	}
	public void updateList(
			ArrayList<Animal> temp) {
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
	private float x,ux;
	LinearLayout rightLayout ;
	RelativeLayout leftLayout;
	int touchSlop;
	public static boolean isSlidingTouch=false;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_user_focus_listview, null);
			holder.icon=(RoundImageView)convertView.findViewById(R.id.icon_circleView);
			holder.name=(TextView)convertView.findViewById(R.id.textView2);
			holder.gender=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.race=(TextView)convertView.findViewById(R.id.textView3);
			holder.age=(TextView)convertView.findViewById(R.id.textView5);
			holder.rank=(TextView)convertView.findViewById(R.id.textView1);
			holder.rightLayout=(LinearLayout)convertView.findViewById(R.id.buttons_layout);
			holder.leftLayout=(RelativeLayout)convertView.findViewById(R.id.linearlayout);
			holder.userName=(TextView)convertView.findViewById(R.id.textView8);
			holder.rLayout3=(RelativeLayout)convertView.findViewById(R.id.rlayout3);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		LogUtil.i("exception", "position========"+position);
			final Animal animal=list.get(position);
		
          
			loadIcon(holder.icon, animal);
		if(animal.a_gender==1){
			holder.gender.setImageResource(R.drawable.male1);
		}else if(animal.a_gender==2){
			holder.gender.setImageResource(R.drawable.female1);
		}else{
//			holder.gender.setImageResource();
		}
		holder.name.setText(""+animal.pet_nickName);
		holder.race.setText(animal.race);
		holder.age.setText(""+animal.a_age_str);
		holder.rank.setText(""+animal.t_rq);
		holder.userName.setText(""+animal.u_name);
		holder.leftLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.isSuccess&&Constants.user!=null&&Constants.user.userId==user.userId){
					showButtons(animal);
				}else{
					if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.finish();
					Intent intent=new Intent(context,PetKingdomActivity.class);
					intent.putExtra("animal", animal);
					context.startActivity(intent);
					
					context.progresslayout.setVisibility(View.INVISIBLE);
					context.progresslayout.setClickable(false);
					isShowingButton=false;
				}
			}
		});
		final RoundImageView view=holder.icon;
       /* holder.rightLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MarginLayoutParams params=(MarginLayoutParams)leftLayout.getLayoutParams();
				params.leftMargin=0;
				leftLayout.setLayoutParams(params);
				rightLayout.setVisibility(View.GONE);
				
								// TODO Auto-generated method stub
								if(context instanceof UserDossierActivity){
									final UserDossierActivity p=(UserDossierActivity)context;
									DialogGoRegister dialog=new DialogGoRegister(p.popupParent, p, p.black_layout, 2);
									dialog.setAnimal(animal);
									dialog.setListener(new DialogGoRegister.ResultListener() {
										
										@Override
										public void getResult(boolean isSuccess) {
											// TODO Auto-generated method stub
											if(isSuccess){
												new Thread(new Runnable() {
													
													@Override
													public void run() {
														// TODO Auto-generated method stub
												p.userFocus.onRefresh();
													}
													}).start();
											}else{
												Toast.makeText(context, "取消关注失败失败", Toast.LENGTH_LONG).show();
											}
										}
									});
								}
					
				
			}
		});
		
		convertView.setOnTouchListener(new OnTouchListener() {
			float startX;
			float distance;
		    boolean isRecord=false;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(Constants.user==null||Constants.user.userId!=user.userId){
					return false;
				}
				final Holder holder = (Holder) v.getTag();
				int maxW=holder.rightLayout.getMeasuredWidth();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isRecord=false;
					distance=0;
					startX=0;
					LogUtil.i("scroll", "actiondown");
					if(!isRecord){
						isRecord=true;
						startX=event.getX();
						if (rightLayout != null) {
							if(rightLayout.getVisibility() == View.VISIBLE){
								MarginLayoutParams params=(MarginLayoutParams)leftLayout.getLayoutParams();
								params.leftMargin=0;
								leftLayout.setLayoutParams(params);
								rightLayout.setVisibility(View.GONE);
								return true;
							}
						}
					}
					return true;

				case MotionEvent.ACTION_MOVE:
					LogUtil.i("scroll", "actionmove");
					if(!isRecord){
						isRecord=true;
						startX=event.getX();
						if (rightLayout != null) {
							if(rightLayout.getVisibility() == View.VISIBLE){
								MarginLayoutParams params=(MarginLayoutParams)leftLayout.getLayoutParams();
								params.leftMargin=0;
								leftLayout.setLayoutParams(params);
								rightLayout.setVisibility(View.GONE);
							}
						}
					}
					LogUtil.i("scroll","startx="+startX+",getX="+event.getX()+",width="+holder.rightLayout.getWidth()+",mwidth="+holder.rightLayout.getMeasuredWidth() );
                   if(startX-event.getX()>0&&startX-event.getX()>=touchSlop){
                	   holder.rightLayout.setVisibility(View.VISIBLE);
                	   MarginLayoutParams params=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
                	   MarginLayoutParams rightParams=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
                	   maxW=holder.rightLayout.getMeasuredWidth();
                	   isSlidingTouch=true;
                	   if(params.leftMargin>-5*touchSlop){
   						distance=startX-event.getX();
   						params.leftMargin=(int) -distance;
   						holder.leftLayout.setLayoutParams(params);
   						rightParams.rightMargin=(int) -(maxW-distance);
   						holder.rightLayout.setLayoutParams(rightParams);
   					}else{
   					}
                	   return true;
					}
					break;

				case MotionEvent.ACTION_UP:
					LogUtil.i("scroll", "actionup");
					 MarginLayoutParams params=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
              	   MarginLayoutParams rightParams=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
					if(distance>5*touchSlop){
						
						holder.rightLayout.setVisibility(View.VISIBLE);
						MarginLayoutParams params3=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
						params3.rightMargin=0;
						holder.rightLayout.setLayoutParams(params3);
						MarginLayoutParams params1=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
						params1.leftMargin=-maxW;
						holder.leftLayout.setLayoutParams(params1);
						
						rightLayout = holder.rightLayout;
						leftLayout=holder.leftLayout;
					}else if(distance<touchSlop){
						if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.finish();
						Intent intent=new Intent(context,PetKingdomActivity.class);
						intent.putExtra("animal", animal);
						context.startActivity(intent);
					}
					else{
						MarginLayoutParams params2=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
						params2.leftMargin=0;
						holder.leftLayout.setLayoutParams(params2);
						MarginLayoutParams params3=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
						params3.rightMargin=0;
						holder.rightLayout.setLayoutParams(params3);
						holder.rightLayout.setVisibility(View.GONE);
						
						
						
					}
					isRecord=false;
					break;

				default:
					break;
				}
				if(distance>5*touchSlop){
					holder.rightLayout.setVisibility(View.VISIBLE);
					MarginLayoutParams params3=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
					params3.rightMargin=0;
					holder.rightLayout.setLayoutParams(params3);
					MarginLayoutParams params1=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
					params1.leftMargin=-maxW;
					holder.leftLayout.setLayoutParams(params1);
					
					rightLayout = holder.rightLayout;
					leftLayout=holder.leftLayout;
				}else{
					MarginLayoutParams params2=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
					params2.leftMargin=0;
					holder.leftLayout.setLayoutParams(params2);
					MarginLayoutParams params3=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
					params3.rightMargin=0;
					holder.rightLayout.setLayoutParams(params3);
					holder.rightLayout.setVisibility(View.GONE);
				}
				return true;
			}
		});
		*/
		return convertView;
	}
	public void loadIcon(RoundImageView icon,final Animal data){
		
		imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+data.pet_iconUrl, icon, displayImageOptions);
	}
	class Holder{
		RoundImageView icon;
		ImageView gender;
		TextView name;
		TextView race;
		TextView age;
		TextView rank;
		TextView job;
		TextView userName;
		LinearLayout rightLayout;
		RelativeLayout leftLayout,rLayout3;
	}
	boolean isShowingButton=false;
	private void showButtons(final Animal animal) {
		// TODO Auto-generated method stub
		isShowingButton=true;
		long l1=System.currentTimeMillis();
		LogUtil.i("scroll",""+( System.currentTimeMillis()-l1));
		final View view=LayoutInflater.from(context).inflate(R.layout.popup_user_dossier, null);
		Animation animation=AnimationUtils.loadAnimation(context, R.anim.anim_translate_showtopic_addcommentlayout_in);
		view.clearAnimation();
		view.setAnimation(animation);
		animation.start();
		
		context.progresslayout.removeAllViews();
		context.progresslayout.addView(view);
		context.progresslayout.setBackgroundResource(R.color.window_black_bagd);
		context.progresslayout.setVisibility(View.VISIBLE);
//		context.progresslayout.setClickable(true);
		TextView camera=(TextView)view.findViewById(R.id.textView1);
		TextView album=(TextView)view.findViewById(R.id.textView2);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		view.findViewById(R.id.line1).setVisibility(View.GONE);
		album.setVisibility(View.GONE);
		camera.setText("进入个人主页");
		cancel.setText("取消关注");
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.finish();
				Intent intent=new Intent(context,PetKingdomActivity.class);
				intent.putExtra("animal", animal);
				context.startActivity(intent);
				
				context.progresslayout.setVisibility(View.INVISIBLE);
				context.progresslayout.setClickable(false);
				isShowingButton=false;
				
				
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					final UserDossierActivity p=(UserDossierActivity)context;
					DialogGoRegister dialog=new DialogGoRegister(p.popupParent, p, p.black_layout, 2);
					dialog.setAnimal(animal);
					dialog.setListener(new DialogGoRegister.ResultListener() {
						
						@Override
						public void getResult(boolean isSuccess) {
							// TODO Auto-generated method stub
							if(isSuccess){
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
								p.userFocus.onRefresh(null);
									}
									}).start();
							}else{
								Toast.makeText(context, "取消关注失败失败", Toast.LENGTH_LONG).show();
							}
						}
					});
				context.progresslayout.setVisibility(View.INVISIBLE);
				context.progresslayout.setClickable(false);
				isShowingButton=false;
			}
		});
		context.progresslayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Animation animation=AnimationUtils.loadAnimation(context, R.anim.anim_translate_showtopic_addcommentlayout_out);
					view.clearAnimation();
					view.setAnimation(animation);
					animation.start();
					handleHttpConnectionException.getHandler(context).postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							context.progresslayout.setVisibility(View.INVISIBLE);
							context.progresslayout.setClickable(false);
							isShowingButton=false;
						}
					}, 300);
					break;
				}
				return true;
			}
		});
	}

}
