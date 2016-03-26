package com.sen.haoliyou.mode;

import java.io.Serializable;
import java.util.List;

public class ResourseBean implements Serializable {
	private List<ResourseKindBean> knoeledgeList;
	private String success;
	private String type;

	public List<ResourseKindBean> getKnoeledgeList() {
		if (knoeledgeList == null) {
			return null;
		} else {
			return knoeledgeList;
		}
	}

	public void setKnoeledgeList(List<ResourseKindBean> knoeledgeList) {
		this.knoeledgeList = knoeledgeList;
	}

	public String getSuccess() {
		if (success == null) {
			return null;
		} else {
			return success;
		}
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getType() {
		if (type == null) {
			return null;
		} else {
			return type;
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ResourseBean [knoeledgeList=" + knoeledgeList + ", success="
				+ success + ", type=" + type + "]";
	}
}
