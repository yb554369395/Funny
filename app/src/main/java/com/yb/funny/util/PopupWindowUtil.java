package com.yb.funny.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.yb.funny.R;

import java.io.File;

/**
 * PopupWindow工具类
 * Created by Administrator on 17-4-13.
 */
public class PopupWindowUtil {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_IMAGE_CAPTURE= 2;
    private PopupWindow popupWindow;
    private Button select,camera,cancel;
    private Activity activity;
    private String picPath = null;


    public PopupWindowUtil(Activity activity) {
        this.activity = activity;
    }

    public void show(View view){
        View root = activity.getLayoutInflater().inflate(R.layout.popup_window,null);
        select = (Button) root.findViewById(R.id.button_select);
        camera = (Button) root.findViewById(R.id.button_camera);
        cancel = (Button) root.findViewById(R.id.button_cancel);
        select.setOnClickListener(listener);
        camera.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        popupWindow = new PopupWindow(root,350,500);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    public String getPicPath(){
        return picPath;
    }

    private void onSelect(){
        onCancel();
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void onCamera(){
        onCancel();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String sdPath = activity.getExternalCacheDir().getPath();
        picPath = sdPath + "/" + System.currentTimeMillis()+".jpg";
        Uri uri = Uri.fromFile(new File(picPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, RESULT_IMAGE_CAPTURE);
    }

    private void onCancel(){
        popupWindow.dismiss();
        setBackgroundAlpha(1.0f);
    }


    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button_select:
                    onSelect();
                    break;
                case R.id.button_camera:
                    onCamera();
                    break;
                case R.id.button_cancel:
                    onCancel();
                    break;
            }
        }
    };

    private void setBackgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }
}
