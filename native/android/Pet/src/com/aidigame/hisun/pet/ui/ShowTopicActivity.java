package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Video;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.CommentListViewAdapter;
import com.aidigame.hisun.pet.adapter.ShowTopicHorizontalAdapter;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.CircleView;
import com.aidigame.hisun.pet.view.HorizontalListView2;
import com.aidigame.hisun.pet.view.LinearLayoutForListView;
import com.aidigame.hisun.pet.widget.CreateTitle;
import com.aidigame.hisun.pet.widget.ShowDialog;
import com.aidigame.hisun.pet.widget.ShowFocusTopics;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.ShowWaterFull1;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.XinlangShare;

public class ShowTopicActivity extends Activity implements OnClickListener{
	CircleView bt2;
	TextView tv1,tv2,tv3,tv4,tv31;
	ImageView imageView,bt4,bt1,bt3,add_comment;
	LinearLayout addcommentLayout;
	public static ShowTopicActivity showTopicActivity;
//	ListView comment_listView;
	LinearLayoutForListView linearLayoutForListView;
	LinearLayout progressLayout;
	ShowProgress showProgress;
	CommentListViewAdapter commentAdapter;
	EditText commentET;
	TextView send_comment_tv;
	boolean show_add_comment=false;
	boolean show_shareLayout=false;
//	HorizontialListView horizontialListView;
	HorizontalListView2 horizontialListView;
	ShowTopicHorizontalAdapter adapter;
	LinearLayout linearLayout1;
	LinearLayout linearLayout2;
	ScrollView linearLayout32;
	RelativeLayout relativeLayout1,linearLayout33;
	boolean judgeFlag=true;
	boolean hiden=false;
	CreateTitle createTitle;
	public static final int DOWNLOAD_IMAGE_TX=2;
	
	LinearLayout shareLayout;
	TextView tv5;
	ImageView imageView2,imageView3,imageView4;
	
	
	
	UserImagesJson.Data data;
	public static ArrayList<UserImagesJson.Data> datas;
	ShowWaterFull1 showWaterFull1;
	ShowFocusTopics showFocusTopics;
	ScanActivitys scanActivitys;
	OtherUserTopicActivity otherUserTopicActivity;
	UserHomepageActivity userHomepageActivity;
	int currentPosition=0;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==Constants.ERROR_MESSAGE){
				ShowDialog.show((String)msg.obj, ShowTopicActivity.this);
			}else if(msg.what==44){
				if(adapter!=null){
					if(data.likers_icons_urls!=null)
					adapter.updateAdapter(data.likers_icons_urls);
					adapter.notifyDataSetChanged();
				}
				if(data.likersString!=null&&data.likersString.contains(""+Constants.user.userId)){
					bt4.setImageResource(R.drawable.heart_red);
				}else{
					bt4.setImageResource(R.drawable.heart_white);
				}
				tv4.setText(""+msg.arg1);
			}else if(msg.what==1){
				String path=(String)msg.obj;
				if(path!=null){
					data.path=path;
					
					loadBitmap(data);
					
				}else{
					if(imageView.getHeight()==0){
						handler.sendEmptyMessageAtTime(1, 20);
						return;
					}
					loadBitmap(data);
				}
			}else{
				if(msg.what==DOWNLOAD_IMAGE_TX){
					String pathStr=(String)msg.obj;
					if(pathStr!=null){
						data.user.iconPath=pathStr;
						BitmapFactory.Options options=new BitmapFactory.Options();
						options.inSampleSize=8;
						FileInputStream fis=null;
						try {
							fis=new FileInputStream(pathStr);
							bt2.setImageBitmap(BitmapFactory.decodeStream(fis,null,options));
						} catch (FileNotFoundException e) {
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
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_show_topic);
		//获取用户相关信息
		if(Constants.isSuccess&&Constants.user==null){
			downLoadUserInfo();
		}
		showTopicActivity=this;
		initView();
		initListener();
		setViews();
	}
	private void initView() {
		// TODO Auto-generated method stub
		bt1=(ImageView)findViewById(R.id.button1);
		bt3=(ImageView)findViewById(R.id.button3);
		bt2=(CircleView)findViewById(R.id.button2);
		bt4=(ImageView)findViewById(R.id.button4);
		horizontialListView=(HorizontalListView2)findViewById(R.id.show_topic_listview);
		progressLayout=(LinearLayout)findViewById(R.id.progress);
		progressLayout.setVisibility(View.INVISIBLE);
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv3=(TextView)findViewById(R.id.textView3);
		tv4=(TextView)findViewById(R.id.textView4);
		tv31=(TextView)findViewById(R.id.textView31);
		imageView=(ImageView)findViewById(R.id.imageView1);
		linearLayout33=(RelativeLayout)findViewById(R.id.linearlayout33);
		commentET=(EditText)findViewById(R.id.edit_comment);
//		comment_listView=(ListView)findViewById(R.id.comments_listview);
		linearLayoutForListView=(LinearLayoutForListView)findViewById(R.id.comments_listview);
		send_comment_tv=(TextView)findViewById(R.id.send_comment);
		send_comment_tv.setOnClickListener(this);
		linearLayout33.setClickable(true);
		add_comment=(ImageView)findViewById(R.id.button44);
		add_comment.setOnClickListener(this);
		addcommentLayout=(LinearLayout)findViewById(R.id.add_comment_linearlayout);
        
		data=(UserImagesJson.Data)getIntent().getSerializableExtra("data");
		getFrom();
		if(datas!=null)
		currentPosition=datas.indexOf(data);
		
		
		
		
		
		

		linearLayout1=(LinearLayout)findViewById(R.id.linearLayout1);
		linearLayout2=(LinearLayout)findViewById(R.id.linearlayout2);
		linearLayout32=(ScrollView)findViewById(R.id.linearlayout32);
		relativeLayout1=(RelativeLayout)findViewById(R.id.relativeLayout1);
//		createTitle=new CreateTitle(this,linearLayout1);
		
		
		shareLayout=(LinearLayout)findViewById(R.id.share_linearlayout);
		tv5=(TextView)findViewById(R.id.textView7);
		imageView2=(ImageView)findViewById(R.id.imageView2);
		imageView3=(ImageView)findViewById(R.id.imageView3);
		imageView4=(ImageView)findViewById(R.id.imageView4);
		
		
	}
	private void initListener(){
		
		linearLayout33.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(data.likersString!=null){
					Intent intent=new Intent(ShowTopicActivity.this,UsersListActivity.class);
					intent.putExtra("likers", data.likersString);
					intent.putExtra("title", "用户列表");
					ShowTopicActivity.this.startActivity(intent);
//					ShowTopicActivity.this.finish();
				}
			}
		});
		horizontialListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(data.likersString!=null){
					Intent intent=new Intent(ShowTopicActivity.this,UsersListActivity.class);
					intent.putExtra("likers", data.likersString);
					intent.putExtra("title", "用户列表");
					ShowTopicActivity.this.startActivity(intent);
//					ShowTopicActivity.this.finish();
				}
			}
		});
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		imageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return detector.onTouchEvent(event);
			}
		});
		linearLayout2.setOnClickListener(this);
		
		tv5.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView3.setOnClickListener(this);
		imageView4.setOnClickListener(this);
	}
	public void setViews(){
		show_add_comment=false;
		addcommentLayout.setVisibility(View.GONE);
		if(data.listComments!=null){
			commentAdapter=new CommentListViewAdapter(this, data.listComments);
		}else{
			commentAdapter=new CommentListViewAdapter(this, new ArrayList<UserImagesJson.Comments>());
		}
//		comment_listView.setAdapter(commentAdapter);
		linearLayoutForListView.setAdapter(commentAdapter);
		if(data.user!=null){
			tv1.setText(""+data.user.nickName);
			 final SharedPreferences sp=this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
			 
			    if( data.user!=null&&data.user.race!=null&&sp.contains(data.user.race)){
				  tv2.setText(""+sp.getString(data.user.race, ""));
			   }else{
				   new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(Integer.parseInt(data.user.race)>301)return;
						boolean flag=HttpUtil.getRaceType(ShowTopicActivity.this);
						if(flag){
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									tv2.setText(sp.getString(data.user.race, ""));
								}
							});
						}
					}
				}).start();
			   }
			tv4.setText(""+data.likes);
			tv31.setText(StringUtil.timeFormat(data.create_time));
			if(data.likers_icons_urls!=null&&data.likers_icons_urls.size()>0){
				if(adapter!=null){
					adapter.updateAdapter(data.likers_icons_urls);
					adapter.notifyDataSetChanged();
					linearLayout33.setVisibility(View.VISIBLE);
				}else{
					adapter=new ShowTopicHorizontalAdapter(this, data.likers_icons_urls);
					horizontialListView.setAdapter(adapter);
					linearLayout33.setVisibility(View.VISIBLE);
				}
				
				
			}else{
				adapter=new ShowTopicHorizontalAdapter(this, new ArrayList<String>());
				horizontialListView.setAdapter(adapter);
				linearLayout33.setVisibility(View.GONE);
			}
			
			if(!StringUtil.judgeImageExists(Constants.Picture_ICON_Path+File.separator+data.user.iconUrl)){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(!StringUtil.isEmpty(data.user.iconUrl))
						HttpUtil.downloadIconImage(Constants.USER_DOWNLOAD_TX, data.user.iconUrl, handler,ShowTopicActivity.this);
					}
				}).start();
			}else{
				data.user.iconPath=Constants.Picture_ICON_Path+File.separator+data.user.iconUrl;
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize=8;
				options.inPreferredConfig=Bitmap.Config.RGB_565;
				options.inPurgeable=true;
				options.inInputShareable=true;
				FileInputStream fis=null;
				try {
					fis=new FileInputStream(data.user.iconPath);
					bt2.setImageBitmap(BitmapFactory.decodeStream(fis,null,options));
				} catch (FileNotFoundException e) {
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
		if(!StringUtil.judgeImageExists(Constants.Picture_Topic_Path+File.separator+data.url)){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpUtil.downloadImage(Constants.UPLOAD_IMAGE_RETURN_URL, data.url, handler,ShowTopicActivity.this);
				}
			}).start();
		}else{
			data.path=Constants.Picture_Topic_Path+File.separator+data.url;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(imageView.getHeight()==0){
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					handler.sendEmptyMessage(1);
				}
			}).start();
			
		}
		
		if(data.likersString!=null&&data.likersString.contains(""+Constants.user.userId)){
			bt4.setImageResource(R.drawable.heart_red);
		}else{
			bt4.setImageResource(R.drawable.heart_white);
		}
	}
	boolean sendingComment=false;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			/*Intent intent=new Intent(this,HomeActivity.class);
			this.startActivity(intent);*/
			if("HomeActivity".equals(getIntent().getStringExtra("from"))){
//			if(HomeActivity.homeActivity!=null){
////				HomeActivity.homeActivity.finish();
//			}else{
				Intent intentHome=new Intent(this,HomeActivity.class);
				this.startActivity(intentHome);
//			}
			}
			this.finish();
			break;
		case R.id.button2:
			if(data.user==null||data.usr_id==Constants.user.userId){
				if(UserHomepageActivity.userHomepageActivity==null){
					Intent intent1=new Intent(this,UserHomepageActivity.class);
					this.startActivity(intent1);
				}else{
					UserHomepageActivity.userHomepageActivity.finish();
					Intent intent1=new Intent(this,UserHomepageActivity.class);
					this.startActivity(intent1);
				}
				
//				this.finish();
			}else{
				if(OtherUserTopicActivity.otherUserTopicActivity==null){
					Intent intent1=new Intent(this,OtherUserTopicActivity.class);
					intent1.putExtra("data", data);
					this.startActivity(intent1);
				}else{
					OtherUserTopicActivity.otherUserTopicActivity.finish();
					Intent intent1=new Intent(this,OtherUserTopicActivity.class);
					intent1.putExtra("data", data);
					this.startActivity(intent1);
				}
			}
			break;
		/*case R.id.textView1:
			
			
		case R.id.textView2:

			break;*/
		case R.id.button3:
			if(!UserStatusUtil.isLoginSuccess(this))return;
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			if(show_add_comment){
				show_add_comment=false;
				commentET.setEnabled(false);;
				Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				addcommentLayout.clearAnimation();
				addcommentLayout.setAnimation(animation);;
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						addcommentLayout.setVisibility(View.GONE);
					}
				}, 1000);
				
			}
				show_shareLayout=true;
				TranslateAnimation trAnimation=new TranslateAnimation(0, 0, shareLayout.getHeight(),0 );
				trAnimation.setDuration(1000);
				shareLayout.clearAnimation();
				shareLayout.setAnimation(trAnimation);
				shareLayout.setVisibility(View.VISIBLE);
				trAnimation.start();
				
			
			
			
			break;
		case R.id.button4:
			
			break;
        case R.id.button44:
			if(show_add_comment){
//				show_add_comment=false;
//				addcommentLayout.setVisibility(View.GONE);
			}else{
				show_add_comment=true;
				commentET.setEnabled(true);;
				if(show_shareLayout){
					show_shareLayout=false;
					TranslateAnimation tAnimation=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
					tAnimation.setDuration(1000);
					shareLayout.clearAnimation();
					shareLayout.setAnimation(tAnimation);
					tAnimation.start();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							shareLayout.setVisibility(View.INVISIBLE);
						}
					}, 1000);
					
				}
				
				Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
				addcommentLayout.setAnimation(animation);
				animation.start();
				addcommentLayout.setVisibility(View.VISIBLE);
				
				
			}

			break;
        case R.id.send_comment:
        	if(sendingComment){
        		Toast.makeText(this, "正在发送评论", Toast.LENGTH_LONG).show();
        		return;
        	}
        	final String comment=commentET.getText().toString();
        	commentET.setText("");
        	commentET.setHint("写个评论呗");
        	if(StringUtil.isEmpty(comment)){
        		Toast.makeText(this, "评论内容不能为空。", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	if("写个评论呗".equals(comment)){
        		Toast.makeText(this, "评论内容不能为空。", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	comment.trim();
        	commentET.setEnabled(false);
        	sendingComment=true;
        	//测试 发表评论api
        	new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					/*try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					final boolean flag=HttpUtil.sendComment(comment, data.img_id);
					runOnUiThread(new Runnable() {
						public void run() {
							if(flag){
								UserImagesJson.Comments comments=new UserImagesJson.Comments();
								comments.usr_id=Constants.user.userId;
								comments.create_time=System.currentTimeMillis()/1000;
								comments.body=comment;
								comments.name=Constants.user.nickName;
								if(data.listComments==null){
									data.listComments=new ArrayList<UserImagesJson.Comments>();
									data.listComments.add(comments);
								}else{
									data.listComments.add(0, comments);
								}
								
								commentAdapter=new CommentListViewAdapter(ShowTopicActivity.this, data.listComments);
								linearLayoutForListView.setAdapter(commentAdapter);
								Toast.makeText(ShowTopicActivity.this, "发表评论成功。", Toast.LENGTH_SHORT).show();
								/*InputMethodManager m = (InputMethodManager) commentET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
								if(m.isActive()){
									m.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
								}*/
								getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
								
							}else{
								Toast.makeText(ShowTopicActivity.this, "发表评论失败。", Toast.LENGTH_SHORT).show();
							     
							}
							sendingComment=false;
							commentET.setEnabled(true);
							addcommentLayout.setVisibility(View.GONE);
							show_add_comment=false;
						}
					});
				}
			}).start();
        	
//			
        	break;
		case R.id.linearlayout2:
			if(UserStatusUtil.isLoginSuccess(this)){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						boolean flag=HttpUtil.likeImage(data,tv4,handler);
						if(flag){
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									linearLayout33.setVisibility(View.VISIBLE);
								}
							});
						}
					}
				}).start();
			}
			

			break;
		case R.id.textView7:
			TranslateAnimation tAnimation=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
		
			tAnimation.setDuration(1000);
		
			shareLayout.clearAnimation();
			shareLayout.setAnimation(tAnimation);
			tAnimation.start();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					show_shareLayout=false;
					shareLayout.setVisibility(View.INVISIBLE);
				}
			}, 1000);

			break;
		case R.id.imageView2:
			if(Constants.api==null){
				boolean flag=WeixinShare.regToWeiXin(this);
				if(!flag){
					Toast.makeText(this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
					shareLayout.setVisibility(View.INVISIBLE);
					return;
				}
			}
			if(WeixinShare.shareBitmap(data, 1)){
//				Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this,"分享失败。", Toast.LENGTH_LONG).show();
			}
			TranslateAnimation tAnimation2=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
			
			tAnimation2.setDuration(1000);
		
			shareLayout.clearAnimation();
			shareLayout.setAnimation(tAnimation2);
			tAnimation2.start();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					show_shareLayout=false;
					shareLayout.setVisibility(View.INVISIBLE);
				}
			}, 1000);
			
			break;
		case R.id.imageView3:
			if(Constants.api==null){
				boolean flag=WeixinShare.regToWeiXin(this);
				if(!flag){
					Toast.makeText(this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
					shareLayout.setVisibility(View.INVISIBLE);
					return;
				}
			}
			if(WeixinShare.shareBitmap(data, 2)){
//				Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this,"分享到微信失败。", Toast.LENGTH_LONG).show();
			}
			TranslateAnimation tAnimation3=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
			
			tAnimation3.setDuration(1000);
		
			shareLayout.clearAnimation();
			shareLayout.setAnimation(tAnimation3);
			tAnimation3.start();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					show_shareLayout=false;
					shareLayout.setVisibility(View.INVISIBLE);
				}
			}, 1000);
			
			break;
		case R.id.imageView4:
			if(UserStatusUtil.hasXinlangAuth(this)){
				shareLayout.setVisibility(View.INVISIBLE);
				XinlangShare.sharePicture(data,this);
				
			}
			
			
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(show_add_comment){
//			addcommentLayout.setVisibility(View.GONE);
//			show_add_comment=false;
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 判断从哪个界面跳转到此处
	 */
	public void getFrom(){
		String from=getIntent().getStringExtra("from");
		if("waterfull".equals(from)){
			showWaterFull1=HomeActivity.homeActivity.showWaterFull1;
		}else if("focus_list".equals(from)){
			showFocusTopics=HomeActivity.homeActivity.showFocusTopics;
		}else if("ScanActivity".equals(from)){
			scanActivitys=ScanActivitys.scanActivitys;
		}else if("OtherUserTopicActivity".equals(from)){
			otherUserTopicActivity=OtherUserTopicActivity.otherUserTopicActivity;
		}else if("UserHomepageActivity".equals(from)){
			userHomepageActivity=UserHomepageActivity.userHomepageActivity;
		}
	}
	public void showProgress(){
		
		progressLayout.setVisibility(View.VISIBLE);
		
		progressLayout.setClickable(true);
		if(showProgress==null){
			showProgress=new ShowProgress(this, "数据加载中，请稍后", progressLayout);
		}else{
			showProgress.showProgress();
		}
		
	}
	public void loadBitmap(UserImagesJson.Data data){
		
		if(data!=null){
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=2;
			options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable=true;
			options.inInputShareable=true;
			Bitmap bitmap=BitmapFactory.decodeFile(data.path,options);
			FileInputStream fis=null;
			try {
				fis=new FileInputStream(data.path);
				Bitmap bitmap2=BitmapFactory.decodeStream(fis,null,options);
				int h=imageView.getHeight();
				Matrix matrix=new Matrix();
				matrix.postScale(Constants.screen_width*1f/bitmap.getWidth(), Constants.screen_width*1f/bitmap.getWidth());
				Bitmap tempBitmap=Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
				if(!bitmap2.isRecycled()){
					bitmap2.recycle();
				}
				
				imageView.setImageBitmap(tempBitmap);
			} catch (FileNotFoundException e) {
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
			if("为您爱宠的靓照写个描述吧~".equals(data.comment)){
				data.comment="";
			 }
			tv3.setText(data.comment);
		}
		
	}
	
	public void downLoadUserInfo(){
		if(Constants.isSuccess){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//加载用户信息
					boolean flag=HttpUtil.info(null,ShowTopicActivity.this);
					
				}
			}).start();
		}
	}
	/**
	 * 加载图片详细信息
	 */
	public void loadImageInfo(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(datas!=null&&datas.size()>currentPosition){
					final boolean flag=HttpUtil.imageInfo(datas.get(currentPosition),null,ShowTopicActivity.this);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//关闭加载进度条
							//刷新界面
							showProgress.progressCancel();
							progressLayout.setVisibility(View.INVISIBLE);
							if(flag){
								ShowTopicActivity.this.data=datas.get(currentPosition);
								setViews();
							}else{
								Toast.makeText(ShowTopicActivity.this,"数据加载失败", Toast.LENGTH_LONG).show();
							}
						}
					});
				}
				
			}
		}).start();
	}
	boolean onMore=false;
	boolean onRefresh=false;
	GestureDetector detector=new GestureDetector(new OnGestureListener() {
		Toast toast;
		Toast toast1;
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(ShowTopicActivity.this,ShowPictureActivity.class);
			intent.putExtra("path", data.path);
			ShowTopicActivity.this.startActivity(intent);
			return true;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			// TODO Auto-generated method stub
			LogUtil.i("scroll","distanceX="+distanceX);
			if(datas==null)return true;
			if(Math.abs(distanceX)>Math.abs(distanceY)){
				if(!onMore){
					onMore=true;
					if(currentPosition>datas.size()-10){
						if(showWaterFull1!=null){
							showWaterFull1.onMore();
						}else if(showFocusTopics!=null){
							showFocusTopics.addMore();
						}else if(userHomepageActivity!=null){
							userHomepageActivity.showFocusTopics.addMore();
						}else if(otherUserTopicActivity!=null){
							otherUserTopicActivity.showFocusTopics.addMore();
						}
					}
				}
				if(onRefresh)return true;
				onRefresh=true;
				showProgress();
				if(distanceX<0){
					currentPosition--;
					if(currentPosition<0){
						currentPosition=0;
						if(toast1!=null)
						toast1.cancel();
						showProgress.progressCancel();
						progressLayout.setVisibility(View.INVISIBLE);
						toast1=Toast.makeText(ShowTopicActivity.this, "已经是最新的一张。", Toast.LENGTH_LONG);
						toast1.show();
					}else{
						//显示数据加载进度条
						
						//联网加载数据
						loadImageInfo();
					}
				}else{
					currentPosition++;
					if(currentPosition>=datas.size()){
						currentPosition=datas.size()-1;
						if(toast!=null)
						toast.cancel();
						showProgress.progressCancel();
						progressLayout.setVisibility(View.INVISIBLE);
						toast=Toast.makeText(ShowTopicActivity.this, "已经是最后一张。", Toast.LENGTH_LONG);
						toast.show();
					}else{
						//显示数据加载进度条
						
						//联网加载数据
						loadImageInfo();
					}
				}
				
				return true;
			}else{
				
				return false;
			}
			
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if(Math.abs(velocityX)>Math.abs(velocityY)){
				
				
				return true;
			}else{
				return false;
			}
			
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			onMore=false;
			onRefresh=false;
			return true;
		}
	});
}
