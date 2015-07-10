package com.aidigame.hisun.imengstar.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户间的一个会话
 * @author admin
 *
 */
public class TalkMessage implements Serializable{
	/*
	 * {"msg":{"1410424994":"hi","1410424988":"you","1410424972":"Ilove you"},
            "new_msg":3,"usr_id":"257","usr_tx":"","usr_name":"my"}
	 */
	public String  usr_tx;
	public int usr_id;
	public String usr_name;
	public int new_msg;//网上下载的新信息
	public int old_new_msg_num;
	public ArrayList<Msg> msgList;
	public int position=-1;
	public void sortMsgList(){
		if(msgList.size()>0){
			Msg[] temp=new Msg[msgList.size()];
			for(int i=0;i<temp.length;i++){
				temp[i]=msgList.get(i);
			}
			Msg msg=null;
			for(int i=0;i<temp.length-1;i++){
				for(int j=i;j<temp.length-1;j++){
					if(temp[j].time>temp[j+1].time){
						msg=temp[j];
						temp[j]=temp[j+1];
						temp[j+1]=msg;
					}
				}
			}
			msgList=new ArrayList<TalkMessage.Msg>();
			for(int i=0;i<temp.length;i++){
				msgList.add(temp[i]);
			}
			long time=0;
			
            for(int i=0;i<msgList.size();i++){
            	if(i==0){
            		msgList.get(i).showTime=true;
            		time=msgList.get(i).time;
            	}else{
            		if(msgList.get(i).time-time>=300){
            			msgList.get(i).showTime=true;
                		time=msgList.get(i).time;
            		}else{
            			msgList.get(i).showTime=false;
            		}
            	}
				
			}
		}
	}
	
	













	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + usr_id;
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
		TalkMessage other = (TalkMessage) obj;
		if (usr_id != other.usr_id)
			return false;
		return true;
	}















	public static class Msg implements Serializable{
		public long time;
		public String content;
		public int from;//来自哪，可能为用户，也可能为系统消息
		public boolean showTime=false;
		public long img_id;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (time ^ (time >>> 32));
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
			Msg other = (Msg) obj;
			if (time != other.time)
				return false;
			return true;
		}
		
		
	}
	public TalkMessage(){
		msgList=new ArrayList<TalkMessage.Msg>();
	}

}
