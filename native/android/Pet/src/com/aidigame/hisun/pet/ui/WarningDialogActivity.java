package com.aidigame.hisun.pet.ui;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 举报评论，拉黑弹出框
 * mode 1,举报评论；2，图片；,3，举报用户；4，拉黑用户;5,强制更新弹窗提示;6.用户协议弹出窗
 * @author admin
 *
 */
public class WarningDialogActivity extends Activity {
	int mode=1;
	int img_id;
	int usr_id;
	int talk_id;
	ImageView agreeIV;
	TextView agreeTV;
	Handler handler;
	LinearLayout layout1,agreeLayout,agreeBottomLayout;
	ReportResultListener reportResultListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_warning);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		layout1=(LinearLayout)findViewById(R.id.layout1);
		agreeLayout=(LinearLayout)findViewById(R.id.agree_layout);
		agreeBottomLayout=(LinearLayout)findViewById(R.id.agreelayout);
		mode=getIntent().getIntExtra("mode", 1);
		if(mode==1){
			img_id=getIntent().getIntExtra("img_id", -1);
		}else if(mode==2){
			img_id=getIntent().getIntExtra("img_id", -1);
		}else if(mode==3){
			usr_id=getIntent().getIntExtra("usr_id", -1);
		}else if(mode==4){
			talk_id=getIntent().getIntExtra("talk_id", -1);
		}
		TextView titleTV=(TextView)findViewById(R.id.title_tv);
		TextView msgTV=(TextView)findViewById(R.id.msg_tv);
		TextView cancelTV=(TextView)findViewById(R.id.cancel_tv);
		TextView sureTV=(TextView)findViewById(R.id.sure_tv);
		
		if(mode==2){
			titleTV.setText("举报照片");
			msgTV.setText("举报违规照片");
		}else if(mode==3){
			titleTV.setText("举报此用户");
			msgTV.setText("此用户涉及敏感内容，官方会予以删除");
		}else if(mode==4){
			titleTV.setText("拉黑TA?");
			msgTV.setText("拉黑用户可到设置里查看");
		}else if(mode==5){
			titleTV.setText("提示");
			msgTV.setText("不更新会退出应用");
			sureTV.setText("取消");
			cancelTV.setText("确认");
		}else if(mode==6){
			layout1.setVisibility(View.GONE);
			agreeLayout.setVisibility(View.VISIBLE);
		}else if(mode==7){
			layout1.setVisibility(View.GONE);
			agreeLayout.setVisibility(View.VISIBLE);
			agreeBottomLayout.setVisibility(View.GONE);
		}
		findViewById(R.id.close_iv).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				if(mode==1||mode==2)
				if(NewShowTopicActivity.newShowTopicActivity!=null){
//					ShowTopicActivity.showTopicActivity.reportLayout.setVisibility(View.INVISIBLE);
				}
				if(mode==3)
				if(UserDossierActivity.userDossierActivity!=null){
					UserDossierActivity.userDossierActivity.moreLayout.setVisibility(View.INVISIBLE);
					UserDossierActivity.userDossierActivity.moreParentLayout.setBackgroundDrawable(null);
				}
				if(mode==4){
				
				}
				if(mode==5){
						/*if(UpdateAPKActivity.updateAPKActivity!=null){
							UpdateAPKActivity.updateAPKActivity.finish();
						}*/
						
						finish();
						/*if(NewHomeActivity.homeActivity!=null){
							NewHomeActivity.homeActivity.finish();
						}*/
				}
			}
		});
		cancelTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				if(mode==1||mode==2)
				if(NewShowTopicActivity.newShowTopicActivity!=null){
//					ShowTopicActivity.showTopicActivity.reportLayout.setVisibility(View.INVISIBLE);
				}
				if(mode==3)
					if(UserDossierActivity.userDossierActivity!=null){
						UserDossierActivity.userDossierActivity.moreLayout.setVisibility(View.INVISIBLE);
						UserDossierActivity.userDossierActivity.moreParentLayout.setBackgroundDrawable(null);
					}
               if(mode==4){
					
				}
               if(mode==5){
            	   Intent intent=new Intent(WarningDialogActivity.this,WarningDialogActivity.class);
            	   WarningDialogActivity.this.startActivity(intent);
            	   finish();
            	   /*
            	    *显示进度条 
            	    */
               }
			}
		});
		sureTV.setOnClickListener(new OnClickListener() {
	
	     @Override
	     public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mode==1){
			reportImage();
		}else if(mode==2){
			reportImage();
		}else if(mode==3){
			reportUser();
		}else if(mode==4){
			blockOther();
		}else if(mode==5){
			if(UpdateAPKActivity.updateAPKActivity!=null){
				UpdateAPKActivity.updateAPKActivity.finish();
			}
			
			finish();
			if(HomeActivity.homeActivity!=null){
				HomeActivity.homeActivity.finish();
			}
			
		}
	     }
        });
		
		initAgreementView();
	}
	boolean isAgree=false;
	private void initAgreementView() {
		// TODO Auto-generated method stub
		agreeIV=(ImageView)findViewById(R.id.agree_iv);
		agreeTV=(TextView)findViewById(R.id.agree_tv);
		
		agreeTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		agreeTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
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
				if(ChoseAcountTypeActivity.choseAcountTypeActivity!=null){
					ChoseAcountTypeActivity.choseAcountTypeActivity.agreeMent(isAgree);
				}
			}
		});
	}
	/**
	 * 举报图片,或平乱
	 */
	public void reportImage(){
		if(img_id==-1){
			finish();
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final boolean flag=HttpUtil.reportPicture(handler, img_id, WarningDialogActivity.this);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(flag){
								Toast.makeText(WarningDialogActivity.this, "举报成功", Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(WarningDialogActivity.this, "举报失败", Toast.LENGTH_LONG).show();
							}
							finish();
							if(NewShowTopicActivity.newShowTopicActivity!=null){
//								ShowTopicActivity.showTopicActivity.reportLayout.setVisibility(View.INVISIBLE);
							}
						}
					});
				}
			}).start();
		}
	}
	/**
	 * 举报用户
	 */
	public void reportUser(){
		if(img_id==-1){
			finish();
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final boolean flag=HttpUtil.reportUser(handler, usr_id, WarningDialogActivity.this);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(flag){
								Toast.makeText(WarningDialogActivity.this, "举报成功", Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(WarningDialogActivity.this, "举报失败", Toast.LENGTH_LONG).show();
							}
							finish();
							if(UserDossierActivity.userDossierActivity!=null){
								UserDossierActivity.userDossierActivity.moreLayout.setVisibility(View.INVISIBLE);
								UserDossierActivity.userDossierActivity.moreParentLayout.setBackgroundDrawable(null);
							}
							
						}
					});
				}
			}).start();
		}
	}
	/**
	 * 拉黑用户
	 */
	public void blockOther(){
		if(img_id==-1){
			finish();
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final boolean flag=HttpUtil.blockOther(handler, talk_id, WarningDialogActivity.this);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(flag){
								Toast.makeText(WarningDialogActivity.this, "拉黑成功", Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(WarningDialogActivity.this, "拉黑失败", Toast.LENGTH_LONG).show();
							}
							finish();
							
							
						}
					});
				}
			}).start();
		}
	}
	public void setReportListener(ReportResultListener reportResultListener){
		this.reportResultListener=reportResultListener;
	}
	public static interface ReportResultListener{
		void reportListener();
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
