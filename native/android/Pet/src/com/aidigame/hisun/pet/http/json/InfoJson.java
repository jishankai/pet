package com.aidigame.hisun.pet.http.json;

import java.util.ArrayList;

/**
 * http://54.199.161.210/dc/index.php?r=user/infoApi&sig=beac851bfcd1b0d3dc98b327aa7fbad2&SID=
 * info返回结果{
 * "state":0,"
 * errorCode":0,
 * "errorMessage":"",
 * "version":"1.0",
 * "confVersion":"1.0",
 * "data":[
 *         {"usr_id":"8",
 *          "name":"23",
 *          "gender":"1",
 *          "tx":null,
 *          "age":"1",
 *          "type":"1",
 *          "code":"co9rg7",
 *          "inviter":"0",
 *          "create_time":"0",
 *          "update_time":"2014-06-05 06:48:46"
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
	}

}
