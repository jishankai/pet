package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.UserFocusAdapter;
import com.aidigame.hisun.pet.adapter.UserKingdomListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.LinearLayoutForListView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;
import com.miloisbadboy.view.PullToRefreshView;
/**
 * 用户关注的王国列表
 * @author admin
 *
 */
public class UserFocus {
//   PullToRefreshAndMoreView ptram;
   View view;
   UserDossierActivity context;
   LinearLayoutForListView focusListView;
   UserFocusAdapter focusAdapter;
   ArrayList<Animal> userDatas;
   HandleHttpConnectionException handleHttpConnectionException;
   long last_focus_id=-1;
   User user;
   public UserFocus(UserDossierActivity context,User user){
	 view=LayoutInflater.from(context).inflate(R.layout.widget_llistview1, null);

	  focusListView=(LinearLayoutForListView)view.findViewById(R.id.listView1);
	 this.context=context;
	   this.user=user;
	   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
	   initView();
   }

   private void initView() {
	// TODO Auto-generated method stub
		focusListView.setBackgroundColor(context.getResources().getColor(R.color.dossier_tab_color));

		userDatas=new ArrayList<Animal>();
		focusAdapter=new UserFocusAdapter(context, userDatas, 1,null,user);
		focusListView.setAdapter(focusAdapter);
		last_focus_id=-1;
		new Thread(new Runnable() {
			ArrayList<Animal> temp=new ArrayList<Animal>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> josn=HttpUtil.followList(null, last_focus_id,user.userId,context);
				LogUtil.i("scroll", "下载关注列表，结果返回");
				if(josn!=null){
					if(josn.size()>0){
						Animal data=null;
						for(int i=0;i<josn.size();i++){
							data=josn.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
						context.runOnUiThread(new Runnable() {
							public void run() {
								userDatas=temp;
								focusAdapter=new UserFocusAdapter(context, userDatas, 1,null,user);
								focusListView.setAdapter(focusAdapter);
								LogUtil.i("scroll", "下载关注列表，adapter更新");
							}
						});
					}
				}
			}
		}).start();
//		ptram.setHeaderAndFooterInvisible();
   }
	public void onRefresh(final PullToRefreshView pullToRefreshView) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> temp=new ArrayList<Animal>();
				last_focus_id=-1;
				final ArrayList<Animal> josn=HttpUtil.followList(null, last_focus_id,user.userId,context);
				if(josn!=null){
					if(josn.size()>0){
						
						Animal data=null;
						for(int i=0;i<josn.size();i++){
							data=josn.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
					}
				}
				context.runOnUiThread(new Runnable() {
					public void run() {
						if(temp.size()>0){
							userDatas=temp;
							focusAdapter=new UserFocusAdapter(context, userDatas, 1,null,user);
							focusListView.setAdapter(focusAdapter);
						}
						if(pullToRefreshView!=null){
							pullToRefreshView.onHeaderRefreshComplete();
						}
					}
				});
			}
		}).start();
		
	}
	public void onMore(final  PullToRefreshView pullToRefreshView) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> temp=new ArrayList<Animal>();
				if(userDatas.size()>0){
					last_focus_id=userDatas.get(userDatas.size()-1).a_id;
				}else{
					last_focus_id=-1;
				}
				final ArrayList<Animal> josn=HttpUtil.followList(null, last_focus_id,1,context);
				if(josn!=null){
					if(josn.size()>0){
						Animal data=null;
						for(int i=0;i<userDatas.size();i++){
							temp.add(userDatas.get(i));
						}
						for(int i=0;i<josn.size();i++){
							data=josn.get(i);
							if(!temp.contains(data)){
								temp.add(data);
							}
						}
					}
				}

				context.runOnUiThread(new Runnable() {
					public void run() {
						if(temp.size()>0){
							focusAdapter.updateList(temp);
							focusAdapter.notifyDataSetChanged();
							int i=userDatas.size();
							focusListView.update(i);
							userDatas=temp;
						}
						if(pullToRefreshView!=null){
							pullToRefreshView.onFooterRefreshComplete();
						}
					}
				});
			}
		}).start();
		
	}

	
	public View getView(){
		return view;
	}
	
}
