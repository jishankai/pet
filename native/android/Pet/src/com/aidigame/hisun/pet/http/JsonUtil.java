package com.aidigame.hisun.pet.http;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.aidigame.hisun.pet.widget.ShowDialog;

public class JsonUtil {
	
	public static boolean  getErrorMessage(String json,final Activity context){
		boolean  flag=false;
		try {
			JSONObject o=new JSONObject(json);
			int errorCode=o.getInt("errorCode");
			if(errorCode==-1||errorCode==1){//0  正常；1  异常；2 SID过期
				final String errorString= o.getString("errorMessage");
				if(errorString!=null){
					context.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ShowDialog.show(errorString, context);
						}
					});
					flag=true;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
		
	}

}
