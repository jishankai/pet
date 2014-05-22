package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;

public class SubmitPictureActivity extends Activity implements OnClickListener{
	Button backBt,recodeBt,submitBt;
	ImageView imageView;
	EditText editText;
	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_picture);
		initView();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		backBt=(Button)findViewById(R.id.button1);
		recodeBt=(Button)findViewById(R.id.button2);
		submitBt=(Button)findViewById(R.id.button3);
		imageView=(ImageView)findViewById(R.id.imageView1);
		editText=(EditText)findViewById(R.id.editText1);
		textView=(TextView)findViewById(R.id.textView2);
		imageView.setImageBitmap(HandlePictureActivity.handlingBmp);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		backBt.setOnClickListener(this);
		recodeBt.setOnClickListener(this);
		submitBt.setOnClickListener(this);
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String str=editText.getText().toString();
				if(str!=null)
				textView.setText(str);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			this.finish();
			break;
		case R.id.button2:
			
			break;
		case R.id.button3:
			
			break;
		}
	}

}
