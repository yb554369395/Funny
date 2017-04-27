package com.yb.funny.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.yb.funny.R;
import com.yb.funny.entity.User;
import com.yb.funny.util.AppManager;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import com.yb.funny.util.LoginUser;
import com.yb.funny.util.PopupWindowUtil;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.io.File;


/**
 * 用户个人信息修改界面
 * Created by Yangbin on 17-4-10.
 */

@ContentView(R.layout.activity_user_manager)
public class UserManagerActivity extends AppCompatActivity{

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_IMAGE_CAPTURE= 2;
    private String picturePath = null;

    private PopupWindowUtil pop;

    @ViewInject(R.id.user_manager_name)
    private TextView name;

    @ViewInject(R.id.user_manager_introduction)
    private TextView introduction;

    @ViewInject(R.id.user_manager_icon)
    private ImageView icon;

    @ViewInject(R.id.tbHeadBar)
    private Toolbar mTbHeadBar;

    @ViewInject(R.id.toolbar_title)
    private TextView toolbar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);

        toolbar_title.setText("账户信息管理");
        setSupportActionBar(mTbHeadBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        User user = LoginUser.getInstance().getUser();
        name.setText(user.getName());
        introduction.setText(user.getIntroduction());
        BitmapUtil.loadIcon(user.getIcon(), icon);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Event(value = R.id.user_manager_name,type = TextView.OnClickListener.class)
    private void name_choose(View view){
       showDialog("修改用户昵称", name);
    }

    @Event(value = R.id.user_manager_introduction,type = TextView.OnClickListener.class)
    private void introduction_choose(View view){
        showDialog("请输入个性签名", introduction);
    }

    @Event(value = R.id.user_manager_icon,type = ImageView.OnClickListener.class)
    private void icon_choose(View view){
        pop = new PopupWindowUtil(this);
        pop.show(view);
    }


    public void showDialog(String title, final TextView show) {
        final EditText text = new EditText(UserManagerActivity.this);
        text.setFocusable(true);
        text.setText(show.getText().toString());
        AlertDialog.Builder dialog = new AlertDialog.Builder(UserManagerActivity.this);
        dialog.setTitle(title);
        dialog.setView(text);
        dialog.setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                show.setText(text.getText().toString());
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }

    @Event(R.id.user_manager_submit)
    private void submit(View view){
        RequestParams params = new RequestParams(Constant.URI+"user");
        params.setMultipart(true);
        params.addBodyParameter("method", "upload");
        params.addBodyParameter("username", LoginUser.getInstance().getUser().getUsername());
        params.addBodyParameter("name", name.getText().toString());
        if (picturePath == null) {
            params.addBodyParameter("imgname", Constant.DEFAULT_ICON);
        }else{
            params.addBodyParameter("imgname", LoginUser.getInstance().getUser().getUsername() +".jpg");
            params.addBodyParameter("type","icon");
            params.addBodyParameter("file",new File(picturePath));
        }
        params.addBodyParameter("introduction", introduction.getText().toString() != null ? introduction.getText().toString() : getString(R.string.no_introduction));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if (Integer.parseInt(s) > 0) {
                    Toast.makeText(x.app(), "用户信息修改成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            BitmapUtil.loadIcon(picturePath,icon);
        }else if(requestCode == RESULT_IMAGE_CAPTURE && resultCode == RESULT_OK){
            picturePath =pop.getPicPath();
            BitmapUtil.loadIcon(picturePath,icon);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
