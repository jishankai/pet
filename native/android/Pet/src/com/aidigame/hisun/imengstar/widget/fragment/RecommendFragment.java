package com.aidigame.hisun.imengstar.widget.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aidigame.hisun.imengstar.R;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.Banner;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.widget.ShowProgress;

/**
 * 海选Fragment
 * @author admin
 *
 */
public class RecommendFragment extends Fragment {
	private View view;
	 private Handler handler;
	 private ViewPager viewPager;
	 private ArrayList<DataBean> dataBeans;
	 private PagerAdapter pa;
	 private ArrayList<RecommendPagerFragment> fragmentList=new ArrayList<RecommendPagerFragment>();
	 private ShowProgress sp;
	 private LinearLayout progressLayout;
	 boolean[] fragmentsUpdateFlag;
	@Override
 	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.fragment_recommend, null);
		handler=HandleHttpConnectionException.getInstance().getHandler(getActivity());
		
		initView();
		initData();
		return view;
	}
	public void initData() {
		// TODO Auto-generated method stub
		loadData();
	}
	
	public void initView() {
		// TODO Auto-generated method stub
		progressLayout=(LinearLayout)view.findViewById(R.id.progress_layout);
		sp=new ShowProgress(getActivity(), progressLayout);
		viewPager=(ViewPager)view.findViewById(R.id.viewpager);
		viewPager.setOffscreenPageLimit(0);
		dataBeans=new ArrayList<RecommendFragment.DataBean>();
		pa=new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return dataBeans.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				
//				super.destroyItem(container, position, object);
				container.removeView(fragmentList.get(position).getView());
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				if(fragmentList.size()>position){
					fragmentList.get(position).initData(dataBeans.get(position));
				}else{
					RecommendPagerFragment rpf=new RecommendPagerFragment(dataBeans.get(position), getActivity());
					fragmentList.add(position,rpf);
				}
				
				container.addView(fragmentList.get(position).getView());
				return fragmentList.get(position).getView();
			}
			@Override
			public int getItemPosition(Object object) {
				// TODO Auto-generated method stub
				return POSITION_NONE;
			}
			
		};
		
		viewPager.setAdapter(pa);
	}
	

	public void loadData(){
		new Thread(new Runnable() {
			ArrayList<DataBean> temp=new ArrayList<DataBean>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				temp=HttpUtil.starListApi(handler, getActivity());
//				HttpUtil.starImageListApi(handler, 1,0, getActivity());
					if(getActivity()!=null)
							getActivity().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(temp!=null){
									dataBeans=temp;
									fragmentsUpdateFlag=new boolean[temp.size()];
									
									pa.notifyDataSetChanged();
									}else{
										//TODO 下载失败
									}
									sp.progressCancel();
									
								}
							});
					
				}
				
			
		}).start();
	}
public void pullFresh(){
	new Thread(new Runnable() {
		ArrayList<DataBean> temp=new ArrayList<DataBean>();
		@Override
		public void run() {
			// TODO Auto-generated method stub
			temp=HttpUtil.starListApi(handler, getActivity());
//			HttpUtil.starImageListApi(handler, 1,0, getActivity());
				
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(temp!=null){
								dataBeans=temp;
								
								
								pa.notifyDataSetChanged();
								if(temp.size()>0&&fragmentList.size()>0){
									fragmentList.get(0).initData(dataBeans.get(0));
								}
								}else{
									//TODO 下载失败
								}
								sp.progressCancel();
								
							}
						});
				
			}
			
		
	}).start();
}
	


    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if(fragmentList!=null&&fragmentList.size()>0&&fragmentList.get(0)!=null){
    		fragmentList.get(0).setNumTv();
    	}
    	
    }
public static class DataBean{
		public Banner banner;
		public ArrayList<Animal> animals;
		public ArrayList<PetPicture> images;
		public long currentTime;
		public int gold;
		public int votes;
	}
	
	

}
