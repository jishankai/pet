package com.aidigame.hisun.pet.ui;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.ActivityJson;
import com.aidigame.hisun.pet.http.json.ActivityJson.Reward;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;

public class AwardDetailActivity extends Activity {
	ActivityJson.Data data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);;
		setContentView(R.layout.activity_award_detail);
		data=(ActivityJson.Data)getIntent().getSerializableExtra("data");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean flag=HttpUtil.loadRewardInfo(data);
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

}
