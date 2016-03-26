package com.sen.haoliyou.mode;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Sen on 2016/3/16.
 */
@Table(name = "userinfo")
public class UserInfoBean extends Model implements Serializable {
    @Column(name = "password")
    public String password;
    @Column(name = "userid")
    public String userid;
    @Column(name = "username")
    public String username;
    @Column(name = "success")
    public String success;

    public UserInfoBean(){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


}
