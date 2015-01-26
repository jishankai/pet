package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.KingdomPeoplesAdapter;
import com.aidigame.hisun.pet.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetNews;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/**
 * 宠物的粉丝列表
 * @author admin
 *
 */
public class PetFansActivity extends Activity implements IXListViewListener{
	
	ImageView backIv;
	XListView xListView;
	 KingdomPeoplesAdapter adapter;
	   Handler handler;
	   public ArrayList<MyUser> datas;
	   int last_id=-1;
	   Animal animal;
		public View popupParent;
		public RelativeLayout black_layout;
	   public static PetFansActivity petFansActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
	    petFansActivity=this;
		setContentView(R.layout.activity_pet_fans);
		animal=(Animal)getIntent().getSerializableExtra("animal");
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		backIv=(ImageView)findViewById(R.id.back);
		xListView=(XListView)findViewById(R.id.listview);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		
		backIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isTaskRoot()){
					if(HomeActivity.homeActivity!=null){
						ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
						am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
					}else{
						Intent intent=new Intent(PetFansActivity.this,HomeActivity.class);
					    startActivity(intent);
					}
				}
				petFansActivity=null;
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(PetFansActivity.this)){
					PetApplication.petApp.activityList.remove(PetFansActivity.this);
				}
				finish();
				System.gc();
			}
		});
		
		
		
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		xListView.setAdapter(adapter);
		datas=new ArrayList<MyUser>();
		adapter=new KingdomPeoplesAdapter(this, datas);
	
		xListView.setAdapter(adapter);
		loadData();
	}
	public void loadData(){
		last_id=-1;
		new Thread(new Runnable() {
			ArrayList<MyUser> temp=new ArrayList<MyUser>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<MyUser> userList=HttpUtil.kingdomPeoples(PetFansActivity.this,last_id, animal, handler);
				
				if(userList!=null){
					if(userList.size()>0){
						MyUser data=null;
						for(int i=0;i<userList.size();i++){
							data=userList.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
						runOnUiThread(new Runnable() {
							public void run() {
								datas=temp;
								adapter.updateList(datas);
								adapter.notifyDataSetChanged();
								
							}
						});
					}
				}
			}
				
			
		}).start();
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		last_id=-1;
		new Thread(new Runnable() {
			ArrayList<MyUser> temp=new ArrayList<MyUser>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<MyUser> userList=HttpUtil.kingdomPeoples(PetFansActivity.this,last_id, animal, handler);
				
				if(userList!=null){
					if(userList.size()>0){
						MyUser data=null;
						for(int i=0;i<userList.size();i++){
							data=userList.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						if(temp.size()>0){
							datas=temp;
							adapter.updateList(datas);
							adapter.notifyDataSetChanged();
						}
						xListView.stopRefresh();
						
					}
				});
			}
				
			
		}).start();
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if(datas.size()>0){
			last_id=datas.get(datas.size()-1).userId;
		}
		
		new Thread(new Runnable() {
			ArrayList<MyUser> temp=new ArrayList<MyUser>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<MyUser> userList=HttpUtil.kingdomPeoples(PetFansActivity.this,last_id, animal, handler);
				
				if(userList!=null){
					if(userList.size()>0){
						MyUser data=null;
						for(int i=0;i<datas.size();i++){
							if(!temp.contains(datas.get(i))){
								temp.add(datas.get(i));
							}
						}
						for(int i=0;i<userList.size();i++){
							data=userList.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
						
					}
				}
				runOnUiThread(new Runnable() {
					public void run() {
						if(temp!=null&&temp.size()>0){
							adapter.updateList(temp);
							int start=datas.size();
							datas=temp;
							adapter.updateList(datas);
							adapter.notifyDataSetChanged();
							
						}
						xListView.stopLoadMore();
						
					}
				});
			}
				
			
		}).start();
	}
	   @Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }
	      public void clickItem2() {
	  		// TODO Auto-generated method stub
	  		if(!UserStatusUtil.isLoginSuccess(PetFansActivity.this,popupParent,black_layout)){
//	  		    setBlurImageBackground();
	  			return ;
	  		}
	  		if(Constants.user!=null&&Constants.user.aniList!=null){
	  			/*DialogGiveSbGift dgb=new DialogGiveSbGift(this,data);
	  			final AlertDialog dialog=new AlertDialog.Builder(this).setView(dgb.getView())
	  					.show();*/
	  			if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.finish();
	  			Intent intent=new Intent(this,DialogGiveSbGiftActivity1.class);
	  			intent.putExtra("animal", animal);
	  			this.startActivity(intent);
	  			DialogGiveSbGiftActivity1 dgb=DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity;
	  			DialogGiveSbGiftActivity1.dialogGoListener=new DialogGiveSbGiftActivity1.DialogGoListener() {
	  				
	  				@Override
	  				public void toDo() {
	  					// TODO Auto-generated method stub
	  Intent intent=intent=new Intent(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity,MarketActivity.class);
	  					
	  					DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.startActivity(intent);
	  				}
	  				
	  				@Override
	  				public void closeDialog() {
	  					// TODO Auto-generated method stub
	  					if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null)
	  					DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.finish();
	  					
	  				}
	  				@Override
	  				public void lastResult(boolean isSuccess) {
	  					// TODO Auto-generated method stub
	  					if(isSuccess){
	  						onRefresh();
	  					}
	  				}
	  				@Override
	  				public void unRegister() {
	  					// TODO Auto-generated method stub
	  					
	  				}
	  			};
	  		}
	  		
	  	}
}
