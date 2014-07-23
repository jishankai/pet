package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.FocusAndFansAdapter;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter2;
import com.aidigame.hisun.pet.blur.TopCenterImageView;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.service.DownloadImagesAsyncTask;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.CircleView;
import com.aidigame.hisun.pet.widget.ShowExperence;
import com.aidigame.hisun.pet.widget.ShowFocusAndFansList;
import com.aidigame.hisun.pet.widget.ShowFocusTopics1;
import com.aidigame.hisun.pet.widget.ShowProgress;

public class UserHomepageActivity extends Activity implements OnClickListener{
	ImageView imageView5,xinlang,weixin;
	TextView tv3,tv2,tv1,comming_soon,weiboName;
	TextView nameText;
	TextView pictures,focus,fans;
	public View view;//关注列表，粉丝列表的父控件
	 LinearLayout listViewLinearLayout;
	 int listViewFirstVisionPosition=0;
	RelativeLayout /*pictureRelativeLayout,focusRelativeLayout,fansLinearLayout,*/numRelativeLayout,bottomRelativeLayout3;
	LinearLayout layout;
	public RelativeLayout userInfoRelativeLayout;
	boolean show_comming_soon=true;
	ShowProgress showProgress;
	LinearLayout progressLayout;
	public ListView listView;//关注和粉丝列表
	ListView myListView;//照片列表
	public boolean isOnRefresh=false;
	ShowFocusTopics1 showFocusTopics;

	public static final int MESSAGE_SHOW_COMMING_SOON=0;
   public static UserHomepageActivity userHomepageActivity;
	public static final  int USER_INFO=1;
	//下载完一张图片
	public  static final int MESSAGE_DOWNLOAD_IMAGE=22;
	public static final int MESSAGE_SHOW_FOLLOWING=3;
	public static final int MESSAGE_SHOW_FOLLOWER=4;
	public static final int DISMISS_PROGRESS=202;
	public static final int SHOW_PROGRESS=203;
	//图片列表
	public ArrayList<UserImagesJson.Data> datas;
	public ArrayList<UserJson.Data> userDatas;
	int focusAdapterMode=0;//0 关注 1 粉丝
	public FocusAndFansAdapter focusAdapter;
	CircleView imageView3;
	String path;
	int getPictureMode=1;//1 从相机中获取，2从图库中获取
	public int lastImage_id=-1;
	public int last_focus_id=-1;
	LinearLayout bottomLinearLayout1,bottomLinearLayout2,titleLinearLayout;
	DownloadImagesAsyncTask asyncTask;
	TopCenterImageView blur_view;
	RelativeLayout allLayout;
	ShowTopicsAdapter2 showTopicsAdapter;
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_SHOW_COMMING_SOON:
				if(show_comming_soon){
					comming_soon.setVisibility(View.VISIBLE);
					show_comming_soon=false;
					handler.sendEmptyMessageDelayed(MESSAGE_SHOW_COMMING_SOON, 3000);
				}else{
					comming_soon.setVisibility(View.INVISIBLE);
					show_comming_soon=true;
				}
				break;
			case Constants.MESSAGE_DOWNLOAD_IMAGES_LIST://11
				if(datas==null){
					
				}
				UserImagesJson json=(UserImagesJson)msg.obj;
				if(json.datas!=null){
					asyncTask=new DownloadImagesAsyncTask(handler,UserHomepageActivity.this);
					UserImagesJson.Data[] arr=new UserImagesJson.Data[json.datas.size()];
					if(isOnRefresh&&myListView!=null){
						if(json.datas!=null&&json.datas.size()>0&&datas.size()>0){
							if(datas.get(0).img_id==json.datas.get(0).img_id){
								
								isOnRefresh=false;
								return;
							}else{
								datas.removeAll(datas);
							}
						}
					}
					UserImagesJson.Data data=null;
					for(int i=0;i<json.datas.size();i++){
						data=json.datas.get(i);
						data.user=Constants.user;
						datas.add(data);
						if(!StringUtil.judgeImageExists(Constants.Picture_Topic_Path+File.separator+data.url)){
							arr[i]=data;
						}else{
							data.path=Constants.Picture_Topic_Path+File.separator+data.url;
						}
					}
					LogUtil.i("me", "arr[].length="+arr.length);
					//下载图片
					asyncTask.execute(arr);
					handler.sendEmptyMessage(DISMISS_PROGRESS);
					if(isOnRefresh&&myListView!=null){
						isOnRefresh=false;
					}
					if(json.datas.size()>0)
					showTopicsAdapter.notifyDataSetChanged();
					
					
				}
				break;
			case MESSAGE_DOWNLOAD_IMAGE:
				showTopicsAdapter.notifyDataSetChanged();
				break;
			case USER_INFO:
				if(Constants.user==null){
					handler.sendEmptyMessageDelayed(USER_INFO, 100);
				}else{
					setViews();
				}
				break;
			case MESSAGE_SHOW_FOLLOWING:
				
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				UserJson userJson=(UserJson)msg.obj;
				if(userJson!=null&&userJson.datas!=null){
					if(userDatas==null){
						focus.setText(""+userJson.datas.size());
						return;
					}
					for(UserJson.Data d:userJson.datas){
						if(!userDatas.contains(d))
						userDatas.add(d);
					}
					if(last_focus_id!=-1)
					focusAdapter.notifyDataSetChanged();
					if(userDatas!=null){
						Constants.user.follow=userDatas.size();
						focus.setText(""+userDatas.size());
						focusAdapter.notifyDataSetChanged();
						 if(userJson.final_id!=0){
							 last_focus_id=userJson.final_id;
						 }
					}/*else if(focusAdapterMode==2){
						 fans.setText(""+userDatas.size());
					}*/
				}else{
					if(!getFocusNum){
						getFocusNum=true;
						return;
					}
//					if(view!=null)listViewLinearLayout.removeView(view);
//					showFocus(listViewLinearLayout);
				}
				break;
			case MESSAGE_SHOW_FOLLOWER:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				UserJson userJson1=(UserJson)msg.obj;
				if(userJson1!=null&&userJson1.datas!=null){
					if(userDatas==null){
						fans.setText(""+userJson1.datas.size());
						return;
					}
					for(UserJson.Data d:userJson1.datas){
						if(!userDatas.contains(d))
						userDatas.add(d);
					}
					if(last_focus_id!=-1)
					focusAdapter.notifyDataSetChanged();
					if(userDatas!=null){
						focus.setText(""+Constants.user.follow);
						Constants.user.follower=userDatas.size();
						 fans.setText(""+userDatas.size());
						 focusAdapter.notifyDataSetChanged();
						 if(userJson1.final_id!=0){
							 last_focus_id=userJson1.final_id;
						 }
						 
					}
				}else{
					if(!getFansNum){
						getFansNum=true;
						return;
					}
//					if(view!=null)listViewLinearLayout.removeView(view);
//					showFans(listViewLinearLayout);
				}
				 
				break;
			case DISMISS_PROGRESS:
				if(showProgress!=null)
				showProgress.progressCancel();
				if(listView!=null&&focusAdapter!=null)
					listView.setAdapter(focusAdapter);
				break;
			case SHOW_PROGRESS:
				if(showProgress==null){
					showProgress=new ShowProgress(UserHomepageActivity.this, Constants.NOTE_MESSAGE_1, progressLayout);
				}else{
					showProgress.showProgress();
				}
				
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
//		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.activity_user_homepage);
        userHomepageActivity=this;
		initView();
		initListener();
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String string=null;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(string.equals(""));
			}
		}).start();*/

	}
	private void initView() {
		// TODO Auto-generated method stub
		numRelativeLayout=(RelativeLayout)findViewById(R.id.nums_relativelayout);
		ImageView imageView1=(ImageView)findViewById(R.id.imageView1);
		ImageView imageView2=(ImageView)findViewById(R.id.imageView2);
		imageView3=(CircleView)findViewById(R.id.imageView3);
		nameText=(TextView)findViewById(R.id.textView44);
		weiboName=(TextView)findViewById(R.id.wei_bo_name);
		ImageView imageView4=(ImageView)findViewById(R.id.imageView4);
		imageView5=(ImageView)findViewById(R.id.imageView5);
		xinlang=(ImageView)findViewById(R.id.xinlang);
		weixin=(ImageView)findViewById(R.id.weinxin);
		blur_view=(TopCenterImageView)findViewById(R.id.blur_view);
		allLayout=(RelativeLayout)findViewById(R.id.all);
		bottomRelativeLayout3=(RelativeLayout)findViewById(R.id.bottom_linearlayout3);
		SharedPreferences sp=getSharedPreferences("setup",Context.MODE_WORLD_WRITEABLE);
		if(sp.getBoolean(Constants.LOCK_TO_WEIXIN, false)){
			weixin.setVisibility(View.VISIBLE);
			xinlang.setVisibility(View.INVISIBLE);
		}
		if(sp.getBoolean(Constants.LOCK_TO_XINLANG, false)){
			xinlang.setVisibility(View.VISIBLE);
			weixin.setVisibility(View.INVISIBLE);
		}
		showWeiboInfo();
		xinlang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bitmap bmp=ImageUtil.getBitmapFromView(allLayout,UserHomepageActivity.this);
				blur_view.setImageBitmap(bmp);
				blur_view.setAlpha(0.85f);//0.9342857
				Animation animation=AnimationUtils.loadAnimation(UserHomepageActivity.this, R.anim.anim_user_weibo_info);
				layout.setAnimation(animation);
				animation.start();
				
				bottomRelativeLayout3.setVisibility(View.VISIBLE);
				
				blur_view.setVisibility(View.VISIBLE);
//				XinlangShare.getXinLangInfo(UserHomepageActivity.this);
			}
		});
		sp=null;
		progressLayout=(LinearLayout)findViewById(R.id.progress_linearlayout1);
		comming_soon=(TextView)findViewById(R.id.comming_soon);
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv3=(TextView)findViewById(R.id.textView3);
		bottomLinearLayout1=(LinearLayout)findViewById(R.id.bottom_linearlayout1);
		bottomLinearLayout2=(LinearLayout)findViewById(R.id.bottom_linearlayout2);
		
//		titleLinearLayout=(LinearLayout)findViewById(R.id.linearLayout);
		userInfoRelativeLayout=(RelativeLayout)findViewById(R.id.user_info_relativelayout);
		//标题状态栏
//		CreateTitle createTitle=new CreateTitle(this, titleLinearLayout);
		if(getIntent().getIntExtra("mode", 1)==1){
			showTopics();
			
		}else if(getIntent().getIntExtra("mode", 1)==2){
			numRelativeLayout.setVisibility(View.GONE);
			showExperience();
		}if(getIntent().getIntExtra("mode", 1)==3){
			numRelativeLayout.setVisibility(View.GONE);
			userSetUp();
		}
		

		if(Constants.user==null){
			handler.sendEmptyMessageDelayed(USER_INFO, 100);
		}else{
			setViews();
		}
		imageView1.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView4.setOnClickListener(this);
		imageView3.setOnClickListener(this);
		
	}
	private void initListener() {
		// TODO Auto-generated method stub
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageView1:
			if("HomeActivity".equals(getIntent().getStringExtra("from"))){
			/*if(HomeActivity.homeActivity!=null){
//				HomeActivity.homeActivity.finish();
				this.finish();
			}else{*/
				Intent intentHome=new Intent(this,HomeActivity.class);
				this.startActivity(intentHome);
				this.finish();
//			}
			}else{
				this.finish();
			}
			break;
		case R.id.imageView2:
			handler.sendEmptyMessage(MESSAGE_SHOW_COMMING_SOON);
			break;
		case R.id.imageView4:
			changeUserIcon();
			break;
		case R.id.imageView6:
			showTopics();
			break;
		case R.id.imageView7:
			userSetUp();
			break;
		case R.id.imageView3:
			Intent intent=new Intent(this,ShowPictureActivity.class);
			intent.putExtra("path", Constants.user.iconPath);
			this.startActivity(intent);
			break;
		}
	}
	private void showExperience() {
		// TODO Auto-generated method stub
//		experienceImage.setImageResource(R.drawable.exp_red);
		ShowExperence showExperence=new ShowExperence(this, bottomLinearLayout1);
	}
	boolean isChangingUserIcon=false;
	/**
	 * 改变用户头像 
	 */
	private void changeUserIcon() {
		// TODO Auto-generated method stub
		isChangingUserIcon=true;
		Bitmap bmp=ImageUtil.getBitmapFromView(allLayout,UserHomepageActivity.this);
		blur_view.setImageBitmap(bmp);
		blur_view.setAlpha(0.85f);//0.9342857
		blur_view.setVisibility(View.VISIBLE);
		final View view=LayoutInflater.from(this).inflate(R.layout.popup_user_homepage, null);
		bottomLinearLayout2.removeAllViews();
		bottomLinearLayout2.addView(view);
		bottomLinearLayout2.setVisibility(View.VISIBLE);
		bottomLinearLayout2.setClickable(true);
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
		view.clearAnimation();
		view.setAnimation(animation);
		animation.start();
		
		TextView camera=(TextView)view.findViewById(R.id.textView1);
		TextView album=(TextView)view.findViewById(R.id.textView2);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPictureMode=1;
				Intent intent2=new Intent(UserHomepageActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
				UserHomepageActivity.this.startActivityForResult(intent2, 1);
				
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
				bottomLinearLayout2.setClickable(false);
				blur_view.setVisibility(View.INVISIBLE);
				isChangingUserIcon=false;
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPictureMode=2;
				Intent intent=new Intent(UserHomepageActivity.this,AlbumPictureBackground.class);
				intent.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
				UserHomepageActivity.this.startActivityForResult(intent, 1);
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
				bottomLinearLayout2.setClickable(false);
				blur_view.setVisibility(View.INVISIBLE);
				isChangingUserIcon=false;
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation=AnimationUtils.loadAnimation(UserHomepageActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						bottomLinearLayout2.setVisibility(View.INVISIBLE);
						bottomLinearLayout2.setClickable(false);
						blur_view.setVisibility(View.INVISIBLE);
						isChangingUserIcon=false;
					}
				}, 1000);
				
			}
		});
	
	}
	/**
	 * 用户设置
	 */
	private void userSetUp() {
		// TODO Auto-generated method stub
//		imageView7.setImageResource(R.drawable.set_red);
		View view=LayoutInflater.from(this).inflate(R.layout.item_user_setup, null);
		RelativeLayout setLayout1=(RelativeLayout)view.findViewById(R.id.set_relativelayout01);
		RelativeLayout setLayout2=(RelativeLayout)view.findViewById(R.id.set_relativelayout02);
		RelativeLayout setLayout3=(RelativeLayout)view.findViewById(R.id.set_relativelayout03);
		RelativeLayout setLayout4=(RelativeLayout)view.findViewById(R.id.set_relativelayout04);
		RelativeLayout setLayout5=(RelativeLayout)view.findViewById(R.id.set_relativelayout05);
		/*Button button1=(Button)view.findViewById(R.id.set_button1);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(Activity a:PetApplication.petApp.activityList){
					if(a!=null){
						a.finish();
					}
				}
			}
		});*/
		ImageView imageView1=(ImageView)view.findViewById(R.id.set_imageView1);
		ImageView imageView2=(ImageView)view.findViewById(R.id.set_imageView2);
		final TextView tv2=(TextView)view.findViewById(R.id.set_textView2);
		File file=new File(Constants.Picture_Root_Path);
		long length=0;
		if(file.exists()){
			length=getFileSize(file, 0);
		}
		DecimalFormat dfDecimalFormat=new DecimalFormat("##0.00");
		if((float)length/(1024*1024*1f)>1.0f){
			float f=(float)length/(1024*1024*1f);
			tv2.setText(""+dfDecimalFormat.format(f)+"MB");
		}else if((float)length/(1024*1f)>1.0f){
			float f=(float)length/(1024*1f);
			tv2.setText(""+dfDecimalFormat.format(f)+"KB");
		}else{
			tv2.setText("不足1KB");
		}
		setLayout1.setClickable(true);
		setLayout2.setClickable(true);
		setLayout3.setClickable(true);
		setLayout4.setClickable(true);
		setLayout5.setClickable(true);
		setLayout2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File file=new File(Constants.Picture_Root_Path);
				if(file.exists()){
					StringUtil.deleteFile(file);
					tv2.setText("0kb");
				}
			}
		});
		setLayout1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UserHomepageActivity.this,SetupActivity.class);
				UserHomepageActivity.this.startActivity(intent);
			}
		});
        setLayout4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UserHomepageActivity.this,AdviceActivity.class);
				UserHomepageActivity.this.startActivity(intent);
			}
		});
        setLayout3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
        setLayout5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		bottomLinearLayout1.removeAllViews();
		bottomLinearLayout1.addView(view);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences sp=getSharedPreferences("setup",Context.MODE_WORLD_WRITEABLE);
		if(sp.getBoolean(Constants.LOCK_TO_WEIXIN, false)){
			weixin.setVisibility(View.VISIBLE);
			xinlang.setVisibility(View.INVISIBLE);
		}
		if(sp.getBoolean(Constants.LOCK_TO_XINLANG, false)){
			xinlang.setVisibility(View.VISIBLE);
			weixin.setVisibility(View.INVISIBLE);
		}else{
			xinlang.setVisibility(View.INVISIBLE);
		}
	}
	boolean getFocusNum=false;
	boolean getFansNum=false;
	/**
	 * 显示 用户发表的图片,粉丝，关注的人群
	 */
	private void showTopics() {
		// TODO Auto-generated method stub
//		setMenuGray();
//		imageView6.setImageResource(R.drawable.home_red);
		getFansNum=false;
		getFocusNum=false;
		datas=new ArrayList<UserImagesJson.Data>();
		View view=LayoutInflater.from(this).inflate(R.layout.item_user_topics, null);
		bottomLinearLayout1.removeAllViews();
		bottomLinearLayout1.addView(view);
		
		RelativeLayout pictureRelativeLayout=(RelativeLayout)findViewById(R.id.picture_relativeLayout);
		RelativeLayout focusRelativeLayout=(RelativeLayout)findViewById(R.id.focus_relativeLayout);
		RelativeLayout fansLinearLayout=(RelativeLayout)findViewById(R.id.fans_relativeLayout);
		pictures=(TextView)findViewById(R.id.textView11);
		focus=(TextView)findViewById(R.id.textView33);
		fans=(TextView)findViewById(R.id.textView55);
		final TextView pictures1=(TextView)findViewById(R.id.textView22);
		final TextView focus1=(TextView)findViewById(R.id.textView44);
		final TextView fans1=(TextView)findViewById(R.id.textView66);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(Constants.user==null){
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
						pictures.setText(""+Constants.user.imagesCount);
						focus.setText(""+Constants.user.follow);
						fans.setText(""+Constants.user.follower);
					}
				});
			}
		}).start();
       new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				HttpUtil.followList(handler, last_focus_id,1,UserHomepageActivity.this);
				HttpUtil.followList(handler, last_focus_id,2,UserHomepageActivity.this);
			}
		}).start();
		listViewLinearLayout=(LinearLayout)view.findViewById(R.id.listview_linearLayout);
		showPictures(listViewLinearLayout);
		pictureRelativeLayout.setClickable(true);
		focusRelativeLayout.setClickable(true);
		fansLinearLayout.setClickable(true);
		pictureRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pictures.setTextColor(getResources().getColor(R.color.user_home_nums_checked));;
				focus.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));
				fans.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));
				pictures1.setTextColor(getResources().getColor(R.color.user_home_nums_checked));
				focus1.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));
				fans1.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));
				showPictures(listViewLinearLayout);
			}
		});
		focusRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pictures.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));;
				focus.setTextColor(getResources().getColor(R.color.orange_red));
				fans.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));
				pictures1.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));
				focus1.setTextColor(getResources().getColor(R.color.orange_red));
				fans1.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));
				showFocus(listViewLinearLayout);
			}
		});
		fansLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pictures.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));;
				focus.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));
				fans.setTextColor(getResources().getColor(R.color.user_home_nums_checked));
				pictures1.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));
				focus1.setTextColor(getResources().getColor(R.color.user_home_nums_unchecked));
				fans1.setTextColor(getResources().getColor(R.color.user_home_nums_checked));
				showFans(listViewLinearLayout);
			}
		});
		
	}
/**
 * 显示用户发表的图片
 * @param linearLayout
 */
	private void showPictures(final LinearLayout linearLayout){
//		linearLayout.removeAllViews();
//		listView=null;
//		handler.sendEmptyMessage(SHOW_PROGRESS);
		datas=new ArrayList<UserImagesJson.Data>();
		showFocusTopics=new ShowFocusTopics1(this, linearLayout,datas,1);
		showFocusTopics.addView();
		showTopicsAdapter=showFocusTopics.getAdapter();
		lastImage_id=-1;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(getIntent().getIntExtra("mode", 1)!=2){
					HttpUtil.downloadUserHomepage(handler, lastImage_id,0,UserHomepageActivity.this);
				}
				
			}
		}).start();
		getListView();
	}
	public void getListView(){
		myListView=showFocusTopics.getListView();
		myListView.setDivider(null);
		/*myListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&view.getLastVisiblePosition()==view.getCount()-1){
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							UserImagesJson.Data data=datas.get(datas.size()-1);
							HttpUtil.downloadUserHomepage(handler, data.img_id,0,UserHomepageActivity.this);
						}
					}).start();
					
				}else if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&view.getFirstVisiblePosition()==0){
					listViewFirstVisionPosition=0;
					userInfoRelativeLayout.setVisibility(View.VISIBLE);
					showPictures(listViewLinearLayout);;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				listViewFirstVisionPosition=firstVisibleItem;
					if(firstVisibleItem>1){
						userInfoRelativeLayout.setVisibility(View.GONE);
					}
				
			}
		});*/
	}
    /**
     * 显示用户关注的人群
     * @param linearLayout
     */
	private void showFocus(LinearLayout linearLayout){
//		showFocusTopics=null;
		new ShowFocusAndFansList(this, linearLayout, 1);
		/*focusAdapterMode=1;
		handler.sendEmptyMessage(SHOW_PROGRESS);
		view=LayoutInflater.from(this).inflate(R.layout.widget_show_topics, null);
		PullDownView pullDownView=(PullDownView)view.findViewById(R.id.pull_down_view);
		last_focus_id=-1;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.followList(handler, last_focus_id,1,UserHomepageActivity.this);
			}
		}).start();
		
		listView=(ListView)pullDownView.getListView();
		linearLayout.removeAllViews();
		linearLayout.addView(view);
		userDatas=new ArrayList<UserJson.Data>();
		focusAdapter=new FocusAndFansAdapter(this, userDatas, 1,handler);*/
//		listView.setAdapter(focusAdapter);
		
	}
	/**
	 * 显示粉丝
	 * @param linearLayout
	 */
	private void showFans(LinearLayout linearLayout){
		focusAdapterMode=2;
//		showFocusTopics=null;
		new ShowFocusAndFansList(this, linearLayout, 2);
		/*handler.sendEmptyMessage(SHOW_PROGRESS);
		view=LayoutInflater.from(this).inflate(R.layout.widget_show_topics, null);
		PullDownView pullDownView=(PullDownView)view.findViewById(R.id.pull_down_view);
		listView=(ListView)pullDownView.getListView();
		last_focus_id=-1;
         new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.followList(handler, last_focus_id,2,UserHomepageActivity.this);
			}
		}).start();
		linearLayout.removeAllViews();
		linearLayout.addView(view);
       
		userDatas=new ArrayList<UserJson.Data>();
	    focusAdapter=new FocusAndFansAdapter(this, userDatas, 2,handler);*/
//		listView.setAdapter(focusAdapter);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if(requestCode==1){
				path=data.getStringExtra("path");
				if(path!=null){
					Intent intent=new Intent(this,com.aviary.android.feather.FeatherActivity.class);
					intent.setData(Uri.parse("file://"+path));
					intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
					startActivityForResult(intent, 2); 
				}
			}else if(requestCode==2){
				/*if(data.getBooleanExtra("cancel", false)){
					if(getPictureMode==1){
						Intent intent2=new Intent(UserHomepageActivity.this,TakePictureBackground.class);
						intent2.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
						UserHomepageActivity.this.startActivityForResult(intent2, 1);
					}else{
						Intent intent=new Intent(UserHomepageActivity.this,AlbumPictureBackground.class);
						intent.putExtra("mode", TakePictureBackground.MODE_CHANGE_ICON);
						UserHomepageActivity.this.startActivityForResult(intent, 1);
					}
				}*/
				if(data.getData()!=null)
				loadBitmap(data.getData());
			}
			

			
			break;

		default:
			break;
		}
	}
	public void showWeiboInfo(){
		bottomRelativeLayout3.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				bottomRelativeLayout3.setVisibility(View.INVISIBLE);
				blur_view.setVisibility(View.INVISIBLE);
				return true;
			}
		});
		ImageView icon=(ImageView)findViewById(R.id.imageView6);
		ImageView add=(ImageView)findViewById(R.id.imageView7);
		
		final SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		final String pathString=sp.getString("xinlang_icon", null);
		String name=sp.getString("xinlang_name", "");
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=2;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		if(!StringUtil.isEmpty(name))
			weiboName.setText(""+name);
		if(StringUtil.judgeImageExists(pathString))
		icon.setImageBitmap(BitmapFactory.decodeFile(pathString,options));
		icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UserHomepageActivity.this,ShowPictureActivity.class);
				
				
				if(pathString==null)return;
				intent.putExtra("path", pathString);
				UserHomepageActivity.this.startActivity(intent);
			}
		});
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		layout=(LinearLayout)findViewById(R.id.touch_linearlayout);
		layout.setClickable(true);
	}
	/**
	 * 界面初始化，头像，昵称，性别 等 
	 */
	public void setViews(){
		if(Constants.user.gender==1){
			imageView5.setImageResource(R.drawable.male1);//性别
		}else if(Constants.user.gender==2){
			imageView5.setImageResource(R.drawable.female1);//性别
		}

		tv1.setText(Constants.user.nickName);//昵称
		 final SharedPreferences sp=this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		 
		    if( sp.contains(Constants.user.race)){
			  tv2.setText(""+sp.getString(Constants.user.race, ""));
		   }else{
			   new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					// TODO Auto-generated method stub
					if(Integer.parseInt(Constants.user.race)>301)return;
					boolean flag=HttpUtil.getRaceType(UserHomepageActivity.this);
					if(flag){
						tv2.setText(sp.getString(Constants.user.race, ""));
					}
				}
			}).start();
		   }
		tv3.setText(""+Constants.user.age+"岁");//年龄
		if(Constants.user.iconPath!=null&&new File(Constants.user.iconPath).exists()){
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=4;
			imageView3.setImageBitmap(BitmapFactory.decodeFile(Constants.user.iconPath,options));
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					Constants.user.iconPath=HttpUtil.downloadIconImage(Constants.USER_DOWNLOAD_TX, Constants.user.iconUrl, null,UserHomepageActivity.this);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							BitmapFactory.Options options=new BitmapFactory.Options();
							options.inSampleSize=4;
							imageView3.setImageBitmap(BitmapFactory.decodeFile(Constants.user.iconPath,options));
						}
					});
				}
			}).start();
		}
	}
	
	/**
	 * 获取文件夹大小
	 * @throws IOException 
	 */
	public int  getFileSize(File file,int fileLength){
		
		if(file.isDirectory()){
			File[] list=file.listFiles();
			if(list.length==0)return fileLength;
			for(File f:list){
				if(f.isDirectory()){
					fileLength+=getFileSize(f,fileLength);
				}else{
					FileInputStream fis=null;
					try {
						fis=new FileInputStream(f);
						fileLength+=fis.available();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						if(fis!=null){
							try {
								fis.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}

		}else{
			FileInputStream fis=null;
			try {
				fis=new FileInputStream(file);
				fileLength+=fis.available();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return fileLength;
	}
	public void loadBitmap(Uri uri){
		LogUtil.i("me", "uri="+uri.toString()+",path="+path);
		Cursor cursor=getContentResolver().query(uri, null, null, null, null);
		if(cursor!=null){
			cursor.moveToFirst();
			final String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=4;
			options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			imageView3.setImageBitmap(BitmapFactory.decodeFile(path,options));
			bottomLinearLayout2.setVisibility(View.INVISIBLE);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String nameString=HttpUtil.uploadUserIcon(path,UserHomepageActivity.this);
					if(nameString==null){
						
					}else{
						/*if(new File(Constants.Picture_ICON_Path+File.separator+nameString).exists()){
							Constants.user.iconPath=Constants.Picture_ICON_Path+File.separator+nameString;
							
						}*/
						if(new File(path).exists()){
							Constants.user.iconPath=path;
							
						}
					}
				}
			}).start();
			cursor.close();
		}
		
		
	}	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(asyncTask!=null){
			asyncTask.cancel(true);
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(isChangingUserIcon){
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
				bottomLinearLayout2.setClickable(false);
				blur_view.setVisibility(View.INVISIBLE);
				isChangingUserIcon=false;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
