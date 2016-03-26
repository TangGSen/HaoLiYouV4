package com.sen.haoliyou.exam;

import java.io.Serializable;
import java.util.List;

public class ExamTestHomeBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String paperid;
	private List<QuestionList> questionList;
	private String success;

	public String getPaperid() {
		return paperid;
	}

	public void setPaperid(String paperid) {
		this.paperid = paperid;
	}

	public List<QuestionList> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<QuestionList> questionList) {
		this.questionList = questionList;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
}
