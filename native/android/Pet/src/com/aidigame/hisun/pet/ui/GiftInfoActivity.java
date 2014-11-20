package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
/**
 * 礼物详情界面
 * @author admin
 *
 */
public class GiftInfoActivity extends Activity implements OnClickListener{
	Gift gift;
	ImageView back,giftIV;
	TextView giftDesTV,priceTV,addLikeTV,des2TV,scopeTV,standardTV,gperiodTV,postModeTV,
	         decreaseTV,numTV,addTV,buyTV;
	int num=1;
	LinearLayout blurLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_gift_info);
		gift=(Gift)getIntent().getSerializableExtra("gift");
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		back=(ImageView)findViewById(R.id.back);
		giftIV=(ImageView)findViewById(R.id.imageView1);
		giftDesTV=(TextView)findViewById(R.id.textView1);
		priceTV=(TextView)findViewById(R.id.textView2);
		addLikeTV=(TextView)findViewById(R.id.textView3);
		des2TV=(TextView)findViewById(R.id.textView4);
		scopeTV=(TextView)findViewById(R.id.textView5);
		standardTV=(TextView)findViewById(R.id.textView6);
		gperiodTV=(TextView)findViewById(R.id.textView7);
		postModeTV=(TextView)findViewById(R.id.textView8);
		decreaseTV=(TextView)findViewById(R.id.textView10);
		numTV=(TextView)findViewById(R.id.textView11);
		addTV=(TextView)findViewById(R.id.textView12);
		buyTV=(TextView)findViewById(R.id.textView13);
		back.setOnClickListener(this);
		decreaseTV.setOnClickListener(this);
		addTV.setOnClickListener(this);
		buyTV.setOnClickListener(this);
		setBlurImageBackground();
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		blurLayout=(LinearLayout)findViewById(R.id.layout);
       
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.textView10:
			if(num>0){
				num--;
			}
			numTV.setText(""+num);
			break;
		case R.id.textView12:
			num++;
			numTV.setText(""+num);
			break;
		case R.id.textView13:
			Toast.makeText(this,"购买成功", Toast.LENGTH_SHORT).show();
			break;
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
