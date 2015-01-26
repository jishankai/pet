package com.aidigame.hisun.pet.http.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.R.string;

import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.http.json.LoginJson.Data;
/**
 * {"state":0,"errorCode":0,"errorMessage":"",
 * "version":"1.0","confVersion":"1.0",
 * "data":[[
 *          {"user":{
 *                 "usr_id":"51","name":"\u5927\u54e5",
 *                 "gender":"1","tx":"51_headImage.png",
 *                 "age":"27","type":"1","code":"ugf95i",
 *                 "inviter":"0","create_time":"0",
 *                 "update_time":"2014-06-11 08:26:40"
 *                 },
 *          "isFriend":true},{"user":{"usr_id":"65","name":"\u73af\u5883","gender":"2","tx":"65_headImage.png","age":"8","type":"2","code":"usg1tf","inviter":"0","create_time":"0","update_time":"2014-06-11 09:42:48"},"isFriend":true
 *          }
 *        ]],
 *"currentTime":1402566723}
 * @author admin
 *
 */
public class UserJson {
	public int state;
	public int errorCode;//0  正常；1  异常；2 SID过期
	public String errorMessage;
	public String version;
	public String confVersion;
	public long currentTime;
	public ArrayList<Data> datas;
	public int final_id;
	int mode;//json 数据格式 不一致 
	
	public UserJson(String json,int mode){
		this.mode=mode;
		parseJson(json);
	}
	public UserJson(){
	}
	
	private void parseJson(String json) {
		// TODO Auto-generated method stub
		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(json);
			this.state = jsonObject.getInt("state");
			this.errorCode = jsonObject.getInt("errorCode");
			this.errorMessage = jsonObject.getString("errorMessage");
			this.version = jsonObject.getString("version");
			this.confVersion = jsonObject.getString("confVersion");
			this.currentTime = jsonObject.getLong("currentTime");
			this.datas=new ArrayList<Data>();
			JSONArray array1=jsonObject.getJSONArray("data");
			if(array1==null||array1.length()==0)return;
			
			JSONArray array2=null;
			//////////////////////////
			JSONObject O=array1.getJSONObject(0);
			
			String result=O.getString("result");
			if(result!=null&&!"null".equals(result)){
				this.final_id=O.getInt("final_id");
				array2=O.getJSONArray("result");
//////////////////////////////
/*if(mode==1){
	array2=array1;
}else{
	array2=array1.getJSONArray(0);
}*/
if(array2==null||array2.length()==0)return;
JSONObject  jb=null;
JSONObject  jb1=null;
Data data=null;
MyUser user=null;
for(int i=0;i<array2.length();i++){
	jb=array2.getJSONObject(i);
	jb1=jb.getJSONObject("user");
	data=new Data();
	user=new MyUser();
	data.user=user;
	if(json.contains("usr_id")){
		user.userId=jb1.getInt("usr_id");
	}
	
	if(json.contains("name")){
		user.pet_nickName=jb1.getString("name");
	}
	
	if(json.contains("gender")){
		user.a_gender=jb1.getInt("gender");
	}
	
	if(json.contains("tx")){
		user.pet_iconUrl=jb1.getString("tx");
	}
	
	if(json.contains("age")){
		user.a_age=""+jb1.getInt("age");
	}
	
	if(json.contains("type")){
		user.race=jb1.getString("type");
	}
	
	if(json.contains("code")){
		user.code=jb1.getString("code");
	}
	
	if(json.contains("inviter")){
		user.inviter=jb1.getInt("inviter");
	}
	
	if(json.contains("create_time")){
		user.create_time=jb1.getString("create_time");
	}
	
	if(json.contains("update_time")){
		user.update_time=jb1.getString("update_time");
	}
	
	if(json.contains("isFriend")){
		data.isFriend=jb.getBoolean("isFriend");
	}
	datas.add(data);
}
			}
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static class Data{
		public MyUser user;
		public boolean isFriend;
		public boolean isSelected=false;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((user == null) ? 0 : user.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Data other = (Data) obj;
			if (user == null) {
				if (other.user != null)
					return false;
			} else if (!user.equals(other.user))
				return false;
			return true;
		}
		
	}

}
