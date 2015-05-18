package com.aidigame.hisun.imengstar.http.json;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.R;
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
	public ArrayList<PetPicture> petPictures;

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
			 * "data": [[{"img_id":"2501","aid":"1000000219","topic_id":"0","topic_name":"",
			 * "relates":"","cmt":"Dddddd","url":"1000000219_3.1409797583.png",
			 * "likes":"2","likers":"231,230","gifts":"0","senders":"",
			 * "comments":"","shares":"0","create_time":"1409797581",
			 * "update_time":"2014-09-04 05:05:49",
			 * "is_deleted":"\u0000"}]],
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
			petPictures=new ArrayList<PetPicture>();
			String dataStr=jsonObject.getString("data");
			JSONArray jsonData=null;
			
			if(!StringUtil.isEmpty(dataStr)&&dataStr.contains("[[")){
				if("[[]]".equals(dataStr))return;
				JSONArray jsonData1 = (JSONArray)jsonObject.getJSONArray("data");
				
				if(jsonData1.length()>=1){
					jsonData =(JSONArray)jsonData1.get(0);
				}else{
					//TODO 没有数据，提示异常
					jsonData=new JSONArray();
				}
			}else if(!StringUtil.isEmpty(dataStr)&&!"null".equals(dataStr)){
				jsonData = (JSONArray)jsonObject.getJSONArray("data");
				/*
				 * {"img_id":"2528","url":"1000000220_4.1409912082.png",
				 * "cmt":"","likes":"0","aid":"1000000220",
				 * "tx":"1000000220_headImage.png","name":"01",
				 * "create_time":"1409912087"}
				 */
			}else{
				return;
			}
			
			JSONObject object=null;
//			Data data=null;
			PetPicture temp=null;
			String likersString=null;
			String[] strs=null;
			String str=null;
			for(int i=0;i<jsonData.length();i++){
				object=jsonData.getJSONObject(i);
//				data=new Data();
				temp=new PetPicture();
				
				temp.url=object.getString("url");
				
				if(json.contains("\"is_food\""))
					temp.is_food=object.getString("is_food");
				if(json.contains("\"create_time\""))
				temp.create_time=object.getLong("create_time");
				if(json.contains("likes")){
					temp.likes=object.getInt("likes");
				}
				if(json.contains("\"likers\"")){
					likersString=object.getString("likers");
					temp.likers=likersString;
				}
				temp.img_id=object.getInt("img_id");
				
				temp.animal=new Animal();
				if(json.contains("\"cmt\"")){
					temp.cmt=object.getString("cmt");
				}
				if(json.contains("\"topic_id\"")){
					temp.topic_id=object.getInt("topic_id");
					temp.topic_name=object.getString("topic_name");
				}
				
				
				if(json.contains("\"comments\"")){
					temp.comments=object.getString("comments");
					if(temp.comments!=null){
						
						String[] strss=temp.comments.split("usr_id:");
						
						// 253,name:fans1,reply_id:281,reply_name:äºåäº,body:法人,create_time:1411126981
						 
						if(strss!=null&&strss.length>1){
							PetPicture.Comments comment=null;
							temp.comment_ids="";
							temp.commentsList=new ArrayList<PetPicture.Comments>();
							for(int ii=0;ii<strss.length;ii++){
								comment=new PetPicture.Comments();
								String cstr=strss[ii];
								if(StringUtil.isEmpty(cstr))continue;
								int start=0;
								int end=cstr.indexOf(",name:");
								if(end<0)continue;
								comment.usr_id=Integer.parseInt(cstr.substring(start, end));
								if(StringUtil.isEmpty(temp.comment_ids)){
									temp.comment_ids=""+comment.usr_id;
								}else{
									temp.comment_ids+=","+comment.usr_id;
								}
								cstr=cstr.substring(end+6);
								start=0;
								if(cstr.contains(",reply_name")&&cstr.contains(",reply_id")){
									end=cstr.indexOf(",reply_id:");
									comment.isReply=true;
									comment.name=cstr.substring(0, end);
									cstr=cstr.substring(end+10);
									end=cstr.indexOf(",reply_name:");
									comment.reply_id=Integer.parseInt(cstr.substring(0, end));
									cstr=cstr.substring(end+12);
									end=cstr.indexOf(",body:");
									comment.reply_name=cstr.substring(0, end);
									cstr=cstr.substring(end+6);
								}else{
									end=cstr.indexOf(",body:");
									comment.name=cstr.substring(0,end);
									cstr=cstr.substring(end+6);
								}
								end=cstr.indexOf(",create_time:");
								comment.body=cstr.substring(0, end);
								cstr=cstr.substring(end+13);
								if(cstr.contains(";")){
									comment.create_time=Long.parseLong(cstr.substring(0,cstr.length()-1));
								}else{
									comment.create_time=Long.parseLong(cstr.substring(0,cstr.length()));
								}
								
								temp.commentsList.add(comment);
								
								
							}
							/*if(petPicture.commentsList!=null){
								ArrayList<PetPicture.Comments> temp=new ArrayList<PetPicture.Comments>();
								for(int i=petPicture.commentsList.size()-1;i>=0;i--){
									temp.add(petPicture.commentsList.get(i));
								}
								petPicture.commentsList=temp;
							}*/
						}
					}
					
				}
				
				
				if(json.contains("\"likers_tx\"")){
					
				
				String tx=object.getString("likers_tx");
				if(!StringUtil.isEmpty(tx)){
					JSONArray arrays=object.getJSONArray("likers_tx");
					MyUser user=null;
					if(arrays!=null&&arrays.length()>0){
						ArrayList<String> st=new ArrayList<String>();
//						ArrayList<User> users=new ArrayList<User>();
						for(int e=0;e<arrays.length();e++){
							st.add(arrays.getString(e));
							user=new MyUser();
							user.u_iconUrl=arrays.getString(e);
//							users.add(user);
						}
						temp.like_txUrlList=st;
//						petPicture.likeUsersList=users;
					}
				}
				}
				
				
				if (json.contains("aid")&&json.contains("cmt")){
					temp.animal.a_id=object.getLong("aid");
					temp.cmt=object.getString("cmt");
//					temp.create_time=object.getLong("create_time");
					if(json.contains("\"type\"")){
						temp.animal.type=object.getInt("type");
						int ty=temp.animal.type;
						if(temp.animal.type>200&&temp.animal.type<300){
							 ty-=201;
							  
							  String[] strArray=PetApplication.petApp.getResources().getStringArray(R.array.dog_race);
							  if(ty<strArray.length){
								  temp.animal.race=strArray[ty];
							  }else{
								  temp.animal.race=strArray[0];
							  }
						}else if(temp.animal.type>100&&temp.animal.type<200){
							ty-=101;
							  
							  String[] strArray=PetApplication.petApp.getResources().getStringArray(R.array.cat_race);
							  if(ty<strArray.length){
								  temp.animal.race=strArray[ty];
							  }else{
								  temp.animal.race=strArray[0];
							  }
						}
					}
					if(json.contains("\"usr_id\"")){
						temp.animal.master_id=object.getLong("usr_id");
					}
	                if(json.contains("\"u_tx\"")){
						temp.animal.u_tx=object.getString("u_tx");
					}
	                if(json.contains("\"u_name\"")){
	                	temp.animal.u_name=object.getString("u_name");
	                }
					
				}
				if(json.contains("\"name\"")){
					temp.animal.pet_nickName=object.getString("name");
					temp.animal.pet_iconUrl=object.getString("tx");
				}

				petPictures.add(temp);
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
		public MyUser  user;
		public boolean isUser=true;
		public boolean isFriend;
		public ArrayList<String> likers_icons_urls;
		public String likers_icons_url_strString;
		
		
		
		public String des;
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
