package com.yb.funny.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yb.funny.R;
import com.yb.funny.util.SharedpreferencesUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/1/8.
 */

//@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity{
    private static final String UESR_INFO = "";

//    @ViewInject(R.id.username)
    EditText username;
//    @ViewInject(R.id.password)
    EditText password;

//    @ViewInject(R.id.login)
    Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

//    @Event(R.id.login)
    private void login(View view){
        //将url直接添加到参数里面
        RequestParams params = new RequestParams("http://192.168.0.104:8080/funny/user");
        params.setMultipart(true);
        params.addBodyParameter("method","login");
        params.addBodyParameter("username",username.getText().toString() );
        params.addBodyParameter("password", password.getText().toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                SharedpreferencesUtil.setSharedPreference(getParent(),"userinfo",s);
                Toast.makeText(x.app(), SharedpreferencesUtil.getSahrePreference("userinfo"), Toast.LENGTH_SHORT).show();
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
