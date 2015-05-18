package com.aidigame.hisun.imengstar.widget.fragment;

import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.widget.PLAWaterfull;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
/**
 * 主界面中  最新Tab 
 * @author admin
 *
 */
@SuppressLint("ValidFragment") public class LatestPictureFragment extends Fragment {
	public PLAWaterfull plaWaterfull;
	public HomeSeePictureFragment homeSeePictureFragment;
	public LatestPictureFragment(HomeSeePictureFragment homeSeePictureFragment){
		super();
		this.homeSeePictureFragment=homeSeePictureFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		plaWaterfull=new PLAWaterfull(getActivity(),(LinearLayout) container,3,homeSeePictureFragment );
		
//		plaWaterfull.getXListView().isForScrollView=true;
		return plaWaterfull.getView();
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	public void pullRefresh(){
		if(plaWaterfull!=null){
			plaWaterfull.pullRefresh();
		}
	}

}
