package com.yb.funny.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yb.funny.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2017/1/8.
 */

//@ContentView(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private String picturePath = null;
    private int sex = 0;
//    @ViewInject(R.id.register_username)
//    EditText username;
//    @ViewInject(R.id.register_password)
//    EditText password;
//    @ViewInject(R.id.register_name)
//    EditText name;
//    @ViewInject(R.id.radioGroup)
//    RadioGroup radioGroup;
//    @ViewInject(R.id.man)
//    RadioButton man;
//    @ViewInject(R.id.women)
//    RadioButton women;
//    @ViewInject(R.id.register_icon)
//    ImageView icon;
//    @ViewInject(R.id.register_choose)
//    Button choose;
//    @ViewInject(R.id.register_introduction)
//    EditText introduction;
//    @ViewInject(R.id.register)
//    Button register;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

//    @Event(R.id.register_choose)
    private void choose(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

//    @Event(value = R.id.radioGroup,type = RadioGroup.OnCheckedChangeListener.class)
//    private void radioGroup(RadioGroup group, int checkedId){
//        if (checkedId == man.getId()){
//            sex = 1;
//        }else if (checkedId == women.getId()){
//            sex = 2;
//        }
//    }

//    @Event(R.id.register)
//    private void register(View view){
//        RequestParams params = new RequestParams("http://192.168.0.104:8080/funny/user");
//        params.setMultipart(true);
//        params.addBodyParameter("method","register");
//        params.addBodyParameter("username",username.getText().toString() );
//        params.addBodyParameter("password", password.getText().toString());
//        params.addBodyParameter("name", name.getText().toString());
//        params.addBodyParameter("sex", sex+"");
//        if (picturePath == null) {
//            params.addBodyParameter("imgname", "http://localhost:8089/icon/default.png");
//        }else{
//            params.addBodyParameter("imgname","http://localhost:8089/icon/"+username.getText().toString()+".jpg");
//            params.addBodyParameter("type","icon");
//            params.addBodyParameter("file",new File(picturePath));
//        }
//        params.addBodyParameter("introduction",introduction.getText().toString());
//
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String s) {
//                Toast.makeText(x.app(), "注册成功！", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Throwable throwable, boolean b) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException e) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//
//    }
//
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            icon.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//        }
//    }
}
