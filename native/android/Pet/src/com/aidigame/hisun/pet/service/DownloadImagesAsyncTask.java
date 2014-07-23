package com.aidigame.hisun.pet.service;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;

import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.UserHomepageActivity;
import com.aidigame.hisun.pet.util.LogUtil;

public class DownloadImagesAsyncTask extends AsyncTask<UserImagesJson.Data, Integer, Boolean> {
    Handler handler;
    Activity activity;
	public DownloadImagesAsyncTask(Handler handler,Activity activity){
		this.handler=handler;
		this.activity=activity;
	}
	@Override
	protected Boolean doInBackground(UserImagesJson.Data... params) {
		// TODO Auto-generated method stub
		String url=Constants.UPLOAD_IMAGE_RETURN_URL;
		for(int i=0;i<params.length;i++){
			if(params[i]==null)continue;
			String  path=HttpUtil.downloadImage(url, params[i].url, null,activity);
			if(path!=null){
				params[i].path=Constants.Picture_Topic_Path+File.separator+params[i].url;
				
				handler.sendEmptyMessage(UserHomepageActivity.MESSAGE_DOWNLOAD_IMAGE);
				LogUtil.i("m","图片下载成功："+params[i].url);
				
			}
		}
		
		return false;
	}
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		
	}



}
