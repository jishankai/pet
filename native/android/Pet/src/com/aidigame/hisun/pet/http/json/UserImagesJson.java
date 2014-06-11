package com.aidigame.hisun.pet.http.json;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.util.LogUtil;
/**
 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
 * "data":[{
 *        "like":0,"url":"40_0.1402052236803.jpg",
 *        "file":"","usr_id":"40","comment":"",
 *        "create_time":1402052240,"img_id":"5",
 *        "update_time":null}],
 * "currentTime":1402043245}
 * @author admin
 *
 */
public class UserImagesJson implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2297119075796232979L;
	public int state;
	public int errorCode;
	public String errorMessage;
	public String version;
	public String confVersion;
	public long currentTime;
	public ArrayList<Data> datas;
	public UserImagesJson(){
		
	}
	public UserImagesJson(String json){
		parseJson(json);
	}
	private void  parseJson(String json){
		JSONObject jsonObject;
		try {
			/*
			 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
			 * "confVersion":"1.0",
			 * "data": "data":[{
                           *"like":0,"url":"40_0.1402052236803.jpg",
                           *"file":"","usr_id":"40","comment":"",
                           *"create_time":1402052240,"img_id":"5",
                           *"update_time":null}],
			 * "currentTime":1402043245}
			 */
			jsonObject = new JSONObject(json);
			this.state = jsonObject.getInt("state");
			this.errorCode = jsonObject.getInt("errorCode");
			this.errorMessage = jsonObject.getString("errorMessage");
			this.version = jsonObject.getString("version");
			this.confVersion = jsonObject.getString("confVersion");
			this.currentTime = jsonObject.getLong("currentTime");
			this.datas=new ArrayList<Data>();
			JSONArray jsonData1 = (JSONArray)jsonObject.getJSONArray("data");
			JSONArray jsonData=null;
			if(jsonData1.length()>=1){
				jsonData =(JSONArray)jsonData1.get(0);
			}else{
				//TODO 没有数据，提示异常
				jsonData=new JSONArray();
			}
			
			for(int i=0;i<jsonData.length();i++){
				JSONObject object=jsonData.getJSONObject(i);
				Data data=new Data();
				data.url=object.getString("url");
				data.likes=object.getInt("likes");
				data.img_id=object.getInt("img_id");
				if (json.contains("usr_id")&&json.contains("comment")){
					data.usr_id=object.getInt("usr_id");
					data.comment=object.getString("comment");
					data.create_time=object.getLong("create_time");
				}

				datas.add(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static class Data implements Serializable{
		public int likes;
		public String url;
		public String files;
		public int usr_id;
		public String comment;
		public long create_time;
		public int img_id;
		public String update_time;
		public String path;//图片下载到本地 之后的保存地址
		public User  user;
		public boolean isUser=true;
		public boolean isFriend;
	}
}
