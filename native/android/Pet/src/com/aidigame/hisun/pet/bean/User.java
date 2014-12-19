package com.aidigame.hisun.pet.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.R.integer;
/**
 * 用户个人信息
 * @author admin
 *
 */
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
	public int inviter;//邀请码 自己使用的别人的邀请码
	public String u_iconPath;
	public String u_iconUrl;
	public int u_gender;//1 公；2 母；3 其他
	public int u_age;
	public String u_nick;
	public Animal currentAnimal;
	public ArrayList<Animal> aniList;
	public int locationCode;
	public String province="";
	public String city="";
	public String password;
	public int coinCount=-1;
	public int t_contri;
	public int d_contri;
	public int w_contri;
	public int m_contri;
	public int ranking;//贡献排名
	public int change;
	public String rank="经纪人";
	public int rankCode=-1;
	public boolean showArrow;
	public boolean isBind=false;//是否绑定新浪或微信
	
	public int food;//剩余免费口粮数
	
	
	public String weixin_id="";
	public String xinlang_id="";
	
	
	public String create_time;
	public String update_time;
	public int exp=-1;
	public int lv=-1;
	public int follow;
	public int follower;
	public int con_login;
	public int next_gold;
	public int imagesCount;
	
	
	public int senderOrLiker;//送礼获点赞  1送礼；2，点赞；
	
	
	public boolean isSelected=false;//@用户页面，判断是否选中
	

	public int a_gender;//1 公；2 母；3 其他
	public String race;//族类，像 牧羊犬
	public String pet_nickName;
	public String a_age;
	public String pet_iconPath;
	public String pet_iconUrl;
	
	
	
	
	public int classs;//1 汪星人；2喵星人
	public String code;//招待id  邀请码
	
	
	
	
	
	public String uid;
	public int focus;//是否被关注   0 为 关注；1为未被关注

	
	/*
	 * "usr_id":"51","name":"\u5927\u54e5",
 *                 "gender":"1","tx":"51_headImage.png",
 *                 "age":"27","type":"1","code":"ugf95i",
 *                 "inviter":"0","create_time":"0",
 *                 "update_time":"2014-06-11 08:26:40"
	 */
	
	
	
}
