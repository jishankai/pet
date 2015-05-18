package com.aidigame.hisun.imengstar.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.BegPicturesAdapter;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/**
 * 宠物的求口粮图片列表
 * @author admin
 *
 */
public class BegPicturesActivity extends BaseActivity implements IXListViewListener{
	
	private ImageView backIv;
	private XListView xListView;
	private BegPicturesAdapter adapter;
	private   Handler handler;
	private  ArrayList<PetPicture> datas;
	private int last_id=0;
	private  Animal animal;
	private  View popupParent;
	private  RelativeLayout black_layout;
	   public static BegPicturesActivity begPicturesActivity;
	   private  RelativeLayout rooLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		begPicturesActivity=this;
		setContentView(R.layout.activity_pet_beg_pictures);
		animal=(Animal)getIntent().getSerializableExtra("animal");
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		backIv=(ImageView)findViewById(R.id.back);
		xListView=(XListView)findViewById(R.id.listview);
		xListView.setSelector(R.color.orange_red1);
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
						Intent intent=new Intent(BegPicturesActivity.this,HomeActivity.class);
					    startActivity(intent);
					}
				}
				begPicturesActivity=null;
				
				
				finish();
				System.gc();
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
	private  void loadData(){
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

	     
}
