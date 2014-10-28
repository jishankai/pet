package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.PopularRankListAdapter;
import com.aidigame.hisun.pet.adapter.PopularWindowAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
/**
 * 人气榜
 * @author admin
 *
 */
public class PopularRankListActivity extends Activity {
	FrameLayout frameLayout;
	View viewTopWhite,popup_parent;
	
	RelativeLayout black_layout;
	
	public ListView listView/*,listView2*/;
	TextView findMeTV,popularTV,starTV,raceTV;
	PopularRankListAdapter adapter;
//	PopularRankListAdapter adapter2;
	public ArrayList<Animal> peopleList;
//	public ArrayList<Animal> tempList;
	ArrayList<Animal> myList;
	PopupWindow popularWindow,raceWindow;
	public boolean isAllData=false;//当前界面是否为找我模式
	public int myListIndex=-1;
	 public int position;
	 int category=0;
	 int currentType=100;
	HandleHttpConnectionException handleHttpConnectionException;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_popular_rank);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		loadData(0);
		initView();
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
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						frameLayout.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
						frameLayout.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PopularRankListActivity.this,PetKingdomActivity.class);
				intent.putExtra("animal", peopleList.get(position));
				if(PetKingdomActivity.petKingdomActivity!=null){
					PetKingdomActivity.petKingdomActivity.finish();
				}
				PopularRankListActivity.this.startActivity(intent);
			}
		});
		 listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
				/*	if(adapter.currentArrowImageView!=null){
						adapter.currentArrowImageView.setVisibility(View.GONE);
						adapter.currentArrowImageView=null;
//						listView2.setVisibility(View.GONE);
						peopleList.get(adapter.index).showArrow=false;
						
						
					}*/
					if(scrollState!=SCROLL_STATE_IDLE)return;
					if(isFindMeScroll){
						viewTopWhite.setVisibility(View.VISIBLE);
						isFindMeScroll=false;
						return;
					}
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0).getTop()==0){
						viewTopWhite.setVisibility(View.VISIBLE);
					}else{
						if(viewTopWhite.getVisibility()!=View.GONE){
							viewTopWhite.setVisibility(View.GONE);
							
							/*if(isAllData){
								isAllData=false;
								myListIndex=0;
								adapter.updateData(peopleList);
								if(android.os.Build.VERSION.SDK_INT>8){
									listView.smoothScrollToPosition(position);
								}else{
									listView.setSelection(position);
								}
								adapter.notifyDataSetChanged();
							}*/
							
						}
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					/*if(findMe){
						adapter.updateData(peopleList);
						adapter.notifyDataSetInvalidated();
						findMe=false;
					}*/
				}
			});
		/* listView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(isAllData){
					isAllData=false;
					adapter.updateData(peopleList);
					if(android.os.Build.VERSION.SDK_INT>8){
						listView.smoothScrollToPosition(position);
					}else{
						listView.setSelection(position);;
					}
					
					adapter.notifyDataSetChanged();
				}
				return false;
			}
		});*/
	}
/**
 * 
 * @param category  0,总人气；1，日人气；2，周人气；3月人气。
 */
	private void loadData(final int category) {
		// TODO Auto-generated method stub
		peopleList=new ArrayList<Animal>();
		myList=new ArrayList<Animal>();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				long animal_id=-1;
				if(Constants.user!=null){
					animal_id=Constants.user.currentAnimal.a_id;
				}else{
					animal_id=-1;
				}
				final HashMap<String, ArrayList<Animal>> map=HttpUtil.rqRankApi(category, animal_id, handleHttpConnectionException.getHandler(PopularRankListActivity.this),PopularRankListActivity.this);
				if(map!=null){
					if(category!=0){
						getDataByType(currentType);
						return;
					}
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(map.get("total")!=null){
								peopleList=map.get("total");
								myList=map.get("my");
								/**
								 * 用户未注册
								 */
								if(peopleList!=null){
									adapter.updateData(map.get("total"));
									adapter.notifyDataSetChanged();
									isAllData=true;
									if(Constants.isSuccess&&myList!=null&&myList.size()>0){
										myListIndex=0;
									}else{
										myListIndex=-1;
									}
								}
						}
						}
					});
				}
			}
		}).start();
		
	}
	int count=0;
	int length,itemtH,itemNum;
  
	private void initView() {
		// TODO Auto-generated method stub
		listView=(ListView)findViewById(R.id.listview);
		findMeTV=(TextView)findViewById(R.id.textView3);
		popularTV=(TextView)findViewById(R.id.textView2);
		if(Constants.planet==2){
			popularTV.setText("汪星人气总榜");
		}else{
			popularTV.setText("喵星人气总榜");
		}
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popup_parent=findViewById(R.id.popup_parent);
		starTV=(TextView)findViewById(R.id.textView1);
		raceTV=(TextView)findViewById(R.id.chose_race_tv);
		adapter=new PopularRankListAdapter(this,peopleList,1);
		listView.setAdapter(adapter);
		setBlurImageBackground();
		findMeTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				findMe();
			}
		});
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopularRankListActivity.this.finish();
			}
		});
		popularTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopularWindow();
			}
		});
        raceTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRaceWindow();
			}

			
		});
        findViewById(R.id.relativelayout).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}
	boolean isFindMeScroll=false;
	private void findMe() {
		// TODO Auto-generated method stub
		
		if(myListIndex<0){
			/*
			 * 弹注册框
			 */
			if(!UserStatusUtil.isLoginSuccess(this,popup_parent,black_layout))return;
		
		}
		myListIndex++;
		if(myListIndex>=myList.size()){
			myListIndex=0;
		}
		if(myList.size()>0){
			int target=0;
			target=peopleList.indexOf(myList.get(myListIndex));
			if(target<peopleList.size()){
				
				if(android.os.Build.VERSION.SDK_INT>8){
					listView.smoothScrollToPosition(target);
				}else{
					listView.setSelection(target);
				}
				isFindMeScroll=true;
				viewTopWhite.setVisibility(View.VISIBLE);
			}else{
				/*
				 * 加载数据1000之后的
				 */
			}
		}
		
			
	}
	private void showPopularWindow() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(this).inflate(R.layout.popup_contribute_1, null);
	    TextView tv1=(TextView)view.findViewById(R.id.textView1);
	    TextView tv2=(TextView)view.findViewById(R.id.textView2);
	    TextView tv3=(TextView)view.findViewById(R.id.textView3);
	    TextView tv4=(TextView)view.findViewById(R.id.textView4);
	    tv1.setText("喵星人气日榜");
	    tv2.setText("喵星人气周榜");
	    tv3.setText("喵星人气月榜");
	    tv4.setText("喵星人气总榜");
	    popularWindow=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    popularWindow.setFocusable(true);
	    popularWindow.setBackgroundDrawable(new BitmapDrawable());
	    popularWindow.setOutsideTouchable(true);
	    popularWindow.showAsDropDown(popularTV, 0, -popularTV.getHeight());
		popularTV.setVisibility(View.INVISIBLE);
		popularWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				popularTV.setVisibility(View.VISIBLE);
			}
		});
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popularWindow.dismiss();
				popularTV.setVisibility(View.VISIBLE);
				if(Constants.planet==2){
					popularTV.setText("汪星人气日榜");
				}else{
					popularTV.setText("喵星人气日榜");
				}
				loadDayData();
			}
		});
        tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popularWindow.dismiss();
				popularTV.setVisibility(View.VISIBLE);
				if(Constants.planet==2){
					popularTV.setText("汪星人气周榜");
				}else{
					popularTV.setText("喵星人气周榜");
				}
				loadWeekData();
			}
		});
        tv3.setOnClickListener(new OnClickListener() {
	
	        @Override
	        public void onClick(View v) {
		    // TODO Auto-generated method stub
	        	popularWindow.dismiss();
	        	popularTV.setVisibility(View.VISIBLE);
	        	if(Constants.planet==2){
	    			popularTV.setText("汪星人气月榜");
	    		}else{
	    			popularTV.setText("喵星人气月榜");
	    		}
	        	loadMonthData();
	        }
        });
        tv4.setOnClickListener(new OnClickListener() {
        	
	        @Override
	        public void onClick(View v) {
		    // TODO Auto-generated method stub
	        	popularWindow.dismiss();
	        	popularTV.setVisibility(View.VISIBLE);
	        	if(Constants.planet==2){
	    			popularTV.setText("汪星人气总榜");
	    		}else{
	    			popularTV.setText("喵星人气总榜");
	    		}
	        	loadTotalData();
	        }

			
        });
	}
	/**
	 * 下载总人气榜数据
	 */
	private void loadTotalData() {
		// TODO Auto-generated method stub
		SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		String json=sp.getString("json", null);
		category=0;
		if(StringUtil.isEmpty(json)||"null".equals(json)){
			loadData(0);
			return;
		}
//		parseJson(json,0);
		getDataByType(currentType);
		
	}
	/**
	 * 下载日人气榜数据
	 */
	private void loadDayData() {
		// TODO Auto-generated method stub
		SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		String json=sp.getString("json1", null);
		category=1;
		if(StringUtil.isEmpty(json)||"null".equals(json)){
			loadData(1);
			return;
		}
//		parseJson(json,1);
		getDataByType(currentType);
		
	}
	/**
	 * 下载周人气榜数据
	 */
	private void loadWeekData() {
		// TODO Auto-generated method stub
		SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		String json=sp.getString("json2", null);
		category=2;
		if(StringUtil.isEmpty(json)||"null".equals(json)){
			loadData(2);
			return;
		}
		
//		parseJson(json,2);
		getDataByType(currentType);
		
	}
	/**
	 * 下载月人气榜数据
	 */
	private void loadMonthData() {
		// TODO Auto-generated method stub
		SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		String json=sp.getString("json3", null);
		category=3;
		if(StringUtil.isEmpty(json)||"null".equals(json)){
			loadData(3);
			return;
		}
		
//		parseJson(json,3);
		getDataByType(currentType);
		
	}
	private void parseJson(final String json,final int category){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
		try {
			JSONArray ja=new JSONArray(json);
			if(ja!=null&&ja.length()>0){
				final ArrayList<Animal> temp1=new ArrayList<Animal>();
				final ArrayList<Animal>	temp2=new ArrayList<Animal>();
				Animal animal=null;
				JSONObject j2=null;
				for(int i=0;i<ja.length();i++){
					j2=ja.getJSONObject(i);
					  animal=new Animal();
					  animal.type=j2.getInt("type");
					  animal.a_id=j2.getInt("aid");
					  animal.pet_nickName=j2.getString("name");
					  animal.pet_iconUrl=j2.getString("tx");
					  switch (category) {
					case 0:
						animal.t_rq=j2.getInt("t_rq");
						break;

					case 1:
						animal.d_rq=j2.getInt("d_rq");
						break;

					case 2:
						animal.w_rq=j2.getInt("w_rq");
						break;

					case 3:
						animal.m_rq=j2.getInt("m_rq");
						break;
					}
					  
					  
					  animal.change=j2.getInt("vary");
					  animal.ranking=i+1;
					  for(int j=0;j<Constants.user.aniList.size();j++){
						  if(animal.a_id==Constants.user.aniList.get(j).a_id){
							  animal.hasJoinOrCreate=true;
							  temp2.add(animal);
						  }
					  }
					  
					  if(i<1000)
					  temp1.add(animal);
				}
				if(temp1.size()>=0){
					runOnUiThread(new Runnable() {
						public void run() {
							peopleList=temp1;
							myList=temp2;
							isAllData=false;
							myListIndex=0;
							PopularRankListActivity.this.position=0;
							if(temp1.size()<10){
								viewTopWhite.setVisibility(View.VISIBLE);
							}
							
							PopularRankListActivity.this.adapter.updateData(temp1);
							PopularRankListActivity.this.adapter.notifyDataSetChanged();
						}
					});
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			}
		}).start();
	}
	private void showRaceWindow() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(this).inflate(R.layout.popup_popular_1, null);
	    TextView tv1=(TextView)view.findViewById(R.id.textView1);
	    ListView listView=(ListView)view.findViewById(R.id.listview);
	   String[] array=getResources().getStringArray(R.array.cat_race);
	    final String[] strArray=new String[array.length+1];
	    for(int i=0;i<strArray.length;i++){
	    	if(i==0){
	    		strArray[0]="所有种族";
	    	}else if(i-1>=0&&i-1<array.length){
	    		strArray[i]=array[i-1];
	    	}
	    	
	    }
	    PopularWindowAdapter adapter=new PopularWindowAdapter(this, strArray);
	    listView.setAdapter(adapter);
	    raceWindow=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    raceWindow.setFocusable(true);
	    raceWindow.setBackgroundDrawable(new BitmapDrawable());
	    raceWindow.setOutsideTouchable(true);
	    raceWindow.showAsDropDown(raceTV, 0, -raceTV.getHeight());
		raceTV.setVisibility(View.INVISIBLE);
		tv1.setVisibility(View.GONE);
	
		raceWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				raceTV.setVisibility(View.VISIBLE);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				raceTV.setText(""+strArray[position]);
				raceTV.setVisibility(View.VISIBLE);
				raceWindow.dismiss();
				String str="";
				if(Constants.planet==2){
					str="2";
				}else{
					str="1";
				}
				if(position+1>10){
					str+=""+(position);
				}else{
					str+="0"+(position);
				}
				final int type=Integer.parseInt(str);
               getDataByType(type);
			}
		});
	}
	public void getDataByType(final int type){
		 new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
					String json=sp.getString("json", null);
					switch (category) {
					case 0:
						json=sp.getString("json", null);
						break;

					case 1:
						json=sp.getString("json1", null);
						break;

					case 2:
						json=sp.getString("json2", null);
						break;

					case 3:
						json=sp.getString("json3", null);
						break;
					}
					if(StringUtil.isEmpty(json)||"null".equals(json))return;
					try {
						JSONArray ja=new JSONArray(json);
						if(ja!=null&&ja.length()>0){
							final ArrayList<Animal> temp1=new ArrayList<Animal>();
							final ArrayList<Animal>	temp2=new ArrayList<Animal>();
							Animal animal=null;
							JSONObject j2=null;
							for(int i=0;i<ja.length();i++){
								j2=ja.getJSONObject(i);
								  animal=new Animal();
								  animal.type=j2.getInt("type");
								 
								  animal.a_id=j2.getInt("aid");
								  animal.pet_nickName=j2.getString("name");
								  animal.pet_iconUrl=j2.getString("tx");
								  switch (category) {
									case 0:
										animal.t_rq=j2.getInt("t_rq");
										break;

									case 1:
										animal.d_rq=j2.getInt("d_rq");
										break;

									case 2:
										animal.w_rq=j2.getInt("w_rq");
										break;

									case 3:
										animal.m_rq=j2.getInt("m_rq");
										break;
									}
								  
								  animal.change=j2.getInt("vary");
								  animal.ranking=i+1;
								  if(myList.size()>0&&animal.ranking==myList.get(myListIndex).ranking){
									  animal.hasJoinOrCreate=true;
									  temp2.add(animal);
								  }
								  if(i<1000){
									  if(type==100||type==200||animal.type==type){
										  temp1.add(animal);
									  }
									  
								  }
								 
							}
								runOnUiThread(new Runnable() {
									public void run() {
										peopleList=temp1;
										myList=temp2;
										isAllData=false;
										myListIndex=0;
										if(temp1.size()<10){
											viewTopWhite.setVisibility(View.VISIBLE);
										}
										PopularRankListActivity.this.position=0;
										PopularRankListActivity.this.adapter.updateData(temp1);
										PopularRankListActivity.this.adapter.notifyDataSetChanged();
									}
								});
							}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
	}



}
