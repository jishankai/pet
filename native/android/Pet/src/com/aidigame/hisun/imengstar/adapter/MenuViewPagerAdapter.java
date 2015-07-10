package com.aidigame.hisun.imengstar.adapter;

import java.util.ArrayList;

import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.R;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
/**
 * 侧边栏Menu 王国头像列表
 * @author admin
 *
 */
public class MenuViewPagerAdapter extends PagerAdapter {
    ArrayList<View> list;
    boolean isFirst=true;
    boolean isRepeat=false;
    public MenuViewPagerAdapter(ArrayList<View> list){
    	this.list=list;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(list.get(position%list.size()));
	}
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return super.getPageTitle(position%list.size());
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		
		container.addView(list.get(position%list.size()),0);
		RoundImageView view=(RoundImageView)list.get(position%list.size()).findViewById(R.id.roundImage_one_border);
        if(position==0&&isFirst){
        	view.setmBorderThickness(5,0xd0fb6137);
		}else{
			view.clearAtrribute();;
		}
		
		return list.get(position%list.size());
	}
	

}
