package com.sen.haoliyou.mode;

import java.io.Serializable;

public class SectionItemBean  implements Serializable {
	private String id;
	private String sectionname;
	private String sectionurl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSectionname() {
		return sectionname;
	}

	public void setSectionname(String sectionname) {
		this.sectionname = sectionname;
	}

	public String getSectionurl() {
		return sectionurl;
	}

	public void setSectionurl(String sectionurl) {
		this.sectionurl = sectionurl;
	}
}
