package com.aidigame.hisun.pet.widget;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListViewHeader;
import me.maxwin.view.XListView.IXListViewListener;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomePetPictureAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.widget.fragment.DiscoveryFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HomePetPictures implements IXListViewListener{
	private  Activity context;
	private  View view;
	private  XListView listView;
	private  HomePetPictureAdapter adapter;
	private  ArrayList<Animal> list;
	private  Handler handler;

	private  int page=0;
	public HomePetPictures(Activity context){
		this.context=context;
		handler=HandleHttpConnectionException.getInstance().getHandler(context);
		init();
	}
	public void init(){
		view=LayoutInflater.from(context).inflate(R.layout.widget_home_pet_pictures, null);
		listView=(XListView)view.findViewById(R.id.listview);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		list=new ArrayList<Animal>();
		adapter=new HomePetPictureAdapter(context, list);
		listView.setAdapter(adapter);
		
		handler.postAtTime(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pullRefresh();
			}
		}, 1000);
		
		new Thread(new Runnable() {
        	ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int usr_id=0;
				if(PetApplication.isSuccess&&PetApplication.myUser!=null){
					usr_id=PetApplication.myUser.userId;
				}
				final ArrayList<Animal> animals=HttpUtil.petRecommend(handler, context, 0,usr_id);
				
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(animals!=null){
									list=animals;
									for(int i=list.size()-1;i>=6;i--){
										list.remove(i);
									}
									adapter.updateTopics(list);
									adapter.notifyDataSetChanged();
								}
								
								
								LogUtil.i("scroll","datas大小========="+temp.size());
								
							}
						});
			}
		})/*.start()*/;
	}
	public View getView(){
		ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		/*params.height=Constants.screen_height;
		params.width=Constants.screen_width;*/
		view.setLayoutParams(params);
		/*
		 * 禁止此View的硬件加速，GallaryFlow在硬件加速情况下效果混乱
		 */
//		view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		return view;
	}
	public void refresh(){
		page=0;
		adapter.updateTopics(new ArrayList<Animal>());
		adapter.notifyDataSetChanged();
		
		
		new Thread(new Runnable() {
        	ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int usr_id=0;
				if(PetApplication.isSuccess&&PetApplication.myUser!=null){
					usr_id=PetApplication.myUser.userId;
				}
				final ArrayList<Animal> animals=HttpUtil.petRecommend(handler, context, 0,usr_id);
				
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(animals!=null){
									list=animals;
									for(int i=list.size()-1;i>=6;i--){
										list.remove(i);
									}
									adapter.updateTopics(list);
									adapter.notifyDataSetChanged();
								}
								 DiscoveryFragment.isRefresh=false;
							}
						});
			}
		}).start();
	}
	
	  public void pullRefresh(){
		  page=0;
			adapter.updateTopics(new ArrayList<Animal>());
			adapter.notifyDataSetChanged();
	    	listView.updateHeaderHeight(listView.mHeaderViewHeight);
	    	listView.mHeaderView.setVisibility(View.VISIBLE);
	    	listView.mPullRefreshing = true;
	    	listView.mEnablePullRefresh=true;
	    	listView.mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
				if (listView.mListViewListener != null) {
					listView.mListViewListener.onRefresh();
				}
				listView.resetHeaderHeight();
	    }
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
        	ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int usr_id=0;
				page=0;
				if(PetApplication.isSuccess&&PetApplication.myUser!=null){
					usr_id=PetApplication.myUser.userId;
				}
				final ArrayList<Animal> animals=HttpUtil.petRecommend(handler, context, 0,usr_id);
				
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(animals!=null){
									list=animals;
									for(int i=list.size()-1;i>=6;i--){
										list.remove(i);
									}
									adapter.updateTopics(list);
									adapter.notifyDataSetChanged();
								}
								listView.stopRefresh();
								 DiscoveryFragment.isRefresh=false;
							}
						});
			}
		}).start();
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
        	ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				page++;
				int usr_id=0;
				if(PetApplication.isSuccess&&PetApplication.myUser!=null){
					usr_id=PetApplication.myUser.userId;
				}
				final ArrayList<Animal> animals=HttpUtil.petRecommend(handler, context, page,usr_id);
				if(animals!=null){
					if(list!=null){
						for(int i=animals.size()-1;i>=6;i--){
							animals.remove(i);
						}
						for(int i=0;i<list.size();i++){
							animals.add(i, list.get(i));
						}
					}else{
						
					}
				}
				
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(animals!=null&&animals.size()>0){
							list=animals;
							adapter.updateTopics(list);
							adapter.notifyDataSetChanged();
						}
						listView.stopLoadMore();;
						
						LogUtil.i("scroll","datas大小========="+temp.size());
						
					}
				});
			}
		}).start();
		
	}

}
