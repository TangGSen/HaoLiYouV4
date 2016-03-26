package com.sen.haoliyou.exam;

import java.util.List;

public class ExamHomeBean {
	private String success;
	private List<ExamItemBean> examList ;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public List<ExamItemBean> getExamList() {
		return examList;
	}

	public void setExamList(List<ExamItemBean> examList) {
		this.examList = examList;
	}

	@Override
	public String toString() {
		return "ExamHomeBean [success=" + success + ", ExamList=" + examList
				+ "]";
	}
}
