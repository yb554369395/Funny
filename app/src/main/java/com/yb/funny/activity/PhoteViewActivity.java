package com.yb.funny.activity;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.yb.funny.R;
import com.yb.funny.util.AppManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by Administrator on 2017/4/20.
 */
@ContentView(R.layout.photo_view)
public class PhoteViewActivity extends AppCompatActivity{

    @ViewInject(R.id.photo_view)
    PhotoDraweeView photoDraweeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        AppManager.getAppManager().addActivity(this);

        String path = getIntent().getStringExtra("path");
//        photoDraweeView.setPhotoUri(Uri.parse(path));

        // 需要使用 ControllerBuilder 方式请求图片
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(Uri.parse(path));
        controller.setOldController(photoDraweeView.getController());

        // 需要设置 ControllerListener，获取图片大小后，传递给 PhotoDraweeView 更新图片长宽
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || photoDraweeView == null) {
                    return;
                }
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        photoDraweeView.setController(controller.build());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
