package com.yb.funny.util;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 用户积分工具类
 * 增加积分、减少积分
 * Created by Yangbin on 2017/1/25.
 */

public class IntegralUtil {


    /**
     * 静态方法，减少传入的用户ID对应用户的积分
     * @param integral
     * @param userid
     */
    public static void lessintegral(final int integral, int userid){
        RequestParams params = new RequestParams(Constant.URI + "user");
        params.setMultipart(true);
        params.addBodyParameter("method", "lessintegral");
        params.addBodyParameter("integral", integral + "");
        params.addBodyParameter("userid", userid + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                LoginUser.getInstance().getUser().lessIntegral(integral);
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


    /**
     * 静态方法，增加传入的用户ID对应用户的积分
     * @param integral
     * @param userid
     */
    public static void addintegral(final int integral, int userid){
        RequestParams params = new RequestParams(Constant.URI + "user");
        params.setMultipart(true);
        params.addBodyParameter("method", "addintegral");
        params.addBodyParameter("integral", integral + "");
        params.addBodyParameter("userid", userid + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                LoginUser.getInstance().getUser().addIntegral(integral);
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
