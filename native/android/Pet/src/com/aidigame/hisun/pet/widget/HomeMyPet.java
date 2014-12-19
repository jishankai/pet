package com.aidigame.hisun.pet.widget;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

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
	Activity activity;
	View view;
	XListView listview;
	public HomeMyPetAdapter adapter;
	ArrayList<Animal> animals;
	Handler handler;
	int last_id=-1;
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
	
		handler=HandleHttpConnectionException.getInstance().getHandler(activity);
		refresh();
	}
	public View getView(){
		return view;
	}
	public void refresh(){
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
								if(temp.get(i).master_id==Constants.user.userId){
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
						
						
						
					}
				});
			}
		}).start();
		
	}
	@Override
	public void onRefresh() {
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
								if(temp.get(i).master_id==Constants.user.userId){
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
								if(temp.get(i).master_id==Constants.user.userId){
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
