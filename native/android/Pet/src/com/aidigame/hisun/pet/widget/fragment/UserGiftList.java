package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.MarketGridViewAdapter;
import com.aidigame.hisun.pet.adapter.UserGiftGridViewAdapter;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.miloisbadboy.view.PullToRefreshView;
/**
 * 用户加入的王国列表
 * @author admin
 *
 */
public class UserGiftList {
//   PullToRefreshAndMoreView pullView;
   View view;
   UserDossierActivity context;
//   ListView listView;
   GridView gridView;
   UserGiftGridViewAdapter adapter;
//   MarketGridViewAdapter marketGridViewAdapter;
   User user;
   LinearLayout layout;
   ArrayList< Gift> list;
   HandleHttpConnectionException handleHttpConnectionException;
   public UserGiftList(UserDossierActivity context,User user){
	 view=LayoutInflater.from(context).inflate(R.layout.widget_gridview, null);

	   this.context=context;
	   this.user=user;
	   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
	   initView();
   }
	private void initView() {
	// TODO Auto-generated method stub
		gridView=(GridView)view.findViewById(R.id.gridview);
		gridView.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));
		layout=(LinearLayout)view.findViewById(R.id.layout);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		list=new ArrayList<Gift>();
		adapter=new UserGiftGridViewAdapter(context,list);
		gridView.setAdapter(adapter);
//		marketGridViewAdapter=new MarketGridViewAdapter(context, list);
//		gridView.setAdapter(marketGridViewAdapter);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Gift> temp=HttpUtil.userItems(context,user, -1, handleHttpConnectionException.getHandler(context));
                handleHttpConnectionException.getHandler(context).post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null){
							list=temp;
//							marketGridViewAdapter.updateList(list);
//							marketGridViewAdapter.notifyDataSetChanged();
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
				final ArrayList<Gift> temp=HttpUtil.userItems(context,user, -1, handleHttpConnectionException.getHandler(context));
		        handleHttpConnectionException.getHandler(context).post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null){
							list=temp;
//							marketGridViewAdapter.updateList(list);
//							marketGridViewAdapter.notifyDataSetChanged();
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
	public void onMore() {
		// TODO Auto-generated method stub
	}
	public View getView(){
		return view;
	}
	
	boolean isShow=true;

	

}
