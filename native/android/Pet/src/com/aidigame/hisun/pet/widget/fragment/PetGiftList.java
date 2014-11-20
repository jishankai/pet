package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.pet.adapter.MarketGridViewAdapter;
import com.aidigame.hisun.pet.adapter.UserGiftGridViewAdapter;
import com.aidigame.hisun.pet.adapter.UserKingdomListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.miloisbadboy.view.PullToRefreshView;

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
public class PetGiftList {
   View view;
   PetKingdomActivity context;
   GridView gridView;
//   MarketGridViewAdapter adapter;
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
                            gridView.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									int h=gridView.getMeasuredHeight();
									LogUtil.i("me", "============gridview===============的高度是"+h);
									int n=list.size()/3;
									int left=list.size()%3;
									if(n==0)n++;
									if(left!=0)n++;
									LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,h*n);
									gridView.setLayoutParams(param);
								}
							});
						}
					}
				});
			}
		}).start();
	
   }
	
	public void onRefresh(final PullToRefreshView pullToRefreshView) {
		// TODO Auto-generated method stub
//		pullView.onRefreshFinish();
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
		                    gridView.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									int h=gridView.getMeasuredHeight();
									LogUtil.i("me", "============gridview===============的高度是"+h);
									int n=list.size()/3;
									int left=list.size()%3;
									if(n==0)n++;
									if(left!=0)n++;
									LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,h*n);
									gridView.setLayoutParams(param);
								}
							});
						}
						if(pullToRefreshView!=null)
							pullToRefreshView.onHeaderRefreshComplete();
					}
				});
			}
		}).start();
		
	}
	public void onMore(final PullToRefreshView pullToRefreshView) {
		// TODO Auto-generated method stub
	}
	public View getView(){
		return view;
	}

	
	

}
