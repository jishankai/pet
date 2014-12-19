package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
/**
 * 选择账户类型，1.认养宠物，2.建立王国
 * 界面有两种显示模式，1.汪星，2.喵星
 * @author admin
 *
 */
public class ChoseAcountTypeActivity extends Activity {
	ImageView noPetIV,hasPetIV,backIV,agreeIV;
	TextView agreeTV;
	LinearLayout agreeLayout;
	HandleHttpConnectionException handleHttpConnectionException;
	public static ChoseAcountTypeActivity choseAcountTypeActivity;
	boolean isAgree=false;//是否同意用户协议
	int mode;
	int from;//默认值为0，进行注册；1，已经注册过
	boolean isBind=false;//是否绑定新浪微博或微信账号
	User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_chose_acount_type);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		choseAcountTypeActivity=this;
		isBind=getIntent().getBooleanExtra("isBind", false);
		if(isBind)user=(User)getIntent().getSerializableExtra("user");
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		noPetIV=(ImageView)findViewById(R.id.no_pet_iv);
		hasPetIV=(ImageView)findViewById(R.id.has_pet_iv);
		backIV=(ImageView)findViewById(R.id.imageView4);
		from=getIntent().getIntExtra("from", 0);
		agreeIV=(ImageView)findViewById(R.id.agree_iv);
		agreeTV=(TextView)findViewById(R.id.agree_tv);
		agreeLayout=(LinearLayout)findViewById(R.id.agree_layout);
		agreeTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		if(from!=0)agreeLayout.setVisibility(View.INVISIBLE);
		
		agreeTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ChoseAcountTypeActivity.this,WarningDialogActivity.class);
				intent.putExtra("mode", 6);
				ChoseAcountTypeActivity.this.startActivity(intent);
			}
		});
		agreeIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isAgree){
					isAgree=true;
					agreeIV.setImageResource(R.drawable.box_chose_red);
				}else{
					isAgree=false;
					agreeIV.setImageResource(R.drawable.box_chose_gray);
				}
			}
		});
		SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		backIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChoseAcountTypeActivity.this.finish();
			}
		});
		hasPetIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(from==0){
					if(!isAgree){
						Toast.makeText(ChoseAcountTypeActivity.this, "您是否同意用户协议，同意请在下方勾选", Toast.LENGTH_LONG).show();
						return;
					}
				}
				Intent intent=new Intent(ChoseAcountTypeActivity.this, NewRegisterActivity.class);
				intent.putExtra("mode", 3);//创建狗或猫的联萌
				intent.putExtra("from", from);
				intent.putExtra("isBind", isBind);
				if(isBind)
				intent.putExtra("user", user);
				ChoseAcountTypeActivity.this.startActivity(intent);
			}
		});
		noPetIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(from==0){
					if(!isAgree){
						Toast.makeText(ChoseAcountTypeActivity.this, "您是否同意用户协议，同意请在下方勾选", Toast.LENGTH_LONG).show();
						return;
					}
				}
				Intent intent=new Intent(ChoseAcountTypeActivity.this,ChoseKingActivity.class);
				mode=2;
				intent.putExtra("mode", mode);
				intent.putExtra("from", from);
				intent.putExtra("isBind", isBind);
				intent.putExtra("user", user);
				ChoseAcountTypeActivity.this.startActivity(intent);
//				Toast.makeText(ChoseAcountTypeActivity.this, "暂不支持", Toast.LENGTH_LONG).show();
			}
		});
	}
	public void agreeMent(boolean flag){
		isAgree=flag;
		if(isAgree){
			agreeIV.setImageResource(R.drawable.box_chose_red);
		}else{
			agreeIV.setImageResource(R.drawable.box_chose_gray);
		}
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
