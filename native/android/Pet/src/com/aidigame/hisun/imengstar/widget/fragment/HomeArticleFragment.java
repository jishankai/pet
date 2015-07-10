package com.aidigame.hisun.imengstar.widget.fragment;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import me.maxwin.view.XListViewHeader;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.adapter.HomeArticleListAdapter;
import com.aidigame.hisun.imengstar.bean.Article;
import com.aidigame.hisun.imengstar.bean.Gift;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.bean.Article.ArticleComment;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.ui.ArticleActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.R;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;

public class HomeArticleFragment extends Fragment implements IXListViewListener{
	private View view;
	private XListView xListView;
	private Handler handler;
    ArrayList<Article> articleList;
    private HomeArticleListAdapter homeArticleListAdapter;
    int page=0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.widget_home_pet_pictures, null);
		initView();
		return view;
	}
	private void initView() {
		// TODO Auto-generated method stub
		handler=HandleHttpConnectionException.getInstance().getHandler(getActivity());
		xListView=(XListView)view.findViewById(R.id.listview);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		articleList=new ArrayList<Article>();
		homeArticleListAdapter=new HomeArticleListAdapter(getActivity(), articleList);
	    xListView.setAdapter(homeArticleListAdapter);
	    xListView.setXListViewListener(this);
		pullRefresh();
		/*xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				LogUtil.i("me", "position="+position);
			
				
			}
		});*/
	}
	boolean isRefresh=false;
    public void pullRefresh(){
    	if(isRefresh)return;
    	if(xListView!=null){
    	xListView.updateHeaderHeight(xListView.mHeaderViewHeight);
    	xListView.mHeaderView.setVisibility(View.VISIBLE);
    	xListView.mPullRefreshing = true;
    	xListView.mEnablePullRefresh=true;
    	xListView.mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
			if (xListView.mListViewListener != null) {
				isRefresh=true;
				xListView.mListViewListener.onRefresh();
			}
			xListView.resetHeaderHeight();
    	}
    }
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		page=0;
		articleList=new ArrayList<Article>();
		homeArticleListAdapter.update(articleList);
		homeArticleListAdapter.notifyDataSetChanged();
new Thread(new Runnable() {
	ArrayList<Article> temp=new ArrayList<Article>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				temp=HttpUtil.getArticleList(handler, page, getActivity());
				    if(getActivity()!=null)
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(temp!=null/*&&temp.size()>articleList.size()*/){
								articleList=temp;
								homeArticleListAdapter.update(temp);
								homeArticleListAdapter.notifyDataSetChanged();
							}
							isRefresh=false;
							xListView.stopRefresh();
						}
					});
			}
		}).start();
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if(articleList.size()>0){
			page++;
		}else{
			
		}
		
		new Thread(new Runnable() {
			ArrayList<Article> temp=new ArrayList<Article>();
					@Override
					public void run() {
						// TODO Auto-generated method stub
						temp=HttpUtil.getArticleList(handler, page, getActivity());
						 if(getActivity()!=null)
							getActivity().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(temp!=null&&temp.size()>0){
										for(int i=0;i<temp.size();i++){
											articleList.add(temp.get(i));
										}
										homeArticleListAdapter.update(articleList);
										homeArticleListAdapter.notifyDataSetChanged();
									}
									
									xListView.stopRefresh();
								}
							});
					}
				}).start();
		xListView.stopLoadMore();;
	}

}
