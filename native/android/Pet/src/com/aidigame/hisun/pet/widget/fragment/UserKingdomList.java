package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.pet.adapter.UserKingdomListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.AlbumPictureBackground;
import com.aidigame.hisun.pet.ui.ChoseAcountTypeActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.TakePictureBackground;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
/**
 * 用户加入的王国列表
 * @author admin
 *
 */
public class UserKingdomList implements PullToRefreshAndMoreListener{
//   PullToRefreshAndMoreView pullView;
   View view;
   UserDossierActivity context;
   ListView listView;
  public  UserKingdomListAdapter adapter;
   User user;
  public  ArrayList<Animal> animalList;
   HandleHttpConnectionException handleHttpConnectionException;
   public UserKingdomList(UserDossierActivity context,User user){
	 view=LayoutInflater.from(context).inflate(R.layout.widget_llistview, null);
//	  this.pullView=(PullToRefreshAndMoreView)view.findViewById(R.id.listview_linearLayout3);
	 listView=(ListView)view.findViewById(R.id.listView1);  
	 this.context=context;
	   this.user=user;
	   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
	   initView();
	   initListener();
   }
	private void initView() {
	// TODO Auto-generated method stub
//		listView=this.pullView.getListView();
		listView.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));
		listView.setSelector(R.color.dossier_tab_color);
//		setBlurImageBackground();
		animalList=new ArrayList<Animal>();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> temp=HttpUtil.usersKingdom(context,user, -1, handleHttpConnectionException.getHandler(context));
				if(temp!=null&&temp.size()>0){
	               context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.update(temp);
						adapter.notifyDataSetChanged();
						animalList=temp;
					}
				});
				}
			}
		}).start();
		adapter=new UserKingdomListAdapter(animalList,context,user);
		listView.setAdapter(adapter);
		listView.setDivider(null);
//		this.pullView.setListener(this);
	
   }
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
       new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> temp=HttpUtil.usersKingdom(context,user, -1, handleHttpConnectionException.getHandler(context));
				if(temp!=null&&temp.size()>0){
	               context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.update(temp);
						adapter.notifyDataSetChanged();
						animalList=temp;
//						pullView.onRefreshFinish();
						
					}
				});
				}
//				pullView.onRefreshFinish();
			}
		}).start();
		
	}
	@Override
	public void onMore() {
		// TODO Auto-generated method stub
//		pullView.onMoreFinish();
	}
	public View getView(){
		return view;
	}
	
	boolean isShow=true;
	private void initListener() {
			// TODO Auto-generated method stub
		
		listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(scrollState!=SCROLL_STATE_IDLE)return;
					LogUtil.i("scroll", "UserKingdomListAdapter.isSlidingTouch="+UserKingdomListAdapter.isSlidingTouch);
					if(UserKingdomListAdapter.isSlidingTouch){
						UserKingdomListAdapter.isSlidingTouch=false;
						return;
					}

					LogUtil.i("scroll", "UserKingdomListAdapter.isSlidingTouch="+UserKingdomListAdapter.isSlidingTouch);
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0)!=null/*&&listView.getChildAt(0).getBottom()==listView.getChildAt(0).getHeight()*/){

						if(!isShow){
//							animationIn();
							if(isShow){
								isShow=false;
								return;
							}
							isShow=true;
							context.linearLayout3.setVisibility(View.VISIBLE);
							context.infoShadowView.setVisibility(View.VISIBLE);
							context.topWhiteView.setVisibility(View.VISIBLE);
							context.isShowInfoLayout=true;
							
						}
						
					}else{
						if(isShow){
							animationOut();
							
						}
						
						
					}
					
					
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
					
				}
			});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(Constants.isSuccess&&Constants.user!=null&&Constants.user.userId==user.userId){
					showButtons(animalList.get(position));
				}else{
					if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.finish();
					Intent intent=new Intent(context,PetKingdomActivity.class);
					intent.putExtra("animal", animalList.get(position));
					context.startActivity(intent);
					
					context.progresslayout.setVisibility(View.INVISIBLE);
					context.progresslayout.setClickable(false);
					isShowingButton=false;
				}
				
			}
		});
	} 
	float infoYTop;//
	float infoYBottom;
	boolean isRecord=false;
	public void animationOut(){
		if(!isRecord){
			infoYTop=context.linearLayout2.getY();
			infoYBottom=context.linearLayout2.getBottom();
			isRecord=true;
		}
		
		TranslateAnimation anim=new TranslateAnimation(0, 0, 0, -infoYBottom);
		anim.setDuration(100);
		anim.setFillAfter(false);
//		TranslateAnimation anim2=new TranslateAnimation(0, 0, 0, -infoYBottom);
//		anim2.setDuration(100);
//		anim2.setFillAfter(false);
        anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
//				context.linearLayout3.setVisibility(View.GONE);
				context.infoShadowView.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
//				context.linearLayout2.layout(0, (int) -infoYBottom, context.linearLayout2.getWidth(),0);
				context.linearLayout3.setVisibility(View.GONE);
				context.infoShadowView.setVisibility(View.GONE);
				context.topWhiteView.setVisibility(View.GONE);
				context.isShowInfoLayout=false;
				isShow=false;
			}
		});
		/*anim2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
//				context.linearLayout3.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
//				context.linearLayout2.layout(0, (int) -infoYBottom, context.linearLayout2.getWidth(),0);
				context.linearLayout3.setVisibility(View.GONE);
				context.infoShadowView.setVisibility(View.GONE);
				context.topWhiteView.setVisibility(View.GONE);
				context.isShowInfoLayout=false;
				isShow=false;
			}
		});*/
//		context.infoShadowView.clearAnimation();
//		context.infoShadowView.startAnimation(anim2);
		context.linearLayout3.clearAnimation();
		context.linearLayout3.startAnimation(anim);
		
	}
	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		listView.setBackgroundResource(R.color.blur_view_top);;
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(HomeFragment.blurBitmap==null){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						pullView.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
//						pullView.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
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
										animalList.remove(animal);
										animalList.add(0,animal);
										adapter.notifyDataSetChanged();
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
					Toast.makeText(context, "不能退出自己创建的联萌", Toast.LENGTH_LONG).show();
					context.progresslayout.setVisibility(View.INVISIBLE);
					context.progresslayout.setClickable(false);
					isShowingButton=false;
					return;
				}
				if(Constants.user!=null&&Constants.user.currentAnimal!=null&&animal.a_id==Constants.user.currentAnimal.a_id){
					Toast.makeText(context, "不能退出默认联萌，请先取消默认", Toast.LENGTH_LONG).show();
					context.progresslayout.setVisibility(View.INVISIBLE);
					context.progresslayout.setClickable(false);
					isShowingButton=false;
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
