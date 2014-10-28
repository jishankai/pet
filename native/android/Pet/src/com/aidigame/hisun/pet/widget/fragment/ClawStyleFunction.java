package com.aidigame.hisun.pet.widget.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.ui.NewRegisterActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.ShowTopicActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.view.arcmenu.ArcMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 界面底部的爪型弹出按钮
 * @author admin
 *
 */
public class ClawStyleFunction {
   private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera, R.drawable.composer_music,
			R.drawable.composer_place, R.drawable.composer_sleep/*, R.drawable.composer_thought, R.drawable.composer_with*/ };
   View view;
   Activity context;
   ArcMenu arcMenu1,arcMenu2;//底部可弹出菜单
   ClawFunctionChoseListener clawFunctionChoseListener;
   DisplayImageOptions displayImageOptions;
//   Animal animal;
   HandleHttpConnectionException handleHttpConnectionException;
   
   boolean isBite=true;
   boolean isShake=true;
   RelativeLayout parent,clickLayout;
   Handler handler=new Handler(){
	   public void handleMessage(android.os.Message msg) {
		   if(Constants.user!=null){
				ImageLoader imageLoader=ImageLoader.getInstance();
				imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+Constants.user.pet_iconUrl, arcMenu1.getImageView(), displayImageOptions);
				imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+Constants.user.pet_iconUrl, arcMenu2.getImageView(), displayImageOptions);
			}
	   };
   };
   public ClawStyleFunction(Activity context,boolean isBite,boolean isShake){
	 view=LayoutInflater.from(context).inflate(R.layout.fragment_claw_menu, null);
	clickLayout=(RelativeLayout)view.findViewById(R.id.relativelayout);
	
	 handleHttpConnectionException=HandleHttpConnectionException.getInstance();
	   this.context=context;
	   this.isBite=isBite;
	   this.isShake=isShake;
	   BitmapFactory.Options options=new BitmapFactory.Options();
	   options.inJustDecodeBounds=false;
		options.inSampleSize=2;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
               .build();
	   initView();
	   initListener();
   }

   private void initListener() {
	// TODO Auto-generated method stub
	
   }

  private void initView() {
	// TODO Auto-generated method stub
	  arcMenu1=(ArcMenu)view.findViewById(R.id.arc_menu_1);
      arcMenu2=(ArcMenu)view.findViewById(R.id.arc_menu_2);
      parent=(RelativeLayout)view.findViewById(R.id.relativelayout);
	  initArcMenu();
  }
  public View getView(){
		return view;
  }
  public ImageView getImageView1(){
	  return arcMenu1.getImageView();
  }
  public ImageView getImageView2(){
	  return arcMenu2.getImageView();
  }
  boolean isJumping=false;
  boolean isShow=false;
  private void initArcMenu() {
		// TODO Auto-generated method stub
		arcMenu2.setVisibility(View.INVISIBLE);
		arcMenu1.setVisibility(View.INVISIBLE);
		arcMenu1.setInvisible(true,arcMenu2);
		parent.setBackgroundDrawable(null);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(Constants.user==null){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(1);
			}
		}).start();
		if(Constants.user!=null&&Constants.user.currentAnimal!=null){
			final ImageLoader imageLoader=ImageLoader.getInstance();
			LogUtil.i("me", "头像地址url1+"+Constants.user.currentAnimal.pet_iconUrl);
			 
			imageLoader.loadImage(Constants.ANIMAL_DOWNLOAD_TX+Constants.user.currentAnimal.pet_iconUrl,/*arcMenu1.getImageView(), */displayImageOptions,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					LogUtil.i("scroll", "开始下载");
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					LogUtil.i("scroll", ""+failReason.toString());
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					LogUtil.i("scroll", "下载成功");
					arcMenu2.getImageView().setImageBitmap(loadedImage);
					arcMenu1.getImageView().setImageBitmap(loadedImage);
					
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					LogUtil.i("scroll", "取消下载");
					imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+Constants.user.currentAnimal.pet_iconUrl, arcMenu1.getImageView());
				}
			});
			LogUtil.i("me", "头像地址url2+"+Constants.user.currentAnimal.pet_iconUrl);
			
			imageLoader.loadImage(Constants.ANIMAL_DOWNLOAD_TX+Constants.user.currentAnimal.pet_iconUrl,/*arcMenu2.getImageView(),*/displayImageOptions,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					LogUtil.i("scroll", "开始下载");
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					LogUtil.i("scroll", ""+failReason.toString());
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					LogUtil.i("scroll", "下载成功");
					arcMenu2.getImageView().setImageBitmap(loadedImage);
					arcMenu1.getImageView().setImageBitmap(loadedImage);
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					LogUtil.i("scroll", "取消下载");
					imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+Constants.user.currentAnimal.pet_iconUrl, arcMenu2.getImageView());
				}
			});
		}else{
			LogUtil.i("scroll", "快捷头像设置为默认值");
			arcMenu1.getImageView().setImageResource(R.drawable.pet_icon);
			arcMenu2.getImageView().setImageResource(R.drawable.pet_icon);
		}
		parent.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(isShow){
				if(arg1.getAction()==MotionEvent.ACTION_UP){
					
						isShow=false;
						 arcMenu2.swichState();
						 handler.postDelayed(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									parent.setBackgroundDrawable(null);
									arcMenu2.setVisibility(View.INVISIBLE);
									arcMenu1.setVisibility(View.VISIBLE);
								}
							}, 410);
					}
				return true;
				}
				return false;
			}
		});
		
		arcMenu1.controlLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					arcMenu1.setVisibility(View.INVISIBLE);
					parent.setBackgroundResource(R.color.window_black_bagd);
                  handler.postDelayed(new Runnable() {
  					
  					@Override
  					public void run() {
  						// TODO Auto-generated method stub
//  						parent.setClickable(true);
  						
  						arcMenu2.swichState();
  						isShow=true;
  						/*parent.setEnabled(true);
  						parentClickable=true;
  						parent.setFocusable(true);
  						parent.setClickable(true);
						parent.setFocusableInTouchMode(true);*/
  						
  						
						
						
  					}
  				}, 150);
                  arcMenu2.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
     arcMenu2.controlLayout.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction()==MotionEvent.ACTION_DOWN){
             arcMenu2.swichState();
             if(context instanceof NewHomeActivity){
					NewHomeActivity n=(NewHomeActivity)context;
					if(!UserStatusUtil.isLoginSuccess(context,n.homeFragment.popupParent,n.homeFragment.black_layout)){
						 parent.setBackgroundDrawable(null);
//						n.homeFragment.setBlurImageBackground();
						isShow=false;
						arcMenu2.setVisibility(View.INVISIBLE);
						arcMenu1.setVisibility(View.VISIBLE);
					    handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								
							}
						}, 410);
						return false;
					}
				}else if(context instanceof PetKingdomActivity){
					PetKingdomActivity n=(PetKingdomActivity)context;
					if(!UserStatusUtil.isLoginSuccess(context,n.popupParent,n.black_layout)){
//					    n.setBlurImageBackground();
					    handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								isShow=false;
								arcMenu2.setVisibility(View.INVISIBLE);
								arcMenu1.setVisibility(View.VISIBLE);
								parent.setBackgroundDrawable(null);
							}
						}, 410);
						return false;
					}
				}else if(context instanceof ShowTopicActivity){
					ShowTopicActivity n=(ShowTopicActivity)context;
					if(!UserStatusUtil.isLoginSuccess(context,n.popupParent,n.black_layout)){
//					    n.homeFragment.setBlurImageBackground();
					    handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								isShow=false;
								arcMenu2.setVisibility(View.INVISIBLE);
								arcMenu1.setVisibility(View.VISIBLE);
								parent.setBackgroundDrawable(null);
							}
						}, 410);
						return false;
					}
				}
             if(PetKingdomActivity.petKingdomActivity!=null)PetKingdomActivity.petKingdomActivity.finish();
             Intent intent=new Intent(context,PetKingdomActivity.class);
				intent.putExtra("animal", Constants.user.currentAnimal);
				context.startActivity(intent);
      		handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						isShow=false;
						arcMenu2.setVisibility(View.INVISIBLE);
						arcMenu1.setVisibility(View.VISIBLE);
						parent.setBackgroundDrawable(null);
					}
				}, 410);
			}
			
			return false;
		}
	});
		 final int itemCount = ITEM_DRAWABLES.length;
//	        for (int i = 0; i < itemCount; i++) {
//	            ImageView item = new ImageView(context);
//	            item.setImageResource(ITEM_DRAWABLES[i]);

//	            final int position = i;
		 View view1=LayoutInflater.from(context).inflate(R.layout.item_claw_function, null);
		 ((ImageView)(view1.findViewById(R.id.imageView1))).setImageResource(R.drawable.claw_shake);
		 if(isShake){
			 ((ImageView)(view1.findViewById(R.id.imageView1))).setImageResource(R.drawable.claw_shake);
			 ((TextView)(view1.findViewById(R.id.textView1))).setText("摇一摇");
		 }else{
			 ((ImageView)(view1.findViewById(R.id.imageView1))).setImageResource(R.drawable.play_joke);
			 ((TextView)(view1.findViewById(R.id.textView1))).setText("捣捣乱");
		 }
		 
	            arcMenu2.addItem(view1, new OnClickListener() {

	                @Override
	                public void onClick(View v) {
	                	handler.postDelayed(new Runnable() {
	    					
	    					@Override
	    					public void run() {
	    						// TODO Auto-generated method stub
	    						arcMenu2.setVisibility(View.INVISIBLE);
	    						arcMenu1.setVisibility(View.VISIBLE);
	    						parent.setBackgroundDrawable(null);
	    						isShow=false;
//	    						Toast.makeText(context, "摇一摇" , Toast.LENGTH_SHORT).show();
	    						if(clawFunctionChoseListener!=null){
	    							
	    							
	    							clawFunctionChoseListener.clickItem1();
	    						}
	    					}
	    				}, 410);
	                    
	                }
	            });
//	        }
	            View view2=LayoutInflater.from(context).inflate(R.layout.item_claw_function, null);
	   		 ((ImageView)(view2.findViewById(R.id.imageView1))).setImageResource(R.drawable.claw_gift);
	   		 ((TextView)(view2.findViewById(R.id.textView1))).setText("送礼物");
	   		 arcMenu2.addItem(view2, new OnClickListener() {

	   	                @Override
	   	                public void onClick(View v) {
	   	                	handler.postDelayed(new Runnable() {
	   	    					
	   	    					@Override
	   	    					public void run() {
	   	    						// TODO Auto-generated method stub
	   	    						arcMenu2.setVisibility(View.INVISIBLE);
	   	    						arcMenu1.setVisibility(View.VISIBLE);
	   	    						isShow=false;
	   	    						parent.setBackgroundDrawable(null);
//	   	    						Toast.makeText(context, "送礼物", Toast.LENGTH_SHORT).show();
	   	    						if(clawFunctionChoseListener!=null){
	   	    							
		    							clawFunctionChoseListener.clickItem2();
		    						}
	   	    					}
	   	    				}, 410);
	   	                    
	   	                }
	   	            });
	   	         View view3=null;
	   			
	   			 if(isBite){
	   				view3=LayoutInflater.from(context).inflate(R.layout.item_claw_function, null);
	   				((TextView)(view3.findViewById(R.id.textView1))).setText("萌叫叫"); 
	   			 ((ImageView)(view3.findViewById(R.id.imageView1))).setImageResource(R.drawable.claw_bite);
	   			 }else{
	   				view3=LayoutInflater.from(context).inflate(R.layout.item_claw_function, null);
	   				 ((ImageView)(view3.findViewById(R.id.imageView1))).setImageResource(R.drawable.touch);
	   				 ((TextView)(view3.findViewById(R.id.textView1))).setText("萌印象");
	   			 }
	   		     arcMenu2.addItem(view3, new OnClickListener() {

	   		                @Override
	   		                public void onClick(View v) {
	   		                	handler.postDelayed(new Runnable() {
	   		    					
	   		    					@Override
	   		    					public void run() {
	   		    						// TODO Auto-generated method stub
	   		    						arcMenu2.setVisibility(View.INVISIBLE);
	   		    						arcMenu1.setVisibility(View.VISIBLE);
	   		    						isShow=false;
	   		    						parent.setBackgroundDrawable(null);
//	   		    						Toast.makeText(context, "叫一叫" , Toast.LENGTH_SHORT).show();
	   		    						if(clawFunctionChoseListener!=null){
	   		    							
	   		    						 	clawFunctionChoseListener.clickItem3();
	   		    						}
	   		    					}
	   		    				}, 410);
	   		                    
	   		                }
	   		            });
	   		         View view4=LayoutInflater.from(context).inflate(R.layout.item_claw_function, null);
	   				 ((ImageView)(view4.findViewById(R.id.imageView1))).setImageResource(R.drawable.claw_play);
	   				 ((TextView)(view4.findViewById(R.id.textView1))).setText("游乐园");
	   			            arcMenu2.addItem(view4, new OnClickListener() {

	   			                @Override
	   			                public void onClick(View v) {
	   			                	handler.postDelayed(new Runnable() {
	   			    					
	   			    					@Override
	   			    					public void run() {
	   			    						// TODO Auto-generated method stub
	   			    						arcMenu2.setVisibility(View.INVISIBLE);
	   			    						arcMenu1.setVisibility(View.VISIBLE);
	   			    						isShow=false;
	   			    						parent.setBackgroundDrawable(null);
//	   			    						Toast.makeText(context, "摸一摸" , Toast.LENGTH_SHORT).show();
	   			    						if(clawFunctionChoseListener!=null){
	   			    							
	   			    							clawFunctionChoseListener.clickItem4();
	   			    						}
	   			    					}
	   			    				}, 410);
	   			                    
	   			                }
	   			            });
	        new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					while(arcMenu1.getMeasuredHeight()==0){
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					context.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							MarginLayoutParams params=(MarginLayoutParams)arcMenu1.getLayoutParams();
					        params.bottomMargin=-arcMenu1.getMeasuredHeight()/2;
					        arcMenu1.setLayoutParams(params);
					        arcMenu1.setVisibility(View.VISIBLE);
					        MarginLayoutParams params2=(MarginLayoutParams)arcMenu2.getLayoutParams();
					        params2.bottomMargin=-arcMenu2.getMeasuredHeight()*3/8;
					        arcMenu2.setLayoutParams(params2);
						}
					});
				}
			}).start();
	}
  public void setClawFunctionChoseListener(ClawFunctionChoseListener listener){
	  this.clawFunctionChoseListener=listener;
  }
	public static interface ClawFunctionChoseListener{
		void clickItem1();
		void clickItem2();
		void clickItem3();
		void clickItem4();
	}

}
