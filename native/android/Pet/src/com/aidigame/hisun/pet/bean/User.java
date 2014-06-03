package com.aidigame.hisun.pet.bean;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2792458691622487088L;

	public String race;


	public int topicsNum;
	public int focusNum;
	public String userId;
	public String iconPath;
	public int pictures;
	public int focus;
	public int fans;
	
	
	public int classs;//1 喵星人；2汪星人
	public String nickName;
	public int gender;//1 公；2 母；3 其他
	public int code;
	public int age;
	public String uid;
}
