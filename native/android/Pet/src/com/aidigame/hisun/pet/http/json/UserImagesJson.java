package com.aidigame.hisun.pet.http.json;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;

import com.aidigame.hisun.pet.bean.User;
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
public class UserImagesJson  implements Serializable{
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
                           *"like":0,"likers":null,"url":"40_0.1402052236803.jpg",
                           *"file":"","usr_id":"40","comment":"",
                           *"create_time":1402052240,"img_id":"5",
                           *"update_time":null}],
			 * "currentTime":1402043245}
			 * 
			 * 
			 * 
			 * "img_id":"566","usr_id":"114","topic_id":"0",
			 * "cmt":"\u6295\u7a3f\u6295\u7a3f","url":"114_7.1404182092546.jpg",
			 * "likes":"0","likers":"","comments":"","create_time":"1404182063",
			 * "update_time":"2014-07-01 02:34:23","is_deleted":"\u0000"
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
			JSONObject object=null;
			Data data=null;
			String likersString=null;
			String[] strs=null;
			String str=null;
			for(int i=0;i<jsonData.length();i++){
				object=jsonData.getJSONObject(i);
				data=new Data();
				data.url=object.getString("url");
				if(json.contains("likes")){
					data.likes=object.getInt("likes");
				}
				if(json.contains("likers")){
					likersString=object.getString("likers");
					data.likersString=likersString;
				}
				data.img_id=object.getInt("img_id");
				if(likersString!=null){
					strs=likersString.split(",");
					if(strs.length==1&&strs[0].equals("null")){
						
					}else{
						data.likers=new ArrayList<Integer>();
						for(int j=0;j<strs.length; j++){
							str=strs[j];
							if(str!=null&&!"".equals(str)&&!" ".equals(str)&&!"null".equals(str)){
								data.likers.add(Integer.parseInt(str));
							}
						}
					}
					
				}
				if (json.contains("usr_id")&&json.contains("cmt")){
					data.usr_id=object.getInt("usr_id");
					data.comment=object.getString("cmt");
					data.create_time=object.getLong("create_time");
				}

				datas.add(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static class Comments implements Serializable/*,Parcelable*/{

		/**
		 * 对图片发表的评论
		 */
		private static final long serialVersionUID = -679405287083858316L;
		public int usr_id;
		public String name;
		public String body;
		public long create_time;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ (int) (create_time ^ (create_time >>> 32));
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
			Comments other = (Comments) obj;
			if (create_time != other.create_time)
				return false;
			return true;
		}
		/*@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			dest.writeInt(usr_id);
			dest.writeString(name);
			dest.writeString(body);
			dest.writeLong(create_time);
		}
		public static final Parcelable.Creator<Comments> CREATOR=new Creator<UserImagesJson.Comments>() {

			@Override
			public Comments createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				Comments comments=new Comments();
				comments.usr_id=source.readInt();
				comments.name=source.readString();
				comments.body=source.readString();
				comments.create_time=source.readLong();
				return comments;
			}

			@Override
			public Comments[] newArray(int size) {
				// TODO Auto-generated method stub
				return new Comments[size];
			}
		};*/
		
	}
	public static class Data implements Serializable/*,Parcelable*/{
		public boolean isLoadInfo=false;//用户信息是否加载过
		
		public int likes;
		public ArrayList<Integer> likers;
		public String likersString;
		public String url;
		public String files;
		public int usr_id;
		public String comment;
		public String comments;
		public ArrayList<Comments> listComments;
		public long create_time;
		public int img_id;
		public String update_time;
		public String path;//图片下载到本地 之后的保存地址
		public User  user;
		public boolean isUser=true;
		public boolean isFriend;
		public ArrayList<String> likers_icons_urls;
		public String likers_icons_url_strString;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + img_id;
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
			if (img_id != other.img_id)
				return false;
			return true;
		}
		/*@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			dest.writeInt(likes);
			dest.writeList(likers);
			dest.writeString(likersString);
			dest.writeString(url);
			dest.writeString(files);
			dest.writeInt(usr_id);
			dest.writeString(comment);
			dest.writeString(comments);
			dest.writeTypedList(listComments);
			dest.writeLong(create_time);
			dest.writeInt(img_id);
			dest.writeString(update_time);
			dest.writeString(path);
			//TODO
			
			 * 此处暂时不使用，使用Intent传递ArrayList<Object>，list中的每一个对象都是新建的，
			 * 不是地址的引用，而是不同的对象，对list中对象的值进行改变，原有对象的值不会变化，不利于数据的实时更新。
			 
		}*/
		
	}
}
