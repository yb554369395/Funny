package com.yb.funny.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import static android.R.id.text1;

/**
 * Sharedpreferences工具类，用于保存已登录用户的信息
 * Created by Marven on 2017/1/9.
 */


public class SharedpreferencesUtil {
    private static final String USER_INFO="userinfo";
    private static SharedPreferences sharedPreferences ;

    // 存储sharedpreferences
    public static void setSharedPreference(Context context,String name, String text) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, text);
        editor.commit();// 提交修改
    }

    // 清除sharedpreferences的数据
    public static void removeSharedPreference(Context context,String name) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(name);
        editor.commit();// 提交修改
    }

    // 获得sharedpreferences的数据
    public static String getSahrePreference(Context context,String name) {
        sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, "");
    }
}
