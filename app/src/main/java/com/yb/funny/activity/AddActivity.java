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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yb.funny.R;
import com.yb.funny.util.BitmapUtil;
import com.yb.funny.util.Constant;
import com.yb.funny.util.LoginUser;

import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 用户发布资源界面
 * Created by Marven on 2017/1/25.
 */

@ContentView(R.layout.activity_add)
public class AddActivity extends AppCompatActivity{

    private static final int RESULT_LOAD_IMAGE = 1;
    private String picturePath = null;
    @ViewInject(R.id.tbHeadBar)
    Toolbar mTbHeadBar;
    @ViewInject(R.id.toolbar_title)
    TextView toolbar_title;
    @ViewInject(R.id.add_pic)
    SimpleDraweeView image;
    @ViewInject(R.id.add_text)
    EditText text;
    @ViewInject(R.id.add_name)
    TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        toolbar_title.setText("发表");
        setSupportActionBar(mTbHeadBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        name.setText(LoginUser.getInstance().getUser().getName()+"，欢迎投稿！");
        image.setImageDrawable(getResources().getDrawable(R.drawable.add_big));
    }

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
        params.addBodyParameter("publishedtime", getTime());
        params.addBodyParameter("publisher", LoginUser.getInstance().getUser().getUserid()+"");
        params.addBodyParameter("publishername", LoginUser.getInstance().getUser().getName()+"");
        params.addBodyParameter("publishericon", LoginUser.getInstance().getUser().getIcon()+"");
        params.addBodyParameter("hashid", MD5.md5(LoginUser.getInstance().getUser().getUsername()+getTime()));
        if (picturePath != null) {
            params.addBodyParameter("imgname",Constant.DEFAULT_PIC_HEAD+MD5.md5(LoginUser.getInstance().getUser().getUsername()+getTime())+picturePath.substring(picturePath.length()-4));
            params.addBodyParameter("type","pic");
            params.addBodyParameter("file",new File(picturePath));
        }else {
            params.addBodyParameter("imgname","");
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(x.app(), "发表成功！", Toast.LENGTH_SHORT).show();
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


    @Event(value = R.id.add_pic,type = SimpleDraweeView.OnClickListener.class)
    private void uploadimage(View view){
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提醒")
                .setMessage("通过本地图库选择上传图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }

                }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create(); // 创建对话框
        alertDialog.show(); // 显示对话框
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
            image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }


    private String getTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
        return sdf.format(new Date());
    }

}
