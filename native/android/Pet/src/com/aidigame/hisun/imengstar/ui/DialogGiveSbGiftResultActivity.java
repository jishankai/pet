package com.aidigame.hisun.imengstar.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.Gift;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.ImageUtil;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.widget.ShowProgress;
import com.aidigame.hisun.imengstar.widget.fragment.FourGiftBox;
import com.aidigame.hisun.imengstar.widget.fragment.FourGiftBox.SendGiftResultListener;
import com.aidigame.hisun.imengstar.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 给人送礼物，弹出框
 * @author admin
 *
 */
public class DialogGiveSbGiftResultActivity extends Activity{
	

	TextView nextTV,sureTV,rqTV,giftNameTv,animalTV,desTv;
	ImageView closeIV,giftIV;
	RoundImageView icon;
	
	public static DialogGiveSbGiftResultActivity dialogGiveSbGiftActivity;
	RelativeLayout parent,imageLayout;
	Handler handleHttpConnectionException;
	Animal animal;
	Gift gift;
	View shine_view,badview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		dialogGiveSbGiftActivity=this;
		setContentView(R.layout.activity_send_gift_result);
		MobclickAgent.onEvent(this, "gift_button");
		this.animal=(Animal)getIntent().getSerializableExtra("animal");
		gift=(Gift)getIntent().getSerializableExtra("gift");
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
//		options.inSampleSize=4;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.noimg)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		closeIV=(ImageView)findViewById(R.id.close_view);
		animalTV=(TextView)findViewById(R.id.textView1);
		giftNameTv=(TextView)findViewById(R.id.gift_name);
		giftIV=(ImageView)findViewById(R.id.gift_iv);
		rqTV=(TextView)findViewById(R.id.rq_tv);
		nextTV=(TextView)findViewById(R.id.next);
		sureTV=(TextView)findViewById(R.id.sure);
		desTv=(TextView)findViewById(R.id.textView3);
		icon=(RoundImageView)findViewById(R.id.round);
		shine_view=findViewById(R.id.shine_view);
		badview=findViewById(R.id.shine_view_bad);
		imageLayout=(RelativeLayout)findViewById(R.id.imageView1);
		parent=(RelativeLayout)findViewById(R.id.share_bitmap_layout);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance().getHandler(this);
		if(gift.add_rq>0){
			Animation anim=AnimationUtils.loadAnimation(this, R.anim.anim_rotate_repeat);
			anim.setInterpolator(new LinearInterpolator());
			shine_view.setAnimation(anim);
			anim.start();
		}else{
			
			shine_view.setVisibility(View.GONE);
			imageLayout.setBackgroundResource(R.drawable.shake_gift_background1_gray);
			giftNameTv.setBackgroundResource(R.drawable.shake_giftname_background_gray);
			RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)giftNameTv.getLayoutParams();
			if(param==null){
				
			}else{
				param.leftMargin=(int)(getResources().getDimensionPixelSize(R.dimen.waterfall_padding)*0.3f);
			}
			final Animation anim=AnimationUtils.loadAnimation(this, R.anim.anim_scale_y_repeat);
			anim.setInterpolator(new LinearInterpolator());
			anim.setFillAfter(true);
			badview.setAnimation(anim);
            anim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					handleHttpConnectionException.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							badview.startAnimation(anim);
						}
					}, 1000);
					
				}
			});
			badview.startAnimation(anim);
			
		}
		
		
		
		
		 BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		DisplayImageOptions displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		
		if(animal!=null){
			animalTV.setText("给"+animal.pet_nickName+"送个礼物");
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl, icon, displayImageOptions);
		}
		if(gift!=null){
		
			giftNameTv.setText(""+gift.name+" X 1");
			if(gift.add_rq>0){
				rqTV.setText("人气  + "+gift.add_rq);
				if(animal!=null){
					desTv.setText("您送给"+gift.animal.pet_nickName+"一个"+gift.name+"，"+gift.animal.pet_nickName+gift.effect_des);
				}
				parent.setBackgroundResource(R.drawable.shake_ground_get2);
			}else{
				parent.setBackgroundResource(R.drawable.shake_ground_get2_gray);
				rqTV.setText("人气  - "+(-gift.add_rq));
				if(animal!=null){
					desTv.setText("您对"+gift.animal.pet_nickName+"扔了一个"+gift.name+"，"+gift.animal.pet_nickName+gift.effect_des);
				}
			}
			/*try {
				giftIV.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open(""+gift.no+".png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			loadImage(giftIV, gift);
		}
		closeIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dialogGiveSbGiftActivity=null;
				if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null){
					DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.finish();;;
					
					DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity=null;
					
				}
				
				finish();
				System.gc();
			}
		});
		sureTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dialogGiveSbGiftActivity=null;
				if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null){
					DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.finish();;;
					
					DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity=null;
					
				}
				
				finish();
				System.gc();
			}
		});
		nextTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null){
					DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.setInvisible(false);;
				}
			}
		});
		
	}
	DisplayImageOptions displayImageOptions;
	public void loadImage(final ImageView icon,final Gift gift){
			
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(gift.detail_image_url, icon, displayImageOptions,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					String name="gift"+gift.no+"_";
					if(loadedImage!=null&&StringUtil.isEmpty(gift.detail_image_path)){
					   File f=new File(Constants.Picture_Root_Path+File.separator+name+".jpg");
					   if(f.exists()){
						   gift.detail_image_path=Constants.Picture_Root_Path+File.separator+name+".jpg";
					   }else{
						   String path=ImageUtil.compressImageByName(loadedImage, name);
							if(!StringUtil.isEmpty(path)){
								gift.detail_image_path=path;
							}else{
								gift.detail_image_path=ImageUtil.compressImage(loadedImage, name);
							}
					   }
					
					
					/*BitmapFactory.Options options=new BitmapFactory.Options();
					options.inSampleSize=16;
					icon.setImageBitmap(BitmapFactory.decodeFile(article.share_path, options));*/
					}
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
		}

}
