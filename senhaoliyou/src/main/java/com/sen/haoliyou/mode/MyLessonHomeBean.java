package com.sen.haoliyou.mode;

import java.io.Serializable;
import java.util.List;

public class MyLessonHomeBean implements Serializable {
	private String success;
	private List<LessonItemBean> courselist;


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


	public List<LessonItemBean> getCourselist() {
		return courselist;
	}

	public void setCourselist(List<LessonItemBean> courselist) {
		this.courselist = courselist;
	}


	@Override
	public String toString() {
		return "MyLessonHomeBean [success=" + success + ", courseList="
				+ courselist + "]";
	}


}
