package com.sen.haoliyou.mode;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LessonItemBean implements Serializable {
	private int allowday;
	private String comment;
	private String createdate;
	private String id;
	private String isselected;
	private String aaaaaa ;
	private String knowname;
	private int lessonCost;
	//
	private String leName;
	private String name;
	private String picture;
	private String remark;
	private String videoname;
	private String traincomment;
	
	
	public String getLeName() {
		return leName;
	}
	public void setLeName(String leName) {
		this.leName = leName;
	}
	public String getTraincomment() {
		return traincomment;
	}
	public void setTraincomment(String traincomment) {
		this.traincomment = traincomment;
	}
	public int getAllowday() {
		if (allowday == 0) {
			return 0;
		} else {
			return allowday;
		}
	}
	public void setAllowday(int allowday) {
		this.allowday = allowday;
	}
	public String getComment() {
		if (comment == null) {
			return null;
		} else {
			return comment;
		}
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedate() {
		if (createdate == null) {
			return null;
		} else {
			return createdate;
		}
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getId() {
		if (id == null) {
			return null;
		} else {
			return id;
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsselected() {
		if (isselected == null) {
			return null;
		} else {
			return isselected;
		}
	}

	public void setIsselected(String isselected) {
		this.isselected = isselected;
	}
//	public String getkC_FLAG() {
//		return kC_FLAG;
//	}
//	public void setkC_FLAG(String kC_FLAG) {
//		this.kC_FLAG = kC_FLAG;
//	}
	
	
	public String getKnowname() {
		if (knowname == null) {
			return null;
		} else {
			return knowname;
		}
	}

	public String getAaaaaa() {
		return aaaaaa;
	}
	public void setAaaaaa(String aaaaaa) {
		this.aaaaaa = aaaaaa;
	}
	public void setKnowname(String knowname) {
		this.knowname = knowname;
	}

	public int getLessonCost() {
		if (lessonCost == 0) {
			return 0;
		} else {
			return lessonCost;
		}
	}

	public void setLessonCost(int lessonCost) {
		this.lessonCost = lessonCost;
	}

	public String getName() {
		if (name == null) {
			return null;
		} else {
			return name;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		if (picture == null) {
			return null;
		} else {
			return picture;
		}
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getRemark() {
		if (remark == null) {
			return null;
		} else {
			return remark;
		}
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVideoname() {
		if (videoname == null) {
			return null;
		} else {
			return videoname;
		}

	}

	public void setVideoname(String videoname) {
		this.videoname = videoname;
	}
	@Override
	public String toString() {
		return "LessonItemBean [allowday=" + allowday + ", comment=" + comment
				+ ", createdate=" + createdate + ", id=" + id + ", isselected="
				+ isselected + ", aaaaaa=" + aaaaaa + ", knowname=" + knowname
				+ ", lessonCost=" + lessonCost + ", name=" + name
				+ ", picture=" + picture + ", remark=" + remark
				+ ", videoname=" + videoname + "]";
	}

}
