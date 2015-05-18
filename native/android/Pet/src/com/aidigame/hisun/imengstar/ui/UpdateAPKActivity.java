package com.aidigame.hisun.imengstar.ui;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jivesoftware.smack.util.Base64.InputStream;
import org.simple.eventbus.EventBus;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.StringAdapter;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.service.DownLoadApkService;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.R;
/**
 * 更新软件 弹出窗
 * @author admin
 *
 */
public class UpdateAPKActivity extends BaseActivity {
      TextView titleTv,cancelTv,sureTv;
      int mode;//1，摇一摇；2，捣捣乱;
      LinearLayout noteLayout;
//      TextView infoTv;
      Handler handler;
      boolean isSending=false;
//      ScrollView scrollview;
      RelativeLayout progress_view_layout,progressLayout;
      View progressView;
      StringAdapter adapter;
      ArrayList<String> list;
      ListView listView;
      public int currentLength=0;
     public  Handler progressHandler=new Handler(){
    	  public void handleMessage(android.os.Message msg) {
    		  switch (msg.what) {
			case 1:
				//进行中
				long length=msg.arg1;
				currentLength+=length;
	    		  int w=progress_view_layout.getMeasuredWidth();
	    		  int width=(int) (length*1f/Constants.apk_size*w);
	    		  RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)progressView.getLayoutParams();
	    		  if(param==null){
	    			  param=new RelativeLayout.LayoutParams(width,RelativeLayout.LayoutParams.WRAP_CONTENT);
	    		  }
	    		  param.width=width;
	    		  progressView.setLayoutParams(param);
				break;
	case 2:
		//完成
		String path=Constants.Picture_Root_Path+File.separator+"pet.apk";
		Intent intent=new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)),  
                "application/vnd.android.package-archive");  
		UpdateAPKActivity.this.startActivity(intent);
		
		
				break;
	case 3:
		//失败
		Toast.makeText(UpdateAPKActivity.this, "更新失败", Toast.LENGTH_LONG).show();
		break;
			}
    		  
    	  };
      };
      
      public static UpdateAPKActivity updateAPKActivity;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	updateAPKActivity=this;
    	setContentView(R.layout.activity_update_apk);
    	mode=getIntent().getIntExtra("mode", 1);
    	handler=HandleHttpConnectionException.getInstance().getHandler(this);
    	noteLayout=(LinearLayout)findViewById(R.id.note_layout);
//    	infoTv=(TextView)findViewById(R.id.info_tv);
    	progress_view_layout=(RelativeLayout)findViewById(R.id.progress_view_layout);
    	progressLayout=(RelativeLayout)findViewById(R.id.progress_layout);
    	progressView=findViewById(R.id.progress_view);
//    	scrollview=(ScrollView)findViewById(R.id.listview);
    	listView=(ListView)findViewById(R.id.listview);
    	list=new ArrayList<String>();
//    	list.add("1.");
//    	list.add("2.");
//    	list.add("3.");
//    	list.add("4.");
    	adapter=new StringAdapter(this, list);
    	listView.setAdapter(adapter);
    	initView();
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String info=HttpUtil.updateVersionInfo(handler, Constants.VERSION, UpdateAPKActivity.this);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(!StringUtil.isEmpty(info)){
							String[] strs=info.split("&");
							LogUtil.i("mi", "info="+info);
//							infoTv.setText(info);
							if(strs!=null){
								for(int i=0;i<strs.length;i++){
									list.add(strs[i]);
								}
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										adapter.update(list);
										adapter.notifyDataSetChanged();
									}
								});
								
							}
						}
					}
				});
			}
		}).start();
    }


	private void initView() {
		titleTv=(TextView)findViewById(R.id.textView1);
		cancelTv=(TextView)findViewById(R.id.cancel_tv);
		sureTv=(TextView)findViewById(R.id.sure_tv);
		
		/*
		 * 强制更新，只显示 更新按钮
		 * 
		 * 非强制，显示两个按钮
		 */
//		
		if(Constants.VERSION!=null&&StringUtil.canUpdate(this, Constants.VERSION)){
			cancelTv.setVisibility(View.GONE);
			noteLayout.setVisibility(View.VISIBLE);
		}
		cancelTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 是否强制更新
				 * 否，关掉对话框，回到应用
				 * 是   弹框提示必须更新，如果不更新则程序关闭
				 */
				
				
				finish();
				System.gc();
			}
		});
		sureTv.setOnClickListener(new OnClickListener() {
			boolean isloading=false;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isloading){
					return;
				}
				isloading=true;
				Intent intent=new Intent(UpdateAPKActivity.this,DownLoadApkService.class);
				UpdateAPKActivity.this.startService(intent);
				
//				new MultiThreadDownload().start();
				
				if(Constants.VERSION!=null&&StringUtil.canUpdate(UpdateAPKActivity.this, Constants.VERSION)){
					listView.setVisibility(View.INVISIBLE);
					progressLayout.setVisibility(View.VISIBLE);
					isFinish=false;
				}else{
					finish();
				}
				
			}
		});
	}
	boolean isFinish=false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
//		return super.onKeyDown(keyCode, event);
		
		EventBus.getDefault().post("", "finish");
		return true;
	}

	   
	    class MultiThreadDownload extends Thread {
	    	  /**

	    	   * 多线程下载

	    	   * 1：使用RandomAccessFile在任意的位置写入数据。

	    	   * 2：需要计算第一个线程下载的数据量，可以平均分配。如果不够平均时，

	    	   *    则直接最后一个线程处理相对较少的数据

	    	   * 3：必须要在下载之前准备好相同大小的文件，通过文件头获取

	    	   */
	    	  @Override
	    	public void run() {
	    	// TODO Auto-generated method stub
	    	super.run();

	    	       //1：声明文件名和下载的地址
	    	 try {

	    	       String fileName = "imengstar.apk";

//	    	       String urlStr = ""+Constants.android_url;
	    	       String urlStr="http://home4pet.aidigame.com/pet_beta_1.0.15.apk";
//	    	       String urlStr="http://apk.hiapk.com/appdown/com.ss.android.article.news?webparams=sviptodoc291cmNlPTkz";

	    	       //2：声明Url

	    	       URL url = new URL(urlStr/*+"/"+fileName*/);

	    	       //3：获取连接

	    	       HttpURLConnection con =

	    	           (HttpURLConnection) url.openConnection();

	    	       //4:设置请求方式

	    	       con.setRequestMethod("GET");

	    	       //5:获取请求头,即文件的长度

	    	       int length = con.getContentLength();//获取下载文件的长度，以计算每个线程应该下载的数据量。
                   length=13006589;
	    	       //6：在指定的目录下，创建一个同等大小的文件

	    	       RandomAccessFile file = new RandomAccessFile(Constants.Picture_Root_Path+File.separator+fileName, "rw");//创建一个相同大小的文件。

	    	       //7:设置文件大小，占位

	    	       file.setLength(length);//设置文件大小。

	    	 

	    	       file.close();

	    	       //8：定义线程个数

	    	       int size = 3;

	    	       //9:计算每一个线程应该下载多少字节的数据，如果正好整除则最好，否则加1

	    	       int block = length/size==0?length/size:length/size+1;//计算每个线程应该下载的数据量。


	    	       System.err.println("每个线程应该下载："+block);

	    	       //10：运行三个线程并计算从哪个字节开始到哪一个字节结束

	    	       for(int i=0;i<size;i++){

	    	           int start = i*block;

	    	           int end = start+(block-1);//计算每一个线程的开始和结束字节。

	    	 

	    	         System.err.println(i+"="+start+","+end);

	    	           new MyDownThread(fileName, start, end,url).start();

	    	       }
	    	      
	   				
	   			} catch (Exception e) {
	   				// TODO: handle exception
	   			}

	    	    }

	    	    

	    	}
	     class MyDownThread extends Thread{

   	       //定义文件名

   	       private String fileName;

   	       //定义从何地开始下载

   	       private int start;

   	       //定义下载到哪一个字节

   	       private int end;

   	       private URL url;

   	       public MyDownThread(String fileName,int start,int end,URL url){

   	           this.fileName=fileName;

   	           this.start=start;

   	           this.end=end;

   	           this.url=url;

   	       }

   	       @Override

   	       public void run() {

   	           try{

   	              //11:开始下载

   	              HttpURLConnection con =

   	                     (HttpURLConnection) url.openConnection();

   	              con.setRequestMethod("GET");

   	              //12：设置分段下载的请求头

   	              con.setRequestProperty("Range","bytes="+start+"-"+end);//设置从服务器上读取的文件块。

   	 

   	              //13:开始下载，需要判断206

   	              if(con.getResponseCode()==206){//访问成功，则返回的状态码为206。

   	                  InputStream in = (InputStream) con.getInputStream();

   	                  //14:声明随机写文件对象，注意rwd是指即时将数据写到文件中，而不使用缓存区

   	                  RandomAccessFile out = new RandomAccessFile(Constants.Picture_Root_Path+File.separator+fileName,"rwd");

   	                  out.seek(start);//设置从文件的某个位置开始写数据。

   	                  byte[] b=new byte[1024];

   	                  int len = 0;

   	                  while((len=in.read(b))!=-1){
   	                     out.write(b,0,len);
                         Message msg=handler.obtainMessage();
                         msg.what=1;
                         msg.arg1=len;
                         handler.sendMessage(msg);
   	                  }

   	                  out.close();

   	                  in.close();

   	              }

   	              System.err.println(this.getName()+"执行完成");

   	           }catch(Exception e){

   	              throw new RuntimeException(e);

   	           }

   	       }

   	    }
}
