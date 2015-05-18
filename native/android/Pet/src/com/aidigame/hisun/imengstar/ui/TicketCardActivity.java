package com.aidigame.hisun.imengstar.ui;

import java.io.File;
import java.util.ArrayList;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.UserPetsAdapter;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.Banner;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.view.HorizontalListView2;
import com.aidigame.hisun.imengstar.widget.ShowProgress;
import com.aidigame.hisun.imengstar.widget.fragment.ShareDialogFragment;
import com.aidigame.hisun.imengstar.R;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 拉票卡片
 * @author admin
 *
 */
public class TicketCardActivity extends FragmentActivity implements OnClickListener{
	private ImageView closeIv,iconIv,sexIv,first_iv,sec_iv,third_iv;
	private TextView nameTv,myContriTv,ticketNumTv,moreTicketTv,goTicketTv,first_contri_tv,sec_contri_tv,third_contri_tv;
	private ShowProgress showProgress;
	private LinearLayout progresslayout,first_layout,sec_layout,third_layout,no_ticket_layout;
	private Handler handler;
	private View popupParent;
	private RelativeLayout black_layout,whit_frame_layout,share_layout;
	private Animal animal;
	public static TicketCardActivity userCardActivity;
	private int gold;
	private Banner banner;
	private int from;//1,照片详情
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_ticket_card);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		animal=(Animal)getIntent().getSerializableExtra("animal");
		gold=getIntent().getIntExtra("gold", 0);
		banner=(Banner)getIntent().getSerializableExtra("banner");
		userCardActivity=this;
		from=getIntent().getIntExtra("from", 0);
		initView();
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
	private void initView() {
		// TODO Auto-generated method stub
	    closeIv=(ImageView)findViewById(R.id.close_iv);	
	    iconIv=(ImageView)findViewById(R.id.user_icon);
	    sexIv=(ImageView)findViewById(R.id.sex_iv);
	    
	    goTicketTv=(TextView)findViewById(R.id.goTicketTv);
	    goTicketTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	    first_contri_tv=(TextView)findViewById(R.id.first_contri_tv);
	    first_iv=(ImageView)findViewById(R.id.first_iv);
	    first_layout=(LinearLayout)findViewById(R.id.first_layout);
	    
	    whit_frame_layout=(RelativeLayout)findViewById(R.id.white_frame);
	    
	    sec_contri_tv=(TextView)findViewById(R.id.sec_contri_tv);
	    sec_iv=(ImageView)findViewById(R.id.sec_iv);
	    sec_layout=(LinearLayout)findViewById(R.id.sec_layout);
	    
	    third_contri_tv=(TextView)findViewById(R.id.third_contri_tv);
	    third_iv=(ImageView)findViewById(R.id.third_iv);
	    third_layout=(LinearLayout)findViewById(R.id.third_layout);
	    no_ticket_layout=(LinearLayout)findViewById(R.id.no_ticket_layout);
	   
	    if(System.currentTimeMillis()/1000>banner.end_time){
	    	goTicketTv.setVisibility(View.GONE);
	    }
	    

	   
	    
	    
	    
	    popupParent=findViewById(R.id.popup_parent);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
	    
	    nameTv=(TextView)findViewById(R.id.name_tv);
	    myContriTv=(TextView)findViewById(R.id.address_tv);
	    ticketNumTv=(TextView)findViewById(R.id.gold_tv);
	    moreTicketTv=(TextView)findViewById(R.id.button_tv);
	    progresslayout=(LinearLayout)findViewById(R.id.progress_layout);
	    showProgress=new ShowProgress(this, progresslayout);
	    
	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final Animal temp=HttpUtil.ticketCardApi(TicketCardActivity.this, banner.star_id, animal, handler);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showProgress.progressCancel();
						if(temp!=null){
							TicketCardActivity.this.animal=temp;
							animal.votePicture.star_title=banner.name;
							setUserInfo(animal);
						}
					}
				});
			}
		}).start();
	    closeIv.setOnClickListener(this);
	    iconIv.setOnClickListener(this);
	    moreTicketTv.setOnClickListener(this);
	    goTicketTv.setOnClickListener(this);
	    first_iv.setOnClickListener(this);
	    sec_iv.setOnClickListener(this);
	    third_iv.setOnClickListener(this);
	}
	public void setUserInfo(final Animal animal) {
		nameTv.setText(animal.pet_nickName);
		myContriTv.setText("（你贡献了"+animal.my_votes+"票）");
		ticketNumTv.setText(""+animal.total_votes);
		if(animal.a_gender==1){
			//公
			sexIv.setImageResource(R.drawable.male1);
		}else if(animal.a_gender==2){
			//母
			sexIv.setImageResource(R.drawable.female1);
		}else{
			
		}
		
		if(animal.topVoters==null||animal.topVoters.size()==0){
			   RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)whit_frame_layout.getLayoutParams();
		    if(param!=null){
		    	param.height=param.height-getResources().getDimensionPixelOffset(R.dimen.one_dip)*36*2;
		    	whit_frame_layout.setLayoutParams(param);
		    	
		    }
		    first_layout.setVisibility(View.GONE);
		    sec_layout.setVisibility(View.GONE);
		    third_layout.setVisibility(View.GONE);
		    no_ticket_layout.setVisibility(View.VISIBLE);
		}else{
			if(animal.topVoters.size()>=1){
				first_contri_tv.setText(""+animal.topVoters.get(0).give_ticket_num);
				loadUserIcon(first_iv, animal.topVoters.get(0).u_iconUrl);
			}
			 if(animal.topVoters.size()>=2){
				 sec_contri_tv.setText(""+animal.topVoters.get(1).give_ticket_num);
					loadUserIcon(sec_iv, animal.topVoters.get(1).u_iconUrl);
				}
			if(animal.topVoters.size()>=3){
				third_contri_tv.setText(""+animal.topVoters.get(2).give_ticket_num);
				loadUserIcon(third_iv, animal.topVoters.get(2).u_iconUrl);
		    }
			
			if(animal.topVoters.size()>=3){
				
			}else if(animal.topVoters.size()>=2){
				   RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)whit_frame_layout.getLayoutParams();
				    if(param!=null){
				    	param.height=param.height-getResources().getDimensionPixelOffset(R.dimen.one_dip)*36;
				    	whit_frame_layout.setLayoutParams(param);
				    	
				    }
				    third_layout.setVisibility(View.GONE);
			}else if(animal.topVoters.size()>=1){
				   RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)whit_frame_layout.getLayoutParams();
				    if(param!=null){
				    	param.height=param.height-getResources().getDimensionPixelOffset(R.dimen.one_dip)*36*2;
				    	whit_frame_layout.setLayoutParams(param);
				    	
				    }
				    sec_layout.setVisibility(View.GONE);
				    third_layout.setVisibility(View.GONE);
			}
			
			
		}
		 
		
		BitmapFactory.Options options2=new BitmapFactory.Options();
		
		options2.inJustDecodeBounds=false;
		options2.inSampleSize=4;
		options2.inPreferredConfig=Bitmap.Config.RGB_565;
		options2.inPurgeable=true;
		options2.inInputShareable=true;
		DisplayImageOptions displayImageOptions3=new DisplayImageOptions
	            .Builder()
		.showImageOnLoading(R.drawable.pet_icon)
		.showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(false)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		        .decodingOptions(options2)
                .build();
		ImageLoader imageLoader3=ImageLoader.getInstance();
		int w=getResources().getDimensionPixelSize(R.dimen.one_dip)*72;
		LogUtil.i("mi", "头像：："+Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+animal.pet_iconUrl+"@"+w+"w_"+w+"h_0l.jpg");
		imageLoader3.displayImage(Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+animal.pet_iconUrl+"@"+w+"w_"+w+"h_0l.jpg", iconIv, displayImageOptions3);
	
	
	
	
		  ImageFetcher imageFetcher=new ImageFetcher(this, 0);
          int h=Constants.screen_height-getResources().getDimensionPixelSize(R.dimen.one_dip)*52;
          w=Constants.screen_width-getResources().getDimensionPixelSize(R.dimen.one_dip)*52;
          imageFetcher.IP=imageFetcher.UPLOAD_THUMBMAIL_IMAGE;
          imageFetcher.setImageCache(new ImageCache(this, animal.votePicture.url+"@"+w+"w_"+h+"h_"+"1l.jpg"));
          imageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
          	
				@Override
				public void onComplete(Bitmap bitmap) {
					// TODO Auto-generated method stub
					
				    
				}
				@Override
				public void getPath(String path) {
					// TODO Auto-generated method stub
					File f=new File(path);
					animal.votePicture.petPicture_path=path;
						
					
				}
				
				
			});
          
          
          
          
          imageFetcher.loadImage(animal.votePicture.url+"@"+w+"w_"+h+"h_"+"1l.jpg", new ImageView(this), /*options*/null);
			
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.close_iv:
			if(isTaskRoot()){
				if(HomeActivity.homeActivity!=null){
					Intent intent=HomeActivity.homeActivity.getIntent();
					if(intent!=null){
						this.startActivity(intent);
						finish();
						return;
					}
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
				}else{
					Intent intent=new Intent(this,HomeActivity.class);
					this.startActivity(intent);
				}
			}
			
			userCardActivity=null;
			this.finish();
			System.gc();
			break;
		case R.id.user_icon:
			if(NewPetKingdomActivity.petKingdomActivity!=null){
				if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
					NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
				}
				NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
				NewPetKingdomActivity.petKingdomActivity.finish();
				NewPetKingdomActivity.petKingdomActivity=null;
			}
			Intent intent=new Intent(this,NewPetKingdomActivity.class);
			intent.putExtra("animal",animal);
			startActivity(intent);
			break;

		case R.id.button_tv:
			Intent intent2=new Intent(this,ShareActivity.class);
			animal.votePicture.animal_totals_stars=animal.total_votes;
			animal.votePicture.end_time=banner.end_time;
			intent2.putExtra("PetPicture",animal.votePicture);
			this.startActivity(intent2);
			
			
			break;
		case R.id.goTicketTv:
			Intent in=new Intent(this,RecommendActivity.class);
			
			in.putExtra("PetPicture", animal.votePicture);
			in.putExtra("star_id", banner.star_id);
			in.putExtra("star_title", banner.name);
			in.putExtra("gold", gold);
			this.startActivity(in);
			break;
		case R.id.first_iv:
			Intent in2=new Intent(this,UserCardActivity.class);
			in2.putExtra("user",animal.topVoters.get(0));
			startActivity(in2);
			break;
		case R.id.sec_iv:
			Intent in3=new Intent(this,UserCardActivity.class);
			in3.putExtra("user",animal.topVoters.get(1));
			startActivity(in3);
			break;
		case R.id.third_iv:
			Intent in4=new Intent(this,UserCardActivity.class);
			in4.putExtra("user",animal.topVoters.get(2));
			startActivity(in4);
			break;
		}
	}
	public void loadUserIcon(ImageView iv,String url){
BitmapFactory.Options options2=new BitmapFactory.Options();
		
		options2.inJustDecodeBounds=false;
		options2.inSampleSize=4;
		options2.inPreferredConfig=Bitmap.Config.RGB_565;
		options2.inPurgeable=true;
		options2.inInputShareable=true;
		DisplayImageOptions displayImageOptions3=new DisplayImageOptions
	            .Builder()
		.showImageOnLoading(R.drawable.user_icon)
		.showImageOnFail(R.drawable.user_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(false)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		        .decodingOptions(options2)
                .build();
		ImageLoader imageLoader3=ImageLoader.getInstance();
		int w=getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
		imageLoader3.displayImage(Constants.USER_THUBMAIL_DOWNLOAD_TX+url+"@"+w+"w_"+w+"h_0l.jpg", iv, displayImageOptions3);
	}
}
