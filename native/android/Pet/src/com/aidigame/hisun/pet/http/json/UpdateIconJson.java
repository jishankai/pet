package com.aidigame.hisun.pet.http.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;

/**
 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
 * "confVersion":"1.0",
 * "data":{"tx":"47_1402192847902.jpg"},
 * "currentTime":1401420697}
 * @author admin
 *
 */
public class UpdateIconJson {
	public int state;
	public int errorCode;//0  正常；1  异常；2 SID过期
	public String errorMessage;
	public String version;
	public String confVersion;
	public long currentTime;
	public Data data;
	
	public UpdateIconJson(String json){
		parseJson(json);
	}
	private void parseJson(String json){
		JSONObject jsonObject;
		try {
			/*
			 * info返回结果{ "state":0," errorCode":0, "errorMessage":"",
			 * "version":"1.0", "confVersion":"1.0", "data":[ {"usr_id":"8",
			 * "name":"23", "gender":"1", "tx":null, "age":"1", "type":"1",
			 * "code":"co9rg7", "inviter":"0", "create_time":"0",
			 * "update_time":"2014-06-05 06:48:46" }],"currentTime":1401951674}
			 */
			jsonObject = new JSONObject(json);
			this.state = jsonObject.getInt("state");
			this.errorCode = jsonObject.getInt("errorCode");
			this.errorMessage = jsonObject.getString("errorMessage");
			this.version = jsonObject.getString("version");
			this.confVersion = jsonObject.getString("confVersion");
			this.currentTime = jsonObject.getLong("currentTime");
			this.data=new Data();
			JSONObject data = jsonObject.getJSONObject("data");
				this.data.tx=data.getString("tx");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static class Data{
		public String tx;
	}
}


