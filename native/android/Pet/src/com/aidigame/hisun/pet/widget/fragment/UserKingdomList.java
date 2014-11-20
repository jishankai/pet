package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import me.maxwin.view.XListView;

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
import com.aidigame.hisun.pet.view.LinearLayoutForListView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.miloisbadboy.view.PullToRefreshView;

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
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
/**
 * 用户加入的王国列表
 * @author admin
 *
 */
public class UserKingdomList {

   View view;
   UserDossierActivity context;
   LinearLayoutForListView listView;
  public  UserKingdomListAdapter adapter;
   User user;
  public  ArrayList<Animal> animalList;
   HandleHttpConnectionException handleHttpConnectionException;
   public UserKingdomList(UserDossierActivity context,User user){
	 view=LayoutInflater.from(context).inflate(R.layout.widget_llistview1, null);

	 listView=(LinearLayoutForListView)view.findViewById(R.id.listView1);  
	 this.context=context;
	   this.user=user;
	   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
	   initView();
   }
	private void initView() {
	// TODO Auto-generated method stub
		listView.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));

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
					
						animalList=temp;
						adapter=new UserKingdomListAdapter(animalList,context,user);
						listView.setAdapter(adapter);
					}
				});
				}
			}
		}).start();
		adapter=new UserKingdomListAdapter(animalList,context,user);
		listView.setAdapter(adapter);
		 
   }
	public void onRefresh(final PullToRefreshView pullToRefreshView) {
		// TODO Auto-generated method stub
       new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> temp=HttpUtil.usersKingdom(context,user, -1, handleHttpConnectionException.getHandler(context));
				
	               context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null&&temp.size()>0){
						animalList=temp;
						adapter=new UserKingdomListAdapter(animalList,context,user);
						listView.setAdapter(adapter);
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
//		pullView.onMoreFinish();
		pullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
			
				pullToRefreshView.onFooterRefreshComplete();;
			}
		},1000);
	}
	public View getView(){
		LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(param);
		return view;
	}

	
	boolean isShowingButton=false;
	
}
