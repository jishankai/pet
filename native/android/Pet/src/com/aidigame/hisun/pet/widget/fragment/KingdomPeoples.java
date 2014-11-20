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
import com.aidigame.hisun.pet.view.LinearLayoutForListView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;

import com.miloisbadboy.view.PullToRefreshView;

/**
 * 王国人员列表
 * @author admin
 *
 */
public class KingdomPeoples {
//	PullToRefreshAndMoreView ptram;
	LinearLayoutForListView ptram;
	   View view;
	   PetKingdomActivity context;
//	   ListView listView;
	   public KingdomPeoplesAdapter adapter;
	   public ArrayList<User> datas;
	   int last_id=-1;
	   HandleHttpConnectionException handleHttpConnectionException;
	   Animal data;
	public KingdomPeoples(PetKingdomActivity context,Animal data){
		   view=LayoutInflater.from(context).inflate(R.layout.widget_llistview1, null);
			  this.ptram=(LinearLayoutForListView)view.findViewById(R.id.listView1);
			   this.context=context;
			   this.data=data;
			   handleHttpConnectionException=HandleHttpConnectionException.getInstance();
			   initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
			
			datas=new ArrayList<User>();
			adapter=new KingdomPeoplesAdapter(context, datas);
		
			ptram.setAdapter(adapter);
			loadData();
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
								datas=temp;
								adapter=new KingdomPeoplesAdapter(context, datas);
								
								ptram.setAdapter(adapter);
								
							}
						});
					}
				}
			}
				
			
		}).start();
	}

	public void onRefresh(final PullToRefreshView pullToRefreshView) {
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
						if(temp.size()>0){
							datas=temp;
							adapter=new KingdomPeoplesAdapter(context, datas);
							ptram.setAdapter(adapter);
						}
						if(pullToRefreshView!=null)
						pullToRefreshView.onHeaderRefreshComplete();
						
					}
				});
			}
				
			
		}).start();
		
	}
	public void onMore(final PullToRefreshView pullToRefreshView) {
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
							int start=datas.size();
							datas=temp;
							adapter.notifyDataSetChanged();
							ptram.update(start);
						}
						if(pullToRefreshView!=null)
						pullToRefreshView.onFooterRefreshComplete();
						
					}
				});
			}
				
			
		}).start();
		
	}
	public View getView(){
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(params);
//		ptram.setHeaderAndFooterInvisible();
			return view;
	}
	

}
