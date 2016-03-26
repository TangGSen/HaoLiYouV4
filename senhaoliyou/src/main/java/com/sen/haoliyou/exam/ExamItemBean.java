package com.sen.haoliyou.exam;

import java.io.Serializable;

public class ExamItemBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String begindate;
	private String enddate;
	private String deptname;
	private String examid;
	private String examname;
	private String examtime;
	private String examtype;
	private String isenter;
	private String ispass;
	private String joincon;
	private String mark;
	private String remark;
	private String trainingname;
	private String type;
	private String yetjoincon;
	private String passmark;

	public String getPassmark() {
		return passmark;
	}

	public void setPassmark(String passmark) {
		this.passmark = passmark;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getExamid() {
		return examid;
	}

	public void setExamid(String examid) {
		this.examid = examid;
	}

	public String getExamname() {
		return examname;
	}

	public void setExamname(String examname) {
		this.examname = examname;
	}

	public String getExamtime() {
		return examtime;
	}

	public void setExamtime(String examtime) {
		this.examtime = examtime;
	}

	public String getExamtype() {
		return examtype;
	}

	public void setExamtype(String examtype) {
		this.examtype = examtype;
	}

	public String getIsenter() {
		return isenter;
	}

	public void setIsenter(String isenter) {
		this.isenter = isenter;
	}

	public String getIspass() {
		return ispass;
	}

	public void setIspass(String ispass) {
		this.ispass = ispass;
	}

	public String getJoincon() {
		return joincon;
	}

	public void setJoincon(String joincon) {
		this.joincon = joincon;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTrainingname() {
		return trainingname;
	}

	public void setTrainingname(String trainingname) {
		this.trainingname = trainingname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getYetjoincon() {
		return yetjoincon;
	}

	public void setYetjoincon(String yetjoincon) {
		this.yetjoincon = yetjoincon;
	}
}
