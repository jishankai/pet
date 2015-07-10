package com.aidigame.hisun.imengstar.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.KingdomPeoplesAdapter;
import com.aidigame.hisun.imengstar.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.bean.PetNews;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
public class PetFansActivity extends BaseActivity implements IXListViewListener{
	
	ImageView backIv;
	XListView xListView;
	 KingdomPeoplesAdapter adapter;
	   Handler handler;
	   public ArrayList<MyUser> datas;
	   int last_id=-1;
	  int page=0;
	   Animal animal;
		public View popupParent;
		public RelativeLayout black_layout;
	   public static PetFansActivity petFansActivity;
	   RelativeLayout rooLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    petFansActivity=this;
		setContentView(R.layout.activity_pet_fans);
		animal=(Animal)getIntent().getSerializableExtra("animal");
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		backIv=(ImageView)findViewById(R.id.back);
		xListView=(XListView)findViewById(R.id.listview);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		
		rooLayout=(RelativeLayout)findViewById(R.id.root_layout);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		rooLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.blur, options)));
		
		
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
		page=0;
		new Thread(new Runnable() {
			ArrayList<MyUser> temp=new ArrayList<MyUser>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<MyUser> userList=HttpUtil.kingdomPeoples(PetFansActivity.this,page, animal, handler);
				
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
		page=0;
		new Thread(new Runnable() {
			ArrayList<MyUser> temp=new ArrayList<MyUser>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<MyUser> userList=HttpUtil.kingdomPeoples(PetFansActivity.this,page, animal, handler);
				
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
			page++;
		}
		
		new Thread(new Runnable() {
			ArrayList<MyUser> temp=new ArrayList<MyUser>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<MyUser> userList=HttpUtil.kingdomPeoples(PetFansActivity.this,page, animal, handler);
				
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
	 
	      public void clickItem2() {
	  		// TODO Auto-generated method stub
	  		if(!UserStatusUtil.isLoginSuccess(PetFansActivity.this,popupParent,black_layout)){
//	  		    setBlurImageBackground();
	  			return ;
	  		}
	  		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
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
