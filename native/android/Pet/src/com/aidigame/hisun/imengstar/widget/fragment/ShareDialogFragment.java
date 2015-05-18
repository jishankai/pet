package com.aidigame.hisun.imengstar.widget.fragment;

import java.util.ArrayList;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.ui.ChargeActivity;
import com.aidigame.hisun.imengstar.ui.Dialog4Activity;
import com.aidigame.hisun.imengstar.ui.DialogNoteActivity;
import com.aidigame.hisun.imengstar.ui.NewShowTopicActivity;
import com.aidigame.hisun.imengstar.ui.WarningDialogActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.widget.fragment.DialogJoinKingdom.ResultListener;
import com.aidigame.hisun.imengstar.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment") public class ShareDialogFragment extends Fragment {
	/**
	 * 分享模块
	 */
	private View view;
	private PetPicture petPicture;
	private UMSocialService mController;
	private LinearLayout one_button_layout;
	private Handler handler;
	private ShareDialogFragmentResultListener shareDialogFragmentResultListener;
	private View popupParent,black_layout;
	private int mode=1;//1,照片详情页面分享；2精选或关注列表分享;3,投票界面分享
	public ShareDialogFragment(){
		super();
	}
	public ShareDialogFragment(PetPicture petPicture,View popupParent,View black_layout,int mode){
		this.petPicture=petPicture;
		this.popupParent=popupParent;
		this.black_layout=black_layout;
		this.mode=mode;
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.fragment_share_dialog, null);
		return view;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initShareView();
		initMoreView();
	}
	/**
	 * 分享 按钮
	 */
	private  RelativeLayout shareLayout;
	private void initShareView() {
		// TODO Auto-generated method stub
		handler=HandleHttpConnectionException.getInstance().getHandler(getActivity());
		mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(getActivity(),Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		SinaSsoHandler  sinaSsoHandler=new SinaSsoHandler(getActivity());
		sinaSsoHandler.addToSocialSDK();
		
		
		shareLayout=(RelativeLayout)view.findViewById(R.id.sharelayout);
		shareLayout.setVisibility(View.VISIBLE);
		LinearLayout weixinLayout=(LinearLayout)view.findViewById(R.id.imageView22);
		LinearLayout friendLayout=(LinearLayout)view.findViewById(R.id.imageView32);
		LinearLayout xinlangLayout=(LinearLayout)view.findViewById(R.id.imageView42);
		shareLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//TODO Fragment 消失操作
				shareLayout.setVisibility(View.GONE);
				if(shareDialogFragmentResultListener!=null){
					shareDialogFragmentResultListener.onResult(false);
				}
				 FragmentTransaction ft=getFragmentManager().beginTransaction();
					ft.remove(ShareDialogFragment.this);
					ft.commit();
			}
		});
		weixinLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//TODO Fragment 消失操作
				shareLayout.setVisibility(View.GONE);
				weixinShare();
				
			}
		});
		friendLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//TODO Fragment 消失操作
				shareLayout.setVisibility(View.GONE);
				friendShare();
				
			}
		});
		xinlangLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//TODO Fragment 消失操作
				shareLayout.setVisibility(View.GONE);
				xinlangShare();
				
			}
		});
	}
	private void initMoreView() {
		// TODO Auto-generated method stub
		
		TextView chatTv=(TextView)view.findViewById(R.id.messagetv);
		TextView reportTv=(TextView)view.findViewById(R.id.reporttv);
		final TextView pengta_tv=(TextView)view.findViewById(R.id.pengta_tv);
		if(mode==2){
			pengta_tv.setVisibility(View.GONE);
			reportTv.setBackgroundResource(R.drawable.more_report);
			reportTv.setText("");
			chatTv.setBackgroundResource(R.drawable.more_message);
			chatTv.setText("");
			view.findViewById(R.id.report_line0).setVisibility(View.VISIBLE);
			view.findViewById(R.id.report_line2).setVisibility(View.GONE);
			view.findViewById(R.id.report_line4).setVisibility(View.VISIBLE);
			view.findViewById(R.id.report_line1).setVisibility(View.GONE);
			view.findViewById(R.id.textView7).setVisibility(View.GONE);
			view.findViewById(R.id.btns_layout2).setVisibility(View.GONE);
			view.findViewById(R.id.btns_layout1).setVisibility(View.VISIBLE);
			chatTv=(TextView)view.findViewById(R.id.messagetv2);
			reportTv=(TextView)view.findViewById(R.id.reporttv2);
		}else if(mode==3){
			view.findViewById(R.id.one_button_layout).setVisibility(View.GONE);
			view.findViewById(R.id.report_line4).setVisibility(View.GONE);
			
		}else if(mode==4){
			view.findViewById(R.id.one_button_layout).setVisibility(View.GONE);
			view.findViewById(R.id.report_line4).setVisibility(View.GONE);
			view.findViewById(R.id.report_line2).setVisibility(View.GONE);
			view.findViewById(R.id.textView7).setVisibility(View.GONE);
		}
//		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(petPicture.animal) ){
////			pengta_tv.setVisibility(View.GONE);
//		}
		pengta_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					shareLayout.setVisibility(View.INVISIBLE);
					if(shareDialogFragmentResultListener!=null){
						shareDialogFragmentResultListener.onResult(false);
					}
					 FragmentTransaction ft=getFragmentManager().beginTransaction();
						ft.remove(ShareDialogFragment.this);
						ft.commit();
					return;
				}
				if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(petPicture.animal) ){
//					pengta_tv.setVisibility(View.GONE);
					Intent intent=new Intent(getActivity(),DialogNoteActivity.class);
					intent.putExtra("mode", 10);
					intent.putExtra("info", "您已经捧TA了");
					startActivity(intent);
					shareLayout.setVisibility(View.GONE);
					if(shareDialogFragmentResultListener!=null){
						shareDialogFragmentResultListener.onResult(false);
					}
					 FragmentTransaction ft=getFragmentManager().beginTransaction();
						ft.remove(ShareDialogFragment.this);
						ft.commit();
					return ;
				}
				int num=0;
				int count=0;
				for(int i=0;i<PetApplication.myUser.aniList.size();i++){
//					if(Constants.user.aniList.get(i).master_id!=Constants.user.userId)
						count++;
				}
				
				
				if(count>=10&&count<=20){
					num=(count)*5;
				}else if(count>20){
					num=100;
				}
				
				if(PetApplication.myUser.coinCount<num){
//					DialogNote dialog=new DialogNote(popupParent, NewShowTopicActivity.this, black_layout, 1);
					/*Intent intent=new Intent(NewShowTopicActivity.this,DialogNoteActivity.class);
					intent.putExtra("mode", 10);
					intent.putExtra("info", "钱包君告急！挣够金币再来捧萌星吧");
					startActivity(intent);*/
					 Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
							
							@Override
							public void onClose() {
								// TODO Auto-generated method stub
							}
							
							@Override
							public void onButtonTwo() {
								// TODO Auto-generated method stub
								Intent intent=new Intent(getActivity(),ChargeActivity.class);
								startActivity(intent);
							}
							
							@Override
							public void onButtonOne() {
								// TODO Auto-generated method stub
							}
						};
						 Intent intent=new Intent(getActivity(),Dialog4Activity.class);
						 intent.putExtra("mode", 8);
						 intent.putExtra("num", num);
						 startActivity(intent);
						 shareLayout.setVisibility(View.INVISIBLE);
						 
						 
						 if(shareDialogFragmentResultListener!=null){
								shareDialogFragmentResultListener.onResult(false);
							}
							 FragmentTransaction ft=getFragmentManager().beginTransaction();
								ft.remove(ShareDialogFragment.this);
								ft.commit();
						 
					return;
			}
			
			
			DialogJoinKingdom dialog=new DialogJoinKingdom(popupParent, getActivity(), black_layout, petPicture.animal);
			dialog.setResultListener(new ResultListener() {
				
				@Override
				public void getResult(boolean isSuccess) {
					// TODO Auto-generated method stub
					petPicture.animal.hasJoinOrCreate=isSuccess;
//					pengta_tv.setVisibility(View.GONE);
				}
			});
			shareLayout.setVisibility(View.INVISIBLE);
			if(shareDialogFragmentResultListener!=null){
				shareDialogFragmentResultListener.onResult(false);
			}
			 FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.remove(ShareDialogFragment.this);
				ft.commit();
			}
		});
		
		
		chatTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserStatusUtil.isLoginSuccess(getActivity(),popupParent,black_layout)){
					shareLayout.setVisibility(View.INVISIBLE);
					if(shareDialogFragmentResultListener!=null){
						shareDialogFragmentResultListener.onResult(false);
					}
					 FragmentTransaction ft=getFragmentManager().beginTransaction();
						ft.remove(ShareDialogFragment.this);
						ft.commit();
					return;
				}
				
				Intent intent=new Intent(getActivity(),com.aidigame.hisun.imengstar.huanxin.ChatActivity.class);
				MyUser user=new MyUser();
				user.userId=petPicture.animal.master_id;
				user.u_iconUrl=petPicture.animal.u_tx;
				user.u_nick=petPicture.animal.u_name;
				intent.putExtra("user",user);
				getActivity().startActivity(intent);
				shareLayout.setVisibility(View.INVISIBLE);
				if(shareDialogFragmentResultListener!=null){
					shareDialogFragmentResultListener.onResult(false);
				}
				 FragmentTransaction ft=getFragmentManager().beginTransaction();
					ft.remove(ShareDialogFragment.this);
					ft.commit();
			}
		});
		reportTv.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(getActivity(),WarningDialogActivity.class);
		intent.putExtra("mode", 2);//2
		intent.putExtra("img_id", petPicture.img_id);
		getActivity().startActivity(intent);
		
		/*handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				moreLayout.setVisibility(View.INVISIBLE);
			}
		}, 500);*/
		shareLayout.setVisibility(View.INVISIBLE);
		if(shareDialogFragmentResultListener!=null){
			shareDialogFragmentResultListener.onResult(false);
		}
		 FragmentTransaction ft=getFragmentManager().beginTransaction();
			ft.remove(ShareDialogFragment.this);
			ft.commit();
	}
});
		
		
	}

	  public void weixinShare(){
		   WeiXinShareContent weixinContent = new WeiXinShareContent();
		   long time=petPicture.create_time+24*3600-System.currentTimeMillis()/1000;
		  if((!"0".equals(petPicture.is_food))&&time>0){
				 //设置分享文字
				 weixinContent.setShareContent(StringUtil.isEmpty(petPicture.cmt)?"看在我这么努力卖萌的份上快来宠宠我！免费送我点口粮好不好？":petPicture.cmt);
				 //设置title
				 weixinContent.setTitle("轻轻一点，免费赏粮！我的口粮全靠你啦~");
				 
		  }else if(mode==3){
			  weixinContent.setShareContent("这是大萌星"+petPicture.animal.pet_nickName+"在"+petPicture.star_title+"活动里的萌照，你们也来支持个~");
			  weixinContent.setTitle("我是"+petPicture.animal.pet_nickName+"，快来给我投票啦~");
//			  weixinContent.setTitle("这是大萌星"+petPicture.animal.pet_nickName+"在"+petPicture.star_title+"活动里的萌照，你们也来支持个~");
		  }else if(mode==4){
			  if(System.currentTimeMillis()/1000>petPicture.end_time){
				  weixinContent.setShareContent("大萌星"+petPicture.animal.pet_nickName+"在"+petPicture.star_title+"活动里一共得到"+petPicture.animal_totals_stars+"票哟，厉害吧~");
				  weixinContent.setTitle("活动结束，热度不减~来看看"+petPicture.animal.pet_nickName+"的参赛照片的参赛照片");
			  }else{
				  weixinContent.setShareContent("大萌星"+petPicture.animal.pet_nickName+"在"+petPicture.star_title+"活动里已经得到"+petPicture.animal_totals_stars+"票喽，你们也来支持个~");
				  weixinContent.setTitle("我是"+petPicture.animal.pet_nickName+"，快来给我投票啦~");
			  }
			  
		  }else{
			//设置分享文字
			  if(StringUtil.isEmpty(petPicture.cmt)){
				  weixinContent.setShareContent((StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"没有照片描述：这是我最新的美照哦~~打滚儿求表扬~~");
			  }else{
				  weixinContent.setShareContent(petPicture.cmt);
			  }
				
				 //设置title
				 weixinContent.setTitle("我是"+petPicture.animal.pet_nickName+"，你有没有爱上我？");
		  }
		//设置分享内容跳转URL
			 weixinContent.setTargetUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=wechat"*/);
		   

		 //设置分享图片
		 UMImage umImage=new UMImage(getActivity(),petPicture.petPicture_path );
		 weixinContent.setShareImage(umImage);
//		 weixinContent.setShareContent(petPicture.animal.pet_nickName);
		 mController.setShareMedia(weixinContent);
//		 mController.openShare(this, true);
		 mController.postShare(getActivity(),SHARE_MEDIA.WEIXIN, 
			        new SnsPostListener() {
			                @Override
			                public void onStart() {
//			                    Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
			                }
			                @Override
			                public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
			                     if (eCode == 200) {
			                    	 shareNumChange();
			                    	/* if("1".equals(petPicture.is_food)){
			         					MobclickAgent.onEvent(NewShowTopicActivity.this, "food_share_suc");
			         				}else if("2".equals(petPicture.is_food)){
			         					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic1_share_suc");
			         				}else if("3".equals(petPicture.is_food)){
			         					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic2_share_suc");
			         				}*/
			                    	 
			                    	 if("2".equals(petPicture.is_food)){
			                    			if(getActivity()!=null)
			                       		MobclickAgent.onEvent(getActivity(), "topic1_share_suc");
			         					
			         				}else if("3".equals(petPicture.is_food)){
			         					if(getActivity()!=null)
			         					MobclickAgent.onEvent(getActivity(), "topic2_share_suc");
			          				}else if(!"1".equals(petPicture.is_food)){
			          					
			          				}else{
			          					if(getActivity()!=null)
			          					MobclickAgent.onEvent(getActivity(), "food_share_suc");
			          				}
			                    		if(getActivity()!=null)
			                         Toast.makeText(getActivity(), "分享成功.", Toast.LENGTH_SHORT).show();
			                     } else {
			                          String eMsg = "";
			                          if (eCode == -101){
			                              eMsg = "没有授权";
			                          }
			                      	if(getActivity()!=null)
			                          Toast.makeText(getActivity(), "分享失败[" + eCode + "] " + 
			                                             eMsg,Toast.LENGTH_SHORT).show();
			                          if(shareDialogFragmentResultListener!=null){
											shareDialogFragmentResultListener.onResult(false);
										}
			                          /*FragmentTransaction ft=getFragmentManager().beginTransaction();
										ft.remove(ShareDialogFragment.this);
										ft.commit();*/
			                     }
			              }
			});
		   
		   
			
			TranslateAnimation tAnimation2=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
			
			tAnimation2.setDuration(500);
		
			shareLayout.clearAnimation();
			shareLayout.setAnimation(tAnimation2);
			tAnimation2.start();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					shareLayout.setVisibility(View.INVISIBLE);
				}
			}, 500);
	   }
	   public void friendShare(){
		   CircleShareContent circleMedia = new CircleShareContent();
		   UMImage umImage=new UMImage(getActivity(), petPicture.petPicture_path);
		   circleMedia.setShareImage(umImage);
		   long time=petPicture.create_time+24*3600-System.currentTimeMillis()/1000;
		   if((!"0".equals(petPicture.is_food))&&time>0){
				 //设置分享文字
			   circleMedia.setShareContent("看在我这么努力卖萌的份上快来宠宠我！免费送我点口粮好不好？");
				 //设置title
			   circleMedia.setTitle(StringUtil.isEmpty(petPicture.cmt)?"轻轻一点，免费赏粮！我的口粮全靠你啦~":petPicture.cmt);
				
		  }else if(mode==3){
			  circleMedia.setShareContent("这是大萌星"+petPicture.animal.pet_nickName+"在"+petPicture.star_title+"活动里的萌照，你们也来支持个~");
//			  circleMedia.setTitle("我是"+petPicture.animal.pet_nickName+"，快来给我投票啦~");
			  circleMedia.setTitle("这是大萌星"+petPicture.animal.pet_nickName+"在"+petPicture.star_title+"活动里的萌照，你们也来支持个~");
		  }else if(mode==4){
			  
			  if(System.currentTimeMillis()/1000>petPicture.end_time){
				  circleMedia.setShareContent("大萌星"+petPicture.animal.pet_nickName+""+petPicture.star_title+"活动里一共得到"+petPicture.animal_totals_stars+"票哟，厉害吧~");
//				  circleMedia.setTitle("我是"+petPicture.animal.pet_nickName+"，快来给我投票啦~");
				  circleMedia.setTitle("大萌星"+petPicture.animal.pet_nickName+""+petPicture.star_title+"活动里一共得到"+petPicture.animal_totals_stars+"票哟，厉害吧~");
			  }else{
				  circleMedia.setShareContent("大萌星"+petPicture.animal.pet_nickName+"在"+petPicture.star_title+"活动里已经得到"+petPicture.animal_totals_stars+"票喽，你们也来支持个~");
//				  circleMedia.setTitle("我是"+petPicture.animal.pet_nickName+"，快来给我投票啦~");
				  circleMedia.setTitle("大萌星"+petPicture.animal.pet_nickName+"在"+petPicture.star_title+"活动里已经得到"+petPicture.animal_totals_stars+"票喽，你们也来支持个~"); 
			  }
			  
			  
		  }else{
			//设置分享文字
			  if(StringUtil.isEmpty(petPicture.cmt)){
				  circleMedia.setShareContent((StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"没有照片描述：这是我最新的美照哦~~打滚儿求表扬~~");
			  }else{
				  circleMedia.setShareContent(petPicture.cmt);
			  }
				
				 //设置title
			  circleMedia.setTitle(StringUtil.isEmpty(petPicture.cmt)?"我是"+petPicture.animal.pet_nickName+"，你有没有爱上我？":petPicture.cmt);
		  }
		   //设置分享内容跳转URL
		   circleMedia.setTargetUrl("http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=wechat"*/);
		   mController.setShareMedia(circleMedia);
		   mController.postShare(getActivity(),SHARE_MEDIA.WEIXIN_CIRCLE,
				   new SnsPostListener() {
	           @Override
	           public void onStart() {
//	               Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
	           }
	           @Override
	           public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
	                if (eCode == 200) {
	               	 shareNumChange();
	               /*	if("1".equals(petPicture.is_food)){
						MobclickAgent.onEvent(NewShowTopicActivity.this, "food_share_suc");
					}else if("2".equals(petPicture.is_food)){
	 					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic1_share_suc");
	 				}else if("3".equals(petPicture.is_food)){
	 					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic2_share_suc");
	 				}*/
	               	if("2".equals(petPicture.is_food)){
	               		if(getActivity()!=null)
	              		MobclickAgent.onEvent(getActivity(), "topic1_share_suc");
						
					}else if("3".equals(petPicture.is_food)){
						if(getActivity()!=null)
						MobclickAgent.onEvent(getActivity(), "topic2_share_suc");
	 				}else if(!"1".equals(petPicture.is_food)){
	 					
	 				}else{
	 					if(getActivity()!=null)
	 					MobclickAgent.onEvent(getActivity(), "food_share_suc");
	 				}
	               	if(getActivity()!=null)
	                 Toast.makeText(getActivity(), "分享成功.", Toast.LENGTH_SHORT).show();
	                } else {
	                     String eMsg = "";
	                     if (eCode == -101){
	                         eMsg = "没有授权";
	                     }
	                 	if(getActivity()!=null)
	                     Toast.makeText(getActivity(), "分享失败[" + eCode + "] " + 
	                                        eMsg,Toast.LENGTH_SHORT).show();
	                     if(shareDialogFragmentResultListener!=null){
								shareDialogFragmentResultListener.onResult(false);
							}
	                     /*FragmentTransaction ft=getFragmentManager().beginTransaction();
							ft.remove(ShareDialogFragment.this);
							ft.commit();*/
	                }
	         }
	});
		   
			
			TranslateAnimation tAnimation3=new TranslateAnimation(0, 0, 0,shareLayout.getHeight() );
			
			tAnimation3.setDuration(500);
		
			shareLayout.clearAnimation();
			shareLayout.setAnimation(tAnimation3);
			tAnimation3.start();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					shareLayout.setVisibility(View.INVISIBLE);
				}
			}, 500);
			
	   }
	   public void xinlangShare(){
		   UserImagesJson.Data data=new UserImagesJson.Data();
		   String bmpPath=petPicture.petPicture_path;
			if(bmpPath!=null){
				data.path=bmpPath;
				if(StringUtil.isEmpty(petPicture.cmt)){
					data.des=(StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"这是我最新的美照哦~~打滚儿求表扬~~"+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id+" #我是大萌星#";
				}else{
					data.des=petPicture.cmt+(StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id+" #我是大萌星#";
				}
			}
		   SinaShareContent content=new SinaShareContent();
		   long time=petPicture.create_time+24*3600-System.currentTimeMillis()/1000;
		   if((!"0".equals(petPicture.is_food))&&time>0){
			   content.setShareContent((StringUtil.isEmpty(petPicture.cmt)?"看在我这么努力卖萌的份上快来宠宠我！免费送我点口粮好不好？":petPicture.cmt)+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=webo"*/+" #我是大萌星#");
		   }else if(mode==3){
			   content.setShareContent("这是大萌星"+petPicture.animal.pet_nickName+"在"+(StringUtil.isEmpty(petPicture.star_title)?"":("#"+petPicture.star_title+"#"))+"活动里的萌照，你们也来支持个~"+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=webo"*/+" #我是大萌星#");
				  
			  }else if(mode==4){
				  if(System.currentTimeMillis()/1000>petPicture.end_time){
					  content.setShareContent("大萌星"+petPicture.animal.pet_nickName+"在"+(StringUtil.isEmpty(petPicture.star_title)?"":("#"+petPicture.star_title+"#"))+"活动里一共得到"+petPicture.animal_totals_stars+"票哟，厉害吧~"+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=webo"*/+" #我是大萌星#");
				  }else{
					  content.setShareContent("大萌星"+petPicture.animal.pet_nickName+"在"+(StringUtil.isEmpty(petPicture.star_title)?"":("#"+petPicture.star_title+"#"))+"活动里已经得到"+petPicture.animal_totals_stars+"票喽，你们也来支持个~"+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=webo"*/+" #我是大萌星#");
				  }
				  
					  
				  }else{
			   content.setShareContent(data.des);
		   }
		   
		   
		   UMImage umImage=new UMImage(getActivity(), data.path);
		  
		   content.setShareImage(umImage);
		   mController.setShareMedia(content);
		   mController.postShare(getActivity(), SHARE_MEDIA.SINA,new SnsPostListener() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
				// TODO Auto-generated method stub
				if (eCode == 200) {
	              	 shareNumChange();
	              	 
	              	/*if("1".equals(petPicture.is_food)){
						MobclickAgent.onEvent(NewShowTopicActivity.this, "food_share_suc");
					}else if("2".equals(petPicture.is_food)){
	 					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic1_share_suc");
	 				}else if("3".equals(petPicture.is_food)){
	 					MobclickAgent.onEvent(NewShowTopicActivity.this, "topic2_share_suc");
	 				}*/
	              	 
	            	if("2".equals(petPicture.is_food)){
	            		if(getActivity()!=null)
	              		MobclickAgent.onEvent(getActivity(), "topic1_share_suc");
						
					}else if("3".equals(petPicture.is_food)){
						if(getActivity()!=null)
						MobclickAgent.onEvent(getActivity(), "topic2_share_suc");
	 				}else if(!"1".equals(petPicture.is_food)){
	 					
	 				}else{
	 					if(getActivity()!=null)
	 					MobclickAgent.onEvent(getActivity(), "food_share_suc");
	 				}
	              	
	            	 LogUtil.i("mi", "新浪分享："+"分享chenggong");
	            		if(getActivity()!=null)
	                Toast.makeText(getActivity(), "分享成功.", Toast.LENGTH_SHORT).show();
	               } else {
	                    String eMsg = "";
	                    if (eCode == -101){
	                        eMsg = "没有授权";
	                    }
	                	if(getActivity()!=null)
	                    Toast.makeText(getActivity(), "分享失败[" + eCode + "] " + 
	                                       eMsg,Toast.LENGTH_SHORT).show();
	                    if(shareDialogFragmentResultListener!=null){
							shareDialogFragmentResultListener.onResult(false);
						}
	                    LogUtil.i("mi", "新浪分享："+"分享失败[" + eCode + "] " + 
	                                       eMsg);
	                   /* FragmentTransaction ft=getFragmentManager().beginTransaction();
						ft.remove(ShareDialogFragment.this);
						ft.commit();*/
	               }
			}
		});
		   
	   }
		public void shareNumChange(){
			petPicture.shares++;
//			shareTv1.setText(""+petPicture.shares);
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					final MyUser user=HttpUtil.imageShareNumsApi(getActivity(),petPicture.img_id, handler);
					if(getActivity()!=null)
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(user!=null&&PetApplication.myUser!=null){
								if(petPicture.shareUsersList!=null&&petPicture.shareUsersList.size()>0&&petPicture.shareUsersList.contains(PetApplication.myUser)){
									if(shareDialogFragmentResultListener!=null){
										shareDialogFragmentResultListener.onResult(false);
									}
								}else{
									
									if(StringUtil.isEmpty(petPicture.share_ids)){
										petPicture.share_ids=""+PetApplication.myUser.userId;
									}else{
										petPicture.share_ids+=","+PetApplication.myUser.userId;
									}
									if(petPicture.shareUsersList==null){
										petPicture.shareUsersList=new ArrayList<MyUser>();
									}
									petPicture.shareUsersList.add(PetApplication.myUser);
									/*if(current_show==SHOW_SHARE_LIST){
										showShareUsersList();
									}*/
									
									if(shareDialogFragmentResultListener!=null){
										shareDialogFragmentResultListener.onResult(true);
									}
								}
								
								}
								
								
								FragmentTransaction ft=getFragmentManager().beginTransaction();
								ft.remove(ShareDialogFragment.this);
								ft.commit();
							}
						});
				
					
					
					/*if(!Constants.isSuccess)return;
					UserStatusUtil.checkUserExpGoldLvRankChange(user, ShowTopicActivity.this, progressLayout);*/
				}
			}).start();
		}
		public void setShareDialogFragmentResultListener(ShareDialogFragmentResultListener shareDialogFragmentResultListener){
			this.shareDialogFragmentResultListener=shareDialogFragmentResultListener;
		}
		public static interface ShareDialogFragmentResultListener{
			void onResult(boolean isSuccess);
		}
}
