package com.aidigame.hisun.pet.ui;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.FAQListAdapter1;
import com.aidigame.hisun.pet.adapter.FAQListAdapter2;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
/**
 * 常见问题界面
 * @author admin
 *
 */
public class FAQActivity extends Activity {
	 FrameLayout frameLayout;
	 View viewTopWhite;
	 
	 ListView listView1,listView2;
	 FAQListAdapter1 adapter1;
	 FAQListAdapter2 adapter2;
	 TextView commonTv,sendAdviceTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_faq);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		listView1=(ListView)findViewById(R.id.listview1);
		listView2=(ListView)findViewById(R.id.listview2);
		commonTv=(TextView)findViewById(R.id.common_tv);
		sendAdviceTv=(TextView)findViewById(R.id.textView1);
		sendAdviceTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(FAQActivity.this,AdviceActivity.class);
				FAQActivity.this.startActivity(intent);
				FAQActivity.this.finish();
			}
		});
		adapter1=new FAQListAdapter1(getResources().getStringArray(R.array.faq_list1), this);
		listView1.setAdapter(adapter1);
		adapter2=new FAQListAdapter2(getResources().getStringArray(R.array.faq_item_1_q), getResources().getStringArray(R.array.faq_item_1_q), this);
		listView2.setAdapter(adapter2);
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listView1.getVisibility()==View.GONE){
					listView1.setVisibility(View.VISIBLE);
					commonTv.setVisibility(View.GONE);
				}else{
					if(isTaskRoot()){
						if(HomeActivity.homeActivity!=null){
							ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
							am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
						}else{
							Intent intent=new Intent(FAQActivity.this,HomeActivity.class);
							FAQActivity.this.startActivity(intent);
						}
					}
					FAQActivity.this.finish();
				}
			}
		});
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				listView1.setVisibility(View.GONE);
				commonTv.setVisibility(View.GONE);
				switch (position) {
				case 0:
					adapter2.update(getResources().getStringArray(R.array.faq_item_1_q), getResources().getStringArray(R.array.faq_item_1_a));
					adapter2.notifyDataSetChanged();
					break;
				case 1:
					adapter2.update(getResources().getStringArray(R.array.faq_item_2_q), getResources().getStringArray(R.array.faq_item_2_a));
					adapter2.notifyDataSetChanged();
					break;
				case 2:
					adapter2.update(getResources().getStringArray(R.array.faq_item_3_q), getResources().getStringArray(R.array.faq_item_3_a));
					adapter2.notifyDataSetChanged();
					break;
				case 3:
					adapter2.update(getResources().getStringArray(R.array.faq_item_4_q), getResources().getStringArray(R.array.faq_item_4_a));
					adapter2.notifyDataSetChanged();
					break;
				case 4:
					adapter2.update(getResources().getStringArray(R.array.faq_item_5_q), getResources().getStringArray(R.array.faq_item_5_a));
					adapter2.notifyDataSetChanged();
					break;
				case 5:
					adapter2.update(getResources().getStringArray(R.array.faq_item_6_q), getResources().getStringArray(R.array.faq_item_6_a));
					adapter2.notifyDataSetChanged();
					break;
				case 6:
					adapter2.update(getResources().getStringArray(R.array.faq_item_7_q), getResources().getStringArray(R.array.faq_item_7_a));
					adapter2.notifyDataSetChanged();
					break;
			}
			}
		});
		
		setBlurImageBackground();
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		viewTopWhite=(View)findViewById(R.id.top_white_view);
		
        listView1.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(listView1.getFirstVisiblePosition()==0&&listView1.getChildAt(0).getTop()==0){
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
        listView2.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(listView2.getFirstVisiblePosition()==0&&listView2.getChildAt(0).getTop()==0){
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
