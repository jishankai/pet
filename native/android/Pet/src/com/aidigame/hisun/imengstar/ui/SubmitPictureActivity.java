package com.aidigame.hisun.imengstar.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView.BufferType;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.ClassicPictureAdapter;
import com.aidigame.hisun.imengstar.adapter.ClassicPictureAdapter.Holder;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.bean.Topic;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson.Data;
import com.aidigame.hisun.imengstar.huanxin.ExpandGridView;
import com.aidigame.hisun.imengstar.huanxin.ExpressionAdapter;
import com.aidigame.hisun.imengstar.huanxin.ExpressionPagerAdapter;
import com.aidigame.hisun.imengstar.huanxin.NewSmileUtils;
import com.aidigame.hisun.imengstar.huanxin.PasteEditText;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.widget.ShowProgress;
import com.aidigame.hisun.imengstar.widget.WeixinShare;
import com.aidigame.hisun.imengstar.R;
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
public class SubmitPictureActivity extends BaseActivity implements OnClickListener{
	private Button submitBt,closeBt;
	private boolean sendPicture=false;
	private ImageView imageView,backBt,weixin,xinlang,box1,box2;
	private boolean sendToXinlang=false,sendToWeixin=false;
	private TextView editText;
	private TextView textView,topicTV,atTV,sendToTv,titleTv;
	LinearLayout progressLayout;
	private ShowProgress showProgress;
	private LinearLayout weixinLayout,xinlangLayout;
	public static final int DISMISS_PROGRESS=202;
	public static final int SHOW_PROGRESS=203;
	public static SubmitPictureActivity submitPictureActivity;
	private Uri uri;
	String path,finalPath;//path为原始图片，未进行照片编辑
	public static final int UPLOAD_IMAGE_SUCCESS=0;
	public static final int UPLOAD_IMAGE_FAILS=1;
	private String info="";
	private String lastInfo="";
	private String activityName;
	private PetPicture petPicture;
	public String topic_name;//话题名称
	public int topic_id=-1;//官方 话题id，若为-1，则没有过选中话题id
	public long star_id=-1;//海选参加的ID
	public String relatesId;//关联的id;
	public String relateString;
	Animal animal;
	private int isBeg;//0 晒照片；1，挣口粮；2 求摸摸；3 玩球球
	private UMSocialService mController;
	private 	HandleHttpConnectionException handleHttpConnectionException;
	private RelativeLayout rootLayout;
	private 	Handler handler=new Handler(){
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
		setContentView(R.layout.activity_submit_picture);
		topic_id=getIntent().getIntExtra("topic_id", -1);
		if(topic_id!=-1){
			topic_name=getIntent().getStringExtra("topic_name");
		}
		if(!StringUtil.isEmpty(getIntent().getStringExtra("topic_name"))){
			topic_name=getIntent().getStringExtra("topic_name");
		}
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this,Constants.Weixin_APP_KEY,Constants.Weixin_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		
		animal=(Animal)getIntent().getSerializableExtra("animal");
		isBeg=getIntent().getIntExtra("isBeg", 0);
		star_id=getIntent().getLongExtra("star_id", -1);
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
		editText=(TextView)findViewById(R.id.editText1);
		textView=(TextView)findViewById(R.id.textView2);
		topicTV=(TextView)findViewById(R.id.textView5);
		titleTv=(TextView)findViewById(R.id.textView1);
		
		editText.setOnClickListener(this);
		initEmotion();
		
		rootLayout=(RelativeLayout)findViewById(R.id.root_layout);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		rootLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.blur,options)));
		
		if(isBeg==1){
			titleTv.setText("挣口粮");
			topicTV.setText("挣口粮");
		}else if(isBeg!=0){
			SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		      
		    	  String topic=sp.getString("p_mid"+isBeg+"_subject", "");
		    	  if(!StringUtil.isEmpty(topic)){
		    		  topic_name=topic;
		    		  topicTV.setText("#"+topic_name+"#");
		    	  }
		    
		}else if(!StringUtil.isEmpty(topic_name)&&star_id!=-1){
			 topicTV.setText("#"+topic_name+"#");
		}
		/*if(topic_id!=-1){
			topicTV.setText("#"+topic_name+"#");
		}*/
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
	private boolean isShow=false;
	private void initListener() {
		// TODO Auto-generated method stub
		backBt.setOnClickListener(this);
		submitBt.setOnClickListener(this);
		imageView.setOnClickListener(this);
		sendToTv.setOnClickListener(this);
		closeBt.setOnClickListener(this);
		if((isBeg==0&&star_id==-1))
		topicTV.setOnClickListener(this);
		atTV.setOnClickListener(this);
		
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
			
			submitPictureActivity=null;
			
			
			this.finish();
			System.gc();
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
					/*if(new File(finalPath).length()>1024*1024*1){
						BitmapFactory.Options options=new BitmapFactory.Options();
						options.inSampleSize=2;
						bitmap=BitmapFactory.decodeFile(finalPath,options);
					}else if(new File(finalPath).length()>1024*512*1){
						BitmapFactory.Options options=new BitmapFactory.Options();
						options.inSampleSize=2;
						bitmap=BitmapFactory.decodeFile(finalPath,options);
					}*/
					
				  
					
					FileOutputStream fos=null;
					FileOutputStream fos1=null;
					try {
						String path2=null;
						boolean canCompress=false;
//						if(bitmap==null){
							
							BitmapFactory.Options options=new BitmapFactory.Options();
							if(new File(finalPath).length()>=1024*1024*1){
								if(new File(finalPath).length()>1024*1024*9){
									options.inSampleSize=4;
								}else{
									options.inSampleSize=2;
								}
								canCompress=true;
							bitmap=BitmapFactory.decodeFile(finalPath,options);
							try {
								Thread.sleep(60);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if((bitmap.getWidth()>2000||bitmap.getHeight()>2000)&&(bitmap.getHeight()/bitmap.getWidth()<3)){
								float scale=1.0f;
								float scale1=2000f/(bitmap.getWidth()*1f);
								float scale2=2000f/(bitmap.getHeight()*1f);
								if(scale1>scale2){
									scale=scale2;
								}else{
									scale=scale1;
								}
								Matrix m=new Matrix();
								m.postScale(scale, scale);
								bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
							}
							}
						if(bitmap!=null){
							if(canCompress){
								String path=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+"_"+bitmap.getWidth()+"&"+bitmap.getHeight()+".jpg";
								fos = new FileOutputStream(new File(path));
								
										bitmap.compress(CompressFormat.JPEG, 90, fos);
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									path=compressPictureSize(path);
									if(new File(path).length()>1024*300*1){
										options.inSampleSize=2;
										
										
										bitmap=BitmapFactory.decodeFile(path,options);
										path=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+"_"+bitmap.getWidth()+"&"+bitmap.getHeight()+".jpg";
										fos1 = new FileOutputStream(new File(path));
										
										bitmap.compress(CompressFormat.JPEG, 90, fos1);
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									}
									
								File file=new File(path);
								path2=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+"@"+file.length()+"@_"+bitmap.getWidth()+"&"+bitmap.getHeight()+".jpg";
								file.renameTo(new File(path2));
							}else{
								
								String path=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+"_"+bitmap.getWidth()+"&"+bitmap.getHeight()+".jpg";
								fos = new FileOutputStream(new File(path));
								
										bitmap.compress(CompressFormat.JPEG, 100, fos);
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									File file=new File(path);
								path2=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+"@"+file.length()+"@_"+bitmap.getWidth()+"&"+bitmap.getHeight()+".jpg";
								file.renameTo(new File(path2));
							}
						}
						
						
					
					
					
					
					
					
					
					PetPicture petPicture=new PetPicture();
					petPicture.animal=animal;
					petPicture.cmt=info;
					if(path2==null){
						path2=finalPath;
						File file=new File(path2);
						options=new BitmapFactory.Options();
								options.inSampleSize=2;
						bitmap=BitmapFactory.decodeFile(path2,options);
						String temp=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+"@"+file.length()+"@_"+bitmap.getWidth()+"&"+bitmap.getHeight();
						path2=temp+".jpg";
//						file.renameTo(new File(path2));
						fileChannelCopy(file,new File(path2));
						
						bitmap=null;
					}
					petPicture.petPicture_path=path2;
					if(isBeg==1)
					petPicture.isBeg=true;
					petPicture.picture_type=isBeg;
					if(isBeg==1)petPicture.topic_name="挣口粮";
					if(isBeg!=0&&isBeg!=1){
						SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
						petPicture.topic_name=sp.getString("p_mid"+sp.getInt("p_mid"+isBeg+"_label", 0)+"_subject", "");
					}
					if(topic_id!=-1){
						petPicture.topic_id=topic_id;
						petPicture.topic_name=topic_name;
					}else if(!StringUtil.isEmpty(topic_name)){
						petPicture.topic_name=topic_name;
						petPicture.topic_name.replace('#', ' ');
					}
					petPicture.star_id=star_id;
					petPicture.relates=relatesId;
					petPicture.relatesString=relateString;
					PetPicture petPicture2=HttpUtil.uploadImage(petPicture,handleHttpConnectionException.getHandler(SubmitPictureActivity.this),SubmitPictureActivity.this);
					petPicture2.topic_id=petPicture.topic_id;
					petPicture2.topic_name=petPicture.topic_name;
					petPicture2.relates=relatesId;
					petPicture2.relatesString=relateString;
					petPicture2.picture_type=petPicture.picture_type;
					if(petPicture.picture_type==1){
						MobclickAgent.onEvent(SubmitPictureActivity.this, "food_suc");
					}else if(petPicture.picture_type==2){
						MobclickAgent.onEvent(SubmitPictureActivity.this, "topic1_suc");
					}else if(petPicture.picture_type==3){
						MobclickAgent.onEvent(SubmitPictureActivity.this, "topic2_suc");
					}else{
						MobclickAgent.onEvent(SubmitPictureActivity.this, "photo");
					}
					
					if(!StringUtil.isEmpty(petPicture2.url)){
//						HttpUtil.imageInfo(petPicture2, handler, SubmitPictureActivity.this);
						handler.sendEmptyMessage(DISMISS_PROGRESS);
						
						petPicture2.animal=animal;
						
						SubmitPictureActivity.this.petPicture=petPicture2;
						UserImagesJson.Data  data=new UserImagesJson.Data();
						 data.path=path2;
						 data.comment=info;
						 Constants.whereShare=1;
						 
						 
						 if(sendToXinlang){
							 xinlangShare(petPicture);
						 }else
						 if(sendToWeixin){
							 friendShare(petPicture);
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
			if(isBeg!=0){
				return;
			}
			Intent intent7=new Intent(this,AtUserListActivity.class);
			intent7.putExtra("mode", 2);
			this.startActivity(intent7);
			break;
		case R.id.editText1:
			showCommentEditor();
			break;
		}
	}
	/**
	 * 拷贝文件
	 * @param old
	 * @param newfile
	 */
    public void fileChannelCopy(File old, File newfile) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(old);
            fo = new FileOutputStream(newfile);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
//					getContentResolver().delete(uri, null, null);
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
	private  void loadBitmap(Uri uri){
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
	private  void addShares(boolean over1){
		final boolean over=over1;
		petPicture.shares++;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
             final MyUser user=HttpUtil.imageShareNumsApi(SubmitPictureActivity.this,petPicture.img_id, handleHttpConnectionException.getHandler(SubmitPictureActivity.this));
            LogUtil.i("miii", "over==="+over);
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
	private  void over(){
		/*runOnUiThread(new Runnable() {
			
			@Override
			public void run() {*/
				// TODO Auto-generated method stub
				Constants.shareMode=-1;
				Constants.whereShare=-1;
				
				Intent intent=null;
				if(isBeg!=0||star_id!=-1){
					intent=new Intent(SubmitPictureActivity.this,Dialog6Activity.class);
					petPicture.animal=animal;
					petPicture.star_title=topic_name;
					intent.putExtra("picture", petPicture);
					intent.putExtra("star_id", star_id);
				}else{
					if(NewShowTopicActivity.newShowTopicActivity!=null){
						NewShowTopicActivity.newShowTopicActivity.recyle();
					}
					intent=new Intent(SubmitPictureActivity.this,NewShowTopicActivity.class);
					intent.putExtra("PetPicture", petPicture);
				}
				
				SubmitPictureActivity.this.startActivity(intent);
				
				this.finish();
				System.gc();
				LogUtil.i("me", "SubmitPicture关闭掉===================5");
			/*}
		});*/
		
		
	}
	 
	      private void xinlangShare(final PetPicture petPicture) {
				// TODO Auto-generated method stub
	    	  
	    	  runOnUiThread(new Runnable() {
				boolean canContinue=false;
				@Override
				public void run() {
					// TODO Auto-generated method stub
					 SinaShareContent sinaShareContent = new SinaShareContent();
				   	   UMImage umImage=new UMImage(SubmitPictureActivity.this, petPicture.petPicture_path);
				   	sinaShareContent.setShareImage(umImage);
//				   	   circleMedia.setTargetUrl("");
				   	sinaShareContent.setShareContent(petPicture.cmt);
				   	   
				   	
				   	if(StringUtil.isEmpty(petPicture.cmt)){
				   		sinaShareContent.setShareContent((StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"这是我最新的美照哦~~打滚儿求表扬~~"+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id+" #我是大萌星#");
					}else{
						sinaShareContent.setShareContent(petPicture.cmt+(StringUtil.isEmpty(petPicture.topic_name)?"":(" "+petPicture.topic_name+" "))+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id+" #我是大萌星#");
					}
				   	
				    if(petPicture.picture_type!=0){
				    	
				    	sinaShareContent.setShareContent((StringUtil.isEmpty(petPicture.cmt)?"看在我这么努力卖萌的份上快来宠宠我！免费送我点口粮好不好？":petPicture.cmt)+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=webo"*/+" #我是大萌星#");
					   }else if(star_id!=-1){
						   sinaShareContent.setShareContent("我家"+petPicture.animal.pet_nickName+"参加了宠物星球"+petPicture.star_title+"活动，投票走起~"+"http://"+Constants.IP+Constants.URL_ROOT+"r=social/foodShareApi&img_id="+petPicture.img_id/*+"&to=webo"*/+" #我是大萌星#"); 
					   }
				   	
				   	
				   	   mController.setShareMedia(sinaShareContent);
				   	   mController.postShare(SubmitPictureActivity.this,SHARE_MEDIA.SINA,
				   			   new SnsPostListener() {
				              @Override
				              public void onStart() {
//				                  Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
				              }
				              @Override
				              public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
				                   if (eCode == 200) {
				                  	addShares(false);
				                  	if(petPicture.picture_type==1){
										MobclickAgent.onEvent(SubmitPictureActivity.this, "food_share_suc");
									}else if(petPicture.picture_type==2){
			         					MobclickAgent.onEvent(SubmitPictureActivity.this, "topic1_share_suc");
			         				}else if(petPicture.picture_type==2){
			         					MobclickAgent.onEvent(SubmitPictureActivity.this, "topic2_share_suc");
			         				}
				                    Toast.makeText(SubmitPictureActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				                   } else {
//				                	   over();
				                        String eMsg = "";
				                        if (eCode == -101){
				                            eMsg = "没有授权";
				                        }
				                        Toast.makeText(SubmitPictureActivity.this, "分享失败[" + eCode + "] " + 
				                                           eMsg,Toast.LENGTH_SHORT).show();
				                   }
				                   canContinue=true;
				                   LogUtil.i("miii", "新浪分享完毕，sendToWeixin="+sendToWeixin);
				                   if(sendToWeixin){
				                	   
//				                	   friendShare(petPicture);
				                   }else{
				                	   over();
				                   }
				                   
				                   
				            }
				   });
				   	   if(sendToWeixin){
				   		   new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								while(!canContinue){
									
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								friendShare(petPicture);
							}
						}).start();
				   	   }
				      
				}
			});

		   	  
			}
	      private  void friendShare(final PetPicture petPicture){
	    	  runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					CircleShareContent circleMedia = new CircleShareContent();
				   	   UMImage umImage=new UMImage(SubmitPictureActivity.this, petPicture.petPicture_path);
				   	   circleMedia.setShareImage(umImage);
//				   	   circleMedia.setTargetUrl("");
//				   	circleMedia.setShareContent(animal.pet_nickName);
				   	   
				   	   
				   	   
				   	  circleMedia.setShareImage(umImage);
					   if(petPicture.picture_type!=0){
							 //设置分享文字
						   circleMedia.setShareContent("看在我这么努力卖萌的份上快来宠宠我！免费送我点口粮好不好？");
							 //设置title
						   circleMedia.setTitle(StringUtil.isEmpty(petPicture.cmt)?"轻轻一点，免费赏粮！我的口粮全靠你啦~":petPicture.cmt);
							
					  }else if(star_id!=-1){
						  circleMedia.setShareContent("我家"+petPicture.animal.pet_nickName+"参加了宠物星球"+petPicture.star_title+"活动，投票走起~");
//						   circleMedia.setTitle("我是"+pp.animal.pet_nickName+"，快来给我投票啦~");
						   circleMedia.setTitle("我家"+petPicture.animal.pet_nickName+"参加了宠物星球"+petPicture.star_title+"活动，投票走起~");
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
				   	 LogUtil.i("miii", "微信分享完毕，mController="+mController);
				   	   mController.postShare(SubmitPictureActivity.this,SHARE_MEDIA.WEIXIN_CIRCLE,
				   			   new SnsPostListener() {
				              @Override
				              public void onStart() {
				            	  LogUtil.i("miii", "微信kaishi分享，mController="+mController);
//				                  Toast.makeText(NewShowTopicActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
				              }
				              @Override
				              public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
				                   
				            	  
				            	  LogUtil.i("miii", "微信分享完毕，eCode="+eCode);
				            	  if (eCode == 200) {
				            		  LogUtil.i("miii", "微信分享成功，执行addShares方法   sendToWeixin="+sendToWeixin);
				                  	addShares(true);
				                  	
				                  	if(petPicture.picture_type==2){
				                  		MobclickAgent.onEvent(SubmitPictureActivity.this, "topic1_share_suc");
										
									}else if(petPicture.picture_type==3){
										MobclickAgent.onEvent(SubmitPictureActivity.this, "topic2_share_suc");
			         				}else if(petPicture.picture_type==0){
			         					
			         				}else{
			         					MobclickAgent.onEvent(SubmitPictureActivity.this, "food_share_suc");
			         				}
				                  	
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
			});
	   	   
	      }
	      public String compressPictureSize(String path) {
	    	  if(new File(path).length()>1024*300*1){
	    		  BitmapFactory.Options options=new BitmapFactory.Options();
	    		  FileOutputStream fos1=null;
					options.inSampleSize=2;
					
					
					Bitmap bitmap=BitmapFactory.decodeFile(path,options);
					path=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+System.currentTimeMillis()+"_"+bitmap.getWidth()+"&"+bitmap.getHeight()+".jpg";
					try {
						fos1 = new FileOutputStream(new File(path));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					bitmap.compress(CompressFormat.JPEG, 90, fos1);
					
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return compressPictureSize(path);
				}else{
					return path;
				}
	      }
	      
	      
	      
	      
	      private List<String> reslist;
	  	private ViewPager expressionViewpager;
	  	private PasteEditText mEditTextContent;
	  	private ImageView emotionIv;
	  	private  LinearLayout emotionLayout;
	  	boolean showSmile=true;
		private  boolean show_add_comment=false;
		private  TextView send_comment_tv;
		private  LinearLayout addcommentLayout;
		private  	boolean canSend=false;
	  	public    void showCommentEditor(){
	         
	    	emotionIv.setImageResource(R.drawable.chatting_biaoqing_btn_enable);
	    	showSmile=true;
			if(show_add_comment){
				show_add_comment=false;
				mEditTextContent.setVisibility(View.INVISIBLE);
				addcommentLayout.setVisibility(View.GONE);
			}else{
				mEditTextContent.setVisibility(View.VISIBLE);
				show_add_comment=true;
//				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				mEditTextContent.setEnabled(true);
				mEditTextContent.setFocusable(true);
				mEditTextContent.setFocusableInTouchMode(true);
				if(mEditTextContent.requestFocus(EditText.FOCUS_FORWARD)){
					mEditTextContent.setSelection(0);;
					InputMethodManager m = (InputMethodManager)   
							mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
//					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				};
				Animation animation=AnimationUtils.loadAnimation(this, R.anim.anim_translate_showtopic_addcommentlayout_in);
				addcommentLayout.setAnimation(animation);
				animation.start();
				addcommentLayout.setVisibility(View.VISIBLE);
			}

		}
	  	public   void sendComment(){
			if(!canSend){
	    		InputMethodManager m = (InputMethodManager)   
	    				mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
						m.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0);
						addcommentLayout.setVisibility(View.GONE);
						emotionLayout.setVisibility(View.GONE);
//						mEditTextContent.setHint("写个描述呗");
						show_add_comment=false;
						return;
	    	}
	    	final String comment=mEditTextContent.getText().toString();
	    	if(StringUtil.isEmpty(comment)){
	    		Spannable span = NewSmileUtils.getSmiledText(this, comment);
				// 设置内容
	    		editText.setText(span, BufferType.SPANNABLE);
	    	}
	    	
	  	}
	  	
	  	public void initEmotion(){
	  		send_comment_tv=(TextView)findViewById(R.id.send_comment);
			addcommentLayout=(LinearLayout)findViewById(R.id.add_comment_linearlayout);
			send_comment_tv.setText("取消");
			send_comment_tv.setOnClickListener(this);
	  		emotionIv=(ImageView)findViewById(R.id.emotion_iv);
	  		emotionLayout=(LinearLayout)findViewById(R.id.ll_face_container);
	  		expressionViewpager = (ViewPager)findViewById(R.id.vPager);
	  		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
	  		emotionIv.setOnClickListener(new OnClickListener() {
	  			
	  			@Override
	  			public void onClick(View v) {
	  				// TODO Auto-generated method stub
	  				if(showSmile){
	  					emotionIv.setImageResource(R.drawable.expression_keyboard);
	  					
	  					InputMethodManager im=(InputMethodManager)SubmitPictureActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//	  			    	im.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0);i
	  			    	im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
	                      handler.postDelayed(new Runnable() {
	  						
	  						@Override
	  						public void run() {
	  							// TODO Auto-generated method stub
	  							emotionLayout.setVisibility(View.VISIBLE);
	  						}
	  					}, 200);
	  			    	showSmile=false;
	  				}else{
	  					
	  					InputMethodManager m = (InputMethodManager)   
	  							mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
	  							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
	  			    	
	  			    	emotionIv.setImageResource(R.drawable.chatting_biaoqing_btn_enable);
	  					emotionLayout.setVisibility(View.GONE);
	  			    	showSmile=true;
	  				}
	  				/*if(emotionLayout.getVisibility()==View.GONE){
	  					emotionLayout.setVisibility(View.VISIBLE);
	  				}else{
	  					emotionLayout.setVisibility(View.GONE);
	  				}*/
	  				
	  			}
	  		});
	  		send_comment_tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sendComment();
				}
			});
	  		mEditTextContent.addTextChangedListener(new TextWatcher() {
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
					int start=mEditTextContent.getSelectionStart();
					int stop=mEditTextContent.getSelectionEnd();
					String str=mEditTextContent.getText().toString();
					if(str!=null){
						Spannable span = NewSmileUtils.getSmiledText(SubmitPictureActivity.this, str);
						int max=40;
						/*if(str.getBytes().length>str.length()){
							max=40;
						}else{
							max=40;
						}*/
						if(span!=null){
							/*info+=str;
							lastInfo=str;
							editText.;*/
//							textView.setText(str);
							if(span.length()>0){
				    			send_comment_tv.setText("确定");
				    		}
							if(span.length()<=max){
									if(span.length()==max){
										info=span.toString();
									}
									// 设置内容
						    		editText.setText(span, BufferType.SPANNABLE);
//						    		toast=Toast.makeText(SubmitPictureActivity.this, "您已经输入"+span.length()+"个字...", 200);
//						    		toast.show();
								
							}else{
								if(start>0){
									s.delete(start-1, stop);
									
									if(start>max){
										
									}else{
										mEditTextContent.setSelection(start);
									}
									
									toast=Toast.makeText(SubmitPictureActivity.this, "您已经输入"+40+"个字...", 200);
									toast.show();
//									editText.setText(temp);
								}
								
							}
						}
					}
				
						
				}
			});
	  		
	  		// 表情list
	  				reslist = getExpressionRes(44);
	  				// 初始化表情viewpager
	  				List<View> views = new ArrayList<View>();
	  				View gv1 = getGridChildView(1);
	  				View gv2 = getGridChildView(2);
	  				View gv3 = getGridChildView(3);
	  				views.add(gv1);
	  				views.add(gv2);
	  				views.add(gv3);
	  				expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
	  	}
	  	public List<String> getExpressionRes(int getSum) {
	  		List<String> reslist = new ArrayList<String>();
	  		for (int x = 1; x <= getSum; x++) {
	  			String filename = "ee_" + x;

	  			reslist.add(filename);

	  		}
//	  		return reslist;
	  		return NewSmileUtils.tags;

	  	}
	  	/**
	  	 * 获取表情的gridview的子view
	  	 * 
	  	 * @param i
	  	 * @return
	  	 */
	  	private View getGridChildView(int i) {
	  		View view = View.inflate(SubmitPictureActivity.this, R.layout.expression_gridview2, null);
	  		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
	  		List<String> list = new ArrayList<String>();
	  		if (i == 1) {
	  			List<String> list1 = reslist.subList(0, 20);
	  			list.addAll(list1);
	  		} else if (i == 2) {
	  			list.addAll(reslist.subList(20, 40));
	  		}else if(i==3){
	  			
	  			list.addAll(reslist.subList(40, reslist.size()));
	  		}
	  		list.add("delete_expression");
	  		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(SubmitPictureActivity.this, 1, list,1);
	  		gv.setAdapter(expressionAdapter);
	  		gv.setOnItemClickListener(new OnItemClickListener() {

	  			@Override
	  			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	  				String filename = expressionAdapter.getItem(position);
	  				try {
	  					// 文字输入框可见时，才可输入表情
	  					// 按住说话可见，不让输入表情
//	  					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

	  						if (filename != "delete_expression") { // 不是删除键，显示表情
	  							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
	  							Class clz = Class.forName("com.aidigame.hisun.imengstar.huanxin.NewSmileUtils");
//	  							Field field = clz.getField(filename);
	  							mEditTextContent.append(NewSmileUtils.getSmiledText(SubmitPictureActivity.this, /*(String) field.get(null)*/filename));
	  						} else { // 删除文字或者表情
	  							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

	  								int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
	  								if (selectionStart > 0) {
	  									String body = mEditTextContent.getText().toString();
	  									String tempStr = body.substring(0, selectionStart);
	  									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
	  									if (i != -1) {
	  										CharSequence cs = tempStr.substring(i, selectionStart);
	  										if (NewSmileUtils.containsKey(cs.toString()))
	  											mEditTextContent.getEditableText().delete(i, selectionStart);
	  										else
	  											mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
	  									} else {
	  										mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
	  									}
	  								}
	  							}

	  						}
//	  					}
	  				} catch (Exception e) {
	  				}

	  			}
	  		});
	  		return view;
	  	}

}
