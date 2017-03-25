package com.yb.funny.util;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 用户发表内容数量工具类
 * 增加用户的发表内容的条数
 *
 * Created by Yangbin on 17-3-2.
 */
public class CountUtil {

    /**
     * 静态方法，增加传入的用户ID对应用户的发表内容条数
     * 一次只能增加1
     * @param userid
     */
    public static void addResourceCount(int userid){
        RequestParams params = new RequestParams(Constant.URI + "user");
        params.setMultipart(true);
        params.addBodyParameter("method", "addresourcecount");
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
