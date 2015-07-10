package com.aidigame.hisun.imengstar.ui;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 弹出框样式3
 * @author admin
 *
 */
public class Dialog3Activity extends Activity implements OnClickListener{
	ImageView closeIv,arrowIv;
	int mode=1;//1,设置密码;2,兑换礼物，口粮不足提示；3,去设置地址;4,账号没有绑定
	TextView note1Tv,note2Tv,button1,button2;
	public static Dialog3ActivityListener listener;
	
	boolean isWeixin=false;
	LinearLayout choiceOneLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_dialog3);
		mode=getIntent().getIntExtra("mode", 1);
		closeIv=(ImageView)findViewById(R.id.close);
		note1Tv=(TextView)findViewById(R.id.note_tv1);
		note2Tv=(TextView)findViewById(R.id.note_tv2);
		button1=(TextView)findViewById(R.id.button1);
		button2=(TextView)findViewById(R.id.register_tv);
		arrowIv=(ImageView)findViewById(R.id.arrow_iv);
		choiceOneLayout=(LinearLayout)findViewById(R.id.choice_one_layout);
		if(mode==1){
			isWeixin=getIntent().getBooleanExtra("isWeixin", false);
			note1Tv.setVisibility(View.GONE);
			note2Tv.setText("若您尚未设置登陆码，成功切换账号后，当前账号会丢失哦~");
			note2Tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		}
		if(mode==2){
			initView2();
		}
		
		if(mode==3){
			initView3();
		}
		if(mode==4){
			initView4();
		}
		closeIv.setOnClickListener(this);
		button2.setOnClickListener(this);
		choiceOneLayout.setOnClickListener(this);
		
	}
	/**
	 * 新浪或微信账号没有绑定
	 */
	private void initView4() {
		// TODO Auto-generated method stub
		boolean isWeixin=getIntent().getBooleanExtra("isWeixin", true);
		note1Tv.setVisibility(View.GONE);
		if(isWeixin){
			note2Tv.setText("当前微信账号没有注册过应用呢~");
		}else{
			note2Tv.setText("当前微博账号没有注册过应用呢~");
		}
		button1.setText("试试昵称登录吧");
		button2.setText("哎~好吧");
		arrowIv.setVisibility(View.GONE);
		button1.setTextColor(getResources().getColor(R.color.dialog_text_gray));
		
	}
	/**
	 * 修改地址
	 */
	private void initView3() {
		// TODO Auto-generated method stub
		note1Tv.setText("恭喜您兑换成功啦！");
		note2Tv.setText("即将为您备货，快去确认");
		button1.setText("收货地址");
        button2.setText("知道啦");

	}
	/**
	 * 兑换口粮，口粮数目不足，提示
	 */
	 private void initView2() {
		// TODO Auto-generated method stub
		choiceOneLayout.setVisibility(View.GONE);
		note1Tv.setText("储粮不足哟，再去攒攒吧~");
		note2Tv.setVisibility(View.GONE);
		button2.setText("哎~好吧");
	}
	@Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(listener!=null){
			listener.onClose();
		}
	}
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.close:
				
				if(listener!=null){
					listener.onClose();
				}
				
				finish();
				System.gc();
				break;
			case R.id.register_tv:
				
				if(listener!=null){
					listener.onButtonTwo();
				}
				
				finish();
				System.gc();
				break;
			case R.id.choice_one_layout:
				if(mode!=4){
					
					
					finish();
					System.gc();
				}
				if(listener!=null){
					listener.onButtonOne();
				}
				break;

			default:
				break;
				
			}
			
		}
		public static void setDialog3ActivityListener(Dialog3ActivityListener listener){
			Dialog3Activity.listener=listener;
		}
		public static interface Dialog3ActivityListener{
			void onClose();
			void onButtonOne();
			void onButtonTwo();
		}
}
