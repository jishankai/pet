package com.aidigame.hisun.imengstar.ui;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.R;

import android.app.Activity;
import android.os.Bundle;
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
public class Dialog2Activity extends Activity implements OnClickListener{
	ImageView closeIv,arrowIv;
	int mode=1;//1.捧萌星;2,捧星金币不足
	int goldNum=0;//捧萌星需要花费的金币
	TextView note1Tv,note2Tv,note3Tv,button2;
	public static Dialog2ActivityListener listener;
	
	boolean isWeixin=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_dialog2);
		mode=getIntent().getIntExtra("mode", 1);
		goldNum=getIntent().getIntExtra("num", 0);
		closeIv=(ImageView)findViewById(R.id.close);
		note1Tv=(TextView)findViewById(R.id.note_tv1);
		note2Tv=(TextView)findViewById(R.id.note_tv2);
		note3Tv=(TextView)findViewById(R.id.note_tv3);
		if(mode==1){
			if(goldNum<=0){
				note3Tv.setVisibility(View.GONE);
				
			}else{
				note3Tv.setText("温馨提示：本次捧星会消耗您"+goldNum+"个金币哦~");
			}
		}
		button2=(TextView)findViewById(R.id.register_tv);
		
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
		
	}
	/**
	 * 新浪或微信账号没有绑定
	 */
	private void initView4() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 修改地址
	 */
	private void initView3() {
		// TODO Auto-generated method stub

	}
	/**
	 * 捧萌星，金币不足
	 */
	 private void initView2() {
		// TODO Auto-generated method stub
		note1Tv.setText("本次成功捧星或创建萌星");
		note2Tv.setText("需要消耗"+goldNum+"金币哦~");
		note3Tv.setText("您的余额不足~");
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

			default:
				break;
				
			}
			
		}
		public static void setDialog3ActivityListener(Dialog2ActivityListener listener){
			Dialog2Activity.listener=listener;
		}
		public static interface Dialog2ActivityListener{
			void onClose();
			void onButtonTwo();
		}
}
