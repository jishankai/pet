package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.pet.adapter.UserGiftGridViewAdapter;
import com.aidigame.hisun.pet.adapter.UserKingdomListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
/**
 * 用户加入的王国列表
 * @author admin
 *
 */
public class PetGiftList implements PullToRefreshAndMoreListener{
//   PullToRefreshAndMoreView pullView;
   View view;
   PetKingdomActivity context;
//   ListView listView;
   GridView gridView;
//   UserKingdomListAdapter adapter;
   UserGiftGridViewAdapter adapter;
   ArrayList< Gift> list;
   HandleHttpConnectionException handleHttpConnectionException;
   LinearLayout layout;
   Animal animal;
   public PetGiftList(PetKingdomActivity context,Animal animal){
	 view=LayoutInflater.from(context).inflate(R.layout.widget_gridview, null);
//	  this.pullView=(PullToRefreshAndMoreView)view.findViewById(R.id.listview_linearLayout3);
	   this.context=context;
	   this.animal=animal;
	   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
	   initView();
	   initListener();
   }
	private void initView() {
	// TODO Auto-generated method stub
//		listView=this.pullView.getListView();
		gridView=(GridView)view.findViewById(R.id.gridview);
		layout=(LinearLayout)view.findViewById(R.id.layout);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));
		list=new ArrayList<Gift>();
		adapter=new UserGiftGridViewAdapter(context,list);
		gridView.setAdapter(adapter);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Gift> temp=HttpUtil.kingdomGift(context,animal, handleHttpConnectionException.getHandler(context));
				handleHttpConnectionException.getHandler(context).post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null){
							list=temp;
							adapter.update(list);
							adapter.notifyDataSetChanged();
//							setBlurImageBackground();
						}
					}
				});
			}
		}).start();
		/*adapter=new UserKingdomListAdapter(list,context);
		listView.setAdapter(adapter);
		listView.setDivider(null);
		this.pullView.setListener(this);*/
	
   }
	public void refresh(){
		final ArrayList<Gift> temp=HttpUtil.kingdomGift(context,animal, handleHttpConnectionException.getHandler(context));
		handleHttpConnectionException.getHandler(context).post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(temp!=null){
					list=temp;
					adapter.update(list);
					adapter.notifyDataSetChanged();
//					setBlurImageBackground();
				}
			}
		});
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
//		pullView.onRefreshFinish();
		final ArrayList<Gift> temp=HttpUtil.kingdomGift(context,animal, handleHttpConnectionException.getHandler(context));
		handleHttpConnectionException.getHandler(context).post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(temp!=null){
					list=temp;
					adapter.update(list);
					adapter.notifyDataSetChanged();
//					setBlurImageBackground();
				}
			}
		});
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
		gridView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(scrollState!=SCROLL_STATE_IDLE)return;
					LogUtil.i("scroll", "context.isShowInfoLayout="+context.isShowInfoLayout);
					if(gridView.getFirstVisiblePosition()==0&&gridView.getChildAt(0)!=null/*&&gridView.getChildAt(0).getBottom()==gridView.getChildAt(0).getHeight()*/){
						LogUtil.i("scroll", "context.isShowInfoLayout="+context.isShowInfoLayout);
						if(!context.isShowInfoLayout){
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
						if(context.isShowInfoLayout){
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
	} 
	float infoYTop;//
	float infoYBottom;
	float animHeight;
	boolean isRecord=false;
	public void animationOut(){
		if(!isRecord){
			infoYTop=context.linearLayout2.getY();
			infoYBottom=context.linearLayout2.getBottom();
//			animHeight=context.anim_layout.getHeight();
			isRecord=true;
		}
		
		TranslateAnimation anim=new TranslateAnimation(0, 0, 0, -infoYBottom);
		anim.setDuration(100);
		anim.setFillAfter(false);
		/*TranslateAnimation anim2=new TranslateAnimation(0, 0, 0, -infoYBottom);
		anim2.setDuration(100);
		anim2.setFillAfter(false);*/
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
	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		gridView.setBackgroundResource(R.color.blur_view_top);
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
						layout.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
						layout.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
	}
	
	

}
