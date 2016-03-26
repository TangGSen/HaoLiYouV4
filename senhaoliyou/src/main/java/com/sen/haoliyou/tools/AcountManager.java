package com.sen.haoliyou.tools;

import android.text.TextUtils;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.sen.haoliyou.mode.UserInfoBean;

/**
 * Created by Administrator on 2016/3/18.
 * 用户管理
 */
public class AcountManager {
    //若果是userid 存在的话那么就不用再次登录的
    public static boolean isAcountExist(){
        UserInfoBean userInfo = new Select()
                .from(UserInfoBean.class)
                .executeSingle();
        if (userInfo==null){
            return false;
        }
        if (!TextUtils.isEmpty(userInfo.getUserid())){
            if (TextUtils.isEmpty(SenApplication.getInstance().getUserId())){
                SenApplication.getInstance().setUserId(userInfo.getUserid());
            }
            return true;
        }
        return false;
    }

    // 查询
    public static String getAcountId(){
        //先从application拿id
        String userId = SenApplication.getInstance().getUserId();
        if (!TextUtils.isEmpty(userId)){
            return userId;
        }
        //从数据库
        UserInfoBean userInfo = new Select()
                .from(UserInfoBean.class)
                .executeSingle();
        if (userInfo==null){
            return "";
        }
        String userid = userInfo.getUserid();
        if (!TextUtils.isEmpty(userid)){
            SenApplication.getInstance().setUserId(userid);
            return userid;
        }
        return "";
    }

//    删除
    public  static void deleteAcount(){
//        UserInfoBean.delete(UserInfoBean.class, 1);
        new Delete().from(UserInfoBean.class).where("userid = ?", getAcountId()).execute();
    }

}
