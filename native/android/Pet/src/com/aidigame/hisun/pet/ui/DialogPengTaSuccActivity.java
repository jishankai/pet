package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.fragment.MenuFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 提示 ，登陆获取金币，升级，升值等,捧TA成功
 * @author admin
 *
 */
public class DialogPengTaSuccActivity extends Activity{
//	View view;
//	Context context;
	int mode;
	TextView goTV;
	public static int gold=0;
	DialogNoteGoListener dialogNoteGoListener;
	int num;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mode=getIntent().getIntExtra("mode", 1);
		num=getIntent().getIntExtra("num", 0);
		
		if(mode==4){
			/*
			 * 用户做了某种操作，经验增加
			 */
		    setContentView(R.layout.dialog_exp_gold_contribute_add);
		    View view=findViewById(R.id.layout);
		    ImageView iv=(ImageView)findViewById(R.id.imageView1);
		    TextView tv=(TextView)findViewById(R.id.textView1);
		    iv.setImageResource(R.drawable.exp_star);
		    tv.setText("+ "+num);
		    startShowAnimation(view);
		    
			return;
		}else if(mode==5){
			/*
			 * 用户做了某种操作，贡献增加
			 */
		    setContentView(R.layout.dialog_exp_gold_contribute_add);
		    View view=findViewById(R.id.layout);
		    ImageView iv=(ImageView)findViewById(R.id.imageView1);
		    TextView tv=(TextView)findViewById(R.id.textView1);
		    iv.setImageResource(R.drawable.contribute_heart);
		    tv.setText("+ "+num);
		    startShowAnimation(view);
			return;
		}else if(mode==6){
			/*
			 * 用户做了某种操作，金币增加
			 */
		    setContentView(R.layout.dialog_exp_gold_contribute_add);
		    View view=findViewById(R.id.layout);
		    ImageView iv=(ImageView)findViewById(R.id.imageView1);
		    TextView tv=(TextView)findViewById(R.id.textView1);
		    iv.setImageResource(R.drawable.gold1);
		    tv.setText("+ "+num);
		    Constants.user.coinCount+=num;
		    startShowAnimation(view);
		    if(MenuFragment.menuFragment!=null){
				MenuFragment.menuFragment.setViews();
			}
			return;
		}else if(mode==7){
			/*
			 * 捧TA成功
			 */
			setContentView(R.layout.dialog_user_peng_ta_success);
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds=false;
			options.inSampleSize=4;
			options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			DisplayImageOptions displayImageOptions=new DisplayImageOptions
		            .Builder()
		            .showImageOnLoading(R.drawable.user_icon)
			        .cacheInMemory(true)
			        .cacheOnDisc(true)
			        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
			        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			        .decodingOptions(options)
	                .build();
			RoundImageView view=(RoundImageView)findViewById(R.id.icon_circleView);
			
			TextView animalNameTv=(TextView)findViewById(R.id.textView3);
			TextView percentTv=(TextView)findViewById(R.id.percent_tv);
			Animal animal=(Animal)getIntent().getSerializableExtra("animal");
			if(animal!=null){
				animalNameTv.setText(animal.pet_nickName+"");
				percentTv.setText(""+animal.percent+"%");
				ImageLoader imageLoader=ImageLoader.getInstance();
				imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl, view,displayImageOptions);
			}
			
		}
		
		
		
		
		if(mode==1){
			/*
			 * 登录获取金币
			 */
			setContentView(R.layout.dialog_login_reward);
			TextView numTv=(TextView)findViewById(R.id.textView2);
			TextView tv5=(TextView)findViewById(R.id.textView5);
			/*if(Constants.user.con_login==1){
				num=5;
			}else if(Constants.user.con_login>=2&&Constants.user.con_login<=6){
				num=Constants.user.con_login*4;
			}else if(Constants.user.con_login>=7){
				num=30;
			}*/
			numTv.setText("+"+num);
			
			tv5.setText("您已经连续来了1天，明日将受到"+Constants.user.next_gold);
		}else if(mode==2){
			/*
			 * 经验升级
			 */
			setContentView(R.layout.dialog_user_experience_upgrade);
			TextView numTv=(TextView)findViewById(R.id.textView2);
			TextView tv5=(TextView)findViewById(R.id.textView5);
			Constants.user.lv++;
            numTv.setText(""+Constants.user.lv);
			
		
			tv5.setText("+"+num);
			Constants.user.coinCount=Constants.user.coinCount+num;
		}else if(mode==3){
			/*
			 * 升值
			 */
			setContentView(R.layout.dialog_user_job_upgrade);
			TextView numTv=(TextView)findViewById(R.id.textView3);
			TextView tv5=(TextView)findViewById(R.id.textView5);
			String[] str=StringUtil.getUserJobs();
			int rank=getIntent().getIntExtra("rank", 1);
			if(num<str.length){
				numTv.setText(str[rank]);
			}else{
				numTv.setText(str[1]);
			}
			Constants.user.coinCount=Constants.user.coinCount+num;
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds=false;
			options.inSampleSize=4;
			options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			DisplayImageOptions displayImageOptions=new DisplayImageOptions
		            .Builder()
		            .showImageOnLoading(R.drawable.user_icon)
			        .cacheInMemory(true)
			        .cacheOnDisc(true)
			        .bitmapConfig(Bitmap.Config.RGB_565)//毛玻璃处理，必须使用RGB_565
			        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			        .decodingOptions(options)
	                .build();
			RoundImageView view=(RoundImageView)findViewById(R.id.icon_circleView);
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+Constants.user.u_iconUrl, view,displayImageOptions,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					LogUtil.i("me", "头像下载开始"+imageUri);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					LogUtil.i("me", "头像下载失败"+imageUri);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					LogUtil.i("me", "头像下载结束"+imageUri);
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					LogUtil.i("me", "头像下载取消"+imageUri);
				}
			});
//            numTv.setText("凉粉");
			
			tv5.setText("+"+num);
		}
		goTV=(TextView)findViewById(R.id.button1);
		
		goTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				if(dialogNoteGoListener!=null)
				dialogNoteGoListener.todo();
			}
		});
		if(MenuFragment.menuFragment!=null){
			MenuFragment.menuFragment.setViews();
		}
	}
	public void setDialogGoListerer(DialogNoteGoListener listener){
		dialogNoteGoListener=listener;
	}
	
	public static interface DialogNoteGoListener{
		void todo();
	} 
	/**
	 * 动画，显示并隐藏
	 */
	public void startShowAnimation(View view){
		AlphaAnimation alphaAnimation=new AlphaAnimation(0, 1f);
		alphaAnimation.setDuration(500);
		alphaAnimation.setFillAfter(true);
		alphaAnimation.setRepeatCount(1);
		alphaAnimation.setRepeatMode(Animation.REVERSE);
		view.clearAnimation();
		
		alphaAnimation.setAnimationListener(new AnimationListener() {
			
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
				finish();
			}
		});
		view.startAnimation(alphaAnimation);
	}
	   @Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }

}
