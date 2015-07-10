package com.aidigame.hisun.imengstar.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.aidigame.hisun.imengstar.http.json.UserImagesJson.Comments;

import android.R.integer;

public class PetPicture implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -261380292142490480L;
	/*
	 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
	 * "data":[{"topic_id":"0","topic_name":"","cmt":"",
	 * "url":"1000000205_1.1409738733709.jpg","likes":"0","is_deleted":0,
	 * "aid":"1000000205","relates":"","create_time":1409739151,
	 * "img_id":"2","likers":null,"comments":null,"update_time":null}],
	 * "currentTime":1409739151}
	 */
	/*
	 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0","confVersion":"1.0",
	 * "data":{
	 *    "image":{
	 *        "img_id":"2","aid":"1000000205","topic_id":"0",
	 *        "topic_name":"","relates":"","cmt":"",
	 *        "url":"1000000205_1.1409738733709.jpg","likes":"0",
	 *        "likers":"","comments":"","create_time":"1409739151",
	 *        "update_time":"2014-09-03 10:12:31",
	 *        "is_deleted":"\u0000","gifts":null,"senders":null,
	 *        "shares":null},
	 *   "is_follow":false,"sender_tx":null,"liker_tx":null
	 *  },
	 *  "currentTime":1409743714}
	 */
	public long img_id;//图片id
	public String cmt;
	public String topic_name;
	public int topic_id=-1;
	public String petPicture_path;//本地图片路径
	public String url;//图片url
	public int likes;
	public String is_deleted;
	public String relates;//id字符串
	public String relatesString;//名字字符串
	public long create_time;
	public String likers;
	public String comments;
	public long foodNum;
	public long update_time;
	public int gifts;
	public String senders;
	public String share_ids;
	public int shares;
//	public String sender_tx;
//	public String liker_tx;
	public Animal animal;
	public String is_food;
	
	public boolean isBeg;//是否是求口粮
	public int picture_type;//发布照片的类型
	public long star_id;//海选参加的id;
	public String star_title;//海选的活动名
	public long stars;//推荐票
	public long animal_totals_stars;
	public long end_time;
	
	public ArrayList<String> like_txUrlList;
	public ArrayList<String> gift_txUrlList;
	public ArrayList<Comments> commentsList;
	public String comment_ids;
	public ArrayList<MyUser> likeUsersList,giftUsersList,shareUsersList;
	public int errorCode;
	public String errorMessage;
	
	
	

	
	
	public boolean isVoice=false;
	public String voicePath;
	public boolean updateVoiceSuccess;
	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (img_id ^ (img_id >>> 32));
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
		PetPicture other = (PetPicture) obj;
		if (img_id != other.img_id)
			return false;
		return true;
	}

	public int exp;//上传照片后，用户经验值发生变化；
	
	public static class Comments implements Serializable/*,Parcelable*/{

		/**
		 * 对图片发表的评论
		 */
		private static final long serialVersionUID = -679405287083858316L;
		public long usr_id;
		public String usr_tx;
		public String name;
		public String body;
		public long create_time;
		public long reply_id;
		public String reply_name;
		public boolean isReply;
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
		
	}
}
