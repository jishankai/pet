package com.aidigame.hisun.pet.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Topic implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3554359157475118701L;
	public String url;//路径或者url
	public String bmpPath;//下载到本地 后不为null，空表示还未下载

	public String describe;

	public long time;
	public int likesNum;
	public User user;
	
	public int img_id;//图片id





}
