package com.aidigame.hisun.pet.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.R.integer;

/**
 * 宠物
 * @author admin
 *
 */
public class Animal implements Serializable{
	/**
	 * * "data":{"aid":"1000000188","name":"gcc","tx":"","gender":"2","from":"0",
             * "type":"107","age":"5","master_id":"196",
             * "t_rq":"0","u_name":"moon","u_tx":"","u_rank":"1",
             * "fans":"1","followers":"0"},
	 */
	private static final long serialVersionUID = 7660637701280600780L;
	public int a_gender;//1 公；2 母；3 其他
	public String race;//族类，像 牧羊犬
	public int type;//族类代码
	public String pet_nickName;
	public int a_age;
	public String a_age_str;
	public String pet_iconPath;
	public String pet_iconUrl;
	public long  a_id;
	public String job;//用户在此王国的职位
	public String u_rank;//官职代号
	public int u_rankCode;
	public int master_id;
	public int fans;//成员
	public int followers;//粉丝
	public int t_rq;//总人气
	public int d_rq;//日人气
	public int w_rq;//周人气
	public int m_rq;//月人气
	public int t_contri;
	public int rq;
	
	
	public long foodNum;

	
	public String announceStr;
	
	
	public int percent;//击败了多少萌星
	public int shake_count=3;
	public int send_gift_count=0;
	public int touch_count=3;
	public int isVoiced=1;
	public String invite_code;
	public long imagesNum;
	public long totalfoods;
	public long giftsNum;
	public long newsNum;
	
	
	public ArrayList<PetPicture> picturs;
	
	
	public int from;//星球
	public String u_name;
	public String u_tx;
	
	
	public int img_id=-1;
	
	public int news_count;
	public MyUser user;
	
	public int change;//0不变，1上升，-1下降
	public boolean showArrow=false;
	public boolean isScale=false;
	public int ranking;//排名
	public boolean hasJoinOrCreate=false;//加入或者是这个国家的主人
	
	public boolean is_follow;//是否关注
	public boolean is_join;//是否加入
	
	public boolean isTouched;
	public String touchedPath;
	
	
	public boolean isSelected=false;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (a_id ^ (a_id >>> 32));
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
		Animal other = (Animal) obj;
		if (a_id != other.a_id)
			return false;
		return true;
	}
	
	
	
	
}
