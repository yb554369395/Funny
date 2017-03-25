package com.yb.funny.util;

import android.widget.Toast;

import com.yb.funny.R;
import com.yb.funny.activity.LoginActivity;
import com.yb.funny.entity.User;

import org.xutils.x;

import java.util.Calendar;
import java.util.Date;


/**
 * 登陆用户工具类
 * 通过单例模型设置当前已经登陆的用户
 *
 *
 * Created by Yangbin on 2017/1/18.
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
        if (user != null){
                checkIsLogin();
                this.user.setLastlogin(TimeUtil.getDate(TimeUtil.getTimeMinute()));
                TimeUtil.setLastLogin(user.getUserid(), this.user.getLastlogin());
            }
        }



    /**
     * 检查今天是否已经登陆过
     */
    private void checkIsLogin(){
        Calendar lastlogin = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        now.setTime(TimeUtil.getDate(TimeUtil.getTimeMinute()));
        lastlogin.setTime(user.getLastlogin());
        if (lastlogin.get(Calendar.YEAR)<now.get(Calendar.YEAR)){
            show();
            return;
        }else if (lastlogin.get(Calendar.MONTH)<now.get(Calendar.MONTH)){
            show();
            return;
        }else if (lastlogin.get(Calendar.DAY_OF_MONTH)<now.get(Calendar.DAY_OF_MONTH)){
            show();
            return;
        }
    }



    private void show(){
        //每日登陆增加2积分
        IntegralUtil.addintegral(2, LoginUser.getInstance().getUser().getUserid());
        Toast.makeText(x.app(), "获得2点积分！", Toast.LENGTH_SHORT).show();
    }

}
