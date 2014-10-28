package com.aidigame.hisun.pet.ui;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.ActivityJson;
import com.aidigame.hisun.pet.http.json.ActivityJson.Reward;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
/**
 * 奖品详情
 * @author admin
 *
 */
public class AwardDetailActivity extends Activity {
	LinearLayout frameLayout;
	HandleHttpConnectionException handleHttpConnectionException;
	ActivityJson.Data data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);;
		setContentView(R.layout.activity_award_detail);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		data=(ActivityJson.Data)getIntent().getSerializableExtra("data");
		setBlurImageBackground();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean flag=HttpUtil.loadRewardInfo(data,handleHttpConnectionException.getHandler(AwardDetailActivity.this),AwardDetailActivity.this);
				if(flag){
					runOnUiThread(new Runnable() {
						public void run() {
							setView();
						}
					});
					if(data.rewards!=null){
						for(ActivityJson.Reward r:data.rewards){
							if(r.img!=null&&!"".equals(r.img)){
								if(StringUtil.judgeImageExists(Constants.Picture_Topic_Path+File.separator+r.img)){
									r.imgPath=Constants.Picture_Topic_Path+File.separator+r.img;
								}else{
									String path=HttpUtil.downloadImage(Constants.ACTIVITY_IMAGE, r.img, null, AwardDetailActivity.this);
									if(path!=null){
										
										r.imgPath=path;
									}
								}
								
							}
							
						}
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								setImage();
							}
						});
					}
					
				}
			}
		}).start();
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AwardDetailActivity.this.finish();
			}
		});
	}
	public void  setView(){
		
		TextView tv1=null,tv2=null,tv3=null,tv4=null;
		tv1=(TextView)findViewById(R.id.textView3);
		tv2=(TextView)findViewById(R.id.textView4);
		tv3=(TextView)findViewById(R.id.textView5);
		
		if(data.rewards!=null){
			Reward reward=null;
			if(data.rewards.size()>0){
				reward=data.rewards.get(0);
				String name=reward.name==null?"":reward.name;
				String des=reward.des==null?"":reward.des;
				if(!"".equals(name))
				des=name+"\r\n"+des;
				if(!"".equals(des)){
					tv1.setText(des);
				}
			}
			if(data.rewards.size()>1){
				reward=data.rewards.get(1);
				String name=reward.name==null?"":reward.name;
				String des=reward.des==null?"":reward.des;
				if(!"".equals(name))
				des=name+"\r\n"+des;
				if(!"".equals(des)){
					tv2.setText(des);
				}
			}
			if(data.rewards.size()>2){
				reward=data.rewards.get(2);
				String name=reward.name==null?"":reward.name;
				String des=reward.des==null?"":reward.des;
				if(!"".equals(name))
				des=name+"\r\n"+des;
				if(!"".equals(des)){
					tv3.setText(des);
				}
			}
			
		}
		tv4=(TextView)findViewById(R.id.textView6);
		if(data.des!=null)
		tv4.setText(data.des);
		
	}
	public void setImage(){
		ImageView image1=null,image2=null,image3=null;
		image1=(ImageView)findViewById(R.id.imageView2);
		image2=(ImageView)findViewById(R.id.imageView3);
		image3=(ImageView)findViewById(R.id.imageView4);
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inSampleSize=2;
		if(data.rewards!=null){
			if(data.rewards.size()>0){
				image1.setImageBitmap(BitmapFactory.decodeFile(data.rewards.get(0).imgPath,opts));
			}
			if(data.rewards.size()>1){
				image2.setImageBitmap(BitmapFactory.decodeFile(data.rewards.get(1).imgPath,opts));
			}
			if(data.rewards.size()>2){
				image3.setImageBitmap(BitmapFactory.decodeFile(data.rewards.get(2).imgPath,opts));
			}
		}
		
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(LinearLayout)findViewById(R.id.framelayout);
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(HomeFragment.blurBitmap==null){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						frameLayout.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
						frameLayout.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
	}


}
