package com.aidigame.hisun.imengstar.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.aidigame.hisun.imengstar.http.json.ActivityJson.Reward;

/**
 * 话题
 * @author admin
 *
 */
public class Topic implements Serializable{
	public int topic_id=-1;//默认值为-1，表明是自己创建的话题
	public boolean isSelected;//在话题列表中，用于判断是否选中
	public String  topic;
	public String reward;
	public String img;
	public String imgPath;
	public long start_time;
	public long end_time;
	public int people;
	public String remark;
	public String txs;
	public String des;
	public ArrayList<Reward> rewards;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
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
		Topic other = (Topic) obj;
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic))
			return false;
		return true;
	}


	

}
