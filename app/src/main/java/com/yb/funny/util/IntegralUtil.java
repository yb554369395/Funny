package com.yb.funny.util;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/1/25.
 */

public class IntegralUtil {

    public static void lessintegral(int integral, int userid){
        RequestParams params = new RequestParams(Constant.URI + "user");
        params.setMultipart(true);
        params.addBodyParameter("method", "lessintegral");
        params.addBodyParameter("integral", integral + "");
        params.addBodyParameter("userid", userid + "");
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


    public static void addintegral(int integral, int userid){
        RequestParams params = new RequestParams(Constant.URI + "user");
        params.setMultipart(true);
        params.addBodyParameter("method", "addintegral");
        params.addBodyParameter("integral", integral + "");
        params.addBodyParameter("userid", userid + "");
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
