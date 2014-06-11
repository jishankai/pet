package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.FocusAndFansAdapter;
import com.aidigame.hisun.pet.adapter.ShowTopicsAdapter;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.service.DownloadImagesAsyncTask;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.CircleView;
import com.aidigame.hisun.pet.widget.ShowExperence;
import com.aidigame.hisun.pet.widget.ShowFocusTopics;

public class UserHomepageActivity extends Activity implements OnClickListener{
	ImageView imageView1,imageView2,
	          imageView4,imageView5,imageView6,
	          imageView7,experienceImage;
	TextView tv3,tv2,tv1,comming_soon;
	boolean show_comming_soon=true;
	public static final int MESSAGE_SHOW_COMMING_SOON=0;

	
	//下载完一张图片
	public  static final int MESSAGE_DOWNLOAD_IMAGE=2;
	//图片列表
	public ArrayList<UserImagesJson.Data> datas;
	CircleView imageView3;
	String path;
	public int lastImage_id=-1;
	LinearLayout bottomLinearLayout1,bottomLinearLayout2,titleLinearLayout;
	DownloadImagesAsyncTask asyncTask;
	ShowTopicsAdapter showTopicsAdapter;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_SHOW_COMMING_SOON:
				if(show_comming_soon){
					comming_soon.setVisibility(View.VISIBLE);
					show_comming_soon=false;
					handler.sendEmptyMessageDelayed(MESSAGE_SHOW_COMMING_SOON, 3000);
				}else{
					comming_soon.setVisibility(View.INVISIBLE);
					show_comming_soon=true;
				}
				break;
			case Constants.MESSAGE_DOWNLOAD_IMAGES_LIST:
				if(datas==null){
					
				}
				UserImagesJson json=(UserImagesJson)msg.obj;
				if(json.datas!=null){
					asyncTask=new DownloadImagesAsyncTask(handler);
					UserImagesJson.Data[] arr=new UserImagesJson.Data[json.datas.size()];
					
					for(int i=0;i<json.datas.size();i++){
						final UserImagesJson.Data data=json.datas.get(i);
						data.user=Constants.user;
						datas.add(data);
						if(!new File(Constants.Picture_Topic_Path+File.separator+data.url).exists()){
							arr[i]=data;
						}else{
							data.path=Constants.Picture_Topic_Path+File.separator+data.url;
						}
					}
					LogUtil.i("me", "arr[].length="+arr.length);
					//下载图片
					asyncTask.execute(arr);
					showTopicsAdapter.notifyDataSetChanged();
					
				}
				break;
			case MESSAGE_DOWNLOAD_IMAGE:
				showTopicsAdapter.notifyDataSetChanged();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.activity_user_homepage);
		datas=new ArrayList<UserImagesJson.Data>();
		initView();
		initListener();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil.downloadUserHomepage(handler, lastImage_id,0);
			}
		}).start();
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
		experienceImage=(ImageView)findViewById(R.id.imageView8);
		comming_soon=(TextView)findViewById(R.id.comming_soon);
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv3=(TextView)findViewById(R.id.textView3);
		bottomLinearLayout1=(LinearLayout)findViewById(R.id.bottom_linearlayout1);
		bottomLinearLayout2=(LinearLayout)findViewById(R.id.bottom_linearlayout2);
		titleLinearLayout=(LinearLayout)findViewById(R.id.linearLayout);
		//标题状态栏
//		CreateTitle createTitle=new CreateTitle(this, titleLinearLayout);
		showTopics();
		if(Constants.user.gender==1){
			imageView5.setImageResource(R.drawable.male);//性别
		}else if(Constants.user.gender==2){
			imageView5.setImageResource(R.drawable.female);//性别
		}

		tv1.setText(Constants.user.nickName);//昵称
		tv2.setText(Constants.user.race);//种族
		tv3.setText(""+Constants.user.age+"岁");//年龄
		imageView3.setImageBitmap(BitmapFactory.decodeFile(Constants.user.iconPath));
	}
	private void initListener() {
		// TODO Auto-generated method stub
		imageView1.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView4.setOnClickListener(this);
		imageView6.setOnClickListener(this);
		imageView7.setOnClickListener(this);
		experienceImage.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageView1:
			this.finish();
			break;
		case R.id.imageView2:
			handler.sendEmptyMessage(MESSAGE_SHOW_COMMING_SOON);
			break;
		case R.id.imageView4:
			changeUserIcon();
			break;
		case R.id.imageView6:
			showTopics();
			break;
		case R.id.imageView7:
			userSetUp();
			break;
		case R.id.imageView8:
			showExperience();
			break;
		}
	}
	private void showExperience() {
		// TODO Auto-generated method stub
		setMenuGray();
		experienceImage.setImageResource(R.drawable.exp_red);
		ShowExperence showExperence=new ShowExperence(this, bottomLinearLayout1);
	}
	/**
	 * 改变用户头像 
	 */
	private void changeUserIcon() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(this).inflate(R.layout.popup_user_homepage, null);
		bottomLinearLayout2.removeAllViews();
		bottomLinearLayout2.addView(view);
		bottomLinearLayout2.setVisibility(View.VISIBLE);
		TextView camera=(TextView)view.findViewById(R.id.textView1);
		TextView album=(TextView)view.findViewById(R.id.textView2);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UserHomepageActivity.this,TakePictureActivity.class);
				intent.putExtra("mode", 1);
				UserHomepageActivity.this.startActivityForResult(intent, 1);
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UserHomepageActivity.this,PictureAlbumActivity.class);
				intent.putExtra("mode", 1);
				UserHomepageActivity.this.startActivityForResult(intent, 1);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
			}
		});
	
	}
	/**
	 * 用户设置
	 */
	private void userSetUp() {
		// TODO Auto-generated method stub
		setMenuGray();
		imageView7.setImageResource(R.drawable.set_red);
		View view=LayoutInflater.from(this).inflate(R.layout.item_user_setup, null);
		RelativeLayout setLayout1=(RelativeLayout)view.findViewById(R.id.set_relativelayout01);
		RelativeLayout setLayout2=(RelativeLayout)view.findViewById(R.id.set_relativelayout02);
		RelativeLayout setLayout3=(RelativeLayout)view.findViewById(R.id.set_relativelayout03);
		RelativeLayout setLayout4=(RelativeLayout)view.findViewById(R.id.set_relativelayout04);
		RelativeLayout setLayout5=(RelativeLayout)view.findViewById(R.id.set_relativelayout05);
		Button button1=(Button)view.findViewById(R.id.set_button1);
		ImageView imageView1=(ImageView)view.findViewById(R.id.set_imageView1);
		ImageView imageView2=(ImageView)view.findViewById(R.id.set_imageView2);
		TextView tv2=(TextView)view.findViewById(R.id.set_textView2);
		File file=new File(Constants.Picture_Root_Path);
		long length=0;
		if(file.exists()){
			length=file.length();
		}
		if(length/(1024*1024)>0){
			tv2.setText(""+length/(1024*1024)+"MB");
		}else if(length/(1024)>0){
			tv2.setText(""+length/(1024)+"KB");
		}else{
			tv2.setText("不足1KB");
		}
		setLayout1.setClickable(true);
		setLayout2.setClickable(true);
		setLayout3.setClickable(true);
		setLayout4.setClickable(true);
		setLayout5.setClickable(true);
		setLayout1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UserHomepageActivity.this,SetupActivity.class);
				UserHomepageActivity.this.startActivity(intent);
			}
		});

		bottomLinearLayout1.removeAllViews();
		bottomLinearLayout1.addView(view);
	}
	/**
	 * 显示 用户发表的图片,粉丝，关注的人群
	 */
	private void showTopics() {
		// TODO Auto-generated method stub
		setMenuGray();
		imageView6.setImageResource(R.drawable.home_red);
		View view=LayoutInflater.from(this).inflate(R.layout.item_user_topics, null);
		bottomLinearLayout1.removeAllViews();
		bottomLinearLayout1.addView(view);
		RelativeLayout pictureRelativeLayout=(RelativeLayout)view.findViewById(R.id.picture_relativeLayout);
		RelativeLayout focusRelativeLayout=(RelativeLayout)view.findViewById(R.id.focus_relativeLayout);
		RelativeLayout fansLinearLayout=(RelativeLayout)view.findViewById(R.id.fans_relativeLayout);
		TextView pictures=(TextView)view.findViewById(R.id.textView1);
		TextView focus=(TextView)view.findViewById(R.id.textView3);
		TextView fans=(TextView)view.findViewById(R.id.textView5);
		final LinearLayout listViewLinearLayout=(LinearLayout)view.findViewById(R.id.listview_linearLayout);
		showPictures(listViewLinearLayout);
		pictureRelativeLayout.setClickable(true);
		focusRelativeLayout.setClickable(true);
		fansLinearLayout.setClickable(true);
		pictureRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPictures(listViewLinearLayout);
			}
		});
		focusRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFocus(listViewLinearLayout);
			}
		});
		fansLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFans(listViewLinearLayout);
			}
		});
		
	}
/**
 * 显示用户发表的图片
 * @param linearLayout
 */
	private void showPictures(LinearLayout linearLayout){
		linearLayout.removeAllViews();
		ShowFocusTopics showFocusTopics=new ShowFocusTopics(this, linearLayout,datas);
		showTopicsAdapter=showFocusTopics.getAdapter();
		ListView listView=showFocusTopics.getListView();
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&view.getLastVisiblePosition()==view.getCount()-1){
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							UserImagesJson.Data data=datas.get(datas.size()-1);
							HttpUtil.downloadUserHomepage(handler, data.img_id,0);
						}
					}).start();
					
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	}
    /**
     * 显示用户关注的人群
     * @param linearLayout
     */
	private void showFocus(LinearLayout linearLayout){
		View view=LayoutInflater.from(this).inflate(R.layout.widget_show_topics, null);
		ListView listView=(ListView)view.findViewById(R.id.listView1);
		linearLayout.removeAllViews();
		linearLayout.addView(view);
		ArrayList<User> list=new ArrayList<User>();
		User user=new User();
		user.age="2"+"岁";
		user.focus=0;
		user.gender=1;
		user.nickName="无量光";
		user.race="萨摩耶犬";
		list.add(user);
		user=new User();
		user.age="3个月";
		user.focus=0;
		user.gender=2;
		user.nickName="黑漆漆";
		user.race="牧羊犬";
		list.add(user);
		user=new User();
		user.age="9岁";
		user.focus=0;
		user.gender=1;
		user.nickName="熊霸";
		user.race="泰迪熊";
		list.add(user);
		user=new User();
		user.age="一个半月";
		user.focus=0;
		user.gender=2;
		user.nickName="我叫孙悟空";
		user.race="斑点猫";
		list.add(user);
		user=new User();
		user.age="1岁";
		user.focus=0;
		user.gender=1;
		user.nickName="夫子没文化";
		user.race="藏獒";
		list.add(user);
		FocusAndFansAdapter adapter=new FocusAndFansAdapter(this, list, 1);
		listView.setAdapter(adapter);
	}
	/**
	 * 显示粉丝
	 * @param linearLayout
	 */
	private void showFans(LinearLayout linearLayout){
		View view=LayoutInflater.from(this).inflate(R.layout.widget_show_topics, null);
		ListView listView=(ListView)view.findViewById(R.id.listView1);
		linearLayout.removeAllViews();
		linearLayout.addView(view);
		ArrayList<User> list=new ArrayList<User>();
		User user=new User();
		user.age="2"+"岁";
		user.focus=0;
		user.gender=1;
		user.nickName="无量光";
		user.race="萨摩耶犬";
		list.add(user);
		user=new User();
		user.age="3个月";
		user.focus=1;
		user.gender=2;
		user.nickName="黑漆漆";
		user.race="牧羊犬";
		list.add(user);
		user=new User();
		user.age="9岁";
		user.focus=1;
		user.gender=1;
		user.nickName="熊霸";
		user.race="泰迪熊";
		list.add(user);
		user=new User();
		user.age="一个半月";
		user.focus=0;
		user.gender=2;
		user.nickName="我叫孙悟空";
		user.race="斑点猫";
		list.add(user);
		user=new User();
		user.age="1岁";
		user.focus=1;
		user.gender=1;
		user.nickName="夫子没文化";
		user.race="藏獒";
		list.add(user);
		FocusAndFansAdapter adapter=new FocusAndFansAdapter(this, list, 1);
		listView.setAdapter(adapter);
	}
	/**
	 * 将显示图片   经验  设置 按钮设为灰色
	 */
	public void setMenuGray(){
		imageView6.setImageResource(R.drawable.home_gray);
		imageView7.setImageResource(R.drawable.set_gray);
		experienceImage.setImageResource(R.drawable.exp_gray);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			path=data.getStringExtra("path");
			if(path!=null){
				imageView3.setImageBitmap(BitmapFactory.decodeFile(path));
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpUtil.uploadUserIcon(path);
					}
				}).start();
			}

			
			break;

		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		asyncTask.cancel(true);
	}

}
