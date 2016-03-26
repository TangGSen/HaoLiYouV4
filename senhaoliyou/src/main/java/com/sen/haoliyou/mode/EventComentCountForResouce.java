package com.sen.haoliyou.mode;

/**
 * Created by Sen on 2016/3/23.
 */
public class EventComentCountForResouce {
    private int sucessCount;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSucessCount() {
        return sucessCount;
    }

    public void setSucessCount(int sucessCount) {
        this.sucessCount = sucessCount;
    }

    public EventComentCountForResouce(int count){
        this.sucessCount = count;
    }
    public EventComentCountForResouce(){

    }
}
