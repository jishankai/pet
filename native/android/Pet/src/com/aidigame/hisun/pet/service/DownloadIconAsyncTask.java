package com.aidigame.hisun.pet.service;

import java.io.File;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Handler;

import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.UserHomepageActivity;
import com.aidigame.hisun.pet.util.LogUtil;

public class DownloadIconAsyncTask extends AsyncTask<UserImagesJson, Integer, Boolean> {
    Handler handler;
	public DownloadIconAsyncTask(Handler handler){
		this.handler=handler;
		
	}
	@Override
	protected Boolean doInBackground(UserImagesJson... params) {
		// TODO Auto-generated method stub
		String url=Constants.USER_DOWNLOAD_TX;
		for(int i=0;i<params.length;i++){
			if(params[i]==null)continue;
			ArrayList<UserImagesJson.Data> datas=params[i].datas;
			for(int j=0;j<datas.size();j++){
				HttpUtil.otherUserInfo(datas.get(j), null);
				handler.sendEmptyMessage(2);
			}
			for(int j=0;j<datas.size();j++){
				if(new File(datas.get(j).user.iconPath).exists())continue;
				boolean  flag=HttpUtil.downloadIconImage(url, datas.get(j).user.iconUrl, null);
				if(flag){
					File file=new File(Constants.Picture_ICON_Path);
					if(!file.exists()){
						file.mkdirs();
					}
					datas.get(j).user.iconPath=Constants.Picture_ICON_Path+File.separator+datas.get(j).user.iconUrl;
					
					handler.sendEmptyMessage(UserHomepageActivity.MESSAGE_DOWNLOAD_IMAGE);
					LogUtil.i("m","图片下载成功："+datas.get(i).user.iconUrl);
					
				}
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
