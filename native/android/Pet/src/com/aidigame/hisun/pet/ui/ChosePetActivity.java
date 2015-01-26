package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ChosePetListAdapter;
import com.aidigame.hisun.pet.adapter.UsersListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
/**
 * 设置默认宠物
 * @author admin
 *
 */
public class ChosePetActivity extends Activity {
	FrameLayout frameLayout;
	View viewTopWhite;
	
	
	ChosePetListAdapter adapter;
	ArrayList<Animal> list;
	ListView  listView;
	String likerString;
	HandleHttpConnectionException handleHttpConnectionException;
	ImageView back;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_chose_pet);
		listView=(ListView)findViewById(R.id.users_list_listview);
		back=(ImageView)findViewById(R.id.imageView1);
		likerString=getIntent().getStringExtra("likers");
		setBlurImageBackground();
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		list=new ArrayList<Animal>();
		if(Constants.user!=null&&Constants.user.aniList!=null){
			list=Constants.user.aniList;
			loadData();
		}
		adapter=new ChosePetListAdapter(this, list,null);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(NewPetKingdomActivity.petKingdomActivity!=null){
					if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
						NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
						NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
					}
					NewPetKingdomActivity.petKingdomActivity.finish();
					NewPetKingdomActivity.petKingdomActivity=null;
				}
				Intent intent=new Intent(ChosePetActivity.this,NewPetKingdomActivity.class);
				intent.putExtra("animal", list.get(position));
				ChosePetActivity.this.startActivity(intent);
				
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isTaskRoot()){
					if(HomeActivity.homeActivity!=null){
						ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
						am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
					}else{
						Intent intent=new Intent(ChosePetActivity.this,HomeActivity.class);
						ChosePetActivity.this.startActivity(intent);
					}
				}
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(ChosePetActivity.this)){
					PetApplication.petApp.activityList.remove(ChosePetActivity.this);
				}
				finish();
				System.gc();
			}
		});
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.getOthersList(likerString, handler,ChosePetActivity.this);
			}
		}).start();*/
	}
	private void loadData() {
		// TODO Auto-generated method stub
       new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> temp=HttpUtil.usersKingdom(ChosePetActivity.this,Constants.user, 1, handleHttpConnectionException.getHandler(ChosePetActivity.this));
				if(temp!=null&&temp.size()>0){
	               runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.updateList(temp);
						adapter.notifyDataSetChanged();
						list=temp;
						Constants.user.aniList=temp;
					}
				});
				}
			}
		}).start();
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		viewTopWhite=(View)findViewById(R.id.top_white_view);
		
		 listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0).getTop()==0){
						viewTopWhite.setVisibility(View.VISIBLE);
					}else{
						if(viewTopWhite.getVisibility()!=View.GONE){
							viewTopWhite.setVisibility(View.GONE);
						}
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
				}
			});
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

	

}
