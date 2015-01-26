package com.aidigame.hisun.pet.ui;

import java.io.File;
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
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
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
import com.aidigame.hisun.pet.constant.AddressData;
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
/**
 * 修改宠物或用户信息
 * @author admin
 *
 */
public class ModifyPetInfoActivity extends Activity {
	FrameLayout frameLayout;
	
	
	public LinearLayout black_layout;
	
	
	
	DisplayImageOptions displayImageOptions;
	ImageLoader imageLoader;
	Bitmap pet_bmp,user_bmp;
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
	EditText petName,userName,petRace,petAge,userCity;
	ImageView petMale,petFemale,petSex;
	RadioGroup radioGroup;
	Button complete,complete2;
	boolean isLogining=false;
	int mode;//1，宠物信息；2，用户信息
	LinearLayout camera_album,petRaceLayout;
	boolean isUserIcon=true;
	int classs;
	String petNameStr,userNameStr,petAgeStr,userCityStr,userCityCode,
	       petRaceStr,petRaceCode,petIconPath,petSexStr,userSexStr,userIconPath;
	Animal animal;
	HandleHttpConnectionException handleHttpConnectionException;
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
				break;
			case Constants.LOGIN_SUCCESS:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				break;
			case Constants.REGISTER_FAIL:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				String error=(String)msg.obj;
				if(error!=null){
					ShowDialog.show(error,ModifyPetInfoActivity.this);
				}else{
					ShowDialog.show("用户名重复,注册 失败",ModifyPetInfoActivity.this);
				}
				
				break;
			case DISMISS_PROGRESS:
				if(showProgress!=null)
				showProgress.progressCancel();
				break;
			case SHOW_PROGRESS:
				if(showProgress==null){
					showProgress=new ShowProgress(ModifyPetInfoActivity.this, black_layout);
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
		mode=getIntent().getIntExtra("mode", 1);
		TextView title=(TextView)findViewById(R.id.textView11);
		if(mode==1){
			title.setText("修改萌星资料");
		}else
		if(mode==2){
			title.setText("修改用户资料");
		}
		camera_album=(LinearLayout)findViewById(R.id.album_camera_register);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		//显示没有图片
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inJustDecodeBounds=false;
		opts.inSampleSize=4;
		opts.inPreferredConfig=Bitmap.Config.RGB_565;
		opts.inPurgeable=true;
		opts.inInputShareable=true;
		if(mode==1){
			displayImageOptions=new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.pet_icon)
            .showImageOnFail(R.drawable.pet_icon)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .decodingOptions(opts)
            .build();
		}else{
			displayImageOptions=new DisplayImageOptions.Builder()
			 .showImageOnLoading(R.drawable.user_icon)
	            .showImageOnFail(R.drawable.user_icon)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .decodingOptions(opts)
            .build();
		}
		
		
		initView();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		viewPager.setVisibility(View.VISIBLE);
	}
	private void initView() {
		// TODO Auto-generated method stub
		setBlurImageBackground();
		StringUtil.umengOnResume(this);
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
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(ModifyPetInfoActivity.this)){
					PetApplication.petApp.activityList.remove(ModifyPetInfoActivity.this);
				}
				ModifyPetInfoActivity.this.finish();
				System.gc();
			}
		});
		if(mode==1){
			animal=(Animal)getIntent().getSerializableExtra("animal");
			viewList.add(view1);
			petIcon=(RoundImageView)view1.findViewById(R.id.pet_icon);
			petIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.camera1));
			petSex=(ImageView)view1.findViewById(R.id.pet_sex);
			petName=(EditText)view1.findViewById(R.id.editText44);
			petRace=(EditText)view1.findViewById(R.id.editText55);
			complete2=(Button)view1.findViewById(R.id.button1);
			complete2.setVisibility(View.VISIBLE);
			petRaceLayout=(LinearLayout)view1.findViewById(R.id.petrace_layout);
			petRace.setFocusable(false);
			
			petAge=(EditText)view1.findViewById(R.id.editText66);
			petMale=(ImageView)view1.findViewById(R.id.imageview_male);
			petFemale=(ImageView)view1.findViewById(R.id.imageview_female);
			tv1=(TextView)view1.findViewById(R.id.textView1);
			if(animal.type>200&&animal.type<300){
				tv1.setText("狗狗信息");
			}else if(animal.type>100&&animal.type<200){
				tv1.setText("喵喵信息");
			}
			initPetListener();
			petAgeStr=""+animal.a_age;
			petNameStr=animal.pet_nickName;
			petSexStr=""+animal.a_gender;
			petRaceCode=""+animal.type;
			setPetInfo(animal);
		}
		if(mode==2){
			viewList.add(view2);
			userName=(EditText)view2.findViewById(R.id.editText4);
			userIcon=(RoundImageView)view2.findViewById(R.id.user_icon);
			userIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.camera1));
			userCity=(EditText)view2.findViewById(R.id.editText5);
			radioGroup=(RadioGroup)view2.findViewById(R.id.user_sex);
			complete=(Button)view2.findViewById(R.id.button1);
			initUserListener();
			setUserInfo(Constants.user);
			complete.setClickable(true);
			complete.setBackgroundResource(R.drawable.button);
			userSexStr=""+Constants.user.u_gender;
			userCityCode=""+Constants.user.locationCode;
			userNameStr=Constants.user.u_nick;
			userCityStr=Constants.user.province+"|"+Constants.user.city;
			
		}
		
		
		adapter=new HomeViewPagerAdapter(viewList);
		viewPager.setAdapter(adapter);
		
		
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
    	if(Constants.user.u_gender==1){
    		rb2.setChecked(true);
    	}else{
    		rb1.setChecked(true);
    	}
    	
    	userName.setText(Constants.user.u_nick);
    	userCity.setText(Constants.user.province+"|"+Constants.user.city);
    	ImageLoader imageLoader=ImageLoader.getInstance();
    	imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+Constants.user.u_iconUrl, userIcon, displayImageOptions);

	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		
	}
	private void initUserListener() {
		// TODO Auto-generated method stub
		isUserIcon=true;
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
				AlertDialog.Builder builder=new AlertDialog.Builder(ModifyPetInfoActivity.this);
				 addressDialog=new AddressDialog(ModifyPetInfoActivity.this);
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
					Toast.makeText(ModifyPetInfoActivity.this, "正在发送信息，请稍后", Toast.LENGTH_LONG).show();
					return;
				}
				isLogining=true;
					
				if(StringUtil.isEmpty(userSexStr)){
					Toast.makeText(ModifyPetInfoActivity.this, "请选择用户性别", 5000).show();
					isLogining=false;
					return;
				}
				if(StringUtil.isEmpty(userNameStr)){
					Toast.makeText(ModifyPetInfoActivity.this, "请填写用户昵称", 5000).show();
					isLogining=false;
					return;
				}
				if(StringUtil.isEmpty(userCityStr)){
					Toast.makeText(ModifyPetInfoActivity.this, "请选择所在城市", 5000).show();
					isLogining=false;
					return;
				}
				
                if(!judgeStringLength(userNameStr, 30)){
                	Toast.makeText(ModifyPetInfoActivity.this, "用户昵称长度超过20个字符", 5000).show();
                	isLogining=false;
					return;
				}
				
				
                handler.sendEmptyMessage(SHOW_PROGRESS);
					String code="";
					final MyUser user=new MyUser();
						user.u_nick=userNameStr;
						user.u_gender=Integer.parseInt(userSexStr);
						user.uid=code;
						
						user.u_iconPath=userIconPath;
						user.city=userCityCode;
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							boolean flag=HttpUtil.modifyUserInfo(handleHttpConnectionException.getHandler(ModifyPetInfoActivity.this),user,ModifyPetInfoActivity.this);
							if(flag){
								MyUser user=HttpUtil.info(ModifyPetInfoActivity.this,handleHttpConnectionException.getHandler(ModifyPetInfoActivity.this),Constants.user.userId);
								user.currentAnimal=Constants.user.currentAnimal;
								user.aniList=Constants.user.aniList;
								Constants.user=user;
								if(user!=null){
									/*
									 * 上传用户头像和宠物头像
									 */
									//上传头像
									if(userIconPath!=null){
										String path1=HttpUtil.uploadUserIcon(userIconPath,ModifyPetInfoActivity.this,-1);
										if(path1!=null){
											Constants.user.u_iconUrl=path1;
										}
									}
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											handler.sendEmptyMessage(DISMISS_PROGRESS);
											Toast.makeText(ModifyPetInfoActivity.this,"修改资料成功" , Toast.LENGTH_LONG).show();
											ModifyPetInfoActivity.this.finish();
											if(UserCenterFragment.userCenterFragment!=null){
										    	UserCenterFragment.userCenterFragment.updatateInfo(true);;
											}
											if(UserCardActivity.userCardActivity!=null){
												UserCardActivity.userCardActivity.setUserInfo(Constants.user);
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
			}
		});
		userIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isUserIcon=true;
				showCameraAlbum();
				
			}
		});
	}
	private void initPetListener() {
		// TODO Auto-generated method stub
		isUserIcon=false;
		petAge.addTextChangedListener(new MyTextWatcher(petAge));
		petName.addTextChangedListener(new MyTextWatcher(petName));
		
		petRace.addTextChangedListener(new MyTextWatcher(petRace));
		petRace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				viewPager.setVisibility(View.INVISIBLE);
				 /*Intent intent=new Intent(ModifyPetInfoActivity.this,ChoseClassActivity.class);
               if(animal.type>200&&animal.type<300){
            	  
   				intent.putExtra("mode", 1);//选择狗的种族
                  
               }else if(animal.type>100&&animal.type<200){
            	   intent.putExtra("mode", 2);//选择猫的种族
               }
               ModifyPetInfoActivity.this.startActivityForResult(intent,1);*/
               AlertDialog.Builder builder=new AlertDialog.Builder(ModifyPetInfoActivity.this);
				 final RaceDialog raceDialog=new RaceDialog(ModifyPetInfoActivity.this);
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
				AlertDialog.Builder builder=new AlertDialog.Builder(ModifyPetInfoActivity.this);
				final AgeDialog ageDialog=new AgeDialog(ModifyPetInfoActivity.this);
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
				petMale.setImageResource(R.drawable.male);
				petFemale.setImageResource(R.drawable.female_gray);
				petSex.setVisibility(View.VISIBLE);
				petSex.setImageResource(R.drawable.male);
			}
		});
         petFemale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				petSexStr=""+2;
				petMale.setImageResource(R.drawable.male_gray);
				petFemale.setImageResource(R.drawable.female);
				petSex.setVisibility(View.VISIBLE);
				petSex.setImageResource(R.drawable.female);
				
			}
		});
        petIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isUserIcon=false;
				showCameraAlbum();
			}
		});
        complete2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isLogining){
					Toast.makeText(ModifyPetInfoActivity.this, "正在发送信息，请稍后", Toast.LENGTH_LONG).show();
					return;
				}
				isLogining=true;
				if(StringUtil.isEmpty(petSexStr)){
					Toast.makeText(ModifyPetInfoActivity.this, "请选择宠物性别", 5000).show();
					isLogining=false;
					return;
				}
				if(StringUtil.isEmpty(petNameStr)){
					isLogining=false;
					Toast.makeText(ModifyPetInfoActivity.this, "请填写宠物昵称", 5000).show();
					return;
				}
				if(StringUtil.isEmpty(petAgeStr)){
					Toast.makeText(ModifyPetInfoActivity.this, "请填写宠物年龄", 5000).show();
					isLogining=false;
					return;
				}
				if(StringUtil.isEmpty(petRaceCode)){
					Toast.makeText(ModifyPetInfoActivity.this, "请选择宠物种族", 5000).show();
					isLogining=false;
					return;
				}
				if(!judgeStringLength(petNameStr, 30)){
					Toast.makeText(ModifyPetInfoActivity.this, "宠物昵称长度超过20个字符", 5000).show();
					isLogining=false;
					return;
				}
				 handler.sendEmptyMessage(SHOW_PROGRESS);
				 String code="";
					final MyUser user=new MyUser();
				 user.currentAnimal=animal;
					user.pet_nickName=petNameStr;
					user.a_gender=Integer.parseInt(petSexStr);
					user.race=petRaceCode;
					user.a_age=petAgeStr;
					user.uid=code;
					user.pet_iconPath=petIconPath;
                    new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							boolean flag=HttpUtil.modifyPetInfo(handleHttpConnectionException.getHandler(ModifyPetInfoActivity.this),user,ModifyPetInfoActivity.this);
							if(flag){
								Animal an=new Animal();
								an.a_id=user.currentAnimal.a_id;
								final Animal animal=HttpUtil.animalInfo(ModifyPetInfoActivity.this,an,handleHttpConnectionException.getHandler(ModifyPetInfoActivity.this));
								
								if(animal!=null){
									/*
									 * 上传用户头像和宠物头像
									 */
									//上传头像
									if(petIconPath!=null){
										String path=HttpUtil.uploadUserIcon(petIconPath,ModifyPetInfoActivity.this,user.currentAnimal.a_id);
										if(path!=null){
											animal.pet_iconUrl=path;
										}
									}
									
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											handler.sendEmptyMessage(DISMISS_PROGRESS);
											Toast.makeText(ModifyPetInfoActivity.this,"修改资料成功" , Toast.LENGTH_LONG).show();
											
											ModifyPetInfoActivity.this.finish();
											if(Constants.user.aniList!=null){
												int index=Constants.user.aniList.indexOf(animal);
												if(index>=0){
														Constants.user.aniList.remove(index);
														Constants.user.aniList.add(animal);
												}
												
											}
											if(Constants.user.currentAnimal!=null&&Constants.user.currentAnimal.a_id==animal.a_id){
												Constants.user.currentAnimal=animal;
												
											}
											if(UserCenterFragment.userCenterFragment!=null){
										    	UserCenterFragment.userCenterFragment.updatateInfo(true);;
											}
											if(NewPetKingdomActivity.petKingdomActivity!=null){
												NewPetKingdomActivity.petKingdomActivity.setPetInfo(animal);
											
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
		});
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
				break;
            case R.id.editText55:
				
				break;
            case R.id.editText66:
            	
	            break;
            case R.id.editText4:
	            userNameStr=et.getEditableText().toString();
	            break;
            case R.id.editText5:
	            userCityStr=et.getEditableText().toString();
	            break;
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
				Intent intent2=new Intent(ModifyPetInfoActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_REGISTER);
				camera_album.setVisibility(View.INVISIBLE);
				ModifyPetInfoActivity.this.startActivityForResult(intent2, 12);
				camera_album.setClickable(false);
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ModifyPetInfoActivity.this,AlbumPictureBackground.class);
				intent.putExtra("mode", TakePictureBackground.MODE_REGISTER);
				ModifyPetInfoActivity.this.startActivityForResult(intent, 12);
				camera_album.setVisibility(View.INVISIBLE);
				camera_album.setClickable(false);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation=AnimationUtils.loadAnimation(ModifyPetInfoActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
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
			}else if(requestCode==12){
				
				String path=data.getStringExtra("path");
				 File fi=new File(path);
				LogUtil.i("me", "相册返回头像大小"+fi.length());
				/*File f=new File(path);
				String name=f.getParentFile().getAbsolutePath()+File.separator+System.currentTimeMillis()+".jpg";
				path=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+".jpg";
				boolean flag=f.renameTo(new File(path));*/
				
				Intent intent=new Intent(this,com.aviary.android.feather.FeatherActivity.class);
				intent.setData(Uri.parse("file://"+path));
				intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
				startActivityForResult(intent, 2); 
				
			}else if(requestCode==2){
				Uri uri=data.getData();
				if(data.getData()!=null)
				loadBitmap(data.getData());
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
		}else {
			path=uri.getPath();
		}
		if(!StringUtil.isEmpty(path)){
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=StringUtil.topicImageGetScaleByDPI(this);
			if(isUserIcon){
				 File fi=new File(path);
		            LogUtil.i("me", "aviary返回头像大小"+fi.length());
				userIconPath=path;
				
				user_bmp=BitmapFactory.decodeFile(path,options);
	               /*if(user_bmp!=null){
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
				
			}else{
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
	   @Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	    
	

}
