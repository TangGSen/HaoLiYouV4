package com.sen.haoliyou.mode;

import java.io.Serializable;
import java.util.List;

public class CommentHomeBean implements Serializable {
	private int maxPage ;
	private String success ;
	private List<CommentItemBean> commontsList ;
	public int getMaxPage() {
		if(maxPage==0){
			return 0 ;
		}else {
			return maxPage;
		}
	}
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
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
	public List<CommentItemBean> getCommontsList() {
		if(commontsList==null){
			return null ;
		}else {
			return commontsList;
		}
	}
	public void setCommontsList(List<CommentItemBean> commontsList) {
		this.commontsList = commontsList;
	}
	@Override
	public String toString() {
		return "CommentHomeBean [maxPage=" + maxPage + ", success="
				+ success + ", commontsList=" + commontsList + "]";
	}
}
