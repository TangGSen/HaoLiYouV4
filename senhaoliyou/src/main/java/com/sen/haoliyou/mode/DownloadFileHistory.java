package com.sen.haoliyou.mode;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/21.
 */
@Table(name = "downloadhistory")
public class DownloadFileHistory extends Model implements Serializable {
    @Column(name = "filename")
    public String filename;
    @Column(name = "downloadid")
    public String downloadid;
    @Column(name = "lessonid")
    public String lessonid;
    @Column(name = "lessonurl")
    public String lessonurl;

    public DownloadFileHistory() {
        super();
    }

    public DownloadFileHistory(String filename, String downloadid, String lessonid,String lessonurl) {
        this.filename = filename;
        this.downloadid =downloadid;
        this.lessonid = lessonid;
        this.lessonurl = lessonurl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDownloadid() {
        return downloadid;
    }

    public void setDownloadid(String downloadid) {
        this.downloadid = downloadid;
    }

    public String getLessonid() {
        return lessonid;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }
}
