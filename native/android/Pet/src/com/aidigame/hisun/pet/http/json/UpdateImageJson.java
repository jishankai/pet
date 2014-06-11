package com.aidigame.hisun.pet.http.json;

import java.util.ArrayList;

import com.aidigame.hisun.pet.http.json.RegisterJson.Data;
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
public class UpdateImageJson {
	public int state;
	public int errorCode;
	public String errorMessage;
	public String version;
	public String confVersion;
	public long currentTime;
	public Data data;
	
	public static class Data{
		public int likes;
		//  添加likes
		public String url;
		public String file;
		public int usr_id;
		public String comment;
		public long create_time;
		public int img_id;
		public long update_time;
	}
}
