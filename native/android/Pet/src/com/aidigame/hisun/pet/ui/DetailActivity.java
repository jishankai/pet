package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicHorizontalAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.ActivityJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.HorizontalListView2;
import com.aidigame.hisun.pet.widget.PLAWaterfull;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 活动详情页
 * @author admin
 *
 */
public class DetailActivity extends Activity {
	public View popupParent;
	public RelativeLayout black_layout;
	FrameLayout frameLayout;
	View viewTopWhite;
	public static DetailActivity detailActivity;
	DisplayImageOptions displayImageOptions;
	HandleHttpConnectionException handleHttpConnectionException;
	LinearLayout waterFullParent;
	public ActivityJson.Data data;
	HorizontalListView2 horizontalListView2;
//	ShowWaterFull1 showWaterFull1;
	PLAWaterfull plaWaterfull;
	TextView popularTv,newestTv;
	String[] urls;
	ShowTopicHorizontalAdapter horizontalAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_detail);
		data=(ActivityJson.Data)getIntent().getSerializableExtra("data");
		detailActivity=this;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		Bitmap nobmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.noimg);
		Matrix matrix=new Matrix();
		matrix.postScale(Constants.screen_width/(nobmp.getWidth()*1f), Constants.screen_width/(nobmp.getWidth()*1f));
		nobmp=Bitmap.createBitmap(nobmp, 0, 0, nobmp.getWidth(), nobmp.getHeight(),matrix,true);
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inJustDecodeBounds=false;
		opts.inSampleSize=2;
		opts.inPreferredConfig=Bitmap.Config.RGB_565;
		opts.inPurgeable=true;
		opts.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions.Builder()
		                    .showImageOnLoading(new BitmapDrawable(nobmp))
		                    .cacheInMemory(true)
		                    .cacheOnDisc(true)
		                    .bitmapConfig(Config.RGB_565)
		                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		                    .decodingOptions(opts)
		                    .build();
		
		setBlurImageBackground();
		
		horizontalListView2=(HorizontalListView2)findViewById(R.id.show_topic_listview);
		horizontalListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(data.txs==null)return;
				Intent intent=new Intent(DetailActivity.this,AnimalsListActivity.class);
				intent.putExtra("title", "参与用户");
				intent.putExtra("aids", data.txs);
				DetailActivity.this.startActivity(intent);
			}
		});
		findViewById(R.id.imageView7).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(data.txs==null)return;
				Intent intent=new Intent(DetailActivity.this,UsersListActivity.class);
				intent.putExtra("title", "参与用户");
				intent.putExtra("likers", data.txs);
				DetailActivity.this.startActivity(intent);
			}
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean result=HttpUtil.loadActivityInfo(data,handleHttpConnectionException.getHandler(DetailActivity.this),DetailActivity.this);
				if(result){
					runOnUiThread(new Runnable() {
						public void run() {
							setView();
						}
					});
				}
			}
		}).start();
		waterFullParent=(LinearLayout)findViewById(R.id.waterfull_parent);
		newestTv=(TextView)findViewById(R.id.textView8);
		newestTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*showWaterFull1=new ShowWaterFull1(DetailActivity.this, waterFullParent,data,5);
				showWaterFull1.setMode(1);*/
				plaWaterfull=new PLAWaterfull(DetailActivity.this, waterFullParent, 5);
				popularTv.setTextColor(getResources().getColor(R.color.gray_deep));
				newestTv.setTextColor(getResources().getColor(R.color.orange_red));
			}
		});
       popularTv=(TextView)findViewById(R.id.textView7);
       popularTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newestTv.setTextColor(getResources().getColor(R.color.gray_deep));
				popularTv.setTextColor(getResources().getColor(R.color.orange_red));
				/*showWaterFull1=new ShowWaterFull1(DetailActivity.this, waterFullParent,data,4);
				showWaterFull1.setMode(0);*/
				plaWaterfull=new PLAWaterfull(DetailActivity.this, waterFullParent, 4);
			}
		});
		
       
       plaWaterfull=new PLAWaterfull(this, waterFullParent, 5);
      /* showWaterFull1=new ShowWaterFull1(this, waterFullParent,data,5);
		showWaterFull1.setMode(0);
        MyScrollowView my=(MyScrollowView)findViewById(R.id.scrollowview);
        my.setLasyView(showWaterFull1.getScrollView());*/

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(PetApplication.petApp.activityList!=null&&PetApplication.petApp.activityList.contains(DetailActivity.this)){
					PetApplication.petApp.activityList.remove(DetailActivity.this);
				}
				DetailActivity.this.finish();
				System.gc();
			}
		});
		findViewById(R.id.award_linearlayout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(DetailActivity.this,AwardDetailActivity.class);
				intent.putExtra("data", data);
				DetailActivity.this.startActivity(intent);
			}
		});
		LinearLayout layout=(LinearLayout)findViewById(R.id.join_people_linearlayout);
		layout.setClickable(true);
				layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(data.txs==null)return;
				Intent intent=new Intent(DetailActivity.this,AnimalsListActivity.class);
				intent.putExtra("title", "参与用户");
				intent.putExtra("aids", data.txs);
				
				DetailActivity.this.startActivity(intent);
			}
		});
		findViewById(R.id.textView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(DetailActivity.this, popupParent, black_layout)){
					return;
				}
				if(PetApplication.myUser!=null&&PetApplication.myUser.currentAnimal!=null&&PetApplication.myUser.userId==PetApplication.myUser.currentAnimal.master_id){
					Intent intent=new Intent(DetailActivity.this,TakePictureBackground.class);
					intent.putExtra("activity", data.topic);
					intent.putExtra("mode",TakePictureBackground.MODE_ACTIVITY);
					intent.putExtra("topic_id", data.topic_id);
					intent.putExtra("topic_name", data.topic);
					DetailActivity.this.startActivity(intent);
				}else{
					Toast.makeText(DetailActivity.this, "只有用户主人才可以发照片奥~", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		setView();
		
		
		
		
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
		touchSlop=ViewConfiguration.get(this).getScaledTouchSlop();
       
	}
	public void setView(){
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.loadImage(Constants.ACTIVITY_IMAGE+data.img,displayImageOptions, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				Matrix matrix=new Matrix();
				matrix.postScale(Constants.screen_width/(loadedImage.getWidth()*1f), Constants.screen_width/(loadedImage.getWidth()*1f));
				
				Bitmap temp=Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);
//				if(!loadedImage.isRecycled()){
//					loadedImage.recycle();
//				}
				ImageView imageView=(ImageView)findViewById(R.id.imageView1);
				imageView.setImageBitmap(temp);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
		TextView time=null,reward=null,people=null,topic=null,des=null;
		time=(TextView)findViewById(R.id.textView4);
		topic=(TextView)findViewById(R.id.textView2);
		des=(TextView)findViewById(R.id.textView3);
		reward=(TextView)findViewById(R.id.textView5);
		people=(TextView)findViewById(R.id.textView6);
		if(data!=null){
			if(data.topic!=null)
				topic.setText(data.topic);
			if(data.des!=null)
				des.setText(data.des);
			if(data.reward!=null)
				reward.setText(data.reward);
		time.setText(""+StringUtil.timeFormat(data.start_time)+"至"+StringUtil.timeFormat(data.end_time));
		people.setText(""+data.people+"人");
		ImageView iView=(ImageView)findViewById(R.id.imageView9);
		if(data.end_time>System.currentTimeMillis()){
			iView.setImageResource(R.drawable.activity_green);
		}else{
			iView.setImageResource(R.drawable.activity_right);
		}
		}
		if(data.txs!=null){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
//							HttpUtil.getOthersList(data.txs, null,DetailActivity.this);
					final ArrayList<Animal> urls=HttpUtil.otherAnimals(handleHttpConnectionException.getHandler(DetailActivity.this), data.txs,1 , DetailActivity.this);
							
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(urls!=null){
								ArrayList<String> arrs=new ArrayList<String>();
								   for(Animal str:urls){
									   if(!StringUtil.isEmpty(str.pet_iconUrl)){
										   arrs.add(str.pet_iconUrl);
									   }
								   }
								 horizontalAdapter=new ShowTopicHorizontalAdapter(DetailActivity.this, arrs,true);
								   horizontalListView2.setAdapter(horizontalAdapter);
							}
						}
					});
				}
			}).start();
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
