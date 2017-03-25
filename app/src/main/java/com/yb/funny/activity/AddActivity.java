package com.yb.funny.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.yb.funny.R;
import com.yb.funny.util.AppManager;
import com.yb.funny.util.Constant;
import com.yb.funny.util.IntegralUtil;
import com.yb.funny.util.LoginUser;
import com.yb.funny.util.TimeUtil;

import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;


/**
 * 用户发布资源界面
 * Created by Yangbin on 2017/1/25.
 */

@ContentView(R.layout.activity_add)
public class AddActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private String picturePath = null;
    @ViewInject(R.id.tbHeadBar)
    private Toolbar mTbHeadBar;
    @ViewInject(R.id.toolbar_title)
    private  TextView toolbar_title;
    @ViewInject(R.id.add_pic)
    private ImageView image;
    @ViewInject(R.id.add_text)
    private  EditText text;
    @ViewInject(R.id.add_name)
    private  TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);

        toolbar_title.setText(getString(R.string.publish));
        setSupportActionBar(mTbHeadBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        name.setText(LoginUser.getInstance().getUser().getName());
        image.setImageDrawable(getResources().getDrawable(R.drawable.add_big));
    }

    /**
     * Actionbar返回上个页面功能
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_ok) {
            uploadResource();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发表资源，hashid通过MD5将用户名+发表时间加密
     */
    private void uploadResource() {
        RequestParams params = new RequestParams(Constant.URI+"resource");
        params.setMultipart(true);
        params.addBodyParameter("method","upload");
        params.addBodyParameter("resourcetext",text.getText().toString() );
        params.addBodyParameter("publishedtime", TimeUtil.getTimeSeconds());
        params.addBodyParameter("publisher", LoginUser.getInstance().getUser().getUserid()+"");
        params.addBodyParameter("publishername", LoginUser.getInstance().getUser().getName());
        String iconname = LoginUser.getInstance().getUser().getIcon();
        params.addBodyParameter("publishericon",iconname.substring(18+Constant.IP.length()));
        params.addBodyParameter("hashid", MD5.md5(LoginUser.getInstance().getUser().getUsername() + TimeUtil.getTimeSeconds()));
        if (picturePath != null) {
            params.addBodyParameter("imgname", MD5.md5(LoginUser.getInstance().getUser().getUsername() + TimeUtil.getTimeSeconds())+picturePath.substring(picturePath.length()-4));
            params.addBodyParameter("type","pic");
            params.addBodyParameter("file",new File(picturePath));
        }else {
            params.addBodyParameter("imgname","");
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(x.app(), getString(R.string.pubSuccess), Toast.LENGTH_SHORT).show();
                IntegralUtil.addintegral(5, LoginUser.getInstance().getUser().getUserid());
                LoginUser.getInstance().getUser().addResourceCount();
                Intent intent = new Intent();
                intent.setAction("action.refreshUserInfo");
                sendBroadcast(intent);
                finish();
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
     * 通过调用系统图库，让用户选择需要上传的图片
     *
     * @param view
     */
    @Event(value = R.id.add_pic,type = ImageView.OnClickListener.class)
    private void uploadImage(View view){
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
     * 当用户选择好需要上传的图片后，系统会自动调用该方法，在该方法中获取图片的路径，并将需要上传的图片显示到指定的位置，
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
            image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
