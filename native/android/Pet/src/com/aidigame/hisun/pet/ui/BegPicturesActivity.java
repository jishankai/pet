package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.BegPicturesAdapter;
import com.aidigame.hisun.pet.adapter.GridPictureAdapter;
import com.aidigame.hisun.pet.adapter.KingdomPeoplesAdapter;
import com.aidigame.hisun.pet.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter2;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetNews;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
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
 * 宠物的求口粮图片列表
 * @author admin
 *
 */
public class BegPicturesActivity extends Activity implements IXListViewListener{
	
	ImageView backIv;
	XListView xListView;
	BegPicturesAdapter adapter;
	   Handler handler;
	   public ArrayList<PetPicture> datas;
	   int last_id=0;
	   Animal animal;
		public View popupParent;
		public RelativeLayout black_layout;
	   public static BegPicturesActivity begPicturesActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		begPicturesActivity=this;
		setContentView(R.layout.activity_pet_beg_pictures);
		animal=(Animal)getIntent().getSerializableExtra("animal");
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		backIv=(ImageView)findViewById(R.id.back);
		xListView=(XListView)findViewById(R.id.listview);
		xListView.setSelector(R.color.orange_red1);
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
						Intent intent=new Intent(BegPicturesActivity.this,HomeActivity.class);
					    startActivity(intent);
					}
				}
				finish();
			}
		});
		
		
		
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		xListView.setAdapter(adapter);
		datas=new ArrayList<PetPicture>();
		adapter=new BegPicturesAdapter(this, datas);
	
		xListView.setAdapter(adapter);
		loadData();
	}
	public void loadData(){
		last_id=0;
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetPicture> pps=HttpUtil.petBegPicturesList(handler,animal, 0, BegPicturesActivity.this);
				
	          	if(pps!=null){
	          		/*for(int i=0;i<pps.size();i++){
	          			if((pps.get(i).create_time*1000+24*3600*1000)<System.currentTimeMillis()){
	          				pps.remove(i);
	          				i--;
	          			}
	          		}*/
	          		
	          		runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							datas=pps;
							adapter.update(datas);
							adapter.notifyDataSetChanged();
						}
					});
	          	}
			}
		}).start();
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		last_id=0;
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetPicture> pps=HttpUtil.petBegPicturesList(handler,animal, 0, BegPicturesActivity.this);
				
	          	
	          		/*for(int i=0;i<pps.size();i++){
	          			if((pps.get(i).create_time*1000+24*3600*1000)<System.currentTimeMillis()){
	          				pps.remove(i);
	          				i--;
	          			}
	          		}*/
	          		
	          		runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(pps!=null){
								datas=pps;
								adapter.update(datas);
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
		last_id++;
        new Thread(new Runnable() {
			
			@Override
			public void run() {
final ArrayList<PetPicture> pps=HttpUtil.petBegPicturesList(handler,animal, last_id, BegPicturesActivity.this);
				
	          		
	          		runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(pps!=null){
				          		for(int i=0;i<pps.size();i++){
				          			if(!datas.contains(pps.get(i))){
				          				datas.add(pps.get(i));
				          			}
				          		}
				          	}
							
							adapter.update(datas);
							adapter.notifyDataSetChanged();
							xListView.stopLoadMore();;
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
	  		if(!UserStatusUtil.isLoginSuccess(BegPicturesActivity.this,popupParent,black_layout)){
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
