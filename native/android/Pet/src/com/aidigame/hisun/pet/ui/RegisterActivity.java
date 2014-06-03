package com.aidigame.hisun.pet.ui;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.UiUtil;

public class RegisterActivity extends Activity {
	EditText et1,et4;
	Spinner sp1,sp2;
	Button bt1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_register);
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		et1=(EditText)findViewById(R.id.editText1);
		sp1=(Spinner)findViewById(R.id.editText2);
		sp2=(Spinner)findViewById(R.id.editText3);
		et4=(EditText)findViewById(R.id.editText4);
		bt1=(Button)findViewById(R.id.button1);
	}
	private void initListener() {
		// TODO Auto-generated method stub
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name=null;
				try {
					name = new String(et1.getText().toString().getBytes("UTF-8"),Charset.forName("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				String name=et1.getText().toString();
				int position1=sp1.getSelectedItemPosition();
				int position2=sp2.getSelectedItemPosition();
				if(position1==AdapterView.INVALID_POSITION){
					Toast.makeText(RegisterActivity.this, "", 5000).show();
					return;
				}
				if(position2==AdapterView.INVALID_POSITION){
					Toast.makeText(RegisterActivity.this, "", 5000).show();
					return;
				}
				String gender=""+(position1+1);
				String classs=""+(position2+1);
				String age=et4.getText().toString();
				if(isEmpty(name)){
					return;
				}
				if(isEmpty(gender)){
					return;
				}
				if(isEmpty(classs)){
					return;
				}
				if(isEmpty(age)){
					return;
				}
				final User user=new User();
				user.nickName=name;
				user.classs=Integer.parseInt(classs);
				user.gender=Integer.parseInt(gender);
				user.age=Integer.parseInt(age);
				PetApplication.petApp.user=user;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.register(user);
					}
				}).start();
			}
		});
	}
	public boolean isEmpty(String str){
		if(str==null||"".equals(str)||" ".equals(str)){
			return true;
		}
		return false;
	}

}
