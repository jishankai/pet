package com.aidigame.hisun.imengstar.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.huanxin.ChatActivity;
import com.aidigame.hisun.imengstar.ui.ModifyPetInfoActivity;
import com.aidigame.hisun.imengstar.ui.NewPetKingdomActivity;
import com.aidigame.hisun.imengstar.ui.NewShowTopicActivity;
import com.aidigame.hisun.imengstar.ui.TakePictureBackground;
import com.aidigame.hisun.imengstar.ui.TouchActivity;
import com.aidigame.hisun.imengstar.ui.WarningDialogActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.widget.fragment.DialogJoinKingdom;
import com.aidigame.hisun.imengstar.widget.fragment.DialogNote;
import com.aidigame.hisun.imengstar.widget.fragment.DialogQuitKingdom;
import com.aidigame.hisun.imengstar.widget.fragment.DialogJoinKingdom.ResultListener;
import com.aidigame.hisun.imengstar.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 显示更多功能，1，王国资料界面；2，用户资料界面；3，照片详情界面
 * @author admin
 *
 */
public class ShowMore implements OnClickListener{
	LinearLayout parent;
	View view,rootParent;
	Activity activity;
	LinearLayout oneLayout,twoLayout,twoLayout2;
	ImageView weixinIv,friendIv,xinlangIv;
	TextView joinTv,focusTv,pictureOrMailTv,cancelTv,pictrueTv,modifyTv;
	Animal animal;
	MyUser user;
	HandleHttpConnectionException handleHttpConnectionException;
	public String sharePath;
	UMSocialService mController;
	int mode=0;//0为默认；1
	public ShowMore(LinearLayout parent,Activity activity,String sharePath,View rootParent){
		this.parent=parent;
		this.activity=activity;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		this.sharePath=sharePath;
		this.rootParent=rootParent;
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(activity,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		SinaSsoHandler  sinaSsoHandler=new SinaSsoHandler(activity);
		sinaSsoHandler.addToSocialSDK();
		initView();
		
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		view=LayoutInflater.from(activity).inflate(R.layout.widget_more, null);
		parent.removeAllViews();
		parent.setVisibility(View.VISIBLE);
		parent.addView(view);
		rootParent.setBackgroundColor(activity.getResources().getColor(R.color.window_black_bagd));
		oneLayout=(LinearLayout)view.findViewById(R.id.one_button_layout);
		twoLayout=(LinearLayout)view.findViewById(R.id.two_button_layout);
		twoLayout2=(LinearLayout)view.findViewById(R.id.two_button_layout2);
		weixinIv=(ImageView)view.findViewById(R.id.imageView2);
		friendIv=(ImageView)view.findViewById(R.id.imageView1);
		xinlangIv=(ImageView)view.findViewById(R.id.imageView4);
		joinTv=(TextView)view.findViewById(R.id.textView6);
		focusTv=(TextView)view.findViewById(R.id.textView7);
		pictureOrMailTv=(TextView)view.findViewById(R.id.textView5);
		cancelTv=(TextView)view.findViewById(R.id.cancel_tv);
		pictrueTv=(TextView)view.findViewById(R.id.textView9);
		modifyTv=(TextView)view.findViewById(R.id.textView10);
		
	}
	boolean rootCanTouch=true;
	private void initListener() {
		// TODO Auto-generated method stub
		share();
		joinTv.setOnClickListener(this);
		focusTv.setOnClickListener(this);
//		pictureOrMailTv.setOnClickListener(this);
		cancelTv.setOnClickListener(this);
		pictrueTv.setOnClickListener(this);
		modifyTv.setOnClickListener(this);
		rootParent.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(rootCanTouch){
					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						rootParent.setBackgroundDrawable(null);;
						parent.setVisibility(View.INVISIBLE);
						rootCanTouch=false;
						break;

					default:
						break;
					}
					return true;
				}
				return false;
			}
		});
	}
	public void kindomShowMore(Animal animal){
		this.animal=animal;
		/*
		 * 自己的王国
		 */
		if(PetApplication.myUser==null){
			twoLayout.setVisibility(View.GONE);
			oneLayout.setVisibility(View.GONE);
			twoLayout2.setVisibility(View.GONE);
		}else
		if(animal.master_id==PetApplication.myUser.userId){
//			pictureOrMailTv.setText("拍照");
			twoLayout.setVisibility(View.GONE);
			oneLayout.setVisibility(View.GONE);
//			twoLayout2.setVisibility(View.VISIBLE);
			twoLayout2.setVisibility(View.GONE);
		}else{
			/*
			 * 其他人的王国
			 */
			oneLayout.setVisibility(View.GONE);
			twoLayout2.setVisibility(View.GONE);
//			twoLayout.setVisibility(View.VISIBLE);
			twoLayout.setVisibility(View.GONE);
			if(!animal.is_join){
				joinTv.setBackgroundResource(R.drawable.button_green);
				
				joinTv.setText("捧TA");
				
			}else{
				joinTv.setBackgroundResource(R.drawable.button);
				joinTv.setText("不捧了");
			}
			if(!animal.is_follow){
				focusTv.setBackgroundResource(R.drawable.button_green);
				focusTv.setText("关注萌星");
				
			}else{
				focusTv.setText("取消关注");
				focusTv.setBackgroundResource(R.drawable.button);
			}
			focusTv.setVisibility(View.GONE);
		}
	}
	public void userDossier(final MyUser user){
		/*
		 * 查看其他人的个人资料
		 */
		/*oneLayout.setVisibility(View.VISIBLE);
		twoLayout.setVisibility(View.GONE);
		pictureOrMailTv.setText("私信");*/
		/*
		 * 查看自己的个人资料
		 */
		
		this.user=user;
		if(PetApplication.myUser==null){
			oneLayout.setVisibility(View.GONE);
			twoLayout.setVisibility(View.GONE);
			twoLayout2.setVisibility(View.GONE);
		}else
		if(PetApplication.myUser.userId==user.userId){
			oneLayout.setVisibility(View.VISIBLE);
			twoLayout.setVisibility(View.GONE);
			twoLayout2.setVisibility(View.GONE);
			pictureOrMailTv.setText("修改资料");
			pictureOrMailTv.setBackgroundResource(R.drawable.button_gray_light2);;
			 view.findViewById(R.id.report_tv).setVisibility(View.GONE);
			pictureOrMailTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(activity,ModifyPetInfoActivity.class);
					intent.putExtra("mode", 2);
					activity.startActivity(intent);
					parent.setVisibility(View.INVISIBLE);
					rootParent.setBackgroundDrawable(null);
					rootCanTouch=false;
				}
			});
			 view.findViewById(R.id.report_tv).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(activity, "不能举报自己", Toast.LENGTH_LONG).show();
					/*	Intent intent=new Intent(activity,WarningDialogActivity.class);
						intent.putExtra("mode", 3);
						intent.putExtra("usr_id", user.userId);
						activity.startActivity(intent);*/
					}
				});
		}else{
			oneLayout.setVisibility(View.VISIBLE);
			twoLayout.setVisibility(View.GONE);
			twoLayout2.setVisibility(View.GONE);
			pictureOrMailTv.setText("私信");
            pictureOrMailTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent2=new Intent(activity,ChatActivity.class);
					intent2.putExtra("user", user);
					activity.startActivity(intent2);
					parent.setVisibility(View.INVISIBLE);
					rootParent.setBackgroundDrawable(null);
					rootCanTouch=false;
				}
			});
            view.findViewById(R.id.report_tv).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(activity,WarningDialogActivity.class);
					intent.putExtra("mode", 3);
					intent.putExtra("usr_id", user.userId);
					activity.startActivity(intent);
				}
			});
            
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageView2:
			
			break;
		case R.id.imageView1:
			
			break;
		case R.id.imageView4:
			
			break;
		case R.id.textView6:
			/*
			 * 加入王国
			 */
			
			joinkingdom();
			parent.setVisibility(View.INVISIBLE);
			rootParent.setBackgroundDrawable(null);
			rootCanTouch=false;
			break;
		case R.id.textView7:
			/*
			 * 关注王国
			 */
			focusKingdom();
			parent.setVisibility(View.INVISIBLE);
			rootParent.setBackgroundDrawable(null);
			rootCanTouch=false;
			break;
		case R.id.textView5:
			if(animal!=null){
				Intent intent2=new Intent(activity,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				intent2.putExtra("animal", animal);
				activity.startActivity(intent2);
			}else if(user!=null){
				Intent intent2=new Intent(activity,ChatActivity.class);
				intent2.putExtra("user", user);
				activity.startActivity(intent2);
			}
			parent.setVisibility(View.INVISIBLE);
			rootParent.setBackgroundDrawable(null);
			rootCanTouch=false;
			break;
		case R.id.cancel_tv:
			parent.setVisibility(View.INVISIBLE);
			parent.setVisibility(View.INVISIBLE);
			rootParent.setBackgroundDrawable(null);
			rootCanTouch=false;
			break;
		case R.id.textView9:
			if(animal!=null){
				Intent intent2=new Intent(activity,TakePictureBackground.class);
				intent2.putExtra("mode", TakePictureBackground.MODE_TOPIC);
				intent2.putExtra("animal", animal);
				activity.startActivity(intent2);
			}
			parent.setVisibility(View.INVISIBLE);
			rootParent.setBackgroundDrawable(null);
			rootCanTouch=false;
			break;
		case R.id.textView10:
			Intent intent=new Intent(activity,ModifyPetInfoActivity.class);
			intent.putExtra("mode", 1);
			intent.putExtra("animal", animal);
			activity.startActivity(intent);
			parent.setVisibility(View.INVISIBLE);
			rootParent.setBackgroundDrawable(null);
			rootCanTouch=false;
			break;
		}
		
	}

	private void focusKingdom() {
		// TODO Auto-generated method stub
		if(animal==null)return;
		
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean flag=false;
				if(animal.is_follow){
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(activity instanceof NewPetKingdomActivity){
								final NewPetKingdomActivity p=(NewPetKingdomActivity)activity;
								DialogNote dialog=new DialogNote(p.popupParent, p, p.black_layout, 2);
								dialog.setAnimal(animal);
								dialog.setListener(new DialogNote.ResultListener() {
									
									@Override
									public void getResult(boolean isSuccess) {
										// TODO Auto-generated method stub
										if(isSuccess){
											focusTv.setBackgroundResource(R.drawable.button_green);
											focusTv.setText("关注");
											p.fansNum(animal.followers, isSuccess);
										}
									}
								});
							}
							
						}
					});
				}else{
					flag=HttpUtil.userAddFollow(animal, null,activity);
					if(flag){
						animal.is_follow=true;
                       activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(activity, "添加关注成功", Toast.LENGTH_SHORT).show();
								focusTv.setBackgroundResource(R.drawable.button);
								focusTv.setText("取消关注");
								animal.is_follow=true;
								animal.followers++;
								final NewPetKingdomActivity p=(NewPetKingdomActivity)activity;
								p.fansNum(animal.followers, true);
							}
						});
					}else{
						
//						 * 添加关注失败
						 
					}
				}
			}
		}).start();
	}

	private void joinkingdom() {
		// TODO Auto-generated method stub
		if(animal==null)return;
		if(activity instanceof NewPetKingdomActivity){
			final NewPetKingdomActivity p=(NewPetKingdomActivity)activity;
			if(!animal.is_join){
				/*
				 * 退出王国
				 */
			if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.size()>=10&&NewPetKingdomActivity.petKingdomActivity!=null){
				int num=0;
				if(PetApplication.myUser.aniList.size()>=10&&PetApplication.myUser.aniList.size()<=20){
					num=(PetApplication.myUser.aniList.size()+1)*5;
				}else if(PetApplication.myUser.aniList.size()>20){
					num=100;
				}
				
				if(PetApplication.myUser.coinCount<num){
					DialogNote dialog=new DialogNote(NewPetKingdomActivity.petKingdomActivity.popupParent, NewPetKingdomActivity.petKingdomActivity, NewPetKingdomActivity.petKingdomActivity.black_layout, 1);
					return;
				}
					
			}
			
			DialogJoinKingdom dialog=new DialogJoinKingdom(p.popupParent, p, p.black_layout, animal);
			dialog.setResultListener(new ResultListener() {
				
				@Override
				public void getResult(boolean isSuccess) {
					// TODO Auto-generated method stub
					
					if(isSuccess){
//						Toast.makeText(activity,Constants.user.u_nick+ "被萌星"+animal.pet_nickName+"的魅力折服，路人转粉啦~", Toast.LENGTH_LONG).show();
						p.setPeoplesNum(animal.fans,isSuccess);
					}
				}
			});
			}else{
				if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.size()==1){
					Toast.makeText(activity, "不能退出最爱萌星，现在只剩一个啦", Toast.LENGTH_LONG).show();
					parent.setVisibility(View.INVISIBLE);
					rootParent.setBackgroundDrawable(null);
					rootCanTouch=false;
					return;
				}
				DialogQuitKingdom dialog=new DialogQuitKingdom(p.popupParent, p, p.black_layout, animal);
				dialog.setResultListener(new DialogQuitKingdom.ResultListener() {
					
					@Override
					public void getResult(boolean isSuccess) {
						// TODO Auto-generated method stub
						
						if(isSuccess){
							parent.setVisibility(View.INVISIBLE);
							rootParent.setBackgroundDrawable(null);
							rootCanTouch=false;
							p.setPeoplesNum(animal.fans,isSuccess);
						}
					}
				});
			}
		}
		
      
	}
    
	public void share(){
		weixinIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.setVisibility(View.INVISIBLE);
				rootParent.setBackgroundDrawable(null);
				rootCanTouch=false;
				/*if(Constants.api==null){
					boolean flag=WeixinShare.regToWeiXin(activity);
					if(!flag){
						Toast.makeText(activity,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						return;
					}
				}*/
				UserImagesJson.Data data=new UserImagesJson.Data();
				data.path=sharePath;
				weixinShare(sharePath);
					Constants.shareMode=0;
					/*if(animal!=null){
						Constants.whereShare=2;
						
						data.des="雷达报告发现一只萌宠，火速围观！";
					}else if(user!=null){
						Constants.whereShare=3;
						data.des="我发现了一枚萌萌哒的新伙伴浅唱，可以一起愉快的玩耍啦！";
					}
					if(WeixinShare.shareBitmap(data, 1)){
						parent.setVisibility(View.INVISIBLE);
//						Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
					}else{
						parent.setVisibility(View.INVISIBLE);
						Toast.makeText(activity,"分享失败。", Toast.LENGTH_LONG).show();
					}*/
				
				
			}
		});
		friendIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.setVisibility(View.INVISIBLE);
				rootParent.setBackgroundDrawable(null);
				rootCanTouch=false;
				
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=sharePath;
					Constants.shareMode=1;
					friendShare(sharePath);
					if(animal!=null){
						Constants.whereShare=2;
						
						data.des="雷达报告发现一只萌宠，火速围观！";
					}else if(user!=null){
						Constants.whereShare=3;
						data.des="我发现了一枚萌萌哒的新伙伴浅唱，可以一起愉快的玩耍啦！";
					}
					/*if(WeixinShare.shareBitmap(data, 2)){
						parent.setVisibility(View.INVISIBLE);
//						Toast.makeText(activity,"成功分享到微信。", Toast.LENGTH_LONG).show();
					}else{
						parent.setVisibility(View.INVISIBLE);
						Toast.makeText(activity,"分享到微信失败。", Toast.LENGTH_LONG).show();
					}*/
				
			}
		});
		xinlangIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.setVisibility(View.INVISIBLE);
				rootParent.setBackgroundDrawable(null);
				rootCanTouch=false;
				xinlangShare(sharePath);
				/*if(!UserStatusUtil.hasXinlangAuth(activity)){
					return;
				}
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=sharePath;
					if(UserStatusUtil.hasXinlangAuth(activity)){
						
						if(animal!=null){
							Constants.whereShare=2;
							data.des="雷达报告发现一只萌宠，火速围观！http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
						}else if(user!=null){
							Constants.whereShare=3;
							data.des="我发现了一枚萌萌哒新伙伴"+user.u_nick+"，可以一起愉快的玩耍啦！http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
						}
						XinlangShare.sharePicture(data,activity);
					}*/
					parent.setVisibility(View.INVISIBLE);
				
			}
		});
	}
	
	
	
	 public void weixinShare(String path){
	   	   WeiXinShareContent weixinContent = new WeiXinShareContent();
	   	 //设置分享文字
	   	 weixinContent.setShareContent("我是大萌星"+animal.pet_nickName+"，快来跟我一起玩嘛~");
	   	 //设置title
	   	 weixinContent.setTitle("我是大萌星，"+animal.pet_nickName+"~");
	   	 //设置分享内容跳转URL
	   	 weixinContent.setTargetUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=animal/infoShare&aid="+animal.a_id);
	   	 //设置分享图片
	   	 UMImage umImage=new UMImage(activity,animal.pet_iconPath );
	   	 weixinContent.setShareImage(umImage);
	   	 mController.setShareMedia(weixinContent);
//	   	 mController.openShare(this, true);
	   	 mController.postShare(activity,SHARE_MEDIA.WEIXIN, 
	   		        new SnsPostListener() {
	   		                @Override
	   		                public void onStart() {
//	   		                    Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
	   		                }
	   		                @Override
	   		                public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
	   		                	parent.setVisibility(View.INVISIBLE); 
	   		                	if (eCode == 200) {
	   		                         Toast.makeText(activity, "分享成功.", Toast.LENGTH_SHORT).show();
	   		                     } else {
	   		                          String eMsg = "";
	   		                          if (eCode == -101){
	   		                              eMsg = "没有授权";
	   		                          }
	   		                          Toast.makeText(activity, "分享失败[" + eCode + "] " + 
	   		                                             eMsg,Toast.LENGTH_SHORT).show();
	   		                     }
	   		              }
	   		});
	   	   
	   		
	      }
	      public void friendShare(String path){
	   	   CircleShareContent circleMedia = new CircleShareContent();
	   	 //设置分享文字
	   	circleMedia.setShareContent("我是大萌星，"+animal.pet_nickName+"~");
		   	 //设置title
	   	circleMedia.setTitle("我是大萌星"+animal.pet_nickName+"，快来跟我一起玩嘛~");
		   	 //设置分享内容跳转URL
	   	circleMedia.setTargetUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=animal/infoShare&aid="+animal.a_id);
	   	   UMImage umImage=new UMImage(activity, animal.pet_iconPath);
	   	   circleMedia.setShareImage(umImage);
//	   	   circleMedia.setTargetUrl("你的URL链接");
	   	   mController.setShareMedia(circleMedia);
	   	   mController.postShare(activity,SHARE_MEDIA.WEIXIN_CIRCLE,
	   			   new SnsPostListener() {
	              @Override
	              public void onStart() {
//	                  Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
	              }
	              @Override
	              public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
	            	  parent.setVisibility(View.INVISIBLE);
	            	  if (eCode == 200) {
	                    Toast.makeText(activity, "分享成功.", Toast.LENGTH_SHORT).show();
	                   } else {
	                        String eMsg = "";
	                        if (eCode == -101){
	                            eMsg = "没有授权";
	                        }
	                        Toast.makeText(activity, "分享失败[" + eCode + "] " + 
	                                           eMsg,Toast.LENGTH_SHORT).show();
	                   }
	            }
	   });
	   		
	      }
	      public void xinlangShare(String bmpPath){
	   
	   	   SinaShareContent content=new SinaShareContent();
	   	   content.setShareContent("我是大萌星"+animal.pet_nickName+"，快来跟我一起玩嘛~"+"http://"+Constants.IP+Constants.URL_ROOT+"r=animal/infoShare&aid="+animal.a_id+" #我是大萌星#");
	   	   UMImage umImage=new UMImage(activity, animal.pet_iconPath);
	   	  
	   	   content.setShareImage(umImage);
	   	   mController.setShareMedia(content);
	   	   mController.postShare(activity, SHARE_MEDIA.SINA,new SnsPostListener() {
	   		
	   		@Override
	   		public void onStart() {
	   			// TODO Auto-generated method stub
	   			
	   		}
	   		
	   		@Override
	   		public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
	   			// TODO Auto-generated method stub
	   			if (eCode == 200) {
	                   Toast.makeText(activity, "分享成功.", Toast.LENGTH_SHORT).show();
	                  } else {
	                       String eMsg = "";
	                       if (eCode == -101){
	                           eMsg = "没有授权";
	                       }
	                       Toast.makeText(activity, "分享失败[" + eCode + "] " + 
	                                          eMsg,Toast.LENGTH_SHORT).show();
	                  }
	   		}
	   	});
	   	   
	      }
	
	boolean justShare=false;
	public void onlyCanShare(){
		joinTv.setClickable(false);
		focusTv.setClickable(false);
		pictureOrMailTv.setClickable(false);
		pictureOrMailTv.setVisibility(View.GONE);
		twoLayout.setVisibility(View.GONE);
		justShare=true;
	}
}
