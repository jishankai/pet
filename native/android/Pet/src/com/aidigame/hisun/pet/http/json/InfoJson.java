package com.aidigame.hisun.pet.http.json;

import java.util.ArrayList;

import com.aidigame.hisun.pet.bean.User;

import android.R.integer;

/**
 * http://54.199.161.210/dc/index.php?r=user/infoApi&sig=beac851bfcd1b0d3dc98b327aa7fbad2&SID=
 * info返回结果{
 * "state":0,"
 * errorCode":0,
 * "errorMessage":"",
 * "version":"1.0",
 * "confVersion":"1.0",
 * "data":[
 *         {"usr_id":"47","name":"1\u697c\u4e3b","gender":"1",
			 * "tx":"47_icon_profile_block_user.hdpi.png","age":"1",
			 * "type":"1","code":"4dkrc3","inviter":"0","create_time":"0",
			 * "update_time":"2014-06-12 11:38:57","exp":"0","lv":"0",
			 * "follow":"2","follower":"1","con_login":"0","imagesCount":"9"
 *          }],
 *"currentTime":1401951674}
 * @author admin
 *
 */
public class InfoJson {
	public int state;
	public int errorCode;
	public String errorMessage;
	public String version;
	public String confVersion;
	public long currentTime;
	public ArrayList<Data> data;
	public User user;
	
	
	
	
	public static class Data{
		public int user_id;
		public String name;
		public int gender;
		public String tx;
		public String age;
		public int type;
		public String code;
		public String inviter;
		public String create_time;
		public String update_time;
		public int exp;
		public int lv;
		public int follow;
		public int follower;
		public int con_login;
		public int imagesCount;
		
	}

}
