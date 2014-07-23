package com.aidigame.hisun.pet.http.json;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityJson {
	public int state;
	public int errorCode;//0  正常；1  异常；2 SID过期
	public String errorMessage;
	public String version;
	public String confVersion;
	public long currentTime;
	public ArrayList<Data> datas;
	public ActivityJson(String json){
		parseJson(json);
	}
	public ActivityJson(){
		
	}
	public void parseJson(String json){
		/*
		 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0",
		 * "confVersion":"1.0",
		 * "data":[[{"topic_id":"1","topic":"\u6d3b\u52a8\u6d4b\u8bd5",
		 *            "reward":"\u53ef\u53e3\u53ef\u4e50","img":"1.jpeg",
		 *            "start_time":"1403449200","end_time":"1404054000",
		 *            "people":"2"
		 *        }]],
		 * "currentTime":1404096221}
		 */
		try {
			datas=new ArrayList<ActivityJson.Data>();
			JSONObject o1=new JSONObject(json);
			errorCode=o1.getInt("errorCode");
			errorMessage=o1.getString("errorMessage");
			if(errorCode==0){
				String  dataStr=o1.optString("data");
				if(dataStr!=null&&!"null".equals(dataStr)){
					JSONArray o2=o1.getJSONArray("data");
					if(o2!=null&&o2.length()>0){
						JSONArray o3=o2.getJSONArray(0);
						if(o3!=null&&o3.length()>0){
							JSONObject o4=null;
							Data data=null;
							for(int i=0;i<o3.length();i++){
								o4=o3.getJSONObject(i);
								if(o4!=null){
									data=new Data();
									data.end_time=o4.getLong("end_time");
									data.img=o4.getString("img");
									data.people=o4.getInt("people");
									data.reward=o4.getString("reward");
									data.start_time=o4.getLong("start_time");
									data.topic=o4.getString("topic");
									data.topic_id=o4.getInt("topic_id");
									datas.add(data);
								}
							}
							if(datas!=null&&datas.size()>1){
								ArrayList<ActivityJson.Data> temp=new ArrayList<ActivityJson.Data>();
								for(int i=datas.size()-1;i>=0;i--){
									temp.add(datas.get(i));
								}
								datas=temp;
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static class Data implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -4106422125236231159L;
		public int topic_id;
		public String  topic;
		public String reward;
		public String img;
		public String imgPath;
		public long start_time;
		public long end_time;
		public int people;
		public String remark;
		public String txs;
		public String des;
		public ArrayList<Reward> rewards;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + topic_id;
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
			if (topic_id != other.topic_id)
				return false;
			return true;
		}
		
		
	}
	public static class Reward implements Serializable{
		public String  name;
		public String des;
		public float price;
		public int item_id;
		public String img;
		public String imgPath;
		public int level;
	}
}
