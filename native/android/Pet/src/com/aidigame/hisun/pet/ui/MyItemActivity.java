package com.aidigame.hisun.pet.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.MarketGridViewAdapter;
import com.aidigame.hisun.pet.adapter.MarketRealGridViewAdapter;
import com.aidigame.hisun.pet.adapter.UserGiftGridViewAdapter;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
/**
 * 我的物品栏
 * @author admin
 *
 */
public class MyItemActivity extends Activity implements OnClickListener{
	
	    FrameLayout frameLayout;
		View viewTopWhite;
	    
	    ImageView back;
		GridView gridView;
		UserGiftGridViewAdapter adapter;
		HandleHttpConnectionException handleHttpConnectionException;
		ArrayList<Gift> giftList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_my_item);
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
		adapter=new UserGiftGridViewAdapter(this,giftList);
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(Constants.user!=null){
					final ArrayList<Gift> temp=HttpUtil.userItems(MyItemActivity.this,Constants.user, -1, handleHttpConnectionException.getHandler(MyItemActivity.this));
	                handleHttpConnectionException.getHandler(MyItemActivity.this).post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(temp!=null){
								giftList=temp;
								adapter.update(giftList);
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
		viewTopWhite=(View)findViewById(R.id.top_white_view);
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(HomeFragment.blurBitmap==null){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				MyItemActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						frameLayout.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
						frameLayout.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
       
        gridView.setOnScrollListener(new OnScrollListener() {
				
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
			});
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}

}
