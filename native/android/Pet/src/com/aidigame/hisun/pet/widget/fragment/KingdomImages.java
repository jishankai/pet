package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.KingdomTrendsListAdapter;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter2;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * 王国图片列表
 * @author admin
 *
 */
public class KingdomImages implements PullToRefreshAndMoreListener{
	PullAndLoadListView ptram;
	   View view;
	   PetKingdomActivity context;
//	   ListView pictureListView;
	   ArrayList<PetPicture> datas;
	   ShowTopicsAdapter2 showTopicsAdapter;
	   int lastImage_id=-1;
	   Animal data;
	public KingdomImages(PetKingdomActivity context,Animal data){
		   view=LayoutInflater.from(context).inflate(R.layout.widget_pull_to_refresh_and_more1, null);
			  this.ptram=(PullAndLoadListView)view.findViewById(R.id.listview_linearLayout3);
			   this.context=context;
			   this.data=data;
			   initView();
			   initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
			
			datas=new ArrayList<PetPicture>();
			ptram.setDivider(null);
			ptram.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));
			//TODO
			showTopicsAdapter=new ShowTopicsAdapter2(context, datas, 1);
			ptram.setAdapter(showTopicsAdapter);
			lastImage_id=-1;
			new Thread(new Runnable() {
				ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
				@Override
				public void run() {
					// TODO Auto-generated method stub
					UserImagesJson json1=null;
					json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
					   /* if(data.master_id==Constants.user.userId){
					    	json1=HttpUtil.downloadUserHomepage(null, lastImage_id,0,context);
					    }else{
					    	json1=HttpUtil.downloadOtherUserHomepage(null, lastImage_id, data,context);
					    }*/
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
									data.animal=KingdomImages.this.data;
									if(!temp.contains(data)){
										temp.add(data);
									}
								 }
								context.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										datas=temp;
										//TODO
										showTopicsAdapter.updateTopics(temp);
										showTopicsAdapter.notifyDataSetChanged();
										
										LogUtil.i("scroll","datas大小========="+datas.size());
										
									}
								});
							}
						}else{
							//TODO 下载失败
						}
					}
					
				
			}).start();
			ptram.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
							lastImage_id=-1;
							UserImagesJson json1=null;
							json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
						    
							final UserImagesJson json=json1;
							if(json!=null){
								if(json.petPictures!=null&&json.petPictures.size()>0){
//									if(datas.size()>0){
//										if(datas.get(0).img_id==json.petPictures.get(0).img_id){
//											//下载获得的图片列表与现在界面显示的完全相同，则不进行界面更新
////											return;
//										}
//									}
									for(int i=0;i<datas.size();i++){
										temp.add(datas.get(i));
									}
									PetPicture data=null;
									for(int i=0;i<json.petPictures.size();i++){
										if(!temp.contains(json.petPictures.get(i))){
											data=json.petPictures.get(i);
											data.animal=KingdomImages.this.data;
											temp.add(i,data);
										}
										
										
									}
								}else{
								}
							}else{
								//TODO 联网下载失败
							}
							context.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//TODO
									if(temp!=null&&temp.size()>0){
										showTopicsAdapter.updateTopics(temp);
										datas=temp;
										showTopicsAdapter.notifyDataSetChanged();
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
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
							if(datas.size()>0){
								lastImage_id=datas.get(datas.size()-1).img_id;
							}else{
								lastImage_id=-1;
							}
							UserImagesJson json1=null;
//						    if(Constants.user!=null&&data.master_id==Constants.user.userId){
						    	json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
						    /*}else{
						    	json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
						    }*/
							final UserImagesJson json=json1;
							if(json!=null){
								if(json!=null&&json.petPictures.size()>0){
									PetPicture data=null;
									for(int i=0;i<json.petPictures.size();i++){
										if(!datas.contains(json.petPictures.get(i))){
											data=json.petPictures.get(i);
											data.animal=KingdomImages.this.data;
											datas.add(data);
										}
									}
									
									
								}else{
//									ptram.onMoreFinish();
								}
							}else{
								//TODO 联网下载失败
//								ptram.onMoreFinish();
							}
							context.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//TODO
									if(temp!=null&&temp.size()>0){
										showTopicsAdapter.updateTopics(datas);
										showTopicsAdapter.notifyDataSetChanged();
									}
									ptram.onLoadMoreComplete();
									
								}
							});
							
						}
					}).start();
				}
			});
	}
	public void refresh(){
		final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
		lastImage_id=-1;
		UserImagesJson json1=null;
		json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
	    
		final UserImagesJson json=json1;
		if(json!=null){
			if(json.petPictures!=null&&json.petPictures.size()>0){
				if(datas.size()>0){
					if(datas.get(0).img_id==json.petPictures.get(0).img_id){
						//下载获得的图片列表与现在界面显示的完全相同，则不进行界面更新
						return;
					}
				}
				for(int i=0;i<datas.size();i++){
					temp.add(datas.get(i));
				}
				PetPicture data=null;
				for(int i=0;i<json.petPictures.size();i++){
					if(!temp.contains(json.petPictures.get(i))){
						data=json.petPictures.get(i);
						data.animal=KingdomImages.this.data;
						temp.add(i,data);
					}
					
					
				}
				
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//TODO
						showTopicsAdapter.updateTopics(temp);
						datas=temp;
						showTopicsAdapter.notifyDataSetChanged();
					}
				});
				
			}else{
			}
		}else{
			//TODO 联网下载失败
		}
	}
	@Override
	public void onRefresh() {
			// TODO Auto-generated method stub
		final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
		lastImage_id=-1;
		UserImagesJson json1=null;
		json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
	    
		final UserImagesJson json=json1;
		if(json!=null){
			if(json.petPictures!=null&&json.petPictures.size()>0){
				if(datas.size()>0){
					if(datas.get(0).img_id==json.petPictures.get(0).img_id){
						//下载获得的图片列表与现在界面显示的完全相同，则不进行界面更新
//						ptram.onRefreshFinish();
						return;
					}
				}
				for(int i=0;i<datas.size();i++){
					temp.add(datas.get(i));
				}
				PetPicture data=null;
				for(int i=0;i<json.petPictures.size();i++){
					if(!temp.contains(json.petPictures.get(i))){
						data=json.petPictures.get(i);
						data.animal=KingdomImages.this.data;
						temp.add(i,data);
					}
					
					
				}
				
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//TODO
						showTopicsAdapter.updateTopics(temp);
						datas=temp;
						showTopicsAdapter.notifyDataSetChanged();
//						ptram.onRefreshFinish();
					}
				});
				
			}else{
//				ptram.onRefreshFinish();
			}
		}else{
			//TODO 联网下载失败
//			ptram.onRefreshFinish();
		}
	}
	@Override
	public void onMore() {
			// TODO Auto-generated method stub
		final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
		if(datas.size()>0){
			lastImage_id=datas.get(datas.size()-1).img_id;
		}else{
			lastImage_id=-1;
		}
		UserImagesJson json1=null;
//	    if(Constants.user!=null&&data.master_id==Constants.user.userId){
	    	json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
	    /*}else{
	    	json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
	    }*/
		final UserImagesJson json=json1;
		if(json!=null){
			if(json!=null&&json.petPictures.size()>0){
				PetPicture data=null;
				for(int i=0;i<json.petPictures.size();i++){
					if(!datas.contains(json.petPictures.get(i))){
						data=json.petPictures.get(i);
						data.animal=KingdomImages.this.data;
						datas.add(data);
					}
				}
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//TODO
						showTopicsAdapter.updateTopics(datas);
						showTopicsAdapter.notifyDataSetChanged();
//						ptram.onMoreFinish();
					}
				});
				
			}else{
//				ptram.onMoreFinish();
			}
		}else{
			//TODO 联网下载失败
//			ptram.onMoreFinish();
		}
	}
	public View getView(){
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(params);
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
					if(ptram.getFirstVisiblePosition()==0&&ptram.getChildAt(0)!=null/*&&pictureListView.getChildAt(0).getBottom()==pictureListView.getChildAt(0).getHeight()*/){
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

}
