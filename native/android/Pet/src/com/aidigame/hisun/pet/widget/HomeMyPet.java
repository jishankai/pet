package com.aidigame.hisun.pet.widget;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListViewHeader;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomeMyPetAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;

public class HomeMyPet implements IXListViewListener{
	private  Activity activity;
	private  View view;
	private  XListView listview;
	public HomeMyPetAdapter adapter;
	private  ArrayList<Animal> animals;
	private  Handler handler;

	public HomeMyPet(Activity context){
		this.activity=context;
		init();
	}
	public void init(){
		view=LayoutInflater.from(activity).inflate(R.layout.widget_home_pet_pictures, null);
		listview=(XListView)view.findViewById(R.id.listview);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this);
		animals=new ArrayList<Animal>();
		adapter=new HomeMyPetAdapter(activity, animals);
		listview.setAdapter(adapter);
		listview.setSelector(R.color.transparent);
	
		handler=HandleHttpConnectionException.getInstance().getHandler(activity);
//		refresh();
		handler.postAtTime(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pullRefresh();
			}
		}, 1000);
		
	}
	public View getView(){
		return view;
	}
	private  boolean isRefresh=false;
	public void refresh(){
		if(isRefresh)return;
		isRefresh=true;
		adapter.update(new ArrayList<Animal>());
		adapter.notifyDataSetChanged();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> temp=HttpUtil.myPetCard(handler,activity);
				
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null&&temp.size()>0){
							animals=new ArrayList<Animal>();
							for(int i=0;i<temp.size();i++){
								if(temp.get(i).master_id==PetApplication.myUser.userId){
									animals.add(temp.get(i));
								}
							}
							for(int i=0;i<temp.size();i++){
								if(!animals.contains(temp.get(i))){
									animals.add(temp.get(i));
								}
							}
							
							
							adapter.update(animals);
							animals=animals;
							adapter.notifyDataSetChanged();
							
							
						}
						isRefresh=false;
						
					}
					
					
				});
			}
		}).start();
		
	}
	
	
	 public void pullRefresh(){
		 isRefresh=true;
			adapter.update(new ArrayList<Animal>());
			adapter.notifyDataSetChanged();
	    	listview.updateHeaderHeight(listview.mHeaderViewHeight);
	    	listview.mHeaderView.setVisibility(View.VISIBLE);
	    	listview.mPullRefreshing = true;
	    	listview.mEnablePullRefresh=true;
	    	listview.mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
				if (listview.mListViewListener != null) {
					listview.mListViewListener.onRefresh();
				}
				listview.resetHeaderHeight();
	    }
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
          new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(PetApplication.myUser==null)return;
				final ArrayList<Animal> temp=HttpUtil.myPetCard(handler,activity);
			
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null&&temp.size()>0){
							animals=new ArrayList<Animal>();
							for(int i=0;i<temp.size();i++){
								if(temp.get(i).master_id==PetApplication.myUser.userId){
									animals.add(temp.get(i));
								}
							}
							for(int i=0;i<temp.size();i++){
								if(!animals.contains(temp.get(i))){
									animals.add(temp.get(i));
								}
							}
							
							
							adapter.update(animals);
							animals=animals;
							adapter.notifyDataSetChanged();
						}
						
						listview.stopRefresh();
						
					}
				});
			}
		}).start();
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> temp=HttpUtil.myPetCard(handler,activity);
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null&&temp.size()>0){
							animals=new ArrayList<Animal>();
							for(int i=0;i<temp.size();i++){
								if(temp.get(i).master_id==PetApplication.myUser.userId){
									animals.add(temp.get(i));
								}
							}
							for(int i=0;i<temp.size();i++){
								if(!animals.contains(temp.get(i))){
									animals.add(temp.get(i));
								}
							}
							
							
							adapter.update(animals);
							animals=animals;
							adapter.notifyDataSetChanged();
						}
						
						listview.stopLoadMore();
						
					}
				});
			}
		}).start();
	}

}
