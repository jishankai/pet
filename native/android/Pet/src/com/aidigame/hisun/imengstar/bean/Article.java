package com.aidigame.hisun.imengstar.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Article implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6350326400579767141L;
	/*
	 * {"state":0,"errorCode":0,"errorMessage":"","version":"1.0.0","confVersion":"1.1.5",
	 * "data":{"0":{"article_id":"3","icon":"https:\/\/mmbiz.qlogo.cn\/mmbiz\/AWNc3NhEROYUZyxB","image":"",
	 * "url":"http:\/\/mp.weixin.qq.com\/s?__biz=MjM5OTQwMjYwNw==&mid=202789355&idx=1&sn=1e2afab869347f08578801292e2f3cf8#rd",
	 * "title":"\u5982\u4f55\u8c03\u6559\u7537\u76c6\u53cb~",
	 * "description":"\u5982\u4f55\u8c03\u6559\u7537\u76c6\u53cb~",
	 * "create_time":"1428487847","update_time":"2015-04-08 18:19:23"},"1":{"article_id":"2","icon":"https:\/\/mmbiz.qlogo.cn\/mmbiz\/AWNc3NhEROaxCB5G","image":"","url":"http:\/\/mp.weixin.qq.com\/s?__biz=MjM5OTQwMjYwNw==&mid=202337675&idx=1&sn=939984bc77a04e528c63544c78fc7a9d#rd","title":"\u518d\u7ed9\u6211\u5243\u6bdb\uff0c\u6211\u5c31\u628a\u4f60\u559d\u6389\u256d(\u256f^\u2570)\u256e","description":"","create_time":"1428487847","update_time":"2015-04-08 18:18:27"},"banner":{"article_id":"1","icon":"https:\/\/mmbiz.qlogo.cn\/mmbiz\/AWNc3NhEROaZsYk3","image":"https:\/\/mmbiz.qlogo.cn\/mmbiz\/AWNc3NhEROaZsYk3","url":"http:\/\/mp.weixin.qq.com\/s?__biz=MjM5OTQwMjYwNw==&mid=202163703&idx=1&sn=48126bb6c6f8be3a53132a231e65ff69#rd","title":"\u4e07\u4e07\u6ca1\u60f3\u5230\uff01\u5f53\u9ad8\u51b7\u7f8e\u7537\u5b50\u9047\u4e0a\u840c\u59b9\u5b50~","description":"","create_time":"1428487847","update_time":"2015-04-08 18:17:25"}},"currentTime":1428490013}
	 */
	public long article_id;
	public String icon;
	public String image;
	public String url;
	public String title;
	public String description;
	public long create_time;
	public String update_time;
	public ArrayList<Article> banner;
	public String share_path;
	
	
public static class ArticleComment{
		public long id;
		public String comment;
		public String user_name;
		public String usr_tx;
		public long usr_id;
		public long create_time;
		public boolean isReply=false;
	}
}
