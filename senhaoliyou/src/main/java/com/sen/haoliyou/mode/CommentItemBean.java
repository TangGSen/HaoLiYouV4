package com.sen.haoliyou.mode;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentItemBean implements Comparable<CommentItemBean>,Serializable {

	private String content;
	private String ctime;
	private String username;
	private String userphoto;

	public String getContent() {
		if (content == null) {
			return null;
		} else {
			return content;
		}
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCtime() {
		if (ctime == null) {
			return null;
		} else {
			return ctime;
		}
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getUsername() {
		if (username == null) {
			return null;
		} else {
			return username;
		}
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserphoto() {
		if (userphoto == null) {
			return null;
		} else {
			return userphoto;
		}
	}

	public void setUserphoto(String userphoto) {
		this.userphoto = userphoto;
	}

	@Override
	public String toString() {
		return "CommentItemBean [content=" + content + ", ctime=" + ctime
				+ ", username=" + username + ", userphoto=" + userphoto + "]";
	}

	// 将字符串的时间转为long类型的时间
	private long formatTime(String originalTime) {
		String time = originalTime.substring(0, 19);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt;
		try {
			dt = sdf.parse(time);
			// 继续转换得到秒数的long型
			long lTime = dt.getTime() / 1000;
			return lTime;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int compareTo(CommentItemBean other) {
		if (formatTime(this.ctime) == (formatTime(other.ctime))) {
			return 0;
		} else if (formatTime(this.ctime) < (formatTime(other.ctime))) {
			return 1;
		} else {
			return -1;
		}
	}
}
