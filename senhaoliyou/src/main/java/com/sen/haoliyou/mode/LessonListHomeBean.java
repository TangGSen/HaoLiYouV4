package com.sen.haoliyou.mode;

import java.io.Serializable;
import java.util.List;

public class LessonListHomeBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String success ;
	private String type ;
	private List<LessonItemBean> courseslist ;
	public String getSuccess() {
		if(success==null){
			return null ;
		}else {
			return success;
		}
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getType() {
		if(type==null){
			return null ;
		}else {
			return type;
		}
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<LessonItemBean> getCoursesList() {
		if(courseslist==null){
			return null ;
		}else {
			return courseslist;
		}
	}
	public void setCoursesList(List<LessonItemBean> coursesList) {
		this.courseslist = coursesList;
	}
	@Override
	public String toString() {
		return "LessonListHomeBean [success=" + success + ", type=" + type
				+ ", coursesList=" + courseslist + "]";
	}
	
	
}
