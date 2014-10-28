package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.UiUtil;
/**
 * 选择账户类型，1.认养宠物，2.建立王国
 * 界面有两种显示模式，1.汪星，2.喵星
 * @author admin
 *
 */
public class ChoseAcountTypeActivity extends Activity {
	ImageView noPetIV,hasPetIV,backIV;
	HandleHttpConnectionException handleHttpConnectionException;
	public static ChoseAcountTypeActivity choseAcountTypeActivity;
	int mode;
	int from;//默认值为0，进行注册；1，已经注册过
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_chose_acount_type);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		choseAcountTypeActivity=this;
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		noPetIV=(ImageView)findViewById(R.id.no_pet_iv);
		hasPetIV=(ImageView)findViewById(R.id.has_pet_iv);
		backIV=(ImageView)findViewById(R.id.imageView4);
		from=getIntent().getIntExtra("from", 0);
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
				Intent intent=new Intent(ChoseAcountTypeActivity.this, NewRegisterActivity.class);
				intent.putExtra("mode", 3);//创建狗或猫的联萌
				intent.putExtra("from", from);
				ChoseAcountTypeActivity.this.startActivity(intent);
			}
		});
		noPetIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ChoseAcountTypeActivity.this,ChoseKingActivity.class);
				mode=2;
				intent.putExtra("mode", mode);
				intent.putExtra("from", from);
				ChoseAcountTypeActivity.this.startActivity(intent);
//				Toast.makeText(ChoseAcountTypeActivity.this, "暂不支持", Toast.LENGTH_LONG).show();
			}
		});
	}

}
