package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.UserPetsAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.HorizontalListView2;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 用户卡片
 * @author admin
 *
 */
public class UserCardActivity extends Activity implements OnClickListener{
	private ImageView closeIv,iconIv,leftIv,rifhtIv,sexIv;
	private TextView nameTv,addressTv,goldNumTv,mailOrModiryTv;
	private HorizontalListView2 listView2;
	private ShowProgress showProgress;
	private LinearLayout progresslayout;
	private Handler handler;
	private View popupParent;
	private RelativeLayout black_layout;
	private MyUser user;
	public static UserCardActivity userCardActivity;
	private UserPetsAdapter userPetsAdapter;
	private ArrayList<Animal> animals;
	private int current_position=0;
	private int from;//1,照片详情
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_user_card);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		user=(MyUser)getIntent().getSerializableExtra("user");
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
	    leftIv=(ImageView)findViewById(R.id.left_iv);
	    rifhtIv=(ImageView)findViewById(R.id.right_iv);
	    sexIv=(ImageView)findViewById(R.id.sex_iv);
	    
	    
	    popupParent=findViewById(R.id.popup_parent);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
	    
	    nameTv=(TextView)findViewById(R.id.name_tv);
	    addressTv=(TextView)findViewById(R.id.address_tv);
	    goldNumTv=(TextView)findViewById(R.id.gold_tv);
	    mailOrModiryTv=(TextView)findViewById(R.id.button_tv);
	    progresslayout=(LinearLayout)findViewById(R.id.progress_layout);
	    listView2=(HorizontalListView2)findViewById(R.id.listview);
	    animals=new ArrayList<Animal>();
	    userPetsAdapter=new UserPetsAdapter(this, animals, user);
	    listView2.setAdapter(userPetsAdapter);
	    showProgress=new ShowProgress(this, progresslayout);
	    
	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final MyUser u=HttpUtil.info(UserCardActivity.this, handler, user.userId);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showProgress.progressCancel();
						if(u!=null){
							UserCardActivity.this.user=u;
							setUserInfo(UserCardActivity.this.user);
						}
					}
				});
			}
		}).start();
	
	loadIcons();
	    closeIv.setOnClickListener(this);
	    iconIv.setOnClickListener(this);
	    mailOrModiryTv.setOnClickListener(this);
	    leftIv.setOnClickListener(this);
	    rifhtIv.setOnClickListener(this);
	    
	    listView2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(NewPetKingdomActivity.petKingdomActivity!=null&&from==0){
					/*if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
						NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
					}
					NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;*/
					NewPetKingdomActivity.petKingdomActivity.loadAnimal(animals.get(position));
//					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//					am.moveTaskToFront(NewPetKingdomActivity.petKingdomActivity.getTaskId(), 0);
					
					finish();
					return;
				}
				if(NewPetKingdomActivity.petKingdomActivity!=null){
					NewPetKingdomActivity.petKingdomActivity.finish();
					NewPetKingdomActivity.petKingdomActivity=null;
				}
				Intent intent=new Intent(UserCardActivity.this,NewPetKingdomActivity.class);
				intent.putExtra("animal",animals.get(position));
				startActivity(intent);
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(UserCardActivity.this)){
					PetApplication.petApp.activityList.remove(UserCardActivity.this);
				}
				finish();
				System.gc();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	}
	private void loadIcons() {
		// TODO Auto-generated method stub
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> temp=new ArrayList<Animal>();
				ArrayList<Animal> animal=HttpUtil.usersKingdom(UserCardActivity.this,user, -1, handler);
				if(animal!=null){
					for(int i=0;i<animal.size();i++){
						if(animal.get(i).master_id==user.userId){
							temp.add(animal.get(i));
						}
					}
					for(int i=0;i<animal.size();i++){
						if(!temp.contains(animal.get(i))){
							temp.add(animal.get(i));
						}
					}
				}
				if(temp!=null&&temp.size()>0){
	               runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
					
						animals=temp;
						userPetsAdapter.update(animals);
						userPetsAdapter.notifyDataSetChanged();
					}
				});
				}
			}
		}).start();
	}
	public void setUserInfo(MyUser user) {
		nameTv.setText(user.u_nick);
		addressTv.setText(""+user.province+" | "+user.city);
		goldNumTv.setText(""+user.coinCount);
		if(user.u_gender==1){
			//公
			sexIv.setImageResource(R.drawable.male1);
		}else if(user.u_gender==2){
			//母
			sexIv.setImageResource(R.drawable.female1);
		}else{
			
		}
		if(PetApplication.myUser!=null&&PetApplication.myUser.userId==user.userId){
			mailOrModiryTv.setText("修改资料");
		}else{
			mailOrModiryTv.setText("私信");
		}
		
		
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
		        .decodingOptions(options2)
                .build();
		ImageLoader imageLoader3=ImageLoader.getInstance();
		imageLoader3.displayImage(Constants.USER_DOWNLOAD_TX+user.u_iconUrl, iconIv, displayImageOptions3);
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
			if(PetApplication.petApp.activityList.contains(UserCardActivity.userCardActivity)){
				PetApplication.petApp.activityList.remove(UserCardActivity.userCardActivity);
			}
			userCardActivity=null;
			this.finish();
			System.gc();
			break;
		case R.id.user_icon:
			if(PetApplication.myUser!=null&&PetApplication.myUser.userId==user.userId){
				Intent intent6=new Intent(this,ModifyPetInfoActivity.class);
				intent6.putExtra("mode", 2);
				this.startActivity(intent6);
			}else{
				Intent intent3=new Intent(this,ShowPictureActivity.class);
				intent3.putExtra("mode", 2);
				intent3.putExtra("url", user.u_iconUrl);
				this.startActivity(intent3);
			}
			break;

		case R.id.left_iv:
			View view=listView2.getChildAt(0);
			if(view!=null){
				int w=view.getMeasuredWidth();
				int vLeft=view.getLeft();
				int vRight=view.getRight();
				int left=listView2.getLeft();
				int x=listView2.getScrollX();
				int right=listView2.getRight();
				LogUtil.i("mi", "第一个="+listView2.getFirstVisiblePosition()+";最后一个="+listView2.getLastVisiblePosition()+"；view.w="+w+";x="+x);
				int det=current_position*w-w;
				if(det>=0){
					listView2.scrollTo(det);
				}else{
					listView2.scrollTo(0);
				}
				current_position--;
				if(current_position<0)current_position=0;
			}
			
			break;

		case R.id.right_iv:
			View view1=listView2.getChildAt(listView2.getFirstVisiblePosition());
			
			if(view1!=null){
				int listW=listView2.getMeasuredWidth();
				int w=view1.getMeasuredWidth();
				int vLeft=view1.getLeft();
				int vRight=view1.getRight();
				int left=listView2.getLeft();
				int x=listView2.getScrollX();
				int right=listView2.getRight();
				int det=listW-current_position*w;
				int num=vLeft/w;
				current_position++;
				if(current_position<=animals.size()){
					listView2.scrollTo(current_position*w);
				}else{
					listView2.scrollTo(animals.size()*w);
					current_position=animals.size();
				}
				
			}
			
			break;

		case R.id.button_tv:
			if(!UserStatusUtil.isLoginSuccess(this,popupParent,black_layout)){
				return;
			}
			if(PetApplication.myUser!=null&&PetApplication.myUser.userId==user.userId){
				Intent intent=new Intent(this,ModifyPetInfoActivity.class);
				intent.putExtra("mode", 2);
				startActivity(intent);
			}else{
				
				if(com.aidigame.hisun.pet.huanxin.ChatActivity.activityInstance!=null){
					com.aidigame.hisun.pet.huanxin.ChatActivity.activityInstance.finish();
				}
				Intent intent2=new Intent(this,com.aidigame.hisun.pet.huanxin.ChatActivity.class);
				intent2.putExtra("chatType", com.aidigame.hisun.pet.huanxin.ChatActivity.CHATTYPE_SINGLE);
				intent2.putExtra("user", user);
				startActivity(intent2);
				finish();
			}
			
			break;
		}
	}
}
