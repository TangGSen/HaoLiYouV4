package com.sen.haoliyou.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/10.
 */
public class ResouceType implements Serializable {
//    type 1 为ResourseBean
    private int type;

    private ResourseBean resourseBean;

    private ResourceLessonHomeBean lessonHomeBean;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ResourseBean getResourseBean() {
        return resourseBean;
    }

    public void setResourseBean(ResourseBean resourseBean) {
        this.resourseBean = resourseBean;
    }

    public ResourceLessonHomeBean getLessonHomeBean() {
        return lessonHomeBean;
    }

    public void setLessonHomeBean(ResourceLessonHomeBean lessonHomeBean) {
        this.lessonHomeBean = lessonHomeBean;
    }
}
