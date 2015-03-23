package com.dodola.model;

public class DuitangInfo {

	private int height;
	private String albid = "";
	private String msg = "";
	public String isrc = "";
	public int width=200;
	public long img_id;
	public long a_id;
	public String path;

	public int getWidth(){
		return width;
	}
	public String getAlbid() {
		return albid;
	}

	public void setAlbid(String albid) {
		this.albid = albid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getIsrc() {
		return isrc;
	}

	public void setIsrc(String isrc) {
		this.isrc = isrc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (img_id ^ (img_id >>> 32));
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
		DuitangInfo other = (DuitangInfo) obj;
		if (img_id != other.img_id)
			return false;
		return true;
	}
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
