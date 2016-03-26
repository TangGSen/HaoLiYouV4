package com.sen.haoliyou.mode;

import java.io.Serializable;
import java.util.List;

public class ResourceLessonHomeBean implements Serializable {
	private String success;
	private List<LessonItemBean> courseslist;
	private String type;

	public List<LessonItemBean> getCourseslist() {
		return courseslist;
	}

	public void setCourseslist(List<LessonItemBean> courseslist) {
		this.courseslist = courseslist;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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


	


}
