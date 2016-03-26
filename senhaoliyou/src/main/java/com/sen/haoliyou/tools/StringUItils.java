package com.sen.haoliyou.tools;

/**
 * Created by Sen on 2016/3/25.
 */
public class StringUItils {
    public  static String delString(String string,int cont){
        if (string==null){
            return " ";
        }
        return string.substring(0,cont);
    }
}
