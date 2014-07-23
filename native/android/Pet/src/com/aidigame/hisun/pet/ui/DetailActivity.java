package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ShowTopicHorizontalAdapter;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.ActivityJson;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.HorizontalListView2;
import com.aidigame.hisun.pet.view.MyScrollowView;
import com.aidigame.hisun.pet.widget.ShowWaterFull1;
/**
 * 活动详情页
 * @author admin
 *
 */
public class DetailActivity extends Activity {
	LinearLayout waterFullParent;
	ActivityJson.Data data;
	HorizontalListView2 horizontalListView2;
	ShowWaterFull1 showWaterFull1;
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
		
		horizontalListView2=(HorizontalListView2)findViewById(R.id.show_topic_listview);
		horizontalListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(data.txs==null)return;
				Intent intent=new Intent(DetailActivity.this,UsersListActivity.class);
				intent.putExtra("title", "参与用户");
				intent.putExtra("likers", data.txs);
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
				boolean result=HttpUtil.loadActivityInfo(data);
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
				showWaterFull1=new ShowWaterFull1(DetailActivity.this, waterFullParent,data);
				showWaterFull1.setMode(1);
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
				showWaterFull1=new ShowWaterFull1(DetailActivity.this, waterFullParent,data);
				showWaterFull1.setMode(0);
			}
		});
		showWaterFull1=new ShowWaterFull1(this, waterFullParent,data);
		showWaterFull1.setMode(0);
        MyScrollowView my=(MyScrollowView)findViewById(R.id.scrollowview);
        my.setLasyView(showWaterFull1.getScrollView());

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DetailActivity.this.finish();
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
				Intent intent=new Intent(DetailActivity.this,UsersListActivity.class);
				intent.putExtra("title", "参与用户");
				intent.putExtra("likers", data.txs);
				DetailActivity.this.startActivity(intent);
			}
		});
		findViewById(R.id.textView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(DetailActivity.this,TakePictureBackground.class);
				intent.putExtra("activity", data.topic);
				intent.putExtra("mode",TakePictureBackground.MODE_ACTIVITY);
				DetailActivity.this.startActivity(intent);
			}
		});
		setView();
		
	}
	public void setView(){
		if(data.imgPath!=null){
			BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inJustDecodeBounds=true;
			opts.inSampleSize=1;
			BitmapFactory.decodeFile(data.imgPath, opts);
			Matrix matrix=new Matrix();
			matrix.postScale(Constants.screen_width/(opts.outWidth*1f), Constants.screen_width/(opts.outWidth*1f));
			Bitmap bmp=BitmapFactory.decodeFile(data.imgPath);
			Bitmap temp=Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			if(!bmp.isRecycled()){
				bmp.recycle();
			}
			ImageView imageView=(ImageView)findViewById(R.id.imageView1);
			imageView.setImageBitmap(temp);
		}
		
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
					final String[] urls=HttpUtil.getOthersList(data.txs, null,DetailActivity.this);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(urls!=null){
								ArrayList<String> arrs=new ArrayList<String>();
								   for(String str:urls){
									   if(!StringUtil.isEmpty(str)){
										   arrs.add(str);
									   }
								   }
								 horizontalAdapter=new ShowTopicHorizontalAdapter(DetailActivity.this, arrs);
								   horizontalListView2.setAdapter(horizontalAdapter);
							}
						}
					});
				}
			}).start();
			}
	}

}
