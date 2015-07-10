package com.aidigame.hisun.imengstar.http.json;
/**
 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
 * "confVersion":"1.0",
 * "data":{"isSuccess":false,"SID":"v49j7o9hn4jb1r65acho2t34n4"},
 * "currentTime":1401420697}
 * @author admin
 *
 */
public class LoginJson {
	public int state;
	public int errorCode;//0  正常；1  异常；2 SID过期
	public String errorMessage;
	public String version;
	public String confVersion;
	public long currentTime;
	public Data data;
	
	public static class Data{
		public boolean isSuccess;
		public String SID;
		
		
		/*
		 * 获取消息和活动数目api
		 * "state":0,"errorCode":0,"errorMessage":"",
		 * "version":"1.0","confVersion":"1.0",
		 * "data":{"mail_count":"3","topic_count":"1"},
		 * "currentTime":1404794631}
		 */
		
		public int mail_count;
		public int topic_count;
	}
}

