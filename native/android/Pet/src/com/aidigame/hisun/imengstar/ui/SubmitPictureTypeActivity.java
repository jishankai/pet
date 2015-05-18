package com.aidigame.hisun.imengstar.ui;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.SubmitPictureTypeAdapter;
import com.aidigame.hisun.imengstar.adapter.UserPetsAdapter;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.view.HorizontalListView2;
import com.aidigame.hisun.imengstar.R;

/**
 * 发布照片的类型选择界面
 * @author admin
 *
 */

public class SubmitPictureTypeActivity extends BaseActivity implements OnClickListener{
	private ImageView leftIv,rightIv,touchImage,ballImage;
	private LinearLayout foodIv,touchIv,ballIv,pictureIv;
	private TextView touchTv,ballTv;
	private View closeView;
	private HorizontalListView2 listView2;
	private SubmitPictureTypeAdapter userPetsAdapter;
	private int current_position=0;
	private ArrayList<Animal> animals;
	 private  LinearLayout camera_album;//显示获取照片界面
	 private Handler handler;
	 int p_mid_size=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_submit_picture_type);
		initView();
		initEvent();
		iniData();
	}
	private void iniData() {
		// TODO Auto-generated method stub
		animals=new ArrayList<Animal>();
  		for(int i=0;i<PetApplication.myUser.aniList.size();i++){
  			if(PetApplication.myUser.userId==PetApplication.myUser.aniList.get(i).master_id){
  				animals.add(PetApplication.myUser.aniList.get(i));
  			}
  		}
  		if(animals.size()<=2){
  			leftIv.setVisibility(View.INVISIBLE);
  			rightIv.setVisibility(View.INVISIBLE);
  		}
		listView2.showItemBg=true;
		userPetsAdapter=new SubmitPictureTypeAdapter(this, animals, PetApplication.myUser);
		listView2.setAdapter(userPetsAdapter);
		SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		p_mid_size=sp.getInt("p_mid_size", 0);
		if(p_mid_size>0){
			touchIv.setVisibility(View.VISIBLE);
			String path=sp.getString("p_mid"+sp.getInt("p_mid"+0+"_label", 0)+"_icon_path", "");
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=1;
			if(!StringUtil.isEmpty(path))
				touchImage.setImageBitmap(BitmapFactory.decodeFile(path));;
			String text=sp.getString("p_mid"+sp.getInt("p_mid"+0+"_label", 0)+"_txt", "");
			if(!StringUtil.isEmpty(text))
				touchTv.setText(text);
		}
		if(p_mid_size>1){
			ballIv.setVisibility(View.VISIBLE);
			String path=sp.getString("p_mid"+sp.getInt("p_mid"+1+"_label", 0)+"_icon_path", "");
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=1;
			if(!StringUtil.isEmpty(path))
			
			ballImage.setImageBitmap(BitmapFactory.decodeFile(path));;
			
			String text=sp.getString("p_mid"+sp.getInt("p_mid"+1+"_label", 0)+"_txt", "");
			if(!StringUtil.isEmpty(text))
			ballTv.setText(text);
		}
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		closeView.setOnClickListener(this);
		leftIv.setOnClickListener(this);
		rightIv.setOnClickListener(this);
		foodIv.setOnClickListener(this);
		touchIv.setOnClickListener(this);
		ballIv.setOnClickListener(this);
		pictureIv.setOnClickListener(this);
		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				listView2.chose_position=position;
				listView2.resetBackground((SubmitPictureTypeAdapter.Holder)view.getTag());
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		leftIv=(ImageView)findViewById(R.id.left_iv);
		rightIv=(ImageView)findViewById(R.id.right_iv);
		touchImage=(ImageView)findViewById(R.id.touch_image);
		ballImage=(ImageView)findViewById(R.id.ball_image);
		touchTv=(TextView)findViewById(R.id.touch_tv);
		ballTv=(TextView)findViewById(R.id.ball_tv);
		foodIv=(LinearLayout)findViewById(R.id.iv_4);
		touchIv=(LinearLayout)findViewById(R.id.iv_5);
		ballIv=(LinearLayout)findViewById(R.id.iv_6);
		pictureIv=(LinearLayout)findViewById(R.id.iv_7);
		closeView=findViewById(R.id.close_view);
		listView2=(HorizontalListView2)findViewById(R.id.listview);
		camera_album=(LinearLayout)findViewById(R.id.camera_album);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_iv:
			View view=listView2.getChildAt(0);
			if(view!=null){
				LogUtil.i("mi", "点击左按键");
				int w=view.getMeasuredWidth();
				int vLeft=view.getLeft();
				int vRight=view.getRight();
				int left=listView2.getLeft();
				int x=listView2.getScrollX();
				int right=listView2.getRight();
				current_position=listView2.chose_position;
				LogUtil.i("mi", "第一个="+listView2.getFirstVisiblePosition()+";最后一个="+listView2.getLastVisiblePosition()+"；view.w="+w+";x="+x);
				int det=current_position*w-w;
				if(det>=0){
					listView2.scrollTo(det);
				}else{
					listView2.scrollTo(0);
				}
				current_position--;
				if(current_position<0)current_position=0;
			}
			break;

		case R.id.right_iv:
            View view1=listView2.getChildAt(listView2.getFirstVisiblePosition());
			
			if(view1!=null){
				
				int listW=listView2.getMeasuredWidth();
				int w=view1.getMeasuredWidth();
				int vLeft=view1.getLeft();
				int vRight=view1.getRight();
				int left=listView2.getLeft();
				int x=listView2.getScrollX();
				int right=listView2.getRight();
				current_position=listView2.chose_position;
				int det=listW-current_position*w;
				int num=vLeft/w;
				LogUtil.i("mi", "点击右按键  current_position="+current_position);
				current_position++;
				if(current_position<=animals.size()){
					listView2.scrollTo(current_position*w);
				}else{
					listView2.scrollTo(animals.size()*w);
					current_position=animals.size();
				}
				
			}
			break;

		case R.id.iv_4:
			begFood();
			
			break;

		case R.id.iv_5:
			judgePictureType(1);
			break;

		case R.id.iv_6:
			judgePictureType(2);
			break;

		case R.id.iv_7:
			showCameraAlbum(animals.get(listView2.chose_position), 0);
//			finish();
			break;
        case R.id.close_view:
			finish();
			break;
		}
	}
	boolean isShowingCameraAlbum=false;
	/**
	 * 
	 * @param animal
	 * @param mode 0 晒照片；1，挣口粮；2 求摸摸；3 玩球球
	 */
	public void showCameraAlbum(final Animal animal,final int mode) {
		// TODO Auto-generated method stub
		isShowingCameraAlbum=true;
		long l1=System.currentTimeMillis();
		LogUtil.i("scroll",""+( System.currentTimeMillis()-l1));
		final View view=LayoutInflater.from(this).inflate(R.layout.popup_user_homepage, null);
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
		view.clearAnimation();
		view.setAnimation(animation);
		animation.start();
		
		camera_album.removeAllViews();
		camera_album.addView(view);
		camera_album.setVisibility(View.VISIBLE);
		camera_album.setClickable(true);
		TextView camera=(TextView)view.findViewById(R.id.textView1);
		TextView album=(TextView)view.findViewById(R.id.textView2);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(SubmitPictureTypeActivity.this,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				if(animal!=null){
					intent2.putExtra("animal", animal);
					intent2.putExtra("isBeg", mode);
				}else{
					intent2.putExtra("animal", PetApplication.myUser.currentAnimal);
					intent2.putExtra("isBeg", mode);
				}
				
				startActivity(intent2);
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
						isShowingCameraAlbum=false;
						finish();
				
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(SubmitPictureTypeActivity.this,AlbumPictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				
				if(animal!=null){
					intent2.putExtra("animal", animal);
					intent2.putExtra("isBeg", mode);
				}else{
					intent2.putExtra("animal", PetApplication.myUser.currentAnimal);
					intent2.putExtra("isBeg", mode);
				}
			startActivity(intent2);
				
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
						isShowingCameraAlbum=false;
				finish();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation=AnimationUtils.loadAnimation(SubmitPictureTypeActivity.this, R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						camera_album.setVisibility(View.INVISIBLE);
						camera_album.setClickable(false);
						isShowingCameraAlbum=false;
					}
				}, 300);
				
			}
		});
	}
	boolean isBegging=false;
	public void begFood(){
		if(isBegging){
			Toast.makeText(this,"正在加载数据，请稍后", Toast.LENGTH_LONG).show();
			return;
		}
		isBegging=true;
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final PetPicture pp=HttpUtil.judgePictureMenu(handler, animals.get(listView2.chose_position), SubmitPictureTypeActivity.this,1);
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(pp==null){
									if(PetApplication.myUser.userId==animals.get(listView2.chose_position).master_id){
										showCameraAlbum(animals.get(listView2.chose_position),1);
									}
								}else{
									Intent intent6=new Intent(SubmitPictureTypeActivity.this,Dialog6Activity.class);
									intent6.putExtra("picture", pp);
									startActivity(intent6);
								}
								isBegging=false;
							}
						});
						
						
					}
				}).start();
			
		}
	}
	public void judgePictureType(final int iv){
		if(isBegging){
			Toast.makeText(this,"正在加载数据，请稍后", Toast.LENGTH_LONG).show();
			return;
		}
		isBegging=true;
		
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int label=1;
						SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
						if(iv==1){
							label=sp.getInt("p_mid"+/*sp.getInt("p_mid"+0+"_label", 0)*/0+"_label", 1);
						}else if(iv==2){
							label=sp.getInt("p_mid"+/*sp.getInt("p_mid"+1+"_label", 0)*/1+"_label", 1);
						}
						final PetPicture pp=HttpUtil.judgePictureMenu(handler, animals.get(listView2.chose_position), SubmitPictureTypeActivity.this,label);
						final int  labelFinal=label;
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(pp==null){
									if(PetApplication.myUser.userId==animals.get(listView2.chose_position).master_id){
										showCameraAlbum(animals.get(listView2.chose_position),labelFinal);
									}
								}else{
									Intent intent6=new Intent(SubmitPictureTypeActivity.this,Dialog6Activity.class);
									intent6.putExtra("picture", pp);
									startActivity(intent6);
								}
								isBegging=false;
							}
						});
						
						
					}
				}).start();
			
		}
	}

}
