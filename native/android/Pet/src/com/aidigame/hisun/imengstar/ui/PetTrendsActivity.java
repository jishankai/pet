package com.aidigame.hisun.imengstar.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.imengstar.bean.Animal;
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
 * 宠物动态列表
 * @author admin
 *
 */
public class PetTrendsActivity extends BaseActivity implements IXListViewListener{
	
	ImageView backIv;
	XListView xListView;
	 ArrayList<PetNews> petNewsList;
	   KingdomTrendsListAdapter adapter;
	   Handler handler;
	   int nid=-1;
	   Animal animal;
		public View popupParent;
		public RelativeLayout black_layout;
	   public static PetTrendsActivity petTrendsActivity;
	   RelativeLayout rooLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    petTrendsActivity=this;
		setContentView(R.layout.activity_pet_trends);
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
						Intent intent=new Intent(PetTrendsActivity.this,HomeActivity.class);
					    startActivity(intent);
					}
				}
				petTrendsActivity=null;
				
				
				finish();
				System.gc();
			}
		});
		
		
		
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		nid=-1;
		petNewsList=new ArrayList<PetNews>();
		adapter=new KingdomTrendsListAdapter(petNewsList, this,animal);
		xListView.setAdapter(adapter);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetNews> temp=HttpUtil.kingdomTrends(PetTrendsActivity.this,nid, animal.a_id,handler);
			    handler.post(new Runnable() {
					
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
		
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		nid=-1;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetNews> temp=HttpUtil.kingdomTrends(PetTrendsActivity.this,nid, animal.a_id, handler);
			    handler.post(new Runnable() {
					
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
							adapter.update(petNewsList);
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
		if(petNewsList.size()==0){
			nid=-1;
		}else{
			nid=petNewsList.get(petNewsList.size()-1).nid;
		}

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetNews> temp=HttpUtil.kingdomTrends(PetTrendsActivity.this,nid, animal.a_id, handler);
			    handler.post(new Runnable() {
					
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
						}
						xListView.stopLoadMore();
					}
					
				});
			}
		}).start();
	}
	
	      public void clickItem2() {
	  		// TODO Auto-generated method stub
	  		if(!UserStatusUtil.isLoginSuccess(PetTrendsActivity.this,popupParent,black_layout)){
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
