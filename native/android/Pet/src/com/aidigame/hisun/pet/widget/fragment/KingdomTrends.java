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
import com.aidigame.hisun.pet.view.LinearLayoutForListView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.miloisbadboy.view.PullToRefreshView;

import android.content.Context;
import android.graphics.Color;
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
public class KingdomTrends {
	LinearLayoutForListView listView;
   View view;
   PetKingdomActivity context;
   ArrayList<PetNews> petNewsList;
   KingdomTrendsListAdapter adapter;
   HandleHttpConnectionException handleHttpConnectionException;
   int nid=-1;
   Animal animal;
   public KingdomTrends(PetKingdomActivity context,Animal animal){
	 view=LayoutInflater.from(context).inflate(R.layout.widget_llistview1, null);
	  this.listView=(LinearLayoutForListView)view.findViewById(R.id.listView1);
	   this.context=context;
	   this.animal=animal;
	   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
	   initView();
   }
	private void initView() {
	// TODO Auto-generated method stub
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
							adapter=new KingdomTrendsListAdapter(petNewsList, context,animal);
							listView.setAdapter(adapter);
						}
					}
				});
			}
		}).start();
		adapter=new KingdomTrendsListAdapter(petNewsList, context,animal);
		listView.setAdapter(adapter);
	
   }
	public void onRefresh(final PullToRefreshView pullToRefreshView) {
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
									
								}else{
									for(int i=0;i<temp.size();i++){
										if(!petNewsList.contains(temp.get(i))){
											petNewsList.add(i,temp.get(i));
										}
									}
								}
							}
						}
						if(temp!=null&&temp.size()>0){
							petNewsList=temp;
							adapter=new KingdomTrendsListAdapter(petNewsList, context,animal);
							listView.setAdapter(adapter);
						}
						if(pullToRefreshView!=null){
						pullToRefreshView.onHeaderRefreshComplete();
						}
						
					}
				});
			}
		}).start();
		
	}
	public void onMore(final PullToRefreshView pullToRefreshView) {
		// TODO Auto-generated method stub
		if(petNewsList.size()==0){
			nid=-1;
		}else{
			nid=petNewsList.get(petNewsList.size()-1).nid;
		}

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetNews> temp=HttpUtil.kingdomTrends(context,nid, animal.a_id, handleHttpConnectionException.getHandler(context));
			    handleHttpConnectionException.getHandler(context).post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int start=petNewsList.size();
						if(temp!=null&&temp.size()>0){
							if(petNewsList.size()>0){
									for(int i=0;i<temp.size();i++){
										if(!petNewsList.contains(temp.get(i))){
											petNewsList.add(temp.get(i));
										}
									}
							}
						}
						if(petNewsList.size()>0){
							adapter.update(petNewsList);
							adapter.notifyDataSetChanged();
							listView.update(start);
						}
						if(pullToRefreshView!=null){
							pullToRefreshView.onFooterRefreshComplete();
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

}
