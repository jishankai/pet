package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.UserActivityAdapter2;
import com.aidigame.hisun.pet.adapter.UserFocusAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
/**
 * 用户关注的王国列表
 * @author admin
 *
 */
public class UserActivity implements PullToRefreshAndMoreListener{
   PullToRefreshAndMoreView ptram;
   View view;
   UserDossierActivity context;
   ListView focusListView;
   UserActivityAdapter2 focusAdapter;
   ArrayList<PetPicture> userDatas;
   int last_focus_id=-1;
   User user;
   HandleHttpConnectionException handleHttpConnectionException;
   public UserActivity(UserDossierActivity context,User user){
	 view=LayoutInflater.from(context).inflate(R.layout.widget_pull_to_refresh_and_more, null);
	  this.ptram=(PullToRefreshAndMoreView)view.findViewById(R.id.listview_linearLayout3);
	   this.context=context;
	   this.user=user;
	   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
	   initView();
	   initListener();
   }

   private void initView() {
	// TODO Auto-generated method stub
		ptram.setListener(this);
		focusListView=ptram.getListView();
		focusListView.setDivider(null);
		focusListView.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));
		userDatas=new ArrayList<PetPicture>();
		focusAdapter=new UserActivityAdapter2(context, userDatas, 1);
		focusListView.setAdapter(focusAdapter);
		last_focus_id=-1;
		ptram.setHeaderAndFooterInvisible();
		new Thread(new Runnable() {
			ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<PetPicture> josn=
				HttpUtil.userActivity(context,user, -1,handleHttpConnectionException.getHandler(context));
				if(josn!=null){
					if(josn.size()>0){
						PetPicture data=null;
						for(int i=0;i<josn.size();i++){
							data=josn.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
						context.runOnUiThread(new Runnable() {
							public void run() {
								focusAdapter.updateTopics(temp);
								userDatas=temp;
								focusAdapter.notifyDataSetChanged();
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
		final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
		last_focus_id=-1;
		final ArrayList<PetPicture> josn=
		HttpUtil.userActivity(context,user, -1, handleHttpConnectionException.getHandler(context));
		if(josn!=null){
			if(josn.size()>0){
				if(userDatas.size()>0){
					if(userDatas.get(0).img_id==josn.get(0).img_id){
						ptram.onRefreshFinish();
						return;
					}
				}
				for(int i=0;i<userDatas.size();i++){
					temp.add(userDatas.get(i));
				}
				PetPicture data=null;
				for(int i=0;i<josn.size();i++){
					data=josn.get(i);
					if(!temp.contains(data)){
						temp.add(i,data);
					}
				}
				context.runOnUiThread(new Runnable() {
					public void run() {
						focusAdapter.updateTopics(temp);
						userDatas=temp;
						focusAdapter.notifyDataSetChanged();
						ptram.onRefreshFinish();
					}
				});
			}
		}
	}
	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		final ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
		if(userDatas.size()>0){
			last_focus_id=userDatas.get(userDatas.size()-1).img_id;
		}else{
			last_focus_id=-1;
		}
		final ArrayList<PetPicture> josn=
		HttpUtil.userActivity(context,user, -1, handleHttpConnectionException.getHandler(context));
		if(josn!=null){
			if(josn.size()>0){
				PetPicture data=null;
				for(int i=0;i<userDatas.size();i++){
					temp.add(userDatas.get(i));
				}
				for(int i=0;i<josn.size();i++){
					data=josn.get(i);
					if(!temp.contains(data)){
						temp.add(data);
					}
				}
				context.runOnUiThread(new Runnable() {
					public void run() {
						focusAdapter.updateTopics(temp);
						userDatas=temp;
						focusAdapter.notifyDataSetChanged();
						ptram.onMoreFinish();
					}
				});
			}
		}
	}
	boolean isShow=true;
	private void initListener() {
			// TODO Auto-generated method stub
			focusListView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(scrollState!=SCROLL_STATE_IDLE)return;
					if(focusListView.getFirstVisiblePosition()==0&&focusListView.getChildAt(0)!=null&&focusListView.getChildAt(0).getBottom()==focusListView.getChildAt(0).getHeight()){
						
						if(!isShow){
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
						if(isShow){
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
	float animHeight;
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
		TranslateAnimation anim2=new TranslateAnimation(0, 0, 0, -infoYBottom);
		anim2.setDuration(100);
		anim2.setFillAfter(false);
        anim.setAnimationListener(new AnimationListener() {
			
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
		});
		anim2.setAnimationListener(new AnimationListener() {
			
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
		});
		context.linearLayout3.clearAnimation();
		context.linearLayout3.startAnimation(anim);
		context.infoShadowView.clearAnimation();
		context.infoShadowView.startAnimation(anim2);
	}
	

	public View getView(){
		return view;
	}
	

}
