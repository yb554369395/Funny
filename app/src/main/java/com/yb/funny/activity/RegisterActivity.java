package com.yb.funny.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.yb.funny.R;
import com.yb.funny.util.AppManager;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * 用户注册界面
 *
 * Created by Yangbin on 2017/1/8.
 */

@ContentView(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private String picturePath = null;
    private int sex = 0;
    @ViewInject(R.id.tbHeadBar)
    private Toolbar mTbHeadBar;
    @ViewInject(R.id.register_username)
    private EditText username;
    @ViewInject(R.id.register_password)
    private EditText password;
    @ViewInject(R.id.register_name)
    private   EditText name;
//    @ViewInject(R.id.register_sex)
//    private  RadioGroup sexgroup;
    @ViewInject(R.id.register_man)
    private  RadioButton man;
    @ViewInject(R.id.register_women)
    private   RadioButton women;
    @ViewInject(R.id.register_icon)
    private ImageView icon;
    @ViewInject(R.id.register_introduction)
    private   EditText introduction;
//    @ViewInject(R.id.register)
//    private   Button register;
    @ViewInject(R.id.toolbar_title)
    private TextView toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);

        toolbar_title.setText(getString(R.string.register));
        setSupportActionBar(mTbHeadBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.default_icon));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Event(value = R.id.register_icon,type = ImageView.OnClickListener.class)
    private void choose(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.uploadImage))
                .setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }

                }).setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create(); // 创建对话框
        alertDialog.show(); // 显示对话框
    }

    /**
     * 监听事件，获取性别选项
     *
     * @param group
     * @param checkedId
     */
    @Event(value = R.id.register_sex,type = RadioGroup.OnCheckedChangeListener.class)
    private void chooseSex(RadioGroup group, int checkedId){
        if (checkedId == man.getId()){
            sex = 1;
        }else if (checkedId == women.getId()){
            sex = 2;
        }
    }

    /**
     * 用户注册功能
     *
     * @param view
     */
    @Event(R.id.register)
    private void register(View view){
        RequestParams params = new RequestParams(Constant.URI+"user");
        params.setMultipart(true);
        params.addBodyParameter("method","register");
        params.addBodyParameter("username",username.getText().toString() );
        params.addBodyParameter("password", password.getText().toString());
        params.addBodyParameter("name", name.getText().toString());
        params.addBodyParameter("sex", sex+"");
        if (picturePath == null) {
            params.addBodyParameter("imgname", Constant.DEFAULT_ICON);
        }else{
            params.addBodyParameter("imgname",username.getText().toString()+".jpg");
            params.addBodyParameter("type","icon");
            params.addBodyParameter("file",new File(picturePath));
        }
        params.addBodyParameter("introduction",introduction.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(Integer.parseInt(s)>0) {
                    Toast.makeText(x.app(), getString(R.string.registerSuccess), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, getString(R.string.repeatUsername), Toast.LENGTH_SHORT).show();
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
