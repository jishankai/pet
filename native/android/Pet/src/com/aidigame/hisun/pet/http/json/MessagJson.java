package com.aidigame.hisun.pet.http.json;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.http.json.LoginJson.Data;

public class MessagJson implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5584179880978296681L;
	public int state;
	public int errorCode;//0  正常；1  异常；2 SID过期
	public String errorMessage;
	public String version;
	public String confVersion;
	public long currentTime;
	public ArrayList<Data> datas;
	public ArrayList<DataSystem> dataSystems;
	public boolean is_system;
	public MessagJson(){}
	public MessagJson(String result){
		parseJson(result);
	}
	


	/*
	 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
	 * "data":[[{
	 *           "mail_id":"55","usr_id":"143","tx":"121_headImage.png","name":"\u540d\u5b57",
	 *           "gender":"1","from_id":"121","body":"\u540d\u5b57\u8d5e\u4e86\u4f60","is_read":"0",
	 *           "is_system":"0","create_time":"1404795619","update_time":"2014-07-08 05:00:19",
	 *           "is_deleted":"\u0000"},
	 *           {"mail_id":"54","usr_id":"143","tx":"121_headImage.png","name":"\u540d\u5b57",
	 *           "gender":"1","from_id":"121","body":"\u540d\u5b57\u8d5e\u4e86\u4f60","is_read":"0",
	 *           "is_system":"0","create_time":"1404795613","update_time":"2014-07-08 05:00:13",
	 *           "is_deleted":"\u0000"},
	 *           {"mail_id":"36","usr_id":"143","tx":"121_headImage.png","name":"\u540d\u5b57",
	 *           "gender":"1","from_id":"121","body":"\u540d\u5b57\u8d5e\u4e86\u4f60","is_read":"0",
	 *           "is_system":"0","create_time":"1404784831","update_time":"2014-07-08 02:00:31",
	 *           "is_deleted":"\u0000"},
	 *           {"mail_id":"31","usr_id":"143","tx":"121_headImage.png","name":"\u540d\u5b57",
	 *           "gender":"1","from_id":"121","body":"\u540d\u5b57\u8d5e\u4e86\u4f60","is_read":"0",
	 *           "is_system":"0","create_time":"1404784478","update_time":"2014-07-08 01:54:38",
	 *           "is_deleted":"\u0000"},{"mail_id":"30","usr_id":"143","tx":"121_headImage.png",
	 *           "name":"\u540d\u5b57","gender":"1","from_id":"121","body":"\u540d\u5b57\u8d5e\u4e86\u4f60",
	 *           "is_read":"0","is_system":"0","create_time":"1404784138",
	 *           "update_time":"2014-07-08 01:48:58","is_deleted":"\u0000"
	 *        }]],
	 * "currentTime":1404799268}
	 */
	private void parseJson(String result) {
		// TODO Auto-generated method stub
		if(result!=null){
			try {
				String string=new JSONObject(result).getString("data");
				if(string!=null&&!"null".equals(string)){
//					if(is_system){
					JSONArray o1=new JSONObject(result).getJSONArray("data");
					if(o1!=null&&o1.length()>0){
						JSONArray array1=o1.getJSONArray(0);
						if(array1!=null&&array1.length()>0){
							JSONObject o2=null;
							dataSystems=new ArrayList<MessagJson.DataSystem>();
							    DataSystem dataSystem=null;
								for(int i=0;i<array1.length();i++){
									o2=array1.getJSONObject(i);
									dataSystem=new DataSystem();
									
									dataSystem.create_time=o2.getLong("create_time");
									dataSystem.fromUser=new MyUser();
									dataSystem.fromUser.userId=o2.getInt("from_id");
									dataSystem.fromUser.pet_iconUrl=o2.getString("tx");
									dataSystem.fromUser.pet_nickName=o2.getString("name");
									dataSystem.fromUser.a_gender=o2.getInt("gender");
									dataSystem.is_system=o2.getInt("is_system")==0?false:true;
									if(dataSystem.is_system){
										String temp=o2.getString("body");
										int index=temp.indexOf(dataSystem.fromUser.pet_nickName);
										index+=dataSystem.fromUser.pet_nickName.length();
										dataSystem.body=temp.substring(index);
									}else{
										dataSystem.body=o2.getString("body");
									}
									
									dataSystem.is_deleted=o2.getString("is_deleted");
									dataSystem.is_read=Integer.parseInt(o2.getString("is_read"))==0?false:true;
									
									dataSystem.mail_id=o2.getString("mail_id");
									dataSystem.update_time=o2.getString("update_time");
									dataSystems.add(dataSystem);
								}
							
							
						}
					}
//					}
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public static class DataSystem implements Serializable{
		public String mail_id;
		public MyUser fromUser;
		public String body;
		public boolean is_read;
		public boolean is_system;
		public long create_time;
		public String update_time;
		public String is_deleted;
		public boolean isRequest=false;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((mail_id == null) ? 0 : mail_id.hashCode());
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
			DataSystem other = (DataSystem) obj;
			if (mail_id == null) {
				if (other.mail_id != null)
					return false;
			} else if (!mail_id.equals(other.mail_id))
				return false;
			return true;
		}
		
	}
	public static class Data{
		public int  imageid;
		public String name;
		public String time;
		public String text;
	}
}
