package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.KingdomPeoplesAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;

/**
 * 王国人员列表
 * @author admin
 *
 */
public class KingdomPeoples implements PullToRefreshAndMoreListener{
//	PullToRefreshAndMoreView ptram;
	PullAndLoadListView ptram;
	   View view;
	   PetKingdomActivity context;
//	   ListView listView;
	   public KingdomPeoplesAdapter adapter;
	   public ArrayList<User> datas;
	   int last_id=-1;
	   HandleHttpConnectionException handleHttpConnectionException;
	   Animal data;
	public KingdomPeoples(PetKingdomActivity context,Animal data){
		   view=LayoutInflater.from(context).inflate(R.layout.widget_pull_to_refresh_and_more1, null);
			  this.ptram=(PullAndLoadListView)view.findViewById(R.id.listview_linearLayout3);
			   this.context=context;
			   this.data=data;
			   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
			   initView();
			   initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
			
			datas=new ArrayList<User>();
			
//			ptram.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));
			adapter=new KingdomPeoplesAdapter(context, datas);
		
			ptram.setAdapter(adapter);
			loadData();
			ptram.setDivider(null);
			ptram.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					last_id=-1;
					new Thread(new Runnable() {
						ArrayList<User> temp=new ArrayList<User>();
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ArrayList<User> userList=HttpUtil.kingdomPeoples(context,last_id, data, handleHttpConnectionException.getHandler(context));
							
							if(userList!=null){
								if(userList.size()>0){
									User data=null;
									for(int i=0;i<userList.size();i++){
										data=userList.get(i);
										if(!temp.contains(data)){
											temp.add(data);
										}
									}
									
								}
							}
							context.runOnUiThread(new Runnable() {
								public void run() {
									if(temp!=null&&temp.size()>0){
										adapter.updateList(temp);
										datas=temp;
										adapter.notifyDataSetChanged();
										
									}
									ptram.onRefreshComplete();
								}
							});
						}
							
						
					}).start();
				}
			});
			ptram.setOnLoadMoreListener(new OnLoadMoreListener() {
				
				@Override
				public void onLoadMore() {
					// TODO Auto-generated method stub
					if(datas.size()>0){
						last_id=datas.get(datas.size()-1).userId;
					}
					
					new Thread(new Runnable() {
						ArrayList<User> temp=new ArrayList<User>();
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ArrayList<User> userList=HttpUtil.kingdomPeoples(context,last_id, data, handleHttpConnectionException.getHandler(context));
							
							if(userList!=null){
								if(userList.size()>0){
									User data=null;
									for(int i=0;i<datas.size();i++){
										if(!temp.contains(datas.get(i))){
											temp.add(datas.get(i));
										}
									}
									for(int i=0;i<userList.size();i++){
										data=userList.get(i);
										if(!temp.contains(data)){
											temp.add(data);
										}
									}
									
								}
							}
							context.runOnUiThread(new Runnable() {
								public void run() {
									if(temp!=null&&temp.size()>0){
										adapter.updateList(temp);
										datas=temp;
										adapter.notifyDataSetChanged();
										ptram.onLoadMoreComplete();
									}
									
								}
							});
						}
							
						
					}).start();
				}
			});
			
			
			
//			listView=ptram.getListView();
//			listView.setDivider(null);
//			listView.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));
//			ptram.setListener(this);
//			ptram.setHeaderAndFooterInvisible();
	}
	public void loadData(){
		last_id=-1;
		new Thread(new Runnable() {
			ArrayList<User> temp=new ArrayList<User>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<User> userList=HttpUtil.kingdomPeoples(context,last_id, data, handleHttpConnectionException.getHandler(context));
				
				if(userList!=null){
					if(userList.size()>0){
						User data=null;
						for(int i=0;i<userList.size();i++){
							data=userList.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
						context.runOnUiThread(new Runnable() {
							public void run() {
								adapter.updateList(temp);
								datas=temp;
								adapter.notifyDataSetChanged();
								
							}
						});
					}
				}
			}
				
			
		}).start();
	}
	public void refresh(){
		last_id=-1;
		new Thread(new Runnable() {
			ArrayList<User> temp=new ArrayList<User>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<User> userList=HttpUtil.kingdomPeoples(context,last_id, data, handleHttpConnectionException.getHandler(context));
				
				if(userList!=null){
					if(userList.size()>0){
						User data=null;
						for(int i=0;i<userList.size();i++){
							data=userList.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
						context.runOnUiThread(new Runnable() {
							public void run() {
								adapter.updateList(temp);
								datas=temp;
								adapter.notifyDataSetChanged();
							}
						});
					}
				}
			}
				
			
		}).start();
	}
	@Override
	public void onRefresh() {
			// TODO Auto-generated method stub
		last_id=-1;
		new Thread(new Runnable() {
			ArrayList<User> temp=new ArrayList<User>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				/*ArrayList<User> userList=HttpUtil.kingdomPeoples(context,last_id, data, handleHttpConnectionException.getHandler(context));
				
				if(userList!=null){
					if(userList.size()>0){
						User data=null;
						for(int i=0;i<userList.size();i++){
							data=userList.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
						ptram.onRefreshFinish();
						context.runOnUiThread(new Runnable() {
							public void run() {
								adapter.updateList(temp);
								datas=temp;
								adapter.notifyDataSetChanged();
							}
						});
					}
				}
				ptram.onRefreshFinish();*/
			}
				
			
		}).start();
		
	}
	@Override
	public void onMore() {
			// TODO Auto-generated method stub
		/*if(datas.size()>0){
			last_id=datas.get(datas.size()-1).userId;
		}
		
		new Thread(new Runnable() {
			ArrayList<User> temp=new ArrayList<User>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<User> userList=HttpUtil.kingdomPeoples(context,last_id, data, handleHttpConnectionException.getHandler(context));
				
				if(userList!=null){
					if(userList.size()>0){
						User data=null;
						for(int i=0;i<userList.size();i++){
							data=userList.get(i);
							if(!datas.contains(data)){
								datas.add(data);
							}
						}
						ptram.onMoreFinish();
						context.runOnUiThread(new Runnable() {
							public void run() {
								adapter.updateList(datas);
								adapter.notifyDataSetChanged();
							}
						});
					}
				}
				ptram.onMoreFinish();
			}
				
			
		}).start();*/
		
	}
	public View getView(){
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(params);
//		ptram.setHeaderAndFooterInvisible();
			return view;
	}
	boolean isShow=true;
	private void initListener() {
			// TODO Auto-generated method stub
		ptram.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(scrollState!=SCROLL_STATE_IDLE)return;
					LogUtil.i("scroll", "context.isShowInfoLayout="+context.isShowInfoLayout);
					if(ptram.getFirstVisiblePosition()==0&&ptram.getChildAt(0)!=null/*&&listView.getChildAt(0).getBottom()==listView.getChildAt(0).getHeight()*/){
						LogUtil.i("scroll", "context.isShowInfoLayout="+context.isShowInfoLayout);
						if(!context.isShowInfoLayout){
//							animationIn();
							if(isShow){
								isShow=false;
								return;
							}
							isShow=true;
							context.linearLayout3.setVisibility(View.VISIBLE);
							context.infoShadowView.setVisibility(View.VISIBLE);
							context.topWhiteView.setVisibility(View.VISIBLE);
							context.isShowInfoLayout=true;
						}
						
					}else{
						if(context.isShowInfoLayout){
							animationOut();
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
	float infoYTop;//
	float infoYBottom;
	boolean isRecord=false;
	public void animationOut(){
		if(!isRecord){
			infoYTop=context.linearLayout2.getY();
			infoYBottom=context.linearLayout2.getBottom();
			isRecord=true;
		}
		
		TranslateAnimation anim=new TranslateAnimation(0, 0, 0, -infoYBottom);
		anim.setDuration(100);
		anim.setFillAfter(false);
		/*TranslateAnimation anim2=new TranslateAnimation(0, 0, 0, -infoYBottom);
		anim2.setDuration(100);
		anim2.setFillAfter(false);*/
        anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				context.infoShadowView.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
//				context.linearLayout2.layout(0, (int) -infoYBottom, context.linearLayout2.getWidth(),0);
				context.linearLayout3.setVisibility(View.GONE);
				context.infoShadowView.setVisibility(View.GONE);
				context.topWhiteView.setVisibility(View.GONE);
				context.isShowInfoLayout=false;
				isShow=false;
			}
		});
		/*anim2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				context.linearLayout3.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
//				context.linearLayout2.layout(0, (int) -infoYBottom, context.linearLayout2.getWidth(),0);
				context.linearLayout3.setVisibility(View.GONE);
				context.infoShadowView.setVisibility(View.GONE);
				context.topWhiteView.setVisibility(View.GONE);
				context.isShowInfoLayout=false;
				isShow=false;
			}
		});*/
		context.linearLayout3.clearAnimation();
		context.linearLayout3.startAnimation(anim);
//		context.infoShadowView.clearAnimation();
//		context.infoShadowView.startAnimation(anim2);
	}
	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
//		listView.setBackgroundResource(R.color.blur_view_top);;
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
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ptram.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
						ptram.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
	}

}
