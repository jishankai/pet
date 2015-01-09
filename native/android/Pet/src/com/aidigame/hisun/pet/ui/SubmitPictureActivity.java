package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
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
import com.aidigame.hisun.pet.http.json.UserImagesJson.Data;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.ShowProgress;
import com.aidigame.hisun.pet.widget.WeixinShare;
import com.aviary.android.feather.FeatherActivity;
import com.umeng.analytics.MobclickAgent;
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
	TextView textView,topicTV,atTV,sendToTv,titleTv;
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
	boolean isBeg;
	UMSocialService mController;
	HandleHttpConnectionException handleHttpConnectionException;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPLOAD_IMAGE_SUCCESS:
				handler.sendEmptyMessage(DISMISS_PROGRESS);
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
		
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		
		animal=(Animal)getIntent().getSerializableExtra("animal");
		isBeg=getIntent().getBooleanExtra("isBeg", false);
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
		titleTv=(TextView)findViewById(R.id.textView1);
		if(isBeg){
			titleTv.setText("挣口粮");
			topicTV.setText("挣口粮");
		}
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
		sendToTv.setOnClickListener(this);
		closeBt.setOnClickListener(this);
		if(!isBeg)
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
							
//							toast=Toast.makeText(SubmitPictureActivity.this, "您已输入"+str.length()+"个字，还可在输入"+(max-str.length())+"个字", 200);
							
//							toast.show();
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
			
			
			this.finish();
			
			break;
		case R.id.button2:
			
			break;
        case R.id.button3:
        	if(uri!=null){
				Intent intent=new Intent(this,FeatherActivity.class);
				intent.setData(uri);
				intent.putExtra("mode", getIntent().getIntExtra("mode", 0));
				intent.putExtra("from", "SubmitPicture");
				intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, "f6d0dd319088fd5a");
				this.startActivityForResult(intent, 2);
			}
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
					Bitmap bitmap=null;
					if(new File(finalPath).length()>1024*1024*1){
						BitmapFactory.Options options=new BitmapFactory.Options();
						options.inSampleSize=2;
						bitmap=BitmapFactory.decodeFile(finalPath,options);
					}else if(new File(finalPath).length()>1024*512*1){
						BitmapFactory.Options options=new BitmapFactory.Options();
						options.inSampleSize=2;
						bitmap=BitmapFactory.decodeFile(finalPath,options);
					}
					
				  
					
					FileOutputStream fos=null;
					FileOutputStream fos1=null;
					try {
						String path2=null;
						boolean canCompress=true;
						if(bitmap==null){
							canCompress=false;
							BitmapFactory.Options options=new BitmapFactory.Options();
							if(new File(finalPath).length()>1024*1024*1){
								options.inSampleSize=2;
							}else{
								options.inSampleSize=2;
							}
							
							bitmap=BitmapFactory.decodeFile(finalPath,options);
						}
						if(bitmap!=null){
							if(canCompress){
								String path=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+"_"+bitmap.getWidth()+"&"+bitmap.getHeight()+".jpg";
								fos = new FileOutputStream(new File(path));
								
//									if(new File(finalPath).length()>1024*1024*1){
										bitmap.compress(CompressFormat.JPEG, 90, fos);
//									}else{
//										bitmap.compress(CompressFormat.JPEG, 90, fos);
//									}
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								File file=new File(path);
								path2=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+"@"+file.length()+"@_"+bitmap.getWidth()+"&"+bitmap.getHeight()+".jpg";
								file.renameTo(new File(path2));
							}else{
								File file=new File(finalPath);
								path2=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+"@"+file.length()+"@_"+bitmap.getWidth()+"&"+bitmap.getHeight()+".jpg";
								file.renameTo(new File(path2));
							}
						}
						
						
					
					
					
					
					
					
					
					PetPicture petPicture=new PetPicture();
					petPicture.animal=new Animal();
					petPicture.animal.a_id=animal.a_id;
					petPicture.cmt=info;
					if(path2==null){
						path2=finalPath;
					}
					petPicture.petPicture_path=path2;
					petPicture.isBeg=isBeg;
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
					MobclickAgent.onEvent(SubmitPictureActivity.this, "photo");
					if(!StringUtil.isEmpty(petPicture2.url)){
//						HttpUtil.imageInfo(petPicture2, handler, SubmitPictureActivity.this);
						handler.sendEmptyMessage(DISMISS_PROGRESS);
						
						petPicture2.animal=Constants.user.currentAnimal;
						
						SubmitPictureActivity.this.petPicture=petPicture2;
						UserImagesJson.Data  data=new UserImagesJson.Data();
						 data.path=path2;
						 data.comment=info;
						 Constants.whereShare=1;
						 
						 
						 if(sendToXinlang){
							 xinlangShare(data);
						 }else
						 if(sendToWeixin){
							 friendShare(data);
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
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(DISMISS_PROGRESS);
						sendPicture=false;
					}
					
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
		case R.id.sendto_tv6:
			if(isBeg){
				return;
			}
			Intent intent7=new Intent(this,AtUserListActivity.class);
			intent7.putExtra("mode", 2);
			this.startActivity(intent7);
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
	public void setChosePet(Animal animal){
		this.animal=animal;
		sendToTv.setText("发布到"+animal.pet_nickName);
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
		int w=Constants.screen_width-getResources().getDimensionPixelSize(R.dimen.one_dip)*20;
		if(cursor!=null){
			cursor.moveToFirst();
			String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=2;
			options.inJustDecodeBounds=true;
			Bitmap bitmap=BitmapFactory.decodeFile(path,options);
			Matrix matrix=new Matrix();
			submitBt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)imageView.getLayoutParams();
			if(param==null){
				param=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			}
			
			param.height=(int)(w*1f/options.outWidth*1f*options.outHeight);
			imageView.setLayoutParams(param);
			/*submitBt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			float scale=submitBt.getMeasuredWidth()/(bitmap.getWidth()*1f);
			matrix.postScale(scale, scale);
			bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),matrix,true);*/
			
			options.inJustDecodeBounds=false;
			bitmap=BitmapFactory.decodeFile(path,options);
			imageView.setImageBitmap(bitmap);
			finalPath=path;
			cursor.close();
		}else{
			String path=uri.getPath();
			if(!StringUtil.isEmpty(path)){
				finalPath=path;
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize=2;
				
				options.inJustDecodeBounds=true;
				Bitmap bitmap=BitmapFactory.decodeFile(path,options);
				Matrix matrix=new Matrix();
				submitBt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)imageView.getLayoutParams();
				if(param==null){
					param=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				}
				param.height=(int)(w*1f/options.outWidth*1f*options.outHeight);
				imageView.setLayoutParams(param);
				/*Matrix matrix=new Matrix();
				
				submitBt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				float scale=submitBt.getMeasuredWidth()/(bitmap.getWidth()*1f);
				matrix.postScale(scale, scale);
				bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),matrix,true);*/
				if(new File(finalPath).length()>1024*1024){
					options.inSampleSize=2;
				}
				options.inJustDecodeBounds=false;
				bitmap=BitmapFactory.decodeFile(path,options);
				imageView.setImageBitmap(bitmap);
				finalPath=path;
			}
		}
	}
	public void addShares(boolean over1){
		final boolean over=over1;
		petPicture.shares++;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
             final User user=HttpUtil.imageShareNumsApi(SubmitPictureActivity.this,petPicture.img_id, handleHttpConnectionException.getHandler(SubmitPictureActivity.this));
            
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
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				Intent intent=new Intent(SubmitPictureActivity.this,NewShowTopicActivity.class);
				intent.putExtra("PetPicture", petPicture);
				SubmitPictureActivity.this.startActivity(intent);
				SubmitPictureActivity.this.finish();
				LogUtil.i("me", "SubmitPicture关闭掉===================5");
			/*}
		});*/
		
		
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
	      private void xinlangShare(final Data data) {
				// TODO Auto-generated method stub

		   	   SinaShareContent sinaShareContent = new SinaShareContent();
		   	   UMImage umImage=new UMImage(this, data.path);
		   	sinaShareContent.setShareImage(umImage);
//		   	   circleMedia.setTargetUrl("");
		   	sinaShareContent.setShareContent(data.des);
		   	   
		   	   mController.setShareMedia(sinaShareContent);
		   	   mController.postShare(this,SHARE_MEDIA.SINA,
		   			   new SnsPostListener() {
		              @Override
		              public void onStart() {
//		                  Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
		              }
		              @Override
		              public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
		                   if (eCode == 200) {
		                  	addShares(false);
		                    Toast.makeText(SubmitPictureActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
		                   } else {
		                	   over();
		                        String eMsg = "";
		                        if (eCode == -101){
		                            eMsg = "没有授权";
		                        }
		                        Toast.makeText(SubmitPictureActivity.this, "分享失败[" + eCode + "] " + 
		                                           eMsg,Toast.LENGTH_SHORT).show();
		                   }
		                   if(sendToWeixin){
		                	   friendShare(data);
		                   }else{
		                	   over();
		                   }
		                   
		                   
		            }
		   });
		      
			}
	      public void friendShare(UserImagesJson.Data data){
	   	   CircleShareContent circleMedia = new CircleShareContent();
	   	   UMImage umImage=new UMImage(this, data.path);
	   	   circleMedia.setShareImage(umImage);
//	   	   circleMedia.setTargetUrl("");
//	   	circleMedia.setShareContent(animal.pet_nickName);
	   	   
	   	   mController.setShareMedia(circleMedia);
	   	   mController.postShare(this,SHARE_MEDIA.WEIXIN_CIRCLE,
	   			   new SnsPostListener() {
	              @Override
	              public void onStart() {
//	                  Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
	              }
	              @Override
	              public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
	                   if (eCode == 200) {
	                  	addShares(true);
	                    Toast.makeText(SubmitPictureActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
	                   } else {
	                	   over();
	                        String eMsg = "";
	                        if (eCode == -101){
	                            eMsg = "没有授权";
	                        }
	                        Toast.makeText(SubmitPictureActivity.this, "分享失败[" + eCode + "] " + 
	                                           eMsg,Toast.LENGTH_SHORT).show();
	                   }
	                   
	            }
	   });
	      }

}
