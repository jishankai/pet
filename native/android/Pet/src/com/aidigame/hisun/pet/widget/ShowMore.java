package com.aidigame.hisun.pet.widget;

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

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.ChatActivity;
import com.aidigame.hisun.pet.ui.ModifyPetInfoActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.TakePictureBackground;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.fragment.DialogGoRegister;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom;
import com.aidigame.hisun.pet.widget.fragment.DialogJoinKingdom.ResultListener;
import com.aidigame.hisun.pet.widget.fragment.DialogQuitKingdom;
import com.aidigame.hisun.pet.widget.fragment.MenuFragment;

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
	User user;
	HandleHttpConnectionException handleHttpConnectionException;
	public String sharePath;
	public ShowMore(LinearLayout parent,Activity activity,String sharePath,View rootParent){
		this.parent=parent;
		this.activity=activity;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		this.sharePath=sharePath;
		this.rootParent=rootParent;
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
		if(Constants.user==null){
			twoLayout.setVisibility(View.GONE);
			oneLayout.setVisibility(View.GONE);
			twoLayout2.setVisibility(View.GONE);
		}else
		if(animal.master_id==Constants.user.userId){
//			pictureOrMailTv.setText("拍照");
			twoLayout.setVisibility(View.GONE);
			oneLayout.setVisibility(View.GONE);
			twoLayout2.setVisibility(View.VISIBLE);
		}else{
			/*
			 * 其他人的王国
			 */
			oneLayout.setVisibility(View.GONE);
			twoLayout2.setVisibility(View.GONE);
			twoLayout.setVisibility(View.VISIBLE);
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
		}
	}
	public void userDossier(final User user){
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
		if(Constants.user==null){
			oneLayout.setVisibility(View.GONE);
			twoLayout.setVisibility(View.GONE);
			twoLayout2.setVisibility(View.GONE);
		}else
		if(Constants.user.userId==user.userId){
			oneLayout.setVisibility(View.VISIBLE);
			twoLayout.setVisibility(View.GONE);
			twoLayout2.setVisibility(View.GONE);
			pictureOrMailTv.setText("修改资料");
			pictureOrMailTv.setBackgroundResource(R.drawable.button_gray_light2);;
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
							if(activity instanceof PetKingdomActivity){
								final PetKingdomActivity p=(PetKingdomActivity)activity;
								DialogGoRegister dialog=new DialogGoRegister(p.popupParent, p, p.black_layout, 2);
								dialog.setAnimal(animal);
								dialog.setListener(new DialogGoRegister.ResultListener() {
									
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
								final PetKingdomActivity p=(PetKingdomActivity)activity;
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
		if(activity instanceof PetKingdomActivity){
			final PetKingdomActivity p=(PetKingdomActivity)activity;
			if(!animal.is_join){
				/*
				 * 退出王国
				 */
			if(Constants.user!=null&&Constants.user.aniList!=null&&Constants.user.aniList.size()>10&&PetKingdomActivity.petKingdomActivity!=null){
					DialogGoRegister dialog=new DialogGoRegister(PetKingdomActivity.petKingdomActivity.popupParent, PetKingdomActivity.petKingdomActivity, PetKingdomActivity.petKingdomActivity.black_layout, 1);
					return;
			}
			
			DialogJoinKingdom dialog=new DialogJoinKingdom(p.popupParent, p, p.black_layout, animal);
			dialog.setResultListener(new ResultListener() {
				
				@Override
				public void getResult(boolean isSuccess) {
					// TODO Auto-generated method stub
					
					if(isSuccess){
						
						p.setPeoplesNum(animal.fans,isSuccess);
					}
				}
			});
			}else{
				if(Constants.user!=null&&Constants.user.currentAnimal!=null&&animal.a_id==Constants.user.currentAnimal.a_id){
					Toast.makeText(activity, "不能退出最爱萌星，请先取消最爱", Toast.LENGTH_LONG).show();
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
				if(Constants.api==null){
					boolean flag=WeixinShare.regToWeiXin(activity);
					if(!flag){
						Toast.makeText(activity,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						
						return;
					}
				}
				UserImagesJson.Data data=new UserImagesJson.Data();
				data.path=sharePath;
					if(Constants.api==null){
						boolean flag=WeixinShare.regToWeiXin(activity);
						if(!flag){
							Toast.makeText(activity,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
							
							return;
						}
					}
					Constants.shareMode=0;
					if(animal!=null){
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
					}
				
				
			}
		});
		friendIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.setVisibility(View.INVISIBLE);
				rootParent.setBackgroundDrawable(null);
				rootCanTouch=false;
				if(Constants.api==null){
					boolean flag=WeixinShare.regToWeiXin(activity);
					if(!flag){
						Toast.makeText(activity,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
						return;
					}
				}
				
					UserImagesJson.Data data=new UserImagesJson.Data();
					data.path=sharePath;
					Constants.shareMode=1;
					if(animal!=null){
						Constants.whereShare=2;
						
						data.des="雷达报告发现一只萌宠，火速围观！";
					}else if(user!=null){
						Constants.whereShare=3;
						data.des="我发现了一枚萌萌哒的新伙伴浅唱，可以一起愉快的玩耍啦！";
					}
					if(WeixinShare.shareBitmap(data, 2)){
						parent.setVisibility(View.INVISIBLE);
//						Toast.makeText(activity,"成功分享到微信。", Toast.LENGTH_LONG).show();
					}else{
						parent.setVisibility(View.INVISIBLE);
						Toast.makeText(activity,"分享到微信失败。", Toast.LENGTH_LONG).show();
					}
				
			}
		});
		xinlangIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.setVisibility(View.INVISIBLE);
				rootParent.setBackgroundDrawable(null);
				rootCanTouch=false;
				if(!UserStatusUtil.hasXinlangAuth(activity)){
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
							data.des="我发现了一枚萌萌哒的新伙伴“"+user.u_nick+"”，可以一起愉快的玩耍啦！http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
						}
						XinlangShare.sharePicture(data,activity);
					}
					parent.setVisibility(View.INVISIBLE);
				
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
