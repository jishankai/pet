package com.aidigame.hisun.pet.ui;

import java.nio.charset.Charset;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.HomeViewPagerAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.fragment.AddressDialog;
import com.aidigame.hisun.pet.widget.fragment.AgeDialog;
import com.aidigame.hisun.pet.widget.fragment.RaceDialog;
import com.aidigame.hisun.pet.widget.fragment.UserCenterFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;
/**
 * 注册界面，填写个人信息
 * mode 2，只填写用户信息；3创建狗的猫的联萌填写宠物和用户信息,5 修改用户资料,6 微信或新浪绑定
 * @author admin
 *
 */
public class NewRegisterActivity extends Activity {
	FrameLayout frameLayout;
	
	
	public LinearLayout black_layout;
	
	
	
	DisplayImageOptions displayImageOptions;
	ImageLoader imageLoader;
	
	AddressDialog addressDialog;
	
	public static final int DISMISS_PROGRESS=202;
	public static final int SHOW_PROGRESS=203;
	ShowProgress showProgress;
	ViewPager viewPager;
	HomeViewPagerAdapter adapter;
	ImageView back;
	View view1,view2;
	TextView tv1;
	ArrayList<View> viewList;
	RoundImageView petIcon,userIcon;
	Bitmap pet_bmp,user_bmp;
	EditText petName,userName,petRace,petAge,userCity;
	ImageView petMale,petFemale,petSex;
	RadioGroup radioGroup;
	Button complete;
	boolean isLogining=false;
	int mode;
	int from;//默认值为0，进行注册；1，已经注册过,可以创建圈子
	LinearLayout camera_album,petRaceLayout;
	boolean isUserIcon=true;
	int classs;
	String petNameStr,userNameStr,petAgeStr,userCityStr,userCityCode,
	       petRaceStr,petRaceCode,petIconPath,petSexStr,userSexStr,userIconPath;
	Animal animal;
	HandleHttpConnectionException handleHttpConnectionException;
	boolean isBind=false;
	MyUser user;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constants.REGISTER_SUCCESS:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						HttpUtil.login(RegisterActivity.this, handler);
					    
					}
				}).start();
				/*Intent intent1=new Intent(NewRegisterActivity.this,HomeActivity.class);
				NewRegisterActivity.this.startActivity(intent1);
				NewRegisterActivity.this.finish();*/
				break;
			case Constants.LOGIN_SUCCESS:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				/*Intent intent=new Intent(NewRegisterActivity.this,HomeActivity.class);
				NewRegisterActivity.this.startActivity(intent);
				NewRegisterActivity.this.finish();*/
				break;
			case Constants.REGISTER_FAIL:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				String error=(String)msg.obj;
				if(error!=null){
					ShowDialog.show(error,NewRegisterActivity.this);
				}else{
					ShowDialog.show("用户名重复,注册 失败",NewRegisterActivity.this);
				}
				
				break;
			case DISMISS_PROGRESS:
				if(showProgress!=null)
				showProgress.progressCancel();
				break;
			case SHOW_PROGRESS:
				if(showProgress==null){
					showProgress=new ShowProgress(NewRegisterActivity.this, black_layout);
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
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_new_register);
		
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		frameLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.blur, options)));
		
		mode=getIntent().getIntExtra("mode", 3);
		from=getIntent().getIntExtra("from", 0);
		isBind=getIntent().getBooleanExtra("isBind", false);
		if(isBind){
			user=(MyUser)getIntent().getSerializableExtra("user");
		}
		TextView title=(TextView)findViewById(R.id.textView11);
		if(from==1){
			title.setText("创建萌星");
		}else
		if(mode==5){
			title.setText("修改用户资料");
		}else{
			title.setText("注册");
			
		}
		camera_album=(LinearLayout)findViewById(R.id.album_camera_register);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		//显示没有图片
	    Bitmap nobmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.noimg);
		Matrix matrix=new Matrix();
		matrix.postScale(Constants.screen_width/(nobmp.getWidth()*1f), Constants.screen_width/(nobmp.getWidth()*1f));
		nobmp=Bitmap.createBitmap(nobmp, 0, 0, nobmp.getWidth(), nobmp.getHeight(),matrix,true);
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inJustDecodeBounds=false;
		opts.inSampleSize=4;
		opts.inPreferredConfig=Bitmap.Config.RGB_565;
		opts.inPurgeable=true;
		opts.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions.Builder()
		                    .showImageOnLoading(new BitmapDrawable(nobmp))
		                    .cacheInMemory(true)
		                    .cacheOnDisc(true)
		                    .bitmapConfig(Config.RGB_565)
		                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		                    .decodingOptions(opts)
		                    .build();
		
		initView();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		viewPager.setVisibility(View.VISIBLE);
		StringUtil.umengOnResume(this);
	}
	private void initView() {
		// TODO Auto-generated method stub
		back=(ImageView)findViewById(R.id.back);
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		view1=LayoutInflater.from(this).inflate(R.layout.item_register_view1, null);
		view2=LayoutInflater.from(this).inflate(R.layout.item_register_view2, null);
		
		black_layout=(LinearLayout)findViewById(R.id.black_layout);
		
		findViewById(R.id.textView11).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.i("scroll","点击注册");
			}
		});       
		viewList=new ArrayList<View>();
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(NewRegisterActivity.this)){
					PetApplication.petApp.activityList.remove(NewRegisterActivity.this);
				}
				NewRegisterActivity.this.finish();
				System.gc();
			}
		});
		viewList.add(view1);
		viewList.add(view2);
		
		adapter=new HomeViewPagerAdapter(viewList);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.register_viewpager_space));
		viewPager.setAdapter(adapter);
		petIcon=(RoundImageView)view1.findViewById(R.id.pet_icon);
		
		petIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.camera1));
		petSex=(ImageView)view1.findViewById(R.id.pet_sex);
		petName=(EditText)view1.findViewById(R.id.editText44);
		petRace=(EditText)view1.findViewById(R.id.editText55);
		petRaceLayout=(LinearLayout)view1.findViewById(R.id.petrace_layout);
		petRace.setFocusable(false);
		
		petAge=(EditText)view1.findViewById(R.id.editText66);
		petMale=(ImageView)view1.findViewById(R.id.imageview_male);
		petFemale=(ImageView)view1.findViewById(R.id.imageview_female);
		tv1=(TextView)view1.findViewById(R.id.textView1);
		if(mode==3){
			tv1.setText("萌星信息");
		}else if(mode==4){
			tv1.setText("萌星信息");
		}
		initPetListener();
        
        
		userName=(EditText)view2.findViewById(R.id.editText4);
		TextView tv=(TextView)view2.findViewById(R.id.textView1);
		userIcon=(RoundImageView)view2.findViewById(R.id.user_icon);
		userIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.camera1));
		userCity=(EditText)view2.findViewById(R.id.editText5);
		radioGroup=(RadioGroup)view2.findViewById(R.id.user_sex);
		complete=(Button)view2.findViewById(R.id.button1);
		initUserListener();
		/*
		 * 判断用户是否登录成功，登录成功，则将用户信息填入注册表格
		 */
		if(PetApplication.isSuccess){
			//1.填写用户信息
			
			//2.用户信息编辑栏不可编辑
			
		}
		if(from==1){
			setUserInfo(PetApplication.myUser);
			userCity.setEnabled(false);
			userIcon.setEnabled(false);
			userName.setEnabled(false);
			radioGroup.setEnabled(false);
			view2.findViewById(R.id.radiobutton1).setClickable(false);;
			view2.findViewById(R.id.radiobutton2).setClickable(false);;
		}else
		if(mode==1||mode==2){
        	animal=(Animal)getIntent().getSerializableExtra("animal");
        	setPetInfo(animal);
        	if(mode==2)tv.setText("粉丝档案");
        	
		}else if(mode==5){
			//修改用户资料
			setPetInfo(PetApplication.myUser.currentAnimal);
			animal=PetApplication.myUser.currentAnimal;
			setUserInfo(PetApplication.myUser);
			complete.setClickable(true);
			complete.setBackgroundResource(R.drawable.button);
			userSexStr=""+PetApplication.myUser.u_gender;
			userCityCode=""+PetApplication.myUser.locationCode;
			userNameStr=PetApplication.myUser.u_nick;
			userCityStr=PetApplication.myUser.province+"|"+PetApplication.myUser.city;
			if(animal.master_id==PetApplication.myUser.userId){
				petAgeStr=""+animal.a_age;
				petSexStr=""+animal.a_gender;
				petRaceCode=""+animal.type;
				petNameStr=animal.pet_nickName;
				petRaceStr=animal.race;
			}
			
		}else{
			// 创建狗或猫的王国
		}
		LogUtil.i("mi","NewRegisterActivity::::"+ "isBind?"+(isBind));
		if(isBind){
			user=(MyUser)getIntent().getSerializableExtra("user");
			LogUtil.i("mi","NewRegisterActivity::::"+ "user==null?"+(user==null));
			setUserInfo(user);
		}
		
	}
	/**
	 * 宠物信息初始化，认养宠物，或者用户资料修改时使用
	 * @param animal
	 */
	public void setPetInfo(Animal animal){
		// 加入狗或猫的王国,将宠物相关信息填入注册表格中
//    	imageLoader=ImageLoader.getInstance();
    	//TODO 暂时注释掉
//    	imageLoader.displayImage(uri, petIcon, displayImageOptions);
    	
    	if(animal.a_gender==1){
    		petMale.setImageResource(R.drawable.male);
    	}else{
    		petFemale.setImageResource(R.drawable.female);
    	}
    	
    	petName.setText(animal.pet_nickName);
    	petRace.setText(animal.race);
    	petAge.setText(""+animal.a_age_str);
    	ImageLoader imageLoader=ImageLoader.getInstance();
    	imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl, petIcon, displayImageOptions);
    	if(mode==1||mode==2||(mode==5&&PetApplication.myUser.userId!=PetApplication.myUser.currentAnimal.master_id)){
    		petMale.setClickable(false);
        	petFemale.setClickable(false);
        	petName.setEnabled(false);
        	petAge.setEnabled(false);
        	petRace.setEnabled(false);
        	petRaceLayout.setClickable(false);
        	petIcon.setClickable(false);
        	viewPager.setCurrentItem(1);
    	}
	}
	/**
	 * 设置用户信息，资料修改时使用
	 * @param animal
	 */
	public void setUserInfo(MyUser user){
		// 加入狗或猫的王国,将宠物相关信息填入注册表格中
    	imageLoader=ImageLoader.getInstance();
    	//TODO 暂时注释掉
//    	imageLoader.displayImage(uri, petIcon, displayImageOptions);
    	RadioButton rb1=(RadioButton)view2.findViewById(R.id.radiobutton1);
    	RadioButton rb2=(RadioButton)view2.findViewById(R.id.radiobutton2);
    	if(user.u_gender==1){
    		rb2.setChecked(true);
    	}else{
    		rb1.setChecked(true);
    	}
    	LogUtil.i("mi", "用户性别="+user.u_gender);
    	userName.setText(user.u_nick);
    	if(!StringUtil.isEmpty(user.province)&&!isBind){
    		userCity.setText(user.province+"|"+user.city);
    	}
    	
    	if(!StringUtil.isEmpty(user.u_iconPath)){
    		userIcon.setImageBitmap(BitmapFactory.decodeFile(user.u_iconPath));
    		userIconPath=user.u_iconPath;
    	}else{
    		ImageLoader imageLoader=ImageLoader.getInstance();
        	imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+user.u_iconUrl, userIcon, displayImageOptions);
    	}
    	

	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void initUserListener() {
		// TODO Auto-generated method stub
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(arg0==0){
					isUserIcon=false;
				}else{
					isUserIcon=true;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		userName.addTextChangedListener(new MyTextWatcher(userName));
		userCity.addTextChangedListener(new MyTextWatcher(userCity));
        view2.findViewById(R.id.city_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
		});
       
		userCity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new AlertDialog.Builder(NewRegisterActivity.this);
				 addressDialog=new AddressDialog(NewRegisterActivity.this);
				final AlertDialog dialog =builder.setView(addressDialog.getView())
				        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								userCity.setText(addressDialog.getAddress());
								userCityCode=addressDialog.getAddressCode();
								dialog.dismiss();
							}
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
//								userCity.setText(addressDialog.getAddress());
								dialog.dismiss();
							}
						}).show();
			}
		});
        complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isLogining){
					Toast.makeText(NewRegisterActivity.this, "正在发送信息，请稍后", Toast.LENGTH_LONG).show();
					return;
				}
				isLogining=true;
				if(mode==3||mode==4||(mode==5&&animal.master_id==PetApplication.myUser.userId)){
					if(StringUtil.isEmpty(petSexStr)){
						Toast.makeText(NewRegisterActivity.this, "请选择宠物性别", 5000).show();
						isLogining=false;
						return;
					}
					/*if(StringUtil.isEmpty(petIconPath)){
						Toast.makeText(NewRegisterActivity.this, "请选择宠物头像", 5000).show();
						return;
					}*/
					if(StringUtil.isEmpty(petNameStr)){
						isLogining=false;
						Toast.makeText(NewRegisterActivity.this, "请填写宠物昵称", 5000).show();
						return;
					}
					if(StringUtil.isEmpty(petAgeStr)){
						Toast.makeText(NewRegisterActivity.this, "请填写宠物年龄", 5000).show();
						isLogining=false;
						return;
					}
					if(StringUtil.isEmpty(petRaceStr)){
						Toast.makeText(NewRegisterActivity.this, "请选择宠物种族", 5000).show();
						isLogining=false;
						return;
					}
					/*if(!judgeStringLength(petNameStr, 30)){
						Toast.makeText(NewRegisterActivity.this, "宠物昵称长度超过20个字符", 5000).show();
						isLogining=false;
						return;
					}*/
				}
				
				if(StringUtil.isEmpty(userSexStr)){
					Toast.makeText(NewRegisterActivity.this, "请选择用户性别", 5000).show();
					isLogining=false;
					return;
				}
				/*if(StringUtil.isEmpty(userIconPath)){
					Toast.makeText(NewRegisterActivity.this, "请选择用户头像", 5000).show();
					return;
				}*/
				if(StringUtil.isEmpty(userNameStr)){
					Toast.makeText(NewRegisterActivity.this, "请填写用户昵称", 5000).show();
					isLogining=false;
					return;
				}
				if(StringUtil.isEmpty(userCityStr)){
					Toast.makeText(NewRegisterActivity.this, "请选择所在城市", 5000).show();
					isLogining=false;
					return;
				}
				
                /*if(!judgeStringLength(userNameStr, 30)){
                	Toast.makeText(NewRegisterActivity.this, "用户昵称长度超过20个字符", 5000).show();
                	isLogining=false;
					return;
				}*/
//				userCityStr=new String(userCityStr.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
//				userNameStr=new String(userNameStr.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
//				petNameStr=new String(petNameStr.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
//				petRaceStr=new String(petRaceStr.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
				
				
                handler.sendEmptyMessage(SHOW_PROGRESS);
				if(from==1){
					createKingdom();
				}else
				if(mode!=5){
					String code="";
					final MyUser user=new MyUser();
					if(mode==1||mode==2){
						user.currentAnimal=animal;
						user.pet_nickName=animal.pet_nickName;
						user.u_nick=userNameStr;
						user.a_gender=animal.a_gender;
						user.u_gender=Integer.parseInt(userSexStr);
						user.race=""+animal.type;
						user.a_age=""+animal.a_age;
						user.uid=code;
						user.u_iconPath=userIconPath;
						user.city=userCityCode;
						if(isBind){
							user.weixin_id=NewRegisterActivity.this.user.weixin_id;
							user.xinlang_id=NewRegisterActivity.this.user.xinlang_id;
							user.wechat_union=NewRegisterActivity.this.user.wechat_union;
							user.isBind=isBind;
						}
					}else{
						user.pet_nickName=petNameStr;
						user.u_nick=userNameStr;
						user.a_gender=Integer.parseInt(petSexStr);
						user.u_gender=Integer.parseInt(userSexStr);
						user.race=petRaceCode;
						user.a_age=petAgeStr;
						user.uid=code;
						user.pet_iconPath=petIconPath;
						user.u_iconPath=userIconPath;
						user.city=userCityCode;
						if(isBind){
							user.weixin_id=NewRegisterActivity.this.user.weixin_id;
							user.xinlang_id=NewRegisterActivity.this.user.xinlang_id;
							user.isBind=isBind;
						}
					}
					
					
					/*Constants.user=user;
					Constants.user.currentAnimal=new Animal();*/
					
					
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						/*
						 * 1.提交注册信息
						 */
						boolean flag=HttpUtil.register(handleHttpConnectionException.getHandler(NewRegisterActivity.this),user,NewRegisterActivity.this);
						
						
						/*
						 * 获取最新用户信息
						 */
						
						if(flag){
							/*HttpUtil.getSID(NewRegisterActivity.this,handleHttpConnectionException.getHandler(NewRegisterActivity.this));
							 LogUtil.i("me", "获取SID方法执行完毕++++++++++" );
							*/
							/*User user=HttpUtil.info(NewRegisterActivity.this,handleHttpConnectionException.getHandler(NewRegisterActivity.this),Constants.user.userId);
							 LogUtil.i("me", "获取用户信息方法执行完毕++++++++++" );
								Constants.user=user;
							while(user.currentAnimal==null||user.currentAnimal.a_id==0){
								user=HttpUtil.info(NewRegisterActivity.this,handleHttpConnectionException.getHandler(NewRegisterActivity.this),Constants.user.userId);
								 LogUtil.i("me", "获取用户信息方法执行完毕++++++++++" );
							}*/
							user.userId=PetApplication.myUser.userId;
							user.currentAnimal=PetApplication.myUser.currentAnimal;
							PetApplication.myUser=user;
							if(user!=null){
								/*
								 * 上传用户头像和宠物头像
								 */
								
								//上传头像
								try {
									Thread.sleep(100);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								if(userIconPath!=null){
									LogUtil.i("me", "上传用户头像++++++++++" );
									String path1=HttpUtil.uploadUserIcon(userIconPath,NewRegisterActivity.this,-1);
									if(path1!=null){
										PetApplication.myUser.u_iconUrl=path1;
									}
									LogUtil.i("me", "上传用户头像完毕++++++++++" );
								}
								if(petIconPath!=null){
									LogUtil.i("me", "上传宠物头像++++++++++" );
									String path=HttpUtil.uploadUserIcon(petIconPath,NewRegisterActivity.this,user.currentAnimal.a_id);
									if(path!=null){
										PetApplication.myUser.currentAnimal.pet_iconUrl=path;
									}
									LogUtil.i("me", "上传宠物头像完毕++++++++++" );
								}
								
								SharedPreferences sPreferences=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
								Editor editor=sPreferences.edit();
								editor.putBoolean("isRegister", true);
								PetApplication.isSuccess=true;
								editor.commit();
								
								MyUser user=null;
								while(user==null||user.userId==0||user.currentAnimal==null||user.currentAnimal.a_id==0){
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									user=HttpUtil.info(NewRegisterActivity.this,handleHttpConnectionException.getHandler(NewRegisterActivity.this),PetApplication.myUser.userId);
								} 
								LogUtil.i("me", "获取用户信息方法执行完毕++++++++++" );
								PetApplication.myUser=user;
								 
								user.currentAnimal=HttpUtil.animalInfo(NewRegisterActivity.this, PetApplication.myUser.currentAnimal, handleHttpConnectionException.getHandler(NewRegisterActivity.this));
								
								PetApplication.myUser.aniList=new ArrayList<Animal>();
								PetApplication.myUser.aniList.add(PetApplication.myUser.currentAnimal);
								if(ChoseKingActivity.choseKingActivity!=null){
									ChoseKingActivity.choseKingActivity.finish();
									ChoseKingActivity.choseKingActivity=null;
								}
								NewRegisterActivity.this.finish();
								if(ChoseAcountTypeActivity.choseAcountTypeActivity!=null){
									ChoseAcountTypeActivity.choseAcountTypeActivity.finish();
									ChoseAcountTypeActivity.choseAcountTypeActivity=null;
								}
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										handler.sendEmptyMessage(DISMISS_PROGRESS);
										Toast.makeText(NewRegisterActivity.this,"注册成功" , Toast.LENGTH_LONG).show();
										NewRegisterActivity.this.finish();
										
										/*if(!StringUtil.isEmpty(Constants.CON_VERSION)&&"1.0".equals(Constants.CON_VERSION)){
											
										}else{*/
											Intent intent13=new Intent(NewRegisterActivity.this,InviteOthersDialogActivity.class);
											intent13.putExtra("mode", 2);
											NewRegisterActivity.this.startActivity(intent13);
										/*}*/
										if(PetApplication.myUser!=null&&PetApplication.myUser.currentAnimal!=null){
											if(PetApplication.myUser.userId==PetApplication.myUser.currentAnimal.a_id){
												MobclickAgent.onEvent(NewRegisterActivity.this, "register_suc_pet");
											}else{
												MobclickAgent.onEvent(NewRegisterActivity.this, "register_suc_nopet");
											}
											
										}
										
										if(UserCenterFragment.userCenterFragment!=null){
									    	UserCenterFragment.userCenterFragment.updatateInfo(true);;
										}
										if(HomeActivity.homeActivity!=null){
											HomeActivity.homeActivity.initEMChatLogin();
										}
									}
								});
							}else{
								handler.sendEmptyMessage(DISMISS_PROGRESS);
							}
							
						}else{
							handler.sendEmptyMessage(DISMISS_PROGRESS);
						}
						isLogining=false;
						 
					}
				}).start();
				
				}else{
					String code="";
					final MyUser user=new MyUser();
					if(animal.master_id!=PetApplication.myUser.userId){
						//被认养的宠物
						user.pet_nickName=animal.pet_nickName;
						user.u_nick=userNameStr;
						user.a_gender=animal.a_gender;
						user.u_gender=Integer.parseInt(userSexStr);
						user.race=""+animal.type;
						user.a_age=""+animal.a_age;
						user.uid=code;
						user.u_iconPath=userIconPath;
						user.city=userCityCode;
					}else{
						user.currentAnimal=animal;
						user.pet_nickName=petNameStr;
						user.u_nick=userNameStr;
						user.a_gender=Integer.parseInt(petSexStr);
						user.u_gender=Integer.parseInt(userSexStr);
						user.race=petRaceCode;
						user.a_age=petAgeStr;
						user.uid=code;
						user.pet_iconPath=petIconPath;
						user.u_iconPath=userIconPath;
						user.city=userCityCode;
					}
					
					
					/*Constants.user=user;
					Constants.user.currentAnimal=new Animal();*/
					
					handler.sendEmptyMessage(SHOW_PROGRESS);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							boolean flag=HttpUtil.modifyUserInfo(handleHttpConnectionException.getHandler(NewRegisterActivity.this),user,NewRegisterActivity.this);
							if(flag){
								MyUser user=HttpUtil.info(NewRegisterActivity.this,handleHttpConnectionException.getHandler(NewRegisterActivity.this),PetApplication.myUser.userId);
								PetApplication.myUser=user;
								
									
								if(user!=null){
									/*
									 * 上传用户头像和宠物头像
									 */
									//上传头像
									if(petIconPath!=null){
										String path=HttpUtil.uploadUserIcon(petIconPath,NewRegisterActivity.this,user.currentAnimal.a_id);
										if(path!=null){
											PetApplication.myUser.currentAnimal.pet_iconUrl=path;
										}
									}
									if(userIconPath!=null){
										String path1=HttpUtil.uploadUserIcon(userIconPath,NewRegisterActivity.this,-1);
										if(path1!=null){
											PetApplication.myUser.u_iconUrl=path1;
										}
									}
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											handler.sendEmptyMessage(DISMISS_PROGRESS);
											Toast.makeText(NewRegisterActivity.this,"修改资料成功" , Toast.LENGTH_LONG).show();
											
											NewRegisterActivity.this.finish();
											if(UserCenterFragment.userCenterFragment!=null){
										    	UserCenterFragment.userCenterFragment.updatateInfo(true);;
											}
										}
									});
									
								}else{
									handler.sendEmptyMessage(DISMISS_PROGRESS);
								}
								
							}else{
								handler.sendEmptyMessage(DISMISS_PROGRESS);
							}
							isLogining=false;
						}
					}).start();
					
					
				}
			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==R.id.radiobutton1){
					userSexStr=""+2;//女
				}else{
					userSexStr=""+1;//男
				}
				if(checkUserInfo()){
				}
			}
		});
		userIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isUserIcon=true;
				showCameraAlbum();
				
				
				if(checkUserInfo()){
				}
			}
		});
	}
	private void initPetListener() {
		// TODO Auto-generated method stub
		petAge.addTextChangedListener(new MyTextWatcher(petAge));
		petName.addTextChangedListener(new MyTextWatcher(petName));
		
		petRace.addTextChangedListener(new MyTextWatcher(petRace));
		petRace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				viewPager.setVisibility(View.INVISIBLE);
				/* Intent intent=new Intent(NewRegisterActivity.this,ChoseClassActivity.class);
               if(mode==3){
            	  
   				intent.putExtra("mode", 1);//选择狗的种族
                  
               }else if(mode==4){
            	   intent.putExtra("mode", 2);//选择猫的种族
               }
               NewRegisterActivity.this.startActivityForResult(intent,1);*/
				AlertDialog.Builder builder=new AlertDialog.Builder(NewRegisterActivity.this);
				 final RaceDialog raceDialog=new RaceDialog(NewRegisterActivity.this);
				final AlertDialog dialog =builder.setView(raceDialog.getView())
				        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								petRace.setText(raceDialog.getRace());
								userCityCode=raceDialog.getRaceCode();
								petRaceStr=raceDialog.getRace();
								petRaceCode=raceDialog.getRaceCode();
								dialog.dismiss();
							}
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
//								userCity.setText(addressDialog.getAddress());
								dialog.dismiss();
							}
						}).show();
				
			}
		});
		petAge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new AlertDialog.Builder(NewRegisterActivity.this);
				final AgeDialog ageDialog=new AgeDialog(NewRegisterActivity.this);
				builder.setView(ageDialog.getView())
				       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String age=ageDialog.getAge();
							if(!StringUtil.isEmpty(age)){
								petAgeStr=""+ageDialog.getAgeByMonth();
								petAge.setText(age);
							}
							
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).show();
			}
		});
		petMale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				petSexStr=""+1;
				checkPetInfo();
				petMale.setImageResource(R.drawable.male);
				petFemale.setImageResource(R.drawable.female_gray);
				petSex.setVisibility(View.VISIBLE);
				petSex.setImageResource(R.drawable.male);
				if(checkUserInfo()){
					complete.setClickable(true);
					complete.setBackgroundResource(R.drawable.button);
				}
			}
		});
         petFemale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				petSexStr=""+2;
				checkPetInfo();
				petMale.setImageResource(R.drawable.male_gray);
				petFemale.setImageResource(R.drawable.female);
				petSex.setVisibility(View.VISIBLE);
				petSex.setImageResource(R.drawable.female);
				if(checkUserInfo()){
				}
			}
		});
        petIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isUserIcon=false;
				showCameraAlbum();
				
				
				if(checkUserInfo()){
				}
			}
		});
	}
	public boolean checkUserInfo(){
		boolean flag=true;
		if(mode==3||mode==4){
//			flag=checkPetInfo();
			if(StringUtil.isEmpty(petSexStr)){
				flag=false;
			}
			/*if(StringUtil.isEmpty(petIconPath)){
				flag=false;
			}*/
			if(StringUtil.isEmpty(petNameStr)){
				flag=false;
			}
			if(StringUtil.isEmpty(petAgeStr)){
				flag=false;
			}
			if(StringUtil.isEmpty(petRaceStr)){
				flag=false;
			}
		}
		if(StringUtil.isEmpty(userSexStr)){
			flag=false;
		}
		/*if(StringUtil.isEmpty(userIconPath)){
			flag=false;
		}*/
		if(StringUtil.isEmpty(userNameStr)){
			flag=false;
		}
		if(StringUtil.isEmpty(userCityStr)){
			flag=false;
		}
		if(flag){
			complete.setClickable(true);
			complete.setBackgroundResource(R.drawable.button);
		}
		
		return flag;
	}
	public boolean checkPetInfo(){
		boolean flag=true;
		if(StringUtil.isEmpty(petSexStr)){
			flag=false;
		}
		if(StringUtil.isEmpty(petIconPath)){
			flag=false;
		}
		if(StringUtil.isEmpty(petNameStr)){
			flag=false;
		}
		if(StringUtil.isEmpty(petAgeStr)){
			flag=false;
		}
		if(StringUtil.isEmpty(petRaceStr)){
			flag=false;
		}
		if(flag){
			viewPager.setCurrentItem(1);
		}
		return flag;
	}
	class MyTextWatcher implements TextWatcher{
		EditText et;
        public MyTextWatcher(EditText et){
        	this.et=et;
        }
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			switch (et.getId()) {
			case R.id.editText44:
				petNameStr=et.getEditableText().toString();
//				petNameStr=petAgeStr.trim();
				checkPetInfo();
				break;
            case R.id.editText55:
				
				break;
            case R.id.editText66:
            	
//	            petAgeStr=et.getEditableText().toString();
	            checkPetInfo();
	            break;
            case R.id.editText4:
	            userNameStr=et.getEditableText().toString();
//	            userNameStr=userNameStr.trim();
	            break;
            case R.id.editText5:
	            userCityStr=et.getEditableText().toString();
	            
	            break;
			}
			if(checkUserInfo()){
				complete.setClickable(true);
				complete.setBackgroundResource(R.drawable.button);
			}
		}
		
	}
	private void showCameraAlbum() {
		// TODO Auto-generated method stub
		final View view=LayoutInflater.from(this).inflate(R.layout.popup_user_homepage, null);
		camera_album.removeAllViews();
		camera_album.addView(view);
		camera_album.setVisibility(View.VISIBLE);
		camera_album.setClickable(true);
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
				Intent intent2=new Intent(NewRegisterActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_REGISTER);
				camera_album.setVisibility(View.INVISIBLE);
				NewRegisterActivity.this.startActivityForResult(intent2, 12);
				camera_album.setClickable(false);
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(NewRegisterActivity.this,AlbumPictureBackground.class);
				intent.putExtra("mode", TakePictureBackground.MODE_REGISTER);
				NewRegisterActivity.this.startActivityForResult(intent, 12);
				camera_album.setVisibility(View.INVISIBLE);
				camera_album.setClickable(false);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation=AnimationUtils.loadAnimation(NewRegisterActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
					}
				}, 1000);
				
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		visible();
		LogUtil.i("me","onActivityResult"+"："+(data==null));
		switch (resultCode) {
		case RESULT_OK:
			if(requestCode==1){
				petRaceStr=data.getStringExtra("raceName");
				petRaceCode=data.getStringExtra("raceCode");
				SharedPreferences sp=getSharedPreferences("temp.xml",Context.MODE_WORLD_WRITEABLE);
				Editor editor=sp.edit();
				editor.putString("race", petRaceStr);
				editor.commit();
				if (petRaceStr==null) {
					return;
				}
				petRace.setText(petRaceStr);
				checkPetInfo();
				checkUserInfo();
			}else if(requestCode==12){
				
				String path=data.getStringExtra("path");
				Intent intent=new Intent(this,com.aviary.android.feather.FeatherActivity.class);
				intent.setData(Uri.parse("file://"+path));
				intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
				startActivityForResult(intent, 2); 
				
			}else if(requestCode==2){
				if(data.getData()!=null)
				loadBitmap(data.getData());
				checkUserInfo();
			}
			break;
		}
	}
	public void loadBitmap(Uri uri){
		Cursor cursor=getContentResolver().query(uri, null, null, null, null);
		String path=null;
		if(cursor!=null){
			cursor.moveToFirst();
			path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
			cursor.close();
		}else{
			path=uri.getPath();
		}
		if(!StringUtil.isEmpty(path)){
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=StringUtil.topicImageGetScaleByDPI(this);
			if(isUserIcon){
				userIconPath=path;
				user_bmp=BitmapFactory.decodeFile(path,options);
             /*  if(user_bmp!=null){
            	   
				      path=ImageUtil.compressImage(user_bmp,"usr_icon");
				      if(!StringUtil.isEmpty(path)){
				    	  userIconPath=path;
				      }else{
				    	  path=ImageUtil.compressImage(user_bmp,"usr_icon");
				    	  if(!StringUtil.isEmpty(path)){
					    	  userIconPath=path;
					      }
				      }
				}*/
				if(StringUtil.isEmpty(userIconPath)){
					Toast.makeText(this, "图片保存地址为空", Toast.LENGTH_LONG).show();
				}else{
					userIcon.setImageBitmap(BitmapFactory.decodeFile(path,options));
				}
				userIcon.setImageBitmap(BitmapFactory.decodeFile(path,options));
			}else{
				checkPetInfo();
				petIconPath=path;
				pet_bmp=BitmapFactory.decodeFile(path,options);
				/*if(pet_bmp!=null){
					path=ImageUtil.compressImage(pet_bmp,"pet_icon");
				      if(!StringUtil.isEmpty(path)){
				    	  petIconPath=path;
				      }else{
				    	  path=ImageUtil.compressImage(pet_bmp,"pet_icon");
				    	  if(!StringUtil.isEmpty(path)){
				    		  petIconPath=path;
					      }
				      }
				}*/
				if(StringUtil.isEmpty(petIconPath)){
					Toast.makeText(this, "图片保存地址为空", Toast.LENGTH_LONG).show();
				}else{
					petIcon.setImageBitmap(BitmapFactory.decodeFile(path,options));
				}
				
			}
		}
	}
	/**
	 * 判断字符串的字节长度是否超过设定值
	 * @param str 需要检测的字符串
	 * @param max 字符串的长度上线
	 * @return boolean true，为超过；false 超过上限
	 */
	public boolean judgeStringLength(String str,int max){
		boolean flag=true;
		if(str.getBytes().length>max){
			flag=false;
		}
		LogUtil.i("scroll", ""+str+" de长度="+str.getBytes().length);
		
		return flag;
	}
	/**
	 * 创建王国，此方法执行的环境是在工作线程中
	 */
	public void createKingdom(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final Animal animal=new Animal();
				animal.pet_nickName=petNameStr;
				animal.a_gender=Integer.parseInt(petSexStr);
				animal.race=petRaceCode;
				animal.a_age=Integer.parseInt(petAgeStr);
				final long aid=HttpUtil.createKingdom(NewRegisterActivity.this,animal, handleHttpConnectionException.getHandler(NewRegisterActivity.this));
				animal.a_id=aid;
				if(aid>0){
					//上传头像
					if(petIconPath!=null){
						String path=HttpUtil.uploadUserIcon(petIconPath,NewRegisterActivity.this,aid);
						if(path!=null){
							animal.pet_iconUrl=path;
						}
					}
				}
				
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(aid>0){
							animal.job="经纪人";
							animal.u_rank="经纪人";
							animal.u_rankCode=0;
							animal.master_id=PetApplication.myUser.userId;
							animal.u_name=PetApplication.myUser.u_nick;
							animal.u_tx=PetApplication.myUser.u_iconUrl;
							PetApplication.myUser.rank="经纪人";
							PetApplication.myUser.rankCode=0;
							PetApplication.myUser.currentAnimal=animal;
							PetApplication.myUser.aniList.add(0,animal);
							Toast.makeText(NewRegisterActivity.this, "创建萌星成功", Toast.LENGTH_LONG).show();
							if(PetApplication.myUser.aniList.size()>10){
								int num=0;
								if(PetApplication.myUser.aniList.size()<=20){
									num=PetApplication.myUser.aniList.size()*5;
								}else{
									num=100;
								}
								PetApplication.myUser.coinCount-=num;
							}
							if(ChoseAcountTypeActivity.choseAcountTypeActivity!=null){
								ChoseAcountTypeActivity.choseAcountTypeActivity.finish();
								ChoseAcountTypeActivity.choseAcountTypeActivity=null;
							}
							if(HomeActivity.homeActivity!=null&&HomeActivity.homeActivity.myPetFragment!=null){
								HomeActivity.homeActivity.myPetFragment.cameraBt.setVisibility(View.VISIBLE);
								HomeActivity.homeActivity.myPetFragment.homeMyPet.refresh();
							}
							UserStatusUtil.setDefaultKingdom();
							NewRegisterActivity.this.finish();
							
						}else{
							Toast.makeText(NewRegisterActivity.this, "创建萌星失败", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		}).start();
		
	}

	   @Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	   
	


}
