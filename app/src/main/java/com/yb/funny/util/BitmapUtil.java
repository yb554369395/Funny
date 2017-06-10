package com.yb.funny.util;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.yb.funny.R;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * 图片加载工具类
 *
 * Created by Yangbin on 2017/1/16.
 */



public class BitmapUtil {


    /**
     * 加载icon图片(圆形)
     * @param url
     * @param view
     */
    public static void loadIcon(String url, ImageView view){
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setCircular(true)
                .setCrop(true)
                .setLoadingDrawableId(R.drawable.loading)
                .setFailureDrawableId(R.drawable.fail)
                .build();
        x.image().bind(view,url,imageOptions);
    }

    /**
     * 加载资源图片
     *
     * * @param simpleDraweeView view
     * * @param imagePath  Uri
     */
    public static void loadPicture(final SimpleDraweeView simpleDraweeView, String imagePath) {
        simpleDraweeView.setImageURI(imagePath);
    }


    /**
     * 使用SimpleDraweeView加载图片，图片自适应
     * 由于SimpleDraweeView出现问题，该方法暂时不可用
     */
    public static void loadPicture(final SimpleDraweeView simpleDraweeView, final String imagePath, final int screenWidth, final int screenHeight, final TextView hint) {
        final ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        final ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();


                if (((int) ((float) (screenWidth * height) / (float) width)) < (screenHeight * 1.5)) {
                    layoutParams.width = screenWidth;
                    layoutParams.height = (int) ((float) (screenWidth * height) / (float) width);
                    simpleDraweeView.setLayoutParams(layoutParams);
                }else {
                    hint.setVisibility(View.VISIBLE);
                    simpleDraweeView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                Log.d("TAG", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener(controllerListener).setUri(Uri.parse(imagePath)).setAutoPlayAnimations(true).build();
        simpleDraweeView.setController(controller);
    }


    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}
