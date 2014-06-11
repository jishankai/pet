package com.aidigame.hisun.pet.http.json;

import com.aidigame.hisun.pet.http.json.LoginJson.Data;

/**
 * {"state":0,
 * "errorCode":0,
 * "errorMessage":"",
 * "version":"1.0",
 * "confVersion":"1.0",
 * "data":{"isSuccess":true},
 * "currentTime":1401949319
 * }
 * @author admin
 *
 */
public class RegisterJson {
	public int state;
	public int errorCode;
	public String errorMessage;
	public String version;
	public String confVersion;
	public long currentTime;
	public Data data;
	
	public static class Data{
		public boolean isSuccess;
	}
}
