package com.aidigame.hisun.imengstar.widget.fragment;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.GalleryAdapter;
import com.aidigame.hisun.imengstar.adapter.RecommendGridPictureAdapter;
import com.aidigame.hisun.imengstar.adapter.RecommendGridPictureAdapter.ClickRecommendListener;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.Article;
import com.aidigame.hisun.imengstar.bean.Banner;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.ui.ActivityWebActivity;
import com.aidigame.hisun.imengstar.ui.AlbumPictureBackground;
import com.aidigame.hisun.imengstar.ui.ChargeActivity;
import com.aidigame.hisun.imengstar.ui.ContributeRankListActivity;
import com.aidigame.hisun.imengstar.ui.Dialog4Activity;
import com.aidigame.hisun.imengstar.ui.DialogNoteActivity;
import com.aidigame.hisun.imengstar.ui.GetTicketRankListActivity;
import com.aidigame.hisun.imengstar.ui.HomeActivity;
import com.aidigame.hisun.imengstar.ui.NewPetKingdomActivity;
import com.aidigame.hisun.imengstar.ui.NewRegisterActivity;
import com.aidigame.hisun.imengstar.ui.NewShowTopicActivity;
import com.aidigame.hisun.imengstar.ui.RecommendActivity;
import com.aidigame.hisun.imengstar.ui.SubmitPictureTypeActivity;
import com.aidigame.hisun.imengstar.ui.TakePictureBackground;
import com.aidigame.hisun.imengstar.ui.Dialog4Activity.Dialog3ActivityListener;
import com.aidigame.hisun.imengstar.ui.TicketCardActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.ImageUtil;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.view.HorizontalListView2;
import com.aidigame.hisun.imengstar.widget.fragment.RecommendFragment.DataBean;
import com.aidigame.hisun.imengstar.R;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 海选Page 的Fragment
 * @author admin
 *
 */
public class RecommendPagerFragment implements OnClickListener,IXListViewListener{
	private View view;
	private ImageView bigIv,recommendIv,participateIv;
	private TextView numTv,see_all_tv,ticketNote1,ticketNote2;
	private HorizontalListView2 horizontalListView2;
	private XListView xListView;
	private GalleryAdapter galleryAdapter;
	private int last_id=-1;
	 private ArrayList<PetPicture> datas;
	 private RecommendGridPictureAdapter adapter;
	 private Handler handler;
	 private DataBean dataBean;
	 private Activity activity;
	 public RecommendPagerFragment(DataBean dataBean,Activity activity){
		 this.dataBean=dataBean;
		 this.activity=activity;
		 init();
	 }
	 public Activity getActivity(){
		 return activity;
	 }
	public View getView(){
		return view;
	}
	public void init() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(activity).inflate(R.layout.fragment_recommend_page, null);
		handler=HandleHttpConnectionException.getInstance().getHandler(getActivity());
		initView();
		initData(dataBean);
		initListener();
	}
	private void initListener() {
		// TODO Auto-generated method stub
		recommendIv.setOnClickListener(this);
		participateIv.setOnClickListener(this);
		bigIv.setOnClickListener(this);
		/*see_all_tv.setOnClickListener(this);
		horizontalListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			
				
				
					Intent intent=new Intent(getActivity(),TicketCardActivity.class);
					intent.putExtra("animal",dataBean.animals.get(position));
					intent.putExtra("gold", dataBean.gold);
					intent.putExtra("banner", dataBean.banner);
					getActivity().startActivity(intent);
			}
		});*/
	}
	public void initData(final DataBean dataBean) {
		// TODO Auto-generated method stub
	
		this.dataBean=dataBean;
		loadImage(bigIv,dataBean.banner);
		/*if(dataBean.animals!=null){
			galleryAdapter=new GalleryAdapter(getActivity(), dataBean.animals,1);
		}else{
			galleryAdapter=new GalleryAdapter(getActivity(), new ArrayList<Animal>(),1);
		}
		
		horizontalListView2.setAdapter(galleryAdapter);*/
		if(PetApplication.myUser!=null){
			numTv.setText(""+PetApplication.myUser.ticket_num);
		}else{
			numTv.setText("0");
		}
		if(dataBean.images!=null){
			if(dataBean.images.size()==0)dataBean.images.add(new PetPicture());
			adapter=new RecommendGridPictureAdapter(getActivity(), dataBean.images,dataBean.banner.star_id,dataBean.gold,dataBean.banner.name,dataBean.banner.end_time);
			datas=dataBean.images;
		}else{
			if(datas.size()==0)datas.add(new PetPicture());
			adapter=new RecommendGridPictureAdapter(getActivity(), datas,dataBean.banner.star_id,dataBean.gold,dataBean.banner.name,dataBean.banner.end_time);
		}
		xListView.setAdapter(adapter);
adapter.setClickRecommendListener(new ClickRecommendListener() {
			
			@Override
			public void clickRecommend(TextView tv2,PetPicture petPicture) {
				// TODO Auto-generated method stub
				if(isSending)return;
				
//				if(PetApplication.myUser.ticket_num>0){
					/*PetApplication.myUser.ticket_num--;
					String str=tv2.getText().toString();
					int num=0;
					if(!StringUtil.isEmpty(str)){
						num=Integer.parseInt(str);
					}
					num++;
					tv2.setText(""+num);
					numTv.setText(""+PetApplication.myUser.ticket_num);*/
//					giveTicket(tv2,petPicture);
				if(System.currentTimeMillis()/1000>dataBean.banner.end_time){
					Intent intent2=new Intent(getActivity(),DialogNoteActivity.class);
	  				intent2.putExtra("mode", 10);
	  				intent2.putExtra("info", "本期活动已经结束啦~");
	  				getActivity().startActivity(intent2);
					return;
				}
				isSending=true;
					judgeGiveTicket(tv2, petPicture);
					
//				}else{
//					buyTicket();
//				}
				
			}

			@Override
			public void showPetsList(LinearLayout petsLayout) {
				// TODO Auto-generated method stub
				RecommendPagerFragment.this.showPetsList(petsLayout);
			}
		});
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		bigIv=(ImageView)view.findViewById(R.id.big_iv);
		recommendIv=(ImageView)view.findViewById(R.id.bt_recommend);
		participateIv=(ImageView)view.findViewById(R.id.bt_participate);
		numTv=(TextView)view.findViewById(R.id.num_tv);
		
		
		/*see_all_tv=(TextView)view.findViewById(R.id.see_all_tv);
		see_all_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);*/
		
		
		ticketNote1=(TextView)view.findViewById(R.id.ticket_num_note_tv1);
		ticketNote2=(TextView)view.findViewById(R.id.ticket_num_note_tv2);
		if(System.currentTimeMillis()/1000>dataBean.banner.end_time){
			numTv.setVisibility(View.INVISIBLE);
			recommendIv.setVisibility(View.INVISIBLE);
			participateIv.setVisibility(View.GONE);
			ticketNote2.setVisibility(View.GONE);
			ticketNote1.setText("活动结束啦~看看下边的热门照片吧~");
			/*TextView hot_tv=(TextView)view.findViewById(R.id.hot_tv);
			hot_tv.setText("本期排名");*/
		}
		/*horizontalListView2=(HorizontalListView2)view.findViewById(R.id.galleryview);*/
		xListView=(XListView)view.findViewById(R.id.listview);
		datas=new ArrayList<PetPicture>();
		
		
		
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		
		
	}
	public void setNumTv() {
		// TODO Auto-generated method stub
		if(numTv!=null&&PetApplication.myUser!=null)
		numTv.setText(""+PetApplication.myUser.ticket_num);
	}
	public void showPetsList(LinearLayout petsLinearlayout){
		see_all_tv=(TextView)petsLinearlayout.findViewById(R.id.see_all_tv);
		see_all_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		if(System.currentTimeMillis()/1000>dataBean.banner.end_time){
			TextView hot_tv=(TextView)petsLinearlayout.findViewById(R.id.hot_tv);
			hot_tv.setText("本期排名");
		}
		horizontalListView2=(HorizontalListView2)petsLinearlayout.findViewById(R.id.galleryview);
		if(dataBean.animals!=null){
			galleryAdapter=new GalleryAdapter(getActivity(), dataBean.animals,1);
		}else{
			galleryAdapter=new GalleryAdapter(getActivity(), new ArrayList<Animal>(),1);
		}
		
		horizontalListView2.setAdapter(galleryAdapter);
		see_all_tv.setOnClickListener(this);
		horizontalListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			
				
				
					Intent intent=new Intent(getActivity(),TicketCardActivity.class);
					intent.putExtra("animal",dataBean.animals.get(position));
					intent.putExtra("gold", dataBean.gold);
					intent.putExtra("banner", dataBean.banner);
					getActivity().startActivity(intent);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.big_iv:
			Intent intent1=new Intent(getActivity(),ActivityWebActivity.class);
			intent1.putExtra("banner", dataBean.banner);
			getActivity().startActivity(intent1);
			break;
		case R.id.bt_recommend:
			
				Intent intent=new Intent(getActivity(),RecommendActivity.class);
				intent.putExtra("star_id", dataBean.banner.star_id);
				intent.putExtra("gold", dataBean.gold);
				intent.putExtra("star_title", dataBean.banner.name);
				activity.startActivity(intent);
			
			
			break;
		case R.id.bt_participate:
			 if(!UserStatusUtil.isLoginSuccess(getActivity(),HomeActivity.homeActivity.popupParent,HomeActivity.homeActivity.blackLayout)){
					
					return;
				}
			  ArrayList<Animal> animals=new ArrayList<Animal>();
				if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
				  		for(int i=0;i<PetApplication.myUser.aniList.size();i++){
				  			if(PetApplication.myUser.userId==PetApplication.myUser.aniList.get(i).master_id){
				  				animals.add(PetApplication.myUser.aniList.get(i));
				  			}
				  		}
				 }
			  
			  
			
				  		if(animals.size()>0){
				  		//使用系统相机
				  			/*Intent intent2=new Intent(getActivity(),SubmitPictureTypeActivity.class);
				  			startActivity(intent2);*/
				  			showCameraAlbum(animals.get(0), 0);
				  			
				  		}else{
//					Toast.makeText(this, "只有宠物主人才可以上传照片,目前您还没有创建的萌星", Toast.LENGTH_LONG).show();
					Intent i=new Intent(getActivity(),Dialog4Activity.class);
					i.putExtra("mode", 12);
					Dialog4Activity.listener=new Dialog3ActivityListener() {
						
						@Override
						public void onClose() {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onButtonTwo() {
							// TODO Auto-generated method stub
							Intent intent=new Intent(getActivity(),NewRegisterActivity.class);
							intent.putExtra("mode", 3);
							intent.putExtra("from", 1);
							getActivity().startActivity(intent);
						}
						
						@Override
						public void onButtonOne() {
							// TODO Auto-generated method stub
							
						}
					};
					activity.startActivity(i);
				}
			
			break;
		case R.id.see_all_tv:
			Intent intent2=new Intent(getActivity(),GetTicketRankListActivity.class);
			intent2.putExtra("Banner", dataBean.banner);
			intent2.putExtra("gold", dataBean.gold);
			getActivity().startActivity(intent2);
			break;

		default:
			break;
		}
	}
	public void loadData(){
		last_id=-1;
		new Thread(new Runnable() {
			ArrayList<PetPicture> temp=new ArrayList<PetPicture>();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				UserImagesJson json1=null;
				
					if(dataBean!=null){
						if(dataBean.images!=null){
							
							temp=dataBean.images;
							getActivity().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									datas=temp;
									//TODO
									adapter.update(datas);
									adapter.notifyDataSetChanged();
									if(dataBean.animals!=null){
										galleryAdapter.update(dataBean.animals);
										galleryAdapter.notifyDataSetChanged();
									}
									numTv.setText(""+PetApplication.myUser.ticket_num);
									LogUtil.i("scroll","datas大小========="+datas.size());
									
								}
							});
						}
					}else{
						//TODO 下载失败
					}
				}
		}).start();
	}

	public void loadImage(final ImageView icon,final Banner banner){
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		DisplayImageOptions displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.noimg)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.loadImage(banner.banner, /*icon,*/ displayImageOptions,new ImageLoadingListener() {
			
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
				if(loadedImage!=null){
					Matrix matrix=new Matrix();
					matrix.setScale(Constants.screen_width*1f/loadedImage.getWidth(), Constants.screen_width*1f/loadedImage.getWidth());
					icon.setImageBitmap(Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(),matrix,true));
				}
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
		ImageLoader imageloader2=ImageLoader.getInstance();
		imageloader2.loadImage(banner.icon,displayImageOptions,new ImageLoadingListener() {
			
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
				String name="article";
				
				if(loadedImage!=null){
					
				
				String path=ImageUtil.compressImage(loadedImage, name);
				if(!StringUtil.isEmpty(path)){
					banner.icon_path=path;
				}else{
					banner.icon_path=ImageUtil.compressImage(loadedImage, name);
				}
				}
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
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
		final View view=LayoutInflater.from(getActivity()).inflate(R.layout.popup_user_homepage, null);
		Animation animation=AnimationUtils.loadAnimation(getActivity(), R.anim.anim_translate_showtopic_addcommentlayout_in);
		view.clearAnimation();
		view.setAnimation(animation);
		animation.start();
		
		HomeActivity.homeActivity.blackLayout.removeAllViews();
		HomeActivity.homeActivity.blackLayout.addView(view);
		HomeActivity.homeActivity.blackLayout.setVisibility(View.VISIBLE);
		HomeActivity.homeActivity.blackLayout.setClickable(true);
		TextView camera=(TextView)view.findViewById(R.id.textView1);
		TextView album=(TextView)view.findViewById(R.id.textView2);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(getActivity(),TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				if(animal!=null){
					intent2.putExtra("animal", animal);
					intent2.putExtra("isBeg", mode);
					intent2.putExtra("star_id", dataBean.banner.star_id);
					intent2.putExtra("topic_name", dataBean.banner.name);
				}else{
					intent2.putExtra("animal", PetApplication.myUser.currentAnimal);
					intent2.putExtra("isBeg", mode);
					intent2.putExtra("star_id", dataBean.banner.star_id);
					intent2.putExtra("topic_name", dataBean.banner.name);
				}
				
				activity.startActivity(intent2);
				HomeActivity.homeActivity.blackLayout.setVisibility(View.INVISIBLE);
				HomeActivity.homeActivity.blackLayout.setClickable(false);
						isShowingCameraAlbum=false;
						
				
			}
		});
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(getActivity(),AlbumPictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				
				if(animal!=null){
					intent2.putExtra("animal", animal);
					intent2.putExtra("isBeg", mode);
					intent2.putExtra("star_id", dataBean.banner.star_id);
					intent2.putExtra("topic_name", dataBean.banner.name);
				}else{
					intent2.putExtra("animal", PetApplication.myUser.currentAnimal);
					intent2.putExtra("isBeg", mode);
					intent2.putExtra("star_id", dataBean.banner.star_id);
					intent2.putExtra("topic_name", dataBean.banner.name);
				}
				activity.startActivity(intent2);
				
			HomeActivity.homeActivity.blackLayout.setVisibility(View.INVISIBLE);
			HomeActivity.homeActivity.blackLayout.setClickable(false);
						isShowingCameraAlbum=false;
			
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation=AnimationUtils.loadAnimation(getActivity(), R.anim.anim_translate_showtopic_addcommentlayout_out);
				view.clearAnimation();
				view.setAnimation(animation);
				animation.start();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HomeActivity.homeActivity.blackLayout.setVisibility(View.INVISIBLE);
						HomeActivity.homeActivity.blackLayout.setClickable(false);
						isShowingCameraAlbum=false;
					}
				}, 300);
				
			}
		});
	}
	boolean isSending=false;
	private void buyTicket() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(getActivity(),Dialog4Activity.class);
		intent.putExtra("mode", 11);
		Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
			
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				isSending=false;
			}
			
			@Override
			public void onButtonTwo() {
				// TODO Auto-generated method stub
				doBuyTicket();
				isSending=false;
			}
			
			@Override
			public void onButtonOne() {
				// TODO Auto-generated method stub
				isSending=false;
				
			}
		};
		activity.startActivity(intent);
	}
	public void doBuyTicket(){
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final boolean result=HttpUtil.starImageBuyTicketApi(handler, dataBean.banner.star_id, getActivity());
				if(getActivity()!=null)
					getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(result){
							PetApplication.myUser.ticket_num+=3;
							numTv.setText("x "+PetApplication.myUser.ticket_num);
						}else{
							Toast.makeText(getActivity(), "购买成功", Toast.LENGTH_LONG).show();
						}
						
						isSending=false;
					}
				});
			}
		}).start();
	}
	public void judgeGiveTicket(final TextView tv2,final  PetPicture petPicture){
		 if(!UserStatusUtil.isLoginSuccess(getActivity(),HomeActivity.homeActivity.popupParent,HomeActivity.homeActivity.black_layout)){
			 return ;
		 }
		 if(PetApplication.myUser!=null){
			 if(PetApplication.myUser!=null&&PetApplication.myUser.ticket_num>0){
				 giveTicket(tv2, petPicture);
				 return ;
			 }
			 if(PetApplication.myUser.coinCount<dataBean.gold){
				
				 if(Constants.CON_VERSION.equals(StringUtil.getAPKVersionName(getActivity()))){
					 Intent intent=new Intent(HomeActivity.homeActivity,DialogNoteActivity.class);
						intent.putExtra("mode", 10);
						intent.putExtra("info", "金币不足");
						HomeActivity.homeActivity.startActivity(intent);
					
				 }else{
					 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
							
							@Override
							public void onClose() {
								// TODO Auto-generated method stub
								isSending=false;
							}
							
							@Override
							public void onButtonTwo() {
								// TODO Auto-generated method stub
								isSending=false;
								Intent intent=new Intent(getActivity(),ChargeActivity.class);
								getActivity().startActivity(intent);
							}
							
							@Override
							public void onButtonOne() {
								// TODO Auto-generated method stub
								isSending=false;
							}
						};
						 Intent intent=new Intent(getActivity(),Dialog4Activity.class);
						 intent.putExtra("mode", 14);
						 intent.putExtra("num", dataBean.gold);
						 getActivity().startActivity(intent);
				 }
				 
				 
					 return ;
			 }
			
			 SharedPreferences sp=getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
			 boolean flag=sp.getBoolean(Constants.GIVE_TICKET_NOTE_SHOW, false);
			 
			 if(!flag){
				 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
					
					@Override
					public void onClose() {
						// TODO Auto-generated method stub
						isSending=false;
					}
					
					@Override
					public void onButtonTwo() {
						// TODO Auto-generated method stub
						giveTicket(tv2, petPicture);
					}
					
					@Override
					public void onButtonOne() {
						// TODO Auto-generated method stub
						isSending=false;
					}
				};
				 Intent intent=new Intent(getActivity(),Dialog4Activity.class);
				 intent.putExtra("mode", 13);
				 intent.putExtra("num", dataBean.gold);
				 getActivity().startActivity(intent);
			 }else{
				 giveTicket(tv2, petPicture);; 
			 }
			 
		 }
	}
	public void giveTicket(final TextView tv2,final PetPicture petPicture){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final boolean result=HttpUtil.starImageGiveTicketApi(handler, petPicture.img_id, getActivity());
				if(getActivity()!=null)
					getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(result){
							PetApplication.myUser.ticket_num--;
							PetApplication.myUser.coinCount=PetApplication.myUser.coinCount-dataBean.gold;
							petPicture.stars++;
							tv2.setText(""+petPicture.stars);
							if(PetApplication.myUser.ticket_num<0)PetApplication.myUser.ticket_num=0;
							if(PetApplication.myUser.coinCount<0)PetApplication.myUser.coinCount=0;
							numTv.setText("x "+PetApplication.myUser.ticket_num);
						}else{
							Toast.makeText(getActivity(), "推荐失败~", Toast.LENGTH_LONG).show();
						}
						isSending=false;
					}
				});
			}
		}).start();
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
			page=0;
		new Thread(new Runnable() {
			ArrayList<PetPicture> temp=null;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				temp=HttpUtil.starImageListApi(handler, dataBean.banner.star_id,page, getActivity());
				if(getActivity()!=null){
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(temp!=null){
								if(temp.size()==0)temp.add(new PetPicture());
								dataBean.images=temp;
								adapter.update(dataBean.images);
								adapter.notifyDataSetChanged();
							}
							xListView.stopRefresh();;
						}
					});
				}
			}
		}).start();
	}
	int page=0;
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if(dataBean.images==null){
			page=0;
		}else{
			page++;
		}
		new Thread(new Runnable() {
			ArrayList<PetPicture> temp=null;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				temp=HttpUtil.starImageListApi(handler, dataBean.banner.star_id,page, getActivity());
				if(getActivity()!=null){
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(temp!=null){
								for(int i=0;i<temp.size();i++){
									if(!dataBean.images.contains(temp.get(i))){
										dataBean.images.add(temp.get(i));
									}
								}
								if(dataBean.images.size()>0&&StringUtil.isEmpty(dataBean.images.get(0).url))dataBean.images.remove(0);
								adapter.update(dataBean.images);
								adapter.notifyDataSetChanged();
							}
							xListView.stopLoadMore();
						}
					});
				}
			}
		}).start();
	}

}
