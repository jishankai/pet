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
import com.aidigame.hisun.pet.view.LinearLayoutForListView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;

import com.miloisbadboy.view.PullToRefreshView;

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
public class KingdomImages {
	LinearLayoutForListView ptram;
	   View view;
	   PetKingdomActivity context;
	   ArrayList<PetPicture> datas;
	   ShowTopicsAdapter2 showTopicsAdapter;
	   int lastImage_id=-1;
	   Animal data;
	public KingdomImages(PetKingdomActivity context,Animal data){
		   view=LayoutInflater.from(context).inflate(R.layout.widget_llistview1, null);
			  this.ptram=(LinearLayoutForListView)view.findViewById(R.id.listView1);
			   this.context=context;
			   this.data=data;
			   initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
			
			datas=new ArrayList<PetPicture>();
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
										showTopicsAdapter=new ShowTopicsAdapter2(context, datas, 1);
										ptram.setAdapter(showTopicsAdapter);
										
										LogUtil.i("scroll","datas大小========="+datas.size());
										
									}
								});
							}
						}else{
							//TODO 下载失败
						}
					}
					
				
			}).start();
	}

	public void onRefresh(final PullToRefreshView pullToRefreshView) {
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
						if(datas.size()>0){
							if(datas.get(0).img_id==json.petPictures.get(0).img_id){
								//下载获得的图片列表与现在界面显示的完全相同，则不进行界面更新
							}
						}
						/*for(int i=0;i<datas.size();i++){
							temp.add(datas.get(i));
						}*/
						PetPicture data=null;
						for(int i=0;i<json.petPictures.size();i++){
							if(!temp.contains(json.petPictures.get(i))){
								data=json.petPictures.get(i);
								data.animal=KingdomImages.this.data;
								temp.add(i,data);
							}else{
								int index=temp.indexOf(json.petPictures.get(i));
								temp.remove(index);
								temp.add(index, json.petPictures.get(i));
							}
							
							
						}
						

						
					}
				}
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//TODO
						if(temp.size()>0){
							datas=temp;
							showTopicsAdapter=new ShowTopicsAdapter2(context, datas, 1);
							ptram.setAdapter(showTopicsAdapter);
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
				for(int i=0;i<datas.size();i++){
					temp.add(datas.get(i));
				}
				UserImagesJson json1=null;
//			    if(Constants.user!=null&&data.master_id==Constants.user.userId){
			    	json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
			    /*}else{
			    	json1=HttpUtil.downloadPetkingdomImages(null, lastImage_id,0,context,data.a_id);
			    }*/
				final UserImagesJson json=json1;
				if(json!=null){
					if(json!=null&&json.petPictures.size()>0){
						PetPicture data=null;
						for(int i=0;i<json.petPictures.size();i++){
							if(!temp.contains(json.petPictures.get(i))){
								data=json.petPictures.get(i);
								data.animal=KingdomImages.this.data;
								temp.add(data);
							}
						}
					}
				}
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//TODO
						if(temp!=null&&datas.size()<temp.size()){
							showTopicsAdapter.updateTopics(temp);
							showTopicsAdapter.notifyDataSetChanged();
							int start=datas.size();
							ptram.update(start);
							datas=temp;
							
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
			return view;
	}


}
