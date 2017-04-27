package com.yb.funny.util;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * Created by Yangbin on 17-3-3.
 */
public class TimeUtil {

    /**
     * 获取当前时间,精确到秒
     * @return
     */
    public static String getTimeSeconds(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间,精确到分钟
     * @return
     */
    public static String getTimeMinute(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static Date getDate(String time){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDate(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }


    public static void setLastLogin(int userid,Date lastlogin){
        RequestParams params = new RequestParams(Constant.URI + "user");
        params.setMultipart(true);
        params.addBodyParameter("method", "setlastlogin");
        params.addBodyParameter("userid", userid + "");
        params.addBodyParameter("lastlogin", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(lastlogin) + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
