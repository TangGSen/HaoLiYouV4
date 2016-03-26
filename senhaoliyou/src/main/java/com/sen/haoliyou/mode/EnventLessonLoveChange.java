package com.sen.haoliyou.mode;

/**
 * Created by Administrator on 2016/3/20.
 */
public class EnventLessonLoveChange {
    private int position;
    private String love;
    public EnventLessonLoveChange(String love,int position){
        this.position = position;
        this.love = love;
    }
    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
