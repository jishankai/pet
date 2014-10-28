package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aidigame.hisun.pet.widget.XinlangShare;
import com.aidigame.hisun.pet.widget.fragment.DialogExpGoldConAdd;
import com.aviary.android.feather.FeatherActivity;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
/**
 * 发布图片界面
 * @author admin
 *
 */
public class SubmitPictureActivity extends Activity implements OnClickListener{
	Button submitBt,closeBt;
	boolean sendPicture=false;
	ImageView imageView,backBt,weixin,xinlang,box1,box2;
	boolean sendToXinlang=false,sendToWeixin=false;
	EditText editText;
	TextView textView,topicTV,atTV,sendToTv;
	LinearLayout progressLayout;
	ShowProgress showProgress;
	LinearLayout weixinLayout,xinlangLayout;
	public static final int DISMISS_PROGRESS=202;
	public static final int SHOW_PROGRESS=203;
	public static SubmitPictureActivity submitPictureActivity;
	Uri uri;
	String path,finalPath;//path为原始图片，未进行照片编辑
	public static final int UPLOAD_IMAGE_SUCCESS=0;
	public static final int UPLOAD_IMAGE_FAILS=1;
	String info="";
	String lastInfo="";
	String activityName;
	PetPicture petPicture;
	public String topic_name;//话题名称
	public int topic_id=-1;//官方 话题id，若为-1，则没有过选中话题id
	public String relatesId;//关联的id;
	public String relateString;
	Animal animal;
	HandleHttpConnectionException handleHttpConnectionException;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPLOAD_IMAGE_SUCCESS:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
				UserImagesJson.Data data=(UserImagesJson.Data)msg.obj;
				data.path=path;
				
				Intent intent3=new Intent(SubmitPictureActivity.this,ShowTopicActivity.class);
				intent3.putExtra("data",data);
				ShowTopicActivity.petPictures=null;
				SubmitPictureActivity.this.startActivity(intent3);
				SubmitPictureActivity.this.finish();
				break;
			case DISMISS_PROGRESS:
				if(showProgress!=null)
				showProgress.progressCancel();
				break;
			case SHOW_PROGRESS:
				if(showProgress==null){
					showProgress=new ShowProgress(SubmitPictureActivity.this, progressLayout);
				}else{
					showProgress.showProgress();
				}
				
				break;
			case UPLOAD_IMAGE_FAILS:
				sendPicture=false;
				Toast.makeText(SubmitPictureActivity.this, "上传图片失败",  Toast.LENGTH_LONG).show();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_submit_picture);
		topic_id=getIntent().getIntExtra("topic_id", -1);
		if(topic_id!=-1){
			topic_name=getIntent().getStringExtra("topic_name");
		}
		animal=(Animal)getIntent().getSerializableExtra("animal");
		initView();
		this.submitPictureActivity=this;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		backBt=(ImageView)findViewById(R.id.button1);
		submitBt=(Button)findViewById(R.id.button4);
		closeBt=(Button)findViewById(R.id.button3);
		imageView=(ImageView)findViewById(R.id.imageView1);
		editText=(EditText)findViewById(R.id.editText1);
		textView=(TextView)findViewById(R.id.textView2);
		topicTV=(TextView)findViewById(R.id.textView5);
		if(topic_id!=-1){
			topicTV.setText("#"+topic_name+"#");
		}
		sendToTv=(TextView)findViewById(R.id.sendto_tv6);
		sendToTv.setText("发布到"+animal.pet_nickName);
		atTV=(TextView)findViewById(R.id.textView6);
		box1=(ImageView)findViewById(R.id.check_box_1);
		box2=(ImageView)findViewById(R.id.check_box_2);
		progressLayout=(LinearLayout)findViewById(R.id.progress_linearlayout1);
		uri=getIntent().getData();
		activityName=getIntent().getStringExtra("activity");
		path=getIntent().getStringExtra("path");
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				uri=getIntent().getData();
				loadBitmap(uri);
			}
		});
	}
	boolean isShow=false;
	private void initListener() {
		// TODO Auto-generated method stub
		backBt.setOnClickListener(this);
		submitBt.setOnClickListener(this);
		imageView.setOnClickListener(this);
		closeBt.setOnClickListener(this);
		topicTV.setOnClickListener(this);
		atTV.setOnClickListener(this);
		editText.addTextChangedListener(new TextWatcher() {
			String temp;
			Toast toast;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				if(toast!=null){
					toast.cancel();
					toast=null;
				}
				temp=""+s;
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int start=editText.getSelectionStart();
				int stop=editText.getSelectionEnd();
				String str=editText.getText().toString();
				int max=40;
				if(str.getBytes().length>str.length()){
					max=40;
				}else{
					max=40;
				}
				if(str!=null){
					/*info+=str;
					lastInfo=str;
					editText.;*/
//					textView.setText(str);
					if(str.length()<=max){
						if(temp.length()<str.length()){
							if(str.length()==max){
								info=str;
							}
							
							toast=Toast.makeText(SubmitPictureActivity.this, "您已输入"+str.length()+"个字，还可在输入"+(max-str.length())+"个字", 200);
							
							toast.show();
						}
						
					}else{
						if(start>0){
							s.delete(start-1, stop);
							
							if(start>max){
								
							}else{
								editText.setSelection(start);
							}
							
							toast=Toast.makeText(SubmitPictureActivity.this, "您已经输入"+max+"个字...", 200);
							toast.show();
//							editText.setText(temp);
						}
						
					}
				}
					
			}
		});
		weixinLayout=(LinearLayout)findViewById(R.id.weixin_linearlayout);
		xinlangLayout=(LinearLayout)findViewById(R.id.xinlang_linearlayout);
		weixin=(ImageView)findViewById(R.id.weixin1);
		xinlang=(ImageView)findViewById(R.id.xinlang);
		SharedPreferences sp=getSharedPreferences("setup",Context.MODE_WORLD_WRITEABLE);
		sendToWeixin=sp.getBoolean(Constants.PICTURE_SEND_TO_WEIXIN, false);
		sendToXinlang=sp.getBoolean(Constants.PICTURE_SEND_TO_XINLANG, false);
		if(sendToWeixin){
			weixin.setImageResource(R.drawable.friend_weixin);
			box2.setImageResource(R.drawable.box_chose_red);
		}
		if(sendToXinlang){
			xinlang.setImageResource(R.drawable.xinlang);
			box1.setImageResource(R.drawable.box_chose_red);
		}
		weixinLayout.setClickable(true);
		xinlangLayout.setClickable(true);
		weixinLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sendToWeixin){
					sendToWeixin=false;
					weixin.setImageResource(R.drawable.weixin_gray);
					box2.setImageResource(R.drawable.box_chose_gray);
				
				}else{
					sendToWeixin=true;
					weixin.setImageResource(R.drawable.friend_weixin);
					box2.setImageResource(R.drawable.box_chose_red);
					/*if(Constants.api==null){
						boolean flag=WeixinShare.regToWeiXin(SubmitPictureActivity.this);
						if(!flag){
							sendToWeixin=false;
							Toast.makeText(SubmitPictureActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
							
							
						}else{
							sendToWeixin=true;
							weixin.setImageResource(R.drawable.friend_weixin);
							
						}
					}else{
						sendToWeixin=true;
						weixin.setImageResource(R.drawable.friend_weixin);
					}*/
					
					
				}
				
				
				
				
				
			}
		});
		xinlangLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sendToXinlang){
					sendToXinlang=false;
					xinlang.setImageResource(R.drawable.xinlang_gray);
					box1.setImageResource(R.drawable.box_chose_gray);
				}else{
					sendToXinlang=true;
					xinlang.setImageResource(R.drawable.xinlang);
					box1.setImageResource(R.drawable.box_chose_red);
					/*if(UserStatusUtil.hasXinlangAuth(SubmitPictureActivity.this)){
						sendToXinlang=true;
						xinlang.setImageResource(R.drawable.xinlang);
					}else{
						sendToXinlang=false;
					} */
					
					
				}
				
				
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			
			if(uri!=null){
				Intent intent=new Intent(this,FeatherActivity.class);
				intent.setData(uri);
				intent.putExtra("mode", getIntent().getIntExtra("mode", 0));
				intent.putExtra("from", "SubmitPicture");
				intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, "f6d0dd319088fd5a");
				this.startActivityForResult(intent, 2);
			}
			
			
			break;
		case R.id.button2:
			
			break;
        case R.id.button3:
			this.finish();
			break;
		case R.id.button4:
			//FIXME
			if(sendPicture){
				Toast.makeText(this, "正在发送图片，请稍等", Toast.LENGTH_LONG).show();;
				return ;
			}
			
			if(finalPath==null)return;
			if(!info.contains(lastInfo)){
				info=""+lastInfo;
			}
			 info=editText.getText().toString();
			 if("为您爱宠的靓照写个描述吧~".equals(info)){
				 info="";
			 }
			 if(activityName!=null){
				 info="#"+activityName+"#"+info;
			 }
			 if(StringUtil.isEmpty(info))info="";
			 
			 
				
			 
			 
			 sendPicture=true;
			 handler.sendEmptyMessage(SHOW_PROGRESS);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					info = info.replaceAll("\\s+", " ");
					info.trim();
					PetPicture petPicture=new PetPicture();
					petPicture.animal=new Animal();
					petPicture.animal.a_id=animal.a_id;
					petPicture.cmt=info;
					petPicture.petPicture_path=finalPath;
					if(topic_id!=-1){
						petPicture.topic_id=topic_id;
						petPicture.topic_name=topic_name;
					}else if(!StringUtil.isEmpty(topic_name)){
						petPicture.topic_name=topic_name;
						petPicture.topic_name.replace('#', ' ');
					}
					petPicture.relates=relatesId;
					petPicture.relatesString=relateString;
					PetPicture petPicture2=HttpUtil.uploadImage(petPicture,handleHttpConnectionException.getHandler(SubmitPictureActivity.this),SubmitPictureActivity.this);
					petPicture2.topic_id=petPicture.topic_id;
					petPicture2.topic_name="#"+petPicture.topic_name+"#";
					petPicture2.relates=relatesId;
					petPicture2.relatesString=relateString;
					if(!StringUtil.isEmpty(petPicture2.url)){
//						HttpUtil.imageInfo(petPicture2, handler, SubmitPictureActivity.this);
						handler.sendEmptyMessage(DISMISS_PROGRESS);
						
						petPicture2.animal=Constants.user.currentAnimal;
						
						SubmitPictureActivity.this.petPicture=petPicture2;
						UserImagesJson.Data  data=new UserImagesJson.Data();
						 data.path=finalPath;
						 data.comment=info;
						 Constants.whereShare=1;
						 
						 
						 if(sendToXinlang){
							 boolean hasAuth=false;
							 
							 
							 if(Constants.accessToken==null){
									SharedPreferences sPreferences=getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
									String token=sPreferences.getString("xinlangToken", null);
									if(token!=null){
										Oauth2AccessToken accessToken=Oauth2AccessToken.parseAccessToken(token);
										if(accessToken!=null){
											if(accessToken.isSessionValid()){
												Constants.accessToken=accessToken;
												hasAuth=true;
											}
										}
									 }
							  }else{
								  if(Constants.accessToken.isSessionValid()){
										hasAuth=true;
									}
							  }
							 if(hasAuth){
								 Constants.shareMode=2;
								 if(StringUtil.isEmpty(petPicture.cmt)){
									 data.des="分享照片http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
								 }else{
									 data.des=petPicture.cmt+"http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
								 }
								 
								 XinlangShare.sharePicture(data,SubmitPictureActivity.this);
								 addShares(false);
							 }else{
								 if(StringUtil.isEmpty(petPicture.cmt)){
									 data.des="分享照片http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
								 }else{
									 data.des=petPicture.cmt+"http://home4pet.aidigame.com/（分享自@宠物星球社交应用）";
								 }
								 xinlangAuth(SubmitPictureActivity.this,data,petPicture2);
								
								 return;
							 }
						 }
						 if(sendToWeixin){
							 if(Constants.api==null){
									boolean flag=WeixinShare.regToWeiXin(SubmitPictureActivity.this);
									if(!flag){
										Toast.makeText(SubmitPictureActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
										over();
										return;
									}
								}
							 Constants.shareMode=1;
							 if(WeixinShare.shareBitmap(data, 2)){
//									Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
								   
								}else{
									 over();
									Toast.makeText(SubmitPictureActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
								}		
						 }else{
							 over();
							 return;
						 }
						 if(!sendToWeixin&&!sendToXinlang){
							 over();
						 }
						
					}else{
						handler.sendEmptyMessage(DISMISS_PROGRESS);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(SubmitPictureActivity.this, "上传图片失败", Toast.LENGTH_LONG).show();
							}
						});
					}
					
					sendPicture=false;
					
					
				}
			}).start();
			
//			this.finish();
			
			break;
		case R.id.imageView1:
			
			break;
		case R.id.textView5:
			Intent intent5=new Intent(this,TopicListActivity.class);
			this.startActivity(intent5);
			break;
		case R.id.textView6:
			Intent intent6=new Intent(this,AtUserListActivity.class);
			this.startActivity(intent6);
			break;
		}
	}
	public void setTopic(Topic topic){
		topic_name=topic.topic;
		topicTV.setText("#"+topic.topic+"#");
		
		topic_id=topic.topic_id;
		
	}
	public void setAtUser(String str,String relatesId){
		atTV.setText(str);
		this.relatesId=relatesId;
		this.relateString=str;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if(data.getBooleanExtra("cancel", false)){
				this.finish();
				return;
			}
			if(uri!=null){
				LogUtil.i("me", "删除uri="+uri);
				try{
					getContentResolver().delete(uri, null, null);
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			uri=data.getData();
			loadBitmap(uri);
			break;
		case RESULT_CANCELED:
			
			break;
		}
	}
	public void loadBitmap(Uri uri){
		LogUtil.i("me", "uri="+uri.toString()+",path="+path);
		if(uri==null)return;
		Cursor cursor=getContentResolver().query(uri, null, null, null, null);
		if(cursor!=null){
			cursor.moveToFirst();
			String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
			Bitmap bitmap=BitmapFactory.decodeFile(path);
			Matrix matrix=new Matrix();
			
			submitBt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			float scale=submitBt.getMeasuredWidth()/(bitmap.getWidth()*1f);
			matrix.postScale(scale, scale);
//			bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),matrix,true);
			imageView.setImageBitmap(bitmap);
			finalPath=path;
			cursor.close();
		}else{
			String path=uri.getPath();
			if(!StringUtil.isEmpty(path)){
				Bitmap bitmap=BitmapFactory.decodeFile(path);
				Matrix matrix=new Matrix();
				
				submitBt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				float scale=submitBt.getMeasuredWidth()/(bitmap.getWidth()*1f);
				matrix.postScale(scale, scale);
//				bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),matrix,true);
				imageView.setImageBitmap(bitmap);
				finalPath=path;
			}
		}
	}
	public void xinlangAuth(final Activity context,final UserImagesJson.Data data,final PetPicture petPicture2){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				WeiboAuth weiboAuth=new WeiboAuth(context, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
				weiboAuth.authorize(new WeiboAuthListener() {
					
					@Override
					public void onWeiboException(WeiboException arg0) {
						// TODO Auto-generated method stub
						
//							Toast.makeText(FirstPageActivity.this, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();;
							LogUtil.i("exception", arg0.getMessage());
							if(sendToWeixin){
								if(Constants.api==null){
									boolean flag=WeixinShare.regToWeiXin(SubmitPictureActivity.this);
									if(!flag){
										Toast.makeText(SubmitPictureActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
										over();
										return;
									}
								}
								Constants.shareMode=1;
								 if(WeixinShare.shareBitmap(data, 2)){
//										Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
									}else{
										Toast.makeText(SubmitPictureActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
									}		
							 }else{
								 over();
							 }
							return;
					}
					
					@Override
					public void onComplete(Bundle arg0) {
						// TODO Auto-generated method stub
						LogUtil.i("me", "授权code完毕");
						if(arg0==null){
							Toast.makeText(context, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();;
							return;
						};
						String code=arg0.getString("code");
						if(code==null){
							Toast.makeText(context, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();;
							return;
						};;
						WeiboParameters parameters=new WeiboParameters();
						parameters.put(WBConstants.AUTH_PARAMS_CLIENT_ID,Constants.APP_KEY);
						parameters.put(WBConstants.AUTH_PARAMS_CLIENT_SECRET, Constants.APP_SECRET);
						parameters.put(WBConstants.AUTH_PARAMS_GRANT_TYPE,    "authorization_code");
						parameters.put(WBConstants.AUTH_PARAMS_CODE,          code);
						parameters.put(WBConstants.AUTH_PARAMS_REDIRECT_URL,  Constants.REDIRECT_URL);
						//异步请求获取token
						AsyncWeiboRunner.requestAsync(Constants.OAUTH2_ACCESS_TOKEN_URL, parameters, "POST", new RequestListener() {
							
							@Override
							public void onWeiboException(WeiboException arg0) {
								// TODO Auto-generated method stub
								Toast.makeText(context, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();;
								if(sendToWeixin){
									if(Constants.api==null){
										boolean flag=WeixinShare.regToWeiXin(SubmitPictureActivity.this);
										if(!flag){
											over();
											return;
										}
									}
									Constants.shareMode=1;
									 if(WeixinShare.shareBitmap(data, 2)){
//											Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
										}else{
											Toast.makeText(SubmitPictureActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
										}		
								 }else{
									 over();
								 }
							}
							
							@Override
							public void onComplete(String arg0) {
								// TODO Auto-generated method stub
								if(arg0!=null){
									/*
									 * {"access_token":"2.00iDvQeF2RJa2Bd245f03f0fgRNORB","remind_in":"157679999",
									 * "expires_in":157679999,"uid":"5175750186",
									 * "scope":"follow_app_official_microblog"}
									 */
									Oauth2AccessToken accessToken=new Oauth2AccessToken(arg0);
									LogUtil.i("me", ""+arg0);
									if(accessToken!=null&&accessToken.isSessionValid()){
										SharedPreferences sp=context.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
										Editor editor=sp.edit();
										editor.putString("xinlangToken", arg0);
										editor.commit();
										Constants.accessToken=accessToken;
										LogUtil.i("me", ""+arg0);
										XinlangShare.getXinLangInfo(context);
										Toast.makeText(context, "获取新浪微博授权成功", Toast.LENGTH_LONG).show();
										Constants.shareMode=2;
										XinlangShare.sharePicture(data,SubmitPictureActivity.this);
										 
									}else{
										Toast.makeText(context, "获取新浪微博授权  失败", Toast.LENGTH_LONG).show();;
									}
								}else{
									Toast.makeText(context, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();;
								}
								
								if(sendToWeixin){
									if(Constants.api==null){
										boolean flag=WeixinShare.regToWeiXin(SubmitPictureActivity.this);
										if(!flag){
											Toast.makeText(SubmitPictureActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
											over();
											return;
										}
									}
									Constants.shareMode=1;
									 if(WeixinShare.shareBitmap(data, 2)){
//											Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
										}else{
											Toast.makeText(SubmitPictureActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
										}		
								 }else{
									 over();
								 }
								
							}
							
						});
					}
					
					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						Toast.makeText(context, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();
						
						if(sendToWeixin){
							if(Constants.api==null){
								boolean flag=WeixinShare.regToWeiXin(SubmitPictureActivity.this);
								if(!flag){
									Toast.makeText(SubmitPictureActivity.this,"目前您的微信版本过低或未安装微信，安装微信才能使用。", Toast.LENGTH_LONG).show();
									over();
									return;
								}
							}
							Constants.shareMode=1;
							 if(WeixinShare.shareBitmap(data, 2)){
//									Toast.makeText(this,"成功分享到微信。", Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(SubmitPictureActivity.this,"分享到微信失败。", Toast.LENGTH_LONG).show();
								}		
						 }else{
							 over();
						 }
						
						return;
					}
				}, WeiboAuth.OBTAIN_AUTH_CODE);
			}
		});
		
	}
	public void addShares(boolean over1){
		final boolean over=over1;
		petPicture.shares++;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
             final User user=HttpUtil.imageShareNumsApi(SubmitPictureActivity.this,petPicture.img_id, handleHttpConnectionException.getHandler(SubmitPictureActivity.this));
             UserStatusUtil.checkUserExpGoldLvRankChange(user, SubmitPictureActivity.this, progressLayout);
						if(over){
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										Thread.sleep(1800);
										over();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}).start();
						}
						
			}
		}).start();
	}
	public void over(){
		/*runOnUiThread(new Runnable() {
			
			@Override
			public void run() {*/
				// TODO Auto-generated method stub
				Constants.shareMode=-1;
				Constants.whereShare=-1;
				Intent intent=new Intent(SubmitPictureActivity.this,ShowTopicActivity.class);
				intent.putExtra("PetPicture", petPicture);
				SubmitPictureActivity.this.startActivity(intent);
				SubmitPictureActivity.this.finish();
				LogUtil.i("me", "SubmitPicture关闭掉===================5");
			/*}
		});*/
		
		
	}

}
