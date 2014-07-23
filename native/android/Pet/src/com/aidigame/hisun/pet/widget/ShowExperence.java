package com.aidigame.hisun.pet.widget;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.view.ExpView;

public class ShowExperence {
	
	Activity activity;
	LinearLayout parent;
	View showExperience;
    ExpView exp_1_1,exp_2_1,exp_3_1,exp_4_1,exp_5_1,exp_1_2,exp_2_2,exp_3_2,exp_4_2,exp_5_2;
    ImageView level_1,level_2,level_3,level_4,level_5,level_6;
    ImageView ball1,ball2,ball3,ball4,ball5,ball6;
    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    int total=0;
    int exp=0;
    int level=0;
    int classs=0;
    
    Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(level<=0)return;
    		    for(int i=1;i<=level;i++){
    		    	switch (i) {
					case 1:
						if(classs<200){
							level_1.setImageResource(R.drawable.level_1_normal);
							
							
						}else if(classs<300){
							level_1.setImageResource(R.drawable.level_1_dog_normal);
						}else{
							level_1.setImageResource(R.drawable.level_1_other_normal);
						}
						ball1.setImageResource(R.drawable.ball_red);;
						break;
					case 2:
						exp_1_1.setProgress(75,75,activity);
						
						exp_1_2.setProgress(75, 75,activity);
						ball2.setImageResource(R.drawable.ball_red);
						if(classs<200){
							level_2.setImageResource(R.drawable.level_2_normal);
							
						}else if(classs<300){
							level_2.setImageResource(R.drawable.level_2_dog_normal);
						}else{
							level_2.setImageResource(R.drawable.level_2_other_normal);
						}
						
						break;
					case 3:
						ball3.setImageResource(R.drawable.ball_red);
						exp_2_1.setProgress(75,75,activity);
						
						exp_2_2.setProgress(75, 75,activity);
						if(classs<200){
							level_3.setImageResource(R.drawable.level_3_normal);
							
						}else if(classs<300){
							level_3.setImageResource(R.drawable.level_3_dog_normal);
						}else{
							level_3.setImageResource(R.drawable.level_3_other_normal);
						}
						
						break;
					case 4:
						ball4.setImageResource(R.drawable.ball_red);
						exp_3_1.setProgress(75,75,activity);
						
						exp_3_2.setProgress(75, 75,activity);
						if(classs<200){
							level_4.setImageResource(R.drawable.level_4_normal);
							
						}else if(classs<300){
							level_4.setImageResource(R.drawable.level_4_dog_normal);
						}else{
							level_4.setImageResource(R.drawable.level_4_other_normal);
						}
						
						break;
					case 5:
						ball5.setImageResource(R.drawable.ball_red);
						exp_4_1.setProgress(75,75,activity);
						
						exp_4_2.setProgress(75, 75,activity);
						if(classs<200){
							level_5.setImageResource(R.drawable.level_5_normal);
							
						}else if(classs<300){
							level_5.setImageResource(R.drawable.level_5_dog_normal);
						}else{
							level_5.setImageResource(R.drawable.level_5_other_normal);
						}
						
						break;
					case 6:
						ball6.setImageResource(R.drawable.ball_red);
						exp_5_1.setProgress(75,75,activity);
						
						exp_5_2.setProgress(75, 75,activity);
						if(classs<200){
							level_6.setImageResource(R.drawable.level_6_normal);
							
						}else if(classs<300){
							level_6.setImageResource(R.drawable.level_6_dog_normal);
						}else{
							level_6.setImageResource(R.drawable.level_6_other_normal);
						}
						
						break;
						
					}
    		    }
    		    exp=exp<0?0:exp;
    		    switch (level) {
				case 1:
					int max=Constants.LEVEL_2-Constants.LEVEL_1;
					if(exp<=(max/2)){
    					exp_1_1.setProgress(exp, max,activity);
    				}else{
    					exp_1_1.setProgress(75,75,activity);
    					exp_1_2.setProgress(exp-(max/2), max,activity);
    				}
					break;
				case 2:
					max=Constants.LEVEL_3-Constants.LEVEL_2;
					if(exp<=(max/2)){
    					exp_2_1.setProgress(exp, max,activity);
    				}else{
    					exp_2_1.setProgress(75,75,activity);
    					exp_2_2.setProgress(exp-(max/2), max,activity);
    				}
					break;
				case 3:
					max=Constants.LEVEL_4-Constants.LEVEL_3;
					if(exp<=(max/2)){
    					exp_3_1.setProgress(exp, max,activity);
    				}else{
    					exp_3_1.setProgress(75,75,activity);
    					exp_3_2.setProgress(exp-(max/2), max,activity);
    				}
					break;
				case 4:
					max=Constants.LEVEL_5-Constants.LEVEL_4;
					if(exp<=(max/2)){
    					exp_4_1.setProgress(exp, max,activity);
    				}else{
    					exp_4_1.setProgress(75,75,activity);
    					exp_4_2.setProgress(exp-(max/2), max,activity);
    				}
					break;
				case 5:
					max=Constants.LEVEL_6-Constants.LEVEL_5;
					if(exp<=(max/2)){
    					exp_5_1.setProgress(exp, max,activity);
    				}else{
    					exp_5_1.setProgress(75,75,activity);
    					exp_5_2.setProgress(exp-(max/2), max,activity);
    				}
					break;
				case 6:
					
					break;
					
				}
    				
    	};
    };
	public ShowExperence(Activity activity,LinearLayout parent){
		this.activity=activity;
		this.parent=parent;
		level=Constants.user.lv;
		level=0;
		exp=Constants.user.exp;
		total=exp;
//		exp=900;
//		for(int i=0;i<level;i++){
		if(exp>Constants.LEVEL_6){
			level=6;
			exp-=Constants.LEVEL_6;
		}else if(exp>Constants.LEVEL_5){
			level=5;
			exp-=Constants.LEVEL_5;
		}else if(exp>Constants.LEVEL_4){
			level=4;
			exp-=Constants.LEVEL_4;
		}else if(exp>Constants.LEVEL_3){
			level=3;
			exp-=Constants.LEVEL_3;
		}else if(exp>Constants.LEVEL_2){
			level=2;
			exp-=Constants.LEVEL_2;
		}else if(exp>Constants.LEVEL_1){
			level=1;
			exp-=Constants.LEVEL_1;
		}else {
			level=0;
		}
			/*switch (level) {
			case 0:
				break;
			case 1:
				exp-=Constants.LEVEL_1;
				break;
			case 2:
				exp-=Constants.LEVEL_2;
				break;
			case 3:
				exp-=Constants.LEVEL_3;
				break;
			case 4:
				exp-=Constants.LEVEL_4;
				break;
			case 5:
				exp-=Constants.LEVEL_5;
				break;
			}*/
//		}

		initView();
		initListener();
	}
	private void initListener() {
		// TODO Auto-generated method stub
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		classs=Integer.parseInt(Constants.user.race);
		if(classs<200){
			showExperience=LayoutInflater.from(activity).inflate(R.layout.widget_user_experence_cat, null);
			
		}else if(classs<300){
			showExperience=LayoutInflater.from(activity).inflate(R.layout.widget_user_experence_dog, null);
		}else{
			showExperience=LayoutInflater.from(activity).inflate(R.layout.widget_user_experence_other, null);
		}
		parent.removeAllViews();
		parent.addView(showExperience);
		exp_1_1=(ExpView)showExperience.findViewById(R.id.exp_1_1);
		exp_2_1=(ExpView)showExperience.findViewById(R.id.exp_2_1);
		exp_3_1=(ExpView)showExperience.findViewById(R.id.exp_3_1);
		exp_4_1=(ExpView)showExperience.findViewById(R.id.exp_4_1);
		exp_5_1=(ExpView)showExperience.findViewById(R.id.exp_5_1);
		exp_1_2=(ExpView)showExperience.findViewById(R.id.exp_1_2);
		exp_2_2=(ExpView)showExperience.findViewById(R.id.exp_2_2);
		exp_3_2=(ExpView)showExperience.findViewById(R.id.exp_3_2);
		exp_4_2=(ExpView)showExperience.findViewById(R.id.exp_4_2);
		exp_5_2=(ExpView)showExperience.findViewById(R.id.exp_5_2);
		level_1=(ImageView)showExperience.findViewById(R.id.level_1);
		level_2=(ImageView)showExperience.findViewById(R.id.level_2);
		level_3=(ImageView)showExperience.findViewById(R.id.level_3);
		level_4=(ImageView)showExperience.findViewById(R.id.level_4);
		level_5=(ImageView)showExperience.findViewById(R.id.level_5);
		level_6=(ImageView)showExperience.findViewById(R.id.level_6);
		ball1=(ImageView)showExperience.findViewById(R.id.ball_1);
		ball2=(ImageView)showExperience.findViewById(R.id.ball_2);
		ball3=(ImageView)showExperience.findViewById(R.id.ball_3);
		ball4=(ImageView)showExperience.findViewById(R.id.ball_4);
		ball5=(ImageView)showExperience.findViewById(R.id.ball_5);
		ball6=(ImageView)showExperience.findViewById(R.id.ball_6);
		tv1=(TextView)showExperience.findViewById(R.id.imageView11);
		tv2=(TextView)showExperience.findViewById(R.id.imageView12);
		tv3=(TextView)showExperience.findViewById(R.id.imageView13);
		tv4=(TextView)showExperience.findViewById(R.id.imageView14);
		tv5=(TextView)showExperience.findViewById(R.id.imageView15);
		tv6=(TextView)showExperience.findViewById(R.id.imageView16);
		tv1.setText(""+Constants.LEVEL_1);
		tv2.setText(""+Constants.LEVEL_2);
		tv3.setText(""+Constants.LEVEL_3);
		tv4.setText(""+Constants.LEVEL_4);
		tv5.setText(""+Constants.LEVEL_5);
		tv6.setText(""+Constants.LEVEL_6);
		tv1.setBackgroundResource(R.drawable.claw_gray);
		handler.sendEmptyMessage(1);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		setClawColor();
	}
	public void setClawColor(){
		TextView[] tvs=new TextView[]{tv1,tv2,tv3,tv4,tv5,tv6};
		int[] nums=new int[]{Constants.LEVEL_1,Constants.LEVEL_2,Constants.LEVEL_3,Constants.LEVEL_4,Constants.LEVEL_5,Constants.LEVEL_6};
		/*if(level==0){
			tv1.setText(""+total);
			tv1.setTextColor(Color.BLACK);
			tv1.setBackgroundResource(R.drawable.claw_red);
			return;
		}*/
		for(int i=1;i<=level;i++){
			if(i==level){
				/*tvs[i-1].setText(""+total);
				tvs[i-1].setTextColor(Color.BLACK);*/
				tvs[i-1].setBackgroundResource(R.drawable.claw_red);
			}else{
				/*tvs[i-1].setText(""+nums[i-1]);
				tvs[i-1].setTextColor(Color.GRAY);*/
				tvs[i-1].setBackgroundResource(R.drawable.claw_red);
			}
		}
	}

}
