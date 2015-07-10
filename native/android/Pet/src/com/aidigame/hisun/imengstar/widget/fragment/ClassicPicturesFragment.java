package com.aidigame.hisun.imengstar.widget.fragment;

import java.util.ArrayList;

import com.aidigame.hisun.imengstar.adapter.ClassicPictureAdapter.Holder;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import me.maxwin.view.XListView.ScrollowTopListener;
import me.maxwin.view.XListViewHeader;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.ClassicPictureAdapter;
import com.aidigame.hisun.imengstar.adapter.GridPictureAdapter;
import com.aidigame.hisun.imengstar.adapter.HomeArticleListAdapter;
import com.aidigame.hisun.imengstar.adapter.ClassicPictureAdapter.ClassicPictureAdapterListener;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.Gift;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.ui.ArticleActivity;
import com.aidigame.hisun.imengstar.ui.HomeActivity;
import com.aidigame.hisun.imengstar.ui.NewShowTopicActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.widget.fragment.ShareDialogFragment.ShareDialogFragmentResultListener;
import com.aidigame.hisun.imengstar.R;
import com.huewu.pla.lib.internal.PLA_AbsListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.huewu.pla.lib.internal.PLA_ListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

@SuppressLint("ValidFragment") public class ClassicPicturesFragment extends Fragment implements IXListViewListener{
	private View view;
	private XListView xListView;
	private Handler handler;
	private ArrayList<PetPicture> pictures;
	public ClassicPictureAdapter classicPictureAdapter;
//	private GridPictureAdapter gridPictureAdapter;
	private View popupParent,black_layout;
	private int page=0;
	private HomeSeePictureFragment homeSeePictureFragment;
	private int mode=0;//2 精选，1关注；
	public ClassicPicturesFragment(){
		super();
	}
	public ClassicPicturesFragment(HomeSeePictureFragment homeSeePictureFragment,int mode){
		super();
		this.popupParent=homeSeePictureFragment.popupParent;
		this.black_layout=homeSeePictureFragment.black_layout;
		this.homeSeePictureFragment=homeSeePictureFragment;
		this.mode=mode;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		view=inflater.inflate(R.layout.widget_home_pet_pictures, null);
		view=inflater.inflate(R.layout.activity_pet_pictures2, null);
		
		initView();
		return view;
	}
	public static boolean scrollShowTab=true;
	private void initView() {
		// TODO Auto-generated method stub
		handler=HandleHttpConnectionException.getInstance().getHandler(getActivity());
		xListView=(XListView)view.findViewById(R.id.listview);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setSelector(new BitmapDrawable());
		pictures=new ArrayList<PetPicture>();
		classicPictureAdapter=new ClassicPictureAdapter(pictures, getActivity(),popupParent,black_layout,mode);
	    xListView.setAdapter(classicPictureAdapter);
		
	    classicPictureAdapter.setClassicPictureAdapterListener(new ClassicPictureAdapterListener() {
			
			@Override
			public void clickReplyComment(com.aidigame.hisun.imengstar.bean.PetPicture.Comments comments,PetPicture petPicture,Holder holder) {
				// TODO Auto-generated method stub
				homeSeePictureFragment.replyComment(comments,petPicture,holder);
			}
			
			@Override
			public void clickGift() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void clickComment(PetPicture petPicture,Holder holder) {
				// TODO Auto-generated method stub
				homeSeePictureFragment.showCommentEditor(petPicture,holder);
			}

			@Override
			public void clickMore(PetPicture picture) {
				// TODO Auto-generated method stub
				HomeActivity.homeActivity.shareLayout.setVisibility(View.VISIBLE);
				FragmentTransaction ft=HomeActivity.homeActivity.getSupportFragmentManager().beginTransaction();
				ShareDialogFragment sf=new ShareDialogFragment(picture, popupParent, black_layout,2);
				ft.replace(R.id.sharelayout, sf);
				
				sf.setShareDialogFragmentResultListener(new ShareDialogFragmentResultListener() {
					
					@Override
					public void onResult(boolean isSuccess) {
						// TODO Auto-generated method stub
						HomeActivity.homeActivity.shareLayout.setVisibility(View.GONE);
					}
				});
				ft.commit();
			}
		});
	   
	   /* gridPictureAdapter=new GridPictureAdapter(getActivity(), pictures);
	    xListView.setAdapter(gridPictureAdapter);*/
	    xListView.setXListViewListener(this);
	    handler.postAtTime(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pullRefresh();
				
//				loadData();
			}
		}, 1000);
		
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		xListView.setScrollowTopListenenr(new ScrollowTopListener() {
			Thread thread=null;
			@Override
			public void changeTopTab(float firstChildTop) {
				// TODO Auto-generated method stub
					
				
				HomeActivity.topAlpha=HomeActivity.topAlpha+firstChildTop*0.004f;
				if(HomeActivity.topAlpha>=1.0f){
					HomeActivity.topAlpha=1.0f;
				}else if(HomeActivity.topAlpha<=0){
					HomeActivity.topAlpha=0.0f;
				}
				LogUtil.i("mi", "topTabAlpha====="+HomeActivity.topAlpha);
					setAlpha(HomeActivity.topAlpha);
					/*if(HomeActivity.homeActivity!=null&&HomeActivity.homeActivity.bottomTabLayout!=null){
						HomeActivity.homeActivity.bottomTabLayout.setAlpha(topTabAlpha);
					}*/
			
					
				}

			@Override
			public void onScrollStop(final int status) {
				// TODO Auto-generated method stub
				if(status==PLA_AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
					scrollShowTab=true;
					if(thread!=null){
						try {
							thread.interrupt();
							thread=null;
						} catch (Exception e2) {
							// TODO: handle exception
						}
						
					}
					thread=new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(1500);
								if(scrollShowTab){
									handler.post(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											if(scrollShowTab){
												HomeActivity.topAlpha=1.0f;
												setAlpha(HomeActivity.topAlpha);
											}
											thread=null;
										}
									});
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					thread.start();
				}else{
					scrollShowTab=false;
					if(thread!=null){
						try {
							thread.interrupt();
							thread=null;
						} catch (Exception e2) {
							// TODO: handle exception
						}
						
					}
				}
				
                
			}
	
		});
	}
	boolean isrefreshing=false;
	public void setAlpha(float alpha){
		if(homeSeePictureFragment!=null&&homeSeePictureFragment.topTabLayout!=null){
			homeSeePictureFragment.topTabLayout.setAlpha(alpha);
			if(alpha==0){
				homeSeePictureFragment.topTabLayout.setVisibility(View.INVISIBLE);
			}else{
				homeSeePictureFragment.topTabLayout.setVisibility(View.VISIBLE);
			}
			if(HomeActivity.homeActivity!=null){
				HomeActivity.homeActivity.bottomTabLayout.setAlpha(alpha);
				if(alpha==0){
					HomeActivity.homeActivity.bottomTabLayout.setVisibility(View.INVISIBLE);
				}else{
					HomeActivity.homeActivity.bottomTabLayout.setVisibility(View.VISIBLE);
				}
			}
		}
		
	}
    public void pullRefresh(){
    	if(isrefreshing)return;
    	HomeActivity.topAlpha=1.0f;
    	setAlpha(HomeActivity.topAlpha);
    	xListView.updateHeaderHeight(xListView.mHeaderViewHeight);
    	xListView.mHeaderView.setVisibility(View.VISIBLE);
    	xListView.mPullRefreshing = true;
    	xListView.mEnablePullRefresh=true;
    	xListView.mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
			if (xListView.mListViewListener != null) {
				isrefreshing=true;
				xListView.mListViewListener.onRefresh();
			}
			xListView.resetHeaderHeight();
    }
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
		pictures=new ArrayList<PetPicture>();
		classicPictureAdapter.update(pictures);
		classicPictureAdapter.notifyDataSetChanged();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				long usr_id=0;
				page=0;
				if(PetApplication.isSuccess&&PetApplication.myUser!=null){
					usr_id=PetApplication.myUser.userId;
				}
//				final ArrayList<PetPicture> ps=HttpUtil.begFoodList(handler, page, getActivity());
				 UserImagesJson json=HttpUtil.downloadPetkingdomImages(handler,-1,mode,getActivity(),-1);
				 ArrayList<PetPicture> temp=null;
				 if(json!=null){
					 temp=json.petPictures;
				 }
				 final ArrayList<PetPicture> ps=temp;
				 if(getActivity()!=null)
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								if(ps!=null){
									pictures=new ArrayList<PetPicture>();
									for(int i=0;i<ps.size();i++){
										if(i<26){
											pictures.add(ps.get(i));
										}
										
									}
									classicPictureAdapter.update(pictures);;
									classicPictureAdapter.notifyDataSetChanged();
								}
								xListView.stopRefresh();
								isrefreshing=false;
								 DiscoveryFragment.isRefresh=false;
							}
						});
			}
		}).start();
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				page++;
				long usr_id=0;
				if(PetApplication.isSuccess&&PetApplication.myUser!=null){
					usr_id=PetApplication.myUser.userId;
				}
				long last_id=-1;
				if(pictures!=null&&pictures.size()>0){
					last_id=pictures.get(pictures.size()-1).img_id;
				}
//				final ArrayList<PetPicture> ps=HttpUtil.begFoodList(handler, page, getActivity());
				 UserImagesJson json=HttpUtil.downloadPetkingdomImages(handler,last_id,mode,getActivity(),-1);
				 ArrayList<PetPicture> temp=null;
				 if(json!=null){
					 temp=json.petPictures;
				 }
				 final ArrayList<PetPicture> ps=temp;
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(ps!=null){
							for(int i=0;i<ps.size();i++){
								if(i<26)
								pictures.add(ps.get(i));
							}
							classicPictureAdapter.update(pictures);
							classicPictureAdapter.notifyDataSetChanged();
							/*gridPictureAdapter.update(pictures);
							gridPictureAdapter.notifyDataSetChanged();*/
						}
						
						xListView.stopLoadMore();;
						
						
					}
				});
			}
		}).start();
		
	}


}
