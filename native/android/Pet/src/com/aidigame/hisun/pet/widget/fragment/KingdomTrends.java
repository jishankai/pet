package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetNews;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;
/**
 * 王国的最新动态列表
 * @author admin
 *
 */
public class KingdomTrends implements PullToRefreshAndMoreListener{
   PullToRefreshAndMoreView pullView;
   View view;
   PetKingdomActivity context;
   ListView listView;
   ArrayList<PetNews> petNewsList;
   KingdomTrendsListAdapter adapter;
   HandleHttpConnectionException handleHttpConnectionException;
   int nid=-1;
   Animal animal;
   public KingdomTrends(PetKingdomActivity context,Animal animal){
	 view=LayoutInflater.from(context).inflate(R.layout.widget_pull_to_refresh_and_more, null);
	  this.pullView=(PullToRefreshAndMoreView)view.findViewById(R.id.listview_linearLayout3);
	   this.context=context;
	   this.animal=animal;
	   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
	   initView();
	   initListener();
   }
	private void initView() {
	// TODO Auto-generated method stub
		listView=this.pullView.getListView();
		listView.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));
//		setBlurImageBackground();
		nid=-1;
		petNewsList=new ArrayList<PetNews>();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetNews> temp=HttpUtil.kingdomTrends(context,nid, animal.a_id, handleHttpConnectionException.getHandler(context));
			    handleHttpConnectionException.getHandler(context).post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null&&temp.size()>0){
							petNewsList=temp;
							adapter.update(petNewsList);
							adapter.notifyDataSetChanged();
						}
					}
				});
			}
		}).start();
		adapter=new KingdomTrendsListAdapter(petNewsList, context,animal);
		listView.setAdapter(adapter);
		listView.setDivider(null);
		this.pullView.setListener(this);
		this.pullView.setHeaderAndFooterInvisible();
	
   }
	public void refresh(){
		nid=-1;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetNews> temp=HttpUtil.kingdomTrends(context,nid, animal.a_id, handleHttpConnectionException.getHandler(context));
			    handleHttpConnectionException.getHandler(context).post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null&&temp.size()>0){
							if(petNewsList.size()>0){
								if(temp.get(0).nid==petNewsList.get(0).nid){
									return;
								}else{
									for(int i=0;i<temp.size();i++){
										if(!petNewsList.contains(temp.get(i))){
											petNewsList.add(i,temp.get(i));
										}
									}
									adapter.update(petNewsList);
									adapter.notifyDataSetChanged();
								}
							}else{
								petNewsList=temp;
								adapter.update(petNewsList);
								adapter.notifyDataSetChanged();
							}
						}else{
						}
					}
				});
			}
		}).start();
		
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		nid=-1;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetNews> temp=HttpUtil.kingdomTrends(context,nid, animal.a_id, handleHttpConnectionException.getHandler(context));
			    handleHttpConnectionException.getHandler(context).post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null&&temp.size()>0){
							if(petNewsList.size()>0){
								if(temp.get(0).nid==petNewsList.get(0).nid){
									pullView.onRefreshFinish();
									return;
								}else{
									for(int i=0;i<temp.size();i++){
										if(!petNewsList.contains(temp.get(i))){
											petNewsList.add(i,temp.get(i));
										}
									}
									adapter.update(petNewsList);
									adapter.notifyDataSetChanged();
								}
							}else{
								petNewsList=temp;
								adapter.update(petNewsList);
								adapter.notifyDataSetChanged();
							}
							pullView.onRefreshFinish();
						}else{
							pullView.onRefreshFinish();
						}
					}
				});
			}
		}).start();
		
	}
	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		if(petNewsList.size()==0){
			pullView.onMoreFinish();
			return;
		}
		nid=petNewsList.size()-1;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetNews> temp=HttpUtil.kingdomTrends(context,nid, animal.a_id, handleHttpConnectionException.getHandler(context));
			    handleHttpConnectionException.getHandler(context).post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null&&temp.size()>0){
							if(petNewsList.size()>0){
									for(int i=0;i<temp.size();i++){
										if(!petNewsList.contains(temp.get(i))){
											petNewsList.add(petNewsList.get(i));
										}
									}
									adapter.update(petNewsList);
									adapter.notifyDataSetChanged();
							}else{
								petNewsList=temp;
								adapter.update(petNewsList);
								adapter.notifyDataSetChanged();
							}
							pullView.onRefreshFinish();
						}else{
							pullView.onRefreshFinish();
						}
					}
				});
			}
		}).start();
		
	}
	public View getView(){
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(params);
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
					LogUtil.i("scroll", "context.isShowInfoLayout="+context.isShowInfoLayout);
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0)!=null/*&&listView.getChildAt(0).getBottom()==listView.getChildAt(0).getHeight()*/){
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
						pullView.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
						pullView.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
	}

}
