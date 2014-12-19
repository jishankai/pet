package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
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
import com.aidigame.hisun.pet.adapter.MessageListAdapter.Holder;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.fragment.DialogNote;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom;
import com.aidigame.hisun.pet.widget.fragment.DialogQuitKingdom;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom.ResultListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * 用户加入的王国列表，适配器
 * @author admin
 *
 */
public class UserKingdomListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
	ArrayList<Animal> list;
	UserDossierActivity context;
	User user;
	boolean isShowingButton=false;
	HandleHttpConnectionException handleHttpConnectionException;
	public UserKingdomListAdapter(ArrayList<Animal> list,UserDossierActivity context,User user){
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		this.user=user;
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
		this.list=list;
		this.context=context;
		touchSlop=ViewConfiguration.get(context).getScaledTouchSlop();
	}
	public void update(ArrayList<Animal> list){
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

	private float x,ux;
	LinearLayout rightLayout;
	RelativeLayout leftLayout;
	int touchSlop;
	
	boolean hasMeasured=false;
	public static boolean isSlidingTouch=false;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_user_kingdoms, null);
			holder=new Holder();
			holder.rightLayout=(LinearLayout)convertView.findViewById(R.id.buttons_layout);
			holder.quitKingdomLayout=(LinearLayout)convertView.findViewById(R.id.quit_kingdom_layout);
			holder.changKingdomLayout=(LinearLayout)convertView.findViewById(R.id.change_current_kingdom_layout);
			holder.userJobTv=(TextView)convertView.findViewById(R.id.user_job_tv);
			holder.kingdomNameTv=(TextView)convertView.findViewById(R.id.kingdom_name_tv);
			holder.experienceTv=(TextView)convertView.findViewById(R.id.progress_value_tv);
			holder.hotNumTv=(TextView)convertView.findViewById(R.id.people_hot_tv);
			holder.trendsNumTv=(TextView)convertView.findViewById(R.id.trend_num_tv);
			holder.peoplesNumTv=(TextView)convertView.findViewById(R.id.people_num_tv);
		    holder.experienceView=(View)convertView.findViewById(R.id.progress_view);
		    holder.iconCircleView=(RoundImageView)convertView.findViewById(R.id.imageView3);
			holder.greenKingHatIv=(ImageView)convertView.findViewById(R.id.current_imageview);
		    holder.leftLayout=(RelativeLayout)convertView.findViewById(R.id.user_info_relativelayout);
		    holder.progressParentLayout=(RelativeLayout)convertView.findViewById(R.id.progress_parent);
		    holder.linearLayout3=(LinearLayout)convertView.findViewById(R.id.linearLayout3);
		    holder.joinRlaout=(RelativeLayout)convertView.findViewById(R.id.join_layout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final Animal animal=list.get(position);
		hasMeasured=false;
		holder.iconCircleView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.pet_icon));
		holder.joinRlaout.setVisibility(View.GONE);
		final View expLayout=holder.progressParentLayout;
		final TextView experienceProgressTV=holder.experienceTv;
		final View experenceProgressView=holder.experienceView;
		ViewTreeObserver viewTreeObserver=holder.progressParentLayout.getViewTreeObserver();
	    if(position==list.size()-1&&Constants.user!=null&&Constants.user.userId==user.userId){
	    	holder.joinRlaout.setVisibility(View.VISIBLE);
	    	
	    }
	    holder.joinRlaout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.user!=null&&Constants.user.aniList!=null){
					int num=0;
					if(Constants.user.aniList.size()>=10&&Constants.user.aniList.size()<=20){
						num=(Constants.user.aniList.size()+1)*5;
					}else if(Constants.user.aniList.size()>20){
						num=100;
					}
					
					if(Constants.user.coinCount>=num){
						Intent intent=new Intent(context,ChoseAcountTypeActivity.class);
						intent.putExtra("from", 1);
						context.startActivity(intent);
//						DialogGoRegister dialog=new DialogGoRegister(context.popupParent, context, context.black_layout, 4);
					}else{
						DialogNote dialog=new DialogNote(context.popupParent, context, context.black_layout, 1);
					}
					
				}
			}
		});
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			boolean hasMeasured=false;
			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				if(!hasMeasured){
					hasMeasured=true;
					int width=expLayout.getMeasuredWidth();
					LogUtil.i("scroll", "经验条长度"+width);
					int expDex1=0;
					int expDexTotal=0;
					if(animal.u_rankCode<8){
						expDex1=animal.t_contri/*-StringUtil.getContriByJobRank(animal.u_rankCode==0?7:animal.u_rankCode)*/;
						if(expDex1<0){
							expDex1=0;
						}
						expDexTotal=StringUtil.getContriByJobRank((animal.u_rankCode==0?7:animal.u_rankCode)+1)/*-StringUtil.getContriByJobRank(animal.u_rankCode==0?7:animal.u_rankCode)*/;
					}else{
						//升到50级
					}
					experienceProgressTV.setText(""+expDex1+"/"+expDexTotal);
					LayoutParams params=experenceProgressView.getLayoutParams();
					if(params==null){
						params=new RelativeLayout.LayoutParams((int)(width*(expDex1*1f/(expDexTotal*1f))), RelativeLayout.LayoutParams.WRAP_CONTENT);
					}else{
						params.width=(int)(width*(expDex1*1f/(expDexTotal*1f)));
					}
					experenceProgressView.setLayoutParams(params);
				}
				return true;
			}
		});
		
		
		
		holder.kingdomNameTv.setText(animal.u_rank);
		
		holder.userJobTv.setText("萌星 "+animal.pet_nickName);
		if(Constants.user!=null&&Constants.user.currentAnimal!=null&&animal.a_id==Constants.user.currentAnimal.a_id){
			holder.greenKingHatIv.setVisibility(View.VISIBLE);
		}else{
			holder.greenKingHatIv.setVisibility(View.INVISIBLE);
		}
		holder.experienceTv.setText(""+animal.user.t_contri+"/100");
		holder.hotNumTv.setText(""+animal.d_rq);
		holder.trendsNumTv.setText(""+animal.news_count);
		holder.peoplesNumTv.setText(""+animal.fans);
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl, holder.iconCircleView, displayImageOptions);
		
		holder.leftLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.isSuccess&&Constants.user!=null&&Constants.user.userId==user.userId){
					showButtons(animal);
				}else{
					if(PetKingdomActivity.petKingdomActivity!=null){
						if(PetKingdomActivity.petKingdomActivity.loadedImage1!=null){
							if(!PetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
								PetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
								PetKingdomActivity.petKingdomActivity.loadedImage1=null;
							}
							PetKingdomActivity.petKingdomActivity.linearLayout2.setBackgroundDrawable(null);
						}
						PetKingdomActivity.petKingdomActivity.finish();
					}
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
		/*holder.quitKingdomLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				MarginLayoutParams params=(MarginLayoutParams)leftLayout.getLayoutParams();
				params.leftMargin=0;
				leftLayout.setLayoutParams(params);
				rightLayout.setVisibility(View.GONE);
				if(Constants.user!=null&&Constants.user.userId==animal.master_id){
					Toast.makeText(context, "不能退出自己创建的联萌", Toast.LENGTH_LONG).show();
					return;
				}
				if(Constants.user!=null&&Constants.user.currentAnimal!=null&&animal.a_id==Constants.user.currentAnimal.a_id){
					Toast.makeText(context, "不能退出默认联萌，请先取消默认", Toast.LENGTH_LONG).show();
					return;
				}
				DialogQuitKingdom dialog=new DialogQuitKingdom(context.popupParent, context, context.black_layout, animal);
				dialog.setResultListener(new DialogQuitKingdom.ResultListener() {
					
					@Override
					public void getResult(boolean isSuccess) {
						// TODO Auto-generated method stub
						
						if(isSuccess){
							
							context.userKingdomsChanged(animal);
						}
					}
				});
			}
		});
		holder.changKingdomLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MarginLayoutParams params=(MarginLayoutParams)leftLayout.getLayoutParams();
				params.leftMargin=0;
				leftLayout.setLayoutParams(params);
				rightLayout.setVisibility(View.GONE);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final boolean flag=HttpUtil.setDefaultKingdom(context,animal, handleHttpConnectionException.getHandler(context));
						final Animal newA=HttpUtil.animalInfo(context,animal, handleHttpConnectionException.getHandler(context));
						if(newA!=null&&newA.master_id!=0){
							Constants.user.currentAnimal=newA;
						}
						handleHttpConnectionException.getHandler(context).post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								list.remove(newA);
								list.add(0,newA);
								UserKingdomListAdapter.this.notifyDataSetChanged();
								UserStatusUtil.setDefaultKingdom();
							}
						});
					}
				}).start();
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
					
//					break;
					return true;

				case MotionEvent.ACTION_MOVE:
					
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
                	   if(params.leftMargin>-maxW/10){
                		   
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
					 MarginLayoutParams params=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
              	   MarginLayoutParams rightParams=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
					if(distance>maxW/10){
						
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
				if(distance>maxW/10){
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
	class Holder{
		LinearLayout rightLayout,quitKingdomLayout,changKingdomLayout,linearLayout3;
		RelativeLayout leftLayout,progressParentLayout,joinRlaout;
		TextView userJobTv,kingdomNameTv,experienceTv,hotNumTv,trendsNumTv,peoplesNumTv;
		View experienceView;
		ImageView greenKingHatIv;
		RoundImageView iconCircleView;
	}
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
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				  new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							final boolean flag=HttpUtil.setDefaultKingdom(context,animal, handleHttpConnectionException.getHandler(context));
							if(flag){
								final User newA=HttpUtil.info(context,handleHttpConnectionException.getHandler(context),Constants.user.userId);
								newA.aniList=Constants.user.aniList;
								newA.currentAnimal=animal;
								Constants.user=newA;
								Constants.user.currentAnimal=animal;
								handleHttpConnectionException.getHandler(context).post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										list.remove(animal);
										list.add(0,animal);
										notifyDataSetChanged();
										UserStatusUtil.setDefaultKingdom();
									}
								});
							}else{
                             handleHttpConnectionException.getHandler(context).post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(context, "操作失败", Toast.LENGTH_LONG).show();
										context.progresslayout.setVisibility(View.INVISIBLE);
										context.progresslayout.setClickable(false);
										isShowingButton=false;
									}
								});
							}
							
						}
					}).start();
				context.progresslayout.setVisibility(View.INVISIBLE);
				context.progresslayout.setClickable(false);
						isShowingButton=false;
				
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.user!=null&&Constants.user.userId==animal.master_id){
					Toast.makeText(context, "不能不捧自己创建的萌星", Toast.LENGTH_LONG).show();
					context.progresslayout.setVisibility(View.INVISIBLE);
					context.progresslayout.setClickable(false);
					isShowingButton=false;
					return;
				}
				if(Constants.user!=null&&Constants.user.aniList!=null&&Constants.user.aniList.size()==1){
					Toast.makeText(context, "不能不捧最爱萌星哦，现在只剩一个啦", Toast.LENGTH_LONG).show();
					context.progresslayout.setVisibility(View.INVISIBLE);
					context.progresslayout.setClickable(false);
					isShowingButton=false;
					return;
				}
				if(Constants.user!=null&&Constants.user.aniList!=null)
				for(int i=0;i<list.size();i++){
					for(int j=0;j<Constants.user.aniList.size();j++){
						if(list.get(i).a_id==Constants.user.aniList.get(j).a_id){
							Constants.user.aniList.get(j).t_contri=list.get(i).t_contri;
						}
					}
					
				}
				
				
				DialogQuitKingdom dialog=new DialogQuitKingdom(context.popupParent, context, context.black_layout, animal);
				dialog.setResultListener(new DialogQuitKingdom.ResultListener() {
					
					@Override
					public void getResult(boolean isSuccess) {
						// TODO Auto-generated method stub
						
						if(isSuccess){
							
							context.userKingdomsChanged(animal);
							
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
	
	public void setDefaultPet(final Animal animal){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final boolean flag=HttpUtil.setDefaultKingdom(context,animal, handleHttpConnectionException.getHandler(context));
				if(flag){
					final User newA=HttpUtil.info(context,handleHttpConnectionException.getHandler(context),Constants.user.userId);
					newA.aniList=Constants.user.aniList;
					newA.currentAnimal=animal;
					Constants.user=newA;
					Constants.user.currentAnimal=animal;
					handleHttpConnectionException.getHandler(context).post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							list.remove(animal);
							list.add(0,animal);
							notifyDataSetChanged();
							UserStatusUtil.setDefaultKingdom();
						}
					});
				}else{
                 handleHttpConnectionException.getHandler(context).post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(context, "操作失败", Toast.LENGTH_LONG).show();
							context.progresslayout.setVisibility(View.INVISIBLE);
							context.progresslayout.setClickable(false);
							isShowingButton=false;
						}
					});
				}
				
			}
		}).start();
	}
	

}
