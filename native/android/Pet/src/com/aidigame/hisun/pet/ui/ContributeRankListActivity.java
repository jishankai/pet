package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ContributeRankListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
/**
 * 王国贡献榜
 * @author admin
 *
 */
public class ContributeRankListActivity extends Activity {
	FrameLayout frameLayout;
	View viewTopWhite;
	public View popupParent;
	public RelativeLayout black_layout;
	
	public ListView listView/*,listView2*/;
	TextView findMeTV,contributeTv;
	ContributeRankListAdapter adapter;
//	ContributeRankListAdapter adapter2;
	public ArrayList<MyUser> peopleList;
	public ArrayList<MyUser> tempList;
	PopupWindow popupWindow;
	public boolean findMe=true;
	public int category=1;
	HandleHttpConnectionException handleHttpConnectionException;
	Animal animal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		
		setContentView(R.layout.activity_contribute_rank);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		animal=(Animal)getIntent().getSerializableExtra("animal");
		loadData(category);
		initView();
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		viewTopWhite=(View)findViewById(R.id.top_white_view);
       
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ContributeRankListActivity.this,UserCardActivity.class);
				intent.putExtra("user", peopleList.get(position));
				
				if(UserCardActivity.userCardActivity!=null){
					if(PetApplication.petApp.activityList.contains(UserCardActivity.userCardActivity)){
						PetApplication.petApp.activityList.remove(UserCardActivity.userCardActivity);
					}
					
					UserCardActivity.userCardActivity=null;
					UserCardActivity.userCardActivity.finish();
					System.gc();
				}
				ContributeRankListActivity.this.startActivity(intent);
			}
		});
		/* listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(scrollState!=SCROLL_STATE_IDLE)return;
					if(isFindMeScroll){
						viewTopWhite.setVisibility(View.VISIBLE);
						return;
					}
					if(adapter.currentArrowImageView!=null){
						adapter.currentArrowImageView.setVisibility(View.GONE);
						adapter.currentArrowImageView=null;
//						listView2.setVisibility(View.GONE);
						peopleList.get(adapter.index).showArrow=false;
						
					}
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
			});*/
	}

	private void loadData( final int category) {
		// TODO Auto-generated method stub
		this.category=category;
		peopleList=new ArrayList<MyUser>();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<MyUser> users=HttpUtil.contributeRankList(ContributeRankListActivity.this,category, animal.a_id, handleHttpConnectionException.getHandler(ContributeRankListActivity.this));
				if(users!=null){
					runOnUiThread(new Runnable() {
						public void run() {
							peopleList=users;
							adapter.updateData(users);
							adapter.notifyDataSetChanged();
							
							findTempList(0);
						}
					});
				}
				
			}
		}).start();
		tempList=new ArrayList<MyUser>();
		/*for(int i=0;i<4;i++){
			tempList.add(peopleList.get(i));
		}*/
//		findTempList(0);
		
	}
	public void findTempList(int start){
		return;
		/*tempList=new ArrayList<User>();
		for(int i=start;i<peopleList.size();i++){
			if(peopleList.get(i).userId==Constants.user.userId){
				position=i;
				findMe=true;
				break;
			}
		}
		if(position<7){
			findMe=true;
			adapter.updateData(peopleList);
			adapter.notifyDataSetChanged();
			return;
		}else{
			for(int i=start;i<peopleList.size()&&(i<start+4);i++){
				tempList.add(peopleList.get(i));
			}
			if(position+1>=peopleList.size()){
				tempList.add(peopleList.get(position-2));
				tempList.add(peopleList.get(position-1));
				tempList.add(peopleList.get(position));
			}else{
				tempList.add(peopleList.get(position-1));
				tempList.add(peopleList.get(position));
				tempList.add(peopleList.get(position+1));
			}
			
			
		}
		adapter.updateData(tempList);
		adapter.notifyDataSetChanged();*/
	}
	int length,itemtH,itemNum;
    public int position;
	private void initView() {
		// TODO Auto-generated method stub
		listView=(ListView)findViewById(R.id.listview);
		findMeTV=(TextView)findViewById(R.id.textView3);
		contributeTv=(TextView)findViewById(R.id.textView2);
		adapter=new ContributeRankListAdapter(this,peopleList,1);
		listView.setAdapter(adapter);
//		listView2=(ListView)findViewById(R.id.listview2);
//		adapter2=new ContributeRankListAdapter(this,tempList,2);
//		listView2.setAdapter(adapter2);
		
		setBlurImageBackground();
		
		
		findMeTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(UserStatusUtil.isLoginSuccess(ContributeRankListActivity.this, popupParent, black_layout))
				showListArrow();
			}
		});
		contributeTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow();
			}
		});
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContributeRankListActivity.this.finish();
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(ContributeRankListActivity.this)){
					PetApplication.petApp.activityList.remove(ContributeRankListActivity.this);
				}
				System.gc();
			}
		});
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				while(listView2.getTop()==0){
//					try {
//						Thread.sleep(50);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				length=listView2.getTop()-listView.getTop();
//				itemtH=listView.getChildAt(0).getMeasuredHeight();
//				itemNum=length/itemtH-2;
//				peopleList.get(itemNum).showArrow=true;
//				runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						adapter.notifyDataSetChanged();
//					}
//				});
//				while(listView2.getTop()==0||adapter.arrowH==0){
//					try {
//						Thread.sleep(50);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				final int delta=length-adapter.layoutH-itemtH*(itemNum);
//				runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						/*
//						 * listview2的三个条目全部显示在界面上
//						 */
//						RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)listView2.getLayoutParams();
//						params.height=listView2.getChildAt(0).getMeasuredHeight()*2+listView2.getChildAt(1).getMeasuredHeight()+delta-15;
////						listView2.layout(0, listView2.getBottom()-(listView2.getChildAt(0).getMeasuredHeight()/**2+listView2.getChildAt(1).getMeasuredHeight()*/), listView2.getWidth(), listView2.getBottom());
//						listView2.setLayoutParams(params);
//					}
//				});
//				
//			}
//		}).start();
	}
//	String[] strs={"贡献日榜","贡献周榜","贡献月榜","总贡献榜"};
	private void showPopupWindow() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(this).inflate(R.layout.popup_contribute_1, null);
	    TextView tv1=(TextView)view.findViewById(R.id.textView1);
	    TextView tv2=(TextView)view.findViewById(R.id.textView2);
	    TextView tv3=(TextView)view.findViewById(R.id.textView3);
	    TextView tv4=(TextView)view.findViewById(R.id.textView4);
		popupWindow=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		LinearLayout parent=(LinearLayout)view.findViewById(R.id.parent);
		parent.setBackgroundResource(R.drawable.spinner_rank);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAsDropDown(contributeTv, -5, 5);//-contributeTv.getHeight()
//		contributeTv.setVisibility(View.INVISIBLE);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				contributeTv.setVisibility(View.VISIBLE);
			}
		});
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				contributeTv.setVisibility(View.VISIBLE);
				contributeTv.setText("昨日贡献");
				loadData(1);
			}
		});
        tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				contributeTv.setVisibility(View.VISIBLE);
				contributeTv.setText("上周贡献");
				loadData(2);
			}
		});
        tv3.setOnClickListener(new OnClickListener() {
	
	        @Override
	        public void onClick(View v) {
		    // TODO Auto-generated method stub
	        	popupWindow.dismiss();
	        	contributeTv.setVisibility(View.VISIBLE);
	        	contributeTv.setText("上月贡献");
	        	loadData(3);
	        }
        });
        tv4.setOnClickListener(new OnClickListener() {
        	
	        @Override
	        public void onClick(View v) {
		    // TODO Auto-generated method stub
	        	popupWindow.dismiss();
	        	contributeTv.setVisibility(View.VISIBLE);
	        	contributeTv.setText("总贡献榜");
	        	loadData(0);
	        }
        });
	}
    boolean isFindMeScroll=false;
	private void showListArrow() {
		// TODO Auto-generated method stub
		int index=peopleList.indexOf(PetApplication.myUser);
		if(index>=0&&index<peopleList.size()){
//			viewTopWhite.setVisibility(View.VISIBLE);
//			listView.setSelection(index);
			listView.smoothScrollToPosition(index);
//			isFindMeScroll=true;
		}
		
	}

	public static class People{
		public String name;
		public int contributeNum;
		public int rankNo;
		public int userId;
		
		public boolean showArrow=false;
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
