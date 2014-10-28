package com.aidigame.hisun.pet.bean;

import java.io.Serializable;
import java.security.PublicKey;
/**
 * 宠物动态
 * @author admin
 *
 */
public class PetNews implements Serializable{
	public int nid;
	public long aid;
	public int type;//1,成为粉丝；2，加入王国；3,发图片；4，送礼物；5，叫一叫；6，逗一逗
	public boolean content;//有没有内容
	public int usr_id;
	public String u_name="";
	public long create_time;
	
    //type=3
	public int img_id;
	public String img_url;
	
	
	//type=4
//	public String a_name="";
	public int item_id;
	public String item_name="";
	public int rq;
	public int rank;
	public String job="路人";
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nid;
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
		PetNews other = (PetNews) obj;
		if (nid != other.nid)
			return false;
		return true;
	}

}
