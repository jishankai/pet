package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.GridGiftAdapter;
import com.aidigame.hisun.pet.adapter.GridPictureAdapter;
import com.aidigame.hisun.pet.adapter.KingdomPeoplesAdapter;
import com.aidigame.hisun.pet.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter2;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.PetNews;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.MyUser;
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
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 宠物的礼物列表
 * @author admin
 *
 */
public class PetGiftActivity extends Activity implements IXListViewListener{
	
	ImageView backIv;
	XListView xListView;
	GridGiftAdapter adapter;
	   Handler handler;
	   public ArrayList<Gift> datas;
	   int last_id=-1;
	   Animal animal;
		public View popupParent;
		public RelativeLayout black_layout;
	   public static PetGiftActivity petGiftActivity;
	   RelativeLayout rooLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		petGiftActivity=this;
		setContentView(R.layout.activity_pet_pictures);
		animal=(Animal)getIntent().getSerializableExtra("animal");
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		backIv=(ImageView)findViewById(R.id.back);
		xListView=(XListView)findViewById(R.id.listview);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		TextView title=(TextView)findViewById(R.id.title);
		title.setText("礼物");
		
		
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
						Intent intent=new Intent(PetGiftActivity.this,HomeActivity.class);
					    startActivity(intent);
					}
				}
				petGiftActivity=null;
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(PetGiftActivity.this)){
					PetApplication.petApp.activityList.remove(PetGiftActivity.this);
				}
				finish();
				System.gc();
			}
		});
		
		
		
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		xListView.setAdapter(adapter);
		datas=new ArrayList<Gift>();
		adapter=new GridGiftAdapter(this, datas);
	
		xListView.setAdapter(adapter);
		loadData();
	}
	public void loadData(){
		last_id=-1;
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Gift> temp=HttpUtil.kingdomGift(PetGiftActivity.this,animal, handler);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null){
							datas=temp;
							adapter.update(datas);
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
		last_id=-1;
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Gift> temp=HttpUtil.kingdomGift(PetGiftActivity.this,animal, handler);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null){
							datas=temp;
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
		if(datas.size()>0){
			last_id=datas.get(datas.size()-1).img_id;
		}
        
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
	  		if(!UserStatusUtil.isLoginSuccess(PetGiftActivity.this,popupParent,black_layout)){
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
