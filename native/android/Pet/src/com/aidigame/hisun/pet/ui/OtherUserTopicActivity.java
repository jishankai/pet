package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.MailListViewAdapter;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter2;
import com.aidigame.hisun.pet.blur.TopCenterImageView;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.MessagJson;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.service.DownloadImagesAsyncTask;
import com.aidigame.hisun.pet.util.ImageUtil;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.CircleView;
import com.aidigame.hisun.pet.widget.ShowFocusTopics1;
import com.aidigame.hisun.pet.widget.ShowProgress;

public class OtherUserTopicActivity extends Activity implements OnClickListener{
	ImageView imageView1,imageView2,
    imageView4,imageView5,imageView6,
    imageView7;
	EditText editCommen;
	TopCenterImageView blur_view;//毛玻璃效果
	RelativeLayout allLayout,bottomRelativeLayout3;
	boolean showSendMail=false;
	public static OtherUserTopicActivity otherUserTopicActivity;
	ShowProgress showProgress;
	LinearLayout progressLayout;
	ShowFocusTopics1 showFocusTopics;
	ListView listView;
	public boolean isOnRefresh=false;
	int firstVisionItem=0;
	public static final int DISMISS_PROGRESS=202;
	public static final int SHOW_PROGRESS=203;
    TextView tv3,tv2,tv1,line_between;
    CircleView imageView3;
    public UserImagesJson.Data data;
    public ArrayList<UserImagesJson.Data> datas;
    ShowTopicsAdapter2 showTopicsAdapter;
    LinearLayout bottomLinearLayout1,bottomLinearLayout4,bottomLinearLayout5;
   public  RelativeLayout userLayout,bottomLinearLayout2;
    public static final int ADD_SUCCESS=1;//添加关注成功
    public static final int ADD_FAIL=2;
    public static final int DELETE_SUCCESS=3;//删除关注成功
    public static final int DELETE_FAIL=4;
   public int last_id=-1;
    DownloadImagesAsyncTask asyncTask;
   public Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case  Constants.MESSAGE_DOWNLOAD_IMAGES_LIST:
                if(datas==null){
					
				}
				UserImagesJson json=(UserImagesJson)msg.obj;
				setViews();
				showProgress.progressCancel();
				if(json.datas!=null){
					
					asyncTask=new DownloadImagesAsyncTask(handler,OtherUserTopicActivity.this);
					UserImagesJson.Data[] arr=new UserImagesJson.Data[json.datas.size()];
					if(isOnRefresh){
						if(json.datas!=null&&json.datas.size()>0&&datas.size()>0){
							if(datas.get(0).img_id==json.datas.get(0).img_id){
								
								isOnRefresh=false;
								return;
							}else{
								datas.removeAll(datas);
							}
						}
					}
					UserImagesJson.Data data=null;
					for(int i=0;i<json.datas.size();i++){
						data=json.datas.get(i);
						data.user=OtherUserTopicActivity.this.data.user;
						datas.add(data);
					
						if(!StringUtil.judgeImageExists(Constants.Picture_Topic_Path+File.separator+data.url)){
							arr[i]=data;
						}else{
							data.path=Constants.Picture_Topic_Path+File.separator+data.url;
						}
					}
					LogUtil.i("me", "arr[].length="+arr.length);
					//下载图片
					asyncTask.execute(arr);
					if(showTopicsAdapter!=null){
						if(isOnRefresh){
							isOnRefresh=false;
						}
						
						if(json.datas.size()>0)
						showTopicsAdapter.notifyDataSetChanged();
					}
					
					
				}
				break;
			case UserHomepageActivity.MESSAGE_DOWNLOAD_IMAGE:
				if(showTopicsAdapter!=null)
				showTopicsAdapter.notifyDataSetChanged();
				break;
			case ADD_SUCCESS:
				imageView4.setImageResource(R.drawable.focus_delete);
				data.isFriend=true;
				break;
			case ADD_FAIL:
				
				break;
			case DELETE_SUCCESS:
				imageView4.setImageResource(R.drawable.focus_add);
				data.isFriend=false;
				break;
			case  DELETE_FAIL:
				
				break;
			case DISMISS_PROGRESS:
				if(showProgress!=null)
				showProgress.progressCancel();
				break;
			case SHOW_PROGRESS:
				if(showProgress==null){
					showProgress=new ShowProgress(OtherUserTopicActivity.this, Constants.NOTE_MESSAGE_1, progressLayout);
				}else{
					showProgress.showProgress();
				}
				
				break;
			}
    	};
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_other_user_topic);
		this.otherUserTopicActivity=this;
		data=(UserImagesJson.Data)getIntent().getSerializableExtra("data");
        
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		imageView1=(ImageView)findViewById(R.id.imageView1);
		imageView2=(ImageView)findViewById(R.id.imageView2);
		imageView3=(CircleView)findViewById(R.id.imageView3);
		imageView4=(ImageView)findViewById(R.id.imageView4);
		imageView5=(ImageView)findViewById(R.id.imageView5);
		imageView6=(ImageView)findViewById(R.id.imageView6);
		imageView7=(ImageView)findViewById(R.id.imageView7);
		progressLayout=(LinearLayout)findViewById(R.id.progress_linearlayout1);
		blur_view=(TopCenterImageView)findViewById(R.id.blur_view);
		allLayout=(RelativeLayout)findViewById(R.id.all);
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		line_between=(TextView)findViewById(R.id.line_between);
		tv3=(TextView)findViewById(R.id.textView3);
		
		bottomLinearLayout1=(LinearLayout)findViewById(R.id.bottom_linearlayout1);
		bottomLinearLayout2=(RelativeLayout)findViewById(R.id.bottom_linearlayout2);
		bottomLinearLayout4=(LinearLayout)findViewById(R.id.bottom_linearlayout4);
		bottomLinearLayout5=(LinearLayout)findViewById(R.id.bottom_linearlayout5);
		bottomLinearLayout5.setVisibility(View.VISIBLE);
		showProgress=new ShowProgress(this, "数据正在加载中", bottomLinearLayout5);
	    bottomLinearLayout2.setClickable(true);
		userLayout=(RelativeLayout)findViewById(R.id.user_info_relativelayout);
		bottomRelativeLayout3=(RelativeLayout)findViewById(R.id.bottom_linearlayout3);
		bottomRelativeLayout3.setClickable(true);;
		bottomRelativeLayout3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bottomRelativeLayout3.setVisibility(View.INVISIBLE);
				blur_view.setVisibility(View.INVISIBLE);
			}
		});
		if(new File(Constants.Picture_ICON_Path+File.separator+data.user.iconUrl).exists()){
			data.user.iconPath=Constants.Picture_ICON_Path+File.separator+data.user.iconUrl;
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=4;
			imageView3.setImageBitmap(BitmapFactory.decodeFile(data.user.iconPath, options));
		}
		showTopics();
//		imageView5.setImageResource();//��������������������
//		tv1.setText(topic.user.nickName);//��������������������
//		tv2.setText(topic.user.race);//������������������������
//		tv3.setText(topic.user.age);//������������������������
		
	}
	public void setViews(){
		tv1.setText(""+data.user.nickName);
		line_between.setVisibility(View.VISIBLE);
		 final SharedPreferences sp=this.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
		 
		    if( sp.contains(data.user.race)){
			  tv2.setText(""+sp.getString(data.user.race, ""));
		   }else{
			   new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(Integer.parseInt(Constants.user.race)>301)return;
					boolean flag=HttpUtil.getRaceType(OtherUserTopicActivity.this);
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
		tv3.setText(""+data.user.age+"岁");
		
		if(data.isFriend){
			imageView4.setImageResource(R.drawable.focus_delete);
		}else{
			imageView4.setImageResource(R.drawable.focus_add);
		}
		if(data.user.gender==1){
			//公
			imageView5.setImageResource(R.drawable.male1);
		}else if(data.user.gender==2){
			//母
			imageView5.setImageResource(R.drawable.female1);
		}else{
			
		}
		if(new File(Constants.Picture_ICON_Path+File.separator+data.user.iconUrl).exists()){
			data.user.iconPath=Constants.Picture_ICON_Path+File.separator+data.user.iconUrl;
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=4;
			imageView3.setImageBitmap(BitmapFactory.decodeFile(data.user.iconPath, options));
		}
	}
	private void initListener() {
		// TODO Auto-generated method stub
		imageView1.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView4.setOnClickListener(this);
		imageView6.setOnClickListener(this);
		imageView7.setOnClickListener(this);
		imageView3.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.imageView1:
			if("HomeActivity".equals(getIntent().getStringExtra("from"))){
				/*if(HomeActivity.homeActivity!=null){
//					HomeActivity.homeActivity.finish();
					this.finish();
				}else{*/
					Intent intentHome=new Intent(this,HomeActivity.class);
					this.startActivity(intentHome);
					this.finish();
//				}
			}else{
				this.finish();
			}
			
			break;
		case R.id.imageView2:
			showMail();
			break;
		case R.id.imageView4:
			if(!UserStatusUtil.isLoginSuccess(this))return;
			addOrRemoveFocus();
			break;
		case R.id.imageView6:
			showWeiboInfo();
			break;
		case R.id.imageView7:
			
			break;
		case R.id.imageView3:
			Intent intent=new Intent(this,ShowPictureActivity.class);
			intent.putExtra("path", data.user.iconPath);
			this.startActivity(intent);
			break;
		}
	}
	private void showWeiboInfo() {
		// TODO Auto-generated method stub
		blur_view.setVisibility(View.VISIBLE);
		bottomRelativeLayout3.setVisibility(View.VISIBLE);
		ImageView iconImageView=(ImageView)findViewById(R.id.weibo_icon);
		ImageView addView=(ImageView)findViewById(R.id.add_weibo);
		TextView nameTv=(TextView)findViewById(R.id.wei_bo_name);
		
	}
	boolean sendingComment=false;
	public void showMail() {
		// TODO Auto-generated method stub
		showBlurView();
		showSendMail=true;
		bottomLinearLayout2.setVisibility(View.VISIBLE);
	    final ArrayList<MessagJson.DataSystem> mailList= new ArrayList<MessagJson.DataSystem>();
	   final MailListViewAdapter mailListViewAdapter=new MailListViewAdapter(this, mailList);
	  final ListView maiListView= (ListView)findViewById(R.id.mail_listview);
//	  
	   maiListView.setAdapter(mailListViewAdapter);
	    
	   editCommen=(EditText)findViewById(R.id.edit_comment);
	    TextView sendTv=(TextView)findViewById(R.id.send_comment);
	    editCommen.setEnabled(true);
	    sendTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sendingComment){
					Toast.makeText(OtherUserTopicActivity.this, "正在发送私信", Toast.LENGTH_LONG).show();
					return;
				}
				String comment=editCommen.getText().toString();
				if(StringUtil.isEmpty(comment)){
					Toast.makeText(OtherUserTopicActivity.this, "发送私信内容不能不空。", Toast.LENGTH_LONG).show();
					return;
				}
				final MessagJson.DataSystem dataSystem=new MessagJson.DataSystem();
				dataSystem.body=comment;
				dataSystem.create_time=System.currentTimeMillis()/1000;
				dataSystem.fromUser=Constants.user;
				sendingComment=true;
				new Thread(new Runnable() {
					Toast toast;
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//测试发送消息api
						final boolean flag=HttpUtil.sendMail(data.usr_id, dataSystem.body);
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(flag){
									
									mailList.add(dataSystem);
									mailListViewAdapter.update(mailList);
									mailListViewAdapter.notifyDataSetChanged();
									if(toast!=null){
										toast.cancel();
									}
									toast=Toast.makeText(OtherUserTopicActivity.this, "发送私信成功。", Toast.LENGTH_LONG);
									toast.show();
									editCommen.setText("");
									getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
									if(mailList.size()>1)
									maiListView.smoothScrollToPosition(mailList.size()-1);
								}else{
									if(toast!=null){
										toast.cancel();
									}
									toast=Toast.makeText(OtherUserTopicActivity.this, "发送私信失败。", Toast.LENGTH_LONG);
									toast.show();
								}
								sendingComment=false;
							}
						});
						
					}
				}).start();
			}
		});
	}
	public void addOrRemoveFocus() {
		// TODO Auto-generated method stub
		bottomLinearLayout4.setVisibility(View.VISIBLE);
		
		TranslateAnimation animation=new TranslateAnimation(0, 0, bottomLinearLayout4.getHeight(), 0);
		animation.setDuration(600);
		
		bottomLinearLayout4.clearAnimation();
		bottomLinearLayout4.setAnimation(animation);
		animation.start();
		TextView add=(TextView)findViewById(R.id.textView41);
		if(data.isFriend){
			add.setText("取消关注");
		}else{
			add.setText("关注");
		}
		TextView cancel=(TextView)findViewById(R.id.textView42);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				TranslateAnimation animation=new TranslateAnimation(0, 0, 0, bottomLinearLayout4.getHeight());
				animation.setDuration(500);
				
				bottomLinearLayout4.clearAnimation();
				bottomLinearLayout4.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						bottomLinearLayout4.setVisibility(View.INVISIBLE);
					}
				}, 500);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(data.isFriend){
							HttpUtil.userDeleteFollow(data, handler,OtherUserTopicActivity.this);
						}else{
							HttpUtil.userAddFollow(data, handler,OtherUserTopicActivity.this);
						}
						
					}
				}).start();
			}
		});
	    cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TranslateAnimation animation=new TranslateAnimation(0, 0, 0, bottomLinearLayout4.getHeight());
				animation.setDuration(600);
				
				bottomLinearLayout4.clearAnimation();
				bottomLinearLayout4.setAnimation(animation);
				animation.start();
				bottomLinearLayout4.setVisibility(View.INVISIBLE);
			}
		});
	}
	private void showTopics() {
		// TODO Auto-generated method stub
		//TOD0
//		handler.sendEmptyMessage(SHOW_PROGRESS);
		last_id=-1;
		datas=new ArrayList<UserImagesJson.Data>();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.imageInfo(data,null,OtherUserTopicActivity.this);
				HttpUtil.downloadOtherUserHomepage(handler, last_id, data,OtherUserTopicActivity.this);
			}
		}).start();
        showFocusTopics=new ShowFocusTopics1(this, bottomLinearLayout1,datas,2);
        showFocusTopics.addView();
		showTopicsAdapter=showFocusTopics.getAdapter();
		getListView();
	}
	private void getListView(){
		listView=showFocusTopics.getListView();
		listView.setDivider(null);
		/*listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
            if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&view.getLastVisiblePosition()==view.getCount()-1){
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(datas.size()>0){
								UserImagesJson.Data data=datas.get(datas.size()-1);
								HttpUtil.downloadOtherUserHomepage(handler, data.img_id,data,OtherUserTopicActivity.this);
							}
						}
					}).start();
					
				}else if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&view.getFirstVisiblePosition()==0){
					userLayout.setVisibility(View.VISIBLE);
					firstVisionItem=0;
					showTopics();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				firstVisionItem=firstVisibleItem;
				if(firstVisibleItem>1)
					userLayout.setVisibility(View.GONE);
			}
		});*/
	}
	public void showBlurView(){
		Bitmap bmp=ImageUtil.getBitmapFromView(allLayout,OtherUserTopicActivity.this);
		blur_view.setImageBitmap(bmp);
		blur_view.setAlpha(0.85f);//0.9342857
		
		blur_view.setVisibility(View.VISIBLE);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(showSendMail){
				blur_view.setVisibility(View.INVISIBLE);
				showSendMail=false;
				editCommen.setEnabled(false);
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.i("me", "OtherUserTopicActivity销毁");
		super.onDestroy();
	}

}
