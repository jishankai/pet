package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.AbsListView;
/**
 * 认养时，选择国王界面
 * @author admin
 *
 */
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ChoseKingListViewAdapter;
import com.aidigame.hisun.pet.adapter.PopularWindowAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.aidigame.hisun.pet.widget.ShowProgress;
/**
 * 认养时，选择国王界面
 * 两种显示方式   1.狗，2.猫
 * @author admin
 *
 */
public class ChoseKingActivity extends Activity implements OnClickListener ,PullToRefreshAndMoreListener{
	FrameLayout frameLayout;
//	View viewTopWhite;
	public View popupParent;//PopupWindwo位置相关parent
	public RelativeLayout black_layout;
	public static ChoseKingActivity choseKingActivity;
	private PullToRefreshAndMoreView pullToRefreshAndMoreView;
	private LinearLayout progressLayout;
	private ListView listView;
//	String race,style;
	private ImageView back,search,search3,cleanIV;
	private TextView raceTV,choseStyleSpinner;
	private PopupWindow raceWindow;
	private ShowProgress showProgress;
	private ArrayList<Animal> list;
	private ChoseKingListViewAdapter choseKingListViewAdapter;
	private LinearLayout functionLayout;
	private LinearLayout searchLayout;
	private ImageView search2;
	private TextView cancel;
	private EditText inputET;
	private int from;//默认值为0，进行注册；1，已经注册过
	private HandleHttpConnectionException handleHttpConnectionException;
	
	
	
	private RelativeLayout titleLayout;
	private View view1;
	
	
	
	private LinearLayout noteLinearLayout;
	private int mode;
	private int currentType=0;//-1为所有种族
	private int currentStyle=1;//1,推荐；2,人气
	private int currentFrom;
	private long last_aid=0;
	private boolean isBind=false;
	private MyUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_chose_king);
		mode=getIntent().getIntExtra("mode", 2);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		choseKingActivity=this;
		from=getIntent().getIntExtra("from", 0);
		isBind=getIntent().getBooleanExtra("isBind", isBind);
		if(isBind)user=(MyUser)getIntent().getSerializableExtra("user");
		inintView();
	}
	private void inintView(){
		pullToRefreshAndMoreView=(PullToRefreshAndMoreView)findViewById(R.id.chose_king_list);
		functionLayout=(LinearLayout)findViewById(R.id.function_layout);
		
		
		titleLayout=(RelativeLayout)findViewById(R.id.relativeLayout1);
		view1=(View)findViewById(R.id.view1);
//		viewTopWhite=(View)findViewById(R.id.top_white_view);
		
		
		searchLayout=(LinearLayout)findViewById(R.id.search_layout);
		noteLinearLayout=(LinearLayout)findViewById(R.id.note_layout);
		progressLayout=(LinearLayout)findViewById(R.id.progress_layout);
		back=(ImageView)findViewById(R.id.chose_king_back);
		search=(ImageView)findViewById(R.id.chose_king_search);
		search3=(ImageView)findViewById(R.id.chose_king_search3);
		cleanIV=(ImageView)findViewById(R.id.imageView1);
		search2=(ImageView)findViewById(R.id.search);
		cancel=(TextView)findViewById(R.id.cancel_tv);
		inputET=(EditText)findViewById(R.id.input);
		
		popupParent=findViewById(R.id.popup_parent);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		raceTV=(TextView)findViewById(R.id.chose_king_chose_race);
		choseStyleSpinner=(TextView)findViewById(R.id.chose_king_chose_style);
		listView=pullToRefreshAndMoreView.getListView();
		listView.setCacheColorHint(Color.TRANSPARENT);
		pullToRefreshAndMoreView.setHeaderAndFooterInvisible();
		pullToRefreshAndMoreView.setListener(this);
        list=new ArrayList<Animal>();
        choseKingListViewAdapter=new ChoseKingListViewAdapter(this, list,mode,from,isBind,user);
		listView.setAdapter(choseKingListViewAdapter);
		raceTV.setText("所有");
		loadData();
		setBlurImageBackground();
		
		listView.setDivider(null);
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		raceTV.setOnClickListener(this);
		choseStyleSpinner.setOnClickListener(this);
		search2.setOnClickListener(this);
		cancel.setOnClickListener(this);
		cleanIV.setOnClickListener(this);
		search3.setOnClickListener(this);
		inputET.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(arg0.length()>0){
					cancel.setText("搜索");
					isSearching=true;
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	/**
	 * 下载数据
	 * 1.种族类别
	 * 2.国王或族长列表
	 */
	private void loadData() {
		// TODO Auto-generated method stub
		if(showProgress==null){
			showProgress=new ShowProgress(this, progressLayout);
		}else{
			showProgress.showProgress();;
		}
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			final ArrayList<Animal> temp=HttpUtil.recommendKingdom(ChoseKingActivity.this,0, 0, handleHttpConnectionException.getHandler(ChoseKingActivity.this),currentStyle,currentFrom);
			
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(temp!=null){
						list=temp;
						choseKingListViewAdapter.updateList(temp);
						choseKingListViewAdapter.notifyDataSetChanged();
						}
						showProgress.progressCancel();
					}
				});
			
			}
		}).start();
	
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
       
		 listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(list.size()<=0)return;
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0).getTop()==0){
//						viewTopWhite.setVisibility(View.VISIBLE);
					}else{
						/*if(viewTopWhite.getVisibility()!=View.GONE){
							viewTopWhite.setVisibility(View.GONE);
						}*/
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
				}
			});
	}
	private boolean isSearching=false;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.chose_king_back:
			choseKingActivity=null;
			
			if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(this)){
				PetApplication.petApp.activityList.remove(this);
			}
			this.finish();
			System.gc();
			break;
		case R.id.chose_king_search:
			functionLayout.setVisibility(View.INVISIBLE);
			searchLayout.setVisibility(View.VISIBLE);
			titleLayout.setVisibility(View.GONE);
			view1.setVisibility(View.VISIBLE);
//			viewTopWhite.setVisibility(View.GONE);
			break;
		case R.id.chose_king_chose_race:
//			showRaceWindow();
			showStarWindow();
			break;
		case R.id.chose_king_chose_style:
			showStyleWindow();
			break;
		case R.id.search:
			
			break;
		case R.id.cancel_tv:
			if(!isSearching){
				functionLayout.setVisibility(View.VISIBLE);
				searchLayout.setVisibility(View.INVISIBLE);
				titleLayout.setVisibility(View.VISIBLE);
				view1.setVisibility(View.GONE);
//				viewTopWhite.setVisibility(View.VISIBLE);
				loadData();
			}else{
				String name=inputET.getText().toString();
				if(!StringUtil.isEmpty(name)){
					searchPet(name);
				}else{
					Toast.makeText(this, "搜索宠物名称不能为空", Toast.LENGTH_LONG).show();
				}
			}
			
			break;
		case R.id.imageView1:
			inputET.setText("");
			cancel.setText("取消");
			
			isSearching=false;
			break;
		case R.id.chose_king_search3:
			functionLayout.setVisibility(View.INVISIBLE);
			searchLayout.setVisibility(View.VISIBLE);
			titleLayout.setVisibility(View.GONE);
			view1.setVisibility(View.VISIBLE);
//			viewTopWhite.setVisibility(View.GONE);
			break;
		}
	}
	private  void showStyleWindow(){
		View view=LayoutInflater.from(this).inflate(R.layout.popup_popular_2, null);
	    TextView tv1=(TextView)view.findViewById(R.id.textView1);
	    ListView listView=(ListView)view.findViewById(R.id.listview);
	    listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	    final String[] strArray=new String[2];
	    strArray[0]="推荐";
	    strArray[1]="人气";
	    tv1.setVisibility(View.GONE);
	    PopularWindowAdapter adapter=new PopularWindowAdapter(this, strArray);
	    listView.setAdapter(adapter);
	    
	    raceWindow=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    raceWindow.setFocusable(true);
	    raceWindow.setBackgroundDrawable(new BitmapDrawable());
	    raceWindow.setOutsideTouchable(true);
	    raceWindow.showAsDropDown(choseStyleSpinner, 0, -choseStyleSpinner.getHeight());
	    choseStyleSpinner.setVisibility(View.INVISIBLE);
		raceWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				choseStyleSpinner.setVisibility(View.VISIBLE);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				choseStyleSpinner.setText(""+strArray[position]);
				choseStyleSpinner.setVisibility(View.VISIBLE);
				raceWindow.dismiss();
				currentStyle=position+1;
				showProgress.showProgress();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final ArrayList<Animal> animals=HttpUtil.recommendKingdom(ChoseKingActivity.this,currentType, 0, handleHttpConnectionException.getHandler(ChoseKingActivity.this),currentStyle,currentFrom);
						
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(animals!=null){
										list=animals;
										choseKingListViewAdapter.updateList(animals);
										choseKingListViewAdapter.notifyDataSetChanged();
										if(animals.size()==0){
											noteLinearLayout.setVisibility(View.VISIBLE);
										}else{
											noteLinearLayout.setVisibility(View.INVISIBLE);
										}
										}else{
											list=new ArrayList<Animal>();
											choseKingListViewAdapter.updateList(list);
											choseKingListViewAdapter.notifyDataSetChanged();
											noteLinearLayout.setVisibility(View.VISIBLE);
										}
									showProgress.progressCancel();
								}
							});
						
					}
				}).start();
				
				
			}
		});
	}
	private void showRaceWindow() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(this).inflate(R.layout.popup_popular_1, null);
	    TextView tv1=(TextView)view.findViewById(R.id.textView1);
	    ListView listView=(ListView)view.findViewById(R.id.listview);
	    listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	    String[] temp=null;
	    if(Constants.planet==2){
	    	temp=getResources().getStringArray(R.array.dog_race);
	    }else{
	    	temp=getResources().getStringArray(R.array.cat_race);
	    }
	    final String[] strArray=new String[temp.length+1];
	    for(int i=0;i<strArray.length;i++){
	    	if(i==0){
	    		strArray[0]="所有种族";
	    	}else{
	    		strArray[i]=temp[i-1];
	    	}
	    	
	    }
	    
	    tv1.setVisibility(View.GONE);
	    PopularWindowAdapter adapter=new PopularWindowAdapter(this, strArray);
	    listView.setAdapter(adapter);
	    raceWindow=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    raceWindow.setFocusable(true);
	    raceWindow.setBackgroundDrawable(new BitmapDrawable());
	    raceWindow.setOutsideTouchable(true);
	    raceWindow.showAsDropDown(raceTV, 0, -raceTV.getHeight());
		raceTV.setVisibility(View.INVISIBLE);
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProgress.showProgress();
              new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final ArrayList<Animal> animals=HttpUtil.recommendKingdom(ChoseKingActivity.this,-1, -1, handleHttpConnectionException.getHandler(ChoseKingActivity.this),currentStyle,currentFrom);
						
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(animals!=null){
									list=animals;
									choseKingListViewAdapter.updateList(animals);
									choseKingListViewAdapter.notifyDataSetChanged();
									if(animals.size()==0){
										noteLinearLayout.setVisibility(View.VISIBLE);
									}else{
										noteLinearLayout.setVisibility(View.INVISIBLE);
									}
									}else{
										list=new ArrayList<Animal>();
										choseKingListViewAdapter.updateList(list);
										choseKingListViewAdapter.notifyDataSetChanged();
										noteLinearLayout.setVisibility(View.VISIBLE);
									}
									showProgress.progressCancel();
								}
							});
						
					}
				}).start();
				raceTV.setText("所有种族");
				raceWindow.dismiss();
			}
		});
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
				showProgress.showProgress();
				raceTV.setText(""+strArray[position]);
				raceTV.setVisibility(View.VISIBLE);
				raceWindow.dismiss();
//				position+=1;
				String type="";
				if(position<10){
					type="0"+position;
				}else{
					type=""+position;
				}
				if(Constants.planet==2){
					type="2"+type;
				}else{
					type="1"+type;
				}
				if(position==0){
					type=""+-1;
				}
				final String temp=type;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						currentType=Integer.parseInt(temp);
						final ArrayList<Animal> animals=HttpUtil.recommendKingdom(ChoseKingActivity.this,Integer.parseInt(temp), -1, handleHttpConnectionException.getHandler(ChoseKingActivity.this),currentStyle,currentFrom);
						
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(animals!=null){
										list=animals;
										choseKingListViewAdapter.updateList(animals);
										choseKingListViewAdapter.notifyDataSetChanged();
										if(animals.size()==0){
											noteLinearLayout.setVisibility(View.VISIBLE);
										}else{
											noteLinearLayout.setVisibility(View.INVISIBLE);
										}
										}else{
											list=new ArrayList<Animal>();
											choseKingListViewAdapter.updateList(list);
											choseKingListViewAdapter.notifyDataSetChanged();
											noteLinearLayout.setVisibility(View.VISIBLE);
										}
									showProgress.progressCancel();
								}
							});
						
					}
				}).start();
				
				
			}
		});
	}
	private void showStarWindow() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(this).inflate(R.layout.popup_popular_1, null);
	    TextView tv1=(TextView)view.findViewById(R.id.textView1);
	    ListView listView=(ListView)view.findViewById(R.id.listview);
	    listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	    String[] temp=null;
	    if(Constants.planet==2){
	    	temp=getResources().getStringArray(R.array.dog_race);
	    }else{
	    	temp=getResources().getStringArray(R.array.cat_race);
	    }
	    final String[] strArray=new String[4];
	    strArray[0]="所有";
	    strArray[1]="喵喵";
	    strArray[2]="汪汪";
	    strArray[3]="其他";
	    tv1.setVisibility(View.GONE);
	    PopularWindowAdapter adapter=new PopularWindowAdapter(this, strArray);
	    listView.setAdapter(adapter);
	    raceWindow=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    raceWindow.setFocusable(true);
	    raceWindow.setBackgroundDrawable(new BitmapDrawable());
	    raceWindow.setOutsideTouchable(true);
	    raceWindow.showAsDropDown(raceTV, 0, -raceTV.getHeight());
		raceTV.setVisibility(View.INVISIBLE);
		
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProgress.showProgress();
              new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						last_aid=0;
						final ArrayList<Animal> animals=HttpUtil.recommendKingdom(ChoseKingActivity.this,0, 0, handleHttpConnectionException.getHandler(ChoseKingActivity.this),currentStyle,currentFrom);
						
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(animals!=null){
									list=animals;
									choseKingListViewAdapter.updateList(animals);
									choseKingListViewAdapter.notifyDataSetChanged();
									if(animals.size()==0){
										noteLinearLayout.setVisibility(View.VISIBLE);
									}else{
										noteLinearLayout.setVisibility(View.INVISIBLE);
									}
									}else{
										list=new ArrayList<Animal>();
										choseKingListViewAdapter.updateList(list);
										choseKingListViewAdapter.notifyDataSetChanged();
										noteLinearLayout.setVisibility(View.VISIBLE);
									}
									showProgress.progressCancel();
								}
							});
						
					}
				}).start();
				raceTV.setText("喵喵");
				raceWindow.dismiss();
			}
		});
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
				showProgress.showProgress();
				raceTV.setText(""+strArray[position]);
				raceTV.setVisibility(View.VISIBLE);
				raceWindow.dismiss();
				final String temp=""+(position);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						currentFrom=Integer.parseInt(temp);
						last_aid=0;
						final ArrayList<Animal> animals=HttpUtil.recommendKingdom(ChoseKingActivity.this,currentFrom, 0, handleHttpConnectionException.getHandler(ChoseKingActivity.this),currentStyle,currentFrom);
						
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(animals!=null){
										list=animals;
										choseKingListViewAdapter.updateList(animals);
										choseKingListViewAdapter.notifyDataSetChanged();
										if(animals.size()==0){
											noteLinearLayout.setVisibility(View.VISIBLE);
										}else{
											noteLinearLayout.setVisibility(View.INVISIBLE);
										}
										}else{
											list=new ArrayList<Animal>();
											choseKingListViewAdapter.updateList(list);
											choseKingListViewAdapter.notifyDataSetChanged();
											noteLinearLayout.setVisibility(View.VISIBLE);
										}
									showProgress.progressCancel();
								}
							});
						
					}
				}).start();
				
				
			}
		});
	}
	private  void searchPet(final String name){
	    	showProgress.showProgress();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final ArrayList<Animal> animals=HttpUtil.searchUserOrPet(ChoseKingActivity.this,name, -1, handleHttpConnectionException.getHandler(ChoseKingActivity.this));
//					HttpUtil.animalInfo(animal,  handleHttpConnectionException.getHandler(ChoseKingActivity.this));
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(animals!=null){
								list=animals;
								choseKingListViewAdapter.updateList(list);
								choseKingListViewAdapter.notifyDataSetChanged();
							}else{
								Toast.makeText(ChoseKingActivity.this, "没有搜索到名字为 "+name+" 的宠物", Toast.LENGTH_LONG).show();
							}
							isSearching=false;
							showProgress.progressCancel();
						}
					});
				}
			}).start();
	    }
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
//		showProgress.showProgress();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				last_aid=0;
				final ArrayList<Animal> animals=HttpUtil.recommendKingdom(ChoseKingActivity.this,currentType, last_aid, handleHttpConnectionException.getHandler(ChoseKingActivity.this),currentStyle,currentFrom);
				
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(animals!=null){
								list=animals;
								choseKingListViewAdapter.updateList(animals);
								choseKingListViewAdapter.notifyDataSetChanged();
								if(animals.size()==0){
									noteLinearLayout.setVisibility(View.VISIBLE);
								}else{
									noteLinearLayout.setVisibility(View.INVISIBLE);
								}
								}else{
									list=new ArrayList<Animal>();
									choseKingListViewAdapter.updateList(list);
									choseKingListViewAdapter.notifyDataSetChanged();
									noteLinearLayout.setVisibility(View.VISIBLE);
								}
							pullToRefreshAndMoreView.onRefreshFinish();
//							showProgress.progressCancel();
						}
					});
				
			}
		}).start();
		
	}
	@Override
	public void onMore() {
		// TODO Auto-generated method stub
//		showProgress.showProgress();
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				if(list!=null&&list.size()>0){
					if(last_aid>=0){
						last_aid++;
					}else{
						last_aid=1;
					}
				}else{
					list=new ArrayList<Animal>();
					
				}
				final ArrayList<Animal> animals=HttpUtil.recommendKingdom(ChoseKingActivity.this,currentType, last_aid, handleHttpConnectionException.getHandler(ChoseKingActivity.this),currentStyle,currentFrom);
				
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(animals!=null){
								for(int i=0;i<animals.size();i++){
									if(!list.contains(animals.get(i))){
										list.add(animals.get(i));
									}
								}
								choseKingListViewAdapter.updateList(list);
								choseKingListViewAdapter.notifyDataSetChanged();
								if(list.size()==0){
									noteLinearLayout.setVisibility(View.VISIBLE);
								}else{
									noteLinearLayout.setVisibility(View.INVISIBLE);
								}
								}else{
									last_aid--;
									if(last_aid<0)last_aid=0;
									/*list=new ArrayList<Animal>();
									choseKingListViewAdapter.updateList(list);
									choseKingListViewAdapter.notifyDataSetChanged();
									noteLinearLayout.setVisibility(View.VISIBLE);*/
								}
							pullToRefreshAndMoreView.onMoreFinish();;
//							showProgress.progressCancel();
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

}
