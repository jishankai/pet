package com.aidigame.hisun.imengstar.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.MarketGridViewAdapter;
import com.aidigame.hisun.imengstar.adapter.MarketRealGridViewAdapter;
import com.aidigame.hisun.imengstar.adapter.UserGiftGridViewAdapter;
import com.aidigame.hisun.imengstar.bean.Gift;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
/**
 * 我的物品栏
 * @author admin
 *
 */
public class MyItemActivity extends BaseActivity implements OnClickListener{
	public static MyItemActivity myItemActivity;
	    FrameLayout frameLayout;
		View viewTopWhite;
	    
	    ImageView back;
		GridView gridView;
		MarketGridViewAdapter adapter;
		HandleHttpConnectionException handleHttpConnectionException;
		ArrayList<Gift> giftList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_item);
		myItemActivity=this;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		
		
		
		
		
		giftList=new ArrayList<Gift>();
		back=(ImageView)findViewById(R.id.back);
		gridView=(GridView)findViewById(R.id.market_gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		giftList=new ArrayList<Gift>();
		setBlurImageBackground();
		adapter=new MarketGridViewAdapter(this,giftList);
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(PetApplication.myUser!=null){
					final ArrayList<Gift> temp=HttpUtil.userItems(MyItemActivity.this,PetApplication.myUser, -1, handleHttpConnectionException.getHandler(MyItemActivity.this));
	                handleHttpConnectionException.getHandler(MyItemActivity.this).post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(temp!=null){
								giftList=temp;
								adapter.updateList(giftList);
								adapter.notifyDataSetChanged();
//								setBlurImageBackground();
							}
						}
					});
				}
				
			}
		}).start();
		gridView.setAdapter(adapter);
	    back.setOnClickListener(this);
	    gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
				
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		
		
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		frameLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.blur, options)));
		
		
		viewTopWhite=(View)findViewById(R.id.top_white_view);
       
       
       /* gridView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(gridView.getFirstVisiblePosition()==0&&gridView.getChildAt(0).getTop()==0){
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
			});*/
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.back:
			if(isTaskRoot()){
				if(HomeActivity.homeActivity!=null){
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
				}else{
					Intent intent=new Intent(this,HomeActivity.class);
					this.startActivity(intent);
				}
				
			}
			myItemActivity=null;
			
			
			this.finish();
			System.gc();
			break;
		}
	}


}
