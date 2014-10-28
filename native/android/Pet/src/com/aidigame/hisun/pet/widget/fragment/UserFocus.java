package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.UserFocusAdapter;
import com.aidigame.hisun.pet.adapter.UserKingdomListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
/**
 * 用户关注的王国列表
 * @author admin
 *
 */
public class UserFocus implements PullToRefreshAndMoreListener{
//   PullToRefreshAndMoreView ptram;
   View view;
   UserDossierActivity context;
   ListView focusListView;
   UserFocusAdapter focusAdapter;
   ArrayList<Animal> userDatas;
   HandleHttpConnectionException handleHttpConnectionException;
   long last_focus_id=-1;
   User user;
   public UserFocus(UserDossierActivity context,User user){
	 view=LayoutInflater.from(context).inflate(R.layout.widget_llistview, null);
//	  this.ptram=(PullToRefreshAndMoreView)view.findViewById(R.id.listview_linearLayout3);
	  focusListView=(ListView)view.findViewById(R.id.listView1);
	 this.context=context;
	   this.user=user;
	   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
	   initView();
	   initListener();
   }

   private void initView() {
	// TODO Auto-generated method stub
//		ptram.setListener(this);
//		focusListView=ptram.getListView();
		focusListView.setDivider(null);
//		setBlurImageBackground();
		focusListView.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));
		focusListView.setSelector(R.color.dossier_tab_color);
		userDatas=new ArrayList<Animal>();
		focusAdapter=new UserFocusAdapter(context, userDatas, 1,null,user);
		focusListView.setAdapter(focusAdapter);
		last_focus_id=-1;
//		ptram.setHeaderAndFooterInvisible();
		new Thread(new Runnable() {
			ArrayList<Animal> temp=new ArrayList<Animal>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> josn=HttpUtil.followList(null, last_focus_id,user.userId,context);
				LogUtil.i("scroll", "下载关注列表，结果返回");
				if(josn!=null){
					if(josn.size()>0){
						Animal data=null;
						for(int i=0;i<josn.size();i++){
							data=josn.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
						context.runOnUiThread(new Runnable() {
							public void run() {
								focusAdapter.updateList(temp);
								userDatas=temp;
								focusAdapter.notifyDataSetChanged();
								LogUtil.i("scroll", "下载关注列表，adapter更新");
							}
						});
					}
				}
			}
		}).start();
//		ptram.setHeaderAndFooterInvisible();
   }
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		final ArrayList<Animal> temp=new ArrayList<Animal>();
		last_focus_id=-1;
		final ArrayList<Animal> josn=HttpUtil.followList(null, last_focus_id,user.userId,context);
		if(josn!=null){
			if(josn.size()>0){
				
				Animal data=null;
				for(int i=0;i<josn.size();i++){
					data=josn.get(i);
					if(!temp.contains(data)){
						temp.add(data);
					}
				}
				context.runOnUiThread(new Runnable() {
					public void run() {
						focusAdapter.updateList(temp);
						userDatas=temp;
						focusAdapter.notifyDataSetChanged();
//						ptram.onRefreshFinish();
					}
				});
			}else{
//				ptram.onRefreshFinish();
			}
		}else{
//			ptram.onRefreshFinish();
		}
	}
	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		final ArrayList<Animal> temp=new ArrayList<Animal>();
		if(userDatas.size()>0){
			last_focus_id=userDatas.get(userDatas.size()-1).a_id;
		}else{
			last_focus_id=-1;
		}
		final ArrayList<Animal> josn=HttpUtil.followList(null, last_focus_id,1,context);
		if(josn!=null){
			if(josn.size()>0){
				Animal data=null;
				for(int i=0;i<userDatas.size();i++){
					temp.add(userDatas.get(i));
				}
				for(int i=0;i<josn.size();i++){
					data=josn.get(i);
					if(!temp.contains(data)){
						temp.add(data);
					}
				}
				context.runOnUiThread(new Runnable() {
					public void run() {
						focusAdapter.updateList(temp);
						userDatas=temp;
						focusAdapter.notifyDataSetChanged();
//						ptram.onMoreFinish();
					}
				});
			}else{
//				ptram.onMoreFinish();
			}
		}else{
//			ptram.onMoreFinish();
		}
	}
	boolean isShow=true;
	private void initListener() {
			// TODO Auto-generated method stub
			focusListView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(scrollState!=SCROLL_STATE_IDLE)return;
					LogUtil.i("scroll", "UserKingdomListAdapter.isSlidingTouch="+UserFocusAdapter.isSlidingTouch);
					if(UserFocusAdapter.isSlidingTouch){
						UserFocusAdapter.isSlidingTouch=false;
						return;
					}

					if(focusListView.getFirstVisiblePosition()==0&&focusListView.getChildAt(0)!=null&&focusListView.getChildAt(0).getBottom()==focusListView.getChildAt(0).getHeight()){
						
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
			focusListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(Constants.isSuccess&&Constants.user!=null&&Constants.user.userId==user.userId){
						showButtons(userDatas.get(position));
					}else{
						if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.finish();
						Intent intent=new Intent(context,PetKingdomActivity.class);
						intent.putExtra("animal", userDatas.get(position));
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
	float animHeight;
	boolean isRecord=false;
	public void animationOut(){
		if(!isRecord){
			infoYTop=context.linearLayout2.getY();
			infoYBottom=context.linearLayout2.getBottom();
			isRecord=true;
		}
		
		TranslateAnimation anim=new TranslateAnimation(0, 0, 0, -infoYBottom);
		anim.setDuration(5);
		anim.setFillAfter(false);
//		TranslateAnimation anim2=new TranslateAnimation(0, 0, 0, -infoYBottom);
//		anim2.setDuration(100);
//		anim2.setFillAfter(false);
        anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
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
				context.linearLayout3.setVisibility(View.GONE);
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
		context.linearLayout3.clearAnimation();
		context.linearLayout3.startAnimation(anim);
//		context.infoShadowView.clearAnimation();
//		context.infoShadowView.startAnimation(anim2);
	}
	

	public View getView(){
		return view;
	}
	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		focusListView.setBackgroundResource(R.color.blur_view_top);;
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
//						ptram.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
//						ptram.setAlpha(0.9342857f);
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
								p.userFocus.onRefresh();
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
