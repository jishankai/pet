package com.aidigame.hisun.imengstar.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.GridPictureAdapter;
import com.aidigame.hisun.imengstar.adapter.KingdomPeoplesAdapter;
import com.aidigame.hisun.imengstar.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.bean.PetNews;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
/**
 * 宠物的粉丝列表
 * @author admin
 *
 */
public class PetPicturesActivity extends BaseActivity implements IXListViewListener{
	
	ImageView backIv;
	XListView xListView;
	GridPictureAdapter adapter;
	LinearLayout containerLayout;
	   Handler handler;
	   public ArrayList<PetPicture> datas;
	   long last_id=-1;
	   Animal animal;
		public View popupParent;
		public RelativeLayout black_layout,root_layout;
	   public static PetPicturesActivity petPictureActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    petPictureActivity=this;
		setContentView(R.layout.activity_pet_pictures);
		animal=(Animal)getIntent().getSerializableExtra("animal");
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		backIv=(ImageView)findViewById(R.id.back);
		/*containerLayout=(LinearLayout)findViewById(R.id.container_layout);
		View view=LayoutInflater.from(this).inflate(R.layout.activity_pet_pictures2, null);
		containerLayout.addView(view);*/
		xListView=(XListView)/*view.*/findViewById(R.id.listview);
		
		
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		
		root_layout=(RelativeLayout)findViewById(R.id.root_layout);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		root_layout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.blur, options)));
		
		backIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isTaskRoot()){
					if(HomeActivity.homeActivity!=null){
						ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
						am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
					}else{
						Intent intent=new Intent(PetPicturesActivity.this,HomeActivity.class);
					    startActivity(intent);
					}
				}
				petPictureActivity=null;
				adapter.update(new ArrayList<PetPicture>());
				adapter.notifyDataSetChanged();
				
				
				finish();
				System.gc();
			}
		});
		
		
		
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
//		xListView.setAdapter(adapter);
		datas=new ArrayList<PetPicture>();
		adapter=new GridPictureAdapter(this, datas);
	
		xListView.setAdapter(adapter);
		loadData();
	}
	public void loadData(){
		last_id=-1;
		new Thread(new Runnable() {
			ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				UserImagesJson json1=null;
				json1=HttpUtil.downloadPetkingdomImages(null, last_id,0,PetPicturesActivity.this,animal.a_id);
				 
					final UserImagesJson json=json1;
					if(json!=null){
						if(json.petPictures!=null&&json.petPictures.size()>0){
							PetPicture data=null;
							if(datas.size()>0){
								if(datas.get(0).img_id==json.petPictures.get(0).img_id){
									return;
								}
							}
							for(int i=0;i<json.petPictures.size();i++){
								data=json.petPictures.get(i);
								data.animal=animal;
								if(temp.size()>=15)break;
								if(!temp.contains(data)){
									temp.add(data);
								}
							 }
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									datas=temp;
									//TODO
									adapter.update(datas);
									adapter.notifyDataSetChanged();
									
									LogUtil.i("scroll","datas大小========="+datas.size());
									
								}
							});
						}
					}else{
						//TODO 下载失败
					}
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
				final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
				UserImagesJson json1=null;
				json1=HttpUtil.downloadPetkingdomImages(null, last_id,0,PetPicturesActivity.this,animal.a_id);
			    
				final UserImagesJson json=json1;
				if(json!=null){
					if(json.petPictures!=null&&json.petPictures.size()>0){
						if(datas.size()>0){
							if(datas.get(0).img_id==json.petPictures.get(0).img_id){
								//下载获得的图片列表与现在界面显示的完全相同，则不进行界面更新
							}
						}
						/*for(int i=0;i<datas.size();i++){
							temp.add(datas.get(i));
						}*/
						PetPicture data=null;
						for(int i=0;i<json.petPictures.size();i++){
							if(temp.size()>=15)break;
							if(!temp.contains(json.petPictures.get(i))){
								data=json.petPictures.get(i);
								data.animal=animal;
								temp.add(i,data);
							}else{
								int index=temp.indexOf(json.petPictures.get(i));
								temp.remove(index);
								temp.add(index, json.petPictures.get(i));
							}
							
							
						}
						

						
					}
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//TODO
						if(temp.size()>0){
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
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
				if(datas.size()>0){
					last_id=datas.get(datas.size()-1).img_id;
				}else{
					last_id=-1;
				}
				/*for(int i=0;i<datas.size();i++){
					temp.add(datas.get(i));
				}*/
				UserImagesJson json1=null;
//			    if(Constants.user!=null&&data.master_id==Constants.user.userId){
			    	json1=HttpUtil.downloadPetkingdomImages(null, last_id,0,PetPicturesActivity.this,animal.a_id);
			    /*}else{
			    	json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
			    }*/
				final UserImagesJson json=json1;
				if(json!=null){
					if(json!=null&&json.petPictures.size()>0){
						PetPicture data=null;
						for(int i=0;i<json.petPictures.size();i++){
							if(i>=15)break;
							if(!temp.contains(json.petPictures.get(i))){
								data=json.petPictures.get(i);
								data.animal=animal;
								temp.add(data);
							}
						}
					}
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//TODO
						if(temp!=null&&temp.size()>0){
							for(int i=0;i<temp.size();i++){
								if(!datas.contains(temp.get(i))){
									datas.add(temp.get(i));
								}
							}
							adapter.update(datas);
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
	  		if(!UserStatusUtil.isLoginSuccess(PetPicturesActivity.this,popupParent,black_layout)){
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
