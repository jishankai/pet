package com.aidigame.hisun.pet.widget.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.ui.AboutUsActivity;
import com.aidigame.hisun.pet.ui.AdviceActivity;
import com.aidigame.hisun.pet.ui.ChosePetActivity;
import com.aidigame.hisun.pet.ui.FAQActivity;
import com.aidigame.hisun.pet.ui.InviteOthersDialogActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.ui.NewRegisterActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.ReceiverAddressActivity;
import com.aidigame.hisun.pet.ui.UpdateAPKActivity;
import com.aidigame.hisun.pet.ui.UsersListActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.PLAWaterfull;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.WeixinShare;

public class SetupFragment extends Fragment implements OnClickListener{
	FrameLayout frameLayout;
	View viewTopWhite;
	ScrollView scrollView;
	public View popupParent;
	public RelativeLayout black_layout;
	TextView fileSizeTV,tv1,tv2,tv3,tv4,tv5,tv6,tv12,tv13;
	View menuView;
	NewHomeActivity homeActivity;
	LinearLayout linearLayout1,linearLayout2,linearLayout3,progressLayout,
	linearLayout6,linearLayout7,linearLayout8,linearLayout9,linearLayout10,linearLayout11,chosePetLayout,linearlayout12,linearlayout13;
	RelativeLayout linearLayout4,linearLayout5;
	ImageView back;
	ImageView setup_image1,iv3,iv4,iv5,iv12,iv13;
	ImageView setup_image3,setup_image4;
	public static boolean getXinlangAuth=false;
	ShowProgress showProgress;
	int acount=-1;//1同步发送到新浪微博，2 绑定新浪微博；-1 Activity启动时进入onResume方法，啥也不做。
	
	
	
	Handler handler;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		menuView=inflater.inflate(R.layout.fragment_setup, null);
		homeActivity=NewHomeActivity.homeActivity;
		handler=HandleHttpConnectionException.getInstance().getHandler(homeActivity);
		initView();
		initListener();
		return menuView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		homeActivity=NewHomeActivity.homeActivity;
		super.onViewCreated(view, savedInstanceState);
	}
	public void setHomeActivity(NewHomeActivity activity){
		this.homeActivity=activity;
        
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		back=(ImageView)menuView.findViewById(R.id.imageView1);
		setup_image1=(ImageView)menuView.findViewById(R.id.setup_imageview1);
		setup_image3=(ImageView)menuView.findViewById(R.id.setup_imageview3);
		setup_image4=(ImageView)menuView.findViewById(R.id.setup_imageview4);
		
		black_layout=(RelativeLayout)menuView.findViewById(R.id.black_layout);
		popupParent=(View)menuView.findViewById(R.id.popup_parent);
		
		
		fileSizeTV=(TextView)menuView.findViewById(R.id.cach_file_size);
		
		iv3=(ImageView)menuView.findViewById(R.id.imageView3);
		iv4=(ImageView)menuView.findViewById(R.id.imageView4);
		iv5=(ImageView)menuView.findViewById(R.id.imageView5);
		iv12=(ImageView)menuView.findViewById(R.id.imageView124);
		iv13=(ImageView)menuView.findViewById(R.id.imageView135);
		tv12=(TextView)menuView.findViewById(R.id.tv122);
		tv13=(TextView)menuView.findViewById(R.id.tv133);
		tv1=(TextView)menuView.findViewById(R.id.tv1);
		tv2=(TextView)menuView.findViewById(R.id.tv2);
		tv3=(TextView)menuView.findViewById(R.id.tv3);
		tv4=(TextView)menuView.findViewById(R.id.tv4);
		tv5=(TextView)menuView.findViewById(R.id.tv5);
		tv6=(TextView)menuView.findViewById(R.id.tv6);
		linearLayout1=(LinearLayout)menuView.findViewById(R.id.linearlayout1);
		linearLayout2=(LinearLayout)menuView.findViewById(R.id.linearlayout2);
		linearLayout3=(LinearLayout)menuView.findViewById(R.id.linearlayout3);
		linearLayout4=(RelativeLayout)menuView.findViewById(R.id.linearlayout4);
		linearLayout5=(RelativeLayout)menuView.findViewById(R.id.linearlayout5);
		linearLayout6=(LinearLayout)menuView.findViewById(R.id.linearlayout6);
		linearLayout7=(LinearLayout)menuView.findViewById(R.id.linearlayout7);
		linearLayout8=(LinearLayout)menuView.findViewById(R.id.linearlayout8);
		linearLayout9=(LinearLayout)menuView.findViewById(R.id.linearlayout9);
		linearLayout10=(LinearLayout)menuView.findViewById(R.id.linearlayout10);
		linearLayout11=(LinearLayout)menuView.findViewById(R.id.linearlayout11);
		chosePetLayout=(LinearLayout)menuView.findViewById(R.id.chose_pet_layout);
		linearlayout12=(LinearLayout)menuView.findViewById(R.id.linearlayout12);
		linearlayout13=(LinearLayout)menuView.findViewById(R.id.linearlayout13);
		progressLayout=(LinearLayout)menuView.findViewById(R.id.progresslayout);
		if(!StringUtil.isEmpty(Constants.CON_VERSION)&&"1.0".equals(Constants.CON_VERSION))
		{
			linearlayout12.setVisibility(View.GONE);
		}else{
			linearlayout12.setVisibility(View.VISIBLE);
		}		
		setBlurImageBackground();
		setFileSize();
		
//		linearLayout11.setBackgroundResource(R.color.orange_red);
		SharedPreferences sp=homeActivity.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		if(sp.getBoolean(Constants.LOCK_TO_XINLANG, false)){
			setup_image1.setImageResource(R.drawable.checked);;
		}else{
			setup_image1.setImageResource(R.drawable.unchecked);;
		}
		if(sp.getBoolean(Constants.PICTURE_SEND_TO_XINLANG, false)){
			setup_image3.setImageResource(R.drawable.checked);;
		}else{
			setup_image3.setImageResource(R.drawable.unchecked);;
		}
		if(sp.getBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false)){
			setup_image4.setImageResource(R.drawable.checked);;
		}else{
			setup_image4.setImageResource(R.drawable.unchecked);;
		}
	}
	/**
	 * 检测缓存文件大小
	 */
	private void setFileSize() {
		// TODO Auto-generated method stub
		File file=new File(Constants.Picture_Root_Path);
		long length=0;
		if(file.exists()){
			length=getFileSize(file, 0);
		}
		LogUtil.i("scroll", "length="+length);
		DecimalFormat dfDecimalFormat=new DecimalFormat("##0.00");
		if((float)length/(1024*1024*1f)>1.0f){
			float f=(float)length/(1024*1024*1f);
			fileSizeTV.setText(""+dfDecimalFormat.format(f)+"MB");
//			fileSizeTV.setText("28.12"+"MB");
		}else if((float)length/(1024*1f)>1.0f){
			float f=(float)length/(1024*1f);
			fileSizeTV.setText(""+dfDecimalFormat.format(f)+"KB");
		}else{
			fileSizeTV.setText("不足1KB");
		}


	}
	/**
	 * 获取文件或文件夹大小
	 * @param file
	 * @param fileLength
	 * @return
	 */
    public int  getFileSize(File file,int fileLength){
		
		if(file.isDirectory()){
			File[] list=file.listFiles();
			if(list.length==0)return fileLength;
			for(File f:list){
				if(f.isDirectory()){
					fileLength=getFileSize(f,fileLength);
				}else{
					FileInputStream fis=null;
					try {
						fis=new FileInputStream(f);
//						fileLength+=fis.available();
						fileLength+=f.length();
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

	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */
    int touchSlop;
    int distance;
    boolean isRecord=false;
    int yDown;
	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)menuView.findViewById(R.id.framelayout);
		viewTopWhite=(View)menuView.findViewById(R.id.top_white_view);
		scrollView=(ScrollView)menuView.findViewById(R.id.scrollview);
		touchSlop=ViewConfiguration.get(homeActivity).getScaledTouchSlop();
		
        scrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isRecord=false;
					if(!isRecord){
						yDown=(int) event.getY();
						isRecord=true;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if(!isRecord){
						yDown=(int) event.getY();
						isRecord=true;
					}
					distance=(int)event.getY()-yDown;
					
					break;
				case MotionEvent.ACTION_UP:
					LogUtil.i("scroll", "setupfragment,up+distance="+distance+",touchslop="+touchSlop+",scrollView.getChildAt(0).getTop()"+scrollView.getChildAt(0).getTop()+",getScrollY="+scrollView.getScrollY());
					if(Math.abs(distance)>touchSlop){
						if(scrollView.getScrollY()==0){
							viewTopWhite.setVisibility(View.VISIBLE);
						}else{
							viewTopWhite.setVisibility(View.GONE);
						}
					}
					break;
				}
				return false;
			}
		});
	}

	private void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(this);
		if(Constants.isSuccess){
			setup_image1.setOnClickListener(this);
			setup_image3.setOnClickListener(this);
			setup_image4.setOnClickListener(this);
			linearLayout1.setOnClickListener(this);
			linearLayout2.setOnClickListener(this);
			chosePetLayout.setOnClickListener(this);
			linearLayout4.setOnClickListener(this);
			linearLayout5.setOnClickListener(this);
			linearLayout3.setOnClickListener(this);
			iv3.setAlpha(1.0f);
			iv4.setAlpha(1.0f);
			iv5.setAlpha(1.0f);
			tv1.setTextColor(homeActivity.getResources().getColor(R.color.black));
			tv2.setTextColor(homeActivity.getResources().getColor(R.color.black));
			tv3.setTextColor(homeActivity.getResources().getColor(R.color.black));
			tv4.setTextColor(homeActivity.getResources().getColor(R.color.black));
			tv5.setTextColor(homeActivity.getResources().getColor(R.color.black));
			tv6.setTextColor(homeActivity.getResources().getColor(R.color.black));
		}else{
			setup_image1.setEnabled(false);
			setup_image3.setEnabled(false);
			setup_image4.setEnabled(false);
			linearLayout1.setEnabled(false);
			linearLayout2.setEnabled(false);
			chosePetLayout.setEnabled(false);
			linearLayout4.setEnabled(false);
			linearLayout5.setEnabled(false);
			linearLayout3.setEnabled(false);
			linearlayout12.setEnabled(false);
			linearlayout13.setEnabled(false);
			iv3.setAlpha(0.3f);
			iv4.setAlpha(0.3f);
			iv5.setAlpha(0.3f);
			iv12.setAlpha(0.3f);
			iv13.setAlpha(0.3f);
			tv1.setTextColor(homeActivity.getResources().getColor(R.color.set_up_text_gray));
			tv2.setTextColor(homeActivity.getResources().getColor(R.color.set_up_text_gray));
			tv3.setTextColor(homeActivity.getResources().getColor(R.color.set_up_text_gray));
			tv4.setTextColor(homeActivity.getResources().getColor(R.color.set_up_text_gray));
			tv5.setTextColor(homeActivity.getResources().getColor(R.color.set_up_text_gray));
			tv6.setTextColor(homeActivity.getResources().getColor(R.color.set_up_text_gray));
			tv12.setTextColor(homeActivity.getResources().getColor(R.color.set_up_text_gray));
			tv13.setTextColor(homeActivity.getResources().getColor(R.color.set_up_text_gray));
			
		}
		
		linearLayout6.setOnClickListener(this);
		linearLayout7.setOnClickListener(this);
		linearLayout8.setOnClickListener(this);
		linearLayout9.setOnClickListener(this);
		linearLayout10.setOnClickListener(this);
		linearLayout11.setOnClickListener(this);
		linearlayout12.setOnClickListener(this);
		linearlayout13.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences sp=homeActivity.getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		Editor editor=sp.edit();
		switch (v.getId()) {
		case R.id.imageView1:
//			this.finish();
			homeActivity.toggle();
			break;
		case R.id.setup_imageview1:
			if(!sp.getBoolean(Constants.LOCK_TO_XINLANG, false)){
				acount=2;
				if(UserStatusUtil.hasXinlangAuth(homeActivity)){
					editor.putBoolean(Constants.LOCK_TO_XINLANG, true);
					setup_image1.setImageResource(R.drawable.checked);
				}else{
					setup_image1.setImageResource(R.drawable.unchecked);
					editor.putBoolean(Constants.LOCK_TO_XINLANG, false);
				}
			}else{
				acount=-1;
				setup_image1.setImageResource(R.drawable.unchecked);
				editor.putBoolean(Constants.LOCK_TO_XINLANG, false);
//				editor.putString("xinlangToken", null);
				editor.remove("xinlangToken");
				Constants.accessToken=null;
			}
			
			break;
		case R.id.setup_imageview3:
			acount=1;
			if(!sp.getBoolean(Constants.PICTURE_SEND_TO_XINLANG, false)){
				if(UserStatusUtil.hasXinlangAuth(homeActivity)){
					setup_image3.setImageResource(R.drawable.checked);
					editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, true);
				}else{
					setup_image3.setImageResource(R.drawable.unchecked);
					editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, false);
				}
			}else{
				editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG, false);
				setup_image3.setImageResource(R.drawable.unchecked);
			}
			break;
		case R.id.setup_imageview4:
			if(!sp.getBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false)){
				
				if(Constants.api==null){
					boolean flag=WeixinShare.regToWeiXin(homeActivity);
					if(!flag){
						Toast.makeText(homeActivity,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						setup_image4.setImageResource(R.drawable.unchecked);
						editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
						return;
					}else{
						editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, true);
						setup_image4.setImageResource(R.drawable.checked);
					}
				}else{
					editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, true);
					setup_image4.setImageResource(R.drawable.checked);
				}
			}else{
				editor.putBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
				setup_image4.setImageResource(R.drawable.unchecked);
			}
			
			break;
		/*case R.id.account_visibility_relativelayout:
			Intent intent=new Intent(homeActivity,AcountVisibilty.class);
			intent.putExtra("acount", acount);
			homeActivity.startActivity(intent);
			break;*/
		case R.id.linearlayout1:
			Intent intent1=new Intent(homeActivity,NewRegisterActivity.class);
			intent1.putExtra("mode", 5);
			homeActivity.startActivity(intent1);
			break;
		case R.id.linearlayout2:
			Intent intent2=new Intent(homeActivity,ReceiverAddressActivity.class);
			homeActivity.startActivity(intent2);
			break;
		case R.id.linearlayout3:
			
			break;
		case R.id.linearlayout4:
			
			break;
		case R.id.linearlayout5:
			
			break;
		case R.id.linearlayout6:
			deleteFile();
			
			break;
		case R.id.linearlayout7:
			if(Constants.realVersion!=null&&StringUtil.canUpdate(homeActivity, Constants.realVersion)){
				Intent intent=new Intent(homeActivity,UpdateAPKActivity.class);
				homeActivity.startActivity(intent);
			}else{
				Toast.makeText(homeActivity, "已是新版本", Toast.LENGTH_LONG).show();
			}
			
			
			break;
		case R.id.linearlayout8:
			Intent intent8=new Intent(homeActivity,FAQActivity.class);
			homeActivity.startActivity(intent8);
			break;
		case R.id.linearlayout9:
			Intent intent9=new Intent(homeActivity,AdviceActivity.class);
			homeActivity.startActivity(intent9);
			break;
		case R.id.linearlayout10:
			
			break;
		case R.id.linearlayout11:
			Intent intent11=new Intent(homeActivity,AboutUsActivity.class);
			homeActivity.startActivity(intent11);
			break;
		case R.id.chose_pet_layout:
			Intent intent12=new Intent(homeActivity,ChosePetActivity.class);
			intent12.putExtra("likers", "36,54,78,25,16,39,47,98,67");
			homeActivity.startActivity(intent12);
			break;
		case R.id.linearlayout12:
			if(Constants.user!=null&&Constants.user.inviter!=0){
				Intent intent13=new Intent(homeActivity,InviteOthersDialogActivity.class);
				intent13.putExtra("mode", 2);
				homeActivity.startActivity(intent13);
			}else
			if(Constants.user!=null&&Constants.user.aniList!=null){
				if(Constants.user.aniList.size()>=10){
					DialogGoRegister dialog=new DialogGoRegister(popupParent, homeActivity,black_layout, 3);
				}else{
					Intent intent13=new Intent(homeActivity,InviteOthersDialogActivity.class);
					intent13.putExtra("mode", 2);
					homeActivity.startActivity(intent13);
				}
			}

			break;
		case R.id.linearlayout13:
			Intent intent14=new Intent(homeActivity,UsersListActivity.class);
			intent14.putExtra("mode", 1);
			homeActivity.startActivity(intent14);
			break;
		}
		editor.commit();
	}
	public void getXinlangToken(boolean getXinlangAuth){
		LogUtil.i("exception", "onResume启动");
		SharedPreferences sp=homeActivity.getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		Editor editor=sp.edit();
		if(getXinlangAuth){
			if(acount==2){
				setup_image1.setImageResource(R.drawable.checked);
				acount=-1;
				editor.putBoolean(Constants.LOCK_TO_XINLANG,true);
			}else if(acount==1){
				setup_image3.setImageResource(R.drawable.checked);
				editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG,true);
			}
			
			
		}else{
			if(acount!=-1){
				setup_image3.setImageResource(R.drawable.unchecked);
				setup_image1.setImageResource(R.drawable.unchecked);
				editor.putBoolean(Constants.PICTURE_SEND_TO_XINLANG,false);
				editor.putBoolean(Constants.LOCK_TO_XINLANG, false);
				acount=-1;
			}
			
		}
		editor.commit();
	};
	
	public void deleteFile(){
		if(showProgress==null){
			showProgress=new ShowProgress(homeActivity, progressLayout);
		}else{
			showProgress.showProgress();
		}
				File file=new File(Constants.Picture_Root_Path);
				if(file.exists()){
					StringUtil.deleteFile(file);
					fileSizeTV.setText("0kb");
				}
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(showProgress!=null)
							showProgress.progressCancel();
						if(HomeFragment.homeFragment!=null){
							HomeFragment.homeFragment.uiRefresh();
						}
						File file=new File(Constants.Picture_Topic_Path);
						if(!file.exists()){
							file.mkdirs();
						}
					}
				}, 2000);
	}
	
	

}
