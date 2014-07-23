package com.aidigame.hisun.pet.bean;

import java.io.Serializable;

public class User implements Serializable{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
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
		User other = (User) obj;
		if (userId != other.userId)
			return false;
		return true;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2792458691622487088L;

	



	public int userId;
	public String iconPath;
	public String iconUrl;


	
	public String race;//族类，像 牧羊犬
	public int classs;//1 汪星人；2喵星人
	public String nickName;
	public int gender;//1 公；2 母；3 其他
	public String code;//招待id  邀请码
	public String age;
	public String uid;
	public int focus;//是否被关注   0 为 关注；1为未被关注
	public String inviter;//邀请码 自己使用的别人的邀请码
	public String create_time;
	public String update_time;
	public int exp;
	public int lv;
	public int follow;
	public int follower;
	public int con_login;
	public int imagesCount;
	/*
	 * "usr_id":"51","name":"\u5927\u54e5",
 *                 "gender":"1","tx":"51_headImage.png",
 *                 "age":"27","type":"1","code":"ugf95i",
 *                 "inviter":"0","create_time":"0",
 *                 "update_time":"2014-06-11 08:26:40"
	 */
	
	
	
}
