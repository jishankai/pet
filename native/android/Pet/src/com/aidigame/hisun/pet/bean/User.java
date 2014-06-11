package com.aidigame.hisun.pet.bean;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2792458691622487088L;

	


	public int topicsNum;
	public int focusNum;
	public int userId;
	public String iconPath;
	public String iconUrl;
	public int pictures;
	
	public int fans;
	
	public String race;//族类，像 牧羊犬
	public int classs;//1 汪星人；2喵星人
	public String nickName;
	public int gender;//1 公；2 母；3 其他
	public int code;//招待id  邀请码
	public String age;
	public String uid;
	public int focus;//是否被关注   0 为 关注；1为未被关注
	
	
	
	
}
