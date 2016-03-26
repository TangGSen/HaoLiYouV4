package com.sen.haoliyou.mode;

import java.io.Serializable;
import java.util.List;

public class SectionBean implements Serializable {
	private List<SectionItemBean> sectionlist;
	private String success;

	public List<SectionItemBean> getSectionlist() {
		return sectionlist;
	}

	public void setSectionlist(List<SectionItemBean> sectionlist) {
		this.sectionlist = sectionlist;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
}
