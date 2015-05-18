package com.aidigame.hisun.imengstar.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.widget.ShowProgress;
import com.aidigame.hisun.imengstar.widget.WeixinShare;
import com.aidigame.hisun.imengstar.widget.fragment.DialogNote;
import com.aidigame.hisun.imengstar.R;
import com.example.android.bitmapfun.util.DiskLruCache;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 设置界面
 * @author admin
 *
 */
public class SetupActivity extends BaseActivity implements OnClickListener{

	
	public static SetupActivity setupActivity;
	
	
	FrameLayout frameLayout;
	View viewTopWhite;
	ScrollView scrollView;
	public View popupParent;
	public RelativeLayout black_layout;
	TextView fileSizeTV,tv12;
	LinearLayout progressLayout,
	linearLayout6,linearLayout7,linearLayout8,linearLayout9,linearLayout10,linearLayout11,shangNoteLayout,ticketNoteLayout;

	ImageView back,shangNoteIv,ticketNoteIv;
	ImageView iv3;
	public static boolean getXinlangAuth=false;
	ShowProgress showProgress;
	int acount=-1;//1同步发送到新浪微博，2 绑定新浪微博；-1 Activity启动时进入onResume方法，啥也不做。
	
	
	
	Handler handler;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setup);		
        setupActivity=this;
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		initView();
		initListener();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		back=(ImageView)findViewById(R.id.imageView1);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		
		
		fileSizeTV=(TextView)findViewById(R.id.cach_file_size);
		
		iv3=(ImageView)findViewById(R.id.imageView3);
		tv12=(TextView)findViewById(R.id.tv122);
		linearLayout6=(LinearLayout)findViewById(R.id.linearlayout6);
		linearLayout7=(LinearLayout)findViewById(R.id.linearlayout7);
		linearLayout8=(LinearLayout)findViewById(R.id.linearlayout8);
		linearLayout9=(LinearLayout)findViewById(R.id.linearlayout9);
		linearLayout10=(LinearLayout)findViewById(R.id.linearlayout10);
		linearLayout11=(LinearLayout)findViewById(R.id.linearlayout11);
		shangNoteLayout=(LinearLayout)findViewById(R.id.shang_note_layout);
		shangNoteIv=(ImageView)findViewById(R.id.shang_note_iv);
		ticketNoteLayout=(LinearLayout)findViewById(R.id.ticket_note_layout);
		ticketNoteIv=(ImageView)findViewById(R.id.ticket_note_iv);
		progressLayout=(LinearLayout)findViewById(R.id.progresslayout);
				
		setBlurImageBackground();
		setFileSize();
		SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME,Context.MODE_WORLD_WRITEABLE);
		boolean show=sp.getBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
		if(!show){
			shangNoteIv.setImageResource(R.drawable.checked);
		}else{
			shangNoteIv.setImageResource(R.drawable.unchecked);
		}
		show=sp.getBoolean(Constants.GIVE_TICKET_NOTE_SHOW, false);
		if(!show){
			ticketNoteIv.setImageResource(R.drawable.checked);
		}else{
			ticketNoteIv.setImageResource(R.drawable.unchecked);
		}
		
		
		
//		linearLayout11.setBackgroundResource(R.color.orange_red);
		
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
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		viewTopWhite=(View)findViewById(R.id.top_white_view);
		scrollView=(ScrollView)findViewById(R.id.scrollview);
		touchSlop=ViewConfiguration.get(this).getScaledTouchSlop();
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=8;
//		frameLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.blur, options)));
        /*scrollView.setOnTouchListener(new OnTouchListener() {
			
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
		});*/
	}

	private void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(this);
		if(PetApplication.isSuccess){
			iv3.setAlpha(1.0f);
		}else{
			iv3.setAlpha(0.3f);
			
		}
		
		linearLayout6.setOnClickListener(this);
		linearLayout7.setOnClickListener(this);
		linearLayout8.setOnClickListener(this);
		linearLayout9.setOnClickListener(this);
		linearLayout10.setOnClickListener(this);
		linearLayout11.setOnClickListener(this);
		shangNoteLayout.setOnClickListener(this);
		ticketNoteLayout.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences sp=getSharedPreferences("setup", Context.MODE_WORLD_READABLE);
		Editor editor=sp.edit();
		switch (v.getId()) {
		case R.id.imageView1:
			if(isTaskRoot()){
				if(HomeActivity.homeActivity!=null){
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
				}else{
					Intent intent=new Intent(this,HomeActivity.class);
					this.startActivity(intent);
				}
			}
			setupActivity=null;
			
			
			finish();
			System.gc();
			break;
		/*case R.id.account_visibility_relativelayout:
			Intent intent=new Intent(homeActivity,AcountVisibilty.class);
			intent.putExtra("acount", acount);
			homeActivity.startActivity(intent);
			break;*/
		case R.id.linearlayout1:
			Intent intent1=new Intent(this,NewRegisterActivity.class);
			intent1.putExtra("mode", 5);
			this.startActivity(intent1);
			break;
		case R.id.linearlayout3:
			
			break;
		case R.id.linearlayout6:
			deleteFile();
			
			break;
		case R.id.linearlayout7:
			if(StringUtil.isEmpty(Constants.realVersion)){
				Constants.realVersion=sp.getString("real_version", "1.0.0");
			}
			if((Constants.realVersion!=null&&StringUtil.canUpdate(this, Constants.realVersion))||(Constants.VERSION!=null&&StringUtil.canUpdate(this, Constants.VERSION))){
				Intent intent=new Intent(this,UpdateAPKActivity.class);
				this.startActivity(intent);
			}else{
				Toast.makeText(this, "已是新版本", Toast.LENGTH_LONG).show();
			}
			
			
			break;
		case R.id.linearlayout8:
			Intent intent8=new Intent(this,FAQActivity.class);
			this.startActivity(intent8);
			break;
		case R.id.linearlayout9:
			Intent intent9=new Intent(this,AdviceActivity.class);
			this.startActivity(intent9);
			break;
		case R.id.linearlayout10:
			
			break;
		case R.id.linearlayout11:
			Intent intent11=new Intent(this,AboutUsActivity.class);
			this.startActivity(intent11);
			break;
		case R.id.shang_note_layout:
			boolean show=sp.getBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
			if(!show){
				shangNoteIv.setImageResource(R.drawable.unchecked);
				editor.putBoolean(Constants.GIVE_FOOD_NOTE_SHOW, true);
			}else{
				shangNoteIv.setImageResource(R.drawable.checked);
				editor.putBoolean(Constants.GIVE_FOOD_NOTE_SHOW, false);
			}
			break;
		case R.id.ticket_note_layout:
			show=sp.getBoolean(Constants.GIVE_TICKET_NOTE_SHOW, false);
			if(!show){
				ticketNoteIv.setImageResource(R.drawable.unchecked);
				editor.putBoolean(Constants.GIVE_TICKET_NOTE_SHOW, true);
			}else{
				ticketNoteIv.setImageResource(R.drawable.checked);
				editor.putBoolean(Constants.GIVE_TICKET_NOTE_SHOW, false);
			}
			break;
		
		}
		
		editor.commit();
	}
	
	public void deleteFile(){
		if(showProgress==null){
			showProgress=new ShowProgress(this, progressLayout);
		}else{
			showProgress.showProgress();
		}
		DiskLruCache.clearCache(SetupActivity.this, Constants.Picture_Topic_Path);
				File file=new File(Constants.Picture_Root_Path);
				if(file.exists()){
					StringUtil.deleteFile(file);
					fileSizeTV.setText("0kb");
				}
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						File file=new File(Constants.Picture_Root_Path);
						if(!file.exists()){
							file.mkdir();
						}
						String path=Constants.Picture_Root_Path+File.separator+".nomedia";
						File file2=new File(path);
						if(file2.exists()){
							if(file2.isDirectory())
							{
								file2.delete();
								try {
									file2.createNewFile();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else{
							try {
								file2.createNewFile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(showProgress!=null)
							showProgress.progressCancel();
						
						File file1=new File(Constants.Picture_Topic_Path);
						if(!file1.exists()){
							file1.mkdirs();
						}
						
					}
				}, 2000);
	}

	
	
}
