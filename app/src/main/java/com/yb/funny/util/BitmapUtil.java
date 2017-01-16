package com.yb.funny.util;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2017/1/16.
 */



public class BitmapUtil {


    /**
     * 加载图片
     * @param url
     * @param view
     */
    public static void loadBitmap(String url, SimpleDraweeView view){
        Uri uri = Uri.parse(url);
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();
        view.setController(draweeController);
    }
}
