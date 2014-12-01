package com.aidigame.hisun.pet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import dalvik.system.VMRuntime;

public class PetApplication extends Application{
	public static PetApplication petApp;
	public User user;
    Constants constants;
    private final static float TARGET_HEAP_UTILIZATION = 0.75f;
    public LinkedList<Activity> activityList;
    public Bitmap blurBmp;
//    public static final String ERROR_MESSAGE="COM.AIDIGAME.HISUN.PET.ERROR_MESSAGE";
	
    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	
		VMRuntime.getRuntime().setTargetHeapUtilization(TARGET_HEAP_UTILIZATION);
		final int HEAP_SIZE = 10 * 1024* 1024 ; 
		VMRuntime.getRuntime().setMinimumHeapSize(HEAP_SIZE);
		petApp=this;
		CrashHandler crashHandler=CrashHandler.getInstance();
		crashHandler.init(this);
		LogUtil.i("exception","petApplication执行onCreate方法" );
		activityList=new LinkedList<Activity>();
		constants=new Constants();
		
		File file=new File(Constants.Picture_Root_Path);
		if(!file.exists()){
			file.mkdir();
		}
		String path=Constants.Picture_Root_Path+File.separator+".nomedia";
		File file2=new File(path);
		if(file2.exists()){
			if(file2.isDirectory())
			{
				file2.delete();
				try {
					file2.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			try {
				file2.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*
		 * 配置ImageLoaderConfiguration
		 */
		//图片缓存路径
		File cacheDir=StorageUtils.getOwnCacheDirectory(getApplicationContext(), "pet/imageloader/cache");
		ImageLoaderConfiguration config=new ImageLoaderConfiguration
				.Builder(getApplicationContext())
		        .threadPriority(Thread.NORM_PRIORITY-2)//
		        .denyCacheImageMultipleSizesInMemory()
		        .discCache(new UnlimitedDiscCache(cacheDir))
		        .discCacheSize(100*1024*1024)
		        .tasksProcessingOrder(QueueProcessingType.LIFO)
//		        .memoryCacheSize(2*1024*1024)
		        .threadPoolSize(2)//建议小于5个
//				.memoryCache(new WeakMemoryCache())
//		        .writeDebugLogs()//当应用发布时移出
		        .build();//构建完成
		ImageLoader.getInstance().init(config);
		
	}

	

}
