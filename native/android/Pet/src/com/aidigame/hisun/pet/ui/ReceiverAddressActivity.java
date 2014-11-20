package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.AddressData;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
/**
 * 用户收货地址
 * @author admin
 *
 */
public class ReceiverAddressActivity extends Activity implements OnClickListener{
	NumberPicker provicePicker;
	NumberPicker cityPicker;
	ArrayList<String> provincesList;
	ArrayList<String> cityList;
	int provinceValue=0;
	int cityValue=0;
	String province;
	String city;
	EditText nameET,phoneET,postCodeET,provinceCityET,addressET;
	TextView provinceTV,titleTV,saveTV;
	ImageView back;
	LinearLayout addressLayout,blurLayout;
	String name,phone,postCode,provinceCity,address;
	UserAddress userAddress;
	HandleHttpConnectionException handleHttpConnectionException;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_receiver_address);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		initView();
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		userAddress=new UserAddress();
		if(Constants.user!=null){
			userAddress.aid=Constants.user.currentAnimal.a_id;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//下载地址
					boolean flag=HttpUtil.userAddress(ReceiverAddressActivity.this,userAddress, handleHttpConnectionException.getHandler(ReceiverAddressActivity.this));
					if(flag){
						runOnUiThread(new Runnable() {
							public void run() {
								nameET.setText(userAddress.name);
								phoneET.setText(userAddress.telephone);
								postCodeET.setText(userAddress.zipcode);
								provinceCityET.setText(userAddress.region);
								addressET.setText(""+userAddress.building);
								saveTV.setBackgroundResource(R.drawable.button_green);;
								saveTV.setClickable(true);
							}
						});
					}
				}
			}).start();
		}
		
		provicePicker=(NumberPicker)findViewById(R.id.numberpicker1);
		cityPicker=(NumberPicker)findViewById(R.id.numberpicker2);
		nameET=(EditText)findViewById(R.id.name_et);
		phoneET=(EditText)findViewById(R.id.phone_num_et);
		addressLayout=(LinearLayout)findViewById(R.id.address_layout);
		blurLayout=(LinearLayout)findViewById(R.id.blur_layout);
		
		
//		blurLayout.setBackgroundResource(R.color.orange_red);
		postCodeET=(EditText)findViewById(R.id.post_code_et);
		provinceCityET=(EditText)findViewById(R.id.province_city_et);
		addressET=(EditText)findViewById(R.id.receiver_address_et);
		provinceTV=(TextView)findViewById(R.id.province_tv);
		saveTV=(TextView)findViewById(R.id.save_tv);
		titleTV=(TextView)findViewById(R.id.title_tv);
		back=(ImageView)findViewById(R.id.back);
		
		
		
		
		provinceCityET.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					addressLayout.setVisibility(View.VISIBLE);
					StringUtil.hideSoftKeybord(ReceiverAddressActivity.this);
				}else{
					addressLayout.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		
		
		provincesList=new ArrayList<String>();
		for(int i=0;i<AddressData.PROVINCES.length;i++){
			provincesList.add(AddressData.PROVINCES[i]);
		}
		cityList=new ArrayList<String>();
		for(int i=0;i<AddressData.CITIES[0].length;i++){
			cityList.add(AddressData.CITIES[0][i]);
		}
		provicePicker.setDisplayedValues(AddressData.PROVINCES);
		provicePicker.setMaxValue(AddressData.PROVINCES.length-1);
		provicePicker.setMinValue(0);
		provicePicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				cityPicker.setValue(0);
				provinceValue=newVal;
				province=AddressData.PROVINCES[newVal];
				
				cityPicker.setDisplayedValues(AddressData.CITIES[newVal]);
				cityPicker.setMaxValue(AddressData.CITIES[newVal].length-1);
				cityPicker.setMinValue(0);
				
				
				city=AddressData.CITIES[newVal][0];
				provinceCityET.setText(province+" "+city);
				provinceTV.setText(province);
			}
		});
		cityPicker.setDisplayedValues(AddressData.CITIES[0]);
		cityPicker.setMaxValue(AddressData.CITIES[0].length-1);
		cityPicker.setMinValue(0);
		cityPicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				cityValue=newVal;
				city=AddressData.CITIES[provinceValue][newVal];
				provinceCityET.setText(province+" "+city);
				
			}
		});
		saveTV.setOnClickListener(this);
		back.setOnClickListener(this);
		nameET.addTextChangedListener(new MyTextWtcher());
		phoneET.addTextChangedListener(new MyTextWtcher());
		postCodeET.addTextChangedListener(new MyTextWtcher());
		provinceCityET.addTextChangedListener(new MyTextWtcher());
		addressET.addTextChangedListener(new MyTextWtcher());
		
		province=AddressData.PROVINCES[0];
		city=AddressData.CITIES[0][0];
		provinceCityET.setText(province+" "+city);
		provinceTV.setText(province);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			if(NewHomeActivity.homeActivity!=null){
				ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
				am.moveTaskToFront(NewHomeActivity.homeActivity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
			}else{
				Intent intent=new Intent(this,NewHomeActivity.class);
				this.startActivity(intent);
			}
			
			this.finish();
			break;
		case R.id.save_tv:
			saveAddress();
			break;
		}
	}
	public void checkInputInfo(){
		boolean flag=true;
		name=nameET.getText().toString();
		if(StringUtil.isEmpty(name)){
			flag=false;
		}
		phone=phoneET.getText().toString();
		if(StringUtil.isEmpty(phone)){
			flag=false;
		}
		postCode=postCodeET.getText().toString();
		if(StringUtil.isEmpty(postCode)){
			flag=false;
		}
		provinceCity=provinceCityET.getText().toString();
		if(StringUtil.isEmpty(provinceCity)){
			flag=false;
		}
		address=addressET.getText().toString();
		if(StringUtil.isEmpty(address)){
			flag=false;
		}
		if(flag){
			saveTV.setBackgroundResource(R.drawable.button_green);;
			saveTV.setClickable(true);
		}
		
	}
	private void saveAddress() {
		// TODO Auto-generated method stub
		name=nameET.getText().toString();
		if(StringUtil.isEmpty(name)){
			Toast.makeText(this, "收货人姓名不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		phone=phoneET.getText().toString();
		if(StringUtil.isEmpty(phone)){
			Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		postCode=postCodeET.getText().toString();
		if(StringUtil.isEmpty(postCode)){
			Toast.makeText(this, "邮政编码不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		provinceCity=provinceCityET.getText().toString();
		if(StringUtil.isEmpty(provinceCity)){
			Toast.makeText(this, "所在地区不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		address=addressET.getText().toString();
		if(StringUtil.isEmpty(address)){
			Toast.makeText(this, "详细地址不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		userAddress.building=address;
		userAddress.name=name;
		userAddress.region=provinceCity;
		userAddress.telephone=phone;
		userAddress.zipcode=postCode;
		if(Constants.user!=null){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final  boolean flag=HttpUtil.userAddress(ReceiverAddressActivity.this,userAddress, handleHttpConnectionException.getHandler(ReceiverAddressActivity.this));
				    runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(ReceiverAddressActivity.this,"保存信息成功", Toast.LENGTH_LONG).show();
						}
					});
				}
			}).start();
		}else{
			Toast.makeText(this, "请先注册", Toast.LENGTH_LONG).show();
		}
		
		
	}
	public static class UserAddress{
		/*
		 * name,telephone,zipcode,region,building

		 */
		public String name;
		public String telephone;
		public String zipcode;
		public String region;
		public String building;
		public long aid;
	}
	
	public class MyTextWtcher implements TextWatcher{

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
			checkInputInfo();
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
