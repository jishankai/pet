package com.aidigame.hisun.imengstar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.simple.eventbus.EventBus;

import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.util.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.format.DateFormat;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler {
	public static final String TAG="CrashHandler";
	
	//系统默认的UncaughtException处理
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	//CrashHandler实例
	private static CrashHandler INSTANCE=new CrashHandler();
	private Context mContext;
	//用来存储设备信息和异常信息
	private Map<String, String> infos=new HashMap<String, String>();
	//用来格式化日期，作为系统日志的一部分
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    //保证只有一个CrashHandler实例
	private CrashHandler(){
		
	}
	//获取CrashHandler实例，单例模式
	public static CrashHandler getInstance(){
		return INSTANCE;
	}
	/*
	 * 初始化
	 */
	public void init(Context context){
		mContext=context;
		//获取系统默认的UncaughtException处理器
		mDefaultHandler=Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为系统默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 * 当UncaughtException发生时，会默认转入该方法中处理。
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
	
		if(!handleException(ex)&&mDefaultHandler!=null){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//退出程序
			/*for(Activity a:PetApplication.petApp.activityList){
				if(a!=null){
					a.finish();
					
				}
			}*/
			EventBus.getDefault().post("","finish");
			mDefaultHandler.uncaughtException(thread, ex);
		}else{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//退出程序
			/*for(Activity a:PetApplication.petApp.activityList){
				if(a!=null){
					a.finish();
				}
			}
			System.exit(1);
			android.os.Process.killProcess(android.os.Process.myPid());*/
			EventBus.getDefault().post("","finish");
			
		}

	}
	/**
	 * 自定义错误处理，收集错误信息，发送错误报告都在此处进行
	 * @param ex
	 * @return true如果处理该异常；false  未处理该异常
	 */
	private boolean handleException(Throwable ex){
		if(ex==null)return false;
		//使用Toast来显示异常信息
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				Toast.makeText(mContext,"很抱歉，程序出现异常，即将退出." , Toast.LENGTH_LONG).show();;
				Looper.loop();
			}
		}).start();*/
		//收集设备参数信息
		collectDeviceInfo(mContext);
		//保存日志文件
		saveCrashInfo2File(ex);
//		return true;
		return false;//暂时不做处理，是收集信息
	}
	/**
	 * 收集设备参数信息
	 * @param context
	 */
	public void collectDeviceInfo(Context context){
		PackageManager pm=context.getPackageManager();
		try {
			PackageInfo pi=pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			if(pi!=null){
				String versionName=pi.versionName==null?"null":pi.versionName;
				String versionCode=""+pi.versionCode;
				infos.put("versionName", versionName);
				infos.put("versionCode",versionCode);
			}
			Field[] fields=Build.class.getDeclaredFields();
			if(fields!=null){
				for(Field f:fields){
					f.setAccessible(true);
					infos.put(f.getName(), f.get(null).toString());
					LogUtil.i("scroll", "field.getName() ="+f.getName() +";field.get(null)="+f.get(null));
				}
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String saveCrashInfo2File(Throwable ex){
		StringBuffer sb=new StringBuffer();
		for(Map.Entry<String, String> entry:infos.entrySet()){
			String key=entry.getKey();
			String value=entry.getValue();
			sb.append(key+"="+value+"\n");
		}
		Writer writer=new StringWriter();
		PrintWriter pw=new PrintWriter(writer);
		ex.printStackTrace(pw);
		Throwable cause=ex.getCause();
		while(cause!=null){
			cause.printStackTrace(pw);
			cause=cause.getCause();
		}
		pw.close();
		String result=writer.toString();
		sb.append(result);
		long timeStamp=System.currentTimeMillis();
		String time=format.format(new Date(timeStamp));
		String fileName="crash-"+time+".log";
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			String path=Constants.Picture_Root_Path+File.separator+"log";
			File dir=new File(path);
			if(!dir.exists()){
				dir.mkdirs();
			}
			FileOutputStream fis=null;
			try {
				fis=new FileOutputStream(path+File.separator+fileName);
				fis.write(sb.toString().getBytes());
				fis.flush();
				return fileName;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		return null;
	}

}
