package com.sen.haoliyou.mode;

import java.io.Serializable;

public class ResourseKindBean implements Serializable {
	private String id ;
	private String name ;
	private String tindex ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTindex() {
		return tindex;
	}
	public void setTindex(String tindex) {
		this.tindex = tindex;
	}
	@Override
	public String toString() {
		return "ResourseKindBean [id=" + id + ", name=" + name + ", tindex="
				+ tindex + "]";
	}

}
