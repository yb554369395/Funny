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
    
    private static SharedPreferences sharedPreferences;

    // 存储sharedpreferences
    public static void setSharedPreference(Activity activity,String name, String text) {
        sharedPreferences = activity.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, text);
        editor.commit();// 提交修改
    }

    // 清除sharedpreferences的数据
    public static void removeSharedPreference(Application application,String name) {
        sharedPreferences = application.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(name);
        editor.commit();// 提交修改
    }

    // 获得sharedpreferences的数据
    public static String getSahrePreference(String name) {
        return sharedPreferences.getString(name, "");
    }
}
