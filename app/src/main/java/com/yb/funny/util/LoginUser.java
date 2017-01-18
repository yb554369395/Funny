package com.yb.funny.util;

import com.yb.funny.entity.User;

/**
 * Created by Administrator on 2017/1/18.
 */

public class LoginUser {
       private User user = null;
       private static LoginUser loginUser = null;

    public static LoginUser getInstance(){
        synchronized(LoginUser.class){
            if(loginUser==null){
                loginUser=new LoginUser();
            }
        }
        return loginUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
