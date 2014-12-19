package com.aidigame.hisun.pet.bean;

import java.io.Serializable;
/**
 * 礼物
 * @author admin
 *
 */
public class Gift implements Serializable{
	public int no;
	public String name;
	public int price;
	public int add_rq;
	public float ratio;
	public int level;
	public boolean isReal;
	public int detail_image;
	public int sale_status;
	public String effect_des;
	
	public long aid;
	public Animal animal;
	public int img_id=-1;
	public boolean is_buy=false;
	public boolean is_shake=false;
	
	
	
	public String code;
	public boolean is_update;
	public String totalDes;
	public String smallImage;
	public String bigImage;
	public int type;
	public String spec;
	
	
	
	public String status;//状态：新品， 特卖
	public boolean hasBought;//此物品是否买过
	public int boughtNum;//现有数目
	public int buyingNum;//购买数目
	public int hasSendNum;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + no;
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
		Gift other = (Gift) obj;
		if (no != other.no)
			return false;
		return true;
	}
	

}
